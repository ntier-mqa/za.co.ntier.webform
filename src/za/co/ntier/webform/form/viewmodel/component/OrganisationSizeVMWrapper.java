package za.co.ntier.webform.form.viewmodel.component;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.component.OrganisationSizeInfo;

@Init(superclass = true)
public class OrganisationSizeVMWrapper extends ComponentVMWrapper<OrganisationSizeInfo> {

	@Command
	public void notifyParentCompleteness() {
		if (getApplicationProgramVM() != null && getNotifyTarget() != null) {
			BindUtils.postNotifyChange(null, null, getApplicationProgramVM(), getNotifyTarget());
		}
	}
	
	@Command
	public void numEmployerChanging(@BindingParam("val") String val) {
	    Integer n = null;
	    if (val != null) {
	        String s = val.trim();
	        if (!s.isEmpty()) {
	            try { n = Integer.valueOf(s); } catch (NumberFormatException ignore) {}
	        }
	    }
	    
	    getComponent().setNumOfEmployer(n);  // <-- write latest (including null)
	    BindUtils.postNotifyChange(null, null, getApplicationProgramVM(), "organisationComplete");
	}
}
