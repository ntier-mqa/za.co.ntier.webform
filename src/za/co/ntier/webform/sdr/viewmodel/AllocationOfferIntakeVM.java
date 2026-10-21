package za.co.ntier.webform.sdr.viewmodel;

import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.adempiere.webui.AdempiereWebUI;
import org.apache.commons.io.IOUtils;
import org.compiere.model.MAttachment;
import org.compiere.model.MAttachmentEntry;
import org.compiere.model.MBPartner;
import org.compiere.model.MClient;
import org.compiere.model.MSysConfig;
import org.compiere.model.MUser;
import org.compiere.model.Query;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.UploadEvent;

import za.co.ntier.api.model.I_ZZ_Allocation_Intake_Plan;
import za.co.ntier.api.model.I_ZZ_Allocation_Offer;
import za.co.ntier.api.model.I_ZZ_Application_Form;
import za.co.ntier.api.model.I_ZZ_Trade;
import za.co.ntier.api.model.X_ZZ_Allocation_Intake_Plan;
import za.co.ntier.api.model.X_ZZ_Allocation_Offer;
import za.co.ntier.api.model.X_ZZ_Application_Form;
import za.co.ntier.api.model.X_ZZ_Program_Master_Data;
import za.co.ntier.api.model.X_ZZ_Trade;
import za.co.ntier.webform.form.AttachmentUtil;
import za.co.ntier.webform.form.MasterUtil;

/**
 * ViewModel for Allocation Offer and Intake Plan Web Form (Ticket #421 / BUC 9A).
 * Handles employer offer review, SLA document upload, and intake schedule planning.
 */
public class AllocationOfferIntakeVM implements Serializable
{
	private static final long serialVersionUID = 1L;
	private static final CLogger log = CLogger.getCLogger(AllocationOfferIntakeVM.class);
	private static final String ATTACHMENT_TITLE_SIGNED_SLA = "Signed_SLA";

	// Organisation context
	private MBPartner currentOrg;
	private String orgName = "";
	private String sdlNo = "";
	private boolean isEmployerUser = false;

	// Application & Program allocation
	private List<X_ZZ_Application_Form> availableAllocations = new ArrayList<>();
	private X_ZZ_Application_Form selectedApplication;
	private String selectedProgramTitle = "";
	private int allocatedLearners = 0;

	// Decision and status workflow
	private String offerDecision = X_ZZ_Allocation_Offer.ZZOFFERDECISION_Accept;
	private String declineReason = "";
	private String currentDocStatus = X_ZZ_Allocation_Offer.ZZ_DOCSTATUS_Draft;
	private boolean inQueryState = false;

	// Intake schedule
	private List<IntakePlanRow> intakeRows = new ArrayList<>();
	private int totalLearners = 0;

	// SLA document upload
	private String uploadedFileName = "";
	private byte[] uploadedFileBytes;

	// Existing persisted offer and trade lookup cache
	private X_ZZ_Allocation_Offer existingOffer;
	private List<String> tradeSuggestions = new ArrayList<>();

	// Financial year boundaries
	private String financialYearString = "";
	private LocalDate financialYearStartDate;
	private LocalDate financialYearEndDate;

	@Init
	public void init()
	{
		initFinancialYear();
		loadTradeSuggestions();
		initOrganisationAndAllocations();
	}

	private void initFinancialYear()
	{
		LocalDate now = LocalDate.now();
		int startYear = (now.getMonthValue() >= 4) ? now.getYear() : now.getYear() - 1;
		financialYearString = startYear + "/" + (startYear + 1);
		financialYearStartDate = LocalDate.of(startYear, 4, 1);
		financialYearEndDate = LocalDate.of(startYear + 1, 3, 31);
	}

	public String getFinancialYearString()
	{
		return financialYearString;
	}

	public LocalDate getFinancialYearStartDate()
	{
		return financialYearStartDate;
	}

	public LocalDate getFinancialYearEndDate()
	{
		return financialYearEndDate;
	}

