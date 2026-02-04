package za.co.ntier.webform.sdr.component.bean.cell;

import java.util.List;
import java.util.function.Function;

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
	public List<MCity> getDataProvider() {
		return MasterUtil.getCities();// search on all cities, not only in data provider
	}
	
	private Function<MCity, Object> defaultValueConvert = area -> {
		return area.getC_City_ID();
	};
	
	@Override
	public Function<MCity, Object> getValueConvert() {
		if (getColModel().getValueConvert() == null) {
			return defaultValueConvert;
		}else {
			return getColModel().getValueConvert();
		}
	}

	public void updateProvinceSelected() {
		ProvinceCellModel provinceCellModel = getCellModelByType(ProvinceCellModel.class);
		if (provinceCellModel != null) {
			if (getModel().isSelectionEmpty()) {
				provinceCellModel.setValue(0);
			}else {
				provinceCellModel.setValue(getSelectedItem().getC_Region_ID());
			}
		}
	}

	@Override
	public String getDisplayText(MCity item) {
		return item.getName();
	}

	public static ListColumnModel<MCity> getAreaColumnModel(String title, String daoPropertyName) {
	
		@SuppressWarnings("unchecked")
		ListColumnModel<MCity> listColumnModel = ListCellModel.getListColumnModel(ListColumnModel.class, AreaCellModel.class, title, daoPropertyName, MasterUtil.getInitCities(), null, null);
		listColumnModel.setUseForID(true);
		return listColumnModel;
	}


}
