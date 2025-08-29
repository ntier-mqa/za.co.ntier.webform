package za.co.ntier.webform.form.viewmodel.program;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.program.CentreOfSpecialisationProgram;

public class CentreOfSpecialisationVMWrapper {
	private CentreOfSpecialisationProgram centreOfSpecialisation;

	@Init
	public void init(@ExecutionArgParam("centreOfSpecialisationInfo") CentreOfSpecialisationProgram centreOfSpecialisation) {
		this.setCentreOfSpecialisation(centreOfSpecialisation);
	}

	/**
	 * @return the centreOfSpecialisation
	 */
	public CentreOfSpecialisationProgram getCentreOfSpecialisation() {
		return centreOfSpecialisation;
	}

	/**
	 * @param centreOfSpecialisation the centreOfSpecialisation to set
	 */
	public void setCentreOfSpecialisation(CentreOfSpecialisationProgram centreOfSpecialisation) {
		this.centreOfSpecialisation = centreOfSpecialisation;
	}
}
