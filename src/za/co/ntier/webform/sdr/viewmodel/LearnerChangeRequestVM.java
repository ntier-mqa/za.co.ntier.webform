package za.co.ntier.webform.sdr.viewmodel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

import org.compiere.model.I_C_BPartner;
import org.compiere.model.MBPartner;
import org.compiere.model.MColumn;
import org.compiere.model.MRefList;
import org.compiere.model.MUser;
import org.compiere.model.Query;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Trx;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.api.model.I_ZZLearner;
import za.co.ntier.api.model.I_ZZLearnerDGProgramme;
import za.co.ntier.api.model.I_ZZLearnerTermination;
import za.co.ntier.api.model.I_ZZLearner_v;
import za.co.ntier.api.model.I_ZZSdfOrganisation_v;
import za.co.ntier.api.model.I_ZZ_Program_Master_Data;
import za.co.ntier.api.model.X_ZZLearnerDGProgramme;
import za.co.ntier.api.model.X_ZZLearnerTermination;
import za.co.ntier.api.model.X_ZZLearner_v;
import za.co.ntier.api.model.X_ZZSdfOrganisation_v;
import za.co.ntier.api.model.X_ZZ_Program_Master_Data;
import za.co.ntier.report.LearnerTerminationReport;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;

/** Electronic learner change requests. Termination is the first supported request type. */
public class LearnerChangeRequestVM implements Serializable
{
	private static final long						serialVersionUID	= 1L;
	private static final CLogger					log					= CLogger.getCLogger(LearnerChangeRequestVM.class);

	private final List<MBPartner>					organisations		= new ArrayList<>();
	private final List<X_ZZ_Program_Master_Data>	programmes			= new ArrayList<>();
	private final Map<String, String>				fieldErrors			= new LinkedHashMap<>();
	private MBPartner								organisation;
	private X_ZZ_Program_Master_Data				programme;
	private X_ZZLearner_v							learner;
	private X_ZZLearnerTermination					request;
	private String									identityNumber		= "";
	private String									reason				= "";
	private Date									terminationDate;
	private boolean									permanentEmployment;
	private String									validationMessage	= "";
	private String									feedbackMessage		= "";
	private boolean									learnerConfirmed;
	private boolean									confirmationPending;
	private boolean									viewingRequest;
	private boolean									unavailable;
	private boolean									terminationRequest	= true;
	private int										learnerCount;
	private int										reasonMaxLength		= 4000;

