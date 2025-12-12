package za.co.ntier.webform.sdr.viewmodel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.Env;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.CheckEvent;

import za.co.ntier.api.model.I_AD_User;
import za.co.ntier.api.model.I_ZZPersonAddress;
import za.co.ntier.api.model.I_ZZSdf;
import za.co.ntier.api.model.X_AD_User;
import za.co.ntier.api.model.X_ZZPersonAddress;
import za.co.ntier.api.model.X_ZZSdf;
import za.co.ntier.api.model.X_ZZ_AlternateIDType;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.form.bean.component.FormInfo;
import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.TableModel.DaoManage;
import za.co.ntier.webform.sdr.component.bean.cell.AreaCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.CheckboxCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.DateCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.ListCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.PostalCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.ProvinceCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.UploadCellModel;
import za.co.ntier.webform.sdr.component.bean.column.ListColumnModel;
import za.co.ntier.webform.sdr.component.tab.bean.NavTab;
import za.co.ntier.webform.sdr.component.tab.bean.NavTabPanel;

public class MainSrdFormVM {
	private MenuContextInfo menuContextInfo;
	private FormInfo formInfo;
	private NavTab mainTab;
	private TableModel names;
	X_ZZSdf sdf;
	X_AD_User person;
	
	private void initSDF() {
		Query savedDataQuery = MTable.get(Env.getCtx(), I_ZZSdf.Table_Name)
					.createQuery(String.format("%s = ?", I_ZZSdf.COLUMNNAME_AD_User_ID), null);
		savedDataQuery.setParameters(person.getAD_User_ID());
		savedDataQuery.setOnlyActiveRecords(true);
		sdf = savedDataQuery.firstOnly();
		if (sdf == null) {
			sdf = new X_ZZSdf(Env.getCtx(), 0, null);
			sdf.setAD_User_ID(person.getAD_User_ID());
			sdf.setZZFirstName(person.getName());
			sdf.saveEx(null);
		}else {
			if(sdf.getZZFirstName() == null) {
				sdf.setZZFirstName(person.getName());
			}
		}
	}
	
	
	@Init
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo){
		this.menuContextInfo = menuContextInfo;
		
		setFormInfo(new FormInfo(menuContextInfo));

		DaoManage personManage = new DaoManage();
		
		int loginUserId = Env.getAD_User_ID(Env.getCtx());
		person = new X_AD_User(Env.getCtx(), loginUserId, null);
		initSDF();
		
		personManage.setDao(person);
		personManage.setDao(sdf);
		
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
		
		physicalAddress = getAddressDetailComp("Physical ", "Physical", "Physical");
		postalAddress = getAddressDetailComp("Postal ", "Postal", "Postal");
		addressDetailTab.getCompModel().add(physicalAddress);
		addressDetailTab.getCompModel().add(getAddressControlComp());
		addressDetailTab.getCompModel().add(postalAddress);

		NavTabPanel educationDetailTab = new NavTabPanel(mainTab);
		educationDetailTab.setTabTitle("Education & Experience");
		// new component
		educationDetailTab.getCompModel().add(getEducationComp(personManage));

		
		//OrglinkTabPanel orgLinkTab = new OrglinkTabPanel(mainTab, sdf);
		//orgLinkTab.setTabTitle("Org Link");
	}

	
	
	TableModel physicalAddress;
	TableModel postalAddress;
	
	private TableModel getNamesComp(DaoManage personManage) {
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel firstNameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZSdf.Table_Name, I_ZZSdf.COLUMNNAME_ZZFirstName)
				, I_ZZSdf.COLUMNNAME_ZZFirstName
				).required()
				.setTableName(I_ZZSdf.Table_Name);
		cols.add(firstNameCol);
		
		ColumnModel midNameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZSdf.Table_Name, I_ZZSdf.COLUMNNAME_ZZMiddleName)
				, I_ZZSdf.COLUMNNAME_ZZMiddleName
				).setTableName(I_ZZSdf.Table_Name);
				;
		cols.add(midNameCol);
		
		ColumnModel surnameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZSdf.Table_Name, I_ZZSdf.COLUMNNAME_ZZSurname)
				, I_ZZSdf.COLUMNNAME_ZZSurname
				).setTableName(I_ZZSdf.Table_Name);
		cols.add(surnameCol);

		
		TableModel namesBean = TableModel.getTableBean(TableModel.class, cols, false);

		namesBean.setDaoManage(personManage);
		namesBean.init(null, null);

		return namesBean;
	}

	private TableModel getEducationComp(DaoManage personManage) {
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel highestEducationCol = 
				ListCellModel.getListColumnModel(
						MasterUtil.getNameOfColTranslated(I_ZZSdf.Table_Name, I_ZZSdf.COLUMNNAME_ZZ_LI_HighestEducation_ID)
						, I_ZZSdf.COLUMNNAME_ZZ_LI_HighestEducation_ID
						, MasterUtil.getHighestEducations()
						, highestEducation -> {return highestEducation.getName();}
						, highestEducation -> {return highestEducation.getZZ_LI_HighestEducation_ID();}
					).setUseForID(true)
					.required()
					.setTableName(I_ZZSdf.Table_Name);
		cols.add(highestEducationCol);

		ColumnModel highestEducationDescriptionCol = 
				CellModel.getColModelForText(
						MasterUtil.getNameOfColTranslated(I_ZZSdf.Table_Name, I_ZZSdf.COLUMNNAME_ZZHighestEducationDesc)
						, I_ZZSdf.COLUMNNAME_ZZHighestEducationDesc
						)
				.setTableName(I_ZZSdf.Table_Name);
		cols.add(highestEducationDescriptionCol);

		ColumnModel nameOfAccreditedTrainingProviderCol = 
				CellModel.getColModelForText(
						MasterUtil.getNameOfColTranslated(I_ZZSdf.Table_Name, I_ZZSdf.COLUMNNAME_ZZAccreditedTrainingProvider)
						, I_ZZSdf.COLUMNNAME_ZZAccreditedTrainingProvider
						)
				.setTableName(I_ZZSdf.Table_Name);
		cols.add(nameOfAccreditedTrainingProviderCol);

		ColumnModel experienceCol = 
				CellModel.getColModelForPositiveNumber(
						MasterUtil.getNameOfColTranslated(I_ZZSdf.Table_Name, I_ZZSdf.COLUMNNAME_ZZExperience)
						, I_ZZSdf.COLUMNNAME_ZZExperience
						).required()
				.setTableName(I_ZZSdf.Table_Name);
		cols.add(experienceCol);

		ColumnModel currentOccupationCol = 
				CellModel.getColModelForText(
						MasterUtil.getNameOfColTranslated(I_ZZSdf.Table_Name, I_ZZSdf.COLUMNNAME_ZZCurrentOccupation)
						, I_ZZSdf.COLUMNNAME_ZZCurrentOccupation
				).required()
				.setTableName(I_ZZSdf.Table_Name);
		cols.add(currentOccupationCol);

		ColumnModel yearInOccupationCol = 
				CellModel.getColModelForPositiveNumber(
						MasterUtil.getNameOfColTranslated(I_ZZSdf.Table_Name, I_ZZSdf.COLUMNNAME_ZZYearsInOccupation)
						, I_ZZSdf.COLUMNNAME_ZZYearsInOccupation
						).required()
				.setTableName(I_ZZSdf.Table_Name);
		cols.add(yearInOccupationCol);

		ColumnModel generalCommentsCol = 
				CellModel.getColModelForText(
						MasterUtil.getNameOfColTranslated(I_ZZSdf.Table_Name, I_ZZSdf.COLUMNNAME_ZZGeneralComments)
						, I_ZZSdf.COLUMNNAME_ZZGeneralComments
						).required()
				.setTableName(I_ZZSdf.Table_Name);
		cols.add(generalCommentsCol);

		TableModel educationBean = TableModel.getTableBean(TableModel.class, cols, false);
		educationBean.setSclass("two-col srd-education");

		educationBean.setDaoManage(personManage);
		
		educationBean.init(null, null);
		
		return educationBean;
	}

	private TableModel getAddressControlComp() {
		List<ColumnModel> cols = new ArrayList<>();
		
		ColumnModel gpsCoordinatesCol = CellModel.getColModelForText("GPS Coordinates", null).setReadonly(true);
		cols.add(gpsCoordinatesCol);
		
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
		
		//dupplicateCol.setShowTitle(false);
		cols.add(dupplicateCol);

		/*
		 * ColumnModel addressCol = CellModel.getColModelForText("Address", null);
		 * cols.add(addressCol);
		 */
		
		TableModel addressDetailCtr = TableModel.getTableBean(TableModel.class, cols, false);
		addressDetailCtr.setSclass("srd-address-ctrl");

		addressDetailCtr.init(null, null);
		return addressDetailCtr;
	}
	
	private TableModel getAddressDetailComp(String prefixName, String addressType, String subHeader) {
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
				prefixName + MasterUtil.getNameOfColTranslated(I_ZZPersonAddress.Table_Name, I_ZZPersonAddress.COLUMNNAME_Postal)
				, I_ZZPersonAddress.COLUMNNAME_Postal
				).required();
		colsAddress.add(physicalCodeCol);

		ColumnModel physicalAreaCol = AreaCellModel.getAreaColumnModel(
				prefixName + MasterUtil.getNameOfColTranslated(I_ZZPersonAddress.Table_Name, I_ZZPersonAddress.COLUMNNAME_C_City_ID)
				, I_ZZPersonAddress.COLUMNNAME_C_City_ID).required();
		colsAddress.add(physicalAreaCol);
		
		ColumnModel physicalProvinceCol = ProvinceCellModel.getProvinceColumnModel(
				prefixName + MasterUtil.getNameOfColTranslated(I_ZZPersonAddress.Table_Name, I_ZZPersonAddress.COLUMNNAME_C_Region_ID)
				, I_ZZPersonAddress.COLUMNNAME_C_Region_ID).required();
		colsAddress.add(physicalProvinceCol);

		TableModel addressDetailBean = TableModel.getTableBean(TableModel.class, colsAddress, false);
		addressDetailBean.setSclass(addressType + " srd-address");
		addressDetailBean.setPoSupplier((ann, appForm) -> {
			X_ZZPersonAddress po = new X_ZZPersonAddress(appForm.getCtx(), 0, null);
			po.setZZAddressType(addressType);
			po.setAD_User_ID(sdf.getAD_User_ID());
			return po;
		});

		addressDetailBean.setSubSectionHeader(subHeader);
		
		Query savedDataQuery = MTable.get(Env.getCtx(), X_ZZPersonAddress.Table_Name)
				.createQuery(String.format("%s = ? AND %s = ?"
						, X_ZZPersonAddress.COLUMNNAME_AD_User_ID, I_ZZPersonAddress.COLUMNNAME_ZZAddressType), null);
		savedDataQuery.setParameters(sdf.getAD_User_ID(), addressType);
		savedDataQuery.setOrderBy(X_ZZPersonAddress.COLUMNNAME_Created + " DESC");
		PO po = savedDataQuery.first();
		if (po == null) {
			addressDetailBean.init(null, null);
		}else {
			addressDetailBean.init(null, List.of(po));
		}
		

		return addressDetailBean;
	}

	private TableModel getContactDetailComp(DaoManage personManage) {
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel telephoneNumberCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_Phone2)
				, I_AD_User.COLUMNNAME_Phone2
				).setTableName(I_AD_User.Table_Name);
		cols.add(telephoneNumberCol);

		ColumnModel cellPhoneNumberCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_Phone)
				, I_AD_User.COLUMNNAME_Phone
				).required()
				.setTableName(I_AD_User.Table_Name);
		cols.add(cellPhoneNumberCol);

		ColumnModel emailCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_EMail)
				, I_AD_User.COLUMNNAME_EMail
				).required()
				.setReadonly(true)
				.setTableName(I_AD_User.Table_Name);
		cols.add(emailCol);

		TableModel contactDetailBean = TableModel.getTableBean(TableModel.class, cols, false);
		contactDetailBean.setSclass("two-col srd-contact");
		contactDetailBean.setDaoManage(personManage);
		contactDetailBean.init(null, null);

		return contactDetailBean;
	}

	private TableModel getPersonDetailComp(DaoManage personManage) {

		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel idDocUploadCol = UploadCellModel.getUploadColumnModel("ID Document Upload", null, null, "UPLOAD FILE");
		cols.add(idDocUploadCol);

		ColumnModel greettingCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZSdf.Table_Name, I_ZZSdf.COLUMNNAME_ZZLkpTitle)
				, I_ZZSdf.COLUMNNAME_ZZLkpTitle
				, MasterUtil.getLkpTitleLists()
				, title -> {return title.getName();}
				, title -> {return title.getValue();}
			).required()
			.setTableName(I_ZZSdf.Table_Name);
		cols.add(greettingCol);

		ColumnModel idNoCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_ZZ_ID_Passport_No)
				, I_AD_User.COLUMNNAME_ZZ_ID_Passport_No
			).required()
			.setTableName(I_AD_User.Table_Name);
		cols.add(idNoCol);

		ColumnModel initialsCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZSdf.Table_Name, I_ZZSdf.COLUMNNAME_ZZInitials)
				, I_ZZSdf.COLUMNNAME_ZZInitials
			).required()
			.setTableName(I_ZZSdf.Table_Name);
		cols.add(initialsCol);

		ColumnModel dateOfBirthCol = DateCellModel.getDateColumnModel(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_Birthday)
				, I_AD_User.COLUMNNAME_Birthday
			).required()
			.setTableName(I_AD_User.Table_Name);
		cols.add(dateOfBirthCol);

		ColumnModel genderCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZSdf.Table_Name, I_ZZSdf.COLUMNNAME_ZZGender)
				, I_ZZSdf.COLUMNNAME_ZZGender
				, MasterUtil.getLkpGenders()
				, title -> {return title.getName();}
				, title -> {return title.getValue();}
			).required()
			.setTableName(I_ZZSdf.Table_Name);
		cols.add(genderCol);

		ColumnModel equityCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZSdf.Table_Name, I_ZZSdf.COLUMNNAME_ZZEquity)
				, I_ZZSdf.COLUMNNAME_ZZEquity
				, MasterUtil.getLkpEquity()
				, title -> {return title.toString();}
				, title -> {return title.getValue();}
			).required()
			.setTableName(I_ZZSdf.Table_Name);
		cols.add(equityCol);

		ColumnModel disabilityCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZSdf.Table_Name, I_ZZSdf.COLUMNNAME_ZZ_LI_Disability_ID)
				, I_ZZSdf.COLUMNNAME_ZZ_LI_Disability_ID
				, MasterUtil.getDisability()
				, title -> {return title.getName();}
				, title -> {return title.getZZ_LI_Disability_ID();}
			).setUseForID(true)
			.required()
			.setTableName(I_ZZSdf.Table_Name);
		cols.add(disabilityCol);

		ColumnModel homeLanguageCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZSdf.Table_Name, I_ZZSdf.COLUMNNAME_ZZ_LI_HomeLanguage_ID)
				, I_ZZSdf.COLUMNNAME_ZZ_LI_HomeLanguage_ID
				, MasterUtil.getHomeLanguage()
				, title -> {return title.getName();}
				, title -> {return title.getZZ_LI_HomeLanguage_ID();}
			).setUseForID(true)
			.required()
			.setTableName(I_ZZSdf.Table_Name);
		cols.add(homeLanguageCol);

		ColumnModel citizenResidentialStatusCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZSdf.Table_Name, I_ZZSdf.COLUMNNAME_ZZ_LI_CitizenResidentialStatus_ID)
				, I_ZZSdf.COLUMNNAME_ZZ_LI_CitizenResidentialStatus_ID
				, MasterUtil.getCitizenResidentialStatus()
				, title -> {return title.getName();}
				, title -> {return title.getZZ_LI_CitizenResidentialStatus_ID();}
			).setUseForID(true)
			.required()
			.setTableName(I_ZZSdf.Table_Name);
		cols.add(citizenResidentialStatusCol);

		
		ListColumnModel<X_ZZ_AlternateIDType> alternateIDTypeCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZSdf.Table_Name, I_ZZSdf.COLUMNNAME_ZZ_AlternateIDType_ID)
				, I_ZZSdf.COLUMNNAME_ZZ_AlternateIDType_ID
				, MasterUtil.getAlternateIDType()
				, title -> {return title.getName();}
				, title -> {return title.getZZ_AlternateIDType_ID();}
			);
		alternateIDTypeCol.setUseForID(true)
			.required()
			.setTableName(I_ZZSdf.Table_Name);
		cols.add(alternateIDTypeCol);

		ColumnModel nationalityCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZSdf.Table_Name, I_ZZSdf.COLUMNNAME_ZZ_Nationality_ID)
				, I_ZZSdf.COLUMNNAME_ZZ_Nationality_ID
				, MasterUtil.getNationality()
				, title -> {return title.getName();}
				, title -> {return title.getZZ_Nationality_ID();}
			).setUseForID(true)
			.required()
			.setTableName(I_ZZSdf.Table_Name);
		cols.add(nationalityCol);

		ColumnModel socioEconomicStatusCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZSdf.Table_Name, I_ZZSdf.COLUMNNAME_ZZ_LI_SocioEconomicStatus_ID)
				, I_ZZSdf.COLUMNNAME_ZZ_LI_SocioEconomicStatus_ID
				, MasterUtil.getSocioEconomicStatus()
				, title -> {return title.getName();}
				, title -> {return title.getZZ_LI_SocioEconomicStatus_ID();}
			).setUseForID(true)
			.required()
			.setTableName(I_ZZSdf.Table_Name);
		cols.add(socioEconomicStatusCol);

		TableModel personDetailBean = TableModel.getTableBean(TableModel.class, cols, false);
		personDetailBean.setSclass("srd-person-detail");
		
		personDetailBean.setDaoManage(personManage);
		
		personDetailBean.init(null, null);

		@SuppressWarnings("unchecked")
		ListCellModel<X_ZZ_AlternateIDType> listCellModel = (ListCellModel<X_ZZ_AlternateIDType>)personDetailBean.getRow().get(alternateIDTypeCol);
		listCellModel.setValue("RSA ID Number", 
					item -> {
						return alternateIDTypeCol.getDisplayConvert().apply(item).equals("RSA ID Number");
					});
		
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
			MasterUtil.showDialog("ZZSDFCreatedSuccess");
		}else {
			MasterUtil.showDialog("ZZSDFSavedSuccess");
		}
		
		//MasterUtil.openForm(MasterUtil.SDRLinkedOrganisationsUU);
	}
}
