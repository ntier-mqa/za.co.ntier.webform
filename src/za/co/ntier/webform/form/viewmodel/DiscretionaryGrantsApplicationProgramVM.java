package za.co.ntier.webform.form.viewmodel;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.adempiere.model.GenericPO;
import org.adempiere.model.POWrapper;
import org.adempiere.webui.desktop.DefaultDesktop;
import org.adempiere.webui.session.SessionManager;
import org.apache.commons.lang3.StringUtils;
import org.compiere.model.I_C_Year;
import org.compiere.model.MBPartner;
import org.compiere.model.MClient;
import org.compiere.model.MMailText;
import org.compiere.model.MSysConfig;
import org.compiere.model.MTable;
import org.compiere.model.MUser;
import org.compiere.model.MYear;
import org.compiere.model.Query;
import org.compiere.model.X_AD_User;
import org.compiere.model.X_C_BPartner;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.DependsOn;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Tabbox;

import za.co.ntier.api.model.I_C_BPartner;
import za.co.ntier.api.model.I_ZZ_Application_Form;
import za.co.ntier.api.model.I_ZZ_Levy_Paying;
import za.co.ntier.api.model.I_ZZ_WSP_ATR_Approvals;
import za.co.ntier.api.model.X_ZZDocumentUpload;
import za.co.ntier.api.model.X_ZZ_Application_Form;
import za.co.ntier.api.model.X_ZZ_Program_Master_Data;
import za.co.ntier.webform.form.AbstractProgram;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.form.bean.DataType;
import za.co.ntier.webform.form.bean.ProgramType;
import za.co.ntier.webform.form.bean.component.AddressInfo;
import za.co.ntier.webform.form.bean.component.AnnexureRow;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.Dialog;
import za.co.ntier.webform.form.bean.component.EmployerDeclarationInfo;
import za.co.ntier.webform.form.bean.component.FormInfo;
import za.co.ntier.webform.form.bean.component.OrganisationInfo;
import za.co.ntier.webform.form.bean.component.UploadData;
import za.co.ntier.webform.form.bean.component.UploadDocComponent;
import za.co.ntier.webform.form.bean.component.UploadInput;
import za.co.ntier.webform.form.bean.program.AetProgram;
import za.co.ntier.webform.form.bean.program.ArtisanAidesProgram;
import za.co.ntier.webform.form.bean.program.ArtisanDevProgram;
import za.co.ntier.webform.form.bean.program.ArtisanRPLProgram;
import za.co.ntier.webform.form.bean.program.CandidacyProgram;
import za.co.ntier.webform.form.bean.program.CentreOfSpecialisationProgram;
import za.co.ntier.webform.form.bean.program.CetTvetProgram;
import za.co.ntier.webform.form.bean.program.HetLectureSupport;
import za.co.ntier.webform.form.bean.program.InhouseTrainingProgram;
import za.co.ntier.webform.form.bean.program.InternshipProgram;
import za.co.ntier.webform.form.bean.program.LearningMaterialsDevelopment;
import za.co.ntier.webform.form.bean.program.MedpProgram;
import za.co.ntier.webform.form.bean.program.MiningCommunityUnemployedYouthProgram;
import za.co.ntier.webform.form.bean.program.MultiyearPartnershipInternship;
import za.co.ntier.webform.form.bean.program.MultiyearPartnershipWorkExperience;
import za.co.ntier.webform.form.bean.program.NcvGraduatesProgram;
import za.co.ntier.webform.form.bean.program.NonArtisanDevProgram;
import za.co.ntier.webform.form.bean.program.NonArtisanDevRPLProgram;
import za.co.ntier.webform.form.bean.program.OhasspProgram;
import za.co.ntier.webform.form.bean.program.SmallBusiness;
import za.co.ntier.webform.form.bean.program.StandardSetting;
import za.co.ntier.webform.form.bean.program.WorkExperienceProgram;
import za.co.ntier.webform.form.bean.program.WorkerInitiatedTraining;
import za.co.ntier.webform.form.bean.program.WorkplaceCoachesProgram;
import za.co.ntier.webform.form.viewmodel.component.ComponentVMWrapper;

public class DiscretionaryGrantsApplicationProgramVM {
	public class EmailPoInfo extends GenericPO {

		private static final long serialVersionUID = -433026634223871908L;

		public EmailPoInfo() {
			super(MUser.Table_Name, Env.getCtx(), 0);
		}

		public String getAppFormDocno(){
			return applicationForm.getDocumentNo();
		}

		public String getAppFormSubmitedDate() {
			return applicationForm.getUpdated().toLocalDateTime().truncatedTo(ChronoUnit.SECONDS).format(dtf);
		}

		public String getAppFormTitle(){
			return menuContextInfo.getProgramMasterData().getTitle();
		}

	}

	public static DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

	public static final int EMPLOYER_APP_AD_FORM_ID = 1000000; // your WebForm AD_Form_ID

	public static final int FROM_EMAIL_USER_ID = MSysConfig.getIntValue("FROM_EMAIL_USER_ID",1000011);

