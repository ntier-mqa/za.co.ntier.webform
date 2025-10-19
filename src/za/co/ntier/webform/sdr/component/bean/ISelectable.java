package za.co.ntier.webform.sdr.component.bean;

/**
 * implement to handle event selected a item on UI like listbox
 */
public interface ISelectable {
	/**
	 * handle logic when a item is selected on UI
	 * @param selectedObj
	 */
	public void cmdSelected(Object selectedObj);
}
