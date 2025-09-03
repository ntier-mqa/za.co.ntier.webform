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
import za.co.ntier.webform.form.bean.component.IntData;
import za.co.ntier.webform.form.bean.component.LearnerInputInfo;
import za.co.ntier.webform.form.bean.component.PostalData;
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
		CandidacyProgram.saveFormDisciplines(trxName, applicationForm, disciplines, X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_Discipline);
		
	}
	
	public static void saveFormLearnership(String trxName, X_ZZ_Application_Form applicationForm, ProgramInput learnership, String learnershipType) throws IOException {
		saveFormDisciplines(trxName, applicationForm, learnership, learnershipType);
	}
	
	public static void saveFormDisciplines(String trxName, X_ZZ_Application_Form applicationForm, ProgramInput disciplines, String disciplineType) throws IOException {
		
		ColumnInfo<?> disciplineColl = AnnexureInfo.lookupColByDataType(DataType.LearnerInfo, disciplines);
		ColumnInfo<?> nunLearnersColl = AnnexureInfo.lookupColByTitle(ProgramInput.NumLearnersTitle, disciplines);
		ColumnInfo<?> areaColl = AnnexureInfo.lookupColByDataType(DataType.Area, disciplines);
		ColumnInfo<?> postalColl = AnnexureInfo.lookupColByDataType(DataType.Postal, disciplines);
		ColumnInfo<?> wpaColl = AnnexureInfo.lookupColByTitle(ProgramInput.WPATitle, disciplines);
		ColumnInfo<?> accredColl = AnnexureInfo.lookupColByTitle(ProgramInput.AccredTitle, disciplines);
		
		
		ColumnInfo<?> numEmployedColl = AnnexureInfo.lookupColByTitle(ProgramInput.NumEmployedLearnersTitle, disciplines);
		ColumnInfo<?> numUnEmployedColl = AnnexureInfo.lookupColByTitle(ProgramInput.NumUnEmployedLearnersTitle, disciplines);
		int totalLearners = 0;
		
		for (Map<ColumnInfo<?>, Object> row : disciplines.getRows()) {
			IntData nunLearners = null;
			if (nunLearnersColl != null) {
				nunLearners = (IntData)row.get(nunLearnersColl);
			}
			
			IntData nunEmployer = null;
			if (numEmployedColl != null) {
				nunEmployer = (IntData)row.get(numEmployedColl);
			}
			
			IntData nunUnEmployer = null;
			if (numUnEmployedColl != null) {
				nunUnEmployer = (IntData)row.get(numUnEmployedColl);
			}
			
			
			if ((X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_Trade.equals(disciplineType) ||
					X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_Discipline.equals(disciplineType))
					&& (nunLearners.getValue() == null || nunLearners.getValue().intValue() == 0)){
				continue;
			}
				
			
			if ((X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_4IRLearnership.equals(disciplineType) ||
					X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_AETLearnership.equals(disciplineType) ||
					X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_GeneralLearnership.equals(disciplineType)) &&
					(nunEmployer.getValue() == null || nunEmployer.getValue() == 0) && 
					(nunUnEmployer.getValue() == null || nunUnEmployer.getValue() == 0))
				continue;
			
			X_ZZ_FormDiscipline formDisciplines = new X_ZZ_FormDiscipline(Env.getCtx(), 0, null); formDisciplines.setZZ_Application_Form_ID(applicationForm.getZZ_Application_Form_ID());
			
			if (nunLearners != null && nunLearners.getValue() != null && nunLearners.getValue() != 0) {
				formDisciplines.setZZ_LearnersNo(nunLearners.getValue());
				totalLearners += nunLearners.getValue();
			}
			
			if (nunEmployer != null && nunEmployer.getValue() != null && nunEmployer.getValue() != 0) {
				formDisciplines.setZZNoEmployedLearners(nunEmployer.getValue());
				totalLearners += nunEmployer.getValue();
			}
			
			if (nunUnEmployer != null && nunUnEmployer.getValue() != null && nunUnEmployer.getValue() != 0) {
				formDisciplines.setZZNoUnEmployedLearners(nunUnEmployer.getValue());
				totalLearners += nunUnEmployer.getValue();
			}
				
			
			MCity area = ((AreaData)row.get(areaColl)).getSelectedArea();
			if (area != null)
				formDisciplines.setC_City_ID(area.getC_City_ID());
			
			PostalData postalCode = (PostalData)row.get(postalColl);
			if (postalCode.getPostal() != null)
				formDisciplines.setPostal(postalCode.getPostal());
			
			formDisciplines.setZZ_DisciplineType(disciplineType);
			
			LearnerInputInfo learnerInfo = (LearnerInputInfo)row.get(disciplineColl);
			
			if (X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_Trade.equals(disciplineType)) {
				 formDisciplines.setZZ_Trade_ID(learnerInfo.getLearnerInputID()); 
			}else if (X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_Discipline.equals(disciplineType)) {
				 formDisciplines.setZZ_Disciplines_ID(learnerInfo.getLearnerInputID()); 
			}else {
				 formDisciplines.setZZ_Learnerships_ID(learnerInfo.getLearnerInputID()); 
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
		
		
		totalLearners += applicationForm.getZZTotalNumberApplied();
		applicationForm.setZZTotalNumberApplied(totalLearners);
		applicationForm.saveEx(trxName);
	}
}
