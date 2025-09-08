package za.co.ntier.webform.form.bean.program;

import java.util.List;

import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.ProjectInput;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class ArtisanRPLProgram implements ISaveForm, IProgram {
	private ProjectInput allLearners;

	public ArtisanRPLProgram() {
		allLearners = ProjectInput.getProject(List.of(ColumnInfo.getColPositiveNumber(ProjectInput.colNoEmployedLabel),
				ColumnInfo.getColPositiveNumber(ProjectInput.colNoUnEmployedLabel),
				ColumnInfo.getColPositiveNumber(ProjectInput.colTotalLearnersLabel)));
	}

	/**
	 * @return the allLearners
	 */
	public ProjectInput getAllLearners() {
		return allLearners;
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm)  {
		ProjectInput.saveProjectInput(trxName, applicationForm, allLearners);
		
	}

	/**
	 * @param allLearners the allLearners to set
	 */
	public void setAllLearners(ProjectInput allLearners) {
		this.allLearners = allLearners;
	}

}
