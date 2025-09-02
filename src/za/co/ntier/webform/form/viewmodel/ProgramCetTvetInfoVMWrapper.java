package za.co.ntier.webform.form.viewmodel;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.InputEvent;

import za.co.ntier.webform.form.bean.AnnexureInfo;
import za.co.ntier.webform.form.bean.ColumnInfo;
import za.co.ntier.webform.form.bean.ProgramCetTvetInfo;

public class ProgramCetTvetInfoVMWrapper {
	private ProgramCetTvetInfo programCetTvetInfo;
	private String addSubLineLabel = "add more";

	@Command
	public void addSubLine(@BindingParam("subAnnexure") AnnexureInfo subAnnexure) {
		subAnnexure.addRow();
	}

	public String getAddSubLineLabel() {
		return addSubLineLabel;
	}

	public ProgramCetTvetInfo getProgramCetTvetInfo() {
		return programCetTvetInfo;
	}

	@Init
	public void init(@ExecutionArgParam("programCetTvetInfo") ProgramCetTvetInfo programCetTvetInfo) {
		this.setProgramCetTvetInfo(programCetTvetInfo);
	}

	public void setAddSubLineLabel(String addSubLineLabel) {
		this.addSubLineLabel = addSubLineLabel;
	}

	public void setProgramCetTvetInfo(ProgramCetTvetInfo programCetTvetInfo) {
		this.programCetTvetInfo = programCetTvetInfo;
	}

	@Command
	public void subAnnexureNumChange(@BindingParam("subAnnexure") AnnexureInfo subAnnexure,
			@BindingParam("col") ColumnInfo<?> detailCol, @ContextParam(ContextType.TRIGGER_EVENT) InputEvent event) {
		subAnnexure.valueNumChange(detailCol);
	}

}
