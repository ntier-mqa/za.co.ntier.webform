package za.co.ntier.webform.form.bean.component;

import java.sql.Timestamp;
import java.time.LocalDate;

import org.compiere.model.MUser;
import org.compiere.util.Env;

import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class EmployerDeclarationInfo implements ISaveForm {
	private Boolean acknowledged;  // use Boolean, not boolean, so null is handled
	private LocalDate localDate;
	
	private String userName;

    
	public EmployerDeclarationInfo(X_ZZ_Application_Form appForm) {
		initForm(appForm);
	}

	public void initForm(X_ZZ_Application_Form appForm) {
		if (appForm != null) {
			acknowledged = true;
			if (appForm.getDateDoc() != null) {
				localDate = appForm.getDateDoc().toLocalDateTime().toLocalDate();
			}
			
			userName = appForm.getUserName();
		}
		
		if(localDate == null) {
			localDate = LocalDate.now();
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
		
		return Timestamp.valueOf(localDate.atStartOfDay());
	}

	public LocalDate getLocalDate() {
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

	public void setLocalDate(LocalDate localDate) {
		this.localDate = localDate;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