	@Init
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey)
	MenuContextInfo context)
	{
		MColumn reasonColumn = MColumn.get(	Env.getCtx(), I_ZZLearnerTermination.Table_Name,
											I_ZZLearnerTermination.COLUMNNAME_ZZ_Reason);
		if (reasonColumn != null && reasonColumn.getFieldLength() > 0)
			reasonMaxLength = reasonColumn.getFieldLength();
		organisations.addAll(organisationQuery(null).setOrderBy(I_C_BPartner.COLUMNNAME_Name).list());
		if (context != null && (context.getRecordID() > 0 || hasText(context.getRecordUU())))
		{
			loadRequest(context);
			return;
		}
		if (organisations.size() == 1)
			selectOrganisation(organisations.get(0));
	}

	/** Only the user's organisation and approved organisation links are available. */
	private Query organisationQuery(String trxName)
	{
		int userId = Env.getAD_User_ID(Env.getCtx());
		int ownOrganisation = MUser.get(Env.getCtx(), userId).getC_BPartner_ID();
		String where = I_C_BPartner.COLUMNNAME_C_BPartner_ID	+ " = ? OR "
						+ I_C_BPartner.COLUMNNAME_C_BPartner_ID + " IN (SELECT "
						+ I_ZZSdfOrganisation_v.COLUMNNAME_C_BPartner_ID + " FROM " + I_ZZSdfOrganisation_v.Table_Name
						+ " WHERE " + I_ZZSdfOrganisation_v.COLUMNNAME_AD_User_ID + " = ? AND "
						+ I_ZZSdfOrganisation_v.COLUMNNAME_ZZ_DocStatus + " = ? AND "
						+ I_ZZSdfOrganisation_v.COLUMNNAME_IsActive + " = 'Y' AND "
						+ I_ZZSdfOrganisation_v.COLUMNNAME_AD_Client_ID + " = ?)";
		return new Query(Env.getCtx(), I_C_BPartner.Table_Name, "(" + where + ")", trxName)
																							.setParameters(	ownOrganisation, userId,
																											X_ZZSdfOrganisation_v.ZZ_DOCSTATUS_Approved,
																											Env.getAD_Client_ID(Env.getCtx()))
																							.setClient_ID().setOnlyActiveRecords(true);
	}

	@Command
	public void selectRequestType(@BindingParam("type")
	String type)
	{
		if (confirmationPending)
			return;
		terminationRequest = "TE".equals(type);
		notifyForm();
	}

	@Command
	public void selectOrganisation(@BindingParam("organisation")
	MBPartner selected)
	{
		if (isSelectionLocked() || selected == null)
			return;
		organisation = organisations.stream().filter(item -> item.get_ID() == selected.get_ID()).findFirst().orElse(null);
		programme = null;
		programmes.clear();
		clearLearner();
		learnerCount = 0;
		if (organisation != null)
		{
			String where = I_ZZ_Program_Master_Data.COLUMNNAME_ZZ_Program_Master_Data_ID	+ " IN (SELECT "
							+ programmeLink() + " FROM "
							+ I_ZZLearnerDGProgramme.Table_Name + " WHERE " + registrationWhere(true) + ")";
			programmes.addAll(new Query(Env.getCtx(), I_ZZ_Program_Master_Data.Table_Name, where, null)
																										.setParameters(registrationParameters(true))
																										.setClient_ID().setOnlyActiveRecords(true)
																										.setOrderBy(I_ZZ_Program_Master_Data.COLUMNNAME_Title)
																										.list());
			if (programmes.size() == 1)
				selectProgramme(programmes.get(0));
		}
		notifyForm();
	}

	@Command
	public void selectProgramme(@BindingParam("programme")
	X_ZZ_Program_Master_Data selected)
	{
		if (isSelectionLocked() || selected == null)
			return;
		programme = programmes.stream().filter(item -> item.get_ID() == selected.get_ID()).findFirst().orElse(null);
		clearLearner();
		learnerCount = 0;
		if (programme != null)
		{
			// Count learner records, so duplicate registrations do not inflate the displayed total.
			String where = I_ZZLearner.COLUMNNAME_ZZLearner_ID	+ " IN (SELECT "
							+ I_ZZLearnerDGProgramme.COLUMNNAME_ZZLearner_ID + " FROM " + I_ZZLearnerDGProgramme.Table_Name
							+ " WHERE " + registrationWhere(true) + " AND "
							+ programmeLink() + " = ?)";
			List<Object> parameters = registrationParameters(true);
			parameters.add(programme.get_ID());
			learnerCount = new Query(Env.getCtx(), I_ZZLearner.Table_Name, where, null)
																						.setParameters(parameters).setClient_ID().setOnlyActiveRecords(true)
																						.count();
		}
		notifyForm();
	}

	private String registrationWhere(boolean registeredOnly)
	{
		String where = "("	+ I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_Employer_ID + " = ? OR "
						+ I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_SDP_ID + " = ?) AND "
						+ I_ZZLearnerDGProgramme.COLUMNNAME_AD_Client_ID + " = ? AND "
						+ I_ZZLearnerDGProgramme.COLUMNNAME_IsActive + " = 'Y'";
		if (registeredOnly)
			where += " AND " + I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_DocStatus + " = ?";
		return where;
	}

	private List<Object> registrationParameters(boolean registeredOnly)
	{
		List<Object> parameters = new ArrayList<>();
		parameters.add(organisation.get_ID());
		parameters.add(organisation.get_ID());
		parameters.add(Env.getAD_Client_ID(Env.getCtx()));
		if (registeredOnly)
			parameters.add(X_ZZLearnerDGProgramme.ZZ_DOCSTATUS_Registered);
		return parameters;
	}

	private Query registrationQuery(int learnerId, int programmeId, String trxName)
	{
		return new Query(	Env.getCtx(), I_ZZLearnerDGProgramme.Table_Name,
							I_ZZLearnerDGProgramme.COLUMNNAME_ZZLearner_ID	+ " = ? AND "
																				+ programmeLink() + " = ?", trxName)
																													.setParameters(learnerId, programmeId)
																													.setClient_ID().setOnlyActiveRecords(true);
	}

	/**
	 * The combined form prefers the DG link; older registrations only have the original programme
	 * column.
	 */
	private static String programmeLink()
	{
		return "COALESCE(NULLIF("	+ I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_DGProgram_ID + ", 0), "
				+ I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_Program_Master_Data_ID + ")";
	}

	private boolean belongsToOrganisation(X_ZZLearnerDGProgramme registration)
	{
		return registration.getZZ_Employer_ID() == organisation.get_ID()
				|| registration.getZZ_SDP_ID() == organisation.get_ID();
	}

	@Command
	public void searchLearner()
	{
		if (isSelectionLocked())
			return;
		clearValidation();
		feedbackMessage = "";
		if (organisation == null || programme == null || !hasText(identityNumber))
		{
			if (organisation == null)
				fieldErrors.put("organisation", "Select an organisation.");
			if (programme == null)
				fieldErrors.put("programme", "Select a programme.");
			if (!hasText(identityNumber))
				fieldErrors.put("identity", "Enter the learner's ID or passport number.");
			notifyForm();
			return;
		}
		String where = "("	+ I_ZZLearner_v.COLUMNNAME_ZZ_ID_Passport_No + " = ? OR "
						+ I_ZZLearner_v.COLUMNNAME_ZZOtherIDNo + " = ?) AND " + I_ZZLearner_v.COLUMNNAME_ZZLearner_ID
						+ " IN (SELECT " + I_ZZLearnerDGProgramme.COLUMNNAME_ZZLearner_ID + " FROM "
						+ I_ZZLearnerDGProgramme.Table_Name + " WHERE " + registrationWhere(true) + " AND "
						+ programmeLink() + " = ?)";
		List<Object> parameters = new ArrayList<>();
		parameters.add(identityNumber.trim());
		parameters.add(identityNumber.trim());
		parameters.addAll(registrationParameters(true));
		parameters.add(programme.get_ID());
		List<X_ZZLearner_v> matches = new Query(Env.getCtx(), I_ZZLearner_v.Table_Name, where, null)
																									.setParameters(parameters).setClient_ID()
																									.setOnlyActiveRecords(true).list();
		if (matches.size() != 1)
		{
			fieldErrors.put("identity", matches.isEmpty()	? "No registered learner was found for this organisation, programme and ID number."
															: "More than one learner matches this ID number. Ask MQA to check the learner records.");
			notifyForm();
			return;
		}
		X_ZZLearner_v selected = matches.get(0);
		List<X_ZZLearnerDGProgramme> registrations = registrationQuery(selected.getZZLearner_ID(), programme.get_ID(), null).list();
		if (registrations.size() != 1 || !belongsToOrganisation(registrations.get(0)))
		{
			fieldErrors.put("identity",
							"More than one registration exists for this learner and programme. Ask MQA to check the registration before continuing.");
			notifyForm();
			return;
		}
		List<X_ZZLearnerTermination> existing = requestQuery(selected.getZZLearner_ID(), programme.get_ID(), null).list();
		if (existing.size() > 1 || (!existing.isEmpty() && !ownsRequest(existing.get(0))))
		{
			fieldErrors.put("identity", "A termination request already exists for this learner and programme. Contact MQA for assistance.");
			notifyForm();
			return;
		}
		learner = selected;
		request = existing.isEmpty() ? null : existing.get(0);
		viewingRequest = request != null;
		loadRequestDetails(request);
		learnerConfirmed = false;
		notifyForm();
	}

	private Query requestQuery(int learnerId, int programmeId, String trxName)
	{
		return new Query(	Env.getCtx(), I_ZZLearnerTermination.Table_Name,
							I_ZZLearnerTermination.COLUMNNAME_ZZLearner_ID	+ " = ? AND "
																				+ I_ZZLearnerTermination.COLUMNNAME_ZZ_Program_Master_Data_ID + " = ?", trxName)
																																								.setParameters(	learnerId,
																																												programmeId)
																																								.setClient_ID()
																																								.setOnlyActiveRecords(true);
	}

	private boolean ownsRequest(X_ZZLearnerTermination saved)
	{
		int userId = Env.getAD_User_ID(Env.getCtx());
		return saved.getCreatedBy() == userId || saved.getZZ_SubmittedBy_ID() == userId;
	}

	@Command
	public void changeSelection()
	{
		if (confirmationPending || unavailable)
			return;
		if (!isEditable())
		{
			X_ZZ_Program_Master_Data previousProgramme = programme;
			clearLearner();
			selectOrganisation(organisation);
			if (previousProgramme != null)
				programmes	.stream().filter(item -> item.get_ID() == previousProgramme.get_ID())
							.findFirst().ifPresent(this::selectProgramme);
			return;
		}
		MasterUtil.showConfirmDialog(	"dialog", "Change learner or programme",
										List.of("Unsaved changes will be cleared. Any saved request will remain available."), ignored -> {
											clearLearner();
											selectOrganisation(organisation);
										});
	}

	private void clearLearner()
	{
		learner = null;
		request = null;
		viewingRequest = false;
		identityNumber = "";
		reason = "";
		terminationDate = null;
		permanentEmployment = false;
		learnerConfirmed = false;
		clearValidation();
		feedbackMessage = "";
	}

	@Command
	public void saveDraft()
	{
		if (validate(false) && persist(false))
			feedbackMessage = "Draft saved. Reference: " + getReferenceNumber();
		notifyForm();
	}

	@Command
	public void submitRequest()
	{
		if (!validate(true) || !persist(false))
		{
			notifyForm();
			return;
		}
		int requestId = request.get_ID();
		String savedReason = reason.trim();
		Timestamp savedTerminationDate = terminationTimestamp();
		boolean savedPermanentEmployment = permanentEmployment;
		confirmationPending = true;
		feedbackMessage = "Draft saved. Reference: " + getReferenceNumber();
		notifyForm();
		MasterUtil.showConfirmDialog(	"dialog", "Confirm termination request",
										List.of("Submit a termination request for " + getLearnerName() + " (" + getLearnerIdentity() + ")?",
												"Programme: " + getProgrammeName(programme),
												"MQA will review the request before the learner's registration is terminated."), ignored -> {
													confirmationPending = false;
													if (request == null || request.get_ID() != requestId || !Objects.equals(reason.trim(), savedReason)
														|| !Objects.equals(terminationTimestamp(), savedTerminationDate)
														|| permanentEmployment != savedPermanentEmployment)
														validationMessage = "The request changed. Review the details and submit again.";
													else if (validate(true) && persist(true))
														feedbackMessage = "Termination request submitted to MQA for approval. Reference: "
																			+ getReferenceNumber();
													notifyForm();
												}, ignored -> {
													confirmationPending = false;
													feedbackMessage = "Submission cancelled. Your request remains saved. Reference: " + getReferenceNumber();
													notifyForm();
												});
	}

	private boolean validate(boolean submitting)
	{
		clearValidation();
		feedbackMessage = "";
		if (!isEditable() || confirmationPending)
		{
			validationMessage = "This request is not available for editing.";
			return false;
		}
		if (organisation == null)
			fieldErrors.put("organisation", "Select an organisation.");
		if (programme == null)
			fieldErrors.put("programme", "Select a programme.");
		if (learner == null)
			fieldErrors.put("identity", "Find and select the learner first.");
		else
		{
			if (!learnerConfirmed)
				fieldErrors.put("confirmation", "Confirm that the learner details are correct.");
			if (submitting && terminationDate == null)
				fieldErrors.put("terminationDate", "Enter the termination effective date.");
			if (submitting && !hasText(reason))
				fieldErrors.put("reason", "Enter the reason for termination.");
			else if (reason.length() > reasonMaxLength)
				fieldErrors.put("reason", "The reason must be no longer than " + reasonMaxLength + " characters.");
		}
		return fieldErrors.isEmpty();
	}

	private void clearValidation()
	{
		validationMessage = "";
		fieldErrors.clear();
	}

	/**
	 * Lock the source registration before checking requests, so concurrent sessions cannot create
	 * duplicates.
	 */
	private boolean persist(boolean submitting)
	{
		Trx trx = Trx.get(Trx.createTrxName("LearnerTermination"), true);
		X_ZZLearnerTermination saved;
		try
		{
			boolean authorised = organisationQuery(trx.getTrxName()).<MBPartner> list().stream()
																	.anyMatch(item -> item.get_ID() == organisation.get_ID());
			if (!authorised)
				throw new IllegalStateException("Your organisation link is no longer available.");
			List<X_ZZLearnerDGProgramme> registrations = registrationQuery(	learner.getZZLearner_ID(), programme.get_ID(),
																			trx.getTrxName()).setForUpdate(true).list();
			if (registrations.size() != 1
				|| !belongsToOrganisation(registrations.get(0))
				|| !X_ZZLearnerDGProgramme.ZZ_DOCSTATUS_Registered.equals(registrations.get(0).getZZ_DocStatus()))
				throw new IllegalStateException("The learner's registration changed. Select the learner again.");
			X_ZZ_Program_Master_Data currentProgramme = new Query(
																	Env.getCtx(), I_ZZ_Program_Master_Data.Table_Name,
																	I_ZZ_Program_Master_Data.COLUMNNAME_ZZ_Program_Master_Data_ID + " = ?", trx.getTrxName())
																																								.setParameters(programme.get_ID())
																																								.setClient_ID()
																																								.setOnlyActiveRecords(true)
																																								.first();
			if (currentProgramme == null)
				throw new IllegalStateException("The programme is no longer available. Select the learner and programme again.");
			List<X_ZZLearnerTermination> requests = requestQuery(	learner.getZZLearner_ID(), programme.get_ID(),
																	trx.getTrxName()).setForUpdate(true).list();
			if (requests.size() > 1)
				throw new IllegalStateException("Multiple termination requests exist. Contact MQA for assistance.");
			saved = requests.isEmpty() ? null : requests.get(0);
			if (request == null && saved != null)
				throw new IllegalStateException("A termination request was created in another session. Select the learner again.");
			if (request != null && (saved == null	|| saved.get_ID() != request.get_ID() || !ownsRequest(saved)
									|| !isEditableStatus(saved.getZZ_DocStatus()) || !Objects.equals(saved.getUpdated(), request.getUpdated())))
				throw new IllegalStateException("The request changed since it was opened. Select the learner again.");
			if (saved == null)
			{
				saved = new X_ZZLearnerTermination(Env.getCtx(), 0, trx.getTrxName());
				saved.setDateDoc(new Timestamp(System.currentTimeMillis()));
				saved.setZZLearner_ID(learner.getZZLearner_ID());
				saved.setZZ_Program_Master_Data_ID(programme.get_ID());
				saved.setZZ_DocStatus(X_ZZLearnerTermination.ZZ_DOCSTATUS_Draft);
				saved.setZZ_DocAction(X_ZZLearnerTermination.ZZ_DOCACTION_Submit);
			}
			saved.setAD_Org_ID(currentProgramme.getAD_Org_ID());
			saved.setZZ_Reason(reason.trim());
			saved.setZZTerminationDate(terminationTimestamp());
			saved.setZZIsPermanentEmployment(permanentEmployment);
			if (submitting)
			{
				saved.setZZ_SubmittedBy_ID(Env.getAD_User_ID(Env.getCtx()));
				saved.setZZ_DocStatus(X_ZZLearnerTermination.ZZ_DOCSTATUS_Submitted);
				saved.setZZ_DocAction(X_ZZLearnerTermination.ZZ_DOCACTION_ApproveDoNotApprove);
			}
			saved.saveEx(trx.getTrxName());
			if (submitting)
				LearnerTerminationReport.attach(saved, registrations.get(0), currentProgramme, trx.getTrxName());
			trx.commit(true);
		}
		catch (Exception ex)
		{
			trx.rollback();
			log.log(Level.WARNING, "Could not save learner termination request", ex);
			validationMessage = ex instanceof IllegalStateException ? ex.getMessage()
																	: "The termination request could not be saved. Please try again or contact MQA.";
			return false;
		}
		finally
		{
			trx.close();
		}
		saved.set_TrxName(null);
		request = saved;
		loadRequestDetails(saved);
		return true;
	}

	private void loadRequest(MenuContextInfo context)
	{
		String key = context.getRecordID() > 0	? I_ZZLearnerTermination.COLUMNNAME_ZZLearnerTermination_ID
												: I_ZZLearnerTermination.COLUMNNAME_ZZLearnerTermination_UU;
		Object value = context.getRecordID() > 0 ? context.getRecordID() : context.getRecordUU();
		X_ZZLearnerTermination saved = new Query(Env.getCtx(), I_ZZLearnerTermination.Table_Name, key + " = ?", null)
																														.setParameters(value).setClient_ID()
																														.setOnlyActiveRecords(true).first();
		if (saved == null || !ownsRequest(saved))
		{
			unavailable = true;
			validationMessage = "This termination request is not available to your user account.";
			return;
		}
		for (MBPartner candidate : organisations)
		{
			organisation = candidate;
			List<X_ZZLearnerDGProgramme> registrations = registrationQuery(	saved.getZZLearner_ID(),
																			saved.getZZ_Program_Master_Data_ID(), null).list();
			if (registrations.size() == 1 && belongsToOrganisation(registrations.get(0)))
			{
				X_ZZ_Program_Master_Data selected = new Query(	Env.getCtx(), I_ZZ_Program_Master_Data.Table_Name,
																I_ZZ_Program_Master_Data.COLUMNNAME_ZZ_Program_Master_Data_ID + " = ?", null)
																																				.setParameters(saved.getZZ_Program_Master_Data_ID())
																																				.setClient_ID()
																																				.first();
				X_ZZLearner_v selectedLearner = new Query(	Env.getCtx(), I_ZZLearner_v.Table_Name,
															I_ZZLearner_v.COLUMNNAME_ZZLearner_ID + " = ?", null)
																													.setParameters(saved.getZZLearner_ID())
																													.setClient_ID().first();
				if (selected == null || selectedLearner == null)
					break;
				programme = selected;
				programmes.add(selected);
				learner = selectedLearner;
				identityNumber = getLearnerIdentity();
				request = saved;
				viewingRequest = true;
				loadRequestDetails(saved);
				return;
			}
		}
		organisation = null;
		unavailable = true;
		validationMessage = "The registration linked to this request is no longer available. Contact MQA for assistance.";
	}

	private void loadRequestDetails(X_ZZLearnerTermination saved)
	{
		reason = saved == null ? "" : text(saved.getZZ_Reason());
		terminationDate = saved == null ? null : saved.getZZTerminationDate();
		permanentEmployment = saved != null && saved.isZZIsPermanentEmployment();
	}

	private Timestamp terminationTimestamp()
	{
		return terminationDate == null ? null : new Timestamp(terminationDate.getTime());
	}

	private void notifyForm()
	{
		BindUtils.postNotifyChange(null, null, this, "*");
	}

	private static boolean hasText(String value)
	{
		return value != null && !value.isBlank();
	}

	private static String text(String value)
	{
		return value == null ? "" : value;
	}

	static boolean isEditableStatus(String status)
	{
		return X_ZZLearnerTermination.ZZ_DOCSTATUS_Draft.equals(status)
				|| X_ZZLearnerTermination.ZZ_DOCSTATUS_Query.equals(status)
				|| X_ZZLearnerTermination.ZZ_DOCSTATUS_NotApproved.equals(status);
	}

	public boolean isTerminationRequest()
	{
		return terminationRequest;
	}

	public boolean isViewingRequest()
	{
		return viewingRequest;
	}

	public boolean isEditable()
	{
		return terminationRequest && !unavailable && (request == null || isEditableStatus(request.getZZ_DocStatus()));
	}

	public boolean isSelectionLocked()
	{
		return unavailable || learner != null || confirmationPending;
	}

	public boolean isUnavailable()
	{
		return unavailable;
	}

	public boolean isConfirmationPending()
	{
		return confirmationPending;
	}

	public boolean isLearnerConfirmed()
	{
		return learnerConfirmed;
	}

	public void setLearnerConfirmed(boolean value)
	{
		learnerConfirmed = value;
		if (value && fieldErrors.remove("confirmation") != null)
			BindUtils.postNotifyChange(null, null, this, "fieldErrors");
	}

	public List<MBPartner> getOrganisations()
	{
		return organisations;
	}

	public MBPartner getOrganisation()
	{
		return organisation;
	}

	public List<X_ZZ_Program_Master_Data> getProgrammes()
	{
		return programmes;
	}

	public X_ZZ_Program_Master_Data getProgramme()
	{
		return programme;
	}

	public X_ZZLearner_v getLearner()
	{
		return learner;
	}

	public String getIdentityNumber()
	{
		return identityNumber;
	}

	public void setIdentityNumber(String value)
	{
		identityNumber = text(value);
		if (fieldErrors.remove("identity") != null)
			BindUtils.postNotifyChange(null, null, this, "fieldErrors");
	}

	public String getReason()
	{
		return reason;
	}

	public void setReason(String value)
	{
		reason = text(value);
		if (hasText(reason) && reason.length() <= reasonMaxLength && fieldErrors.remove("reason") != null)
			BindUtils.postNotifyChange(null, null, this, "fieldErrors");
	}

	public int getReasonMaxLength()
	{
		return reasonMaxLength;
	}

	public Date getTerminationDate()
	{
		return terminationDate;
	}

	public void setTerminationDate(Date value)
	{
		terminationDate = value;
		if (value != null && fieldErrors.remove("terminationDate") != null)
			BindUtils.postNotifyChange(null, null, this, "fieldErrors");
	}

	public boolean isPermanentEmployment()
	{
		return permanentEmployment;
	}

	public void setPermanentEmployment(boolean value)
	{
		permanentEmployment = value;
	}

	public int getLearnerCount()
	{
		return learnerCount;
	}

	public String getValidationMessage()
	{
		return validationMessage;
	}

	public Map<String, String> getFieldErrors()
	{
		return fieldErrors;
	}

	public String getFeedbackMessage()
	{
		return feedbackMessage;
	}

	public String getReferenceNumber()
	{
		return request == null ? "Assigned when saved" : request.getDocumentNo();
	}

	public String getLearnerIdentity()
	{
		return learner == null ? ""
						: hasText(learner.getZZ_ID_Passport_No())
									? learner.getZZ_ID_Passport_No()
							: text(learner.getZZOtherIDNo());
	}

	public String getLearnerName()
	{
		return learner == null	? ""
								: (text(learner.getZZFirstName())	+ " "
									+ text(learner.getSurname())).trim();
	}

	public String getProgrammeName(X_ZZ_Program_Master_Data item)
	{
		return item == null ? ""
						: hasText(item.getTitle())
									? item.getTitle()
							: text(item.getName());
	}

	public String getStatusLabel()
	{
		MColumn column = MColumn.get(	Env.getCtx(), I_ZZLearnerTermination.Table_Name,
										I_ZZLearnerTermination.COLUMNNAME_ZZ_DocStatus);
		String status = request == null ? X_ZZLearnerTermination.ZZ_DOCSTATUS_Draft : request.getZZ_DocStatus();
		return MRefList.getListName(Env.getCtx(), column.getAD_Reference_Value_ID(), status);
	}

	public Map<String, String> getLabels()
	{
		Map<String, String> labels = new LinkedHashMap<>();
		labels.put("organisation", MasterUtil.getNameOfColTranslated(I_C_BPartner.Table_Name, I_C_BPartner.COLUMNNAME_C_BPartner_ID));
		labels.put("programme", MasterUtil.getNameOfColTranslated(	I_ZZLearnerTermination.Table_Name,
																	I_ZZLearnerTermination.COLUMNNAME_ZZ_Program_Master_Data_ID));
		labels.put("learner", MasterUtil.getNameOfColTranslated(I_ZZLearnerTermination.Table_Name,
																I_ZZLearnerTermination.COLUMNNAME_ZZLearner_ID));
		labels.put("reason", MasterUtil.getNameOfColTranslated(	I_ZZLearnerTermination.Table_Name,
																I_ZZLearnerTermination.COLUMNNAME_ZZ_Reason));
		labels.put("terminationDate", MasterUtil.getNameOfColTranslated(I_ZZLearnerTermination.Table_Name,
																		I_ZZLearnerTermination.COLUMNNAME_ZZTerminationDate));
		labels.put("identity", MasterUtil.getNameOfColTranslated(	I_ZZLearner_v.Table_Name,
																	I_ZZLearner_v.COLUMNNAME_ZZ_ID_Passport_No));
		labels.put("reference", MasterUtil.getNameOfColTranslated(	I_ZZLearnerTermination.Table_Name,
																	I_ZZLearnerTermination.COLUMNNAME_DocumentNo));
		labels.put("status", MasterUtil.getNameOfColTranslated(	I_ZZLearnerTermination.Table_Name,
																I_ZZLearnerTermination.COLUMNNAME_ZZ_DocStatus));
		return labels;
	}
}
