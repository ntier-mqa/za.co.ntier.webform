package za.co.ntier.webform.form.viewmodel;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.ArtisanRPLInfo;

public class ArtisanRPLVMWrapper {
	private ArtisanRPLInfo artisanRPLInfo;

	@Init
	public void init(@ExecutionArgParam("artisanRPLInfo") ArtisanRPLInfo artisanRPLInfo) {
		this.artisanRPLInfo = artisanRPLInfo;
	}

	/**
	 * @return the artisanRPLGrantInfo
	 */
	public ArtisanRPLInfo getArtisanRPLInfo() {
		return artisanRPLInfo;
	}

	/**
	 * @param artisanRPLGrantInfo the artisanRPLGrantInfo to set
	 */
	public void setArtisanRPLInfo(ArtisanRPLInfo artisanRPLInfo) {
		this.artisanRPLInfo = artisanRPLInfo;
	}
}
