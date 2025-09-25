package za.co.ntier.webform.form.bean.program;

import java.util.List;
import java.util.Map;

import za.co.ntier.webform.form.AbstractProgram;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.AreaData;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.IntData;
import za.co.ntier.webform.form.bean.component.PostalData;
import za.co.ntier.webform.form.bean.component.ProjectInput;
import za.co.ntier.api.model.I_ZZLearnersApplied;
import za.co.ntier.api.model.X_ZZ_Application_Form;

public class OhasspProgram extends AbstractProgram {
	private ProjectInput healthSafetySkills;

	public OhasspProgram(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm)  {
		super(menuContextInfo, applicationForm);	
		ColumnInfo<?> colNameProgram = ColumnInfo.getColLabel(ColumnInfo.colNameProgrammeLabel, I_ZZLearnersApplied.COLUMNNAME_Name);
		ColumnInfo<?> colNoEmployed = ColumnInfo.getColPositiveNumber(ColumnInfo.colNoEmployedLabel, I_ZZLearnersApplied.COLUMNNAME_ZZNoEmployedLearners);
		colNoEmployed.setCalTotal(true);
		healthSafetySkills = ProjectInput.getProject(
				List.of(colNameProgram, colNoEmployed));
		healthSafetySkills.initProject(applicationForm, 
				List.of(Map.of(colNameProgram, "Occupational Health and Safety Skills Programmes")));
	}

	/**
	 * @return the healthSafetySkills
	 */
	public ProjectInput getHealthSafetySkills() {
		return healthSafetySkills;
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm)  {
		super.saveForm(applicationForm);
		healthSafetySkills.save(trxName, applicationForm);
		
	}

	/**
	 * @param healthSafetySkills the healthSafetySkills to set
	 */
	public void setHealthSafetySkills(ProjectInput healthSafetySkills) {
		this.healthSafetySkills = healthSafetySkills;
	}

	 @Override
	    public boolean isProgramValid() {
	        return hasValidRow(healthSafetySkills);
	    }

	    // --- helpers ---
	    private static boolean hasValidRow(AnnexureInfo table) {
	        if (table == null || table.getRows() == null) return false;

	        ColumnInfo<?> colNum    = AnnexureInfo.lookupColByTitle(ColumnInfo.colNoEmployedLabel, table);
	        ColumnInfo<?> colPostal = AnnexureInfo.lookupColByTitle(ColumnInfo.colPostalCodeLabel, table);
	        ColumnInfo<?> colArea   = AnnexureInfo.lookupColByTitle(ColumnInfo.colAreaLabel, table);

	        for (Map<ColumnInfo<?>, Object> row : table.getRows()) {
	            int n = 0;
	            if (colNum != null && row.get(colNum) instanceof IntData) {
	                Integer v = ((IntData) row.get(colNum)).getValue();
	                n = (v != null) ? v : 0;
	            }

	            boolean postalOk = false;
	            if (colPostal != null && row.get(colPostal) instanceof PostalData) {
	                String p = ((PostalData) row.get(colPostal)).getPostal();
	                postalOk = p != null && !p.trim().isEmpty();
	            }

	            boolean areaOk = false;
	            if (colArea != null && row.get(colArea) instanceof AreaData) {
	                areaOk = ((AreaData) row.get(colArea)).getSelectedArea() != null;
	            }

	            if (n > 0 && postalOk && areaOk) {
	                return true;
	            }
	        }
	        return false;
	    }

}
