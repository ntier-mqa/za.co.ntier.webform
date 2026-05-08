package za.co.ntier.webform.sdr.component.bean.column;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Triple;
import org.compiere.util.ValueNamePair;

import za.co.ntier.api.model.I_AD_User;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.cell.ListCellModel;

public class ListColumnModel<T> extends ColumnModel {
	private Class<T> zClass;
	private boolean isUseForID = false;
	private List<T> dataProvider;
	private String beanPropertyName;
	private Function<T, String> selectedItemDisplayConvert;
	
	public ListColumnModel(String colTitle) {
		super(colTitle);
	}

	public ListColumnModel(String colTitle, String daoProperty) {
		super(colTitle, daoProperty);
	}

	public ListColumnModel(String colTitle, String daoProperty, List<T> dataProvider) {
		super(colTitle, daoProperty);
		setDataProvider(dataProvider);
	}

	@Override
	public ListCellModel<T> initCellModel(TableModel tableModel, RowModel rowModel) {
		@SuppressWarnings("unchecked")
		ListCellModel<T> listCellModel = (ListCellModel<T>)getCellModelSupplier().apply(Triple.of(tableModel, rowModel, this));
		listCellModel.setDataProvider(dataProvider);
		return listCellModel;
	}

	@Override
	public ColumnModel setDefaultValue(Object defaultValue) {
		//if (defaultValue != null && value instanceof T)
		
		throw new IllegalArgumentException("call overload function setDefaultValue(Object defaultValue, Function<T, Boolean> compareFunction)");
	}
	
	private BiFunction<ListCellModel<T>, T, Boolean> compareFunction;
	
	public ListColumnModel<T> setDefaultValue(Object defaultValue, BiFunction<ListCellModel<T>, T, Boolean> compareFunction) {
		super.setDefaultValue(defaultValue); 
		this.compareFunction = compareFunction;
		return this;
	}
	
	/**
	 * @return the dataProvider
	 */
	public List<T> getDataProvider() {
		return dataProvider;
	}

	/**
	 * @param dataProvider the dataProvider to set
	 */
	public void setDataProvider(List<T> dataProvider) {
		this.dataProvider = dataProvider;
	}

	/**
	 * @return the beanPropertyName
	 */
	public String getBeanPropertyName() {
		return beanPropertyName;
	}

	/**
	 * @param beanPropertyName the beanPropertyName to set
	 */
	public void setBeanPropertyName(String beanPropertyName) {
		this.beanPropertyName = beanPropertyName;
	}

	/**
	 * @return the displayConvert
	 */
	public Function<T, String> getSelectedItemDisplayConvert() {
		return selectedItemDisplayConvert;
	}
	
	private Function<T, Object> selectedItemValueConvert;
	

	/**
	 * @param selectedItemDisplayConvert the displayConvert to set
	 */
	public void setSelectedItemDisplayConvert(Function<T, String> selectedItemDisplayConvert) {
		this.selectedItemDisplayConvert = selectedItemDisplayConvert;
	}

	/**
	 * @return the valueConvert
	 */
	public Function<T, Object> getSelectedItemValueConvert() {
		return selectedItemValueConvert;
	}

	/**
	 * @param selectedItemValueConvert the valueConvert to set
	 */
	public void setSelectedItemValueConvert(Function<T, Object> selectedItemValueConvert) {
		this.selectedItemValueConvert = selectedItemValueConvert;
	}

	/**
	 * @return the isUseForID
	 */
	public boolean isUseForID() {
		return isUseForID;
	}

	/**
	 * @param isUseForID the isUseForID to set
	 */
	public ListColumnModel<T> setUseForID(boolean isUseForID) {
		this.isUseForID = isUseForID;
		return this;
	}

	public BiFunction<ListCellModel<T>, T, Boolean> getCompareFunction() {
		return compareFunction;
	}

	public void setCompareFunction(BiFunction<ListCellModel<T>, T, Boolean> compareFunction) {
		this.compareFunction = compareFunction;
	}

	public Class<T> getzClass() {
		return zClass;
	}

	public ListColumnModel<T> setzClass(Class<T> zClass) {
		this.zClass = zClass;
		return this;
	}
	
}
