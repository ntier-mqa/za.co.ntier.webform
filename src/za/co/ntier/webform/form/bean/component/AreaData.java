package za.co.ntier.webform.form.bean.component;

import java.util.List;
import java.util.Map;

import org.compiere.model.MCity;
import org.zkoss.bind.BindUtils;
import org.zkoss.zul.ListModelList;

import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.bean.DataType;


public class AreaData {
	private AnnexureInfo annexure;
	private ListModelList<MCity> areas;
	private Map<ColumnInfo<?>, Object> row;
	
	public AreaData(AnnexureInfo annexure, Map<ColumnInfo<?>, Object> row){
		this.annexure = annexure;
		this.row = row;
		areas = new ListModelList<MCity>();
		
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
	 * @param annexure the annexure to set
	 */
	public void setAnnexure(AnnexureInfo annexure) {
		this.annexure = annexure;
	}

	/**
	 * @param dataProvider the dataProvider to set
	 */
	public void setDataProvider(List<MCity> dataProvider) {
		areas.clear();
		
		if (dataProvider != null)
			areas.addAll(dataProvider);
		
		if (areas.size() == 1) {
			areas.addToSelection(areas.get(0));
		}
	}

	/**
	 * @param row the row to set
	 */
	public void setRow(Map<ColumnInfo<?>, Object> row) {
		this.row = row;
	}

	
	public void areaSelect(MCity selectedArea) {
		if (annexure != null && row != null) {
			ColumnInfo<?> postalCol = AnnexureInfo.lookupColByDataType(DataType.Postal, annexure);
			if (postalCol != null) {
				PostalData postalData = (PostalData) row.get(postalCol);
				if (selectedArea == null) {
					postalData.setPostal(null);
				}else {
					postalData.setPostal(selectedArea.getPostal());
				}
					
				BindUtils.postNotifyChange(postalData, "postal");
			}
			
		}
	}
	

	/**
	 * @return the areas
	 */
	public ListModelList<MCity> getAreas() {
		return areas;
	}
	
	public boolean isSelected() {
		return !areas.isSelectionEmpty();
	}
	
	public MCity getSelectedArea() {
		if (areas.getSelection().isEmpty())
			return null;
		return areas.getSelection().iterator().next();
	}
	
	public void setSelectedArea(Object cityId) {
		areas.clearSelection();
		if (cityId == null || (int)cityId == 0) {
			;
		}else {
			//for(MCity area : areaData.getDataProvider()) {
			for(MCity area : MasterUtil.getCities()) {// search on all cities, not only in data provider
				if (area.getC_City_ID() == (int)cityId) {
					if (!areas.contains(area))
						areas.add(area);
					
					areas.addToSelection(area);
				}
			}
		}
	}
}
