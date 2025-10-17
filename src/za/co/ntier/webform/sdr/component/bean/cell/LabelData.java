package za.co.ntier.webform.sdr.component.bean.cell;

import za.co.ntier.webform.sdr.component.bean.TableModel;

public class LabelData extends AbstractCellModel{
	public LabelData(TableModel annexure, RowModel row) {
		super(annexure, row);
	}

	private String value;

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
