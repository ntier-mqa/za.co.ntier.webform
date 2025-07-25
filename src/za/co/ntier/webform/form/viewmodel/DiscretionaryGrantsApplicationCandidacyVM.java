package za.co.ntier.webform.form.viewmodel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import za.co.ntier.webform.form.bean.AddressCategory;
import za.co.ntier.webform.form.bean.AddressInfoBase;
import za.co.ntier.webform.form.bean.CompanyInfo;
import za.co.ntier.webform.form.bean.DisciplineHDSA;
import za.co.ntier.webform.form.bean.EmployerInfo;
import za.co.ntier.webform.form.bean.FormInfo;
import za.co.ntier.webform.form.bean.OrganisationSizeInfo;
import za.co.ntier.webform.form.bean.Province;
import za.co.ntier.webform.form.viewmodel.master.MasterUtil;

public class DiscretionaryGrantsApplicationCandidacyVM {
	private CompanyInfo companyInfo;
	private FormInfo formInfo;
	
	private EmployerInfo employerInfo;
	
	private AddressInfoBase physicalAddressInfo;
	
	private AddressInfoBase postAddressInfo;
	
	private OrganisationSizeInfo orgSizeInfo;
	
	private List<DisciplineHDSA> disciplineHDSAs;
	
	private AddressInfoBase candidacyContact;
	
	private Province employerProvinceSelected;
	
	Map<String, List<String>> formData;
		
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

	public DiscretionaryGrantsApplicationCandidacyVM() throws IOException {
        // Initialize with default values if needed
        
        setCompanyInfo(CompanyInfo.getDefaultCompanyInfo());
        setFormInfo(new FormInfo());
        
        employerInfo = new EmployerInfo();
        employerInfo.setEmployerName("Le Quy Hiep");
        postAddressInfo = new AddressInfoBase(AddressCategory.POSTAL, Province.KZN);
        
        physicalAddressInfo = new AddressInfoBase(AddressCategory.PHYSICAL, Province.NC);
        physicalAddressInfo.setDistrictMunicipalitySelected(MasterUtil.districtMunicipalities.get(2));
        physicalAddressInfo.setLocalMunicipalitySelected(MasterUtil.localMunicipalities.get(3));
        physicalAddressInfo.setMunicipalityTypeSelected(MasterUtil.municipalityTypes.get(1));
        
        orgSizeInfo = new OrganisationSizeInfo(MasterUtil.organisationSizes.get(2), false, true);
        
        disciplineHDSAs =  List.of(
        		new DisciplineHDSA("Electrical Engineering GCC", 10, "4000"),
				new DisciplineHDSA("Mechanical Engineering GCC", 5, "5000"),
				new DisciplineHDSA("Mine Managers Certificate of Competence MMC", 5, "5000"),
				new DisciplineHDSA("Mine Survey GCC", 5, "5000"),
				new DisciplineHDSA("Mine Overseer", 5, "5000"),
				new DisciplineHDSA("Blasting Certificate", 5, "5000"));

        disciplineHDSAs = new ArrayList<DisciplineHDSA>(disciplineHDSAs);
        
        candidacyContact = new AddressInfoBase(AddressCategory.CANDIDACY_CONTACT, Province.WS);
    }

    // --- Getters and Setters for Data Binding ---

    // --- Command Method ---
    @Command
    @NotifyChange("savedDataOutput") // Notify ZK to update the 'savedDataOutput' label
    public void saveEmployerInfo() {
        // In a real application, you would save this data to a database or perform business logic.
        // For this example, we'll just display the collected data.
        StringBuilder sb = new StringBuilder();
        System.out.println("Employer Info Saved:\n" + sb.toString());

    }
	/**
	 * @return the employerProvinceSelected
	 */
	public Province getEmployerProvinceSelected() {
		return employerProvinceSelected;
	}
	/**
	 * @param employerProvinceSelected the employerProvinceSelected to set
	 */
	public void setEmployerProvinceSelected(Province employerProvinceSelected) {
		this.employerProvinceSelected = employerProvinceSelected;
	}
	/**
	 * @return the addressInfo
	 */
	public AddressInfoBase getPhysicalAddressInfo() {
		return physicalAddressInfo;
	}
	/**
	 * @param addressInfo the addressInfo to set
	 */
	public void setPhysicalAddressInfo(AddressInfoBase physicalAddressInfo) {
		this.physicalAddressInfo = physicalAddressInfo;
	}
	/**
	 * @return the postAddressInfo
	 */
	public AddressInfoBase getPostAddressInfo() {
		return postAddressInfo;
	}
	/**
	 * @param postAddressInfo the postAddressInfo to set
	 */
	public void setPostAddressInfo(AddressInfoBase postAddressInfo) {
		this.postAddressInfo = postAddressInfo;
	}
	/**
	 * @return the orgSizeInfo
	 */
	public OrganisationSizeInfo getOrgSizeInfo() {
		return orgSizeInfo;
	}
	/**
	 * @param orgSizeInfo the orgSizeInfo to set
	 */
	public void setOrgSizeInfo(OrganisationSizeInfo orgSizeInfo) {
		this.orgSizeInfo = orgSizeInfo;
	}
	/**
	 * @return the employerInfo
	 */
	public EmployerInfo getEmployerInfo() {
		return employerInfo;
	}
	/**
	 * @param employerInfo the employerInfo to set
	 */
	public void setEmployerInfo(EmployerInfo employerInfo) {
		this.employerInfo = employerInfo;
	}
	/**
	 * @return the candidacyContact
	 */
	public AddressInfoBase getCandidacyContact() {
		return candidacyContact;
	}
	/**
	 * @param candidacyContact the candidacyContact to set
	 */
	public void setCandidacyContact(AddressInfoBase candidacyContact) {
		this.candidacyContact = candidacyContact;
	}
	/**
	 * @return the disciplineHDSAs
	 */
	public List<DisciplineHDSA> getDisciplineHDSAs() {
		return disciplineHDSAs;
	}
	/**
	 * @param disciplineHDSAs the disciplineHDSAs to set
	 */
	public void setDisciplineHDSAs(List<DisciplineHDSA> disciplineHDSAs) {
		this.disciplineHDSAs = disciplineHDSAs;
	}
	

	
}