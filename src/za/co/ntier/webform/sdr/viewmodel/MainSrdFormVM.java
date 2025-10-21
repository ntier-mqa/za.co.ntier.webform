package za.co.ntier.webform.sdr.viewmodel;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.api.model.X_ZZ_LI_HighestEducation;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.form.bean.AddressType;
import za.co.ntier.webform.form.bean.ProgramType;
import za.co.ntier.webform.form.bean.component.FormInfo;
import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.cell.ListCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.CheckboxCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.DateCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.UploadCellModel;
import za.co.ntier.webform.sdr.component.bean.column.ListColumnModel;
import za.co.ntier.webform.sdr.component.tab.bean.NavTab;
import za.co.ntier.webform.sdr.component.tab.bean.NavTabPanel;
import za.co.ntier.webform.sdr.component.util.AddressUtil;

public class MainSrdFormVM {
	private MenuContextInfo menuContextInfo;
	private FormInfo formInfo;
	private NavTab mainTab;
	private TableModel names;

	@Init
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo){
		this.menuContextInfo = menuContextInfo;
		setFormInfo(new FormInfo(menuContextInfo));

		names = getNamesComp();

		mainTab = new NavTab();

		// new pannel
		NavTabPanel personDetailTab = new NavTabPanel(mainTab);
		personDetailTab.setTabTitle("Person Details");
		// new component
		personDetailTab.getCompModel().add(getPersonDetailComp());

		// new pannel
		NavTabPanel contactDetailTab = new NavTabPanel(mainTab);
		contactDetailTab.setTabTitle("Contact Details");
		// new component
		contactDetailTab.getCompModel().add(getContactDetailComp());

		//=>TEST ONLY
		contactDetailTab.getCompModel()
		.add(AddressUtil.initAddress(ProgramType.ARTISAN_DEV, AddressType.MAIN, null));

		NavTabPanel addressDetailTab = new NavTabPanel(mainTab);
		addressDetailTab.setTabTitle("Address Details");
		// new component
		addressDetailTab.getCompModel().add(getAddressDetailComp());

		NavTabPanel educationDetailTab = new NavTabPanel(mainTab);
		educationDetailTab.setTabTitle("Education & Experience");
		// new component
		educationDetailTab.getCompModel().add(getEducationComp());

	}

	private TableModel getNamesComp() {
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel firstNameCol = CellModel.getColModelForText("First Name", null).required();
		cols.add(firstNameCol);

		ColumnModel middleNameCol = CellModel.getColModelForText("Middle Name", null);
		cols.add(middleNameCol);

		ColumnModel surNameCol = CellModel.getColModelForText("Surname", null).required();
		cols.add(surNameCol);

		TableModel namesBean = TableModel.getTableBean(TableModel.class, cols, false);

		namesBean.setPoSupplier((ann, appForm) -> {
			//X_ZZ_FormContact po = new X_ZZ_FormContact(appForm.getCtx(), 0, null);
			//po.setZZ_Application_Form_ID(appForm.getZZ_Application_Form_ID());
			//po.setZZ_ContactType(ann.getDataType());
			return null;
		});

		namesBean.init(null, null);

		return namesBean;
	}

	private TableModel getEducationComp() {
		List<ColumnModel> cols = new ArrayList<>();

		ListColumnModel<X_ZZ_LI_HighestEducation> highestEducationCol = 
				ListCellModel.getListColumnModel("Highest Education"
						, (String)null
						, MasterUtil.getHighestEducations()
						, highestEducation -> {return highestEducation.getName();}
					);
		highestEducationCol.required();
		cols.add(highestEducationCol);

		ColumnModel highestEducationDescriptionCol = CellModel.getColModelForText("Highest Education Description", null);
		cols.add(highestEducationDescriptionCol);

		ColumnModel nameOfAccreditedTrainingProviderCol = CellModel.getColModelForText("Name Of Accredited Training Provider", null);
		cols.add(nameOfAccreditedTrainingProviderCol);

		ColumnModel experienceCol = CellModel.getColModelForText("Experience", null).required();
		cols.add(experienceCol);

		ColumnModel currentOccupationCol = CellModel.getColModelForText("Current Occupation", null).required();
		cols.add(currentOccupationCol);

		ColumnModel yearInOccupationCol = CellModel.getColModelForText("Year In Occupation", null).required();
		cols.add(yearInOccupationCol);

		ColumnModel generalCommentsCol = CellModel.getColModelForText("General Comments", null).required();
		cols.add(generalCommentsCol);

		TableModel educationBean = TableModel.getTableBean(TableModel.class, cols, false);
		educationBean.setSclass("two-col srd-education");
		educationBean.setPoSupplier((ann, appForm) -> {
			//X_ZZ_FormContact po = new X_ZZ_FormContact(appForm.getCtx(), 0, null);
			//po.setZZ_Application_Form_ID(appForm.getZZ_Application_Form_ID());
			//po.setZZ_ContactType(ann.getDataType());
			return null;
		});

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

	private TableModel getContactDetailComp() {
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel telephoneNumberCol = CellModel.getColModelForText("Telephone Number", null);
		cols.add(telephoneNumberCol);

		ColumnModel cellPhoneNumberCol = CellModel.getColModelForText("Cell Phone Number", null).required();
		cols.add(cellPhoneNumberCol);

		ColumnModel faxNumberCol = CellModel.getColModelForText("Fax Number", null);
		cols.add(faxNumberCol);

		ColumnModel emailCol = CellModel.getColModelForText("E Mail", null).required();
		cols.add(emailCol);

		TableModel contactDetailBean = TableModel.getTableBean(TableModel.class, cols, false);
		contactDetailBean.setSclass("two-col srd-contact");
		contactDetailBean.setPoSupplier((ann, appForm) -> {
			//X_ZZ_FormContact po = new X_ZZ_FormContact(appForm.getCtx(), 0, null);
			//po.setZZ_Application_Form_ID(appForm.getZZ_Application_Form_ID());
			//po.setZZ_ContactType(ann.getDataType());
			return null;
		});

		contactDetailBean.init(null, null);

		return contactDetailBean;
	}

	private TableModel getPersonDetailComp() {

		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel idDocUploadCol = UploadCellModel.getUploadColumnModel("ID Document Upload", null, null, "UPLOAD FILE");
		cols.add(idDocUploadCol);

		ColumnModel greettingCol = ListCellModel.getListColumnModel("Title"
				, (String)null
				, MasterUtil.getLkpTitleLists()
				, title -> {return title.getName();}
			).required();
		cols.add(greettingCol);

		ColumnModel idNoCol = CellModel.getColModelForText("ID No", null).required();
		cols.add(idNoCol);

		ColumnModel initialsCol = CellModel.getColModelForText("Initials", null).required();
		cols.add(initialsCol);

		ColumnModel dateOfBirthCol = DateCellModel.getDateColumnModel("Date of Birth", null).required();
		cols.add(dateOfBirthCol);

		ColumnModel genderCol = ListCellModel.getListColumnModel("Gender"
				, (String)null
				, MasterUtil.getLkpGenders()
				, title -> {return title.getName();}
			).required();
		cols.add(genderCol);

		ColumnModel equityCol = ListCellModel.getListColumnModel("Equity"
				, (String)null
				, MasterUtil.getLkpEquity()
				, title -> {return title.toString();}
			).required();
		cols.add(equityCol);

		ColumnModel disabilityCol = ListCellModel.getListColumnModel("Disability"
				, (String)null
				, MasterUtil.getDisability()
				, title -> {return title.getName();}
			).required();
		cols.add(disabilityCol);

		ColumnModel homeLanguageCol = ListCellModel.getListColumnModel("Home Language"
				, (String)null
				, MasterUtil.getHomeLanguage()
				, title -> {return title.getName();}
			).required();
		cols.add(homeLanguageCol);

		ColumnModel citizenResidentialStatusCol = ListCellModel.getListColumnModel("Citizen Residential Status"
				, (String)null
				, MasterUtil.getCitizenResidentialStatus()
				, title -> {return title.getName();}
			).required();
		cols.add(citizenResidentialStatusCol);

		ColumnModel alternateIDTypeCol = ListCellModel.getListColumnModel("Alternate ID Type"
				, (String)null
				, MasterUtil.getLkpAltID()
				, title -> {return title.toString();}
			).required();
		cols.add(alternateIDTypeCol);

		ColumnModel nationalityCol = ListCellModel.getListColumnModel("Nationality"
				, (String)null
				, MasterUtil.getNationality()
				, title -> {return title.getDescription();}
			).required();
		cols.add(nationalityCol);

		ColumnModel socioEconomicStatusCol = ListCellModel.getListColumnModel("Socio Economic Status"
				, (String)null
				, MasterUtil.getSocioEconomicStatus()
				, title -> {return title.getName();}
			).required();
		cols.add(socioEconomicStatusCol);

		TableModel personDetailBean = TableModel.getTableBean(TableModel.class, cols, false);
		personDetailBean.setSclass("srd-person-detail");
		personDetailBean.setPoSupplier((ann, appForm) -> {
			//X_ZZ_FormContact po = new X_ZZ_FormContact(appForm.getCtx(), 0, null);
			//po.setZZ_Application_Form_ID(appForm.getZZ_Application_Form_ID());
			//po.setZZ_ContactType(ann.getDataType());
			return null;
		});

		personDetailBean.init(null, null);

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


}
