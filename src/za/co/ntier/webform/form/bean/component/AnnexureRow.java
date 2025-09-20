package za.co.ntier.webform.form.bean.component;

import java.util.HashMap;

public class AnnexureRow<T> extends HashMap<ColumnInfo<?>, Object>{
	
	private AnnexureInfo annexure;
	public AnnexureRow(AnnexureInfo annexure) {
		this.setAnnexure(annexure);
	}
	private static final long serialVersionUID = 3444756682531476154L;
	private T data;

	/**
	 * @return the data
	 */
	public T getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(T data) {
		this.data = data;
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
}
