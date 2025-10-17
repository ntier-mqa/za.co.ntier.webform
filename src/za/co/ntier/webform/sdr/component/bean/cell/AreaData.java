package za.co.ntier.webform.sdr.component.bean.cell;

import org.compiere.model.MCity;
import org.zkoss.bind.BindUtils;
import org.zkoss.zul.ListModelList;

import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.sdr.component.bean.DataType;
import za.co.ntier.webform.sdr.component.bean.TableModel;


public class AreaData extends AbstractListCellModel<MCity>{
	
	public AreaData(TableModel annexure, RowModel row){
		super(annexure, row);
		setModel(new ListModelList<MCity>());
	}
	
	@Override
	public void cmdSelectedHandle(MCity selectedArea) {
		if (getAnnexure() != null && getRow() != null) {
			ColumnModel postalCol = TableModel.lookupColByDataType(DataType.Postal, getAnnexure());
			if (postalCol != null) {
				PostalData postalData = (PostalData) getRow().get(postalCol);
				if (selectedArea == null) {
					postalData.setPostal(null);
				}else {
					postalData.setPostal(selectedArea.getPostal());
				}
					
				BindUtils.postNotifyChange(postalData, "postal");
			}
		
			updateProvinceSelected();
		}
	}
	
	@Override
	public void setSelectedItemById(Object cityId) {
		getModel().clearSelection();
		if (cityId == null || (int)cityId == 0) {
			;
		}else {
			//for(MCity area : areaData.getDataProvider()) {
			for(MCity area : MasterUtil.getCities()) {// search on all cities, not only in data provider
				if (area.getC_City_ID() == (int)cityId) {
					if (!getModel().contains(area))
						getModel().add(area);
					
					getModel().addToSelection(area);
				}
			}
		}
	}
	
	public void updateProvinceSelected() {
		ColumnModel provinceCol = TableModel.lookupColByDataType(DataType.Province, getAnnexure());
		
		if (provinceCol != null) {
			ProvinceData provinceData = (ProvinceData) getRow().get(provinceCol);
			if (getModel().isSelectionEmpty()) {
				provinceData.setSelectedItemById(null);
			}else {
				provinceData.setSelectedItemById(getSelectedItem().getC_Region_ID());
			}
		}
	}
	
	@Override
	public int getSelectedID() {
		if (getSelectedItem() == null)
			return 0;
		else
			return getSelectedItem().getC_City_ID();
	}

	@Override
	public String getDisplayText(MCity item) {
		return item.getName();
	}
}
