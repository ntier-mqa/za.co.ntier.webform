package za.co.ntier.webform.form.viewmodel.component;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.component.AddressInfo;

public class AddressInfoVMWrapper {
	private AddressInfo addressInfo;

	/**
	 * @return the addressInfo
	 */
	public AddressInfo getAddressInfo() {
		return addressInfo;
	}

	@Init
	public void init(@ExecutionArgParam("addressInfo") AddressInfo addressInfo) {
		this.addressInfo = addressInfo;
	}

	/**
	 * @param addressInfo the addressInfo to set
	 */
	public void setAddressInfo(AddressInfo addressInfo) {
		this.addressInfo = addressInfo;
	}
}
