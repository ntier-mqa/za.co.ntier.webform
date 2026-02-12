package za.co.ntier.webform.sdr.component.bean.cell;

import java.util.List;
import java.util.function.Function;

import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.CellModel.CellModelInfo;
import za.co.ntier.webform.sdr.component.bean.CellModel.CellModelParams;
import za.co.ntier.webform.sdr.component.bean.column.ListColumnModel;
import za.co.ntier.webform.sdr.component.bean.column.PresetTitleColumnModel;

public class PresetTitleCellModel<T> extends CellModel{

	public PresetTitleCellModel(TableModel tableModel, RowModel rowModel, PresetTitleColumnModel<T> colModel) {
		super(tableModel, rowModel, colModel);
		this.setCellType(CellModel.PRESET_TITLE_CELL);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public PresetTitleColumnModel<T> getColModel() {
		return (PresetTitleColumnModel<T>)super.getColModel();
	}
	
	public String getTitleValue() {
		@SuppressWarnings("unchecked")
		T tValue = (T)getValue();
		return getColModel().getDisplayConvert().apply(tValue);
	}
	
	public static <L> PresetTitleColumnModel<L> getPresetTitleColumnModel(String title, Function<L, String> displayConvert) {
		
		@SuppressWarnings("unchecked")
		PresetTitleColumnModel<L> columnModel = (PresetTitleColumnModel<L>)CellModel.getColModelForCell(CellModelInfo.of(PresetTitleColumnModel.class, PresetTitleCellModel.class, null),
				CellModelParams.of(title, null, null));
		columnModel.setDisplayConvert(displayConvert);
		return columnModel;
	}
	
}
