package za.co.ntier.webform.form.bean;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.compiere.model.MCity;

import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.model.X_ZZ_FormDiscipline;

public class LearnerInput extends AnnexureInfo {
	
	private String tableTitle;

	public static LearnerInput getTrade(int programMasterDataID, String tableTitle) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return LearnerInput.getTradeDiscipline(true, programMasterDataID, tableTitle);
	}
	
	public static LearnerInput getDisciplines(int programMasterDataID, String tableTitle) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return LearnerInput.getTradeDiscipline(false, programMasterDataID, tableTitle);
	}
	
	@SuppressWarnings("unchecked")
	private static LearnerInput getTradeDiscipline(boolean isTrade, int programMasterDataID, String tableTitle) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		List<Object> rObjs = MasterUtil.queryLearnerInputInfos(programMasterDataID, isTrade?X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_Trade:X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_Discipline);
		List<LearnerInputInfo> learnerInputInfos = (List<LearnerInputInfo>)rObjs.get(0);
		boolean hasWPAReq = (boolean)rObjs.get(1);
		boolean hasAccred = (boolean)rObjs.get(2);
		
		List<ColumnInfo<?>> columns = new ArrayList<>();
		columns.add(ColumnInfo.getColLabel("Trade"));
		columns.add(ColumnInfo.getColPositiveNumber("No. of Learners"));
		columns.add(ColumnInfo.getColPositiveNumber("Site Postal Code"));
		
		ColumnInfo<MCity> areaCol = ColumnInfo.getColArea("Area");
		areaCol.setDataProvider(MasterUtil.getCities().stream().limit(MasterUtil.limitItem).toList());		
		columns.add(areaCol);
		
		if (hasWPAReq) {
			columns.add(ColumnInfo.getColFileUpload("WPA", "WPA"));
		}
		
		if (hasAccred) {
			columns.add(ColumnInfo.getColFileUpload("Accreditation", "Accred./SLA"));
		}
		
		LearnerInput learnerInput = AnnexureInfo.getAnnexureInfo(LearnerInput.class, 
				columns, true);
		learnerInput.setTableTitle(tableTitle);
		
		Map<ColumnInfo<?>, Object> rowDataInits = new HashMap<>();
		for (LearnerInputInfo learnerInputInfo : learnerInputInfos) {
			rowDataInits.put(columns.get(0), learnerInputInfo.getLearnerInputText());
			Map<ColumnInfo<?>, Object> newRow = AnnexureInfo.createDetailRow(columns, rowDataInits);
			learnerInput.getRows().add(newRow);
		}
		return learnerInput;
	}

	/**
	 * @return the tableTitle
	 */
	public String getTableTitle() {
		return tableTitle;
	}

	/**
	 * @param tableTitle the tableTitle to set
	 */
	public void setTableTitle(String tableTitle) {
		this.tableTitle = tableTitle;
	}

	
}
