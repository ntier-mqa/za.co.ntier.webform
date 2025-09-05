package za.co.ntier.webform.form.bean.component;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.model.X_ZZ_FormDiscipline;

public class ProgramInput extends AnnexureInfo {
	public static final String TradeLabel = "Trade";
	public static final String DisciplineLabel = "Discipline";
	public static final String LearnershipLabel = "Learnership Type";
	public static final String PostalCodeLabel = "Site Postal Code";
	public static final String NumEmployedLearnersTitle = "No. of Employed Learners";
	public static final String NumUnEmployedLearnersTitle = "No. of Unemployed Learners";
	public static final String NumLearnersTitle = "No. of Learners";
	public static final String WPATitle = "WPA";
	public static final String WPAButtonText = "WPA";
	public static final String AccredTitle = "Accreditation";
	public static final String AccredButtonText = "Accred./SLA";
	public static final String AreaTitle = "Area";
	
	
	public static ProgramInput getDisciplines(int programMasterDataID, String tableTitle) throws NoSuchMethodException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return ProgramInput.getTradeDiscipline(false, programMasterDataID, tableTitle);
	}

	@SuppressWarnings("unchecked")
	public static ProgramInput getLearnership(String learnershipType, int programMasterDataID)
			throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		List<Object> rObjs = MasterUtil.queryLearnerInputInfos(programMasterDataID, learnershipType);
		List<LearnerInputInfo> learnerInputInfos = (List<LearnerInputInfo>) rObjs.get(0);
		boolean hasWPAReq = (boolean) rObjs.get(1);
		boolean hasAccred = (boolean) rObjs.get(2);

		List<ColumnInfo<?>> columns = new ArrayList<>();
		columns.add(ColumnInfo.getColLearnerInfo(LearnershipLabel));
		columns.add(ColumnInfo.getColPositiveNumber(NumEmployedLearnersTitle));
		columns.add(ColumnInfo.getColPositiveNumber(NumUnEmployedLearnersTitle));
		columns.add(ColumnInfo.getColPostal(PostalCodeLabel));

		columns.add(
				ColumnInfo.getColArea(AreaTitle, MasterUtil.getInitCities()));

		if (hasWPAReq) {
			columns.add(ColumnInfo.getColFileUpload(WPATitle, WPAButtonText));
		}

		if (hasAccred) {
			columns.add(ColumnInfo.getColFileUpload(AccredTitle, AccredButtonText));
		}

		ProgramInput programInput = AnnexureInfo.getAnnexureInfo(ProgramInput.class, columns, true);

		Map<ColumnInfo<?>, Object> rowDataInits = new HashMap<>();
		for (LearnerInputInfo learnerInputInfo : learnerInputInfos) {
			rowDataInits.put(columns.get(0), learnerInputInfo);
			Map<ColumnInfo<?>, Object> newRow = programInput.createDetailRow(columns, rowDataInits);
			programInput.getRows().add(newRow);
		}
		return programInput;
	}

	public static ProgramInput getTrade(int programMasterDataID, String tableTitle) throws NoSuchMethodException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return ProgramInput.getTradeDiscipline(true, programMasterDataID, tableTitle);
	}

	@SuppressWarnings("unchecked")
	private static ProgramInput getTradeDiscipline(boolean isTrade, int programMasterDataID, String tableTitle)
			throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		List<Object> rObjs = MasterUtil.queryLearnerInputInfos(programMasterDataID,
				isTrade ? X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_Trade
						: X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_Discipline);
		List<LearnerInputInfo> learnerInputInfos = (List<LearnerInputInfo>) rObjs.get(0);
		boolean hasWPAReq = (boolean) rObjs.get(1);
		boolean hasAccred = (boolean) rObjs.get(2);

		List<ColumnInfo<?>> columns = new ArrayList<>();
		columns.add(ColumnInfo.getColLearnerInfo(isTrade ? TradeLabel : DisciplineLabel));
		columns.add(ColumnInfo.getColPositiveNumber(NumLearnersTitle));
		columns.add(ColumnInfo.getColPostal(PostalCodeLabel));

		columns.add(
				ColumnInfo.getColArea(AreaTitle, MasterUtil.getInitCities()));

		if (hasWPAReq) {
			columns.add(ColumnInfo.getColFileUpload(WPATitle, "WPA"));
		}

		if (hasAccred) {
			columns.add(ColumnInfo.getColFileUpload(AccredTitle, "Accred./SLA"));
		}

		ProgramInput programInput = AnnexureInfo.getAnnexureInfo(ProgramInput.class, columns, true);
		programInput.setTableTitle(tableTitle);

		Map<ColumnInfo<?>, Object> rowDataInits = new HashMap<>();
		for (LearnerInputInfo learnerInputInfo : learnerInputInfos) {
			rowDataInits.put(columns.get(0), learnerInputInfo);
			Map<ColumnInfo<?>, Object> newRow = programInput.createDetailRow(columns, rowDataInits);
			programInput.getRows().add(newRow);
		}
		return programInput;
	}
}
