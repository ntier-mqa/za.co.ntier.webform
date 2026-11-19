package za.co.ntier.webform.sdr.component.bean.cell;

import org.compiere.model.PO;

import za.co.ntier.api.model.I_AD_User;
import za.co.ntier.api.model.X_ZZ_AlternateIDType;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.column.ListColumnModel;

public class IDTypeCellModel extends ListCellModel<X_ZZ_AlternateIDType>{
	@Override
	public void setValueFromDao(PO daoPerCol) {
		super.setValueFromDao(daoPerCol);
		
		// Self-registration stores no ID Type. If the user registered with a passport
		// (passport number, no SA ID number), default the type to Passport instead of RSA ID.
		if (daoPerCol.get_ColumnIndex(I_AD_User.COLUMNNAME_ZZ_AlternateIDType_ID) > -1
				&& daoPerCol.get_ValueAsInt(I_AD_User.COLUMNNAME_ZZ_AlternateIDType_ID) <= 0
				&& isBlank(daoPerCol, I_AD_User.COLUMNNAME_ZZ_ID_Passport_No)
				&& !isBlank(daoPerCol, IDCellModel.COLUMNNAME_ZZ_Passport_No)) {
			for (X_ZZ_AlternateIDType type : MasterUtil.getAlternateIDType()) {
				if (type.getName() != null && type.getName().toLowerCase().contains("passport")) {
					setValue(type.getZZ_AlternateIDType_ID());
					break;
				}
			}
		}
	}
	
	private static boolean isBlank(PO po, String columnName) {
		if (po.get_ColumnIndex(columnName) < 0)
			return true;
		Object value = po.get_Value(columnName);
		return value == null || value.toString().trim().isEmpty();
	}
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
