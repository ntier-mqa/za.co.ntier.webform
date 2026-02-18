package za.co.ntier.webform.sdr.component.bean;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;

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
		rowInputCheckResult.setEmpty(true).setFillMandatory(true).setNotChange(true);
		
		for (CellModel cellModel : values()) {
			ColumnModel colModel = cellModel.getColModel();
			// TODO condition should move to cellMode
			if(colModel.isReadonly() || (cellModel.getCellType() != CellModel.BTUPLOAD_CELL && StringUtils.isBlank(colModel.getDaoPropertyName())))
				continue;
			
			InputCheckResult cellInputCheckResult = cellModel.parseInputState();
			
			if (!cellInputCheckResult.getEmpty()) {// has at least once field have value
				rowInputCheckResult.setEmpty(false);
			}
			
			if (!cellInputCheckResult.getFillMandatory()) {
				rowInputCheckResult.setFillMandatory(false);// has at least once field have value
				log.warning("not input for mandatory field:" + colModel.getTitle());
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
		
	}

	@Override
	public void syncUIToDao(String trxName) {
		InputCheckResult rowInputCheckResult = parseInputState();
		
		if (rowInputCheckResult.getNotChange() && !rowInputCheckResult.getFillMandatory() && data != null) {
			data.deleteEx(true, trxName);
			data = null;
			return;
		}
		
		if (rowInputCheckResult.getNotChange() && !rowInputCheckResult.getFillMandatory() && data == null) 
			return;
		
		if (!rowInputCheckResult.getFillMandatory()) {
			throw new AdempiereException("Madatory not yet full fill");
		}
				
		PO daoPerCol = null;
		for (CellModel cellModel : values()) {
			if (StringUtils.isNotBlank(cellModel.getColModel().getDaoPropertyName())) {
				daoPerCol = getDaoAllway(cellModel);
				
				//TODO:Move logic set ui value to dao to cellMode
				Object value = convertDataType(daoPerCol, cellModel);
				if (value == null) {
					daoPerCol.set_ValueOfColumn(cellModel.getColModel().getDaoPropertyName(), null);
				}else {
					MasterUtil.setObjectProperty(daoPerCol, cellModel.getColModel().getDaoPropertyName(), value);
				}
				
			}
		}
			
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
			throw new AdempiereException("ZZTableModelMissingPO");
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

	private Object convertDataType(PO daoPerCol, CellModel cellModel) {
		if (cellModel.getValue() == null)
			return null;
		
		if (cellModel.getCellType() != CellModel.POSITIVE_NUM_CELL) {
			return cellModel.getValue();
		}
		
		int colDataType = MTable.get(Env.getCtx(), daoPerCol.get_TableName())
			.getColumn(cellModel.getColModel().getDaoPropertyName()).getAD_Reference_ID();
		
		if (colDataType == DisplayType.Amount || colDataType == DisplayType.Number || colDataType == DisplayType.CostPrice
				|| colDataType == DisplayType.Quantity) {
			if (cellModel.getValue() instanceof Integer) {
				return BigDecimal.valueOf(((Integer)cellModel.getValue()).longValue());
			}else if (cellModel.getValue() instanceof BigDecimal) {
				return cellModel.getValue();
			}else {
				throw new ApplicationException(Msg.getMsg(Env.getCtx(), "ZZCellDataWrongDataType"));
			}
		}else if (cellModel.getValue() instanceof BigDecimal){
			// DisplayType.Integer
			// on tab org information, field "Number of Employees" is Positive Number. when load from I_ZZ_Application_Form.COLUMNNAME_NumberEmployees or input by user it's integer
			// but when sdl is change, value can be set from MBPartner_New.getZZ_Number_Of_Employees()
			// in this case value become BigDecimal
			return ((BigDecimal)cellModel.getValue()).intValueExact();
		}else if (cellModel.getValue() instanceof Integer){
			return cellModel.getValue();
		}else {
			throw new ApplicationException(Msg.getMsg(Env.getCtx(), "ZZCellDataWrongDataType"));
		}
		
		
	}
	
	@Override
	public boolean validate(Boolean isSubmit) {
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
			PO daoToSave = getDirectDao();
			if (tableModel.getBeforeSave() != null) {
				tableModel.getBeforeSave().apply(daoToSave);
			}
			
			daoToSave.saveEx(trxName);
			
			for (CellModel cellModel : values()) {
				if (CellModel.BTUPLOAD_CELL == cellModel.getCellType()) {
					((UploadCellModel)cellModel).attachFile(daoToSave, trxName);
				}
			}
		}
		
	}
}
