package za.co.ntier.webform.form.bean;

public class OrganisationSizeInfo {
	private boolean isSubmittedPivotal;
	private boolean isSubmittedWSP;
	private int numOfEmployer;

	private String numOfEmployerTitle = "Number of Employees";

	private String submittedPivotalTitle = "Has the organisation submitted the Pivotal Plan and Report In previous financial year?";

	private String submittedWSPTitle = "Has the organisation submitted the WSP/ATR In previous financial year?";

	public OrganisationSizeInfo() {};
	
	public OrganisationSizeInfo(int numOfEmployer, boolean isSubmittedWSP, boolean isSubmittedPivotal) {
		this.setSubmittedWSP(isSubmittedWSP);
		this.setSubmittedPivotal(isSubmittedPivotal);
		setNumOfEmployer(numOfEmployer);
	}

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

	/**
	 * @return the submittedPivotalTitle
	 */
	public String getSubmittedPivotalTitle() {
		return submittedPivotalTitle;
	}

	/**
	 * @return the submittedWSPTitle
	 */
	public String getSubmittedWSPTitle() {
		return submittedWSPTitle;
	}

	/**
	 * @return the isSubmittedPivotal
	 */
	public boolean isSubmittedPivotal() {
		return isSubmittedPivotal;
	}

	
	public String getSubmittedPivotalText() {
		return isSubmittedPivotal ? "YES" : "NO";
	}
	
	public String getSubmittedWSPText() {
		return isSubmittedWSP ? "YES" : "NO";
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
	 * @param isSubmittedPivotal the isSubmittedPivotal to set
	 */
	public void setSubmittedPivotal(boolean isSubmittedPivotal) {
		this.isSubmittedPivotal = isSubmittedPivotal;
	}

	/**
	 * @param submittedPivotalTitle the submittedPivotalTitle to set
	 */
	public void setSubmittedPivotalTitle(String submittedPivotalTitle) {
		this.submittedPivotalTitle = submittedPivotalTitle;
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
