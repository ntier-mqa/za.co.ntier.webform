package za.co.ntier.webform.form.bean.component;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.webui.exception.ApplicationException;
import org.compiere.model.MCity;
import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.event.UploadEvent;

import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.bean.DataType;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class AnnexureInfo implements ISaveForm{
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

		List<Map<ColumnInfo<?>, Object>> rows = new ArrayList<>();
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
	
	public static <T extends AnnexureInfo> T getAnnexureInfoOneLine(Class<T> clazz, String sectionHeader,
			List<ColumnInfo<?>> columnInfos, String rowTitle, boolean isShowTotal, List<String> twoTitleValue){
		T annexureInfo = AnnexureInfo.getAnnexureInfo(clazz, columnInfos, isShowTotal);

		Map<ColumnInfo<?>, Object> rowDataInits = new HashMap<>();
		for (ColumnInfo<?> colInfo : columnInfos) {
			if (colInfo.getDataType() == DataType.Label && rowTitle != null) {
				rowDataInits.put(colInfo, rowTitle);
			} else if (colInfo.getDataType() == DataType.TwoTitles && twoTitleValue != null) {
				rowDataInits.put(colInfo, twoTitleValue);
			}
		}

		annexureInfo.createDetailRow(columnInfos, rowDataInits);
		annexureInfo.setSectionHeader(sectionHeader);
		return annexureInfo;
	}

	protected static ColumnInfo<?> lookupCol(DataType dataType, String colName, AnnexureInfo annexure){
		return lookupCol(dataType, colName, annexure.getColumnInfos());
	}
	
	protected static ColumnInfo<?> lookupCol(DataType dataType, String colName, List<ColumnInfo<?>> cols){
		ColumnInfo<?> foundCol = null;
		
		for (ColumnInfo<?> col : cols) {
			if (dataType != null && dataType.equals(col.getDataType())){
				return  col;
			}
			
			if (colName != null && colName.equals(col.getTitle())){
				return  col;
			}
		}
		
		return foundCol;
	}

	public static ColumnInfo<?> lookupColByDataType(DataType dataType, List<ColumnInfo<?>> cols){
		return lookupCol(dataType, null, cols);
	}
	
	public static ColumnInfo<?> lookupColByDataType(DataType dataType, AnnexureInfo annexure){
		return lookupCol(dataType, null, annexure);
	}
	
	public static ColumnInfo<?> lookupColByTitle(String colName, List<ColumnInfo<?>> cols){
		return lookupCol(null, colName, cols);
	}

	public static ColumnInfo<?> lookupColByTitle(String colName, AnnexureInfo annexure){
		return lookupCol(null, colName, annexure);
	}

	private List<ColumnInfo<?>> columnInfos;
	private List<Map<ColumnInfo<?>, Object>> rows;

	private String sectionHeader;
	private String subSectionHeader;
	private boolean showAddButton = false;
	private boolean showTotal = false;

	private AnnexureInfo subAnnexure;

	private String tableTitle;

	private Map<ColumnInfo<?>, Object> totalRow;

	public void addRow() {
		Map<ColumnInfo<?>, Object> row = createDetailRow(getColumnInfos());
		BindUtils.postNotifyChange(this, "rows");
	}

	public Supplier<Map<ColumnInfo<?>, Object>> getSupplier() {
		return rowInitSupplier;
	}

	public void setSupplier(Supplier<Map<ColumnInfo<?>, Object>> supplier) {
		this.rowInitSupplier = supplier;
	}

	public void areaSelect (Map<ColumnInfo<?>, Object> row, 
			ColumnInfo<?> col,
			SelectEvent<?, ?> event){
		
	}

	private Supplier<Map<ColumnInfo<?>, Object>> rowInitSupplier;
	public Map<ColumnInfo<?>, Object> createDetailRow(List<ColumnInfo<?>> columnInfos) {
		return createDetailRow(columnInfos, null);
	}

	protected Map<ColumnInfo<?>, Object> createEmptyRow(){
		return rowInitSupplier != null?rowInitSupplier.get():new HashMap<>();
		
	}
	
	
	
	@SuppressWarnings("unchecked")
	public Map<ColumnInfo<?>, Object> createDetailRow(List<ColumnInfo<?>> columnInfos,
			Map<ColumnInfo<?>, Object> rowDataInits) {
		if(rowDataInits == null) {
			rowDataInits = createEmptyRow();
		}

		for (ColumnInfo<?> columnInfo : columnInfos) {
			Object cellData = null;
			
			cellData = rowDataInits.get(columnInfo);
			
			if (cellData == null) {

				if (columnInfo.getDataType() == DataType.TwoTitles) {
					cellData = Arrays.asList(null, null);

				} else if (columnInfo.getDataType() == DataType.TwoValues) {
					cellData = Arrays.asList(null, null);

				} else if (columnInfo.getDataType() == DataType.FileUpload) {
					cellData = new UploadData();

				} else if (columnInfo.getDataType() == DataType.Area) {
					AreaData areaData = new AreaData(this, rowDataInits);
					areaData.setDataProvider((List<MCity>)columnInfo.getDataProvider());
					cellData = areaData;
				} else if (columnInfo.getDataType() == DataType.Postal) {
					PostalData textData = new PostalData(this, rowDataInits, null);
					cellData = textData;
				}else if (columnInfo.getDataType() == DataType.PositiveNumber) {
					cellData = new IntData(this, rowDataInits, null);
				}

			}
			rowDataInits.put(columnInfo, cellData);

		}

		getRows().add(rowDataInits);
		return rowDataInits;
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
	public List<Map<ColumnInfo<?>, Object>> getRows() {
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

	public void numChange(Map<ColumnInfo<?>, Object> row, ColumnInfo<?> col, InputEvent event) {
		if (col.getDataType() == DataType.PositiveNumber) {
			Integer total = 0;
			for (Map<ColumnInfo<?>, Object> r : getRows()) {
				IntData intData = (IntData)r.get(col);
				if (intData.getValue() != null) {
					total += intData.getValue();
				}
			}
			
			IntData totalValue = (IntData)totalRow.get(col);
			totalValue.setValue(total);
			BindUtils.postNotifyChange(totalValue, "value");
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
	public void setRows(List<Map<ColumnInfo<?>, Object>> rows) {
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

	public static Integer getIntegerValue(Map<ColumnInfo<?>, Object> cetTvetMultiLineRow, ColumnInfo<?> colInfo) {
		
		if (colInfo != null && colInfo.getDataType() == DataType.PositiveNumber && cetTvetMultiLineRow.get(colInfo) != null) {
			IntData cellData = (IntData)cetTvetMultiLineRow.get(colInfo);
			return cellData.getValue();
		}
		
		return null;
	}


	public static Integer getIntegerValue(AnnexureInfo cetTvetOneLineInput, ColumnInfo<?> col) {
		return AnnexureInfo.getIntegerValue(cetTvetOneLineInput.getRows().get(0), col);
	}

	/**
	 * @return the subSectionHeader
	 */
	public String getSubSectionHeader() {
		return subSectionHeader;
	}

	/**
	 * @param subSectionHeader the subSectionHeader to set
	 */
	public void setSubSectionHeader(String subSectionHeader) {
		this.subSectionHeader = subSectionHeader;
	}
}