	private static final CLogger log = CLogger.getCLogger(DiscretionaryGrantsApplicationProgramVM.class);

	public static void showAppList(){
		// 2) close the CURRENT tab first
		DefaultDesktop desktop = (DefaultDesktop) SessionManager.getAppDesktop();
		desktop.closeActiveWindow();  // <— closes the active AD Form tab

		// Open ApplicationsList.zul via WebForm context
		String ctx = ""
				+ "zulPath=/za/co/ntier/webform/zul/program/ApplicationsList.zul\n"
				+ "formTitle=Applications\n"
				+ "ZZ_Program_Master_Data_UU=a3db65ee-97d9-429d-9734-aca9e89dd3af\n"
				+ "programType=UNKNOWN\n";


		desktop.setPredefinedContextVariables(ctx);
		desktop.openForm(EMPLOYER_APP_AD_FORM_ID);
	}

	private AddressInfo alternateProgramContact;

	private X_ZZ_Application_Form applicationForm;

	private String documentNo;
	private EmployerDeclarationInfo employerDeclarationInfo;

	// --- Getters and Setters for Data Binding ---

	private FormInfo formInfo;

	private MenuContextInfo menuContextInfo;

	private OrganisationInfo organisationInfo;

	private AbstractProgram program;

	private AddressInfo programContact;

	private ProgramType programType;

	private int recordId;

	private int tableId;

	private UploadDocComponent uploadDoc;

	private int selectedTabIndex; // defaults to 0

	public void deleteApp() {
		if (applicationForm != null) {
			try {
				applicationForm.setIsActive(false);
				applicationForm.saveEx(null);
				showAppList();
			}catch (Exception e) {
				Clients.showNotification(
						"Can't Delete application form:" + e.getMessage(),
						"error", null, "end_center", 0, true);
			}
		}else {
			showAppList();
		}

	}

	/**
	 * @return the alternateProgramContact
	 */
	public AddressInfo getAlternateProgramContact() {
		return alternateProgramContact;
	}

	/**
	 * @return the sdf
	 */
	public X_ZZ_Application_Form getApplicationForm() {
		return applicationForm;
	}


	/**
	 * @return the documentNo
	 */
	public String getDocumentNo() {
		return documentNo;
	}

	public EmployerDeclarationInfo getEmployerDeclarationInfo() {
		return employerDeclarationInfo;
	}

	/**
	 * @return the formInfo
	 */
	public FormInfo getFormInfo() {
		return formInfo;
	}

	/**
	 * @return the programMasterDataID
	 */
	public MenuContextInfo getMenuContextInfo() {
		return menuContextInfo;
	}

	/**
	 * @return the organisationInfo
	 */
	public OrganisationInfo getOrganisationInfo() {
		return organisationInfo;
	}

	/**
	 * @return the program
	 */
	public AbstractProgram getProgram() {
		return program;
	}


	/**
	 * @return the programContact
	 */
	public AddressInfo getProgramContact() {
		return programContact;
	}

	public ProgramType getProgramType() {
		return programType;
	}

	public int getRecordId() {
		return recordId;
	}

	public String getSubmitButtonLabel() {
		return "Submit Application";
	}

	public int getTableId() {
		return tableId;
	}

	/**
	 * @return the uploadDoc
	 */
	public UploadDocComponent getUploadDoc() {
		return uploadDoc;
	}

