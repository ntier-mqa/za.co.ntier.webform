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

public class ArtisanRPLProgram implements ISaveForm, IProgram {
	private ProjectInput allLearners;

	public ArtisanRPLProgram(X_ZZ_Application_Form applicationForm) {
		allLearners = ProjectInput.getProject(
				List.of(ColumnInfo.getColPositiveNumber(ColumnInfo.colNoEmployedLabel, I_ZZLearnersApplied.COLUMNNAME_ZZNoEmployedLearners),
						ColumnInfo.getColPositiveNumber(ColumnInfo.colNoUnEmployedLabel, I_ZZLearnersApplied.COLUMNNAME_ZZNoUnEmployedLearners),
						ColumnInfo.getColPositiveNumber(ColumnInfo.colTotalLearnersLabel, I_ZZLearnersApplied.COLUMNNAME_ZZNoTotalLearners)));
		allLearners.initProject(applicationForm);
	}

	/**
	 * @return the allLearners
	 */
	public ProjectInput getAllLearners() {
		return allLearners;
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm)  {
		allLearners.save(trxName, applicationForm);
		// update total
		applicationForm.saveEx(trxName);
		
	}

	/**
	 * @param allLearners the allLearners to set
	 */
	public void setAllLearners(ProjectInput allLearners) {
		this.allLearners = allLearners;
	}

	@Override
	public boolean isProgramValid() {
	    // `allLearners` is your ProjectInput/AnnexureInfo shown in the ZUL
	    return hasAtLeastOneValidRplRow(allLearners);
	}

	private static boolean hasAtLeastOneValidRplRow(AnnexureInfo table) {
	    if (table == null || table.getRows() == null) return false;

	    ColumnInfo<?> colEmp    = AnnexureInfo.lookupColByTitle(ColumnInfo.colNoEmployedLabel,     table);
	    ColumnInfo<?> colUnemp  = AnnexureInfo.lookupColByTitle(ColumnInfo.colNoUnEmployedLabel,  table);
	    ColumnInfo<?> colTotal  = AnnexureInfo.lookupColByTitle(ColumnInfo.colTotalLearnersLabel,  table);
	    ColumnInfo<?> colPostal = AnnexureInfo.lookupColByTitle(ColumnInfo.colPostalCodeLabel,     table);
	    ColumnInfo<?> colArea   = AnnexureInfo.lookupColByTitle(ColumnInfo.colAreaLabel,           table);

	    for (Map<ColumnInfo<?>, Object> row : table.getRows()) {
	        int employed   = (colEmp   != null && row.get(colEmp)   instanceof IntData)
	                ? ((IntData) row.get(colEmp)).getValue() == null ? 0 : ((IntData) row.get(colEmp)).getValue()
	                : 0;
	        int unemployed = (colUnemp != null && row.get(colUnemp) instanceof IntData)
	                ? ((IntData) row.get(colUnemp)).getValue() == null ? 0 : ((IntData) row.get(colUnemp)).getValue()
	                : 0;
	        int total      = (colTotal != null && row.get(colTotal) instanceof IntData)
	                ? ((IntData) row.get(colTotal)).getValue() == null ? 0 : ((IntData) row.get(colTotal)).getValue()
	                : 0;

	        boolean postalOk = (colPostal != null && row.get(colPostal) instanceof PostalData)
	                && notEmpty(((PostalData) row.get(colPostal)).getPostal());

	        boolean areaOk   = (colArea != null && row.get(colArea) instanceof AreaData)
	                && ((AreaData) row.get(colArea)).getSelectedArea() != null;

	        boolean anyLearners = (employed > 0) || (unemployed > 0) || (total > 0);

	        if (anyLearners && postalOk && areaOk) {
	            return true; // one valid line is enough
	        }
	    }
	    return false;
	}

	private static boolean notEmpty(String s) {
	    return s != null && !s.trim().isEmpty();
	}

}
