package za.co.ntier.webform.sdr.component.bean.cell;

import java.util.List;

import org.compiere.model.MCity;

import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.sdr.component.bean.BaseCellModel;
import za.co.ntier.webform.sdr.component.bean.BaseColumnModel;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;

public class PostalCellModel extends BaseCellModel{
	public PostalCellModel(TableModel tableModel, RowModel rowModel) {
		super(tableModel, rowModel, BaseCellModel.TEXT_CELL);
	}

	@Override
	public void cmdValueChange(String value) {
		AreaCellModel areaCellModel = getCellModel(AreaCellModel.class);

		if (areaCellModel != null) {
			List<MCity> citys = MasterUtil.getCitiesByPostal(value);

			areaCellModel.setDataProvider(citys);

			areaCellModel.updateProvinceSelected();
		}
	}

	public static BaseColumnModel getPostalColumnModel(String title, String daoPropertyName) {
		return BaseCellModel.getColModel(PostalCellModel.class, title, daoPropertyName);
	}


}
