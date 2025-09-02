package za.co.ntier.webform.form.bean.program;

import java.lang.reflect.InvocationTargetException;
import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.ProgramInput;
import za.co.ntier.webform.model.X_ZZ_Application_Form;
import za.co.ntier.webform.model.X_ZZ_FormDiscipline;

public class NonArtisanDevProgram implements ISaveForm, IProgram {
	private ProgramInput generalLearnership;
	private ProgramInput firLearnership;

	public NonArtisanDevProgram(MenuContextInfo menuContextInfo) throws NoSuchMethodException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		setGeneralLearnership(ProgramInput.getLearnership(X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_GeneralLearnership,
				menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID()));

		setFirLearnership(ProgramInput.getLearnership(X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_4IRLearnership,
				menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID()));

	}

	/**
	 * @return the firLearnership
	 */
	public ProgramInput getFirLearnership() {
		return firLearnership;
	}

	/**
	 * @return the generalLearnership
	 */
	public ProgramInput getGeneralLearnership() {
		return generalLearnership;
	}

	/**
	 * @param firLearnership the firLearnership to set
	 */
	public void setFirLearnership(ProgramInput firLearnership) {
		this.firLearnership = firLearnership;
	}

	/**
	 * @param generalLearnership the generalLearnership to set
	 */
	public void setGeneralLearnership(ProgramInput generalLearnership) {
		this.generalLearnership = generalLearnership;
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		// TODO Auto-generated method stub
		
	}

}
