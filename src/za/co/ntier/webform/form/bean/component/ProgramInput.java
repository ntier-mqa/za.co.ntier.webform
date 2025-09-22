package za.co.ntier.webform.form.bean.component;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;

import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.DB;
import org.compiere.util.Env;

import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.bean.DataType;
import za.co.ntier.webform.model.I_ZZ_FormDiscipline;
import za.co.ntier.webform.model.X_ZZ_Application_Form;
import za.co.ntier.webform.model.X_ZZ_Disciplines;
import za.co.ntier.webform.model.X_ZZ_FormDiscipline;
import za.co.ntier.webform.model.X_ZZ_Learnerships;
import za.co.ntier.webform.model.X_ZZ_Program_Disciplines;
import za.co.ntier.webform.model.X_ZZ_Program_Learnerships;
import za.co.ntier.webform.model.X_ZZ_Program_Trade;
import za.co.ntier.webform.model.X_ZZ_Trade;

public class ProgramInput extends AnnexureInfo {
	public static Entry<Boolean, Boolean> queryUploadColumnForTrade(int programMasterDataID, boolean isTrade) {
		String learnerInputID;
		String learnerInputProgramTable;
		String learnerInputTable;
		
		if(isTrade) {
			learnerInputID = X_ZZ_Program_Trade.COLUMNNAME_ZZ_Trade_ID;
			learnerInputProgramTable = X_ZZ_Program_Trade.Table_Name;
			learnerInputTable = X_ZZ_Trade.Table_Name;
		}else {
			learnerInputID = X_ZZ_Program_Disciplines.COLUMNNAME_ZZ_Disciplines_ID;
			learnerInputProgramTable = X_ZZ_Program_Disciplines.Table_Name;
			learnerInputTable = X_ZZ_Disciplines.Table_Name;
		}
		
		String uploadColumnSql = String.format("""
				SELECT MAX(%s), MAX(%s) 
				FROM %s INNER JOIN %s 
					ON (%s.%s = %s.%s) 
				WHERE %s = ?
				"""
				, X_ZZ_Program_Trade.COLUMNNAME_ZZ_WPA_Req, X_ZZ_Program_Trade.COLUMNNAME_ZZ_Is_Accred_SLA_Req 
				, learnerInputProgramTable, learnerInputTable
				, learnerInputProgramTable, learnerInputID, learnerInputTable, learnerInputID
				, X_ZZ_Program_Trade.COLUMNNAME_ZZ_Program_Master_Data_ID
				);
		
		List<Object> uploadConditions = DB.getSQLValueObjectsEx(null, uploadColumnSql, programMasterDataID);
		return convertUploadCondition(uploadConditions);
	}
	
	public static Entry<Boolean, Boolean> convertUploadCondition(List<Object> uploadConditions) {
		boolean hasWPAReq = Boolean.FALSE;
		if(uploadConditions != null && uploadConditions.get(0) != null || "Y".equalsIgnoreCase((String)uploadConditions.get(0))) {
			hasWPAReq = Boolean.TRUE;
		}
		
		Boolean hasAccred = Boolean.FALSE;
		if(uploadConditions != null && uploadConditions.get(1) != null || "Y".equalsIgnoreCase((String)uploadConditions.get(1))) {
			hasAccred = Boolean.TRUE;
		}
		
		return new AbstractMap.SimpleEntry<>(hasWPAReq, hasAccred);
	}
	
	public static Entry<Boolean, Boolean> queryUploadColumnForLearnership(int programMasterDataID, String learnerships) {
		String learnerInputID = X_ZZ_Program_Learnerships.COLUMNNAME_ZZ_Learnerships_ID;
		String learnerInputProgramTable = X_ZZ_Program_Learnerships.Table_Name;
		String learnerInputTable = X_ZZ_Learnerships.Table_Name;

		String learnershipType = null;
		if (learnerships == X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_4IRLearnership) {
			learnershipType = X_ZZ_Program_Learnerships.ZZ_LEARNERSHIPS_TYPE_4IR;
		} else if (learnerships == X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_GeneralLearnership) {
			learnershipType = X_ZZ_Program_Learnerships.ZZ_LEARNERSHIPS_TYPE_General;
		} else if (learnerships == X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_AETLearnership) {
			learnershipType = X_ZZ_Program_Learnerships.ZZ_LEARNERSHIPS_TYPE_AET;
		}

		String uploadColumnSql = String.format("""
				SELECT MAX(%s), MAX(%s) 
				FROM %s INNER JOIN %s 
					ON (%s.%s = %s.%s) 
				WHERE %s = ? AND %s.%s = ?
				"""
				, X_ZZ_Program_Learnerships.COLUMNNAME_ZZ_WPA_Req, X_ZZ_Program_Learnerships.COLUMNNAME_ZZ_Is_Accred_SLA_Req 
				, learnerInputProgramTable, learnerInputTable
				, learnerInputProgramTable, learnerInputID, learnerInputTable, learnerInputID
				, X_ZZ_Program_Learnerships.COLUMNNAME_ZZ_Program_Master_Data_ID, learnerInputProgramTable, X_ZZ_Program_Learnerships.COLUMNNAME_ZZ_Learnerships_Type
				);
		List<Object> uploadConditions = DB.getSQLValueObjectsEx(null, uploadColumnSql, programMasterDataID, learnershipType);
		return convertUploadCondition(uploadConditions);
	}
	
