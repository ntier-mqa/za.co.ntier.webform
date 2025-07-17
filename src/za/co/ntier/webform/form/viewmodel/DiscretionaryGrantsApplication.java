package za.co.ntier.webform.form.viewmodel;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

public class DiscretionaryGrantsApplication {
	private CompanyInfo companyInfo;
	private FormInfo formInfo;
	
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

	
}