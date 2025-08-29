package za.co.ntier.webform.form.viewmodel.program;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.program.AETProgram;

public class AETVMWrapper {
    private AETProgram aetInfo;

    @Init
    public void init(@ExecutionArgParam("aetInfo") AETProgram aetInfo) {
        this.aetInfo = aetInfo;
    }

	/**
	 * @return the aetInfo
	 */
	public AETProgram getAetInfo() {
		return aetInfo;
	}

	/**
	 * @param aetInfo the aetInfo to set
	 */
	public void setAetInfo(AETProgram aetInfo) {
		this.aetInfo = aetInfo;
	}

}
