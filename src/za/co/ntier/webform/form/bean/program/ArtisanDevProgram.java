package za.co.ntier.webform.form.bean.program;

import java.util.Map;

import za.co.ntier.api.model.X_ZZ_Application_Form;
import za.co.ntier.webform.form.AbstractProgram;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.AreaData;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.IntData;
import za.co.ntier.webform.form.bean.component.PostalData;
import za.co.ntier.webform.form.bean.component.ProgramInput;

public class ArtisanDevProgram extends AbstractProgram {
	private ProgramInput trade;

	public ArtisanDevProgram(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm){
		super(menuContextInfo, applicationForm);
		setTrade(ProgramInput.getTrade(menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID(), applicationForm, null));
	}

	
	/**
	 * @return the trade
	 */
	public ProgramInput getTrade() {
		return trade;
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm)  {
		super.saveForm(applicationForm);
		trade.save(trxName, applicationForm);
	}

	/**
	 * @param trade the trade to set
	 */
	public void setTrade(ProgramInput trade) {
		this.trade = trade;
	}

	@Override
	public boolean isProgramValid() {
	    // 1) No incomplete lines in trade
	    if (!tableHasNoPartialRows(trade)) {
	        return false;
	    }

	    // 2) At least one complete line in trade
	    return hasCompleteRow(trade);
	}

	private static boolean tableHasNoPartialRows(AnnexureInfo table) {
	    if (table == null || table.getRows() == null) {
	        return true; // treat as empty; overall validity checked via hasCompleteRow
	    }

	    ColumnInfo<?> cNoLearners   = AnnexureInfo.lookupColByTitle(ColumnInfo.colNoLearnersLabel,   table);
	    ColumnInfo<?> cNoEmployed   = AnnexureInfo.lookupColByTitle(ColumnInfo.colNoEmployedLabel,   table);
	    ColumnInfo<?> cNoUnemployed = AnnexureInfo.lookupColByTitle(ColumnInfo.colNoUnEmployedLabel, table);

	    ColumnInfo<?> cPostal = AnnexureInfo.lookupColByTitle(ColumnInfo.colPostalCodeLabel, table);
	    ColumnInfo<?> cArea   = AnnexureInfo.lookupColByTitle(ColumnInfo.colAreaLabel,       table);

	    boolean requiresPostal = (cPostal != null);
	    boolean requiresArea   = (cArea   != null);

	    for (Map<ColumnInfo<?>, Object> row : table.getRows()) {
	        if (row == null) {
	            continue;
	        }

	        int n = 0;
	        if (cNoLearners   != null && row.get(cNoLearners)   instanceof IntData) n += safeInt((IntData) row.get(cNoLearners));
	        if (cNoEmployed   != null && row.get(cNoEmployed)   instanceof IntData) n += safeInt((IntData) row.get(cNoEmployed));
	        if (cNoUnemployed != null && row.get(cNoUnemployed) instanceof IntData) n += safeInt((IntData) row.get(cNoUnemployed));

	        boolean learnersFilled = n > 0;

	        boolean postalFilled = false;
	        if (cPostal != null && row.get(cPostal) instanceof PostalData) {
	            postalFilled = notEmpty(((PostalData) row.get(cPostal)).getPostal());
	        }

	        boolean areaFilled = false;
	        if (cArea != null && row.get(cArea) instanceof AreaData) {
	            areaFilled = ((AreaData) row.get(cArea)).isSelected();
	        }

	        boolean isBlank = !learnersFilled
	                && (!requiresPostal || !postalFilled)
	                && (!requiresArea   || !areaFilled);

	        boolean isComplete = learnersFilled
	                && (!requiresPostal || postalFilled)
	                && (!requiresArea   || areaFilled);

	        if (isBlank) {
	            // completely empty -> allowed
	            continue;
	        }

	        if (isComplete) {
	            // good line
	            continue;
	        }

	        // Not blank and not complete -> partial -> invalid
	        return false;
	    }

	    return true;
	}

	private static boolean hasCompleteRow(AnnexureInfo table) {
	    if (table == null || table.getRows() == null) return false;

	    ColumnInfo<?> cNoLearners   = AnnexureInfo.lookupColByTitle(ColumnInfo.colNoLearnersLabel,   table);
	    ColumnInfo<?> cNoEmployed   = AnnexureInfo.lookupColByTitle(ColumnInfo.colNoEmployedLabel,   table);
	    ColumnInfo<?> cNoUnemployed = AnnexureInfo.lookupColByTitle(ColumnInfo.colNoUnEmployedLabel, table);

	    ColumnInfo<?> cPostal = AnnexureInfo.lookupColByTitle(ColumnInfo.colPostalCodeLabel, table);
	    ColumnInfo<?> cArea   = AnnexureInfo.lookupColByTitle(ColumnInfo.colAreaLabel,       table);

	    boolean requiresPostal = (cPostal != null);
	    boolean requiresArea   = (cArea   != null);

	    for (Map<ColumnInfo<?>, Object> row : table.getRows()) {
	        if (row == null) {
	            continue;
	        }

	        int n = 0;
	        if (cNoLearners   != null && row.get(cNoLearners)   instanceof IntData) n += safeInt((IntData) row.get(cNoLearners));
	        if (cNoEmployed   != null && row.get(cNoEmployed)   instanceof IntData) n += safeInt((IntData) row.get(cNoEmployed));
	        if (cNoUnemployed != null && row.get(cNoUnemployed) instanceof IntData) n += safeInt((IntData) row.get(cNoUnemployed));

	        boolean learnersFilled = n > 0;

	        boolean postalFilled = false;
	        if (cPostal != null && row.get(cPostal) instanceof PostalData) {
	            postalFilled = notEmpty(((PostalData) row.get(cPostal)).getPostal());
	        }

	        boolean areaFilled = false;
	        if (cArea != null && row.get(cArea) instanceof AreaData) {
	            areaFilled = ((AreaData) row.get(cArea)).isSelected();
	        }

	        boolean isComplete = learnersFilled
	                && (!requiresPostal || postalFilled)
	                && (!requiresArea   || areaFilled);

	        if (isComplete) {
	            return true;
	        }
	    }

	    return false;
	}

	// Already in your class:
	private static int safeInt(IntData d) { return d.getValue() == null ? 0 : d.getValue(); }
	private static boolean notEmpty(String s) { return s != null && !s.trim().isEmpty(); }

}
