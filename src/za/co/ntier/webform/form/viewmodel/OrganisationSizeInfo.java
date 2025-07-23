package za.co.ntier.webform.form.viewmodel;

import za.co.ntier.webform.form.viewmodel.master.OrganisationSize;

public class OrganisationSizeInfo {
	private OrganisationSize orgSizeSelected;
	private boolean isSubmittedWSP;
	private boolean isSubmittedPivotal;

	public OrganisationSize getOrgSizeSelected() {
		return orgSizeSelected;
	}

	public void setOrgSizeSelected(OrganisationSize orgSizeSelected) {
		this.orgSizeSelected = orgSizeSelected;
	}
	
	public OrganisationSizeInfo(OrganisationSize orgSizeSelected, boolean isSubmittedWSP, boolean isSubmittedPivotal) {
		this.orgSizeSelected = orgSizeSelected;
		this.setSubmittedWSP(isSubmittedWSP);
		this.setSubmittedPivotal(isSubmittedPivotal);
		
	}

	/**
	 * @return the isSubmittedWSP
	 */
	public boolean isSubmittedWSP() {
		return isSubmittedWSP;
	}

	/**
	 * @param isSubmittedWSP the isSubmittedWSP to set
	 */
	public void setSubmittedWSP(boolean isSubmittedWSP) {
		this.isSubmittedWSP = isSubmittedWSP;
	}

	/**
	 * @return the isSubmittedPivotal
	 */
	public boolean isSubmittedPivotal() {
		return isSubmittedPivotal;
	}

	/**
	 * @param isSubmittedPivotal the isSubmittedPivotal to set
	 */
	public void setSubmittedPivotal(boolean isSubmittedPivotal) {
		this.isSubmittedPivotal = isSubmittedPivotal;
	}
}
