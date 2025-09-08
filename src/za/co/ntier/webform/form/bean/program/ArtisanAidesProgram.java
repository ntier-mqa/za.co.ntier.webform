package za.co.ntier.webform.form.bean.program;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.adempiere.exceptions.AdempiereException;

import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.ProjectInput;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class ArtisanAidesProgram implements ISaveForm, IProgram {
	private ProjectInput qualification;
	private ProjectInput skill;

	public ArtisanAidesProgram() {
		qualification = ProjectInput.getProject(null,
				List.of(ColumnInfo.getColPositiveNumber(ProjectInput.colNoEmployedLabel),
						ColumnInfo.getColPositiveNumber(ProjectInput.colNoUnEmployedLabel),
						ColumnInfo.getColPositiveNumber(ProjectInput.colTotalLearnersLabel)));
		qualification.setSubSectionHeader("QUALIFICATION");
		
		skill = ProjectInput.getProject(null,
				List.of(ColumnInfo.getColPositiveNumber(ProjectInput.colNoEmployedLabel),
						ColumnInfo.getColPositiveNumber(ProjectInput.colNoUnEmployedLabel),
						ColumnInfo.getColPositiveNumber(ProjectInput.colTotalLearnersLabel)));
		skill.setSubSectionHeader("SKILLS PROGRAMME");
	}

	/**
	 * @return the qualification
	 */
	public ProjectInput getQualification() {
		return qualification;
	}

	/**
	 * @return the skill
	 */
	public ProjectInput getSkill() {
		return skill;
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm)  {
		ProjectInput.saveProjectInput(trxName, applicationForm, qualification);
		ProjectInput.saveProjectInput(trxName, applicationForm, skill);
		
	}

	/**
	 * @param qualification the qualification to set
	 */
	public void setQualification(ProjectInput qualification) {
		this.qualification = qualification;
	}

	/**
	 * @param skill the skill to set
	 */
	public void setSkill(ProjectInput skill) {
		this.skill = skill;
	}

}
