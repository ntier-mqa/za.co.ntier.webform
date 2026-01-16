package za.co.ntier.webform.form.bean.program;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import za.co.ntier.api.model.I_ZZLearnersApplied;
import za.co.ntier.api.model.I_ZZSubAnnex;
import za.co.ntier.api.model.X_ZZ_Application_Form;
import za.co.ntier.webform.form.AbstractProgram;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.AreaData;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.IntData;
import za.co.ntier.webform.form.bean.component.LearnerInputInfo;
import za.co.ntier.webform.form.bean.component.PostalData;
import za.co.ntier.webform.form.bean.component.ProgramInput;
import za.co.ntier.webform.form.bean.component.ProjectInput;
import za.co.ntier.webform.form.bean.component.UploadData;

public class NcvGraduatesProgram extends AbstractProgram {
	private ProjectInput unemployed;

	public NcvGraduatesProgram(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm) {
		super(menuContextInfo, applicationForm);
		ColumnInfo<?> colNoUnEmployed = ColumnInfo.getColPositiveNumber(ColumnInfo.colNoUnEmployedLabel, I_ZZLearnersApplied.COLUMNNAME_ZZNoUnEmployedLearners);
		colNoUnEmployed.setCalTotal(true);
		
		List<LearnerInputInfo> tradeObj = ProgramInput.queryLearnerInputInfos(menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID(), true);
		ColumnInfo<?> tradeCol = ColumnInfo.getColList("Trades", tradeObj, I_ZZLearnersApplied.COLUMNNAME_ZZ_Trade_ID, "learnerInputID");
		
		if (menuContextInfo.getIsUploadWPAForNVC()) {
			unemployed = ProjectInput.getProject(
					List.of(tradeCol,
							colNoUnEmployed,
							ColumnInfo.getColPostal(ColumnInfo.colPostalCodeLabel, I_ZZLearnersApplied.COLUMNNAME_Postal),
							ColumnInfo.getColArea(ColumnInfo.colAreaLabel, MasterUtil.getInitCities(), I_ZZLearnersApplied.COLUMNNAME_C_City_ID),
							ColumnInfo.getColFileUpload(ColumnInfo.colWPALabel, ColumnInfo.btWPAText, 
									I_ZZLearnersApplied.COLUMNNAME_ZZ_WPAFile, I_ZZLearnersApplied.COLUMNNAME_ZZWPAFileName)
							)
					, null
					, false
					, true);
			
		}else {
			unemployed = ProjectInput.getProject(
					List.of(colNoUnEmployed), true);
		}
		
		unemployed.setShowAddButton(true);
		unemployed.initProject(applicationForm);
		
	}

	/**
	 * @return the unemployed
	 */
	public ProjectInput getUnemployed() {
		return unemployed;
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm)  {
		super.saveForm(applicationForm);
		unemployed.save(trxName, applicationForm);
	}

	/**
	 * @param unemployed the unemployed to set
	 */
	public void setUnemployed(ProjectInput unemployed) {
		this.unemployed = unemployed;
	}

	@Override
	public boolean isProgramValid() {
	    // WPA is required only when the column exists (same flag you used to add it)
	    boolean wpaRequired = getMenuContextInfo().getIsUploadWPAForNVC(); // if you have getter, else pass in via field
	    return hasValidRow(unemployed, true, wpaRequired);
	}

	// --- helpers ---
	private static boolean hasValidRow(AnnexureInfo table, boolean tradeRequired, boolean wpaRequired) {
	    if (table == null || table.getRows() == null) return false;

	    ColumnInfo<?> colTrade  = AnnexureInfo.lookupColByTitle("Trades", table);
	    ColumnInfo<?> colNum    = AnnexureInfo.lookupColByTitle(ColumnInfo.colNoUnEmployedLabel, table);
	    ColumnInfo<?> colPostal = AnnexureInfo.lookupColByTitle(ColumnInfo.colPostalCodeLabel, table);
	    ColumnInfo<?> colArea   = AnnexureInfo.lookupColByTitle(ColumnInfo.colAreaLabel, table);
	    ColumnInfo<?> colWPA    = AnnexureInfo.lookupColByTitle(ColumnInfo.colWPALabel, table);

	    boolean hasCompleteRow = false;

	    for (Map<ColumnInfo<?>, Object> row : table.getRows()) {

	        // trade
	        Object tradeObj = (colTrade != null) ? row.get(colTrade) : null;
	        boolean tradeFilled = !tradeRequired || tradeObj != null;

	        // number
	        Integer nVal = null;
	        if (colNum != null && row.get(colNum) instanceof IntData) {
	            nVal = ((IntData) row.get(colNum)).getValue();
	        }
	        boolean numFilled = (nVal != null && nVal > 0);

	        // postal
	        String pVal = null;
	        if (colPostal != null && row.get(colPostal) instanceof PostalData) {
	            pVal = ((PostalData) row.get(colPostal)).getPostal();
	        }
	        boolean postalFilled = (pVal != null && !pVal.trim().isEmpty());

	        // area
	        boolean areaFilled = false;
	        if (colArea != null && row.get(colArea) instanceof AreaData) {
	            areaFilled = ((AreaData) row.get(colArea)).getSelectedArea() != null;
	        }

	        // wpa
	        boolean wpaFilled = !wpaRequired;
	        UploadData up = null;
	        if (wpaRequired) {
	            if (colWPA != null && row.get(colWPA) instanceof UploadData) {
	                up = (UploadData) row.get(colWPA);
	                wpaFilled = hasFile(up);
	            } else {
	                wpaFilled = false;
	            }
	        }

	        // ---- row state checks ----

	        // Is this row "started" (user touched anything)?
	        boolean started =
	                (tradeRequired && tradeObj != null)
	             || (nVal != null && nVal > 0)
	             || (pVal != null && !pVal.trim().isEmpty())
	             || areaFilled
	             || (wpaRequired && up != null && (hasFile(up)));

	        // If not started at all -> ignore (blank row allowed)
	        if (!started) {
	            continue;
	        }

	        // If started, it must be fully complete, otherwise program invalid (disable Next)
	        boolean complete = tradeFilled && numFilled && postalFilled && areaFilled && wpaFilled;
	        if (!complete) {
	            return false; // <-- this is what disables Next when a new line is partially filled
	        }

	        hasCompleteRow = true;
	    }

	    // Must have at least one complete row overall
	    return hasCompleteRow;
	}


	private static boolean hasFile(UploadData up) {
	    if (up == null) return false;
	    if (up.getBytes() != null && up.getBytes().length > 0) return true;
	    return up.getFileName() != null && !up.getFileName().isBlank();
	}


}
