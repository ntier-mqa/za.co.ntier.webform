package za.co.ntier.webform.form.viewmodel;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import za.co.ntier.webform.form.bean.NCVGraduatesInfo;

public class NCVGraduatesVMWrapper {
    private NCVGraduatesInfo ncvGraduatesInfo;

    @Init
    public void init(@ExecutionArgParam("ncvGraduatesInfo") NCVGraduatesInfo ncvGraduatesInfo) {
        this.setNcvGraduatesInfo(ncvGraduatesInfo);
    }

	/**
	 * @return the ncvGraduatesInfo
	 */
	public NCVGraduatesInfo getNcvGraduatesInfo() {
		return ncvGraduatesInfo;
	}

	/**
	 * @param ncvGraduatesInfo the ncvGraduatesInfo to set
	 */
	public void setNcvGraduatesInfo(NCVGraduatesInfo ncvGraduatesInfo) {
		this.ncvGraduatesInfo = ncvGraduatesInfo;
	}

}
