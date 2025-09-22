package za.co.ntier.webform.form.bean.program;

import java.util.List;
import java.util.Map;

import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.AreaData;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.IntData;
import za.co.ntier.webform.form.bean.component.PostalData;
import za.co.ntier.webform.form.bean.component.ProjectInput;
import za.co.ntier.webform.model.I_ZZLearnersApplied;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class NonArtisanDevRPLProgram implements ISaveForm, IProgram {
	private ProjectInput totalNumApplied;

	public NonArtisanDevRPLProgram(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm)  {
		ColumnInfo<?> colTotalLearners = ColumnInfo.getColPositiveNumber(ColumnInfo.colTotalLearnersLabel, I_ZZLearnersApplied.COLUMNNAME_ZZNoTotalLearners);
		colTotalLearners.setCalTotal(true);
		totalNumApplied = ProjectInput.getProject(List.of(colTotalLearners));
		totalNumApplied.initProject(applicationForm);

	}

	/**
	 * @return the totalNumApplied
	 */
	public ProjectInput getTotalNumApplied() {
		return totalNumApplied;
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm)  {
		totalNumApplied.save(trxName, applicationForm);
	}

	/**
	 * @param totalNumApplied the totalNumApplied to set
	 */
	public void setTotalNumApplied(ProjectInput totalNumApplied) {
		this.totalNumApplied = totalNumApplied;
	}

	@Override
	public boolean isProgramValid() {
	    return hasValidRow(totalNumApplied);
	}

	private boolean hasValidRow(ProjectInput tbl) {
	    if (tbl == null || tbl.getRows() == null) return false;

	    ColumnInfo<?> colTotal  = AnnexureInfo.lookupColByTitle(ColumnInfo.colTotalLearnersLabel, tbl);
	    ColumnInfo<?> colPostal = AnnexureInfo.lookupColByTitle(ColumnInfo.colPostalCodeLabel, tbl);
	    ColumnInfo<?> colArea   = AnnexureInfo.lookupColByTitle(ColumnInfo.colAreaLabel, tbl);

	    for (Map<ColumnInfo<?>, Object> row : tbl.getRows()) {
	        IntData total = (colTotal != null) ? (IntData) row.get(colTotal) : null;
	        int totalVal  = (total != null && total.getValue() != null) ? total.getValue() : 0;

	        PostalData postal = (colPostal != null) ? (PostalData) row.get(colPostal) : null;
	        boolean postalOk  = postal != null && postal.getPostal() != null && !postal.getPostal().trim().isEmpty();

	        AreaData area   = (colArea != null) ? (AreaData) row.get(colArea) : null;
	        boolean areaOk  = area != null && area.getSelectedArea() != null;

	        if (totalVal > 0 && postalOk && areaOk) {
	            return true; // valid
	        }
	    }
	    return false; // nothing valid
	}

}
