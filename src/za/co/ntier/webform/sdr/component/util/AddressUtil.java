package za.co.ntier.webform.sdr.component.util;

import java.util.ArrayList;
import java.util.List;

import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.Query;

import za.co.ntier.api.model.I_ZZ_FormContact;
import za.co.ntier.api.model.X_ZZ_Application_Form;
import za.co.ntier.api.model.X_ZZ_FormContact;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.bean.AddressType;
import za.co.ntier.webform.form.bean.ProgramType;
import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.cell.AreaCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.PostalCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.ProvinceCellModel;

public class AddressUtil {
	public static TableModel initAddress(ProgramType programType, AddressType addressType, X_ZZ_Application_Form applicationForm) {
		List<ColumnModel> cols = new ArrayList<>();

		if(showSiteName(programType, addressType)) {
			ColumnModel siteNameCol = CellModel.getColModelForText("Site Name", I_ZZ_FormContact.COLUMNNAME_ZZ_SideName).required();
			cols.add(siteNameCol);
		}

		if(showLineAddress(programType, addressType)) {
			ColumnModel addressCol = CellModel.getColModelForText("Street Name and Number", I_ZZ_FormContact.COLUMNNAME_Address).required();
			cols.add(addressCol);
		}

		if (showPostalAddress(addressType)) {
			ColumnModel addressCol = CellModel.getColModelForText("Post Address", I_ZZ_FormContact.COLUMNNAME_Address).required();
			cols.add(addressCol);
		}

		if (showGeographicAddress(addressType)) {
			ColumnModel postalCodeCol = PostalCellModel.getPostalColumnModel(
					MasterUtil.getNameOfColTranslated(I_ZZ_FormContact.Table_Name, I_ZZ_FormContact.COLUMNNAME_Postal)
					, I_ZZ_FormContact.COLUMNNAME_Postal
					).required();
			cols.add(postalCodeCol);

			ColumnModel areaCol = AreaCellModel.getAreaColumnModel(
					MasterUtil.getNameOfColTranslated(I_ZZ_FormContact.Table_Name, I_ZZ_FormContact.COLUMNNAME_C_City_ID)
					, I_ZZ_FormContact.COLUMNNAME_C_City_ID).required();
			cols.add(areaCol);

			ColumnModel provinceCol = ProvinceCellModel.getProvinceColumnModel(
					MasterUtil.getNameOfColTranslated(I_ZZ_FormContact.Table_Name, I_ZZ_FormContact.COLUMNNAME_C_Region_ID)
					, I_ZZ_FormContact.COLUMNNAME_C_Region_ID).required();
			cols.add(provinceCol);
		}

		if (showContact(addressType)) {
			ColumnModel nameSurnameCol = CellModel.getColModelForText("Name and Surname", I_ZZ_FormContact.COLUMNNAME_ContactName).required();
			cols.add(nameSurnameCol);

			ColumnModel telNumberCol = CellModel.getColModelForText("Tel Number", I_ZZ_FormContact.COLUMNNAME_Phone).required();
			cols.add(telNumberCol);

			ColumnModel alternativeNumberCol = CellModel.getColModelForText("Alternative Number", I_ZZ_FormContact.COLUMNNAME_Phone2).required();
			cols.add(alternativeNumberCol);

			ColumnModel emailCol = CellModel.getColModelForText("E-mail", I_ZZ_FormContact.COLUMNNAME_EMail).required();
			cols.add(emailCol);
		}

		TableModel addressFormBean = TableModel.getTableBean(TableModel.class, cols, false);
		addressFormBean.setSectionHeader(getAddressTitle(programType, addressType));
		addressFormBean.setDataType(addressType.toString());
		addressFormBean.setPoSupplier((ann, appForm) -> {
			X_ZZ_FormContact po = new X_ZZ_FormContact(appForm.getCtx(), 0, null);
			po.setZZ_Application_Form_ID(appForm.getZZ_Application_Form_ID());
			po.setZZ_ContactType(ann.getDataType());
			return po;
		});


		List<PO> savedDaos = null;
		if(applicationForm != null) {
			String where = String.format("%s = ? AND %s = ?",
					I_ZZ_FormContact.COLUMNNAME_ZZ_Application_Form_ID
					, I_ZZ_FormContact.COLUMNNAME_ZZ_ContactType);

			Query querySavedDaos = MTable.get(X_ZZ_FormContact.Table_ID).createQuery(where, null);
			savedDaos = querySavedDaos.setParameters(
					applicationForm.getZZ_Application_Form_ID()
					, addressFormBean.getDataType()).list();
		}

		addressFormBean.init(applicationForm, savedDaos);
		return addressFormBean;
	}

	private static boolean showSiteName(ProgramType programType, AddressType addressType) {
		return (programType != null && programType.isShowAddressSiteField())  || addressType == AddressType.VACATION;
	}

	private static boolean showLineAddress(ProgramType programType, AddressType addressType) {
		return addressType == AddressType.MAIN || addressType == AddressType.MAIN_ALTER
				|| addressType == AddressType.PHYSICAL || addressType == AddressType.VACATION;
	}

	private static boolean showPostalAddress(AddressType addressType) {
		return addressType == AddressType.POSTAL;
	}

	private static boolean showGeographicAddress(AddressType addressType) {
		return addressType == AddressType.MAIN || addressType == AddressType.MAIN_ALTER
				|| addressType == AddressType.PHYSICAL || addressType == AddressType.POSTAL
				|| addressType == AddressType.VACATION;
	}

	private static boolean showContact(AddressType addressType) {
		return addressType == AddressType.MAIN || addressType == AddressType.MAIN_ALTER
				|| addressType == AddressType.ORG || addressType == AddressType.ORG_ALTER
				|| addressType == AddressType.VACATION;
	}

	private static String getAddressTitle(ProgramType programType, AddressType addressType) {
		return addressType.getAddressTitle(programType, addressType == AddressType.MAIN_ALTER);
	}
}
