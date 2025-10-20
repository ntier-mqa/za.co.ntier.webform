package za.co.ntier.webform.sdr.component.bean;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.webui.exception.ApplicationException;
import org.apache.commons.beanutils.PropertyUtils;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.zkoss.bind.BindUtils;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.event.UploadEvent;

import za.co.ntier.api.model.X_ZZ_Application_Form;
import za.co.ntier.webform.sdr.component.bean.cell.IntCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.UploadCellModel;

public class TableModel {
	protected static final CLogger log = CLogger.getCLogger(TableModel.class);
	public final static String AnnexureTypeExitStrategy = "EXIT STRATEGY";
	public final static String AnnexureTypeTargetGroup = "TARGET GROUP";
	public final static String AnnexureTypeBudgetOverview = "BUDGET OVERVIEW";
	private String sclass = "";

	private String dataType;
	private boolean createNewRowWhenEmpty = true;

	public static <T extends TableModel> T getTableBean(Class<T> clazz, List<BaseColumnModel> columnInfos,
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
			Map<BaseColumnModel, Object> totalRow = new HashMap<>();
			for (BaseColumnModel columnInfo : columnInfos) {
				if (columnInfo.isCalTotal()) {
					totalRow.put(columnInfo, new IntCellModel(tableModel, 0));
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

	private List<BaseColumnModel> baseColumnModels;
	private BiFunction<TableModel, X_ZZ_Application_Form, PO> poSupplier;
	private List<RowModel> rows;
	private String sectionHeader;

	private boolean showAddButton = false;

	private boolean showTotal = false;

	private boolean showColumnHeader = true;

	private TableModel subAnnexure;

	private String subSectionHeader;

	private String tableTitle;

	private Map<BaseColumnModel, Object> totalRow;

	public void addRow() {
		createDetailRow();
		BindUtils.postNotifyChange(this, "rows");
	}

	public void cmdValueChanged (Map<BaseColumnModel, Object> row,
			BaseColumnModel col,
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
	public void cmdSelected (Map<BaseColumnModel, Object> row,
			BaseColumnModel col,
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

	public RowModel createDetailRow(Map<BaseColumnModel, Object> rowTitle) {

		RowModel row = new RowModel(this);

		for (BaseColumnModel columnInfo : baseColumnModels) {
			BaseCellModel cellModel = columnInfo.getCellModel(this, row);
			row.put(columnInfo, cellModel);
		}

		if (rowTitle != null) {
			for (Entry<BaseColumnModel, Object> colTile : rowTitle.entrySet()) {
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

		for (BaseColumnModel col:getColumnInfos()) {
			if (col.isCalTotal()) {
				updateTotalRow(col, false);
			}
		}
	}

	/**
	 * update total cell of column has data change
	 * @param col
	 * @param needNotify
	 */
	public void updateTotalRow(BaseColumnModel col, boolean needNotify) {
		Integer total = 0;
		for (Map<BaseColumnModel, BaseCellModel> r : getRows()) {
			IntCellModel intCellModel = (IntCellModel)r.get(col);
			if (intCellModel.getValue() != null) {
				total += intCellModel.getValue();
			}
		}

		IntCellModel totalValue = (IntCellModel)totalRow.get(col);
		totalValue.setValue(total);

		if(needNotify) {
			BindUtils.postNotifyChange(totalValue, "value");
		}
	}

	/**
	 * @return the baseColumnModels
	 */
	public List<BaseColumnModel> getColumnInfos() {
		return baseColumnModels;
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
	public Map<BaseColumnModel, Object> getTotalRow() {
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

	public void numChange(RowModel row, BaseColumnModel col, InputEvent event) {
		// update total row
		if (showTotal && row.get(col) instanceof IntCellModel) {
			updateTotalRow(col, true);
		}

		if (row.get(col) instanceof IntCellModel) {
			updateExpressionCol (row, true);
		}

	}

	public void updateExpressionCol () {
		boolean hasExpressionCol = false;
		for (BaseColumnModel colTotal : getColumnInfos()) {
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
		for (BaseColumnModel colTotal : getColumnInfos()) {
			if (colTotal.getExpression() != null) {
				Integer value = colTotal.getExpression().apply(row);
				BaseCellModel expressionLable = row.get(colTotal);
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
	 * @param baseColumnModels the baseColumnModels to set
	 */
	public void setColumnInfos(List<BaseColumnModel> columnInfos) {
		this.baseColumnModels = columnInfos;
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
	public void setTotalRow(Map<BaseColumnModel, Object> totalRow) {
		this.totalRow = totalRow;
	}


	public void uploadFile(Map<BaseColumnModel, Object> row, BaseColumnModel col, UploadEvent event) {
		UploadCellModel ud = (UploadCellModel) row.get(col);
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

	public void fillDaoData (Collection<BaseColumnModel> cols, RowModel row, Object dao){
		for (BaseColumnModel col:cols) {
			BaseCellModel cellModel = row.get(col);
			cellModel.setValueToDao(dao);
		}

		//Object cellValueObj = null;
		/*if (col.getDataType() == DataType.PositiveNumber) {
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
					DateCellModel dateData = (DateCellModel)row.get(col);
					cellValueObj = dateData.getTimestamp();
					if (cellValueObj != null) {
						hasCellData = true;
					}
				}else if (col.getDataType() == DataType.FileUpload) {
					// Martin changed to save to attachments instead to a binary file
					hasCellData = Boolean.TRUE;// upload data become option
					ignoreSetDao = true; // never set DAO properties for files anymore
				}else if (col instanceof ListColumnModel) {
					ListColumnModel<?> colList = (ListColumnModel<?>)col;
					Object selectedObj = row.get(col);
					if (StringUtils.isNotBlank(colList.getBeanPropertyName())) {
						if (selectedObj != null) {
							cellValueObj = TableModel.getDaoValue(selectedObj, colList.getBeanPropertyName());
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

		}*/

		// null mean don't need to save po (no properties for save or empty input data)
		// false mean input some item
		// true mean input full
	}


	public void save(String trxName, X_ZZ_Application_Form applicationForm) {
		for (RowModel row : getRows()) {
			PO po = row.getData();
			if (po == null) {
				applicationForm.set_TrxName(trxName); // important
				po = poSupplier.apply(this, applicationForm);
			}

			if (row.inputState() == RowModel.INPUT_STATE_EMPTY) {
				// delete row record if user cleared all input for this row
				po.delete(true);
			}else if (row.inputState() == RowModel.INPUT_STATE_FULL) {

				po.saveEx(trxName);
				row.saveUploadFiles(po, trxName);

			}else {// some required column is missing (validate getting fail)
				throw new AdempiereException("Please input full required data for all columns in the row or clear all data to remove the row (validate is fail).");
			}
		}

		applicationForm.setZZTotalNumberApplied(this.getGrandTotal() + applicationForm.getZZTotalNumberApplied());
	}


	private int getGrandTotal() {
		// TODO Auto-generated method stub
		return 0;
	}

	public static void setCellValue(RowModel row, BaseColumnModel col, Object value) {
		BaseCellModel valueObj = row.get(col);
		log.info(String.format("Set cell value: row=%s, col=%s, cell datatype=%s, value=%s", row, col.getTitle(), valueObj.getClass().getName(), value));

		valueObj.setCellValue(value);
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


	public void init(X_ZZ_Application_Form applicationForm) {
		init(applicationForm, null);
	}

	public void init(X_ZZ_Application_Form applicationForm, List<PO> savedDatas) {
		init(applicationForm, savedDatas, null);
	}

	public void init(X_ZZ_Application_Form applicationForm, List<PO> savedDatas, List<Map<BaseColumnModel, Object>> rowTitles) {
		if(rowTitles == null || rowTitles.size() == 0)
			init(applicationForm, savedDatas, null, null);
		else
			init(applicationForm, savedDatas, rowTitles, rowTitles.get(0).keySet());
	}

	public void init(X_ZZ_Application_Form applicationForm,
			List<PO> savedDatas, List<Map<BaseColumnModel, Object>> rowTitles, Collection<BaseColumnModel> keyColumns) {
		// init rows with rowTitles
		if (rowTitles != null)
			for (Map<BaseColumnModel, Object> rowTitle : rowTitles) {
				createDetailRow(rowTitle);
			}

		// init rows with saved data
		if (savedDatas != null && savedDatas.size() > 0) {
			List<RowModel> matchedRows = new ArrayList<>();

			for (PO dao : savedDatas) {
				boolean isMatching = false;
				if (keyColumns != null && keyColumns.size() > 0) {
					for (RowModel row : getRows()) {
						if (matchedRows.contains(row))
							continue;

						isMatching = row.isMatchingRow(keyColumns, dao);
						if (isMatching) {
							matchedRows.add(row);
							row.setData(dao);
							row.fillRowDataFromDao(dao);
							break;
						}
					}
				}

				if(!isMatching) {
					RowModel row = createDetailRow();
					row.setData(dao);
					row.fillRowDataFromDao(dao);
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
