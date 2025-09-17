package za.co.ntier.webform.form.viewmodel.program;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.form.viewmodel.DiscretionaryGrantsApplicationProgramVM;

public class ProgramVMWrapper<T> {
	public static final String ProgramKey = "program";
	private T program;
	private DiscretionaryGrantsApplicationProgramVM applicationProgramVM;
	private String notifyTarget;
	private MenuContextInfo menuContextInfo;
	/**
	 * @return the program
	 */
	public T getProgram() {
		return program;
	}

	@Init
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo,
			@ExecutionArgParam(ProgramKey) T program,
			@ExecutionArgParam("applicationProgramVM") DiscretionaryGrantsApplicationProgramVM applicationProgramVM,
			@ExecutionArgParam("notifyTarget") String notifyTarget) throws Exception {
		this.program = program;
		this.applicationProgramVM = applicationProgramVM;
		this.setNotifyTarget(notifyTarget);
		this.setMenuContextInfo(menuContextInfo);
		BindUtils.postNotifyChange(null, null, this.applicationProgramVM, "programComplete"); // if editing check if values are there
	}

	/**
	 * @param program the program to set
	 */
	public void setProgram(T program) {
		this.program = program;
	}

	/**
	 * @return the applicationProgramVM
	 */
	public DiscretionaryGrantsApplicationProgramVM getApplicationProgramVM() {
		return applicationProgramVM;
	}

	/**
	 * @param applicationProgramVM the applicationProgramVM to set
	 */
	public void setApplicationProgramVM(DiscretionaryGrantsApplicationProgramVM applicationProgramVM) {
		this.applicationProgramVM = applicationProgramVM;
	}

	/**
	 * @return the notifyTarget
	 */
	public String getNotifyTarget() {
		return notifyTarget;
	}

	/**
	 * @param notifyTarget the notifyTarget to set
	 */
	public void setNotifyTarget(String notifyTarget) {
		this.notifyTarget = notifyTarget;
	}

	/**
	 * @return the menuContextInfo
	 */
	public MenuContextInfo getMenuContextInfo() {
		return menuContextInfo;
	}

	/**
	 * @param menuContextInfo the menuContextInfo to set
	 */
	public void setMenuContextInfo(MenuContextInfo menuContextInfo) {
		this.menuContextInfo = menuContextInfo;
	}
	
	@Command("notifyProgramComplete")
    public void notifyProgramComplete() {
        // Tell the top-level VM to re-evaluate isProgramComplete()
        BindUtils.postNotifyChange(null, null, getApplicationProgramVM(), "programComplete");
    }
	

}
