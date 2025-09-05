package za.co.ntier.webform.form.bean.program;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.component.ProgramInput;
import za.co.ntier.webform.model.X_ZZ_Application_Form;
import za.co.ntier.webform.model.X_ZZ_FormDiscipline;

public class AetProgram implements ISaveForm, IProgram {
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

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) throws IOException {
		ProgramInput.saveFormLearnership(trxName, applicationForm, aetLearnership, X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_AETLearnership);
		
	}

	/**
	 * @param aetLearnership the aetLearnership to set
	 */
	public void setAetLearnership(ProgramInput aetLearnership) {
		this.aetLearnership = aetLearnership;
	}

}
