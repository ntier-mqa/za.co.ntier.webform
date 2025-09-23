package za.co.ntier.webform.form.viewmodel.program;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.CheckEvent;

import za.co.ntier.webform.form.bean.program.HetLectureSupport;

@Init(superclass = true)
public class HetLectureSupportVMWrapper extends ProgramVMWrapper<HetLectureSupport> {
	@Command
	public void checkInstitutionParticipated(@ContextParam(ContextType.TRIGGER_EVENT) CheckEvent event) {
		getProgram().checkInstitutionParticipated(event);
	}
}
