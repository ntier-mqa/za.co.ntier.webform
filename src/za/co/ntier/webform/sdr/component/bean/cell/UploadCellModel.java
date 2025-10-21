package za.co.ntier.webform.sdr.component.bean.cell;

import za.co.ntier.webform.sdr.component.bean.BaseCellModel;
import za.co.ntier.webform.sdr.component.bean.BaseColumnModel;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.column.UploadColumnModel;

public class UploadCellModel extends BaseCellModel {
	public UploadCellModel(TableModel annexure, RowModel row, BaseColumnModel colModel) {
		super(annexure, row, colModel, BTUPLOAD_CELL);
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
		UploadColumnModel uploadColumnModel = BaseCellModel.getColModel(UploadColumnModel.class, UploadCellModel.class, title);
		uploadColumnModel.setDaoPropertyName(daoPropertyName);
		uploadColumnModel.setDaoPropertyFileName(daoPropertyFileName);
		uploadColumnModel.setBtText(btText);

		return uploadColumnModel;
	}
}
