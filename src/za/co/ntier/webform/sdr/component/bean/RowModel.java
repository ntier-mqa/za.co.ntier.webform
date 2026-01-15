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

import za.co.ntier.api.model.X_ZZSdf;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.sdr.component.bean.cell.UploadCellModel;

public class RowModel extends HashMap<ColumnModel, CellModel> implements ISupportSave{
	private static final CLogger log = CLogger.getCLogger(RowModel.class);
	private TableModel tableModel;
	public RowModel(TableModel annexure) {
		this.setAnnexure(annexure);
	}
	private static final long serialVersionUID = 3444756682531476154L;
	public static final int INPUT_STATE_EMPTY = 0;
	public static final int INPUT_STATE_FULL_REQUIRED = 1;
	private PO data;

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
	public TableModel getAnnexure() {
		return tableModel;
	}

	/**
	 * @param tableModel the tableModel to set
	 */
	public void setAnnexure(TableModel annexure) {
		this.tableModel = annexure;
	}

	public boolean checkState(int state) {
		boolean missRequired = false;
		boolean isInputed = false;
		for (CellModel cellModel : values()) {
			ColumnModel colModel = cellModel.getColModel();
			if(StringUtils.isBlank(colModel.getDaoPropertyName()))
				continue;

			if (cellModel.notInputed() && colModel.isMandatory()) {
				log.warning("not input for mandatory field:" + colModel.getTitle());
				missRequired = true;
			}
				
			if (!cellModel.notInputed())
				isInputed = true;
		}
		
		if (state == INPUT_STATE_EMPTY) {
			return !isInputed;
		}else if (state == INPUT_STATE_FULL_REQUIRED) {
			return !missRequired;
		}
		
		throw new IllegalStateException("check for wrong state");
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
		// TODO Auto-generated method stub
		return false;
	}

	public void fillRowDataFromDao() {
		values().stream()
			.filter(cellModel -> {return StringUtils.isNotBlank(cellModel.getColModel().getDaoPropertyName());})
			.forEach(cellModel -> {
				PO daoPerCol = null;
				if (tableModel.getDaoManage() != null) {
					daoPerCol = tableModel.getDaoManage().getDao(cellModel.getColModel().getTableName());
				}
				
				if (daoPerCol == null)
					daoPerCol = getData();
				
				cellModel.setValue(MasterUtil.getObjectPropertyValue(daoPerCol, cellModel.getColModel().getDaoPropertyName()));
 			});

	}

	@Override
	public void save(X_ZZSdf applicationForm, String trxName) {
		boolean isNothingInputed = checkState(RowModel.INPUT_STATE_EMPTY);
		if (isNothingInputed && data != null) {
			data.deleteEx(true);
			data = null;
			return;
		}
		
		if (isNothingInputed && data == null) 
			return;
		
		if (!checkState(RowModel.INPUT_STATE_FULL_REQUIRED)) {
			throw new AdempiereException("Madatory not yet full fill");
		}
		
		/*
		 * if (data == null) { if (tableModel.getDaoManage() != null) { if
		 * (tableModel.getDaoManage().getDao() != null) { data =
		 * tableModel.getDaoManage().getDao(); } }
		 * 
		 * 
		 * if (data == null) { applicationForm.set_TrxName(trxName); // important data =
		 * tableModel.getPoSupplier().apply(tableModel, applicationForm); if
		 * (tableModel.getDaoManage() != null) { tableModel.getDaoManage().setDao(data);
		 * } }
		 * 
		 * 
		 * }
		 */		
		PO daoPerCol = null;
		for (CellModel cellModel : values()) {
			if (StringUtils.isNotBlank(cellModel.getColModel().getDaoPropertyName())) {
				if (tableModel.getDaoManage() != null)
					daoPerCol =  tableModel.getDaoManage().getDao(cellModel.getColModel().getTableName());
				if (daoPerCol == null && data == null) {
					data = tableModel.getPoSupplier().apply(tableModel, applicationForm);
				}
				if (daoPerCol == null)
					daoPerCol = data;
				
				//TODO:Move logic set ui value to dao to cellMode
				Object value = convertDataType(daoPerCol, cellModel);
				MasterUtil.setObjectProperty(daoPerCol, cellModel.getColModel().getDaoPropertyName(), value);
			}
		}
		
		if (daoPerCol != null) {
			daoPerCol.saveEx(trxName);
			
			for (CellModel cellModel : values()) {
				if (CellModel.BTUPLOAD_CELL == cellModel.getCellType()) {
					((UploadCellModel)cellModel).attachFile(daoPerCol, trxName);
				}
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
		}else {// DisplayType.Integer
			return cellModel.getValue();
		}
		
		
	}
}
