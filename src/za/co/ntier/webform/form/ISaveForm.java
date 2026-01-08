package za.co.ntier.webform.form;

import za.co.ntier.api.model.X_ZZ_Application_Form;

public interface ISaveForm {
	default public void beforeSaveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		
	}
	
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) ;
	
	default void afterSaveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		
	}
	
	default public void doSaveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		beforeSaveForm(trxName, applicationForm);
		saveForm(trxName, applicationForm);
		afterSaveForm(trxName, applicationForm);
	}
	
	/**
	 * return true when form is validate
	 * @return
	 */
	default public boolean validateForm() {
		return true;
	}
}
