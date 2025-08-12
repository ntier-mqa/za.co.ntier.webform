package za.co.ntier.webform.form.viewmodel;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.ArtisanAidesGrantInfo;

public class ArtisanAidesGrantVMWrapper {
	private ArtisanAidesGrantInfo artisanAidesGrantInfo;

	@Init
	public void init(@ExecutionArgParam("artisanAidesGrantInfo") ArtisanAidesGrantInfo artisanAidesGrantInfo) {
		this.setArtisanAidesGrantInfo(artisanAidesGrantInfo);
	}

	/**
	 * @return the artisanAidesGrantInfo
	 */
	public ArtisanAidesGrantInfo getArtisanAidesGrantInfo() {
		return artisanAidesGrantInfo;
	}

	/**
	 * @param artisanAidesGrantInfo the artisanAidesGrantInfo to set
	 */
	public void setArtisanAidesGrantInfo(ArtisanAidesGrantInfo artisanAidesGrantInfo) {
		this.artisanAidesGrantInfo = artisanAidesGrantInfo;
	}
}
