package za.co.ntier.webform.form.viewmodel.program;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;

public class ProgramVMWrapper<T> {
	public static final String ProgramKey = "program";
	private T program;

	/**
	 * @return the program
	 */
	public T getProgram() {
		return program;
	}

	@Init
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo,
			@ExecutionArgParam(ProgramKey) T program) throws Exception {
		this.setProgram(program);
	}

	/**
	 * @param program the program to set
	 */
	public void setProgram(T program) {
		this.program = program;
	}

}