	private void loadTradeSuggestions()
	{
		List<X_ZZ_Trade> trades = new Query(Env.getCtx(), I_ZZ_Trade.Table_Name, null, null)
				.setOnlyActiveRecords(true)
				.setOrderBy(I_ZZ_Trade.COLUMNNAME_Name)
				.list();

		tradeSuggestions.clear();
		for (X_ZZ_Trade trade : trades)
		{
			String name = trade.getName();
			if (name != null && !name.isBlank())
			{
				tradeSuggestions.add(name);
			}
		}
	}

	private void initOrganisationAndAllocations()
	{
		int userId = Env.getAD_User_ID(Env.getCtx());
		if (userId > 0)
		{
			MUser user = new MUser(Env.getCtx(), userId, null);
			int bpId = user.getC_BPartner_ID();
			if (bpId > 0)
			{
				isEmployerUser = true;
				currentOrg = new MBPartner(Env.getCtx(), bpId, null);
				orgName = currentOrg.getName();
				sdlNo = currentOrg.getValue();
			}
		}

		StringBuilder whereClause = new StringBuilder(I_ZZ_Application_Form.COLUMNNAME_ZZTotalNumberApplied)
				.append(" > 0");
		List<Object> params = new ArrayList<>();

		if (currentOrg != null)
		{
			whereClause.append(" AND ").append(I_ZZ_Application_Form.COLUMNNAME_C_BPartner_ID).append(" = ?");
			params.add(currentOrg.getC_BPartner_ID());
		}

		Query query = new Query(Env.getCtx(), I_ZZ_Application_Form.Table_Name, whereClause.toString(), null)
				.setOnlyActiveRecords(true)
				.setOrderBy(I_ZZ_Application_Form.COLUMNNAME_DocumentNo + " DESC");

		if (!params.isEmpty())
		{
			query.setParameters(params);
		}

		availableAllocations = query.list();

		if (!availableAllocations.isEmpty())
		{
			onSelectApplication(availableAllocations.get(0));
		}
		else
		{
			intakeRows.clear();
			totalLearners = 0;
		}
	}

	@Command
	@NotifyChange({"selectedApplication", "selectedProgramTitle", "orgName", "sdlNo", "allocatedLearners", "existingOffer",
			"inQueryState", "declineReason", "offerDecision", "intakeRows", "totalLearners", "uploadedFileName", "programDisplayName"})
	public void onSelectApplication(@BindingParam("app") X_ZZ_Application_Form app)
	{
		if (app == null)
			return;

		this.selectedApplication = app;
		this.allocatedLearners = app.getZZTotalNumberApplied();
		this.selectedProgramTitle = resolveProgramTitle(app.getZZ_Program_Master_Data_ID());

		if (!isEmployerUser)
		{
			if (app.getOrgName() != null && !app.getOrgName().isBlank())
			{
				orgName = app.getOrgName();
				sdlNo = app.getZZ_SDL_No() != null ? app.getZZ_SDL_No() : "";
			}
			else if (app.getC_BPartner_ID() > 0)
			{
				currentOrg = new MBPartner(Env.getCtx(), app.getC_BPartner_ID(), null);
				orgName = currentOrg.getName();
				sdlNo = currentOrg.getValue();
			}
		}

		checkExistingOffer(app);
	}

