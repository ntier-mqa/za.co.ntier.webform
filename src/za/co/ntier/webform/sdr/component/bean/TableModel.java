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
import java.util.function.Consumer;
import java.util.function.Function;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.webui.exception.ApplicationException;
import org.apache.commons.beanutils.PropertyUtils;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.zkoss.bind.BindUtils;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.event.UploadEvent;

import za.co.ntier.webform.sdr.component.bean.CellModel.InputCheckResult;
import za.co.ntier.webform.sdr.component.bean.cell.IntCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.UploadCellModel;

public class TableModel implements ISaveForm {
	private ISaveApp saveApp;
	
	private RowModel activeRow;
	
	public RowModel getActiveRow() {
		return activeRow;
	}
	
	private RowModel virtualRow;
	public RowModel getVirtualRow() {
		if (virtualRow == null) {
			virtualRow = doCreateDetailRow(null);
			if (getRows().size() == 0) {
				init();
			}
			
			setActiveRow(rows.get(0));
		}
		
		return virtualRow;
	}
	public int getCurrentIndex () {
		return getRows().indexOf(activeRow);
	}
	public boolean navNextRow() {
		// first
		if (activeRow == null && getRows().size() > 0) {
			setActiveRow(getRows().get(0));
			return true;
		}
		
		// not yet init
		if (activeRow == null && getRows().size() == 0) {
			return false;
		}
		
		// already end
		if (activeRow != null && getRows().indexOf(activeRow) == getRows().size() - 1) {
			return false;
		}
		
		// just next
		if (activeRow != null && getRows().indexOf(activeRow) < getRows().size() - 1){
			int newIndex = getRows().indexOf(activeRow) + 1;
			setActiveRow(getRows().get(newIndex));
			return true;
		}
		
		
		// not yet handle
		throw new AdempiereException("wrong condition - rows size:" + getRows().size());

	}
	
	public void deleteRow() {
		int newIndex = getRows().indexOf(activeRow) - 1;
		
		if (activeRow != null)
			removeRow(activeRow);
		
		if (newIndex < 0 && getRows().size() > 0) {
			newIndex = 0;
		}
		
		if (getRows().size() > 0)// always exists 1 record because latest record is reset not remove
			setActiveRow(getRows().get(newIndex));
		
	}
	
	List<PO> removedRows = new ArrayList<>();
	public void removeRow(RowModel row) {
		if (row.getData() != null) {
			removedRows.add(row.getData());
			row.setData(null);
		}
		if (getRows().size() > 1)
			getRows().remove(row);
		else {
			row.resetRow();
		}
		
		updateTotalRow();
		BindUtils.postNotifyChange(this, "rows");
	}
	
	protected void setActiveRow(RowModel newActiveRow) {
		// copy virtual row to active row
		copyRow(virtualRow, activeRow);
		// reset virtual row
		virtualRow.resetRow();
		// set new active
		activeRow = newActiveRow;
		// copy new row to virtual row
		copyRow(activeRow, virtualRow, true);
	}
	
	public boolean navPrevRow() {
		// not yet init
		if (activeRow == null) {
			return false;
		}
		
		int newIndex = getRows().indexOf(activeRow) - 1;

		// not yet init or already first
		if (activeRow != null && newIndex < 0) {
			return false;
		}
		
		// just prev
		if (activeRow != null && newIndex >= 0){
			setActiveRow(getRows().get(newIndex));
			return true;
		}
		
		// not yet handle
		throw new AdempiereException("wrong condition - rows size:" + getRows().size());
	}
	
	public void newRow() {
		RowModel newRow = addNewRow(activeRow);
		
		setActiveRow(newRow);
	}
	
	public RowModel addNewRow(RowModel currentRow) {
		RowModel newRow = doCreateDetailRow(null);
		
		if (currentRow == null) {
			getRows().add(newRow);
		}else {
			int index = getRows().indexOf(currentRow);
			getRows().add(index + 1, newRow);
		}
		
		BindUtils.postNotifyChange(this, "rows");
		
		return newRow;
	}
	
	public void copyRow(RowModel rowSrc, RowModel rowDes) {
		copyRow(rowSrc, rowDes, false);
	}
	public void copyRow(RowModel rowSrc, RowModel rowDes, boolean copyPo) {
		if (rowSrc != null && rowDes != null) {
			for (ColumnModel col : rowSrc.getTableModel().getColumnInfos()) {
				rowSrc.get(col).copyTo(rowDes.get(col));
			}
			
			if (copyPo)
				rowDes.setData(rowSrc.getData());
		}
			
	}
	
	protected static final CLogger log = CLogger.getCLogger(TableModel.class);
	private String sclass = "";
	
	private ViewType viewMode = ViewType.VIEW_FORM;	
	
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

