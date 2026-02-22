package za.co.ntier.webform.sdr.component.bean.cell;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.adempiere.exceptions.AdempiereException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.ListModelList;

import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ISelectable;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.column.ListColumnModel;

public class ListCellModel<T> extends CellModel implements ISelectable {
	
	@Override
	public Object getValue() {
		return getSelectedID();
	}
	
	@Override
	public void initDefaultValue() {
		setValue(getDefaultValue());
	}
	
	private T defaultValue = null;
	
	@Override
	public T getDefaultValue() {
		if (defaultValue == null)
			defaultValue = lookupItem(getColModel().getDefaultValue(), getColModel().getCompareFunction());
		
		return defaultValue;
	}
	
	public T lookupItem (Object value, Function<T, Boolean> compareFunction) {
		if (value == null)
			return null;
		
		if (value.getClass().equals(getColModel().getzClass())) {
			return getColModel().getzClass().cast(value);
		}
		
		if (compareFunction == null) {
			return getColModel().getzClass().cast(value);
		}
		
		for (T item : getDataProvider()) {
			if (compareFunction.apply(item)) {
				return item;
			}
		}
		
		return null;
	}
	
	public List<T> getDataProvider(){
		return getColModel().getDataProvider();
	}
	
	public Function<T, Object> getValueConvert() {
		return getColModel().getSelectedItemValueConvert();
	}
	
	protected void setValue(Object value, Function<T, Boolean> compareFunction) {
		getModel().clearSelection();
		
		Object lookupValue = null;
		T lookupResult = lookupItem(value, compareFunction);
		
		if (lookupResult == null) {
			lookupValue = null;
		}else {
			if (lookupResult != null && getValueConvert() != null) {
				Object valueConver = getValueConvert().apply(lookupResult);
				lookupValue = valueConver;
			}else {
				lookupValue = lookupResult;
			}
		}
		
		if (lookupResult != null && !getModel().contains(lookupResult)) {
			getModel().add(lookupResult);
		}
		
		if (lookupResult != null)
			getModel().addToSelection(lookupResult);
		
		super.setValue(lookupValue);
	}
	
	@Override
	public void setValue(Object value) {
		setValue(value, item -> {
			if (getValueConvert() != null) {
				Object converValue = getValueConvert().apply(item);
				return Objects.equals(converValue, value);
			}
			
			throw new AdempiereException("Wrong type");
		});
		dirtyValue = getSelectedItem();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ListColumnModel<T> getColModel() {
		return (ListColumnModel<T>)super.getColModel();
	}
	/**
	 * sub class should override this method to provide display text
	 * generic should provide DisplayConvert
	 * @param item
	 * @return
	 */
	public String getDisplayText(T item) {
		Function<T, String> displayConvert = getColModel().getSelectedItemDisplayConvert();
		if (displayConvert != null) {
			return displayConvert.apply(item);
		}
		throw new IllegalStateException("DisplayConvert function not provided in ListColumnModel");
	}

	private ListModelList<T> model = new ListModelList<>();;
	
	/**
	 * need to init model on constructor of child class
	 * @param tableModel
	 * @param rowModel
	 */
	public ListCellModel(TableModel tableModel, RowModel rowModel, ListColumnModel<T> colModel) {
		super(tableModel, rowModel, colModel);
		setCellType(LIST_CELL);
	}

	/**
	 * @param dataProvider the dataProvider to set
	 */
	public void setDataProvider(List<T> dataProvider) {
		model.clear();

		if (dataProvider != null)
			model.addAll(dataProvider);

		/*if (model.size() == 1) {
			model.addToSelection(model.get(0));
		}*/
	}

	@Override
	public void cmdSelected(Event selectedEvent) {
		cmdSelectedHandle(selectedEvent);
	}

	public void cmdSelectedHandle(Event selectedEvent) {
		callCollModelHandle(selectedEvent);
	}
	
	public T getSelectedObj(Event event) {
		@SuppressWarnings("unchecked")
		SelectEvent<?, T> selectedEvent = (SelectEvent<?, T>)event;
		if (selectedEvent.getSelectedObjects().isEmpty())
			return null;
		else
			return selectedEvent.getSelectedObjects().iterator().next();
		
	}

	public boolean isSelected() {
		return !getModel().isSelectionEmpty();
	}

	public T getSelectedItem() {
		if (model.getSelection().isEmpty())
			return null;
		return model.getSelection().iterator().next();
	}

	public Object getSelectedID() {
		T selectedItem = getSelectedItem();
		
		if (selectedItem == null)
			return null;
		//if (selectedItem == null && getColModel().isUseForID())
			//return 0;
		//else if (selectedItem == null && !getColModel().isUseForID()) 
			//return null;
		
		Function<T, Object> valueConvert = getValueConvert();
		if (valueConvert != null) {
			return valueConvert.apply(selectedItem);
		}else {
			return selectedItem;
		}
	}

	/**
	 * @return the model
	 */
	public ListModelList<T> getModel() {
		return model;
	}

	protected void setModel(ListModelList<T> model) {
		this.model = model;
	}
	
	protected static  <T extends ListCellModel<L>, K extends ListColumnModel<L>, L> K
		getListColumnModel(Class<K> coClass, Class<T> ceClass, String title, String daoPropertyName, List<L> dataProvider, 
				Function<L, String> displayConvert, 
				Function<L, Object> valueConvert, int cellType) {
		
		K listColumnModel = CellModel.getColModelForCell(CellModelInfo.of(coClass, ceClass, null), 
				CellModelParams.of(title, daoPropertyName, cellType)
				);
				
		listColumnModel.setDataProvider(dataProvider);
		
		listColumnModel.setSelectedItemDisplayConvert(displayConvert);
		listColumnModel.setSelectedItemValueConvert(valueConvert);
		
		return listColumnModel;
	}
	
	public static <L> ListColumnModel<L> getListColumnModel(
			String title, String daoPropertyName, List<L> dataProvider, 
			Function<L, String> displayConvert, 
			Function<L, Object> valueConvert) {
		return getListColumnModel (title, daoPropertyName, dataProvider, displayConvert, valueConvert, CellModel.LIST_CELL);
	}
	
	public static <L> ListColumnModel<L> getListColumnModel(
			String title, String daoPropertyName, List<L> dataProvider, 
			Function<L, String> displayConvert, 
			Function<L, Object> valueConvert, 
			int cellType) {
		@SuppressWarnings("unchecked")
		ListColumnModel<L> listColumnModel = getListColumnModel(ListColumnModel.class, ListCellModel.class, title, daoPropertyName, dataProvider, displayConvert, valueConvert, cellType);
		return listColumnModel;
	}
}
