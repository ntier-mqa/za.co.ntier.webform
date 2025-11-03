package za.co.ntier.webform.sdr.component.viewmodel;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.sdr.component.bean.Dialog;

/**
 * If super has an init method but its ChildViewModel doesn't, you can
 * add @Init(superclass=true) on the ChildViewModel to use super's init.
 */
@Init(superclass = true)
public class DialogVMWrapper extends BaseComponentVM<Dialog> {
	

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
        //DiscretionaryGrantsApplicationProgramVM.showAppList();
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
