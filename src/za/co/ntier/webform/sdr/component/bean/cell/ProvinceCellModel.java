package za.co.ntier.webform.sdr.component.bean.cell;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.compiere.model.MCity;
import org.compiere.model.MRegion;
import org.zkoss.bind.BindUtils;

import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.column.ListColumnModel;

public class ProvinceCellModel extends ListCellModel<MRegion>{

	public ProvinceCellModel(TableModel tableModel, RowModel rowModel, ListColumnModel<MRegion> colModel){
		super(tableModel, rowModel, colModel);
	}

	@Override
	public void cmdSelectedHandle(MRegion selectedProvince) {
		if (getTableModel() != null && getRowModel() != null) {
			if (selectedProvince == null) {
				return;
				// do nothing
			}

			AreaCellModel areaCellModel = getCellModelByType(AreaCellModel.class);
			if (areaCellModel != null) {
				List<MCity> areaFilters = new ArrayList<>();

				MasterUtil.getCities().stream().filter(city -> city.getC_Region_ID() == selectedProvince.getC_Region_ID())
				.limit(MasterUtil.limitItem).forEach(city -> {
					areaFilters.add(city);
				});
				areaCellModel.setDataProvider(areaFilters);
			}

			PostalCellModel postalCellModel = getCellModelByType(PostalCellModel.class);
			if (postalCellModel != null && areaCellModel != null) {
				if (areaCellModel.isSelected()) {
					postalCellModel.setValue(areaCellModel.getSelectedItem().getPostal());
				}else {
					postalCellModel.setValue(null);
				}

				BindUtils.postNotifyChange(postalCellModel, "value");
			}

		}
	}

	@Override
	public List<MRegion> getDataProvider() {
		return MasterUtil.getRegions();// search on all cities, not only in data provider
	}
	
	private Function<MRegion, Object> defaultValueConvert = province -> {
		return province.getC_Region_ID();
	};
	
	@Override
	public Function<MRegion, Object> getValueConvert() {
		if (getColModel().getValueConvert() == null) {
			return defaultValueConvert;
		}else {
			return getColModel().getValueConvert();
		}
	}

	@Override
	public String getDisplayText(MRegion item) {
		return item.getName();
	}

	public static ListColumnModel<MRegion> getProvinceColumnModel(String title, String daoPropertyName) {
		@SuppressWarnings("unchecked")
		ListColumnModel<MRegion> listColumnModel = ListCellModel.getListColumnModel(ListColumnModel.class, ProvinceCellModel.class, title, daoPropertyName, MasterUtil.getRegions(), null, null);
		listColumnModel.setUseForID(true);
		return listColumnModel;
	}

}
