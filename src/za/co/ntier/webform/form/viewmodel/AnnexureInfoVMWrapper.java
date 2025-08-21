package za.co.ntier.webform.form.viewmodel;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.AnnexureInfo;

public class AnnexureInfoVMWrapper {
	private AnnexureInfo annexureInfo;
	
	@Init
	public void init(@ExecutionArgParam("annexureInfo") AnnexureInfo annexureInfo) {
		
	}

	public AnnexureInfo getAnnexureInfo() {
		return annexureInfo;
	}

	public void setAnnexureInfo(AnnexureInfo annexureInfo) {
		this.annexureInfo = annexureInfo;
	}
		
}
