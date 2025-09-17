package za.co.ntier.webform.form.bean.component;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

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
	public final static String AnnexureTypeExitStrategy = "EXIT STRATEGY";
	public final static String AnnexureTypeTargetGroup = "TARGET GROUP";
	public final static String AnnexureTypeBudgetOverview = "BUDGET OVERVIEW";
	
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
	
	public static ColumnInfo<?> lookupCol(DataType dataType, String colName, List<ColumnInfo<?>> cols){
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

	public static ColumnInfo<?> lookupColByDataType(DataType dataType, List<ColumnInfo<?>> cols){
		return lookupCol(dataType, null, cols);
	}

	public static ColumnInfo<?> lookupColByTitle(String colName, AnnexureInfo annexure){
		return lookupCol(null, colName, annexure);
	}
	public static ColumnInfo<?> lookupColByTitle(String colName, List<ColumnInfo<?>> cols){
		return lookupCol(null, colName, cols);
	}

	private List<ColumnInfo<?>> columnInfos;
	private Function<AnnexureInfo, AnnexureRow<?>> rowInitSupplier;
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
		
		AnnexureRow<?> rowDataInits = rowInitSupplier.apply(this);

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
				}else if (columnInfo.getDataType() == DataType.Label) {
					cellData = new LabelData();
				}

			}
			rowDataInits.put(columnInfo, cellData);

		}

		getRows().add(rowDataInits);
		return rowDataInits;
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

	public Function<AnnexureInfo, AnnexureRow<?>> getSupplier() {
		return rowInitSupplier;
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

	public void setSupplier(Function<AnnexureInfo, AnnexureRow<?>> supplier) {
		this.rowInitSupplier = supplier;
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
	
	
}
