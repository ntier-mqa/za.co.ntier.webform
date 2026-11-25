package za.co.ntier.webform.sdr.viewmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.Env;
import org.compiere.util.ValueNamePair;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.api.model.I_AD_User;
import za.co.ntier.api.model.I_ZZSdf;
import za.co.ntier.api.model.MUser_New;
import za.co.ntier.api.model.X_ZZPersonAddress;
import za.co.ntier.api.model.X_ZZSdf;
import za.co.ntier.api.model.X_ZZ_AlternateIDType;
import za.co.ntier.api.model.X_ZZ_LI_CitizenResidentialStatus;
import za.co.ntier.api.model.X_ZZ_LI_HighestEducation;
import za.co.ntier.api.model.X_ZZ_LI_HomeLanguage;
import za.co.ntier.api.model.X_ZZ_LI_SocioEconomicStatus;
import za.co.ntier.api.model.X_ZZ_Nationality;
import za.co.ntier.api.model.X_ZZ_No_Yes_Ref;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.CellModel.InputCheckResult;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.ISaveForm;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.TableModel.DaoManage;
import za.co.ntier.webform.sdr.component.bean.cell.DateCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.IDCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.IDTypeCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.ListCellModel;
import za.co.ntier.webform.sdr.component.bean.column.ListColumnModel;
import za.co.ntier.webform.sdr.component.tab.bean.NavTab;
import za.co.ntier.webform.sdr.component.tab.bean.NavTabPanel;
import za.co.ntier.webform.sdr.component.util.BuildFormUtil;

public class MainSrdFormVM extends BaseAppVM {
	
	private static final String SOCIO_ECONOMIC_STATUS_EMPLOYED = "Employed";

	private NavTab mainTab;
	private TableModel names;
	private X_ZZSdf sdf;
	private MUser_New person;
	private final DaoManage personManage = new DaoManage();
	private boolean isNewSdf = true;

	private ListColumnModel<X_ZZ_LI_SocioEconomicStatus> socioEconomicStatusCol;
	private ColumnModel idNoCol;
	private ListColumnModel<X_ZZ_AlternateIDType> alternateIDTypeCol;
	private ColumnModel dateOfBirthCol;
	private ColumnModel genderCol;
	
