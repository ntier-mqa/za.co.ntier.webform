package za.co.ntier.webform.sdr.component.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Dialog {
	private List<String> msgs;
	private String title;
	private boolean visible = false;
	private Consumer<Object> onCloseDialog;
	private String sclass = "sdrDialog";
	
	public Dialog(String title, List<String> msgs) {
		this.title = title;
		this.setMsgs(msgs);
	}
	
	public Dialog(String title, String msg) {
		this(title, msg, true);
	}

	private Dialog(String title, String msg, boolean visible) {
		this(title, msg == null? new ArrayList<String>():List.of(msg.split("\\n")));
		this.visible = visible;
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
	 * @return the sclass
	 */
	public String getSclass() {
		return sclass;
	}

	/**
	 * @param sclass the sclass to set
	 */
	public void setSclass(String sclass) {
		this.sclass = "sdrDialog";
		if (sclass != null) {
			this.sclass = this.sclass + " " + sclass;
		}
	}

	public Consumer<Object> getOnCloseDialog() {
		return onCloseDialog;
	}

	public void setOnCloseDialog(Consumer<Object> onCloseDialog) {
		this.onCloseDialog = onCloseDialog;
	}

}
