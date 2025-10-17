package za.co.ntier.webform.sdr.component.bean.cell;

import java.beans.Introspector;
import java.util.List;
import java.util.function.Function;

import org.compiere.model.MRegion;

import za.co.ntier.webform.sdr.component.bean.DataType;

public class ColumnModel {
	
	public static  ColumnModel getColArea(String title, List dataProvider) {
		ColumnModel colInfo = new ColumnModel(title, DataType.Area);
		colInfo.setDataProvider(dataProvider);
		return colInfo;

	}

	public static  ColumnModel getColArea(String title, List dataProvider, String daoPropertyName) {
		ColumnModel col = getColArea(title, dataProvider);
		col.setDaoPropertyName(correctDaoPropertyName(daoPropertyName));
		return col;
	}

	public static ColumnModel getColProvince(String title, List<MRegion> regions, String daoPropertyName) {
		ColumnModel col = new ColumnModel(title, DataType.Province);
		col.setDataProvider(regions);
		col.setDaoPropertyName(correctDaoPropertyName(daoPropertyName));
		return col;
	}

	public static ColumnModel getColDocUpload(String title) {
		return new ColumnModel(title, DataType.DocUploadDef);

	}
	public static  ColumnModel getColFileUpload(String title, String btText) {
		ColumnModel colInfo = new ColumnModel(title, DataType.FileUpload);
		colInfo.setBtText(btText);
		return colInfo;

	}

	public static  ColumnModel getColFileUpload(String title, String btText, String daoPropertyName, String daoPropertyFileName) {
		ColumnModel col = getColFileUpload(title, btText);
		col.setDaoPropertyName(correctDaoPropertyName(daoPropertyName));
		col.setDaoPropertyFileName(correctDaoPropertyName(daoPropertyFileName));

		return col;
	}

	public static  ColumnModel getColLabel(String title) {
		return new ColumnModel(title, DataType.Label);
	}

	public static  ColumnModel getColRadio(String title, String daoPropertyName) {
		ColumnModel col = new ColumnModel(title, DataType.Radio);
		col.setDaoPropertyName(correctDaoPropertyName(daoPropertyName));
		return col;
	}

	public static  ColumnModel getColLabel(String title, String daoPropertyName) {
		ColumnModel col = ColumnModel.getColLabel(title);
		col.setDaoPropertyName(correctDaoPropertyName(daoPropertyName));
		return col;
	}

	private static String correctDaoPropertyName(String daoPropertyName) {
		return Introspector.decapitalize(daoPropertyName);
	}

	public static ColumnModel getColLearnerInfo(String title) {
		return new ColumnModel(title, DataType.LearnerInfo);

	}

	public static ColumnModel getColLearnerInfo(String title, String daoPropertyName) {
		ColumnModel col = ColumnModel.getColLearnerInfo(title);
		col.setDaoPropertyName(correctDaoPropertyName(daoPropertyName));
		return col;

	}

	public static ColumnModel getColListString(String title, List<String> dataProvider) {
		ColumnModel col = new ColumnModel(title, DataType.StringList);
		col.setDataProvider(dataProvider);
		return col;
	}
	
	public static  ColumnModel getColList(String title, List dataProvider) {
		ColumnModel col = new ColumnModel(title, DataType.List);
		col.setDataProvider(dataProvider);
		return col;
	}

	public static  ColumnModel getColList(String title, List dataProvider, String daoPropertyName) {
		ColumnModel col = getColList(title, dataProvider);
		col.setDaoPropertyName(correctDaoPropertyName(daoPropertyName));
		return col;
	}

	public static  ColumnModel getColList(String title, List dataProvider, String daoPropertyName, String beanPropertyName) {
		ColumnModel col = getColList(title, dataProvider);
		col.setDaoPropertyName(correctDaoPropertyName(daoPropertyName));
		col.setBeanPropertyName(correctDaoPropertyName(beanPropertyName));

		return col;
	}

	public static  ColumnModel getColPositiveNumber(String title) {
		return new ColumnModel(title, DataType.PositiveNumber);
	}

	public static  ColumnModel getColPositiveNumber(String title, String daoPropertyName) {
		ColumnModel col = ColumnModel.getColPositiveNumber(title);
		col.setDaoPropertyName(correctDaoPropertyName(daoPropertyName));
		return col;
	}

	public static  ColumnModel getColDate(String title, String daoPropertyName) {
		ColumnModel col = new ColumnModel(title, DataType.Date);
		col.setDaoPropertyName(correctDaoPropertyName(daoPropertyName));
		return col;
	}

	public static  ColumnModel getColPostal(String title) {
		return new ColumnModel(title, DataType.Postal);

	}

	public static  ColumnModel getColPostal(String title, String daoPropertyName) {
		ColumnModel col = getColPostal(title);
		col.setDaoPropertyName(correctDaoPropertyName(daoPropertyName));
		return col;
	}


	public static  ColumnModel getColText(String title) {
		return new ColumnModel(title, DataType.Text);
	}

	public static  ColumnModel getColText(String title, String daoPropertyName) {
		ColumnModel col = getColText(title);
		col.setDaoPropertyName(correctDaoPropertyName(daoPropertyName));
		return col;
	}


	private Function<RowModel, Integer> expression;

	public static  ColumnModel getColExpression(String title, Function<RowModel, Integer> expression) {
		ColumnModel colTotal = new ColumnModel(title, DataType.Label);
		colTotal.setExpression(expression);
		return colTotal;
	}

	public static ColumnModel getColTwoTitle(String title) {
		return new ColumnModel(title, DataType.TwoTitles);

	}

	public static ColumnModel getColTwoValue(String title) {
		return new ColumnModel(title, DataType.TwoValues);

	}

	private boolean isCalTotal = false;

	private String btText;

	private List dataProvider;

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

	// in ColumnModel (or ColumnModel)
	private boolean mandatory;

	public ColumnModel(String colTitle, DataType dataType) {
		this.title = colTitle;
		this.dataType = dataType;
	}




	public boolean isMandatory() { return mandatory; }
	public ColumnModel setMandatory(boolean mandatory) { this.mandatory = mandatory; return this; }
	// (optional fluent alias)
	public ColumnModel required() { this.mandatory = true; return this; }


	/**
	 * @return the btText
	 */
	public String getBtText() {
		return btText;
	}

	/**
	 * @return the dataProvider
	 */
	public List getDataProvider() {
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
	public void setDataProvider(List dataProvider) {
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
	public Function<RowModel, Integer> getExpression() {
		return expression;
	}
	/**
	 * @param expression the expression to set
	 */
	public void setExpression(Function<RowModel, Integer> expression) {
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
