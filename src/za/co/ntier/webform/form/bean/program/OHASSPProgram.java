package za.co.ntier.webform.form.bean.program;

import za.co.ntier.webform.form.bean.OneLineTableInfo;

public class OHASSPProgram {
	private OneLineTableInfo employedTableInfo;
	
	public OHASSPProgram() {
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
