package za.co.ntier.webform.form.viewmodel;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.AnnexureInfo;

public class AnnexureTableVMWrapper {
	private AnnexureInfo annexureInfo;

	
	
	@Init
	public void init(@ExecutionArgParam("annexureInfo") AnnexureInfo annexureInfo) {
		this.setAnnexureInfo(annexureInfo);
	}



	/**
	 * @return the annexureInfo
	 */
	public AnnexureInfo getAnnexureInfo() {
		return annexureInfo;
	}



	/**
	 * @param annexureInfo the annexureInfo to set
	 */
	public void setAnnexureInfo(AnnexureInfo annexureInfo) {
		this.annexureInfo = annexureInfo;
	}
}