	public static List<LearnerInputInfo> queryLearnerInputInfos(int programMasterDataID, boolean isTrade) {
		String learnerInputProgramID;
		String learnerInputID;
		String learnerInputText = X_ZZ_Trade.COLUMNNAME_Name;
		String learnerInputProgramTable;
		String learnerInputTable;
		
		if (isTrade) {
			learnerInputProgramID = X_ZZ_Program_Trade.COLUMNNAME_ZZ_Program_Trade_ID;
			learnerInputID = X_ZZ_Program_Trade.COLUMNNAME_ZZ_Trade_ID;
			learnerInputProgramTable = X_ZZ_Program_Trade.Table_Name;
			learnerInputTable = X_ZZ_Trade.Table_Name;
		} else {
			learnerInputProgramID = X_ZZ_Program_Disciplines.COLUMNNAME_ZZ_Program_Disciplines_ID;
			learnerInputID = X_ZZ_Program_Disciplines.COLUMNNAME_ZZ_Disciplines_ID;
			learnerInputProgramTable = X_ZZ_Program_Disciplines.Table_Name;
			learnerInputTable = X_ZZ_Disciplines.Table_Name;
		}
		
		String sql = String.format("""
				SELECT %s, %s.%s, %s, %s, %s
				FROM %s INNER JOIN %s 
					ON (%s.%s = %s.%s) 
				WHERE %s = ?
				ORDER BY %s
				"""
				, learnerInputProgramID, learnerInputTable, learnerInputID, X_ZZ_Program_Trade.COLUMNNAME_ZZ_WPA_Req, X_ZZ_Program_Trade.COLUMNNAME_ZZ_Is_Accred_SLA_Req, learnerInputText
				, learnerInputProgramTable, learnerInputTable
				, learnerInputProgramTable, learnerInputID, learnerInputTable, learnerInputID
				, X_ZZ_Program_Trade.COLUMNNAME_ZZ_Program_Master_Data_ID
				, X_ZZ_Program_Trade.COLUMNNAME_Line);
		
		return convertLearnerInputInfo(DB.getSQLArrayObjectsEx(null, sql, programMasterDataID));
	}
	
	public static List<LearnerInputInfo> convertLearnerInputInfo (List<List<Object>> learnerInputInfoObjs) {
		List<LearnerInputInfo> learnerInputInfos = new ArrayList<>();
		
		if (learnerInputInfoObjs != null) {
			for (List<Object> learnerInputInfoObj : learnerInputInfoObjs) {
				LearnerInputInfo learnerInputInfo = new LearnerInputInfo(learnerInputInfoObj);
				learnerInputInfos.add(learnerInputInfo);
			}
		}
		
		return learnerInputInfos;
	}
	
