package za.co.ntier.webform.form.viewmodel;

import org.compiere.util.Env;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.form.bean.ProgramInfo;
import za.co.ntier.webform.form.bean.ProgramType;
import za.co.ntier.webform.form.bean.CompanyInfo;
import za.co.ntier.webform.form.bean.EmployerInfo;
import za.co.ntier.webform.form.bean.FormInfo;
import za.co.ntier.webform.model.I_ZZ_Program_Master_Data;
import za.co.ntier.webform.model.X_ZZ_Program_Master_Data;

public class DiscretionaryGrantsApplicationProgramVM {
	private ProgramInfo programInfo;

	private CompanyInfo companyInfo;

	private EmployerInfo employerInfo;

	private FormInfo formInfo;

	private int programMasterDataID;

	/**
	 * @return the programInfo
	 */
	public ProgramInfo getProgramInfo() {
		return programInfo;
	}

	/**
	 * @return the companyInfo
	 */
	public CompanyInfo getCompanyInfo() {
		return companyInfo;
	}

	// --- Getters and Setters for Data Binding ---

	/**
	 * @return the employerInfo
	 */
	public EmployerInfo getEmployerInfo() {
		return employerInfo;
	}

	/**
	 * @return the formInfo
	 */
	public FormInfo getFormInfo() {
		return formInfo;
	}
	
	private ProgramType programType;

	/**
	 * @return the programMasterDataID
	 */
	public int getProgramMasterDataID() {
		return programMasterDataID;
	}

	@Init
	public void init(@ExecutionArgParam(WebForm.programMasterDataUUKey) String programMasterDataKey,
			@ExecutionArgParam(WebForm.programType) String sProgramType) {
		
		this.programType = ProgramType.valueOf(sProgramType.toUpperCase());
		I_ZZ_Program_Master_Data masterData = new X_ZZ_Program_Master_Data(Env.getCtx(), programMasterDataKey, null);

		setProgramMasterDataID(masterData.getZZ_Program_Master_Data_ID());

		setCompanyInfo(CompanyInfo.getDefaultCompanyInfo());
		setFormInfo(new FormInfo(programType, "DGAF01"));

		employerInfo = new EmployerInfo();
		programInfo = new ProgramInfo(masterData.getZZ_Program_Master_Data_ID(), programType);

	}

	/**
	 * @param programInfo the programInfo to set
	 */
	public void setProgramInfo(ProgramInfo programInfo) {
		this.programInfo = programInfo;
	}

	/**
	 * @param companyInfo the companyInfo to set
	 */
	public void setCompanyInfo(CompanyInfo companyInfo) {
		this.companyInfo = companyInfo;
	}

	/**
	 * @param employerInfo the employerInfo to set
	 */
	public void setEmployerInfo(EmployerInfo employerInfo) {
		this.employerInfo = employerInfo;
	}

	/**
	 * @param formInfo the formInfo to set
	 */
	public void setFormInfo(FormInfo formInfo) {
		this.formInfo = formInfo;
	}

	/**
	 * @param programMasterDataID the programMasterDataID to set
	 */
	public void setProgramMasterDataID(int programMasterDataID) {
		this.programMasterDataID = programMasterDataID;
	}

}