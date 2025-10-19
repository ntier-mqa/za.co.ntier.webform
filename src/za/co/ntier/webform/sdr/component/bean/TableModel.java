package za.co.ntier.webform.sdr.component.bean;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.webui.exception.ApplicationException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.compiere.model.MCity;
import org.compiere.model.MRegion;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.CLogger;
import org.zkoss.bind.BindUtils;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.event.UploadEvent;

import za.co.ntier.api.model.I_ZZBankingDetails;
import za.co.ntier.api.model.X_ZZBankingDetails;
import za.co.ntier.api.model.X_ZZ_Application_Form;
import za.co.ntier.webform.form.AttachmentUtil;
import za.co.ntier.webform.form.Util;
import za.co.ntier.webform.sdr.component.bean.cell.AbstractCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.AbstractListCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.AreaData;
import za.co.ntier.webform.sdr.component.bean.cell.CheckboxData;
import za.co.ntier.webform.sdr.component.bean.cell.DateData;
import za.co.ntier.webform.sdr.component.bean.cell.IntData;
import za.co.ntier.webform.sdr.component.bean.cell.LabelData;
import za.co.ntier.webform.sdr.component.bean.cell.PostalData;
import za.co.ntier.webform.sdr.component.bean.cell.ProvinceData;
import za.co.ntier.webform.sdr.component.bean.cell.StringListCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.TextData;
import za.co.ntier.webform.sdr.component.bean.cell.UploadData;
import za.co.ntier.webform.sdr.component.bean.powrapper.LearnerInputInfo;

public class TableModel {
	protected static final CLogger log = CLogger.getCLogger(TableModel.class);
	public final static String AnnexureTypeExitStrategy = "EXIT STRATEGY";
	public final static String AnnexureTypeTargetGroup = "TARGET GROUP";
	public final static String AnnexureTypeBudgetOverview = "BUDGET OVERVIEW";
	private String sclass = "";
	
	private String dataType;
	private boolean createNewRowWhenEmpty = true;
	
	public static <T extends TableModel> T getTableBean(Class<T> clazz, List<ColumnModel> columnInfos,
			boolean isShowTotal){

		T tableModel;
		try {
			tableModel = clazz.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			throw new ApplicationException(e.getMessage(), e);
		}
		tableModel.setShowTotal(isShowTotal);
		tableModel.setColumnInfos(columnInfos);

		List<RowModel> rows = new ArrayList<>();
		tableModel.setRows(rows);

		if (isShowTotal) {
			Map<ColumnModel, Object> totalRow = new HashMap<>();
			for (ColumnModel columnInfo : columnInfos) {
				if (columnInfo.getDataType() == DataType.PositiveNumber) {
					totalRow.put(columnInfo, new IntData(tableModel, null, 0));
				} else {
					totalRow.put(columnInfo, null);
				}
			}

			if (totalRow.get(columnInfos.get(0)) == null) {
				totalRow.put(columnInfos.get(0), "Total");
			}

			tableModel.setTotalRow(totalRow);
		}
		return tableModel;
	}

	public static Integer getIntegerValue(TableModel cetTvetOneLineInput, ColumnModel col) {
		return TableModel.getIntegerValue(cetTvetOneLineInput.getRows().get(0), col);
	}

	public static Integer getIntegerValue(Map<ColumnModel, Object> cetTvetMultiLineRow, ColumnModel colInfo) {

		if (colInfo != null && colInfo.getDataType() == DataType.PositiveNumber && cetTvetMultiLineRow.get(colInfo) != null) {
			IntData cellData = (IntData)cetTvetMultiLineRow.get(colInfo);
			return cellData.getValue();
		}

		return null;
	}

	public static ColumnModel lookupCol(DataType dataType, String colName, TableModel annexure){
		return lookupCol(dataType, colName, annexure.getColumnInfos());
	}

