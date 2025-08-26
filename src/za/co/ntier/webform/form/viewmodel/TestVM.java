package za.co.ntier.webform.form.viewmodel;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zul.Tabbox;

import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.form.bean.AddressInfoBase;
import za.co.ntier.webform.form.bean.CompanyInfo;
import za.co.ntier.webform.form.bean.FormInfo;
import za.co.ntier.webform.form.bean.OrganisationInfo;
import za.co.ntier.webform.form.bean.ProgramCetTvetInfo;
import za.co.ntier.webform.form.bean.ProgramType;

public class TestVM {
	private CompanyInfo companyInfo;
	private MenuContextInfo menuContextInfo;
	private FormInfo formInfo;
	private OrganisationInfo organisationInfo;
	private AddressInfoBase alternateProgramContact;
	private AddressInfoBase programContact;
	private ProgramType programType; 
	private ProgramCetTvetInfo programCetTvetInfo;
	/**
	 * @return the companyInfo
	 */
	public CompanyInfo getCompanyInfo() {
		return companyInfo;
	}

	/**
	 * @param companyInfo the companyInfo to set
	 */
	public void setCompanyInfo(CompanyInfo companyInfo) {
		this.companyInfo = companyInfo;
	}

	@Init
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo) {
		
		this.setMenuContextInfo(menuContextInfo);
		this.programType = menuContextInfo.getProgramType();
		this.companyInfo = CompanyInfo.getDefaultCompanyInfo();
		this.formInfo = new FormInfo(menuContextInfo);
		organisationInfo = new OrganisationInfo(menuContextInfo);

		// main contact
		if (programType.isShowMainAddress())
			programContact = new AddressInfoBase(programType, false, null);

		// main alternate contact
		if (programType.isShowMainAddressAlter())
			alternateProgramContact = new AddressInfoBase(programType, true, null);
		
		if (programType.isCetTvet()) {
			setProgramCetTvetInfo(new ProgramCetTvetInfo(menuContextInfo));
		}
	}

	/**
	 * @return the menuContextInfo
	 */
	public MenuContextInfo getMenuContextInfo() {
		return menuContextInfo;
	}

	/**
	 * @param menuContextInfo the menuContextInfo to set
	 */
	public void setMenuContextInfo(MenuContextInfo menuContextInfo) {
		this.menuContextInfo = menuContextInfo;
	}

	/**
	 * @return the formInfo
	 */
	public FormInfo getFormInfo() {
		return formInfo;
	}

	/**
	 * @param formInfo the formInfo to set
	 */
	public void setFormInfo(FormInfo formInfo) {
		this.formInfo = formInfo;
	}

	/**
	 * @return the organisationInfo
	 */
	public OrganisationInfo getOrganisationInfo() {
		return organisationInfo;
	}

	/**
	 * @param organisationInfo the organisationInfo to set
	 */
	public void setOrganisationInfo(OrganisationInfo organisationInfo) {
		this.organisationInfo = organisationInfo;
	}

	/**
	 * @return the alternateProgramContact
	 */
	public AddressInfoBase getAlternateProgramContact() {
		return alternateProgramContact;
	}

	/**
	 * @param alternateProgramContact the alternateProgramContact to set
	 */
	public void setAlternateProgramContact(AddressInfoBase alternateProgramContact) {
		this.alternateProgramContact = alternateProgramContact;
	}

	/**
	 * @return the programContact
	 */
	public AddressInfoBase getProgramContact() {
		return programContact;
	}

	/**
	 * @param programContact the programContact to set
	 */
	public void setProgramContact(AddressInfoBase programContact) {
		this.programContact = programContact;
	}

	/**
	 * @return the programType
	 */
	public ProgramType getProgramType() {
		return programType;
	}

	/**
	 * @param programType the programType to set
	 */
	public void setProgramType(ProgramType programType) {
		this.programType = programType;
	}

	/**
	 * @return the programCetTvetInfo
	 */
	public ProgramCetTvetInfo getProgramCetTvetInfo() {
		return programCetTvetInfo;
	}

	/**
	 * @param programCetTvetInfo the programCetTvetInfo to set
	 */
	public void setProgramCetTvetInfo(ProgramCetTvetInfo programCetTvetInfo) {
		this.programCetTvetInfo = programCetTvetInfo;
	}
	
	@Command
	public void nextTab(@BindingParam("tab") Tabbox tab) {
		int currentIndex = tab.getSelectedIndex();
		tab.setSelectedIndex(currentIndex + 1); 
	}
    
	@Command
	public void prevTab(@BindingParam("tab") Tabbox tab) {
		int currentIndex = tab.getSelectedIndex();
		tab.setSelectedIndex(currentIndex - 1); 
	}
	
	@Command
	public void submit() {
		 
	}

	
}
