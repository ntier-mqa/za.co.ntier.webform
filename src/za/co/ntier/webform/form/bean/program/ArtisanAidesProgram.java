package za.co.ntier.webform.form.bean.program;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.bean.AnnexureInfo;
import za.co.ntier.webform.form.bean.ColumnInfo;
import za.co.ntier.webform.form.bean.ProjectInput;

public class ArtisanAidesProgram {
	private AnnexureInfo qualification;
	private AnnexureInfo skill;
	
	
	public ArtisanAidesProgram() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		qualification = ProjectInput.getProject("QUALIFICATION",
						List.of(ColumnInfo.getColPositiveNumber("No. of Employed Learners"),
								ColumnInfo.getColPositiveNumber("No. of Unemployed Learners"),
								ColumnInfo.getColPositiveNumber("Total No. of Learners Applied For")));
				
		skill = ProjectInput.getProject("SKILLS PROGRAMME",
				List.of(ColumnInfo.getColPositiveNumber("No. of Employed Learners"),
						ColumnInfo.getColPositiveNumber("No. of Unemployed Learners"),
						ColumnInfo.getColPositiveNumber("Total No. of Learners Applied For")));
		
	}
	
	/**
	 * @return the skill
	 */
	public AnnexureInfo getSkill() {
		return skill;
	}
	/**
	 * @param skill the skill to set
	 */
	public void setSkill(AnnexureInfo skill) {
		this.skill = skill;
	}
	/**
	 * @return the qualification
	 */
	public AnnexureInfo getQualification() {
		return qualification;
	}
	/**
	 * @param qualification the qualification to set
	 */
	public void setQualification(AnnexureInfo qualification) {
		this.qualification = qualification;
	}
	
		
}
