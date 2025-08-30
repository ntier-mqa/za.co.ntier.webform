package za.co.ntier.webform.form.bean.program;

import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.LearnerInputTableInfo;
import za.co.ntier.webform.form.bean.OneLineTableInfo;
import za.co.ntier.webform.form.bean.TradeDisciplineInputTableInfo;

public class ArtisanDevProgram {
	private LearnerInputTableInfo tradeTableInfo;
	
	private OneLineTableInfo totalNumTableInfo;

	/**
	 * @return the tradeTableInfo
	 */
	public LearnerInputTableInfo getTradeTableInfo() {
		return tradeTableInfo;
	}

	/**
	 * @param tradeTableInfo the tradeTableInfo to set
	 */
	public void setTradeTableInfo(LearnerInputTableInfo tradeTableInfo) {
		this.tradeTableInfo = tradeTableInfo;
	}
	
	public ArtisanDevProgram(MenuContextInfo menuContextInfo) {
		tradeTableInfo = TradeDisciplineInputTableInfo.getTrade (menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID(), menuContextInfo.getProgramType());
		totalNumTableInfo = OneLineTableInfo.createNoTotalApplyTableInfo();
	}

	/**
	 * @return the totalNumTableInfo
	 */
	public OneLineTableInfo getTotalNumTableInfo() {
		return totalNumTableInfo;
	}

	/**
	 * @param totalNumTableInfo the totalNumTableInfo to set
	 */
	public void setTotalNumTableInfo(OneLineTableInfo totalNumTableInfo) {
		this.totalNumTableInfo = totalNumTableInfo;
	}
	
}
