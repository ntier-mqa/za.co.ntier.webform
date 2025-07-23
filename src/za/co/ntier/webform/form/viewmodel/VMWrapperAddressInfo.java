package za.co.ntier.webform.form.viewmodel;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

public class VMWrapperAddressInfo {
	private AddressInfoBase addressInfo;
	
	@Init
    public void init(@ExecutionArgParam("addressInfo") AddressInfoBase addressInfo) {
		this.addressInfo = addressInfo;
    }

	/**
	 * @return the addressInfo
	 */
	public AddressInfoBase getAddressInfo() {
		return addressInfo;
	}

	/**
	 * @param addressInfo the addressInfo to set
	 */
	public void setAddressInfo(AddressInfoBase addressInfo) {
		this.addressInfo = addressInfo;
	}
}
