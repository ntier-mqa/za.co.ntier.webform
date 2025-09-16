package za.co.ntier.webform.form.bean.component;

import java.util.List;
import java.util.Map.Entry;
import java.util.function.Function;

import za.co.ntier.webform.form.bean.DataType;
import za.co.ntier.webform.model.X_ZZDocumentUpload;

public class ColumnInfo<T> {
	public static <T> ColumnInfo<T> getColArea(String title, List<T> dataProvider) {
		ColumnInfo<T> colInfo = new ColumnInfo<T>(title, DataType.Area);
		colInfo.setDataProvider(dataProvider);
		return colInfo;

	}
	public static <T> ColumnInfo<T> getColDate(String title) {
		return new ColumnInfo<T>(title, DataType.Date);
	}
	public static ColumnInfo<X_ZZDocumentUpload> getColDocUpload(String title) {
		return new ColumnInfo<X_ZZDocumentUpload>(title, DataType.DocUploadDef);

	}
	public static <T> ColumnInfo<T> getColFileUpload(String title, String btText) {
		ColumnInfo<T> colInfo = new ColumnInfo<T>(title, DataType.FileUpload);
		colInfo.setBtText(btText);
		return colInfo;

	}

	public static <T> ColumnInfo<T> getColLabel(String title) {
		return new ColumnInfo<T>(title, DataType.Label);
	}

	public static ColumnInfo<LearnerInputInfo> getColLearnerInfo(String title) {
		return new ColumnInfo<LearnerInputInfo>(title, DataType.LearnerInfo);

	}

	public static <T> ColumnInfo<T> getColList(String title, List<T> dataProvider) {
		ColumnInfo<T> col = new ColumnInfo<T>(title, DataType.List);
		col.setDataProvider(dataProvider);
		return col;
	}

	public static <T> ColumnInfo<T> getColPositiveNumber(String title) {
		return new ColumnInfo<T>(title, DataType.PositiveNumber);
	}

	public static <T> ColumnInfo<T> getColPostal(String title) {
		return new ColumnInfo<T>(title, DataType.Postal);

	}
	
	public static <T> ColumnInfo<T> getColText(String title) {
		return new ColumnInfo<T>(title, DataType.Text);
	}
	
	public static <T> ColumnInfo<T> getColTotal(String title, List<ColumnInfo<?>> colValues) {
		ColumnInfo<T> colTotal = new ColumnInfo<T>(title, DataType.Label);
		colTotal.setColValues(colValues);
		return colTotal;
	}
	
	private Function<AnnexureRow<?>, Integer> expression;
	
	public static <T> ColumnInfo<T> getColExpression(String title, Function<AnnexureRow<?>, Integer> expression) {
		ColumnInfo<T> colTotal = new ColumnInfo<T>(title, DataType.Label);
		colTotal.setExpression(expression);
		return colTotal;
	}
	
	public static ColumnInfo<Entry<String, String>> getColTwoTitle(String title) {
		return new ColumnInfo<Entry<String, String>>(title, DataType.TwoTitles);

	}
	
	public static ColumnInfo<Entry<Integer, Integer>> getColTwoValue(String title) {
		return new ColumnInfo<Entry<Integer, Integer>>(title, DataType.TwoValues);

	}

	private List<ColumnInfo<?>> colValues;
	
	private String btText;

	private List<T> dataProvider;

	private DataType dataType;

	private String title;

	public ColumnInfo(String colTitle, DataType dataType) {
		this.title = colTitle;
		this.dataType = dataType;
	}

	/**
	 * @return the btText
	 */
	public String getBtText() {
		return btText;
	}

	/**
	 * @return the dataProvider
	 */
	public List<T> getDataProvider() {
		return dataProvider;
	}

	/**
	 * @return the dataType
	 */
	public DataType getDataType() {
		return dataType;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param btText the btText to set
	 */
	public void setBtText(String btText) {
		this.btText = btText;
	}

	/**
	 * @param dataProvider the dataProvider to set
	 */
	public void setDataProvider(List<T> dataProvider) {
		this.dataProvider = dataProvider;
	}

	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the colValues
	 */
	public List<ColumnInfo<?>> getColValues() {
		return colValues;
	}
	/**
	 * @param colValues the colValues to set
	 */
	public void setColValues(List<ColumnInfo<?>> colValues) {
		this.colValues = colValues;
	}
	/**
	 * @return the expression
	 */
	public Function<AnnexureRow<?>, Integer> getExpression() {
		return expression;
	}
	/**
	 * @param expression the expression to set
	 */
	public void setExpression(Function<AnnexureRow<?>, Integer> expression) {
		this.expression = expression;
	}
}
