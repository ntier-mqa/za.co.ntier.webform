package za.co.ntier.webform.form.bean.program;

import java.util.List;

import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.ProjectInput;
import za.co.ntier.webform.model.I_ZZLearnersApplied;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class NcvGraduatesProgram implements ISaveForm, IProgram {
	private ProjectInput unemployed;

	public NcvGraduatesProgram(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm) {
		if (menuContextInfo.getIsUploadWPAForNVC()) {
			unemployed = ProjectInput.getProject(
					List.of(
							ColumnInfo.getColPositiveNumber(ColumnInfo.colNoUnEmployedLabel, I_ZZLearnersApplied.COLUMNNAME_ZZNoUnEmployedLearners),
							ColumnInfo.getColFileUpload(ColumnInfo.colWPALabel, ColumnInfo.btWPAText, I_ZZLearnersApplied.COLUMNNAME_ZZ_WPAFile)
							));
			
		}else {
			unemployed = ProjectInput.getProject(
					List.of(
							ColumnInfo.getColPositiveNumber(ColumnInfo.colNoUnEmployedLabel)
							));
		}
		
		ProjectInput.initProject(unemployed, applicationForm);
		
	}

	/**
	 * @return the unemployed
	 */
	public ProjectInput getUnemployed() {
		return unemployed;
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm)  {
		ProjectInput.saveProjectInput(trxName, applicationForm, unemployed);
		// update total
		applicationForm.saveEx(trxName);
	}

	/**
	 * @param unemployed the unemployed to set
	 */
	public void setUnemployed(ProjectInput unemployed) {
		this.unemployed = unemployed;
	}

	@Override
	public boolean isProgramValid() {
		// TODO Auto-generated method stub
		return true;
	}
}
