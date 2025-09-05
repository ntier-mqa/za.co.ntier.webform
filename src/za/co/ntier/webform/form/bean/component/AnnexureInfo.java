package za.co.ntier.webform.form.bean.component;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public Map<ColumnInfo<?>, Object> createDetailRow(List<ColumnInfo<?>> columnInfos) {
		return createDetailRow(columnInfos, null);
	}

	@SuppressWarnings("unchecked")
	public Map<ColumnInfo<?>, Object> createDetailRow(List<ColumnInfo<?>> columnInfos,
			Map<ColumnInfo<?>, Object> rowDataInits) {
		Map<ColumnInfo<?>, Object> newRow = new HashMap<>();

		for (ColumnInfo<?> columnInfo : columnInfos) {
			Object cellData = null;
			if (rowDataInits != null) {
				cellData = rowDataInits.get(columnInfo);
			}
			if (cellData == null) {

				if (columnInfo.getDataType() == DataType.TwoTitles) {
					cellData = Arrays.asList(null, null);

				} else if (columnInfo.getDataType() == DataType.TwoValues) {
					cellData = Arrays.asList(null, null);

				} else if (columnInfo.getDataType() == DataType.FileUpload) {
					cellData = new UploadData();

				} else if (columnInfo.getDataType() == DataType.Area) {
					AreaData areaData = new AreaData(this, newRow);
					areaData.setDataProvider((List<MCity>)columnInfo.getDataProvider());
					cellData = areaData;
				} else if (columnInfo.getDataType() == DataType.Postal) {
					PostalData textData = new PostalData(this, newRow, null);
					cellData = textData;
				}else if (columnInfo.getDataType() == DataType.PositiveNumber) {
					cellData = new IntData(this, newRow, null);
				}

			}
			newRow.put(columnInfo, cellData);

		}

		return newRow;
	}

	public static <T extends AnnexureInfo> T getAnnexureInfo(Class<T> clazz, List<ColumnInfo<?>> columnInfos,
			boolean isShowTotal) throws NoSuchMethodException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {

		T annexureInfo = clazz.getDeclaredConstructor().newInstance();
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
			List<ColumnInfo<?>> columnInfos, String rowTitle, boolean isShowTotal, List<String> twoTitleValue)
			throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		T annexureInfo = AnnexureInfo.getAnnexureInfo(clazz, columnInfos, isShowTotal);

		Map<ColumnInfo<?>, Object> rowDataInits = new HashMap<>();
		for (ColumnInfo<?> colInfo : columnInfos) {
			if (colInfo.getDataType() == DataType.Label && rowTitle != null) {
				rowDataInits.put(colInfo, rowTitle);
			} else if (colInfo.getDataType() == DataType.TwoTitles && twoTitleValue != null) {
				rowDataInits.put(colInfo, twoTitleValue);
			}
		}

		Map<ColumnInfo<?>, Object> fistRow = annexureInfo.createDetailRow(columnInfos, rowDataInits);
		annexureInfo.getRows().add(fistRow);
		annexureInfo.setSectionHeader(sectionHeader);
		return annexureInfo;
	}

	private String tableTitle;

	private List<ColumnInfo<?>> columnInfos;
	private List<Map<ColumnInfo<?>, Object>> rows;

	private String sectionHeader;
	private boolean showTotal = false;
	private boolean showAddButton = false;

	private AnnexureInfo subAnnexure;

	private Map<ColumnInfo<?>, Object> totalRow;

	public void addRow() {
		Map<ColumnInfo<?>, Object> row = createDetailRow(getColumnInfos());
		getRows().add(row);
		BindUtils.postNotifyChange(this, "rows");
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

	public void uploadFile(Map<ColumnInfo<?>, Object> row, ColumnInfo<?> col, UploadEvent event) throws IOException {
		UploadData uploadInfoObj = (UploadData) row.get(col);
		uploadInfoObj.setFileName(event.getMedia().getName());
		uploadInfoObj.setFullPath(MasterUtil.saveUploadFile(event.getMedia()));

		BindUtils.postNotifyChange(row.get(col), "fileName");
	}

	public void postalChange (Map<ColumnInfo<?>, Object> row, 
			ColumnInfo<?> col,
			InputEvent event){
		
	}
	
	public void areaSelect (Map<ColumnInfo<?>, Object> row, 
			ColumnInfo<?> col,
			SelectEvent<?, ?> event){
		
	}
	
	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return the showAddButton
	 */
	public boolean isShowAddButton() {
		return showAddButton;
	}

	/**
	 * @param showAddButton the showAddButton to set
	 */
	public void setShowAddButton(boolean showAddButton) {
		this.showAddButton = showAddButton;
	}

	public static ColumnInfo<?> lookupColByDataType(DataType dataType, AnnexureInfo annexure){
		return lookupCol(dataType, null, annexure);
	}
	
	public static ColumnInfo<?> lookupColByTitle(String colName, AnnexureInfo annexure){
		return lookupCol(null, colName, annexure);
	}
	
	protected static ColumnInfo<?> lookupCol(DataType dataType, String colName, AnnexureInfo annexure){
		ColumnInfo<?> foundCol = null;
		for (ColumnInfo<?> col : annexure.getColumnInfos()) {
			if (dataType != null && dataType.equals(col.getDataType())){
				return  col;
			}
			
			if (colName != null && colName.equals(col.getTitle())){
				return  col;
			}
		}
		
		return foundCol;
	}
}
