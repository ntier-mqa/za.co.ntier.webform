package za.co.ntier.webform.sdr.component.bean.cell;

import org.zkoss.zk.ui.event.CheckEvent;

import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.ICheckbox;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;

public class CheckboxCellModel extends CellModel implements ICheckbox {
	private String title;

	public CheckboxCellModel(TableModel annexure, RowModel row, ColumnModel colModel) {
		super(annexure, row, colModel);
		setCellType(CHECK_CELL);
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

	public static  ColumnModel getCheckboxColModel(String title, String daoPropertyName) {
		return CellModel.getColModelForCell(
					CellModelInfo.of(ColumnModel.class, CheckboxCellModel.class
							, (colModel, celModel) -> {
								celModel.setTitle(title);
							}), 
				CellModelParams.of(title, daoPropertyName, null)
				);
	}

	@Override
	public void cmdChecked(CheckEvent checkEvent) {
		getColModel().cellValueChange(checkEvent, this);
		
	}

}
