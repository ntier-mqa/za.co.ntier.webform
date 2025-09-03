package za.co.ntier.webform.form.bean.program;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.compiere.model.MCity;
import org.compiere.util.Env;

import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.DataType;
import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.AreaData;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.LearnerInputInfo;
import za.co.ntier.webform.form.bean.component.ProgramInput;
import za.co.ntier.webform.form.bean.component.UploadData;
import za.co.ntier.webform.model.X_ZZ_Application_Form;
import za.co.ntier.webform.model.X_ZZ_FormDiscipline;

public class CandidacyProgram implements ISaveForm, IProgram{
	private ProgramInput disciplines;

	public CandidacyProgram(MenuContextInfo menuContextInfo) throws NoSuchMethodException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		this.setDisciplines(ProgramInput.getDisciplines(
				menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID(),

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

	/**
	 * @param disciplines the disciplines to set
	 */
	public void setDisciplines(ProgramInput disciplines) {
		this.disciplines = disciplines;
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) throws IOException {
		saveFormDisciplines(trxName, applicationForm, disciplines, X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_Discipline);
		
	}
	
	public void saveFormDisciplines(String trxName, X_ZZ_Application_Form applicationForm, ProgramInput disciplines, String disciplineType) throws IOException {
		ColumnInfo<?> disciplineColl = null;
		ColumnInfo<?> nunLearnersColl = null;
		ColumnInfo<?> areaColl = null;
		ColumnInfo<?> postalColl = null;
		ColumnInfo<?> wpaColl = null;
		ColumnInfo<?> accredColl = null;
		
		disciplineColl = AnnexureInfo.lookupColByDataType(DataType.LearnerInfo, disciplines);
		nunLearnersColl = AnnexureInfo.lookupColByDataType(DataType.LearnerInfo, disciplines);
		areaColl = AnnexureInfo.lookupColByDataType(DataType.Area, disciplines);
		postalColl = AnnexureInfo.lookupColByDataType(DataType.Postal, disciplines);
		wpaColl = AnnexureInfo.lookupColByTitle(ProgramInput.WPATitle, disciplines);
		accredColl = AnnexureInfo.lookupColByTitle(ProgramInput.AccredTitle, disciplines);
		
		
		for (Map<ColumnInfo<?>, Object> row : disciplines.getRows()) {
			Integer nunLearners = (Integer)row.get(nunLearnersColl);
			if (nunLearners == null || nunLearners == 0) 
				continue;
			
			X_ZZ_FormDiscipline formDisciplines = new X_ZZ_FormDiscipline(Env.getCtx(), 0, null); formDisciplines.setZZ_Application_Form_ID(applicationForm.getZZ_Application_Form_ID());
			
			formDisciplines.setZZ_LearnersNo(nunLearners);
			
			MCity area = ((AreaData)row.get(areaColl)).getSelectedArea();
			if (area != null)
				formDisciplines.setC_City_ID(area.getC_City_ID());
			
			String postalCode= (String)row.get(postalColl);
			if (postalCode != null)
				formDisciplines.setPostal(postalCode);
			
			formDisciplines.setZZ_DisciplineType(disciplineType);
			
			LearnerInputInfo learnerInfo = (LearnerInputInfo)row.get(disciplineColl);
			
			if (X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_Trade.equals(disciplineType)) {
				 formDisciplines.setZZ_Trade_ID(learnerInfo.getLearnerInputID()); 
			}else {
				 formDisciplines.setZZ_Disciplines_ID(learnerInfo.getLearnerInputID()); 
			}
			
			UploadData wpaUploadInfo = (UploadData)row.get(wpaColl);
			
			if (wpaUploadInfo != null && StringUtils.isNoneEmpty(wpaUploadInfo.getFullPath())) {
				formDisciplines.setZZ_WPAFile(Files.readAllBytes(Paths.get(wpaUploadInfo.getFullPath()))); 
			}
			
			UploadData accredUploadInfo = (UploadData)row.get(accredColl);
			
			if (accredUploadInfo != null && StringUtils.isNoneEmpty(accredUploadInfo.getFullPath())) {
				formDisciplines.setZZ_AccredFile(Files.readAllBytes(Paths.get(accredUploadInfo.getFullPath()))); 
			}
			
			formDisciplines.saveEx(trxName);
		}
	}
}
