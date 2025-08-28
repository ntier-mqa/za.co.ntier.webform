package za.co.ntier.webform.form.bean;

import za.co.ntier.webform.model.X_ZZ_FormDiscipline;

public class LearnershipTableInfo extends LearnerInputTableInfo {
	private LearnershipTableInfo(int programMasterDataID, ProgramType programType, String learnerInputType) {
		super(programMasterDataID, programType, learnerInputType);
		setShowNoEmployed(true);
		setShowNoUnEmployed(true);
		setShowNoLearners(false);
	}

	public static LearnershipTableInfo create4IRLearnership(int programMasterDataID, ProgramType programType) {
		return new LearnershipTableInfo(programMasterDataID, programType, X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_4IRLearnership);
	}
	
	public static LearnershipTableInfo createGeneralLearnership(int programMasterDataID, ProgramType programType) {
		return new LearnershipTableInfo(programMasterDataID, programType, X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_GeneralLearnership);
	}
	
	public static LearnershipTableInfo createAetLearnership(int programMasterDataID, ProgramType programType) {
		return new LearnershipTableInfo(programMasterDataID, programType, X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_AETLearnership);
	}
	
	@Override
	public String getLearnerTilte() {
		if(X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_AETLearnership.equalsIgnoreCase(getLearnerInputType())) {
			return "Name of Programme";
		}
		return "Learnership Type";
	}

	@Override
	public int getNumOfCol (){
		if (isHasAccred() && isHasWPAReq())
			return 7;
		else if(!isHasAccred() && !isHasWPAReq())
			return 5;
		else
			return 6;
	}
	
}
