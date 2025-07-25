package za.co.ntier.webform.form.bean;

import za.co.ntier.webform.form.viewmodel.master.OrganisationSize;

public class OrganisationSizeInfo {
	private boolean isSubmittedPivotal;
	private boolean isSubmittedWSP;
	private OrganisationSize orgSizeSelected;

	public OrganisationSizeInfo(OrganisationSize orgSizeSelected, boolean isSubmittedWSP, boolean isSubmittedPivotal) {
		this.orgSizeSelected = orgSizeSelected;
		this.setSubmittedWSP(isSubmittedWSP);
		this.setSubmittedPivotal(isSubmittedPivotal);

	}

	public OrganisationSize getOrgSizeSelected() {
		return orgSizeSelected;
	}

	/**
	 * @return the isSubmittedPivotal
	 */
	public boolean isSubmittedPivotal() {
		return isSubmittedPivotal;
	}

	/**
	 * @return the isSubmittedWSP
	 */
	public boolean isSubmittedWSP() {
		return isSubmittedWSP;
	}

	public void setOrgSizeSelected(OrganisationSize orgSizeSelected) {
		this.orgSizeSelected = orgSizeSelected;
	}

	/**
	 * @param isSubmittedPivotal the isSubmittedPivotal to set
	 */
	public void setSubmittedPivotal(boolean isSubmittedPivotal) {
		this.isSubmittedPivotal = isSubmittedPivotal;
	}

	/**
	 * @param isSubmittedWSP the isSubmittedWSP to set
	 */
	public void setSubmittedWSP(boolean isSubmittedWSP) {
		this.isSubmittedWSP = isSubmittedWSP;
	}
}
