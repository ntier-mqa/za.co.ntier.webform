package za.co.ntier.webform.sdr.component.bean.cell;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

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
	public void initDefaultValue(TableModel tableModel, RowModel rowModel) {
		ListColumnModel<T> colModel = getColModel();
		if (colModel.getDefaultValue() != null) {
			setValue(colModel.getDefaultValue(), colModel.getCompareFunction());
		}
	}
	
	public List<T> getDataProvider(){
		return getColModel().getDataProvider();
	}
	
	public Function<T, Object> getValueConvert() {
		return getColModel().getValueConvert();
	}
	
	protected void setValue(Object value, Function<T, Boolean> compareFunction) {
		getModel().clearSelection();
		for (T item : getDataProvider()) {
			Boolean isSelectedValue = null;

			if (compareFunction != null) {
				isSelectedValue = compareFunction.apply(item);
			}else {
				Object itemValue;
				if (getValueConvert() != null) {
					itemValue = getValueConvert().apply(item);
				}else {
					itemValue = item;
				}
				
				isSelectedValue = Objects.equals(value, itemValue);
			}
				
			if (isSelectedValue) {
				if (!getModel().contains(item)) {
					getModel().add(item);
				}
				
				getModel().addToSelection(item);
				//super.setValue(value);
				return;
			}
		}
	}
	
	
	@Override
	public void setValue(Object value) {
		setValue(value, null);
		dirtyValue = getValue();
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
		Function<T, String> displayConvert = getColModel().getDisplayConvert();
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
	public void cmdSelected(Object selectedObj) {
		@SuppressWarnings("unchecked")
		T selected = (T)selectedObj;
		cmdSelectedHandle(selected);
	}

	public void cmdSelectedHandle(T selected) {
		
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
	getListColumnModel(Class<K> coClass, Class<T> ceClass, String title, String daoPropertyName, List<L> dataProvider, Function<L, String> displayConvert, Function<L, Object> valueConvert) {
		K listColumnModel = CellModel.getColModelForCell(CellModelInfo.of(coClass, ceClass, null), 
				CellModelParams.of(title, daoPropertyName, null)
				);
				
		listColumnModel.setDataProvider(dataProvider);
		
		listColumnModel.setDisplayConvert(displayConvert);
		listColumnModel.setValueConvert(valueConvert);
		
		return listColumnModel;
	}
	
	@Override
	public boolean notInputed() {
		Object value = getValue();
		return Objects.isNull(value) || (getColModel().isUseForID() && (int)value == 0);
	}
	
	public static <L> ListColumnModel<L> getListColumnModel(String title, String daoPropertyName, List<L> dataProvider, Function<L, String> displayConvert, Function<L, Object> valueConvert) {
		@SuppressWarnings("unchecked")
		ListColumnModel<L> listColumnModel = getListColumnModel(ListColumnModel.class, ListCellModel.class, title, daoPropertyName, dataProvider, displayConvert, valueConvert);
		return listColumnModel;
	}


}
