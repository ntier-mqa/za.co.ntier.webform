package za.co.ntier.webform.form.bean;

public class ArtisanRPLInfo {
	private OneLineTableInfo fullEmployedTableInfo;
	
	public ArtisanRPLInfo() {
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
