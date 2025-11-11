package za.co.ntier.webform.sdr.component.bean;

import java.beans.Introspector;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.commons.lang3.Functions.FailableBiConsumer;
import org.apache.commons.lang3.tuple.Triple;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.InputEvent;

/**
 * do {@link #setCellModelSupplier(BiFunction)} or provide in constructor
 * otherwise override {@link #getCellModel(TableModel, RowModel)}
 */
public class ColumnModel {
	private boolean isCalTotlal = false;
	
	private Function<Triple<TableModel, RowModel, ColumnModel>, CellModel> cellModelSupplier;

	public CellModel getCellModel(TableModel tableModel, RowModel rowModel) {
		if (getCellModelSupplier() == null) {
			throw new IllegalStateException("Need to provide a cellModelSupplier or override getCellModel");
		}
		return cellModelSupplier.apply(Triple.of(tableModel, rowModel, this));
	}

	private static String correctDaoPropertyName(String daoPropertyName) {
		return Introspector.decapitalize(daoPropertyName);
	}
	
	private int tableId = 0; 
	
	private Function<RowModel, Integer> expression;

	private boolean isCalTotal = false;

	private String title;

	private String daoPropertyName;

	// in ColumnModel (or ColumnModel)
	private boolean mandatory;

	public ColumnModel(String colTitle) {
		this.title = colTitle;
	}


	public ColumnModel(String colTitle, String daoPropertyName) {
		this (colTitle);
		setDaoPropertyName(daoPropertyName);
	}

	public boolean isMandatory() { return mandatory; }
	public ColumnModel setMandatory(boolean mandatory) { this.mandatory = mandatory; return this; }
	// (optional fluent alias)
	public ColumnModel required() { this.mandatory = true; return this; }
	
	
	private boolean readonly = false;

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
	public ColumnModel setDaoPropertyName(String daoPropertyName) {
		this.daoPropertyName = correctDaoPropertyName(daoPropertyName);
		return this;
	}


	public Function<Triple<TableModel, RowModel, ColumnModel>, CellModel> getCellModelSupplier() {
		return cellModelSupplier;
	}

	/**
	 * @param cellModelSupplier
	 */
	public void setCellModelSupplier(Function<Triple<TableModel, RowModel, ColumnModel>, CellModel> cellModelSupplier) {
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
	
	/**
	 * @return the showTitle
	 */
	public Boolean getShowTitle() {
		return showTitle;
	}

	/**
	 * @param showTitle the showTitle to set
	 */
	public void setShowTitle(Boolean showTitle) {
		this.showTitle = showTitle;
	}

	/**
	 * @return the readonly
	 */
	public boolean isReadonly() {
		return readonly;
	}

	/**
	 * @param readonly the readonly to set
	 */
	public ColumnModel setReadonly(boolean readonly) {
		this.readonly = readonly;
		return this;
	}

	/**
	 * @return the tableId
	 */
	public int getTableId() {
		return tableId;
	}

	/**
	 * @param tableId the tableId to set
	 */
	public ColumnModel setTableId(int tableId) {
		this.tableId = tableId;
		return this;
	}

	private Boolean showTitle = null;
	 
	private BiConsumer<Event, CellModel> eventHandle;
	
	public void cellValueChange(Event event, CellModel cellModel) {
		if (eventHandle != null)
			eventHandle.accept(event, cellModel);
	}

	/**
	 * @return the eventHandle
	 */
	public BiConsumer<Event, CellModel> getEventHandle() {
		return eventHandle;
	}

	/**
	 * @param eventHandle the eventHandle to set
	 */
	public void setEventHandle(BiConsumer<Event, CellModel> eventHandle) {
		this.eventHandle = eventHandle;
	}
}
