package za.co.ntier.webform.sdr.component.bean.cell;

import org.compiere.model.MCity;
import org.zkoss.bind.BindUtils;

import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.column.ListColumnModel;


public class AreaCellModel extends ListCellModel<MCity>{

	public AreaCellModel(TableModel tableModel, RowModel rowModel, ListColumnModel<MCity> colModel) {
		super(tableModel, rowModel, colModel);
	}

	@Override
	public void cmdSelectedHandle(MCity selectedArea) {
		if (getTableModel() != null && getRowModel() != null) {
			PostalCellModel postalCellModel = getCellModelByType(PostalCellModel.class);
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
		ProvinceCellModel provinceCellModel = getCellModelByType(ProvinceCellModel.class);
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

	public static ListColumnModel<MCity> getAreaColumnModel(String title, String daoPropertyName) {
	
		@SuppressWarnings("unchecked")
		ListColumnModel<MCity> listColumnModel = ListCellModel.getListColumnModel(ListColumnModel.class, AreaCellModel.class, title, daoPropertyName, MasterUtil.getInitCities(), null);
		return listColumnModel;
	}


}
