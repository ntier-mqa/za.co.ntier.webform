package za.co.ntier.webform.sdr.viewmodel;

import java.util.ArrayList;
import java.util.List;

import org.compiere.model.I_AD_User;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.Env;
import org.compiere.util.ValueNamePair;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.api.model.I_ZZAssessorPerson;
import za.co.ntier.api.model.I_ZZSdf;
import za.co.ntier.api.model.MUser_New;
import za.co.ntier.api.model.X_ZZAssessorPerson;
import za.co.ntier.api.model.X_ZZPersonAddress;
import za.co.ntier.api.model.X_ZZ_AlternateIDType;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.form.bean.component.FormInfo;
import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.ISaveForm;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.TableModel.DaoManage;
import za.co.ntier.webform.sdr.component.bean.cell.DateCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.ListCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.UploadCellModel;
import za.co.ntier.webform.sdr.component.bean.column.ListColumnModel;
import za.co.ntier.webform.sdr.component.tab.bean.NavTab;
import za.co.ntier.webform.sdr.component.tab.bean.NavTabPanel;
import za.co.ntier.webform.sdr.component.util.BuildFormUtil;

public class AssessorRegistrationVM extends BaseAppVM {

	private MenuContextInfo menuContextInfo;
	private FormInfo formInfo;
	
	private TableModel tmNames;
	private NavTab mainTab;
	X_ZZAssessorPerson assessorPerson;
	
	DaoManage daoManage = new DaoManage();
	
	@Override
	public Object getMainApp() {
		return null;
	}

	@Override
	public MenuContextInfo getMenuContextInfo() {
		return menuContextInfo;
	}
	
	@Override
	public List<DaoManage> getDaoManages() {
		return List.of(daoManage);
	}
	
	@Override
	public List<ISaveForm> getSaveComponents() {
		return List.of(mainTab, tmNames);
	}

	@Override
	protected void showResult(boolean isSubmit) {
		if(isNew) {
			MasterUtil.showInfoDialog("ZZAssessorCreatedSuccess", MasterUtil.fCloseActiveWindow);
		}else {
			MasterUtil.showInfoDialog("ZZAssessorSavedSuccess", MasterUtil.fCloseActiveWindow);
		}
	}
	
	/**
	 * @param menuContextInfo the menuContextInfo to set
	 */
	public void setMenuContextInfo(MenuContextInfo menuContextInfo) {
		this.menuContextInfo = menuContextInfo;
	}
	
	/**
	 * @param formInfo the formInfo to set
	 */
	public void setFormInfo(FormInfo formInfo) {
		this.formInfo = formInfo;
	}
	
	/**
	 * @return the formInfo
	 */
	public FormInfo getFormInfo() {
		return formInfo;
	}
	
	private MUser_New person;
	boolean isNew = true;
	
