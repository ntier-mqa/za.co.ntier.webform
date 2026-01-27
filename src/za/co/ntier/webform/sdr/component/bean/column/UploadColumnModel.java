package za.co.ntier.webform.sdr.component.bean.column;

import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.cell.UploadCellModel;

public class UploadColumnModel extends ColumnModel {
	private String daoPropertyFileName;
	public String getDaoPropertyFileName() {
		return daoPropertyFileName;
	}

	public void setDaoPropertyFileName(String daoPropertyFileName) {
		this.daoPropertyFileName = daoPropertyFileName;
	}

	public String getBtText() {
		return btText;
	}

	public void setBtText(String btText) {
		this.btText = btText;
	}

	private String btText;

	public UploadColumnModel(String colTitle) {
		super(colTitle);
	}

	public UploadColumnModel(String colTitle, String daoProperty, String daoPropertyFileName, String btText) {
		super(colTitle, daoProperty);
		this.daoPropertyFileName = daoPropertyFileName;
		this.btText = btText;
	}

	@Override
	public CellModel initCellModel(TableModel tableModel, RowModel rowModel) {
		UploadCellModel cellModel = new UploadCellModel(tableModel, rowModel, this);
		cellModel.setBtText(btText);
		return cellModel;
	}


}
