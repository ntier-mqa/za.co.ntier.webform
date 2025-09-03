package za.co.ntier.webform.form.bean.component;

import java.util.Map;

public class IntData {
	private Integer value;
	private AnnexureInfo annexure;
	private Map<ColumnInfo<?>, Object> row;

	public IntData(AnnexureInfo annexure, Map<ColumnInfo<?>, Object> row, Integer value) {
		this.value = value;
		this.annexure = annexure;
		this.setRow(row);
	}
	
	/**
	 * @return the number
	 */
	public Integer getValue() {
		return value;
	}

	/**
	 * @param number the number to set
	 */
	public void setValue(Integer value) {
		this.value = value;
		
	}
	

	/**
	 * @return the annexure
	 */
	public AnnexureInfo getAnnexure() {
		return annexure;
	}

	/**
	 * @param annexure the annexure to set
	 */
	public void setAnnexure(AnnexureInfo annexure) {
		this.annexure = annexure;
	}


	/**
	 * @return the row
	 */
	public Map<ColumnInfo<?>, Object> getRow() {
		return row;
	}

	/**
	 * @param row the row to set
	 */
	public void setRow(Map<ColumnInfo<?>, Object> row) {
		this.row = row;
	}
}
