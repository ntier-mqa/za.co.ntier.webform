package za.co.ntier.webform.form.viewmodel;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.ArtisanAidesInfo;

public class ArtisanAidesVMWrapper {
	private ArtisanAidesInfo artisanAidesInfo;

	@Init
	public void init(@ExecutionArgParam("artisanAidesInfo") ArtisanAidesInfo artisanAidesInfo) {
		this.artisanAidesInfo = artisanAidesInfo;
	}

	/**
	 * @return the artisanAidesGrantInfo
	 */
	public ArtisanAidesInfo getArtisanAidesInfo() {
		return artisanAidesInfo;
	}

	/**
	 * @param artisanAidesGrantInfo the artisanAidesGrantInfo to set
	 */
	public void setArtisanAidesInfo(ArtisanAidesInfo artisanAidesInfo) {
		this.artisanAidesInfo = artisanAidesInfo;
	}
}
