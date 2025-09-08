package za.co.ntier.webform.form.bean.component;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.compiere.model.MUser;
import org.compiere.util.Env;

import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class EmployerDeclarationInfo implements ISaveForm {
	private Boolean acknowledged;  // use Boolean, not boolean, so null is handled
	private LocalDateTime localDate;
	
	private String userName;
	private X_ZZ_Application_Form applicationForm;
    
	public EmployerDeclarationInfo() {
		
	}

	public void initComponent(X_ZZ_Application_Form applicationForm) {
		this.setApplicationForm(applicationForm);
		if (applicationForm != null) {
			acknowledged = true;
			if (applicationForm.getDateDoc() != null) {
				localDate = applicationForm.getDateDoc().toLocalDateTime();
			}
			
			userName = applicationForm.getUserName();
		}
		
		if(localDate == null) {
			localDate = LocalDateTime.now();
		}
		
		if (userName == null) {
			userName = new MUser(Env.getCtx(), Env.getAD_User_ID(Env.getCtx()), null).getName(); 
		}	
	}
	
	/**
	 * @return the acknowledged
	 */
	public Boolean getAcknowledged() {
		return acknowledged;
	}

	public Timestamp getDate() {
		if (localDate == null)
			return null;
		
		return Timestamp.valueOf(localDate);
	}

	public LocalDateTime getLocalDate() {
		return localDate;
	}

	public String getUserName() {
		return userName;
	}
	
	@Override
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

	public void setLocalDate(LocalDateTime localDate) {
		this.localDate = localDate;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the applicationForm
	 */
	public X_ZZ_Application_Form getApplicationForm() {
		return applicationForm;
	}

	/**
	 * @param applicationForm the applicationForm to set
	 */
	public void setApplicationForm(X_ZZ_Application_Form applicationForm) {
		this.applicationForm = applicationForm;
	}
}