	@Init(superclass = true)
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo){
		
		int loginUserId = Env.getAD_User_ID(Env.getCtx());
		person = new MUser_New(Env.getCtx(), loginUserId, null);
		
		personManage.setDao(person);
		
		Query savedDataQuery = MTable.get(Env.getCtx(), I_ZZSdf.Table_Name)
				.createQuery(String.format("%s = ?", I_ZZSdf.COLUMNNAME_AD_User_ID), null);
		savedDataQuery.setParameters(person.getAD_User_ID());
		savedDataQuery.setOnlyActiveRecords(true);
		sdf = savedDataQuery.firstOnly();
		if (sdf == null) {
			isNewSdf = true;
			personManage.setPoSupplier(I_ZZSdf.Table_Name, t -> {
				sdf = new X_ZZSdf(Env.getCtx(), 0, null);
				sdf.setAD_User_ID(person.getAD_User_ID());
				return sdf;
			});
			

		}else {
			isNewSdf = false;
			personManage.setDao(sdf);
		}
		
		names = getNamesComp(personManage);
		mainTab = new NavTab();

		// new pannel
		NavTabPanel personDetailTab = new NavTabPanel(mainTab);
		personDetailTab.setTabTitle("Person Details");
		// new component
		personDetailTab.getCompModel().add(getPersonDetailComp(personManage));

		// new pannel
		NavTabPanel contactDetailTab = new NavTabPanel(mainTab);
		contactDetailTab.setTabTitle("Other Contact Details");
		// new component
		contactDetailTab.getCompModel().add(getContactDetailComp(personManage));

		NavTabPanel addressDetailTab = new NavTabPanel(mainTab);
		addressDetailTab.setSclass("address");
		addressDetailTab.setTabTitle("Address Details");
		
		TableModel postalAddress = BuildFormUtil.getAddressDetailComp("Postal ", "Postal", "Postal", null);
		TableModel physicalAddress = BuildFormUtil.getAddressDetailComp("Physical ", "Physical", "Physical", postalAddress);
		
		postalAddress.setPoSupplier(rowModel -> {
			X_ZZPersonAddress address = BuildFormUtil.getNewAddress(rowModel.getTableModel().getDataType());
			address.setAD_User_ID(person.getAD_User_ID());
			return address;
		});
		
		physicalAddress.setPoSupplier(rowModel -> {
			X_ZZPersonAddress address = BuildFormUtil.getNewAddress(rowModel.getTableModel().getDataType());
			address.setAD_User_ID(person.getAD_User_ID());
			return address;
		});
		
		// reload address
		PO savedPo = BuildFormUtil.getSavedAddress(person.getAD_User_ID(), physicalAddress.getDataType(), true);
		physicalAddress.getRow().setDataOneRow(savedPo);
		physicalAddress.reloadDao();
		
		savedPo = BuildFormUtil.getSavedAddress(person.getAD_User_ID(), postalAddress.getDataType(), true);
		postalAddress.getRow().setDataOneRow(savedPo);
		postalAddress.reloadDao();
		
		addressDetailTab.getCompModel().add(physicalAddress);
		addressDetailTab.getCompModel().add(postalAddress);

		NavTabPanel educationDetailTab = new NavTabPanel(mainTab);
		educationDetailTab.setTabTitle("Education & Experience");
		educationDetailTab.getCompModel().add(getEducationComp(personManage));
	}

	
	private TableModel getNamesComp(DaoManage personManage) {
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel firstNameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_ZZFirstName)
				, I_AD_User.COLUMNNAME_ZZFirstName
				).required()
				.setDefaultValue(person.getFirstName());
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

		
		TableModel namesBean = TableModel.getTableBean(TableModel.class, cols, false, I_AD_User.Table_Name);

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
					)
					.setzClass(X_ZZ_LI_HighestEducation.class)
					.setUseForID(true)
					.required();
		cols.add(highestEducationCol);

		ColumnModel highestEducationDescriptionCol = 
				CellModel.getColModelForText(
						MasterUtil.getNameOfColTranslated(I_ZZSdf.Table_Name, I_ZZSdf.COLUMNNAME_ZZHighestEducationDesc)
						, I_ZZSdf.COLUMNNAME_ZZHighestEducationDesc
						);
		cols.add(highestEducationDescriptionCol);

		ColumnModel nameOfAccreditedTrainingProviderCol = 
				CellModel.getColModelForText(
						MasterUtil.getNameOfColTranslated(I_ZZSdf.Table_Name, I_ZZSdf.COLUMNNAME_ZZAccreditedTrainingProvider)
						, I_ZZSdf.COLUMNNAME_ZZAccreditedTrainingProvider
						);
		cols.add(nameOfAccreditedTrainingProviderCol);

		ColumnModel experienceCol = 
				CellModel.getColModelForPositiveNumber(
						MasterUtil.getNameOfColTranslated(I_ZZSdf.Table_Name, I_ZZSdf.COLUMNNAME_ZZExperience)
						, I_ZZSdf.COLUMNNAME_ZZExperience
						).required();
		cols.add(experienceCol);

		ColumnModel currentOccupationCol = 
				CellModel.getColModelForText(
						MasterUtil.getNameOfColTranslated(I_ZZSdf.Table_Name, I_ZZSdf.COLUMNNAME_ZZCurrentOccupation)
						, I_ZZSdf.COLUMNNAME_ZZCurrentOccupation
				).required();
		cols.add(currentOccupationCol);

		ColumnModel yearInOccupationCol = 
				CellModel.getColModelForPositiveNumber(
						MasterUtil.getNameOfColTranslated(I_ZZSdf.Table_Name, I_ZZSdf.COLUMNNAME_ZZYearsInOccupation)
						, I_ZZSdf.COLUMNNAME_ZZYearsInOccupation
						).required();
		yearInOccupationCol.setTitle("Years in Current Occupation");
		cols.add(yearInOccupationCol);

		ColumnModel generalCommentsCol = 
				CellModel.getColModelForText(
						MasterUtil.getNameOfColTranslated(I_ZZSdf.Table_Name, I_ZZSdf.COLUMNNAME_ZZGeneralComments)
						, I_ZZSdf.COLUMNNAME_ZZGeneralComments
						);
		cols.add(generalCommentsCol);

		TableModel educationBean = TableModel.getTableBean(TableModel.class, cols, false, I_ZZSdf.Table_Name);
		educationBean.setSclass("two-col srd-education");

		educationBean.setDaoManage(personManage);
		
		educationBean.init(null, null);
		
		return educationBean;
	}

	private TableModel getContactDetailComp(DaoManage personManage) {
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel cellPhoneNumberCol = CellModel.getColModelForPhone(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_Phone)
				, I_AD_User.COLUMNNAME_Phone
				).required();
		cols.add(cellPhoneNumberCol);

		ColumnModel telephoneNumberCol = CellModel.getColModelForPhone(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_Phone2)
				, I_AD_User.COLUMNNAME_Phone2
				);
		cols.add(telephoneNumberCol);

		ColumnModel emailCol = CellModel.getColModelForEmail(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_EMail)
				, I_AD_User.COLUMNNAME_EMail
				).required()
				.setReadonly(true);
		cols.add(emailCol);

		TableModel contactDetailBean = TableModel.getTableBean(TableModel.class, cols, false, I_AD_User.Table_Name);
		contactDetailBean.setSclass("two-col srd-contact");
		contactDetailBean.setDaoManage(personManage);
		contactDetailBean.init(null, null);

		return contactDetailBean;
	}

	private TableModel getPersonDetailComp(DaoManage personManage) {

		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel greettingCol = ListCellModel.getLkpTitleColumnModel();
		cols.add(greettingCol);

		idNoCol = IDCellModel.getIDColumnModel()
					.required().setTableName(I_AD_User.Table_Name);
		idNoCol.setTitle("ID/Passport No.");
		idNoCol.setEventHandle((event, cellModel) -> {
			Object idValue = cellModel.getDirtyValue();
			if (idValue != null && isRsaIdSelected(cellModel.getRowModel())) {
				BuildFormUtil.autoPopulateDobAndGender(idValue.toString(), cellModel.getRowModel(), dateOfBirthCol, genderCol, true);
			}
		});
		cols.add(idNoCol);

		ColumnModel initialsCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZSdf.Table_Name, I_ZZSdf.COLUMNNAME_ZZInitials)
				, I_ZZSdf.COLUMNNAME_ZZInitials
			).required();
		cols.add(initialsCol);

		dateOfBirthCol = DateCellModel.getDateColumnModel(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_Birthday)
				, I_AD_User.COLUMNNAME_Birthday
			).required()
			.setTableName(I_AD_User.Table_Name);
		cols.add(dateOfBirthCol);

		genderCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZSdf.Table_Name, I_ZZSdf.COLUMNNAME_ZZGender)
				, I_ZZSdf.COLUMNNAME_ZZGender
				, MasterUtil.getLkpGenders()
				, title -> {return title.getName();}
				, title -> {return title.getValue();}
			).setzClass(ValueNamePair.class).required();
		cols.add(genderCol);

		ColumnModel equityCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZSdf.Table_Name, I_ZZSdf.COLUMNNAME_ZZEquity)
				, I_ZZSdf.COLUMNNAME_ZZEquity
				, MasterUtil.getLkpEquity()
				, title -> {return title.toString();}
				, title -> {return title.getValue();}
			)
			.setzClass(ValueNamePair.class)
			.required();
		cols.add(equityCol);

		ColumnModel disabilityCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZSdf.Table_Name, I_ZZSdf.COLUMNNAME_ZZ_LI_Disability_ID)
				, I_ZZSdf.COLUMNNAME_ZZ_LI_Disability_ID
				, MasterUtil.getZZNoYes()
				, title -> {return title.getName();}
				, title -> {return title.getZZ_No_Yes_Ref_ID();}
			)
			.setzClass(X_ZZ_No_Yes_Ref.class)
			.setUseForID(true)
			.required();
		cols.add(disabilityCol);

		ColumnModel homeLanguageCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZSdf.Table_Name, I_ZZSdf.COLUMNNAME_ZZ_LI_HomeLanguage_ID)
				, I_ZZSdf.COLUMNNAME_ZZ_LI_HomeLanguage_ID
				, MasterUtil.getHomeLanguage()
				, title -> {return title.getName();}
				, title -> {return title.getZZ_LI_HomeLanguage_ID();}
			)
			.setzClass(X_ZZ_LI_HomeLanguage.class)
			.setUseForID(true)
			.required();
		cols.add(homeLanguageCol);

		ColumnModel citizenResidentialStatusCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZSdf.Table_Name, I_ZZSdf.COLUMNNAME_ZZ_LI_CitizenResidentialStatus_ID)
				, I_ZZSdf.COLUMNNAME_ZZ_LI_CitizenResidentialStatus_ID
				, MasterUtil.getCitizenResidentialStatus()
				, title -> {return title.getName();}
				, title -> {return title.getZZ_LI_CitizenResidentialStatus_ID();}
			)
			.setzClass(X_ZZ_LI_CitizenResidentialStatus.class)
			.setUseForID(true)
			.required();
		cols.add(citizenResidentialStatusCol);

		
		alternateIDTypeCol = IDTypeCellModel.getIDTypeCol();
		alternateIDTypeCol.setTitle("ID Type");
		// ID Type is captured at registration; only let the user pick one if it was never stored
		alternateIDTypeCol.setReadonly(person.get_ValueAsInt(I_AD_User.COLUMNNAME_ZZ_AlternateIDType_ID) > 0);
		alternateIDTypeCol.setEventHandle((event, cellModel) -> {
			@SuppressWarnings("unchecked")
			ListCellModel<X_ZZ_AlternateIDType> idTypeCell = (ListCellModel<X_ZZ_AlternateIDType>) cellModel;
			if (idTypeCell.getSelectedItem() != null) {
				// keep the user's choice when the form reloads data
				idTypeCell.getColModel().setDefaultValue(idTypeCell.getSelectedItem().getName(), MasterUtil.nameAlternateIdTypeCompare);
			}
			RowModel row = cellModel.getRowModel();
			if (isRsaIdSelected(row)) {
				CellModel idCell = row.get(idNoCol);
				Object idValue = idCell.getDirtyValue() != null ? idCell.getDirtyValue() : idCell.getValue();
				if (idValue != null)
					BuildFormUtil.autoPopulateDobAndGender(idValue.toString(), row, dateOfBirthCol, genderCol, true);
			} else {
				dateOfBirthCol.setReadonly(false);
				genderCol.setReadonly(false);
			}
			((IDCellModel) row.get(idNoCol)).validate();
		});
		cols.add(alternateIDTypeCol);
		
		
		ColumnModel nationalityCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZSdf.Table_Name, I_ZZSdf.COLUMNNAME_ZZ_Nationality_ID)
				, I_ZZSdf.COLUMNNAME_ZZ_Nationality_ID
				, MasterUtil.getNationality()
				, title -> {return title.getName();}
				, title -> {return title.getZZ_Nationality_ID();}
			)
			.setzClass(X_ZZ_Nationality.class)
			.setUseForID(true)
			.required();
		cols.add(nationalityCol);

		socioEconomicStatusCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZSdf.Table_Name, I_ZZSdf.COLUMNNAME_ZZ_LI_SocioEconomicStatus_ID)
				, I_ZZSdf.COLUMNNAME_ZZ_LI_SocioEconomicStatus_ID
				, MasterUtil.getSocioEconomicStatus()
				, title -> {return title.getName();}
				, title -> {return title.getZZ_LI_SocioEconomicStatus_ID();}
			).setzClass(X_ZZ_LI_SocioEconomicStatus.class)
			.setUseForID(true);
		socioEconomicStatusCol.required();
		// an SDF is always employed: lock Socio Economic Status to "Employed"
		X_ZZ_LI_SocioEconomicStatus employed = getEmployedSocioEconomicStatus();
		if (employed != null) {
			socioEconomicStatusCol.setDefaultValue(employed,
					(cell, item) -> item.getZZ_LI_SocioEconomicStatus_ID() == employed.getZZ_LI_SocioEconomicStatus_ID());
			socioEconomicStatusCol.setReadonly(true);
		}
		cols.add(socioEconomicStatusCol);

		TableModel personDetailBean = TableModel.getTableBean(TableModel.class, cols, false, I_ZZSdf.Table_Name);
		personDetailBean.setSclass("srd-person-detail");
		
		personDetailBean.setDaoManage(personManage);
		
		personDetailBean.init(null, null);

		// RSA ID: derive birthday and gender from the ID number and lock them
		RowModel row = personDetailBean.getRow();
		if (isRsaIdSelected(row)) {
			Object idValue = row.get(idNoCol).getValue();
			if (idValue != null) {
				BuildFormUtil.autoPopulateDobAndGender(idValue.toString(), row, dateOfBirthCol, genderCol, true);
			}
		}

		// existing SDFs may have another status saved; show "Employed" regardless
		if (employed != null) {
			row.get(socioEconomicStatusCol).setValue(employed);
		}

		return personDetailBean;
	}

	/**
	 * @return the "Employed" Socio Economic Status record, or null (with a warning) if it is not configured
	 */
	private X_ZZ_LI_SocioEconomicStatus getEmployedSocioEconomicStatus() {
		X_ZZ_LI_SocioEconomicStatus employed = MasterUtil.getSocioEconomicStatus().stream()
				.filter(status -> status.getName() != null && SOCIO_ECONOMIC_STATUS_EMPLOYED.equalsIgnoreCase(status.getName().trim()))
				.findFirst().orElse(null);
		if (employed == null) {
			log.warning("Socio Economic Status '" + SOCIO_ECONOMIC_STATUS_EMPLOYED + "' not found; SDF field left editable");
		}
		return employed;
	}

	private boolean isRsaIdSelected(RowModel row) {
		@SuppressWarnings("unchecked")
		ListCellModel<X_ZZ_AlternateIDType> idTypeCell = (ListCellModel<X_ZZ_AlternateIDType>) row.get(alternateIDTypeCol);
		return idTypeCell != null && idTypeCell.getSelectedItem() != null
				&& IDCellModel.idTypeRSA_ID.equals(idTypeCell.getSelectedItem().getName());
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

	@Override
	public List<DaoManage> getDaoManages() {
		return List.of(personManage);
	}
	
	@Override
	public List<ISaveForm> getSaveComponents() {
		return List.of(mainTab, names);
	}
	
	@Override
	protected void showResult(boolean isSubmit) {
		if(isNewSdf) {
			MasterUtil.showInfoDialog("ZZSDFCreatedSuccess", MasterUtil.fCloseActiveWindow);
		}else {
			MasterUtil.showInfoDialog("ZZSDFSavedSuccess", MasterUtil.fCloseActiveWindow);
		}
	}
	
	@Override
	public boolean isSupportDelete() {
		return false;
	}

	@Override
	public boolean onNextTab(NavTab navTab) {
		if (navTab == null) {
			return false;
		}

		int activeIndex = navTab.getActiveTabIndex();
		if (!validateTabsUpTo(activeIndex) || !saveTillTab(activeIndex)) {
			return true;
		}

		navTab.doNextTab();
		return true;
	}

	@Override
	public boolean onPrevTab(NavTab navTab) {
		if (navTab == null) {
			return false;
		}

		int activeIndex = navTab.getActiveTabIndex();
		if (mainTab != null && mainTab.getTabPanelModel() != null && activeIndex >= 0 && activeIndex < mainTab.getTabPanelModel().size()) {
			NavTabPanel currentPanel = mainTab.getTabPanelModel().get(activeIndex);
			if (currentPanel != null) {
				InputCheckResult check = currentPanel.parseInputState();
				if (check != null && !check.getNotChange()) {
					boolean isHeaderValid = names == null || names.validate(null);
					if (isHeaderValid && currentPanel.validate(null)) {
						saveTillTab(activeIndex);
					}
				}
			}
		}

		navTab.doPrevTab();
		return true;
	}

	private boolean validateTabsUpTo(int activeIndex) {
		if (names != null && !names.validate(null)) {
			return false;
		}
		if (mainTab != null && mainTab.getTabPanelModel() != null) {
			List<NavTabPanel> tabPanels = mainTab.getTabPanelModel();
			for (int i = 0; i <= activeIndex && i < tabPanels.size(); i++) {
				NavTabPanel panel = tabPanels.get(i);
				if (panel != null && !panel.validate(null)) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean saveTillTab(int activeIndex) {
		try {
			trxWrapperFunc(trxName -> {
				doSaveTillTab(activeIndex, trxName);
			}, getClass().getName() + "_onTabSave", this);

			return true;
		} catch (Exception e) {
			log.log(Level.WARNING, "Failed to save record on tab transition: " + e.getMessage(), e);
			showException(e);
			return false;
		}
	}

	private void doSaveTillTab(int activeIndex, String trxName) {
		// 1. Sync and save names (header)
		if (names != null) {
			names.syncUIToDao(trxName);
			names.saveToDb(trxName);
		}

		// 2. Sync and save tab panels up to activeIndex
		if (mainTab != null && mainTab.getTabPanelModel() != null) {
			List<NavTabPanel> tabPanels = mainTab.getTabPanelModel();
			for (int i = 0; i <= activeIndex && i < tabPanels.size(); i++) {
				NavTabPanel panel = tabPanels.get(i);
				if (panel != null) {
					panel.syncUIToDao(trxName);
					panel.saveToDb(trxName);
					panel.saveAttachment(trxName);
				}
			}
		}

		// 3. Save DAOs in personManage
		personManage.setTrxName(trxName);
		personManage.saveDao(trxName);

		// 4. Update SDF record
		updateSdfRecord(trxName);
	}

	private void updateSdfRecord(String trxName) {
		if (sdf == null) {
			sdf = (X_ZZSdf) personManage.getDao(I_ZZSdf.Table_Name);
		}
		if (sdf != null) {
			if (isNewSdf && sdf.getAD_User_ID() == 0) {
				sdf.setAD_User_ID(person.getAD_User_ID());
			}
			sdf.setZZMaintainStatus(calculateMaintainStatus());
			X_ZZ_LI_SocioEconomicStatus employed = getEmployedSocioEconomicStatus();
			if (employed != null) {
				sdf.setZZ_LI_SocioEconomicStatus_ID(employed.getZZ_LI_SocioEconomicStatus_ID());
			}
			sdf.saveEx(trxName);
			isNewSdf = false;
			personManage.setDao(sdf);
		}
	}

	private String calculateMaintainStatus() {
		if (mainTab != null && mainTab.getTabPanelModel() != null) {
			for (NavTabPanel tabPanel : mainTab.getTabPanelModel()) {
				InputCheckResult inputCheckResult = tabPanel.parseInputState();
				if (inputCheckResult != null && !inputCheckResult.getFillMandatory()) {
					return X_ZZSdf.ZZMAINTAINSTATUS_No;
				}
			}
		}
		return X_ZZSdf.ZZMAINTAINSTATUS_Yes;
	}

	@Override
	public void doSave(String trxName) {
		int maxIndex = (mainTab != null && mainTab.getTabPanelModel() != null)
				? mainTab.getTabPanelModel().size() - 1
				: 0;
		int activeIndex = isSubmitting() ? maxIndex : (mainTab != null ? mainTab.getActiveTabIndex() : maxIndex);
		doSaveTillTab(activeIndex, trxName);
	}
}
