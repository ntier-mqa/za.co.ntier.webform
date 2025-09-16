package za.co.ntier.webform.form.viewmodel.component;

import org.compiere.model.MCity;
import org.compiere.model.MRegion;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.component.AddressInfo;
import za.co.ntier.webform.form.viewmodel.DiscretionaryGrantsApplicationProgramVM;
@Init(superclass = true)
public class AddressInfoVMWrapper extends ComponentVMWrapper<AddressInfo>{
	private DiscretionaryGrantsApplicationProgramVM applicationProgramVM;
    private String notifyTarget;
	
	// set text fields while typing ("" -> null)
    @Command
    public void fieldChanging(@BindingParam("field") String field,
                              @BindingParam("val") String val) {
        String v = (val == null || val.trim().isEmpty()) ? null : val.trim();
        switch (field) {
            case "siteName":                   component.setSiteName(v); break;
            case "addressLine":                component.setAddressLine(v); break;
            case "postalCode":                 component.setPostalCode(v); break;
            case "nameSiteRepresentative":     component.setNameSiteRepresentative(v); break;
            case "representativeDesignation":  component.setRepresentativeDesignation(v); break;
            case "mobileNumber":               component.setMobileNumber(v); break;
            case "landlineNumber":             component.setLandlineNumber(v); break;
            case "email":                      component.setEmail(v); break;
        }
        BindUtils.postNotifyChange(null, null, applicationProgramVM, notifyTarget);
    }
    
    @Command
    public void areaSelected(@BindingParam("item") MCity item) {
    	component.setAreaSelected(item); // matches setter signature
        BindUtils.postNotifyChange(null, null, applicationProgramVM, notifyTarget);
    }

    @Command
    public void provinceSelected(@BindingParam("item") MRegion item) {
    	component.setProvinceSelected(item); // matches setter signature
        BindUtils.postNotifyChange(null, null, applicationProgramVM, notifyTarget);
    }
	@Command
    public void notifyParentCompleteness() {
        if (applicationProgramVM != null && notifyTarget != null) {
            org.zkoss.bind.BindUtils.postNotifyChange(null, null, applicationProgramVM, notifyTarget);
        }
    }
	
}
