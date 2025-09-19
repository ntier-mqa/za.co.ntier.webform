package za.co.ntier.webform.form.bean.program;

import java.util.List;
import java.util.Map;

import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.ProjectInput;
import za.co.ntier.webform.model.I_ZZLearnersApplied;
import za.co.ntier.webform.model.X_ZZLearnersApplied;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class InhouseTrainingProgram implements ISaveForm, IProgram{
	private ProjectInput inhouse;

	public InhouseTrainingProgram(X_ZZ_Application_Form applicationForm) {
		
		ColumnInfo<?> colNameProgram = ColumnInfo.getColLabel(ColumnInfo.colNameProgrammeLabel, I_ZZLearnersApplied.COLUMNNAME_Name);
		this.inhouse = ProjectInput.getProject(
				List.of(colNameProgram,
						ColumnInfo.getColPositiveNumber(ColumnInfo.colNoLearnersLable, I_ZZLearnersApplied.COLUMNNAME_ZZNoLearners)
						)
				);
		ProjectInput.initProject(inhouse, applicationForm, 
				List.of(Map.of(colNameProgram, "Inhouse /Industry/Company based short courses"))
				);
		
	}

	/**
	 * @return the inhouse
	 */
	public ProjectInput getInhouse() {
		return inhouse;
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm)  {
		ProjectInput.saveProjectInput(trxName, applicationForm, inhouse);
		// update total
		applicationForm.saveEx(trxName);
	}

	/**
	 * @param inhouse the inhouse to set
	 */
	public void setInhouse(ProjectInput inhouse) {
		this.inhouse = inhouse;
	}

	@Override
	public boolean isProgramValid() {
		// TODO Auto-generated method stub
		return true;
	}
}
