package za.co.ntier.webform.form.viewmodel;

import java.util.List;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.media.Media;

import za.co.ntier.webform.form.bean.DisciplineHDSA;

public class DisciplineHDSAVMWrapper {
	private List<DisciplineHDSA> disciplineHDSAs;

	/**
	 * @return the disciplineHDSAs
	 */
	public List<DisciplineHDSA> getDisciplineHDSAs() {
		return disciplineHDSAs;
	}

	@Init
	public void init(@ExecutionArgParam("disciplineHDSAs") List<DisciplineHDSA> disciplineHDSAs) {
		this.setDisciplineHDSAs(disciplineHDSAs);
	}

	@Command({ "postalCodeLookup" })
	public void postalCodeLookup(@BindingParam("discipline") DisciplineHDSA discipline,
			@BindingParam("sitePostalCode") String sitePostalCode) {

	}

	/**
	 * @param disciplineHDSAs the disciplineHDSAs to set
	 */
	public void setDisciplineHDSAs(List<DisciplineHDSA> disciplineHDSAs) {
		this.disciplineHDSAs = disciplineHDSAs;
	}

	@Command
	public void uploadFile(@BindingParam("media") Media media, @BindingParam("isDSA") boolean isDSA,
			@BindingParam("discipline") DisciplineHDSA discipline, @BindingParam("index") int index) {
		discipline.uploadFile(media, isDSA);
		if (isDSA) {
			discipline.setFileNameDSA(media.getName());
		} else {
			discipline.setFileNameHDSA(media.getName());
		}
	}
}
