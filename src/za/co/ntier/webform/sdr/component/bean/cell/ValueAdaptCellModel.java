package za.co.ntier.webform.sdr.component.bean.cell;

import org.apache.commons.lang3.StringUtils;
import org.compiere.model.PO;
import org.zkoss.bind.BindUtils;

import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.column.ValueAdaptColumnModel;

public class ValueAdaptCellModel extends CellModel{

	public ValueAdaptCellModel(TableModel tableModel, RowModel rowModel, ValueAdaptColumnModel colModel) {
		super(tableModel, rowModel, colModel);
	}
	
	@Override
	public void setValue(Object value) {
		super.setValue(value);
		BindUtils.postNotifyChange(this, "displayValue");
	}
	
	@Override
	public Object getValue() {
		ValueAdaptColumnModel colModel = getColModel();
		if (colModel.getValueAdaptHandle() != null) {
			return colModel.getValueAdaptHandle().apply(super.getValue());
		}
		
		return super.getValue();
	}
	
	public Object getSelectedValue() {
		return super.getValue();
	}
	
	@Override
	public ValueAdaptColumnModel getColModel() {
		return (ValueAdaptColumnModel)super.getColModel();
	}
	
	public Object getDisplayValue() {
		ValueAdaptColumnModel colModel = getColModel();
		if (colModel.getDisplayAdaptHandle() != null) {
			return colModel.getDisplayAdaptHandle().apply(super.getValue());
		}
		
		return super.getValue();
	}
	
	public static ValueAdaptColumnModel getValueAdaptColumnModel(String title, String daoPropertyName, Integer colType) {
		
		ValueAdaptColumnModel valueAdaptColumnModel = CellModel.getColModelForCell(
				CellModelInfo.of(ValueAdaptColumnModel.class, ValueAdaptCellModel.class, null)
				, CellModelParams.of(title, daoPropertyName, colType));
		
		return valueAdaptColumnModel;
	}

	@Override
	public void setValueFromDao(PO daoPerCol) {
		ValueAdaptColumnModel colModel = getColModel();
		if (colModel.getValueFromDaoAdaptHandle() != null) {
			if (StringUtils.isNotBlank(getColModel().getDaoPropertyName())) {
				Object fieldValue = daoPerCol.get_Value(getColModel().getDaoPropertyName());
				Object adaptValue = colModel.getValueFromDaoAdaptHandle().apply(fieldValue);
				setValue(adaptValue);
			}
		}else {
			super.setValueFromDao(daoPerCol);
		}
	}
}
