package za.co.ntier.webform.form.bean.program;

import java.util.List;
import java.util.Map;

import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.Util;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.ProjectInput;
import za.co.ntier.webform.model.I_ZZLearnersApplied;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class MultiyearPartnershipWorkExperience implements ISaveForm, IProgram{
	
	private ProjectInput learnersApplied;
	  // keep a field reference to the numeric column so we can read values later
    private ColumnInfo<?> colLearnersApplied;
	
	public MultiyearPartnershipWorkExperience(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm) {
		ColumnInfo<?> colLearnersAppliedTitle = ColumnInfo.getColLabel("PROGRAMME"
				, I_ZZLearnersApplied.COLUMNNAME_Name);
		colLearnersApplied = ColumnInfo.getColPositiveNumber("NUMBER OF LEARNERS APPLYING FOR*"
						, I_ZZLearnersApplied.COLUMNNAME_ZZNoLearners);
		colLearnersApplied.setCalTotal(true);
		List<ColumnInfo<?>> colLearnersAppliedCols = List.of(colLearnersAppliedTitle, colLearnersApplied);
		
		learnersApplied = ProjectInput.getProject(colLearnersAppliedCols, null, false);
		
		List<Map<ColumnInfo<?>, Object>> titleRows = List.of(Map.of(colLearnersAppliedTitle, "WORK EXPERIENCE (P1/P2)")
				, Map.of(colLearnersAppliedTitle, "WORK EXPERIENCE (VACATION WORK)"));
		learnersApplied.initProject(applicationForm, titleRows);	
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		learnersApplied.save(trxName, applicationForm);
		
	}

	/**
	 * @return the learnersApplied
	 */
	public ProjectInput getLearnersApplied() {
		return learnersApplied;
	}

	/**
	 * @param learnersApplied the learnersApplied to set
	 */
	public void setLearnersApplied(ProjectInput learnersApplied) {
		this.learnersApplied = learnersApplied;
	}
	
	
    @Override
    public boolean isProgramValid() {
        if (learnersApplied == null || learnersApplied.getRows() == null || colLearnersApplied == null) return false;

        // return true if any row has a number > 0
        for (Map<ColumnInfo<?>, Object> row : learnersApplied.getRows()) {
            Integer v = Util.getInt(row.get(colLearnersApplied));
            if (v != null && v > 0) return true;
        }
        return false;
    }

   

}
