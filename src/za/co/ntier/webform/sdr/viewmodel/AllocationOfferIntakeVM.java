package za.co.ntier.webform.sdr.viewmodel;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.logging.Level;

import org.adempiere.webui.AdempiereWebUI;
import org.compiere.model.I_AD_User;
import org.compiere.model.I_C_Period;
import org.compiere.model.MAttachment;
import org.compiere.model.MAttachmentEntry;
import org.compiere.model.MBPartner;
import org.compiere.model.MClient;
import org.compiere.model.MColumn;
import org.compiere.model.MPeriod;
import org.compiere.model.MRefList;
import org.compiere.model.MSysConfig;
import org.compiere.model.MUser;
import org.compiere.model.MYear;
import org.compiere.model.Query;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Trx;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.DependsOn;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.Media;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModelList;

import za.co.ntier.api.model.I_ZZQualification;
import za.co.ntier.api.model.I_ZZ_Allocation_Intake_Plan;
import za.co.ntier.api.model.I_ZZ_Allocation_Offer;
import za.co.ntier.api.model.I_ZZ_Application_Form;
import za.co.ntier.api.model.I_ZZ_Trade;
import za.co.ntier.api.model.X_ZZQualification;
import za.co.ntier.api.model.X_ZZ_Allocation_Intake_Plan;
import za.co.ntier.api.model.X_ZZ_Allocation_Offer;
import za.co.ntier.api.model.X_ZZ_Application_Form;
import za.co.ntier.api.model.X_ZZ_Program_Master_Data;
import za.co.ntier.api.model.X_ZZ_Trade;
import za.co.ntier.webform.form.AttachmentUtil;
import za.co.ntier.webform.form.MasterUtil;

