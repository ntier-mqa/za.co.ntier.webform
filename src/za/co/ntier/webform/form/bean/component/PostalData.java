package za.co.ntier.webform.form.bean.component;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.compiere.model.MCity;
import org.zkoss.bind.BindUtils;

import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.bean.DataType;

public class PostalData{
	private String postal;
	private AnnexureInfo annexure;
	private Map<ColumnInfo<?>, Object> row;
	
	public PostalData(AnnexureInfo annexure, Map<ColumnInfo<?>, Object> row, String postal) {
		this.annexure = annexure;
		this.postal = postal;
		this.row = row;
	}
	
	public void setPostalInternal(String postal) {
		this.postal = postal;
	}
	
	
	public void setPostal(String postal) {
		this.postal = postal;
		
		if (annexure != null && row != null) {
			ColumnInfo<?> areaCol = AnnexureInfo.lookupColByDataType(DataType.Area, annexure);
			if (areaCol != null) {
				List<MCity> citys = MasterUtil.getCitiesByPostal(postal);
				
				AreaData areaData = (AreaData) row.get(areaCol);
				areaData.setDataProvider(citys);
				
				areaData.setSelectedAreaInternal(null);
				if (citys.size() == 1) {
					areaData.setSelectedAreaInternal(citys.get(0));
				}
				BindUtils.postNotifyChange(areaData, "dataProvider");
				
			}
			
		}
		
	}

	/**
	 * @return the postal
	 */
	public String getPostal() {
		return postal;
	}
	
}
