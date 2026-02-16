package za.co.ntier.webform.sdr.component.bean;

import org.zkoss.zk.ui.event.Event;

/**
 * implement to handle event selected a item on UI like listbox
 */
public interface ISelectable {
	/**
	 * handle logic when a item is selected on UI
	 * @param selectedObj
	 */
	public void cmdSelected(Event selectedEvent);
}
