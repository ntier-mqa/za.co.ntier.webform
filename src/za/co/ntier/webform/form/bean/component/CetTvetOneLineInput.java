package za.co.ntier.webform.form.bean.component;

import java.util.List;

public class CetTvetOneLineInput extends AnnexureInfo{
	public static final String colDisciplineTitle = "Discipline Applying For";
	public static final String colNoBeneficiariesTitle = "Number Of Beneficiaries Applying For";
	public static final String colProgrammeApplyTitle = "Programme Applying For";
	public static final String colTotalNoBeneficiariesTitle = "Total Number of beneficiaries applying for";
	/**
	 * for CetTvet master, show row title but not total
	 *
	 * @param sectionHeader
	 * @param columnInfos
	 * @param rowTitle
	 * @return
	 */
	public static CetTvetOneLineInput getAnnexureInfoOneLine(String sectionHeader,
			List<ColumnInfo<?>> columnInfos, String rowTitle) {
		return AnnexureInfo.getAnnexureInfoOneLine(CetTvetOneLineInput.class, sectionHeader, columnInfos, rowTitle, false, null);
	}

	public static CetTvetOneLineInput getCetTvetOneLineInput(String sectionHeader,
			List<ColumnInfo<?>> columnInfos, String rowTitle, List<String> twoTitleValue) {
		return AnnexureInfo.getAnnexureInfoOneLine(CetTvetOneLineInput.class, sectionHeader, columnInfos, rowTitle, false, twoTitleValue);
	}

}
