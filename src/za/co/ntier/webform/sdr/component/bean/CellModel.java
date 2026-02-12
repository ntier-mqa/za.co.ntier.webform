package za.co.ntier.webform.sdr.component.bean;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiConsumer;

import org.adempiere.webui.panel.RegistrationWindow;
import org.apache.commons.lang3.StringUtils;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.jfree.util.Log;
import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.InputEvent;

/**
 * Represents a cell in a table, it hold data and related info
 * the cell manage single value like label, text,... can use this one
 * @author darren
 *
 */
public class CellModel implements IValueChange {
	
	protected static final CLogger log = CLogger.getCLogger(CellModel.class);
	private String iconSclass;
	
	private TableModel tableModel;
	private RowModel rowModel;
	private ColumnModel colModel;
	
	private Object value;
	
	private final PropertyChangeSupport valuePropertyChangeSupport = new PropertyChangeSupport(this);
	
	public void addPropertyChangeListener(PropertyChangeListener pcl) {
        valuePropertyChangeSupport.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        valuePropertyChangeSupport.removePropertyChangeListener(pcl);
    }
    
	/**
	 * 
	 * when a field not pass validate by org.zkoss.bind.Validator value isn't save 
	 */
	protected Object dirtyValue;
	
	public static record InputCheckResult (Boolean empty, Boolean emptyOrDefault) {
		
	}
	InputCheckResult d = new InputCheckResult(null, null);
	
			
	public boolean notInputed(boolean defaultAsNotInput) {
		boolean isNotInput = Objects.isNull(dirtyValue);
		if (!isNotInput && getColModel().getDefaultValue() != null && defaultAsNotInput) {
			if (Objects.equals(dirtyValue, getColModel().getDefaultValue())) {
				isNotInput = true;
			}
		}
		
		return isNotInput;
	}
	
	public ColumnModel getColModel() {
		return colModel;
	}

	private String validateMsgKey = UUID.randomUUID().toString();
	
