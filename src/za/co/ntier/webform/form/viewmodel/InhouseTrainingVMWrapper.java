package za.co.ntier.webform.form.viewmodel;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.InhouseTrainingInfo;

public class InhouseTrainingVMWrapper {
	private InhouseTrainingInfo inHouseTrainingInfo;

	@Init
    public void init(@ExecutionArgParam("inHouseTrainingInfo") InhouseTrainingInfo inHouseTrainingInfo) {
		this.setInHouseTrainingInfo(inHouseTrainingInfo);
    }

	/**
	 * @return the inHouseTrainingInfo
	 */
	public InhouseTrainingInfo getInHouseTrainingInfo() {
		return inHouseTrainingInfo;
	}

	/**
	 * @param inHouseTrainingInfo the inHouseTrainingInfo to set
	 */
	public void setInHouseTrainingInfo(InhouseTrainingInfo inHouseTrainingInfo) {
		this.inHouseTrainingInfo = inHouseTrainingInfo;
	}
}
