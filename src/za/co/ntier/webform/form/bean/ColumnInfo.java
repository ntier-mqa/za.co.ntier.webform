package za.co.ntier.webform.form.bean;

import java.util.List;

public class ColumnInfo<T> {
	private String title;
	private DataType dataType;
	private List<T> dataProvider;
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the dataType
	 */
	public DataType getDataType() {
		return dataType;
	}
	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}
	
	public ColumnInfo(String colTitle, DataType dataType) {
		this.title = colTitle;
		this.dataType = dataType;
	}
	
	public static <T> ColumnInfo<T> getColLabel(String title) {
		return new ColumnInfo<T>(title, DataType.Label);
	}
	
	public static <T> ColumnInfo<T> getColText(String title) {
		return new ColumnInfo<T>(title, DataType.Text);
	}
	
	public static <T> ColumnInfo<T> getColPositiveNumber(String title) {
		return new ColumnInfo<T>(title, DataType.PositiveNumber);
	}
	
	public static <T> ColumnInfo<T> getColDate(String title) {
		return new ColumnInfo<T>(title, DataType.Date);
	}
	
	public static <T> ColumnInfo<T> getColList(String title, List<T> dataProvider) {
		ColumnInfo<T> col = new ColumnInfo<T>(title, DataType.List);
		col.setDataProvider(dataProvider);
		return col;
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
}
