package za.co.ntier.webform.form.bean.program;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.ProjectInput;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class ArtisanRPLProgram implements ISaveForm, IProgram {
	private AnnexureInfo allLearners;

	public ArtisanRPLProgram() throws NoSuchMethodException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		allLearners = ProjectInput.getProject(List.of(ColumnInfo.getColPositiveNumber("No. of Employed Learners"),
				ColumnInfo.getColPositiveNumber("No. of Unemployed Learners"),
				ColumnInfo.getColPositiveNumber("Total No. of Learners Applied For")));
	}

	/**
	 * @return the allLearners
	 */
	public AnnexureInfo getAllLearners() {
		return allLearners;
	}

	/**
	 * @param allLearners the allLearners to set
	 */
	public void setAllLearners(AnnexureInfo allLearners) {
		this.allLearners = allLearners;
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		// TODO Auto-generated method stub
		
	}

}
