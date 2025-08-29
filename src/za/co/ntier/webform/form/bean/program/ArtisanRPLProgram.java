package za.co.ntier.webform.form.bean.program;

import za.co.ntier.webform.form.bean.OneLineTableInfo;

public class ArtisanRPLProgram {
	private OneLineTableInfo fullEmployedTableInfo;
	
	public ArtisanRPLProgram() {
		this.fullEmployedTableInfo = OneLineTableInfo.createFullEmployedTableInfo();
	}

	/**
	 * @return the fullEmployedTableInfo
	 */
	public OneLineTableInfo getFullEmployedTableInfo() {
		return fullEmployedTableInfo;
	}

	/**
	 * @param fullEmployedTableInfo the fullEmployedTableInfo to set
	 */
	public void setFullEmployedTableInfo(OneLineTableInfo fullEmployedTableInfo) {
		this.fullEmployedTableInfo = fullEmployedTableInfo;
	}
}
