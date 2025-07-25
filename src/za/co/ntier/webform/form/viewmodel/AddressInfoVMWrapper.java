package za.co.ntier.webform.form.viewmodel;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.AddressInfoBase;

public class AddressInfoVMWrapper {
	private AddressInfoBase addressInfo;

	/**
	 * @return the addressInfo
	 */
	public AddressInfoBase getAddressInfo() {
		return addressInfo;
	}

	@Init
	public void init(@ExecutionArgParam("addressInfo") AddressInfoBase addressInfo) {
		this.addressInfo = addressInfo;
	}

	/**
	 * @param addressInfo the addressInfo to set
	 */
	public void setAddressInfo(AddressInfoBase addressInfo) {
		this.addressInfo = addressInfo;
	}
}
