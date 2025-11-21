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

	@Override
	public Object getValue() {
		if (super.getValue() == null) {
			return false;// TODO: DAO for checkbox don't accept null because setter use boolean. neet to overider setDao for treatment null value
			
		}
			
		return super.getValue();
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
