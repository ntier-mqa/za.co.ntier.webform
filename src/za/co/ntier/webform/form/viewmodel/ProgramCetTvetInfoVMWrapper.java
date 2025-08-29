package za.co.ntier.webform.form.viewmodel;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.SelectEvent;

import za.co.ntier.webform.form.bean.AnnexureInfo;
import za.co.ntier.webform.form.bean.ColumnInfo;
import za.co.ntier.webform.form.bean.ProgramCetTvetInfo;
import za.co.ntier.webform.form.bean.SubAnnexure;

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
	public void subAnnexureTradeSelection ( 
			@BindingParam("subAnnexure") SubAnnexure subAnnexure,
			@ContextParam (ContextType.TRIGGER_EVENT) SelectEvent<Component, Object> event){
		
		/*Iterator<Object> iSelectedObj = event.getSelectedObjects().iterator();
		if (iSelectedObj.hasNext()) {
			subAnnexure.setSelectedItem(iSelectedObj.next());
		}else {
			subAnnexure.setSelectedItem(null);
		}*/
		
		
	}
	
	@Command
	public void subAnnexureNumChange ( 
			@BindingParam("subAnnexure") AnnexureInfo subAnnexure,
			@BindingParam("col") ColumnInfo<?> detailCol,
			@ContextParam (ContextType.TRIGGER_EVENT) InputEvent event){
		subAnnexure.valueNumChange(detailCol);
	}
	
	@Command
	public void addSubLine(
			@BindingParam("subAnnexure") AnnexureInfo subAnnexure){
		subAnnexure.addRow();
	}
	
}
