package za.co.ntier.webform.form.bean;

public class NonArtisanDevInfo {
	private LearnershipTableInfo generalLearnership;
	private LearnershipTableInfo firLearnership;
	private int programMasterDataID;
	
	public NonArtisanDevInfo(int programMasterDataID, ProgramType programType) {
		this.setProgramMasterDataID(programMasterDataID);
		setGeneralLearnership(LearnershipTableInfo.createGeneralLearnership(programMasterDataID, programType));
		setFirLearnership(LearnershipTableInfo.create4IRLearnership(programMasterDataID, programType));
	}

	/**
	 * @return the programMasterDataID
	 */
	public int getProgramMasterDataID() {
		return programMasterDataID;
	}

	/**
	 * @param programMasterDataID the programMasterDataID to set
	 */
	public void setProgramMasterDataID(int programMasterDataID) {
		this.programMasterDataID = programMasterDataID;
	}

	/**
	 * @return the generalLearnership
	 */
	public LearnershipTableInfo getGeneralLearnership() {
		return generalLearnership;
	}

	/**
	 * @param generalLearnership the generalLearnership to set
	 */
	public void setGeneralLearnership(LearnershipTableInfo generalLearnership) {
		this.generalLearnership = generalLearnership;
	}

	/**
	 * @return the firLearnership
	 */
	public LearnershipTableInfo getFirLearnership() {
		return firLearnership;
	}

	/**
	 * @param firLearnership the firLearnership to set
	 */
	public void setFirLearnership(LearnershipTableInfo firLearnership) {
		this.firLearnership = firLearnership;
	}
}
