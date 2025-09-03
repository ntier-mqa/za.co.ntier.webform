package za.co.ntier.webform.form;

import java.io.IOException;

import za.co.ntier.webform.model.X_ZZ_Application_Form;

public interface ISaveForm {
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) throws IOException;
}
