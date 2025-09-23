package za.co.ntier.webform.form.bean.program;

import java.util.ArrayList;
import java.util.List;

import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.ProgramInput;
import za.co.ntier.webform.model.I_ZZ_FormDiscipline;
import za.co.ntier.webform.model.X_ZZ_Application_Form;
import za.co.ntier.webform.model.X_ZZ_FormDiscipline;

public class HetLectureSupport  implements ISaveForm, IProgram {
	private ProgramInput disciplines;
	
	public HetLectureSupport(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm) {
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
		disciplines.save(trxName, applicationForm);
		
	}
}
