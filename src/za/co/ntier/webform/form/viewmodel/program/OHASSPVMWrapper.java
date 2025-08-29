package za.co.ntier.webform.form.viewmodel.program;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.program.OHASSPProgram;

public class OHASSPVMWrapper {
	private OHASSPProgram ohasspInfo;

	@Init
    public void init(@ExecutionArgParam("ohasspInfo") OHASSPProgram ohasspInfo) {
		this.setOhasspInfo(ohasspInfo);
    }

	/**
	 * @return the ohasspInfo
	 */
	public OHASSPProgram getOhasspInfo() {
		return ohasspInfo;
	}

	/**
	 * @param ohasspInfo the ohasspInfo to set
	 */
	public void setOhasspInfo(OHASSPProgram ohasspInfo) {
		this.ohasspInfo = ohasspInfo;
	}
}
