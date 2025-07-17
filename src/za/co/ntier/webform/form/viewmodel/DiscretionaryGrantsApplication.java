package za.co.ntier.webform.form.viewmodel;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import za.co.ntier.webform.form.viewmodel.Discipline.Province;

public class DiscretionaryGrantsApplication {
	private CompanyInfo companyInfo;
	private FormInfo formInfo;
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
	
	public List<Province> getAllProvinces() {
        return Arrays.asList(Discipline.Province.values());
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

	// --- Employer/Applicant Information ---
    private String employerName;
    private String sdlNumber;
    private String siteSdlNumber;
    private String organisationRegistrationNumber;

    // --- Physical Address ---
    private String physicalStreetName;
    private String physicalAreaSuburb;
    private String physicalCityTown;
    private String physicalProvince;
    private String physicalPostalCode;
    private String physicalDistrictMunicipalities;
    private String physicalLocalMunicipalities;
    private String physicalRuralUrban;

    // --- Postal Address ---
    private String postalAddress;
    private String postalAreaSuburb;
    private String postalCityTown;
    private String postalProvince;
    private String postalPostalCode;

    // --- Size of the Organisation ---
    private String organisationSize; // "Small", "Medium", "Large"
    private boolean submittedWSPATR;
    private boolean submittedPivotalPlan;
    private boolean workplaceApproved;

    // --- For demonstration output ---
    private String savedDataOutput;

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
        this.employerName = "";
        this.sdlNumber = "";
        this.siteSdlNumber = "";
        this.organisationRegistrationNumber = "";

        this.physicalStreetName = "";
        this.physicalAreaSuburb = "";
        this.physicalCityTown = "";
        this.physicalProvince = "";
        this.physicalPostalCode = "";
        this.physicalDistrictMunicipalities = "";
        this.physicalLocalMunicipalities = "";
        this.physicalRuralUrban = "";

        this.postalAddress = "";
        this.postalAreaSuburb = "";
        this.postalCityTown = "";
        this.postalProvince = "";
        this.postalPostalCode = "";

        this.organisationSize = ""; // Default to empty
        this.submittedWSPATR = false;
        this.submittedPivotalPlan = false;
        this.workplaceApproved = false;

        this.savedDataOutput = "Fill the form and click 'Save Data'.";
        
        setCompanyInfo(CompanyInfo.getDefaultCompanyInfo());
        setFormInfo(new FormInfo());
    }

    // --- Getters and Setters for Data Binding ---

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public String getSdlNumber() {
        return sdlNumber;
    }

    public void setSdlNumber(String sdlNumber) {
        this.sdlNumber = sdlNumber;
    }

    public String getSiteSdlNumber() {
        return siteSdlNumber;
    }

    public void setSiteSdlNumber(String siteSdlNumber) {
        this.siteSdlNumber = siteSdlNumber;
    }

    public String getOrganisationRegistrationNumber() {
        return organisationRegistrationNumber;
    }

    public void setOrganisationRegistrationNumber(String organisationRegistrationNumber) {
        this.organisationRegistrationNumber = organisationRegistrationNumber;
    }

    public String getPhysicalStreetName() {
        return physicalStreetName;
    }

    public void setPhysicalStreetName(String physicalStreetName) {
        this.physicalStreetName = physicalStreetName;
    }

    public String getPhysicalAreaSuburb() {
        return physicalAreaSuburb;
    }

    public void setPhysicalAreaSuburb(String physicalAreaSuburb) {
        this.physicalAreaSuburb = physicalAreaSuburb;
    }

    public String getPhysicalCityTown() {
        return physicalCityTown;
    }

    public void setPhysicalCityTown(String physicalCityTown) {
        this.physicalCityTown = physicalCityTown;
    }

    public String getPhysicalProvince() {
        return physicalProvince;
    }

    public void setPhysicalProvince(String physicalProvince) {
        this.physicalProvince = physicalProvince;
    }

    public String getPhysicalPostalCode() {
        return physicalPostalCode;
    }

    public void setPhysicalPostalCode(String physicalPostalCode) {
        this.physicalPostalCode = physicalPostalCode;
    }

    public String getPhysicalDistrictMunicipalities() {
        return physicalDistrictMunicipalities;
    }

    public void setPhysicalDistrictMunicipalities(String physicalDistrictMunicipalities) {
        this.physicalDistrictMunicipalities = physicalDistrictMunicipalities;
    }

    public String getPhysicalLocalMunicipalities() {
        return physicalLocalMunicipalities;
    }