	public static List<LearnerInputInfo> queryLearnerInputInfos(int programMasterDataID, String learnerships) {
		
		String learnerInputProgramID = X_ZZ_Program_Learnerships.COLUMNNAME_ZZ_Program_Learnerships_ID;
		String learnerInputID = X_ZZ_Program_Learnerships.COLUMNNAME_ZZ_Learnerships_ID;
		String learnerInputText = X_ZZ_Learnerships.COLUMNNAME_Name;
		String learnerInputProgramTable = X_ZZ_Program_Learnerships.Table_Name;
		String learnerInputTable = X_ZZ_Learnerships.Table_Name;

		String learnershipType = null;
		if (learnerships == X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_4IRLearnership) {
			learnershipType = X_ZZ_Program_Learnerships.ZZ_LEARNERSHIPS_TYPE_4IR;
		} else if (learnerships == X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_GeneralLearnership) {
			learnershipType = X_ZZ_Program_Learnerships.ZZ_LEARNERSHIPS_TYPE_General;
		} else if (learnerships == X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_AETLearnership) {
			learnershipType = X_ZZ_Program_Learnerships.ZZ_LEARNERSHIPS_TYPE_AET;
		}

		String sql = String.format("""
				SELECT %s, %s.%s, %s, %s, %s 
				FROM %s INNER JOIN %s 
					ON (%s.%s = %s.%s) 
				WHERE %s = ? AND %s.%s = ?
				ORDER BY %s
				"""
				, learnerInputProgramID, learnerInputTable, learnerInputID, X_ZZ_Program_Learnerships.COLUMNNAME_ZZ_WPA_Req, X_ZZ_Program_Learnerships.COLUMNNAME_ZZ_Is_Accred_SLA_Req, learnerInputText
				, learnerInputProgramTable, learnerInputTable
				, learnerInputProgramTable, learnerInputID, learnerInputTable, learnerInputID
				, X_ZZ_Program_Learnerships.COLUMNNAME_ZZ_Program_Master_Data_ID, learnerInputProgramTable, X_ZZ_Program_Learnerships.COLUMNNAME_ZZ_Learnerships_Type
				, X_ZZ_Program_Learnerships.COLUMNNAME_Line);
		
		return convertLearnerInputInfo(DB.getSQLArrayObjectsEx(null, sql, programMasterDataID, learnershipType));
	}
	
	public static ProgramInput getDisciplines(int programMasterDataID, X_ZZ_Application_Form applicationForm, String tableTitle){
		Entry<Boolean, Boolean> uploadColCondition = ProgramInput.queryUploadColumnForTrade(programMasterDataID, false);
		
		ProgramInput programInput = ProgramInput.getProgramInput(X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_Discipline, applicationForm, tableTitle, uploadColCondition.getKey(), uploadColCondition.getValue());
		programInput.iniProgram(applicationForm, programMasterDataID, X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_Discipline);
		return programInput;
	}

	public static ProgramInput getTrade(int programMasterDataID, X_ZZ_Application_Form applicationForm, String tableTitle){
		Entry<Boolean, Boolean> uploadColCondition = ProgramInput.queryUploadColumnForTrade(programMasterDataID, true);
		ProgramInput programInput = ProgramInput.getProgramInput(X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_Trade, applicationForm, tableTitle, uploadColCondition.getKey(), uploadColCondition.getValue());
		programInput.iniProgram(applicationForm, programMasterDataID, X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_Trade);
		return programInput;
	}

	public static ProgramInput getLearnership(String learnershipType, X_ZZ_Application_Form applicationForm, int programMasterDataID){
		Entry<Boolean, Boolean> uploadColCondition = ProgramInput.queryUploadColumnForLearnership(programMasterDataID, learnershipType);
		ProgramInput programInput = ProgramInput.getProgramInput(learnershipType, applicationForm, null, uploadColCondition.getKey(), uploadColCondition.getValue());
		programInput.iniProgram(applicationForm, programMasterDataID, learnershipType);
		return programInput;
	}
	
	
	static ColumnInfo<?> colNoEmployed;
	static ColumnInfo<?> colNoUnEmployed;
	static ColumnInfo<?> colNoLearners;
	static {
		colNoEmployed = ColumnInfo.getColPositiveNumber(ColumnInfo.colNoEmployedLabel
				, I_ZZ_FormDiscipline.COLUMNNAME_ZZNoEmployedLearners);
		colNoEmployed.setCalTotal(true);
		
		colNoUnEmployed = ColumnInfo.getColPositiveNumber(ColumnInfo.colNoUnEmployedLabel
				, I_ZZ_FormDiscipline.COLUMNNAME_ZZNoUnEmployedLearners);
		colNoUnEmployed.setCalTotal(true);
		
		colNoLearners = ColumnInfo.getColPositiveNumber(ColumnInfo.colNoLearnersLabel
				, I_ZZ_FormDiscipline.COLUMNNAME_ZZNoLearners);
		colNoLearners.setCalTotal(true);
	}
	
	
	
