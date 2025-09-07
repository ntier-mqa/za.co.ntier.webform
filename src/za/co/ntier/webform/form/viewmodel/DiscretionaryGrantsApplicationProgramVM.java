package za.co.ntier.webform.form.viewmodel;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.adempiere.webui.desktop.DefaultDesktop;
import org.adempiere.webui.session.SessionManager;
import org.apache.commons.lang3.StringUtils;
import org.compiere.model.MUser;
import org.compiere.util.Env;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Tabbox;

import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;
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
import za.co.ntier.webform.form.bean.program.NcvGraduatesProgram;
import za.co.ntier.webform.form.bean.program.NonArtisanDevProgram;
import za.co.ntier.webform.form.bean.program.NonArtisanDevRPLProgram;
import za.co.ntier.webform.form.bean.program.OhasspProgram;
import za.co.ntier.webform.form.bean.program.WorkExperienceProgram;
import za.co.ntier.webform.form.viewmodel.component.ComponentVMWrapper;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class DiscretionaryGrantsApplicationProgramVM {
	private AddressInfo alternateProgramContact;
	
	private String documentNo;

	private EmployerDeclarationInfo employerDeclarationInfo;

	private FormInfo formInfo;

	private MenuContextInfo menuContextInfo;

	private OrganisationInfo organisationInfo;

	private IProgram program;

	private AddressInfo programContact;

	private ProgramType programType;
	private int recordId;

	// --- Getters and Setters for Data Binding ---

	private int tableId;

	private UploadDocComponent uploadDoc;

	/**
	 * @return the alternateProgramContact
	 */
	public AddressInfo getAlternateProgramContact() {
		return alternateProgramContact;
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
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo)
			throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {

		setMenuContextInfo(menuContextInfo);
		programType = menuContextInfo.getProgramType();
		
		if (StringUtils.isNotBlank(menuContextInfo.getApplicationFormUU())) {
			applicationForm = new X_ZZ_Application_Form(Env.getCtx(), menuContextInfo.getApplicationFormUU(), null);
		}
		
		employerDeclarationInfo = new EmployerDeclarationInfo();
		employerDeclarationInfo.initComponent(applicationForm);
		
		formInfo = new FormInfo(menuContextInfo);

		organisationInfo = new OrganisationInfo(menuContextInfo);
		organisationInfo.initComponent(applicationForm);

		if (programType.isCetTvet()) {
			setProgram(new CetTvetProgram(menuContextInfo));
		} else if (ProgramType.INTERNSHIP == programType) {
			setProgram(new InternshipProgram(menuContextInfo));
		} else if (ProgramType.CANDIDACY == programType) {
			setProgram(new CandidacyProgram(menuContextInfo));
		} else if (ProgramType.EXPERIENCE == programType) {
			setProgram(new WorkExperienceProgram(menuContextInfo));
		} else if (ProgramType.DEV_PROGRAM == programType) {
			setProgram(new MedpProgram());
		} else if (ProgramType.ARTISAN_AIDES == programType) {
			setProgram(new ArtisanAidesProgram());
		} else if (ProgramType.ARTISAN_DEV == programType) {
			setProgram(new ArtisanDevProgram(menuContextInfo));
		} else if (ProgramType.CENTRE_SPECIALISATION == programType) {
			setProgram(new CentreOfSpecialisationProgram(menuContextInfo));
		} else if (ProgramType.ARTISAN_RPL == programType) {
			setProgram(new ArtisanRPLProgram());
		} else if (ProgramType.NON_ARTISAN_DEV == programType) {
			setProgram(new NonArtisanDevProgram(menuContextInfo));
		} else if (ProgramType.NON_ARTISAN_DEV_RPL == programType) {
			setProgram(new NonArtisanDevRPLProgram(menuContextInfo));
		} else if (ProgramType.NCV_GRADUATES == programType) {
			setProgram(new NcvGraduatesProgram(menuContextInfo));
		} else if (ProgramType.AET == programType) {
			setProgram(new AetProgram(menuContextInfo));
		} else if (ProgramType.OHASSP == programType) {
			setProgram(new OhasspProgram());
		} else if (ProgramType.INHOUSE_TRAINING == programType) {
			setProgram(new InhouseTrainingProgram());
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
			

		uploadDoc = new UploadDocComponent(menuContextInfo);

	}

	public boolean isShowUploadDoc() {
		return uploadDoc != null && uploadDoc.getUploadDoc().getRows().size() > 0;
	}


	private void saveAppFormCommonPart(X_ZZ_Application_Form applicationForm) {
 		MUser loginUser = MUser.get(Env.getAD_User_ID(Env.getCtx()));
		DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
		applicationForm.setName(loginUser.getName() + " - " + LocalDateTime.now().format(dtf));
 	}

	/**
	 * @param alternateProgramContact the alternateProgramContact to set
	 */
	public void setAlternateProgramContact(AddressInfo alternateProgramContact) {
		this.alternateProgramContact = alternateProgramContact;
	}


	/**
	 * @param documentNo the documentNo to set
	 */
	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
	}

	public void setEmployerDeclarationInfo(EmployerDeclarationInfo employerDeclarationInfo) {
		this.employerDeclarationInfo = employerDeclarationInfo;
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
	}

	/**
	 * @param program the program to set
	 */
	public void setProgram(IProgram program) {
		this.program = program;
	}

 	/**
	 * @param programContact the programContact to set
	 */
	public void setProgramContact(AddressInfo programContact) {
		this.programContact = programContact;
	}
	 	
	public void setProgramType(ProgramType programType) {
		this.programType = programType;
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
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		// Set the context class loader to this bundle's class loader to ensure that
		// classes provided by the bundle (e.g., za.co.ntier.webform.form.viewmodel.*)
		// used in ZUL files can be found.
		String zulPathRelative = WebForm.getBundleResourcePath("component/dialog.zul");
		Map<String, Object> args = new HashMap<>();
		args.put(ComponentVMWrapper.ComponentKey, new Dialog(title, msg, tableId, recordId, documentNo, true));
		
		Thread.currentThread().setContextClassLoader(WebForm.class.getClassLoader());
		try {
			
			Executions.createComponents(zulPathRelative, null, args);
		} finally {
			Thread.currentThread().setContextClassLoader(cl);
		}
	}

	public void deleteApp() {
		if (applicationForm != null) {
			try {
				applicationForm.deleteEx(true);
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
	
	public void saveClose() throws IOException {
		saveAppForm(true, "Successfully save the application form",
				"The application form has been saved.");
	}
	
	public void prevTab(Tabbox tab) {
		int currentIndex = tab.getSelectedIndex();
		tab.setSelectedIndex(currentIndex - 1);
	}
	
	public void nextTab(Tabbox tab) {
		int currentIndex = tab.getSelectedIndex();
		tab.setSelectedIndex(currentIndex + 1);
	}

	private X_ZZ_Application_Form applicationForm;
	
	public void submitApplication() throws IOException {
		saveAppForm(false, "Successfully submitted the application form",
				"The application form has been submitted.");

	}
	
	public void saveAppForm(boolean isSave, String title, String msg) throws IOException {
		String trxName = null;
		
		if (applicationForm == null) 
			applicationForm = new X_ZZ_Application_Form(Env.getCtx(), 0, trxName);
		
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

		if (organisationInfo != null){
			organisationInfo.saveForm(trxName, applicationForm);
		}

		applicationForm.saveEx(trxName);
		
		if (programContact != null) {
			programContact.saveForm(trxName, applicationForm);
		}

		if (alternateProgramContact != null) {
			alternateProgramContact.saveForm(trxName, applicationForm);
		}
		
		if(uploadDoc != null) {
			uploadDoc.saveForm(trxName, applicationForm);
		}

		program.saveForm(trxName, applicationForm);
		
		applicationForm.saveEx();
		
		recordId = applicationForm.getZZ_Application_Form_ID();
		tableId = applicationForm.get_Table_ID();
		setDocumentNo(applicationForm.getDocumentNo());
		
		showDialog(title, msg);
	}

	/**
	 * @return the applicationForm
	 */
	public X_ZZ_Application_Form getApplicationForm() {
		return applicationForm;
	}

	/**
	 * @param applicationForm the applicationForm to set
	 */
	public void setApplicationForm(X_ZZ_Application_Form applicationForm) {
		this.applicationForm = applicationForm;
	}
	public static final int EMPLOYER_APP_AD_FORM_ID = 1000000; // your WebForm AD_Form_ID
	
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
}