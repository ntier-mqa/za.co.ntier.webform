package za.co.ntier.webform.form.bean.component;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class Dialog {
	private String documentNo;
	private List<String> msgs;
	private String title;
	private boolean visible = false;
	private Consumer<Object> onCloseDialog;
	public Dialog(String title, String msg) {
		this(title, msg, true);
	}
	
	private Dialog(String title, String msg, boolean visible) {
		this.visible = visible;
		this.title = title;
		if (msg != null) {
			msgs = List.of(msg.split("\n"));
		}
		this.setDocumentNo(documentNo);
	}
	/**
	 * @return the documentNo
	 */
	public String getDocumentNo() {
		return documentNo;
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
	 * @return the msgs
	 */
	public List<String> getMsgs() {
		return msgs;
	}

	/**
	 * @param msgs the msgs to set
	 */
	public void setMsgs(List<String> msgs) {
		this.msgs = msgs;
	}

	/**
	 * @return the onCloseDialog
	 */
	public Consumer<Object> getOnCloseDialog() {
		return onCloseDialog;
	}

	/**
	 * @param onCloseDialog the onCloseDialog to set
	 */
	public void setOnCloseDialog(Consumer<Object> onCloseDialog) {
		this.onCloseDialog = onCloseDialog;
	}
}
