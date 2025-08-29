package za.co.ntier.webform.form.viewmodel.program;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.program.NonArtisanDevProgram;

public class NonArtisanDevVMWrapper {
	private NonArtisanDevProgram nonArtisanDevInfo;
	@Init
	public void init(@ExecutionArgParam("nonArtisanDevInfo") NonArtisanDevProgram nonArtisanDevInfo) {
		this.setNonArtisanDevInfo(nonArtisanDevInfo);
	}
	/**
	 * @return the nonArtisanDevInfo
	 */
	public NonArtisanDevProgram getNonArtisanDevInfo() {
		return nonArtisanDevInfo;
	}
	/**
	 * @param nonArtisanDevInfo the nonArtisanDevInfo to set
	 */
	public void setNonArtisanDevInfo(NonArtisanDevProgram nonArtisanDevInfo) {
		this.nonArtisanDevInfo = nonArtisanDevInfo;
	}
	
}
