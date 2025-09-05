package za.co.ntier.webform.form.bean.component;

public class Dialog {
	private String documentNo;
	private int recordId;
	private int tableId;
	private String title;
	private boolean visible = false;
	public Dialog(String title, int tableId, int recordId, String documentNo, boolean visible) {
		this.tableId = tableId;
		this.recordId = recordId;
		this.visible = visible;
		this.title = title;
		this.setDocumentNo(documentNo);
	}
	/**
	 * @return the documentNo
	 */
	public String getDocumentNo() {
		return documentNo;
	}
	/**
	 * @return the recordId
	 */
	public int getRecordId() {
		return recordId;
	}
	/**
	 * @return the tableId
	 */
	public int getTableId() {
		return tableId;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @return the visible
	 */
	public boolean isVisible() {
		return visible;
	}
	/**
	 * @param documentNo the documentNo to set
	 */
	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
	}
	/**
	 * @param recordId the recordId to set
	 */
	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}
	/**
	 * @param tableId the tableId to set
	 */
	public void setTableId(int tableId) {
		this.tableId = tableId;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @param visible the visible to set
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
