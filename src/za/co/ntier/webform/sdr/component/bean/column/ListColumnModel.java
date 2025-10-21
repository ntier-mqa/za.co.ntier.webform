package za.co.ntier.webform.sdr.component.bean.column;

import java.util.List;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Triple;

import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.cell.ListCellModel;

public class ListColumnModel<T> extends ColumnModel {
	private List<T> dataProvider;
	private String beanPropertyName;
	private Function<T, String> displayConvert;
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
	public ListCellModel<T> getCellModel(TableModel tableModel, RowModel rowModel) {
		@SuppressWarnings("unchecked")
		ListCellModel<T> listCellModel = (ListCellModel<T>)getCellModelSupplier().apply(Triple.of(tableModel, rowModel, this));
		listCellModel.setDataProvider(dataProvider);
		return listCellModel;
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
	public Function<T, String> getDisplayConvert() {
		return displayConvert;
	}

	/**
	 * @param displayConvert the displayConvert to set
	 */
	public void setDisplayConvert(Function<T, String> displayConvert) {
		this.displayConvert = displayConvert;
	}
}
