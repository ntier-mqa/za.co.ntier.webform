package za.co.ntier.webform.form.viewmodel.program;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.InputEvent;

import za.co.ntier.webform.form.bean.AnnexureInfo;
import za.co.ntier.webform.form.bean.ColumnInfo;
import za.co.ntier.webform.form.bean.program.ProgramCetTvetInfo;

/**
 * If super has an init method but its ChildViewModel doesn't, you can
 * add @Init(superclass=true) on the ChildViewModel to use super's init.
 */
@Init(superclass = true)
public class ProgramCetTvetInfoVMWrapper extends ProgramVMWrapper<ProgramCetTvetInfo>{
	private String addSubLineLabel = "add more";

	@Command
	public void addSubLine(@BindingParam("subAnnexure") AnnexureInfo subAnnexure) {
		subAnnexure.addRow();
	}

	public String getAddSubLineLabel() {
		return addSubLineLabel;
	}


	public void setAddSubLineLabel(String addSubLineLabel) {
		this.addSubLineLabel = addSubLineLabel;
	}


	@Command
	public void subAnnexureNumChange(@BindingParam("subAnnexure") AnnexureInfo subAnnexure,
			@BindingParam("col") ColumnInfo<?> detailCol, @ContextParam(ContextType.TRIGGER_EVENT) InputEvent event) {
		subAnnexure.valueNumChange(detailCol);
	}

}
