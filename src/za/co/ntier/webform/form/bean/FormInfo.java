package za.co.ntier.webform.form.bean;

import org.compiere.model.MYear;
import org.compiere.util.Env;

import za.co.ntier.webform.form.MenuContextInfo;

public class FormInfo {
	private String approved;
	private String approvedDate;
	private String approvedDateTitle;
	private String approvedTitle;
	private String formHeader;

	private String orgName;
	private String revision;
	private String revisionTitle;

	public FormInfo(MenuContextInfo menuContextInfo) {
		String formName = menuContextInfo.getProgramMasterData().getTitle();
		int year = 0;
		if (menuContextInfo.getProgramMasterData().getC_Year_ID() != 0) {
			MYear fiscalYear = new MYear(Env.getCtx(), menuContextInfo.getProgramMasterData().getC_Year_ID(), null);
			year = fiscalYear.getYearAsInt();
		}

		this.formHeader = String.format("%s - %d/%d", formName, year, year + 1);
		this.orgName = "COO";
		this.revision = "02";
		this.approved = "ACOO";
		this.approvedDate = "03/10/2024";

		this.revisionTitle = "Revision";
		this.approvedTitle = "Approved";
		this.approvedDateTitle = "Date";
	}

	/**
	 * @return the approved
	 */
	public String getApproved() {
		return approved;
	}

	/**
	 * @return the approvedDate
	 */
	public String getApprovedDate() {
		return approvedDate;
	}

	/**
	 * @return the approvedDateTitle
	 */
	public String getApprovedDateTitle() {
		return approvedDateTitle;
	}

	/**
	 * @return the approvedTitle
	 */
	public String getApprovedTitle() {
		return approvedTitle;
	}

	/**
	 * @return the formHeader
	 */
	public String getFormHeader() {
		return formHeader;
	}

	/**
	 * @return the orgName
	 */
	public String getOrgName() {
		return orgName;
	}

	/**
	 * @return the revision
	 */
	public String getRevision() {
		return revision;
	}

	/**
	 * @return the revisionTitle
	 */
	public String getRevisionTitle() {
		return revisionTitle;
	}

	/**
	 * @param approved the approved to set
	 */
	public void setApproved(String approved) {
		this.approved = approved;
	}

	/**
	 * @param approvedDate the approvedDate to set
	 */
	public void setApprovedDate(String approvedDate) {
		this.approvedDate = approvedDate;
	}

	/**
	 * @param approvedDateTitle the approvedDateTitle to set
	 */
	public void setApprovedDateTitle(String approvedDateTitle) {
		this.approvedDateTitle = approvedDateTitle;
	}

	/**
	 * @param approvedTitle the approvedTitle to set
	 */
	public void setApprovedTitle(String approvedTitle) {
		this.approvedTitle = approvedTitle;
	}

	/**
	 * @param formHeader the formHeader to set
	 */
	public void setFormHeader(String formHeader) {
		this.formHeader = formHeader;
	}

	/**
	 * @param orgName the orgName to set
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	/**
	 * @param revision the revision to set
	 */
	public void setRevision(String revision) {
		this.revision = revision;
	}

	/**
	 * @param revisionTitle the revisionTitle to set
	 */
	public void setRevisionTitle(String revisionTitle) {
		this.revisionTitle = revisionTitle;
	}
}
