package za.co.ntier.webform.sdr.viewmodel;

import java.util.ArrayList;
import java.util.List;

import org.compiere.model.MTable;
import org.compiere.model.Query;
import org.compiere.util.Env;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.api.model.I_AD_User;
import za.co.ntier.api.model.I_ZZSdf;
import za.co.ntier.api.model.MUser_New;
import za.co.ntier.api.model.X_ZZSdf;
import za.co.ntier.api.model.X_ZZ_AlternateIDType;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.form.bean.component.FormInfo;
import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.TableModel.DaoManage;
import za.co.ntier.webform.sdr.component.bean.cell.DateCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.ListCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.UploadCellModel;
import za.co.ntier.webform.sdr.component.bean.column.ListColumnModel;
import za.co.ntier.webform.sdr.component.tab.bean.NavTab;
import za.co.ntier.webform.sdr.component.tab.bean.NavTabPanel;
import za.co.ntier.webform.sdr.component.util.BuildFormUtil;

public class MainSrdFormVM extends BaseVM {
	private MenuContextInfo menuContextInfo;
	private FormInfo formInfo;
	private NavTab mainTab;
	private TableModel names;
	X_ZZSdf sdf;
	MUser_New person;
	
	private void initSDF() {
		Query savedDataQuery = MTable.get(Env.getCtx(), I_ZZSdf.Table_Name)
					.createQuery(String.format("%s = ?", I_ZZSdf.COLUMNNAME_AD_User_ID), null);
		savedDataQuery.setParameters(person.getAD_User_ID());
		savedDataQuery.setOnlyActiveRecords(true);
		sdf = savedDataQuery.firstOnly();
		if (sdf == null) {
			isNewSdf = true;
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
	
	DaoManage personManage = new DaoManage();
	@Init
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo){
		this.menuContextInfo = menuContextInfo;
		
		setFormInfo(new FormInfo(menuContextInfo));
		
		int loginUserId = Env.getAD_User_ID(Env.getCtx());
		person = new MUser_New(Env.getCtx(), loginUserId, null);
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
		
		TableModel postalAddress = BuildFormUtil.getAddressDetailComp("Postal ", "Postal", "Postal", true, sdf.getAD_User_ID(), null);
		TableModel physicalAddress = BuildFormUtil.getAddressDetailComp("Physical ", "Physical", "Physical", true, sdf.getAD_User_ID(), postalAddress);
		
		addressDetailTab.getCompModel().add(physicalAddress);
		//addressDetailTab.getCompModel().add(BuildFormUtil.getAddressControlComp(physicalAddress, postalAddress));
		addressDetailTab.getCompModel().add(postalAddress);

		NavTabPanel educationDetailTab = new NavTabPanel(mainTab);
		educationDetailTab.setTabTitle("Education & Experience");
		// new component
		educationDetailTab.getCompModel().add(getEducationComp(personManage));

		
		//OrglinkTabPanel orgLinkTab = new OrglinkTabPanel(mainTab, sdf);
		//orgLinkTab.setTabTitle("Org Link");
	}

	
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

	private TableModel getContactDetailComp(DaoManage personManage) {
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel telephoneNumberCol = CellModel.getColModelForPhone(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_Phone2)
				, I_AD_User.COLUMNNAME_Phone2
				).setTableName(I_AD_User.Table_Name);
		cols.add(telephoneNumberCol);

		ColumnModel cellPhoneNumberCol = CellModel.getColModelForPhone(
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

		ColumnModel idDocUploadCol = UploadCellModel.getUploadColumnModel("ID Document Upload", null, null, "UPLOAD FILE")
				.setTableName(I_ZZSdf.Table_Name);
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
	
	private boolean isNewSdf = false;
	
	@Override
	protected void doSave(String trxName) {
		mainTab.save(sdf, trxName);
		names.save(sdf, trxName);
		//sdf.setDateDoc(Timestamp.valueOf(LocalDateTime.now()));
		personManage.saveDao(trxName);
		mainTab.saveAttachment(sdf, trxName);
		names.saveAttachment(sdf, trxName);
	}
	
	@Override
	protected boolean showResult(Exception exc) {
		if (exc != null) {
			showException(exc);
		}else {
			if(isNewSdf) {
				MasterUtil.showDialog("ZZSDFCreatedSuccess", MasterUtil.fCloseActiveWindow);
			}else {
				MasterUtil.showDialog("ZZSDFSavedSuccess", MasterUtil.fCloseActiveWindow);
			}
		}

		return false;
	}

}
