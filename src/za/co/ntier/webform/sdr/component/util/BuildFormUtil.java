package za.co.ntier.webform.sdr.component.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.Env;

import za.co.ntier.api.model.I_ZZPersonAddress;
import za.co.ntier.api.model.I_ZZ_FormContact;
import za.co.ntier.api.model.X_ZZPersonAddress;
import za.co.ntier.api.model.X_ZZ_Application_Form;
import za.co.ntier.api.model.X_ZZ_FormContact;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.bean.AddressType;
import za.co.ntier.webform.form.bean.ProgramType;
import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.cell.AreaCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.PostalCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.ProvinceCellModel;

public class BuildFormUtil {
	public static TableModel buildFormContact(ProgramType programType, AddressType addressType, 
			X_ZZ_Application_Form applicationForm,
			Function<RowModel, PO> poSupplier, Function<PO, Boolean> beforeSave,
			TableModel copyFrom) {
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel dupplicateCol = null;
		if (copyFrom != null) {
			dupplicateCol = CellModel.getColModelForGenericCell("Copy Address", null, CellModel.BUTTON_CELL);
			dupplicateCol.setShowTitle(false);
			
			dupplicateCol.setEventHandle((inputEvent, cellModel) -> {
				
					RowModel postalRow = cellModel.getRowModel();
					RowModel physicalRow = copyFrom.getRow();
					
					for (ColumnModel postalColModel : cellModel.getTableModel().getColumnInfos()) {
						for (ColumnModel physicalColModel : copyFrom.getColumnInfos()) {
							if (postalColModel.getDaoPropertyName() != null && StringUtils.equals(physicalColModel.getDaoPropertyName(), postalColModel.getDaoPropertyName())) {
								postalRow.get(postalColModel).setValue(physicalRow.get(physicalColModel).getValue());
								break;
							}
						}
					}
			});
			
			cols.add(dupplicateCol);
		}
		
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
					//MasterUtil.getNameOfColTranslated(I_ZZ_FormContact.Table_Name, I_ZZ_FormContact.COLUMNNAME_Postal)
					null
					, I_ZZ_FormContact.COLUMNNAME_Postal
					).required();
			cols.add(postalCodeCol);

			ColumnModel areaCol = AreaCellModel.getAreaColumnModel(
					//MasterUtil.getNameOfColTranslated(I_ZZ_FormContact.Table_Name, I_ZZ_FormContact.COLUMNNAME_C_City_ID)
					null
					, I_ZZ_FormContact.COLUMNNAME_C_City_ID).required();
			cols.add(areaCol);