	private List<ColumnModel> columnModels;
	private Function<RowModel, PO> poSupplier;
	
	public static class DaoManage{
		private String trxName;
		
		private Map<String, PO> daos = new HashMap<>();
		
		private Map<String, Function<DaoManage, PO>> poSuppliers = new HashMap<>();
		
		/**
		 * @return the dao
		 */
		public PO getDao(String tableName) {
			if (tableName == null && daos.size() == 1) {
				return daos.values().iterator().next();
			}else {
				return daos.get(tableName);
			}

		}
		

		public PO getDaoForSave(String tableName) {
			PO dao = getDao(tableName);
			if (dao == null && getPoSupplier(tableName) != null) {
				dao = getPoSupplier(tableName).apply(this);
				daos.put(tableName, dao);
			}
			return dao;
		}

		/**
		 * @param dao the dao to set
		 */
		public void setDao(PO dao) {
			daos.put(dao.get_TableName(), dao);
		}
		
		public void saveDao(String trxName) {
			daos.forEach(
					(t, po) -> po.saveEx(trxName)
					);
		}

		private Function<DaoManage, PO> getPoSupplier(String tableName) {
			return poSuppliers.get(tableName);
		}

		public void setPoSupplier(String tableName, Function<DaoManage, PO> poSupplier) {
			poSuppliers.put(tableName, poSupplier);
		}

		public String getTrxName() {
			return trxName;
		}

		public void setTrxName(String trxName) {
			this.trxName = trxName;
		}
	}
	
	private DaoManage daoManage;

	
	private List<RowModel> rows;
	private String sectionHeader;

	private boolean showAddButton = false;

	private boolean showTotal = false;

	private boolean showColumnHeader = true;

	private String subSectionHeader;

	private String tableTitle;

	private Map<ColumnModel, Object> totalRow;

	public void cmdValueChanged (RowModel row,
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
	public void cmdSelected (RowModel row,
			ColumnModel col,
			Event event){

		ISelectable selectable = (ISelectable)row.get(col);
		selectable.cmdSelected(event);
	}
	
	public void cmdCheckeck (RowModel row,
			ColumnModel col,
			CheckEvent event){
		ICheckbox checkbox = (ICheckbox)row.get(col);
		checkbox.cmdChecked(event);
	}
	

	public void cmdCellAction(RowModel row, ColumnModel col, Event event) {
		CellModel cellModel = row.get(col);
		cellModel.cmdCellAction(event);
		
	}
	
	public void cmdUploadFile(RowModel row, ColumnModel col, UploadEvent event) {
		UploadCellModel uploadCellModel = (UploadCellModel)row.get(col);
		uploadCellModel.cmdUploadFile(event);
	}
	
	public void cmdRemoveAttachment(RowModel row, ColumnModel col, Event event) {
		UploadCellModel uploadCellModel = (UploadCellModel)row.get(col);
		uploadCellModel.cmdRemoveAttachment();
	}
	
	public RowModel createDetailRow() {
		return createDetailRow(null);
	}

	public RowModel createDetailRow(Map<ColumnModel, Object> rowTitle) {
		RowModel row = doCreateDetailRow(rowTitle);
		getRows().add(row);
		return row;
	}
	
	public RowModel doCreateDetailRow(Map<ColumnModel, Object> rowTitle) {

		RowModel row = new RowModel(this);

		for (ColumnModel columnInfo : columnModels) {
			CellModel cellModel = columnInfo.getCellModel(this, row);
			row.put(columnInfo, cellModel);
		}

		row.forEach((colModel, cellModel) -> {
			cellModel.initDefaultValue();
		});
		
		if (rowTitle != null) {
			 for (Entry<ColumnModel, Object> presetTitleInfo : rowTitle.entrySet()) {
				 row.get(presetTitleInfo.getKey()).setValue(presetTitleInfo.getValue()); 
			 }
			 
		}

		if (decoratorCell != null) {
			decoratorCell.accept(row);
		}

		return row;
	}

	private Consumer<RowModel> decoratorCell;
	private Function<PO, Boolean> beforeSave;

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
	public void updateTotalRow(ColumnModel col, boolean needNotify) {
		Integer total = 0;
		for (RowModel r : getRows()) {
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
		if (showTotal && row.get(col) instanceof IntCellModel) {
			updateTotalRow(col, true);
		}

		if (row.get(col) instanceof IntCellModel) {
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
				CellModel expressionLable = row.get(colTotal);
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


	public void init() {
		init(null);
	}

	public void init(List<PO> savedDatas) {
		init(savedDatas, null);
	}

	public void init(List<PO> savedDatas, List<Map<ColumnModel, Object>> rowTitles) {
		if(rowTitles == null || rowTitles.size() == 0)
			init(savedDatas, null, null);
		else
			init(savedDatas, rowTitles, rowTitles.get(0).keySet());
	}

	public void init(
			List<PO> savedDatas, List<Map<ColumnModel, Object>> rowTitles, Collection<ColumnModel> keyColumns) {
		// init rows with rowTitles
		if (rowTitles != null)
			for (Map<ColumnModel, Object> rowTitle : rowTitles) {
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
							row.fillRowDataFromDao();
							break;
						}
					}
				}

				if(!isMatching) {
					RowModel row = createDetailRow();
					row.setData(dao);
					row.fillRowDataFromDao();
				}
			}
		}

		if (daoManage != null) {
			RowModel row = createDetailRow();
			row.fillRowDataFromDao();
		}
		
		if (getRows().size() == 0 && createNewRowWhenEmpty) {
			createDetailRow();
		}

		updateExpressionCol();
		updateTotalRow();
	}

