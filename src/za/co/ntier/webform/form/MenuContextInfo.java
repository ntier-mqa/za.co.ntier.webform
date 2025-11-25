package za.co.ntier.webform.form;

import java.util.HashMap;
import java.util.Map;

import za.co.ntier.api.model.I_ZZ_Program_Master_Data;
import za.co.ntier.webform.form.bean.ProgramType;

public class MenuContextInfo {
	private String applicationFormUU;
	private String formTitle;
	private boolean isUploadWPAForNVC;
	private I_ZZ_Program_Master_Data programMasterData;
	private ProgramType programType;
	private String zulPath;
	private String recordUU;
	private int recordID = 0;
	private Map<String, String> moreContext = new HashMap<>();
	
	public String getContextParam(String paramKey) {
		return moreContext.get(paramKey);
	}
	
	public MenuContextInfo(ProgramType programType, String zulPath, I_ZZ_Program_Master_Data programMasterData,
			boolean isUploadWPAForNVC, String formTitle, String applicationFormUU) {
		super();
		this.programType = programType;
		this.zulPath = zulPath;
		this.programMasterData = programMasterData;
		this.isUploadWPAForNVC = isUploadWPAForNVC;
		this.setFormTitle(formTitle);
		this.setApplicationFormUU(applicationFormUU);
	}

	/**
	 * @return the applicationFormUU
	 */
	public String getApplicationFormUU() {
		return applicationFormUU;
	}

	/**
	 * @return the formTitle
	 */
	public String getFormTitle() {
		return formTitle;
	}

	/**
	 * @return the isUploadWPAForNVC
	 */
	public boolean getIsUploadWPAForNVC() {
		return isUploadWPAForNVC;
	}

	/**
	 * @return the programMasterDataID
	 */
	public I_ZZ_Program_Master_Data getProgramMasterData() {
		return programMasterData;
	}

	/**
	 * @return the programTypeMenuContextKey
	 */
	public ProgramType getProgramType() {
		return programType;
	}

	/**
	 * @return the zulPath
	 */
	public String getZulPath() {
		return zulPath;
	}

	/**
	 * @param applicationFormUU the applicationFormUU to set
	 */
	public void setApplicationFormUU(String applicationFormUU) {
		this.applicationFormUU = applicationFormUU;
	}

	/**
	 * @param formTitle the formTitle to set
	 */
	public void setFormTitle(String formTitle) {
		this.formTitle = formTitle;
	}

	/**
	 * @param isUploadWPAForNVC the isUploadWPAForNVC to set
	 */
	public void setIsUploadWPAForNVC(boolean isUploadWPAForNVC) {
		this.isUploadWPAForNVC = isUploadWPAForNVC;
	}

	/**
	 * @param programMasterDataID the programMasterDataID to set
	 */
	public void setProgramMasterData(I_ZZ_Program_Master_Data programMasterData) {
		this.programMasterData = programMasterData;
	}

	/**
	 * @param programTypeMenuContextKey the programTypeMenuContextKey to set
	 */
	public void setProgramType(ProgramType programType) {
		this.programType = programType;
	}

	/**
	 * @param zulPath the zulPath to set
	 */
	public void setZulPath(String zulPath) {
		this.zulPath = zulPath;
	}

	/**
	 * @return the recordUU
	 */
	public String getRecordUU() {
		return recordUU;
	}

	/**
	 * @param recordUU the recordUU to set
	 */
	public void setRecordUU(String recordUU) {
		this.recordUU = recordUU;
	}

	/**
	 * @return the recordID
	 */
	public int getRecordID() {
		return recordID;
	}

	/**
	 * @param recordID the recordID to set
	 */
	public void setRecordID(int recordID) {
		this.recordID = recordID;
	}

	/**
	 * @return the moreParaContext
	 */
	public Map<String, String> getMoreContext() {
		return moreContext;
	}
}
