package za.co.ntier.webform.form.bean.program;

import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.OneLineTableInfo;

public class NCVGraduatesProgram {
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
	
	public NCVGraduatesProgram(MenuContextInfo menuContextInfo) {
		this.unEmployedTableInfo = OneLineTableInfo.createUnEmployedTableInfo(menuContextInfo);
	}
}