	public String getValidateMsgKey() {
		return validateMsgKey;
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
	 * consider this case
	 * cell-1 is depend on (calculate) cell-2
	 * in case we call this one after calculate, value of cell-1 is reseted
	 * so this method should call by bellow way
	 * example reset all value of row
	 * 1. call reset to null for all cells
	 * 2. call initDefaultValue for all cells
	 * 
	 * for only case reset one cell we call
	 * 1. reset to null for this cell
	 * 2. call initDefaultValue for this cell
	 * @param tableModel
	 * @param rowModel
	 */
	public void initDefaultValue () {
		if (value == null)
			setValue(colModel.getDefaultValue());
	}
	
	private boolean formValidate = false;
	private List<String> validateMsgs = new ArrayList<>();
	
	/**
	 * call when save or move next tab for validate form
	 */
	public boolean validate() {
		setFormValidate(true);
		setValidateMsgs(doValidate(dirtyValue));
		return validateMsgs.size() == 0;
	}
	
	public void resetValidate() {
		dirtyValue = getValue();
		setFormValidate(false);
	}
	
	public boolean isMandatory () {
		return getColModel().isMandatory();
	}
	
	protected List<String> doValidate(Object inputValue) {
		List<String> validateMsgs = new ArrayList<>();
		if (isMandatory() && inputValue == null) {
			validateMsgs.add(Msg.getMsg(Env.getCtx(), "ZZValidateNotNull"));
		}else if (inputValue == null) {
			// no validate	
		}else {
			try {
				if (getCellType() == CellModel.PHONE_CELL) {
					RegistrationWindow.validateCellNo(null, inputValue.toString());
				}else if (getCellType() == CellModel.EMAIL_CELL) {
					RegistrationWindow.validateEmail(null, inputValue.toString());
				}else if (getCellType() == CellModel.ID_PASSPORTNO_CELL) {
					RegistrationWindow.validateIdNo(null, inputValue.toString());
				}
				
			}catch (WrongValueException e) {
				validateMsgs.add(e.getMessage());
				
			}
		}
		return validateMsgs;
	}
	/**
	 * call from org.zkoss.bind.Validator for immediate validate a input
	 * If validation fails, ViewModel's (or middle object's) properties will be unchanged
	 * @param inputValue
	 * @return
	 */
	public List<String> validate(Object inputValue) {
		setFormValidate(false);
		dirtyValue = inputValue;
		return doValidate(inputValue);
	}
	
	/**
	 * child classes should override this method to handle value changes
	 * @param value The new value
	 */
	@Override
	public void cmdValueChange(String value) {

	}
	
	@Override
	public void cmdValueChange(InputEvent event) {
		this.cmdValueChange(event.getValue());
		callCollModelHandle(event);
	}
	 
	protected void callCollModelHandle(Event event) {
		getColModel().cellValueChange(event, this);
	}
	
	public void cmdCellAction(Event event) {
		callCollModelHandle(event);
		
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
	public static int BUTTON_CELL=10;
	public static int PHONE_CELL=11;
	public static int EMAIL_CELL=12;
	public static int ID_PASSPORTNO_CELL=13;
	public static int PRESET_TITLE_CELL=14;

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
			if (properties.colType != null && properties.colType == CellModel.LABEL_CELL) {
				colModel.setReadonly(true);
				colModel.setMandatory(false);
			}
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
	
	public static  ColumnModel getColModelForLabel(String title, String daoPropertyName) {
		return getColModelForCell(CellModelInfo.of(ColumnModel.class, CellModel.class, null), CellModelParams.of(title, daoPropertyName, LABEL_CELL));
	}

	public static  ColumnModel getColModelForText(String title, String daoPropertyName) {
		return getColModelForCell(CellModelInfo.of(ColumnModel.class, CellModel.class, null), CellModelParams.of(title, daoPropertyName, TEXT_CELL));
	}
	
	public static  ColumnModel getColModelForPositiveNumber(String title, String daoPropertyName) {
		return getColModelForCell(CellModelInfo.of(ColumnModel.class, CellModel.class, null), CellModelParams.of(title, daoPropertyName, POSITIVE_NUM_CELL));
	}
	
	public static  ColumnModel getColModelForPhone(String title, String daoPropertyName) {
		return getColModelForCell(CellModelInfo.of(ColumnModel.class, CellModel.class, null), CellModelParams.of(title, daoPropertyName, PHONE_CELL));
	}
	
	public static  ColumnModel getColModelForEmail(String title, String daoPropertyName) {
		return getColModelForCell(CellModelInfo.of(ColumnModel.class, CellModel.class, null), CellModelParams.of(title, daoPropertyName, EMAIL_CELL));
	}
	
	public static  ColumnModel getColModelForIDPASS(String title, String daoPropertyName) {
		return getColModelForCell(CellModelInfo.of(ColumnModel.class, CellModel.class, null), CellModelParams.of(title, daoPropertyName, ID_PASSPORTNO_CELL));
	}
	
	public static  ColumnModel getColModelForRadio(String title, String daoPropertyName) {
		return getColModelForCell(CellModelInfo.of(ColumnModel.class, CellModel.class, null), CellModelParams.of(title, daoPropertyName, RADIO_CELL));
	}
	
	public static ColumnModel getColModelForGenericCell(
			String title, String daoPropertyName, int cellType) {
		return getColModelForCell(CellModelInfo.of(ColumnModel.class, CellModel.class, null)
				, CellModelParams.of(title, daoPropertyName, cellType));
	}

	public boolean isTextfield () {
		return getCellType() == TEXT_CELL || getCellType() == EMAIL_CELL || getCellType() == ID_PASSPORTNO_CELL || getCellType() == PHONE_CELL; 
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
		Object oldValue = this.value;
		this.value = value;
		this.dirtyValue = value;
		valuePropertyChangeSupport.firePropertyChange("value", oldValue, value);
		BindUtils.postNotifyChange(this, "value");
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

	public String getIconSclass() {
		return iconSclass;
	}

	public void setIconSclass(String iconSclass) {
		this.iconSclass = iconSclass;
	}

	public void setValueFromDao(PO daoPerCol) {
		if (StringUtils.isNotBlank(getColModel().getDaoPropertyName())) {
			Object fieldValue = daoPerCol.get_Value(getColModel().getDaoPropertyName());
			setValue(fieldValue);
		}else {
			if(Log.isInfoEnabled()) {
				log.info("Column " + getColModel().getTitle() + " don't set matching to dao");
			}
			
		}
		
	}

	public boolean isFormValidate() {
		return formValidate;
	}

	public boolean isFieldValidate() {
		return !formValidate;
	}
	
	public void setFormValidate(boolean formValidate) {
		this.formValidate = formValidate;
		BindUtils.postNotifyChange(this, "formValidate");
		BindUtils.postNotifyChange(this, "fieldValidate");
		
	}

	public List<String> getValidateMsgs() {
		return validateMsgs;
	}

	public void setValidateMsgs(List<String> validateMsgs) {
		this.validateMsgs = validateMsgs;
		BindUtils.postNotifyChange(this, "validateMsgs");
	}

	public void copyTo(CellModel cellModel) {
		cellModel.setValue(getValue());
		
	}	
	
	public void reset(boolean perRow) {
		dirtyValue = null;//TODO: move to reset function of cell
		value = null;
		if (!perRow) {
			initDefaultValue();
		}
		
	}

}
