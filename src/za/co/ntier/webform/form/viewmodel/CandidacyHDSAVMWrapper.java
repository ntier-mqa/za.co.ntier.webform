package za.co.ntier.webform.form.viewmodel;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.Media;

import za.co.ntier.webform.form.bean.CandidacyInfo;
import za.co.ntier.webform.form.bean.DisciplineHDSA;

public class CandidacyHDSAVMWrapper {

	private CandidacyInfo candidacyInfo;

	/**
	 * @return the candidacyInfo
	 */
	public CandidacyInfo getCandidacyInfo() {
		return candidacyInfo;
	}

	@Init
	public void init(@ExecutionArgParam("candidacyInfo") CandidacyInfo candidacyInfo) {
		this.candidacyInfo = candidacyInfo;

	}

	@Command({ "noOfLearnerChange" })
	public void noOfLearnerChange() {
		candidacyInfo.noOfLearnerChange();
	}

	/**
	 * @param candidacyInfo the candidacyInfo to set
	 */
	public void setCandidacyInfo(CandidacyInfo candidacyInfo) {
		this.candidacyInfo = candidacyInfo;
	}

	@Command
	public void uploadFile(@BindingParam("media") Media media, @BindingParam("isDSA") boolean isDSA,
			@BindingParam("discipline") DisciplineHDSA discipline, @BindingParam("index") int index) {
		discipline.uploadFile(media, isDSA);

	}
}
