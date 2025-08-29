package za.co.ntier.webform.form.viewmodel.program;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.program.ArtisanDevProgram;

public class ArtisanDevVMWrapper {
	private ArtisanDevProgram artisanDevInfo;

	@Init
	public void init(@ExecutionArgParam("artisanDevInfo") ArtisanDevProgram artisanDevInfo) {
		this.setArtisanDevInfo(artisanDevInfo);
	}

	/**
	 * @return the artisanDevInfo
	 */
	public ArtisanDevProgram getArtisanDevInfo() {
		return artisanDevInfo;
	}

	/**
	 * @param artisanDevInfo the artisanDevInfo to set
	 */
	public void setArtisanDevInfo(ArtisanDevProgram artisanDevInfo) {
		this.artisanDevInfo = artisanDevInfo;
	}
}
