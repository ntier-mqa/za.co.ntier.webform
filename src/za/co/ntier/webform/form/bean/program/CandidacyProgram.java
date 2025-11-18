package za.co.ntier.webform.form.bean.program;


import java.util.Map;

import za.co.ntier.api.model.X_ZZ_Application_Form;
import za.co.ntier.webform.form.AbstractProgram;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.DataType;
import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.IntData;
import za.co.ntier.webform.form.bean.component.ProgramInput;

public class CandidacyProgram extends AbstractProgram{
	private ProgramInput disciplines;

	public CandidacyProgram(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm) {
		super(menuContextInfo, applicationForm);

		this.setDisciplines(ProgramInput.getDisciplines(
				menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID(),
				applicationForm,

				"""
										List of disciplines supported for Internships which the number of learners applying should
						be based on.
										"""));

	}

	/**
	 * @return the disciplines
	 */
	public ProgramInput getDisciplines() {
		return disciplines;
	}
	
	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm)  {
		super.saveForm(applicationForm);
		disciplines.save(trxName, applicationForm);
		
	}
	
	/**
	 * @param disciplines the disciplines to set
	 */
	public void setDisciplines(ProgramInput disciplines) {
		this.disciplines = disciplines;
	}

	@Override
	public boolean isProgramValid() {
		return isCandidacyValid();
	}
	
	private boolean isCandidacyValid() {
	    if (!(this instanceof CandidacyProgram)) return false;
	    AnnexureInfo disciplines = ((CandidacyProgram) this).getDisciplines();
	    if (disciplines == null) return true;

	    
	    ColumnInfo<?> learnersCol   = AnnexureInfo.lookupColByTitle(ColumnInfo.colNoLearnersLabel,   disciplines);
	    ColumnInfo<?> postalCol   = AnnexureInfo.lookupColByTitle(ColumnInfo.colPostalCodeLabel,   disciplines);
	    ColumnInfo<?> areaCol     = AnnexureInfo.lookupColByTitle(ColumnInfo.colAreaLabel,   disciplines);
	    // Optionally a WPA column check…

	    int validRows = 0;
	    for (Map<ColumnInfo<?>, Object> row : disciplines.getRows()) {
	        Integer learners = getLearners(learnersCol.getDataType(), row.get(learnersCol));
	        String  postal   = extractPostal(row.get(postalCol));
	        boolean areaOK   = extractAreaSelected(row.get(areaCol));

	        boolean blank = (learners == null &&
	                         (postal == null || postal.isBlank()) &&
	                         !areaOK);
	        if (blank) continue; // ignore empty line

	        if (learners == null || learners <= 0) return false;
	        if (postal == null || postal.isBlank()) return false;
	        if (!areaOK) return false;
	        validRows++;
	    }
	    return validRows >= 1;
	}
		
	
	private Integer getLearners(DataType dt, Object cell) {
	    if (cell == null) return null;
	    switch (dt) {
	        case PositiveNumber:
                Integer v = ((IntData)cell).getValue();
                return v;
	        default:
	            return null;
	    }
	}
	
	private String extractPostal(Object postalCell) {
	    if (postalCell == null) return null;
	    try { return (String) postalCell.getClass().getMethod("getPostal").invoke(postalCell); }
	    catch (Exception e) { return postalCell.toString().trim(); }
	}
	private boolean extractAreaSelected(Object areaCell) {
	    if (areaCell == null) return false;
	    try { return areaCell.getClass().getMethod("getSelectedArea").invoke(areaCell) != null; }
	    catch (Exception e) { return false; }
	}

}
