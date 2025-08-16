package za.co.ntier.webform.form.bean;

public class OHASSPInfo {
	private OneLineTableInfo employedTableInfo;
	
	public OHASSPInfo() {
		this.setEmployedTableInfo(OneLineTableInfo.createEmployedTableInfo("Occupational Health and Safety Skills Programmes"));
	}

	/**
	 * @return the employedTableInfo
	 */
	public OneLineTableInfo getEmployedTableInfo() {
		return employedTableInfo;
	}

	/**
	 * @param employedTableInfo the employedTableInfo to set
	 */
	public void setEmployedTableInfo(OneLineTableInfo employedTableInfo) {
		this.employedTableInfo = employedTableInfo;
	}
}
