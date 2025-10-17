package za.co.ntier.webform.sdr.component.bean.cell;

import org.zkoss.zul.ListModelList;

import za.co.ntier.webform.sdr.component.bean.TableModel;

public class StringListCellModel extends AbstractListCellModel<String> {

	public StringListCellModel(TableModel annexure, RowModel row) {
		super(annexure, row);
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
}
