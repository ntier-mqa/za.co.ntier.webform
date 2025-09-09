package za.co.ntier.webform.form.viewmodel.component;

import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.exception.ApplicationException;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import za.co.ntier.webform.form.bean.component.Dialog;
import za.co.ntier.webform.form.viewmodel.DiscretionaryGrantsApplicationProgramVM;

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

	@Command("closeDialogAndOpenList")
    public void closeDialogAndOpenList() {
        // Close the modal via the bound component's 'visible' property
        if (getComponent() != null) {
            getComponent().setVisible(false);
            BindUtils.postNotifyChange(null, null, getComponent(), "visible");
        }
        DiscretionaryGrantsApplicationProgramVM.showAppList();
    }


	/**
	 * @return the moreInfo
	 */
	public String getMoreInfo() {
		return moreInfo;
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
	 * @param moreInfo the moreInfo to set
	 */
	public void setMoreInfo(String moreInfo) {
		this.moreInfo = moreInfo;
	}

}
