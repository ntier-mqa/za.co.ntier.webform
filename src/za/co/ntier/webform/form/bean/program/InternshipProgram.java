package za.co.ntier.webform.form.bean.program;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.compiere.model.MCity;

import za.co.ntier.api.model.X_ZZ_Application_Form;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.AreaData;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.PostalData;
import za.co.ntier.webform.form.bean.component.ProgramInput;

public class InternshipProgram extends CandidacyProgram {
	private ProgramInput trade;

	public InternshipProgram(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm) {
		super(menuContextInfo, applicationForm);
		this.trade = ProgramInput.getTrade(menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID(),
				applicationForm,

				"""
										List of disciplines supported for Artisan Internships which the number of learners applying
						should be based on. Preference will be given to the following trades that are hard to fill
						according MQA SPOI list, (Diesel Mechanic and Millwright).
										""");
	}

	/**
	 * @return the trade
	 */
	public ProgramInput getTrade() {
		return trade;
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm)  {
		super.saveForm(trxName, applicationForm);
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
	    // both tables must have at least one fully-complete row
	    return hasValidRow(getDisciplines()) || hasValidRow(trade);
	}

	private boolean hasValidRow(ProgramInput table) {
	    if (table == null || table.getRows() == null) return false;

	    ColumnInfo<?> noLearnersCol = AnnexureInfo.lookupColByTitle(ColumnInfo.colNoLearnersLabel, table);
	    ColumnInfo<?> postalCol     = AnnexureInfo.lookupColByTitle(ColumnInfo.colPostalCodeLabel, table);
	    ColumnInfo<?> areaCol       = AnnexureInfo.lookupColByTitle(ColumnInfo.colAreaLabel, table);
	    if (noLearnersCol == null || postalCol == null || areaCol == null) return false;

	    for (Map<ColumnInfo<?>, Object> row : table.getRows()) {
	        // No. of learners
	        Integer learners = AnnexureInfo.getIntegerValue(row, noLearnersCol);

	        // Postal code
	        String postal = null;
	        Object postalCell = row.get(postalCol);
	        if (postalCell instanceof PostalData) {
	            postal = ((PostalData) postalCell).getPostal();
	        } else if (postalCell instanceof String) { // defensive
	            postal = (String) postalCell;
	        }

	        // Area
	        MCity area = null;
	        Object areaCell = row.get(areaCol);
	        if (areaCell instanceof AreaData) {
	            area = ((AreaData) areaCell).getSelectedArea();
	        }

	        if (learners != null && learners > 0
	                && StringUtils.isNotBlank(postal)
	                && area != null) {
	            return true; // this table has a complete row
	        }
	    }
	    return false;
	}
}
