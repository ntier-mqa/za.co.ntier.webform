package za.co.ntier.webform.sdr.component.bean.cell;

import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.CellModel.CellModelInfo;
import za.co.ntier.webform.sdr.component.bean.CellModel.CellModelParams;
import za.co.ntier.webform.sdr.component.bean.column.UploadColumnModel;

public class UploadCellModel extends CellModel {
	public UploadCellModel(TableModel annexure, RowModel row, ColumnModel colModel) {
		super(annexure, row, colModel);
		setCellType(BTUPLOAD_CELL);
	}

	private String btText;
	public String getBtText() {
		return btText;
	}
	public void setBtText(String btText) {
		this.btText = btText;
	}

	private String fileName;
	private byte[] bytes;     // <- in-memory payload (no disk)

	public String getFileName() { return fileName; }
	public void setFileName(String fileName) { this.fileName = fileName; }

	public byte[] getBytes() { return bytes; }
	public void setBytes(byte[] bytes) { this.bytes = bytes; }

	// (optional) convenience
	public boolean hasBytes() { return bytes != null && bytes.length > 0; }


	public static UploadColumnModel getUploadColumnModel(String title, String daoPropertyName, String daoPropertyFileName, String btText) {
		
		UploadColumnModel uploadColumnModel = CellModel.getColModelForCell(
				CellModelInfo.of(UploadColumnModel.class, UploadCellModel.class, null), CellModelParams.of(title, daoPropertyName, null));
		
		uploadColumnModel.setDaoPropertyFileName(daoPropertyFileName);
		uploadColumnModel.setBtText(btText);
		return uploadColumnModel;
	}
}
