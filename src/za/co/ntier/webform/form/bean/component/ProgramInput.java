package za.co.ntier.webform.form.bean.component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.adempiere.webui.exception.ApplicationException;
import org.apache.commons.lang3.StringUtils;
import org.compiere.model.MCity;
import org.compiere.util.Env;

import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.bean.DataType;
import za.co.ntier.webform.model.X_ZZ_Application_Form;
import za.co.ntier.webform.model.X_ZZ_FormDiscipline;

public class ProgramInput extends AnnexureInfo {
	public static final String btAccredText = "Accred./SLA";
	public static final String btWPAText = "WPA";
	public static final String colAccredLabel = "Accreditation";
	public static final String colAreaLabel = "Area";
	public static final String colDisciplineLabel = "Discipline";
	public static final String colLearnershipLabel = "Learnership Type";
	public static final String colNoEmployedLabel = "No. of Employed Learners";
	public static final String colNoLearnersLabel = "No. of Learners";
	public static final String colNoUnEmployedLabel = "No. of Unemployed Learners";
	public static final String colPostalCodeLabel = "Site Postal Code";
	public static final String colTradeLabel = "Trade";
	public static final String colWPALabel = "WPA";
	
	public static ProgramInput getDisciplines(int programMasterDataID, String tableTitle){
		return ProgramInput.getTradeDiscipline(false, programMasterDataID, tableTitle);
	}

	@SuppressWarnings("unchecked")
	public static ProgramInput getLearnership(String learnershipType, int programMasterDataID){
		List<Object> rObjs = MasterUtil.queryLearnerInputInfos(programMasterDataID, learnershipType);
		List<LearnerInputInfo> learnerInputInfos = (List<LearnerInputInfo>) rObjs.get(0);
		boolean hasWPAReq = (boolean) rObjs.get(1);
		boolean hasAccred = (boolean) rObjs.get(2);

		List<ColumnInfo<?>> columns = new ArrayList<>();
		columns.add(ColumnInfo.getColLearnerInfo(colLearnershipLabel));
		columns.add(ColumnInfo.getColPositiveNumber(colNoEmployedLabel));
		columns.add(ColumnInfo.getColPositiveNumber(colNoUnEmployedLabel));
		columns.add(ColumnInfo.getColPostal(colPostalCodeLabel));

		columns.add(
				ColumnInfo.getColArea(colAreaLabel, MasterUtil.getInitCities()));

		if (hasWPAReq) {
			columns.add(ColumnInfo.getColFileUpload(colWPALabel, btWPAText));
		}

		if (hasAccred) {
			columns.add(ColumnInfo.getColFileUpload(colAccredLabel, btAccredText));
		}

		ProgramInput programInput = AnnexureInfo.getAnnexureInfo(ProgramInput.class, columns, true);

		Map<ColumnInfo<?>, Object> rowDataInits = null;
		for (LearnerInputInfo learnerInputInfo : learnerInputInfos) {
			rowDataInits = new HashMap<>();
			rowDataInits.put(columns.get(0), learnerInputInfo);
			programInput.createDetailRow(columns, rowDataInits);
			
		}
		return programInput;
	}

	public static ProgramInput getTrade(int programMasterDataID, String tableTitle){
		return ProgramInput.getTradeDiscipline(true, programMasterDataID, tableTitle);
	}

	@SuppressWarnings("unchecked")
	private static ProgramInput getTradeDiscipline(boolean isTrade, int programMasterDataID, String tableTitle){
		List<Object> rObjs = MasterUtil.queryLearnerInputInfos(programMasterDataID,
				isTrade ? X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_Trade
						: X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_Discipline);
		List<LearnerInputInfo> learnerInputInfos = (List<LearnerInputInfo>) rObjs.get(0);
		boolean hasWPAReq = (boolean) rObjs.get(1);
		boolean hasAccred = (boolean) rObjs.get(2);

		List<ColumnInfo<?>> columns = new ArrayList<>();
		columns.add(ColumnInfo.getColLearnerInfo(isTrade ? colTradeLabel : colDisciplineLabel));
		columns.add(ColumnInfo.getColPositiveNumber(colNoLearnersLabel));
		columns.add(ColumnInfo.getColPostal(colPostalCodeLabel));

		columns.add(
				ColumnInfo.getColArea(colAreaLabel, MasterUtil.getInitCities()));

		if (hasWPAReq) {
			columns.add(ColumnInfo.getColFileUpload(colWPALabel, "WPA"));
		}

		if (hasAccred) {
			columns.add(ColumnInfo.getColFileUpload(colAccredLabel, "Accred./SLA"));
		}

		ProgramInput programInput = AnnexureInfo.getAnnexureInfo(ProgramInput.class, columns, true);
		programInput.setTableTitle(tableTitle);

		Map<ColumnInfo<?>, Object> rowDataInits = null;
		for (LearnerInputInfo learnerInputInfo : learnerInputInfos) {
			rowDataInits = new HashMap<>();
			rowDataInits.put(columns.get(0), learnerInputInfo);
			programInput.createDetailRow(columns, rowDataInits);
			
		}
		return programInput;
	}

	public static void saveFormDisciplines(String trxName, X_ZZ_Application_Form applicationForm, ProgramInput disciplines, String disciplineType)  {
		
		ColumnInfo<?> disciplineColl = AnnexureInfo.lookupColByDataType(DataType.LearnerInfo, disciplines);
		ColumnInfo<?> nunLearnersColl = AnnexureInfo.lookupColByTitle(colNoLearnersLabel, disciplines);
		ColumnInfo<?> areaColl = AnnexureInfo.lookupColByDataType(DataType.Area, disciplines);
		ColumnInfo<?> postalColl = AnnexureInfo.lookupColByDataType(DataType.Postal, disciplines);
		ColumnInfo<?> wpaColl = AnnexureInfo.lookupColByTitle(colWPALabel, disciplines);
		ColumnInfo<?> accredColl = AnnexureInfo.lookupColByTitle(colAccredLabel, disciplines);
		
		
		ColumnInfo<?> numEmployedColl = AnnexureInfo.lookupColByTitle(colNoEmployedLabel, disciplines);
		ColumnInfo<?> numUnEmployedColl = AnnexureInfo.lookupColByTitle(colNoUnEmployedLabel, disciplines);
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
				formDisciplines.setZZNoLearners(nunLearners.getValue());
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
				try {
					formDisciplines.setZZ_WPAFile(Files.readAllBytes(Paths.get(wpaUploadInfo.getFullPath())));
				} catch (IOException e) {
					e.printStackTrace();
					throw new ApplicationException(e.getMessage(), e);
				} 
			}
			
			UploadData accredUploadInfo = (UploadData)row.get(accredColl);
			
			if (accredUploadInfo != null && StringUtils.isNoneEmpty(accredUploadInfo.getFullPath())) {
				try {
					formDisciplines.setZZ_AccredFile(Files.readAllBytes(Paths.get(accredUploadInfo.getFullPath())));
				} catch (IOException e) {
					e.printStackTrace();
					throw new ApplicationException(e.getMessage(), e);
				} 
			}
			
			formDisciplines.saveEx(trxName);
		}
		
		
		totalLearners += applicationForm.getZZTotalNumberApplied();
		applicationForm.setZZTotalNumberApplied(totalLearners);
		applicationForm.saveEx(trxName);
	}

	public static void saveFormLearnership(String trxName, X_ZZ_Application_Form applicationForm, ProgramInput learnership, String learnershipType) {
		saveFormDisciplines(trxName, applicationForm, learnership, learnershipType);
	}
}
