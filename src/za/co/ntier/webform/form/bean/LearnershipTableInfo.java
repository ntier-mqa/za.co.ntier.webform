package za.co.ntier.webform.form.bean;

public class LearnershipTableInfo extends LearnerInputTableInfo {
	public final static String learnerInputType_General_Learnership = "General Learnership";
	public final static String learnerInputType_4IR_Learnership = "4IR Learnership";
	
	private LearnershipTableInfo(int programMasterDataID, ProgramType programType, String learnerInputType) {
		super(programMasterDataID, programType, learnerInputType);
		setShowEmployedLearners(true);
		setShowLearners(false);
	}

	public static LearnershipTableInfo create4IRLearnership(int programMasterDataID, ProgramType programType) {
		return new LearnershipTableInfo(programMasterDataID, programType, learnerInputType_4IR_Learnership);
	}
	
	public static LearnershipTableInfo createGeneralLearnership(int programMasterDataID, ProgramType programType) {
		return new LearnershipTableInfo(programMasterDataID, programType, learnerInputType_General_Learnership);
	}
	
	@Override
	public String getLearnerInputColTilte() {
		return "Learnership Type";
	}
	
}