	public static ColumnModel lookupCol(DataType dataType, String colName, Collection<ColumnModel> cols){
		ColumnModel foundCol = null;

		for (ColumnModel col : cols) {
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

	public static ColumnModel lookupColByDataType(DataType dataType, TableModel annexure){
		return lookupCol(dataType, null, annexure);
	}

	public static ColumnModel lookupColByDataType(DataType dataType, Collection<ColumnModel> cols){
		return lookupCol(dataType, null, cols);
	}

	public static ColumnModel lookupColByTitle(String colName, TableModel annexure){
		return lookupCol(null, colName, annexure);
	}
	public static ColumnModel lookupColByTitle(String colName, Collection<ColumnModel> cols){
		return lookupCol(null, colName, cols);
	}

	private List<ColumnModel> columnModels;
	private BiFunction<TableModel, X_ZZ_Application_Form, PO> poSupplier;
	private List<RowModel> rows;
	private String sectionHeader;

	private boolean showAddButton = false;

	private boolean showTotal = false;

	private boolean showColumnHeader = true;

	private TableModel subAnnexure;

	private String subSectionHeader;

	private String tableTitle;

	private Map<ColumnModel, Object> totalRow;

	public void addRow() {
		createDetailRow();
		BindUtils.postNotifyChange(this, "rows");
	}

	public void cmdValueChanged (Map<ColumnModel, Object> row, 
			ColumnModel col,
			InputEvent event){
		IValueChange valueChange = (IValueChange)row.get(col);
		if (valueChange != null)
			valueChange.cmdValueChange(event);
	}
	
	/**
	 * handle event when use choose a area
	 * @param row
	 * @param col
	 * @param event
	 */
	public void cmdSelected (Map<ColumnModel, Object> row, 
			ColumnModel col,
			SelectEvent<?, ?> event){
		
		ISelectable selectable = (ISelectable)row.get(col);
		if (event.getSelectedObjects().isEmpty())
			selectable.cmdSelected(null);
		else
			selectable.cmdSelected(event.getSelectedObjects().iterator().next());
	}
	
	public RowModel createDetailRow() {
		return createDetailRow(null);
	}
	
	
	private AbstractCellModel createCellModel(RowModel rowModel, ColumnModel colModel) {
		AbstractCellModel cellData = null;
		
		if (colModel.getDataType() == DataType.FileUpload) {
			cellData = new UploadData(this, rowModel);

		} else if (colModel.getDataType() == DataType.Checkbox) {
			CheckboxData cellDataCheckbox = new CheckboxData(this, rowModel);
			cellDataCheckbox.setTitle(colModel.getTitle());// default title for case use one row
			cellData = cellDataCheckbox;
		} else if (colModel.getDataType() == DataType.Area) {
			AreaData areaData = new AreaData(this, rowModel);
			@SuppressWarnings("unchecked")
			List<MCity> initListData = (List<MCity>)colModel.getDataProvider();
			areaData.setDataProvider(initListData);
			cellData = areaData;
		} else if (colModel.getDataType() == DataType.Province) {
			ProvinceData provinceData = new ProvinceData(this, rowModel);
			@SuppressWarnings("unchecked")
			List<MRegion> initListData = (List<MRegion>)colModel.getDataProvider();
			provinceData.setDataProvider(initListData);
			cellData = provinceData;
		} else if (colModel.getDataType() == DataType.StringList) {
			StringListCellModel stringListCellModel = new StringListCellModel(this, rowModel);
			
			@SuppressWarnings("unchecked")
			List<String> initListData = (List<String>)colModel.getDataProvider();
			stringListCellModel.setDataProvider(initListData);
			cellData = stringListCellModel;

		}else if (colModel.getDataType() == DataType.Postal) {
			PostalData textData = new PostalData(this, rowModel, null);
			cellData = textData;
		}else if (colModel.getDataType() == DataType.PositiveNumber) {
			cellData = new IntData(this, rowModel, null);
		}else if (colModel.getDataType() == DataType.Label) {
			cellData = new LabelData(this, rowModel);
		}else if (colModel.getDataType() == DataType.Date) {
			cellData = new DateData(this, rowModel);
		}else if (colModel.getDataType() == DataType.Text) {
			cellData = new TextData(this, rowModel);
		}	
		
		return cellData;
	}
	
	@SuppressWarnings("unchecked")
	public RowModel createDetailRow(Map<ColumnModel, Object> rowTitle) {

		RowModel row = new RowModel(this);

		for (ColumnModel columnInfo : columnModels) {
			Object cellData = createCellModel(row, columnInfo);
			
			row.put(columnInfo, cellData);
		}
		
		if (rowTitle != null) {
			for (Entry<ColumnModel, Object> colTile : rowTitle.entrySet()) {
				TableModel.setCellValue(row, colTile.getKey(), colTile.getValue());
			}
		}
		
		if (decoratorCell != null) {
			decoratorCell.accept(row);
		}

		getRows().add(row);
		return row;
	}

	private Consumer<RowModel> decoratorCell;
	
	public Consumer<RowModel> getDecoratorCell() {
		return decoratorCell;
	}

	public void setDecoratorCell(Consumer<RowModel> decoratorCell) {
		this.decoratorCell = decoratorCell;
	}

	/**
	 * update all cell on total row, use on init data
	 */
	public void updateTotalRow() {
		if (!showTotal)
			return;

		for (ColumnModel col:getColumnInfos()) {
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
	public void updateTotalRow(ColumnModel col, boolean needNotify) {
		Integer total = 0;
		for (Map<ColumnModel, Object> r : getRows()) {
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
	 * @return the columnModels
	 */
	public List<ColumnModel> getColumnInfos() {
		return columnModels;
	}

	public RowModel getRow() {
		if (rows == null || rows.size() == 0)
			return null;
		
		return rows.get(0);
	}
	
	/**
	 * @return the rows
	 */
	public List<RowModel> getRows() {
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
	public TableModel getSubAnnexure() {
		return subAnnexure;
	}

	/**
	 * @return the subSectionHeader
	 */
	public String getSubSectionHeader() {
		return subSectionHeader;
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
	public Map<ColumnModel, Object> getTotalRow() {
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

	public void numChange(RowModel row, ColumnModel col, InputEvent event) {
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
		for (ColumnModel colTotal : getColumnInfos()) {
			if (colTotal.getExpression() != null) {
				hasExpressionCol = true;
			}
		}

		if(hasExpressionCol) {
			for(RowModel row:getRows()) {
				updateExpressionCol(row, false);
			}
		}
	}
	/**
	 * update cell all Expression Col on current row
	 * @param row
	 */
	public void updateExpressionCol (RowModel row, boolean needNotify) {
		// update expression column
		for (ColumnModel colTotal : getColumnInfos()) {
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


	/**
	 * @param columnModels the columnModels to set
	 */
	public void setColumnInfos(List<ColumnModel> columnInfos) {
		this.columnModels = columnInfos;
	}

	/**
	 * @param rows the rows to set
	 */
	public void setRows(List<RowModel> rows) {
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
	public void setSubAnnexure(TableModel subAnnexure) {
		this.subAnnexure = subAnnexure;
	}

	/**
	 * @param subSectionHeader the subSectionHeader to set
	 */
	public void setSubSectionHeader(String subSectionHeader) {
		this.subSectionHeader = subSectionHeader;
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
	public void setTotalRow(Map<ColumnModel, Object> totalRow) {
		this.totalRow = totalRow;
	}	


	public void uploadFile(Map<ColumnModel, Object> row, ColumnModel col, UploadEvent event) {
		UploadData ud = (UploadData) row.get(col);
		Media m = event.getMedia();

		ud.setFileName(m.getName());

		try {
			byte[] data;
			if (m.isBinary() && m.getByteData() != null) {
				data = m.getByteData();  // fast path
			} else {
				// robust stream fallback (also works for large uploads)
				try (InputStream in = m.getStreamData();
						ByteArrayOutputStream out = new ByteArrayOutputStream(32 * 1024)) {
					byte[] buf = new byte[32 * 1024];
					int n;
					while ((n = in.read(buf)) > 0) out.write(buf, 0, n);
					data = out.toByteArray();
				}
				if (data == null && !m.isBinary() && m.getStringData() != null) {
					data = m.getStringData().getBytes(StandardCharsets.UTF_8);
				}
			}
			ud.setBytes(data);
		} catch (Exception e) {
			throw new AdempiereException("Unable to read uploaded file", e);
		}

		// refresh the filename label
		BindUtils.postNotifyChange(ud, "fileName");
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

	public Entry<Integer, Boolean> fillDaoData (Collection<ColumnModel> cols, RowModel row, Object dao){
		Integer total = Integer.valueOf(0);
		boolean isEmptyData = true;
		boolean isFullData = true;
		boolean hasDaoPropertyName = false;
		for (ColumnModel col:cols) {
			boolean ignoreSetDao = false;
			if (StringUtils.isNotBlank(col.getDaoPropertyName())){
				hasDaoPropertyName = true;
				Boolean hasCellData = Boolean.FALSE;
				Object cellValueObj = null;
				if (col.getDataType() == DataType.PositiveNumber) {
					Entry<Integer, Boolean>  entryIntObj = getCellValue(row, col);
					if (col.isCalTotal())
						total += entryIntObj.getKey();
					cellValueObj = entryIntObj.getKey();
					hasCellData = entryIntObj.getValue();
				}else if (col.getDataType() == DataType.Text) {
					Entry<Integer, Boolean>  entryTextObj = getCellValue(row, col);
					cellValueObj = entryTextObj.getKey();
					hasCellData = entryTextObj.getValue();
				}else if (col.getDataType() == DataType.LearnerInfo) {
					LearnerInputInfo learnerInfo = (LearnerInputInfo)row.get(col);
					int learnerInputID = 0;// int of PO don't accept null
					if (learnerInfo != null) {
						learnerInputID = learnerInfo.getLearnerInputID();
						hasCellData = Boolean.TRUE;
					}
					cellValueObj = learnerInputID;
				}else if (col.getDataType() == DataType.Date) {
					DateData dateData = (DateData)row.get(col);
					cellValueObj = dateData.getTimestamp();
					if (cellValueObj != null) {
						hasCellData = true;
					}
				}else if (col.getDataType() == DataType.FileUpload) {
					// Martin changed to save to attachments instead to a binary file
					hasCellData = Boolean.TRUE;// upload data become option
					ignoreSetDao = true; // never set DAO properties for files anymore
				}else if (col.getDataType() == DataType.List) {
					Object selectedObj = row.get(col);
					if (StringUtils.isNotBlank(col.getBeanPropertyName())) {
						if (selectedObj != null) {
							cellValueObj = TableModel.getDaoValue(selectedObj, col.getBeanPropertyName());
							hasCellData = Boolean.TRUE;
						}else {
							cellValueObj = null;
						}
					}else {
						cellValueObj = selectedObj;
						if (cellValueObj != null)
							hasCellData = Boolean.TRUE;
					}
				}else {
					Entry<Object, Boolean> celDataEntryObj = getCellValue(row, col);
					cellValueObj = celDataEntryObj.getKey();
					hasCellData = celDataEntryObj.getValue();
				}

				if (hasCellData) {
					isEmptyData = false;
				}else {
					isFullData = false;
				}

				if (!ignoreSetDao) {
					if (cellValueObj == null) {
						try {
							PropertyDescriptor pd = new PropertyDescriptor(col.getDaoPropertyName(), dao.getClass());
							Class<?> propertyType = pd.getPropertyType();
							if(propertyType.getTypeName().equals("int")) {
								cellValueObj = 0;
							}
						} catch (IntrospectionException e) {
							e.printStackTrace();
							throw new AdempiereException(e.getMessage(), e);
						}
					}

					setDaoValue(dao, col.getDaoPropertyName(), cellValueObj);
				}
			}
		}
		
		// null mean don't need to save po (no properties for save or empty input data)
		// false mean input some item
		// true mean input full
		Boolean rowStatus = null;
		if (!hasDaoPropertyName) {
			rowStatus = null;
		}else if (isEmptyData) {
			rowStatus = null;
		}else if (!isFullData) {
			rowStatus = false;
		}else {
			rowStatus = true;
		}
		return new AbstractMap.SimpleEntry<>(total, rowStatus);
	}


	public void save(String trxName, X_ZZ_Application_Form applicationForm) {

		int total = 0;

		for (RowModel row : getRows()) {
			PO po = (PO) row.getData();
			if (po == null) {
				applicationForm.set_TrxName(trxName); // important
				po = poSupplier.apply(this, applicationForm);
			}

			Entry<Integer, Boolean> result = fillDaoData(getColumnInfos(), row, po);

			if (result.getValue() == null) {
				// delete row record if user cleared all input for this row
				po.delete(true);
			}else if (result.getValue()) {
				po.saveEx(trxName);
				total += result.getKey();

				// --- Save FileUpload columns as ATTACHMENTS (memory only) ---
				for (ColumnModel col : getColumnInfos()) {
					if (col.getDataType() != DataType.FileUpload) continue;

					UploadData upload = (UploadData) row.get(col);
					if (upload == null) continue;

					byte[] bytes = upload.getBytes(); // <-- in-memory only
					String fileName = upload.getFileName();

					if (bytes != null && bytes.length > 0 && org.apache.commons.lang3.StringUtils.isNotBlank(fileName)) {
						// one-entry semantics: delete-and-recreate
						AttachmentUtil.addOrReplaceAttachmentEntry(po, fileName, bytes, col.getBtText() ,trxName);

						// free memory for this row after persisting
						upload.setBytes(null);
					}

					// If you want to support "delete attachment when user clears the file":
					// else if ((bytes == null || bytes.length == 0)) {
					//     MAttachment att = MAttachment.get(Env.getCtx(), po.get_Table_ID(), po.get_ID(), trxName);
					//     if (att != null) att.delete(true);
					// }
				}
			} else {
				log.warning("row isn't input full data so don't save to dao also delete dao saved");
				po.delete(true);
			}
		}

		applicationForm.setZZTotalNumberApplied(total + applicationForm.getZZTotalNumberApplied());
	}


	public static void setCellValue(RowModel row, ColumnModel col, Object value) {
		log.info(String.format("Set cell value: row=%s, col=%s, col datatype=%s, value=%s", row, col.getTitle(), col.getDataType(), value));
		Object valueObj = row.get(col);
		if (valueObj instanceof AbstractListCellModel) {
			@SuppressWarnings("rawtypes")
			AbstractListCellModel listCellModel = (AbstractListCellModel)valueObj;
			listCellModel.setSelectedItemById(value);
		}else if (col.getDataType() == DataType.PositiveNumber) {
			IntData intData = (IntData)valueObj;
			intData.setValue(Util.convert((int)value));
		}else if (col.getDataType() == DataType.Text) {
			TextData textData = (TextData)valueObj;
			textData.setValue(Util.convertStr((String)value));
		}else if (col.getDataType() == DataType.Postal) {
			PostalData postalData = (PostalData)valueObj;
			postalData.setPostal(Util.convertStr((String)value));
		}else if (col.getDataType() == DataType.Label) {
			LabelData valueData = (LabelData)valueObj;
			valueData.setValue(Util.convertStr((String)value));
		}else if (col.getDataType() == DataType.FileUpload) {
			UploadData valueData = (UploadData)valueObj;
			valueData.setFileName(Util.convertStr((String)value));
		}else if (col.getDataType() == DataType.LearnerInfo && valueObj != null && !(value instanceof LearnerInputInfo)) {
			// don't need, already set when init row tile
		}else if (col.getDataType() == DataType.List && StringUtils.isNotBlank(col.getBeanPropertyName())) {
			if (value == null || (int)value == 0) {
				row.put(col, null);
			}else {
				for(Object selectedObj : col.getDataProvider()) {
					Object obj = TableModel.getDaoValue(selectedObj, col.getBeanPropertyName());
					if (com.google.common.base.Objects.equal(value, obj)) {
						row.put(col, selectedObj);
						break;
					}
				}
			}
		}else if (col.getDataType() == DataType.Date) {
			DateData valueData = (DateData)valueObj;
			valueData.setLocalDate((Timestamp)value);
		}else {
			row.put(col, value);
		}
	}

	@SuppressWarnings("unchecked")
	/**
	 * return entry with value already convert (example 0 for non input int), value is true in case input non-null
	 */
	public static <T> Entry<T, Boolean> getCellValue(RowModel row, ColumnModel col) {
		Object valueObj = row.get(col);

		if (valueObj instanceof AbstractListCellModel) {
			@SuppressWarnings("rawtypes")
			AbstractListCellModel listData = (AbstractListCellModel)valueObj;
			int id = listData.getSelectedID();
			return new AbstractMap.SimpleEntry<>((T)Integer.valueOf(id), id != 0);
		}else if (col.getDataType() == DataType.PositiveNumber) {
			IntData intData = (IntData)valueObj;
			if (intData.getValue() == null) {
				return new AbstractMap.SimpleEntry<>((T)Integer.valueOf(0), Boolean.FALSE);
			}else {
				return new AbstractMap.SimpleEntry<>((T)intData.getValue(), Boolean.TRUE);
			}

		}else if (col.getDataType() == DataType.Text) {
			TextData textData = (TextData)valueObj;
			if (StringUtils.isBlank(textData.getValue())) {
				return new AbstractMap.SimpleEntry<>(null, Boolean.FALSE);
			}else {
				return new AbstractMap.SimpleEntry<>((T)Util.convertStr(textData.getValue()), Boolean.TRUE);
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

	public static Object getDaoValue(Object dao, String propertyName) {
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
	public boolean isMatchingRow(RowModel row, Object dao, Collection<ColumnModel> keyColumns) {
		boolean isMatching = true;
		for (ColumnModel keyColumn : keyColumns) {
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
	public void fillRowDataFromDao(List<ColumnModel> cols, RowModel row, Object dao) {
		for (ColumnModel col : cols) {
			if (StringUtils.isNotBlank(col.getDaoPropertyName())){
				Object daoValue = null;
				if (col.getDataType() == DataType.FileUpload && StringUtils.isNotBlank(col.getDaoPropertyFileName())) {
					if (dao instanceof PO) {
						daoValue = AttachmentUtil.getFileNameFromAttachmentEntries(
								(PO) dao,
								col.getBtText(),
								((PO) dao).get_TrxName()
								);
					}
					//daoValue = getDaoValue(dao, col.getDaoPropertyFileName());
				}else if (col.getDataType() != DataType.FileUpload  && StringUtils.isNotBlank(col.getDaoPropertyName())) {
					daoValue = getDaoValue(dao, col.getDaoPropertyName());
				}

				TableModel.setCellValue(row, col, daoValue);
			}
		}
	}

	public void init(X_ZZ_Application_Form applicationForm) {
		init(applicationForm, null);
	}

	public void init(X_ZZ_Application_Form applicationForm, List<PO> savedDatas) {
		init(applicationForm, savedDatas, null);
	}

	public void init(X_ZZ_Application_Form applicationForm, List<PO> savedDatas, List<Map<ColumnModel, Object>> rowTitles) {
		if(rowTitles == null || rowTitles.size() == 0)
			init(applicationForm, savedDatas, null, null);
		else
			init(applicationForm, savedDatas, rowTitles, rowTitles.get(0).keySet());
	}

	public void init(X_ZZ_Application_Form applicationForm, 
			List<PO> savedDatas, List<Map<ColumnModel, Object>> rowTitles, Collection<ColumnModel> keyColumns) {
		// init rows with rowTitles
		if (rowTitles != null)
			for (Map<ColumnModel, Object> rowTitle : rowTitles) {
				createDetailRow(rowTitle);
			}

		// init rows with saved data
		if (savedDatas != null && savedDatas.size() > 0) {
			List<RowModel> matchedRows = new ArrayList<>();

			boolean learnerInfoKey = false;
			if (keyColumns != null && keyColumns.size() > 0) {
				if (lookupColByDataType(DataType.LearnerInfo, keyColumns) != null) {
					learnerInfoKey = true;
				}
			}

			for (PO dao : savedDatas) {
				boolean isMatching = false;
				if (keyColumns != null && keyColumns.size() > 0) {
					for (RowModel row : getRows()) {
						if (matchedRows.contains(row))
							continue;

						isMatching = isMatchingRow(row, dao, keyColumns);

						if (isMatching) {
							matchedRows.add(row);
							row.setData(dao);
							fillRowDataFromDao(getColumnInfos(), row, dao);
							break;
						}
					}
				}

				if (!isMatching && learnerInfoKey) {
					// moment don't handle this case, in case what to handle it need to change createDetailRow to create LearnerInputInfo for LearnerInfo column
				}else if(!isMatching) {
					RowModel row = createDetailRow();
					row.setData(dao);
					fillRowDataFromDao(getColumnInfos(), row, dao);
				}
			}

		}

		if (getRows().size() == 0 && createNewRowWhenEmpty) {
			createDetailRow();
		}

		updateExpressionCol();
		updateTotalRow();
	}

	/**
	 * @return the poSupplier
	 */
	public BiFunction<TableModel, X_ZZ_Application_Form, PO> getPoSupplier() {
		return poSupplier;
	}

	/**
	 * @param poSupplier the poSupplier to set
	 */
	public void setPoSupplier(BiFunction<TableModel, X_ZZ_Application_Form, PO> poSupplier) {
		this.poSupplier = poSupplier;
	}

	/**
	 * @return the createNewRowWhenEmpty
	 */
	public boolean isCreateNewRowWhenEmpty() {
		return createNewRowWhenEmpty;
	}

	/**
	 * @param createNewRowWhenEmpty the createNewRowWhenEmpty to set
	 */
	public void setCreateNewRowWhenEmpty(boolean createNewRowWhenEmpty) {
		this.createNewRowWhenEmpty = createNewRowWhenEmpty;
	}


	public static TableModel getBankInfo(X_ZZ_Application_Form applicationForm) {
		/* List<ColumnModel> cols = new ArrayList<ColumnModel>();
		cols.add(ColumnModel.getColText("Name of Bank", I_ZZBankingDetails.COLUMNNAME_BankName));
		cols.add(ColumnModel.getColText("Branch Name", I_ZZBankingDetails.COLUMNNAME_ZZ_Branch_Name));
		cols.add(ColumnModel.getColText("Branch Codes", I_ZZBankingDetails.COLUMNNAME_ZZ_Branch_Number));
		cols.add(ColumnModel.getColText("Account Number", I_ZZBankingDetails.COLUMNNAME_AccountNo));
		*/
		
		
	    List<ColumnModel> cols = new ArrayList<>();
	    ColumnModel cBank   = ColumnModel.getColText("Name of Bank",  I_ZZBankingDetails.COLUMNNAME_BankName).required();
	    ColumnModel cBranch = ColumnModel.getColText("Branch Name",   I_ZZBankingDetails.COLUMNNAME_ZZ_Branch_Name).required();
	    ColumnModel cCode   = ColumnModel.getColText("Branch Codes",  I_ZZBankingDetails.COLUMNNAME_ZZ_Branch_Number).required();
	    ColumnModel cAcct   = ColumnModel.getColText("Account Number",I_ZZBankingDetails.COLUMNNAME_AccountNo).required();

	    cols.add(cBank); cols.add(cBranch); cols.add(cCode); cols.add(cAcct);

		TableModel bankInfo = TableModel.getTableBean(TableModel.class, cols, false);
		bankInfo.setSubSectionHeader("TRAINING PROVIDER BANKING DETAILS");
		bankInfo.setPoSupplier((ann, appForm) -> {
			X_ZZBankingDetails po = new X_ZZBankingDetails(appForm.getCtx(), 0, null);
			po.setZZ_Application_Form_ID(appForm.getZZ_Application_Form_ID());
			return po;
		});


		List<PO> savedDaos = null;
		if(applicationForm != null) {
			String where = String.format("%s = ?", 
					I_ZZBankingDetails.COLUMNNAME_ZZ_Application_Form_ID);

			Query querySavedDaos = MTable.get(I_ZZBankingDetails.Table_ID).createQuery(where, null);
			savedDaos = querySavedDaos.setParameters(applicationForm.getZZ_Application_Form_ID()).list();
		}

		bankInfo.init(applicationForm, savedDaos);

		return bankInfo;
	}
	
	public boolean areMandatoryFieldsFilled() {
	    if (rows == null || rows.isEmpty()) return false; // bank form needs one row
	    for (RowModel r : rows) {
	        for (ColumnModel col : columnModels) {
	            if (!col.isMandatory()) continue;
	            var present = TableModel.getCellValue(r, col).getValue();
	            if (!Boolean.TRUE.equals(present)) return false;
	            //Object v = r.get(col);
	          //  if (v == null) return false;
	          //  if (v instanceof CharSequence && ((CharSequence) v).toString().trim().isEmpty()) return false;
	        }
	    }
	    return true;
	}

	/**
	 * @return the sclass
	 */
	public String getSclass() {
		return sclass;
	}

	/**
	 * @param sclass the sclass to set
	 */
	public void setSclass(String sclass) {
		this.sclass = sclass;
	}
}
