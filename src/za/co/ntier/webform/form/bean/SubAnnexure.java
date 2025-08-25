package za.co.ntier.webform.form.bean;

import java.util.HashMap;

public class SubAnnexure extends HashMap<String, Object> {

	private static final long serialVersionUID = 6640742369503743090L;
	
	private Object selectedItem;

	/**
	 * @return the selectedItem
	 */
	public Object getSelectedItem() {
		return selectedItem;
	}

	/**
	 * @param selectedItem the selectedItem to set
	 */
	public void setSelectedItem(Object selectedItem) {
		this.selectedItem = selectedItem;
	}
	
}
