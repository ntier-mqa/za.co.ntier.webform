package za.co.ntier.webform.form.bean.component;

import java.beans.Introspector;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Function;

import za.co.ntier.api.model.X_ZZDocumentUpload;
import za.co.ntier.webform.form.bean.DataType;

public class ColumnInfo<T> {
	public static <T> ColumnInfo<T> getColArea(String title, List<T> dataProvider) {
		ColumnInfo<T> colInfo = new ColumnInfo<T>(title, DataType.Area);
		colInfo.setDataProvider(dataProvider);
		return colInfo;

	}
	
	public static <T> ColumnInfo<T> getColArea(String title, List<T> dataProvider, String daoPropertyName) {
		ColumnInfo<T> col = getColArea(title, dataProvider);
		col.setDaoPropertyName(correctDaoPropertyName(daoPropertyName));
		return col;
	}
	
	
	public static ColumnInfo<X_ZZDocumentUpload> getColDocUpload(String title) {
		return new ColumnInfo<X_ZZDocumentUpload>(title, DataType.DocUploadDef);

	}
	public static <T> ColumnInfo<T> getColFileUpload(String title, String btText) {
		ColumnInfo<T> colInfo = new ColumnInfo<T>(title, DataType.FileUpload);
		colInfo.setBtText(btText);
		return colInfo;

	}
	
	public static <T> ColumnInfo<T> getColFileUpload(String title, String btText, String daoPropertyName, String daoPropertyFileName) {
		ColumnInfo<T> col = getColFileUpload(title, btText);
		col.setDaoPropertyName(correctDaoPropertyName(daoPropertyName));
		col.setDaoPropertyFileName(correctDaoPropertyName(daoPropertyFileName));
		
		return col;
	}

	public static <T> ColumnInfo<T> getColLabel(String title) {
		return new ColumnInfo<T>(title, DataType.Label);
	}
	
	public static <T> ColumnInfo<T> getColRadio(String title, String daoPropertyName) {
		ColumnInfo<T> col = new ColumnInfo<T>(title, DataType.Radio);
		col.setDaoPropertyName(correctDaoPropertyName(daoPropertyName));
		return col;
	}
	
	public static <T> ColumnInfo<T> getColLabel(String title, String daoPropertyName) {
		ColumnInfo<T> col = ColumnInfo.getColLabel(title);
		col.setDaoPropertyName(correctDaoPropertyName(daoPropertyName));
		return col;
	}

	private static String correctDaoPropertyName(String daoPropertyName) {
		return Introspector.decapitalize(daoPropertyName);
	}
	
	public static ColumnInfo<LearnerInputInfo> getColLearnerInfo(String title) {
		return new ColumnInfo<LearnerInputInfo>(title, DataType.LearnerInfo);

	}

	public static ColumnInfo<LearnerInputInfo> getColLearnerInfo(String title, String daoPropertyName) {
		ColumnInfo<LearnerInputInfo> col = ColumnInfo.getColLearnerInfo(title);
		col.setDaoPropertyName(correctDaoPropertyName(daoPropertyName));
		return col;

	}
	
	public static <T> ColumnInfo<T> getColList(String title, List<T> dataProvider) {
		ColumnInfo<T> col = new ColumnInfo<T>(title, DataType.List);
		col.setDataProvider(dataProvider);
		return col;
	}
	
	public static <T> ColumnInfo<T> getColList(String title, List<T> dataProvider, String daoPropertyName) {
		ColumnInfo<T> col = getColList(title, dataProvider);
		col.setDaoPropertyName(correctDaoPropertyName(daoPropertyName));
		return col;
	}
	
	public static <T> ColumnInfo<T> getColList(String title, List<T> dataProvider, String daoPropertyName, String beanPropertyName) {
		ColumnInfo<T> col = getColList(title, dataProvider);
		col.setDaoPropertyName(correctDaoPropertyName(daoPropertyName));
		col.setBeanPropertyName(correctDaoPropertyName(beanPropertyName));
		
		return col;
	}
	
	public static <T> ColumnInfo<T> getColPositiveNumber(String title) {
		return new ColumnInfo<T>(title, DataType.PositiveNumber);
	}
	
	public static <T> ColumnInfo<T> getColPositiveNumber(String title, String daoPropertyName) {
		ColumnInfo<T> col = ColumnInfo.getColPositiveNumber(title);
		col.setDaoPropertyName(correctDaoPropertyName(daoPropertyName));
		return col;
	}

