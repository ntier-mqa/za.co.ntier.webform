package za.co.ntier.webform.form.bean.program;

import java.lang.reflect.InvocationTargetException;

import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.ProgramInput;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class CandidacyProgram implements ISaveForm{
	private ProgramInput disciplines;

	public CandidacyProgram(MenuContextInfo menuContextInfo) throws NoSuchMethodException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		this.setDisciplines(ProgramInput.getDisciplines(
				menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID(),

				"""
										List of disciplines supported for Internships which the number of learners applying should
						be based on.
										"""));

	}

	/**
	 * @return the disciplines
	 */
	public ProgramInput getDisciplines() {
		return disciplines;
	}

	/**
	 * @param disciplines the disciplines to set
	 */
	public void setDisciplines(ProgramInput disciplines) {
		this.disciplines = disciplines;
	}

	@Override
	public void saveForm(X_ZZ_Application_Form applicationForm) {
		// TODO Auto-generated method stub
		
	}
}