/** Organisation response to an allocation offer and its intake schedule (Ticket #421). */
public class AllocationOfferIntakeVM implements Serializable
{
	private static final long					serialVersionUID		= 1L;
	private static final CLogger				log						= CLogger.getCLogger(AllocationOfferIntakeVM.class);
	private static final String					SLA_TITLE				= "Signed_SLA";
	private static final String					SLA_PREFIX				= SLA_TITLE + ":";
	private static final int					MAX_REASON_LENGTH		= 2000;
	private static final int					MAX_TRADE_NAME_LENGTH	= 255;
	private static final DateTimeFormatter		FINANCIAL_DATE_FORMAT	= DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH);

	private MBPartner							currentOrg;
	private String								orgName					= "";
	private String								sdlNo					= "";
	private List<X_ZZ_Application_Form>			availableAllocations	= new ArrayList<>();
	private final Map<Integer, String>			programTitles			= new HashMap<>();
	private X_ZZ_Application_Form				selectedApplication;
	private String								selectedProgramTitle	= "";
	private int									allocatedLearners;
	private X_ZZ_Allocation_Offer				existingOffer;
	private String								currentDocStatus		= X_ZZ_Allocation_Offer.ZZ_DOCSTATUS_Draft;
	private String								offerDecision			= X_ZZ_Allocation_Offer.ZZOFFERDECISION_Accept;
	private String								declineReason			= "";
	private String								queryComments			= "";
	private final ListModelList<IntakePlanRow>	intakeRows				= new ListModelList<>();
	private long								totalLearners;
	private final List<String>					tradeSuggestions		= new ArrayList<>();
	private String								uploadedFileName		= "";
	private byte[]								uploadedFileBytes;
	private boolean								documentChanged;
	private LocalDate							financialYearStartDate;
	private LocalDate							financialYearEndDate;
	private String								validationMessage		= "";
	private String								feedbackMessage			= "";
	private boolean								dirty;
	private final Map<Integer, OfferDraft>		workingCopies			= new HashMap<>();

	@Init
	public void init()
	{
		MUser user = MUser.get(Env.getCtx(), Env.getAD_User_ID(Env.getCtx()));
		if (user.getC_BPartner_ID() <= 0)
			return;

		currentOrg = new MBPartner(Env.getCtx(), user.getC_BPartner_ID(), null);
		if (currentOrg.getAD_Client_ID() != Env.getAD_Client_ID(Env.getCtx()))
		{
			currentOrg = null;
			return;
		}
		orgName = currentOrg.getName();
		sdlNo = currentOrg.getValue();
		availableAllocations = new Query(	Env.getCtx(), I_ZZ_Application_Form.Table_Name,
											I_ZZ_Application_Form.COLUMNNAME_C_BPartner_ID	+ " = ? AND "
																							+ I_ZZ_Application_Form.COLUMNNAME_ZZTotalNumberApplied + " > 0",
											null)
													.setParameters(currentOrg.getC_BPartner_ID())
													.setClient_ID().setOnlyActiveRecords(true)
													.setOrderBy(I_ZZ_Application_Form.COLUMNNAME_DocumentNo + " DESC").list();
		if (!availableAllocations.isEmpty())
		{
			loadTradeSuggestions();
			onSelectApplication(availableAllocations.get(0));
		}
	}

	private void loadTradeSuggestions()
	{
		TreeSet<String> names = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
		List<X_ZZ_Trade> trades = new Query(Env.getCtx(), I_ZZ_Trade.Table_Name, null, null)
																							.setClient_ID().setOnlyActiveRecords(true).list();
		for (X_ZZ_Trade trade : trades)
		{
			if (trade.getName() != null && !trade.getName().isBlank())
				names.add(trade.getName().trim());
		}
		List<X_ZZQualification> qualifications = new Query(Env.getCtx(), I_ZZQualification.Table_Name, null, null)
																													.setClient_ID().setOnlyActiveRecords(true)
																													.list();
		for (X_ZZQualification qualification : qualifications)
		{
			if (qualification.getName() != null && !qualification.getName().isBlank())
				names.add(qualification.getName().trim());
		}
		tradeSuggestions.addAll(names);
	}

	@Command
	@NotifyChange({
					"selectedApplication",
						"currentDocStatus",
						"allocatedLearners",
						"offerDecision",
						"declineReason",
						"queryComments",
						"uploadedFileName",
						"dirty",
						"validationMessage",
						"feedbackMessage",
						"totalLearners" })
	public void onSelectApplication(@BindingParam("app")
	X_ZZ_Application_Form app)
	{
		if (app == null || currentOrg == null || availableAllocations	.stream()
																		.noneMatch(item -> item.get_ID() == app.get_ID()))
			return;
		if (selectedApplication != null && selectedApplication.get_ID() == app.get_ID())
			return;
		if (selectedApplication != null && dirty)
			workingCopies.put(selectedApplication.get_ID(), new OfferDraft(this));

		selectedApplication = app;
		allocatedLearners = app.getZZTotalNumberApplied();
		selectedProgramTitle = resolveProgramTitle(app.getZZ_Program_Master_Data_ID());
		initFinancialYear(app);
		validationMessage = "";
		feedbackMessage = "";
		dirty = false;
		loadOffer(app);
		OfferDraft draft = workingCopies.remove(app.get_ID());
		if (draft != null && isEditable())
		{
			if (Objects.equals(draft.updatedAt, getOfferVersion()))
			{
				offerDecision = draft.decision;
				declineReason = draft.reason;
				intakeRows.clear();
				intakeRows.addAll(draft.rows);
				uploadedFileName = draft.fileName;
				uploadedFileBytes = draft.fileBytes;
				documentChanged = draft.documentChanged;
				dirty = true;
			}
			else
				feedbackMessage = "MQA updated this offer. The latest saved details have been loaded; please review them before editing.";
		}
		calculateTotalLearners();
	}

	private void initFinancialYear(X_ZZ_Application_Form app)
	{
		LocalDate anchor = app.getDateDoc() != null ? toLocalDate(app.getDateDoc()) : LocalDate.now();
		int startYear = anchor.getMonthValue() >= 4 ? anchor.getYear() : anchor.getYear() - 1;
		X_ZZ_Program_Master_Data program = new X_ZZ_Program_Master_Data(Env.getCtx(), app.getZZ_Program_Master_Data_ID(), null);
		if (program.getC_Year_ID() > 0)
		{
			MPeriod firstPeriod = new Query(Env.getCtx(), I_C_Period.Table_Name,
											I_C_Period.COLUMNNAME_C_Year_ID + " = ? AND " + I_C_Period.COLUMNNAME_PeriodType + " = ?", null)
																																			.setParameters(	program.getC_Year_ID(),
																																							MPeriod.PERIODTYPE_StandardCalendarPeriod)
																																			.setOnlyActiveRecords(true)
																																			.setOrderBy(I_C_Period.COLUMNNAME_StartDate)
																																			.first();
			if (firstPeriod != null && firstPeriod.getStartDate() != null
				&& toLocalDate(firstPeriod.getStartDate()).getMonthValue() == 4)
				startYear = toLocalDate(firstPeriod.getStartDate()).getYear();
			else
			{
				String fiscalYear = new MYear(Env.getCtx(), program.getC_Year_ID(), null).getFiscalYear();
				if (fiscalYear != null && fiscalYear.matches("\\d{4}"))
					startYear = Integer.parseInt(fiscalYear);
			}
		}
		financialYearStartDate = LocalDate.of(startYear, 4, 1);
		financialYearEndDate = LocalDate.of(startYear + 1, 3, 31);
	}

	private void loadOffer(X_ZZ_Application_Form app)
	{
		existingOffer = offerQuery(app.get_ID(), null).first();
		currentDocStatus = existingOffer == null ? X_ZZ_Allocation_Offer.ZZ_DOCSTATUS_Draft : existingOffer.getZZ_DocStatus();
		queryComments = isInQueryState() && existingOffer.getZZ_Reason() != null ? existingOffer.getZZ_Reason() : "";
		offerDecision = existingOffer != null && existingOffer.getZZOfferDecision() != null
																							? existingOffer.getZZOfferDecision()
																							: X_ZZ_Allocation_Offer.ZZOFFERDECISION_Accept;
		// A reviewer's query belongs in the banner, never in the organisation's decline response.
		declineReason = existingOffer != null && !isInQueryState() && existingOffer.getZZ_Reason() != null
																											? existingOffer.getZZ_Reason()
																											: "";
		intakeRows.clear();
		uploadedFileName = "";
		uploadedFileBytes = null;
		documentChanged = false;
		if (existingOffer != null)
		{
			List<X_ZZ_Allocation_Intake_Plan> lines = new Query(Env.getCtx(), I_ZZ_Allocation_Intake_Plan.Table_Name,
																I_ZZ_Allocation_Intake_Plan.COLUMNNAME_ZZ_Allocation_Offer_ID + " = ?", null)
																																				.setParameters(existingOffer.get_ID())
																																				.setClient_ID()
																																				.setOnlyActiveRecords(true)
																																				.setOrderBy(I_ZZ_Allocation_Intake_Plan.COLUMNNAME_LineNo)
																																				.list();
			for (X_ZZ_Allocation_Intake_Plan line : lines)
			{
				IntakePlanRow row = new IntakePlanRow();
				row.intakePlanId = line.getZZ_Allocation_Intake_Plan_ID();
				row.setLineNo(intakeRows.size() + 1);
				row.setTradeName(line.getZZTradeName());
				row.tradeId = line.getZZ_Trade_ID();
				row.qualificationId = line.getZZQualification_ID();
				row.setNumberOfLearners(line.getZZNoLearners());
				row.setIntakeDate(line.getZZIntakeDate());
				intakeRows.add(row);
			}
			loadExistingAttachment();
		}
	}

	private Query offerQuery(int applicationId, String trxName)
	{
		return new Query(	Env.getCtx(), I_ZZ_Allocation_Offer.Table_Name,
							I_ZZ_Allocation_Offer.COLUMNNAME_ZZ_Application_Form_ID + " = ? AND "
																			+ I_ZZ_Allocation_Offer.COLUMNNAME_C_BPartner_ID + " = ?", trxName)
																																				.setParameters(	applicationId,
																																								currentOrg.get_ID())
																																				.setClient_ID()
																																				.setOnlyActiveRecords(true)
																																				.setOrderBy(I_ZZ_Allocation_Offer.COLUMNNAME_ZZ_Allocation_Offer_ID
																																							+ " DESC");
	}

	private void loadExistingAttachment()
	{
		MAttachment attachment = MAttachment.get(Env.getCtx(), existingOffer.get_Table_ID(), existingOffer.get_ID());
		if (attachment == null)
			return;

		for (MAttachmentEntry entry : attachment.getEntries())
		{
			if (entry.getName().startsWith(SLA_PREFIX))
			{
				uploadedFileName = entry.getName().substring(SLA_PREFIX.length()).trim();
				uploadedFileBytes = entry.getData();
				return;
			}
		}
	}

	private String resolveProgramTitle(int programId)
	{
		if (programId <= 0)
			return "";
		return programTitles.computeIfAbsent(programId, id -> {
			X_ZZ_Program_Master_Data program = new X_ZZ_Program_Master_Data(Env.getCtx(), id, null);
			return program.getTitle() != null && !program.getTitle().isBlank()
																				? program.getTitle()
																				: (program.getName() != null ? program.getName() : "");
		});
	}

	@Command
	@NotifyChange({ "offerDecision", "dirty", "validationMessage", "feedbackMessage" })
	public void onDecisionChanged(@BindingParam("decision")
	String decision)
	{
		if (!isEditable() || (!X_ZZ_Allocation_Offer.ZZOFFERDECISION_Accept.equals(decision)
								&& !X_ZZ_Allocation_Offer.ZZOFFERDECISION_Declined.equals(decision)))
			return;
		offerDecision = decision;
		markChanged();
	}

	@Command
	@NotifyChange({ "dirty", "validationMessage", "feedbackMessage", "totalLearners" })
	public void onFieldChanged()
	{
		markChanged();
		calculateTotalLearners();
	}

	private void markChanged()
	{
		if (isEditable())
		{
			dirty = true;
			validationMessage = "";
			feedbackMessage = "";
		}
	}

	@Command
	@NotifyChange({ "dirty", "validationMessage", "feedbackMessage" })
	public void addIntakeRow()
	{
		if (!isEditable())
			return;
		intakeRows.add(new IntakePlanRow(intakeRows.size() + 1));
		markChanged();
	}

	@Command
	@NotifyChange({ "dirty", "validationMessage", "feedbackMessage", "totalLearners" })
	public void removeIntakeRow(@BindingParam("row")
	IntakePlanRow row)
	{
		if (!isEditable() || !intakeRows.remove(row))
			return;
		for (int i = 0; i < intakeRows.size(); i++)
		{
			IntakePlanRow remaining = intakeRows.get(i);
			if (remaining.getLineNo() != i + 1)
			{
				remaining.setLineNo(i + 1);
				intakeRows.notifyChange(remaining);
			}
		}
		markChanged();
		calculateTotalLearners();
	}

	private void calculateTotalLearners()
	{
		totalLearners = 0;
		for (IntakePlanRow row : intakeRows)
			totalLearners += row.getNumberOfLearners() == null ? 0 : row.getNumberOfLearners();
	}

	public String getUploadSetting()
	{
		return AdempiereWebUI.getUploadSetting() + ",multiple=false,accept=.pdf";
	}

	@Command
	@NotifyChange({ "uploadedFileName", "dirty", "validationMessage", "feedbackMessage" })
	public void onUploadDocument(@BindingParam("media")
	Media media)
	{
		if (!isEditable() || media == null)
			return;
		feedbackMessage = "";
		try
		{
			String fileName = media.getName() == null ? "" : media.getName().replace('\\', '/');
			fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
			if (!fileName.toLowerCase(Locale.ROOT).endsWith(".pdf") || !media.isBinary())
			{
				validationMessage = "Choose a PDF of your signed Allocation Offer / SLA. Your current document has been kept.";
				return;
			}
			byte[] data;
			if (media.inMemory())
			{
				data = media.getByteData();
			}
			else
			{
				try (InputStream stream = media.getStreamData())
				{
					data = stream.readAllBytes();
				}
			}
			if (!isPdf(data))
			{
				validationMessage = "This file is empty or is not a PDF. Choose the signed PDF document again.";
				return;
			}
			uploadedFileName = fileName;
			uploadedFileBytes = data;
			documentChanged = true;
			markChanged();
		}
		catch (Exception ex)
		{
			log.log(Level.WARNING, "Could not read allocation SLA upload", ex);
			validationMessage = "The document could not be uploaded. Please try again. Your current document has been kept.";
		}
	}

	private static boolean isPdf(byte[] data)
	{
		return data != null && data.length >= 5 && data[0] == '%' && data[1] == 'P'
				&& data[2] == 'D' && data[3] == 'F' && data[4] == '-';
	}

	@Command
	public void downloadDocument()
	{
		if (uploadedFileBytes != null && uploadedFileBytes.length > 0)
			Filedownload.save(uploadedFileBytes, "application/pdf", uploadedFileName);
	}

	@Command
	@NotifyChange({ "uploadedFileName", "dirty", "validationMessage", "feedbackMessage" })
	public void removeUploadedDocument()
	{
		if (!isEditable())
			return;
		uploadedFileName = "";
		uploadedFileBytes = null;
		documentChanged = true;
		markChanged();
	}

	@Command
	@NotifyChange({
					"currentDocStatus",
						"queryComments",
						"dirty",
						"validationMessage",
						"feedbackMessage",
						"totalLearners" })
	public void submitAccept()
	{
		if (!canSave() || !X_ZZ_Allocation_Offer.ZZOFFERDECISION_Accept.equals(offerDecision) || !validateAccept())
			return;
		if (saveOfferRecord(X_ZZ_Allocation_Offer.ZZ_DOCSTATUS_Submitted))
		{
			sendEmailConfirmation(false);
			feedbackMessage = "Your intake plan has been submitted to MQA for review. Reference: "	+ existingOffer.getDocumentNo()
								+ ". You can select another allocation offer above.";
		}
	}

	@Command
	@NotifyChange({
					"currentDocStatus",
						"queryComments",
						"dirty",
						"validationMessage",
						"feedbackMessage",
						"totalLearners" })
	public void submitDecline()
	{
		if (!canSave() || !X_ZZ_Allocation_Offer.ZZOFFERDECISION_Declined.equals(offerDecision))
			return;
		if (declineReason.isBlank())
		{
			validationMessage = "Enter a reason for declining this allocation offer.";
			return;
		}
		if (declineReason.length() > MAX_REASON_LENGTH)
		{
			validationMessage = "Keep your decline reason within 2,000 characters.";
			return;
		}
		if (saveOfferRecord(X_ZZ_Allocation_Offer.ZZ_DOCSTATUS_Query))
		{
			sendEmailConfirmation(true);
			feedbackMessage = "Your non-acceptance decision has been sent to MQA for follow-up. Reference: "
								+ existingOffer.getDocumentNo() + ".";
		}
	}

	@Command
	@NotifyChange({
					"currentDocStatus",
						"queryComments",
						"dirty",
						"validationMessage",
						"feedbackMessage",
						"totalLearners" })
	public void saveDraft()
	{
		if (!canSave() || !validateDraft())
			return;

		// Keep a queried offer in Query until the organisation explicitly resubmits it.
		String status = isInQueryState() ? X_ZZ_Allocation_Offer.ZZ_DOCSTATUS_Query : X_ZZ_Allocation_Offer.ZZ_DOCSTATUS_Draft;
		if (saveOfferRecord(status))
			feedbackMessage = "Draft saved. You can return to this offer and complete it later. Reference: " + existingOffer.getDocumentNo() + ".";
	}

	private boolean validateDraft()
	{
		calculateTotalLearners();
		if (declineReason.length() > MAX_REASON_LENGTH)
		{
			validationMessage = "Keep your decline reason within 2,000 characters.";
			return false;
		}
		if (totalLearners > Integer.MAX_VALUE)
		{
			validationMessage = "The total learner count is too large. Check the number entered on each intake line.";
			return false;
		}
		for (IntakePlanRow row : intakeRows)
		{
			if (row.getTradeName().length() > MAX_TRADE_NAME_LENGTH)
			{
				validationMessage = "Line " + row.getLineNo() + ": keep the qualification or trade name within 255 characters.";
				return false;
			}
			if (row.getNumberOfLearners() != null && row.getNumberOfLearners() < 0)
			{
				validationMessage = "Line " + row.getLineNo() + ": the learner count cannot be negative.";
				return false;
			}
		}
		return true;
	}

	private boolean canSave()
	{
		feedbackMessage = "";
		validationMessage = "";
		if (!isEditable())
		{
			validationMessage = "This offer is read-only. Only draft offers and offers returned with a query can be changed.";
			return false;
		}
		if (!X_ZZ_Allocation_Offer.ZZOFFERDECISION_Accept.equals(offerDecision)
			&& !X_ZZ_Allocation_Offer.ZZOFFERDECISION_Declined.equals(offerDecision))
		{
			validationMessage = "Choose whether to accept or decline the allocation offer.";
			return false;
		}
		return true;
	}

	private boolean validateAccept()
	{
		if (!validateDraft())
			return false;
		if (uploadedFileName.isBlank() || !isPdf(uploadedFileBytes))
		{
			validationMessage = "Upload the signed Allocation Offer / SLA as a PDF before submitting.";
			return false;
		}
		if (intakeRows.isEmpty())
		{
			validationMessage = "Add at least one intake line with a qualification or trade, learner count and intake date.";
			return false;
		}

		for (IntakePlanRow row : intakeRows)
		{
			String prefix = "Line " + row.getLineNo() + ": ";
			if (row.getTradeName().isBlank())
			{
				validationMessage = prefix + "enter a qualification or trade name.";
				return false;
			}
			if (row.getNumberOfLearners() == null || row.getNumberOfLearners() <= 0)
			{
				validationMessage = prefix + "enter a whole number of learners greater than zero.";
				return false;
			}
			if (row.getIntakeDate() == null)
			{
				validationMessage = prefix + "choose an intake date.";
				return false;
			}
			LocalDate intakeDate = toLocalDate(row.getIntakeDate());
			if (intakeDate.isBefore(financialYearStartDate) || intakeDate.isAfter(financialYearEndDate))
			{
				validationMessage = prefix + "choose an intake date within " + getFinancialYearRange() + ".";
				return false;
			}
		}
		if (totalLearners <= 0 || totalLearners > allocatedLearners)
		{
			validationMessage = "Plan between 1 and " + allocatedLearners + " learners. The current total is " + totalLearners + ".";
			return false;
		}
		validationMessage = "";
		return true;
	}

	private boolean saveOfferRecord(String status)
	{
		Trx trx = Trx.get(Trx.createTrxName("AllocationOffer"), true);
		Map<IntakePlanRow, Integer> savedLineIds = new HashMap<>();
		X_ZZ_Allocation_Offer offer;
		try
		{
			offer = saveOffer(status, trx.getTrxName(), savedLineIds);
			trx.commit(true);
		}
		catch (Exception ex)
		{
			trx.rollback();
			log.log(Level.SEVERE, "Could not save allocation offer for application " + selectedApplication.get_ID(), ex);
			validationMessage = "Your changes could not be saved. The offer may have changed since you opened it. "
								+ "Keep a copy of your changes and reopen the form to review the latest offer before trying again.";
			return false;
		}
		finally
		{
			trx.close();
		}

		offer.set_TrxName(null);
		existingOffer = offer;
		if (X_ZZ_Allocation_Offer.ZZOFFERDECISION_Declined.equals(offerDecision))
		{
			intakeRows.clear();
			calculateTotalLearners();
		}
		for (Map.Entry<IntakePlanRow, Integer> entry : savedLineIds.entrySet())
			entry.getKey().intakePlanId = entry.getValue();
		currentDocStatus = offer.getZZ_DocStatus();
		if (isInQueryState())
			queryComments = offer.getZZ_Reason() == null ? "" : offer.getZZ_Reason();
		documentChanged = false;
		workingCopies.remove(selectedApplication.get_ID());
		dirty = false;
		validationMessage = "";
		return true;
	}

	private X_ZZ_Allocation_Offer saveOffer(String status, String trxName, Map<IntakePlanRow, Integer> savedLineIds)
	{
		// Lock the application so two sessions cannot create separate responses to the same offer.
		X_ZZ_Application_Form application = new Query(	Env.getCtx(), I_ZZ_Application_Form.Table_Name,
														I_ZZ_Application_Form.COLUMNNAME_ZZ_Application_Form_ID + " = ? AND "
																										+ I_ZZ_Application_Form.COLUMNNAME_C_BPartner_ID
																										+ " = ?", trxName)
																															.setParameters(	selectedApplication.get_ID(),
																																			currentOrg.get_ID())
																															.setClient_ID()
																															.setOnlyActiveRecords(true)
																															.setForUpdate(true).first();
		if (application == null || application.getZZTotalNumberApplied() != allocatedLearners
			|| application.getZZ_Program_Master_Data_ID() != selectedApplication.getZZ_Program_Master_Data_ID())
			throw new IllegalStateException("Allocation changed or is no longer available");

		X_ZZ_Allocation_Offer offer = offerQuery(application.get_ID(), trxName).setForUpdate(true).first();
		if (offer != null && (existingOffer == null || offer.get_ID() != existingOffer.get_ID()
								|| !isEditableStatus(offer.getZZ_DocStatus()) || !Objects.equals(offer.getUpdated(), existingOffer.getUpdated())))
			throw new IllegalStateException("Offer changed since it was opened");
		if (offer == null && existingOffer != null)
			throw new IllegalStateException("Offer is no longer available");
		if (offer == null)
		{
			offer = new X_ZZ_Allocation_Offer(Env.getCtx(), 0, trxName);
			offer.setAD_Org_ID(application.getAD_Org_ID());
		}

		boolean declining = X_ZZ_Allocation_Offer.ZZOFFERDECISION_Declined.equals(offerDecision);
		String reason = null;
		if (declining)
			reason = declineReason.trim();
		else if (X_ZZ_Allocation_Offer.ZZ_DOCSTATUS_Query.equals(status) && isInQueryState())
			reason = queryComments;
		offer.setC_BPartner_ID(currentOrg.get_ID());
		offer.setZZ_Program_Master_Data_ID(application.getZZ_Program_Master_Data_ID());
		offer.setZZ_Application_Form_ID(application.get_ID());
		offer.setZZAllocatedLearners(allocatedLearners);
		offer.setZZAcceptedLearners(declining ? 0 : Math.toIntExact(totalLearners));
		offer.setZZOfferDecision(offerDecision);
		offer.setZZ_Reason(reason);
		offer.setZZ_DocStatus(status);
		offer.setDateDoc(new Timestamp(System.currentTimeMillis()));
		offer.setAD_User_ID(Env.getAD_User_ID(Env.getCtx()));
		offer.saveEx(trxName);

		saveIntakeLines(offer, declining, trxName, savedLineIds);
		persistAttachment(offer, trxName);
		return offer;
	}

	private void saveIntakeLines(	X_ZZ_Allocation_Offer offer, boolean declining, String trxName,
									Map<IntakePlanRow, Integer> savedLineIds)
	{
		List<X_ZZ_Allocation_Intake_Plan> savedLines = new Query(	Env.getCtx(), I_ZZ_Allocation_Intake_Plan.Table_Name,
																	I_ZZ_Allocation_Intake_Plan.COLUMNNAME_ZZ_Allocation_Offer_ID + " = ?", trxName)
																																					.setParameters(offer.get_ID())
																																					.setClient_ID()
																																					.setOnlyActiveRecords(true)
																																					.setForUpdate(true)
																																					.list();
		Map<Integer, X_ZZ_Allocation_Intake_Plan> remainingLines = new HashMap<>();
		for (X_ZZ_Allocation_Intake_Plan line : savedLines)
			remainingLines.put(line.get_ID(), line);

		if (!declining)
		{
			for (IntakePlanRow row : intakeRows)
			{
				X_ZZ_Allocation_Intake_Plan line = remainingLines.remove(row.intakePlanId);
				if (line == null)
				{
					if (row.intakePlanId > 0)
						throw new IllegalStateException("Intake line changed since the offer was opened");
					line = new X_ZZ_Allocation_Intake_Plan(Env.getCtx(), 0, trxName);
					line.setAD_Org_ID(offer.getAD_Org_ID());
					line.setZZ_Allocation_Offer_ID(offer.get_ID());
				}
				line.setLineNo(row.getLineNo());
				line.setZZTradeName(row.getTradeName());
				line.setZZ_Trade_ID(row.tradeId);
				line.setZZQualification_ID(row.qualificationId);
				line.setZZNoLearners(row.getNumberOfLearners() == null ? 0 : row.getNumberOfLearners());
				line.setZZIntakeDate(row.getIntakeDate() == null ? null : new Timestamp(row.getIntakeDate().getTime()));
				line.saveEx(trxName);
				savedLineIds.put(row, line.get_ID());
			}
		}

		for (X_ZZ_Allocation_Intake_Plan line : remainingLines.values())
			line.deleteEx(false, trxName);
	}

	private void persistAttachment(X_ZZ_Allocation_Offer offer, String trxName)
	{
		if (!documentChanged)
			return;
		if (uploadedFileBytes != null && uploadedFileBytes.length > 0)
			AttachmentUtil.addOrReplaceAttachmentEntry(offer, uploadedFileName, uploadedFileBytes, SLA_TITLE, trxName);
		else
			AttachmentUtil.removeAttachmentEntry(offer, SLA_TITLE, trxName);
	}

	private void sendEmailConfirmation(boolean declined)
	{
		try
		{
			List<MUser> contacts = new Query(	Env.getCtx(), I_AD_User.Table_Name,
												I_AD_User.COLUMNNAME_C_BPartner_ID + " = ?", null)	.setParameters(currentOrg.get_ID())
																									.setClient_ID().setOnlyActiveRecords(true).list();
			TreeSet<String> sentAddresses = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
			MClient client = MClient.get(Env.getCtx());
			MUser from = MUser.get(Env.getCtx(), MSysConfig.getIntValue("FROM_EMAIL_USER_ID", 1000011, Env.getAD_Client_ID(Env.getCtx())));
			String subject = "MQA Allocation Offer "	+ (declined ? "Non-Acceptance" : "& Intake Plan Confirmation")
								+ " - " + existingOffer.getDocumentNo();
			StringBuilder body = new StringBuilder("Your allocation offer response has been submitted to MQA.\n\n")
																													.append("Reference: ").append(existingOffer
																																								.getDocumentNo())
																													.append("\nOrganisation: ").append(orgName)
																													.append("\nProgramme: ").append(
																																					selectedProgramTitle)
																													.append("\nFinancial year: ").append(
																																							getFinancialYearString());
			if (declined)
				body.append("\nDecision: Not accepted\nReason: ").append(declineReason)
					.append("\n\nMQA will follow up on your response.");
			else
			{
				body.append("\nLearners planned: ").append(totalLearners).append(" of ").append(allocatedLearners).append("\n\nIntake schedule:\n");
				for (IntakePlanRow row : intakeRows)
					body.append(String.format(	"Line %d: %s | Learners: %d | Intake date: %tF%n",
												row.getLineNo(), row.getTradeName(), row.getNumberOfLearners(), row.getIntakeDate()));
				body.append("\nYour submission will now be reviewed by the MQA LP Administration team.");
			}
			body.append("\n\nRegards,\nMQA Discretionary Grants");
			for (MUser contact : contacts)
			{
				String email = contact.getEMail();
				if (email == null || email.isBlank() || !sentAddresses.add(email.trim()))
					continue;
				try
				{
					if (!client.sendEMail(from, contact, subject, body.toString(), null, false))
						log.warning("Allocation offer confirmation email could not be sent to user " + contact.get_ID());
				}
				catch (Exception ex)
				{
					log.log(Level.WARNING, "Allocation offer confirmation email failed for user " + contact.get_ID(), ex);
				}
			}
		}
		catch (Exception ex)
		{
			log.log(Level.WARNING, "Allocation offer saved but confirmation email failed", ex);
		}
	}

	private static LocalDate toLocalDate(Date date)
	{
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}

	private Timestamp getOfferVersion()
	{
		return existingOffer == null ? null : existingOffer.getUpdated();
	}

	private static class OfferDraft implements Serializable
	{
		private static final long			serialVersionUID	= 1L;
		private final String				decision;
		private final String				reason;
		private final String				fileName;
		private final Timestamp				updatedAt;
		private final boolean				documentChanged;
		private final List<IntakePlanRow>	rows;
		private final byte[]				fileBytes;

		private OfferDraft(AllocationOfferIntakeVM vm)
		{
			decision = vm.offerDecision;
			reason = vm.declineReason;
			fileName = vm.uploadedFileName;
			fileBytes = vm.uploadedFileBytes;
			documentChanged = vm.documentChanged;
			rows = new ArrayList<>();
			for (IntakePlanRow row : vm.intakeRows)
				rows.add(new IntakePlanRow(row));
			updatedAt = vm.getOfferVersion();
		}
	}

	public static class IntakePlanRow implements Serializable
	{
		private static final long	serialVersionUID	= 1L;
		private int					intakePlanId;
		private int					tradeId;
		private int					qualificationId;
		private int					lineNo;
		private String				tradeName			= "";
		private Integer				numberOfLearners;
		private Date				intakeDate;

		public IntakePlanRow()
		{
		}

		private IntakePlanRow(IntakePlanRow row)
		{
			intakePlanId = row.intakePlanId;
			tradeId = row.tradeId;
			qualificationId = row.qualificationId;
			lineNo = row.lineNo;
			tradeName = row.tradeName;
			numberOfLearners = row.numberOfLearners;
			intakeDate = row.intakeDate == null ? null : new Date(row.intakeDate.getTime());
		}

		public IntakePlanRow(int lineNo)
		{
			this.lineNo = lineNo;
		}

		public int getLineNo()
		{
			return lineNo;
		}

		public String getDeleteTooltip()
		{
			return "Delete intake line " + lineNo;
		}

		public void setLineNo(int lineNo)
		{
			this.lineNo = lineNo;
		}

		public String getTradeName()
		{
			return tradeName;
		}

		public void setTradeName(String tradeName)
		{
			String name = tradeName == null ? "" : tradeName.trim();
			if (!Objects.equals(this.tradeName, name))
			{
				tradeId = 0;
				qualificationId = 0;
			}
			this.tradeName = name;
		}

		public Integer getNumberOfLearners()
		{
			return numberOfLearners;
		}

		public void setNumberOfLearners(Integer count)
		{
			numberOfLearners = count;
		}

		public Date getIntakeDate()
		{
			return intakeDate;
		}

		public void setIntakeDate(Date intakeDate)
		{
			this.intakeDate = intakeDate == null ? null : new Date(intakeDate.getTime());
		}
	}

	private static boolean isEditableStatus(String status)
	{
		return X_ZZ_Allocation_Offer.ZZ_DOCSTATUS_Draft.equals(status)
				|| X_ZZ_Allocation_Offer.ZZ_DOCSTATUS_Query.equals(status);
	}

	public boolean isOrganisationLinked()
	{
		return currentOrg != null;
	}

	@DependsOn("availableAllocations")
	public boolean isHasAllocations()
	{
		return !availableAllocations.isEmpty();
	}

	@DependsOn("availableAllocations")
	public boolean isHasMultipleAllocations()
	{
		return availableAllocations.size() > 1;
	}

	@DependsOn({ "selectedApplication", "currentDocStatus" })
	public boolean isEditable()
	{
		return selectedApplication != null && isEditableStatus(currentDocStatus);
	}

	@DependsOn("currentDocStatus")
	public boolean isInQueryState()
	{
		return X_ZZ_Allocation_Offer.ZZ_DOCSTATUS_Query.equals(currentDocStatus);
	}

	@DependsOn({ "totalLearners", "allocatedLearners" })
	public boolean isOverAllocation()
	{
		return totalLearners > allocatedLearners;
	}

	public boolean isDirty()
	{
		return dirty;
	}

	public String getOrgName()
	{
		return orgName;
	}

	public String getSdlNo()
	{
		return sdlNo;
	}

	public String getCurrentDocStatus()
	{
		return currentDocStatus;
	}

	@DependsOn("currentDocStatus")
	public String getStatusLabel()
	{
		MColumn column = MColumn.get(	Env.getCtx(), I_ZZ_Allocation_Offer.Table_Name,
										I_ZZ_Allocation_Offer.COLUMNNAME_ZZ_DocStatus);
		return MRefList.getListName(Env.getCtx(), column.getAD_Reference_Value_ID(), currentDocStatus);
	}

	public String getQueryComments()
	{
		return queryComments;
	}

	@DependsOn("currentDocStatus")
	public String getQueryBannerTitle()
	{
		return existingOffer != null && X_ZZ_Allocation_Offer.ZZOFFERDECISION_Declined.equals(existingOffer.getZZOfferDecision())
																																	? "Non-acceptance response / MQA follow-up"
																																	: "MQA query — action required";
	}

	public String getValidationMessage()
	{
		return validationMessage;
	}

	public String getFeedbackMessage()
	{
		return feedbackMessage;
	}

	public List<X_ZZ_Application_Form> getAvailableAllocations()
	{
		return availableAllocations;
	}

	public X_ZZ_Application_Form getSelectedApplication()
	{
		return selectedApplication;
	}

	public int getAllocatedLearners()
	{
		return allocatedLearners;
	}

	public String getOfferDecision()
	{
		return offerDecision;
	}

	public String getDeclineReason()
	{
		return declineReason;
	}

	public void setDeclineReason(String reason)
	{
		if (isEditable())
			declineReason = reason == null ? "" : reason;
	}

	public ListModelList<IntakePlanRow> getIntakeRows()
	{
		return intakeRows;
	}

	public long getTotalLearners()
	{
		return totalLearners;
	}

	@DependsOn({ "totalLearners", "allocatedLearners" })
	public String getLearnerTotalLabel()
	{
		return totalLearners + " of " + allocatedLearners + " allocated learners planned";
	}

	public List<String> getTradeSuggestions()
	{
		return tradeSuggestions;
	}

	public String getUploadedFileName()
	{
		return uploadedFileName;
	}

	@DependsOn("currentDocStatus")
	public String getReferenceNumber()
	{
		return existingOffer == null ? "Assigned when saved" : existingOffer.getDocumentNo();
	}

	@DependsOn("inQueryState")
	public String getSubmitLabel()
	{
		return isInQueryState() ? "Resubmit Intake Plan" : "Submit Intake Plan";
	}

	@DependsOn("selectedApplication")
	public String getProgramDisplayName()
	{
		return getAppDisplayName(selectedApplication);
	}

	@DependsOn("selectedApplication")
	public String getFinancialYearString()
	{
		return financialYearStartDate == null ? "" : financialYearStartDate.getYear() + "/" + financialYearEndDate.getYear();
	}

	@DependsOn("selectedApplication")
	public String getFinancialYearRange()
	{
		return financialYearStartDate == null	? ""
												: financialYearStartDate.format(FINANCIAL_DATE_FORMAT)
													+ " – " + financialYearEndDate.format(FINANCIAL_DATE_FORMAT);
	}

	@DependsOn("financialYearRange")
	public String getIntakeDatesLabel()
	{
		return "Intake dates: " + getFinancialYearRange();
	}

	@DependsOn({ "totalLearners", "allocatedLearners" })
	public String getLearnerSummary()
	{
		long remaining = allocatedLearners - totalLearners;
		if (remaining < 0)
			return "Over allocation by " + -remaining + " learners. Reduce the intake total before submitting.";
		if (remaining == 0)
			return "All allocated learners are included in your intake plan.";
		return remaining + " allocated learners are not included. Plan only the learners you intend to register.";
	}

	public String getIntakeNote()
	{
		return "The intake plan should reflect the total number of learners allocated for the financial year by your company. "
				+ "In a case that the company is aware that they will not be registering the total number of learners as per the allocation, "
				+ "the intake plan must ONLY reflect the exact number of learners to be registered by the company for the financial year.";
	}

	public String getAppDisplayName(X_ZZ_Application_Form app)
	{
		if (app == null)
			return "";
		String title = resolveProgramTitle(app.getZZ_Program_Master_Data_ID());
		return app.getDocumentNo() + " - " + (title.isBlank() ? orgName : title);
	}

	public String getLineNumberLabel()
	{
		return MasterUtil.getNameOfColTranslated(I_ZZ_Allocation_Intake_Plan.Table_Name, I_ZZ_Allocation_Intake_Plan.COLUMNNAME_LineNo);
	}

	public String getTradeNameLabel()
	{
		return MasterUtil.getNameOfColTranslated(I_ZZ_Allocation_Intake_Plan.Table_Name, I_ZZ_Allocation_Intake_Plan.COLUMNNAME_ZZTradeName);
	}

	public String getLearnerCountLabel()
	{
		return MasterUtil.getNameOfColTranslated(I_ZZ_Allocation_Intake_Plan.Table_Name, I_ZZ_Allocation_Intake_Plan.COLUMNNAME_ZZNoLearners);
	}

	public String getIntakeDateLabel()
	{
		return MasterUtil.getNameOfColTranslated(I_ZZ_Allocation_Intake_Plan.Table_Name, I_ZZ_Allocation_Intake_Plan.COLUMNNAME_ZZIntakeDate);
	}
}
