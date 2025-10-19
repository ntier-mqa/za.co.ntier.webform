package za.co.ntier.webform.sdr.component.bean.cell;

import za.co.ntier.webform.sdr.component.bean.TableModel;

public class CheckboxData extends AbstractCellModel {
	private String title;
	
	public CheckboxData(TableModel annexure, RowModel row) {
		super(annexure, row);
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

	
}
