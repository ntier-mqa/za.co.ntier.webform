package za.co.ntier.webform.sdr.component.bean.cell;

import java.util.List;

import org.zkoss.zul.ListModelList;

import za.co.ntier.webform.sdr.component.bean.ISelectable;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;

public abstract class AbstractListCellModel<T> extends AbstractCellModel implements ISelectable {
	abstract public String getDisplayText(T item);
	
	private ListModelList<T> model;
	
	public static boolean isListCellModel(Object obj) {
		return obj instanceof AbstractListCellModel;
	}
	
	/**
	 * need to init model on constructor of child class
	 * @param annexure
	 * @param row
	 */
	public AbstractListCellModel(TableModel annexure, RowModel row) {
		super(annexure, row);
	}

	/**
	 * @param dataProvider the dataProvider to set
	 */
	public void setDataProvider(List<T> dataProvider) {
		model.clear();
		
		if (dataProvider != null)
			model.addAll(dataProvider);
		
		if (model.size() == 1) {
			model.addToSelection(model.get(0));
		}
	}
	
	@Override
	public void cmdSelected(Object selectedObj) {
		@SuppressWarnings("unchecked")
		T selected = (T)selectedObj;
		cmdSelectedHandle(selected);
	}
	
	public abstract void cmdSelectedHandle(T selected);
	
	public boolean isSelected() {
		return !getModel().isSelectionEmpty();
	}
	
	public T getSelectedItem() {
		if (model.getSelection().isEmpty())
			return null;
		return model.getSelection().iterator().next();
	}
	
	abstract public int getSelectedID();
	
	public abstract void setSelectedItemById(Object cityId);
	
	/**
	 * @return the model
	 */
	public ListModelList<T> getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(ListModelList<T> model) {
		this.model = model;
	}

}
