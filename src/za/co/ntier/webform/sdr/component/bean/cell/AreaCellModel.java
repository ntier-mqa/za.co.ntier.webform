package za.co.ntier.webform.sdr.component.bean.cell;

import org.compiere.model.MCity;
import org.zkoss.bind.BindUtils;
import org.zkoss.zul.ListModelList;

import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.column.ListColumnModel;


public class AreaCellModel extends AbstractListCellModel<MCity>{
	
	public AreaCellModel(TableModel tableModel, RowModel rowModel){
		super(tableModel, rowModel);
		setModel(new ListModelList<MCity>());
	}
	
	@Override
	public void cmdSelectedHandle(MCity selectedArea) {
		if (getAnnexure() != null && getRow() != null) {
			PostalCellModel postalCellModel = getCellModel(PostalCellModel.class);
			if (postalCellModel != null) {
				if (selectedArea == null) {
					postalCellModel.setValue(null);
				}else {
					postalCellModel.setValue(selectedArea.getPostal());
				}
					
				BindUtils.postNotifyChange(postalCellModel, "value");
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
		ProvinceCellModel provinceCellModel = getCellModel(ProvinceCellModel.class);
		if (provinceCellModel != null) {
			if (getModel().isSelectionEmpty()) {
				provinceCellModel.setSelectedItemById(null);
			}else {
				provinceCellModel.setSelectedItemById(getSelectedItem().getC_Region_ID());
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
	
	public static ListColumnModel<MCity> getColumnModel(String title, String daoPropertyName) {
		ListColumnModel<MCity> colAreaModel = new ListColumnModel<MCity>(title);
		colAreaModel.setDaoPropertyName(daoPropertyName); 
		colAreaModel.setCellModelSupplier((tableModel, rowModel)->{
			return new AreaCellModel(tableModel, rowModel);
		});
		colAreaModel.setDataProvider(MasterUtil.getInitCities());
		return colAreaModel;
	}

	
}
