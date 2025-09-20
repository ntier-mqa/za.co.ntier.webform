package za.co.ntier.webform.form.bean.component;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.webui.exception.ApplicationException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.compiere.model.MCity;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.event.UploadEvent;

import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.Util;
import za.co.ntier.webform.form.bean.DataType;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class AnnexureInfo implements ISaveForm{
	protected static final CLogger log = CLogger.getCLogger(AnnexureInfo.class);
	public final static String AnnexureTypeExitStrategy = "EXIT STRATEGY";
	public final static String AnnexureTypeTargetGroup = "TARGET GROUP";
	public final static String AnnexureTypeBudgetOverview = "BUDGET OVERVIEW";
	
	private String dataType;
	
	public static <T extends AnnexureInfo> T getAnnexureInfo(Class<T> clazz, List<ColumnInfo<?>> columnInfos,
			boolean isShowTotal){

		T annexureInfo;
		try {
			annexureInfo = clazz.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			throw new ApplicationException(e.getMessage(), e);
		}
		annexureInfo.setShowTotal(isShowTotal);
		annexureInfo.setColumnInfos(columnInfos);

		List<AnnexureRow<?>> rows = new ArrayList<>();
		annexureInfo.setRows(rows);

		if (isShowTotal) {
			Map<ColumnInfo<?>, Object> totalRow = new HashMap<>();
			for (ColumnInfo<?> columnInfo : columnInfos) {
				if (columnInfo.getDataType() == DataType.PositiveNumber) {
					totalRow.put(columnInfo, new IntData(annexureInfo, totalRow, 0));
				} else {
					totalRow.put(columnInfo, null);
				}
			}
			
			if (totalRow.get(columnInfos.get(0)) == null) {
				totalRow.put(columnInfos.get(0), "Total");
			}

			annexureInfo.setTotalRow(totalRow);
		}
		return annexureInfo;
	}
	
	public static Integer getIntegerValue(AnnexureInfo cetTvetOneLineInput, ColumnInfo<?> col) {
		return AnnexureInfo.getIntegerValue(cetTvetOneLineInput.getRows().get(0), col);
	}
	
	public static Integer getIntegerValue(Map<ColumnInfo<?>, Object> cetTvetMultiLineRow, ColumnInfo<?> colInfo) {
		
		if (colInfo != null && colInfo.getDataType() == DataType.PositiveNumber && cetTvetMultiLineRow.get(colInfo) != null) {
			IntData cellData = (IntData)cetTvetMultiLineRow.get(colInfo);
			return cellData.getValue();
		}
		
		return null;
	}

	public static ColumnInfo<?> lookupCol(DataType dataType, String colName, AnnexureInfo annexure){
		return lookupCol(dataType, colName, annexure.getColumnInfos());
	}
	
	public static ColumnInfo<?> lookupCol(DataType dataType, String colName, Collection<ColumnInfo<?>> cols){
		ColumnInfo<?> foundCol = null;
		
		for (ColumnInfo<?> col : cols) {
			if (dataType != null  && colName != null && dataType.equals(col.getDataType()) && colName.equals(col.getTitle())) {
				return col;
			}
			
			if (dataType != null && dataType.equals(col.getDataType())){
				return  col;
			}
			
			if (colName != null && colName.equals(col.getTitle())){
				return  col;
			}
		}
		
		return foundCol;
	}
	
	public static ColumnInfo<?> lookupColByDataType(DataType dataType, AnnexureInfo annexure){
		return lookupCol(dataType, null, annexure);
	}

	public static ColumnInfo<?> lookupColByDataType(DataType dataType, Collection<ColumnInfo<?>> cols){
		return lookupCol(dataType, null, cols);
	}

	public static ColumnInfo<?> lookupColByTitle(String colName, AnnexureInfo annexure){
		return lookupCol(null, colName, annexure);
	}
	public static ColumnInfo<?> lookupColByTitle(String colName, Collection<ColumnInfo<?>> cols){
		return lookupCol(null, colName, cols);
	}

	private List<ColumnInfo<?>> columnInfos;
	private Function<AnnexureInfo, AnnexureRow<?>> newRowSupplier;
	private BiFunction<AnnexureInfo, X_ZZ_Application_Form, PO> poSupplier;
	private List<AnnexureRow<?>> rows;
	private String sectionHeader;

	private boolean showAddButton = false;

	private boolean showTotal = false;
	
	private boolean showColumnHeader = true;

	private AnnexureInfo subAnnexure;

	private String subSectionHeader;
	
	private String tableTitle;

	private Map<ColumnInfo<?>, Object> totalRow;

	public void addRow() {
		createDetailRow();
		BindUtils.postNotifyChange(this, "rows");
	}

	public void areaSelect (Map<ColumnInfo<?>, Object> row, 
			ColumnInfo<?> col,
			SelectEvent<?, ?> event){
		
	}

	@SuppressWarnings("unchecked")
	public AnnexureRow<?> createDetailRow() {
		
		AnnexureRow<?> row = newRowSupplier.apply(this);

		for (ColumnInfo<?> columnInfo : columnInfos) {
			Object cellData = null;
			
			cellData = row.get(columnInfo);
			
			if (cellData == null) {

				if (columnInfo.getDataType() == DataType.TwoTitles) {
					cellData = Arrays.asList(null, null);

				} else if (columnInfo.getDataType() == DataType.TwoValues) {
					cellData = Arrays.asList(null, null);

				} else if (columnInfo.getDataType() == DataType.FileUpload) {
					cellData = new UploadData();

				} else if (columnInfo.getDataType() == DataType.Area) {
					AreaData areaData = new AreaData(this, row);
					areaData.setDataProvider((List<MCity>)columnInfo.getDataProvider());
					cellData = areaData;
				} else if (columnInfo.getDataType() == DataType.Postal) {
					PostalData textData = new PostalData(this, row, null);
					cellData = textData;
				}else if (columnInfo.getDataType() == DataType.PositiveNumber) {
					cellData = new IntData(this, row, null);
				}else if (columnInfo.getDataType() == DataType.Label) {
					cellData = new LabelData();
				}

			}
			row.put(columnInfo, cellData);

		}

		getRows().add(row);
		return row;
	}
	
	/**
	 * update all cell on total row, use on init data
	 */
	public void updateTotalRow() {
		if (!showTotal)
			return;
		
		for (ColumnInfo<?> col:getColumnInfos()) {
			if (col.getDataType() == DataType.PositiveNumber) {
				updateTotalRow(col, false);
			}
		}
	}
	
	/**
	 * update total cell of column has data change
	 * @param col
	 * @param needNotify
	 */
	public void updateTotalRow(ColumnInfo<?> col, boolean needNotify) {
		Integer total = 0;
		for (Map<ColumnInfo<?>, Object> r : getRows()) {
			IntData intData = (IntData)r.get(col);
			if (intData.getValue() != null) {
				total += intData.getValue();
			}
		}
		
		IntData totalValue = (IntData)totalRow.get(col);
		totalValue.setValue(total);
		
		if(needNotify) {
			BindUtils.postNotifyChange(totalValue, "value");
		}
	}

	/**
	 * @return the columnInfos
	 */
	public List<ColumnInfo<?>> getColumnInfos() {
		return columnInfos;
	}

	/**
	 * @return the rows
	 */
	public List<AnnexureRow<?>> getRows() {
		return rows;
	}

	/**
	 * @return the sectionHeader
	 */
	public String getSectionHeader() {
		return sectionHeader;
	}

	/**
	 * @return the subAnnexure
	 */
	public AnnexureInfo getSubAnnexure() {
		return subAnnexure;
	}

	/**
	 * @return the subSectionHeader
	 */
	public String getSubSectionHeader() {
		return subSectionHeader;
	}

	public Function<AnnexureInfo, AnnexureRow<?>> getNewRowSupplier() {
		return newRowSupplier;
	}

	/**
	 * @return the tableTitle
	 */
	public String getTableTitle() {
		return tableTitle;
	}

	/**
	 * @return the totalRow
	 */
	public Map<ColumnInfo<?>, Object> getTotalRow() {
		return totalRow;
	}

	/**
	 * @return the showAddButton
	 */
	public boolean isShowAddButton() {
		return showAddButton;
	}

	/**
	 * @return the showTotal
	 */
	public boolean isShowTotal() {
		return showTotal;
	}

	public void numChange(AnnexureRow<?> row, ColumnInfo<?> col, InputEvent event) {
		// update total row
		if (col.getDataType() == DataType.PositiveNumber && showTotal) {
			updateTotalRow(col, true);
		}
		
		if (col.getDataType() == DataType.PositiveNumber) {
			updateExpressionCol (row, true);
		}
			
	}

	public void updateExpressionCol () {
		boolean hasExpressionCol = false;
		for (ColumnInfo<?> colTotal : getColumnInfos()) {
			if (colTotal.getExpression() != null) {
				hasExpressionCol = true;
			}
		}
		
		if(hasExpressionCol) {
			for(AnnexureRow<?> row:getRows()) {
				updateExpressionCol(row, false);
			}
		}
	}
	/**
	 * update cell all Expression Col on current row
	 * @param row
	 */
	public void updateExpressionCol (AnnexureRow<?> row, boolean needNotify) {
		// update expression column
		for (ColumnInfo<?> colTotal : getColumnInfos()) {
			if (colTotal.getExpression() != null) {
				Integer value = colTotal.getExpression().apply(row);
				LabelData expressionLable = (LabelData) row.get(colTotal);
				if (value == null) {
					expressionLable.setValue(null);
				}else {
					expressionLable.setValue(String.valueOf(value));
				}
				if(needNotify) {
					BindUtils.postNotifyChange(expressionLable, "value");
				}
				
			}
		}
	}
	
	public void postalChange (Map<ColumnInfo<?>, Object> row, 
			ColumnInfo<?> col,
			InputEvent event){
		
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * @param columnInfos the columnInfos to set
	 */
	public void setColumnInfos(List<ColumnInfo<?>> columnInfos) {
		this.columnInfos = columnInfos;
	}
	
	/**
	 * @param rows the rows to set
	 */
	public void setRows(List<AnnexureRow<?>> rows) {
		this.rows = rows;
	}

	/**
	 * @param sectionHeader the sectionHeader to set
	 */
	public void setSectionHeader(String sectionHeader) {
		this.sectionHeader = sectionHeader;
	}

	/**
	 * @param showAddButton the showAddButton to set
	 */
	public void setShowAddButton(boolean showAddButton) {
		this.showAddButton = showAddButton;
	}

	/**
	 * @param showTotal the showTotal to set
	 */
	public void setShowTotal(boolean showTotal) {
		this.showTotal = showTotal;
	}
	
	/**
	 * @param subAnnexure the subAnnexure to set
	 */
	public void setSubAnnexure(AnnexureInfo subAnnexure) {
		this.subAnnexure = subAnnexure;
	}
	
	/**
	 * @param subSectionHeader the subSectionHeader to set
	 */
	public void setSubSectionHeader(String subSectionHeader) {
		this.subSectionHeader = subSectionHeader;
	}

	public void setNewRowSupplier(Function<AnnexureInfo, AnnexureRow<?>> newRowSupplier) {
		this.newRowSupplier = newRowSupplier;
	}


	/**
	 * @param tableTitle the tableTitle to set
	 */
	public void setTableTitle(String tableTitle) {
		this.tableTitle = tableTitle;
	}

	/**
	 * @param totalRow the totalRow to set
	 */
	public void setTotalRow(Map<ColumnInfo<?>, Object> totalRow) {
		this.totalRow = totalRow;
	}

	public void uploadFile(Map<ColumnInfo<?>, Object> row, ColumnInfo<?> col, UploadEvent event) {
		UploadData uploadInfoObj = (UploadData) row.get(col);
		uploadInfoObj.setFileName(event.getMedia().getName());
		try {
			uploadInfoObj.setFullPath(MasterUtil.saveUploadFile(event.getMedia()));
		} catch (IOException e) {
			e.printStackTrace();
			throw new AdempiereException(e.getMessage(), e);
		}

		BindUtils.postNotifyChange(row.get(col), "fileName");
	}

	/**
	 * @return the showColumnHeader
	 */
	public boolean isShowColumnHeader() {
		return showColumnHeader;
	}

	/**
	 * @param showColumnHeader the showColumnHeader to set
	 */
	public void setShowColumnHeader(boolean showColumnHeader) {
		this.showColumnHeader = showColumnHeader;
	}

	/**
	 * @return the dataType
	 */
	public String getDataType() {
		return dataType;
	}

	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Entry<Integer, Boolean> fillDaoData (Collection<ColumnInfo<?>> cols, AnnexureRow<?> row, Object dao){
		Integer total = Integer.valueOf(0);
		Boolean hasRowData = Boolean.FALSE;
		for (ColumnInfo<?> col:cols) {
			if (StringUtils.isNotBlank(col.getDaoPropertyName())){
			
				Boolean hasCellData = Boolean.FALSE;
				Object cellValueObj = null;
				if (col.getDataType() == DataType.PositiveNumber) {
					Entry<Integer, Boolean>  entryIntObj = getCellValue(row, col);
					total += entryIntObj.getKey();
					cellValueObj = entryIntObj.getKey();
					hasCellData = entryIntObj.getValue();
				}else if (col.getDataType() == DataType.LearnerInfo) {
					LearnerInputInfo learnerInfo = (LearnerInputInfo)row.get(col);
					int learnerInputID = 0;// int of PO don't accept null
					if (learnerInfo != null) {
						learnerInputID = learnerInfo.getLearnerInputID();
						hasCellData = Boolean.TRUE;
					}
					cellValueObj = learnerInputID;
				}else if (col.getDataType() == DataType.FileUpload) {
					UploadData uploadData = (UploadData)row.get(col);
							
					if (uploadData != null && StringUtils.isNoneEmpty(uploadData.getFullPath())) {
						try {
							cellValueObj = Files.readAllBytes(Paths.get(uploadData.getFullPath()));
							setDaoValue(dao, col.getDaoPropertyFileName(), uploadData.getFileName());
							hasCellData = Boolean.TRUE;
						} catch (IOException e) {
							e.printStackTrace();
							throw new ApplicationException(e.getMessage(), e);
						}
					}
				}else {
					Entry<Object, Boolean> celDataEntryObj = getCellValue(row, col);
					cellValueObj = celDataEntryObj.getKey();
					hasCellData = celDataEntryObj.getValue();
				}
				if (hasCellData)
					hasRowData = Boolean.TRUE;
				
				setDaoValue(dao, col.getDaoPropertyName(), cellValueObj);
			}
		}
		
		return new AbstractMap.SimpleEntry<>(total, hasRowData);
	}

	public void save(String trxName, X_ZZ_Application_Form applicationForm) {
	
		int total = 0;
		for (AnnexureRow<?> row : getRows()) {
			PO learnersApplied = (PO)row.getData();
			if (learnersApplied == null) {
				applicationForm.set_TrxName(trxName);//importance
				learnersApplied = poSupplier.apply(this, applicationForm);
			}
			
			Entry<Integer, Boolean> result = fillDaoData(getColumnInfos(), row, learnersApplied);
			if (result.getValue()){
				learnersApplied.saveEx(trxName);
			}else {
				learnersApplied.delete(true);// delete if no input or data is cleared
			}
			total += result.getKey();
		}
		applicationForm.setZZTotalNumberApplied(total + applicationForm.getZZTotalNumberApplied());
		
	}

	static void setCellValue(AnnexureRow<?> row, ColumnInfo<?> col, Object value) {
		log.info(String.format("Set cell value: row=%s, col=%s, col datatype=%s, value=%s", row, col.getTitle(), col.getDataType(), value));
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
		}else if (col.getDataType() == DataType.LearnerInfo && valueObj != null && !(value instanceof LearnerInputInfo)) {
			// don't need, already set when init row tile
		}else {
			row.put(col, value);
		}
	}

	@SuppressWarnings("unchecked")
	/**
	* return entry with value already convert (example 0 for non input int), value is true in case input non-null
	*/
	public <T> Entry<T, Boolean> getCellValue(AnnexureRow<?> row, ColumnInfo<?> col) {
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
		}else if (col.getDataType() == DataType.LearnerInfo) {
			LearnerInputInfo valueData = (LearnerInputInfo)valueObj;
			Integer value = Integer.valueOf(0);
			if(valueData != null) {
				value = Integer.valueOf(valueData.getLearnerInputID());
			}
			return new AbstractMap.SimpleEntry<>((T)value, value.equals(0) ? Boolean.FALSE:Boolean.TRUE);
		}	
		return new AbstractMap.SimpleEntry<>((T)valueObj, valueObj == null?Boolean.FALSE:Boolean.TRUE);
	}

	public void setDaoValue(Object dao, String propertyName, Object value) {
		try {
			PropertyUtils.setSimpleProperty(dao, propertyName, value);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AdempiereException(e.getMessage(), e);
		}
	}

	public Object getDaoValue(Object dao, String propertyName) {
		try {
			return PropertyUtils.getSimpleProperty(dao, propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AdempiereException(e.getMessage(), e);
		}
		
	}

	/**
	 * 
	 * @param row
	 * @param dao
	 * @param keyColumns
	 * @return
	 */
	public boolean isMatchingRow(AnnexureRow<?> row, Object dao, Collection<ColumnInfo<?>> keyColumns) {
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

	/**
	 * check each column to get matching property of bean, query data of bean and set to row 
	 * @param cols
	 * @param row
	 * @param dao
	 */
	public void fillRowDataFromDao(List<ColumnInfo<?>> cols, AnnexureRow<?> row, Object dao) {
		for (ColumnInfo<?> col : cols) {
			Object daoValue = null;
			if (col.getDataType() == DataType.FileUpload && StringUtils.isNotBlank(col.getDaoPropertyFileName())) {
				daoValue = getDaoValue(dao, col.getDaoPropertyFileName());
			}else if (col.getDataType() != DataType.FileUpload  && StringUtils.isNotBlank(col.getDaoPropertyName())) {
				daoValue = getDaoValue(dao, col.getDaoPropertyName());
			}
			
			AnnexureInfo.setCellValue(row, col, daoValue);
		}
	}
	
	public void init(X_ZZ_Application_Form applicationForm) {
		init(applicationForm, null);
	}
	
	public void init(X_ZZ_Application_Form applicationForm, List<PO> savedDatas) {
		init(applicationForm, savedDatas, null);
	}
	
	public void init(X_ZZ_Application_Form applicationForm, List<PO> savedDatas, List<Map<ColumnInfo<?>, Object>> rowTitles) {
		if(rowTitles == null || rowTitles.size() == 0)
			init(applicationForm, savedDatas, null, null);
		else
			init(applicationForm, savedDatas, rowTitles, rowTitles.get(0).keySet());
	}
	
	public void init(X_ZZ_Application_Form applicationForm, 
			List<PO> savedDatas, List<Map<ColumnInfo<?>, Object>> rowTitles, Collection<ColumnInfo<?>> keyColumns) {
		// init rows with rowTitles
		if (rowTitles != null)
			for (Map<ColumnInfo<?>, Object> rowTitle : rowTitles) {
				AnnexureRow<?> row = (AnnexureRow<?>)createDetailRow();
				
				for (Entry<ColumnInfo<?>, Object> colTile : rowTitle.entrySet()) {
					AnnexureInfo.setCellValue(row, colTile.getKey(), colTile.getValue());
				}
			}
		
		// init rows with saved data
		if (savedDatas != null && savedDatas.size() > 0) {
			List<AnnexureRow<?>> matchedRows = new ArrayList<>();
			
			boolean learnerInfoKey = false;
			if (keyColumns != null && keyColumns.size() > 0) {
				if (lookupColByDataType(DataType.LearnerInfo, keyColumns) != null) {
					learnerInfoKey = true;
				}
			}
			
			for (PO dao : savedDatas) {
				boolean isMatching = false;
				if (keyColumns != null && keyColumns.size() > 0) {
					for (AnnexureRow<?> row : getRows()) {
						if (matchedRows.contains(row))
							continue;
						
						isMatching = isMatchingRow(row, dao, keyColumns);
						
						if (isMatching) {
							matchedRows.add(row);
							//row.setData(dao);
							fillRowDataFromDao(getColumnInfos(), row, dao);
							break;
						}
					}
				}
				
				if (!isMatching && learnerInfoKey) {
					// moment don't handle this case, in case what to handle it need to change createDetailRow to create LearnerInputInfo for LearnerInfo column
				}else if(!isMatching) {
					AnnexureRow<?> row = createDetailRow();
					fillRowDataFromDao(getColumnInfos(), row, dao);
				}
			}
			
		}
		
		if (getRows().size() == 0) {
			createDetailRow();
		}
		
		updateExpressionCol();
		updateTotalRow();
	}

	/**
	 * @return the poSupplier
	 */
	public BiFunction<AnnexureInfo, X_ZZ_Application_Form, PO> getPoSupplier() {
		return poSupplier;
	}

	/**
	 * @param poSupplier the poSupplier to set
	 */
	public void setPoSupplier(BiFunction<AnnexureInfo, X_ZZ_Application_Form, PO> poSupplier) {
		this.poSupplier = poSupplier;
	}
	
	
}
