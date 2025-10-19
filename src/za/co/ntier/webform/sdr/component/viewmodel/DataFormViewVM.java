package za.co.ntier.webform.sdr.component.viewmodel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.SelectEvent;

import za.co.ntier.api.model.I_ZZ_FormContact;
import za.co.ntier.api.model.X_ZZ_Application_Form;
import za.co.ntier.api.model.X_ZZ_FormContact;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.bean.AddressType;
import za.co.ntier.webform.form.viewmodel.component.ComponentVMWrapper;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;

public class DataFormViewVM extends ComponentVMWrapper<TableModel>{
	
	private AddressType addressType;
	private X_ZZ_Application_Form applicationForm;
	
	@Init(superclass = true)
	public void init(
			@ExecutionArgParam("addressType") AddressType addressType) {
		this.addressType = addressType;
		if (this.addressType != null)
			initAddress();
	}
	
	@Command
	public void cmdValueChanged(
			@BindingParam("annexure") TableModel annexure
			, @BindingParam("row") Map<ColumnModel, Object> row
			, @BindingParam("col") ColumnModel col
			, @ContextParam(ContextType.TRIGGER_EVENT) InputEvent event) throws IOException {
		annexure.cmdValueChanged(row, col, event);
		//notifyProgramComplete(); 
	}
	
	@Command
	public void cmdSelected(
			@BindingParam("annexure") TableModel annexure
			, @BindingParam("row") Map<ColumnModel, Object> row
			, @BindingParam("col") ColumnModel col
			, @ContextParam(ContextType.TRIGGER_EVENT) SelectEvent<?, ?> event) throws IOException {
		annexure.cmdSelected(row, col, event);
	}
	
	public boolean showSiteName() {
		return (getProgramType() != null && getProgramType().isShowAddressSiteField()) || addressType == AddressType.VACATION;
	}
	
	public boolean showLineAddress() {
		return addressType == AddressType.MAIN || addressType == AddressType.MAIN_ALTER
				|| addressType == AddressType.PHYSICAL || addressType == AddressType.VACATION;
	}
	
	public boolean showPostalAddress() {
		return addressType == AddressType.POSTAL;
	}
	
	public boolean showGeographicAddress() {
		return addressType == AddressType.MAIN || addressType == AddressType.MAIN_ALTER
				|| addressType == AddressType.PHYSICAL || addressType == AddressType.POSTAL
				|| addressType == AddressType.VACATION;
	}
	
	public boolean showContact() {
		return addressType == AddressType.MAIN || addressType == AddressType.MAIN_ALTER
				|| addressType == AddressType.ORG || addressType == AddressType.ORG_ALTER
				|| addressType == AddressType.VACATION;
	}
	
	public String getAddressTitle() {
		return addressType.getAddressTitle(getProgramType(), addressType == AddressType.MAIN_ALTER);
	}
	
	private void initAddress() {
		List<ColumnModel> cols = new ArrayList<>();
		
		if(showSiteName()) {
		    ColumnModel siteNameCol = ColumnModel.getColText("Site Name", I_ZZ_FormContact.COLUMNNAME_ZZ_SideName).required();
		    cols.add(siteNameCol);
		}
		
		if(showLineAddress()) {
		    ColumnModel addressCol = ColumnModel.getColText("Street Name and Number", I_ZZ_FormContact.COLUMNNAME_Address).required();
		    cols.add(addressCol);
		}
		
		if (showPostalAddress()) {
			ColumnModel addressCol = ColumnModel.getColText("Post Address", I_ZZ_FormContact.COLUMNNAME_Address).required();
			cols.add(addressCol);
		}
		
		if (showGeographicAddress()) {
			ColumnModel postalCodeCol = ColumnModel.getColPostal(ColumnModel.colPostalCodeLabel, I_ZZ_FormContact.COLUMNNAME_Postal).required();
			cols.add(postalCodeCol);
			
			ColumnModel areaCol = ColumnModel.getColArea(ColumnModel.colAreaLabel, MasterUtil.getInitCities(), I_ZZ_FormContact.COLUMNNAME_C_City_ID).required();
			cols.add(areaCol);
			
			ColumnModel provinceCol = ColumnModel.getColProvince("Province", MasterUtil.getRegions(), I_ZZ_FormContact.COLUMNNAME_C_Region_ID).required();
			cols.add(provinceCol);
		}
		
		if (showContact()) {
			ColumnModel nameSurnameCol = ColumnModel.getColText("Name and Surname", I_ZZ_FormContact.COLUMNNAME_ContactName).required();
			cols.add(nameSurnameCol);
			
			ColumnModel telNumberCol = ColumnModel.getColText("Tel Number", I_ZZ_FormContact.COLUMNNAME_Phone).required();
			cols.add(telNumberCol);
			
			ColumnModel alternativeNumberCol = ColumnModel.getColText("Alternative Number", I_ZZ_FormContact.COLUMNNAME_Phone2).required();
			cols.add(alternativeNumberCol);
			
			ColumnModel emailCol = ColumnModel.getColText("E-mail", I_ZZ_FormContact.COLUMNNAME_EMail).required();
			cols.add(emailCol);
		}
		
		TableModel addressFormBean = TableModel.getTableBean(TableModel.class, cols, false);
		component = addressFormBean;
		addressFormBean.setSectionHeader(getAddressTitle());
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

	}
	
}
