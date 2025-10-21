package za.co.ntier.webform.sdr.component.bean;

import java.beans.Introspector;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.apache.commons.lang3.tuple.Triple;

/**
 * do {@link #setCellModelSupplier(BiFunction)} or provide in constructor
 * otherwise override {@link #getCellModel(TableModel, RowModel)}
 */
public class BaseColumnModel {
	private boolean isCalTotlal = false;
	
	private Function<Triple<TableModel, RowModel, BaseColumnModel>, BaseCellModel> cellModelSupplier;

	public BaseCellModel getCellModel(TableModel tableModel, RowModel rowModel) {
		if (getCellModelSupplier() == null) {
			throw new IllegalStateException("Need to provide a cellModelSupplier or override getCellModel");
		}
		return cellModelSupplier.apply(Triple.of(tableModel, rowModel, this));
	}

	private static String correctDaoPropertyName(String daoPropertyName) {
		return Introspector.decapitalize(daoPropertyName);
	}

	private Function<RowModel, Integer> expression;

	private boolean isCalTotal = false;

	private String title;

	private String daoPropertyName;

	// in BaseColumnModel (or BaseColumnModel)
	private boolean mandatory;

	public BaseColumnModel(String colTitle) {
		this.title = colTitle;
	}


	public BaseColumnModel(String colTitle, String daoPropertyName) {
		this (colTitle);
		setDaoPropertyName(daoPropertyName);
	}

	public boolean isMandatory() { return mandatory; }
	public BaseColumnModel setMandatory(boolean mandatory) { this.mandatory = mandatory; return this; }
	// (optional fluent alias)
	public BaseColumnModel required() { this.mandatory = true; return this; }

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
	public BaseColumnModel setDaoPropertyName(String daoPropertyName) {
		this.daoPropertyName = correctDaoPropertyName(daoPropertyName);
		return this;
	}


	public Function<Triple<TableModel, RowModel, BaseColumnModel>, BaseCellModel> getCellModelSupplier() {
		return cellModelSupplier;
	}

	/**
	 * @param cellModelSupplier
	 */
	public void setCellModelSupplier(Function<Triple<TableModel, RowModel, BaseColumnModel>, BaseCellModel> cellModelSupplier) {
		this.cellModelSupplier = cellModelSupplier;
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

	/**
	 * @return the isCalTotlal
	 */
	public boolean isCalTotlal() {
		return isCalTotlal;
	}

	/**
	 * @param isCalTotlal the isCalTotlal to set
	 */
	public void setCalTotlal(boolean isCalTotlal) {
		this.isCalTotlal = isCalTotlal;
	}
}
