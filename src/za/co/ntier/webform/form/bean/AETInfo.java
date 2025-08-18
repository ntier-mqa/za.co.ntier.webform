package za.co.ntier.webform.form.bean;

import za.co.ntier.webform.form.MenuContextInfo;

public class AETInfo {
	private LearnershipTableInfo aetLearnership;
	public AETInfo(MenuContextInfo menuContextInfo) {
		aetLearnership = LearnershipTableInfo.createGeneralLearnership(
					menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID(), menuContextInfo.getProgramType());
	}
	/**
	 * @return the aetLearnership
	 */
	public LearnershipTableInfo getAetLearnership() {
		return aetLearnership;
	}
	/**
	 * @param aetLearnership the aetLearnership to set
	 */
	public void setAetLearnership(LearnershipTableInfo aetLearnership) {
		this.aetLearnership = aetLearnership;
	}
}
