package za.co.ntier.webform.form.viewmodel;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.ProgramCetTvetInfo;

public class ProgramCetTvetInfoVMWrapper {
	private ProgramCetTvetInfo programCetTvetInfo;

	@Init
	public void init(@ExecutionArgParam("programCetTvetInfo") ProgramCetTvetInfo programCetTvetInfo) {
		this.setProgramCetTvetInfo(programCetTvetInfo);
	}

	public ProgramCetTvetInfo getProgramCetTvetInfo() {
		return programCetTvetInfo;
	}

	public void setProgramCetTvetInfo(ProgramCetTvetInfo programCetTvetInfo) {
		this.programCetTvetInfo = programCetTvetInfo;
	}
}
