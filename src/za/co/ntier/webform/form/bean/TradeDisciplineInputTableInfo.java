package za.co.ntier.webform.form.bean;

import za.co.ntier.webform.model.X_ZZ_FormDiscipline;

public class TradeDisciplineInputTableInfo extends LearnerInputTableInfo{

	private TradeDisciplineInputTableInfo(int programMasterDataID, ProgramType programType, String learnerInputType) {
		super(programMasterDataID, programType, learnerInputType);
	}
	
	@Override
	public int getNumOfCol (){
		if (isHasAccred() && isHasWPAReq())
			return 6;
		else if(!isHasAccred() && !isHasWPAReq())
			return 4;
		else
			return 5;
	}
	
	@Override
	public String getLearnerTilte() {
		return X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_Discipline.equals(getLearnerInputType()) ? 
				"Discipline" : "Trade"; 
		
	}
	
	public static LearnerInputTableInfo getTrade(int programMasterDataID, ProgramType programType) {
		return new TradeDisciplineInputTableInfo(programMasterDataID, programType, X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_Trade);
	}
	
	public static LearnerInputTableInfo getDiscipline(int programMasterDataID, ProgramType programType) {
		return new TradeDisciplineInputTableInfo(programMasterDataID, programType, X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_Discipline);
	}
}