	@Init
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo){
		this.setMenuContextInfo(menuContextInfo);
			
		daoManage.setPoSupplier(I_AD_User.Table_Name, daoManage -> {
			person = new MUser_New(Env.getCtx(), 0, null);
			String name = (String)tmNames.getRow().get(firstNameCol).getValue();
			person.setName(name);
			return person;
		});
		
		daoManage.setPoSupplier(I_ZZAssessorPerson.Table_Name, daoManage -> {
			assessorPerson = new X_ZZAssessorPerson(Env.getCtx(), 0, null);
			return assessorPerson;
		});
		
		setFormInfo(new FormInfo(menuContextInfo));
		setMainTab(new NavTab());
		initForm();
		
		idNoCol.setEventHandle((event, cellMode) -> {
			Object idValue = cellMode.getDirtyValue();
			
			if (idValue != null) {
				Query userQuery = MTable.get(Env.getCtx(), I_AD_User.Table_Name)
						.createQuery(String.format("%s = ?", I_AD_User.COLUMNNAME_ZZ_ID_Passport_No), null);
				userQuery.setParameters(idValue);
				userQuery.setOnlyActiveRecords(true);
				person = userQuery.firstOnly();
				
				X_ZZAssessorPerson assessorPersonSaved = null;
				
				if (person != null) {
					daoManage.setDao(person);
					Query savedDataQuery = MTable.get(Env.getCtx(), I_ZZAssessorPerson.Table_Name)
							.createQuery(String.format("%s = ?", I_ZZAssessorPerson.COLUMNNAME_AD_User_ID), null);
					savedDataQuery.setParameters(person.getAD_User_ID());
					savedDataQuery.setOnlyActiveRecords(true);
					
					assessorPersonSaved = savedDataQuery.firstOnly();
					
					firstNameCol.setDefaultValue(person.getName());
				}else {
					daoManage.resetDao(I_AD_User.Table_Name);
					firstNameCol.setDefaultValue(null);
				}
				
				if (assessorPersonSaved != null) {
					daoManage.setDao(assessorPersonSaved);
				}else {
					daoManage.resetDao(I_ZZAssessorPerson.Table_Name);
				}
				
				isNew = assessorPersonSaved == null;
				assessorPerson = assessorPersonSaved;
				loadData();
			}
		});
	}

	private void loadData() {
		tmNames.reloadDao();
		
		mainTab.getTabPanelModel().forEach(tabModel -> {
			tabModel.getCompModel().forEach(tableModel -> {
				((TableModel)tableModel).reloadDao();
			});
		});
	}
	
	private void initForm() {
		tmNames = initTbName();
		initGeneralDetail();
		initContactDetail();
		initHealthFunction();
		//initAddresss();
	}

	ColumnModel idNoCol;
	
	private void initGeneralDetail() {
		List<ColumnModel> cols = new ArrayList<>();

		idNoCol = CellModel.getColModelForIDPASS(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_ZZ_ID_Passport_No)
				, I_AD_User.COLUMNNAME_ZZ_ID_Passport_No
			).required()
			.setTableName(I_AD_User.Table_Name);
		cols.add(idNoCol);
		
		ListColumnModel<X_ZZ_AlternateIDType> alternateIDTypeCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZAssessorPerson.Table_Name, I_ZZAssessorPerson.COLUMNNAME_ZZ_AlternateIDType_ID)
				, I_ZZAssessorPerson.COLUMNNAME_ZZ_AlternateIDType_ID
				, MasterUtil.getAlternateIDType()
				, title -> {return title.getName();}
				, title -> {return title.getZZ_AlternateIDType_ID();}
			);
		alternateIDTypeCol.setUseForID(true)
			.setDefaultValue("RSA ID Number", item -> {
				return alternateIDTypeCol.getSelectedItemDisplayConvert().apply(item).equals("RSA ID Number");
			})
			.required()
			.setTableName(I_ZZAssessorPerson.Table_Name);
		cols.add(alternateIDTypeCol);

		ColumnModel initialsCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZAssessorPerson.Table_Name, I_ZZAssessorPerson.COLUMNNAME_ZZInitials)
				, I_ZZAssessorPerson.COLUMNNAME_ZZInitials
			).required()
			.setTableName(I_ZZAssessorPerson.Table_Name);
		cols.add(initialsCol);

		ColumnModel dateOfBirthCol = DateCellModel.getDateColumnModel(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_Birthday)
				, I_AD_User.COLUMNNAME_Birthday
			).required()
			.setTableName(I_AD_User.Table_Name);
		cols.add(dateOfBirthCol);

		ColumnModel genderCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZAssessorPerson.Table_Name, I_ZZAssessorPerson.COLUMNNAME_ZZGender)
				, I_ZZAssessorPerson.COLUMNNAME_ZZGender
				, MasterUtil.getLkpGenders()
				, title -> {return title.getName();}
				, title -> {return title.getValue();}
			).setzClass(ValueNamePair.class).required()
			.setTableName(I_ZZAssessorPerson.Table_Name);
		cols.add(genderCol);

		ColumnModel equityCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZAssessorPerson.Table_Name, I_ZZAssessorPerson.COLUMNNAME_ZZEquity)
				, I_ZZAssessorPerson.COLUMNNAME_ZZEquity
				, MasterUtil.getLkpEquity()
				, title -> {return title.toString();}
				, title -> {return title.getValue();}
			).required()
			.setTableName(I_ZZAssessorPerson.Table_Name);
		cols.add(equityCol);

		ColumnModel homeLanguageCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZAssessorPerson.Table_Name, I_ZZAssessorPerson.COLUMNNAME_ZZ_LI_HomeLanguage_ID)
				, I_ZZAssessorPerson.COLUMNNAME_ZZ_LI_HomeLanguage_ID
				, MasterUtil.getHomeLanguage()
				, title -> {return title.getName();}
				, title -> {return title.getZZ_LI_HomeLanguage_ID();}
			).setUseForID(true)
			.required()
			.setTableName(I_ZZAssessorPerson.Table_Name);
		cols.add(homeLanguageCol);
		
		ColumnModel nationalityCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZAssessorPerson.Table_Name, I_ZZAssessorPerson.COLUMNNAME_ZZ_Nationality_ID)
				, I_ZZAssessorPerson.COLUMNNAME_ZZ_Nationality_ID
				, MasterUtil.getNationality()
				, title -> {return title.getName();}
				, title -> {return title.getZZ_Nationality_ID();}
			).setUseForID(true)
			.required()
			.setTableName(I_ZZAssessorPerson.Table_Name);
		cols.add(nationalityCol);

		ColumnModel citizenResidentialStatusCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZAssessorPerson.Table_Name, I_ZZAssessorPerson.COLUMNNAME_ZZ_LI_CitizenResidentialStatus_ID)
				, I_ZZAssessorPerson.COLUMNNAME_ZZ_LI_CitizenResidentialStatus_ID
				, MasterUtil.getCitizenResidentialStatus()
				, title -> {return title.getName();}
				, title -> {return title.getZZ_LI_CitizenResidentialStatus_ID();}
			).setUseForID(true)
			.required()
			.setTableName(I_ZZAssessorPerson.Table_Name);
		cols.add(citizenResidentialStatusCol);

		ColumnModel socioEconomicStatusCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZAssessorPerson.Table_Name, I_ZZAssessorPerson.COLUMNNAME_ZZ_LI_SocioEconomicStatus_ID)
				, I_ZZAssessorPerson.COLUMNNAME_ZZ_LI_SocioEconomicStatus_ID
				, MasterUtil.getSocioEconomicStatus()
				, title -> {return title.getName();}
				, title -> {return title.getZZ_LI_SocioEconomicStatus_ID();}
			).setUseForID(true)
			.required()
			.setTableName(I_ZZAssessorPerson.Table_Name);
		cols.add(socioEconomicStatusCol);

		TableModel tmGeneralDetail = TableModel.getTableBean(TableModel.class, cols, false);
		tmGeneralDetail.setSclass("srd-general srd-general-assessor");
		
		tmGeneralDetail.setDaoManage(daoManage);
		
		tmGeneralDetail.init();
		
		NavTabPanel tabPanelGeneralDetail = new NavTabPanel(mainTab);
		tabPanelGeneralDetail.setTabTitle("General Details");
		// new component
		tabPanelGeneralDetail.getCompModel().add(tmGeneralDetail);
		
	}

	ColumnModel firstNameCol;
	
	private TableModel initTbName() {
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel greettingCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZAssessorPerson.Table_Name, I_ZZAssessorPerson.COLUMNNAME_ZZLkpTitle)
				, I_ZZAssessorPerson.COLUMNNAME_ZZLkpTitle
				, MasterUtil.getLkpTitleLists()
				, title -> {return title.getName();}
				, title -> {return title.getValue();}
			).setzClass(ValueNamePair.class).required()
			.setTableName(I_ZZAssessorPerson.Table_Name);
		cols.add(greettingCol);
		
		firstNameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZAssessorPerson.Table_Name, I_ZZAssessorPerson.COLUMNNAME_ZZFirstName)
				, I_ZZAssessorPerson.COLUMNNAME_ZZFirstName
				).required()
				.setTableName(I_ZZAssessorPerson.Table_Name)
				;
			
		cols.add(firstNameCol);
		
		ColumnModel midNameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZAssessorPerson.Table_Name, I_ZZAssessorPerson.COLUMNNAME_ZZMiddleName)
				, I_ZZAssessorPerson.COLUMNNAME_ZZMiddleName
				).setTableName(I_ZZAssessorPerson.Table_Name);
				;
		cols.add(midNameCol);
		
		ColumnModel surnameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZAssessorPerson.Table_Name, I_ZZAssessorPerson.COLUMNNAME_ZZSurname)
				, I_ZZAssessorPerson.COLUMNNAME_ZZSurname
				).setTableName(I_ZZAssessorPerson.Table_Name);
		cols.add(surnameCol);
		
		TableModel tmNames = TableModel.getTableBean(TableModel.class, cols, false);
		tmNames.setSclass("srd-name srd-name-assessor");
		tmNames.setDaoManage(daoManage);
		tmNames.init();
		
		return tmNames;
	}

	private void initContactDetail() {
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel cellPhoneNumberCol = CellModel.getColModelForPhone(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_Phone)
				, I_AD_User.COLUMNNAME_Phone
				).required()
				.setTableName(I_AD_User.Table_Name);
		cols.add(cellPhoneNumberCol);
		
		ColumnModel telephoneNumberCol = CellModel.getColModelForPhone(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_Phone2)
				, I_AD_User.COLUMNNAME_Phone2
				).setTableName(I_AD_User.Table_Name);
		cols.add(telephoneNumberCol);

		ColumnModel emailCol = CellModel.getColModelForEmail(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_EMail)
				, I_AD_User.COLUMNNAME_EMail
				).required()
				.setTableName(I_AD_User.Table_Name);
		cols.add(emailCol);

		TableModel tmContactDetail = TableModel.getTableBean(TableModel.class, cols, false);
		tmContactDetail.setSclass("srd-contact srd-contact-assessor");
		tmContactDetail.setDaoManage(daoManage);
		tmContactDetail.init();
		
		NavTabPanel contactDetailTab = new NavTabPanel(mainTab);
		contactDetailTab.setTabTitle("Contact Details");
		contactDetailTab.getCompModel().add(tmContactDetail);

	}
	
	private void initHealthFunction () {
		List<ColumnModel> cols = new ArrayList<>();
		
		ColumnModel seeingCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZAssessorPerson.Table_Name, I_ZZAssessorPerson.COLUMNNAME_ZZHealthSeeing)
				, I_ZZAssessorPerson.COLUMNNAME_ZZHealthSeeing
				, MasterUtil.getHealthFunctions()
				, title -> {return title.getName();}
				, title -> {return title.getValue();}
			).setzClass(ValueNamePair.class).required()
			.setTableName(I_ZZAssessorPerson.Table_Name);
		cols.add(seeingCol);
		
		ColumnModel hearingCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZAssessorPerson.Table_Name, I_ZZAssessorPerson.COLUMNNAME_ZZHealthHearing)
				, I_ZZAssessorPerson.COLUMNNAME_ZZHealthHearing
				, MasterUtil.getHealthFunctions()
				, title -> {return title.getName();}
				, title -> {return title.getValue();}
			).setzClass(ValueNamePair.class).required()
			.setTableName(I_ZZAssessorPerson.Table_Name);
		cols.add(hearingCol);
		
		ColumnModel communicatingCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZAssessorPerson.Table_Name, I_ZZAssessorPerson.COLUMNNAME_ZZHealthCommunicating)
				, I_ZZAssessorPerson.COLUMNNAME_ZZHealthCommunicating
				, MasterUtil.getHealthFunctions()
				, title -> {return title.getName();}
				, title -> {return title.getValue();}
			).setzClass(ValueNamePair.class).required()
			.setTableName(I_ZZAssessorPerson.Table_Name);
		cols.add(communicatingCol);
		
		ColumnModel walkingCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZAssessorPerson.Table_Name, I_ZZAssessorPerson.COLUMNNAME_ZZHealthWalking)
				, I_ZZAssessorPerson.COLUMNNAME_ZZHealthWalking
				, MasterUtil.getHealthFunctions()
				, title -> {return title.getName();}
				, title -> {return title.getValue();}
			).setzClass(ValueNamePair.class).required()
			.setTableName(I_ZZAssessorPerson.Table_Name);
		cols.add(walkingCol);
		
		ColumnModel rememberingCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZAssessorPerson.Table_Name, I_ZZAssessorPerson.COLUMNNAME_ZZHealthRemembering)
				, I_ZZAssessorPerson.COLUMNNAME_ZZHealthRemembering
				, MasterUtil.getHealthFunctions()
				, title -> {return title.getName();}
				, title -> {return title.getValue();}
			).setzClass(ValueNamePair.class).required()
			.setTableName(I_ZZAssessorPerson.Table_Name);
		cols.add(rememberingCol);
		
		ColumnModel selfcareCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZAssessorPerson.Table_Name, I_ZZAssessorPerson.COLUMNNAME_ZZHealthSelfcare)
				, I_ZZAssessorPerson.COLUMNNAME_ZZHealthSelfcare
				, MasterUtil.getHealthFunctions()
				, title -> {return title.getName();}
				, title -> {return title.getValue();}
			).setzClass(ValueNamePair.class).required()
			.setTableName(I_ZZAssessorPerson.Table_Name);
		cols.add(selfcareCol);
		
		TableModel tmHealthFunctions = TableModel.getTableBean(TableModel.class, cols, false);
		tmHealthFunctions.setSclass("srd-health-function srd-health-function-assessor");
		tmHealthFunctions.setDaoManage(daoManage);
		tmHealthFunctions.init();
		
		NavTabPanel tabPanelHealthFunctions = new NavTabPanel(mainTab);
		tabPanelHealthFunctions.setTabTitle("Health Functions Values");
		tabPanelHealthFunctions.getCompModel().add(tmHealthFunctions);
	}
	
	/*
	 * private void initAddresss() { TableModel tmPostalAddress =
	 * BuildFormUtil.getAddressDetailComp("Postal ", "Postal", "Postal", null);
	 * TableModel tmPhysicalAddress =
	 * BuildFormUtil.getAddressDetailComp("Physical ", "Physical", "Physical",
	 * tmPostalAddress);
	 * 
	 * tmPostalAddress.setPoSupplier(rowModel -> { X_ZZPersonAddress address =
	 * BuildFormUtil.getNewAddress(rowModel.getTableModel().getDataType());
	 * //address.setZZAssessorPerson_ID(assessorPerson.getZZAssessorPerson_ID());
	 * return address;
	 * 
	 * });
	 * 
	 * tmPhysicalAddress.setPoSupplier(rowModel -> { X_ZZPersonAddress address =
	 * BuildFormUtil.getNewAddress(rowModel.getTableModel().getDataType());
	 * //address.setZZAssessorPerson_ID(assessorPerson.getZZAssessorPerson_ID());
	 * return address; });
	 * 
	 * NavTabPanel addressDetailTab = new NavTabPanel(mainTab);
	 * addressDetailTab.setSclass("sdr-address sdr-address-assessor");
	 * addressDetailTab.setTabTitle("Address Details");
	 * 
	 * addressDetailTab.getCompModel().add(tmPhysicalAddress);
	 * //addressDetailTab.getCompModel().add(BuildFormUtil.getAddressControlComp(
	 * physicalAddress, postalAddress));
	 * addressDetailTab.getCompModel().add(tmPostalAddress); }
	 */
	
	public TableModel getTmNames() {
		return tmNames;
	}

	public NavTab getMainTab() {
		return mainTab;
	}

	public void setMainTab(NavTab mainTab) {
		this.mainTab = mainTab;
	}
	
	@Override
	public void doSave(String trxName) {
		super.doSave(trxName);
		assessorPerson.setAD_User_ID(person.getAD_User_ID());
		assessorPerson.save(trxName);
	}
}
