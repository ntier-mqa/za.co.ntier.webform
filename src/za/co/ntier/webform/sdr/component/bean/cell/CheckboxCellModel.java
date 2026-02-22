package za.co.ntier.webform.sdr.component.bean.cell;

import java.util.List;

import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.zkoss.zk.ui.event.CheckEvent;

import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ICheckbox;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.column.CheckboxColumnModel;

public class CheckboxCellModel extends CellModel implements ICheckbox {
	private String title;

	public CheckboxCellModel(TableModel tableModel, RowModel rowModel, CheckboxColumnModel colModel) {
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

	public static CheckboxColumnModel getCheckboxColModel(String title, String daoPropertyName) {
		CheckboxColumnModel checkboxColModel = CellModel.getColModelForCell(
					CellModelInfo.of(CheckboxColumnModel.class, CheckboxCellModel.class
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
	
	@Override
	protected List<String> doValidate(Object value) {
		List<String> msgs = super.doValidate(value);
		CheckboxColumnModel colModel =(CheckboxColumnModel)getColModel();
		if (msgs.size() == 0 && colModel.isRequireChecked() && (value == null || !(boolean)value)) {
			msgs.add(Msg.getMsg(Env.getCtx(), "ZZValidateRequiredCheck"));
		}
		return msgs;
	}

	@Override
	public Object getDefaultValue() {
		if(super.getDefaultValue() == null)
			return false;
		else 
			return super.getDefaultValue();
	}
}
