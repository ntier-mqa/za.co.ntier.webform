package za.co.ntier.webform.sdr.viewmodel;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.webui.panel.RegistrationWindow;
import org.compiere.model.MBPartner;
import org.compiere.model.MColumn;
import org.compiere.model.MLocation;
import org.compiere.model.MRefList;
import org.compiere.model.MTable;
import org.compiere.model.MUser;
import org.compiere.model.MYear;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Util;
import org.compiere.util.ValueNamePair;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Filedownload;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import za.co.ntier.api.model.I_ZZLearnerDGProgramme;
import za.co.ntier.api.model.I_ZZRegistrationDocs;
import za.co.ntier.api.model.I_ZZ_Program_Master_Data;
import za.co.ntier.api.model.X_ZZLearner;
import za.co.ntier.api.model.X_ZZLearnerDGProgramme;
import za.co.ntier.api.model.X_ZZLearnerLearnership;
import za.co.ntier.api.model.X_ZZLearnerQCTOArtisans;
import za.co.ntier.api.model.X_ZZLearnerQCTOLearnership;
import za.co.ntier.api.model.X_ZZLearnerQCTOSkillsProgramme;
import za.co.ntier.api.model.X_ZZLearnerSkillsProgramme;
import za.co.ntier.api.model.X_ZZLearner_v;
import za.co.ntier.api.model.X_ZZLkpSchoolEmis;
import za.co.ntier.api.model.X_ZZLkpStatssaAreaCode;
import za.co.ntier.api.model.X_ZZPerson;
import za.co.ntier.api.model.X_ZZQualification_v;
import za.co.ntier.api.model.X_ZZRegistrationDocs;
import za.co.ntier.api.model.X_ZZ_AlternateIDType;
import za.co.ntier.api.model.X_ZZ_EmploymentHistory;
import za.co.ntier.api.model.X_ZZ_ExperientialLearning;
import za.co.ntier.api.model.X_ZZ_LI_CitizenResidentialStatus;
import za.co.ntier.api.model.X_ZZ_LI_HomeLanguage;
import za.co.ntier.api.model.X_ZZ_LI_SocioEconomicStatus;
import za.co.ntier.api.model.X_ZZ_Nationality;
import za.co.ntier.api.model.X_ZZ_ParentDetails;
import za.co.ntier.api.model.X_ZZ_PostSchoolEducation_Details;
import za.co.ntier.api.model.X_ZZ_Program_Master_Data;
import za.co.ntier.webform.form.AttachmentUtil;
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
import za.co.ntier.webform.sdr.component.bean.column.UploadColumnModel;
import za.co.ntier.webform.sdr.component.bean.column.ValueAdaptColumnModel;
import za.co.ntier.webform.sdr.component.tab.bean.NavTab;
import za.co.ntier.webform.sdr.component.tab.bean.NavTabPanel;
import za.co.ntier.webform.sdr.component.util.BuildFormUtil;
import za.co.ntier.webform.sdr.component.util.BuildFormUtil.SettingAddress;
import za.co.ntier.webform.sdr.component.util.BuildFormUtil.SettingTableMode;
import za.co.ntier.webform.sdr.viewmodel.LearnerQualficationRegVM.InterventionConfig;

public class LearnerRegistrationVM extends BaseAppVM {

	private TableModel tmNames;
	private TableModel tmGeneralDetail;
	private TableModel tmParentDetails;
	private TableModel tmEducationDetail;
	private TableModel tmPostSchoolEducation;
	private TableModel tmExperientialLearning;
	private TableModel tmContactableReference;
	private TableModel tmEmploymentHistory;
	private TableModel tmDocumentUpload;
	private NavTab mainTab;
	X_ZZLearner learner;

	DaoManage daoManage = new DaoManage();

	private ValueAdaptColumnModel parentPersonCol;
	private ColumnModel parentFirstNameCol;
	private ColumnModel parentMiddleNameCol;
	private ColumnModel parentSurnameCol;
	private ColumnModel parentTitleCol;

	private ColumnModel qualificationNameCol;
	private ColumnModel linkedOfoDescCol;

	private ColumnModel empUploadCol;
	private ColumnModel progCol;

	// Identity validation state
	private String idNumber;
	private X_ZZ_AlternateIDType selectedIdType;
	private List<X_ZZ_AlternateIDType> alternateIdTypes;
	private String validationMessage = "";
	private boolean showCreateNew = false;
	private boolean identityValidated = false;

	private int loginId;
	private int adminBpId;

	// DG Programme Selection
	private X_ZZ_Program_Master_Data selectedDGProgramme;
	private String selectedDGProgrammeTitle = "";
	private boolean enterQualificationRequired = false;

	// Intervention / Qualification Selection
	public static final String INTERVENTION_QCTO_SKILLS = "QCTO Skills Programme";
	public static final String INTERVENTION_QCTO_LEARNERSHIPS = "QCTO Learnerships";
	public static final String INTERVENTION_QCTO_ARTISANS = "QCTO Artisans";
	public static final String INTERVENTION_SKILLS = "Skills Programme";
	public static final String INTERVENTION_LEARNERSHIPS = "Learnerships";
	public static final String INTERVENTION_ARTISANS = "Artisans";

	private List<String> interventionTypeList = List.of(
			INTERVENTION_QCTO_SKILLS,
			INTERVENTION_QCTO_LEARNERSHIPS,
			INTERVENTION_QCTO_ARTISANS,
			INTERVENTION_SKILLS,
			INTERVENTION_LEARNERSHIPS,
			INTERVENTION_ARTISANS);
	private String selectedInterventionType = INTERVENTION_QCTO_SKILLS;
	private PO selectedQualification;
	private String selectedQualificationTitle = "";

	// Registration & Document Upload TabModels & Panels
	private TableModel tmRegistration;
	private NavTabPanel registrationTabPanel;
	private NavTabPanel uploadDetailTabPanel;
	private PO targetRegistrationPo;
	private PO targetChildPo;

	public String getSelectedDGProgrammeTitle() {
		return selectedDGProgrammeTitle;
	}

	public boolean isEnterQualificationRequired() {
		return enterQualificationRequired;
	}

	public X_ZZ_Program_Master_Data getSelectedDGProgramme() {
		return selectedDGProgramme;
	}

	public List<String> getInterventionTypeList() {
		return interventionTypeList;
	}

	public String getSelectedInterventionType() {
		return selectedInterventionType;
	}

	public void setSelectedInterventionType(String selectedInterventionType) {
		this.selectedInterventionType = selectedInterventionType;
	}

	public String getSelectedQualificationTitle() {
		return selectedQualificationTitle;
	}

	public PO getSelectedQualification() {
		return selectedQualification;
	}

	public TableModel getTmRegistration() {
		return tmRegistration;
	}

	@Command
	@NotifyChange({ "selectedDGProgrammeTitle", "enterQualificationRequired", "selectedQualificationTitle" })
	public void openDGProgrammeLookup() {
		InfoPanelPara para = InfoPanelPara.getInstance(
				I_ZZ_Program_Master_Data.Table_Name,
				I_ZZ_Program_Master_Data.COLUMNNAME_ZZ_Program_Master_Data_ID);
		StringBuilder sql = new StringBuilder(I_ZZ_Program_Master_Data.Table_Name + "." + I_ZZ_Program_Master_Data.COLUMNNAME_IsActive + " = 'Y'");
		sql.append(" AND ").append(I_ZZ_Program_Master_Data.Table_Name).append(".").append(I_ZZ_Program_Master_Data.COLUMNNAME_ZZ_IsLearnerRegistration).append(" = 'Y'");
		para.setWhereClause(sql.toString());

		showInfoPanel(para, (obj, infoPanel) -> {
			Object[] values = (Object[]) obj;
			if (values == null || values.length == 0 || values[0] == null)
				return;
			int progId = values[0] instanceof Number ? ((Number) values[0]).intValue() : Integer.parseInt(values[0].toString());
			if (progId <= 0)
				return;

			loadDGProgramme(progId);
		});
	}

	private void loadDGProgramme(int progId) {
		selectedDGProgramme = new X_ZZ_Program_Master_Data(Env.getCtx(), progId, null);
		String title = selectedDGProgramme.getTitle();
		if (title == null || title.isBlank()) {
			title = selectedDGProgramme.getName();
		}
		selectedDGProgrammeTitle = title;

		if (tmRegistration != null && tmRegistration.getRow() != null && progCol != null) {
			CellModel progCell = tmRegistration.getRow().get(progCol);
			if (progCell != null) {
				progCell.setValue(selectedDGProgrammeTitle);
				BindUtils.postNotifyChange(null, null, progCell, "value");
			}
		}

		// Check setting on selected programme definition: 'Enter Qualification' (Y/N)
		boolean enterQual = false;
		try {
			enterQual = selectedDGProgramme.isEnterQualification();
		} catch (Exception ignored) {
		}
		if (!enterQual) {
			Object enterQualVal = selectedDGProgramme.get_Value(I_ZZ_Program_Master_Data.COLUMNNAME_IsEnterQualification);
			if (enterQualVal == null) {
				enterQualVal = selectedDGProgramme.get_Value("ZZ_EnterQualification");
			}
			if (enterQualVal != null) {
				if (enterQualVal instanceof Boolean) {
					enterQual = (Boolean) enterQualVal;
				} else {
					enterQual = "Y".equalsIgnoreCase(enterQualVal.toString()) || "true".equalsIgnoreCase(enterQualVal.toString());
				}
			}
		}
		enterQualificationRequired = enterQual;

		selectedQualification = null;
		selectedQualificationTitle = "";

		BindUtils.postNotifyChange(null, null, this, "selectedDGProgrammeTitle");
		BindUtils.postNotifyChange(null, null, this, "enterQualificationRequired");
		BindUtils.postNotifyChange(null, null, this, "selectedQualificationTitle");

		if (learner != null && learner.getZZLearner_ID() > 0) {
			loadExistingRegistration(learner.getZZLearner_ID(), progId);
			if (tmRegistration != null && tmRegistration.getRow() != null) {
				populateRegistrationDefaults();
			}
		}
	}

	@Command
	@NotifyChange({ "selectedQualificationTitle" })
	public void onSelectInterventionType() {
		selectedQualification = null;
		selectedQualificationTitle = "";
		if (learner != null && learner.getZZLearner_ID() > 0) {
			int pmdId = selectedDGProgramme != null ? selectedDGProgramme.getZZ_Program_Master_Data_ID() : 0;
			loadExistingRegistration(learner.getZZLearner_ID(), pmdId);
			if (tmRegistration != null && tmRegistration.getRow() != null) {
				populateRegistrationDefaults();
			}
		}
	}

	@Command
	@NotifyChange({ "selectedQualificationTitle" })
	public void openQualificationLookup() {
		InterventionConfig config = getInterventionConfig(selectedInterventionType);
		if (config == null) {
			config = LearnerQualficationRegVM.INTERVENTION_CONFIGS.get(0);
		}

		InfoPanelPara para = InfoPanelPara.getInstance(config.programmeTable, config.programmeKeyColumn);

		String whereClause = buildQualificationWhereClause(config);
		if (whereClause != null && !whereClause.isBlank()) {
			para.setWhereClause(whereClause);
		}

		final InterventionConfig finalConfig = config;
		showInfoPanel(para, (obj, infoPanel) -> {
			Object[] values = (Object[]) obj;
			if (values == null || values.length == 0 || values[0] == null)
				return;
			int progId = values[0] instanceof Number ? ((Number) values[0]).intValue() : Integer.parseInt(values[0].toString());
			if (progId <= 0)
				return;

			selectedQualification = finalConfig.programmeLoader.apply(progId);
			if (selectedQualification != null) {
				Object code = selectedQualification.get_Value(finalConfig.programmeCodeColumn);
				Object title = selectedQualification.get_Value(finalConfig.programmeTitleColumn);
				selectedQualificationTitle = (code != null ? code.toString() : "") + " - " + (title != null ? title.toString() : "");
			}
			if (learner != null && learner.getZZLearner_ID() > 0) {
				int pmdId = selectedDGProgramme != null ? selectedDGProgramme.getZZ_Program_Master_Data_ID() : 0;
				loadExistingRegistration(learner.getZZLearner_ID(), pmdId);
				if (tmRegistration != null && tmRegistration.getRow() != null) {
					populateRegistrationDefaults();
				}
			}
			BindUtils.postNotifyChange(null, null, this, "selectedQualificationTitle");
		});
	}

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

		loginId = Env.getAD_User_ID(Env.getCtx());
		if (loginId > 0) {
			MUser sdpAdmin = new MUser(Env.getCtx(), loginId, null);
			adminBpId = sdpAdmin.getC_BPartner_ID();
		}

		if (menuContextInfo != null && menuContextInfo.getProgramMasterData() != null) {
			loadDGProgramme(menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID());
		}

		alternateIdTypes = MasterUtil.getAlternateIDType();
		selectedIdType = alternateIdTypes.stream().filter(t -> IDCellModel.idTypeRSA_ID.equals(t.getName())).findFirst()
				.orElse(null);

		daoManage.setPoSupplier(X_ZZPerson.Table_Name, daoManage -> {
			person = new X_ZZPerson(Env.getCtx(), 0, null);
			String name = (String) tmNames.getRow().get(firstNameCol).getValue();
			person.setZZFirstName(name);
			return person;
		});

		daoManage.setPoSupplier(X_ZZLearner.Table_Name, daoManage -> {
			learner = new X_ZZLearner(Env.getCtx(), 0, null);
			return learner;
		});

		setFormInfo(new FormInfo(menuContextInfo));
		setMainTab(new NavTab() {
			@Override
			protected boolean validateActiveTab(boolean emptyAsValid) {
				if (emptyAsValid) {
					return true;
				}
				boolean isHeaderValid = true;
				if (tmNames != null) {
					isHeaderValid = tmNames.validate(null);
				}
				boolean isTabValid = super.validateActiveTab(emptyAsValid);
				return isHeaderValid && isTabValid;
			}

			@Override
			public void doNextTab() {
				super.doNextTab();
				BindUtils.postNotifyChange(null, null, LearnerRegistrationVM.this, "registrationTabActive");
			}

			@Override
			public void doPrevTab() {
				super.doPrevTab();
				BindUtils.postNotifyChange(null, null, LearnerRegistrationVM.this, "registrationTabActive");
			}
		});
		initForm();

