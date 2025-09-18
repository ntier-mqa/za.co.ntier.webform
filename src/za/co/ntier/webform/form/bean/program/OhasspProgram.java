package za.co.ntier.webform.form.bean.program;

import java.util.List;
import java.util.Map;

import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.ProjectInput;
import za.co.ntier.webform.model.I_ZZLearnersApplied;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class OhasspProgram implements ISaveForm, IProgram {
	private ProjectInput healthSafetySkills;

	public OhasspProgram(X_ZZ_Application_Form applicationForm)  {
		ColumnInfo<?> colNameProgram = ColumnInfo.getColLabel(ColumnInfo.colNameProgrammeLabel, I_ZZLearnersApplied.COLUMNNAME_Name);
		healthSafetySkills = ProjectInput.getProject(
				List.of(colNameProgram,
						ColumnInfo.getColPositiveNumber(ColumnInfo.colNoEmployedLabel, I_ZZLearnersApplied.COLUMNNAME_ZZNoEmployedLearners)));
		ProjectInput.initProject(healthSafetySkills, applicationForm, 
				List.of(Map.of(colNameProgram, "Occupational Health and Safety Skills Programmes")));
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
		// update total
		applicationForm.saveEx(trxName);
		
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
