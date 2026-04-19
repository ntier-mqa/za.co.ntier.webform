package za.co.ntier.webform.sdr.component.bean.cell;

import java.util.List;

import org.adempiere.webui.panel.RegistrationWindow;
import org.apache.commons.lang3.StringUtils;
import org.compiere.model.PO;
import org.zkoss.zk.ui.WrongValueException;

import za.co.ntier.api.model.I_AD_User;
import za.co.ntier.api.model.X_ZZ_AlternateIDType;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;


public class IDCellModel extends CellModel {
	public static final String idTypeRSA_ID = "RSA ID Number"; 
	public IDCellModel(TableModel tableModel, RowModel rowModel, ColumnModel colModel) {
		super(tableModel, rowModel, colModel);
		setCellType(ID_PASSPORTNO_CELL);
	}
	
	public static ColumnModel getIDColumnModel() {
		String title = MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_ZZ_ID_Passport_No);
		ColumnModel col = CellModel.getColModelForCell(CellModelInfo.of(ColumnModel.class, IDCellModel.class, null), CellModelParams.of(title, I_AD_User.COLUMNNAME_ZZ_ID_Passport_No, null));
		return col;
	}
	
	
	@Override
	protected List<String> doValidate(Object inputValue) {
		List<String> validateMsgs = super.doValidate(inputValue);
		if (StringUtils.isBlank((String)inputValue)) {
			return validateMsgs;
		}
		
		
		
		if (isRsaId()) {
			try {
				RegistrationWindow.validateIdNo(null, inputValue.toString());
			}catch (WrongValueException e) {
				validateMsgs.add(e.getMessage());
			}
		}
		
		return validateMsgs;
	}
	
	protected boolean isRsaId () {
		for(ColumnModel col : getTableModel().getColumnInfos()) {
			if (I_AD_User.COLUMNNAME_ZZ_AlternateIDType_ID.equals(col.getDaoPropertyName())) {
				@SuppressWarnings("unchecked")
				ListCellModel<X_ZZ_AlternateIDType> idTypeCell = (ListCellModel<X_ZZ_AlternateIDType>)getRowModel().get(col);
				String selectedIDTypeName = null;
				if (idTypeCell.getSelectedItem() != null)
					selectedIDTypeName = idTypeCell.getSelectedItem().getName();
				return idTypeRSA_ID.equals(selectedIDTypeName);
			}
		}
		
		return true;
	}
	
	@Override
	public void saveUIToDao(PO daoPerCol) {
		if (isRsaId()) {
			if (daoPerCol.get_ColumnIndex(I_AD_User.COLUMNNAME_ZZOtherIDNo) > -1) {// exclude for case on table I_ZZ_EDP_Application
				MasterUtil.setObjectProperty(daoPerCol, I_AD_User.COLUMNNAME_ZZOtherIDNo, null);
			}
			MasterUtil.setObjectProperty(daoPerCol, I_AD_User.COLUMNNAME_ZZ_ID_Passport_No, getValue());
		}else {
			MasterUtil.setObjectProperty(daoPerCol, I_AD_User.COLUMNNAME_ZZ_ID_Passport_No, null);
			MasterUtil.setObjectProperty(daoPerCol, I_AD_User.COLUMNNAME_ZZOtherIDNo, getValue());
		}
	}
	
	@Override
	public void setValueFromDao(PO daoPerCol) {
		Object fieldValue = null;
		if (isRsaId()) {
			fieldValue = daoPerCol.get_Value(I_AD_User.COLUMNNAME_ZZ_ID_Passport_No);
		}else {
			fieldValue = daoPerCol.get_Value(I_AD_User.COLUMNNAME_ZZOtherIDNo);
		}
		
		setValue(fieldValue);
	}
}
