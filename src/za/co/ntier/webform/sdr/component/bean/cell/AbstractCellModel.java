package za.co.ntier.webform.sdr.component.bean.cell;

import za.co.ntier.webform.sdr.component.bean.TableModel;

public abstract class AbstractCellModel {
	private TableModel annexure;
	private RowModel row;
	public AbstractCellModel(TableModel annexure, RowModel row){
		this.setAnnexure(annexure);
		this.setRow(row);
	}
	/**
	 * @return the annexure
	 */
	public TableModel getAnnexure() {
		return annexure;
	}
	/**
	 * @param annexure the annexure to set
	 */
	public void setAnnexure(TableModel annexure) {
		this.annexure = annexure;
	}
	/**
	 * @return the row
	 */
	public RowModel getRow() {
		return row;
	}
	/**
	 * @param row the row to set
	 */
	public void setRow(RowModel row) {
		this.row = row;
	}
}
