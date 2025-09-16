package za.co.ntier.webform.form.viewmodel.component;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.component.OrganisationInfo;

@Init(superclass = true)
public class OrganisationInfoVMWrapper extends ComponentVMWrapper<OrganisationInfo>{
	@Command
	public void sdlNumberChange() {
		getComponent().sdlNumberChange();
	}

}
