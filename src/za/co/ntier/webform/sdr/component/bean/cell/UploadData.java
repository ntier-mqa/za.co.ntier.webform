package za.co.ntier.webform.sdr.component.bean.cell;

import za.co.ntier.webform.sdr.component.bean.TableModel;

public class UploadData extends AbstractCellModel {
    public UploadData(TableModel annexure, RowModel row) {
		super(annexure, row);
	}

	private String fileName;
    private byte[] bytes;     // <- in-memory payload (no disk)

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public byte[] getBytes() { return bytes; }
    public void setBytes(byte[] bytes) { this.bytes = bytes; }

    // (optional) convenience
    public boolean hasBytes() { return bytes != null && bytes.length > 0; }
}
