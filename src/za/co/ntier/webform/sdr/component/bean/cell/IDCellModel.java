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
	/** Passport number captured at self-registration (AD_User only) */
	public static final String COLUMNNAME_ZZ_Passport_No = "ZZ_Passport_No";
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
			// ZZ_Passport_No is read first (see setValueFromDao), so keep it in step with the
			// saved value; otherwise an edited number would be hidden by the registration one.
			if (hasColumn(daoPerCol, COLUMNNAME_ZZ_Passport_No)) {
				MasterUtil.setObjectProperty(daoPerCol, COLUMNNAME_ZZ_Passport_No, getValue());
			}
		}
	}
	
	@Override
	public void setValueFromDao(PO daoPerCol) {
		Object fieldValue = null;
		if (isRsaIdForDao(daoPerCol)) {
			fieldValue = daoPerCol.get_Value(I_AD_User.COLUMNNAME_ZZ_ID_Passport_No);
		}else {
			// Passport number from self-registration first, then the "other ID" number.
			// Last, ZZ_ID_Passport_No: SDF migration stores non-RSA numbers there too.
			fieldValue = firstNonBlank(daoPerCol, COLUMNNAME_ZZ_Passport_No, I_AD_User.COLUMNNAME_ZZOtherIDNo,
					I_AD_User.COLUMNNAME_ZZ_ID_Passport_No);
		}
		
		setValue(fieldValue);
	}
	
	/**
	 * RSA ID or not, judged from the ID Type saved on the record when there is one. The ID Type
	 * cell may come after this cell in the form and so still hold its default (RSA ID) while
	 * this cell is loaded; fall back to the saved numbers, then the cell, when nothing is saved.
	 */
	protected boolean isRsaIdForDao(PO daoPerCol) {
		if (hasColumn(daoPerCol, I_AD_User.COLUMNNAME_ZZ_AlternateIDType_ID)) {
			int savedTypeId = daoPerCol.get_ValueAsInt(I_AD_User.COLUMNNAME_ZZ_AlternateIDType_ID);
			if (savedTypeId > 0) {
				for (X_ZZ_AlternateIDType type : MasterUtil.getAlternateIDType()) {
					if (type.getZZ_AlternateIDType_ID() == savedTypeId)
						return idTypeRSA_ID.equals(type.getName());
				}
			}
		}
		// No ID Type saved yet (self-registration does not store one): a registered passport
		// number with no SA ID number means this is not an RSA ID.
		if (firstNonBlank(daoPerCol, I_AD_User.COLUMNNAME_ZZ_ID_Passport_No) == null
				&& firstNonBlank(daoPerCol, COLUMNNAME_ZZ_Passport_No) != null) {
			return false;
		}
		return isRsaId();
	}
	
	private static Object firstNonBlank(PO po, String... columnNames) {
		for (String columnName : columnNames) {
			if (!hasColumn(po, columnName))
				continue;
			Object value = po.get_Value(columnName);
			if (value != null && !StringUtils.isBlank(value.toString()))
				return value;
		}
		return null;
	}
	
	private static boolean hasColumn(PO po, String columnName) {
		return po.get_ColumnIndex(columnName) > -1;
	}
}
