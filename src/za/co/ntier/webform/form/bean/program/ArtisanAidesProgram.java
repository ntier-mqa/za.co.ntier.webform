package za.co.ntier.webform.form.bean.program;

import java.util.List;

import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.bean.DataType;
import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.ProjectInput;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class ArtisanAidesProgram implements ISaveForm, IProgram {
	private ProjectInput qualification;
	private ProjectInput skill;

	public ArtisanAidesProgram(X_ZZ_Application_Form applicationForm) {
		ColumnInfo<?> colNoEmployed = ColumnInfo.getColPositiveNumber(ProjectInput.colNoEmployedLabel);
		ColumnInfo<?> colNoUnEmployed = ColumnInfo.getColPositiveNumber(ProjectInput.colNoUnEmployedLabel);
		ColumnInfo<?> colTotal = ColumnInfo.getColTotal(ProjectInput.colTotalLearnersLabel, List.of(colNoEmployed, colNoUnEmployed));
		
		qualification = ProjectInput.getProject(null,
				
				List.of(colNoEmployed,
						colNoUnEmployed,
						colTotal), applicationForm);
		qualification.setSubSectionHeader("QUALIFICATION");
		
		skill = ProjectInput.getProject(null,
				List.of(colNoEmployed,
						colNoUnEmployed,
						colTotal), applicationForm);
		skill.setSubSectionHeader("SKILLS PROGRAMME");
	}

	/**
	 * @return the qualification
	 */
	public ProjectInput getQualification() {
		return qualification;
	}

	/**
	 * @return the skill
	 */
	public ProjectInput getSkill() {
		return skill;
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm)  {
		ProjectInput.saveProjectInput(trxName, applicationForm, qualification);
		ProjectInput.saveProjectInput(trxName, applicationForm, skill);
		
	}

	/**
	 * @param qualification the qualification to set
	 */
	public void setQualification(ProjectInput qualification) {
		this.qualification = qualification;
	}

	/**
	 * @param skill the skill to set
	 */
	public void setSkill(ProjectInput skill) {
		this.skill = skill;
	}

	@Override
	public boolean isProgramValid() {
		if (!(this instanceof ArtisanAidesProgram)) return false;
	    ArtisanAidesProgram p = (ArtisanAidesProgram) this;

	    AnnexureInfo qual  = p.getQualification();
	    AnnexureInfo skill = p.getSkill();

	    boolean qualOk  = qual  == null ? true : validateArtisanAnnexure_NoProgramme(qual);
	    boolean skillOk = skill == null ? true : validateArtisanAnnexure_NoProgramme(skill);

	    // At least one annexure must have a valid row
	    return (qualOk && skillOk) && (hasValidRow_NoProgramme(qual) || hasValidRow_NoProgramme(skill));
	}
	
	private boolean validateArtisanAnnexure_NoProgramme(AnnexureInfo a) {
	    if (a == null) return true;

	    ColumnInfo<?> employedCol   = findCol(a, "No. of Employed Learners", "Employed", "No Employed", "Employed Learners");
	    ColumnInfo<?> unemployedCol = findCol(a, "No. of Unemployed Learners", "Unemployed", "No Unemployed", "Unemployed Learners");
	    ColumnInfo<?> totalCol      = findCol(a, "Total No. of Learners Applied For", "Total No. of Learners", "Total Learners");
	    ColumnInfo<?> postalCol     = findCol(a, "Site Postal Code", "Postal Code", "Site Postal");
	    ColumnInfo<?> areaCol       = findCol(a, "Area");

	    // Must at least have the three counts; location is also required by your UI
	    if (employedCol == null || unemployedCol == null || totalCol == null || postalCol == null || areaCol == null) {
	        return false;
	    }

	    int validRows = 0;
	    for (var row : a.getRows()) {
	        Integer employed   = getLearners(employedCol.getDataType(),   row.get(employedCol));
	        Integer unemployed = getLearners(unemployedCol.getDataType(), row.get(unemployedCol));
	        Integer total      = getLearners(totalCol.getDataType(),      row.get(totalCol));

	        // postal stored as PostalData; area stored as AreaData (from your AnnexureInfo)
	        String postal = null;
	        Object postalCell = row.get(postalCol);
	        if (postalCell != null) {
	            try { postal = (String) postalCell.getClass().getMethod("getPostal").invoke(postalCell); } catch (Exception ignore) {}
	        }

	        Object areaCell = row.get(areaCol);
	        Object areaSelected = null;
	        if (areaCell != null) {
	            try { areaSelected = areaCell.getClass().getMethod("getSelectedArea").invoke(areaCell); } catch (Exception ignore) {}
	        }

	        boolean allBlank = (employed == null && unemployed == null && total == null && postal == null && areaSelected == null);
	        if (allBlank) {
	            // Ignore empty line
	            continue;
	        }

	        // Mandatory rules:
	        //  - counts present and consistent
	        //  - at least one count > 0
	        //  - location filled (postal + area)
	        boolean countsPresent = employed != null && unemployed != null && total != null;
	        boolean countsPositive = (employed != null && unemployed != null && total != null) &&
	                                 (employed > 0 || unemployed > 0); // (or require both > 0 if needed)
	        boolean countsConsistent = (employed != null && unemployed != null && total != null) &&
	                                   (employed + unemployed == total);

	        boolean locationOk = (postal != null && !postal.trim().isEmpty() && areaSelected != null);

	        boolean rowValid = countsPresent && countsPositive && countsConsistent && locationOk;
	        if (!rowValid) return false;  // any partially filled/invalid row fails
	        validRows++;
	    }

	    // Need at least one valid row in this annexure (qualification or skills)
	    return validRows >= 1;
	}
	
	
	private boolean hasValidRow_NoProgramme(AnnexureInfo a) {
	    if (a == null) return false;

	    ColumnInfo<?> employedCol   = findCol(a, "No. of Employed Learners", "Employed", "No Employed", "Employed Learners");
	    ColumnInfo<?> unemployedCol = findCol(a, "No. of Unemployed Learners", "Unemployed", "No Unemployed", "Unemployed Learners");
	    ColumnInfo<?> totalCol      = findCol(a, "Total No. of Learners Applied For", "Total No. of Learners", "Total Learners");
	    ColumnInfo<?> postalCol     = findCol(a, "Site Postal Code", "Postal Code", "Site Postal");
	    ColumnInfo<?> areaCol       = findCol(a, "Area");
	    if (employedCol == null || unemployedCol == null || totalCol == null || postalCol == null || areaCol == null) return false;

	    for (var row : a.getRows()) {
	        Integer employed   = getLearners(employedCol.getDataType(),   row.get(employedCol));
	        Integer unemployed = getLearners(unemployedCol.getDataType(), row.get(unemployedCol));
	        Integer total      = getLearners(totalCol.getDataType(),      row.get(totalCol));

	        String postal = null;
	        Object postalCell = row.get(postalCol);
	        if (postalCell != null) {
	            try { postal = (String) postalCell.getClass().getMethod("getPostal").invoke(postalCell); } catch (Exception ignore) {}
	        }

	        Object areaCell = row.get(areaCol);
	        Object areaSelected = null;
	        if (areaCell != null) {
	            try { areaSelected = areaCell.getClass().getMethod("getSelectedArea").invoke(areaCell); } catch (Exception ignore) {}
	        }

	        if (employed != null && unemployed != null && total != null &&
	            (employed > 0 || unemployed > 0) &&
	            employed + unemployed == total &&
	            postal != null && !postal.trim().isEmpty() &&
	            areaSelected != null) {
	            return true;
	        }
	    }
	    return false;
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


}
