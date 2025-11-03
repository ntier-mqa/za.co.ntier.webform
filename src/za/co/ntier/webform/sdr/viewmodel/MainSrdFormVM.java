package za.co.ntier.webform.sdr.viewmodel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.compiere.model.MUser;
import org.compiere.util.Env;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.CheckEvent;

import za.co.ntier.api.model.I_AD_User;
import za.co.ntier.api.model.I_ZZPersonAddress;
import za.co.ntier.api.model.X_AD_User;
import za.co.ntier.api.model.X_ZZPersonAddress;
import za.co.ntier.api.model.X_ZZSdf;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.form.bean.component.FormInfo;
import za.co.ntier.webform.form.viewmodel.component.ComponentVMWrapper;
import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.Dialog;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.TableModel.DaoManage;
import za.co.ntier.webform.sdr.component.bean.cell.CheckboxCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.DateCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.ListCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.UploadCellModel;
import za.co.ntier.webform.sdr.component.tab.bean.NavTab;
import za.co.ntier.webform.sdr.component.tab.bean.NavTabPanel;

public class MainSrdFormVM {
	private MenuContextInfo menuContextInfo;
	private FormInfo formInfo;
	private NavTab mainTab;
	private TableModel names;
	X_ZZSdf sdf;
	X_AD_User person;
	
