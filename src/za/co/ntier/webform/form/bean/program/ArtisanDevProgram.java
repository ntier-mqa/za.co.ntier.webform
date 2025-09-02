package za.co.ntier.webform.form.bean.program;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.AnnexureInfo;
import za.co.ntier.webform.form.bean.ColumnInfo;
import za.co.ntier.webform.form.bean.ProgramInput;
import za.co.ntier.webform.form.bean.ProjectInput;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class ArtisanDevProgram implements ISaveForm, IProgram {
	private AnnexureInfo totalNumApplied;

	private ProgramInput trade;

	public ArtisanDevProgram(MenuContextInfo menuContextInfo) throws NoSuchMethodException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		setTrade(ProgramInput.getTrade(menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID(), null));

		setTotalNumApplied(ProjectInput
				.getProject(List.of(ColumnInfo.getColPositiveNumber("Total Number of Learners Applied For"))));

	}

	/**
	 * @return the totalNumApplied
	 */
	public AnnexureInfo getTotalNumApplied() {
		return totalNumApplied;
	}

	/**
	 * @return the trade
	 */
	public ProgramInput getTrade() {
		return trade;
	}

	/**
	 * @param totalNumApplied the totalNumApplied to set
	 */
	public void setTotalNumApplied(AnnexureInfo totalNumApplied) {
		this.totalNumApplied = totalNumApplied;
	}

	/**
	 * @param trade the trade to set
	 */
	public void setTrade(ProgramInput trade) {
		this.trade = trade;
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		// TODO Auto-generated method stub
		
	}

}
