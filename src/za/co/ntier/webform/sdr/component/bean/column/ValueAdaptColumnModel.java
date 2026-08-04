package za.co.ntier.webform.sdr.component.bean.column;

import java.util.function.Function;

import za.co.ntier.webform.sdr.component.bean.ColumnModel;

public class ValueAdaptColumnModel extends ColumnModel{
	public ValueAdaptColumnModel(String colTitle) {
		super(colTitle);
	}
	
	public ValueAdaptColumnModel(String colTitle, String daoPropertyName) {
		super(colTitle, daoPropertyName);
	}

	public Function<Object, Object> getValueAdaptHandle() {
		return valueAdaptHandle;
	}

	public void setValueAdaptHandle(Function<Object, Object> valueAdaptHandle) {
		this.valueAdaptHandle = valueAdaptHandle;
	}

	public Function<Object, Object> getDisplayAdaptHandle() {
		return displayAdaptHandle;
	}

	public void setDisplayAdaptHandle(Function<Object, Object> displayAdaptHandle) {
		this.displayAdaptHandle = displayAdaptHandle;
	}

	public Function<Object, Object> getValueFromDaoAdaptHandle() {
		return valueFromDaoAdaptHandle;
	}

	public void setValueFromDaoAdaptHandle(Function<Object, Object> valueFromDaoAdaptHandle) {
		this.valueFromDaoAdaptHandle = valueFromDaoAdaptHandle;
	}

	public boolean isAllowClearText() {
		return allowClearText;
	}

	public ValueAdaptColumnModel setAllowClearText(boolean allowClearText) {
		this.allowClearText = allowClearText;
		return this;
	}

	private Function<Object, Object> valueAdaptHandle;
	
	private Function<Object, Object> displayAdaptHandle;
	
	private Function<Object, Object> valueFromDaoAdaptHandle;
	
	private boolean allowClearText = false;
	
}
