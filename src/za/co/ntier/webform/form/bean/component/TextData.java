package za.co.ntier.webform.form.bean.component;

import java.util.Map;


public class TextData implements ICellData{
	private AnnexureInfo annexure;
	private Map<ColumnInfo<?>, Object> row;
	private String value;
	private int lines = 1;
	
	public TextData(AnnexureInfo annexure, Map<ColumnInfo<?>, Object> row, String value) {
		this.value = value;
		this.annexure = annexure;
		this.setRow(row);
	}
	
	public TextData() {
		
	}
	
	/**
	 * @return the annexure
	 */
	public AnnexureInfo getAnnexure() {
		return annexure;
	}

	/**
	 * @return the row
	 */
	public Map<ColumnInfo<?>, Object> getRow() {
		return row;
	}
	
	

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param annexure the annexure to set
	 */
	public void setAnnexure(AnnexureInfo annexure) {
		this.annexure = annexure;
	}

	/**
	 * @param row the row to set
	 */
	public void setRow(Map<ColumnInfo<?>, Object> row) {
		this.row = row;
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
	public void clearData() {
		value = null;
		
	}
}
