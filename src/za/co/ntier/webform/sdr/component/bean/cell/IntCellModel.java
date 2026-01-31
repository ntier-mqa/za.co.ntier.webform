package za.co.ntier.webform.sdr.component.bean.cell;

import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;

/**
 * use for total line only
 */
public class IntCellModel extends CellModel {
	private Integer value;

	public IntCellModel(TableModel tableModel, RowModel rowModel, ColumnModel colModel) {
		super(tableModel, rowModel, colModel);
	}

	public IntCellModel(TableModel tableModel, Integer value) {
		super(tableModel, null, null);
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
