package za.co.ntier.webform.sdr.component.bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Triple;

/**
 * Represents a cell in a table, it hold data and related info
 * the cell manage single value like label, text,... can use this one
 * @author darren
 *
 */
public class CellModel implements IValueChange {
	private TableModel tableModel;
	private RowModel rowModel;
	private ColumnModel colModel;
	
	private Object value;
	

	public ColumnModel getColModel() {
		return colModel;
	}

	public void setColModel(ColumnModel colModel) {
		this.colModel = colModel;
	}

	public CellModel(TableModel tableModel, RowModel rowModel, ColumnModel colModel){
		this.setTableModel(tableModel);
		this.setRowModel(rowModel);
		this.colModel = colModel;
	}

	/**
	 * child classes should override this method to handle value changes
	 * @param value The new value
	 */
	@Override
	public void cmdValueChange(String value) {

	}

	/**
	 * @return the tableModel
	 */
	public TableModel getTableModel() {
		return tableModel;
	}
	/**
	 * @param tableModel the tableModel to set
	 */
	public void setTableModel(TableModel tableModel) {
		this.tableModel = tableModel;
	}
	/**
	 * @return the rowModel
	 */
	public RowModel getRowModel() {
		return rowModel;
	}
	/**
	 * @param rowModel the rowModel to set
	 */
	public void setRowModel(RowModel rowModel) {
		this.rowModel = rowModel;
	}

	public static int LABEL_CELL=1;
	public static int TEXT_CELL=2;
	public static int LIST_CELL=3;
	public static int CHECK_CELL=4;
	public static int RADIO_CELL=5;
	public static int BTUPLOAD_CELL=6;
	public static int DATE_CELL=7;
	public static int POSITIVE_NUM_CELL=8;
	public static int DOCUPLOAD_CELL=9;

	private int cellType;

	public int getCellType() {
		if (cellType == 0) {
			throw new IllegalStateException("Cell type not set for " + this.getClass().getName());
		}
		return cellType;
	}
	
	protected void setCellType(int cellType) {
		this.cellType = cellType;
	}
	
	public static record CellModelInfo<CO extends ColumnModel, CE extends CellModel>(Class<CO> colClass, Class<CE> cellClass, BiConsumer<CO, CE> cellModelDecorator) {
		public static <CO extends ColumnModel, CE extends CellModel> CellModelInfo<CO, CE>  of(Class<CO> colClass, Class<CE> cellClass, BiConsumer<CO, CE> cellModelDecorator) {
	        return new CellModelInfo<>(colClass, cellClass, cellModelDecorator);
	    }
	}
	
	public static record CellModelParams(String title, String daoPropertyName, Integer colType) {
		public static CellModelParams of(String title, String daoPropertyName, Integer colType) {
	        return new CellModelParams(title, daoPropertyName, colType);
	    }
	}
	
	/**
	 * 
	 * @param <CO>
	 * @param <CE>
	 * @param main triple of column model class, cell model class, cell decorator
	 * @param properties triple of title, dao property name, cell type
	 * @return
	 */
	public static <CO extends ColumnModel, CE extends CellModel> CO getColModelForCell(
			CellModelInfo<CO, CE> main, CellModelParams properties) {
		CO colModel;
		
		try {
			Constructor<CO> cons = main.colClass.getConstructor(String.class);
			colModel = cons.newInstance(properties.title);
			colModel.setDaoPropertyName(properties.daoPropertyName);
		} catch (NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
			throw new IllegalStateException("Error creating col model for " + main.colClass.getName());
		}

		colModel.setCellModelSupplier(models -> {
			try {
				Constructor<CE> cons = main.cellClass.getConstructor(models.getLeft().getClass(), models.getMiddle().getClass(), models.getRight().getClass());
				CE cellModel = cons.newInstance(models.getLeft(), models.getMiddle(), models.getRight());
				if (properties.colType != null)
					cellModel.setCellType(properties.colType.intValue());
				
				if (main.cellModelDecorator != null) {
					main.cellModelDecorator.accept(colModel, cellModel);
				}
				return cellModel;
			} catch (NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
				throw new IllegalStateException("Error creating cell model for " + main.cellClass.getName());
			}

		});

		return colModel;
	}

	public static  ColumnModel getColModelForLabel(String title) {
		return getColModelForCell(CellModelInfo.of(ColumnModel.class, CellModel.class, null), CellModelParams.of(title, null, LABEL_CELL));
	}

	public static  ColumnModel getColModelForText(String title, String daoPropertyName) {
		return getColModelForCell(CellModelInfo.of(ColumnModel.class, CellModel.class, null), CellModelParams.of(title, daoPropertyName, TEXT_CELL));
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	public void setValueToDao(Object dao) {
		// TODO Auto-generated method stub

	}

	public void setCellValue(Object value2) {
		// TODO Auto-generated method stub

	}

	/**
	 * Get a cell model in the row by its type
	 * it use for postal, area, province lookup together
	 * in case row has multiple columns use same cell model type, it's not guaranteed which one will be returned
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	public <T> T getCellModelByType(Class<T> clazz) {
		for (CellModel cellMode : getRowModel().values()) {
			if (cellMode.getClass().equals(clazz)) {
				return clazz.cast(cellMode);
			}
		}

		return null;
	}

}
