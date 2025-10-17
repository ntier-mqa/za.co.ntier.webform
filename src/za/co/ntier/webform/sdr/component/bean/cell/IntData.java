package za.co.ntier.webform.sdr.component.bean.cell;

import za.co.ntier.webform.sdr.component.bean.TableModel;

public class IntData extends AbstractCellModel {
	private Integer value;

	public IntData(TableModel annexure, RowModel row, Integer value) {
		super(annexure, row);
		this.value = value;
	}

	/**
	 * @return the number
	 */
	public Integer getValue() {
		return value;
	}


	/**
	 * @param number the number to set
	 */
	public void setValue(Integer value) {
		this.value = value;
		
	}
}
