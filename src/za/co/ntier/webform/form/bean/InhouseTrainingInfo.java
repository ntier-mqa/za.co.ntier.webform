package za.co.ntier.webform.form.bean;

public class InhouseTrainingInfo {
	private OneLineTableInfo learnersTableInfo;
	public InhouseTrainingInfo() {
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
