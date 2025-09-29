package za.co.ntier.webform.form;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import za.co.ntier.api.model.X_ZZ_Application_Form;

public interface ISaveForm {
	default public void beforeSaveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		applicationForm.setZZTotalNumberApplied(0);
	}
	
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) ;
	
	default void afterSaveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		applicationForm.setDateDoc(Timestamp.valueOf(LocalDateTime.now()));
		applicationForm.saveEx(trxName);
	}
	
	default public void doSaveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		beforeSaveForm(trxName, applicationForm);
		saveForm(trxName, applicationForm);
		afterSaveForm(trxName, applicationForm);
	}
	
}