	@Init
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo){

		setMenuContextInfo(menuContextInfo);
		programType = menuContextInfo.getProgramType();

		if (StringUtils.isNotBlank(menuContextInfo.getApplicationFormUU())) {
			applicationForm = new X_ZZ_Application_Form(Env.getCtx(), menuContextInfo.getApplicationFormUU(), null);
			if (!applicationForm.isActive()) {
				showDialog("Deleted Application Form", "This application form is deleted");
			}

		}

		employerDeclarationInfo = new EmployerDeclarationInfo();
		employerDeclarationInfo.initComponent(applicationForm);

		formInfo = new FormInfo(menuContextInfo);

		organisationInfo = new OrganisationInfo(menuContextInfo, this);
		organisationInfo.initComponent(applicationForm);

		if (programType.isCetTvet()) {
			setProgram(new CetTvetProgram(menuContextInfo, applicationForm));
		} else if (ProgramType.INTERNSHIP == programType) {
			setProgram(new InternshipProgram(menuContextInfo, applicationForm));
		} else if (ProgramType.CANDIDACY == programType) {
			setProgram(new CandidacyProgram(menuContextInfo, applicationForm));
		} else if (ProgramType.EXPERIENCE == programType) {
			setProgram(new WorkExperienceProgram(menuContextInfo, applicationForm));
		} else if (ProgramType.DEV_PROGRAM == programType) {
			setProgram(new MedpProgram(menuContextInfo, applicationForm));
		} else if (ProgramType.ARTISAN_AIDES == programType) {
			setProgram(new ArtisanAidesProgram(menuContextInfo, applicationForm));
		} else if (ProgramType.ARTISAN_DEV == programType) {
			setProgram(new ArtisanDevProgram(menuContextInfo, applicationForm));
		} else if (ProgramType.CENTRE_SPECIALISATION == programType) {
			setProgram(new CentreOfSpecialisationProgram(menuContextInfo, applicationForm));
		} else if (ProgramType.ARTISAN_RPL == programType) {
			setProgram(new ArtisanRPLProgram(menuContextInfo, applicationForm));
		} else if (ProgramType.NON_ARTISAN_DEV == programType) {
			setProgram(new NonArtisanDevProgram(menuContextInfo, applicationForm));
		} else if (ProgramType.NON_ARTISAN_DEV_RPL == programType) {
			setProgram(new NonArtisanDevRPLProgram(menuContextInfo, applicationForm));
		} else if (ProgramType.NCV_GRADUATES == programType) {
			setProgram(new NcvGraduatesProgram(menuContextInfo, applicationForm));
		} else if (ProgramType.AET == programType) {
			setProgram(new AetProgram(menuContextInfo, applicationForm));
		} else if (ProgramType.OHASSP == programType) {
			setProgram(new OhasspProgram(menuContextInfo, applicationForm));
		} else if (ProgramType.INHOUSE_TRAINING == programType) {
			setProgram(new InhouseTrainingProgram(menuContextInfo, applicationForm));
		} else if (ProgramType.WORKPLACE_COACHES == programType) {
			setProgram(new WorkplaceCoachesProgram(menuContextInfo, applicationForm));
		}else if (ProgramType.MINING_COMMUNITY == programType
				|| ProgramType.UNEMPLOYED_YOUTH == programType
				|| ProgramType.SMALL_SCALE_MINING == programType) {
			program = new MiningCommunityUnemployedYouthProgram(menuContextInfo, applicationForm);
		}else if (ProgramType.HET_LECTURE_SUPPORT == programType) {
			program = new HetLectureSupport(menuContextInfo, applicationForm);
		}else if (ProgramType.WORKER_INITIATED_TRAINING == programType) {
			program = new WorkerInitiatedTraining(menuContextInfo, applicationForm);
		}else if (ProgramType.MULTIYEAR_PARTNERSHIP_INTERNSHIP == programType) {
			program = new MultiyearPartnershipInternship(menuContextInfo, applicationForm);
		}else if (ProgramType.MULTIYEAR_PARTNERSHIP_WORK_EXPERIENCE == programType) {
			program = new MultiyearPartnershipWorkExperience(menuContextInfo, applicationForm);
		}else if (ProgramType.STANDARD_SETTING == programType) {
			program = new StandardSetting(menuContextInfo, applicationForm);
		}else if (ProgramType.SMALL_BUSINESS == programType) {
			program = new SmallBusiness(menuContextInfo, applicationForm);
		}else if (ProgramType.LEARNING_MATERIALS_DEVELOPMENT == programType) {
			program = new LearningMaterialsDevelopment(menuContextInfo, applicationForm);
		}



		// main contact
		if (programType.isShowMainAddress()) {
			programContact = new AddressInfo(programType, false);
			programContact.initComponent(applicationForm);
		}


		// main alternate contact
		if (programType.isShowMainAddressAlter()) {
			alternateProgramContact = new AddressInfo(programType, true);
			alternateProgramContact.initComponent(applicationForm);
		}


		uploadDoc = new UploadDocComponent(menuContextInfo, applicationForm);

		org.zkoss.bind.BindUtils.postNotifyChange(null, null, this, "declarationComplete");
		org.zkoss.bind.BindUtils.postNotifyChange(null, null, this, "organisationComplete");
		org.zkoss.bind.BindUtils.postNotifyChange(null, null, this, "programComplete");
		org.zkoss.bind.BindUtils.postNotifyChange(null, null, this, "programContactComplete");

	}

	private boolean isAddressBlockComplete(AddressInfo a) {
		boolean siteOk   = !a.showSiteName()        || notEmpty(a.getSiteName());
		boolean lineOk   = !a.showLineAddress()     || notEmpty(a.getAddressLine());
		boolean postOk   = !a.showPostalAddress()   ||  notEmpty(a.getAddressLine()); //notEmpty(a.getPostAddress());
		boolean geoOk    = !a.showGeographicAddress() ||
				(notEmpty(a.getPostalCode())
						&& a.getAreaSelected() != null
						&& a.getProvinceSelected() != null);
		boolean contactOk = !a.showContact() || (
				notEmpty(a.getNameSiteRepresentative()) &&
				notEmpty(a.getRepresentativeDesignation()) &&
				isTenDigits(a.getMobileNumber()) &&
				isTenDigits(a.getLandlineNumber()) &&
				isEmail(a.getEmail())
				);
		return siteOk && lineOk && postOk && geoOk && contactOk;
	}

	@DependsOn("employerDeclarationInfo.acknowledged")
	public boolean isDeclarationComplete() {
		return employerDeclarationInfo != null && Boolean.TRUE.equals(employerDeclarationInfo.getAcknowledged());
	}

	private boolean isEmail(String s)     { return s != null && s.matches("^[^@\\s]+@[^@\\s]+\\.[A-Za-z]{2,}$"); }


	@DependsOn({
		"programType",
		"organisationInfo.physicalAddressInfo.areaSelected",
		"organisationInfo.physicalAddressInfo.provinceSelected",
		"organisationInfo.postAddressInfo.areaSelected",
		"organisationInfo.postAddressInfo.provinceSelected",
		"organisationInfo.orgName",
		"organisationInfo.orgRegistrationNumber",
		"organisationInfo.orgTaxNumber",
		"organisationInfo.physicalAddressInfo.addressLine",
		"organisationInfo.physicalAddressInfo.postalCode",
		"organisationInfo.cetTvetCollegeSelected",
		"organisationInfo.sdlNumber",
		"organisationInfo.siteSDLNumber",
		"organisationInfo.postAddressInfo.postalCode",
		"organisationInfo.orgSizeInfo.numOfEmployer",
		"organisationInfo.orgContact.nameSiteRepresentative",
		"organisationInfo.orgContact.representativeDesignation",
		"organisationInfo.orgContact.mobileNumber",
		"organisationInfo.orgContact.landlineNumber",
		"organisationInfo.orgContact.email",
		"organisationInfo.alternateOrgContact.nameSiteRepresentative",
		"organisationInfo.alternateOrgContact.representativeDesignation",
		"organisationInfo.alternateOrgContact.mobileNumber",
		"organisationInfo.alternateOrgContact.landlineNumber",
		"organisationInfo.alternateOrgContact.email"
	})
	public boolean isOrganisationComplete() {
		final boolean isStandard = ProgramType.STANDARD_SETTING.equals(programType);
		final boolean isCetTvet  = programType.isCetTvet();

		// --- Organisation basics (only when not STANDARD) ---
		if (!isStandard) {
			if (!isCetTvet) {
				Integer numEmployers = getNumOfEmployers(organisationInfo);
				if (!(notEmpty(organisationInfo.getOrgTaxNumber())
						&& notEmpty(organisationInfo.getOrgRegistrationNumber())
						&& numEmployers != null)) { // 0 allowed, only null disallowed
					return false;
				}
			}

			// SDL + Site SDL always required when not STANDARD
			if (!(notEmpty(organisationInfo.getSdlNumber())
					&& notEmpty(organisationInfo.getSiteSDLNumber()))) {
				return false;
			}
		}

		// --- Org name vs. College selection ---
		if (!organisationInfo.isUseBpartner()) {
			// Org name required only when not STANDARD
			if (!isStandard && !notEmpty(organisationInfo.getOrgName())) {
				return false;
			}
		} else {
			// College selection required whenever using BP
			if (organisationInfo.getCetTvetCollegeSelected() == null) {
				return false;
			}
		}

		// --- Physical address checks ---
		if (!validatePhysicalAddress(organisationInfo.getPhysicalAddressInfo())) {
			return false;
		}

		// --- Postal address checks ---
		if (!validatePostalAddress(organisationInfo.getPostAddressInfo())) {
			return false;
		}

		// --- Contacts (orgContact only when not STANDARD; alternate always "if shown") ---
		if (isShowContact() && !validateContactIfShown(organisationInfo.getOrgContact())) {
			return false;
		}
		if (isShowContact() && !validateContactIfShown(organisationInfo.getAlternateOrgContact())) {
			return false;
		}

		return true;
	}

	public Boolean isMandatoryDocsUploaded() {

	    // If there is no upload section configured, nothing to enforce
	    if (uploadDoc == null || uploadDoc.getUploadDoc() == null) {
	        return true;
	    }

	    // This is the UploadInput that backs annexureTable.zul
	    UploadInput uploadInput = uploadDoc.getUploadDoc();

	    ColumnInfo<?> uploadDefCol  = UploadInput.lookupColByDataType(DataType.DocUploadDef, uploadInput);
	    ColumnInfo<?> uploadFileCol = UploadInput.lookupColByDataType(DataType.FileUpload,  uploadInput);

	    if (uploadDefCol == null || uploadFileCol == null) {
	        // Misconfigured, be defensive
	        return true;
	    }

	    for (Map<ColumnInfo<?>, Object> rowObj : uploadInput.getRows()) {
	        AnnexureRow row = (AnnexureRow) rowObj;

	        X_ZZDocumentUpload docDef = (X_ZZDocumentUpload) row.get(uploadDefCol);
	        if (docDef == null || !docDef.isMandatory()) {
	            continue; // only enforce mandatory docs
	        }

	        // This is whatever object backs row[detailCol] for DataType.FileUpload
	        Object cell = row.get(uploadFileCol);
	        if (!(cell instanceof UploadData)) {
	            // No upload object at all -> fail
	            return false;
	        }

	        UploadData uploadData = (UploadData) cell;

	        // annexureTable.zul binds label to row[detailCol].fileName
	        String fileName = uploadData.getFileName();

	        if (fileName == null || fileName.trim().isEmpty() ) {
	            // Mandatory doc without a real file
	            return false;
	        }
	    }

	    // All mandatory docs have some file in the in-memory model
	    return true;
	}



	/* -------------------- Helpers -------------------- */

	private static Integer getNumOfEmployers(OrganisationInfo info) {
		return (info.getOrgSizeInfo() != null) ? info.getOrgSizeInfo().getNumOfEmployer() : null;
	}

	private boolean validatePhysicalAddress(AddressInfo phys) {
		if (phys == null) return true; // nothing to validate
		boolean lineOk = !phys.showLineAddress() || notEmpty(phys.getAddressLine());
		boolean geoOk  = !phys.showGeographicAddress()
				|| (notEmpty(phys.getPostalCode())
						&& phys.getAreaSelected() != null
						&& phys.getProvinceSelected() != null);
		return lineOk && geoOk;
	}

	private boolean validatePostalAddress(AddressInfo post) {
		if (post == null) return true; // nothing to validate
		boolean lineOk = !post.showPostalAddress() || notEmpty(post.getAddressLine());
		boolean geoOk  = !post.showGeographicAddress()
				|| (notEmpty(post.getPostalCode())
						&& post.getAreaSelected() != null
						&& post.getProvinceSelected() != null);
		return lineOk && geoOk;
	}

	private boolean validateContactIfShown(AddressInfo contact) {
		if (contact == null || !contact.showContact()) return true;
		return notEmpty(contact.getNameSiteRepresentative())
				&& notEmpty(contact.getRepresentativeDesignation())
				&& notEmpty(contact.getMobileNumber())
				&& notEmpty(contact.getLandlineNumber())
				&& isEmail(contact.getEmail());
	}




	@DependsOn("program") // re-evaluate when program/annexure rows change (notified from table VM)
	public boolean isProgramComplete() {
		if (program != null) {
			return program.isProgramValid();
		}
		return true;//app list
	}



	@DependsOn({
		"programType",

		// MAIN contact
		"programContact.siteName",
		"programContact.addressLine",
		//  "programContact.postAddress",
		"programContact.postalCode",
		"programContact.areaSelected",
		"programContact.provinceSelected",
		"programContact.nameSiteRepresentative",
		"programContact.representativeDesignation",
		"programContact.mobileNumber",
		"programContact.landlineNumber",
		"programContact.email",

		// ALTERNATE contact (only enforced if shown)
		"alternateProgramContact.siteName",
		"alternateProgramContact.addressLine",
		//  "alternateProgramContact.postAddress",
		"alternateProgramContact.postalCode",
		"alternateProgramContact.areaSelected",
		"alternateProgramContact.provinceSelected",
		"alternateProgramContact.nameSiteRepresentative",
		"alternateProgramContact.representativeDesignation",
		"alternateProgramContact.mobileNumber",
		"alternateProgramContact.landlineNumber",
		"alternateProgramContact.email"
	})
	public boolean isProgramContactComplete() {
		if (!programType.isShowMainAddress()) return true;

		boolean mainOk = isAddressBlockComplete(programContact);

		boolean altOk = true;
		if (programType.isShowMainAddressAlter()) {
			altOk = isAddressBlockComplete(alternateProgramContact);
		}

		return mainOk && altOk;
	}

	public boolean isShowUploadDoc() {
		return uploadDoc != null && uploadDoc.getUploadDoc().getRows().size() > 0;
	}

	public boolean isShowContact() {
		return programType != ProgramType.STANDARD_SETTING; // && programType != ProgramType.DEV_PROGRAM;
	}

	public String getOrganisationTabTile () {
		if (programType == ProgramType.STANDARD_SETTING)
			return "Individual Information";
		else
			return "Organisation Information";
	}

	private boolean isTenDigits(String s) { return s != null && s.matches("^\\d{10}$"); }

	public void nextTab(Tabbox tab) {

		int currentIndex = tab.getSelectedIndex();
		tab.setSelectedIndex(currentIndex + 1);
	}

	private boolean notEmpty(String s){ return s != null && !s.trim().isEmpty(); }

	public void prevTab(Tabbox tab) {
		int currentIndex = tab.getSelectedIndex();
		tab.setSelectedIndex(currentIndex - 1);
	}

	public boolean checkExistAppForm(String registrationNumber){
		String existAppFormWhere = String.format("%s = ? AND %s = ?", I_ZZ_Application_Form.COLUMNNAME_ZZ_Application_Form_ID, I_ZZ_Application_Form.COLUMNNAME_ZZ_Org_Reg_No);
		Query existAppFormQuery = MTable.get(Env.getCtx(), I_ZZ_Application_Form.Table_Name).createQuery(existAppFormWhere, null);
		List<X_ZZ_Application_Form> exitsAppForms = existAppFormQuery.setOnlyActiveRecords(true)
				.setParameters(menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID(), registrationNumber)
				.list();
		if (exitsAppForms.size() > 0) {
			showDialog("Exist Application Form", 
					String.format("An application for %s already exists. It was created by User: %s at %s", registrationNumber, 
							exitsAppForms.get(0).getUserName(), 
							exitsAppForms.get(0).getCreated().toLocalDateTime().truncatedTo(ChronoUnit.SECONDS).format(dtf)));
			return true;
		}
		return false;
	}

	public boolean checkExistAppForm(X_C_BPartner bparter){
		String existAppFormWhere = String.format("%s = ? AND %s = ?", I_ZZ_Application_Form.COLUMNNAME_ZZ_Program_Master_Data_ID, I_ZZ_Application_Form.COLUMNNAME_C_BPartner_ID);
		Query existAppFormQuery = MTable.get(Env.getCtx(), I_ZZ_Application_Form.Table_Name).createQuery(existAppFormWhere, null);
		List<X_ZZ_Application_Form> exitsAppForms = existAppFormQuery.setOnlyActiveRecords(true).setParameters(menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID(), bparter.getC_BPartner_ID()).list();
		if (exitsAppForms.size() > 0) {
			X_AD_User createdUser = new X_AD_User(Env.getCtx(), exitsAppForms.get(0).getCreatedBy(), null);
			String phoneInfo = null;
			if (StringUtils.isNotBlank(createdUser.getPhone())) {
				phoneInfo = " - " + createdUser.getPhone();
			}else {
				phoneInfo = "";
			}
			showDialog("Exist Application Form", 
					String.format("An application for %s already exists.\n"
							+ "It was created by User: %s%s on %s", 
							bparter.getName(), 
							exitsAppForms.get(0).getUserName(),
							phoneInfo,
							exitsAppForms.get(0).getCreated().toLocalDateTime().truncatedTo(ChronoUnit.SECONDS).format(dtf)));
			return true;
		}
		return false;
	}


	public void saveAppForm(boolean isSave) throws IOException {
		String trxName = null;

		if (applicationForm == null) {
			applicationForm = new X_ZZ_Application_Form(Env.getCtx(), 0, trxName);
			applicationForm.setZZProgramType(programType.toString());
		}


		applicationForm.setAD_Org_ID(menuContextInfo.getProgramMasterData().getAD_Org_ID());
		applicationForm.setZZ_Program_Master_Data_ID(menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID());

		if (isSave) {
			applicationForm.setZZ_DocStatus(X_ZZ_Application_Form.ZZ_DOCSTATUS_Draft);
		}else {
			applicationForm.setZZ_DocStatus(X_ZZ_Application_Form.ZZ_DOCSTATUS_Submitted);
		}

		saveAppFormCommonPart(applicationForm);

		if (employerDeclarationInfo != null) {
			employerDeclarationInfo.saveForm(trxName, applicationForm);
		}

		applicationForm.saveEx(trxName);// save here to get id when save address on org info

		if (organisationInfo != null){
			organisationInfo.saveForm(trxName, applicationForm);
		}

		if (programContact != null) {
			programContact.saveForm(trxName, applicationForm);
		}

		if (alternateProgramContact != null) {
			alternateProgramContact.saveForm(trxName, applicationForm);
		}

		if(uploadDoc != null) {
			uploadDoc.saveForm(trxName, applicationForm);
		}

		program.doSaveForm(trxName, applicationForm);

		applicationForm.saveEx();
		// Attach VAT certificate to ZZ_Application_Form (one entry per record)
		if (organisationInfo.getVatCertBytes() != null && organisationInfo.getVatCertBytes().length > 0
				&& org.apache.commons.lang3.StringUtils.isNotBlank(organisationInfo.getFileNameVATCer())) {

			// This helper deletes any existing attachment for this record and creates a new one with a single entry.
			// That enforces "only one attachment for this record" as requested.
			za.co.ntier.webform.form.AttachmentUtil.addOrReplaceAttachmentEntry(
					applicationForm,
					organisationInfo.getFileNameVATCer(),
					organisationInfo.getVatCertBytes(),
					"VAT Exempt Cert",
					trxName
					);

			// free memory after persisting
			organisationInfo.setFileNameVATCer(null);
		}

		recordId = applicationForm.getZZ_Application_Form_ID();
		tableId = applicationForm.get_Table_ID();
		setDocumentNo(applicationForm.getDocumentNo());

		// sent email
		showSubmitedDialog(isSave);
		if (!isSave)
			sentEmail();
	}

	private void saveAppFormCommonPart(X_ZZ_Application_Form applicationForm) {
		MUser loginUser = MUser.get(Env.getAD_User_ID(Env.getCtx()));
		applicationForm.setName(loginUser.getName() + " - " + LocalDateTime.now().format(dtf));
	}
	public void saveClose() throws IOException {
		saveAppForm(true);
	}

	public void sentEmail() {
		EmailPoInfo emailPoInfo = new EmailPoInfo();

		MMailText submitedEmail = new MMailText(Env.getCtx(), "bb8d6f79-4bea-448d-a55e-43f52116a03c", null);
		MClient client = MClient.get(Env.getCtx());
		MUser from = MUser.get(Env.getCtx(), FROM_EMAIL_USER_ID);
		submitedEmail.setPO(emailPoInfo);

		int loginId = Env.getAD_User_ID(Env.getCtx());
		MUser receiver = MUser.get(loginId);
		submitedEmail.setUser(receiver);
		if (!client.sendEMail(from, receiver, submitedEmail.getMailHeader(), submitedEmail.getMailText(), null, submitedEmail.isHtml())) {
			log.fine("Problem Sending Email.  Please contact Support");
		}

		if(applicationForm.getC_BPartner_ID() != 0) {
			MBPartner partner = MBPartner.get(Env.getCtx(), applicationForm.getC_BPartner_ID());
			MUser[] contacts = partner.getContacts(false);
			if (contacts.length > 0 && contacts[0].getAD_User_ID() != loginId) {
				receiver = contacts[0];
				submitedEmail.setUser(receiver);
				if (!client.sendEMail(from, receiver, submitedEmail.getMailHeader(), submitedEmail.getMailText(), null, submitedEmail.isHtml())) {
					log.fine("Problem Sending Email.  Please contact Support");
				}
			}

		}


	}

	public int getSelectedTabIndex() { return selectedTabIndex; }
	public void setSelectedTabIndex(int selectedTabIndex) {
		this.selectedTabIndex = selectedTabIndex;
		org.zkoss.bind.BindUtils.postNotifyChange(null, null, this, "selectedTabIndex");
	}

	/**
	 * @param alternateProgramContact the alternateProgramContact to set
	 */
	public void setAlternateProgramContact(AddressInfo alternateProgramContact) {
		this.alternateProgramContact = alternateProgramContact;
	}
	/**
	 * @param sdf the sdf to set
	 */
	public void setApplicationForm(X_ZZ_Application_Form applicationForm) {
		this.applicationForm = applicationForm;
	}

	/**
	 * @param documentNo the documentNo to set
	 */
	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
	}


	public void setEmployerDeclarationInfo(EmployerDeclarationInfo employerDeclarationInfo) {
		this.employerDeclarationInfo = employerDeclarationInfo;
		org.zkoss.bind.BindUtils.postNotifyChange(null, null, this, "declarationComplete");
	}

	/**
	 * @param formInfo the formInfo to set
	 */
	public void setFormInfo(FormInfo formInfo) {
		this.formInfo = formInfo;
	}

	/**
	 * @param programMasterDataID the programMasterDataID to set
	 */
	public void setMenuContextInfo(MenuContextInfo menuContextInfo) {
		this.menuContextInfo = menuContextInfo;
	}


	/**
	 * @param organisationInfo the organisationInfo to set
	 */
	public void setOrganisationInfo(OrganisationInfo organisationInfo) {
		this.organisationInfo = organisationInfo;
		org.zkoss.bind.BindUtils.postNotifyChange(null, null, this, "organisationComplete");
	}

	/**
	 * @param program the program to set
	 */
	public void setProgram(AbstractProgram program) {
		this.program = program;
		org.zkoss.bind.BindUtils.postNotifyChange(null, null, this, "programComplete");
	}

	/**
	 * @param programContact the programContact to set
	 */
	public void setProgramContact(AddressInfo programContact) {
		this.programContact = programContact;
		org.zkoss.bind.BindUtils.postNotifyChange(null, null, this, "programContactComplete");
	}

	public void setProgramType(ProgramType programType) {
		this.programType = programType;
		org.zkoss.bind.BindUtils.postNotifyChange(null, null, this, "organisationComplete");
		org.zkoss.bind.BindUtils.postNotifyChange(null, null, this, "programComplete");
		org.zkoss.bind.BindUtils.postNotifyChange(null, null, this, "programContactComplete");
	}


	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}

	public void setTableId(int tableId) {
		this.tableId = tableId;
	}

	/**
	 * @param uploadDoc the uploadDoc to set
	 */
	public void setUploadDoc(UploadDocComponent uploadDoc) {
		this.uploadDoc = uploadDoc;
	}


	protected void showSubmitedDialog(boolean isSave) {
		String title = isSave?"Successfully saved the application form":"Successfully submitted the application form";


		StringBuilder msgs = new StringBuilder("");
		if (isSave)
			msgs.append("The application form has been saved");
		else
			msgs.append("The application form has been submitted");

		msgs.append("\nYour Application Reference No is:");
		msgs.append(applicationForm.getDocumentNo());
		msgs.append("\n");

		if (isSave) {
			msgs.append("Please note this for future queries");
		}else {
			msgs.append("This has been sent as an email to you for future reference");
		}

		showDialog(title, msgs.toString());
	}

	protected void showDialog(String title, String msg) {
		showDialog(title, msg, true, null);
	}
	
	protected void showDialog(String title, String msg, boolean isCloseApp, Consumer<Object> onCloseDialog) {
		Dialog dialog = new Dialog(title, msg);
		dialog.setOnCloseDialog(t -> {
			if (isCloseApp) {
				DiscretionaryGrantsApplicationProgramVM.showAppList();
			}else if (onCloseDialog != null){
				onCloseDialog.accept(null);
			}
			
		});
		
		Map<String, Object> args = new HashMap<>();
		args.put(ComponentVMWrapper.ComponentKey, dialog);
		String zulPathRelative = WebForm.getBundleResourcePath("component/dialog.zul");				
		Executions.createComponents(zulPathRelative, null, args);
	}
	public void submitApplication() throws IOException {
		saveAppForm(false);

	}



	@Command("tabChanged")
	public void tabChanged() {
		// make sure the parent VM’s property is dirty so any bindings depending on it can re-evaluate
		BindUtils.postNotifyChange(null, null, this, "selectedTabIndex");

		// now *broadcast* to all binders, including the ones created in <include> (your mainButton VMs)
		BindUtils.postGlobalCommand(null, null, "tabSelectionChanged", null);
	}

	public boolean checkCriteria(X_C_BPartner bPartner, Consumer<Object> onCloseDialog) {
		String criteriaValueAll = getMenuContextInfo().getProgramMasterData().getZZ_Criteria();
		if (StringUtils.isBlank(criteriaValueAll)){
			return false;
		}else {
			if (bPartner == null) {
				StringBuilder errBuild = new StringBuilder("Your company has not met the required criteria");
				errBuild.append("\nThe Skills Development Levy (SDL) Number does not exist\nYou can not continue with this application");
				showDialog("Required criteria", errBuild.toString(), false, onCloseDialog);
				return true;
			}
		}

		String[] criteriaValues = criteriaValueAll.split(",");

		I_C_BPartner zzBPartner = POWrapper.create(bPartner, I_C_BPartner.class);

		StringBuilder errBuild = new StringBuilder("Your company has not met the required criteria");
		errBuild.append("\n");
		boolean hasError = false;
		for(String criteriaValue : criteriaValues) {
			if (X_ZZ_Program_Master_Data.ZZ_CRITERIA_OrganizationInMQASector.equals(criteriaValue) && checkOrganizationInMQASector(zzBPartner)) {
				errBuild.append("Organisation in MQA Sector");
				errBuild.append("\n");
				hasError = true;
			}

			if (X_ZZ_Program_Master_Data.ZZ_CRITERIA_LevyPaying.equals(criteriaValue) && checkLevyPaying(zzBPartner)) {
				errBuild.append("Levy Paying");
				errBuild.append("\n");
				hasError = true;
			}

			if (X_ZZ_Program_Master_Data.ZZ_CRITERIA_WSP_ATRSubmitted.equals(criteriaValue) && checkATRSubmitted(zzBPartner)) {
				errBuild.append("WSP ATR Approvals");
				errBuild.append("\n");
				hasError = true;
			}
		}

		errBuild.append("You can not continue with this application");

		if (hasError) {
			showDialog("Required criteria", errBuild.toString(), false, onCloseDialog);
			return true;
		}else {
			return false;	
		}
	}

	protected boolean checkOrganizationInMQASector(I_C_BPartner bPartner) {
		return !bPartner.isZZ_Is_MQA_Sector();
	}
	protected boolean checkLevyPaying(I_C_BPartner bPartner) {
		MYear currentFinYear = new MYear(Env.getCtx(), menuContextInfo.getProgramMasterData().getC_Year_ID(), null);
		String fiscalYearStr = currentFinYear.getFiscalYear();
		String prevFiscalYearStr = String.valueOf(Integer.valueOf(fiscalYearStr) - 1);

		Query queryLevyPaying = MTable.get(Env.getCtx(), I_ZZ_Levy_Paying.Table_Name).createQuery(
				String.format("%s = ? AND %s = ?", I_ZZ_Levy_Paying.COLUMNNAME_C_BPartner_ID, I_C_Year.COLUMNNAME_FiscalYear), null);
		queryLevyPaying.addTableDirectJoin(I_C_Year.Table_Name);
		queryLevyPaying.setParameters(bPartner.getC_BPartner_ID(), prevFiscalYearStr);

		return queryLevyPaying.list().size() == 0;
	}
	protected boolean checkATRSubmitted(I_C_BPartner bPartner) {
		MYear currentFinYear = new MYear(Env.getCtx(), menuContextInfo.getProgramMasterData().getC_Year_ID(), null);
		Query queryLevyPaying = MTable.get(Env.getCtx(), I_ZZ_WSP_ATR_Approvals.Table_Name).createQuery(
				String.format("%s = ? AND %s = ?", I_ZZ_WSP_ATR_Approvals.COLUMNNAME_C_BPartner_ID, I_ZZ_WSP_ATR_Approvals.COLUMNNAME_ZZ_Financial_Year), null);
		queryLevyPaying.setParameters(bPartner.getC_BPartner_ID(), currentFinYear.getFiscalYear());
		return queryLevyPaying.list().size() == 0;
	}


}