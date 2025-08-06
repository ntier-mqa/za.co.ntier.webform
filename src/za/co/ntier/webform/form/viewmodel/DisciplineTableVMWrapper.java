package za.co.ntier.webform.form.viewmodel;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.media.Media;

import za.co.ntier.webform.form.bean.Discipline;
import za.co.ntier.webform.form.bean.DisciplineTableInfo;

public class DisciplineTableVMWrapper {
	private DisciplineTableInfo disciplineTableInfo;

	/**
	 * @return the disciplineTableInfo
	 */
	public DisciplineTableInfo getDisciplineTableInfo() {
		return disciplineTableInfo;
	}

	@Init
	public void init(@ExecutionArgParam("disciplineTableInfo") DisciplineTableInfo disciplineTableInfo) {
		this.disciplineTableInfo = disciplineTableInfo;

	}

	@Command({ "noOfLearnerChange" })
	public void noOfLearnerChange() {
		disciplineTableInfo.noOfLearnerChange();
	}

	/**
	 * @param disciplineTableInfo the disciplineTableInfo to set
	 */
	public void setDisciplineTableInfo(DisciplineTableInfo disciplineTableInfo) {
		this.disciplineTableInfo = disciplineTableInfo;
	}

	@Command
	public void uploadFile(@BindingParam("media") Media media, @BindingParam("isDSA") boolean isDSA,
			@BindingParam("discipline") Discipline discipline, @BindingParam("index") int index) {
		discipline.uploadFile(media, isDSA);
	}

}
