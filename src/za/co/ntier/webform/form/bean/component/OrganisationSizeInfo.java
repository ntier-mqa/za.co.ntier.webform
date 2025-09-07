package za.co.ntier.webform.form.bean.component;

import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class OrganisationSizeInfo implements ISaveForm {
	/**
	 * case no submitted WSP treat as false
	 */
	private boolean isSubmittedWSP;
	private Integer numOfEmployer;

	private String numOfEmployerTitle = "Number of Employees";

	private String submittedWSPTitle = "Has the organisation submitted the WSP/ATR In previous financial year?";

	public OrganisationSizeInfo() {
	};

	/**
	 * @return the numOfEmployer
	 */
	public Integer getNumOfEmployer() {
		if (numOfEmployer == null || numOfEmployer == 0)
			return null;
		return numOfEmployer;
	}

	/**
	 * @return the numOfEmployerTitle
	 */
	public String getNumOfEmployerTitle() {
		return numOfEmployerTitle;
	}

	public String getSubmittedWSPText() {
		return isSubmittedWSP ? "YES" : "NO";
	}

	/**
	 * @return the submittedWSPTitle
	 */
	public String getSubmittedWSPTitle() {
		return submittedWSPTitle;
	}

	/**
	 * @return the isSubmittedWSP
	 */
	public boolean isSubmittedWSP() {
		return isSubmittedWSP;
	}

	private X_ZZ_Application_Form applicationForm;
	
	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		if(getNumOfEmployer() == null) {
			applicationForm.set_ValueOfColumn(X_ZZ_Application_Form.COLUMNNAME_NumberEmployees, null);
		}else
			applicationForm.setNumberEmployees(getNumOfEmployer());
		applicationForm.setZZ_HasWSPSubmited(isSubmittedWSP());
		
	}

	public void initComponent(X_ZZ_Application_Form applicationForm) {
		this.applicationForm = applicationForm;
		if (applicationForm != null) {
			setNumOfEmployer(applicationForm.getNumberEmployees());
			setSubmittedWSP(applicationForm.isZZ_HasWSPSubmited());
		}else {
			setNumOfEmployer(null);
			setSubmittedWSP(false);			
		}
		
	}
	/**
	 * @param numOfEmployer the numOfEmployer to set
	 */
	public void setNumOfEmployer(Integer numOfEmployer) {
		if (numOfEmployer == null ||numOfEmployer == 0)
			this.numOfEmployer = null;
		else
			this.numOfEmployer = numOfEmployer;
	}

	/**
	 * @param numOfEmployerTitle the numOfEmployerTitle to set
	 */
	public void setNumOfEmployerTitle(String numOfEmployerTitle) {
		this.numOfEmployerTitle = numOfEmployerTitle;
	}

	/**
	 * @param isSubmittedWSP the isSubmittedWSP to set
	 */
	public void setSubmittedWSP(boolean isSubmittedWSP) {
		this.isSubmittedWSP = isSubmittedWSP;
	}

	/**
	 * @param submittedWSPTitle the submittedWSPTitle to set
	 */
	public void setSubmittedWSPTitle(String submittedWSPTitle) {
		this.submittedWSPTitle = submittedWSPTitle;
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
