package za.co.ntier.webform.sdr.component.bean.cell;

import org.zkoss.zk.ui.event.CheckEvent;

import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.ICheckbox;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;

public class CheckboxCellModel extends CellModel implements ICheckbox {
	private String title;

	public CheckboxCellModel(TableModel tableModel, RowModel rowModel, ColumnModel colModel) {
		super(tableModel, rowModel, colModel);
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
		ColumnModel checkboxColModel = CellModel.getColModelForCell(
					CellModelInfo.of(ColumnModel.class, CheckboxCellModel.class
							, (colModel, celModel) -> {
								celModel.setTitle(title);
							}), 
				CellModelParams.of(title, daoPropertyName, null)
				);
		
		return checkboxColModel;
	}

	@Override
	public void cmdChecked(CheckEvent checkEvent) {
		this.setValue(checkEvent.isChecked());
		getColModel().cellValueChange(checkEvent, this);
	}

}
