package za.co.ntier.webform.form.bean.component;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import za.co.ntier.webform.form.MasterUtil;
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

		Map<ColumnInfo<?>, Object> rowDataInits = new HashMap<>();
		for (LearnerInputInfo learnerInputInfo : learnerInputInfos) {
			rowDataInits.put(columns.get(0), learnerInputInfo);
			Map<ColumnInfo<?>, Object> newRow = programInput.createDetailRow(columns, rowDataInits);
			programInput.getRows().add(newRow);
		}
		return programInput;
	}
}
