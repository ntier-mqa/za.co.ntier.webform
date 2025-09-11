package za.co.ntier.webform.form.bean.program;

import java.util.List;

import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.ProjectInput;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class OhasspProgram implements ISaveForm, IProgram {
	private ProjectInput healthSafetySkills;

	public OhasspProgram(X_ZZ_Application_Form applicationForm)  {
		setHealthSafetySkills(ProjectInput.getProject(
				List.of(ColumnInfo.getColLabel(ProjectInput.colNameProgrammeLabel),
						ColumnInfo.getColPositiveNumber(ProjectInput.colNoEmployedLabel)),
				"Occupational Health and Safety Skills Programmes", applicationForm));
	}

	/**
	 * @return the healthSafetySkills
	 */
	public ProjectInput getHealthSafetySkills() {
		return healthSafetySkills;
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm)  {
		ProjectInput.saveProjectInput(trxName, applicationForm, healthSafetySkills);
		
	}

	/**
	 * @param healthSafetySkills the healthSafetySkills to set
	 */
	public void setHealthSafetySkills(ProjectInput healthSafetySkills) {
		this.healthSafetySkills = healthSafetySkills;
	}

}
