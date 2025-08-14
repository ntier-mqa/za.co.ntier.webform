package za.co.ntier.webform.form.viewmodel;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.NonArtisanDevInfo;

public class NonArtisanDevVMWrapper {
	private NonArtisanDevInfo nonArtisanDevInfo;
	@Init
	public void init(@ExecutionArgParam("nonArtisanDevInfo") NonArtisanDevInfo nonArtisanDevInfo) {
		this.setNonArtisanDevInfo(nonArtisanDevInfo);
	}
	/**
	 * @return the nonArtisanDevInfo
	 */
	public NonArtisanDevInfo getNonArtisanDevInfo() {
		return nonArtisanDevInfo;
	}
	/**
	 * @param nonArtisanDevInfo the nonArtisanDevInfo to set
	 */
	public void setNonArtisanDevInfo(NonArtisanDevInfo nonArtisanDevInfo) {
		this.nonArtisanDevInfo = nonArtisanDevInfo;
	}
}
