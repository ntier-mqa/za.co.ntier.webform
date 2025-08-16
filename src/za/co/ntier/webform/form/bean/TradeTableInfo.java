package za.co.ntier.webform.form.bean;

import za.co.ntier.webform.model.X_ZZ_FormDiscipline;

public class TradeTableInfo extends LearnerInputTableInfo {
	public TradeTableInfo(int programMasterDataID, ProgramType programType) {
		super(programMasterDataID, programType, X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_Trade);
	}

	@Override
	public String getLearnerTilte() {
		return "Trade";
	}
}
