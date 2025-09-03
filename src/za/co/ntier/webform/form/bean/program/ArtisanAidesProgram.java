package za.co.ntier.webform.form.bean.program;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.ProjectInput;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class ArtisanAidesProgram implements ISaveForm, IProgram {
	private AnnexureInfo qualification;
	private AnnexureInfo skill;

	public ArtisanAidesProgram() throws NoSuchMethodException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		qualification = ProjectInput.getProject(null,
				List.of(ColumnInfo.getColPositiveNumber("No. of Employed Learners"),
						ColumnInfo.getColPositiveNumber("No. of Unemployed Learners"),
						ColumnInfo.getColPositiveNumber("Total No. of Learners Applied For")));

		skill = ProjectInput.getProject(null,
				List.of(ColumnInfo.getColPositiveNumber("No. of Employed Learners"),
						ColumnInfo.getColPositiveNumber("No. of Unemployed Learners"),
						ColumnInfo.getColPositiveNumber("Total No. of Learners Applied For")));

	}

	/**
	 * @return the qualification
	 */
	public AnnexureInfo getQualification() {
		return qualification;
	}

	/**
	 * @return the skill
	 */
	public AnnexureInfo getSkill() {
		return skill;
	}

	/**
	 * @param qualification the qualification to set
	 */
	public void setQualification(AnnexureInfo qualification) {
		this.qualification = qualification;
	}

	/**
	 * @param skill the skill to set
	 */
	public void setSkill(AnnexureInfo skill) {
		this.skill = skill;
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		// TODO Auto-generated method stub
		
	}

}