    public void setPhysicalLocalMunicipalities(String physicalLocalMunicipalities) {
        this.physicalLocalMunicipalities = physicalLocalMunicipalities;
    }

    public String getPhysicalRuralUrban() {
        return physicalRuralUrban;
    }

    public void setPhysicalRuralUrban(String physicalRuralUrban) {
        this.physicalRuralUrban = physicalRuralUrban;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getPostalAreaSuburb() {
        return postalAreaSuburb;
    }

    public void setPostalAreaSuburb(String postalAreaSuburb) {
        this.postalAreaSuburb = postalAreaSuburb;
    }

    public String getPostalCityTown() {
        return postalCityTown;
    }

    public void setPostalCityTown(String postalCityTown) {
        this.postalCityTown = postalCityTown;
    }

    public String getPostalProvince() {
        return postalProvince;
    }

    public void setPostalProvince(String postalProvince) {
        this.postalProvince = postalProvince;
    }

    public String getPostalPostalCode() {
        return postalPostalCode;
    }

    public void setPostalPostalCode(String postalPostalCode) {
        this.postalPostalCode = postalPostalCode;
    }

    public String getOrganisationSize() {
        return organisationSize;
    }

    public void setOrganisationSize(String organisationSize) {
        this.organisationSize = organisationSize;
    }

    public boolean isSubmittedWSPATR() {
        return submittedWSPATR;
    }

    public void setSubmittedWSPATR(boolean submittedWSPATR) {
        this.submittedWSPATR = submittedWSPATR;
    }

    public boolean isSubmittedPivotalPlan() {
        return submittedPivotalPlan;
    }

    public void setSubmittedPivotalPlan(boolean submittedPivotalPlan) {
        this.submittedPivotalPlan = submittedPivotalPlan;
    }

    public boolean isWorkplaceApproved() {
        return workplaceApproved;
    }

    public void setWorkplaceApproved(boolean workplaceApproved) {
        this.workplaceApproved = workplaceApproved;
    }

    public String getSavedDataOutput() {
        return savedDataOutput;
    }

    public void setSavedDataOutput(String savedDataOutput) {
        this.savedDataOutput = savedDataOutput;
    }

    // --- Command Method ---
    @Command
    @NotifyChange("savedDataOutput") // Notify ZK to update the 'savedDataOutput' label
    public void saveEmployerInfo() {
        // In a real application, you would save this data to a database or perform business logic.
        // For this example, we'll just display the collected data.
        StringBuilder sb = new StringBuilder();
        sb.append("--- Employer Information ---\n");
        sb.append("Employer Name: ").append(employerName).append("\n");
        sb.append("SDL Number: ").append(sdlNumber).append("\n");
        sb.append("Site SDL Number: ").append(siteSdlNumber).append("\n");
        sb.append("Organisation Registration Number: ").append(organisationRegistrationNumber).append("\n\n");

        sb.append("--- Physical Address ---\n");
        sb.append("Street Name and Number: ").append(physicalStreetName).append("\n");
        sb.append("Area/Suburb: ").append(physicalAreaSuburb).append("\n");
        sb.append("City/Town: ").append(physicalCityTown).append("\n");
        sb.append("Province: ").append(physicalProvince).append("\n");
        sb.append("Postal Code: ").append(physicalPostalCode).append("\n");
        sb.append("District Municipalities: ").append(physicalDistrictMunicipalities).append("\n");
        sb.append("Local Municipalities: ").append(physicalLocalMunicipalities).append("\n");
        sb.append("Rural or Urban: ").append(physicalRuralUrban).append("\n\n");

        sb.append("--- Postal Address ---\n");
        sb.append("Post Address: ").append(postalAddress).append("\n");
        sb.append("Area/Suburb: ").append(postalAreaSuburb).append("\n");
        sb.append("City/Town: ").append(postalCityTown).append("\n");
        sb.append("Province: ").append(postalProvince).append("\n");
        sb.append("Postal Code: ").append(postalPostalCode).append("\n\n");

        sb.append("--- Size of the Organisation ---\n");
        sb.append("Organisation Size: ").append(organisationSize).append("\n");
        sb.append("Submitted WSP/ATR in 2022?: ").append(submittedWSPATR ? "Yes" : "No").append("\n");
        sb.append("Submitted Pivotal Plan and Report in 2022?: ").append(submittedPivotalPlan ? "Yes" : "No").append("\n");
        sb.append("Is workplace approved?: ").append(workplaceApproved ? "Yes" : "No").append("\n");

        this.setSavedDataOutput(sb.toString());
        System.out.println("Employer Info Saved:\n" + sb.toString());

    }

	
}