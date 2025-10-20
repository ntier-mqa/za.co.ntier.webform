package za.co.ntier.webform.form.bean.program;

import java.util.Map;

import za.co.ntier.api.model.X_ZZ_Application_Form;
import za.co.ntier.api.model.X_ZZ_FormDiscipline;
import za.co.ntier.webform.form.AbstractProgram;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.AreaData;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.IntData;
import za.co.ntier.webform.form.bean.component.PostalData;
import za.co.ntier.webform.form.bean.component.ProgramInput;

public class NonArtisanDevProgram extends AbstractProgram {
	private ProgramInput firLearnership;
	private ProgramInput generalLearnership;

	public NonArtisanDevProgram(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm){
		super(menuContextInfo, applicationForm);
		setGeneralLearnership(ProgramInput.getLearnership(X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_GeneralLearnership,
				applicationForm,
				menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID()));

		setFirLearnership(ProgramInput.getLearnership(X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_4IRLearnership,
				applicationForm,
				menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID()));

	}

	/**
	 * @return the firLearnership
	 */
	public ProgramInput getFirLearnership() {
		return firLearnership;
	}

	/**
	 * @return the generalLearnership
	 */
	public ProgramInput getGeneralLearnership() {
		return generalLearnership;
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm)  {
		super.saveForm(applicationForm);
		generalLearnership.save(trxName, applicationForm);
		firLearnership.save(trxName, applicationForm);
	}

	/**
	 * @param firLearnership the firLearnership to set
	 */
	public void setFirLearnership(ProgramInput firLearnership) {
		this.firLearnership = firLearnership;
	}

	/**
	 * @param generalLearnership the generalLearnership to set
	 */
	public void setGeneralLearnership(ProgramInput generalLearnership) {
		this.generalLearnership = generalLearnership;
	}

	@Override
	public boolean isProgramValid() {
	    return hasAtLeastOneValidRow(generalLearnership) || hasAtLeastOneValidRow(firLearnership);
	}

	private boolean hasAtLeastOneValidRow(ProgramInput tbl) {
	    if (tbl == null || tbl.getRows() == null) return false;

	    // Column lookups
	    ColumnInfo<?> colEmp   = AnnexureInfo.lookupColByTitle(ColumnInfo.colNoEmployedLabel, tbl);
	    ColumnInfo<?> colUnemp = AnnexureInfo.lookupColByTitle(ColumnInfo.colNoUnEmployedLabel, tbl);
	    ColumnInfo<?> colPostal= AnnexureInfo.lookupColByTitle(ColumnInfo.colPostalCodeLabel, tbl);
	    ColumnInfo<?> colArea  = AnnexureInfo.lookupColByTitle(ColumnInfo.colAreaLabel, tbl);

	    for (Map<ColumnInfo<?>, Object> row : tbl.getRows()) {
	        IntData employed   = (colEmp   != null) ? (IntData) row.get(colEmp)   : null;
	        IntData unemployed = (colUnemp != null) ? (IntData) row.get(colUnemp) : null;

	        int empVal   = (employed   != null && employed.getValue()   != null) ? employed.getValue()   : 0;
	        int unempVal = (unemployed != null && unemployed.getValue() != null) ? unemployed.getValue() : 0;

	        boolean anyCount = (empVal + unempVal) > 0;

	        PostalData postal = (colPostal != null) ? (PostalData) row.get(colPostal) : null;
	        boolean hasPostal = postal != null && postal.getPostal() != null && !postal.getPostal().trim().isEmpty();

	        AreaData area = (colArea != null) ? (AreaData) row.get(colArea) : null;
	        boolean hasArea = area != null && area.getSelectedArea() != null;

	        boolean wpaOk = true;
	        /*
	        if (colWpa != null && anyCount) {
	            UploadCellModel up = (UploadCellModel) row.get(colWpa);
	            wpaOk = (up != null && up.getFullPath() != null && !up.getFullPath().isEmpty());
	        }
	        */

	        boolean accredOk = true;
	        /*
	        if (colAcc != null && anyCount) {
	            UploadCellModel up = (UploadCellModel) row.get(colAcc);
	            accredOk = (up != null && up.getFullPath() != null && !up.getFullPath().isEmpty());
	        }
	        */

	        if (anyCount && hasPostal && hasArea && wpaOk && accredOk) {
	            return true; // at least one valid line
	        }
	    }
	    return false;
	}


}
