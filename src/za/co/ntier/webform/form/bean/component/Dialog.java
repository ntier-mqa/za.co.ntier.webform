package za.co.ntier.webform.form.bean.component;

public class Dialog {
	private String documentNo;
	private String msg;
	private int recordId;
	private int tableId;
	private String title;
	private boolean visible = false;
	public Dialog(String title, String msg, int tableId, int recordId, String documentNo, boolean visible) {
		this.tableId = tableId;
		this.recordId = recordId;
		this.visible = visible;
		this.title = title;
		this.setMsg(msg);
		this.setDocumentNo(documentNo);
	}
	/**
	 * @return the documentNo
	 */
	public String getDocumentNo() {
		return documentNo;
	}
	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
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
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
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
