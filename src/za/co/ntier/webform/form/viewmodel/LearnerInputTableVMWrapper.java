package za.co.ntier.webform.form.viewmodel;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.media.Media;

import za.co.ntier.webform.form.bean.LearnerInputInfo;
import za.co.ntier.webform.form.bean.LearnerInputTableInfo;

public class LearnerInputTableVMWrapper {
	private LearnerInputTableInfo learnerInputTableInfo;

	/**
	 * @return the learnerInputTableInfo
	 */
	public LearnerInputTableInfo getLearnerInputTableInfo() {
		return learnerInputTableInfo;
	}

	@Init
	public void init(@ExecutionArgParam("learnerInputTableInfo") LearnerInputTableInfo learnerInputTableInfo) {
		this.learnerInputTableInfo = learnerInputTableInfo;

	}

	@Command({ "noOfLearnersChange" })
	public void noOfLearnersChange() {
		learnerInputTableInfo.noOfLearnersChange();
	}
	
	@Command({ "noOfEmployedLearnersChange" })
	public void noOfEmployedLearnersChange() {
		learnerInputTableInfo.noOfEmployedLearnersChange();
	}

	@Command({ "noOfUnEmployedLearnersChange" })
	public void noOfUnEmployedLearnersChange() {
		learnerInputTableInfo.noOfUnEmployedLearnersChange();
	}
	
	/**
	 * @param learnerInputTableInfo the learnerInputTableInfo to set
	 */
	public void setLearnerInputTableInfo(LearnerInputTableInfo learnerInputTableInfo) {
		this.learnerInputTableInfo = learnerInputTableInfo;
	}

	@Command
	public void uploadFile(@BindingParam("media") Media media, @BindingParam("isDSA") boolean isDSA,
			@BindingParam("learnerInputInfo") LearnerInputInfo learnerInputInfo, @BindingParam("index") int index) {
		learnerInputInfo.uploadFile(media, isDSA);
	}

}
