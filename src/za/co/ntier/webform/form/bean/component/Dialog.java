package za.co.ntier.webform.form.bean.component;

public class Dialog {
	private String documentNo;
	private String msg;
	private Integer recordId;
	private Integer tableId;
	private String title;
	private boolean visible = false;
	public Dialog(String title, String msg) {
		this(title, msg, null, null, null, true);
	}
	
	public Dialog(String title, String msg, Integer tableId, Integer recordId, String documentNo) {
		this(title, msg, tableId, recordId, documentNo, true);
	}
	
	private Dialog(String title, String msg, Integer tableId, Integer recordId, String documentNo, boolean visible) {
		this.setTableId(tableId);
		this.setRecordId(recordId);
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
	/**
	 * @return the recordId
	 */
	public Integer getRecordId() {
		return recordId;
	}
	/**
	 * @param recordId the recordId to set
	 */
	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}
	/**
	 * @return the tableId
	 */
	public Integer getTableId() {
		return tableId;
	}
	/**
	 * @param tableId the tableId to set
	 */
	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}
}
