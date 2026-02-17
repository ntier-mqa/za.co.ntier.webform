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
	
	@Command("cmdCancel")
	public void cmdCancel() {
		closeDialog();
        
        if (getComponent().getCancelHandle() != null) {
        	getComponent().getCancelHandle().accept(null);
        }
	}
	
	private void closeDialog() {
        getComponent().setVisible(false);
        BindUtils.postNotifyChange(null, null, getComponent(), "visible");
	}
	
	@Command("cmdOk")
    public void cmdOk() {
        // Close the modal via the bound component's 'visible' property
		closeDialog();
        
        if (getComponent().getOkHandle() != null) {
        	getComponent().getOkHandle().accept(null);
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
