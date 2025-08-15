package za.co.ntier.webform.form.bean;

public class NonArtisanDevRPLInfo {
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
	
	public NonArtisanDevRPLInfo() {
		tableInfo = OneLineTableInfo.createNoTotalApplyTableInfo();
	}
}
