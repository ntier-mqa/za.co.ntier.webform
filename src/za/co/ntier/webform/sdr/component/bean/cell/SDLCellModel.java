package za.co.ntier.webform.sdr.component.bean.cell;

import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.event.InputEvent;

import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;

public class SDLCellModel extends CellModel {
	public SDLCellModel(TableModel tableModel, RowModel rowModel, ColumnModel colModel) {
		super(tableModel, rowModel, colModel);
		setCellType(TEXT_CELL);
	}

	public static ColumnModel getSDLColumnModel(String title, String daoPropertyName) {
		if (title == null)
			title = "Skills Development Levy (SDL) Number (Paying or Exempted)";
		return CellModel.getColModelForCell(CellModelInfo.of(ColumnModel.class, SDLCellModel.class, null), CellModelParams.of(title, daoPropertyName, null));
	}
	
	@Override
	public void cmdValueChange(String value) {
		// TODO Auto-generated method stub
		super.cmdValueChange(value);
	}
	
	@Override
	protected List<String> doValidate(Object inputValue) {
		
		return super.doValidate(inputValue);
	}
}
