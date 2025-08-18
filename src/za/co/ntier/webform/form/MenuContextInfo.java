package za.co.ntier.webform.form;

import za.co.ntier.webform.form.bean.ProgramType;
import za.co.ntier.webform.model.I_ZZ_Program_Master_Data;

public class MenuContextInfo {
	private boolean isUploadWPAForNVC;
	private I_ZZ_Program_Master_Data programMasterData;
	private ProgramType programType;
	private String zulPath;
	private String formTitle;
	
	public MenuContextInfo(ProgramType programType, String zulPath, I_ZZ_Program_Master_Data programMasterData, boolean isUploadWPAForNVC, String formTitle) {
		super();
		this.programType = programType;
		this.zulPath = zulPath;
		this.programMasterData = programMasterData;
		this.isUploadWPAForNVC = isUploadWPAForNVC;
		this.setFormTitle(formTitle);
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
	 * @return the zulPath
	 */
	public String getZulPath() {
		return zulPath;
	}

	/**
	 * @param zulPath the zulPath to set
	 */
	public void setZulPath(String zulPath) {
		this.zulPath = zulPath;
	}

	/**
	 * @return the formTitle
	 */
	public String getFormTitle() {
		return formTitle;
	}

	/**
	 * @param formTitle the formTitle to set
	 */
	public void setFormTitle(String formTitle) {
		this.formTitle = formTitle;
	}
}
