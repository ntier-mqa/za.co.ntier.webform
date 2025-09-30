package za.co.ntier.webform.form.bean.component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.compiere.model.MUser;
import org.compiere.util.Env;

import za.co.ntier.api.model.X_ZZ_Application_Form;
import za.co.ntier.webform.form.ISaveForm;

public class EmployerDeclarationInfo implements ISaveForm {
	private Boolean acknowledged;  // use Boolean, not boolean, so null is handled
	private X_ZZ_Application_Form applicationForm;
	
	private LocalDateTime localDate;
	private String userName;
    
	public EmployerDeclarationInfo() {
		
	}

	/**
	 * @return the acknowledged
	 */
	public Boolean getAcknowledged() {
		return acknowledged;
	}
	
	/**
	 * @return the applicationForm
	 */
	public X_ZZ_Application_Form getApplicationForm() {
		return applicationForm;
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
	
	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		applicationForm.setUserName(getUserName());
	}

	/**
	 * @param acknowledged the acknowledged to set
	 */
	public void setAcknowledged(Boolean acknowledged) {
		this.acknowledged = acknowledged;
	}

	/**
	 * @param applicationForm the applicationForm to set
	 */
	public void setApplicationForm(X_ZZ_Application_Form applicationForm) {
		this.applicationForm = applicationForm;
	}

	public void setLocalDate(LocalDateTime localDate) {
		this.localDate = localDate;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