	public static <T> ColumnInfo<T> getColDate(String title, String daoPropertyName) {
		ColumnInfo<T> col = new ColumnInfo<T>(title, DataType.Date);
		col.setDaoPropertyName(correctDaoPropertyName(daoPropertyName));
		return col;
	}
	
	public static <T> ColumnInfo<T> getColPostal(String title) {
		return new ColumnInfo<T>(title, DataType.Postal);

	}
	
	public static <T> ColumnInfo<T> getColPostal(String title, String daoPropertyName) {
		ColumnInfo<T> col = getColPostal(title);
		col.setDaoPropertyName(correctDaoPropertyName(daoPropertyName));
		return col;
	}
	
	
	public static <T> ColumnInfo<T> getColText(String title) {
		return new ColumnInfo<T>(title, DataType.Text);
	}
	
	public static <T> ColumnInfo<T> getColText(String title, String daoPropertyName) {
		ColumnInfo<T> col = getColText(title);
		col.setDaoPropertyName(correctDaoPropertyName(daoPropertyName));
		return col;
	}
	
	
	private Function<AnnexureRow, Integer> expression;
	
	public static <T> ColumnInfo<T> getColExpression(String title, Function<AnnexureRow, Integer> expression) {
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
	
	private boolean isCalTotal = false;
	
	private String btText;

	private List<T> dataProvider;

	private DataType dataType;

	private String title;
	
	private String daoPropertyName;
	
	private String beanPropertyName;
	
	private String daoPropertyFileName;

	public static final String DocUploadTitle = "Document Name";

	public static final String colTotalNoBeneficiariesTitle = "Total Number of beneficiaries applying for";

	public static final String colProgrammeApplyTitle = "Programme Applying For";

	public static final String colNoBeneficiariesTitle = "Number Of Beneficiaries Applying For";

	public static final String colDisciplineTitle = "Discipline Applying For";

	public static final String colRequestedProgrammeTitle = "Requested Programme";

	public static final String colNoManagersTitle = "Number of managers";

	public static final String colNoLearners = "Number of learners";

	public static final String colFieldStudyTitle = "Field of Study";

	public static final String colTotalLearnersLabel = "Total No. of Learners Applied For";

	public static final String colNoLearnersLable = "No of Learners applied for";

	public static final String colNameProgrammeLabel = "Name of Programme";

	public static final String colWPALabel = "WPA";

	public static final String colTradeLabel = "Trade";

	public static final String colPostalCodeLabel = "Site Postal Code";

	public static final String colNoUnEmployedLabel = "No. of Unemployed Learners";

	public static final String colNoLearnersLabel = "No. of Learners";

	public static final String colNoEmployedLabel = "No. of Employed Learners";

	public static final String colLearnershipLabel = "Learnership Type";

	public static final String colDisciplineLabel = "Discipline";

	public static final String colAreaLabel = "Area";

	public static final String colAccredLabel = "Accreditation";

	public static final String btWPAText = "WPA";

	public static final String btAccredText = "Accred./SLA";

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
	 * @return the expression
	 */
	public Function<AnnexureRow, Integer> getExpression() {
		return expression;
	}
	/**
	 * @param expression the expression to set
	 */
	public void setExpression(Function<AnnexureRow, Integer> expression) {
		this.expression = expression;
	}
	
	/**
	 * @return the daoPropertyName
	 */
	public String getDaoPropertyName() {
		return daoPropertyName;
	}
	/**
	 * @param daoPropertyName the daoPropertyName to set
	 */
	public void setDaoPropertyName(String daoPropertyName) {
		this.daoPropertyName = daoPropertyName;
	}

	/**
	 * @return the daoPropertyFileName
	 */
	public String getDaoPropertyFileName() {
		return daoPropertyFileName;
	}

	/**
	 * @param daoPropertyFileName the daoPropertyFileName to set
	 */
	public void setDaoPropertyFileName(String daoPropertyFileName) {
		this.daoPropertyFileName = daoPropertyFileName;
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
	 * @return the isCalTotal
	 */
	public boolean isCalTotal() {
		return isCalTotal;
	}

	/**
	 * @param isCalTotal the isCalTotal to set
	 */
	public void setCalTotal(boolean isCalTotal) {
		this.isCalTotal = isCalTotal;
	}
}
