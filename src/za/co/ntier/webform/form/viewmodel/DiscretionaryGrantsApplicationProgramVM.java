package za.co.ntier.webform.form.viewmodel;

import java.util.List;

import org.apache.commons.lang3.RandomUtils;
import org.compiere.model.MUser;
import org.compiere.util.Env;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.form.bean.ProgramInfo;
import za.co.ntier.webform.form.bean.ProgramType;
import za.co.ntier.webform.form.bean.AddressInfoBase;
import za.co.ntier.webform.form.bean.CompanyInfo;
import za.co.ntier.webform.form.bean.LearnerInputInfo;
import za.co.ntier.webform.form.bean.LearnerInputTableInfo;
import za.co.ntier.webform.form.bean.ProgramCetTvetInfo;
import za.co.ntier.webform.form.bean.EmployerInfo;
import za.co.ntier.webform.form.bean.FormInfo;
import za.co.ntier.webform.model.X_ZZ_Application_Form;
import za.co.ntier.webform.model.X_ZZ_FormContact;
import za.co.ntier.webform.model.X_ZZ_FormDiscipline;

public class DiscretionaryGrantsApplicationProgramVM {
	private ProgramInfo programInfo;

	private CompanyInfo companyInfo;

	private EmployerInfo employerInfo;

	private FormInfo formInfo;

	private MenuContextInfo menuContextInfo;

	/**
	 * @return the programInfo
	 */
	public ProgramInfo getProgramInfo() {
		return programInfo;
	}

	/**
	 * @return the companyInfo
	 */
	public CompanyInfo getCompanyInfo() {
		return companyInfo;
	}

	// --- Getters and Setters for Data Binding ---

	/**
	 * @return the employerInfo
	 */
	public EmployerInfo getEmployerInfo() {
		return employerInfo;
	}

	/**
	 * @return the formInfo
	 */
	public FormInfo getFormInfo() {
		return formInfo;
	}
	
	private ProgramType programType;

	public ProgramType getProgramType() {
		return programType;
	}

	public void setProgramType(ProgramType programType) {
		this.programType = programType;
	}

	private ProgramCetTvetInfo programCetTvetInfo;

	/**
	 * @return the programMasterDataID
	 */
	public MenuContextInfo getMenuContextInfo() {
		return menuContextInfo;
	}

