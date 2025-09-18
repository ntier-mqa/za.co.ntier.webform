package za.co.ntier.webform.form.bean.component;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Function;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.webui.exception.ApplicationException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.compiere.model.MCity;
import org.compiere.model.MTable;
import org.compiere.model.Query;
import org.compiere.util.CLogger;

import com.akunagroup.uk.postcode.AddressLookup;

import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.Util;
import za.co.ntier.webform.form.bean.DataType;
import za.co.ntier.webform.model.I_ZZLearnersApplied;
import za.co.ntier.webform.model.I_ZZ_Application_Form;
import za.co.ntier.webform.model.X_ZZLearnersApplied;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class ProjectInput extends AnnexureInfo {
	
	public static ProjectInput getProject(List<ColumnInfo<?>> initColumnInfos) {
		return ProjectInput.getProject(initColumnInfos, null);
	}
	
	/**
	 * no row title, no total, has section title
	 * 
	 * @param secctionTitle
	 * @param initColumnInfos
	 * @return
	 */
	public static ProjectInput getProject(List<ColumnInfo<?>> initColumnInfos, String secctionTitle){
		List<ColumnInfo<?>> columnInfos = new ArrayList<>(initColumnInfos);
		columnInfos.add(ColumnInfo.getColPostal(ColumnInfo.colPostalCodeLabel, I_ZZLearnersApplied.COLUMNNAME_Postal));
		columnInfos.add(
				ColumnInfo.getColArea(ColumnInfo.colAreaLabel, MasterUtil.getInitCities(), I_ZZLearnersApplied.COLUMNNAME_C_City_ID));
				
		ProjectInput projectInput = AnnexureInfo.getAnnexureInfo(ProjectInput.class, columnInfos, false);
		Function<AnnexureInfo, AnnexureRow<?>> supplierRowAppForm = (parent) -> new AnnexureRow<X_ZZLearnersApplied>(parent);
		projectInput.setSupplier(supplierRowAppForm);
		
		projectInput.setSectionHeader(secctionTitle);
		
		return projectInput;		

	}
	
	public static void initProject(ProjectInput projectInput, X_ZZ_Application_Form applicationForm) {
		initProject(projectInput, applicationForm, null, null);
	}
	
	public static void initProject(ProjectInput projectInput, X_ZZ_Application_Form applicationForm, List<Map<ColumnInfo<?>, Object>> rowTitles) {
		if (rowTitles == null || rowTitles.size() == 0) {
			initProject(projectInput, applicationForm, null, null);
		}else {
			initProject(projectInput, applicationForm, rowTitles, rowTitles.get(0).keySet());
		}
		
	}
	
	public static void initProject(ProjectInput projectInput, X_ZZ_Application_Form applicationForm, List<Map<ColumnInfo<?>, Object>> rowTitles, Collection<ColumnInfo<?>> keyColumns) {
		List<X_ZZLearnersApplied> learnersApplieds = null;
		
		// init rows with rowTitles
		if (rowTitles != null)
			for (Map<ColumnInfo<?>, Object> rowTitle : rowTitles) {
				@SuppressWarnings("unchecked")
				AnnexureRow<X_ZZLearnersApplied> row = (AnnexureRow<X_ZZLearnersApplied>)projectInput.createDetailRow();
				
				for (Entry<ColumnInfo<?>, Object> colTile : rowTitle.entrySet()) {
					setCellValue(row, colTile.getKey(), colTile.getValue());
				}
			}
		
		
		// get saved data
		if (applicationForm != null) {
			String whereLearnersApplied = null;
			if(projectInput.getDataType() == null) {
				whereLearnersApplied = String.format("%s = ?", I_ZZ_Application_Form.COLUMNNAME_ZZ_Application_Form_ID);
			}else {
				whereLearnersApplied = String.format("%s = ? AND %s = ?", 
						I_ZZLearnersApplied.COLUMNNAME_ZZ_Application_Form_ID,
						I_ZZLearnersApplied.COLUMNNAME_DataType);
			}

			Query queryLearnersApplied = MTable.get(I_ZZLearnersApplied.Table_ID).createQuery(whereLearnersApplied, null);
			if(projectInput.getDataType() == null) {
				queryLearnersApplied.setParameters(applicationForm.getZZ_Application_Form_ID());
			}else {
				queryLearnersApplied.setParameters(applicationForm.getZZ_Application_Form_ID(), projectInput.getDataType());
			}
			
			learnersApplieds = queryLearnersApplied.list();
		}
		
		// init rows with saved data
		if (learnersApplieds != null && learnersApplieds.size() > 0) {
			for (X_ZZLearnersApplied dao : learnersApplieds) {
				boolean isMatching = false;
				for (AnnexureRow<?> row : projectInput.getRows()) {
					isMatching = isMatchingRow(row, dao, keyColumns);
					if (isMatching) {
						fillRowDataFromDao(projectInput.getColumnInfos(), row, dao);
						break;
					}
				}
				
				if(!isMatching) {
					AnnexureRow<?> row = projectInput.createDetailRow();
					fillRowDataFromDao(projectInput.getColumnInfos(), row, dao);
				}
			}
			
		}
		
		if (projectInput.getRows().size() == 0) {
			projectInput.createDetailRow();
		}
		
		projectInput.updateExpressionCol();
	}
	
	/**
	 * check each column to get matching property of bean, query data of bean and set to row 
	 * @param cols
	 * @param row
	 * @param dao
	 */
	private static void fillRowDataFromDao(List<ColumnInfo<?>> cols, AnnexureRow<?> row, Object dao) {
		for (ColumnInfo<?> col : cols) {
			if (StringUtils.isNotBlank(col.getDaoPropertyName())) {
				Object daoValue = getDaoValue(dao, col.getDaoPropertyName());
				setCellValue(row, col, daoValue);
				
			}
			
		}
	}

	/**
	 * 
	 * @param row
	 * @param dao
	 * @param keyColumns
	 * @return
	 */
	private static boolean isMatchingRow(AnnexureRow<?> row, Object dao, Collection<ColumnInfo<?>> keyColumns) {
		boolean isMatching = true;
		for (ColumnInfo<?> keyColumn : keyColumns) {
			if (StringUtils.isNotBlank(keyColumn.getDaoPropertyName())) {
				Entry<Object, Boolean> result = getCellValue(row, keyColumn);
				Object daoValue = getDaoValue(dao, keyColumn.getDaoPropertyName());
				isMatching = Objects.equals(result.getKey(), daoValue);
				if (!isMatching)
					break;
			}else {
				log.warning(String.format("DaoPropertyName of Key column is blank: %s", keyColumn.getTitle()));
			}
		}
		return isMatching;
	}

	private static Object getDaoValue(Object dao, String propertyName) {
		try {
			return PropertyUtils.getSimpleProperty(dao, propertyName);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
			throw new AdempiereException(e.getMessage(), e);
		}
		
	}
	
	private static void setDaoValue(Object dao, String propertyName, Object value) {
		try {
			PropertyUtils.setSimpleProperty(dao, propertyName, value);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
			throw new AdempiereException(e.getMessage(), e);
		}
	}
	

	@SuppressWarnings("unchecked")
	/**
	* return entry with value already convert (example 0 for non input int), value is true in case input non-null
	*/
	private static <T> Entry<T, Boolean> getCellValue(AnnexureRow<?> row, ColumnInfo<?> col) {
		Object valueObj = row.get(col);
		
		if (col.getDataType() == DataType.Area) {
			AreaData areaData = (AreaData)valueObj;
			MCity area = areaData.getSelectedArea();
			if (area == null)
				return new AbstractMap.SimpleEntry<>((T)Integer.valueOf(0), Boolean.FALSE);
			else
				return new AbstractMap.SimpleEntry<>((T)Integer.valueOf(area.getC_City_ID()), Boolean.TRUE);
				
		}else if (col.getDataType() == DataType.PositiveNumber) {
			IntData intData = (IntData)valueObj;
			if (intData.getValue() == null) {
				return new AbstractMap.SimpleEntry<>((T)Integer.valueOf(0), Boolean.FALSE);
			}else {
				return new AbstractMap.SimpleEntry<>((T)intData.getValue(), Boolean.TRUE);
			}
			
		}else if (col.getDataType() == DataType.Postal) {
			PostalData postalData = (PostalData)valueObj;
			T value = (T)postalData.getPostal();
			return new AbstractMap.SimpleEntry<>(value, value == null?Boolean.FALSE:Boolean.TRUE);
		}else if (col.getDataType() == DataType.Label) {
			LabelData valueData = (LabelData)valueObj;
			T value = (T)valueData.getValue();
			return new AbstractMap.SimpleEntry<>(value, value == null?Boolean.FALSE:Boolean.TRUE);
		}else if (col.getDataType() == DataType.FileUpload) {
			UploadData valueData = (UploadData)valueObj;
			T value = (T)valueData.getFileName();
			return new AbstractMap.SimpleEntry<>(value, value == null?Boolean.FALSE:Boolean.TRUE);
		}

		return new AbstractMap.SimpleEntry<>((T)valueObj, valueObj == null?Boolean.FALSE:Boolean.TRUE);
	}

	private static void setCellValue(AnnexureRow<?> row, ColumnInfo<?> col, Object value) {
		Object valueObj = row.get(col);
		if (col.getDataType() == DataType.Area) {
			AreaData areaData = (AreaData)valueObj;
			if (value == null || (int)value == 0) {
				areaData.setSelectedAreaInternal(null);
			}else {
				for(MCity area : areaData.getDataProvider()) {
					if (area.getC_City_ID() == (int)value) {
						areaData.setSelectedAreaInternal(area);
					}
				}
			}
		}else if (col.getDataType() == DataType.PositiveNumber) {
			IntData intData = (IntData)valueObj;
			intData.setValue(Util.convert((int)value));
		}else if (col.getDataType() == DataType.Postal) {
			PostalData postalData = (PostalData)valueObj;
			postalData.setPostalInternal(Util.convertStr((String)value));
		}else if (col.getDataType() == DataType.Label) {
			LabelData valueData = (LabelData)valueObj;
			valueData.setValue(Util.convertStr((String)value));
		}else if (col.getDataType() == DataType.FileUpload) {
			UploadData valueData = (UploadData)valueObj;
			valueData.setFileName(Util.convertStr((String)value));
		}else {
			row.put(col, value);
		}
	}

	public static void saveProjectInput(String trxName, X_ZZ_Application_Form applicationForm, ProjectInput projectInput) {

		int total = 0;
		for (AnnexureRow<?> row : projectInput.getRows()) {
			X_ZZLearnersApplied learnersApplied = (X_ZZLearnersApplied)row.getData();
			if (learnersApplied == null) {
				learnersApplied = new X_ZZLearnersApplied(null, 0, trxName);
				learnersApplied.setDataType(projectInput.getDataType());
				learnersApplied.setZZ_Application_Form_ID(applicationForm.getZZ_Application_Form_ID());
			}
			
			Entry<Integer, Boolean> result = fillDaoData(projectInput.getColumnInfos(), row, learnersApplied);
			if (result.getValue()){
				learnersApplied.saveEx(trxName);
				total += result.getKey();
			}
		}
		applicationForm.setZZTotalNumberApplied(total + applicationForm.getZZTotalNumberApplied());
		
	}
	
	protected static Entry<Integer, Boolean> fillDaoData (Collection<ColumnInfo<?>> cols, AnnexureRow<?> row, Object dao){
		Integer total = Integer.valueOf(0);
		Boolean hasRowData = false;
		for (ColumnInfo<?> col:cols) {
			if (StringUtils.isNotBlank(col.getDaoPropertyName())){
			
			Boolean hasCellData = null;
			Object cellValueObj = null;
			if (col.getDataType() == DataType.PositiveNumber) {
				Entry<Integer, Boolean>  entryIntObj = getCellValue(row, col);
				total += entryIntObj.getKey();
				cellValueObj = entryIntObj.getKey();
				hasCellData = entryIntObj.getValue();
			}else {
				Entry<Object, Boolean> celDataEntryObj = getCellValue(row, col);
				cellValueObj = celDataEntryObj.getKey();
				hasCellData = celDataEntryObj.getValue();
			}
			if (hasCellData)
				hasRowData = true;
			
			setDaoValue(dao, col.getDaoPropertyName(), cellValueObj);
			}
		}
		
		return new AbstractMap.SimpleEntry<>(total, hasRowData);
	}
	
}
