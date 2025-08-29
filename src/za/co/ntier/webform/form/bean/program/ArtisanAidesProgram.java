package za.co.ntier.webform.form.bean.program;

import za.co.ntier.webform.form.bean.OneLineTableInfo;

public class ArtisanAidesProgram {
	private OneLineTableInfo qualificationTableInfo;
	private OneLineTableInfo skillTableInfo;
	
	public ArtisanAidesProgram() {
		this.qualificationTableInfo = OneLineTableInfo.createFullEmployedTableInfo();
		this.skillTableInfo = OneLineTableInfo.createFullEmployedTableInfo();
	}
	/**
	 * @return the qualificationTableInfo
	 */
	public OneLineTableInfo getQualificationTableInfo() {
		return qualificationTableInfo;
	}
	/**
	 * @param qualificationTableInfo the qualificationTableInfo to set
	 */
	public void setQualificationTableInfo(OneLineTableInfo qualificationTableInfo) {
		this.qualificationTableInfo = qualificationTableInfo;
	}
	/**
	 * @return the skillTableInfo
	 */
	public OneLineTableInfo getSkillTableInfo() {
		return skillTableInfo;
	}
	/**
	 * @param skillTableInfo the skillTableInfo to set
	 */
	public void setSkillTableInfo(OneLineTableInfo skillTableInfo) {
		this.skillTableInfo = skillTableInfo;
	}
	
		
}
