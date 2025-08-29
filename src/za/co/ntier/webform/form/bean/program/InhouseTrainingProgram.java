package za.co.ntier.webform.form.bean.program;

import za.co.ntier.webform.form.bean.OneLineTableInfo;

public class InhouseTrainingProgram {
	private OneLineTableInfo learnersTableInfo;
	public InhouseTrainingProgram() {
		this.learnersTableInfo = OneLineTableInfo.createLearnersTableInfo("Inhouse /Industry/Company based short courses");
	}
	/**
	 * @return the learnersTableInfo
	 */
	public OneLineTableInfo getLearnersTableInfo() {
		return learnersTableInfo;
	}
	/**
	 * @param learnersTableInfo the learnersTableInfo to set
	 */
	public void setLearnersTableInfo(OneLineTableInfo learnersTableInfo) {
		this.learnersTableInfo = learnersTableInfo;
	}
}
