package za.co.ntier.webform.form.bean.component;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class CetTvetOneLineInput extends AnnexureInfo{
	public static final String colBeneficiaries = "Number Of Beneficiaries Applying For";
	public static final String colTotalBeneficiaries = "Total Number of beneficiaries applying for";
	public static final String colDiscipline = "Discipline Applying For";
	public static final String colProgramme = "Programme Applying For";
	public static CetTvetOneLineInput getCetTvetOneLineInput(String sectionHeader,
			List<ColumnInfo<?>> columnInfos, String rowTitle, List<String> twoTitleValue) throws NoSuchMethodException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return AnnexureInfo.getAnnexureInfoOneLine(CetTvetOneLineInput.class, sectionHeader, columnInfos, rowTitle, false, twoTitleValue);
	}

	/**
	 * for CetTvet master, show row title but not total
	 *
	 * @param sectionHeader
	 * @param columnInfos
	 * @param rowTitle
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws NoSuchMethodException
	 */
	public static CetTvetOneLineInput getAnnexureInfoOneLine(String sectionHeader,
			List<ColumnInfo<?>> columnInfos, String rowTitle) throws NoSuchMethodException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return AnnexureInfo.getAnnexureInfoOneLine(CetTvetOneLineInput.class, sectionHeader, columnInfos, rowTitle, false, null);
	}

}
