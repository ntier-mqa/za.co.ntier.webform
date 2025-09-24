package za.co.ntier.webform.form.bean.program;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.event.CheckEvent;

import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.ProgramInput;
import za.co.ntier.webform.form.bean.component.ProjectInput;
import za.co.ntier.webform.model.I_ZZLearnersApplied;
import za.co.ntier.webform.model.I_ZZ_FormDiscipline;
import za.co.ntier.webform.model.X_ZZ_Application_Form;
import za.co.ntier.webform.model.X_ZZ_FormDiscipline;

public class HetLectureSupport  implements ISaveForm, IProgram {
	private ProgramInput disciplines;
	private String institutionParticipatedStr = null;
	private ProjectInput institutionParticipated;
	// keep refs for validation
	private ColumnInfo<?> noLectureAllocated, noLectureAbsorbed, noLectureResigned, noLectureCompleted, noLectureContinuing;
	private ColumnInfo<?> titleCol, colNoLecture, colStartDate;

	public Boolean getShowInstitutionParticipated(){
		return institutionParticipatedStr != null && "Y".equalsIgnoreCase(institutionParticipatedStr);
	}

	public HetLectureSupport(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm) {
		List<ColumnInfo<?>> cols = new ArrayList<>();

		noLectureAllocated = ColumnInfo.getColPositiveNumber("No. of Lectures Allocated", I_ZZLearnersApplied.COLUMNNAME_ZZNoLecturesAllocated);
		noLectureAbsorbed = ColumnInfo.getColPositiveNumber("No. of Lectures Absorbed", I_ZZLearnersApplied.COLUMNNAME_ZZNoLecturesAbsorbed);
		noLectureResigned = ColumnInfo.getColPositiveNumber("No. of Lectures Resigned", I_ZZLearnersApplied.COLUMNNAME_ZZNoLecturesResigned);
		noLectureCompleted = ColumnInfo.getColPositiveNumber("No. of Lectures Completed The Programme", I_ZZLearnersApplied.COLUMNNAME_ZZNoLecturesCompleted);
		noLectureContinuing = ColumnInfo.getColPositiveNumber("No. of Lectures Continuing The Programme", I_ZZLearnersApplied.COLUMNNAME_ZZNoLecturesContinuing);

		cols.add(noLectureAllocated);
		cols.add(noLectureAbsorbed);
		cols.add(noLectureResigned);
		cols.add(noLectureCompleted);
		cols.add(noLectureContinuing);

		institutionParticipated = ProjectInput.getProject(cols, null, false);
		institutionParticipated.setTableTitle("If the answer to the above is yes, please provide details of your institution’s participation in terms of");
		institutionParticipated.initProject(applicationForm);

		titleCol = ColumnInfo.getColLearnerInfo("MINING DISCIPLINES"
				, X_ZZ_FormDiscipline.COLUMNNAME_ZZ_Learnerships_ID);

		colNoLecture = ColumnInfo.getColPositiveNumber("NUMBER OF LECTURERS"
				, I_ZZ_FormDiscipline.COLUMNNAME_ZZNoLecture);
		colNoLecture.setCalTotal(true);

		colStartDate = ColumnInfo.getColDate("START DATE"
				, I_ZZ_FormDiscipline.COLUMNNAME_StartDate);


		List<ColumnInfo<?>> columns = new ArrayList<>();
		columns.add(titleCol);
		columns.add(colNoLecture);
		columns.add(colStartDate);

		disciplines = ProgramInput.getDisciplines(menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID(),
				applicationForm, columns);
		disciplines.setSectionHeader("HET LECTURE SUPPORT GRANT APPLICATION");

		if(applicationForm != null) {
			institutionParticipatedStr = applicationForm.getZZHasPastParticipatedLecturer();
		}

	}

	/**
	 * @return the disciplines
	 */
	public ProgramInput getDisciplines() {
		return disciplines;
	}

	/**
	 * @param disciplines the disciplines to set
	 */
	public void setDisciplines(ProgramInput disciplines) {
		this.disciplines = disciplines;
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		applicationForm.setZZHasPastParticipatedLecturer(institutionParticipatedStr);
		institutionParticipated.save(trxName, applicationForm);
		disciplines.save(trxName, applicationForm);

	}
	/**
	 * @return the institutionParticipatedStr
	 */
	public String getInstitutionParticipatedStr() {
		return institutionParticipatedStr;
	}

