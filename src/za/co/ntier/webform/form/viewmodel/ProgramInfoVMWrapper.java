package za.co.ntier.webform.form.viewmodel;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.ProgramInfo;

public class ProgramInfoVMWrapper {

	private ProgramInfo programInfo;

	/**
	 * @return the programInfo
	 */
	public ProgramInfo getProgramInfo() {
		return programInfo;
	}

	@Init
	public void init(@ExecutionArgParam("programInfo") ProgramInfo programInfo) {
		this.programInfo = programInfo;

	}

	/**
	 * @param programInfo the programInfo to set
	 */
	public void setProgramInfo(ProgramInfo programInfo) {
		this.programInfo = programInfo;
	}

}
