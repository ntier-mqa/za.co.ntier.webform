package za.co.ntier.webform.form.bean;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.model.X_ZZ_FormDiscipline;

public class ProgramInput extends AnnexureInfo {

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
		columns.add(ColumnInfo.getColLabel("Learnership Type"));
		columns.add(ColumnInfo.getColPositiveNumber("No. of employed Learners"));
		columns.add(ColumnInfo.getColPositiveNumber("No. of Unemployed Learners"));

		columns.add(
				ColumnInfo.getColArea("Area", MasterUtil.getCities().stream().limit(MasterUtil.limitItem).toList()));

		if (hasWPAReq) {
			columns.add(ColumnInfo.getColFileUpload("WPA", "WPA"));
		}

		if (hasAccred) {
			columns.add(ColumnInfo.getColFileUpload("Accreditation", "Accred./SLA"));
		}

		ProgramInput programInput = AnnexureInfo.getAnnexureInfo(ProgramInput.class, columns, true);

		Map<ColumnInfo<?>, Object> rowDataInits = new HashMap<>();
		for (LearnerInputInfo learnerInputInfo : learnerInputInfos) {
			rowDataInits.put(columns.get(0), learnerInputInfo.getLearnerInputText());
			Map<ColumnInfo<?>, Object> newRow = AnnexureInfo.createDetailRow(columns, rowDataInits);
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
		columns.add(ColumnInfo.getColLabel("Trade"));
		columns.add(ColumnInfo.getColPositiveNumber("No. of Learners"));
		columns.add(ColumnInfo.getColPositiveNumber("Site Postal Code"));

		columns.add(
				ColumnInfo.getColArea("Area", MasterUtil.getCities().stream().limit(MasterUtil.limitItem).toList()));

		if (hasWPAReq) {
			columns.add(ColumnInfo.getColFileUpload("WPA", "WPA"));
		}

		if (hasAccred) {
			columns.add(ColumnInfo.getColFileUpload("Accreditation", "Accred./SLA"));
		}

		ProgramInput programInput = AnnexureInfo.getAnnexureInfo(ProgramInput.class, columns, true);
		programInput.setTableTitle(tableTitle);

		Map<ColumnInfo<?>, Object> rowDataInits = new HashMap<>();
		for (LearnerInputInfo learnerInputInfo : learnerInputInfos) {
			rowDataInits.put(columns.get(0), learnerInputInfo.getLearnerInputText());
			Map<ColumnInfo<?>, Object> newRow = AnnexureInfo.createDetailRow(columns, rowDataInits);
			programInput.getRows().add(newRow);
		}
		return programInput;
	}
}
