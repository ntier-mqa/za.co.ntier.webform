package za.co.ntier.webform.sdr.component.bean.cell;

import java.util.List;
import java.util.function.Function;

import org.compiere.model.MCity;
import org.compiere.model.MRegion;
import org.zkoss.zul.ListModelList;

import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.ISelectable;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.CellModel.CellModelInfo;
import za.co.ntier.webform.sdr.component.bean.CellModel.CellModelParams;
import za.co.ntier.webform.sdr.component.bean.column.ListColumnModel;

public class ListCellModel<T> extends CellModel implements ISelectable {
	
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
	 * @param annexure
	 * @param row
	 */
	public ListCellModel(TableModel annexure, RowModel row, ListColumnModel<T> colModel) {
		super(annexure, row, colModel);
		setCellType(LIST_CELL);
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

	public int getSelectedID() {
		return 0;
	}

	public void setSelectedItemById(Object cityId) {
		
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
	getListColumnModel(Class<K> coClass, Class<T> ceClass, String title, String daoPropertyName, List<L> dataProvider, Function<L, String> displayConvert) {
		K listColumnModel = CellModel.getColModelForCell(CellModelInfo.of(coClass, ceClass, null), 
				CellModelParams.of(title, daoPropertyName, null)
				);
				
		listColumnModel.setDataProvider(dataProvider);
		
		listColumnModel.setDisplayConvert(displayConvert);
		
		return listColumnModel;
	}
	
	public static <L> ListColumnModel<L> getListColumnModel(String title, String daoPropertyName, List<L> dataProvider, Function<L, String> displayConvert) {
		@SuppressWarnings("unchecked")
		ListColumnModel<L> listColumnModel = getListColumnModel(ListColumnModel.class, ListCellModel.class, title, daoPropertyName, dataProvider, displayConvert);
		return listColumnModel;
	}
}
