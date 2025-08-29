package za.co.ntier.webform.form.bean.program;

import za.co.ntier.webform.form.bean.OneLineTableInfo;

public class NonArtisanDevRPLProgram {
	private OneLineTableInfo tableInfo;

	/**
	 * @return the tableInfo
	 */
	public OneLineTableInfo getTableInfo() {
		return tableInfo;
	}

	/**
	 * @param tableInfo the tableInfo to set
	 */
	public void setTableInfo(OneLineTableInfo tableInfo) {
		this.tableInfo = tableInfo;
	}
	
	public NonArtisanDevRPLProgram() {
		tableInfo = OneLineTableInfo.createNoTotalApplyTableInfo();
	}
}
