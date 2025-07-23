package za.co.ntier.webform.form.viewmodel;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

public class VMWrapperEmployerInfo {
	private EmployerInfo employerInfo;
	
	@Init
    public void init(@ExecutionArgParam("employerInfo") EmployerInfo employerInfo) {
		this.employerInfo = employerInfo;
    }

	/**
	 * @return the employerInfo
	 */
	public EmployerInfo getEmployerInfo() {
		return employerInfo;
	}

	/**
	 * @param employerInfo the employerInfo to set
	 */
	public void setEmployerInfo(EmployerInfo employerInfo) {
		this.employerInfo = employerInfo;
	}
}
