package za.co.ntier.webform.form.viewmodel;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.CentreOfSpecialisationInfo;

public class CentreOfSpecialisationVMWrapper {
	private CentreOfSpecialisationInfo centreOfSpecialisation;

	@Init
	public void init(@ExecutionArgParam("centreOfSpecialisationInfo") CentreOfSpecialisationInfo centreOfSpecialisation) {
		this.setCentreOfSpecialisation(centreOfSpecialisation);
	}

	/**
	 * @return the centreOfSpecialisation
	 */
	public CentreOfSpecialisationInfo getCentreOfSpecialisation() {
		return centreOfSpecialisation;
	}

	/**
	 * @param centreOfSpecialisation the centreOfSpecialisation to set
	 */
	public void setCentreOfSpecialisation(CentreOfSpecialisationInfo centreOfSpecialisation) {
		this.centreOfSpecialisation = centreOfSpecialisation;
	}
}
