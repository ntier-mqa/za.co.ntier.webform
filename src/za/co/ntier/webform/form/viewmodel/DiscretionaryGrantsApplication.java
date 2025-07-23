package za.co.ntier.webform.form.viewmodel;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import za.co.ntier.webform.form.viewmodel.master.MasterUtil;

public class DiscretionaryGrantsApplication {
	private CompanyInfo companyInfo;
	private FormInfo formInfo;
	
	private AddressInfoBase physicalAddressInfo;
	
	private AddressInfoBase postAddressInfo;
	
	private OrganisationSizeInfo orgSizeInfo;
	
	private EmployerInfo employerInfo;
	
	private Province employerProvinceSelected;
	
	Map<String, List<String>> formData;
	private List<Discipline> disciplines = Arrays.asList(new Discipline[] {new Discipline("Electrical Engineering GCC"),
			new Discipline("Mechanical Engineering GCC "),
			new Discipline("Mine Managers Certificate of Competence MMC"),
			new Discipline("Mine Survey GCC"),
			new Discipline("Mine Overseer"),
			new Discipline("Blasting Certificate")
			});
	
	@Command("toggleProvince")
	@NotifyChange("disciplines") // Notify ZK to update the 'disciplines' list
	public void toggleProvince(@BindingParam("discipline") Discipline discipline, 
			@BindingParam("province") Province province,
			@BindingParam("checked") boolean checked) {
		discipline.toggleProvince(province, checked);
	}
	/**
	 * @return the disciplines
	 */
	public List<Discipline> getDisciplines() {
		return disciplines;
	}

	/**
	 * @param disciplines the disciplines to set
	 */
	public void setDisciplines(List<Discipline> disciplines) {
		this.disciplines = disciplines;
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

	public DiscretionaryGrantsApplication() throws IOException {
        // Initialize with default values if needed
        
        setCompanyInfo(CompanyInfo.getDefaultCompanyInfo());
        setFormInfo(new FormInfo());
        
        physicalAddressInfo = new AddressInfoBase(AddressCategory.PHYSICAL, Province.KZN);
        physicalAddressInfo.setDistrictMunicipalitySelected(MasterUtil.districtMunicipalities.get(2));
        physicalAddressInfo.setLocalMunicipalitySelected(MasterUtil.localMunicipalities.get(3));
        physicalAddressInfo.setMunicipalityTypeSelected(MasterUtil.municipalityTypes.get(1));
        
        postAddressInfo = new AddressInfoBase(AddressCategory.POSTAL, Province.KZN);
        orgSizeInfo = new OrganisationSizeInfo(MasterUtil.organisationSizes.get(2), false, true);
        employerInfo = new EmployerInfo();
        employerInfo.setEmployerName("Le Quy Hiep");
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

	
}