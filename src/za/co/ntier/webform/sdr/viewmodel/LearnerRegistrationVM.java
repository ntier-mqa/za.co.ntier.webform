package za.co.ntier.webform.sdr.viewmodel;

import java.sql.Timestamp;
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

import org.compiere.model.PO;

import za.co.ntier.api.model.I_ZZLearner;
import za.co.ntier.api.model.I_ZZPerson;
import za.co.ntier.api.model.I_ZZQualification_v;
import za.co.ntier.api.model.I_ZZ_AlternateIDType;
import za.co.ntier.api.model.I_ZZ_EmploymentHistory;
import za.co.ntier.api.model.I_ZZ_ExperientialLearning;
import za.co.ntier.api.model.I_ZZ_ParentDetails;
import za.co.ntier.api.model.I_ZZ_PostSchoolEducation_Details;
import za.co.ntier.api.model.X_ZZLearner;
import za.co.ntier.api.model.X_ZZPerson;
import za.co.ntier.api.model.X_ZZQualification_v;
import za.co.ntier.api.model.X_ZZ_AlternateIDType;
import za.co.ntier.api.model.X_ZZ_EmploymentHistory;
import za.co.ntier.api.model.X_ZZ_ExperientialLearning;
import za.co.ntier.api.model.X_ZZ_LI_CitizenResidentialStatus;
import za.co.ntier.api.model.X_ZZ_LI_HomeLanguage;
import za.co.ntier.api.model.X_ZZ_LI_SocioEconomicStatus;
import za.co.ntier.api.model.X_ZZ_Nationality;
import za.co.ntier.api.model.X_ZZ_ParentDetails;
import za.co.ntier.api.model.X_ZZ_PostSchoolEducation_Details;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.form.bean.component.FormInfo;
import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.ISaveForm;
import za.co.ntier.webform.sdr.component.bean.RowModel;
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
	private TableModel tmParentDetails;
	private TableModel tmPostSchoolEducation;
	private TableModel tmExperientialLearning;
	private TableModel tmContactableReference;
	private TableModel tmEmploymentHistory;
	private NavTab mainTab;
	X_ZZLearner learner;

	DaoManage daoManage = new DaoManage();

	private ColumnModel parentFirstNameCol;
	private ColumnModel parentMiddleNameCol;
	private ColumnModel parentSurnameCol;
	private ColumnModel parentTitleCol;

	private ColumnModel qualificationNameCol;
	private ColumnModel linkedOfoDescCol;

	private ColumnModel empUploadCol;

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
			//idNoCol.setReadonly(true);
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
		//idNoCol.setReadonly(true);

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
			
			if (idNumber != null && idNumber.trim().matches("\\d{13}"))
			{
				autoPopulateDobAndGender(idNumber, tmGeneralDetail.getRow());
			}
		}

		if (tmParentDetails != null && tmParentDetails.getRow() != null) {
			tmParentDetails.getRow().setDataOneRow(null);
			tmParentDetails.reloadDao();
		}
		if (tmPostSchoolEducation != null && tmPostSchoolEducation.getRow() != null) {
			tmPostSchoolEducation.getRow().setDataOneRow(null);
			tmPostSchoolEducation.reloadDao();
		}
		if (tmExperientialLearning != null && tmExperientialLearning.getRow() != null) {
			tmExperientialLearning.getRow().setDataOneRow(null);
			tmExperientialLearning.reloadDao();
		}
		if (tmContactableReference != null && tmContactableReference.getRow() != null) {
			tmContactableReference.getRow().setDataOneRow(null);
			tmContactableReference.reloadDao();
		}
		if (tmEmploymentHistory != null && tmEmploymentHistory.getRow() != null) {
			tmEmploymentHistory.getRow().setDataOneRow(null);
			tmEmploymentHistory.reloadDao();
		}

		isNew = true;
	}

	private void loadForEdit() {
		alternateIDTypeCol.setReadonly(true);
		//idNoCol.setReadonly(true);
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
		initParentDetails();
		initPostSchoolEducation();
		initExperientialLearning();
		initEmploymentHistory();
	}

	ColumnModel idNoCol;
	ListColumnModel<X_ZZ_AlternateIDType> alternateIDTypeCol;
	ColumnModel								dateOfBirthCol;
	ColumnModel								genderCol;

	private void autoPopulateDobAndGender(String idNumber, RowModel rowModel)
	{
		String idString = idNumber.trim();
		Timestamp dob = MasterUtil.getDobFromId(idString);
		if (dob != null)
		{
			CellModel dobCell = rowModel.get(dateOfBirthCol);
			if (dobCell != null)
			{
				dobCell.setValue(dob);
				dateOfBirthCol.setReadonly(true);
				//BindUtils.postNotifyChange(null, null, dobCell, "value");
			}
		}
		String genderCode = MasterUtil.getGenderFromId(idString);
		if (genderCode != null)
		{
			ValueNamePair genderVal = MasterUtil.getLkpGenders().stream()
												.filter(v -> v.getValue().equals(genderCode))
												.findFirst().orElse(null);
			CellModel genderCell = rowModel.get(genderCol);
			if (genderCell != null && genderVal != null)
			{
				genderCell.setValue(genderVal);
				genderCol.setReadonly(true);
				//BindUtils.postNotifyChange(null, null, genderCell, "value");
			}
		}
	}

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

		idNoCol = IDCellModel.getIDColumnModel().required().setTableName(I_ZZPerson.Table_Name).setReadonly(true);
		idNoCol.setEventHandle((event, cellMode) -> {
			Object idValue = cellMode.getDirtyValue();
			if (idValue != null) {
				cellMode.getColModel().setDefaultValue(idValue);
				String idString = idValue.toString().trim();
				if (idString.matches("\\d{13}")) {
					autoPopulateDobAndGender(idString, cellMode.getRowModel());
				}
			}
		});
		cols.add(idNoCol);

		dateOfBirthCol = DateCellModel.getDateColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZPerson.Table_Name, I_ZZPerson.COLUMNNAME_Birthday),
				I_ZZPerson.COLUMNNAME_Birthday).required().setTableName(I_ZZPerson.Table_Name);
		cols.add(dateOfBirthCol);

		genderCol = ListCellModel.getListColumnModel(
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

		saveChildTables(trxName, learner.getZZLearner_ID());
	}

	private static volatile boolean s_schemaCompatibilityChecked = false;

	private static void ensureSchemaCompatibility() {
		if (s_schemaCompatibilityChecked) {
			return;
		}
		synchronized (LearnerRegistrationVM.class) {
			if (s_schemaCompatibilityChecked) {
				return;
			}
			try {
				DB.executeUpdateEx("ALTER TABLE zz_parentdetails ALTER COLUMN name DROP NOT NULL", null);
				DB.executeUpdateEx("ALTER TABLE zz_parentdetails ALTER COLUMN name SET DEFAULT ''", null);
			} catch (Exception e) {
				// ignore if already applied or column not present
			}
			try {
				DB.executeUpdateEx("ALTER TABLE zz_employmenthistory ALTER COLUMN name DROP NOT NULL", null);
				DB.executeUpdateEx("ALTER TABLE zz_employmenthistory ALTER COLUMN name SET DEFAULT ''", null);
			} catch (Exception e) {
				// ignore
			}
			s_schemaCompatibilityChecked = true;
		}
	}

	private void saveChildTables(String trxName, int learnerId) {
		ensureSchemaCompatibility();

		// 1. Parent/Guardian Details
		if (tmParentDetails != null && tmParentDetails.getRow() != null) {
			X_ZZ_ParentDetails pd = tmParentDetails.getRow().getDataOneRow(X_ZZ_ParentDetails.class, I_ZZ_ParentDetails.Table_Name);
			boolean hasPd = pd != null && (pd.getZZParentPerson_ID() > 0
					|| (pd.getZZFirstName() != null && !pd.getZZFirstName().isBlank())
					|| (pd.getZZMiddleName() != null && !pd.getZZMiddleName().isBlank())
					|| (pd.getSurname() != null && !pd.getSurname().isBlank())
					|| (pd.getTitle() != null && !pd.getTitle().isBlank()));
			if (hasPd) {
				pd.setZZLearner_ID(learnerId);
				String fn = pd.getZZFirstName();
				String sn = pd.getSurname();
				String fullName = ((fn != null ? fn : "") + " " + (sn != null ? sn : "")).trim();
				if (fullName.isEmpty()) {
					fullName = "Parent Details";
				}
				pd.saveEx(trxName);
				try {
					DB.executeUpdateEx("UPDATE zz_parentdetails SET name = ? WHERE zz_parentdetails_id = ?",
							new Object[] { fullName, pd.get_ID() }, trxName);
				} catch (Exception e) {
					// ignore
				}
			}
		}

		// 2. Post School Educational Details
		if (tmPostSchoolEducation != null && tmPostSchoolEducation.getRow() != null) {
			X_ZZ_PostSchoolEducation_Details ps = tmPostSchoolEducation.getRow()
					.getDataOneRow(X_ZZ_PostSchoolEducation_Details.class, I_ZZ_PostSchoolEducation_Details.Table_Name);
			boolean hasPs = ps != null && ((ps.getQualification() != null && !ps.getQualification().isBlank())
					|| (ps.getName() != null && !ps.getName().isBlank())
					|| ps.getZZ_DateAchieved() != null
					|| ps.getOFO_Occupation_ID() > 0
					|| (ps.getDescription() != null && !ps.getDescription().isBlank()));
			if (hasPs) {
				ps.setZZLearner_ID(learnerId);
				if (ps.getName() == null || ps.getName().isBlank()) {
					ps.setName(ps.getQualification() != null && !ps.getQualification().isBlank()
							? ps.getQualification() : "Post School Education");
				}
				ps.saveEx(trxName);
			}
		}

		// 3. Experiential Learning
		if (tmExperientialLearning != null && tmExperientialLearning.getRow() != null) {
			X_ZZ_ExperientialLearning exp = tmExperientialLearning.getRow()
					.getDataOneRow(X_ZZ_ExperientialLearning.class, I_ZZ_ExperientialLearning.Table_Name);
			if (tmContactableReference != null && tmContactableReference.getRow() != null) {
				X_ZZ_ExperientialLearning ref = tmContactableReference.getRow()
						.getDataOneRow(X_ZZ_ExperientialLearning.class, I_ZZ_ExperientialLearning.Table_Name);
				if (ref != null && exp != null) {
					if (ref.getTitle() != null && !ref.getTitle().isBlank())
						exp.setTitle(ref.getTitle());
					if (ref.getName() != null && !ref.getName().isBlank())
						exp.setName(ref.getName());
				}
			}
			boolean hasExp = exp != null && ((exp.getZZ_ExperianceWork() != null && !exp.getZZ_ExperianceWork().isBlank())
					|| exp.getDateFrom() != null
					|| exp.getDateTo() != null
					|| (exp.getZZ_NameWork() != null && !exp.getZZ_NameWork().isBlank())
					|| (exp.getTitle() != null && !exp.getTitle().isBlank())
					|| (exp.getName() != null && !exp.getName().isBlank()));
			if (hasExp) {
				exp.setZZLearner_ID(learnerId);
				if (exp.getName() == null || exp.getName().isBlank()) {
					exp.setName(exp.getZZ_NameWork() != null && !exp.getZZ_NameWork().isBlank()
							? exp.getZZ_NameWork() : "Experiential Learning");
				}
				exp.saveEx(trxName);
			}
		}

		// 4. Employment History
		if (tmEmploymentHistory != null && tmEmploymentHistory.getRow() != null) {
			X_ZZ_EmploymentHistory eh = tmEmploymentHistory.getRow()
					.getDataOneRow(X_ZZ_EmploymentHistory.class, I_ZZ_EmploymentHistory.Table_Name);
			CellModel uploadCell = tmEmploymentHistory.getRow().get(empUploadCol);
			boolean hasUpload = uploadCell instanceof UploadCellModel
					&& (((UploadCellModel) uploadCell).hasBytes() || ((UploadCellModel) uploadCell).getFileName() != null);
			boolean hasEh = eh != null && ((eh.getPosition() != null && !eh.getPosition().isBlank())
					|| (eh.getCompanyCode() != null && !eh.getCompanyCode().isBlank())
					|| eh.getDuration() > 0
					|| (eh.getZZ_Location() != null && !eh.getZZ_Location().isBlank())
					|| (eh.getDescription() != null && !eh.getDescription().isBlank())
					|| hasUpload);
			if (hasEh) {
				eh.setZZLearner_ID(learnerId);
				eh.saveEx(trxName);
				if (uploadCell instanceof UploadCellModel) {
					((UploadCellModel) uploadCell).attachFile(eh, trxName);
				}
			}
		}
	}

	private void initParentDetails() {
		List<ColumnModel> cols = new ArrayList<>();

		ValueAdaptColumnModel parentPersonCol = ValueAdaptCellModel.getValueAdaptColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZ_ParentDetails.Table_Name,
						I_ZZ_ParentDetails.COLUMNNAME_ZZParentPerson_ID),
				I_ZZ_ParentDetails.COLUMNNAME_ZZParentPerson_ID,
				CellModel.SEARCH_CELL);

		parentPersonCol.setEventHandle((event, cellModel) -> {
			showInfoPanel(
				InfoPanelPara.getInstance(I_ZZPerson.Table_Name, I_ZZPerson.COLUMNNAME_ZZPerson_ID),
				(obj, infoPanel) -> {
					Object[] objs = (Object[]) obj;
					if (objs == null || objs.length == 0 || objs[0] == null) {
						return;
					}
					int personId = objs[0] instanceof Number ? ((Number) objs[0]).intValue() : Integer.parseInt(objs[0].toString());
					X_ZZPerson selected = new X_ZZPerson(Env.getCtx(), personId, null);
					cellModel.setValue(selected);
					RowModel rm = cellModel.getRowModel();
					if (rm.get(parentFirstNameCol) != null) {
						rm.get(parentFirstNameCol).setValue(selected.getZZFirstName());
					}
					if (rm.get(parentMiddleNameCol) != null) {
						rm.get(parentMiddleNameCol).setValue(selected.getZZMiddleName());
					}
					if (rm.get(parentSurnameCol) != null) {
						rm.get(parentSurnameCol).setValue(selected.getSurname());
					}
					if (rm.get(parentTitleCol) != null) {
						String title = selected.get_ValueAsString("ZZLkpTitle");
						if (title != null && !title.isBlank()) {
							rm.get(parentTitleCol).setValue(title);
						}
					}
				});
		});

		parentPersonCol.setDisplayAdaptHandle(value -> {
			if (value == null)
				return null;
			X_ZZPerson p = (X_ZZPerson) value;
			String fn = p.getZZFirstName() != null ? p.getZZFirstName() : "";
			String sn = p.getSurname() != null ? p.getSurname() : "";
			return (fn + " " + sn).trim();
		});

		parentPersonCol.setValueAdaptHandle(value -> {
			if (value == null)
				return null;
			X_ZZPerson p = (X_ZZPerson) value;
			return p.getZZPerson_ID();
		});

		parentPersonCol.setValueFromDaoAdaptHandle(obj -> {
			if (obj == null)
				return null;
			Integer id = Integer.class.cast(obj);
			if (id == 0)
				return null;
			return new X_ZZPerson(Env.getCtx(), id, null);
		});
		cols.add(parentPersonCol);

		parentFirstNameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZ_ParentDetails.Table_Name,
						I_ZZ_ParentDetails.COLUMNNAME_ZZFirstName),
				I_ZZ_ParentDetails.COLUMNNAME_ZZFirstName).setReadonly(true);
		cols.add(parentFirstNameCol);

		parentMiddleNameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZ_ParentDetails.Table_Name,
						I_ZZ_ParentDetails.COLUMNNAME_ZZMiddleName),
				I_ZZ_ParentDetails.COLUMNNAME_ZZMiddleName).setReadonly(true);
		cols.add(parentMiddleNameCol);

		parentSurnameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZ_ParentDetails.Table_Name,
						I_ZZ_ParentDetails.COLUMNNAME_Surname),
				I_ZZ_ParentDetails.COLUMNNAME_Surname).setReadonly(true);
		cols.add(parentSurnameCol);

		parentTitleCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZ_ParentDetails.Table_Name,
						I_ZZ_ParentDetails.COLUMNNAME_Title),
				I_ZZ_ParentDetails.COLUMNNAME_Title,
				MasterUtil.getLkpTitleLists(),
				title -> title.getName(),
				title -> title.getValue()
		).setzClass(ValueNamePair.class);
		parentTitleCol.setReadonly(true);
		cols.add(parentTitleCol);

		tmParentDetails = TableModel.getTableBean(TableModel.class, cols, false, I_ZZ_ParentDetails.Table_Name);
		tmParentDetails.setSclass("two-col srd-parent-details");
		tmParentDetails.setRowSaveFilter(row -> false);
		tmParentDetails.init();

		tmParentDetails.setLoadSavedDataHandle(tm -> {
			if (learner != null && learner.getZZLearner_ID() > 0) {
				X_ZZ_ParentDetails pd = new Query(Env.getCtx(), I_ZZ_ParentDetails.Table_Name, "ZZLearner_ID=?", null)
						.setParameters(learner.getZZLearner_ID())
						.setOnlyActiveRecords(true)
						.firstOnly();
				tm.getRow().setDataOneRow(pd);
			} else {
				tm.getRow().setDataOneRow(null);
			}
			tm.reloadDao();
		});

		NavTabPanel tabPanelParentDetails = new NavTabPanel(mainTab);
		tabPanelParentDetails.setTabTitle("Parent/Guardian Details");
		tabPanelParentDetails.getCompModel().add(tmParentDetails);
	}

	private void initPostSchoolEducation() {
		List<ColumnModel> cols = new ArrayList<>();

		ValueAdaptColumnModel qualificationCol = ValueAdaptCellModel.getValueAdaptColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZ_PostSchoolEducation_Details.Table_Name,
						I_ZZ_PostSchoolEducation_Details.COLUMNNAME_Qualification),
				I_ZZ_PostSchoolEducation_Details.COLUMNNAME_Qualification,
				CellModel.SEARCH_CELL);

		qualificationCol.setEventHandle((event, cellModel) -> {
			showInfoPanel(
				InfoPanelPara.getInstance(I_ZZQualification_v.Table_Name, I_ZZQualification_v.COLUMNNAME_ZZQualification_v_UU),
				(obj, infoPanel) -> {
					Object[] objs = (Object[]) obj;
					if (objs == null || objs.length == 0 || objs[0] == null) {
						return;
					}
					X_ZZQualification_v selected = null;
					if (objs[0] instanceof Number) {
						selected = new X_ZZQualification_v(Env.getCtx(), ((Number) objs[0]).intValue(), null);
					} else if (objs[0] instanceof String) {
						String val = (String) objs[0];
						if (val.matches("\\d+")) {
							selected = new X_ZZQualification_v(Env.getCtx(), Integer.parseInt(val), null);
						} else {
							selected = new X_ZZQualification_v(Env.getCtx(), val, null);
						}
					}
					if (selected != null) {
						String qualCode = selected.getZZSaqaQualificationCode();
						String qualTitle = selected.getZZSaqaQualificationTitle();
						cellModel.setValue(qualCode != null && !qualCode.isBlank() ? qualCode : qualTitle);
						RowModel rm = cellModel.getRowModel();
						if (rm.get(qualificationNameCol) != null) {
							rm.get(qualificationNameCol).setValue(qualTitle);
						}
					}
				});
		});

		qualificationCol.setDisplayAdaptHandle(value -> value != null ? value.toString() : null);
		qualificationCol.setValueAdaptHandle(value -> value != null ? value.toString() : null);
		qualificationCol.setValueFromDaoAdaptHandle(obj -> obj != null ? obj.toString() : null);
		cols.add(qualificationCol);

		qualificationNameCol = CellModel.getColModelForText(
				"Qualification Name",
				I_ZZ_PostSchoolEducation_Details.COLUMNNAME_Name).setReadonly(true);
		cols.add(qualificationNameCol);

		ColumnModel dateAchievedCol = DateCellModel.getDateColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZ_PostSchoolEducation_Details.Table_Name,
						I_ZZ_PostSchoolEducation_Details.COLUMNNAME_ZZ_DateAchieved),
				I_ZZ_PostSchoolEducation_Details.COLUMNNAME_ZZ_DateAchieved);
		cols.add(dateAchievedCol);

		ValueAdaptColumnModel linkedOfoCodeCol = ValueAdaptCellModel.getValueAdaptColumnModel(
				"Linked OFO Code",
				I_ZZ_PostSchoolEducation_Details.COLUMNNAME_OFO_Occupation_ID,
				CellModel.SEARCH_CELL);

		linkedOfoCodeCol.setEventHandle((event, cellModel) -> {
			showInfoPanel(
				InfoPanelPara.getInstance("ZZ_Occupations_Ref", "ZZ_Occupations_Ref_ID"),
				(obj, infoPanel) -> {
					Object[] objs = (Object[]) obj;
					if (objs == null || objs.length == 0 || objs[0] == null) {
						return;
					}
					int occId = objs[0] instanceof Number ? ((Number) objs[0]).intValue() : Integer.parseInt(objs[0].toString());
					PO occ = MTable.get(Env.getCtx(), "ZZ_Occupations_Ref").getPO(occId, null);
					if (occ != null) {
						cellModel.setValue(occ);
						RowModel rm = cellModel.getRowModel();
						if (rm.get(linkedOfoDescCol) != null) {
							rm.get(linkedOfoDescCol).setValue(occ.get_ValueAsString("Name"));
						}
					}
				});
		});

		linkedOfoCodeCol.setDisplayAdaptHandle(value -> {
			if (value == null)
				return null;
			PO occ = (PO) value;
			return occ.get_ValueAsString("Value");
		});

		linkedOfoCodeCol.setValueAdaptHandle(value -> {
			if (value == null)
				return null;
			PO occ = (PO) value;
			return occ.get_ID();
		});

		linkedOfoCodeCol.setValueFromDaoAdaptHandle(obj -> {
			if (obj == null)
				return null;
			Integer id = Integer.class.cast(obj);
			if (id == 0)
				return null;
			return MTable.get(Env.getCtx(), "ZZ_Occupations_Ref").getPO(id, null);
		});
		cols.add(linkedOfoCodeCol);

		linkedOfoDescCol = CellModel.getColModelForText(
				"Linked OFO Description",
				I_ZZ_PostSchoolEducation_Details.COLUMNNAME_Description).setReadonly(true);
		cols.add(linkedOfoDescCol);

		tmPostSchoolEducation = TableModel.getTableBean(TableModel.class, cols, false,
				I_ZZ_PostSchoolEducation_Details.Table_Name);
		tmPostSchoolEducation.setSclass("two-col srd-post-school-education");
		tmPostSchoolEducation.setRowSaveFilter(row -> false);
		tmPostSchoolEducation.init();

		tmPostSchoolEducation.setLoadSavedDataHandle(tm -> {
			if (learner != null && learner.getZZLearner_ID() > 0) {
				X_ZZ_PostSchoolEducation_Details ps = new Query(Env.getCtx(),
						I_ZZ_PostSchoolEducation_Details.Table_Name, "ZZLearner_ID=?", null)
						.setParameters(learner.getZZLearner_ID())
						.setOnlyActiveRecords(true)
						.firstOnly();
				tm.getRow().setDataOneRow(ps);
			} else {
				tm.getRow().setDataOneRow(null);
			}
			tm.reloadDao();
		});

		NavTabPanel tabPanelPostSchoolEducation = new NavTabPanel(mainTab);
		tabPanelPostSchoolEducation.setTabTitle("Post School Educational Details");
		tabPanelPostSchoolEducation.getCompModel().add(tmPostSchoolEducation);
	}

	private void initExperientialLearning() {
		// Section 1: Experiential Learning
		List<ColumnModel> expCols = new ArrayList<>();

		ColumnModel experienceWorkCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZ_ExperientialLearning.Table_Name,
						I_ZZ_ExperientialLearning.COLUMNNAME_ZZ_ExperianceWork),
				I_ZZ_ExperientialLearning.COLUMNNAME_ZZ_ExperianceWork);
		expCols.add(experienceWorkCol);

		ColumnModel expDateFromCol = DateCellModel.getDateColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZ_ExperientialLearning.Table_Name,
						I_ZZ_ExperientialLearning.COLUMNNAME_DateFrom),
				I_ZZ_ExperientialLearning.COLUMNNAME_DateFrom);
		expCols.add(expDateFromCol);

		ColumnModel expDateToCol = DateCellModel.getDateColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZ_ExperientialLearning.Table_Name,
						I_ZZ_ExperientialLearning.COLUMNNAME_DateTo),
				I_ZZ_ExperientialLearning.COLUMNNAME_DateTo);
		expCols.add(expDateToCol);

		ColumnModel natureOfWorkCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZ_ExperientialLearning.Table_Name,
						I_ZZ_ExperientialLearning.COLUMNNAME_ZZ_NameWork),
				I_ZZ_ExperientialLearning.COLUMNNAME_ZZ_NameWork);
		expCols.add(natureOfWorkCol);

		tmExperientialLearning = TableModel.getTableBean(TableModel.class, expCols, false,
				I_ZZ_ExperientialLearning.Table_Name);
		tmExperientialLearning.setSubSectionHeader("Experiential Learning");
		tmExperientialLearning.setSclass("two-col srd-experiential-learning");
		tmExperientialLearning.setRowSaveFilter(row -> false);
		tmExperientialLearning.init();

		// Section 2: Contactable Reference:
		List<ColumnModel> refCols = new ArrayList<>();

		ColumnModel refTitleCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZ_ExperientialLearning.Table_Name,
						I_ZZ_ExperientialLearning.COLUMNNAME_Title),
				I_ZZ_ExperientialLearning.COLUMNNAME_Title,
				MasterUtil.getLkpTitleLists(),
				title -> title.getName(),
				title -> title.getValue()
		).setzClass(ValueNamePair.class);
		refCols.add(refTitleCol);

		ColumnModel refNameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZ_ExperientialLearning.Table_Name,
						I_ZZ_ExperientialLearning.COLUMNNAME_Name),
				I_ZZ_ExperientialLearning.COLUMNNAME_Name);
		refCols.add(refNameCol);

		tmContactableReference = TableModel.getTableBean(TableModel.class, refCols, false,
				I_ZZ_ExperientialLearning.Table_Name);
		tmContactableReference.setSubSectionHeader("Contactable Reference:");
		tmContactableReference.setSclass("two-col srd-contactable-reference");
		tmContactableReference.setRowSaveFilter(row -> false);
		tmContactableReference.init();

		tmExperientialLearning.setLoadSavedDataHandle(tm -> {
			if (learner != null && learner.getZZLearner_ID() > 0) {
				X_ZZ_ExperientialLearning exp = new Query(Env.getCtx(),
						I_ZZ_ExperientialLearning.Table_Name, "ZZLearner_ID=?", null)
						.setParameters(learner.getZZLearner_ID())
						.setOnlyActiveRecords(true)
						.firstOnly();
				tm.getRow().setDataOneRow(exp);
			} else {
				tm.getRow().setDataOneRow(null);
			}
			tm.reloadDao();
		});

		tmContactableReference.setLoadSavedDataHandle(tm -> {
			if (learner != null && learner.getZZLearner_ID() > 0) {
				X_ZZ_ExperientialLearning exp = new Query(Env.getCtx(),
						I_ZZ_ExperientialLearning.Table_Name, "ZZLearner_ID=?", null)
						.setParameters(learner.getZZLearner_ID())
						.setOnlyActiveRecords(true)
						.firstOnly();
				tm.getRow().setDataOneRow(exp);
			} else {
				tm.getRow().setDataOneRow(null);
			}
			tm.reloadDao();
		});

		NavTabPanel tabPanelExperientialLearning = new NavTabPanel(mainTab);
		tabPanelExperientialLearning.setTabTitle("Experiential Learning");
		tabPanelExperientialLearning.getCompModel().add(tmExperientialLearning);
		tabPanelExperientialLearning.getCompModel().add(tmContactableReference);
	}

	private void initEmploymentHistory() {
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel positionCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZ_EmploymentHistory.Table_Name,
						I_ZZ_EmploymentHistory.COLUMNNAME_Position),
				I_ZZ_EmploymentHistory.COLUMNNAME_Position);
		cols.add(positionCol);

		ColumnModel companyCol = CellModel.getColModelForText(
				"Company",
				I_ZZ_EmploymentHistory.COLUMNNAME_CompanyCode);
		cols.add(companyCol);

		ColumnModel durationCol = CellModel.getColModelForPositiveNumber(
				MasterUtil.getNameOfColTranslated(I_ZZ_EmploymentHistory.Table_Name,
						I_ZZ_EmploymentHistory.COLUMNNAME_Duration),
				I_ZZ_EmploymentHistory.COLUMNNAME_Duration);
		cols.add(durationCol);

		ColumnModel locationCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZ_EmploymentHistory.Table_Name,
						I_ZZ_EmploymentHistory.COLUMNNAME_ZZ_Location),
				I_ZZ_EmploymentHistory.COLUMNNAME_ZZ_Location);
		cols.add(locationCol);

		ColumnModel empDescriptionCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZ_EmploymentHistory.Table_Name,
						I_ZZ_EmploymentHistory.COLUMNNAME_Description),
				I_ZZ_EmploymentHistory.COLUMNNAME_Description);
		cols.add(empDescriptionCol);

		empUploadCol = UploadCellModel.getUploadColumnModel("", null, null, "UPLOAD FILE");
		empUploadCol.setShowTitle(false);
		cols.add(empUploadCol);

		tmEmploymentHistory = TableModel.getTableBean(TableModel.class, cols, false,
				I_ZZ_EmploymentHistory.Table_Name);
		tmEmploymentHistory.setSclass("two-col srd-employment-history");
		tmEmploymentHistory.setRowSaveFilter(row -> false);
		tmEmploymentHistory.init();

		tmEmploymentHistory.setLoadSavedDataHandle(tm -> {
			if (learner != null && learner.getZZLearner_ID() > 0) {
				X_ZZ_EmploymentHistory eh = new Query(Env.getCtx(),
						I_ZZ_EmploymentHistory.Table_Name, "ZZLearner_ID=?", null)
						.setParameters(learner.getZZLearner_ID())
						.setOnlyActiveRecords(true)
						.firstOnly();
				tm.getRow().setDataOneRow(eh);
			} else {
				tm.getRow().setDataOneRow(null);
			}
			tm.reloadDao();
		});

		NavTabPanel tabPanelEmploymentHistory = new NavTabPanel(mainTab);
		tabPanelEmploymentHistory.setTabTitle("Employment History");
		tabPanelEmploymentHistory.getCompModel().add(tmEmploymentHistory);
	}

	public TableModel getTmParentDetails() {
		return tmParentDetails;
	}

	public TableModel getTmPostSchoolEducation() {
		return tmPostSchoolEducation;
	}

	public TableModel getTmExperientialLearning() {
		return tmExperientialLearning;
	}

	public TableModel getTmContactableReference() {
		return tmContactableReference;
	}

	public TableModel getTmEmploymentHistory() {
		return tmEmploymentHistory;
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