	private void checkExistingOffer(X_ZZ_Application_Form app)
	{
		int bpId = currentOrg != null ? currentOrg.getC_BPartner_ID() : app.getC_BPartner_ID();
		int progId = app.getZZ_Program_Master_Data_ID();

		String where = I_ZZ_Allocation_Offer.COLUMNNAME_C_BPartner_ID + " = ? AND "
				+ I_ZZ_Allocation_Offer.COLUMNNAME_ZZ_Program_Master_Data_ID + " = ?";

		existingOffer = new Query(Env.getCtx(), I_ZZ_Allocation_Offer.Table_Name, where, null)
				.setParameters(bpId, progId)
				.setOnlyActiveRecords(true)
				.setOrderBy(I_ZZ_Allocation_Offer.COLUMNNAME_ZZ_Allocation_Offer_ID + " DESC")
				.first();

		if (existingOffer != null)
		{
			currentDocStatus = existingOffer.getZZ_DocStatus();
			inQueryState = X_ZZ_Allocation_Offer.ZZ_DOCSTATUS_Query.equals(currentDocStatus);
			declineReason = existingOffer.getZZ_Reason() != null ? existingOffer.getZZ_Reason() : "";
			offerDecision = existingOffer.getZZOfferDecision() != null ? existingOffer.getZZOfferDecision() : X_ZZ_Allocation_Offer.ZZOFFERDECISION_Accept;

			loadExistingIntakeLines(existingOffer);
			loadExistingAttachment(existingOffer);
		}
		else
		{
			inQueryState = false;
			declineReason = "";
			offerDecision = X_ZZ_Allocation_Offer.ZZOFFERDECISION_Accept;
			uploadedFileName = "";
			uploadedFileBytes = null;
			initDefaultIntakeRow();
		}
	}

	private void loadExistingIntakeLines(X_ZZ_Allocation_Offer offer)
	{
		List<X_ZZ_Allocation_Intake_Plan> lines = new Query(Env.getCtx(), I_ZZ_Allocation_Intake_Plan.Table_Name,
				I_ZZ_Allocation_Intake_Plan.COLUMNNAME_ZZ_Allocation_Offer_ID + " = ?", null)
				.setParameters(offer.getZZ_Allocation_Offer_ID())
				.setOnlyActiveRecords(true)
				.setOrderBy(I_ZZ_Allocation_Intake_Plan.COLUMNNAME_LineNo)
				.list();

		intakeRows.clear();
		for (X_ZZ_Allocation_Intake_Plan line : lines)
		{
			IntakePlanRow row = new IntakePlanRow();
			row.setLineNo(line.getLineNo());
			row.setTradeName(line.getZZTradeName());
			row.setNumberOfLearners(line.getZZNoLearners());
			if (line.getZZIntakeDate() != null)
			{
				row.setIntakeDate(new Date(line.getZZIntakeDate().getTime()));
			}
			intakeRows.add(row);
		}

		if (intakeRows.isEmpty() && X_ZZ_Allocation_Offer.ZZOFFERDECISION_Accept.equals(offerDecision))
		{
			initDefaultIntakeRow();
		}
		else
		{
			calculateTotalLearners();
		}
	}

	private void loadExistingAttachment(X_ZZ_Allocation_Offer offer)
	{
		MAttachment att = MAttachment.get(Env.getCtx(), offer.get_Table_ID(), offer.get_ID());
		if (att != null && att.getEntries().length > 0)
		{
			MAttachmentEntry entry = att.getEntries()[0];
			uploadedFileName = entry.getName();
			uploadedFileBytes = entry.getData();
		}
		else
		{
			uploadedFileName = "";
			uploadedFileBytes = null;
		}
	}

	private void initDefaultIntakeRow()
	{
		intakeRows.clear();
		IntakePlanRow row = new IntakePlanRow();
		row.setLineNo(1);
		row.setTradeName(selectedProgramTitle);
		row.setNumberOfLearners(allocatedLearners);
		row.setIntakeDate(new Date());
		intakeRows.add(row);
		calculateTotalLearners();
	}

	private String resolveProgramTitle(int programMasterDataId)
	{
		if (programMasterDataId <= 0)
			return "";

		X_ZZ_Program_Master_Data prog = new X_ZZ_Program_Master_Data(Env.getCtx(), programMasterDataId, null);
		if (prog.get_ID() > 0)
		{
			String title = prog.getTitle();
			if (title != null && !title.isBlank())
				return title;
			return prog.getName() != null ? prog.getName() : "";
		}
		return "";
	}

