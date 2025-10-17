package za.co.ntier.webform.sdr.component.bean.cell;

import java.util.HashMap;

import org.compiere.model.PO;

import za.co.ntier.webform.sdr.component.bean.TableModel;

public class RowModel extends HashMap<ColumnModel, Object>{
	
	private TableModel annexure;
	public RowModel(TableModel annexure) {
		this.setAnnexure(annexure);
	}
	private static final long serialVersionUID = 3444756682531476154L;
	private PO data;

	/**
	 * @return the data
	 */
	public PO getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(PO data) {
		this.data = data;
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
}
