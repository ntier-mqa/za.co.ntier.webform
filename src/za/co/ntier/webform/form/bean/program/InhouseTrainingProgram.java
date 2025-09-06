package za.co.ntier.webform.form.bean.program;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.ProjectInput;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class InhouseTrainingProgram implements ISaveForm, IProgram{
	private ProjectInput inhouse;

	public InhouseTrainingProgram() throws NoSuchMethodException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		this.setInhouse(ProjectInput.getProject(
				List.of(ColumnInfo.getColLabel(ProjectInput.colNameProgrammeLabel),
						ColumnInfo.getColPositiveNumber(ProjectInput.colNoLearnersLable)
						),
				"Inhouse /Industry/Company based short courses"));

	}

	/**
	 * @return the inhouse
	 */
	public ProjectInput getInhouse() {
		return inhouse;
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) throws IOException {
		ProjectInput.saveProjectInput(trxName, applicationForm, inhouse);
		
	}

	/**
	 * @param inhouse the inhouse to set
	 */
	public void setInhouse(ProjectInput inhouse) {
		this.inhouse = inhouse;
	}
}