	public String getIntakeNote()
	{
		String prog = (selectedProgramTitle != null && !selectedProgramTitle.isBlank()) ? selectedProgramTitle : "artisan development";
		return "The intake plan should reflect the total number of " + prog + " learners allocated for the "
				+ financialYearString + " financial year by your company. In a case that the company is aware that they will not be registering the total number of learners as per the allocation, the intake plan must ONLY reflect the exact number of learners to be registered by the company for "
				+ financialYearString + " financial year.";
	}

	@Command
	@NotifyChange({"offerDecision"})
	public void onDecisionChanged()
	{
		// ZK @NotifyChange updates the binding automatically
	}

	@Command
	@NotifyChange({"intakeRows", "totalLearners"})
	public void addIntakeRow()
	{
		IntakePlanRow row = new IntakePlanRow();
		row.setLineNo(intakeRows.size() + 1);
		row.setTradeName("");
		row.setNumberOfLearners(0);
		row.setIntakeDate(new Date());
		intakeRows.add(row);
		calculateTotalLearners();
	}

	@Command
	@NotifyChange({"intakeRows", "totalLearners"})
	public void removeIntakeRow(@BindingParam("row") IntakePlanRow row)
	{
		if (intakeRows.size() <= 1)
		{
			MasterUtil.showInfoDialog("At least one intake row is required.", null);
			return;
		}
		intakeRows.remove(row);
		resequenceRows();
		calculateTotalLearners();
	}

	private void resequenceRows()
	{
		int idx = 1;
		for (IntakePlanRow r : intakeRows)
		{
			r.setLineNo(idx++);
		}
	}

	@Command
	@NotifyChange({"totalLearners"})
	public void onLearnersChanged()
	{
		calculateTotalLearners();
	}

	private void calculateTotalLearners()
	{
		totalLearners = 0;
		for (IntakePlanRow r : intakeRows)
		{
			totalLearners += r.getNumberOfLearners();
		}
	}

	public String getUploadSetting()
	{
		return AdempiereWebUI.getUploadSetting() + ",multiple=false";
	}

	@Command
	@NotifyChange({"uploadedFileName"})
	public void onUploadDocument(@BindingParam("media") Media mediaParam,
			@ContextParam(ContextType.TRIGGER_EVENT) UploadEvent event)
	{
		Media media = mediaParam;
		if (media == null && event != null)
		{
			media = event.getMedia();
			if (media == null && event.getMedias() != null && event.getMedias().length > 0)
			{
				media = event.getMedias()[0];
			}
		}

		if (media == null)
		{
			log.warning("onUploadDocument: No media provided");
			return;
		}

		uploadedFileName = media.getName();
		try
		{
			byte[] data = null;
			if (media.inMemory() && media.isBinary())
			{
				data = media.getByteData();
			}
			if (data == null)
			{
				try (InputStream is = media.getStreamData())
				{
					if (is != null)
					{
						data = IOUtils.toByteArray(is);
					}
				}
			}
			if (data == null && media.getStringData() != null)
			{
				data = media.getStringData().getBytes(StandardCharsets.UTF_8);
			}

			uploadedFileBytes = data;
			log.info("Uploaded SLA document: " + uploadedFileName + " (" + (uploadedFileBytes != null ? uploadedFileBytes.length : 0) + " bytes)");
		}
		catch (Exception ex)
		{
			log.severe("Failed to read uploaded media: " + ex.getMessage());
			MasterUtil.showInfoDialog("Error uploading file: " + ex.getMessage(), null);
		}
	}

	@Command
	@NotifyChange({"uploadedFileName"})
	public void removeUploadedDocument()
	{
		uploadedFileName = "";
		uploadedFileBytes = null;
	}

