package za.co.ntier.webform.form.bean.program;

import java.lang.reflect.InvocationTargetException;

import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.LearnerInput;

public class InternshipProgram {
	private LearnerInput trade;
	private LearnerInput disciplines;
	
	public InternshipProgram(MenuContextInfo menuContextInfo) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		this.trade = LearnerInput.getTrade(menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID(), 
				menuContextInfo.getProgramType(),
				"""
				List of disciplines supported for Artisan Internships which the number of learners applying
should be based on. Preference will be given to the following trades that are hard to fill
according MQA SPOI list, (Diesel Mechanic and Millwright).
				""");
		
		this.disciplines = LearnerInput.getDisciplines(menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID(), 
				menuContextInfo.getProgramType(),
				"""
				List of disciplines supported for Internships which the number of learners applying should
be based on.
				""");
		
	}
	/**
	 * @return the trade
	 */
	public LearnerInput getTrade() {
		return trade;
	}
	/**
	 * @param trade the trade to set
	 */
	public void setTrade(LearnerInput trade) {
		this.trade = trade;
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
