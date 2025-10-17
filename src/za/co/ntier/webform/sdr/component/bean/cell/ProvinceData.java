package za.co.ntier.webform.sdr.component.bean.cell;

import java.util.ArrayList;
import java.util.List;

import org.compiere.model.MCity;
import org.compiere.model.MRegion;
import org.zkoss.bind.BindUtils;
import org.zkoss.zul.ListModelList;

import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.sdr.component.bean.DataType;
import za.co.ntier.webform.sdr.component.bean.TableModel;

public class ProvinceData extends AbstractListCellModel<MRegion>{
	
	public ProvinceData(TableModel annexure, RowModel row){
		super(annexure, row);
		setModel(new ListModelList<MRegion>());		
	}

	@Override
	public void cmdSelectedHandle(MRegion selectedProvince) {
		if (getAnnexure() != null && getRow() != null) {
			ColumnModel postalCol = TableModel.lookupColByDataType(DataType.Postal, getAnnexure());
			ColumnModel areaCol = TableModel.lookupColByDataType(DataType.Area, getAnnexure());
			
			if (selectedProvince == null) {
				return;
				// do nothing
			}
			
			AreaData areaData = null;
			if (areaCol != null) {
				List<MCity> areaFilters = new ArrayList<>();

				MasterUtil.getCities().stream().filter(city -> city.getC_Region_ID() == selectedProvince.getC_Region_ID())
						.limit(MasterUtil.limitItem).forEach(city -> {
							areaFilters.add(city);
						});
				
				areaData = (AreaData)getRow().get(areaCol);
				areaData.setDataProvider(areaFilters);
			}
			
			if (postalCol != null && areaData != null) {
				PostalData postalData = (PostalData) getRow().get(postalCol);
				
				if (areaData.isSelected()) {
					postalData.setPostal(areaData.getSelectedItem().getPostal());
				}else {
					postalData.setPostal(null);
				}
					
				BindUtils.postNotifyChange(postalData, "postal");
			}
			
		}
	}
	

	@Override
	public void setSelectedItemById(Object regionId) {
		getModel().clearSelection();
		if (regionId == null || (int)regionId == 0) {
			;
		}else {
			//for(MCity area : areaData.getDataProvider()) {
			for(MRegion province : MasterUtil.getRegions()) {// search on all cities, not only in data provider
				if (province.getC_Region_ID() == (int)regionId) {
					if (!getModel().contains(province))
						getModel().add(province);
					
					getModel().addToSelection(province);
				}
			}
		}
		
	}

	@Override
	public int getSelectedID() {
		if (getSelectedItem() == null)
			return 0;
		else
			return getSelectedItem().getC_Region_ID();
	}

	@Override
	public String getDisplayText(MRegion item) {
		return item.getName();
	}
}
