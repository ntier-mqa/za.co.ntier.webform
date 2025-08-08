package za.co.ntier.webform.form.viewmodel;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.OrganisationSizeInfo;
import za.co.ntier.webform.form.bean.WorkInfo;

public class WorkInfoVMWrapper {

	private WorkInfo workInfo;

	public WorkInfo getWorkInfo() {
		return workInfo;
	}

	public void setWorkInfo(WorkInfo workInfo) {
		this.workInfo = workInfo;
	}	

	@Init
	public void init(@ExecutionArgParam("workInfo") WorkInfo workInfo) {
		this.setWorkInfo(workInfo);
	}

	
}
