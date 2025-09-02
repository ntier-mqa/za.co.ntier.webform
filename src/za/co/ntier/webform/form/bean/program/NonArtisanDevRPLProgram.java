package za.co.ntier.webform.form.bean.program;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.ProjectInput;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class NonArtisanDevRPLProgram implements ISaveForm, IProgram {
	private AnnexureInfo totalNumApplied;

	public NonArtisanDevRPLProgram(MenuContextInfo menuContextInfo) throws NoSuchMethodException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

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
	 * @param totalNumApplied the totalNumApplied to set
	 */
	public void setTotalNumApplied(AnnexureInfo totalNumApplied) {
		this.totalNumApplied = totalNumApplied;
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		// TODO Auto-generated method stub
		
	}

}
