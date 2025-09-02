package za.co.ntier.webform.form.bean.program;

import java.lang.reflect.InvocationTargetException;

import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.ProgramInput;

public class InternshipProgram extends CandidacyProgram implements ISaveForm {
	private ProgramInput trade;

	public InternshipProgram(MenuContextInfo menuContextInfo) throws NoSuchMethodException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		super(menuContextInfo);
		this.trade = ProgramInput.getTrade(menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID(),

				"""
										List of disciplines supported for Artisan Internships which the number of learners applying
						should be based on. Preference will be given to the following trades that are hard to fill
						according MQA SPOI list, (Diesel Mechanic and Millwright).
										""");
	}

	/**
	 * @return the trade
	 */
	public ProgramInput getTrade() {
		return trade;
	}

	/**
	 * @param trade the trade to set
	 */
	public void setTrade(ProgramInput trade) {
		this.trade = trade;
	}
}
