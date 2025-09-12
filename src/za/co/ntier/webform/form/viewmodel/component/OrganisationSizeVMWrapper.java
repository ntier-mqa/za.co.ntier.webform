package za.co.ntier.webform.form.viewmodel.component;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.component.OrganisationSizeInfo;
import za.co.ntier.webform.form.viewmodel.DiscretionaryGrantsApplicationProgramVM;

public class OrganisationSizeVMWrapper {

	
	private OrganisationSizeInfo orgSizeInfo;
    private DiscretionaryGrantsApplicationProgramVM applicationProgramVM;
    private String notifyTarget;

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

	@Init
	public void init(
			@ExecutionArgParam("orgSizeInfo") OrganisationSizeInfo orgSizeInfo,
			@ExecutionArgParam("applicationProgramVM") DiscretionaryGrantsApplicationProgramVM appVM,
			@ExecutionArgParam("notifyTarget") String notifyTarget
			) {
		this.orgSizeInfo = orgSizeInfo;
		this.applicationProgramVM = appVM;
		this.notifyTarget = notifyTarget;
	}

	@Command
	public void notifyParentCompleteness() {
		if (applicationProgramVM != null && notifyTarget != null) {
			BindUtils.postNotifyChange(null, null, applicationProgramVM, notifyTarget);
		}
	}
	
	@Command
	public void numEmployerChanging(@BindingParam("val") String val) {
	    Integer n = null;
	    if (val != null) {
	        String s = val.trim();
	        if (!s.isEmpty()) {
	            try { n = Integer.valueOf(s); } catch (NumberFormatException ignore) {}
	        }
	    }
	    orgSizeInfo.setNumOfEmployer(n);  // <-- write latest (including null)
	    BindUtils.postNotifyChange(null, null, applicationProgramVM, "organisationComplete");
	}
}
