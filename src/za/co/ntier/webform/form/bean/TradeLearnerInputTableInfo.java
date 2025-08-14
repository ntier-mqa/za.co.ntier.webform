package za.co.ntier.webform.form.bean;

public class TradeLearnerInputTableInfo extends LearnerInputTableInfo {
	public TradeLearnerInputTableInfo(int programMasterDataID, ProgramType programType, String learnerInputType) {
		super(programMasterDataID, programType, learnerInputType);
	}

	@Override
	public String getLearnerInputColTilte() {
		return "Trade";
	}
}
