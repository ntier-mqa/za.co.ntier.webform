package za.co.ntier.webform.form.viewmodel;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.OHASSPInfo;

public class OHASSPVMWrapper {
	private OHASSPInfo ohasspInfo;

	@Init
    public void init(@ExecutionArgParam("ohasspInfo") OHASSPInfo ohasspInfo) {
		this.setOhasspInfo(ohasspInfo);
    }

	/**
	 * @return the ohasspInfo
	 */
	public OHASSPInfo getOhasspInfo() {
		return ohasspInfo;
	}

	/**
	 * @param ohasspInfo the ohasspInfo to set
	 */
	public void setOhasspInfo(OHASSPInfo ohasspInfo) {
		this.ohasspInfo = ohasspInfo;
	}
}
