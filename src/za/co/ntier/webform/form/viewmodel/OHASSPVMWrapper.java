package za.co.ntier.webform.form.viewmodel;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.OHASSPInfo;

public class OHASSPVMWrapper {
	private OHASSPInfo oHASSPInfo;

	@Init
    public void init(@ExecutionArgParam("oHASSPInfo") OHASSPInfo oHASSPInfo) {
		this.setoHASSPInfo(oHASSPInfo);
    }

	/**
	 * @return the oHASSPInfo
	 */
	public OHASSPInfo getoHASSPInfo() {
		return oHASSPInfo;
	}

	/**
	 * @param oHASSPInfo the oHASSPInfo to set
	 */
	public void setoHASSPInfo(OHASSPInfo oHASSPInfo) {
		this.oHASSPInfo = oHASSPInfo;
	}
}
