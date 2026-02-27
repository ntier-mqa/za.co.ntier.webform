package za.co.ntier.webform.sdr.component.viewmodel;

import org.zkoss.bind.annotation.Init;

@Init(superclass = true)
public class DataListViewVM extends DataFormViewVM{
	public String getSclass(){
		String cmd = getComponent().isShowAddButton() ? "-command" : "";
		return "grid-listView " + getComponent().getSclass() + " grid-listView-" + String.valueOf(getComponent().getColumnInfos().size()) + cmd;
	}
}
