package za.co.ntier.webform.form.bean.component;

import java.util.HashMap;

public class AnnexureRow<T> extends HashMap<ColumnInfo<?>, Object>{
	private AnnexureInfo annexure;
	public AnnexureRow(AnnexureInfo annexure) {
		this.annexure = annexure;
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

	public void initTotalCol() {
		for(ColumnInfo<?> totalCol : annexure.getColumnInfos()) {
			if (totalCol.getColValues() != null) {
				int total = 0;
				for (ColumnInfo<?> colValue : totalCol.getColValues()) {
					IntData value = (IntData)this.get(colValue);
					if (value != null && value.getValue() != null) {
						total += value.getValue();
					}
				}
				
				LabelData totalValue = (LabelData)this.get(totalCol);
				if (total > 0) {
					totalValue.setValue(String.valueOf(total));
				}else {
					totalValue.setValue(null);
				}
			}
		}
		
	}
}
