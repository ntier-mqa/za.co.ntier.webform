package za.co.ntier.webform.form.bean.component;



public class UploadData implements ICellData{
    private String fileName;
    private byte[] bytes;     // <- in-memory payload (no disk)

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public byte[] getBytes() { return bytes; }
    public void setBytes(byte[] bytes) { this.bytes = bytes; }

    // (optional) convenience
    public boolean hasBytes() { return bytes != null && bytes.length > 0; }
	@Override
	public void clearData() {
		bytes = null;
		fileName = null;
		
	}
}
