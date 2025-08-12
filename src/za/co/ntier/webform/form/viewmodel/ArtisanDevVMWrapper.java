package za.co.ntier.webform.form.viewmodel;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.ArtisanDevInfo;

public class ArtisanDevVMWrapper {
	private ArtisanDevInfo artisanDevInfo;

	@Init
	public void init(@ExecutionArgParam("artisanDevInfo") ArtisanDevInfo artisanDevInfo) {
		this.setArtisanDevInfo(artisanDevInfo);
	}

	/**
	 * @return the artisanDevInfo
	 */
	public ArtisanDevInfo getArtisanDevInfo() {
		return artisanDevInfo;
	}

	/**
	 * @param artisanDevInfo the artisanDevInfo to set
	 */
	public void setArtisanDevInfo(ArtisanDevInfo artisanDevInfo) {
		this.artisanDevInfo = artisanDevInfo;
	}
}
