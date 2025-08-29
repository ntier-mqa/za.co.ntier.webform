package za.co.ntier.webform.form.viewmodel.program;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.program.InhouseTrainingProgram;

public class InhouseTrainingVMWrapper {
	private InhouseTrainingProgram inhouseTrainingInfo;

	@Init
    public void init(@ExecutionArgParam("inhouseTrainingInfo") InhouseTrainingProgram inhouseTrainingInfo) {
		this.setInhouseTrainingInfo(inhouseTrainingInfo);
    }

	/**
	 * @return the inhouseTrainingInfo
	 */
	public InhouseTrainingProgram getInhouseTrainingInfo() {
		return inhouseTrainingInfo;
	}

	/**
	 * @param inhouseTrainingInfo the inhouseTrainingInfo to set
	 */
	public void setInhouseTrainingInfo(InhouseTrainingProgram inhouseTrainingInfo) {
		this.inhouseTrainingInfo = inhouseTrainingInfo;
	}
}
