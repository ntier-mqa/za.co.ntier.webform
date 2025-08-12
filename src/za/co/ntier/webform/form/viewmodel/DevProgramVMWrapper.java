package za.co.ntier.webform.form.viewmodel;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.DevProgramInfo;

public class DevProgramVMWrapper {
	private DevProgramInfo devProgramInfo;
	@Init
	public void init(@ExecutionArgParam("devProgramInfo") DevProgramInfo devProgramInfo) {
		this.setDevProgramInfo(devProgramInfo);
	}
	/**
	 * @return the devProgramInfo
	 */
	public DevProgramInfo getDevProgramInfo() {
		return devProgramInfo;
	}
	/**
	 * @param devProgramInfo the devProgramInfo to set
	 */
	public void setDevProgramInfo(DevProgramInfo devProgramInfo) {
		this.devProgramInfo = devProgramInfo;
	}
}
