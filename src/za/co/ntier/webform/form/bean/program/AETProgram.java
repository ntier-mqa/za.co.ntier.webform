package za.co.ntier.webform.form.bean.program;

import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.LearnershipTableInfo;

public class AETProgram {
	private LearnershipTableInfo aetLearnership;
	public AETProgram(MenuContextInfo menuContextInfo) {
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
