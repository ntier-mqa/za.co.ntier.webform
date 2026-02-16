package za.co.ntier.webform.sdr.component.viewmodel;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.SmartNotifyChange;

import za.co.ntier.webform.sdr.component.bean.TableModel;

@Init(superclass = true)
public class DataCardViewVM extends BaseComponentVM<TableModel>{

	@Command
	@SmartNotifyChange("indexInfo")
	public void cmdNew() {
		if (getComponent().getVirtualRow().validate(null))
			getComponent().newRow();
	}
	
	@Command
	@SmartNotifyChange("indexInfo")
	public void cmdDelete() {
		getComponent().deleteRow();
		getComponent().getVirtualRow().validate(null);
	}
	
	@Command
	@SmartNotifyChange("indexInfo")
	public void cmdPrev() {
		if (getComponent().getVirtualRow().validate(null))
			getComponent().navPrevRow();
	}
	
	@Command
	@SmartNotifyChange("indexInfo")
	public void cmdNext() {
		if (getComponent().getVirtualRow().validate(null))
			getComponent().navNextRow();
	}

	public String getIndexInfo() {
		return String.format("%s / %s", getComponent().getCurrentIndex() + 1, getComponent().getRows().size());
	}
}