	@Init
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo) {
		
		setMenuContextInfo(menuContextInfo);
		programType = menuContextInfo.getProgramType();
		setCompanyInfo(CompanyInfo.getDefaultCompanyInfo());
		setFormInfo(new FormInfo(menuContextInfo));

		employerInfo = new EmployerInfo(menuContextInfo);
		
		if (programType.isCetTvet()) {
			setProgramCetTvetInfo(new ProgramCetTvetInfo(menuContextInfo));
		}else {
			programInfo = new ProgramInfo(menuContextInfo);
		}
		

	}

	/**
	 * @param programInfo the programInfo to set
	 */
	public void setProgramInfo(ProgramInfo programInfo) {
		this.programInfo = programInfo;
	}

	/**
	 * @param companyInfo the companyInfo to set
	 */
	public void setCompanyInfo(CompanyInfo companyInfo) {
		this.companyInfo = companyInfo;
	}

	/**
	 * @param employerInfo the employerInfo to set
	 */
	public void setEmployerInfo(EmployerInfo employerInfo) {
		this.employerInfo = employerInfo;
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
	
	public String getSubmitButtonLabel() {
		return "Submit Application";
	}
	
	@Command(value = "submitApplication")
	public void submitApplication() {
		X_ZZ_Application_Form applicationForm = new X_ZZ_Application_Form(Env.getCtx(), 0, null);
		applicationForm.setName("test" + RandomUtils.nextInt());
		
		MUser loginUser = MUser.get(Env.getCtx(), Env.getAD_User_ID(Env.getCtx()));
		applicationForm.setC_BPartner_ID(loginUser.getC_BPartner_ID());
		applicationForm.setZZ_SDL_No(employerInfo.getSdlNumber());
		applicationForm.setZZ_Side_SDL_No(employerInfo.getSiteSDLNumber());
		applicationForm.setZZ_VAT(employerInfo.getEmployerTaxNumber());
		
		applicationForm.setNumberEmployees(employerInfo.getOrgSizeInfo().getNumOfEmployer());
		applicationForm.setZZ_HasWSPSubmited(employerInfo.getOrgSizeInfo().isSubmittedWSP());
		applicationForm.setZZ_HasPivotalPlanSubmited(employerInfo.getOrgSizeInfo().isSubmittedPivotal());
		
		if (programType.isShowDisciplineTable())
			applicationForm.setZZ_DisciplineTotalLearners(programInfo.getDisciplineTableInfo().getTotalLearners());
		
		if (programType.isShowTradeTable())
			applicationForm.setZZ_TradeTotalLearners(programInfo.getTradeTableInfo().getTotalLearners());
		
		applicationForm.saveEx();
		
		X_ZZ_FormContact contact = createFormContact(employerInfo.getPhysicalAddressInfo(), applicationForm.getZZ_Application_Form_ID());
		contact.saveEx();
		
		contact = createFormContact(employerInfo.getPostAddressInfo(), applicationForm.getZZ_Application_Form_ID());
		contact.saveEx();
		
		contact = createFormContact(employerInfo.getOrgContact(), applicationForm.getZZ_Application_Form_ID());
		contact.saveEx();
		
		contact = createFormContact(employerInfo.getAlternateOrgContact(), applicationForm.getZZ_Application_Form_ID());
		contact.saveEx();
		
		contact = createFormContact(programInfo.getProgramContact(), applicationForm.getZZ_Application_Form_ID());
		contact.saveEx();
		
		contact = createFormContact(programInfo.getAlternateProgramContact(), applicationForm.getZZ_Application_Form_ID());		
		contact.saveEx();
		if (programType.isShowDisciplineTable())
			createDiscipline(programInfo.getDisciplineTableInfo(), applicationForm.getZZ_Application_Form_ID());
		
		if (programType.isShowTradeTable())
			createDiscipline(programInfo.getTradeTableInfo(), applicationForm.getZZ_Application_Form_ID());
		
	}
	
	public List<X_ZZ_FormDiscipline> createDiscipline(LearnerInputTableInfo disciplineTableInfo, int applicationFormID) {
		for (LearnerInputInfo discipline : disciplineTableInfo.getLearnerInputInfos()) {
			if (discipline.getNoLearners() == null)
				continue;
			
			X_ZZ_FormDiscipline formDisciplines = new X_ZZ_FormDiscipline(Env.getCtx(), 0, null);
			formDisciplines.setZZ_Application_Form_ID(applicationFormID);
			formDisciplines.setZZ_LearnersNo(discipline.getNoLearners());
			
			if (discipline.getAreaSelected() != null)
				formDisciplines.setC_City_ID(discipline.getAreaSelected().getC_City_ID());
			
			if (discipline.getProvince() != null)
				formDisciplines.setC_Region_ID(discipline.getProvince().getC_Region_ID());
				
			formDisciplines.setPostal(discipline.getPostalCode());
			formDisciplines.setZZ_DisciplineType(disciplineTableInfo.getLearnerInputType());
			if (disciplineTableInfo.isTrade()) {
				formDisciplines.setZZ_Trade_ID(discipline.getLearnerInputID());
			}else {
				formDisciplines.setZZ_Disciplines_ID(discipline.getLearnerInputID());
			}

			formDisciplines.saveEx();
		}
		
		return null;
	}
	
	public X_ZZ_FormContact createFormContact(AddressInfoBase addressInfoBase, int applicationFormID) {
		X_ZZ_FormContact contact = new X_ZZ_FormContact(Env.getCtx(), 0, null);
		contact.setZZ_Application_Form_ID(applicationFormID);
		if(addressInfoBase.getProvinceSelected() != null)
			contact.setC_Region_ID(addressInfoBase.getProvinceSelected().getC_Region_ID());
		
		if(addressInfoBase.getAreaSelected() != null)
			contact.setC_City_ID(addressInfoBase.getAreaSelected().getC_City_ID());
		
		contact.setPostal(addressInfoBase.getPostalCode());
		
		contact.setZZ_SideName(addressInfoBase.getSiteName());
		contact.setAddress(addressInfoBase.getAddressLine());
		
		contact.setContactName(addressInfoBase.getNameSiteRepresentative());
		contact.setZZ_Designation(addressInfoBase.getRepresentativeDesignation());
		contact.setPhone(addressInfoBase.getMobileNumber());
		contact.setPhone2(addressInfoBase.getLandlineNumber());
		contact.setEMail(addressInfoBase.getEmail());
		contact.setZZ_ContactType(addressInfoBase.getAddressCategory().toString());
		return contact;
	}

	public ProgramCetTvetInfo getProgramCetTvetInfo() {
		return programCetTvetInfo;
	}

	public void setProgramCetTvetInfo(ProgramCetTvetInfo programCetTvetInfo) {
		this.programCetTvetInfo = programCetTvetInfo;
	}
}