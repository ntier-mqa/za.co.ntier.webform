package za.co.ntier.webform.form.bean;

public class AETInfo {
	private LearnershipTableInfo aetLearnership;
	public AETInfo(int programMasterDataID, ProgramType programType) {
		aetLearnership = LearnershipTableInfo.createGeneralLearnership(programMasterDataID, programType);
	}
	/**
	 * @return the aetLearnership
	 */
	public LearnershipTableInfo getAetLearnership() {
		return aetLearnership;
	}
	/**
	 * @param aetLearnership the aetLearnership to set
	 */
	public void setAetLearnership(LearnershipTableInfo aetLearnership) {
		this.aetLearnership = aetLearnership;
	}
}
