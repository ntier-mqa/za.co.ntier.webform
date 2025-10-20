package za.co.ntier.webform.sdr.viewmodel;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.form.bean.AddressType;
import za.co.ntier.webform.form.bean.ProgramType;
import za.co.ntier.webform.form.bean.component.FormInfo;
import za.co.ntier.webform.sdr.component.bean.BaseCellModel;
import za.co.ntier.webform.sdr.component.bean.BaseColumnModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.cell.CheckboxCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.DateCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.StringListCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.UploadCellModel;
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
				.add(AddressUtil.initAddress(ProgramType.ARTISAN_DEV, AddressType.MAIN, "Just For Test", null));
		
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
		List<BaseColumnModel> cols = new ArrayList<>();
		
		BaseColumnModel firstNameCol = BaseCellModel.getColModelForText("First Name").required();
		cols.add(firstNameCol);
		
		BaseColumnModel middleNameCol = BaseCellModel.getColModelForText("Middle Name");
		cols.add(middleNameCol);
		
		BaseColumnModel surNameCol = BaseCellModel.getColModelForText("Surname").required();
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
		List<BaseColumnModel> cols = new ArrayList<>();
		
		BaseColumnModel highestEducationCol = StringListCellModel.getColumnModel("Highest Education", demoData).required();
		cols.add(highestEducationCol);
		
		BaseColumnModel highestEducationDescriptionCol = BaseCellModel.getColModelForText("Highest Education Description");
		cols.add(highestEducationDescriptionCol);
		
		BaseColumnModel nameOfAccreditedTrainingProviderCol = BaseCellModel.getColModelForText("Name Of Accredited Training Provider");
		cols.add(nameOfAccreditedTrainingProviderCol);
		
		BaseColumnModel experienceCol = BaseCellModel.getColModelForText("Experience").required();
		cols.add(experienceCol);
		
		BaseColumnModel currentOccupationCol = BaseCellModel.getColModelForText("Current Occupation").required();
		cols.add(currentOccupationCol);
		
		BaseColumnModel yearInOccupationCol = BaseCellModel.getColModelForText("Year In Occupation").required();
		cols.add(yearInOccupationCol);
		
		BaseColumnModel generalCommentsCol = BaseCellModel.getColModelForText("General Comments").required();
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
		List<BaseColumnModel> cols = new ArrayList<>();
		
		BaseColumnModel dupplicateCol = CheckboxCellModel.getCheckboxColModel("Use Physical Address For Postal Address?", null);
		cols.add(dupplicateCol);
		
		BaseColumnModel addressCol = BaseCellModel.getColModelForText("Address");
		cols.add(addressCol);
		
		BaseColumnModel gpsCoordinatesCol = BaseCellModel.getColModelForText("GPS Coordinates");
		cols.add(gpsCoordinatesCol);
		
		BaseColumnModel physicalAddress1Col = BaseCellModel.getColModelForText("Physical Address 1").required();
		cols.add(physicalAddress1Col);
		
		BaseColumnModel postalAddressLine1Col = BaseCellModel.getColModelForText("Postal Address Line 1").required();
		cols.add(postalAddressLine1Col);
		
		BaseColumnModel physicalAddress2Col = BaseCellModel.getColModelForText("Physical Address 2").required();
		cols.add(physicalAddress2Col);
		
		BaseColumnModel postalAddressLine2Col = BaseCellModel.getColModelForText("Postal Address Line 2").required();
		cols.add(postalAddressLine2Col);
		
		BaseColumnModel physicalAddress3Col = BaseCellModel.getColModelForText("Physical Address 3").required();
		cols.add(physicalAddress3Col);
		
		BaseColumnModel postalAddressLine3Col = BaseCellModel.getColModelForText("Postal Address Line 3").required();
		cols.add(postalAddressLine3Col);
		
		BaseColumnModel physicalCodeCol = BaseCellModel.getColModelForText("Physical Code").required();
		cols.add(physicalCodeCol);
		
		BaseColumnModel postalCodeCol = BaseCellModel.getColModelForText("Postal Code").required();
		cols.add(postalCodeCol);
		
		BaseColumnModel physicalProvinceCol = BaseCellModel.getColModelForText("Province");
		cols.add(physicalProvinceCol);
		
		BaseColumnModel postalProvinceCol = BaseCellModel.getColModelForText("Province");
		cols.add(postalProvinceCol);
		
		BaseColumnModel physicalMunicipalityCol = BaseCellModel.getColModelForText("Municipality");
		cols.add(physicalMunicipalityCol);
		
		BaseColumnModel postalMunicipalityCol = BaseCellModel.getColModelForText("Municipality");
		cols.add(postalMunicipalityCol);
		
		BaseColumnModel physicalUrbanRuralCol = BaseCellModel.getColModelForText("Urban Rural");
		cols.add(physicalUrbanRuralCol);
		
		BaseColumnModel postalUrbanRuralCol = BaseCellModel.getColModelForText("Urban Rural");
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
		List<BaseColumnModel> cols = new ArrayList<>();
		
		BaseColumnModel telephoneNumberCol = BaseCellModel.getColModelForText("Telephone Number");
		cols.add(telephoneNumberCol);
		
		BaseColumnModel cellPhoneNumberCol = BaseCellModel.getColModelForText("Cell Phone Number").required();
		cols.add(cellPhoneNumberCol);
		
		BaseColumnModel faxNumberCol = BaseCellModel.getColModelForText("Fax Number");
		cols.add(faxNumberCol);
		
		BaseColumnModel emailCol = BaseCellModel.getColModelForText("E Mail").required();
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

	public static List<String> demoData = List.of("item 1", "item 2", "item 3");
	
	private TableModel getPersonDetailComp() {
		
		List<BaseColumnModel> cols = new ArrayList<>();
		
		BaseColumnModel idDocUploadCol = UploadCellModel.getUploadColumnModel(null, "ID Document Upload", null, null);
		cols.add(idDocUploadCol);
		
		BaseColumnModel greettingCol = StringListCellModel.getColumnModel("Title"
				, List.of("Adv", "Dr", "Me", "Miss", "Mr")).required();
		cols.add(greettingCol);
		
		BaseColumnModel idNoCol = BaseCellModel.getColModelForText("ID No").required();
		cols.add(idNoCol);
		
		BaseColumnModel initialsCol = BaseCellModel.getColModelForText("Initials").required();
		cols.add(initialsCol);
		
		BaseColumnModel dateOfBirthCol = DateCellModel.getDateColumnModel("Date of Birth", null).required();
		cols.add(dateOfBirthCol);
		
		BaseColumnModel genderCol = StringListCellModel.getColumnModel("Gender"
				, List.of("Fermale", "Male", "Other")).required();
		cols.add(genderCol);
		
		BaseColumnModel equityCol = StringListCellModel.getColumnModel("Equity"
				, demoData).required();
		cols.add(equityCol);
		
		BaseColumnModel disabilityCol = StringListCellModel.getColumnModel("Disability"
				, demoData).required();
		cols.add(disabilityCol);
		
		BaseColumnModel homeLanguageCol = StringListCellModel.getColumnModel("Home Language"
				, demoData).required();
		cols.add(homeLanguageCol);
		
		BaseColumnModel citizenResidentialStatusCol = StringListCellModel.getColumnModel("Citizen Residential Status"
				, demoData).required();
		cols.add(citizenResidentialStatusCol);
		
		BaseColumnModel alternateIDTypeCol = StringListCellModel.getColumnModel("Alternate ID Type"
				, demoData).required();
		cols.add(alternateIDTypeCol);
		
		BaseColumnModel nationalityCol = StringListCellModel.getColumnModel("Nationality"
				, demoData).required();
		cols.add(nationalityCol);
		
		BaseColumnModel socioEconomicStatusCol = StringListCellModel.getColumnModel("Socio Economic Status"
				, demoData).required();
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
