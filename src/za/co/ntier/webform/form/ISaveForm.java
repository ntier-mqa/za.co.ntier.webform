package za.co.ntier.webform.form;

import za.co.ntier.webform.model.X_ZZ_Application_Form;

public interface ISaveForm {
	default public void beforeSaveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		applicationForm.setZZTotalNumberApplied(0);
	}
	
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) ;
	
	default void afterSaveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		applicationForm.saveEx(trxName);
	}
	
	default public void doSaveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		beforeSaveForm(trxName, applicationForm);
		saveForm(trxName, applicationForm);
		afterSaveForm(trxName, applicationForm);
	}
}
