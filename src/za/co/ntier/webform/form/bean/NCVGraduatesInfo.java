package za.co.ntier.webform.form.bean;

import za.co.ntier.webform.form.MenuContextInfo;

public class NCVGraduatesInfo {
	private OneLineTableInfo unEmployedTableInfo;

	/**
	 * @return the unEmployedTableInfo
	 */
	public OneLineTableInfo getUnEmployedTableInfo() {
		return unEmployedTableInfo;
	}

	/**
	 * @param unEmployedTableInfo the unEmployedTableInfo to set
	 */
	public void setUnEmployedTableInfo(OneLineTableInfo unEmployedTableInfo) {
		this.unEmployedTableInfo = unEmployedTableInfo;
	}
	
	public NCVGraduatesInfo(MenuContextInfo menuContextInfo) {
		this.unEmployedTableInfo = OneLineTableInfo.createUnEmployedTableInfo(menuContextInfo);
	}
}
