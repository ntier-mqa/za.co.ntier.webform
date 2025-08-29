package za.co.ntier.webform.form.bean.program;

import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.LearnershipTableInfo;

public class NonArtisanDevProgram {
	private LearnershipTableInfo generalLearnership;
	private LearnershipTableInfo firLearnership;
	private MenuContextInfo menuContextInfo;
	
	public NonArtisanDevProgram(MenuContextInfo menuContextInfo) {
		this.setMenuContextInfo(menuContextInfo);
		setGeneralLearnership(LearnershipTableInfo.createGeneralLearnership(
						menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID(), menuContextInfo.getProgramType()));
		setFirLearnership(LearnershipTableInfo.create4IRLearnership(
						menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID(), menuContextInfo.getProgramType()));
	}

	/**
	 * @return the generalLearnership
	 */
	public LearnershipTableInfo getGeneralLearnership() {
		return generalLearnership;
	}

	/**
	 * @param generalLearnership the generalLearnership to set
	 */
	public void setGeneralLearnership(LearnershipTableInfo generalLearnership) {
		this.generalLearnership = generalLearnership;
	}

	/**
	 * @return the firLearnership
	 */
	public LearnershipTableInfo getFirLearnership() {
		return firLearnership;
	}

	/**
	 * @param firLearnership the firLearnership to set
	 */
	public void setFirLearnership(LearnershipTableInfo firLearnership) {
		this.firLearnership = firLearnership;
	}

	/**
	 * @return the menuContextInfo
	 */
	public MenuContextInfo getMenuContextInfo() {
		return menuContextInfo;
	}

	/**
	 * @param menuContextInfo the menuContextInfo to set
	 */
	public void setMenuContextInfo(MenuContextInfo menuContextInfo) {
		this.menuContextInfo = menuContextInfo;
	}
}