	/**
	 * @param institutionParticipatedStr the institutionParticipatedStr to set
	 */
	public void setInstitutionParticipatedStr(String institutionParticipatedStr) {
		this.institutionParticipatedStr = institutionParticipatedStr;
	}

	public void checkInstitutionParticipated(CheckEvent event){
		BindUtils.postNotifyChange(this, "showInstitutionParticipated");
	}

	/**
	 * @return the institutionParticipated
	 */
	public ProjectInput getInstitutionParticipated() {
		return institutionParticipated;
	}

	/**
	 * @param institutionParticipated the institutionParticipated to set
	 */
	public void setInstitutionParticipated(ProjectInput institutionParticipated) {
		this.institutionParticipated = institutionParticipated;
	}

	@Override
	public boolean isProgramValid() {
		// 1) At least one FULL discipline row
		boolean hasOneCompleteDiscipline =
				disciplines != null
				&& disciplines.getRows() != null
				&& disciplines.getRows().stream().anyMatch(this::isDisciplineRowComplete);
		if (!hasOneCompleteDiscipline) return false;

		// 2) If YES, all five top numbers must be entered (0 allowed)
		if (getShowInstitutionParticipated()) {
			if (!allNumbersPresent(institutionParticipated, List.of(
					noLectureAllocated, noLectureAbsorbed, noLectureResigned,
					noLectureCompleted, noLectureContinuing))) {
				return false;
			}
		}
		return true;
	}

	private boolean isDisciplineRowComplete(java.util.Map<ColumnInfo<?>, Object> row) {
	    boolean hasDiscipline = row.get(titleCol) != null;

	    Integer nLect = getInt(row.get(colNoLecture));
	    boolean hasLecturers = nLect != null && nLect > 0;

	    boolean hasDate = hasDateValue(row.get(colStartDate));

	    return hasDiscipline && hasLecturers && hasDate;
	}

	// Extract Integer from whatever the table stores (IntData/Number/String/etc.)
	private Integer getInt(Object cell) {
		if (cell == null) return null;
		try {
			// IntData style: has getValue()
			var m = cell.getClass().getMethod("getValue");
			Object v = m.invoke(cell);
			if (v instanceof Number) return ((Number) v).intValue();
			if (v instanceof String) {
				String s = ((String) v).trim();
				return s.isEmpty() ? null : Integer.valueOf(s);
			}
		} catch (NoSuchMethodException ignore) {
			// fall through and try common shapes
			if (cell instanceof Number) return ((Number) cell).intValue();
			if (cell instanceof String) {
				String s = ((String) cell).trim();
				return s.isEmpty() ? null : Integer.valueOf(s);
			}
		} catch (Exception ignore) { /* swallow and return null */ }
		return null;
	}

	/** True iff every column in cols has a non-null integer in the FIRST row of the ProjectInput */
	private boolean allNumbersPresent(ProjectInput pi, java.util.List<ColumnInfo<?>> cols) {
		if (pi == null || pi.getRows() == null || pi.getRows().isEmpty()) return false;
		@SuppressWarnings("unchecked")
		java.util.Map<ColumnInfo<?>, Object> row = (java.util.Map<ColumnInfo<?>, Object>) pi.getRows().get(0);
		for (ColumnInfo<?> c : cols) {
			Integer v = getInt(row.get(c));
			if (v == null) return false;   // (if you require >0, use: if (v==null || v<=0) return false;)
		}
		return true;
	}
	
	private boolean hasDateValue(Object cell) {
	    if (cell == null) return false;
	    try {
	        // Common wrapper used in these tables: has getLocalDate()
	        var m = cell.getClass().getMethod("getLocalDate");
	        Object v = m.invoke(cell);
	        if (v != null) return true; // v is typically java.time.LocalDate
	    } catch (NoSuchMethodException ignore) {
	        // fall through
	    } catch (Exception ignore) {
	        return false;
	    }
	    // Fallbacks in case a different shape is used
	    if (cell instanceof java.time.LocalDate) return true;
	    if (cell instanceof java.util.Date) return true;
	    try {
	        var m = cell.getClass().getMethod("getValue");
	        return m.invoke(cell) != null;
	    } catch (Exception ignore) {}
	    return false;
	}

}
