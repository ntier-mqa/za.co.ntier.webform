package za.co.ntier.webform.sdr.component.bean.cell;

import za.co.ntier.webform.sdr.component.bean.IValueChange;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;


public class TextData extends AbstractCellModel implements IValueChange{
	private String value;
	private int lines = 1;
	
	public TextData(TableModel annexure, RowModel row) {
		super(annexure, row);
	}
	
	

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}


	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
		
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
	public void cmdValueChange(String selectedObj) {
		// do nothing
		
	}
}
