package za.co.ntier.webform.form.viewmodel;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.CompanyInfo;

public class CompanyHeaderVMWrapper {
	private CompanyInfo companyInfo;
	
	@Init
    public void init(@ExecutionArgParam("companyInfo") CompanyInfo companyInfo) {
		this.setCompanyInfo(companyInfo);
    }

	/**
	 * @return the companyInfo
	 */
	public CompanyInfo getCompanyInfo() {
		return companyInfo;
	}

	/**
	 * @param companyInfo the companyInfo to set
	 */
	public void setCompanyInfo(CompanyInfo companyInfo) {
		this.companyInfo = companyInfo;
	}

}
