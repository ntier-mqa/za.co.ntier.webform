package za.co.ntier.webform.form.viewmodel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.compiere.model.Query;
import org.compiere.util.Env;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.form.bean.AddressCategory;
import za.co.ntier.webform.form.bean.AddressInfoBase;
import za.co.ntier.webform.form.bean.CompanyInfo;
import za.co.ntier.webform.form.bean.DisciplineHDSA;
import za.co.ntier.webform.form.bean.EmployerInfo;
import za.co.ntier.webform.form.bean.FormInfo;
import za.co.ntier.webform.form.bean.OrganisationSizeInfo;
import za.co.ntier.webform.form.bean.Province;
import za.co.ntier.webform.model.I_ZZ_Disciplines;
import za.co.ntier.webform.model.I_ZZ_Program_Disciplines;
import za.co.ntier.webform.model.I_ZZ_Program_Master_Data;
import za.co.ntier.webform.model.X_ZZ_Program_Disciplines;
import za.co.ntier.webform.model.X_ZZ_Program_Master_Data;

public class DiscretionaryGrantsApplicationCandidacyVM {
	private AddressInfoBase candidacyContact;
	private AddressInfoBase alternateCandidacyContact;
	private CompanyInfo companyInfo;

	private List<DisciplineHDSA> disciplineHDSAs;

	private EmployerInfo employerInfo;

	private Province employerProvinceSelected;

	Map<String, List<String>> formData;

	private FormInfo formInfo;

	private OrganisationSizeInfo orgSizeInfo;
	
	private AddressInfoBase orgContact;
	
	private AddressInfoBase alternateOrgContact;

	private AddressInfoBase physicalAddressInfo;

	private AddressInfoBase postAddressInfo;

	public DiscretionaryGrantsApplicationCandidacyVM() throws IOException {
		// Initialize with default values if needed

		setCompanyInfo(CompanyInfo.getDefaultCompanyInfo());
		setFormInfo(new FormInfo("DGA CANDIDACY", "DGAF01"));

		employerInfo = new EmployerInfo();
		employerInfo.setEmployerName("Le Quy Hiep");
		postAddressInfo = new AddressInfoBase(AddressCategory.POSTAL, Province.KZN);

		physicalAddressInfo = new AddressInfoBase(AddressCategory.PHYSICAL, Province.NC);

		orgSizeInfo = new OrganisationSizeInfo(new Random().nextInt(), false, true);

		candidacyContact = new AddressInfoBase(AddressCategory.CANDIDACY_CONTACT, Province.WS);
		alternateCandidacyContact = new AddressInfoBase(AddressCategory.CANDIDACY_CONTACT, Province.LIM);
		
		orgContact = new AddressInfoBase(AddressCategory.ORG_CONTACT, Province.NW);
		alternateOrgContact = new AddressInfoBase(AddressCategory.ORG_CONTACT, Province.EC);
	}

	@Init
    public void init(@ExecutionArgParam(WebForm.programMasterDataKey) String programMasterDataKey) {
        I_ZZ_Program_Master_Data masterData = new X_ZZ_Program_Master_Data(Env.getCtx(), programMasterDataKey, null);
        
        List<X_ZZ_Program_Disciplines> programDisciplines = new Query(Env.getCtx(), I_ZZ_Program_Disciplines.Table_Name, 
        		String.format("%s = ?", I_ZZ_Program_Master_Data.COLUMNNAME_ZZ_Program_Master_Data_ID), null)
			.setParameters(masterData.getZZ_Program_Master_Data_ID())
			.setClient_ID()
			.list();
        
        disciplineHDSAs = new ArrayList<>();
        
        programDisciplines.stream().forEach((programDiscipline) -> {
        	I_ZZ_Disciplines discipline = programDiscipline.getZZ_Disciplines();
        	DisciplineHDSA disciplineHDSA = new DisciplineHDSA(discipline.getName(), programDiscipline.isZZ_WPA_Req(), programDiscipline.isZZ_Is_Accred_SLA_Req());
        	disciplineHDSAs.add(disciplineHDSA);
        });
    }
	
	/**
	 * @return the candidacyContact
	 */
	public AddressInfoBase getCandidacyContact() {
		return candidacyContact;
	}

	/**
	 * @return the companyInfo
	 */
	public CompanyInfo getCompanyInfo() {
		return companyInfo;
	}

