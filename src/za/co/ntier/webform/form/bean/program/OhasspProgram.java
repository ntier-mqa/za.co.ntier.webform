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
		healthSafetySkills = ProjectInput.getProject(
				List.of(ColumnInfo.getColLabel(ColumnInfo.colNameProgrammeLabel),
						ColumnInfo.getColPositiveNumber(ColumnInfo.colNoEmployedLabel)));
		ProjectInput.initProject(healthSafetySkills, applicationForm, "Occupational Health and Safety Skills Programmes");
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

	@Override
	public boolean isProgramValid() {
		// TODO Auto-generated method stub
		return true;
	}

}
