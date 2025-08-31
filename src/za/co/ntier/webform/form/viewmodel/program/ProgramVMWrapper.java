package za.co.ntier.webform.form.viewmodel.program;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;

public class ProgramVMWrapper<T> {
	private T program;
	public static final String ProgramKey = "program";
	

	@Init
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo,
			@ExecutionArgParam(ProgramKey) T program) throws Exception {
		this.setProgram(program);
	}


	/**
	 * @return the program
	 */
	public T getProgram() {
		return program;
	}


	/**
	 * @param program the program to set
	 */
	public void setProgram(T program) {
		this.program = program;
	}




}
