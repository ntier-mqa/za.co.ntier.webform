package za.co.ntier.webform.form.bean.component;

import java.util.List;
import java.util.Map;

import org.compiere.model.MCity;
import org.zkoss.bind.BindUtils;

import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.bean.DataType;

public class PostalData{
	private AnnexureInfo annexure;
	private String postal;
	private Map<ColumnInfo<?>, Object> row;
	
	public PostalData(AnnexureInfo annexure, Map<ColumnInfo<?>, Object> row, String postal) {
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

	public void postalChange(String postal) {
		setPostal(postal);
		
		if (annexure != null && row != null) {
			ColumnInfo<?> areaCol = AnnexureInfo.lookupColByDataType(DataType.Area, annexure);
			if (areaCol != null) {
				List<MCity> citys = MasterUtil.getCitiesByPostal(postal);
				
				AreaData areaData = (AreaData) row.get(areaCol);
				
				areaData.setDataProvider(citys);
				BindUtils.postNotifyChange(this, "postal");
				BindUtils.postNotifyChange(areaData, "dataProvider");
			}
			
		}
	}
	
}
