package za.co.ntier.webform.form.bean.program;

import java.util.ArrayList;
import java.util.List;

import org.compiere.model.MColumn;
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
	
	public Boolean getShowInstitutionParticipated(){
		return institutionParticipatedStr != null && "Y".equalsIgnoreCase(institutionParticipatedStr);
	}
	
	public HetLectureSupport(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm) {
		List<ColumnInfo<?>> cols = new ArrayList<>();
		
		ColumnInfo<?> noLectureAllocated = ColumnInfo.getColPositiveNumber("No. of Lectures Allocated", I_ZZLearnersApplied.COLUMNNAME_ZZNoLecturesAllocated);
		ColumnInfo<?> noLectureAbsorbed = ColumnInfo.getColPositiveNumber("No. of Lectures Absorbed", I_ZZLearnersApplied.COLUMNNAME_ZZNoLecturesAbsorbed);
		ColumnInfo<?> noLectureResigned = ColumnInfo.getColPositiveNumber("No. of Lectures Resigned", I_ZZLearnersApplied.COLUMNNAME_ZZNoLecturesResigned);
		ColumnInfo<?> noLectureCompleted = ColumnInfo.getColPositiveNumber("No. of Lectures Completed The Programme", I_ZZLearnersApplied.COLUMNNAME_ZZNoLecturesCompleted);
		ColumnInfo<?> noLectureContinuing = ColumnInfo.getColPositiveNumber("No. of Lectures Continuing The Programme", I_ZZLearnersApplied.COLUMNNAME_ZZNoLecturesContinuing);
		
		cols.add(noLectureAllocated);
		cols.add(noLectureAbsorbed);
		cols.add(noLectureResigned);
		cols.add(noLectureCompleted);
		cols.add(noLectureContinuing);
		
		institutionParticipated = ProjectInput.getProject(cols, null, false);
		institutionParticipated.setTableTitle("If the answer to the above is yes, please provide details of your institution’s participation in terms of");
		institutionParticipated.initProject(applicationForm);
		
		ColumnInfo<?> titleCol = ColumnInfo.getColLearnerInfo("MINING DISCIPLINES"
				, X_ZZ_FormDiscipline.COLUMNNAME_ZZ_Learnerships_ID);
		
		ColumnInfo<?> colNoLecture = ColumnInfo.getColPositiveNumber("NUMBER OF LECTURERS"
				, I_ZZ_FormDiscipline.COLUMNNAME_ZZNoLecture);
		colNoLecture.setCalTotal(true);
		
		ColumnInfo<?> colStartDate = ColumnInfo.getColDate("START DATE"
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
}
