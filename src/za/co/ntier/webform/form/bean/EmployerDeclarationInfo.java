package za.co.ntier.webform.form.bean;

import java.sql.Timestamp;
import java.time.LocalDate;

import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class EmployerDeclarationInfo implements ISaveForm {
	private String userName;
	private LocalDate localDate;

	public Timestamp getDate() {
		if (localDate == null)
			return null;
		
		return Timestamp.valueOf(localDate.atStartOfDay());
	}

	public LocalDate getLocalDate() {
		return localDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setLocalDate(LocalDate localDate) {
		this.localDate = localDate;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public void saveForm(X_ZZ_Application_Form applicationForm) {
		applicationForm.setUserName(getUserName());
		applicationForm.setDateDoc(getDate());
	}
}
