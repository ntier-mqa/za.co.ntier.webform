package za.co.ntier.webform.form.viewmodel;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.ProgramCetTvetInfo;
import za.co.ntier.webform.model.MAnnexure;

public class ProgramCetTvetInfoVMWrapper {
	private ProgramCetTvetInfo programCetTvetInfo;
	private String addSubLineLabel = "add more";

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

	public String getAddSubLineLabel() {
		return addSubLineLabel;
	}

	public void setAddSubLineLabel(String addSubLineLabel) {
		this.addSubLineLabel = addSubLineLabel;
	}
	
	@Command
	public void addSubLine(@BindingParam MAnnexure annexure) {
		annexure.addSubAnnexure();
	}
}
