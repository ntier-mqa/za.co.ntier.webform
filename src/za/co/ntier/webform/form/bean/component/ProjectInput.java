package za.co.ntier.webform.form.bean.component;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import za.co.ntier.webform.form.MasterUtil;

public class ProjectInput extends AnnexureInfo {
	public static final String colNameProgrammeLabel = "Name of Programme";
	public static final String colNoEmployedLabel = "No. of Employed Learners";
	public static final String colNoLearnersLable = "No of Learners applied for";
	public static final String colNoUnEmployedLabel =  "No. of Unemployed Learners";
	public static final String colTotalLearnersLabel = "Total No. of Learners Applied For";
	
	
	public static ProjectInput getProject(List<ColumnInfo<?>> initColumnInfos) throws NoSuchMethodException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return ProjectInput.getProject(null, initColumnInfos);
	}

	public static ProjectInput getProject(List<ColumnInfo<?>> initColumnInfos, String rowTitle) throws NoSuchMethodException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		List<ColumnInfo<?>> columnInfos = new ArrayList<>(initColumnInfos);
		columnInfos.add(ColumnInfo.getColPostal(ProgramInput.colPostalCodeLabel));
		columnInfos.add(
				ColumnInfo.getColArea(ProgramInput.colAreaLabel, MasterUtil.getInitCities()));
		return AnnexureInfo.getAnnexureInfoOneLine(ProjectInput.class, null, columnInfos, rowTitle, false, null);
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
		columnInfos.add(ColumnInfo.getColPostal(ProgramInput.colPostalCodeLabel));
		columnInfos.add(
				ColumnInfo.getColArea(ProgramInput.colAreaLabel, MasterUtil.getInitCities()));
		return AnnexureInfo.getAnnexureInfoOneLine(ProjectInput.class, secctionTitle, columnInfos, null, false, null);

	}
}