	private static ProgramInput getProgramInput(String disciplineType, 
			X_ZZ_Application_Form applicationForm, 
			String tableTitle, boolean hasWPAReq, boolean hasAccred) {
		
		List<ColumnInfo<?>> columns = new ArrayList<>();
		ColumnInfo<?> titleCol = null;
		if ((X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_4IRLearnership.equals(disciplineType) ||
				X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_AETLearnership.equals(disciplineType) ||
				X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_GeneralLearnership.equals(disciplineType))) {
			
			titleCol = ColumnInfo.getColLearnerInfo(ColumnInfo.colLearnershipLabel
					, X_ZZ_FormDiscipline.COLUMNNAME_ZZ_Learnerships_ID);
			columns.add(titleCol);
			
			columns.add(colNoEmployed);
			
			columns.add(colNoUnEmployed);
		}else {
			if (X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_Trade.equals(disciplineType)) {
				titleCol = ColumnInfo.getColLearnerInfo(ColumnInfo.colTradeLabel
						, X_ZZ_FormDiscipline.COLUMNNAME_ZZ_Trade_ID);
				columns.add(titleCol);
			}else {
				titleCol = ColumnInfo.getColLearnerInfo(ColumnInfo.colDisciplineLabel
						, X_ZZ_FormDiscipline.COLUMNNAME_ZZ_Disciplines_ID);
				columns.add(titleCol);
			}
			
			columns.add(colNoLearners);
		}
		
		columns.add(ColumnInfo.getColPostal(ColumnInfo.colPostalCodeLabel
				, I_ZZ_FormDiscipline.COLUMNNAME_Postal));
		columns.add(
				ColumnInfo.getColArea(ColumnInfo.colAreaLabel 
						, MasterUtil.getInitCities()
						, I_ZZ_FormDiscipline.COLUMNNAME_C_City_ID));
		
		if (hasWPAReq) {
			columns.add(ColumnInfo.getColFileUpload(ColumnInfo.colWPALabel
					, ColumnInfo.btWPAText
					, I_ZZ_FormDiscipline.COLUMNNAME_ZZ_WPAFile
					, I_ZZ_FormDiscipline.COLUMNNAME_ZZWPAFileName));
		}

		if (hasAccred) {
			columns.add(ColumnInfo.getColFileUpload(ColumnInfo.colAccredLabel
					, ColumnInfo.btAccredText
					, I_ZZ_FormDiscipline.COLUMNNAME_ZZ_AccredFile
					, I_ZZ_FormDiscipline.COLUMNNAME_ZZAccredFileName));
		}
		ProgramInput projectInput = AnnexureInfo.getAnnexureInfo(ProgramInput.class, columns, true);
		projectInput.setCreateNewRowWhenEmpty(false);
		
		BiFunction<AnnexureInfo, X_ZZ_Application_Form, PO> poSupplier = (annexure, appForm) -> {
			X_ZZ_FormDiscipline po = new X_ZZ_FormDiscipline(Env.getCtx(), 0, appForm.get_TrxName());
			po.setZZ_Application_Form_ID(appForm.getZZ_Application_Form_ID());
			return po;
		};
		projectInput.setPoSupplier(poSupplier);
		projectInput.setTableTitle(tableTitle);
		
		return projectInput;	
	}
	
	public void iniProgram(X_ZZ_Application_Form applicationForm, int programMasterDataID, String disciplineType) {
		List<LearnerInputInfo> learnerInputInfos = null;
		if (X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_Discipline.equalsIgnoreCase(disciplineType))
			learnerInputInfos = ProgramInput.queryLearnerInputInfos(programMasterDataID, false);
		else if (X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_Trade.equalsIgnoreCase(disciplineType))
			learnerInputInfos = ProgramInput.queryLearnerInputInfos(programMasterDataID, true);
		else
			learnerInputInfos = ProgramInput.queryLearnerInputInfos(programMasterDataID, disciplineType);
		
		ColumnInfo<?> titleCol = lookupColByDataType(DataType.LearnerInfo, this);
		
		List<Map<ColumnInfo<?>, Object>> titleRows = new ArrayList<>();
		for (LearnerInputInfo learnerInputInfo:learnerInputInfos) {
			Map<ColumnInfo<?>, Object> titleRow = new HashMap<>();
			titleRow.put(titleCol, learnerInputInfo);
			titleRows.add(titleRow);
		}
		
		List<PO> formDisciplines = null;
		if(applicationForm != null) {
			String whereFormDiscipline = String.format("%s = ?", 
					I_ZZ_FormDiscipline.COLUMNNAME_ZZ_Application_Form_ID);
			Query queryFormDiscipline = MTable.get(X_ZZ_FormDiscipline.Table_ID).createQuery(whereFormDiscipline, null);
			formDisciplines = queryFormDiscipline.setParameters(applicationForm.getZZ_Application_Form_ID()).list();
		}
		
		init(applicationForm, formDisciplines, titleRows);
	}
}
