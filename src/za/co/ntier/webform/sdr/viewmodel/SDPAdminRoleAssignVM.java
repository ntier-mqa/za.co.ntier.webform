package za.co.ntier.webform.sdr.viewmodel;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;

public class SDPAdminRoleAssignVM extends BaseAppVM{
	
	@Init(superclass = true)
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo){

		
		initForm();
	}

	private void initForm() {
		// TODO Auto-generated method stub
		
	}

}