	@Command
	public void submitDecline()
	{
		if (selectedApplication == null)
		{
			MasterUtil.showInfoDialog("No allocation offer is selected.", null);
			return;
		}

		if (declineReason == null || declineReason.trim().isEmpty())
		{
			MasterUtil.showInfoDialog("Please enter a mandatory reason for declining the offer.", null);
			return;
		}

		saveOfferRecord(X_ZZ_Allocation_Offer.ZZOFFERDECISION_Declined, X_ZZ_Allocation_Offer.ZZ_DOCSTATUS_Query);

		MasterUtil.showInfoDialog("Allocation offer declined. Your reason has been submitted to MQA.", MasterUtil.fCloseActiveWindow);
	}

	@Command
	public void submitAccept()
	{
		if (!validateAccept())
			return;

		saveOfferRecord(X_ZZ_Allocation_Offer.ZZOFFERDECISION_Accept, X_ZZ_Allocation_Offer.ZZ_DOCSTATUS_Submitted);

		sendEmailConfirmation();

		String docNo = existingOffer != null ? existingOffer.getDocumentNo() : "";
		MasterUtil.showInfoDialog("Intake Plan submitted successfully! Reference Number: " + docNo, MasterUtil.fCloseActiveWindow);
	}

	@Command
	public void saveDraft()
	{
		if (selectedApplication == null)
		{
			MasterUtil.showInfoDialog("No allocation offer is selected.", null);
			return;
		}

		saveOfferRecord(offerDecision, X_ZZ_Allocation_Offer.ZZ_DOCSTATUS_Draft);
		MasterUtil.showInfoDialog("Intake Plan saved as draft.", null);
	}

