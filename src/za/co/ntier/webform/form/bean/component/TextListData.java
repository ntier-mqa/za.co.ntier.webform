package za.co.ntier.webform.form.bean.component;

public class TextListData implements ICellData{
	private ColumnInfo<?> col;
	public TextListData(ColumnInfo<?> col) {
		this.col = col;
	}
	
	private boolean isList = false;

	private int lines = 1;
	

	/**
	 * @return the isList
	 */
	public boolean isList() {
		return isList;
	}

	/**
	 * @param isList the isList to set
	 */
	public void setList(boolean isList) {
		this.isList = isList;
	}
	
	private Object value;
	public Object getValue() {
		return value;
	}
	
	public void setValue(Object value) {
		if (value == null)
			this.value = value;
		
		if (isList && value instanceof String) {
			for(Object item : col.getDataProvider()) {
				if (value.equals(AnnexureInfo.getDaoValue(item, col.getBeanPropertyName()))) {
					this.value = item;
					return;
				}
			}
		}else {
			this.value = value;
		}
	}

	/**
	 * @return the lines
	 */
	public int getLines() {
		return lines;
	}

	/**
	 * @param lines the lines to set
	 */
	public void setLines(int lines) {
		this.lines = lines;
	}

	@Override
	public void clearData() {
		value = null;
		
	}
}
