package za.co.ntier.webform.sdr.component.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.webui.exception.ApplicationException;
import org.apache.commons.lang3.StringUtils;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.jfree.util.Log;

import za.co.ntier.api.model.X_ZZQualification;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.sdr.component.bean.CellModel.InputCheckResult;
import za.co.ntier.webform.sdr.component.bean.cell.UploadCellModel;
import za.co.ntier.webform.sdr.component.bean.column.PresetTitleColumnModel;

public class RowModel extends HashMap<ColumnModel, CellModel> implements ISaveForm{
	private static final CLogger log = CLogger.getCLogger(RowModel.class);
	private TableModel tableModel;
	public RowModel(TableModel tableModel) {
		this.setTableModel(tableModel);
	}
	private static final long serialVersionUID = 3444756682531476154L;
	public static final int INPUT_STATE_EMPTY = 0;
	public static final int INPUT_STATE_FULL_REQUIRED = 1;
	
	/**
	 * not null, in case have daoManage take po from daoManage first and failback to rowData 
	 */
	private RowData rowData;
	
	
	
	public static class RowData{
		private RowModel rowModel;
		public RowData(RowModel rowModel) {
			this.rowModel = rowModel;
		}
		
		private Map<String, PO> mData = new HashMap<>();
		
		public PO getDataNewWhenNull(String tableName) {
			return getData(tableName, true);
		}
		
		public PO getDataNullable(String tableName) {
			return getData(tableName, false);
		}
		
		public PO getDataNullable(CellModel cellModel) {
			return getDataNullable(cellModel.getTableName());
		}
		
		public PO getDataNewWhenNull(CellModel cellModel) {
			return getDataNewWhenNull(cellModel.getTableName());
		}
		
		public static List<List<PO>> standardToMultiPo(List<PO> singlePOs) {
			if (singlePOs != null) {
				List<List<PO>> multiPOs = new ArrayList<>();
				singlePOs.forEach(po -> {
					List<PO> multiPO = new ArrayList<>();
					multiPO.add(po);
					multiPOs.add(multiPO);
				});
				
				return multiPOs;
			}else {
				return null;
			}
		}
		
		public static List<List<PO>> mergedList(List<PO> po1s, List<PO> po2s){
			List<List<PO>> savedObjs = new ArrayList<>();
			
			for(int i = 0; i < po1s.size(); i++) {
				List<PO> savedRowObjs = new ArrayList<>();
				savedRowObjs.add(po1s.get(i));
				savedRowObjs.add(po2s.get(i));
				
				savedObjs.add(savedRowObjs);
			}
			
			return savedObjs;
		}
		
		/**
		 * @return the data
		 */
		private PO getData(String tableName, boolean isCreateNew) {
			if (mData.get(tableName) == null && isCreateNew) {
				synchronized (tableName) {
					if (mData.get(tableName) == null) {
						PO po = null;
						if (rowModel.getTableModel().getPoSupplier() != null) {
							po = rowModel.getTableModel().getPoSupplier().apply(rowModel);
						}else {
							po = MTable.get(Env.getCtx(), tableName).getPO(0, null);
						}
						
						mData.put(tableName, po);
					}
				}
			}
			
			return mData.get(tableName);
		}
		
		/**
		 * @param data the data to set
		 */
		protected void addData(PO data) {
			mData.put(data.get_TableName(), data);
		}
		
		/**
		 * clear old record and add new one
		 * @param datas
		 */
		protected void setDatas(List<PO> datas) {
			mData.clear();
			if (datas != null)
				datas.forEach(po -> addData(po));
		}
		
		public boolean isEmpty () {
			return mData.isEmpty();
		}
		
		public void deleteData(String trxName) {
			Iterator<Map.Entry<String, PO>> iterator = mData.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, PO> entry = iterator.next();
				if (entry.getValue() != null && !rowModel.isIgnore(entry.getValue().get_TableName())) {
					entry.getValue().delete(true, trxName);
					iterator.remove();
				}
			}
			
			if (rowModel.getTableModel().getAfterDelete() != null) {
				rowModel.getTableModel().getAfterDelete().apply(trxName, rowModel);
			}
		}
		
