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
	    // Valid if either table satisfies the “one complete row” rule
	    return hasAtLeastOneValidProgramRow(trade);
	}

	private static boolean hasAtLeastOneValidProgramRow(AnnexureInfo table) {
	    if (table == null || table.getRows() == null) return false;

	    ColumnInfo<?> cNoLearners   = AnnexureInfo.lookupColByTitle(ColumnInfo.colNoLearnersLabel,   table);
	    ColumnInfo<?> cNoEmployed   = AnnexureInfo.lookupColByTitle(ColumnInfo.colNoEmployedLabel,   table);
	    ColumnInfo<?> cNoUnemployed = AnnexureInfo.lookupColByTitle(ColumnInfo.colNoUnEmployedLabel, table);

	    ColumnInfo<?> cPostal = AnnexureInfo.lookupColByTitle(ColumnInfo.colPostalCodeLabel, table);
	    ColumnInfo<?> cArea   = AnnexureInfo.lookupColByTitle(ColumnInfo.colAreaLabel,       table);

	    for (Map<ColumnInfo<?>, Object> row : table.getRows()) {
	        int n = 0;
	        if (cNoLearners   != null && row.get(cNoLearners)   instanceof IntData) n += safeInt((IntData) row.get(cNoLearners));
	        if (cNoEmployed   != null && row.get(cNoEmployed)   instanceof IntData) n += safeInt((IntData) row.get(cNoEmployed));
	        if (cNoUnemployed != null && row.get(cNoUnemployed) instanceof IntData) n += safeInt((IntData) row.get(cNoUnemployed));

	        boolean postalOk = (cPostal != null && row.get(cPostal) instanceof PostalData)
	                && notEmpty(((PostalData) row.get(cPostal)).getPostal());

	        boolean areaOk = (cArea != null && row.get(cArea) instanceof AreaData)
	                && ((AreaData) row.get(cArea)).isSelected();

	        if (n > 0 && postalOk && areaOk) return true;
	    }
	    return false;
	}	

	private static int safeInt(IntData d) { return d.getValue() == null ? 0 : d.getValue(); }
	private static boolean notEmpty(String s) { return s != null && !s.trim().isEmpty(); }
}
