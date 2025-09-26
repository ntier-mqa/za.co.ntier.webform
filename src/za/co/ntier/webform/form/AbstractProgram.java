package za.co.ntier.webform.form;

import za.co.ntier.api.model.X_ZZ_Application_Form;

public abstract class AbstractProgram implements ISaveForm {
	private MenuContextInfo menuContextInfo;
	private X_ZZ_Application_Form applicationForm;

	public AbstractProgram(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm) {
		this.menuContextInfo = menuContextInfo;
		this.applicationForm = applicationForm;
	}

	public void saveForm(X_ZZ_Application_Form applicationForm)  {
		this.applicationForm = applicationForm;
	}

	public MenuContextInfo getMenuContextInfo() {
		return menuContextInfo;
	}

	public void setMenuContextInfo(MenuContextInfo menuContextInfo) {
		this.menuContextInfo = menuContextInfo;
	}

	public X_ZZ_Application_Form getApplicationForm() {
		return applicationForm;
	}

	public void setApplicationForm(X_ZZ_Application_Form applicationForm) {
		this.applicationForm = applicationForm;
	}

	public boolean isProgramValid() {
		return true;
	}




}
