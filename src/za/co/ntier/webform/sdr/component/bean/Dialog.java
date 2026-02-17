package za.co.ntier.webform.sdr.component.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;

public class Dialog {
	private List<String> msgs;
	private String title;
	private boolean visible = false;
	private Consumer<Object> okHandle;
	private Consumer<Object> cancelHandle;
	private String sclass = "sdrDialog";
	private boolean isShowOk = false;
	private boolean isShowCancel = false;
	
	public Dialog(String title, List<String> msgs) {
		this.title = title;
		this.setMsgs(msgs);
		this.isShowOk = true;
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
		if (StringUtils.isBlank(title))
			return "Dialog";
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

	public Consumer<Object> getOkHandle() {
		return okHandle;
	}

	public void setOkHandle(Consumer<Object> okHandle) {
		this.okHandle = okHandle;
	}

	public Consumer<Object> getCancelHandle() {
		return cancelHandle;
	}

	public void setCancelHandle(Consumer<Object> cancelHandle) {
		this.cancelHandle = cancelHandle;
	}

	public boolean isShowOk() {
		return isShowOk;
	}

	public void setShowOk(boolean isShowOk) {
		this.isShowOk = isShowOk;
	}

	public boolean isShowCancel() {
		return isShowCancel;
	}

	public void setShowCancel(boolean isShowCancel) {
		this.isShowCancel = isShowCancel;
	}

}
