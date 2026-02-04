package za.co.ntier.webform.sdr.component.viewmodel;

import java.time.LocalDate;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyCommand;
import org.zkoss.bind.annotation.SmartNotifyChange;
import org.zkoss.bind.impl.BinderUtil;

import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.sdr.component.bean.TableModel;

@Init(superclass = true)
public class DataCardViewVM extends BaseComponentVM<TableModel>{

	@Command
	@SmartNotifyChange("indexInfo")
	public void cmdNew() {
		if (getComponent().getVirtualRow().validate())
			getComponent().newRow();
	}
	
	@Command
	@SmartNotifyChange("indexInfo")
	public void cmdDelete() {
		getComponent().deleteRow();
		getComponent().getVirtualRow().validate();
	}
	
	@Command
	@SmartNotifyChange("indexInfo")
	public void cmdPrev() {
		if (getComponent().getVirtualRow().validate())
			getComponent().navPrevRow();
	}
	
	@Command
	@SmartNotifyChange("indexInfo")
	public void cmdNext() {
		if (getComponent().getVirtualRow().validate())
			getComponent().navNextRow();
	}

	public String getIndexInfo() {
		return String.format("%s / %s", getComponent().getCurrentIndex() + 1, getComponent().getRows().size());
	}
}
