package za.co.ntier.webform.sdr.component.bean.cell;

import java.util.List;
import java.util.function.Function;

import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.column.ListColumnModel;

public class RadioCellModel<T> extends ListCellModel<T>{

	public RadioCellModel(TableModel tableModel, RowModel rowModel, ListColumnModel<T> colModel) {
		super(tableModel, rowModel, colModel);

	}
	
	public void setSelectedItem(T selectedItem) {
		setValue(selectedItem);
	}
	
	public static <L> ListColumnModel<L> getRadioColumnModel(
			String title, String daoPropertyName, List<L> dataProvider, 
			Function<L, String> displayConvert, 
			Function<L, Object> valueConvert) {
		
		@SuppressWarnings("unchecked")
		ListColumnModel<L> listColumnModel = getListColumnModel(ListColumnModel.class, RadioCellModel.class, 
				title, daoPropertyName, dataProvider, 
				displayConvert, valueConvert, CellModel.RADIO_CELL);
		
		return listColumnModel;
	}

}
