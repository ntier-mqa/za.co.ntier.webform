package za.co.ntier.webform.form.bean.component;

import za.co.ntier.api.model.I_ZZ_Application_Form;
import za.co.ntier.api.model.X_ZZ_Application_Form;
import za.co.ntier.webform.form.ISaveForm;

public class OrganisationSizeInfo implements ISaveForm {
	private X_ZZ_Application_Form applicationForm;
	/**
	 * case no submitted WSP treat as false
	 */
	private boolean isSubmittedWSP;

	private Integer numOfEmployer;

	private String numOfEmployerTitle = "Number of Employees";

	private String submittedWSPTitle = "Has the organisation submitted the WSP/ATR In previous financial year?";;

	public OrganisationSizeInfo() {
	}

	/**
	 * @return the sdf
	 */
	public X_ZZ_Application_Form getApplicationForm() {
		return applicationForm;
	}

	/**
	 * @return the numOfEmployer
	 */
	/*
	public Integer getNumOfEmployer() {
		if (numOfEmployer == null || numOfEmployer == 0)
			return null;
		return numOfEmployer;
	}
	*/

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
	 * @return the isSubmittedWSP
	 */
	public boolean isSubmittedWSP() {
		return isSubmittedWSP;
	}
	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		if(getNumOfEmployer() == null) {
			applicationForm.set_ValueOfColumn(I_ZZ_Application_Form.COLUMNNAME_NumberEmployees, null);
		}else
			applicationForm.setNumberEmployees(getNumOfEmployer());
		applicationForm.setZZ_HasWSPSubmited(isSubmittedWSP());
		
	}

	/**
	 * @param sdf the sdf to set
	 */
	public void setApplicationForm(X_ZZ_Application_Form applicationForm) {
		this.applicationForm = applicationForm;
	}

	/**
	 * @param numOfEmployer the numOfEmployer to set
	 */
	/*
	public void setNumOfEmployer(Integer numOfEmployer) {
		if (numOfEmployer == null ||numOfEmployer == 0)
			this.numOfEmployer = null;
		else
			this.numOfEmployer = numOfEmployer;
	}
	*/
	
	public void setNumOfEmployer(Integer v) {
	    // ❌ don't do: if (v == null || v <= 0) this.numOfEmployer = null;
	    this.numOfEmployer = v;      // keep 0 as 0
	}
	public Integer getNumOfEmployer() { return numOfEmployer; }


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
}
