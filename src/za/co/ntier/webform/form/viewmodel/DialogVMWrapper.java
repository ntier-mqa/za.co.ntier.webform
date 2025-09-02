package za.co.ntier.webform.form.viewmodel;

import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.exception.ApplicationException;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import za.co.ntier.webform.form.bean.Dialog;

/**
 * If super has an init method but its ChildViewModel doesn't, you can
 * add @Init(superclass=true) on the ChildViewModel to use super's init.
 */
@Init(superclass = true)
public class DialogVMWrapper extends ComponentVMWrapper<Dialog> {
	private String moreInfo;
	@Command
	public void closeDialog() {
		getComponent().setVisible(false);
		BindUtils.postNotifyChange(this.getComponent(), "visible");
	}
	
	@Command
	@NotifyChange("visible")
	public void openAppForm() {
		try {
			AEnv.zoom(getComponent().getTableId(), getComponent().getRecordId());
			closeDialog();
		}catch (ApplicationException e) {
			moreInfo = e.getMessage();
			BindUtils.postNotifyChange(this, "moreInfo");
		}	
	}

	/**
	 * @return the moreInfo
	 */
	public String getMoreInfo() {
		return moreInfo;
	}

	/**
	 * @param moreInfo the moreInfo to set
	 */
	public void setMoreInfo(String moreInfo) {
		this.moreInfo = moreInfo;
	}
}
