package za.co.ntier.webform.form.bean.component;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import za.co.ntier.webform.form.MasterUtil;

public class ProjectInput extends AnnexureInfo {
	public static ProjectInput getProject(List<ColumnInfo<?>> initColumnInfos) throws NoSuchMethodException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return ProjectInput.getProject(null, initColumnInfos);
	}

	/**
	 * no row title, no total, has section title
	 * 
	 * @param secctionTitle
	 * @param initColumnInfos
	 * @return
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static ProjectInput getProject(String secctionTitle, List<ColumnInfo<?>> initColumnInfos)
			throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		List<ColumnInfo<?>> columnInfos = new ArrayList<>(initColumnInfos);
		columnInfos.add(ColumnInfo.getColPostal(ProgramInput.PostalCodeLabel));
		columnInfos.add(
				ColumnInfo.getColArea(ProgramInput.AreaTitle, MasterUtil.getCities().stream().limit(MasterUtil.limitItem).toList()));
		return AnnexureInfo.getAnnexureInfoOneLine(ProjectInput.class, secctionTitle, columnInfos, null, false, null);

	}
}
