package za.co.ntier.webform.form.bean.component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.adempiere.webui.exception.ApplicationException;
import org.apache.commons.lang3.StringUtils;
import org.compiere.model.MCity;
import org.compiere.model.MTable;
import org.compiere.model.Query;

import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.bean.DataType;
import za.co.ntier.webform.model.I_ZZLearnersApplied;
import za.co.ntier.webform.model.I_ZZ_Application_Form;
import za.co.ntier.webform.model.X_ZZLearnersApplied;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class ProjectInput extends AnnexureInfo {
	public static final String colNameProgrammeLabel = "Name of Programme";
	public static final String colNoEmployedLabel = "No. of Employed Learners";
	public static final String colNoLearnersLable = "No of Learners applied for";
	public static final String colNoUnEmployedLabel =  "No. of Unemployed Learners";
	public static final String colTotalLearnersLabel = "Total No. of Learners Applied For";
	
	
	public static ProjectInput getProject(List<ColumnInfo<?>> initColumnInfos, X_ZZ_Application_Form applicationForm) {
		return ProjectInput.getProject(initColumnInfos, applicationForm, null, null);
	}

	public static ProjectInput getProject(List<ColumnInfo<?>> initColumnInfos, String rowTitle, X_ZZ_Application_Form applicationForm) {
		return getProject(initColumnInfos, applicationForm, null, rowTitle);
	}
	
	
	public static ProjectInput getProject(String secctionTitle, List<ColumnInfo<?>> initColumnInfos, X_ZZ_Application_Form applicationForm) {
		return getProject(initColumnInfos, applicationForm, secctionTitle, null);
	}
	/**
	 * no row title, no total, has section title
	 * 
	 * @param secctionTitle
	 * @param initColumnInfos
	 * @return
	 */
	public static ProjectInput getProject(List<ColumnInfo<?>> initColumnInfos, X_ZZ_Application_Form applicationForm, String secctionTitle, String rowTitle){
		List<ColumnInfo<?>> columnInfos = new ArrayList<>(initColumnInfos);
		columnInfos.add(ColumnInfo.getColPostal(ProgramInput.colPostalCodeLabel));
		columnInfos.add(
				ColumnInfo.getColArea(ProgramInput.colAreaLabel, MasterUtil.getInitCities()));
		
		
		ProjectInput projectInput = AnnexureInfo.getAnnexureInfo(ProjectInput.class, columnInfos, false);
		Function<AnnexureInfo, AnnexureRow<?>> supplierRowAppForm = (parent) -> new AnnexureRow<X_ZZLearnersApplied>(parent);
		projectInput.setSupplier(supplierRowAppForm);
		
		projectInput.setSectionHeader(secctionTitle);
		
		@SuppressWarnings("unchecked")
		AnnexureRow<X_ZZLearnersApplied> row = (AnnexureRow<X_ZZLearnersApplied>)projectInput.createDetailRow();
		
		X_ZZLearnersApplied learnersApplied = null;
		if (applicationForm != null) {
			String whereLearnersApplied = String.format("%s = ?", I_ZZ_Application_Form.COLUMNNAME_ZZ_Application_Form_ID);
			Query queryLearnersApplied = MTable.get(I_ZZLearnersApplied.Table_ID).createQuery(whereLearnersApplied, null);
			learnersApplied = queryLearnersApplied.setParameters(applicationForm.getZZ_Application_Form_ID()).first();
		}
		
		row.setData(learnersApplied);
		
		ColumnInfo<?> colNameProgramme = AnnexureInfo.lookupColByTitle(ProjectInput.colNameProgrammeLabel, projectInput);
		ColumnInfo<?> colNoEmployed = AnnexureInfo.lookupColByTitle(ProjectInput.colNoEmployedLabel, projectInput);
		ColumnInfo<?> colNoUnEmployed = AnnexureInfo.lookupColByTitle(ProjectInput.colNoUnEmployedLabel, projectInput);
		ColumnInfo<?> colNoLearners = AnnexureInfo.lookupColByTitle(ProjectInput.colNoLearnersLable, projectInput);
		
		ColumnInfo<?> colTotalLearnersInput = AnnexureInfo.lookupCol(DataType.PositiveNumber, ProjectInput.colTotalLearnersLabel, projectInput);
		ColumnInfo<?> colTotalLearnersLabel = AnnexureInfo.lookupCol(DataType.Label, ProjectInput.colTotalLearnersLabel, projectInput);
		
		ColumnInfo<?> areaColl = AnnexureInfo.lookupColByDataType(DataType.Area, projectInput);
		ColumnInfo<?> postalColl = AnnexureInfo.lookupColByDataType(DataType.Postal, projectInput);
		ColumnInfo<?> wpaColl = AnnexureInfo.lookupColByTitle(ProgramInput.colWPALabel, projectInput);
		
		if (columnInfos.get(0).getDataType() == DataType.Label && rowTitle != null) {
			((LabelData)row.get(columnInfos.get(0))).setValue(rowTitle);
		}

		if (colNameProgramme != null && learnersApplied != null && StringUtils.isNoneBlank(learnersApplied.getName())) {
			row.put(colNameProgramme, learnersApplied.getName());
		}
		
		if (colNoEmployed != null && learnersApplied != null && learnersApplied.getZZNoEmployedLearners() > 0) {
			((IntData)row.get(colNoEmployed)).setValue(learnersApplied.getZZNoEmployedLearners());
		}
		
		if (colNoUnEmployed != null && learnersApplied != null && learnersApplied.getZZNoUnEmployedLearners() > 0) {
			((IntData)row.get(colNoUnEmployed)).setValue(learnersApplied.getZZNoUnEmployedLearners());
		}
		
		if (colNoLearners != null && learnersApplied != null && learnersApplied.getZZNoLearners() > 0) {
			((IntData)row.get(colNoLearners)).setValue(learnersApplied.getZZNoLearners());
		}
		
		if (colTotalLearnersInput != null && learnersApplied != null && learnersApplied.getZZNoTotalLearners() > 0) {
			((IntData)row.get(colTotalLearnersInput)).setValue(learnersApplied.getZZNoTotalLearners());
		}
	
		if(postalColl != null && learnersApplied != null && StringUtils.isNoneBlank(learnersApplied.getPostal())) {
			PostalData postal = (PostalData)row.get(postalColl);
			postal.setPostalInternal(learnersApplied.getPostal());
		}

		if(areaColl != null && learnersApplied != null && learnersApplied.getC_City_ID() > 0) {
			AreaData areaData = (AreaData)row.get(areaColl);
			for(MCity area : areaData.getDataProvider()) {
				if (area.getC_City_ID() == learnersApplied.getC_City_ID()) {
					areaData.setSelectedAreaInternal(area);
				}
			}
		}
		
		row.initTotalCol();
		
		return projectInput;		

	}
	
	@SuppressWarnings("unchecked")
	public static void saveProjectInput(String trxName, X_ZZ_Application_Form applicationForm, ProjectInput projectInput) {
		ColumnInfo<?> colNameProgramme = AnnexureInfo.lookupColByTitle(ProjectInput.colNameProgrammeLabel, projectInput);
		ColumnInfo<?> colNoEmployed = AnnexureInfo.lookupColByTitle(ProjectInput.colNoEmployedLabel, projectInput);
		ColumnInfo<?> colNoUnEmployed = AnnexureInfo.lookupColByTitle(ProjectInput.colNoUnEmployedLabel, projectInput);
		ColumnInfo<?> colNoLearners = AnnexureInfo.lookupColByTitle(ProjectInput.colNoLearnersLable, projectInput);
		
		ColumnInfo<?> colTotalLearnersInput = AnnexureInfo.lookupCol(DataType.PositiveNumber, ProjectInput.colTotalLearnersLabel, projectInput);
		
		ColumnInfo<?> areaColl = AnnexureInfo.lookupColByDataType(DataType.Area, projectInput);
		ColumnInfo<?> postalColl = AnnexureInfo.lookupColByDataType(DataType.Postal, projectInput);
		ColumnInfo<?> wpaColl = AnnexureInfo.lookupColByTitle(ProgramInput.colWPALabel, projectInput);
		
		AnnexureRow<X_ZZLearnersApplied> row = (AnnexureRow<X_ZZLearnersApplied>)projectInput.getRows().get(0);
		X_ZZLearnersApplied learnersApplied = row.getData();
		if (learnersApplied == null) {
			learnersApplied = new X_ZZLearnersApplied(null, 0, trxName);
		}
		
		boolean hasData = false;
		int total = 0;
		Integer cellData = AnnexureInfo.getIntegerValue(row, colNoEmployed);
		if (cellData != null && cellData != 0) {
			learnersApplied.setZZNoEmployedLearners(cellData);
			hasData = true;
			total += cellData;
		}
		
		cellData = AnnexureInfo.getIntegerValue(row, colNoUnEmployed);
		if (cellData != null && cellData != 0) {
			learnersApplied.setZZNoUnEmployedLearners(cellData);
			hasData = true;
			total += cellData;
		}
		
		cellData = AnnexureInfo.getIntegerValue(row, colNoLearners);
		if (cellData != null && cellData != 0) {
			learnersApplied.setZZNoLearners(cellData);
			hasData = true;
			total += cellData;
		}
		
		cellData = AnnexureInfo.getIntegerValue(row, colTotalLearnersInput);
		if (cellData != null && cellData != 0) {
			learnersApplied.setZZNoTotalLearners(cellData);
			hasData = true;
			total += cellData;
		}
		
		String rowTitle = (String)row.get(colNameProgramme);
		if (rowTitle != null) {
			learnersApplied.setName(rowTitle);
		}else if (projectInput.getSubSectionHeader() != null) {
			learnersApplied.setName(projectInput.getSubSectionHeader());
		}else if (projectInput.getSectionHeader() != null) {
			learnersApplied.setName(projectInput.getSectionHeader());
		}else {
			learnersApplied.setName("Project");
		}
		
		UploadData wpaUploadInfo = (UploadData)row.get(wpaColl);		
		if (wpaUploadInfo != null && StringUtils.isNoneEmpty(wpaUploadInfo.getFullPath())) {
			try {
				learnersApplied.setZZ_WPAFile(Files.readAllBytes(Paths.get(wpaUploadInfo.getFullPath())));
			} catch (IOException e) {
				e.printStackTrace();
				throw new ApplicationException(e.getMessage(), e);
			} 
		}
		
		if (hasData) {
			learnersApplied.setZZ_Application_Form_ID(applicationForm.getZZ_Application_Form_ID());
			
			MCity area = ((AreaData)row.get(areaColl)).getSelectedArea();
			if (area != null)
				learnersApplied.setC_City_ID(area.getC_City_ID());
			
			PostalData postalCode = (PostalData)row.get(postalColl);
			if (postalCode.getPostal() != null)
				learnersApplied.setPostal(postalCode.getPostal());
			
			learnersApplied.saveEx(trxName);
		}
		
		applicationForm.setZZTotalNumberApplied(total + applicationForm.getZZTotalNumberApplied());
	}
}
