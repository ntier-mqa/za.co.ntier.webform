package za.co.ntier.webform.sdr.component.bean;

import java.util.Collection;
import java.util.HashMap;

import org.compiere.model.PO;

public class RowModel extends HashMap<ColumnModel, CellModel>{

	private TableModel annexure;
	public RowModel(TableModel annexure) {
		this.setAnnexure(annexure);
	}
	private static final long serialVersionUID = 3444756682531476154L;
	public static final int INPUT_STATE_EMPTY = 0;
	public static final int INPUT_STATE_FULL = 1;
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

	public int inputState() {
		// TODO Auto-generated method stub
		return INPUT_STATE_EMPTY;
	}

	public void saveUploadFiles(PO po, String trxName) {
		// TODO Auto-generated method stub

	}

	public boolean isMatchingRow(Collection<ColumnModel> keyColumns, PO dao) {
		// TODO Auto-generated method stub
		return false;
	}

	public void fillRowDataFromDao(PO dao) {
		// TODO Auto-generated method stub

	}
}
