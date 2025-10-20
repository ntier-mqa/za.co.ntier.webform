package za.co.ntier.webform.sdr.component.bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class BaseCellModel implements IValueChange {
	private TableModel tableModel;
	private RowModel rowModel;
	private Object value;

	public BaseCellModel(TableModel tableModel, RowModel rowModel){
		this.setAnnexure(tableModel);
		this.setRow(rowModel);
	}

	public BaseCellModel(TableModel tableModel, RowModel rowModel, int cellType){
		this(tableModel, rowModel);
		this.cellType = cellType;
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
	public TableModel getAnnexure() {
		return tableModel;
	}
	/**
	 * @param tableModel the tableModel to set
	 */
	public void setAnnexure(TableModel annexure) {
		this.tableModel = annexure;
	}
	/**
	 * @return the rowModel
	 */
	public RowModel getRow() {
		return rowModel;
	}
	/**
	 * @param rowModel the rowModel to set
	 */
	public void setRow(RowModel row) {
		this.rowModel = row;
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

	private int cellType = 0;

	public int getCellType() {
		if (cellType == 0) {
			throw new IllegalStateException("Cell type not set for " + this.getClass().getName());
		}
		return cellType;
	}

	private static  BaseColumnModel getColModel(String title, int cellType) {
		BaseColumnModel colModel = new BaseColumnModel(title);
		colModel.setCellModelSupplier((tableModel, rowModel) -> {
			return new BaseCellModel(tableModel, rowModel, cellType);
		});

		return colModel;
	}

	private static  BaseColumnModel getColModel(String title, int cellType, String daoPropertyName) {
		return getColModel(title, cellType).setDaoPropertyName(daoPropertyName);
	}

	public static <CO extends BaseColumnModel, CE extends BaseCellModel> CO getColModel(Class<CO> coClass, Class<CE> ceClass, String title) {
		CO colModel = null;
		try {
			Constructor<CO> cons = coClass.getConstructor(String.class);
			colModel = cons.newInstance(title);
		} catch (NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
			throw new IllegalStateException("Error creating col model for " + coClass.getName());
		}

		colModel.setCellModelSupplier((tableModel, rowModel) -> {
			try {
				Constructor<CE> cons = ceClass.getConstructor(tableModel.getClass(), rowModel.getClass());
				return cons.newInstance(tableModel, rowModel);
			} catch (NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
				throw new IllegalStateException("Error creating cell model for " + ceClass.getName());
			}

		});

		return colModel;
	}

	public static <T extends BaseCellModel> BaseColumnModel getColModel(Class<T> zClass, String title, String daoPropertyName) {
		return getColModel(BaseColumnModel.class, zClass, title).setDaoPropertyName(daoPropertyName);
	}

	public static  BaseColumnModel getColModelForLabel(String title) {
		return getColModel(title, LABEL_CELL);
	}

	public static  BaseColumnModel getColModelForText(String title) {
		return getColModel(title, TEXT_CELL);
	}

	public static  BaseColumnModel getColModelForText(String title, String daoPropertyName) {
		return getColModel(title, TEXT_CELL, daoPropertyName);
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

	public <T> T getCellModel(Class<T> clazz) {
		for (BaseCellModel cellMode : getRow().values()) {
			if (cellMode.getClass().equals(clazz)) {
				return clazz.cast(cellMode);
			}
		}

		return null;
	}

}
