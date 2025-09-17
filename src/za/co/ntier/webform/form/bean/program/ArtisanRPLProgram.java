package za.co.ntier.webform.form.bean.program;

import java.util.List;
import java.util.Map;

import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.bean.DataType;
import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.ProjectInput;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class ArtisanRPLProgram implements ISaveForm, IProgram {
	private ProjectInput allLearners;

	public ArtisanRPLProgram(X_ZZ_Application_Form applicationForm) {
		allLearners = ProjectInput.getProject(
				List.of(ColumnInfo.getColPositiveNumber(ColumnInfo.colNoEmployedLabel),
						ColumnInfo.getColPositiveNumber(ColumnInfo.colNoUnEmployedLabel),
						ColumnInfo.getColPositiveNumber(ColumnInfo.colTotalLearnersLabel)));
		ProjectInput.initProject(allLearners, applicationForm);
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

	@Override
	public boolean isProgramValid() {
		if (!(this instanceof ArtisanRPLProgram)) return false;
	    ArtisanRPLProgram p = (ArtisanRPLProgram) this;
	    AnnexureInfo allLearners = p.getAllLearners(); // or getAnnexureInfos().get(0)

	    if (allLearners == null) return true;

	    ColumnInfo<?> learnersCol = findCol(allLearners, "No. of Learners", "Learners", "No Learners");
	    ColumnInfo<?> postalCol   = findCol(allLearners, "Site Postal Code", "Postal Code");
	    ColumnInfo<?> areaCol     = findCol(allLearners, "Area");

	    if (learnersCol == null || postalCol == null || areaCol == null) return false;

	    int validRows = 0;
	    for (Map<ColumnInfo<?>, Object> row : allLearners.getRows()) {
	        Integer learners = getLearners(learnersCol.getDataType(), row.get(learnersCol));
	        String  postal   = extractPostal(row.get(postalCol));  // like in your dev solution
	        boolean areaSel  = extractAreaSelected(row.get(areaCol));

	        boolean blank = (learners == null && (postal == null || postal.isEmpty()) && !areaSel);
	        if (blank) continue;

	        if (learners == null || learners <= 0) return false;
	        if (postal == null || postal.isBlank()) return false;
	        if (!areaSel) return false;

	        validRows++;
	    }
	    return validRows >= 1;
	}
	
	private ColumnInfo<?> findCol(AnnexureInfo a, String... aliases) {
	    for (ColumnInfo<?> c : a.getColumnInfos()) {
	        String t = (c.getTitle() != null ? c.getTitle() : "").trim().toLowerCase();
	        for (String alias : aliases) {
	            if (t.equals(alias.toLowerCase())) return c;
	        }
	    }
	    return null;
	}
	
	private Integer getLearners(DataType dt, Object cell) {
	    if (cell == null) return null;
	    switch (dt) {
	        case PositiveNumber:
	            // your ZUL uses row[col].value for numbers
	            try {
	                Object v = cell.getClass().getMethod("getValue").invoke(cell);
	                if (v instanceof Number) return ((Number)v).intValue();
	            } catch (Exception ignore) {}
	            return null;
	        case Text:
	            try {
	                String s = ((String)cell).trim();
	                if (s.isEmpty()) return null;
	                return Integer.parseInt(s);
	            } catch (Exception e) { return null; }
	        default:
	            return null;
	    }
	}
	
	private String extractPostal(Object postalCell) {
	    if (postalCell == null) return null;
	    try { return (String) postalCell.getClass().getMethod("getPostal").invoke(postalCell); }
	    catch (Exception e) { return postalCell.toString().trim(); }
	}
	private boolean extractAreaSelected(Object areaCell) {
	    if (areaCell == null) return false;
	    try { return areaCell.getClass().getMethod("getSelectedArea").invoke(areaCell) != null; }
	    catch (Exception e) { return false; }
	}

}
