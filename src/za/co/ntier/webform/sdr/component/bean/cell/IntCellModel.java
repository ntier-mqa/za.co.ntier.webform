package za.co.ntier.webform.sdr.component.bean.cell;

import za.co.ntier.webform.sdr.component.bean.BaseCellModel;
import za.co.ntier.webform.sdr.component.bean.BaseColumnModel;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;

public class IntCellModel extends BaseCellModel {
	private Integer value;

	public IntCellModel(TableModel annexure, RowModel row, BaseColumnModel colModel) {
		super(annexure, row, colModel);
	}

	public IntCellModel(TableModel annexure, Integer value) {
		super(annexure, null, null);
		setValue(value);
	}

	/**
	 * @return the number
	 */
	@Override
	public Integer getValue() {
		return value;
	}


	/**
	 * @param number the number to set
	 */
	public void setValue(Integer value) {
		this.value = value;

	}

	@Override
	public int getCellType() {
		return POSITIVE_NUM_CELL;
	}
}
