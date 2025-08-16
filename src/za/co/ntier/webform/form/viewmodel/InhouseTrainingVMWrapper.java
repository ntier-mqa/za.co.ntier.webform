package za.co.ntier.webform.form.viewmodel;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.InhouseTrainingInfo;

public class InhouseTrainingVMWrapper {
	private InhouseTrainingInfo inhouseTrainingInfo;

	@Init
    public void init(@ExecutionArgParam("inhouseTrainingInfo") InhouseTrainingInfo inhouseTrainingInfo) {
		this.setInhouseTrainingInfo(inhouseTrainingInfo);
    }

	/**
	 * @return the inhouseTrainingInfo
	 */
	public InhouseTrainingInfo getInhouseTrainingInfo() {
		return inhouseTrainingInfo;
	}

	/**
	 * @param inhouseTrainingInfo the inhouseTrainingInfo to set
	 */
	public void setInhouseTrainingInfo(InhouseTrainingInfo inhouseTrainingInfo) {
		this.inhouseTrainingInfo = inhouseTrainingInfo;
	}
}
