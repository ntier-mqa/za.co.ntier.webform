package za.co.ntier.webform.sdr.component.bean.column;

import za.co.ntier.webform.sdr.component.bean.ColumnModel;

public class CheckboxColumnModel extends ColumnModel {
	private boolean requireChecked = false;
	
	public CheckboxColumnModel(String colTitle) {
		super(colTitle);
	}
	
	public CheckboxColumnModel(String colTitle, String daoPropertyName) {
		super(colTitle, daoPropertyName);
	}

	public boolean isRequireChecked() {
		return requireChecked;
	}

	public CheckboxColumnModel setRequireChecked(boolean requireChecked) {
		this.requireChecked = requireChecked;
		return this;
	}

	

}
