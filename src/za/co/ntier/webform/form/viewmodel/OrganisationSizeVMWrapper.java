package za.co.ntier.webform.form.viewmodel;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.OrganisationSizeInfo;

public class OrganisationSizeVMWrapper {

	private OrganisationSizeInfo orgSizeInfo;

	/**
	 * @return the orgSizeInfo
	 */
	public OrganisationSizeInfo getOrgSizeInfo() {
		return orgSizeInfo;
	}

	@Init
	public void init(@ExecutionArgParam("orgSizeInfo") OrganisationSizeInfo orgSizeInfo) {
		this.setOrgSizeInfo(orgSizeInfo);
	}

	/**
	 * @param orgSizeInfo the orgSizeInfo to set
	 */
	public void setOrgSizeInfo(OrganisationSizeInfo orgSizeInfo) {
		this.orgSizeInfo = orgSizeInfo;
	}
}
