package za.co.ntier.webform.form.bean.program;

import java.util.List;

import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.DataType;
import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.ProgramInput;
import za.co.ntier.webform.form.bean.component.ProjectInput;
import za.co.ntier.webform.model.I_ZZLearnersApplied;
import za.co.ntier.webform.model.X_ZZ_Application_Form;
import za.co.ntier.webform.model.X_ZZ_FormDiscipline;

public class ArtisanDevProgram implements ISaveForm, IProgram {
	private ProjectInput totalNumApplied;

	private ProgramInput trade;

	public ArtisanDevProgram(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm){
		setTrade(ProgramInput.getTrade(menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID(), applicationForm, null));

		totalNumApplied = ProjectInput.getProject(
				List.of(ColumnInfo.getColPositiveNumber(ColumnInfo.colTotalLearnersLabel, I_ZZLearnersApplied.COLUMNNAME_ZZNoTotalLearners)));
		ProjectInput.initProject(totalNumApplied, applicationForm);

	}

	/**
	 * @return the totalNumApplied
	 */
	public ProjectInput getTotalNumApplied() {
		return totalNumApplied;
	}

	/**
	 * @return the trade
	 */
	public ProgramInput getTrade() {
		return trade;
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm)  {
		ProjectInput.saveProjectInput(trxName, applicationForm, totalNumApplied);
		ProgramInput.saveFormDisciplines(trxName, applicationForm, trade, X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_Trade);
		// update total
		applicationForm.saveEx(trxName);
	}

	/**
	 * @param totalNumApplied the totalNumApplied to set
	 */
	public void setTotalNumApplied(ProjectInput totalNumApplied) {
		this.totalNumApplied = totalNumApplied;
	}

	/**
	 * @param trade the trade to set
	 */
	public void setTrade(ProgramInput trade) {
		this.trade = trade;
	}

	@Override
	public boolean isProgramValid() {
	    if (!(this instanceof ArtisanDevProgram)) return false;
	    ArtisanDevProgram p = (ArtisanDevProgram) this;

	    // main “trade” grid
	    AnnexureInfo trade = p.getTrade();
	    // the “Total No. of Learners Applied For” one-liner at the bottom
	    AnnexureInfo total = p.getTotalNumApplied();

	    boolean tradeOk = trade == null ? true : validateArtisanDevTrade(trade);
	    boolean totalOk = total == null ? true : validateArtisanDevTotal(total, trade);

	    // require at least one valid trade row and the total row coherent (if present)
	    return tradeOk && totalOk && hasValidArtisanDevRow(trade);
	}
	
	
	private boolean validateArtisanDevTrade(AnnexureInfo a) {
	    if (a == null) return true;

	    ColumnInfo<?> tradeCol   = findCol(a, "Trade"); // label column (not mandatory itself)
	    ColumnInfo<?> learnersCol= findCol(a, "No. of Learners", "No of Learners", "Learners");
	    ColumnInfo<?> postalCol  = findCol(a, "Site Postal Code", "Postal Code");
	    ColumnInfo<?> areaCol    = findCol(a, "Area");

	    // these three are required for a filled row
	    if (learnersCol == null || postalCol == null || areaCol == null) return false;

	    for (var row : a.getRows()) {
	        Integer learners = getLearners(learnersCol.getDataType(), row.get(learnersCol));

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

	        boolean emptyLine = (learners == null && (postal == null || postal.isBlank()) && areaSelected == null);
	        if (emptyLine) continue; // ignore blanks

	        // row is considered filled → must be valid
	        if (learners == null || learners <= 0) return false;
	        if (postal == null || postal.isBlank()) return false;
	        if (areaSelected == null) return false;
	    }
	    return true;
	}
	
	
	private boolean validateArtisanDevTotal(AnnexureInfo total, AnnexureInfo trade) {
	    if (total == null) return true;

	    // total grid looks like: [Total No. of Learners Applied For | Site Postal Code | Area]
	    ColumnInfo<?> totalCol  = findCol(total, "Total No. of Learners Applied For", "Total No. of Learners", "Total Learners");
	    ColumnInfo<?> postalCol = findCol(total, "Site Postal Code", "Postal Code");
	    ColumnInfo<?> areaCol   = findCol(total, "Area");
	    if (totalCol == null || postalCol == null || areaCol == null) return false;

	    if (total.getRows().isEmpty()) return false;
	    var row = total.getRows().get(0);

	    Integer totalLearners = getLearners(totalCol.getDataType(), row.get(totalCol));

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

	    // if the line is entirely blank, allow it (some programs don’t require the bottom summary)
	    boolean blank = (totalLearners == null && (postal == null || postal.isBlank()) && areaSelected == null);
	    if (blank) return true;

	    // otherwise, it must be valid and (optionally) coherent with the sum of trade rows
	    if (totalLearners == null || totalLearners <= 0) return false;
	    if (postal == null || postal.isBlank()) return false;
	    if (areaSelected == null) return false;

	    // Optional coherence check: sum of trade learners equals total
	    if (trade != null) {
	        ColumnInfo<?> learnersCol = findCol(trade, "No. of Learners", "No of Learners", "Learners");
	        if (learnersCol != null) {
	            int sum = 0;
	            for (var r : trade.getRows()) {
	                Integer n = getLearners(learnersCol.getDataType(), r.get(learnersCol));
	                if (n != null) sum += n;
	            }
	            if (sum > 0 && totalLearners != sum) {
	                return false; // enforce consistency; drop this if not required
	            }
	        }
	    }
	    return true;
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
	
	private boolean hasValidArtisanDevRow(AnnexureInfo a) {
	    if (a == null) return false;
	    ColumnInfo<?> learnersCol= findCol(a, "No. of Learners", "No of Learners", "Learners");
	    ColumnInfo<?> postalCol  = findCol(a, "Site Postal Code", "Postal Code");
	    ColumnInfo<?> areaCol    = findCol(a, "Area");
	    if (learnersCol == null || postalCol == null || areaCol == null) return false;

	    for (var row : a.getRows()) {
	        Integer learners = getLearners(learnersCol.getDataType(), row.get(learnersCol));
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

	        if (learners != null && learners > 0
	                && postal != null && !postal.isBlank()
	                && areaSelected != null) {
	            return true;
	        }
	    }
	    return false;
	}



}
