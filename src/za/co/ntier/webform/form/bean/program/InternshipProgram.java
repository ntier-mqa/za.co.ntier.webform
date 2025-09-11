package za.co.ntier.webform.form.bean.program;

import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.component.ProgramInput;
import za.co.ntier.webform.model.X_ZZ_Application_Form;
import za.co.ntier.webform.model.X_ZZ_FormDiscipline;

public class InternshipProgram extends CandidacyProgram implements ISaveForm, IProgram {
	private ProgramInput trade;

	public InternshipProgram(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm) {
		super(menuContextInfo, applicationForm);
		this.trade = ProgramInput.getTrade(menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID(),
				applicationForm,

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

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm)  {
		super.saveForm(trxName, applicationForm);
		ProgramInput.saveFormDisciplines(trxName, applicationForm, trade, X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_Trade);
	}
	
	/**
	 * @param trade the trade to set
	 */
	public void setTrade(ProgramInput trade) {
		this.trade = trade;
	}
}
