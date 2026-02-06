package za.co.ntier.webform.sdr.component.bean.cell;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.compiere.model.MCity;

import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;

public class PostalCellModel extends CellModel{
	public PostalCellModel(TableModel tableModel, RowModel rowModel, ColumnModel colModel) {
		super(tableModel, rowModel, colModel);
		setCellType(TEXT_CELL);
	}

	@Override
	public void cmdValueChange(String value) {
		AreaCellModel areaCellModel = getCellModelByType(AreaCellModel.class);

		if (areaCellModel != null) {
			List<MCity> citys = MasterUtil.getCitiesByPostal(value);
			
			areaCellModel.setDataProvider(citys);

			if (citys.size() > 0) {
				areaCellModel.updateProvinceSelected(citys.get(0));
			}
			
		}
	}
	
	public static ColumnModel getPostalColumnModel(String title, String daoPropertyName) {
		if (title == null)
			title = "Postal Code";
		return CellModel.getColModelForCell(CellModelInfo.of(ColumnModel.class, PostalCellModel.class, null), CellModelParams.of(title, daoPropertyName, null));
	}


}
