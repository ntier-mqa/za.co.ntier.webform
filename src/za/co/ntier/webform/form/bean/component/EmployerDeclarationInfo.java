package za.co.ntier.webform.form.bean.component;

import java.sql.Timestamp;
import java.time.LocalDate;

import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class EmployerDeclarationInfo implements ISaveForm {
	private Boolean acknowledged;  // use Boolean, not boolean, so null is handled
	private LocalDate localDate;
	
	private String userName;

    
    

	/**
	 * @return the acknowledged
	 */
	public Boolean getAcknowledged() {
		return acknowledged;
	}

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

	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		applicationForm.setUserName(getUserName());
		applicationForm.setDateDoc(getDate());
	}
	
	/**
	 * @param acknowledged the acknowledged to set
	 */
	public void setAcknowledged(Boolean acknowledged) {
		this.acknowledged = acknowledged;
	}

	public void setLocalDate(LocalDate localDate) {
		this.localDate = localDate;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
