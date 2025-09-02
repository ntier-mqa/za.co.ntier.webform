package za.co.ntier.webform.form.bean.program;

import java.lang.reflect.InvocationTargetException;

import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.ProgramInput;
import za.co.ntier.webform.model.X_ZZ_Application_Form;
import za.co.ntier.webform.model.X_ZZ_FormDiscipline;

public class AetProgram implements ISaveForm {
	private ProgramInput aetLearnership;

	public AetProgram(MenuContextInfo menuContextInfo) throws NoSuchMethodException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		setAetLearnership(ProgramInput.getLearnership(X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_AETLearnership,
				menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID()));
	}

	/**
	 * @return the aetLearnership
	 */
	public ProgramInput getAetLearnership() {
		return aetLearnership;
	}

	/**
	 * @param aetLearnership the aetLearnership to set
	 */
	public void setAetLearnership(ProgramInput aetLearnership) {
		this.aetLearnership = aetLearnership;
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		// TODO Auto-generated method stub
		
	}

}
