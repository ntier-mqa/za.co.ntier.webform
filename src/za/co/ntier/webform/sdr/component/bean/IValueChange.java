package za.co.ntier.webform.sdr.component.bean;

import org.zkoss.zk.ui.event.InputEvent;

public interface IValueChange {
	public void cmdValueChange(String selectedObj);

	default void cmdValueChange(InputEvent event) {
		cmdValueChange(event.getValue());
	}

}
