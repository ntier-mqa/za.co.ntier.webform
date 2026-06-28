package za.co.ntier.webform.sdr.viewmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.webui.panel.RegistrationWindow;
import org.compiere.model.I_C_Location;
import org.compiere.model.MTable;
import org.compiere.model.Query;
import org.compiere.model.X_C_Location;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.ValueNamePair;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.WrongValueException;

import za.co.ntier.api.model.I_ZZLearner;
import za.co.ntier.api.model.I_ZZLkpSchoolEmis;
import za.co.ntier.api.model.I_ZZPerson;
import za.co.ntier.api.model.I_ZZ_AlternateIDType;
import za.co.ntier.api.model.X_ZZLearner;
import za.co.ntier.api.model.X_ZZLkpSchoolEmis;
import za.co.ntier.api.model.X_ZZLkpStatssaAreaCode;
import za.co.ntier.api.model.X_ZZPerson;
import za.co.ntier.api.model.X_ZZ_AlternateIDType;
import za.co.ntier.api.model.X_ZZ_LI_CitizenResidentialStatus;
import za.co.ntier.api.model.X_ZZ_LI_HomeLanguage;
import za.co.ntier.api.model.X_ZZ_LI_SocioEconomicStatus;
import za.co.ntier.api.model.X_ZZ_Nationality;
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
import za.co.ntier.webform.sdr.component.bean.cell.IDCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.IDTypeCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.ListCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.UploadCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.ValueAdaptCellModel;
import za.co.ntier.webform.sdr.component.bean.column.ListColumnModel;
import za.co.ntier.webform.sdr.component.bean.column.ValueAdaptColumnModel;
import za.co.ntier.webform.sdr.component.tab.bean.NavTab;
import za.co.ntier.webform.sdr.component.tab.bean.NavTabPanel;
import za.co.ntier.webform.sdr.component.util.BuildFormUtil;
import za.co.ntier.webform.sdr.component.util.BuildFormUtil.SettingAddress;
import za.co.ntier.webform.sdr.component.util.BuildFormUtil.SettingTableMode;

public class LearnerRegistrationVM extends BaseAppVM {

	private TableModel tmNames;
	private TableModel tmGeneralDetail;
	private NavTab mainTab;
	X_ZZLearner learner;

	DaoManage daoManage = new DaoManage();

	public static final String healthFunctionDefault = "No difficulty";
	BiFunction<ListCellModel<ValueNamePair>, ValueNamePair, Boolean> healthFunctionNameCompare = (cellModel, item) -> {
		String compareValue = cellModel.getColModel().getSelectedItemDisplayConvert().apply(item);
		return cellModel.getColModel().getDefaultValue().equals(compareValue);
	};

	// Identity validation state
	private String idNumber;
	private X_ZZ_AlternateIDType selectedIdType;
	private List<X_ZZ_AlternateIDType> alternateIdTypes;
	private String validationMessage = "";
	private boolean showCreateNew = false;
	private boolean identityValidated = false;

	@Override
	public Object getMainApp() {
		return null;
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
		if (isNew) {
			MasterUtil.showInfoDialog("ZZLearnerCreatedSuccess", MasterUtil.fCloseActiveWindow);
		} else {
			MasterUtil.showInfoDialog("ZZLearnerSavedSuccess", MasterUtil.fCloseActiveWindow);
		}
	}

	private X_ZZPerson person;
	boolean isNew = true;

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public X_ZZ_AlternateIDType getSelectedIdType() {
		return selectedIdType;
	}

	public void setSelectedIdType(X_ZZ_AlternateIDType selectedIdType) {
		this.selectedIdType = selectedIdType;
	}

	public List<X_ZZ_AlternateIDType> getAlternateIdTypes() {
		return alternateIdTypes;
	}

	public String getValidationMessage() {
		return validationMessage;
	}

	public boolean isShowCreateNew() {
		return showCreateNew;
	}

	public boolean isIdentityValidated() {
		return identityValidated;
	}

