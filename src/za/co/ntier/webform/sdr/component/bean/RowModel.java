package za.co.ntier.webform.sdr.component.bean;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;

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
	private PO data;

	public void resetRow() {
		for (CellModel cell : values()) {
			cell.reset(true);
		}
		
		for (CellModel cell : values()) {
			cell.initDefaultValue();
		}
	}
	/**
	 * @return the data
	 */
	public PO getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(PO data) {
		this.data = data;
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
	public boolean isMatchingRow(Collection<ColumnModel> keyColumns, PO dao) {
		boolean isMatching = true;
		for (ColumnModel col : keyColumns) {
			PresetTitleColumnModel<?> presetTitleCol = (PresetTitleColumnModel<?>) col;
			isMatching = isMatching && presetTitleCol.getMatchingLoaded().apply(this, dao);
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
					daoPerCol = tableModel.getDaoManage().getDao(cellModel.getColModel().getTableName());
				}
				
				if (daoPerCol == null)
					daoPerCol = getData();
				
				if (daoPerCol != null) {
					cellModel.setValueFromDao(daoPerCol);
				}else {
					if (Log.isInfoEnabled())
						log.info(String.format("not yet dao for table %s to set to properties %s", cellModel.getColModel().getTableName(), cellModel.getColModel().getDaoPropertyName()));
				}
			}
		}
		
		if (getTableModel().getAfterFillFromDaoHandle() != null)
			getTableModel().getAfterFillFromDaoHandle().accept(this);
		
	}

	InputCheckResult rowInputCheckResult;
	
	protected boolean ignoreInput() {
		boolean ignore = (rowInputCheckResult.getNotChange() && rowInputCheckResult.nonMandatoryOrNotFullFill() && data != null);
		
		if (!ignore)
			ignore = (rowInputCheckResult.getNotChange() && rowInputCheckResult.nonMandatoryOrNotFullFill() && data == null);
		
		return ignore;
			
	}
	
	@Override
	public void syncUIToDao(String trxName) {
		rowInputCheckResult = parseInputState();
		
		if (rowInputCheckResult.getIgnore())
			return;
		
		// empty value then delete current data
		// improve to handle case po manage
		if (rowInputCheckResult.getEmpty() && data != null) {
			data.deleteEx(true, trxName);
			data = null;
			return;
		}
		
		// empty (or default) value and not touch mandatory (data isn't saved
		if (rowInputCheckResult.getNotChange() && rowInputCheckResult.nonMandatoryOrNotFullFill() && data == null) 
			return;
		
		// touch mandatory and not yet full fill mandatory
		if (rowInputCheckResult.haveMandatoryAndNotFullFill()) {
			throw new AdempiereException("Madatory not yet full fill");
		}
				
		PO daoPerCol = null;
		for (CellModel cellModel : values()) {
			if (StringUtils.isNotBlank(cellModel.getColModel().getDaoPropertyName())) {
				daoPerCol = getDaoAllway(cellModel);
				cellModel.saveUIToDao(daoPerCol);
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
		currentDao = data;
		if (currentDao != null) {
			return currentDao;
		}
		
		if (tableModel.getDaoManage() != null) {
			currentDao = tableModel.getDaoManage().getDao(cellModel.getColModel().getTableName());
		}
		
		if (currentDao != null) {
			return currentDao;
		}
		
		return currentDao;
	}
	
	private PO getDaoFromDaoManage(CellModel cellModel) {
		if (tableModel.getDaoManage() != null)
			return tableModel.getDaoManage().getDaoForSave(cellModel.getColModel().getTableName());
		
		return null;
	}
	
	private PO getDaoAllway(CellModel cellModel) {
		PO daoPerCol = null;
		
		daoPerCol =  getDaoFromDaoManage(cellModel);
		
		if (daoPerCol == null) {
			daoPerCol = getDirectDao();
		}
		
		return daoPerCol;
	}
	
	private PO getDirectDao() {
		if (data == null && tableModel.getPoSupplier() == null) {
			throw new AdempiereException(Msg.getMsg(Env.getCtx(), "ZZTableModelMissingPO") + ":" + tableModel.getTableTitle() + ":" + tableModel.getSclass());
		}
		
		if (data == null)
			data = tableModel.getPoSupplier().apply(this);
		
		return data;
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
		
		if (tableModel.getDaoManage() == null) {
			if(data != null && !getTableModel().isUsed()) {
				data.deleteEx(true);//TODO: seem it's done on syncUIToDao
				setData(null);
				resetRow();
				return;
			}
			
			if (ignoreInput())
				return;
			
			PO daoToSave = getDirectDao();
			
			if (tableModel.getBeforeSave() != null) {
				tableModel.getBeforeSave().apply(daoToSave, this);
			}
			
			daoToSave.saveEx(trxName);
			
			for (CellModel cellModel : values()) {
				if (CellModel.BTUPLOAD_CELL == cellModel.getCellType()) {
					((UploadCellModel)cellModel).attachFile(daoToSave, trxName);
				}
			}
			
			if (tableModel.getAfterSave() != null) {
				tableModel.getAfterSave().apply(daoToSave, this);
			}
		}
		
	}
}