	private boolean validateAccept()
	{
		if (selectedApplication == null)
		{
			MasterUtil.showInfoDialog("No allocation offer is selected.", null);
			return false;
		}

		if (uploadedFileName.isEmpty() && uploadedFileBytes == null)
		{
			MasterUtil.showInfoDialog("Please upload the signed Allocation Offer / SLA document.", null);
			return false;
		}

		if (intakeRows.isEmpty())
		{
			MasterUtil.showInfoDialog("Please provide at least one intake line.", null);
			return false;
		}

		LocalDate fyStart = getFinancialYearStartDate();
		LocalDate fyEnd = getFinancialYearEndDate();

		for (IntakePlanRow row : intakeRows)
		{
			if (row.getTradeName() == null || row.getTradeName().trim().isEmpty())
			{
				MasterUtil.showInfoDialog("Line " + row.getLineNo() + ": Qualification / Trade name cannot be blank.", null);
				return false;
			}
			if (row.getNumberOfLearners() <= 0)
			{
				MasterUtil.showInfoDialog("Line " + row.getLineNo() + ": Number of learners must be greater than zero.", null);
				return false;
			}
			if (row.getIntakeDate() == null)
			{
				MasterUtil.showInfoDialog("Line " + row.getLineNo() + ": Intake date is mandatory.", null);
				return false;
			}

			LocalDate date = Instant.ofEpochMilli(row.getIntakeDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
			if (date.isBefore(fyStart) || date.isAfter(fyEnd))
			{
				MasterUtil.showInfoDialog("Line " + row.getLineNo() + ": Intake date must fall within the current financial year ("
						+ fyStart + " to " + fyEnd + ").", null);
				return false;
			}
		}

		if (totalLearners > allocatedLearners)
		{
			MasterUtil.showInfoDialog("Total number of learners (" + totalLearners
					+ ") cannot exceed the total allocated learners (" + allocatedLearners + ").", null);
			return false;
		}

		return true;
	}

	private void saveOfferRecord(String decision, String docStatus)
	{
		Trx.run(trxName -> {
			if (existingOffer == null)
			{
				existingOffer = new X_ZZ_Allocation_Offer(Env.getCtx(), 0, trxName);
				int orgId = selectedApplication != null && selectedApplication.getAD_Org_ID() > 0
						? selectedApplication.getAD_Org_ID() : Env.getAD_Org_ID(Env.getCtx());
				existingOffer.setAD_Org_ID(orgId);
			}
			else
			{
				existingOffer.set_TrxName(trxName);
			}

			int bpId = currentOrg != null ? currentOrg.getC_BPartner_ID() : (selectedApplication != null ? selectedApplication.getC_BPartner_ID() : 0);
			int progId = selectedApplication != null ? selectedApplication.getZZ_Program_Master_Data_ID() : 0;
			int appId = selectedApplication != null ? selectedApplication.getZZ_Application_Form_ID() : 0;

			existingOffer.setC_BPartner_ID(bpId);
			existingOffer.setZZ_Program_Master_Data_ID(progId);
			existingOffer.setZZ_Application_Form_ID(appId);
			existingOffer.setZZAllocatedLearners(allocatedLearners);
			existingOffer.setZZAcceptedLearners(X_ZZ_Allocation_Offer.ZZOFFERDECISION_Accept.equals(decision) ? totalLearners : 0);
			existingOffer.setZZOfferDecision(decision);
			existingOffer.setZZ_Reason(declineReason);
			existingOffer.setZZ_DocStatus(docStatus);
			existingOffer.setDateDoc(new Timestamp(System.currentTimeMillis()));
			existingOffer.setAD_User_ID(Env.getAD_User_ID(Env.getCtx()));

			existingOffer.saveEx(trxName);

			// Save or update uploaded SLA attachment
			if (uploadedFileBytes != null && uploadedFileBytes.length > 0)
			{
				AttachmentUtil.addOrReplaceAttachmentEntry(existingOffer, uploadedFileName, uploadedFileBytes, ATTACHMENT_TITLE_SIGNED_SLA, trxName);
			}
			else
			{
				AttachmentUtil.removeAttachmentEntry(existingOffer, ATTACHMENT_TITLE_SIGNED_SLA, trxName);
			}

			// Save child intake rows if accepted
			if (X_ZZ_Allocation_Offer.ZZOFFERDECISION_Accept.equals(decision))
			{
				DB.executeUpdateEx("DELETE FROM " + I_ZZ_Allocation_Intake_Plan.Table_Name
						+ " WHERE " + I_ZZ_Allocation_Intake_Plan.COLUMNNAME_ZZ_Allocation_Offer_ID + " = ?",
						new Object[]{existingOffer.getZZ_Allocation_Offer_ID()}, trxName);

				for (IntakePlanRow row : intakeRows)
				{
					X_ZZ_Allocation_Intake_Plan line = new X_ZZ_Allocation_Intake_Plan(Env.getCtx(), 0, trxName);
					line.setAD_Org_ID(existingOffer.getAD_Org_ID());
					line.setZZ_Allocation_Offer_ID(existingOffer.getZZ_Allocation_Offer_ID());
					line.setLineNo(row.getLineNo());
					line.setZZTradeName(row.getTradeName() != null ? row.getTradeName().trim() : "");
					line.setZZNoLearners(row.getNumberOfLearners());
					if (row.getIntakeDate() != null)
					{
						line.setZZIntakeDate(new Timestamp(row.getIntakeDate().getTime()));
					}
					line.saveEx(trxName);
				}
			}
		});

		if (existingOffer != null)
		{
			existingOffer.set_TrxName(null);
			currentDocStatus = existingOffer.getZZ_DocStatus();
			inQueryState = X_ZZ_Allocation_Offer.ZZ_DOCSTATUS_Query.equals(currentDocStatus);
		}
	}

	private void sendEmailConfirmation()
	{
		try
		{
			int loginId = Env.getAD_User_ID(Env.getCtx());
			MUser receiver = MUser.get(loginId);
			if (receiver == null || receiver.getEMail() == null || receiver.getEMail().isBlank())
				return;

			MClient client = MClient.get(Env.getCtx());
			int fromUserId = MSysConfig.getIntValue("FROM_EMAIL_USER_ID", 1000011);
			MUser from = MUser.get(Env.getCtx(), fromUserId);

			String subject = "MQA Allocation Offer & Intake Plan Confirmation - " + existingOffer.getDocumentNo();
			StringBuilder body = new StringBuilder();
			body.append("Dear ").append(receiver.getName()).append(",\n\n")
				.append("Your signed Allocation Offer and Intake Plan has been successfully submitted to MQA.\n\n")
				.append("Details:\n")
				.append("- Reference Number: ").append(existingOffer.getDocumentNo()).append("\n")
				.append("- Organisation: ").append(orgName).append("\n")
				.append("- Programme: ").append(selectedProgramTitle).append("\n")
				.append("- Total Learners Accepted: ").append(totalLearners).append(" of ").append(allocatedLearners).append(" allocated\n\n")
				.append("Intake Schedule:\n");

			for (IntakePlanRow row : intakeRows)
			{
				body.append(String.format("  Line %d: %s | Learners: %d | Intake Date: %tF\n",
						row.getLineNo(), row.getTradeName(), row.getNumberOfLearners(), row.getIntakeDate()));
			}
			body.append("\nYour submission will now be reviewed by the MQA LP Administration team.\n\nRegards,\nMQA Discretionary Grants");

			client.sendEMail(from, receiver, subject, body.toString(), null, false);
		}
		catch (Exception ex)
		{
			log.warning("Could not send confirmation email: " + ex.getMessage());
		}
	}

	// --- Bean for Intake Plan Line ---

	/**
	 * Serializable DTO representing a single line in the intake plan grid.
	 */
	public static class IntakePlanRow implements Serializable
	{
		private static final long serialVersionUID = 1L;

		private int lineNo;
		private String tradeName = "";
		private int numberOfLearners = 0;
		private Date intakeDate;

		public int getLineNo()
		{
			return lineNo;
		}

		public String getLineNoDisplay()
		{
			return lineNo + ".";
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
			this.tradeName = tradeName;
		}

		public int getNumberOfLearners()
		{
			return numberOfLearners;
		}

		public void setNumberOfLearners(int numberOfLearners)
		{
			this.numberOfLearners = numberOfLearners;
		}

		public Date getIntakeDate()
		{
			return intakeDate;
		}

		public void setIntakeDate(Date intakeDate)
		{
			this.intakeDate = intakeDate;
		}
	}

	// --- Getters & Setters for ZUL Binding ---

	public String getOrgName()
	{
		return orgName;
	}

	public String getSdlNo()
	{
		return sdlNo;
	}

	public List<X_ZZ_Application_Form> getAvailableAllocations()
	{
		return availableAllocations;
	}

	public X_ZZ_Application_Form getSelectedApplication()
	{
		return selectedApplication;
	}

	public String getSelectedProgramTitle()
	{
		return selectedProgramTitle;
	}

	public int getAllocatedLearners()
	{
		return allocatedLearners;
	}

	public String getOfferDecision()
	{
		return offerDecision;
	}

	public void setOfferDecision(String offerDecision)
	{
		this.offerDecision = offerDecision;
	}

	public String getDeclineReason()
	{
		return declineReason;
	}

	public void setDeclineReason(String declineReason)
	{
		this.declineReason = declineReason;
	}

	public List<IntakePlanRow> getIntakeRows()
	{
		return intakeRows;
	}

	public int getTotalLearners()
	{
		return totalLearners;
	}

	public String getUploadedFileName()
	{
		return uploadedFileName;
	}

	public boolean isInQueryState()
	{
		return inQueryState;
	}

	public List<String> getTradeSuggestions()
	{
		return tradeSuggestions;
	}

	public boolean isHasMultipleAllocations()
	{
		return availableAllocations != null && availableAllocations.size() > 1;
	}

	public String getProgramDisplayName()
	{
		if (selectedApplication == null)
			return "";
		String docNo = selectedApplication.getDocumentNo() != null ? selectedApplication.getDocumentNo() : "";
		String name = (orgName != null && !orgName.isBlank()) ? orgName
				: (selectedApplication.getOrgName() != null ? selectedApplication.getOrgName() : "");
		return docNo + (name.isEmpty() ? "" : " - " + name);
	}
}
