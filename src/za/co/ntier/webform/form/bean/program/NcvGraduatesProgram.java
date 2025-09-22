package za.co.ntier.webform.form.bean.program;

import java.util.List;
import java.util.Map;

import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.AreaData;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.IntData;
import za.co.ntier.webform.form.bean.component.PostalData;
import za.co.ntier.webform.form.bean.component.ProjectInput;
import za.co.ntier.webform.form.bean.component.UploadData;
import za.co.ntier.webform.model.I_ZZLearnersApplied;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class NcvGraduatesProgram implements ISaveForm, IProgram {
	private ProjectInput unemployed;

	public NcvGraduatesProgram(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm) {
		ColumnInfo<?> colNoUnEmployed = ColumnInfo.getColPositiveNumber(ColumnInfo.colNoUnEmployedLabel, I_ZZLearnersApplied.COLUMNNAME_ZZNoUnEmployedLearners);
		colNoUnEmployed.setCalTotal(true);
		
		if (menuContextInfo.getIsUploadWPAForNVC()) {
			unemployed = ProjectInput.getProject(
					List.of(
							colNoUnEmployed,
							ColumnInfo.getColPostal(ColumnInfo.colPostalCodeLabel, I_ZZLearnersApplied.COLUMNNAME_Postal),
							ColumnInfo.getColArea(ColumnInfo.colAreaLabel, MasterUtil.getInitCities(), I_ZZLearnersApplied.COLUMNNAME_C_City_ID),
							ColumnInfo.getColFileUpload(ColumnInfo.colWPALabel, ColumnInfo.btWPAText, 
									I_ZZLearnersApplied.COLUMNNAME_ZZ_WPAFile, I_ZZLearnersApplied.COLUMNNAME_ZZWPAFileName)
							)
					, null
					, false);
			
		}else {
			unemployed = ProjectInput.getProject(
					List.of(colNoUnEmployed));
		}
		
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
        // accept either a saved full path or at least a chosen file name
        return (up.getFullPath() != null && !up.getFullPath().isBlank())
            || (up.getFileName() != null && !up.getFileName().isBlank());
    }
}
