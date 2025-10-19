package za.co.ntier.webform.sdr.component.bean.cell;

import java.util.List;
import java.util.Map;

import org.compiere.model.MCity;

import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.DataType;
import za.co.ntier.webform.sdr.component.bean.IValueChange;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;

public class PostalData extends AbstractCellModel implements IValueChange{
	private TableModel annexure;
	private String postal;
	private Map<ColumnModel, Object> row;
	
	public PostalData(TableModel annexure, RowModel row, String postal) {
		super(annexure, row);
		this.annexure = annexure;
		this.postal = postal;
		this.row = row;
	}
	
	/**
	 * @return the postal
	 */
	public String getPostal() {
		return postal;
	}
	
	
	public void setPostal(String postal) {
		this.postal = postal;
	}

	@Override
	public void cmdValueChange(String selectedObj) {
		if (annexure != null && row != null) {
			ColumnModel areaCol = TableModel.lookupColByDataType(DataType.Area, annexure);
			AreaData areaData = null;
			
			if (areaCol != null) {
				List<MCity> citys = MasterUtil.getCitiesByPostal(postal);
				
				areaData = (AreaData) row.get(areaCol);
				
				areaData.setDataProvider(citys);
				
				areaData.updateProvinceSelected();
			}
		}
	}
	
}
