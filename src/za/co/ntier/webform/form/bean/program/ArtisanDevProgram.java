package za.co.ntier.webform.form.bean.program;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.ProgramInput;
import za.co.ntier.webform.form.bean.component.ProjectInput;
import za.co.ntier.webform.model.X_ZZ_Application_Form;
import za.co.ntier.webform.model.X_ZZ_FormDiscipline;

public class ArtisanDevProgram implements ISaveForm, IProgram {
	private ProjectInput totalNumApplied;

	private ProgramInput trade;

	public ArtisanDevProgram(MenuContextInfo menuContextInfo) throws NoSuchMethodException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		setTrade(ProgramInput.getTrade(menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID(), null));

		setTotalNumApplied(ProjectInput
				.getProject(List.of(ColumnInfo.getColPositiveNumber(ProjectInput.colTotalLearnersLabel))));

	}

	/**
	 * @return the totalNumApplied
	 */
	public ProjectInput getTotalNumApplied() {
		return totalNumApplied;
	}

	/**
	 * @return the trade
	 */
	public ProgramInput getTrade() {
		return trade;
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) throws IOException {
		ProjectInput.saveProjectInput(trxName, applicationForm, totalNumApplied);
		ProgramInput.saveFormDisciplines(trxName, applicationForm, trade, X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_Trade);
		
	}

	/**
	 * @param totalNumApplied the totalNumApplied to set
	 */
	public void setTotalNumApplied(ProjectInput totalNumApplied) {
		this.totalNumApplied = totalNumApplied;
	}

	/**
	 * @param trade the trade to set
	 */
	public void setTrade(ProgramInput trade) {
		this.trade = trade;
	}

}
