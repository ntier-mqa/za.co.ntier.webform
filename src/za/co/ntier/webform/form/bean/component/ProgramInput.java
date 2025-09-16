package za.co.ntier.webform.form.bean.component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.adempiere.webui.exception.ApplicationException;
import org.apache.commons.lang3.StringUtils;
import org.compiere.model.MCity;
import org.compiere.model.MTable;
import org.compiere.model.Query;
import org.compiere.util.Env;

import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.Util;
import za.co.ntier.webform.form.bean.DataType;
import za.co.ntier.webform.model.I_ZZ_FormDiscipline;
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
	
	public static ProgramInput getDisciplines(int programMasterDataID, X_ZZ_Application_Form applicationForm, String tableTitle){
		return ProgramInput.getTradeDiscipline(X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_Discipline, programMasterDataID, applicationForm, tableTitle);
	}

	public static ProgramInput getTrade(int programMasterDataID, X_ZZ_Application_Form applicationForm, String tableTitle){
		return ProgramInput.getTradeDiscipline(X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_Trade, programMasterDataID, applicationForm, tableTitle);
	}

	public static ProgramInput getLearnership(String learnershipType, X_ZZ_Application_Form applicationForm, int programMasterDataID){
		return ProgramInput.getTradeDiscipline(learnershipType, programMasterDataID, applicationForm, null);
	}
	
	@SuppressWarnings("unchecked")
	private static ProgramInput getTradeDiscipline(String disciplineType, int programMasterDataID, X_ZZ_Application_Form applicationForm, String tableTitle){
		List<Object> rObjs = MasterUtil.queryLearnerInputInfos(programMasterDataID, disciplineType);
		List<LearnerInputInfo> learnerInputInfos = (List<LearnerInputInfo>) rObjs.get(0);
		boolean hasWPAReq = (boolean) rObjs.get(1);
		boolean hasAccred = (boolean) rObjs.get(2);

		List<ColumnInfo<?>> columns = new ArrayList<>();
		
		if ((X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_4IRLearnership.equals(disciplineType) ||
				X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_AETLearnership.equals(disciplineType) ||
				X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_GeneralLearnership.equals(disciplineType))) {
			columns.add(ColumnInfo.getColLearnerInfo(colLearnershipLabel));
			columns.add(ColumnInfo.getColPositiveNumber(colNoEmployedLabel));
			columns.add(ColumnInfo.getColPositiveNumber(colNoUnEmployedLabel));
		}else {
			if (X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_Trade.equals(disciplineType)) {
				columns.add(ColumnInfo.getColLearnerInfo(colTradeLabel));
			}else {
				columns.add(ColumnInfo.getColLearnerInfo(colDisciplineLabel));
			}
			
			columns.add(ColumnInfo.getColPositiveNumber(colNoLearnersLabel));
		}
		
		columns.add(ColumnInfo.getColPostal(colPostalCodeLabel));
		columns.add(
				ColumnInfo.getColArea(colAreaLabel, MasterUtil.getInitCities()));

		if (hasWPAReq) {
			columns.add(ColumnInfo.getColFileUpload(colWPALabel, btWPAText));
		}

		if (hasAccred) {
			columns.add(ColumnInfo.getColFileUpload(colAccredLabel, btAccredText));
		}

		List<X_ZZ_FormDiscipline> formDisciplines = null;
		if(applicationForm != null) {
			String whereFormDiscipline = String.format("%s = ?", 
					I_ZZ_FormDiscipline.COLUMNNAME_ZZ_Application_Form_ID);
			Query queryFormDiscipline = MTable.get(X_ZZ_FormDiscipline.Table_ID).createQuery(whereFormDiscipline, null);
			formDisciplines = queryFormDiscipline.setParameters(applicationForm.getZZ_Application_Form_ID()).list();
		}
		 
		
		ProgramInput programInput = AnnexureInfo.getAnnexureInfo(ProgramInput.class, columns, true);
		Function<AnnexureInfo, AnnexureRow<?>> supplierRowFormDiscipline = (parent) -> new AnnexureRow<X_ZZ_FormDiscipline>(parent);
		programInput.setSupplier(supplierRowFormDiscipline);
		programInput.setTableTitle(tableTitle);

		ColumnInfo<?> disciplineColl = AnnexureInfo.lookupColByDataType(DataType.LearnerInfo, programInput);
		ColumnInfo<?> nunLearnersColl = AnnexureInfo.lookupColByTitle(colNoLearnersLabel, programInput);
		ColumnInfo<?> areaColl = AnnexureInfo.lookupColByDataType(DataType.Area, programInput);
		ColumnInfo<?> postalColl = AnnexureInfo.lookupColByDataType(DataType.Postal, programInput);
		ColumnInfo<?> wpaColl = AnnexureInfo.lookupColByTitle(colWPALabel, programInput);
		ColumnInfo<?> accredColl = AnnexureInfo.lookupColByTitle(colAccredLabel, programInput);
		ColumnInfo<?> numEmployedColl = AnnexureInfo.lookupColByTitle(colNoEmployedLabel, programInput);
		ColumnInfo<?> numUnEmployedColl = AnnexureInfo.lookupColByTitle(colNoUnEmployedLabel, programInput);
		
		for (LearnerInputInfo learnerInputInfo : learnerInputInfos) {
			AnnexureRow<X_ZZ_FormDiscipline> row = (AnnexureRow<X_ZZ_FormDiscipline>)programInput.createDetailRow();
			
			if (formDisciplines != null) {
				for(X_ZZ_FormDiscipline formDiscipline : formDisciplines) {
					if (formDiscipline.getZZ_Trade_ID() == learnerInputInfo.getLearnerInputID()
							|| formDiscipline.getZZ_Disciplines_ID() == learnerInputInfo.getLearnerInputID()
							|| formDiscipline.getZZ_Learnerships_ID() == learnerInputInfo.getLearnerInputID()) {
						row.setData(formDiscipline);
						break;
					}
				}
			}
			
			X_ZZ_FormDiscipline formDisciplineSaved = row.getData();
			
			row.put(disciplineColl, learnerInputInfo);
			
			if(nunLearnersColl != null && formDisciplineSaved != null) {
				((IntData)row.get(nunLearnersColl)).setValue(Util.convert(formDisciplineSaved.getZZNoLearners()));
			}
			
			if(numEmployedColl != null && formDisciplineSaved != null) {
				((IntData)row.get(numEmployedColl)).setValue(Util.convert(formDisciplineSaved.getZZNoEmployedLearners()));
			}
			
			if(numUnEmployedColl != null && formDisciplineSaved != null) {
				((IntData)row.get(numUnEmployedColl)).setValue(Util.convert(formDisciplineSaved.getZZNoUnEmployedLearners()));
			}
			
			if(areaColl != null && formDisciplineSaved != null && formDisciplineSaved.getC_City_ID() > 0) {
				AreaData areaData = (AreaData)row.get(areaColl);
				for(MCity area : areaData.getDataProvider()) {
					if (area.getC_City_ID() == formDisciplineSaved.getC_City_ID()) {
						areaData.setSelectedAreaInternal(area);
					}
				}
			}
			
			
			if(postalColl != null && formDisciplineSaved != null && StringUtils.isNoneBlank(formDisciplineSaved.getPostal())) {
				PostalData postal = (PostalData)row.get(postalColl);
				postal.setPostalInternal(formDisciplineSaved.getPostal());
			}
		/*
			if(wpaColl != null && formDisciplineSaved != null && formDisciplineSaved.getname > 0) {
				((IntData)row.get(numUnEmployedColl)).setValue(formDisciplineSaved.getZZNoUnEmployedLearners());
			}
			*/
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
		
		for (Map<ColumnInfo<?>, Object> rowObj : disciplines.getRows()) {
			@SuppressWarnings("unchecked")
			AnnexureRow<X_ZZ_FormDiscipline> row = (AnnexureRow<X_ZZ_FormDiscipline>)rowObj;
			
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
			
			
			X_ZZ_FormDiscipline formDisciplines = row.getData();
			if (formDisciplines == null) {
				formDisciplines = new X_ZZ_FormDiscipline(Env.getCtx(), 0, null);
			}
			
			
			formDisciplines.setZZ_Application_Form_ID(applicationForm.getZZ_Application_Form_ID());
			
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
