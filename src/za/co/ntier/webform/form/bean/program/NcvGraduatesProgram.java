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
        return hasValidRow(unemployed, false);
    }

    // --- helpers ---
    private static boolean hasValidRow(AnnexureInfo table, boolean wpaRequired) {
        if (table == null || table.getRows() == null) return false;

        ColumnInfo<?> colNum    = AnnexureInfo.lookupColByTitle(ColumnInfo.colNoUnEmployedLabel, table);
        ColumnInfo<?> colPostal = AnnexureInfo.lookupColByTitle(ColumnInfo.colPostalCodeLabel, table);
        ColumnInfo<?> colArea   = AnnexureInfo.lookupColByTitle(ColumnInfo.colAreaLabel, table);
        ColumnInfo<?> colWPA    = AnnexureInfo.lookupColByTitle(ColumnInfo.colWPALabel, table); // may be null if not added

        for (Map<ColumnInfo<?>, Object> row : table.getRows()) {
            // number
            int n = 0;
            if (colNum != null && row.get(colNum) instanceof IntData) {
                Integer v = ((IntData) row.get(colNum)).getValue();
                n = (v != null) ? v : 0;
            }

            // postal
            boolean postalOk = false;
            if (colPostal != null && row.get(colPostal) instanceof PostalData) {
                String p = ((PostalData) row.get(colPostal)).getPostal();
                postalOk = p != null && !p.trim().isEmpty();
            }

            // area
            boolean areaOk = false;
            if (colArea != null && row.get(colArea) instanceof AreaData) {
                areaOk = ((AreaData) row.get(colArea)).getSelectedArea() != null;
            }

            // wpa (only if required)
            boolean wpaOk = true;
            if (wpaRequired) {
                wpaOk = (colWPA != null && row.get(colWPA) instanceof UploadData)
                        && hasFile((UploadData) row.get(colWPA));
            }

            if (n > 0 && postalOk && areaOk && wpaOk) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasFile(UploadData up) {
        if (up == null) return false;
        // New upload in this session
        if (up.getBytes() != null && up.getBytes().length > 0) return true;
        // Or an already-saved file (filename populated on init)
        return up.getFileName() != null && !up.getFileName().isBlank();
    }

}
