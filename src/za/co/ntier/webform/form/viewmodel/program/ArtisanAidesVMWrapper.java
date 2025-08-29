package za.co.ntier.webform.form.viewmodel.program;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.program.ArtisanAidesProgram;

public class ArtisanAidesVMWrapper {
	private ArtisanAidesProgram artisanAidesInfo;

	@Init
	public void init(@ExecutionArgParam("artisanAidesInfo") ArtisanAidesProgram artisanAidesInfo) {
		this.artisanAidesInfo = artisanAidesInfo;
	}

	/**
	 * @return the artisanAidesGrantInfo
	 */
	public ArtisanAidesProgram getArtisanAidesInfo() {
		return artisanAidesInfo;
	}

	/**
	 * @param artisanAidesGrantInfo the artisanAidesGrantInfo to set
	 */
	public void setArtisanAidesInfo(ArtisanAidesProgram artisanAidesInfo) {
		this.artisanAidesInfo = artisanAidesInfo;
	}
}
