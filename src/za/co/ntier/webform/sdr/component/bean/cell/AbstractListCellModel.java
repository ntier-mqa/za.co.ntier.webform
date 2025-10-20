package za.co.ntier.webform.sdr.component.bean.cell;

import java.util.List;

import org.compiere.model.MCity;
import org.compiere.model.MRegion;
import org.zkoss.zul.ListModelList;

import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.sdr.component.bean.BaseCellModel;
import za.co.ntier.webform.sdr.component.bean.ISelectable;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.column.ListColumnModel;

public abstract class AbstractListCellModel<T> extends BaseCellModel implements ISelectable {
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
		super(annexure, row, LIST_CELL);
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
	
	protected void setModel(ListModelList<T> model) {
		this.model = model;
	}
	
	public static  <T extends AbstractListCellModel<L>, K extends ListColumnModel<L>, L> K  
			getListColumnModel(Class<K> coClass, Class<T> ceClass, String title, String daoPropertyName, List<L> dataProvider) {
		K listColumnModel = BaseCellModel.getColModel(coClass, ceClass, title);
		listColumnModel.setDaoPropertyName(daoPropertyName);
		listColumnModel.setDataProvider(dataProvider);
		return listColumnModel;
	}
	
	public static ListColumnModel<MCity> getAreaColumnModel(String title, String daoPropertyName) {
		@SuppressWarnings("unchecked")
		ListColumnModel<MCity> listColumnModel = AbstractListCellModel.getListColumnModel(ListColumnModel.class, AreaCellModel.class, title, daoPropertyName, MasterUtil.getInitCities());
		return listColumnModel;
	}
	
	public static ListColumnModel<MRegion> getProvinceColumnModel(String title, String daoPropertyName, List<MRegion> dataProvider) {
		@SuppressWarnings("unchecked")
		ListColumnModel<MRegion> listColumnModel = AbstractListCellModel.getListColumnModel(ListColumnModel.class, ProvinceCellModel.class, title, daoPropertyName, MasterUtil.getRegions());
		return listColumnModel;
	}
	
	public static ListColumnModel<String> getListStringColumnModel(String title, String daoPropertyName, List<String> dataProvider) {
		@SuppressWarnings("unchecked")
		ListColumnModel<String> listColumnModel = AbstractListCellModel.getListColumnModel(ListColumnModel.class, StringListCellModel.class, title, daoPropertyName, dataProvider);
		return listColumnModel;
	}
}