	/**
	 * @return the disciplineHDSAs
	 */
	public List<DisciplineHDSA> getDisciplineHDSAs() {
		return disciplineHDSAs;
	}

	/**
	 * @return the employerInfo
	 */
	public EmployerInfo getEmployerInfo() {
		return employerInfo;
	}

	// --- Getters and Setters for Data Binding ---

	/**
	 * @return the employerProvinceSelected
	 */
	public Province getEmployerProvinceSelected() {
		return employerProvinceSelected;
	}

	/**
	 * @return the formInfo
	 */
	public FormInfo getFormInfo() {
		return formInfo;
	}

	/**
	 * @return the orgSizeInfo
	 */
	public OrganisationSizeInfo getOrgSizeInfo() {
		return orgSizeInfo;
	}

	/**
	 * @return the addressInfo
	 */
	public AddressInfoBase getPhysicalAddressInfo() {
		return physicalAddressInfo;
	}

	/**
	 * @return the postAddressInfo
	 */
	public AddressInfoBase getPostAddressInfo() {
		return postAddressInfo;
	}

	// --- Command Method ---
	@Command
	@NotifyChange("savedDataOutput") // Notify ZK to update the 'savedDataOutput' label
	public void saveEmployerInfo() {
		// In a real application, you would save this data to a database or perform
		// business logic.
		// For this example, we'll just display the collected data.
		StringBuilder sb = new StringBuilder();
		System.out.println("Employer Info Saved:\n" + sb.toString());

	}

	/**
	 * @param candidacyContact the candidacyContact to set
	 */
	public void setCandidacyContact(AddressInfoBase candidacyContact) {
		this.candidacyContact = candidacyContact;
	}

	/**
	 * @param companyInfo the companyInfo to set
	 */
	public void setCompanyInfo(CompanyInfo companyInfo) {
		this.companyInfo = companyInfo;
	}

	/**
	 * @param disciplineHDSAs the disciplineHDSAs to set
	 */
	public void setDisciplineHDSAs(List<DisciplineHDSA> disciplineHDSAs) {
		this.disciplineHDSAs = disciplineHDSAs;
	}

	/**
	 * @param employerInfo the employerInfo to set
	 */
	public void setEmployerInfo(EmployerInfo employerInfo) {
		this.employerInfo = employerInfo;
	}

	/**
	 * @param employerProvinceSelected the employerProvinceSelected to set
	 */
	public void setEmployerProvinceSelected(Province employerProvinceSelected) {
		this.employerProvinceSelected = employerProvinceSelected;
	}

	/**
	 * @param formInfo the formInfo to set
	 */
	public void setFormInfo(FormInfo formInfo) {
		this.formInfo = formInfo;
	}

	/**
	 * @param orgSizeInfo the orgSizeInfo to set
	 */
	public void setOrgSizeInfo(OrganisationSizeInfo orgSizeInfo) {
		this.orgSizeInfo = orgSizeInfo;
	}

	/**
	 * @param addressInfo the addressInfo to set
	 */
	public void setPhysicalAddressInfo(AddressInfoBase physicalAddressInfo) {
		this.physicalAddressInfo = physicalAddressInfo;
	}

	/**
	 * @param postAddressInfo the postAddressInfo to set
	 */
	public void setPostAddressInfo(AddressInfoBase postAddressInfo) {
		this.postAddressInfo = postAddressInfo;
	}

	/**
	 * @return the alternateCandidacyContact
	 */
	public AddressInfoBase getAlternateCandidacyContact() {
		return alternateCandidacyContact;
	}

	/**
	 * @param alternateCandidacyContact the alternateCandidacyContact to set
	 */
	public void setAlternateCandidacyContact(AddressInfoBase alternateCandidacyContact) {
		this.alternateCandidacyContact = alternateCandidacyContact;
	}

	/**
	 * @return the orgContact
	 */
	public AddressInfoBase getOrgContact() {
		return orgContact;
	}

	/**
	 * @param orgContact the orgContact to set
	 */
	public void setOrgContact(AddressInfoBase orgContact) {
		this.orgContact = orgContact;
	}

	/**
	 * @return the alternateOrgContact
	 */
	public AddressInfoBase getAlternateOrgContact() {
		return alternateOrgContact;
	}

	/**
	 * @param alternateOrgContact the alternateOrgContact to set
	 */
	public void setAlternateOrgContact(AddressInfoBase alternateOrgContact) {
		this.alternateOrgContact = alternateOrgContact;
	}

}