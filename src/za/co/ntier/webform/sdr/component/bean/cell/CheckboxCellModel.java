package za.co.ntier.webform.sdr.component.bean.cell;

import za.co.ntier.webform.sdr.component.bean.BaseCellModel;
import za.co.ntier.webform.sdr.component.bean.BaseColumnModel;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;

public class CheckboxCellModel extends BaseCellModel {
	private String title;

	public CheckboxCellModel(TableModel annexure, RowModel row, BaseColumnModel colModel) {
		super(annexure, row, colModel, CHECK_CELL);
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public static  BaseColumnModel getCheckboxColModel(String title, String daoPropertyName) {
		return BaseCellModel.getColModel(CheckboxCellModel.class, title, daoPropertyName);
	}

}
