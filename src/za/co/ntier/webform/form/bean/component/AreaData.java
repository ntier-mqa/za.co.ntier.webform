package za.co.ntier.webform.form.bean.component;

import java.util.List;
import java.util.Map;

import org.compiere.model.MCity;
import org.zkoss.bind.BindUtils;

import za.co.ntier.webform.form.bean.DataType;


public class AreaData {
	private AnnexureInfo annexure;
	private List<MCity> dataProvider;
	private Map<ColumnInfo<?>, Object> row;
	private MCity selectedArea;
	
	public AreaData(AnnexureInfo annexure, Map<ColumnInfo<?>, Object> row){
		this.annexure = annexure;
		this.row = row;
	}

	/**
	 * @return the annexure
	 */
	public AnnexureInfo getAnnexure() {
		return annexure;
	}

	/**
	 * @return the dataProvider
	 */
	public List<MCity> getDataProvider() {
		return dataProvider;
	}

	/**
	 * @return the row
	 */
	public Map<ColumnInfo<?>, Object> getRow() {
		return row;
	}

	/**
	 * @return the selectedArea
	 */
	public MCity getSelectedArea() {
		return selectedArea;
	}
	
	/**
	 * @param annexure the annexure to set
	 */
	public void setAnnexure(AnnexureInfo annexure) {
		this.annexure = annexure;
	}

	/**
	 * @param dataProvider the dataProvider to set
	 */
	public void setDataProvider(List<MCity> dataProvider) {
		this.dataProvider = dataProvider;
	}

	/**
	 * @param row the row to set
	 */
	public void setRow(Map<ColumnInfo<?>, Object> row) {
		this.row = row;
	}

	/**
	 * @param selectedArea the selectedArea to set
	 */
	public void setSelectedArea(MCity selectedArea) {
		this.selectedArea = selectedArea;
		
		if (annexure != null && row != null) {
			ColumnInfo<?> postalCol = AnnexureInfo.lookupColByDataType(DataType.Postal, annexure);
			if (postalCol != null) {
				PostalData postalData = (PostalData) row.get(postalCol);
				if (selectedArea == null) {
					postalData.setPostalInternal(null);
				}else {
					postalData.setPostalInternal(selectedArea.getPostal());
				}
					
				BindUtils.postNotifyChange(postalData, "postal");
			}
			
		}		
	}

	public void setSelectedAreaInternal(MCity selectedArea) {
		this.selectedArea = selectedArea;
	}

	
}
