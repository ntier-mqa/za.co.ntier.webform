package za.co.ntier.webform.sdr.component.bean.cell;

import java.util.List;

import org.zkoss.zul.ListModelList;

import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.column.ListColumnModel;

public class StringListCellModel extends AbstractListCellModel<String> {

	public StringListCellModel(TableModel tableModel, RowModel rowModel) {
		super(tableModel, rowModel);
		setModel(new ListModelList<String>());
	}

	@Override
	public void cmdSelectedHandle(String selected) {

	}

	@Override
	public int getSelectedID() {
		return 0;
	}

	@Override
	public void setSelectedItemById(Object cityId) {

	}

	@Override
	public String getDisplayText(String item) {
		return item;
	}


	public static ListColumnModel<String> getColumnModel(String title, List<String> provider) {
		ListColumnModel<String> colStringModel = new ListColumnModel<String>(title);
		colStringModel.setCellModelSupplier((tableModel, rowModel)->{
			return new StringListCellModel(tableModel, rowModel);
		});
		colStringModel.setDataProvider(provider);
		return colStringModel;
	}
}
