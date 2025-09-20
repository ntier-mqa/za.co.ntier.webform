package za.co.ntier.webform.form.bean.program;

import java.util.List;
import java.util.Map;

import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.AreaData;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.IntData;
import za.co.ntier.webform.form.bean.component.PostalData;
import za.co.ntier.webform.form.bean.component.ProjectInput;
import za.co.ntier.webform.model.I_ZZLearnersApplied;
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
		inhouse.initProject(applicationForm, 
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
		inhouse.save(trxName, applicationForm);
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
	    return hasValidRow(inhouse);
	}

	private static boolean hasValidRow(AnnexureInfo table) {
	    if (table == null || table.getRows() == null) return false;

	    // NOTE: use the same labels your columns were created with.
	    // In your class you used `colNoLearnersLable` (with an 'e' missing).
	    ColumnInfo<?> colNum    = AnnexureInfo.lookupColByTitle(ColumnInfo.colNoLearnersLable, table);
	    ColumnInfo<?> colPostal = AnnexureInfo.lookupColByTitle(ColumnInfo.colPostalCodeLabel, table);
	    ColumnInfo<?> colArea   = AnnexureInfo.lookupColByTitle(ColumnInfo.colAreaLabel, table);

	    for (Map<ColumnInfo<?>, Object> row : table.getRows()) {
	        int n = 0;
	        if (colNum != null && row.get(colNum) instanceof IntData) {
	            Integer v = ((IntData) row.get(colNum)).getValue();
	            n = (v != null) ? v : 0;
	        }

	        boolean postalOk = false;
	        if (colPostal != null && row.get(colPostal) instanceof PostalData) {
	            String p = ((PostalData) row.get(colPostal)).getPostal();
	            postalOk = p != null && !p.trim().isEmpty();
	        }

	        boolean areaOk = false;
	        if (colArea != null && row.get(colArea) instanceof AreaData) {
	            areaOk = ((AreaData) row.get(colArea)).getSelectedArea() != null;
	        }

	        if (n > 0 && postalOk && areaOk) {
	            return true; // at least one valid line
	        }
	    }
	    return false;
	}
}
