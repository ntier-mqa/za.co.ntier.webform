package za.co.ntier.webform.form.bean.program;

import java.util.List;
import java.util.Map;

import za.co.ntier.webform.form.AbstractProgram;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.Util;
import za.co.ntier.webform.form.bean.DataType;
import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.AreaData;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.PostalData;
import za.co.ntier.webform.form.bean.component.ProjectInput;
import za.co.ntier.api.model.I_ZZLearnersApplied;
import za.co.ntier.api.model.X_ZZ_Application_Form;

public class ArtisanRPLProgram extends AbstractProgram {
	private ProjectInput allLearners;

	public ArtisanRPLProgram(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm) {
		super(menuContextInfo, applicationForm);
		
		ColumnInfo<?> colNoEmployed = ColumnInfo.getColPositiveNumber(ColumnInfo.colNoEmployedLabel, I_ZZLearnersApplied.COLUMNNAME_ZZNoEmployedLearners);
		colNoEmployed.setCalTotal(true);
		ColumnInfo<?> colNoUnEmployed = ColumnInfo.getColPositiveNumber(ColumnInfo.colNoUnEmployedLabel, I_ZZLearnersApplied.COLUMNNAME_ZZNoUnEmployedLearners);
		colNoUnEmployed.setCalTotal(true);
		ColumnInfo<?> colTotal = ColumnInfo.getColExpression(ColumnInfo.colTotalLearnersLabel, row -> {
			return Util.sumCol(row, List.of(colNoEmployed, colNoUnEmployed));
		});

		allLearners = ProjectInput.getProject(
				List.of(colNoEmployed, colNoUnEmployed, colTotal));

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
		super.saveForm(applicationForm);
		allLearners.save(trxName, applicationForm);

	}

	/**
	 * @param allLearners the allLearners to set
	 */
	public void setAllLearners(ProjectInput allLearners) {
		this.allLearners = allLearners;
	}

	@Override
	public boolean isProgramValid() {
		if (allLearners == null || allLearners.getRows() == null) return false;

		// columns we need
		ColumnInfo<?> empCol    = AnnexureInfo.lookupColByTitle(ColumnInfo.colNoEmployedLabel,    allLearners);
		ColumnInfo<?> unempCol  = AnnexureInfo.lookupColByTitle(ColumnInfo.colNoUnEmployedLabel, allLearners);
		ColumnInfo<?> postalCol = AnnexureInfo.lookupColByDataType(DataType.Postal, allLearners);
		ColumnInfo<?> areaCol   = AnnexureInfo.lookupColByDataType(DataType.Area,   allLearners);

		boolean hasAtLeastOneRow = false;

		for (Map<ColumnInfo<?>, Object> row : allLearners.getRows()) {
			Integer emp   = AnnexureInfo.getIntegerValue(row, empCol);
			Integer unemp = AnnexureInfo.getIntegerValue(row, unempCol);

			// A row is "filled" if the user entered any positive learner count
			boolean started = (emp != null && emp > 0) || (unemp != null && unemp > 0);
			if (!started) continue;

			hasAtLeastOneRow = true;

			// Require BOTH postal and area for any started row
			String postal = null;
			Object pObj = row.get(postalCol);
			if (pObj instanceof PostalData) {
				postal = ((PostalData) pObj).getPostal();
			}
			Object aObj = row.get(areaCol);
			boolean areaOk = (aObj instanceof AreaData) && ((AreaData) aObj).getSelectedArea() != null;
			boolean postalOk = postal != null && !postal.trim().isEmpty();

			if (!postalOk || !areaOk) {
				return false; // missing postal or area ⇒ invalid
			}
		}

		// At least one valid row must exist
		return hasAtLeastOneRow;
	}

}
