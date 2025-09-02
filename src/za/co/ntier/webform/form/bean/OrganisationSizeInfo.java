package za.co.ntier.webform.form.bean;

public class OrganisationSizeInfo {
	private boolean isSubmittedWSP;
	private int numOfEmployer;

	private String numOfEmployerTitle = "Number of Employees";

	private String submittedWSPTitle = "Has the organisation submitted the WSP/ATR In previous financial year?";

	public OrganisationSizeInfo() {
	};

	/**
	 * @return the numOfEmployer
	 */
	public int getNumOfEmployer() {
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

	/**
	 * @param numOfEmployer the numOfEmployer to set
	 */
	public void setNumOfEmployer(int numOfEmployer) {
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
}
