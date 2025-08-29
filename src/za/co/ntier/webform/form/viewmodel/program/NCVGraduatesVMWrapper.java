package za.co.ntier.webform.form.viewmodel.program;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.program.NCVGraduatesProgram;

public class NCVGraduatesVMWrapper {
    private NCVGraduatesProgram ncvGraduatesInfo;

    @Init
    public void init(@ExecutionArgParam("ncvGraduatesInfo") NCVGraduatesProgram ncvGraduatesInfo) {
        this.setNcvGraduatesInfo(ncvGraduatesInfo);
    }

	/**
	 * @return the ncvGraduatesInfo
	 */
	public NCVGraduatesProgram getNcvGraduatesInfo() {
		return ncvGraduatesInfo;
	}

	/**
	 * @param ncvGraduatesInfo the ncvGraduatesInfo to set
	 */
	public void setNcvGraduatesInfo(NCVGraduatesProgram ncvGraduatesInfo) {
		this.ncvGraduatesInfo = ncvGraduatesInfo;
	}

}
