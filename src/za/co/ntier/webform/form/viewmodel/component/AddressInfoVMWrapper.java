package za.co.ntier.webform.form.viewmodel.component;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.component.AddressInfo;
import za.co.ntier.webform.form.viewmodel.DiscretionaryGrantsApplicationProgramVM;

public class AddressInfoVMWrapper {
	private AddressInfo addressInfo;
	private DiscretionaryGrantsApplicationProgramVM applicationProgramVM;
    private String notifyTarget;

	/**
	 * @return the addressInfo
	 */
	public AddressInfo getAddressInfo() {
		return addressInfo;
	}

	@Init
	public void init(@ExecutionArgParam("addressInfo") AddressInfo addressInfo, // you already have this
	        @ExecutionArgParam("applicationProgramVM") DiscretionaryGrantsApplicationProgramVM appVM,
	        @ExecutionArgParam("notifyTarget") String notifyTarget) {
		this.applicationProgramVM = appVM;
	    this.notifyTarget = notifyTarget;
		this.addressInfo = addressInfo;
	}

	/**
	 * @param addressInfo the addressInfo to set
	 */
	public void setAddressInfo(AddressInfo addressInfo) {
		this.addressInfo = addressInfo;
	}
	@Command
    public void notifyParentCompleteness() {
        if (applicationProgramVM != null && notifyTarget != null) {
            org.zkoss.bind.BindUtils.postNotifyChange(null, null, applicationProgramVM, notifyTarget);
        }
    }
}
