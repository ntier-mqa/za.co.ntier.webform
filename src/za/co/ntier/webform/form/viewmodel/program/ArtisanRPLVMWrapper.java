package za.co.ntier.webform.form.viewmodel.program;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.program.ArtisanRPLProgram;

public class ArtisanRPLVMWrapper {
	private ArtisanRPLProgram artisanRPLInfo;

	@Init
	public void init(@ExecutionArgParam("artisanRPLInfo") ArtisanRPLProgram artisanRPLInfo) {
		this.artisanRPLInfo = artisanRPLInfo;
	}

	/**
	 * @return the artisanRPLGrantInfo
	 */
	public ArtisanRPLProgram getArtisanRPLInfo() {
		return artisanRPLInfo;
	}

	/**
	 * @param artisanRPLGrantInfo the artisanRPLGrantInfo to set
	 */
	public void setArtisanRPLInfo(ArtisanRPLProgram artisanRPLInfo) {
		this.artisanRPLInfo = artisanRPLInfo;
	}
}
