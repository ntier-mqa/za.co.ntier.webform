package za.co.ntier.webform.sdr.viewmodel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.compiere.model.MTable;
import org.compiere.model.MUser;
import org.compiere.model.Query;
import org.compiere.util.Env;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.api.model.I_AD_User;
import za.co.ntier.api.model.I_ZZPerson;
import za.co.ntier.api.model.I_ZZ_Application_Form;
import za.co.ntier.api.model.X_ZZPerson;
import za.co.ntier.api.model.X_ZZSdf;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.form.bean.AddressType;
import za.co.ntier.webform.form.bean.ProgramType;
import za.co.ntier.webform.form.bean.component.FormInfo;
import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.TableModel.DaoManage;
import za.co.ntier.webform.sdr.component.bean.cell.CheckboxCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.DateCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.ListCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.UploadCellModel;
import za.co.ntier.webform.sdr.component.tab.bean.NavTab;
import za.co.ntier.webform.sdr.component.tab.bean.NavTabPanel;
import za.co.ntier.webform.sdr.component.util.AddressUtil;

public class MainSrdFormVM {
	private MenuContextInfo menuContextInfo;
	private FormInfo formInfo;
	private NavTab mainTab;
	private TableModel names;
	X_ZZSdf sdf;
	MUser loginUser;
	X_ZZPerson person;
	
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
		loginUser = MUser.getCopy(Env.getCtx(), loginUserId, null);
		personManage.setDao(loginUser);
		
		Query personQuery = MTable.get(I_ZZPerson.Table_ID).createQuery(String.format("%s = ?",
				I_ZZPerson.COLUMNNAME_AD_User_ID), null);
		personQuery.setParameters(loginUserId);
		personQuery.setOrderBy(I_ZZ_Application_Form.COLUMNNAME_Created);
		person = personQuery.first();
		if(person == null) {
			person = new X_ZZPerson(Env.getCtx(), 0, null);
			person.setAD_User_ID(loginUserId);
			person.saveEx();
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
		addressDetailTab.getCompModel().add(getAddressDetailComp());

		NavTabPanel educationDetailTab = new NavTabPanel(mainTab);
		educationDetailTab.setTabTitle("Education & Experience");
		// new component
		educationDetailTab.getCompModel().add(getEducationComp(personManage));

	}

