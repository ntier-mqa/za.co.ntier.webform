package za.co.ntier.webform.form.bean.program;

import java.lang.reflect.InvocationTargetException;

import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.LearnerInput;

public class CandidacyProgram {
	private LearnerInput disciplines;
	
	public CandidacyProgram(MenuContextInfo menuContextInfo) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		this.setDisciplines(LearnerInput.getDisciplines(menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID(), 
				
				"""
				List of disciplines supported for Internships which the number of learners applying should
be based on.
				"""));
		
	}

	/**
	 * @return the disciplines
	 */
	public LearnerInput getDisciplines() {
		return disciplines;
	}

	/**
	 * @param disciplines the disciplines to set
	 */
	public void setDisciplines(LearnerInput disciplines) {
		this.disciplines = disciplines;
	}
}
