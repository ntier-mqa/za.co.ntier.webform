package za.co.ntier.webform.sdr.viewmodel;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.form.bean.component.FormInfo;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.cell.ColumnModel;
import za.co.ntier.webform.sdr.component.tab.bean.NavTab;
import za.co.ntier.webform.sdr.component.tab.bean.NavTabPanel;

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
		
		ColumnModel firstNameCol = ColumnModel.getColText("First Name").required();
		cols.add(firstNameCol);
		
		ColumnModel middleNameCol = ColumnModel.getColText("Middle Name");
		cols.add(middleNameCol);
		
		ColumnModel surNameCol = ColumnModel.getColText("Surname").required();
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
		
		ColumnModel highestEducationCol = ColumnModel.getColListString("Highest Education", demoData).required();
		cols.add(highestEducationCol);
		
		ColumnModel highestEducationDescriptionCol = ColumnModel.getColText("Highest Education Description");
		cols.add(highestEducationDescriptionCol);
		
		ColumnModel nameOfAccreditedTrainingProviderCol = ColumnModel.getColText("Name Of Accredited Training Provider");
		cols.add(nameOfAccreditedTrainingProviderCol);
		
		ColumnModel experienceCol = ColumnModel.getColText("Experience").required();
		cols.add(experienceCol);
		
		ColumnModel currentOccupationCol = ColumnModel.getColText("Current Occupation").required();
		cols.add(currentOccupationCol);
		
		ColumnModel yearInOccupationCol = ColumnModel.getColText("Year In Occupation").required();
		cols.add(yearInOccupationCol);
		
		ColumnModel generalCommentsCol = ColumnModel.getColText("General Comments").required();
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
		
		ColumnModel addressCol = ColumnModel.getColText("Address");
		cols.add(addressCol);
		
		ColumnModel gpsCoordinatesCol = ColumnModel.getColText("GPS Coordinates");
		cols.add(gpsCoordinatesCol);
		
		ColumnModel physicalAddress1Col = ColumnModel.getColText("Physical Address 1").required();
		cols.add(physicalAddress1Col);
		
		ColumnModel postalAddressLine1Col = ColumnModel.getColText("Postal Address Line 1").required();
		cols.add(postalAddressLine1Col);
		
		ColumnModel physicalAddress2Col = ColumnModel.getColText("Physical Address 2").required();
		cols.add(physicalAddress2Col);
		
		ColumnModel postalAddressLine2Col = ColumnModel.getColText("Postal Address Line 2").required();
		cols.add(postalAddressLine2Col);
		
		ColumnModel physicalAddress3Col = ColumnModel.getColText("Physical Address 3").required();
		cols.add(physicalAddress3Col);
		
		ColumnModel postalAddressLine3Col = ColumnModel.getColText("Postal Address Line 3").required();
		cols.add(postalAddressLine3Col);
		
		ColumnModel physicalCodeCol = ColumnModel.getColText("Physical Code").required();
		cols.add(physicalCodeCol);
		
		ColumnModel postalCodeCol = ColumnModel.getColText("Postal Code").required();
		cols.add(postalCodeCol);
		
		ColumnModel physicalProvinceCol = ColumnModel.getColText("Province");
		cols.add(physicalProvinceCol);
		
		ColumnModel postalProvinceCol = ColumnModel.getColText("Province");
		cols.add(postalProvinceCol);
		
		ColumnModel physicalMunicipalityCol = ColumnModel.getColText("Municipality");
		cols.add(physicalMunicipalityCol);
		
		ColumnModel postalMunicipalityCol = ColumnModel.getColText("Municipality");
		cols.add(postalMunicipalityCol);
		
		ColumnModel physicalUrbanRuralCol = ColumnModel.getColText("Urban Rural");
		cols.add(physicalUrbanRuralCol);
		
		ColumnModel postalUrbanRuralCol = ColumnModel.getColText("Urban Rural");
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
		
		ColumnModel telephoneNumberCol = ColumnModel.getColText("Telephone Number");
		cols.add(telephoneNumberCol);
		
		ColumnModel cellPhoneNumberCol = ColumnModel.getColText("Cell Phone Number").required();
		cols.add(cellPhoneNumberCol);
		
		ColumnModel faxNumberCol = ColumnModel.getColText("Fax Number");
		cols.add(faxNumberCol);
		
		ColumnModel emailCol = ColumnModel.getColText("E Mail").required();
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
		
		List<ColumnModel> cols = new ArrayList<>();
		
		ColumnModel greettingCol = ColumnModel.getColListString("Title"
				, List.of("Adv", "Dr", "Me", "Miss", "Mr")).required();
		cols.add(greettingCol);
		
		ColumnModel idNoCol = ColumnModel.getColText("ID No").required();
		cols.add(idNoCol);
		
		ColumnModel initialsCol = ColumnModel.getColText("Initials").required();
		cols.add(initialsCol);
		
		ColumnModel dateOfBirthCol = ColumnModel.getColDate("Date of Birth", null).required();
		cols.add(dateOfBirthCol);
		
		ColumnModel genderCol = ColumnModel.getColListString("Gender"
				, List.of("Fermale", "Male", "Other")).required();
		cols.add(genderCol);
		
		ColumnModel equityCol = ColumnModel.getColListString("Equity"
				, demoData).required();
		cols.add(equityCol);
		
		ColumnModel disabilityCol = ColumnModel.getColListString("Disability"
				, demoData).required();
		cols.add(disabilityCol);
		
		ColumnModel homeLanguageCol = ColumnModel.getColListString("Home Language"
				, demoData).required();
		cols.add(homeLanguageCol);
		
		ColumnModel citizenResidentialStatusCol = ColumnModel.getColListString("Citizen Residential Status"
				, demoData).required();
		cols.add(citizenResidentialStatusCol);
		
		ColumnModel alternateIDTypeCol = ColumnModel.getColListString("Alternate ID Type"
				, demoData).required();
		cols.add(alternateIDTypeCol);
		
		ColumnModel nationalityCol = ColumnModel.getColListString("Nationality"
				, demoData).required();
		cols.add(nationalityCol);
		
		ColumnModel socioEconomicStatusCol = ColumnModel.getColListString("Socio Economic Status"
				, demoData).required();
		cols.add(socioEconomicStatusCol);
		
		TableModel personDetailBean = TableModel.getTableBean(TableModel.class, cols, false);
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
