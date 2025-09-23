package za.co.ntier.webform.form.bean.program;

import java.util.List;
import java.util.Map;

import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.ProjectInput;
import za.co.ntier.webform.model.I_ZZLearnersApplied;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class MultiyearPartnershipInternship implements ISaveForm, IProgram{
	private ProjectInput learnersApplied;
	
	public ProjectInput getLearnersApplied() {
		return learnersApplied;
	}

	public void setLearnersApplied(ProjectInput learnersApplied) {
		this.learnersApplied = learnersApplied;
	}

	public MultiyearPartnershipInternship(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm) {
		ColumnInfo<?> colLearnersAppliedTitle = ColumnInfo.getColLabel("PROGRAMME"
				, I_ZZLearnersApplied.COLUMNNAME_Name);
		ColumnInfo<?> colLearnersApplied = ColumnInfo.getColPositiveNumber("NUMBER OF LEARNERS APPLYING FOR*"
						, I_ZZLearnersApplied.COLUMNNAME_ZZNoLearners);
		colLearnersApplied.setCalTotal(true);
		List<ColumnInfo<?>> colLearnersAppliedCols = List.of(colLearnersAppliedTitle, colLearnersApplied);
		
		learnersApplied = ProjectInput.getProject(colLearnersAppliedCols, null, false);
		learnersApplied.setTableTitle("Please indicate below the number of learners applying for");	
		List<Map<ColumnInfo<?>, Object>> titleRows = List.of(Map.of(colLearnersAppliedTitle, "INTERNSHIP PROGRAMME"));
		learnersApplied.initProject(applicationForm, titleRows);	

	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		learnersApplied.save(trxName, applicationForm);
		
	}
	
	
}
