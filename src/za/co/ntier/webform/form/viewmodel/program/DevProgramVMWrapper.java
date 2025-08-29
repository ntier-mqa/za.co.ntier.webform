package za.co.ntier.webform.form.viewmodel.program;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.program.DevProgramProgram;

public class DevProgramVMWrapper {
	private DevProgramProgram devProgramInfo;
	@Init
	public void init(@ExecutionArgParam("devProgramInfo") DevProgramProgram devProgramInfo) {
		this.setDevProgramInfo(devProgramInfo);
	}
	/**
	 * @return the devProgramInfo
	 */
	public DevProgramProgram getDevProgramInfo() {
		return devProgramInfo;
	}
	/**
	 * @param devProgramInfo the devProgramInfo to set
	 */
	public void setDevProgramInfo(DevProgramProgram devProgramInfo) {
		this.devProgramInfo = devProgramInfo;
	}
}