	private TableModel getNamesComp(DaoManage personManage) {
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel firstNameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_Name)
				, I_AD_User.COLUMNNAME_Name
				).required()
				.setTableId(I_AD_User.Table_ID);
		cols.add(firstNameCol);

		
		TableModel namesBean = TableModel.getTableBean(TableModel.class, cols, false);

		namesBean.setDaoManage(personManage);
		namesBean.init(sdf, null);

		return namesBean;
	}

	private TableModel getEducationComp(DaoManage personManage) {
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel highestEducationCol = 
				ListCellModel.getListColumnModel(
						MasterUtil.getNameOfColTranslated(I_ZZPerson.Table_Name, I_ZZPerson.COLUMNNAME_ZZ_LI_HighestEducation_ID)
						, I_ZZPerson.COLUMNNAME_ZZ_LI_HighestEducation_ID
						, MasterUtil.getHighestEducations()
						, highestEducation -> {return highestEducation.getName();}
						, highestEducation -> {return highestEducation.getZZ_LI_HighestEducation_ID();}
					).setUseForID(true)
					.setTableId(I_ZZPerson.Table_ID)
					.required();
		cols.add(highestEducationCol);

		ColumnModel highestEducationDescriptionCol = 
				CellModel.getColModelForText(
						MasterUtil.getNameOfColTranslated(I_ZZPerson.Table_Name, I_ZZPerson.COLUMNNAME_ZZHighestEducationDesc)
						, I_ZZPerson.COLUMNNAME_ZZHighestEducationDesc
						).setTableId(I_ZZPerson.Table_ID);
		cols.add(highestEducationDescriptionCol);

		ColumnModel nameOfAccreditedTrainingProviderCol = 
				CellModel.getColModelForText(
						MasterUtil.getNameOfColTranslated(I_ZZPerson.Table_Name, I_ZZPerson.COLUMNNAME_ZZAccreditedTrainingProvider)
						, I_ZZPerson.COLUMNNAME_ZZAccreditedTrainingProvider
						).setTableId(I_ZZPerson.Table_ID);
		cols.add(nameOfAccreditedTrainingProviderCol);

		ColumnModel experienceCol = 
				CellModel.getColModelForText(
						MasterUtil.getNameOfColTranslated(I_ZZPerson.Table_Name, I_ZZPerson.COLUMNNAME_ZZExperience)
						, I_ZZPerson.COLUMNNAME_ZZExperience
						).required()
						.setTableId(I_ZZPerson.Table_ID)
				;
		cols.add(experienceCol);

		ColumnModel currentOccupationCol = 
				CellModel.getColModelForText(
						MasterUtil.getNameOfColTranslated(I_ZZPerson.Table_Name, I_ZZPerson.COLUMNNAME_ZZCurrentOccupation)
						, I_ZZPerson.COLUMNNAME_ZZCurrentOccupation
				).setTableId(I_ZZPerson.Table_ID)
				.required();
		cols.add(currentOccupationCol);

		ColumnModel yearInOccupationCol = 
				CellModel.getColModelForPositiveNumber(
						MasterUtil.getNameOfColTranslated(I_ZZPerson.Table_Name, I_ZZPerson.COLUMNNAME_ZZYearsInOccupation)
						, I_ZZPerson.COLUMNNAME_ZZYearsInOccupation
						).setTableId(I_ZZPerson.Table_ID)
						.required();
		cols.add(yearInOccupationCol);

		ColumnModel generalCommentsCol = 
				CellModel.getColModelForText(
						MasterUtil.getNameOfColTranslated(I_ZZPerson.Table_Name, I_ZZPerson.COLUMNNAME_ZZGeneralComments)
						, I_ZZPerson.COLUMNNAME_ZZGeneralComments
						).setTableId(I_ZZPerson.Table_ID)
						.required();
		cols.add(generalCommentsCol);

		TableModel educationBean = TableModel.getTableBean(TableModel.class, cols, false);
		educationBean.setSclass("two-col srd-education");

		educationBean.setDaoManage(personManage);
		
		educationBean.init(null, null);

		return educationBean;
	}

	private TableModel getAddressDetailComp() {
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel dupplicateCol = CheckboxCellModel.getCheckboxColModel("Use Physical Address For Postal Address?", null);
		dupplicateCol.setShowTitle(false);
		cols.add(dupplicateCol);

		ColumnModel addressCol = CellModel.getColModelForText("Address", null);
		cols.add(addressCol);

		ColumnModel gpsCoordinatesCol = CellModel.getColModelForText("GPS Coordinates", null);
		cols.add(gpsCoordinatesCol);

		ColumnModel physicalAddress1Col = CellModel.getColModelForText("Physical Address 1", null).required();
		cols.add(physicalAddress1Col);

		ColumnModel postalAddressLine1Col = CellModel.getColModelForText("Postal Address Line 1", null).required();
		cols.add(postalAddressLine1Col);

		ColumnModel physicalAddress2Col = CellModel.getColModelForText("Physical Address 2", null).required();
		cols.add(physicalAddress2Col);

		ColumnModel postalAddressLine2Col = CellModel.getColModelForText("Postal Address Line 2", null).required();
		cols.add(postalAddressLine2Col);

		ColumnModel physicalAddress3Col = CellModel.getColModelForText("Physical Address 3", null).required();
		cols.add(physicalAddress3Col);

		ColumnModel postalAddressLine3Col = CellModel.getColModelForText("Postal Address Line 3", null).required();
		cols.add(postalAddressLine3Col);

		ColumnModel physicalCodeCol = CellModel.getColModelForText("Physical Code", null).required();
		cols.add(physicalCodeCol);

		ColumnModel postalCodeCol = CellModel.getColModelForText("Postal Code", null).required();
		cols.add(postalCodeCol);

		ColumnModel physicalProvinceCol = CellModel.getColModelForText("Province", null);
		cols.add(physicalProvinceCol);

		ColumnModel postalProvinceCol = CellModel.getColModelForText("Province", null);
		cols.add(postalProvinceCol);

		ColumnModel physicalMunicipalityCol = CellModel.getColModelForText("Municipality", null);
		cols.add(physicalMunicipalityCol);

		ColumnModel postalMunicipalityCol = CellModel.getColModelForText("Municipality", null);
		cols.add(postalMunicipalityCol);

		ColumnModel physicalUrbanRuralCol = CellModel.getColModelForText("Urban Rural", null);
		cols.add(physicalUrbanRuralCol);

		ColumnModel postalUrbanRuralCol = CellModel.getColModelForText("Urban Rural", null);
		cols.add(postalUrbanRuralCol);


		TableModel addressDetailBean = TableModel.getTableBean(TableModel.class, cols, false);
		addressDetailBean.setSclass("two-col srd-address");
		addressDetailBean.setPoSupplier((ann, appForm) -> {
			//X_ZZ_FormContact po = new X_ZZ_FormContact(appForm.getCtx(), 0, null);
			//po.setZZ_Application_Form_ID(appForm.getZZ_Application_Form_ID());
			//po.setZZ_ContactType(ann.getDataType());
			return null;
		});

		addressDetailBean.init(null, null);

		return addressDetailBean;
	}

	private TableModel getContactDetailComp(DaoManage personManage) {
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel telephoneNumberCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_Phone2)
				, I_AD_User.COLUMNNAME_Phone2
				).setTableId(I_AD_User.Table_ID);
		cols.add(telephoneNumberCol);

		ColumnModel cellPhoneNumberCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_Phone)
				, I_AD_User.COLUMNNAME_Phone
				).required()
				.setTableId(I_AD_User.Table_ID);
		cols.add(cellPhoneNumberCol);

		ColumnModel faxNumberCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_Fax)
				, I_AD_User.COLUMNNAME_Fax
				).setTableId(I_AD_User.Table_ID);
		cols.add(faxNumberCol);

		ColumnModel emailCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_EMail)
				, I_AD_User.COLUMNNAME_EMail
				).required()
				.setTableId(I_AD_User.Table_ID);
		cols.add(emailCol);

		TableModel contactDetailBean = TableModel.getTableBean(TableModel.class, cols, false);
		contactDetailBean.setSclass("two-col srd-contact");
		contactDetailBean.setDaoManage(personManage);
		contactDetailBean.init(sdf, null);

		return contactDetailBean;
	}

	private TableModel getPersonDetailComp(DaoManage personManage) {

		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel idDocUploadCol = UploadCellModel.getUploadColumnModel("ID Document Upload", null, null, "UPLOAD FILE")
				.setTableId(I_AD_User.Table_ID);
		cols.add(idDocUploadCol);

		ColumnModel greettingCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_Title)
				, I_AD_User.COLUMNNAME_Title
				, MasterUtil.getLkpTitleLists()
				, title -> {return title.getName();}
				, title -> {return title.getValue();}
			).required()
			.setTableId(I_AD_User.Table_ID);
		cols.add(greettingCol);

		ColumnModel idNoCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_ZZ_ID_Passport_No)
				, I_AD_User.COLUMNNAME_ZZ_ID_Passport_No
			).required()
			.setTableId(I_AD_User.Table_ID);
		cols.add(idNoCol);

		ColumnModel initialsCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZPerson.Table_Name, I_ZZPerson.COLUMNNAME_ZZInitials)
				, I_ZZPerson.COLUMNNAME_ZZInitials
			).required()
			.setTableId(I_ZZPerson.Table_ID);
		cols.add(initialsCol);

		ColumnModel dateOfBirthCol = DateCellModel.getDateColumnModel(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_Birthday)
				, I_AD_User.COLUMNNAME_Birthday
			).required()
			.setTableId(I_AD_User.Table_ID);
		cols.add(dateOfBirthCol);

		ColumnModel genderCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZPerson.Table_Name, I_ZZPerson.COLUMNNAME_ZZGender)
				, I_ZZPerson.COLUMNNAME_ZZGender
				, MasterUtil.getLkpGenders()
				, title -> {return title.getName();}
				, title -> {return title.getValue();}
			).required()
			.setTableId(I_ZZPerson.Table_ID);
		cols.add(genderCol);

		ColumnModel equityCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZPerson.Table_Name, I_ZZPerson.COLUMNNAME_ZZEquity)
				, I_ZZPerson.COLUMNNAME_ZZEquity
				, MasterUtil.getLkpEquity()
				, title -> {return title.toString();}
				, title -> {return title.getValue();}
			).required()
			.setTableId(I_ZZPerson.Table_ID);
		cols.add(equityCol);

		ColumnModel disabilityCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZPerson.Table_Name, I_ZZPerson.COLUMNNAME_ZZ_LI_Disability_ID)
				, I_ZZPerson.COLUMNNAME_ZZ_LI_Disability_ID
				, MasterUtil.getDisability()
				, title -> {return title.getName();}
				, title -> {return title.getZZ_LI_Disability_ID();}
			).setUseForID(true)
			.setTableId(I_ZZPerson.Table_ID)
			.required();
		cols.add(disabilityCol);

		ColumnModel homeLanguageCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZPerson.Table_Name, I_ZZPerson.COLUMNNAME_ZZ_LI_HomeLanguage_ID)
				, I_ZZPerson.COLUMNNAME_ZZ_LI_HomeLanguage_ID
				, MasterUtil.getHomeLanguage()
				, title -> {return title.getName();}
				, title -> {return title.getZZ_LI_HomeLanguage_ID();}
			).setUseForID(true)
			.setTableId(I_ZZPerson.Table_ID)
			.required();
		cols.add(homeLanguageCol);

		ColumnModel citizenResidentialStatusCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZPerson.Table_Name, I_ZZPerson.COLUMNNAME_ZZ_LI_CitizenResidentialStatus_ID)
				, I_ZZPerson.COLUMNNAME_ZZ_LI_CitizenResidentialStatus_ID
				, MasterUtil.getCitizenResidentialStatus()
				, title -> {return title.getName();}
				, title -> {return title.getZZ_LI_CitizenResidentialStatus_ID();}
			).setUseForID(true)
			.setTableId(I_ZZPerson.Table_ID)
			.required();
		cols.add(citizenResidentialStatusCol);

		
		ColumnModel alternateIDTypeCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZPerson.Table_Name, I_ZZPerson.COLUMNNAME_ZZ_AlternateIDType_ID)
				, I_ZZPerson.COLUMNNAME_ZZ_AlternateIDType_ID
				, MasterUtil.getAlternateIDType()
				, title -> {return title.getName();}
				, title -> {return title.getZZ_AlternateIDType_ID();}
			).setUseForID(true)
			.required()
			.setTableId(I_ZZPerson.Table_ID);
		cols.add(alternateIDTypeCol);

		ColumnModel nationalityCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZPerson.Table_Name, I_ZZPerson.COLUMNNAME_ZZ_Nationality_ID)
				, I_ZZPerson.COLUMNNAME_ZZ_Nationality_ID
				, MasterUtil.getNationality()
				, title -> {return title.getName();}
				, title -> {return title.getZZ_Nationality_ID();}
			).setUseForID(true)
			.setTableId(I_ZZPerson.Table_ID)
			.required();
		cols.add(nationalityCol);

		ColumnModel socioEconomicStatusCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZPerson.Table_Name, I_ZZPerson.COLUMNNAME_ZZ_LI_SocioEconomicStatus_ID)
				, I_ZZPerson.COLUMNNAME_ZZ_LI_SocioEconomicStatus_ID
				, MasterUtil.getSocioEconomicStatus()
				, title -> {return title.getName();}
				, title -> {return title.getZZ_LI_SocioEconomicStatus_ID();}
			).setUseForID(true)
			.setTableId(I_ZZPerson.Table_ID)
			.required();
		cols.add(socioEconomicStatusCol);

		TableModel personDetailBean = TableModel.getTableBean(TableModel.class, cols, false);
		personDetailBean.setSclass("srd-person-detail");
		
		personDetailBean.setDaoManage(personManage);
		
		//Query savedDataQuery = MTable.get(I_ZZPerson.Table_ID).createQuery(String.format("%s = ?",
			//	I_ZZPerson.COLUMNNAME_ZZSdf_ID), null);
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
		if (sdf == null) {
			sdf = new X_ZZSdf(Env.getCtx(), 0, null);
			sdf.setAD_Org_ID(0);
			
			sdf.saveEx(null);
		}
		
		mainTab.save(sdf, null);
		names.save(sdf, null);
		//sdf.setDateDoc(Timestamp.valueOf(LocalDateTime.now()));
		sdf.saveEx(null);
	}
}
