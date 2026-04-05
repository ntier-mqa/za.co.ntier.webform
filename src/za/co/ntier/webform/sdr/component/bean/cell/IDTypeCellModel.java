package za.co.ntier.webform.sdr.component.bean.cell;

import za.co.ntier.api.model.I_AD_User;
import za.co.ntier.api.model.X_ZZ_AlternateIDType;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.column.ListColumnModel;

public class IDTypeCellModel extends ListCellModel<X_ZZ_AlternateIDType>{

	public IDTypeCellModel(TableModel tableModel, RowModel rowModel, ListColumnModel<X_ZZ_AlternateIDType> colModel) {
		super(tableModel, rowModel, colModel);
	}
	
	public static ListColumnModel<X_ZZ_AlternateIDType> getIDTypeCol() {
		@SuppressWarnings("unchecked")
		ListColumnModel<X_ZZ_AlternateIDType> alternateIDTypeCol = 
				getListColumnModel(ListColumnModel.class
						, IDTypeCellModel.class
						, MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_ZZ_AlternateIDType_ID)
						, I_AD_User.COLUMNNAME_ZZ_AlternateIDType_ID
						, MasterUtil.getAlternateIDType()
						, title -> {return title.getName();}
						, title -> {return title.getZZ_AlternateIDType_ID();}
						, CellModel.LIST_CELL);
		
		alternateIDTypeCol
			.setzClass(X_ZZ_AlternateIDType.class)
			.setUseForID(true)
			.setDefaultValue(IDCellModel.idTypeRSA_ID, MasterUtil.nameAlternateIdTypeCompare)
			.required()
			.setTableName(I_AD_User.Table_Name);
		
		return alternateIDTypeCol;
	}

}