	@Init(superclass = true)
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo) {

		alternateIdTypes = MasterUtil.getAlternateIDType();
		selectedIdType = alternateIdTypes.stream().filter(t -> IDCellModel.idTypeRSA_ID.equals(t.getName())).findFirst()
				.orElse(null);

		daoManage.setPoSupplier(I_ZZPerson.Table_Name, daoManage -> {
			person = new X_ZZPerson(Env.getCtx(), 0, null);
			String name = (String) tmNames.getRow().get(firstNameCol).getValue();
			person.setZZFirstName(name);
			return person;
		});

		daoManage.setPoSupplier(I_ZZLearner.Table_Name, daoManage -> {
			learner = new X_ZZLearner(Env.getCtx(), 0, null);
			return learner;
		});

		setFormInfo(new FormInfo(menuContextInfo));
		setMainTab(new NavTab() {
			@Override
			protected boolean validateActiveTab(boolean emptyAsValid) {
				boolean isHeaderValid = true;
				if (tmNames != null) {
					isHeaderValid = tmNames.validate(null);
				}
				boolean isTabValid = super.validateActiveTab(emptyAsValid);
				return isHeaderValid && isTabValid;
			}
		});
		initForm();

		if (menuContextInfo.getRecordID() > 0) {
			loadForEdit();
		}
	}

	@Command
	@NotifyChange({ "validationMessage", "showCreateNew", "identityValidated" })
	public void onValidateIdentity() {
		if (idNumber == null || idNumber.isBlank()) {
			validationMessage = "Please enter an ID Number.";
			showCreateNew = false;
			return;
		}
		if (selectedIdType == null) {
			validationMessage = "Please select an ID Type.";
			showCreateNew = false;
			return;
		}

		if (IDCellModel.idTypeRSA_ID.equals(selectedIdType.getName())) {
			try {
				RegistrationWindow.validateIdNo(null, idNumber);
			} catch (WrongValueException e) {
				validationMessage = e.getMessage();
				showCreateNew = false;
				return;
			}
		}

		idNoCol.setDefaultValue(idNumber);
		alternateIDTypeCol.setDefaultValue(selectedIdType.getName(), MasterUtil.nameAlternateIdTypeCompare);

		// Lookup ZZPerson by ID No
		loadSaved(idNumber, selectedIdType.getZZ_AlternateIDType_ID());

		if (person != null) {
			// Person found - check learner status
			if (learner != null) {
				boolean isDraft = learner.getZZ_DocStatus() == null
						|| X_ZZLearner.ZZ_DOCSTATUS_Draft.equals(learner.getZZ_DocStatus());
				if (!isDraft) {
					validationMessage = "A learner with " + selectedIdType.getName() + " " + idNumber
							+ " already exists and is not in Draft status.";
					showCreateNew = false;
					identityValidated = false;
					return;
				}
				validationMessage = "Person and learner record found. You may edit the details below.";
			} else {
				validationMessage = "Person found. A new learner record will be created upon save.";
			}
			showCreateNew = false;
			identityValidated = true;
			alternateIDTypeCol.setReadonly(true);
			idNoCol.setReadonly(true);
		} else {
			// Person NOT found
			validationMessage = "A person with " + selectedIdType.getName() + " " + idNumber
					+ " does not exist in the system.";
			showCreateNew = true;
			identityValidated = false;
		}
	}

	@Command
	@NotifyChange({ "identityValidated", "showCreateNew", "validationMessage" })
	public void onCreateNew() {
		identityValidated = true;
		showCreateNew = false;
		validationMessage = "Creating new learner record. Please fill in all required fields.";

		// Reset DAOs for new person
		daoManage.resetDao(I_ZZPerson.Table_Name);
		daoManage.resetDao(I_ZZLearner.Table_Name);

		idNoCol.setDefaultValue(idNumber);
		if (selectedIdType != null) {
			alternateIDTypeCol.setDefaultValue(selectedIdType.getName(), MasterUtil.nameAlternateIdTypeCompare);
		}
		alternateIDTypeCol.setReadonly(true);
		idNoCol.setReadonly(true);

		if (tmGeneralDetail != null && tmGeneralDetail.getRow() != null) {
			CellModel idCell = tmGeneralDetail.getRow().get(idNoCol);
			if (idCell != null) {
				idCell.setValue(idNumber);
				BindUtils.postNotifyChange(null, null, idCell, "value");
			}

			@SuppressWarnings("unchecked")
			ListCellModel<X_ZZ_AlternateIDType> altIdCell = (ListCellModel<X_ZZ_AlternateIDType>) tmGeneralDetail
					.getRow().get(alternateIDTypeCol);
			if (altIdCell != null && selectedIdType != null) {
				altIdCell.getModel().clearSelection();
				altIdCell.getModel().addToSelection(selectedIdType);
				BindUtils.postNotifyChange(null, null, altIdCell, "selectedItem");
				BindUtils.postNotifyChange(null, null, altIdCell, "value");
			}
		}

		isNew = true;
	}

	private void loadForEdit() {
		alternateIDTypeCol.setReadonly(true);
		idNoCol.setReadonly(true);
		learner = (X_ZZLearner) MTable.get(Env.getCtx(), I_ZZLearner.Table_Name)
				.getPO(getMenuContextInfo().getRecordID(), null);
		if (learner == null) {
			MasterUtil.showInfoDialog("ZZLearnerNotFoundLearner", MasterUtil.fCloseActiveWindow);
		} else {
			person = (X_ZZPerson) MTable.get(Env.getCtx(), I_ZZPerson.Table_Name).getPO(learner.getZZPerson_ID(), null);
		}

		if (person == null) {
			MasterUtil.showInfoDialog("ZZLearnerNotFoundUser", MasterUtil.fCloseActiveWindow);
		}

		daoManage.setDao(learner);
		daoManage.setDao(person);

		identityValidated = true;
		if (person != null) {
			if (person.getZZ_ID_Passport_No() != null && !person.getZZ_ID_Passport_No().isBlank()) {
				idNumber = person.getZZ_ID_Passport_No();
			} else {
				idNumber = person.getZZOtherIDNo();
			}
		}
		validationMessage = "";
		showCreateNew = false;

		isNew = false;
		loadData();
	}

	private void loadSaved(String idValue, int idTypeId) {
		Query userQuery;
		if (IDCellModel.idTypeRSA_ID.equals(selectedIdType.getName())) {
			userQuery = MTable.get(Env.getCtx(), I_ZZPerson.Table_Name)
					.createQuery(String.format("%s = ? AND %s.%s = ?", I_ZZPerson.COLUMNNAME_ZZ_ID_Passport_No,
							I_ZZ_AlternateIDType.Table_Name, I_ZZ_AlternateIDType.COLUMNNAME_ZZ_AlternateIDType_ID),
							null);
		} else {
			userQuery = MTable.get(Env.getCtx(), I_ZZPerson.Table_Name)
					.createQuery(String.format("%s = ? AND %s.%s = ?", I_ZZPerson.COLUMNNAME_ZZOtherIDNo,
							I_ZZ_AlternateIDType.Table_Name, I_ZZ_AlternateIDType.COLUMNNAME_ZZ_AlternateIDType_ID),
							null);
		}

		userQuery.addTableDirectJoin(I_ZZ_AlternateIDType.Table_Name);

		userQuery.setParameters(idValue, idTypeId);
		userQuery.setOnlyActiveRecords(true);
		person = userQuery.firstOnly();

		X_ZZLearner learnerSaved = null;

		if (person != null) {
			daoManage.setDao(person);
			Query savedDataQuery = MTable.get(Env.getCtx(), I_ZZLearner.Table_Name)
					.createQuery(String.format("%s = ?", I_ZZLearner.COLUMNNAME_ZZPerson_ID), null);

			savedDataQuery.setParameters(person.getZZPerson_ID());
			savedDataQuery.setOnlyActiveRecords(true);

			learnerSaved = savedDataQuery.firstOnly();

			firstNameCol.setDefaultValue(person.getZZFirstName());
		} else {
			daoManage.resetDao(I_ZZPerson.Table_Name);
		}

		if (learnerSaved != null) {
			boolean isDraft = learnerSaved.getZZ_DocStatus() == null
					|| X_ZZLearner.ZZ_DOCSTATUS_Draft.equals(learnerSaved.getZZ_DocStatus());
			if (!isDraft) {
				MasterUtil.showInfoDialog("ZZLearnerWrongStatus", MasterUtil.fCloseActiveWindow);
			}
			daoManage.setDao(learnerSaved);
		} else {
			daoManage.resetDao(I_ZZLearner.Table_Name);
		}

		isNew = learnerSaved == null;
		learner = learnerSaved;

		loadData();
	}

	private void loadData() {
		if (person != null)// don't reload when null to keep user input
			tmNames.reloadDao();

		if (learner != null)
			mainTab.getTabPanelModel().forEach(tabModel -> {
				tabModel.getCompModel().forEach(tableModel -> {
					((TableModel) tableModel).reloadDao();
				});
			});

		mainTab.getTabPanelModel().forEach(tabModel -> {
			tabModel.getCompModel().forEach(tableModel -> {
				((TableModel) tableModel).loadSavedData();
			});
		});
	}

	private void initForm() {
		tmNames = initTbName();
		initGeneralDetail();
		initContactDetail();
		initHealthFunction();
		initAddresss();
		initEducationDetail();
		initUploadDocument();
	}

	ColumnModel idNoCol;
	ListColumnModel<X_ZZ_AlternateIDType> alternateIDTypeCol;

	private void initGeneralDetail() {
		List<ColumnModel> cols = new ArrayList<>();

		alternateIDTypeCol = IDTypeCellModel.getIDTypeCol();
		alternateIDTypeCol.setTableName(I_ZZPerson.Table_Name);
		alternateIDTypeCol.setEventHandle((event, cellMode) -> {
			@SuppressWarnings("unchecked")
			ListCellModel<X_ZZ_AlternateIDType> alternateIDCellMode = (ListCellModel<X_ZZ_AlternateIDType>) cellMode;
			alternateIDCellMode.resetDefaultValue();
			alternateIDCellMode.getColModel().setDefaultValue(alternateIDCellMode.getSelectedItem().getName(),
					MasterUtil.nameAlternateIdTypeCompare);
			IDCellModel idCellMode = (IDCellModel) cellMode.getRowModel().get(idNoCol);
			idCellMode.validate();
		});
		cols.add(alternateIDTypeCol);

		idNoCol = IDCellModel.getIDColumnModel().required().setTableName(I_ZZPerson.Table_Name);
		idNoCol.setEventHandle((event, cellMode) -> {
			Object idValue = cellMode.getDirtyValue();
			if (idValue != null)
				cellMode.getColModel().setDefaultValue(idValue);
		});
		cols.add(idNoCol);

		ColumnModel dateOfBirthCol = DateCellModel.getDateColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZPerson.Table_Name, I_ZZPerson.COLUMNNAME_Birthday),
				I_ZZPerson.COLUMNNAME_Birthday).required().setTableName(I_ZZPerson.Table_Name);
		cols.add(dateOfBirthCol);

		ColumnModel genderCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZPerson.Table_Name, I_ZZPerson.COLUMNNAME_ZZGender),
				I_ZZPerson.COLUMNNAME_ZZGender, MasterUtil.getLkpGenders(), title -> {
					return title.getName();
				}, title -> {
					return title.getValue();
				}).setzClass(ValueNamePair.class).required();
		cols.add(genderCol);

		ColumnModel equityCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZPerson.Table_Name, I_ZZPerson.COLUMNNAME_ZZEquity),
				I_ZZPerson.COLUMNNAME_ZZEquity, MasterUtil.getLkpEquity(), title -> {
					return title.toString();
				}, title -> {
					return title.getValue();
				}).setzClass(ValueNamePair.class).required();
		cols.add(equityCol);

		ColumnModel homeLanguageCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZPerson.Table_Name, I_ZZPerson.COLUMNNAME_ZZ_LI_HomeLanguage_ID),
				I_ZZPerson.COLUMNNAME_ZZ_LI_HomeLanguage_ID, MasterUtil.getHomeLanguage(), title -> {
					return title.getName();
				}, title -> {
					return title.getZZ_LI_HomeLanguage_ID();
				}).setzClass(X_ZZ_LI_HomeLanguage.class).setUseForID(true).required();
		cols.add(homeLanguageCol);

		ColumnModel nationalityCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZPerson.Table_Name, I_ZZPerson.COLUMNNAME_ZZ_Nationality_ID),
				I_ZZPerson.COLUMNNAME_ZZ_Nationality_ID, MasterUtil.getNationality(), title -> {
					return title.getName();
				}, title -> {
					return title.getZZ_Nationality_ID();
				}).setzClass(X_ZZ_Nationality.class).setUseForID(true).required();
		cols.add(nationalityCol);

		ColumnModel citizenResidentialStatusCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZPerson.Table_Name,
						I_ZZPerson.COLUMNNAME_ZZ_LI_CitizenResidentialStatus_ID),
				I_ZZPerson.COLUMNNAME_ZZ_LI_CitizenResidentialStatus_ID, MasterUtil.getCitizenResidentialStatus(),
				title -> {
					return title.getName();
				}, title -> {
					return title.getZZ_LI_CitizenResidentialStatus_ID();
				}).setzClass(X_ZZ_LI_CitizenResidentialStatus.class).setUseForID(true).required();
		cols.add(citizenResidentialStatusCol);

		ColumnModel socioEconomicStatusCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZPerson.Table_Name,
						I_ZZPerson.COLUMNNAME_ZZ_LI_SocioEconomicStatus_ID),
				I_ZZPerson.COLUMNNAME_ZZ_LI_SocioEconomicStatus_ID, MasterUtil.getSocioEconomicStatus(), title -> {
					return title.getName();
				}, title -> {
					return title.getZZ_LI_SocioEconomicStatus_ID();
				}).setzClass(X_ZZ_LI_SocioEconomicStatus.class).setUseForID(true).required();
		cols.add(socioEconomicStatusCol);

		tmGeneralDetail = TableModel.getTableBean(TableModel.class, cols, false, I_ZZPerson.Table_Name);
		tmGeneralDetail.setSclass("srd-general srd-general-learner");

		tmGeneralDetail.setDaoManage(daoManage);

		tmGeneralDetail.init();

		NavTabPanel tabPanelGeneralDetail = new NavTabPanel(mainTab);
		tabPanelGeneralDetail.setTabTitle("General Details");
		tabPanelGeneralDetail.getCompModel().add(tmGeneralDetail);
	}

	ColumnModel firstNameCol;

	private TableModel initTbName() {
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel greettingCol = ListCellModel.getLkpTitleColumnModel();
		greettingCol.setTableName(I_ZZPerson.Table_Name);
		cols.add(greettingCol);

		firstNameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZPerson.Table_Name, I_ZZPerson.COLUMNNAME_ZZFirstName),
				I_ZZPerson.COLUMNNAME_ZZFirstName).required();

		cols.add(firstNameCol);

		ColumnModel midNameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZPerson.Table_Name, I_ZZPerson.COLUMNNAME_ZZMiddleName),
				I_ZZPerson.COLUMNNAME_ZZMiddleName);
		cols.add(midNameCol);

		ColumnModel surnameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZPerson.Table_Name, I_ZZPerson.COLUMNNAME_Surname),
				I_ZZPerson.COLUMNNAME_Surname).required();
		cols.add(surnameCol);

		TableModel tmNames = TableModel.getTableBean(TableModel.class, cols, false, I_ZZPerson.Table_Name);
		tmNames.setSclass("srd-name srd-name-assessor");
		tmNames.setDaoManage(daoManage);
		tmNames.init();

		return tmNames;
	}

	private void initContactDetail() {
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel cellPhoneNumberCol = CellModel.getColModelForPhone(
				MasterUtil.getNameOfColTranslated(I_ZZPerson.Table_Name, I_ZZPerson.COLUMNNAME_Phone),
				I_ZZPerson.COLUMNNAME_Phone).required();
		cols.add(cellPhoneNumberCol);

		ColumnModel telephoneNumberCol = CellModel.getColModelForPhone(
				MasterUtil.getNameOfColTranslated(I_ZZPerson.Table_Name, I_ZZPerson.COLUMNNAME_Phone2),
				I_ZZPerson.COLUMNNAME_Phone2);
		cols.add(telephoneNumberCol);

		ColumnModel emailCol = CellModel.getColModelForEmail(
				MasterUtil.getNameOfColTranslated(I_ZZPerson.Table_Name, I_ZZPerson.COLUMNNAME_EMail),
				I_ZZPerson.COLUMNNAME_EMail).required();
		cols.add(emailCol);

		TableModel tmContactDetail = TableModel.getTableBean(TableModel.class, cols, false, I_ZZPerson.Table_Name);
		tmContactDetail.setSclass("srd-contact srd-contact-learner");
		tmContactDetail.setDaoManage(daoManage);
		tmContactDetail.init();

		NavTabPanel contactDetailTab = new NavTabPanel(mainTab);
		contactDetailTab.setTabTitle("Contact Details");
		contactDetailTab.getCompModel().add(tmContactDetail);

	}

	private void initEducationDetail() {
		List<ColumnModel> cols = new ArrayList<>();

		ValueAdaptColumnModel lastSchoolEmisCol = ValueAdaptCellModel.getValueAdaptColumnModel(
				Msg.getElement(Env.getCtx(), "ZZLastSchoolEmis"), I_ZZPerson.COLUMNNAME_ZZLkpSchoolEmis_ID,
				CellModel.SEARCH_CELL);
		lastSchoolEmisCol.required();

		lastSchoolEmisCol.setEventHandle((event, cellModel) -> {
			showInfoPanel(obj -> {
				Object[] objs = (Object[]) obj;
				X_ZZLkpSchoolEmis selected = new X_ZZLkpSchoolEmis(Env.getCtx(), (int) objs[0], null);
				cellModel.setValue(selected);
			}, I_ZZLkpSchoolEmis.Table_Name, I_ZZLkpSchoolEmis.COLUMNNAME_ZZLkpSchoolEmis_ID);
		});

		lastSchoolEmisCol.setDisplayAdaptHandle(value -> {
			if (value == null)
				return null;

			X_ZZLkpSchoolEmis schoolEmis = (X_ZZLkpSchoolEmis) value;
			return schoolEmis.getName();
		});

		lastSchoolEmisCol.setValueAdaptHandle(value -> {
			if (value == null)
				return null;

			X_ZZLkpSchoolEmis schoolEmis = (X_ZZLkpSchoolEmis) value;
			return schoolEmis.getZZLkpSchoolEmis_ID();
		});

		lastSchoolEmisCol.setValueFromDaoAdaptHandle(obj -> {
			if (obj == null)
				return null;

			Integer id = Integer.class.cast(obj);
			if (id == 0)
				return null;

			return new X_ZZLkpSchoolEmis(Env.getCtx(), id, null);
		});

		cols.add(lastSchoolEmisCol);

		ColumnModel lastSchoolYearCol = CellModel.getColModelForPositiveNumber(
				MasterUtil.getNameOfColTranslated(I_ZZPerson.Table_Name, I_ZZPerson.COLUMNNAME_ZZLastSchoolYear),
				I_ZZPerson.COLUMNNAME_ZZLastSchoolYear).required();
		cols.add(lastSchoolYearCol);

		ValueAdaptColumnModel areaCodeCol = ValueAdaptCellModel.getValueAdaptColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZPerson.Table_Name, I_ZZPerson.COLUMNNAME_ZZLkpStatssaAreaCode_ID),
				I_ZZPerson.COLUMNNAME_ZZLkpStatssaAreaCode_ID, CellModel.SEARCH_CELL);
		areaCodeCol.required();

		areaCodeCol.setEventHandle((event, cellModel) -> {
			showInfoPanel(obj -> {
				Object[] objs = (Object[]) obj;
				X_ZZLkpStatssaAreaCode selected = new X_ZZLkpStatssaAreaCode(Env.getCtx(), (int) objs[0], null);
				cellModel.setValue(selected);
			}, X_ZZLkpStatssaAreaCode.Table_Name, X_ZZLkpStatssaAreaCode.COLUMNNAME_ZZLkpStatssaAreaCode_ID);
		});

		areaCodeCol.setDisplayAdaptHandle(value -> {
			if (value == null)
				return null;
			X_ZZLkpStatssaAreaCode schoolEmis = (X_ZZLkpStatssaAreaCode) value;
			return schoolEmis.getName();
		});

		areaCodeCol.setValueAdaptHandle(value -> {
			if (value == null)
				return null;

			X_ZZLkpStatssaAreaCode statssaAreaCode = (X_ZZLkpStatssaAreaCode) value;
			return statssaAreaCode.getZZLkpStatssaAreaCode_ID();
		});

		areaCodeCol.setValueFromDaoAdaptHandle(obj -> {
			if (obj == null)
				return null;

			Integer id = Integer.class.cast(obj);
			if (id == 0)
				return null;

			return new X_ZZLkpStatssaAreaCode(Env.getCtx(), id, null);
		});

		cols.add(areaCodeCol);

		ColumnModel popiActStatusCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZPerson.Table_Name, I_ZZPerson.COLUMNNAME_ZZPopiActStatus),
				I_ZZPerson.COLUMNNAME_ZZPopiActStatus, MasterUtil.getPopiActStatus(), title -> {
					return title.getName();
				}, title -> {
					return title.getValue();
				}).setzClass(ValueNamePair.class).required();
		cols.add(popiActStatusCol);

		ColumnModel popiActStatusDateCol = DateCellModel
				.getDateColumnModel(MasterUtil.getNameOfColTranslated(I_ZZPerson.Table_Name,
						I_ZZPerson.COLUMNNAME_ZZPopiActStatusDate), I_ZZPerson.COLUMNNAME_ZZPopiActStatusDate)
				.required();
		cols.add(popiActStatusDateCol);

		TableModel tmEducationDetail = TableModel.getTableBean(TableModel.class, cols, false, I_ZZPerson.Table_Name);
		tmEducationDetail.setSclass("srd-education-detail srd-education-detail-learner");
		tmEducationDetail.setDaoManage(daoManage);
		tmEducationDetail.init();

		NavTabPanel tabPanelEducationDetail = new NavTabPanel(mainTab);
		tabPanelEducationDetail.setTabTitle("Education Details");
		tabPanelEducationDetail.getCompModel().add(tmEducationDetail);
	}

	private void initHealthFunction() {
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel seeingCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZPerson.Table_Name, I_ZZPerson.COLUMNNAME_ZZHealthSeeing),
				I_ZZPerson.COLUMNNAME_ZZHealthSeeing, MasterUtil.getHealthFunctions(), title -> {
					return title.getName();
				}, title -> {
					return title.getValue();
				}).setzClass(ValueNamePair.class).setDefaultValue(healthFunctionDefault, healthFunctionNameCompare)
				.required();
		cols.add(seeingCol);

		ColumnModel hearingCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZPerson.Table_Name, I_ZZPerson.COLUMNNAME_ZZHealthHearing),
				I_ZZPerson.COLUMNNAME_ZZHealthHearing, MasterUtil.getHealthFunctions(), title -> {
					return title.getName();
				}, title -> {
					return title.getValue();
				}).setzClass(ValueNamePair.class).setDefaultValue(healthFunctionDefault, healthFunctionNameCompare)
				.required();
		cols.add(hearingCol);

		ColumnModel communicatingCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZPerson.Table_Name, I_ZZPerson.COLUMNNAME_ZZHealthCommunicating),
				I_ZZPerson.COLUMNNAME_ZZHealthCommunicating, MasterUtil.getHealthFunctions(), title -> {
					return title.getName();
				}, title -> {
					return title.getValue();
				}).setzClass(ValueNamePair.class).setDefaultValue(healthFunctionDefault, healthFunctionNameCompare)
				.required();
		cols.add(communicatingCol);

		ColumnModel walkingCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZPerson.Table_Name, I_ZZPerson.COLUMNNAME_ZZHealthWalking),
				I_ZZPerson.COLUMNNAME_ZZHealthWalking, MasterUtil.getHealthFunctions(), title -> {
					return title.getName();
				}, title -> {
					return title.getValue();
				}).setzClass(ValueNamePair.class).setDefaultValue(healthFunctionDefault, healthFunctionNameCompare)
				.required();
		cols.add(walkingCol);

		ColumnModel rememberingCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZPerson.Table_Name, I_ZZPerson.COLUMNNAME_ZZHealthRemembering),
				I_ZZPerson.COLUMNNAME_ZZHealthRemembering, MasterUtil.getHealthFunctions(), title -> {
					return title.getName();
				}, title -> {
					return title.getValue();
				}).setzClass(ValueNamePair.class).setDefaultValue(healthFunctionDefault, healthFunctionNameCompare)
				.required();
		cols.add(rememberingCol);

		ColumnModel selfcareCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZPerson.Table_Name, I_ZZPerson.COLUMNNAME_ZZHealthSelfcare),
				I_ZZPerson.COLUMNNAME_ZZHealthSelfcare, MasterUtil.getHealthFunctions(), title -> {
					return title.getName();
				}, title -> {
					return title.getValue();
				}).setzClass(ValueNamePair.class).setDefaultValue(healthFunctionDefault, healthFunctionNameCompare)
				.required();
		cols.add(selfcareCol);

		TableModel tmHealthFunctions = TableModel.getTableBean(TableModel.class, cols, false, I_ZZPerson.Table_Name);
		tmHealthFunctions.setSclass("srd-health-function srd-health-function-learner");
		tmHealthFunctions.setDaoManage(daoManage);
		tmHealthFunctions.init();

		NavTabPanel tabPanelHealthFunctions = new NavTabPanel(mainTab);
		tabPanelHealthFunctions.setTabTitle("Health Functions Values");
		tabPanelHealthFunctions.getCompModel().add(tmHealthFunctions);
	}

	private void initAddresss() {
		TableModel tmPostalAddress = BuildFormUtil.getAddressDetailComp(SettingTableMode.getSimple("Postal"),
				SettingAddress.getSimple("Postal"));

		TableModel tmPhysicalAddress = BuildFormUtil.getAddressDetailComp(SettingTableMode.getSimple("Physical"),
				SettingAddress.getSimple("Physical", tmPostalAddress));

		NavTabPanel addressDetailTab = new NavTabPanel(mainTab);
		addressDetailTab.setSclass("sdr-address sdr-address-learner");
		addressDetailTab.setTabTitle("Address Details");
		addressDetailTab.getCompModel().add(tmPhysicalAddress);
		addressDetailTab.getCompModel().add(tmPostalAddress);

		tmPhysicalAddress.setAfterAppSave((tableModel, trxName) -> {
			TableModel tmAddress = (TableModel) tableModel;
			X_C_Location location = tmAddress.getRow().getDataOneRow(X_C_Location.class, I_C_Location.Table_Name);
			if (location != null) {
				person.setZZPhysicalLocation_ID(location.getC_Location_ID());
				person.saveEx(trxName);
			}
			return true;
		});

		tmPostalAddress.setAfterAppSave((tableModel, trxName) -> {
			TableModel tmAddress = (TableModel) tableModel;
			X_C_Location location = tmAddress.getRow().getDataOneRow(X_C_Location.class, I_C_Location.Table_Name);
			if (location != null) {
				person.setZZPostalLocation_ID(location.getC_Location_ID());
				person.saveEx(trxName);
			}
			return true;
		});

		tmPhysicalAddress.setLoadSavedDataHandle(tm -> {
			if (person != null && person.getZZPhysicalLocation_ID() > 0) {
				X_C_Location physicalLocation = org.compiere.model.MLocation.getCopy(Env.getCtx(),
						person.getZZPhysicalLocation_ID(), null);
				tm.getRow().setDataOneRow(physicalLocation);
			} else {
				tm.getRow().setDataOneRow(null);
			}
			tm.reloadDao();
		});

		tmPostalAddress.setLoadSavedDataHandle(tm -> {
			if (person != null && person.getZZPostalLocation_ID() > 0) {
				X_C_Location postalLocation = org.compiere.model.MLocation.getCopy(Env.getCtx(),
						person.getZZPostalLocation_ID(), null);
				tm.getRow().setDataOneRow(postalLocation);
			} else {
				tm.getRow().setDataOneRow(null);
			}
			tm.reloadDao();
		});
	}

	private TableModel tmDocumentUpload;

	private void initUploadDocument() {
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel photoUploadCol = UploadCellModel.getUploadColumnModel("Photograph",
				I_ZZPerson.COLUMNNAME_ZZPhotographFileName, I_ZZPerson.COLUMNNAME_ZZPhotographFileName, "Photograph");
		photoUploadCol.setMandatory(true);
		cols.add(photoUploadCol);

		ColumnModel cvUploadCol = UploadCellModel.getUploadColumnModel("Curriculum Vitae (CV)",
				I_ZZPerson.COLUMNNAME_ZZCVFileName, I_ZZPerson.COLUMNNAME_ZZCVFileName, "CV");
		cvUploadCol.setMandatory(true);
		cols.add(cvUploadCol);

		tmDocumentUpload = TableModel.getTableBean(TableModel.class, cols, false, I_ZZPerson.Table_Name);
		tmDocumentUpload.setDaoManage(daoManage);
		tmDocumentUpload.init();

		NavTabPanel uploadDetailTab = new NavTabPanel(mainTab);
		uploadDetailTab.setTabTitle("Document Uploads");
		uploadDetailTab.getCompModel().add(tmDocumentUpload);
	}

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
		if (learner == null) {
			learner = (X_ZZLearner) daoManage.getDaoForSave(I_ZZLearner.Table_Name);
		}
		if (person == null) {
			person = (X_ZZPerson) daoManage.getDaoForSave(I_ZZPerson.Table_Name);
		}

		boolean isDraft = true;
		if (learner != null) {
			isDraft = learner.getZZ_DocStatus() == null
					|| X_ZZLearner.ZZ_DOCSTATUS_Draft.equals(learner.getZZ_DocStatus());
		}

		if (!isDraft) {
			throw new AdempiereException(Msg.getMsg(Env.getCtx(), "ZZLearnerWrongStatus"));
		}

		super.doSave(trxName);

		int alternateIdTypeId = person.getZZ_AlternateIDType_ID();
		if (alternateIdTypeId > 0) {
			X_ZZ_AlternateIDType altType = new X_ZZ_AlternateIDType(Env.getCtx(), alternateIdTypeId, null);
			if (IDCellModel.idTypeRSA_ID.equals(altType.getName())) {
				String idPassportNo = person.getZZ_ID_Passport_No();
				if (idPassportNo != null && !idPassportNo.isBlank()) {
					String sql = "SELECT COUNT(1) FROM ZZPerson WHERE ZZ_ID_Passport_No = ? AND ZZ_AlternateIDType_ID = ? AND ZZPerson_ID != ? AND IsActive='Y'";
					int count = DB.getSQLValue(trxName, sql, idPassportNo, alternateIdTypeId, person.get_ID());
					if (count > 0) {
						throw new AdempiereException(
								"A person with this ID Type and ID Number already exists in the system.");
					}
				}
			} else {
				String otherIdNo = person.getZZOtherIDNo();
				if (otherIdNo != null && !otherIdNo.isBlank()) {
					String sql = "SELECT COUNT(1) FROM ZZPerson WHERE ZZOtherIDNo = ? AND ZZ_AlternateIDType_ID = ? AND ZZPerson_ID != ? AND IsActive='Y'";
					int count = DB.getSQLValue(trxName, sql, otherIdNo, alternateIdTypeId, person.get_ID());
					if (count > 0) {
						throw new AdempiereException(
								"A person with this ID Type and ID Number already exists in the system.");
					}
				}
			}
		}

		learner.setZZPerson_ID(person.getZZPerson_ID());
		learner.saveEx(trxName);
	}

	@Override
	public void doSubmit(String trxName) {
		super.doSubmit(trxName);
	}

	@Override
	public boolean isSupportSubmit() {
		return true;
	}
}