	@Init
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo){
		this.menuContextInfo = menuContextInfo;
		//this.menuContextInfo.setApplicationFormUU("7adc5a38-163d-4c78-88f4-0236fe4ed8b2"); 
		
		if (StringUtils.isNotBlank(menuContextInfo.getApplicationFormUU())) {
			sdf = new X_ZZSdf(Env.getCtx(), menuContextInfo.getApplicationFormUU(), null);
			if (!sdf.isActive()) {
				//showDialog("Deleted Application Form", "This application form is deleted");
			}
			
		}
		
		setFormInfo(new FormInfo(menuContextInfo));

		DaoManage personManage = new DaoManage();
		
		int loginUserId = Env.getAD_User_ID(Env.getCtx());
		MUser loginUser = MUser.get(loginUserId);
		
		person = new X_AD_User(Env.getCtx(), loginUserId, null);
		if (StringUtils.isBlank(person.getZZFirstName())) {
			person.setZZFirstName(loginUser.getName());
		}
		
		personManage.setDao(person);
		
		names = getNamesComp(personManage);
		mainTab = new NavTab();

		// new pannel
		NavTabPanel personDetailTab = new NavTabPanel(mainTab);
		personDetailTab.setTabTitle("Person Details");
		// new component
		personDetailTab.getCompModel().add(getPersonDetailComp(personManage));

		// new pannel
		NavTabPanel contactDetailTab = new NavTabPanel(mainTab);
		contactDetailTab.setTabTitle("Contact Details");
		// new component
		contactDetailTab.getCompModel().add(getContactDetailComp(personManage));

		NavTabPanel addressDetailTab = new NavTabPanel(mainTab);
		addressDetailTab.setSclass("address");
		addressDetailTab.setTabTitle("Address Details");
		// new component
		physicalAddress = getAddressDetailComp(true);
		postalAddress = getAddressDetailComp(false);
		addressDetailTab.getCompModel().add(getAddressControlComp());
		addressDetailTab.getCompModel().add(physicalAddress);
		addressDetailTab.getCompModel().add(postalAddress);

		NavTabPanel educationDetailTab = new NavTabPanel(mainTab);
		educationDetailTab.setTabTitle("Education & Experience");
		// new component
		educationDetailTab.getCompModel().add(getEducationComp(personManage));

	}

	TableModel physicalAddress;
	TableModel postalAddress;
	
	private TableModel getNamesComp(DaoManage personManage) {
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel firstNameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_ZZFirstName)
				, I_AD_User.COLUMNNAME_ZZFirstName
				).required();
		cols.add(firstNameCol);
		
		ColumnModel midNameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_ZZMiddleName)
				, I_AD_User.COLUMNNAME_ZZMiddleName
				);
		cols.add(midNameCol);
		
		ColumnModel surnameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_ZZSurname)
				, I_AD_User.COLUMNNAME_ZZSurname
				);
		cols.add(surnameCol);

		
		TableModel namesBean = TableModel.getTableBean(TableModel.class, cols, false);

		namesBean.setDaoManage(personManage);
		namesBean.init(sdf, null);

		return namesBean;
	}

	private TableModel getEducationComp(DaoManage personManage) {
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel highestEducationCol = 
				ListCellModel.getListColumnModel(
						MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_ZZ_LI_HighestEducation_ID)
						, I_AD_User.COLUMNNAME_ZZ_LI_HighestEducation_ID
						, MasterUtil.getHighestEducations()
						, highestEducation -> {return highestEducation.getName();}
						, highestEducation -> {return highestEducation.getZZ_LI_HighestEducation_ID();}
					).setUseForID(true)
					.required();
		cols.add(highestEducationCol);

		ColumnModel highestEducationDescriptionCol = 
				CellModel.getColModelForText(
						MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_ZZHighestEducationDesc)
						, I_AD_User.COLUMNNAME_ZZHighestEducationDesc
						);
		cols.add(highestEducationDescriptionCol);

		ColumnModel nameOfAccreditedTrainingProviderCol = 
				CellModel.getColModelForText(
						MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_ZZAccreditedTrainingProvider)
						, I_AD_User.COLUMNNAME_ZZAccreditedTrainingProvider
						);
		cols.add(nameOfAccreditedTrainingProviderCol);

		ColumnModel experienceCol = 
				CellModel.getColModelForText(
						MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_ZZExperience)
						, I_AD_User.COLUMNNAME_ZZExperience
						).required();
		cols.add(experienceCol);

		ColumnModel currentOccupationCol = 
				CellModel.getColModelForText(
						MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_ZZCurrentOccupation)
						, I_AD_User.COLUMNNAME_ZZCurrentOccupation
				).required();
		cols.add(currentOccupationCol);

		ColumnModel yearInOccupationCol = 
				CellModel.getColModelForPositiveNumber(
						MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_ZZYearsInOccupation)
						, I_AD_User.COLUMNNAME_ZZYearsInOccupation
						).required();
		cols.add(yearInOccupationCol);

		ColumnModel generalCommentsCol = 
				CellModel.getColModelForText(
						MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_ZZGeneralComments)
						, I_AD_User.COLUMNNAME_ZZGeneralComments
						).required();
		cols.add(generalCommentsCol);

		TableModel educationBean = TableModel.getTableBean(TableModel.class, cols, false);
		educationBean.setSclass("two-col srd-education");

		educationBean.setDaoManage(personManage);
		
		educationBean.init(null, null);

		return educationBean;
	}

	private TableModel getAddressControlComp() {
		List<ColumnModel> cols = new ArrayList<>();
		
		ColumnModel dupplicateCol = CheckboxCellModel.getCheckboxColModel("Use Physical Address For Postal Address?", null);
		dupplicateCol.setEventHandle((inputEvent, cellModel) -> {
			CheckEvent checkEvent = (CheckEvent)inputEvent;
			if (checkEvent.isChecked()) {
				RowModel physicalRow = physicalAddress.getRow();
				RowModel postalRow = postalAddress.getRow();
				for (ColumnModel physicalColModel : physicalAddress.getColumnInfos()) {
					for (ColumnModel postalColModel : postalAddress.getColumnInfos()) {
						if (physicalColModel.getDaoPropertyName() != null && StringUtils.equals(postalColModel.getDaoPropertyName(), physicalColModel.getDaoPropertyName())) {
							postalRow.get(postalColModel).setValue(physicalRow.get(physicalColModel).getValue());
							break;
						}
					}
				}
			}
		});
		
		dupplicateCol.setShowTitle(false);
		cols.add(dupplicateCol);

		ColumnModel addressCol = CellModel.getColModelForText("Address", null);
		cols.add(addressCol);

		ColumnModel gpsCoordinatesCol = CellModel.getColModelForText("GPS Coordinates", null);
		cols.add(gpsCoordinatesCol);
		
		TableModel addressDetailCtr = TableModel.getTableBean(TableModel.class, cols, false);
		addressDetailCtr.setSclass("two-col srd-address");

		addressDetailCtr.init(null, null);
		return addressDetailCtr;
	}
	
	private TableModel getAddressDetailComp(boolean isPhysicalAddress) {
		List<ColumnModel> colsAddress = new ArrayList<>();

		String prefixName = isPhysicalAddress?"Physical":"Postal"; 
		
		ColumnModel physicalAddress1Col = CellModel.getColModelForText(
				prefixName + MasterUtil.getNameOfColTranslated(I_ZZPersonAddress.Table_Name, I_ZZPersonAddress.COLUMNNAME_Address1)
				, I_ZZPersonAddress.COLUMNNAME_Address1
				).required();
		colsAddress.add(physicalAddress1Col);

		ColumnModel physicalAddress2Col = CellModel.getColModelForText(
				prefixName + MasterUtil.getNameOfColTranslated(I_ZZPersonAddress.Table_Name, I_ZZPersonAddress.COLUMNNAME_Address2)
				, I_ZZPersonAddress.COLUMNNAME_Address2
				).required();
		colsAddress.add(physicalAddress2Col);

		ColumnModel physicalAddress3Col = CellModel.getColModelForText(
				prefixName + MasterUtil.getNameOfColTranslated(I_ZZPersonAddress.Table_Name, I_ZZPersonAddress.COLUMNNAME_Address3)
				, I_ZZPersonAddress.COLUMNNAME_Address3
				).required();
		colsAddress.add(physicalAddress3Col);

		ColumnModel physicalCodeCol = CellModel.getColModelForText(
				prefixName + MasterUtil.getNameOfColTranslated(I_ZZPersonAddress.Table_Name, I_ZZPersonAddress.COLUMNNAME_Postal)
				, I_ZZPersonAddress.COLUMNNAME_Postal
				).required();
		colsAddress.add(physicalCodeCol);

		ColumnModel physicalProvinceCol = CellModel.getColModelForText(
				prefixName + MasterUtil.getNameOfColTranslated(I_ZZPersonAddress.Table_Name, I_ZZPersonAddress.COLUMNNAME_ZZProvince)
				, I_ZZPersonAddress.COLUMNNAME_ZZProvince);
		colsAddress.add(physicalProvinceCol);

		TableModel addressDetailBean = TableModel.getTableBean(TableModel.class, colsAddress, false);
		addressDetailBean.setSclass("one-col srd-address");
		addressDetailBean.setPoSupplier((ann, appForm) -> {
			X_ZZPersonAddress po = new X_ZZPersonAddress(appForm.getCtx(), 0, null);
			po.setZZAddressType(prefixName);
			po.setAD_User_ID(person.getAD_User_ID());
			return po;
		});

		addressDetailBean.init(null, null);

		return addressDetailBean;
	}

	private TableModel getContactDetailComp(DaoManage personManage) {
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel telephoneNumberCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_Phone2)
				, I_AD_User.COLUMNNAME_Phone2
				);
		cols.add(telephoneNumberCol);

		ColumnModel cellPhoneNumberCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_Phone)
				, I_AD_User.COLUMNNAME_Phone
				).required();
		cols.add(cellPhoneNumberCol);

		ColumnModel faxNumberCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_Fax)
				, I_AD_User.COLUMNNAME_Fax);
		cols.add(faxNumberCol);

		ColumnModel emailCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_EMail)
				, I_AD_User.COLUMNNAME_EMail
				).required()
				.setReadonly(true);
		cols.add(emailCol);

		TableModel contactDetailBean = TableModel.getTableBean(TableModel.class, cols, false);
		contactDetailBean.setSclass("two-col srd-contact");
		contactDetailBean.setDaoManage(personManage);
		contactDetailBean.init(sdf, null);

		return contactDetailBean;
	}

	private TableModel getPersonDetailComp(DaoManage personManage) {

		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel idDocUploadCol = UploadCellModel.getUploadColumnModel("ID Document Upload", null, null, "UPLOAD FILE");
		cols.add(idDocUploadCol);

		ColumnModel greettingCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_Title)
				, I_AD_User.COLUMNNAME_Title
				, MasterUtil.getLkpTitleLists()
				, title -> {return title.getName();}
				, title -> {return title.getValue();}
			).required();
		cols.add(greettingCol);

		ColumnModel idNoCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_ZZ_ID_Passport_No)
				, I_AD_User.COLUMNNAME_ZZ_ID_Passport_No
			).required();
		cols.add(idNoCol);

		ColumnModel initialsCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_ZZInitials)
				, I_AD_User.COLUMNNAME_ZZInitials
			).required();
		cols.add(initialsCol);

		ColumnModel dateOfBirthCol = DateCellModel.getDateColumnModel(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_Birthday)
				, I_AD_User.COLUMNNAME_Birthday
			).required();
		cols.add(dateOfBirthCol);

		ColumnModel genderCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_ZZGender)
				, I_AD_User.COLUMNNAME_ZZGender
				, MasterUtil.getLkpGenders()
				, title -> {return title.getName();}
				, title -> {return title.getValue();}
			).required();
		cols.add(genderCol);

		ColumnModel equityCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_ZZEquity)
				, I_AD_User.COLUMNNAME_ZZEquity
				, MasterUtil.getLkpEquity()
				, title -> {return title.toString();}
				, title -> {return title.getValue();}
			).required();
		cols.add(equityCol);

		ColumnModel disabilityCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_ZZ_LI_Disability_ID)
				, I_AD_User.COLUMNNAME_ZZ_LI_Disability_ID
				, MasterUtil.getDisability()
				, title -> {return title.getName();}
				, title -> {return title.getZZ_LI_Disability_ID();}
			).setUseForID(true)
			.required();
		cols.add(disabilityCol);

		ColumnModel homeLanguageCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_ZZ_LI_HomeLanguage_ID)
				, I_AD_User.COLUMNNAME_ZZ_LI_HomeLanguage_ID
				, MasterUtil.getHomeLanguage()
				, title -> {return title.getName();}
				, title -> {return title.getZZ_LI_HomeLanguage_ID();}
			).setUseForID(true)
			.required();
		cols.add(homeLanguageCol);

		ColumnModel citizenResidentialStatusCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_ZZ_LI_CitizenResidentialStatus_ID)
				, I_AD_User.COLUMNNAME_ZZ_LI_CitizenResidentialStatus_ID
				, MasterUtil.getCitizenResidentialStatus()
				, title -> {return title.getName();}
				, title -> {return title.getZZ_LI_CitizenResidentialStatus_ID();}
			).setUseForID(true)
			.required();
		cols.add(citizenResidentialStatusCol);

		
		ColumnModel alternateIDTypeCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_ZZ_AlternateIDType_ID)
				, I_AD_User.COLUMNNAME_ZZ_AlternateIDType_ID
				, MasterUtil.getAlternateIDType()
				, title -> {return title.getName();}
				, title -> {return title.getZZ_AlternateIDType_ID();}
			).setUseForID(true)
			.required();
		cols.add(alternateIDTypeCol);

		ColumnModel nationalityCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_ZZ_Nationality_ID)
				, I_AD_User.COLUMNNAME_ZZ_Nationality_ID
				, MasterUtil.getNationality()
				, title -> {return title.getName();}
				, title -> {return title.getZZ_Nationality_ID();}
			).setUseForID(true)
			.required();
		cols.add(nationalityCol);

		ColumnModel socioEconomicStatusCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_ZZ_LI_SocioEconomicStatus_ID)
				, I_AD_User.COLUMNNAME_ZZ_LI_SocioEconomicStatus_ID
				, MasterUtil.getSocioEconomicStatus()
				, title -> {return title.getName();}
				, title -> {return title.getZZ_LI_SocioEconomicStatus_ID();}
			).setUseForID(true)
			.required();
		cols.add(socioEconomicStatusCol);

		TableModel personDetailBean = TableModel.getTableBean(TableModel.class, cols, false);
		personDetailBean.setSclass("srd-person-detail");
		
		personDetailBean.setDaoManage(personManage);
		
		//Query savedDataQuery = MTable.get(I_AD_User.Table_ID).createQuery(String.format("%s = ?",
			//	I_AD_User.COLUMNNAME_ZZSdf_ID), null);
		//savedDataQuery.setParameters(sdf.getZZSdf_ID());
		//savedDataQuery.setOrderBy(I_ZZ_Application_Form.COLUMNNAME_Created);
		
		personDetailBean.init(sdf, null);

		return personDetailBean;
	}

	/**
	 * @return the menuContextInfo
	 */
	public MenuContextInfo getMenuContextInfo() {
		return menuContextInfo;
	}
	/**
	 * @param menuContextInfo the menuContextInfo to set
	 */
	public void setMenuContextInfo(MenuContextInfo menuContextInfo) {
		this.menuContextInfo = menuContextInfo;
	}
	/**
	 * @return the formInfo
	 */
	public FormInfo getFormInfo() {
		return formInfo;
	}
	/**
	 * @param formInfo the formInfo to set
	 */
	public void setFormInfo(FormInfo formInfo) {
		this.formInfo = formInfo;
	}

	public NavTab getMainTab() {
		return mainTab;
	}

	public void setMainTab(NavTab mainTab) {
		this.mainTab = mainTab;
	}

	/**
	 * @return the names
	 */
	public TableModel getNames() {
		return names;
	}

	/**
	 * @param names the names to set
	 */
	public void setNames(TableModel names) {
		this.names = names;
	}
	
	@Command(value = "saveClose")
	public void saveClose() throws IOException {
		boolean isNewSdf = false;
		if (sdf == null) {
			sdf = new X_ZZSdf(Env.getCtx(), 0, null);
			sdf.setAD_Org_ID(0);
			sdf.setAD_User_ID(person.getAD_User_ID());
			sdf.saveEx(null);
			isNewSdf = true;
			
		}
		
		mainTab.save(sdf, null);
		names.save(sdf, null);
		//sdf.setDateDoc(Timestamp.valueOf(LocalDateTime.now()));
		sdf.saveEx(null);
		
		if(isNewSdf) {
			showDialog("Successfully created the SDF", new StringBuilder("A new SDF is created"));
		}else {
			showDialog("Successfully saved the SDF", new StringBuilder("Data is saved"));
		}
		
		
	}
	
	protected void showDialog(String title, StringBuilder msgs) {
		Dialog dialog = new Dialog(title, msgs.toString());
		Map<String, Object> args = new HashMap<>();
		args.put(ComponentVMWrapper.ComponentKey, dialog);
		String zulPathRelative = WebForm.getBundleResourcePath("sdr/component/zul/dialog.zul");	
		Executions.createComponents(zulPathRelative, null, args);
	}
}
