package za.co.ntier.webform.sdr.component.bean.cell;

import za.co.ntier.webform.sdr.component.bean.BaseCellModel;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;

public class IntCellModel extends BaseCellModel {
	private Integer value;

	public IntCellModel(TableModel annexure, RowModel row) {
		super(annexure, row);
	}
	
	public IntCellModel(TableModel annexure, Integer value) {
		super(annexure, null);
		setValue(value);
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

	@Override
	public int getCellType() {
		return POSITIVE_NUM_CELL;
	}
}
