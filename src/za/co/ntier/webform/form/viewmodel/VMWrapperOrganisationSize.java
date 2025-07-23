package za.co.ntier.webform.form.viewmodel;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

public class VMWrapperOrganisationSize {
	
	private OrganisationSizeInfo orgSizeInfo;
	
	@Init
    public void init(@ExecutionArgParam("orgSizeInfo") OrganisationSizeInfo orgSizeInfo) {
		this.setOrgSizeInfo(orgSizeInfo);
    }

	/**
	 * @return the orgSizeInfo
	 */
	public OrganisationSizeInfo getOrgSizeInfo() {
		return orgSizeInfo;
	}

	/**
	 * @param orgSizeInfo the orgSizeInfo to set
	 */
	public void setOrgSizeInfo(OrganisationSizeInfo orgSizeInfo) {
		this.orgSizeInfo = orgSizeInfo;
	}
}