		public void saveDatas(String trxName) {
			Iterator<Map.Entry<String, PO>> iterator = mData.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, PO> entry = iterator.next();
				
				if(rowModel.isIgnore(entry.getKey()))
					continue;
				
				if (rowModel.getTableModel().getBeforeSave() != null) {
					rowModel.getTableModel().getBeforeSave().apply(entry.getValue(), rowModel);
				}
				
				entry.getValue().saveEx(trxName);
				 
				for (CellModel cellModel : rowModel.values()) {
					if (cellModel.getTableName().equals(entry.getValue().get_TableName()) && 
							CellModel.BTUPLOAD_CELL == cellModel.getCellType()) {
						((UploadCellModel)cellModel).attachFile(entry.getValue(), trxName);
					}
				}
				
				if (rowModel.getTableModel().getAfterSave() != null) {
					rowModel.getTableModel().getAfterSave().apply(entry.getValue(), rowModel);
				}
			}
		}
	}
	
	public boolean isIgnore (String tableName) {
		for (ColumnModel colModel : getTableModel().getColumnInfos()) {
			if (get(colModel).getTableName().equals(tableName) && !get(colModel).isIgnore()) {
				return false;
			}
		}
		
		return true;
	}
	
	public void resetRow() {
		for (CellModel cell : values()) {
			cell.reset(true);
		}
		
		for (CellModel cell : values()) {
			cell.initDefaultValue();
		}
	}

	/**
	 * @return the tableModel
	 */
	public TableModel getTableModel() {
		return tableModel;
	}

	/**
	 * @param tableModel the tableModel to set
	 */
	public void setTableModel(TableModel tableModel) {
		this.tableModel = tableModel;
	}
	
	public InputCheckResult parseInputState() {
		InputCheckResult rowInputCheckResult = new InputCheckResult();
		rowInputCheckResult.setEmpty(true)
			.setFillMandatory(true)
			.setNotChange(true)
			.setHasMandatory(false)
			.setIgnore(true);
		
		for (IInputState cellModel : values()) {
			if(cellModel.isIgnore())
				continue;
			
			rowInputCheckResult.setIgnore(false);
			
			InputCheckResult cellInputCheckResult = cellModel.parseInputState();
			
			if (!cellInputCheckResult.getEmpty()) {// has at least once field have value
				rowInputCheckResult.setEmpty(false);
			}
			
			if (cellInputCheckResult.getHasMandatory()) {
				rowInputCheckResult.setHasMandatory(true);
			}
			
			if (!cellInputCheckResult.getFillMandatory()) {
				rowInputCheckResult.setFillMandatory(false);// has at least once field have value
				log.warning("not input for mandatory field:" + cellModel.toString());
			}
			
			if (!cellInputCheckResult.getNotChange()) {
				rowInputCheckResult.setNotChange(false);// has at least once field has change when compare to default
			}
		}
		
		return rowInputCheckResult;
	}

	public void saveUploadFiles(PO po, String trxName) {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * @param keyColumns
	 * @param dao
	 * @return
	 */
	public boolean isMatchingRow(Collection<ColumnModel> keyColumns, List<PO> daos) {
		boolean isMatching = true;
		for (ColumnModel col : keyColumns) {
			PresetTitleColumnModel<?> presetTitleCol = (PresetTitleColumnModel<?>) col;
			isMatching = isMatching && presetTitleCol.getMatchingLoaded().apply(this, daos);
			if (!isMatching) {
				return false;
			}
		}
		
		return true;
	}

	public void fillRowDataFromDao() {
		for (CellModel cellModel : values()) {
			boolean isDaoValue = false;
			if (cellModel instanceof UploadCellModel || cellModel.getColModel().getDaoPropertyName() != null) {
				isDaoValue = true;
			}
			
			if (isDaoValue) {
				PO daoPerCol = null;
				if (tableModel.getDaoManage() != null) {
					daoPerCol = tableModel.getDaoManage().getDao(cellModel.getTableName());
				}
				
				if (daoPerCol == null)
					daoPerCol = getRowData().getDataNullable(cellModel.getTableName());
				
				if (daoPerCol != null) {
					cellModel.setValueFromDao(daoPerCol);
				}else {
					if (Log.isInfoEnabled())
						log.info(String.format("not yet dao for table %s to set to properties %s", cellModel.getTableName(), cellModel.getColModel().getDaoPropertyName()));
				}
			}
		}
		
		if (getTableModel().getAfterFillFromDaoHandle() != null)
			getTableModel().getAfterFillFromDaoHandle().accept(this);
		
	}

	InputCheckResult rowInputCheckResult;
	
	protected boolean ignoreInput() {
		boolean ignore = (rowInputCheckResult.getNotChange() && rowInputCheckResult.nonMandatoryOrNotFullFill() && getRowData().isEmpty());
		
		if (!ignore)
			ignore = (rowInputCheckResult.getNotChange() && rowInputCheckResult.nonMandatoryOrNotFullFill() && getRowData().isEmpty());
		
		return ignore;
			
	}
	
	@Override
	public void syncUIToDao(String trxName) {
		rowInputCheckResult = parseInputState();
		
		if (rowInputCheckResult.getIgnore())
			return;
		
		// empty value then delete current data
		// improve to handle case po manage
		if (rowInputCheckResult.getEmpty()) {
			getRowData().deleteData(trxName);
			return;
		}
		
		// empty (or default) value and not touch mandatory (data isn't saved
		if (rowInputCheckResult.getNotChange() && rowInputCheckResult.nonMandatoryOrNotFullFill() && getRowData().isEmpty()) 
			return;
		
		// touch mandatory and not yet full fill mandatory
		if (rowInputCheckResult.haveMandatoryAndNotFullFill()) {
			throw new AdempiereException("Mandatory not yet full fill");
		}
				
		PO daoPerCol = null;
		for (CellModel cellModel : values()) {
			if (StringUtils.isBlank(cellModel.getColModel().getDaoPropertyName()) 
					|| StringUtils.isBlank(cellModel.getTableName()) 
					|| isIgnore(cellModel.getTableName()))
				continue;
			
			if (StringUtils.isNotBlank(cellModel.getColModel().getDaoPropertyName())) {
				daoPerCol = getDaoAllway(cellModel);
				cellModel.saveUIToDao(daoPerCol);
			}
			
			if (CellModel.BTUPLOAD_CELL == cellModel.getCellType() && cellModel.isIgnore()) {// remove data when button hidden
				((UploadCellModel)cellModel).cmdRemoveAttachment();// mark remove attachment of this button
			}
			
			if (CellModel.BTUPLOAD_CELL != cellModel.getCellType() && cellModel.isIgnore()) {
				// TODO:reset cell to null
			}
		}
		
		
			
	}
	
	/**
	 * get current dao, don't ask PoSupplier create new one when null
	 * @param cellModel
	 * @return
	 */
	public PO getCurrentDao (CellModel cellModel) {
		PO currentDao = null;
		if (tableModel.getDaoManage() != null) {
			currentDao = tableModel.getDaoManage().getDao(cellModel.getTableName());
		}
		
		if (currentDao != null) {
			return currentDao;
		}
		
		currentDao = getRowData().getDataNullable(cellModel);
		if (currentDao != null) {
			return currentDao;
		}
		
		return currentDao;
	}
	
	private PO getDaoFromDaoManage(CellModel cellModel) {
		if (tableModel.getDaoManage() != null)
			return tableModel.getDaoManage().getDaoForSave(cellModel.getTableName());
		
		return null;
	}
	
	private PO getDaoAllway(CellModel cellModel) {
		PO daoPerCol = null;
		
		daoPerCol =  getDaoFromDaoManage(cellModel);
		
		if (daoPerCol == null) {
			daoPerCol = getRowData().getDataNewWhenNull(cellModel);
		}
		
		return daoPerCol;
	}
	
	/**
	 * @param for case use dao manage
	 * @param trxName
	 */
	@Override
	public void saveAttachment(String trxName) {
		
		for (CellModel cellModel : values()) {
			if (CellModel.BTUPLOAD_CELL == cellModel.getCellType()) {
				PO daoPerCol = getDaoFromDaoManage(cellModel);
				if (daoPerCol != null)
					((UploadCellModel)cellModel).attachFile(daoPerCol, trxName);
			}
		}
	}
	
	@Override
	public boolean validate(Boolean isSubmit) {
		// will delete so don't need to validate
		if(!getTableModel().isUsed())
			return true;
		
		boolean isValid = true;
		
		for (CellModel validation : this.values()) {
			if (!validation.validate()) {
				isValid = false;
			}
			
			if (validation.getColModel().getComposeValidator() != null &&
					!validation.getColModel().getComposeValidator().apply(validation)) {
				isValid = false;
			}
		}
		return isValid;
	}
	
	@Override
	public void saveToDb(String trxName) {
		if (getTableModel().getBeforeSave() != null) {
			getTableModel().getBeforeSave().apply(null, this);
		}
		
		if (tableModel.getDaoManage() == null) {
			if(!getRowData().isEmpty() && !getTableModel().isUsed()) {
				getRowData().deleteData(trxName);
				resetRow();
				return;
			}
			
			if (!ignoreInput())
				getRowData().saveDatas(trxName);
			
		}
		
		if (getTableModel().getAfterSave() != null) {
			getTableModel().getAfterSave().apply(null, this);
		}
		
	}

	public RowData getRowData() {
		return rowData;
	}

	public void setRowData(RowData rowData) {
		this.rowData = rowData;
	}

	
	public <T extends PO> T getDataOneRow(Class<T> zclass, String tableName) {
		PO dao = null;
		if (tableModel.getDaoManage() != null) {
			dao = tableModel.getDaoManage().getDao(tableName);
		}
		
		if (dao == null) {
			dao = getRowData().getDataNullable(tableName);
		}
		
		return zclass.cast(dao);
	}
	
	public void setDataOneRow(PO dao) {
		getRowData().addData(dao);
	}
}
