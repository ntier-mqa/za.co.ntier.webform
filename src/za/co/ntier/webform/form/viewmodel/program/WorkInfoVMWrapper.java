package za.co.ntier.webform.form.viewmodel.program;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.program.WorkProgram;

public class WorkInfoVMWrapper {

	private WorkProgram workInfo;

	public WorkProgram getWorkInfo() {
		return workInfo;
	}

	public void setWorkInfo(WorkProgram workInfo) {
		this.workInfo = workInfo;
	}	

	@Init
	public void init(@ExecutionArgParam("workInfo") WorkProgram workInfo) {
		this.setWorkInfo(workInfo);
	}

	
}