			ColumnModel provinceCol = ProvinceCellModel.getProvinceColumnModel(
					//MasterUtil.getNameOfColTranslated(I_ZZ_FormContact.Table_Name, I_ZZ_FormContact.COLUMNNAME_C_Region_ID)
					null
					, I_ZZ_FormContact.COLUMNNAME_C_Region_ID).required();
			cols.add(provinceCol);
		}

		if (showContact(addressType)) {
			ColumnModel nameSurnameCol = CellModel.getColModelForText("Name and Surname", I_ZZ_FormContact.COLUMNNAME_ContactName).required();
			cols.add(nameSurnameCol);

			ColumnModel designCol = CellModel.getColModelForText(
					MasterUtil.getNameOfColTranslated(I_ZZ_FormContact.Table_Name, I_ZZ_FormContact.COLUMNNAME_ZZ_Designation)
					, I_ZZ_FormContact.COLUMNNAME_ZZ_Designation
					).required();
			cols.add(designCol);
			
			ColumnModel telNumberCol = CellModel.getColModelForPhone("Tel Number", I_ZZ_FormContact.COLUMNNAME_Phone).required();
			cols.add(telNumberCol);

			ColumnModel alternativeNumberCol = CellModel.getColModelForPhone("Alternative Number", I_ZZ_FormContact.COLUMNNAME_Phone2).required();
			cols.add(alternativeNumberCol);

			ColumnModel emailCol = CellModel.getColModelForEmail("E-mail", I_ZZ_FormContact.COLUMNNAME_EMail).required();
			cols.add(emailCol);
		}

		TableModel addressFormBean = TableModel.getTableBean(TableModel.class, cols, false);
		addressFormBean.setSubSectionHeader(getAddressTitle(programType, addressType));
		addressFormBean.setDataType(addressType.toString());
		if (copyFrom != null) {
			addressFormBean.setSclass("formContact formContactCopy");
		}else{
			addressFormBean.setSclass("formContact");
		}
		
		
		if (beforeSave != null)
			addressFormBean.setBeforeSave(beforeSave);
		if (poSupplier != null)
			addressFormBean.setPoSupplier(poSupplier);

		final ColumnModel dupplicateColF = dupplicateCol;
		if (copyFrom != null) {
			addressFormBean.setDecoratorCell(rowModel -> {
				CellModel btDupplicate = rowModel.get(dupplicateColF);
				btDupplicate.setIconSclass("z-icon-fw z-icon-clone z-icon-solid");
			});
		}
		
		List<PO> savedDaos = null;
		if(applicationForm != null) {
			String where = String.format("%s = ? AND %s = ?",
					I_ZZ_FormContact.COLUMNNAME_ZZ_Application_Form_ID
					, I_ZZ_FormContact.COLUMNNAME_ZZ_ContactType);

			Query querySavedDaos = MTable.get(Env.getCtx(), X_ZZ_FormContact.Table_Name).createQuery(where, null);
			PO savedDao = querySavedDaos.setParameters(
					applicationForm.getZZ_Application_Form_ID()
					, addressFormBean.getDataType()).first();
			
			if (savedDao != null)
				savedDaos = List.of(savedDao);
		}

		addressFormBean.init(savedDaos);
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

	public static TableModel getAddressDetailComp(String prefixName, String addressType, String subHeader, boolean isSdfAddress, int parentId, TableModel copyto) {
		prefixName = "";
		List<ColumnModel> colsAddress = new ArrayList<>();
		
		ColumnModel complexSectionFarmCol = CellModel.getColModelForText(
				prefixName + MasterUtil.getDescOfColTranslated(I_ZZPersonAddress.Table_Name, I_ZZPersonAddress.COLUMNNAME_ZZComplexSectionFarm)
				, I_ZZPersonAddress.COLUMNNAME_ZZComplexSectionFarm
				).required();
		colsAddress.add(complexSectionFarmCol);
		
		ColumnModel physicalAddress1Col = CellModel.getColModelForText(
				prefixName + MasterUtil.getNameOfColTranslated(I_ZZPersonAddress.Table_Name, I_ZZPersonAddress.COLUMNNAME_Address1)
				, I_ZZPersonAddress.COLUMNNAME_Address1
				).required();
		colsAddress.add(physicalAddress1Col);
	
		ColumnModel physicalCodeCol = PostalCellModel.getPostalColumnModel(
				null
				, I_ZZPersonAddress.COLUMNNAME_Postal
				).required();
		colsAddress.add(physicalCodeCol);
	
		ColumnModel physicalAreaCol = AreaCellModel.getAreaColumnModel(
				null
				, I_ZZPersonAddress.COLUMNNAME_C_City_ID).required();
		colsAddress.add(physicalAreaCol);
		
		ColumnModel physicalProvinceCol = ProvinceCellModel.getProvinceColumnModel(
				null
				, I_ZZPersonAddress.COLUMNNAME_C_Region_ID).required();
		colsAddress.add(physicalProvinceCol);
	
		ColumnModel dupplicateCol = null;
		if (copyto != null) {
			dupplicateCol = CellModel.getColModelForGenericCell("Use Physical Address For Postal Address?", null, CellModel.BUTTON_CELL);
			dupplicateCol.setShowTitle(false);
			
			dupplicateCol.setEventHandle((inputEvent, cellModel) -> {
				
					RowModel physicalRow = cellModel.getRowModel();
					RowModel postalRow = copyto.getRow();
					for (ColumnModel physicalColModel : cellModel.getTableModel().getColumnInfos()) {
						for (ColumnModel postalColModel : copyto.getColumnInfos()) {
							if (physicalColModel.getDaoPropertyName() != null && StringUtils.equals(postalColModel.getDaoPropertyName(), physicalColModel.getDaoPropertyName())) {
								postalRow.get(postalColModel).setValue(physicalRow.get(physicalColModel).getValue());
								break;
							}
						}
					}
			});
			
			colsAddress.add(dupplicateCol);
		}
		
		TableModel addressDetailBean = TableModel.getTableBean(TableModel.class, colsAddress, false);
		addressDetailBean.setSclass(addressType + " srd-address");
		addressDetailBean.setPoSupplier((ann) -> {
			X_ZZPersonAddress po = new X_ZZPersonAddress(Env.getCtx(), 0, null);
			po.setZZAddressType(addressType);
			if(isSdfAddress) {
				po.setAD_User_ID(parentId);
			}else {//org address
				po.setC_BPartner_ID(parentId);
			}
			
			return po;
		});
	
		addressDetailBean.setSubSectionHeader(subHeader);
		
		final ColumnModel dupplicateColF = dupplicateCol;
		if (copyto != null) {
			addressDetailBean.setDecoratorCell(rowModel -> {
				CellModel btDupplicate = rowModel.get(dupplicateColF);
				btDupplicate.setIconSclass("z-icon-fw z-icon-clone z-icon-solid");
			});
		}
		
		Query savedDataQuery = MTable.get(Env.getCtx(), X_ZZPersonAddress.Table_Name)
				.createQuery(String.format("%s = ? AND %s = ?"
						, isSdfAddress ? X_ZZPersonAddress.COLUMNNAME_AD_User_ID : X_ZZPersonAddress.COLUMNNAME_C_BPartner_ID
						, I_ZZPersonAddress.COLUMNNAME_ZZAddressType), null);
		
		savedDataQuery.setParameters(parentId, addressType);
		
		savedDataQuery.setOrderBy(X_ZZPersonAddress.COLUMNNAME_Created + " DESC");
		PO po = savedDataQuery.first();
		if (po == null) {
			addressDetailBean.init(null);
		}else {
			addressDetailBean.init(List.of(po));
		}
		
	
		return addressDetailBean;
	}
}
