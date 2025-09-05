package za.co.ntier.webform.form.bean.component;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.compiere.model.MCity;

import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.bean.DataType;
import za.co.ntier.webform.model.X_ZZLearnersApplied;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

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
	
	public static void saveProjectInput(String trxName, X_ZZ_Application_Form applicationForm, ProjectInput projectInput) {
		ColumnInfo<?> colProgramme = AnnexureInfo.lookupColByTitle(ProjectInput.colNameProgrammeLabel, projectInput);
		ColumnInfo<?> colNoEmployed = AnnexureInfo.lookupColByTitle(ProjectInput.colNoEmployedLabel, projectInput);
		ColumnInfo<?> colNoUnEmployed = AnnexureInfo.lookupColByTitle(ProjectInput.colNoUnEmployedLabel, projectInput);
		ColumnInfo<?> colNoLearners = AnnexureInfo.lookupColByTitle(ProjectInput.colNoLearnersLable, projectInput);
		ColumnInfo<?> colTotalLearners = AnnexureInfo.lookupColByTitle(ProjectInput.colTotalLearnersLabel, projectInput);
		
		ColumnInfo<?> areaColl = AnnexureInfo.lookupColByDataType(DataType.Area, projectInput);
		ColumnInfo<?> postalColl = AnnexureInfo.lookupColByDataType(DataType.Postal, projectInput);
		
		X_ZZLearnersApplied learnersApplied = new X_ZZLearnersApplied(null, 0, trxName);
		Map<ColumnInfo<?>, Object> row = projectInput.getRows().get(0);
		
		boolean hasData = false;

		Integer cellData = AnnexureInfo.getIntegerValue(row, colNoEmployed);
		if (cellData != null && cellData != 0) {
			learnersApplied.setZZNoEmployedLearners(cellData);
			hasData = true;
		}
		
		cellData = AnnexureInfo.getIntegerValue(row, colNoUnEmployed);
		if (cellData != null && cellData != 0) {
			learnersApplied.setZZNoUnEmployedLearners(cellData);
			hasData = true;
		}
		
		cellData = AnnexureInfo.getIntegerValue(row, colNoLearners);
		if (cellData != null && cellData != 0) {
			learnersApplied.setZZNoLearners(cellData);
			hasData = true;
		}
		
		cellData = AnnexureInfo.getIntegerValue(row, colTotalLearners);
		if (cellData != null && cellData != 0) {
			learnersApplied.setZZNoTotalLearners(cellData);
			hasData = true;
		}
		
		String rowTitle = (String)row.get(colProgramme);
		if (rowTitle != null) {
			learnersApplied.setName(rowTitle);
			hasData = true;
		}
		
		if (hasData) {
			learnersApplied.setZZ_Application_Form_ID(applicationForm.getZZ_Application_Form_ID());
			
			MCity area = ((AreaData)row.get(areaColl)).getSelectedArea();
			if (area != null)
				learnersApplied.setC_City_ID(area.getC_City_ID());
			
			if (learnersApplied.getName() == null) {
				learnersApplied.setName("name");
			}
			
			PostalData postalCode = (PostalData)row.get(postalColl);
			if (postalCode.getPostal() != null)
				learnersApplied.setPostal(postalCode.getPostal());
			
			learnersApplied.saveEx(trxName);
		}
	}
}
