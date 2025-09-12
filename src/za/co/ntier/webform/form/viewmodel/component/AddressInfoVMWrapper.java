package za.co.ntier.webform.form.viewmodel.component;

import org.compiere.model.MCity;
import org.compiere.model.MRegion;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
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
	
	// set text fields while typing ("" -> null)
    @Command
    public void fieldChanging(@BindingParam("field") String field,
                              @BindingParam("val") String val) {
        String v = (val == null || val.trim().isEmpty()) ? null : val.trim();
        switch (field) {
            case "siteName":                   addressInfo.setSiteName(v); break;
            case "addressLine":                addressInfo.setAddressLine(v); break;
            case "postalCode":                 addressInfo.setPostalCode(v); break;
            case "nameSiteRepresentative":     addressInfo.setNameSiteRepresentative(v); break;
            case "representativeDesignation":  addressInfo.setRepresentativeDesignation(v); break;
            case "mobileNumber":               addressInfo.setMobileNumber(v); break;
            case "landlineNumber":             addressInfo.setLandlineNumber(v); break;
            case "email":                      addressInfo.setEmail(v); break;
        }
        BindUtils.postNotifyChange(null, null, applicationProgramVM, notifyTarget);
    }
    
    @Command
    public void areaSelected(@BindingParam("item") MCity item) {
        addressInfo.setAreaSelected(item); // matches setter signature
        BindUtils.postNotifyChange(null, null, applicationProgramVM, notifyTarget);
    }

    @Command
    public void provinceSelected(@BindingParam("item") MRegion item) {
        addressInfo.setProvinceSelected(item); // matches setter signature
        BindUtils.postNotifyChange(null, null, applicationProgramVM, notifyTarget);
    }
	@Command
    public void notifyParentCompleteness() {
        if (applicationProgramVM != null && notifyTarget != null) {
            org.zkoss.bind.BindUtils.postNotifyChange(null, null, applicationProgramVM, notifyTarget);
        }
    }
	/**
	 * @param addressInfo the addressInfo to set
	 */
	public void setAddressInfo(AddressInfo addressInfo) {
		this.addressInfo = addressInfo;
	}
}
