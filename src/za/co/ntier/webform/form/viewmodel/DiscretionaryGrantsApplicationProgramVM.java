package za.co.ntier.webform.form.viewmodel;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.adempiere.model.GenericPO;
import org.adempiere.webui.desktop.DefaultDesktop;
import org.adempiere.webui.session.SessionManager;
import org.apache.commons.lang3.StringUtils;
import org.compiere.model.MBPartner;
import org.compiere.model.MClient;
import org.compiere.model.MMailText;
import org.compiere.model.MSysConfig;
import org.compiere.model.MTable;
import org.compiere.model.MUser;
import org.compiere.model.Query;
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

import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.form.bean.DataType;
import za.co.ntier.webform.form.bean.ProgramType;
import za.co.ntier.webform.form.bean.component.AddressInfo;
import za.co.ntier.webform.form.bean.component.Dialog;
import za.co.ntier.webform.form.bean.component.EmployerDeclarationInfo;
import za.co.ntier.webform.form.bean.component.FormInfo;
import za.co.ntier.webform.form.bean.component.OrganisationInfo;
import za.co.ntier.webform.form.bean.component.UploadDocComponent;
import za.co.ntier.webform.form.bean.program.AetProgram;
import za.co.ntier.webform.form.bean.program.ArtisanAidesProgram;
import za.co.ntier.webform.form.bean.program.ArtisanDevProgram;
import za.co.ntier.webform.form.bean.program.ArtisanRPLProgram;
import za.co.ntier.webform.form.bean.program.CandidacyProgram;
import za.co.ntier.webform.form.bean.program.CentreOfSpecialisationProgram;
import za.co.ntier.webform.form.bean.program.CetTvetProgram;
import za.co.ntier.webform.form.bean.program.InhouseTrainingProgram;
import za.co.ntier.webform.form.bean.program.InternshipProgram;
import za.co.ntier.webform.form.bean.program.MedpProgram;
import za.co.ntier.webform.form.bean.program.MiningCommunityUnemployedYouthProgram;
import za.co.ntier.webform.form.bean.program.NcvGraduatesProgram;
import za.co.ntier.webform.form.bean.program.NonArtisanDevProgram;
import za.co.ntier.webform.form.bean.program.NonArtisanDevRPLProgram;
import za.co.ntier.webform.form.bean.program.OhasspProgram;
import za.co.ntier.webform.form.bean.program.WorkExperienceProgram;
import za.co.ntier.webform.form.bean.program.WorkplaceCoachesProgram;
import za.co.ntier.webform.form.viewmodel.component.ComponentVMWrapper;
import za.co.ntier.webform.model.I_ZZ_Application_Form;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

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

	private IProgram program;

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
	 * @return the applicationForm
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

	private Integer getLearners(DataType dt, Object cell) {
	    if (cell == null) return null;
	    switch (dt) {
	        case PositiveNumber:
	            // your ZUL uses row[col].value for numbers
	            try {
	                Object v = cell.getClass().getMethod("getValue").invoke(cell);
	                if (v instanceof Number) return ((Number)v).intValue();
	            } catch (Exception ignore) {}
	            return null;
	        case Text:
	            try {
	                String s = ((String)cell).trim();
	                if (s.isEmpty()) return null;
	                return Integer.parseInt(s);
	            } catch (Exception e) { return null; }
	        default:
	            return null;
	    }
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
	public IProgram getProgram() {
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
			setProgram(new MedpProgram(applicationForm));
		} else if (ProgramType.ARTISAN_AIDES == programType) {
			setProgram(new ArtisanAidesProgram(applicationForm));
		} else if (ProgramType.ARTISAN_DEV == programType) {
			setProgram(new ArtisanDevProgram(menuContextInfo, applicationForm));
		} else if (ProgramType.CENTRE_SPECIALISATION == programType) {
			setProgram(new CentreOfSpecialisationProgram(menuContextInfo, applicationForm));
		} else if (ProgramType.ARTISAN_RPL == programType) {
			setProgram(new ArtisanRPLProgram(applicationForm));
		} else if (ProgramType.NON_ARTISAN_DEV == programType) {
			setProgram(new NonArtisanDevProgram(menuContextInfo, applicationForm));
		} else if (ProgramType.NON_ARTISAN_DEV_RPL == programType) {
			setProgram(new NonArtisanDevRPLProgram(menuContextInfo, applicationForm));
		} else if (ProgramType.NCV_GRADUATES == programType) {
			setProgram(new NcvGraduatesProgram(menuContextInfo, applicationForm));
		} else if (ProgramType.AET == programType) {
			setProgram(new AetProgram(menuContextInfo, applicationForm));
		} else if (ProgramType.OHASSP == programType) {
			setProgram(new OhasspProgram(applicationForm));
		} else if (ProgramType.INHOUSE_TRAINING == programType) {
			setProgram(new InhouseTrainingProgram(applicationForm));
		} else if (ProgramType.WORKPLACE_COACHES == programType) {
			setProgram(new WorkplaceCoachesProgram(applicationForm));
		}else if (ProgramType.MINING_COMMUNITY == programType
				|| ProgramType.UNEMPLOYED_YOUTH == programType) {
			program = new MiningCommunityUnemployedYouthProgram(menuContextInfo, applicationForm);
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
		  // orgContact (all required)
		  "organisationInfo.orgContact.nameSiteRepresentative",
		  "organisationInfo.orgContact.representativeDesignation",
		  "organisationInfo.orgContact.mobileNumber",
		  "organisationInfo.orgContact.landlineNumber",
		  "organisationInfo.orgContact.email",

		  // alternateOrgContact (all required)
		  "organisationInfo.alternateOrgContact.nameSiteRepresentative",
		  "organisationInfo.alternateOrgContact.representativeDesignation",
		  "organisationInfo.alternateOrgContact.mobileNumber",
		  "organisationInfo.alternateOrgContact.landlineNumber",
		  "organisationInfo.alternateOrgContact.email"
		})
	
	public boolean isOrganisationComplete() {
		boolean colOk = true;
		boolean orgOk = true;
	    if (programType.isCetTvet()) {
	    	colOk = organisationInfo.getCetTvetCollegeSelected() != null;
	    } else {
	    	 orgOk = notEmpty(organisationInfo.getOrgName())  
	    			 && notEmpty(organisationInfo.getOrgTaxNumber())
	    			 && notEmpty(organisationInfo.getOrgRegistrationNumber());
	    	 Integer n = organisationInfo.getOrgSizeInfo() != null
	 	            ? organisationInfo.getOrgSizeInfo().getNumOfEmployer()
	 	            : null;
	 	     orgOk = orgOk && n != null && n > 0;
	    }
	    orgOk = orgOk
	        && notEmpty(organisationInfo.getSdlNumber())
	        && notEmpty(organisationInfo.getSiteSDLNumber());	    
	    
	    boolean addrOk = (!organisationInfo.getPhysicalAddressInfo().showLineAddress()
	                      || notEmpty(organisationInfo.getPhysicalAddressInfo().getAddressLine()))
	        && (!organisationInfo.getPhysicalAddressInfo().showGeographicAddress()
	                      || notEmpty(organisationInfo.getPhysicalAddressInfo().getPostalCode()));
	    boolean postalOk = (!organisationInfo.getPostAddressInfo().showPostalAddress()
                || notEmpty(organisationInfo.getPostAddressInfo().getAddressLine()))
	    		&& (!organisationInfo.getPostAddressInfo().showGeographicAddress()
                || notEmpty(organisationInfo.getPostAddressInfo().getPostalCode()));
	    boolean provincesPhysicalOk = organisationInfo.getPhysicalAddressInfo().getProvinceSelected() != null  &&
	    		notEmpty(organisationInfo.getPhysicalAddressInfo().getProvinceSelected().getName());
	    boolean provincesPostalOk = (!organisationInfo.getPostAddressInfo().showPostalAddress()
	    		|| (organisationInfo.getPostAddressInfo().getProvinceSelected() != null &&
	    				notEmpty(organisationInfo.getPostAddressInfo().getProvinceSelected().getName())
	    				));
	    // orgContact required (only if contact section is shown)
	    boolean orgContactOk = organisationInfo.getOrgContact() == null
	    		|| (!organisationInfo.getOrgContact().showContact())
	        || (notEmpty(organisationInfo.getOrgContact().getNameSiteRepresentative())
	            && notEmpty(organisationInfo.getOrgContact().getRepresentativeDesignation())
	            && notEmpty(organisationInfo.getOrgContact().getMobileNumber())
	            && notEmpty(organisationInfo.getOrgContact().getLandlineNumber())
	            && notEmpty(organisationInfo.getOrgContact().getEmail()));

	    // alternateOrgContact required (only if shown)
	    boolean altContactOk = organisationInfo.getAlternateOrgContact() == null
	    	||	!organisationInfo.getAlternateOrgContact().showContact()
	        || (notEmpty(organisationInfo.getAlternateOrgContact().getNameSiteRepresentative())
	            && notEmpty(organisationInfo.getAlternateOrgContact().getRepresentativeDesignation())
	            && notEmpty(organisationInfo.getAlternateOrgContact().getMobileNumber())
	            && notEmpty(organisationInfo.getAlternateOrgContact().getLandlineNumber())
	            && notEmpty(organisationInfo.getAlternateOrgContact().getEmail()));
	    return orgOk && addrOk && colOk && postalOk
	            && provincesPhysicalOk && provincesPostalOk
	            && orgContactOk && altContactOk;
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
		Query existAppFormQuery = MTable.get(I_ZZ_Application_Form.Table_ID).createQuery(existAppFormWhere, null);
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
		Query existAppFormQuery = MTable.get(I_ZZ_Application_Form.Table_ID).createQuery(existAppFormWhere, null);
		List<X_ZZ_Application_Form> exitsAppForms = existAppFormQuery.setOnlyActiveRecords(true).setParameters(menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID(), bparter.getC_BPartner_ID()).list();
		if (exitsAppForms.size() > 0) {
			showDialog("Exist Application Form", 
					String.format("An application for %s already exists. It was created by User: %s at %s", 
							bparter.getName(), 
							exitsAppForms.get(0).getUserName(),
							exitsAppForms.get(0).getCreated().toLocalDateTime().truncatedTo(ChronoUnit.SECONDS).format(dtf)));
			return true;
		}
		return false;
	}
	
	
	public void saveAppForm(boolean isSave, String title, String msg) throws IOException {
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
		
		if(!isSave) {
			applicationForm.setDateDoc(Timestamp.valueOf(LocalDateTime.now()));
		}
		
		applicationForm.saveEx();
		
		recordId = applicationForm.getZZ_Application_Form_ID();
		tableId = applicationForm.get_Table_ID();
		setDocumentNo(applicationForm.getDocumentNo());
		
		// sent email
		showSubmitedDialog(title, msg);
		if (!isSave)
			sentEmail();
	}
	
	private void saveAppFormCommonPart(X_ZZ_Application_Form applicationForm) {
 		MUser loginUser = MUser.get(Env.getAD_User_ID(Env.getCtx()));
		applicationForm.setName(loginUser.getName() + " - " + LocalDateTime.now().format(dtf));
 	}
	public void saveClose() throws IOException {
		saveAppForm(true, "Successfully save the application form",
				"The application form has been saved.");
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
	 * @param applicationForm the applicationForm to set
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
	public void setProgram(IProgram program) {
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

		protected void showDialog(String title, String msg) {
			Dialog dialog = new Dialog(title, msg);
			showDialog(dialog);
		}
		
		protected void showSubmitedDialog(String title, String msg) {
			Dialog dialog = new Dialog(title, msg, tableId, recordId, applicationForm.getDocumentNo());
			showDialog(dialog);
		}
		
		protected void showDialog(Dialog dialog) {
			Map<String, Object> args = new HashMap<>();
			args.put(ComponentVMWrapper.ComponentKey, dialog);
			String zulPathRelative = WebForm.getBundleResourcePath("component/dialog.zul");				
			Executions.createComponents(zulPathRelative, null, args);
		}
		public void submitApplication() throws IOException {
			saveAppForm(false, "Successfully submitted the application form",
					"The application form has been submitted.");

		}
		
		
		
		@Command("tabChanged")
		public void tabChanged() {
		    // make sure the parent VM’s property is dirty so any bindings depending on it can re-evaluate
		    BindUtils.postNotifyChange(null, null, this, "selectedTabIndex");

		    // now *broadcast* to all binders, including the ones created in <include> (your mainButton VMs)
		    BindUtils.postGlobalCommand(null, null, "tabSelectionChanged", null);
		}


}