		if (menuContextInfo.getRecordID() > 0) {
			loadForEdit();
		}
	}

	@Command
	@NotifyChange({ "validationMessage", "showCreateNew", "identityValidated", "selectedDGProgrammeTitle", "selectedQualificationTitle" })
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
		if (selectedDGProgramme == null) {
			validationMessage = "Please choose a DG Programme.";
			showCreateNew = false;
			return;
		}
		if (enterQualificationRequired && selectedQualification == null) {
			validationMessage = "Please choose a Qualification / Programme.";
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
			// Person found
			if (learner != null) {
				validationMessage = "Person and learner record found. You may edit the details below.";
				int pmdId = selectedDGProgramme != null ? selectedDGProgramme.getZZ_Program_Master_Data_ID() : 0;
				loadExistingRegistration(learner.getZZLearner_ID(), pmdId);
			} else {
				validationMessage = "Person found. A new learner record will be created upon save.";
				targetRegistrationPo = null;
				targetChildPo = null;
			}
			showCreateNew = false;
			identityValidated = true;
			alternateIDTypeCol.setReadonly(true);
			//idNoCol.setReadonly(true);
			setupPhase2Tabs();
		} else {
			// Person NOT found
			validationMessage = "A person with " + selectedIdType.getName() + " " + idNumber
					+ " does not exist in the system.";
			showCreateNew = true;
			identityValidated = false;
			targetRegistrationPo = null;
			targetChildPo = null;
		}
	}

	@Command
	@NotifyChange({ "identityValidated", "showCreateNew", "validationMessage" })
	public void onCreateNew() {
		identityValidated = true;
		showCreateNew = false;
		validationMessage = "Creating new learner record. Please fill in all required fields.";
		targetRegistrationPo = null;
		targetChildPo = null;

		// Reset DAOs for new person
		daoManage.resetDao(X_ZZPerson.Table_Name);
		daoManage.resetDao(X_ZZLearner.Table_Name);

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
			updateParentFieldsReadonly(false);
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
		setupPhase2Tabs();
		populateDisabilityFromPerson();
	}

	private void loadForEdit() {
		alternateIDTypeCol.setReadonly(true);
		//idNoCol.setReadonly(true);
		learner = (X_ZZLearner) MTable.get(Env.getCtx(), X_ZZLearner.Table_Name)
				.getPO(getMenuContextInfo().getRecordID(), null);
		if (learner == null) {
			MasterUtil.showInfoDialog("ZZLearnerNotFoundLearner", MasterUtil.fCloseActiveWindow);
		} else {
			person = (X_ZZPerson) MTable.get(Env.getCtx(), X_ZZPerson.Table_Name).getPO(learner.getZZPerson_ID(), null);
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
		int pmdId = selectedDGProgramme != null ? selectedDGProgramme.getZZ_Program_Master_Data_ID() : 0;
		loadExistingRegistration(learner != null ? learner.getZZLearner_ID() : 0, pmdId);
		loadData();
		setupPhase2Tabs();
	}

	private void loadSaved(String idValue, int idTypeId) {
		Query userQuery;
		if (IDCellModel.idTypeRSA_ID.equals(selectedIdType.getName())) {
			userQuery = MTable.get(Env.getCtx(), X_ZZPerson.Table_Name)
					.createQuery(String.format("%s = ? AND %s.%s = ?", X_ZZPerson.COLUMNNAME_ZZ_ID_Passport_No,
							X_ZZ_AlternateIDType.Table_Name, X_ZZ_AlternateIDType.COLUMNNAME_ZZ_AlternateIDType_ID),
							null);
		} else {
			userQuery = MTable.get(Env.getCtx(), X_ZZPerson.Table_Name)
					.createQuery(String.format("%s = ? AND %s.%s = ?", X_ZZPerson.COLUMNNAME_ZZOtherIDNo,
							X_ZZ_AlternateIDType.Table_Name, X_ZZ_AlternateIDType.COLUMNNAME_ZZ_AlternateIDType_ID),
							null);
		}

		userQuery.addTableDirectJoin(X_ZZ_AlternateIDType.Table_Name);

		userQuery.setParameters(idValue, idTypeId);
		userQuery.setOnlyActiveRecords(true);
		person = userQuery.firstOnly();

		X_ZZLearner learnerSaved = null;

		if (person != null) {
			daoManage.setDao(person);
			Query savedDataQuery = MTable.get(Env.getCtx(), X_ZZLearner.Table_Name)
					.createQuery(String.format("%s = ?", X_ZZLearner.COLUMNNAME_ZZPerson_ID), null);

			savedDataQuery.setParameters(person.getZZPerson_ID());
			savedDataQuery.setOnlyActiveRecords(true);

			learnerSaved = savedDataQuery.firstOnly();

			firstNameCol.setDefaultValue(person.getZZFirstName());
		} else {
			daoManage.resetDao(X_ZZPerson.Table_Name);
		}

		if (learnerSaved != null) {
			daoManage.setDao(learnerSaved);
		} else {
			daoManage.resetDao(X_ZZLearner.Table_Name);
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
		populateDisabilityFromPerson();
	}

	private void initForm() {
		tmNames = initTbName();
		initGeneralDetail();
		initContactDetail();
		// Health Functions Values tab removed as per client request
		// initHealthFunction();
		initAddresss();
		initParentDetails();
		initEducationDetail();
		initPostSchoolEducation();
		initExperientialLearning();
		// Employment History tab removed as per client request
		// initEmploymentHistory();
		initRegistrationTab();
		initUploadDocument();
		populateDisabilityFromPerson();
	}

	ColumnModel idNoCol;
	ListColumnModel<X_ZZ_AlternateIDType> alternateIDTypeCol;
	ColumnModel								dateOfBirthCol;
	ColumnModel								genderCol;
	ListColumnModel<ValueNamePair>			disabilityCol;

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
		alternateIDTypeCol.setTableName(X_ZZPerson.Table_Name);
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

		idNoCol = IDCellModel.getIDColumnModel().required().setTableName(X_ZZPerson.Table_Name).setReadonly(true);
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
				MasterUtil.getNameOfColTranslated(X_ZZPerson.Table_Name, X_ZZPerson.COLUMNNAME_Birthday),
				X_ZZPerson.COLUMNNAME_Birthday).required().setTableName(X_ZZPerson.Table_Name);
		cols.add(dateOfBirthCol);

		genderCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(X_ZZPerson.Table_Name, X_ZZPerson.COLUMNNAME_ZZGender),
				X_ZZPerson.COLUMNNAME_ZZGender, MasterUtil.getLkpGenders(), title -> {
					return title.getName();
				}, title -> {
					return title.getValue();
				}).setzClass(ValueNamePair.class).required();
		cols.add(genderCol);

		ColumnModel equityCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(X_ZZPerson.Table_Name, X_ZZPerson.COLUMNNAME_ZZEquity),
				X_ZZPerson.COLUMNNAME_ZZEquity, MasterUtil.getLkpEquity(), title -> {
					return title.toString();
				}, title -> {
					return title.getValue();
				}).setzClass(ValueNamePair.class).required();
		cols.add(equityCol);

		ColumnModel homeLanguageCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(X_ZZPerson.Table_Name, X_ZZPerson.COLUMNNAME_ZZ_LI_HomeLanguage_ID),
				X_ZZPerson.COLUMNNAME_ZZ_LI_HomeLanguage_ID, MasterUtil.getHomeLanguage(), title -> {
					return title.getName();
				}, title -> {
					return title.getZZ_LI_HomeLanguage_ID();
				}).setzClass(X_ZZ_LI_HomeLanguage.class).setUseForID(true).required();
		cols.add(homeLanguageCol);

		ColumnModel nationalityCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(X_ZZPerson.Table_Name, X_ZZPerson.COLUMNNAME_ZZ_Nationality_ID),
				X_ZZPerson.COLUMNNAME_ZZ_Nationality_ID, MasterUtil.getNationality(), title -> {
					return title.getName();
				}, title -> {
					return title.getZZ_Nationality_ID();
				}).setzClass(X_ZZ_Nationality.class).setUseForID(true).required();
		cols.add(nationalityCol);

		ColumnModel citizenResidentialStatusCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(X_ZZPerson.Table_Name,
						X_ZZPerson.COLUMNNAME_ZZ_LI_CitizenResidentialStatus_ID),
				X_ZZPerson.COLUMNNAME_ZZ_LI_CitizenResidentialStatus_ID, MasterUtil.getCitizenResidentialStatus(),
				title -> {
					return title.getName();
				}, title -> {
					return title.getZZ_LI_CitizenResidentialStatus_ID();
				}).setzClass(X_ZZ_LI_CitizenResidentialStatus.class).setUseForID(true).required();
		cols.add(citizenResidentialStatusCol);

		ColumnModel socioEconomicStatusCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(X_ZZPerson.Table_Name,
						X_ZZPerson.COLUMNNAME_ZZ_LI_SocioEconomicStatus_ID),
				X_ZZPerson.COLUMNNAME_ZZ_LI_SocioEconomicStatus_ID, MasterUtil.getSocioEconomicStatus(), title -> {
					return title.getName();
				}, title -> {
					return title.getZZ_LI_SocioEconomicStatus_ID();
				}).setzClass(X_ZZ_LI_SocioEconomicStatus.class).setUseForID(true).required();
		cols.add(socioEconomicStatusCol);

		List<ValueNamePair> yesNoDisability = List.of(
				new ValueNamePair("Y", "Yes"),
				new ValueNamePair("N", "No"));
		disabilityCol = ListCellModel.getListColumnModel(
				"Disability",
				null,
				yesNoDisability,
				ValueNamePair::getName,
				ValueNamePair::getValue);
		disabilityCol.setzClass(ValueNamePair.class).required();
		disabilityCol.setDefaultValue(new ValueNamePair("N", "No"), (cell, item) -> "N".equals(item.getValue()));
		disabilityCol.setEventHandle((event, cellModel) -> {
			updateDisabilityLetterMandatoryState();
		});
		cols.add(disabilityCol);

		tmGeneralDetail = TableModel.getTableBean(TableModel.class, cols, false, X_ZZPerson.Table_Name);
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
		greettingCol.setTableName(X_ZZPerson.Table_Name);
		cols.add(greettingCol);

		firstNameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(X_ZZPerson.Table_Name, X_ZZPerson.COLUMNNAME_ZZFirstName),
				X_ZZPerson.COLUMNNAME_ZZFirstName).required();

		cols.add(firstNameCol);

		ColumnModel midNameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(X_ZZPerson.Table_Name, X_ZZPerson.COLUMNNAME_ZZMiddleName),
				X_ZZPerson.COLUMNNAME_ZZMiddleName);
		cols.add(midNameCol);

		ColumnModel surnameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(X_ZZPerson.Table_Name, X_ZZPerson.COLUMNNAME_Surname),
				X_ZZPerson.COLUMNNAME_Surname).required();
		cols.add(surnameCol);

		TableModel tmNames = TableModel.getTableBean(TableModel.class, cols, false, X_ZZPerson.Table_Name);
		tmNames.setSclass("srd-name srd-name-assessor");
		tmNames.setDaoManage(daoManage);
		tmNames.init();

		return tmNames;
	}

	private void initContactDetail() {
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel cellPhoneNumberCol = CellModel.getColModelForPhone(
				MasterUtil.getNameOfColTranslated(X_ZZPerson.Table_Name, X_ZZPerson.COLUMNNAME_Phone),
				X_ZZPerson.COLUMNNAME_Phone).required();
		cols.add(cellPhoneNumberCol);

		ColumnModel telephoneNumberCol = CellModel.getColModelForPhone(
				MasterUtil.getNameOfColTranslated(X_ZZPerson.Table_Name, X_ZZPerson.COLUMNNAME_Phone2),
				X_ZZPerson.COLUMNNAME_Phone2);
		cols.add(telephoneNumberCol);

		ColumnModel emailCol = CellModel.getColModelForEmail(
				MasterUtil.getNameOfColTranslated(X_ZZPerson.Table_Name, X_ZZPerson.COLUMNNAME_EMail),
				X_ZZPerson.COLUMNNAME_EMail).required();
		cols.add(emailCol);

		TableModel tmContactDetail = TableModel.getTableBean(TableModel.class, cols, false, X_ZZPerson.Table_Name);
		tmContactDetail.setSclass("srd-contact srd-contact-learner");
		tmContactDetail.setDaoManage(daoManage);
		tmContactDetail.init();

		NavTabPanel contactDetailTab = new NavTabPanel(mainTab);
		contactDetailTab.setTabTitle("Contact Details");
		contactDetailTab.getCompModel().add(tmContactDetail);

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
			MLocation location = tmAddress.getRow().getDataOneRow(MLocation.class, MLocation.Table_Name);
			if (location != null) {
				person.setZZPhysicalLocation_ID(location.getC_Location_ID());
				person.saveEx(trxName);
			}
			return true;
		});

		tmPostalAddress.setAfterAppSave((tableModel, trxName) -> {
			TableModel tmAddress = (TableModel) tableModel;
			MLocation location = tmAddress.getRow().getDataOneRow(MLocation.class, MLocation.Table_Name);
			if (location != null) {
				person.setZZPostalLocation_ID(location.getC_Location_ID());
				person.saveEx(trxName);
			}
			return true;
		});

		tmPhysicalAddress.setLoadSavedDataHandle(tm -> {
			if (person != null && person.getZZPhysicalLocation_ID() > 0) {
				MLocation physicalLocation = MLocation.getCopy(Env.getCtx(),
						person.getZZPhysicalLocation_ID(), null);
				tm.getRow().setDataOneRow(physicalLocation);
			} else {
				tm.getRow().setDataOneRow(null);
			}
			tm.reloadDao();
		});

		tmPostalAddress.setLoadSavedDataHandle(tm -> {
			if (person != null && person.getZZPostalLocation_ID() > 0) {
				MLocation postalLocation = MLocation.getCopy(Env.getCtx(),
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

	public boolean isRegistrationTabActive() {
		if (registrationTabPanel != null && mainTab != null && mainTab.getTabPanelModel() != null) {
			java.util.Set<NavTabPanel> selection = mainTab.getTabPanelModel().getSelection();
			if (selection != null && !selection.isEmpty()) {
				NavTabPanel active = selection.iterator().next();
				if (active != null) {
					return active == registrationTabPanel || "Registration".equalsIgnoreCase(active.getTabTitle());
				}
			}
			return !registrationTabPanel.isDisable();
		}
		return false;
	}

	@Override
	public String getBtNextLabel(NavTab navTab) {
		if (isRegistrationTabActive()) {
			return "Download Agreement & Next";
		}
		return "Next";
	}

	@Override
	public String getBtNextIcon(NavTab navTab) {
		if (isRegistrationTabActive()) {
			return "z-icon-file-pdf";
		}
		return "z-icon-arrow-right";
	}

	@Override
	public boolean onNextTab(NavTab navTab) {
		if (isRegistrationTabActive()) {
			if (tmRegistration != null && !tmRegistration.validate(null)) {
				return true;
			}
			boolean downloaded = downloadLearnerAgreement();
			if (downloaded && navTab != null) {
				navTab.doNextTab();
				BindUtils.postNotifyChange(null, null, this, "registrationTabActive");
			}
			return true;
		}
		return false;
	}

	@Override
	public void doSave(String trxName) {
		if (learner == null) {
			learner = (X_ZZLearner) daoManage.getDaoForSave(X_ZZLearner.Table_Name);
		}
		if (person == null) {
			person = (X_ZZPerson) daoManage.getDaoForSave(X_ZZPerson.Table_Name);
		}

		super.doSave(trxName);

		boolean disabilitySelected = isDisabilitySelected();
		if (disabilitySelected) {
			if (person.getZZ_LI_Disability_ID() <= 1000000) {
				person.setZZ_LI_Disability_ID(1000008); // Disabled but unspecified
			}
		} else {
			person.setZZ_LI_Disability_ID(1000000); // None
		}
		person.saveEx(trxName);

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
		int currentUserId = Env.getAD_User_ID(Env.getCtx());
		if (currentUserId > 0) {
			learner.setZZ_SDPAdmin_ID(currentUserId);
		}
		learner.saveEx(trxName);

		saveChildTables(trxName, learner.getZZLearner_ID());
		saveRegistrationRecord(trxName, learner.getZZLearner_ID());
	}

	private void saveChildTables(String trxName, int learnerId) {
		// 1. Parent/Guardian Details
		if (tmParentDetails != null && tmParentDetails.getRow() != null) {
			X_ZZ_ParentDetails pd = tmParentDetails.getRow().getDataOneRow(X_ZZ_ParentDetails.class, X_ZZ_ParentDetails.Table_Name);
			boolean hasPd = pd != null && (pd.getZZParentPerson_ID() > 0
					|| (pd.getZZFirstName() != null && !pd.getZZFirstName().isBlank())
					|| (pd.getZZMiddleName() != null && !pd.getZZMiddleName().isBlank())
					|| (pd.getSurname() != null && !pd.getSurname().isBlank())
					|| (pd.getTitle() != null && !pd.getTitle().isBlank()));
			if (hasPd) {
				pd.setZZLearner_ID(learnerId);
				pd.saveEx(trxName);
			}
		}

		// 2. Post School Educational Details
		if (tmPostSchoolEducation != null && tmPostSchoolEducation.getRow() != null) {
			X_ZZ_PostSchoolEducation_Details ps = tmPostSchoolEducation.getRow()
					.getDataOneRow(X_ZZ_PostSchoolEducation_Details.class, X_ZZ_PostSchoolEducation_Details.Table_Name);
			boolean hasPs = ps != null && ((ps.getQualification() != null && !ps.getQualification().isBlank())
					|| (ps.getName() != null && !ps.getName().isBlank())
					|| ps.getZZ_DateAchieved() != null
					|| ps.getOFO_Occupation_ID() > 0
					|| (ps.getDescription() != null && !ps.getDescription().isBlank()));
			if (hasPs) {
				ps.setZZLearner_ID(learnerId);
				if (ps.getName() == null || ps.getName().isBlank()) {
					String nm = ps.getQualification() != null && !ps.getQualification().isBlank()
							? ps.getQualification() : "Post School Education";
					if (nm.length() > 60) {
						nm = nm.substring(0, 60);
					}
					ps.setName(nm);
				} else if (ps.getName().length() > 60) {
					ps.setName(ps.getName().substring(0, 60));
				}
				ps.saveEx(trxName);
			}
		}

		// 3. Experiential Learning
		if (tmExperientialLearning != null && tmExperientialLearning.getRow() != null) {
			X_ZZ_ExperientialLearning exp = tmExperientialLearning.getRow()
					.getDataOneRow(X_ZZ_ExperientialLearning.class, X_ZZ_ExperientialLearning.Table_Name);
			if (tmContactableReference != null && tmContactableReference.getRow() != null) {
				X_ZZ_ExperientialLearning ref = tmContactableReference.getRow()
						.getDataOneRow(X_ZZ_ExperientialLearning.class, X_ZZ_ExperientialLearning.Table_Name);
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
					String nm = exp.getZZ_NameWork() != null && !exp.getZZ_NameWork().isBlank()
							? exp.getZZ_NameWork() : "Experiential Learning";
					if (nm.length() > 60) {
						nm = nm.substring(0, 60);
					}
					exp.setName(nm);
				} else if (exp.getName().length() > 60) {
					exp.setName(exp.getName().substring(0, 60));
				}
				exp.saveEx(trxName);
			}
		}

		// 4. Employment History
		if (tmEmploymentHistory != null && tmEmploymentHistory.getRow() != null) {
			X_ZZ_EmploymentHistory eh = tmEmploymentHistory.getRow()
					.getDataOneRow(X_ZZ_EmploymentHistory.class, X_ZZ_EmploymentHistory.Table_Name);
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

		parentTitleCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(X_ZZ_ParentDetails.Table_Name,
						X_ZZ_ParentDetails.COLUMNNAME_Title),
				X_ZZ_ParentDetails.COLUMNNAME_Title,
				MasterUtil.getLkpTitleLists(),
				title -> title.getName(),
				title -> title.getValue()
		).setzClass(ValueNamePair.class);

		parentPersonCol = ValueAdaptCellModel.getValueAdaptColumnModel(
				"ID/Passport No.",
				X_ZZ_ParentDetails.COLUMNNAME_ZZParentPerson_ID,
				CellModel.SEARCH_CELL);
		parentPersonCol.setBtText("");

		parentPersonCol.setEventHandle((event, cellModel) -> {
			showInfoPanel(
				InfoPanelPara.getInstance(X_ZZPerson.Table_Name, X_ZZPerson.COLUMNNAME_ZZPerson_ID),
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
						BindUtils.postNotifyChange(null, null, rm.get(parentFirstNameCol), "value");
					}
					if (rm.get(parentMiddleNameCol) != null) {
						rm.get(parentMiddleNameCol).setValue(selected.getZZMiddleName());
						BindUtils.postNotifyChange(null, null, rm.get(parentMiddleNameCol), "value");
					}
					if (rm.get(parentSurnameCol) != null) {
						rm.get(parentSurnameCol).setValue(selected.getSurname());
						BindUtils.postNotifyChange(null, null, rm.get(parentSurnameCol), "value");
					}
					if (rm.get(parentTitleCol) != null) {
						String title = selected.getZZLkpTitle();
						if (title != null && !title.isBlank()) {
							ValueNamePair titlePair = MasterUtil.getLkpTitleLists().stream()
									.filter(p -> p.getValue().equalsIgnoreCase(title) || p.getName().equalsIgnoreCase(title))
									.findFirst().orElse(null);
							if (titlePair != null) {
								rm.get(parentTitleCol).setValue(titlePair);
								BindUtils.postNotifyChange(null, null, rm.get(parentTitleCol), "value");
								BindUtils.postNotifyChange(null, null, rm.get(parentTitleCol), "selectedItem");
							}
						}
					}
					updateParentFieldsReadonly(true);
				});
		});

		parentPersonCol.setDisplayAdaptHandle(value -> {
			if (value == null)
				return null;
			X_ZZPerson p = (X_ZZPerson) value;
			if (p.getZZ_ID_Passport_No() != null && !p.getZZ_ID_Passport_No().isBlank()) {
				return p.getZZ_ID_Passport_No();
			}
			if (p.getZZOtherIDNo() != null && !p.getZZOtherIDNo().isBlank()) {
				return p.getZZOtherIDNo();
			}
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

		// 1. ID/Passport No. first
		cols.add(parentPersonCol);

		// 2. Title second
		cols.add(parentTitleCol);

		parentFirstNameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(X_ZZ_ParentDetails.Table_Name,
						X_ZZ_ParentDetails.COLUMNNAME_ZZFirstName),
				X_ZZ_ParentDetails.COLUMNNAME_ZZFirstName);
		cols.add(parentFirstNameCol);

		parentMiddleNameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(X_ZZ_ParentDetails.Table_Name,
						X_ZZ_ParentDetails.COLUMNNAME_ZZMiddleName),
				X_ZZ_ParentDetails.COLUMNNAME_ZZMiddleName);
		cols.add(parentMiddleNameCol);

		parentSurnameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(X_ZZ_ParentDetails.Table_Name,
						X_ZZ_ParentDetails.COLUMNNAME_Surname),
				X_ZZ_ParentDetails.COLUMNNAME_Surname);
		cols.add(parentSurnameCol);

		tmParentDetails = TableModel.getTableBean(TableModel.class, cols, false, X_ZZ_ParentDetails.Table_Name);
		tmParentDetails.setSclass("srd-parent-details");
		tmParentDetails.setRowSaveFilter(row -> false);
		tmParentDetails.init();

		tmParentDetails.setLoadSavedDataHandle(tm -> {
			if (learner != null && learner.getZZLearner_ID() > 0) {
				X_ZZ_ParentDetails pd = new Query(Env.getCtx(), X_ZZ_ParentDetails.Table_Name, "ZZLearner_ID=?", null)
						.setParameters(learner.getZZLearner_ID())
						.setOnlyActiveRecords(true)
						.firstOnly();
				tm.getRow().setDataOneRow(pd);
				boolean hasPerson = pd != null && pd.getZZParentPerson_ID() > 0;
				updateParentFieldsReadonly(hasPerson);
			} else {
				tm.getRow().setDataOneRow(null);
				updateParentFieldsReadonly(false);
			}
			tm.reloadDao();
		});

		NavTabPanel tabPanelParentDetails = new NavTabPanel(mainTab);
		tabPanelParentDetails.setTabTitle("Parent/Guardian Details");
		tabPanelParentDetails.getCompModel().add(tmParentDetails);
	}

	private void updateParentFieldsReadonly(boolean isReadonly) {
		if (parentTitleCol != null) {
			parentTitleCol.setReadonly(isReadonly);
		}
		if (parentFirstNameCol != null) {
			parentFirstNameCol.setReadonly(isReadonly);
		}
		if (parentMiddleNameCol != null) {
			parentMiddleNameCol.setReadonly(isReadonly);
		}
		if (parentSurnameCol != null) {
			parentSurnameCol.setReadonly(isReadonly);
		}
	}

	private void initEducationDetail() {
		List<ColumnModel> cols = new ArrayList<>();

		ValueAdaptColumnModel lastSchoolEmisCol = ValueAdaptCellModel.getValueAdaptColumnModel(
				Msg.getElement(Env.getCtx(), "ZZLastSchoolEmis"), X_ZZPerson.COLUMNNAME_ZZLkpSchoolEmis_ID,
				CellModel.SEARCH_CELL);
		lastSchoolEmisCol.setBtText("");
		lastSchoolEmisCol.required();

		lastSchoolEmisCol.setEventHandle((event, cellModel) -> {
			showInfoPanel(
				InfoPanelPara.getInstance(X_ZZLkpSchoolEmis.Table_Name, X_ZZLkpSchoolEmis.COLUMNNAME_ZZLkpSchoolEmis_ID),
				(obj, infoPanel) -> {
					Object[] objs = (Object[]) obj;
					if (objs == null || objs.length == 0 || objs[0] == null) {
						return;
					}
					int id = objs[0] instanceof Number ? ((Number) objs[0]).intValue() : Integer.parseInt(objs[0].toString());
					X_ZZLkpSchoolEmis selected = new X_ZZLkpSchoolEmis(Env.getCtx(), id, null);
					cellModel.setValue(selected);
				});
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
				MasterUtil.getNameOfColTranslated(X_ZZPerson.Table_Name, X_ZZPerson.COLUMNNAME_ZZLastSchoolYear),
				X_ZZPerson.COLUMNNAME_ZZLastSchoolYear).required();
		cols.add(lastSchoolYearCol);

		ValueAdaptColumnModel areaCodeCol = ValueAdaptCellModel.getValueAdaptColumnModel(
				MasterUtil.getNameOfColTranslated(X_ZZPerson.Table_Name, X_ZZPerson.COLUMNNAME_ZZLkpStatssaAreaCode_ID),
				X_ZZPerson.COLUMNNAME_ZZLkpStatssaAreaCode_ID, CellModel.SEARCH_CELL);
		areaCodeCol.setBtText("");
		areaCodeCol.required();

		areaCodeCol.setEventHandle((event, cellModel) -> {
			showInfoPanel(
				InfoPanelPara.getInstance(X_ZZLkpStatssaAreaCode.Table_Name, X_ZZLkpStatssaAreaCode.COLUMNNAME_ZZLkpStatssaAreaCode_ID),
				(obj, infoPanel) -> {
					Object[] objs = (Object[]) obj;
					if (objs == null || objs.length == 0 || objs[0] == null) {
						return;
					}
					int id = objs[0] instanceof Number ? ((Number) objs[0]).intValue() : Integer.parseInt(objs[0].toString());
					X_ZZLkpStatssaAreaCode selected = new X_ZZLkpStatssaAreaCode(Env.getCtx(), id, null);
					cellModel.setValue(selected);
				});
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
				MasterUtil.getNameOfColTranslated(X_ZZPerson.Table_Name, X_ZZPerson.COLUMNNAME_ZZPopiActStatus),
				X_ZZPerson.COLUMNNAME_ZZPopiActStatus, MasterUtil.getPopiActStatus(), title -> {
					return title.getName();
				}, title -> {
					return title.getValue();
				}).setzClass(ValueNamePair.class).required();
		cols.add(popiActStatusCol);

		ColumnModel popiActStatusDateCol = DateCellModel
				.getDateColumnModel(MasterUtil.getNameOfColTranslated(X_ZZPerson.Table_Name,
						X_ZZPerson.COLUMNNAME_ZZPopiActStatusDate), X_ZZPerson.COLUMNNAME_ZZPopiActStatusDate)
				.required();
		cols.add(popiActStatusDateCol);

		tmEducationDetail = TableModel.getTableBean(TableModel.class, cols, false, X_ZZPerson.Table_Name);
		tmEducationDetail.setSclass("srd-education-detail srd-education-detail-learner");
		tmEducationDetail.setDaoManage(daoManage);
		tmEducationDetail.init();

		NavTabPanel tabPanelEducationDetail = new NavTabPanel(mainTab);
		tabPanelEducationDetail.setTabTitle("Education Details");
		tabPanelEducationDetail.getCompModel().add(tmEducationDetail);
	}

	private void initPostSchoolEducation() {
		List<ColumnModel> cols = new ArrayList<>();

		ValueAdaptColumnModel qualificationCol = ValueAdaptCellModel.getValueAdaptColumnModel(
				MasterUtil.getNameOfColTranslated(X_ZZ_PostSchoolEducation_Details.Table_Name,
						X_ZZ_PostSchoolEducation_Details.COLUMNNAME_Qualification),
				X_ZZ_PostSchoolEducation_Details.COLUMNNAME_Qualification,
				CellModel.SEARCH_CELL);
		qualificationCol.setBtText("");

		qualificationCol.setEventHandle((event, cellModel) -> {
			showInfoPanel(
				InfoPanelPara.getInstance(X_ZZQualification_v.Table_Name, X_ZZQualification_v.COLUMNNAME_ZZQualification_v_UU),
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
				X_ZZ_PostSchoolEducation_Details.COLUMNNAME_Name).setReadonly(true);
		cols.add(qualificationNameCol);

		ColumnModel dateAchievedCol = DateCellModel.getDateColumnModel(
				MasterUtil.getNameOfColTranslated(X_ZZ_PostSchoolEducation_Details.Table_Name,
						X_ZZ_PostSchoolEducation_Details.COLUMNNAME_ZZ_DateAchieved),
				X_ZZ_PostSchoolEducation_Details.COLUMNNAME_ZZ_DateAchieved);
		cols.add(dateAchievedCol);

		ValueAdaptColumnModel linkedOfoCodeCol = ValueAdaptCellModel.getValueAdaptColumnModel(
				"Linked OFO Code",
				X_ZZ_PostSchoolEducation_Details.COLUMNNAME_OFO_Occupation_ID,
				CellModel.SEARCH_CELL);
		linkedOfoCodeCol.setBtText("");

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
				X_ZZ_PostSchoolEducation_Details.COLUMNNAME_Description).setReadonly(true);
		cols.add(linkedOfoDescCol);

		tmPostSchoolEducation = TableModel.getTableBean(TableModel.class, cols, false,
				X_ZZ_PostSchoolEducation_Details.Table_Name);
		tmPostSchoolEducation.setSclass("two-col srd-post-school-education");
		tmPostSchoolEducation.setRowSaveFilter(row -> false);
		tmPostSchoolEducation.init();

		tmPostSchoolEducation.setLoadSavedDataHandle(tm -> {
			if (learner != null && learner.getZZLearner_ID() > 0) {
				X_ZZ_PostSchoolEducation_Details ps = new Query(Env.getCtx(),
						X_ZZ_PostSchoolEducation_Details.Table_Name, "ZZLearner_ID=?", null)
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
				MasterUtil.getNameOfColTranslated(X_ZZ_ExperientialLearning.Table_Name,
						X_ZZ_ExperientialLearning.COLUMNNAME_ZZ_ExperianceWork),
				X_ZZ_ExperientialLearning.COLUMNNAME_ZZ_ExperianceWork);
		expCols.add(experienceWorkCol);

		ColumnModel expDateFromCol = DateCellModel.getDateColumnModel(
				MasterUtil.getNameOfColTranslated(X_ZZ_ExperientialLearning.Table_Name,
						X_ZZ_ExperientialLearning.COLUMNNAME_DateFrom),
				X_ZZ_ExperientialLearning.COLUMNNAME_DateFrom);
		expCols.add(expDateFromCol);

		ColumnModel expDateToCol = DateCellModel.getDateColumnModel(
				MasterUtil.getNameOfColTranslated(X_ZZ_ExperientialLearning.Table_Name,
						X_ZZ_ExperientialLearning.COLUMNNAME_DateTo),
				X_ZZ_ExperientialLearning.COLUMNNAME_DateTo);
		expCols.add(expDateToCol);

		ColumnModel natureOfWorkCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(X_ZZ_ExperientialLearning.Table_Name,
						X_ZZ_ExperientialLearning.COLUMNNAME_ZZ_NameWork),
				X_ZZ_ExperientialLearning.COLUMNNAME_ZZ_NameWork);
		expCols.add(natureOfWorkCol);

		tmExperientialLearning = TableModel.getTableBean(TableModel.class, expCols, false,
				X_ZZ_ExperientialLearning.Table_Name);
		tmExperientialLearning.setSubSectionHeader("Experiential Learning");
		tmExperientialLearning.setSclass("two-col srd-experiential-learning");
		tmExperientialLearning.setRowSaveFilter(row -> false);
		tmExperientialLearning.init();

		// Section 2: Contactable Reference:
		List<ColumnModel> refCols = new ArrayList<>();

		ColumnModel refTitleCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(X_ZZ_ExperientialLearning.Table_Name,
						X_ZZ_ExperientialLearning.COLUMNNAME_Title),
				X_ZZ_ExperientialLearning.COLUMNNAME_Title,
				MasterUtil.getLkpTitleLists(),
				title -> title.getName(),
				title -> title.getValue()
		).setzClass(ValueNamePair.class);
		refCols.add(refTitleCol);

		ColumnModel refNameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(X_ZZ_ExperientialLearning.Table_Name,
						X_ZZ_ExperientialLearning.COLUMNNAME_Name),
				X_ZZ_ExperientialLearning.COLUMNNAME_Name);
		refCols.add(refNameCol);

		tmContactableReference = TableModel.getTableBean(TableModel.class, refCols, false,
				X_ZZ_ExperientialLearning.Table_Name);
		tmContactableReference.setSubSectionHeader("Contactable Reference:");
		tmContactableReference.setSclass("two-col srd-contactable-reference");
		tmContactableReference.setRowSaveFilter(row -> false);
		tmContactableReference.init();

		tmExperientialLearning.setLoadSavedDataHandle(tm -> {
			if (learner != null && learner.getZZLearner_ID() > 0) {
				X_ZZ_ExperientialLearning exp = new Query(Env.getCtx(),
						X_ZZ_ExperientialLearning.Table_Name, "ZZLearner_ID=?", null)
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
						X_ZZ_ExperientialLearning.Table_Name, "ZZLearner_ID=?", null)
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

	private void setupPhase2Tabs() {
		initRegistrationTab();
		initUploadDocument();
		BindUtils.postNotifyChange(null, null, this, "tmRegistration");
		BindUtils.postNotifyChange(null, null, this, "tmDocumentUpload");
		if (mainTab != null) {
			BindUtils.postNotifyChange(null, null, mainTab, "tabPanelModel");
		}
	}

	private void loadExistingRegistration(int learnerId, int pmdId) {
		if (learnerId <= 0) return;

		X_ZZLearnerDGProgramme dg = null;
		if (pmdId > 0) {
			dg = new Query(Env.getCtx(), X_ZZLearnerDGProgramme.Table_Name, "ZZLearner_ID = ? AND ZZ_Program_Master_Data_ID = ?", null)
					.setParameters(learnerId, pmdId)
					.setOnlyActiveRecords(true)
					.setOrderBy("ZZLearnerDGProgramme_ID DESC")
					.first();
		}
		if (dg == null) {
			dg = new Query(Env.getCtx(), X_ZZLearnerDGProgramme.Table_Name, "ZZLearner_ID = ?", null)
					.setParameters(learnerId)
					.setOnlyActiveRecords(true)
					.setOrderBy("ZZLearnerDGProgramme_ID DESC")
					.first();
		}
		if (dg != null) {
			targetRegistrationPo = dg;
			daoManage.setDao(dg);
			if (selectedDGProgramme == null && dg.getZZ_Program_Master_Data_ID() > 0) {
				loadDGProgramme(dg.getZZ_Program_Master_Data_ID());
			}
		} else {
			targetRegistrationPo = null;
		}

		targetChildPo = null;
		for (InterventionConfig cfg : LearnerQualficationRegVM.INTERVENTION_CONFIGS) {
			PO reg = null;
			if (selectedQualification != null && cfg.learnerProgrammeColumn != null && !cfg.learnerProgrammeColumn.isBlank()) {
				reg = new Query(Env.getCtx(), cfg.learnerTable, "ZZLearner_ID = ? AND " + cfg.learnerProgrammeColumn + " = ?", null)
						.setParameters(learnerId, selectedQualification.get_ID())
						.setOnlyActiveRecords(true)
						.setOrderBy(cfg.learnerTable + "_ID DESC")
						.first();
			}
			if (reg == null && pmdId > 0 && MTable.get(Env.getCtx(), cfg.learnerTable).getColumn("ZZ_Program_Master_Data_ID") != null) {
				reg = new Query(Env.getCtx(), cfg.learnerTable, "ZZLearner_ID = ? AND ZZ_Program_Master_Data_ID = ?", null)
						.setParameters(learnerId, pmdId)
						.setOnlyActiveRecords(true)
						.setOrderBy(cfg.learnerTable + "_ID DESC")
						.first();
			}
			if (reg == null && selectedInterventionType != null && selectedInterventionType.equalsIgnoreCase(cfg.displayName)) {
				reg = new Query(Env.getCtx(), cfg.learnerTable, "ZZLearner_ID = ?", null)
						.setParameters(learnerId)
						.setOnlyActiveRecords(true)
						.setOrderBy(cfg.learnerTable + "_ID DESC")
						.first();
			}
			if (reg != null) {
				targetChildPo = reg;
				if (selectedInterventionType == null || selectedInterventionType.isBlank()) {
					selectedInterventionType = cfg.displayName;
				}
				if (selectedQualification == null && cfg.learnerProgrammeColumn != null) {
					int progId = reg.get_ValueAsInt(cfg.learnerProgrammeColumn);
					if (progId > 0) {
						selectedQualification = cfg.programmeLoader.apply(progId);
						if (selectedQualification != null) {
							Object code = selectedQualification.get_Value(cfg.programmeCodeColumn);
							Object title = selectedQualification.get_Value(cfg.programmeTitleColumn);
							selectedQualificationTitle = (code != null ? code.toString() : "") + " - " + (title != null ? title.toString() : "");
						}
					}
				}
				break;
			}
		}
	}

	private InterventionConfig getInterventionConfig(String type) {
		if (INTERVENTION_ARTISANS.equalsIgnoreCase(type)) {
			return LearnerQualficationRegVM.getInterventionConfig("QCTO Artisans");
		}
		return LearnerQualficationRegVM.getInterventionConfig(type);
	}

	private String buildQualificationWhereClause(InterventionConfig config) {
		StringBuilder sql = new StringBuilder(config.programmeTable + ".IsActive = 'Y'");

		if (config.displayName.equals("QCTO Artisans") || INTERVENTION_ARTISANS.equalsIgnoreCase(selectedInterventionType)) {
			sql.append(" AND (" + config.programmeTable + ".ZZArtisanLearnership = 'Y' OR "
					+ config.programmeTable + ".ZZQctoQualification_ID IN (SELECT q.ZZQctoQualification_ID FROM ZZQctoQualification q WHERE q.ZZArtisanQualification = 'Y'))");
		}

		// Filter 1: SDP Accreditation only (ZZ_Status = 'AC' AND EndDate >= CURRENT_DATE)
		if (adminBpId > 0) {
			if (X_ZZLearnerQCTOArtisans.Table_Name.equals(config.learnerTable)) {
				sql.append(" AND ")
						.append(config.programmeTable)
						.append(".ZZQctoQualification_ID IN (SELECT t.ZZQctoQualification_ID FROM C_BP_Trades t WHERE t.C_BPartner_ID = ")
						.append(adminBpId)
						.append(" AND t.IsActive = 'Y' AND t.ZZ_Status = 'AC' AND (t.EndDate IS NULL OR t.EndDate >= CURRENT_DATE))");
			} else if (X_ZZLearnerQCTOLearnership.Table_Name.equals(config.learnerTable)) {
				sql.append(" AND ")
						.append(config.programmeTable)
						.append(".ZZQctoQualification_ID IN (SELECT bp.ZZQctoQualification_ID FROM C_BP_OC bp WHERE bp.C_BPartner_ID = ")
						.append(adminBpId)
						.append(" AND bp.IsActive = 'Y' AND bp.ZZ_Status = 'AC' AND (bp.EndDate IS NULL OR bp.EndDate >= CURRENT_DATE))");
			} else if (X_ZZLearnerLearnership.Table_Name.equals(config.learnerTable)) {
				sql.append(" AND ")
						.append(config.programmeTable)
						.append(".ZZQualification_ID IN (SELECT bp.ZZQualification_ID FROM C_BP_OC bp WHERE bp.C_BPartner_ID = ")
						.append(adminBpId)
						.append(" AND bp.IsActive = 'Y' AND bp.ZZ_Status = 'AC' AND (bp.EndDate IS NULL OR bp.EndDate >= CURRENT_DATE))");
			} else if (X_ZZLearnerQCTOSkillsProgramme.Table_Name.equals(config.learnerTable)) {
				sql.append(" AND ")
						.append(config.programmeTable)
						.append(".ZZQctoSkillsProgramme_ID IN (SELECT sp.ZZQctoSkillsProgramme_ID FROM C_BP_SkillsProgramme sp WHERE sp.C_BPartner_ID = ")
						.append(adminBpId)
						.append(" AND sp.IsActive = 'Y' AND sp.ZZ_Status = 'AC' AND (sp.EndDate IS NULL OR sp.EndDate >= CURRENT_DATE))");
			} else if (X_ZZLearnerSkillsProgramme.Table_Name.equals(config.learnerTable)) {
				sql.append(" AND ")
						.append(config.programmeTable)
						.append(".ZZSkillsProgramme_ID IN (SELECT sp.ZZSkillsProgramme_ID FROM C_BP_SkillsProgramme sp WHERE sp.C_BPartner_ID = ")
						.append(adminBpId)
						.append(" AND sp.IsActive = 'Y' AND sp.ZZ_Status = 'AC' AND (sp.EndDate IS NULL OR sp.EndDate >= CURRENT_DATE))");
			}
		}

		// Filter 2: Non-expired enrolment date (zzlastenrolmentdate >= CURRENT_DATE or isexpired != 'Y')
		MTable mTable = MTable.get(Env.getCtx(), config.programmeTable);
		boolean hasLastEnrolment = mTable != null ? mTable.getColumn("ZZLastEnrolmentDate") != null : true;
		boolean hasQctoQual = mTable != null ? mTable.getColumn("ZZQctoQualification_ID") != null : "ZZQctoLearnership".equals(config.programmeTable);
		boolean hasLegacyQual = mTable != null ? mTable.getColumn("ZZQualification_ID") != null : ("ZZLearnership".equals(config.programmeTable) || "ZZSkillsProgramme".equals(config.programmeTable));

		if (hasLastEnrolment) {
			sql.append(" AND (" + config.programmeTable + ".ZZLastEnrolmentDate IS NULL OR "
					+ config.programmeTable + ".ZZLastEnrolmentDate >= CURRENT_DATE)");
		}
		if (hasQctoQual) {
			sql.append(" AND (" + config.programmeTable + ".ZZQctoQualification_ID IS NULL OR "
					+ config.programmeTable + ".ZZQctoQualification_ID IN (SELECT q.ZZQctoQualification_ID FROM ZZQctoQualification q WHERE (q.ZZLastEnrolmentDate IS NULL OR q.ZZLastEnrolmentDate >= CURRENT_DATE) AND (q.RegistrationEndDate IS NULL OR q.RegistrationEndDate >= CURRENT_DATE)))");
		} else if (hasLegacyQual) {
			sql.append(" AND (" + config.programmeTable + ".ZZQualification_ID IS NULL OR "
					+ config.programmeTable + ".ZZQualification_ID IN (SELECT q.ZZQualification_ID FROM ZZQualification q WHERE (q.ZZLastEnrolmentDate IS NULL OR q.ZZLastEnrolmentDate >= CURRENT_DATE) AND (q.RegistrationEndDate IS NULL OR q.RegistrationEndDate >= CURRENT_DATE)))");
		}

		return sql.toString();
	}

	private void initRegistrationTab() {
		String tableName = X_ZZLearnerDGProgramme.Table_Name;
		List<ColumnModel> cols = buildDGProgrammeColumns();

		tmRegistration = TableModel.getTableBean(TableModel.class, cols, false, tableName);
		tmRegistration.setDaoManage(daoManage);
		tmRegistration.setRowSaveFilter(row -> false);
		tmRegistration.init();

		populateRegistrationDefaults();

		if (registrationTabPanel == null) {
			registrationTabPanel = new NavTabPanel(mainTab);
			registrationTabPanel.setTabTitle("Registration");
			registrationTabPanel.getCompModel().add(tmRegistration);
		} else {
			registrationTabPanel.getCompModel().clear();
			registrationTabPanel.getCompModel().add(tmRegistration);
		}
	}

	private List<ColumnModel> buildDGProgrammeColumns() {
		List<ColumnModel> cols = new ArrayList<>();
		String learnerTable = X_ZZLearnerDGProgramme.Table_Name;

		List<ValueNamePair> yesNoOptions = List.of(
				new ValueNamePair("N", "No"),
				new ValueNamePair("Y", "Yes"));

		// 1. Program Master Data (Read-only)
		progCol = CellModel.getColModelForLabel(
				MasterUtil.getNameOfColTranslated(learnerTable, I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_Program_Master_Data_ID))
				.setTableName(learnerTable).setReadonly(true);

		// 2. Financial Year (Required)
		ValueAdaptColumnModel colFinancialYear = ValueAdaptCellModel.getValueAdaptColumnModel(
				MasterUtil.getNameOfColTranslated(learnerTable, I_ZZLearnerDGProgramme.COLUMNNAME_ZZFinancialYear_ID),
				I_ZZLearnerDGProgramme.COLUMNNAME_ZZFinancialYear_ID, CellModel.SEARCH_CELL);
		colFinancialYear.setTableName(learnerTable).required();
		colFinancialYear.setBtText("");

		// 3. Funding Type (Required)
		MColumn fundingCol = MTable.get(Env.getCtx(), I_ZZLearnerDGProgramme.Table_Name)
				.getColumn(I_ZZLearnerDGProgramme.COLUMNNAME_ZZFundingType);
		int fundingRefId = fundingCol != null ? fundingCol.getAD_Reference_Value_ID() : 1000367;
		ValueNamePair[] fundingPairs = MRefList.getList(Env.getCtx(), fundingRefId, false);
		List<ValueNamePair> fundingTypeList = fundingPairs != null && fundingPairs.length > 0
				? Arrays.asList(fundingPairs)
				: List.of(
						new ValueNamePair(X_ZZLearnerDGProgramme.ZZFUNDINGTYPE_IndustryFunded, "Industry Funded"),
						new ValueNamePair(X_ZZLearnerDGProgramme.ZZFUNDINGTYPE_SETAFunded, "SETA Funded"));
		ListColumnModel<ValueNamePair> colFundingType = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(learnerTable, I_ZZLearnerDGProgramme.COLUMNNAME_ZZFundingType),
				I_ZZLearnerDGProgramme.COLUMNNAME_ZZFundingType, fundingTypeList, ValueNamePair::getName, ValueNamePair::getValue);
		colFundingType.setzClass(ValueNamePair.class).required();
		colFundingType.setTableName(learnerTable);

		// 4. Agreement Reference Number
		ColumnModel colRefNo = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(learnerTable, I_ZZLearnerDGProgramme.COLUMNNAME_ZZAgreementReferenceNumber),
				I_ZZLearnerDGProgramme.COLUMNNAME_ZZAgreementReferenceNumber);

		// 5. Contract Number
		ColumnModel colContractNo = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(learnerTable, I_ZZLearnerDGProgramme.COLUMNNAME_ZZContractNumber),
				I_ZZLearnerDGProgramme.COLUMNNAME_ZZContractNumber);

		// 6. Student Number
		ColumnModel colStudentNumber = text(learnerTable, I_ZZLearnerDGProgramme.COLUMNNAME_ZZStudentNumber);

		// 7. Start Date (Required)
		ColumnModel startDateCol = editableDate(learnerTable, I_ZZLearnerDGProgramme.COLUMNNAME_ZZCommencementDate).required();
		startDateCol.setTitle("Start Date");

		// 8. End Date (Required)
		ColumnModel endDateCol = editableDate(learnerTable, I_ZZLearnerDGProgramme.COLUMNNAME_ZZCompletionDate).required();
		endDateCol.setTitle("End Date");

		// --- Lead Skills Development Provider (SDP) ---
		ValueAdaptColumnModel colLeadSdp = ValueAdaptCellModel.getValueAdaptColumnModel(
				MasterUtil.getNameOfColTranslated(learnerTable, I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_SDP_ID),
				I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_SDP_ID, CellModel.SEARCH_CELL);
		colLeadSdp.setTableName(learnerTable).required();
		colLeadSdp.setBtText("");

		ValueAdaptColumnModel colLeadSdpContact = ValueAdaptCellModel.getValueAdaptColumnModel(
				MasterUtil.getNameOfColTranslated(learnerTable, I_ZZLearnerDGProgramme.COLUMNNAME_ZZLeadSDProviderContact_ID),
				I_ZZLearnerDGProgramme.COLUMNNAME_ZZLeadSDProviderContact_ID, CellModel.SEARCH_CELL);
		colLeadSdpContact.setTableName(learnerTable).required();
		colLeadSdpContact.setBtText("");

		ListColumnModel<ValueNamePair> colLeadSdpLevy = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(learnerTable, I_ZZLearnerDGProgramme.COLUMNNAME_ZZLeadSDProviderLevy),
				I_ZZLearnerDGProgramme.COLUMNNAME_ZZLeadSDProviderLevy, yesNoOptions, ValueNamePair::getName, ValueNamePair::getValue);
		colLeadSdpLevy.setzClass(ValueNamePair.class).required();
		colLeadSdpLevy.setTableName(learnerTable);

		// --- Lead Workplace Approval (Employer) ---
		ValueAdaptColumnModel colLeadEmployer = ValueAdaptCellModel.getValueAdaptColumnModel(
				MasterUtil.getNameOfColTranslated(learnerTable, I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_Employer_ID),
				I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_Employer_ID, CellModel.SEARCH_CELL);
		colLeadEmployer.setTableName(learnerTable).required();
		colLeadEmployer.setBtText("");

		ValueAdaptColumnModel colLeadEmployerContact = ValueAdaptCellModel.getValueAdaptColumnModel(
				MasterUtil.getNameOfColTranslated(learnerTable, I_ZZLearnerDGProgramme.COLUMNNAME_ZZWAContact_ID),
				I_ZZLearnerDGProgramme.COLUMNNAME_ZZWAContact_ID, CellModel.SEARCH_CELL);
		colLeadEmployerContact.setTableName(learnerTable).required();
		colLeadEmployerContact.setBtText("");

		ListColumnModel<ValueNamePair> colLeadEmployerLevy = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(learnerTable, I_ZZLearnerDGProgramme.COLUMNNAME_ZZWALevy),
				I_ZZLearnerDGProgramme.COLUMNNAME_ZZWALevy, yesNoOptions, ValueNamePair::getName, ValueNamePair::getValue);
		colLeadEmployerLevy.setzClass(ValueNamePair.class).required();
		colLeadEmployerLevy.setTableName(learnerTable);

		// --- Secondary Skills Development Provider (Optional) ---
		ValueAdaptColumnModel colSecondarySdp = ValueAdaptCellModel.getValueAdaptColumnModel(
				MasterUtil.getNameOfColTranslated(learnerTable, I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondarySDProvider_ID),
				I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondarySDProvider_ID, CellModel.SEARCH_CELL);
		colSecondarySdp.setTableName(learnerTable);
		colSecondarySdp.setBtText("");

		ValueAdaptColumnModel colSecondarySdpContact = ValueAdaptCellModel.getValueAdaptColumnModel(
				MasterUtil.getNameOfColTranslated(learnerTable, I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondarySDProviderContact_ID),
				I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondarySDProviderContact_ID, CellModel.SEARCH_CELL);
		colSecondarySdpContact.setTableName(learnerTable);
		colSecondarySdpContact.setBtText("");

		ListColumnModel<ValueNamePair> colSecondarySdpLevy = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(learnerTable, I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondarySDProviderLevy),
				I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondarySDProviderLevy, yesNoOptions, ValueNamePair::getName, ValueNamePair::getValue);
		colSecondarySdpLevy.setzClass(ValueNamePair.class);
		colSecondarySdpLevy.setTableName(learnerTable);

		// --- Secondary Workplace Approval (Optional) ---
		ValueAdaptColumnModel colSecondaryEmployer = ValueAdaptCellModel.getValueAdaptColumnModel(
				MasterUtil.getNameOfColTranslated(learnerTable, I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondaryWA_ID),
				I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondaryWA_ID, CellModel.SEARCH_CELL);
		colSecondaryEmployer.setTableName(learnerTable);
		colSecondaryEmployer.setBtText("");

		ValueAdaptColumnModel colSecondaryEmployerContact = ValueAdaptCellModel.getValueAdaptColumnModel(
				MasterUtil.getNameOfColTranslated(learnerTable, I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondaryWAContact_ID),
				I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondaryWAContact_ID, CellModel.SEARCH_CELL);
		colSecondaryEmployerContact.setTableName(learnerTable);
		colSecondaryEmployerContact.setBtText("");

		ListColumnModel<ValueNamePair> colSecondaryEmployerLevy = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(learnerTable, I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondaryWALevy),
				I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondaryWALevy, yesNoOptions, ValueNamePair::getName, ValueNamePair::getValue);
		colSecondaryEmployerLevy.setzClass(ValueNamePair.class);
		colSecondaryEmployerLevy.setTableName(learnerTable);

		// Grouped 3-column rows
		// Row 1: Programme & Funding
		cols.add(progCol);
		cols.add(colFinancialYear);
		cols.add(colFundingType);

		// Row 2: Agreement References & Student
		cols.add(colRefNo);
		cols.add(colContractNo);
		cols.add(colStudentNumber);

		// Row 3: Lead Skills Development Provider (SDP)
		cols.add(colLeadSdp);
		cols.add(colLeadSdpContact);
		cols.add(colLeadSdpLevy);

		// Row 4: Lead Workplace Approval (Employer)
		cols.add(colLeadEmployer);
		cols.add(colLeadEmployerContact);
		cols.add(colLeadEmployerLevy);

		// Row 5: Secondary Skills Development Provider (Optional)
		cols.add(colSecondarySdp);
		cols.add(colSecondarySdpContact);
		cols.add(colSecondarySdpLevy);

		// Row 6: Secondary Workplace Approval (Optional)
		cols.add(colSecondaryEmployer);
		cols.add(colSecondaryEmployerContact);
		cols.add(colSecondaryEmployerLevy);

		// Row 7: Agreement Dates
		cols.add(startDateCol);
		cols.add(endDateCol);

		// Wire search handlers
		String sdpWhere = MBPartner.Table_Name + "." + MBPartner.COLUMNNAME_IsActive + " = 'Y' AND "
				+ MBPartner.Table_Name + ".ZZ_Is_SDP = 'Y'";
		String empWhere = MBPartner.Table_Name + "." + MBPartner.COLUMNNAME_IsActive + " = 'Y'";
		configureBPartnerSearch(colLeadSdp, sdpWhere, colLeadSdpContact);
		configureBPartnerSearch(colSecondarySdp, sdpWhere, colSecondarySdpContact);
		configureBPartnerSearch(colLeadEmployer, empWhere, colLeadEmployerContact);
		configureBPartnerSearch(colSecondaryEmployer, empWhere, colSecondaryEmployerContact);

		configureUserContactSearch(colLeadSdpContact, colLeadSdp);
		configureUserContactSearch(colSecondarySdpContact, colSecondarySdp);
		configureUserContactSearch(colLeadEmployerContact, colLeadEmployer);
		configureUserContactSearch(colSecondaryEmployerContact, colSecondaryEmployer);

		configureYearSearch(colFinancialYear);

		return cols;
	}

	private void populateRegistrationDefaults() {
		if (tmRegistration == null || tmRegistration.getRow() == null) return;
		RowModel row = tmRegistration.getRow();

		for (Map.Entry<ColumnModel, CellModel> entry : row.entrySet()) {
			ColumnModel col = entry.getKey();
			CellModel cell = entry.getValue();
			String prop = col.getDaoPropertyName();

			if ((col == progCol || I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_Program_Master_Data_ID.equals(prop))
					&& (selectedDGProgramme != null || (selectedDGProgrammeTitle != null && !selectedDGProgrammeTitle.isBlank()))) {
				cell.setValue(selectedDGProgrammeTitle);
			} else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_SDP_ID.equals(prop) && adminBpId > 0 && cell.getValue() == null) {
				MBPartner sdpBp = new MBPartner(Env.getCtx(), adminBpId, null);
				cell.setValue(sdpBp);
			} else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZLeadSDProviderContact_ID.equals(prop) && loginId > 0 && cell.getValue() == null) {
				MUser sdpUser = new MUser(Env.getCtx(), loginId, null);
				cell.setValue(sdpUser);
			}
		}

		X_ZZLearnerDGProgramme dg = (targetRegistrationPo instanceof X_ZZLearnerDGProgramme)
				? (X_ZZLearnerDGProgramme) targetRegistrationPo
				: null;
		PO child = targetChildPo;

		if (dg != null || child != null) {
			for (Map.Entry<ColumnModel, CellModel> entry : row.entrySet()) {
				ColumnModel col = entry.getKey();
				CellModel cell = entry.getValue();
				String prop = col.getDaoPropertyName();
				if (prop == null) continue;

				if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZAgreementReferenceNumber.equals(prop)) {
					String val = dg != null ? dg.getZZAgreementReferenceNumber() : null;
					if ((val == null || val.isBlank()) && child != null && child.get_ColumnIndex("ZZAgreementReferenceNumber") >= 0) {
						val = child.get_ValueAsString("ZZAgreementReferenceNumber");
					}
					if (val != null) cell.setValue(val);
				} else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_SDP_ID.equals(prop)) {
					int sdpId = dg != null ? dg.getZZ_SDP_ID() : 0;
					if (sdpId <= 0 && child != null && child.get_ColumnIndex("ZZ_SDP_ID") >= 0) {
						sdpId = child.get_ValueAsInt("ZZ_SDP_ID");
					}
					if (sdpId <= 0 && child != null && child.get_ColumnIndex("ZZSDProvider_ID") >= 0) {
						sdpId = child.get_ValueAsInt("ZZSDProvider_ID");
					}
					if (sdpId > 0) cell.setValue(new MBPartner(Env.getCtx(), sdpId, null));
				} else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_Employer_ID.equals(prop)) {
					int empId = dg != null ? dg.getZZ_Employer_ID() : 0;
					if (empId <= 0 && child != null && child.get_ColumnIndex("ZZ_Employer_ID") >= 0) {
						empId = child.get_ValueAsInt("ZZ_Employer_ID");
					}
					if (empId <= 0 && child != null && child.get_ColumnIndex("ZZWA_ID") >= 0) {
						empId = child.get_ValueAsInt("ZZWA_ID");
					}
					if (empId <= 0 && child != null && child.get_ColumnIndex("Employer_ID") >= 0) {
						empId = child.get_ValueAsInt("Employer_ID");
					}
					if (empId > 0) cell.setValue(new MBPartner(Env.getCtx(), empId, null));
				} else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZLeadSDProviderLevy.equals(prop)) {
					String val = dg != null ? dg.getZZLeadSDProviderLevy() : null;
					if (val != null) cell.setValue(val);
				} else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZWALevy.equals(prop)) {
					String val = dg != null ? dg.getZZWALevy() : null;
					if (val != null) cell.setValue(val);
				} else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZLeadSDProviderContact_ID.equals(prop)) {
					int id = dg != null ? dg.getZZLeadSDProviderContact_ID() : 0;
					if (id > 0) cell.setValue(new MUser(Env.getCtx(), id, null));
				} else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZWAContact_ID.equals(prop)) {
					int id = dg != null ? dg.getZZWAContact_ID() : 0;
					if (id > 0) cell.setValue(new MUser(Env.getCtx(), id, null));
				} else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondarySDProvider_ID.equals(prop)) {
					int id = dg != null ? dg.getZZSecondarySDProvider_ID() : 0;
					if (id > 0) cell.setValue(new MBPartner(Env.getCtx(), id, null));
				} else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondaryWA_ID.equals(prop)) {
					int id = dg != null ? dg.getZZSecondaryWA_ID() : 0;
					if (id > 0) cell.setValue(new MBPartner(Env.getCtx(), id, null));
				} else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondarySDProviderLevy.equals(prop)) {
					String val = dg != null ? dg.getZZSecondarySDProviderLevy() : null;
					if (val != null) cell.setValue(val);
				} else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondaryWALevy.equals(prop)) {
					String val = dg != null ? dg.getZZSecondaryWALevy() : null;
					if (val != null) cell.setValue(val);
				} else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondarySDProviderContact_ID.equals(prop)) {
					int id = dg != null ? dg.getZZSecondarySDProviderContact_ID() : 0;
					if (id > 0) cell.setValue(new MUser(Env.getCtx(), id, null));
				} else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondaryWAContact_ID.equals(prop)) {
					int id = dg != null ? dg.getZZSecondaryWAContact_ID() : 0;
					if (id > 0) cell.setValue(new MUser(Env.getCtx(), id, null));
				} else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZStudentNumber.equals(prop)) {
					String val = dg != null ? dg.getZZStudentNumber() : null;
					if ((val == null || val.isBlank()) && child != null && child.get_ColumnIndex("ZZStudentNumber") >= 0) {
						val = child.get_ValueAsString("ZZStudentNumber");
					}
					if (val != null) cell.setValue(val);
				} else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZFundingType.equals(prop)) {
					String ft = dg != null ? dg.getZZFundingType() : null;
					if ((ft == null || ft.isBlank()) && child != null && child.get_ColumnIndex("ZZFundingType") >= 0) {
						ft = child.get_ValueAsString("ZZFundingType");
					}
					if (ft != null && !ft.isBlank()) cell.setValue(ft);
				} else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZContractNumber.equals(prop)) {
					String val = dg != null ? dg.getZZContractNumber() : null;
					if ((val == null || val.isBlank()) && child != null && child.get_ColumnIndex("ZZContractNumber") >= 0) {
						val = child.get_ValueAsString("ZZContractNumber");
					}
					if (val != null) cell.setValue(val);
				} else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZFinancialYear_ID.equals(prop)) {
					int fyId = dg != null ? dg.getZZFinancialYear_ID() : 0;
					if (fyId <= 0 && child != null && child.get_ColumnIndex("ZZFinancialYear_ID") >= 0) {
						fyId = child.get_ValueAsInt("ZZFinancialYear_ID");
					}
					if (fyId > 0) cell.setValue(new MYear(Env.getCtx(), fyId, null));
				} else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZCommencementDate.equals(prop)) {
					Object dt = dg != null ? dg.getZZCommencementDate() : null;
					if (dt == null && child != null && child.get_ColumnIndex("ZZCommencementDate") >= 0) {
						dt = child.get_Value("ZZCommencementDate");
					}
					if (dt != null) cell.setValue(dt);
				} else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZCompletionDate.equals(prop)) {
					Object dt = dg != null ? dg.getZZCompletionDate() : null;
					if (dt == null && child != null && child.get_ColumnIndex("ZZCompletionDate") >= 0) {
						dt = child.get_Value("ZZCompletionDate");
					}
					if (dt != null) cell.setValue(dt);
				}
			}
		}

		for (CellModel cell : row.values()) {
			BindUtils.postNotifyChange(null, null, cell, "value");
			if (cell instanceof ValueAdaptCellModel) {
				BindUtils.postNotifyChange(null, null, cell, "displayValue");
			}
			if (cell instanceof ListCellModel) {
				BindUtils.postNotifyChange(null, null, cell, "model");
				BindUtils.postNotifyChange(null, null, cell, "selectedItem");
			}
			if (cell instanceof DateCellModel) {
				BindUtils.postNotifyChange(null, null, cell, "localDate");
			}
		}
		BindUtils.postNotifyChange(null, null, row, "*");
	}

	private void configureBPartnerSearch(ValueAdaptColumnModel colBp, String whereClause, ValueAdaptColumnModel autoContactCol) {
		colBp.setDisplayAdaptHandle(value -> {
			if (value == null)
				return null;
			if (value instanceof MBPartner)
				return ((MBPartner) value).getName();
			if (value instanceof PO)
				return ((PO) value).get_ValueAsString(MBPartner.COLUMNNAME_Name);
			return value.toString();
		});
		colBp.setValueAdaptHandle(value -> {
			if (value == null)
				return null;
			if (value instanceof PO)
				return ((PO) value).get_ID();
			if (value instanceof Number)
				return ((Number) value).intValue();
			return value;
		});
		colBp.setValueFromDaoAdaptHandle(value -> {
			if (value == null)
				return null;
			int id = value instanceof Number ? ((Number) value).intValue() : Integer.parseInt(value.toString());
			return id > 0 ? new MBPartner(Env.getCtx(), id, null) : null;
		});
		colBp.setEventHandle((event, cellModel) -> {
			if (cellModel.getRowModel() != null && !cellModel.getRowModel().isEditable()) {
				return;
			}
			InfoPanelPara para = InfoPanelPara.getInstance(MBPartner.Table_Name, MBPartner.COLUMNNAME_C_BPartner_ID);
			para.setWhereClause(whereClause);
			showInfoPanel(para, (obj, infoPanel) -> {
				Object[] values = (Object[]) obj;
				if (values == null || values.length == 0 || values[0] == null)
					return;
				int bpId = values[0] instanceof Number ? ((Number) values[0]).intValue() : Integer.parseInt(values[0].toString());
				MBPartner bp = new MBPartner(Env.getCtx(), bpId, null);
				cellModel.setValue(bp);
				cellModel.resetValidate();

				if (autoContactCol != null && cellModel.getRowModel() != null) {
					if (bpId == adminBpId && loginId > 0) {
						MUser sdpUser = new MUser(Env.getCtx(), loginId, null);
						CellModel contactCell = cellModel.getRowModel().get(autoContactCol);
						if (contactCell != null) {
							contactCell.setValue(sdpUser);
							contactCell.resetValidate();
							BindUtils.postNotifyChange(null, null, contactCell, "value");
							BindUtils.postNotifyChange(null, null, contactCell, "displayValue");
						}
					} else {
						String userWhere = MUser.Table_Name + "." + MUser.COLUMNNAME_C_BPartner_ID + " = ? AND "
								+ MUser.Table_Name + "." + MUser.COLUMNNAME_IsActive + " = 'Y'";
						PO adUser = MTable.get(Env.getCtx(), MUser.Table_Name)
								.createQuery(userWhere, null)
								.setParameters(bpId)
								.first();
						if (adUser != null) {
							CellModel contactCell = cellModel.getRowModel().get(autoContactCol);
							if (contactCell != null) {
								contactCell.setValue(adUser);
								contactCell.resetValidate();
								BindUtils.postNotifyChange(null, null, contactCell, "value");
								BindUtils.postNotifyChange(null, null, contactCell, "displayValue");
							}
						}
					}
				}
			});
		});
	}

	private void configureUserContactSearch(ValueAdaptColumnModel colContact, ValueAdaptColumnModel parentBpCol) {
		colContact.setDisplayAdaptHandle(value -> {
			if (value == null)
				return null;
			if (value instanceof MUser)
				return ((MUser) value).getName();
			if (value instanceof PO)
				return ((PO) value).get_ValueAsString(MUser.COLUMNNAME_Name);
			return value.toString();
		});
		colContact.setValueAdaptHandle(value -> {
			if (value == null)
				return null;
			if (value instanceof PO)
				return ((PO) value).get_ID();
			if (value instanceof Number)
				return ((Number) value).intValue();
			return value;
		});
		colContact.setValueFromDaoAdaptHandle(value -> {
			if (value == null)
				return null;
			int id = value instanceof Number ? ((Number) value).intValue() : Integer.parseInt(value.toString());
			return id > 0 ? new MUser(Env.getCtx(), id, null) : null;
		});
		colContact.setEventHandle((event, cellModel) -> {
			if (cellModel.getRowModel() != null && !cellModel.getRowModel().isEditable()) {
				return;
			}
			InfoPanelPara para = InfoPanelPara.getInstance(MUser.Table_Name, MUser.COLUMNNAME_AD_User_ID);
			int bpId = 0;
			if (parentBpCol != null && cellModel.getRowModel() != null) {
				CellModel bpCell = cellModel.getRowModel().get(parentBpCol);
				if (bpCell != null) {
					bpId = extractId(bpCell.getValue());
					if (bpId <= 0 && bpCell instanceof ValueAdaptCellModel) {
						bpId = extractId(((ValueAdaptCellModel) bpCell).getSelectedValue());
					}
				}
			}
			String baseWhere = MUser.Table_Name + "." + MUser.COLUMNNAME_IsActive + " = 'Y'";
			if (bpId > 0) {
				para.setWhereClause(baseWhere + " AND " + MUser.Table_Name + "." + MUser.COLUMNNAME_C_BPartner_ID + " = " + bpId);
			} else {
				para.setWhereClause(baseWhere);
			}
			showInfoPanel(para, (obj, infoPanel) -> {
				Object[] values = (Object[]) obj;
				if (values == null || values.length == 0 || values[0] == null)
					return;
				int userId = values[0] instanceof Number ? ((Number) values[0]).intValue() : Integer.parseInt(values[0].toString());
				MUser u = new MUser(Env.getCtx(), userId, null);
				cellModel.setValue(u);
				cellModel.resetValidate();
			});
		});
	}

	private void configureYearSearch(ValueAdaptColumnModel colFinancialYear) {
		colFinancialYear.setDisplayAdaptHandle(value -> {
			if (value == null)
				return null;
			if (value instanceof MYear) {
				String desc = ((MYear) value).getDescription();
				return (desc != null && !desc.isBlank()) ? desc : ((MYear) value).getFiscalYear();
			}
			if (value instanceof PO) {
				String desc = ((PO) value).get_ValueAsString(MYear.COLUMNNAME_Description);
				return (desc != null && !desc.isBlank()) ? desc : ((PO) value).get_ValueAsString(MYear.COLUMNNAME_FiscalYear);
			}
			if (value instanceof Number) {
				int id = ((Number) value).intValue();
				if (id > 0) {
					MYear y = new MYear(Env.getCtx(), id, null);
					String desc = y.getDescription();
					return (desc != null && !desc.isBlank()) ? desc : y.getFiscalYear();
				}
			}
			if (value instanceof String) {
				String s = (String) value;
				if (s.matches("\\d+")) {
					try {
						int id = Integer.parseInt(s);
						if (id > 1000) {
							MYear y = new MYear(Env.getCtx(), id, null);
							if (y.get_ID() > 0) {
								String desc = y.getDescription();
								return (desc != null && !desc.isBlank()) ? desc : y.getFiscalYear();
							}
						}
					} catch (Exception ignored) {
					}
				}
				return s;
			}
			return value.toString();
		});
		colFinancialYear.setValueAdaptHandle(value -> {
			if (value == null)
				return null;
			if (value instanceof PO)
				return ((PO) value).get_ID();
			if (value instanceof Number)
				return ((Number) value).intValue();
			return value;
		});
		colFinancialYear.setValueFromDaoAdaptHandle(value -> {
			if (value == null)
				return null;
			int id = value instanceof Number ? ((Number) value).intValue() : Integer.parseInt(value.toString());
			return id > 0 ? new MYear(Env.getCtx(), id, null) : null;
		});
		colFinancialYear.setEventHandle((event, cellModel) -> {
			if (cellModel.getRowModel() != null && !cellModel.getRowModel().isEditable()) {
				return;
			}
			InfoPanelPara para = InfoPanelPara.getInstance(MYear.Table_Name, MYear.COLUMNNAME_C_Year_ID);
			para.setWhereClause(MYear.Table_Name + "." + MYear.COLUMNNAME_IsActive + " = 'Y'");
			showInfoPanel(para, (obj, infoPanel) -> {
				Object[] values = (Object[]) obj;
				if (values == null || values.length == 0 || values[0] == null)
					return;
				int yearId = values[0] instanceof Number ? ((Number) values[0]).intValue() : Integer.parseInt(values[0].toString());
				MYear y = new MYear(Env.getCtx(), yearId, null);
				cellModel.setValue(y);
				cellModel.resetValidate();
			});
		});
	}

	private int extractId(Object val) {
		if (val == null)
			return 0;
		if (val instanceof PO)
			return ((PO) val).get_ID();
		if (val instanceof ValueNamePair) {
			try {
				return Integer.parseInt(((ValueNamePair) val).getValue());
			} catch (Exception ignored) {
			}
		}
		if (val instanceof Number)
			return ((Number) val).intValue();
		try {
			return Integer.parseInt(val.toString());
		} catch (Exception e) {
			return 0;
		}
	}

	private String extractString(Object val) {
		if (val == null)
			return null;
		if (val instanceof ValueNamePair)
			return ((ValueNamePair) val).getValue();
		String s = val.toString();
		if ("Yes".equalsIgnoreCase(s))
			return "Y";
		if ("No".equalsIgnoreCase(s))
			return "N";
		return s;
	}

	private Timestamp extractTimestamp(CellModel cell, Object val) {
		if (cell instanceof DateCellModel) {
			Timestamp ts = ((DateCellModel) cell).getTimestamp();
			if (ts != null)
				return ts;
		}
		if (val instanceof Timestamp)
			return (Timestamp) val;
		if (val instanceof java.util.Date)
			return new Timestamp(((java.util.Date) val).getTime());
		return null;
	}

	private ColumnModel text(String tableName, String columnName) {
		return CellModel.getColModelForText(MasterUtil.getNameOfColTranslated(tableName, columnName), columnName).setTableName(tableName);
	}

	private ColumnModel editableDate(String tableName, String columnName) {
		return DateCellModel.getDateColumnModel(MasterUtil.getNameOfColTranslated(tableName, columnName), columnName).setTableName(tableName);
	}


	private void initUploadDocument() {
		List<ColumnModel> cols = new ArrayList<>();
		int pmdId = selectedDGProgramme != null ? selectedDGProgramme.getZZ_Program_Master_Data_ID() : 0;
		List<X_ZZRegistrationDocs> docUploads = loadRegistrationDocumentDefinitions(pmdId, true);

		boolean hasDisabilityLetter = false;
		for (X_ZZRegistrationDocs doc : docUploads) {
			if (doc.getName() != null && doc.getName().trim().equalsIgnoreCase("Doctor - Disability Letter")) {
				hasDisabilityLetter = true;
				break;
			}
		}
		if (!hasDisabilityLetter) {
			X_ZZRegistrationDocs disDoc = new X_ZZRegistrationDocs(Env.getCtx(), 0, null);
			disDoc.setName("Doctor - Disability Letter");
			disDoc.setIsMandatory(isDisabilitySelected());
			docUploads.add(disDoc);
		}

		boolean isDisabilityYes = isDisabilitySelected();
		for (X_ZZRegistrationDocs doc : docUploads) {
			String docName = doc.getName() != null ? doc.getName() : "Document";
			boolean isMandatory = doc.isMandatory();
			if (docName.trim().equalsIgnoreCase("Doctor - Disability Letter")) {
				isMandatory = isDisabilityYes;
				doc.setIsMandatory(isMandatory);
			}
			UploadColumnModel upCol = UploadCellModel.getUploadColumnModel(
					docName,
					null,
					null,
					isMandatory ? "Upload Document *" : "Upload Document");
			upCol.setMandatory(isMandatory);
			cols.add(upCol);
		}

		String targetTable = X_ZZLearnerDGProgramme.Table_Name;
		tmDocumentUpload = TableModel.getTableBean(TableModel.class, cols, false, targetTable);
		tmDocumentUpload.setDaoManage(daoManage);
		tmDocumentUpload.init();

		if (uploadDetailTabPanel == null) {
			uploadDetailTabPanel = new NavTabPanel(mainTab);
			uploadDetailTabPanel.setTabTitle("Document Uploads");
			uploadDetailTabPanel.getCompModel().add(tmDocumentUpload);
		} else {
			uploadDetailTabPanel.getCompModel().clear();
			uploadDetailTabPanel.getCompModel().add(tmDocumentUpload);
		}
	}

	private boolean isDisabilitySelected() {
		if (disabilityCol != null && tmGeneralDetail != null && tmGeneralDetail.getRow() != null) {
			CellModel cell = tmGeneralDetail.getRow().get(disabilityCol);
			if (cell != null && cell.getValue() != null) {
				Object val = cell.getValue();
				if (val instanceof ValueNamePair) {
					return "Y".equalsIgnoreCase(((ValueNamePair) val).getValue());
				} else if (val instanceof String) {
					return "Y".equalsIgnoreCase((String) val) || "Yes".equalsIgnoreCase((String) val);
				} else if (val instanceof Boolean) {
					return Boolean.TRUE.equals(val);
				}
			}
		}
		if (person != null && person.getZZ_LI_Disability_ID() > 1000000) {
			return true;
		}
		return false;
	}

	private void updateDisabilityLetterMandatoryState() {
		boolean isDisabilityYes = isDisabilitySelected();
		if (tmDocumentUpload != null) {
			if (tmDocumentUpload.getRow() != null) {
				for (Map.Entry<ColumnModel, CellModel> entry : tmDocumentUpload.getRow().entrySet()) {
					ColumnModel col = entry.getKey();
					if (col.getTitle() != null && col.getTitle().trim().equalsIgnoreCase("Doctor - Disability Letter")) {
						col.setMandatory(isDisabilityYes);
						CellModel cell = entry.getValue();
						if (cell instanceof UploadCellModel) {
							UploadCellModel uCell = (UploadCellModel) cell;
							uCell.setBtText(isDisabilityYes ? "Upload Document *" : "Upload Document");
							BindUtils.postNotifyChange(null, null, uCell, "btText");
						}
					}
				}
			}
			if (tmDocumentUpload.getRows() != null) {
				for (RowModel row : tmDocumentUpload.getRows()) {
					for (Map.Entry<ColumnModel, CellModel> entry : row.entrySet()) {
						ColumnModel col = entry.getKey();
						if (col.getTitle() != null && col.getTitle().trim().equalsIgnoreCase("Doctor - Disability Letter")) {
							col.setMandatory(isDisabilityYes);
							CellModel cell = entry.getValue();
							if (cell instanceof UploadCellModel) {
								UploadCellModel uCell = (UploadCellModel) cell;
								uCell.setBtText(isDisabilityYes ? "Upload Document *" : "Upload Document");
								BindUtils.postNotifyChange(null, null, uCell, "btText");
							}
						}
					}
				}
			}
			BindUtils.postNotifyChange(null, null, tmDocumentUpload, "row");
			BindUtils.postNotifyChange(null, null, tmDocumentUpload, "rows");
		}
	}

	private void populateDisabilityFromPerson() {
		if (disabilityCol == null || tmGeneralDetail == null || tmGeneralDetail.getRow() == null) {
			return;
		}
		CellModel cell = tmGeneralDetail.getRow().get(disabilityCol);
		if (cell != null) {
			boolean hasDisability = person != null && person.getZZ_LI_Disability_ID() > 1000000;
			ValueNamePair pair = hasDisability ? new ValueNamePair("Y", "Yes") : new ValueNamePair("N", "No");
			cell.setValue(pair);
			BindUtils.postNotifyChange(null, null, cell, "value");
		}
		updateDisabilityLetterMandatoryState();
	}

	private List<X_ZZRegistrationDocs> loadRegistrationDocumentDefinitions(int programmeMasterId, boolean includeSharedDocuments) {
		if (programmeMasterId <= 0 && !includeSharedDocuments)
			return new ArrayList<>();

		String programmeFilter;
		if (programmeMasterId > 0 && includeSharedDocuments) {
			programmeFilter = "(" + I_ZZRegistrationDocs.COLUMNNAME_ZZ_Program_Master_Data_ID + " = ? OR "
					+ I_ZZRegistrationDocs.COLUMNNAME_ZZ_Program_Master_Data_ID + " IS NULL)";
		} else if (programmeMasterId > 0) {
			programmeFilter = I_ZZRegistrationDocs.COLUMNNAME_ZZ_Program_Master_Data_ID + " = ?";
		} else {
			programmeFilter = I_ZZRegistrationDocs.COLUMNNAME_ZZ_Program_Master_Data_ID + " IS NULL";
		}

		String sql = "SELECT * FROM " + I_ZZRegistrationDocs.Table_Name
				+ " WHERE " + programmeFilter + " AND " + I_ZZRegistrationDocs.COLUMNNAME_IsActive + " = 'Y' "
				+ "ORDER BY " + I_ZZRegistrationDocs.COLUMNNAME_Name + ", "
				+ I_ZZRegistrationDocs.COLUMNNAME_ZZRegistrationDocs_ID;

		Map<String, X_ZZRegistrationDocs> uniqueDocuments = new LinkedHashMap<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			if (programmeMasterId > 0) {
				pstmt.setInt(1, programmeMasterId);
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				X_ZZRegistrationDocs candidate = new X_ZZRegistrationDocs(Env.getCtx(), rs, null);
				String name = candidate.getName();
				String documentKey = name == null || name.isBlank()
						? "#" + candidate.getZZRegistrationDocs_ID()
						: name.trim().toLowerCase(Locale.ROOT);
				X_ZZRegistrationDocs current = uniqueDocuments.get(documentKey);
				if (current == null || (candidate.isMandatory() && !current.isMandatory())) {
					uniqueDocuments.put(documentKey, candidate);
				}
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "Failed to load registration documents for programme master data " + programmeMasterId, e);
		} finally {
			DB.close(rs, pstmt);
		}
		return new ArrayList<>(uniqueDocuments.values());
	}
	private void safeSetPOValue(PO po, String columnName, Object value) {
		if (po == null || columnName == null || value == null) {
			return;
		}
		int colIndex = po.get_ColumnIndex(columnName);
		if (colIndex < 0) {
			return;
		}
		if (!po.is_new()) {
			MTable table = MTable.get(Env.getCtx(), po.get_TableName());
			MColumn column = table != null ? table.getColumn(columnName) : null;
			if (column != null && !column.isUpdateable()) {
				return;
			}
			Object currentVal = po.get_Value(colIndex);
			if (currentVal instanceof Number && value instanceof Number) {
				if (((Number) currentVal).longValue() == ((Number) value).longValue()) {
					return;
				}
			} else if (String.valueOf(currentVal).trim().equalsIgnoreCase(String.valueOf(value).trim())) {
				return;
			}
		}
		po.set_ValueOfColumn(columnName, value);
	}

	private void saveRegistrationRecord(String trxName, int learnerId) {
		if (tmRegistration == null || tmRegistration.getRow() == null) {
			return;
		}

		String tableName = X_ZZLearnerDGProgramme.Table_Name;
		int pmdId = selectedDGProgramme != null ? selectedDGProgramme.getZZ_Program_Master_Data_ID() : 0;

		X_ZZLearnerDGProgramme dg = null;
		if (targetRegistrationPo instanceof X_ZZLearnerDGProgramme) {
			dg = (X_ZZLearnerDGProgramme) targetRegistrationPo;
		}
		if (dg == null && learnerId > 0 && pmdId > 0) {
			dg = new Query(Env.getCtx(), tableName, "ZZLearner_ID = ? AND ZZ_Program_Master_Data_ID = ?", trxName)
					.setParameters(learnerId, pmdId)
					.setOnlyActiveRecords(true)
					.setOrderBy("ZZLearnerDGProgramme_ID DESC")
					.first();
		}
		if (dg == null && learnerId > 0) {
			dg = new Query(Env.getCtx(), tableName, "ZZLearner_ID = ?", trxName)
					.setParameters(learnerId)
					.setOnlyActiveRecords(true)
					.setOrderBy("ZZLearnerDGProgramme_ID DESC")
					.first();
		}

		if (dg == null) {
			dg = new X_ZZLearnerDGProgramme(Env.getCtx(), 0, trxName);
		} else if (trxName != null) {
			dg.set_TrxName(trxName);
		}

		dg.setZZLearner_ID(learnerId);
		if (pmdId > 0) {
			dg.setZZ_Program_Master_Data_ID(pmdId);
		}

		RowModel row = tmRegistration.getRow();
		for (Map.Entry<ColumnModel, CellModel> entry : row.entrySet()) {
			ColumnModel col = entry.getKey();
			CellModel cell = entry.getValue();
			String prop = col.getDaoPropertyName();
			if (prop == null) continue;
			Object val = cell != null ? cell.getValue() : null;

			if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZAgreementReferenceNumber.equals(prop)) {
				dg.setZZAgreementReferenceNumber(extractString(val));
			} else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZCommencementDate.equals(prop)) {
				Timestamp ts = extractTimestamp(cell, val);
				if (ts != null) dg.setZZCommencementDate(ts);
			} else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZCompletionDate.equals(prop)) {
				Timestamp ts = extractTimestamp(cell, val);
				if (ts != null) dg.setZZCompletionDate(ts);
			} else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZContractNumber.equals(prop)) {
				dg.setZZContractNumber(extractString(val));
			} else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZStudentNumber.equals(prop)) {
				dg.setZZStudentNumber(extractString(val));
			} else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_SDP_ID.equals(prop)) {
				int id = extractId(val);
				if (id > 0) dg.setZZ_SDP_ID(id);
			} else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZLeadSDProviderLevy.equals(prop)) {
				dg.setZZLeadSDProviderLevy(extractString(val));
			} else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZLeadSDProviderContact_ID.equals(prop)) {
				int id = extractId(val);
				if (id > 0) dg.setZZLeadSDProviderContact_ID(id);
			} else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondarySDProvider_ID.equals(prop)) {
				dg.setZZSecondarySDProvider_ID(extractId(val));
			} else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondarySDProviderLevy.equals(prop)) {
				dg.setZZSecondarySDProviderLevy(extractString(val));
			} else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondarySDProviderContact_ID.equals(prop)) {
				dg.setZZSecondarySDProviderContact_ID(extractId(val));
			} else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_Employer_ID.equals(prop)) {
				int id = extractId(val);
				if (id > 0) dg.setZZ_Employer_ID(id);
			} else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZWALevy.equals(prop)) {
				dg.setZZWALevy(extractString(val));
			} else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZWAContact_ID.equals(prop)) {
				int id = extractId(val);
				if (id > 0) dg.setZZWAContact_ID(id);
			} else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondaryWA_ID.equals(prop)) {
				dg.setZZSecondaryWA_ID(extractId(val));
			} else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondaryWALevy.equals(prop)) {
				dg.setZZSecondaryWALevy(extractString(val));
			} else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondaryWAContact_ID.equals(prop)) {
				dg.setZZSecondaryWAContact_ID(extractId(val));
			} else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZFundingType.equals(prop)) {
				String ft = extractString(val);
				if (ft != null && !ft.isBlank()) dg.setZZFundingType(ft);
			} else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZFinancialYear_ID.equals(prop)) {
				int id = extractId(val);
				if (id > 0) dg.setZZFinancialYear_ID(id);
			}
		}

		String currentDocStatus = dg.getZZ_DocStatus();
		if (Util.isEmpty(currentDocStatus)) {
			dg.setZZ_DocStatus(X_ZZLearnerDGProgramme.ZZ_DOCSTATUS_Draft);
		}

		if (dg.get_ColumnIndex("ZZ_IsDisability") >= 0) {
			dg.setZZ_IsDisability(isDisabilitySelected());
		}
		dg.saveEx(trxName);
		targetRegistrationPo = dg;

		// Save into Qualification table as well (ensuring data is saved on both Program DG and Qualification)
		InterventionConfig config = getInterventionConfig(selectedInterventionType);
		if (config != null) {
			PO qualPo = targetChildPo;
			if (qualPo == null && selectedQualification != null && config.learnerProgrammeColumn != null && !config.learnerProgrammeColumn.isBlank()) {
				qualPo = new Query(Env.getCtx(), config.learnerTable, "ZZLearner_ID = ? AND " + config.learnerProgrammeColumn + " = ?", trxName)
						.setParameters(learnerId, selectedQualification.get_ID())
						.setOnlyActiveRecords(true)
						.setOrderBy(config.learnerTable + "_ID DESC")
						.first();
			}
			if (qualPo == null && pmdId > 0 && MTable.get(Env.getCtx(), config.learnerTable).getColumn("ZZ_Program_Master_Data_ID") != null) {
				qualPo = new Query(Env.getCtx(), config.learnerTable, "ZZLearner_ID = ? AND ZZ_Program_Master_Data_ID = ?", trxName)
						.setParameters(learnerId, pmdId)
						.setOnlyActiveRecords(true)
						.setOrderBy(config.learnerTable + "_ID DESC")
						.first();
			}
			if (qualPo == null) {
				qualPo = MTable.get(Env.getCtx(), config.learnerTable).getPO(0, trxName);
			} else if (trxName != null) {
				qualPo.set_TrxName(trxName);
			}

			safeSetPOValue(qualPo, "ZZLearner_ID", learnerId);
			if (selectedQualification != null && config.learnerProgrammeColumn != null) {
				safeSetPOValue(qualPo, config.learnerProgrammeColumn, selectedQualification.get_ID());
			}
			if (pmdId > 0) {
				safeSetPOValue(qualPo, "ZZ_Program_Master_Data_ID", pmdId);
			}
			if (dg.getZZ_SDP_ID() > 0) {
				safeSetPOValue(qualPo, "ZZ_SDP_ID", dg.getZZ_SDP_ID());
			}
			if (dg.getZZ_Employer_ID() > 0) {
				safeSetPOValue(qualPo, "ZZ_Employer_ID", dg.getZZ_Employer_ID());
			}
			if (dg.getZZStudentNumber() != null) {
				safeSetPOValue(qualPo, "ZZStudentNumber", dg.getZZStudentNumber());
			}
			if (dg.getZZFundingType() != null) {
				safeSetPOValue(qualPo, "ZZFundingType", dg.getZZFundingType());
			}
			if (dg.getZZCommencementDate() != null) {
				safeSetPOValue(qualPo, "ZZCommencementDate", dg.getZZCommencementDate());
			}
			if (dg.getZZCompletionDate() != null) {
				safeSetPOValue(qualPo, "ZZCompletionDate", dg.getZZCompletionDate());
			}
			if (dg.getZZFinancialYear_ID() > 0) {
				safeSetPOValue(qualPo, "ZZFinancialYear_ID", dg.getZZFinancialYear_ID());
			}
			if (dg.getZZContractNumber() != null) {
				safeSetPOValue(qualPo, "ZZContractNumber", dg.getZZContractNumber());
			}
			if (dg.getZZAgreementReferenceNumber() != null) {
				safeSetPOValue(qualPo, "ZZAgreementReferenceNumber", dg.getZZAgreementReferenceNumber());
			}
			if (qualPo.get_ColumnIndex("ZZ_DocStatus") >= 0) {
				String qStatus = qualPo.get_ValueAsString("ZZ_DocStatus");
				if (Util.isEmpty(qStatus)) {
					safeSetPOValue(qualPo, "ZZ_DocStatus", X_ZZLearner.ZZ_DOCSTATUS_Draft);
				}
			}
			qualPo.saveEx(trxName);
			targetChildPo = qualPo;
		}

		if (tmDocumentUpload != null && tmDocumentUpload.getRow() != null) {
			for (Map.Entry<ColumnModel, CellModel> entry : tmDocumentUpload.getRow().entrySet()) {
				CellModel cell = entry.getValue();
				if (cell instanceof UploadCellModel) {
					UploadCellModel uCell = (UploadCellModel) cell;
					byte[] bytes = uCell.getBytes();
					String fileName = uCell.getFileName();
					String docTitle = entry.getKey().getTitle();
					if (bytes != null && bytes.length > 0 && fileName != null && !fileName.isBlank()) {
						AttachmentUtil.addOrReplaceAttachmentEntry(dg, fileName, bytes, docTitle, trxName);
						uCell.setBytes(null);
					} else if (fileName == null || fileName.isBlank()) {
						AttachmentUtil.removeAttachmentEntry(dg, docTitle, trxName);
					}
				}
			}
		}
	}

	private static JasperReport learnerAgreementReport;

	static synchronized JasperReport getLearnerAgreementReport() throws Exception {
		if (learnerAgreementReport == null) {
			try (InputStream source = LearnerRegistrationVM.class.getResourceAsStream("/za/co/ntier/report/jrxmls/LearnerAgreement.jrxml")) {
				if (source == null) throw new IllegalStateException("Learner Agreement template is missing.");
				learnerAgreementReport = JasperCompileManager.compileReport(source);
			}
		}
		return learnerAgreementReport;
	}

	@Command
	public boolean downloadLearnerAgreement() {
		try {
			Map<String, Object> jasperParams = new HashMap<>();
			jasperParams.put(LearnerAgreementData.PARAMETER, getLearnerAgreementData());
			ClassLoader reportClassLoader = new ClassLoader(LearnerRegistrationVM.class.getClassLoader()) {
				@Override
				public InputStream getResourceAsStream(String name) {
					InputStream is = super.getResourceAsStream(name);
					if (is == null && name != null && name.startsWith("/")) {
						is = super.getResourceAsStream(name.substring(1));
					}
					return is;
				}
			};
			jasperParams.put(JRParameter.REPORT_CLASS_LOADER, reportClassLoader);

			JasperReport jasperReport = getLearnerAgreementReport();

			if (jasperReport != null) {
				JasperPrint print = JasperFillManager.fillReport(jasperReport, jasperParams, new JREmptyDataSource(1));
				byte[] pdfBytes = JasperExportManager.exportReportToPdf(print);
				if (pdfBytes != null && pdfBytes.length > 0) {
					Filedownload.save(pdfBytes, "application/pdf", "Learner_Agreement.pdf");
					return true;
				}
			}

			MasterUtil.showInfoDialog("Could not generate Learner Agreement.", null);
			return false;
		} catch (Exception e) {
			log.log(Level.WARNING, "Could not download Learner Agreement: " + e.getMessage(), e);
			MasterUtil.showInfoDialog("Could not generate Learner Agreement: " + e.getMessage(), null);
			return false;
		}
	}

	private Map<String, String> getLearnerAgreementData() {
		InterventionConfig config = enterQualificationRequired ? getInterventionConfig(selectedInterventionType) : null;
		RowModel row = tmRegistration != null ? tmRegistration.getRow() : null;
		if (row == null && tmRegistration != null && tmRegistration.getRows() != null && !tmRegistration.getRows().isEmpty()) {
			row = tmRegistration.getRows().get(0);
		}
		if (row == null) {
			throw new AdempiereException("Please complete the registration details before generating the agreement.");
		}
		Map<String, Object> values = new HashMap<>();
		String fundingType = "";
		for (ColumnModel column : row.keySet()) {
			CellModel cell = row.get(column);
			if (cell != null && column.getDaoPropertyName() != null) {
				Object value = cell instanceof DateCellModel ? ((DateCellModel) cell).getTimestamp() : cell.getValue();
				if (cell instanceof ListCellModel)
					value = ((ListCellModel<?>) cell).getSelectedItem();
				if (cell instanceof ValueAdaptCellModel && ((ValueAdaptCellModel) cell).getSelectedValue() != null)
					value = ((ValueAdaptCellModel) cell).getSelectedValue();
				values.put(column.getDaoPropertyName(), value);
				if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZFundingType.equals(column.getDaoPropertyName())) {
					if (value instanceof ValueNamePair)
						fundingType = ((ValueNamePair) value).getName();
					else if (value != null) {
						String table = I_ZZLearnerDGProgramme.Table_Name;
						MColumn metadata = table == null ? null : MTable.get(Env.getCtx(), table).getColumn(column.getDaoPropertyName());
						if (metadata != null && metadata.getAD_Reference_Value_ID() > 0)
							fundingType = MRefList.getListName(Env.getCtx(), metadata.getAD_Reference_Value_ID(), value.toString());
					}
				}
			}
		}
		String type = config == null ? "" : config.displayName;
		List<String> warnings = new ArrayList<>();
		int learnerId = learner != null ? learner.get_ID() : 0;
		Map<String, String> data = LearnerAgreementData.build(new X_ZZLearner_v(Env.getCtx(), learnerId, null), values,
				config == null ? null : config.learnerProgrammeColumn, config == null ? null : config.programmeTable,
				config == null ? null : config.programmeCodeColumn, type, warnings);
		if (!warnings.isEmpty()) {
			MasterUtil.showInfoDialog("Agreement data needs review:\n• " + String.join("\n• ", warnings), null);
		}
		data.put(LearnerAgreementData.FUNDING_TYPE, fundingType);
		if (selectedDGProgramme != null) {
			data.put(LearnerAgreementData.QUALIFICATION_TITLE, selectedDGProgramme.getTitle());
			data.put(LearnerAgreementData.PROGRAMME_TYPE, selectedDGProgramme.getTitle());
		}
		return data;
	}

	public TableModel getTmParentDetails() {
		return tmParentDetails;
	}

	public TableModel getTmEducationDetail() {
		return tmEducationDetail;
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

	public TableModel getTmDocumentUpload() {
		return tmDocumentUpload;
	}

	@Override
	public void doSubmit(String trxName) {
		if (targetRegistrationPo != null && targetRegistrationPo.get_ColumnIndex("ZZ_DocStatus") >= 0) {
			safeSetPOValue(targetRegistrationPo, "ZZ_DocStatus", X_ZZLearnerDGProgramme.ZZ_DOCSTATUS_Created);
			targetRegistrationPo.saveEx(trxName);
		}
		if (targetChildPo != null && targetChildPo.get_ColumnIndex("ZZ_DocStatus") >= 0) {
			safeSetPOValue(targetChildPo, "ZZ_DocStatus", X_ZZLearnerDGProgramme.ZZ_DOCSTATUS_Created);
			targetChildPo.saveEx(trxName);
		}
		super.doSubmit(trxName);
	}

	@Override
	public boolean isSupportSubmit() {
		return true;
	}
}