	public void reloadDao() {
		getRow().fillRowDataFromDao();
	}
	
	/**
	 * @return the poSupplier
	 */
	public Function<RowModel, PO> getPoSupplier() {
		return poSupplier;
	}

	/**
	 * @param poSupplier the poSupplier to set
	 */
	public void setPoSupplier(Function<RowModel, PO> poSupplier) {
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

	@Override
	public void syncUIToDao(String trxName) {
		for (PO removedRow : removedRows) {
			removedRow.deleteEx(true, trxName);
		}
		
		if (virtualRow != null) {
			// sync value from virtual row to active to validate and save
			copyRow(virtualRow, activeRow);
		}
			
		
		ISaveForm.batchSyncToDao(getRows(), trxName);
	}
	
	@Override
	public void saveAttachment(String trxName) {
		for(RowModel rowModel : getRows()) {
			rowModel.saveAttachment(trxName);
		}
		
	}

	/**
	 * @return the daoManage
	 */
	public DaoManage getDaoManage() {
		return daoManage;
	}

	/**
	 * @param daoManage the daoManage to set
	 */
	public void setDaoManage(DaoManage daoManage) {
		this.daoManage = daoManage;
	}

	 
	public enum ViewType {
	    VIEW_FORM,
	    VIEW_GRID,
	    VIEW_CARD;
	}
	/**
	 * @return the formView
	 */
	public boolean isFormView() {
		return viewMode == ViewType.VIEW_FORM;
	}
	
	public boolean isGridView() {
		return viewMode == ViewType.VIEW_GRID;
	}
	
	public boolean isCardView() {
		return viewMode == ViewType.VIEW_CARD;
	}

	/**
	 * @param formView the formView to set
	 */
	public void setViewModel(ViewType viewMode) {
		this.viewMode = viewMode;
	}
	
	public ViewType getViewModel() {
		return viewMode;
	}


	public List<RowModel> getValidateRows (){
		List<RowModel> validateRows = null;
		if (virtualRow != null) {
			validateRows = new ArrayList<>(rows);
			validateRows.remove(activeRow);
			validateRows.add(virtualRow);
		}else {
			validateRows = rows;
		}
		
		return validateRows;
	}
	
	@Override
	public boolean validate(Boolean isSubmit) {
		return ISaveForm.validates(getValidateRows(), null);
	}
	@Override
	public void saveToDb(String trxName) {
		ISaveForm.batchSaveToDb(getRows(), trxName);
		
	}
	public Function<PO, Boolean> getBeforeSave() {
		return beforeSave;
	}
	public void setBeforeSave(Function<PO, Boolean> beforeSave) {
		this.beforeSave = beforeSave;
	}
	
	public ISaveApp getSaveApp() {
		return saveApp;
	}
	public void setSaveApp(ISaveApp saveApp) {
		this.saveApp = saveApp;
	}

	public InputCheckResult parseInputState() {
		
		InputCheckResult rowInputCheckResult = new InputCheckResult();
		rowInputCheckResult.setEmpty(true).setFillMandatory(true).setNotChange(true);
		
		for (IInputState row : getRows()) {
			if(row.isIgnore())
				continue;
			InputCheckResult cellInputCheckResult = row.parseInputState();
			if (!cellInputCheckResult.getEmpty()) {// has at least once field have value
				rowInputCheckResult.setEmpty(false);
			}
			
			if (!cellInputCheckResult.getFillMandatory()) {
				rowInputCheckResult.setFillMandatory(false);// has at least once field have value
				log.warning("not input mandatory field on row:" + getRows().indexOf(row));
			}
			
			if (!cellInputCheckResult.getNotChange()) {
				rowInputCheckResult.setNotChange(false);// has at least once field has change when compare to default
			}
		}
				
		return rowInputCheckResult;
	}

}
