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

	
	// OrganisationSizeVMWrapper.java
	@Command
	public void numEmployerChanging(@BindingParam("val") String val) {
		
		Integer v = (val == null || val.trim().isEmpty()) ? null : Integer.valueOf(val.trim());
	   
	    component.setNumOfEmployer(v);  // v can be 0


	    // 1) Re-evaluate the Organisation tab completeness (Next button state)
	    BindUtils.postNotifyChange(null, null, getApplicationProgramVM(), "organisationComplete");

	    // 2) Also broadcast so any toolbar / main buttons re-check immediately
	    BindUtils.postGlobalCommand(null, null, "tabSelectionChanged", null);
	}
	
	

	
	
}
