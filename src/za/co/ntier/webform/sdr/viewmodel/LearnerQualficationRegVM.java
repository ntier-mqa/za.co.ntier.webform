package za.co.ntier.webform.sdr.viewmodel;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.webui.panel.RegistrationWindow;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.ValueNamePair;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.WrongValueException;

import za.co.ntier.api.model.I_ZZCompletedAssessments_v;
import za.co.ntier.api.model.I_ZZLearner;
import za.co.ntier.api.model.I_ZZLearnerLearnership;
import za.co.ntier.api.model.I_ZZLearnerQCTOArtisans;
import za.co.ntier.api.model.I_ZZLearnerQCTOLearnership;
import za.co.ntier.api.model.I_ZZLearnerQCTOSkillsProgramme;
import za.co.ntier.api.model.I_ZZLearnerSkillsProgramme;
import za.co.ntier.api.model.I_ZZLearnership;
import za.co.ntier.api.model.I_ZZPerson;
import za.co.ntier.api.model.I_ZZQctoLearnership;
import za.co.ntier.api.model.I_ZZQctoSkillsProgramme;
import za.co.ntier.api.model.I_ZZSkillsProgramme;
import za.co.ntier.api.model.I_ZZ_AlternateIDType;
import za.co.ntier.api.model.X_ZZLearner;
import za.co.ntier.api.model.X_ZZLearnerLearnership;
import za.co.ntier.api.model.X_ZZLearnerQCTOArtisans;
import za.co.ntier.api.model.X_ZZLearnerQCTOLearnership;
import za.co.ntier.api.model.X_ZZLearnerQCTOSkillsProgramme;
import za.co.ntier.api.model.X_ZZLearnerSkillsProgramme;
import za.co.ntier.api.model.X_ZZLearnership;
import za.co.ntier.api.model.X_ZZPerson;
import za.co.ntier.api.model.X_ZZQctoLearnership;
import za.co.ntier.api.model.X_ZZQctoSkillsProgramme;
import za.co.ntier.api.model.X_ZZSkillsProgramme;
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
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.RowModel.RowData;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.TableModel.CommandSetting;
import za.co.ntier.webform.sdr.component.bean.TableModel.DaoManage;
import za.co.ntier.webform.sdr.component.bean.TableModel.ViewType;
import za.co.ntier.webform.sdr.component.bean.cell.DateCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.IDCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.IDTypeCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.ListCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.ValueAdaptCellModel;
import za.co.ntier.webform.sdr.component.bean.column.ListColumnModel;
import za.co.ntier.webform.sdr.component.bean.column.ValueAdaptColumnModel;
import za.co.ntier.webform.sdr.component.tab.bean.NavTab;
import za.co.ntier.webform.sdr.component.tab.bean.NavTabPanel;

public class LearnerQualficationRegVM extends BaseAppVM
{

	private TableModel					tmNames;
	private NavTab						mainTab;
	X_ZZLearner							learner;

	DaoManage							daoManage			= new DaoManage();

	private String						idNumber;
	private X_ZZ_AlternateIDType		selectedIdType;
	private List<X_ZZ_AlternateIDType>	alternateIdTypes;
	private String						validationMessage	= "";
	private boolean						identityValidated	= false;

	@Override
	public Object getMainApp()
	{
		return null;
	}

	@Override
	public List<DaoManage> getDaoManages()
	{
		return List.of(daoManage);
	}

	@Override
	public List<ISaveForm> getSaveComponents()
	{
		return List.of(mainTab, tmNames);
	}

	@Override
	protected void showResult(boolean isSubmit)
	{
		if (isNew)
		{
			MasterUtil.showInfoDialog("ZZLearnerCreatedSuccess", MasterUtil.fCloseActiveWindow);
		}
		else
		{
			MasterUtil.showInfoDialog("ZZLearnerSavedSuccess", MasterUtil.fCloseActiveWindow);
		}
	}

	private X_ZZPerson	person;
	boolean				isNew	= true;

	public String getIdNumber()
	{
		return idNumber;
	}

	public void setIdNumber(String idNumber)
	{
		this.idNumber = idNumber;
	}

	public X_ZZ_AlternateIDType getSelectedIdType()
	{
		return selectedIdType;
	}

	public void setSelectedIdType(X_ZZ_AlternateIDType selectedIdType)
	{
		this.selectedIdType = selectedIdType;
	}

	public List<X_ZZ_AlternateIDType> getAlternateIdTypes()
	{
		return alternateIdTypes;
	}

	public String getValidationMessage()
	{
		return validationMessage;
	}

	public boolean isIdentityValidated()
	{
		return identityValidated;
	}

	@Init(superclass = true)
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey)
	MenuContextInfo menuContextInfo)
	{

		alternateIdTypes = MasterUtil.getAlternateIDType();
		selectedIdType = alternateIdTypes	.stream().filter(t -> IDCellModel.idTypeRSA_ID.equals(t.getName())).findFirst()
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
			protected boolean validateActiveTab(boolean emptyAsValid)
			{
				boolean isHeaderValid = true;
				if (tmNames != null)
				{
					isHeaderValid = tmNames.validate(null);
				}
				boolean isTabValid = super.validateActiveTab(emptyAsValid);
				return isHeaderValid && isTabValid;
			}
		});
		initForm();

		if (menuContextInfo.getRecordID() > 0)
		{
			loadForEdit();
		}
	}

	@Command
	@NotifyChange({ "validationMessage", "identityValidated" })
	public void onValidateIdentity()
	{
		if (idNumber == null || idNumber.isBlank())
		{
			validationMessage = "Please enter an ID Number.";
			return;
		}
		if (selectedIdType == null)
		{
			validationMessage = "Please select an ID Type.";
			return;
		}

		if (IDCellModel.idTypeRSA_ID.equals(selectedIdType.getName()))
		{
			try
			{
				RegistrationWindow.validateIdNo(null, idNumber);
			}
			catch (WrongValueException e)
			{
				validationMessage = e.getMessage();
				return;
			}
		}

		idNoCol.setDefaultValue(idNumber);
		alternateIDTypeCol.setDefaultValue(selectedIdType.getName(), MasterUtil.nameAlternateIdTypeCompare);

		// Lookup ZZPerson by ID No
		loadSaved(idNumber, selectedIdType.getZZ_AlternateIDType_ID());

		if (person != null)
		{
			// Person found - check learner status
			if (learner != null)
			{
				boolean isDraft = learner.getZZ_DocStatus() == null || X_ZZLearner.ZZ_DOCSTATUS_Draft.equals(learner.getZZ_DocStatus());
				if (!isDraft)
				{
					validationMessage = "A learner with " + selectedIdType.getName() + " " + idNumber + " already exists and is not in Draft status.";
					identityValidated = false;
					return;
				}
				validationMessage = "Person and learner record found. You may edit the details below.";
			}
			else
			{
				validationMessage = "Person found. Create a new learner record will be created upon save.";
			}
			identityValidated = true;
			alternateIDTypeCol.setReadonly(true);
		}
		else
		{
			// Person NOT found
			validationMessage = "A person with " + selectedIdType.getName() + " " + idNumber + " does not exist in the system.";
			identityValidated = false;
		}
	}

	private void loadForEdit()
	{
		alternateIDTypeCol.setReadonly(true);
		learner = (X_ZZLearner) MTable	.get(Env.getCtx(), I_ZZLearner.Table_Name)
										.getPO(getMenuContextInfo().getRecordID(), null);
		if (learner == null)
		{
			MasterUtil.showInfoDialog("ZZLearnerNotFoundLearner", MasterUtil.fCloseActiveWindow);
		}
		else
		{
			person = (X_ZZPerson) MTable.get(Env.getCtx(), I_ZZPerson.Table_Name).getPO(learner.getZZPerson_ID(), null);
		}

		if (person == null)
		{
			MasterUtil.showInfoDialog("ZZLearnerNotFoundUser", MasterUtil.fCloseActiveWindow);
		}

		daoManage.setDao(learner);
		daoManage.setDao(person);

		identityValidated = true;
		if (person != null)
		{
			if (person.getZZ_ID_Passport_No() != null && !person.getZZ_ID_Passport_No().isBlank())
			{
				idNumber = person.getZZ_ID_Passport_No();
			}
			else
			{
				idNumber = person.getZZOtherIDNo();
			}
		}
		validationMessage = "";

		isNew = false;
		loadData();
	}

	private void loadSaved(String idValue, int idTypeId)
	{
		Query userQuery;
		if (IDCellModel.idTypeRSA_ID.equals(selectedIdType.getName()))
		{
			userQuery = MTable	.get(Env.getCtx(), I_ZZPerson.Table_Name)
								.createQuery(String.format(	"%s = ? AND %s.%s = ?", I_ZZPerson.COLUMNNAME_ZZ_ID_Passport_No, I_ZZ_AlternateIDType.Table_Name,
															I_ZZ_AlternateIDType.COLUMNNAME_ZZ_AlternateIDType_ID), null);
		}
		else
		{
			userQuery = MTable	.get(Env.getCtx(), I_ZZPerson.Table_Name)
								.createQuery(String.format(	"%s = ? AND %s.%s = ?", I_ZZPerson.COLUMNNAME_ZZOtherIDNo, I_ZZ_AlternateIDType.Table_Name,
															I_ZZ_AlternateIDType.COLUMNNAME_ZZ_AlternateIDType_ID), null);
		}

		userQuery.addTableDirectJoin(I_ZZ_AlternateIDType.Table_Name);

		userQuery.setParameters(idValue, idTypeId);
		userQuery.setOnlyActiveRecords(true);
		person = userQuery.firstOnly();

		X_ZZLearner learnerSaved = null;

		if (person != null)
		{
			daoManage.setDao(person);
			Query savedDataQuery = MTable	.get(Env.getCtx(), I_ZZLearner.Table_Name)
											.createQuery(String.format("%s = ?", I_ZZLearner.COLUMNNAME_ZZPerson_ID), null);

			savedDataQuery.setParameters(person.getZZPerson_ID());
			savedDataQuery.setOnlyActiveRecords(true);

			learnerSaved = savedDataQuery.firstOnly();

			firstNameCol.setDefaultValue(person.getZZFirstName());
		}
		else
		{
			daoManage.resetDao(I_ZZPerson.Table_Name);
		}

		if (learnerSaved != null)
		{
			boolean isDraft = learnerSaved.getZZ_DocStatus() == null || X_ZZLearner.ZZ_DOCSTATUS_Draft.equals(learnerSaved.getZZ_DocStatus());
			if (!isDraft)
			{
				MasterUtil.showInfoDialog("ZZLearnerWrongStatus", MasterUtil.fCloseActiveWindow);
			}
			daoManage.setDao(learnerSaved);
		}
		else
		{
			daoManage.resetDao(I_ZZLearner.Table_Name);
		}

		isNew = learnerSaved == null;
		learner = learnerSaved;

		loadData();
	}

	private void loadData()
	{
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

	private void initForm()
	{
		tmNames = initTbName();
		initGeneralDetail();
		initLearnerChildTabs();
	}

	private void initLearnerChildTabs()
	{
		List<ColumnModel> artisanColumns = new ArrayList<>();
		ValueAdaptColumnModel artisanLearnershipCol = ValueAdaptCellModel.getValueAdaptColumnModel(	Msg.getElement(	Env.getCtx(),
																													I_ZZQctoLearnership.COLUMNNAME_ZZLearnershipCode),
																									I_ZZLearnerQCTOArtisans.COLUMNNAME_ZZQctoLearnership_ID,
																									CellModel.SEARCH_CELL);
		artisanLearnershipCol.setTableName(I_ZZLearnerQCTOArtisans.Table_Name);
		ColumnModel artisanLearnershipTitleCol = label(I_ZZQctoLearnership.Table_Name, I_ZZQctoLearnership.COLUMNNAME_ZZLearnershipTitle);
		artisanColumns.add(artisanLearnershipCol);
		artisanColumns.add(artisanLearnershipTitleCol);
		artisanColumns.add(text(I_ZZLearnerQCTOArtisans.Table_Name,
								I_ZZLearnerQCTOArtisans.COLUMNNAME_ZZStudentNumber));
		artisanColumns.add(editableDate(I_ZZLearnerQCTOArtisans.Table_Name,
										I_ZZLearnerQCTOArtisans.COLUMNNAME_ZZCommencementDate));
		artisanColumns.add(editableDate(I_ZZLearnerQCTOArtisans.Table_Name,
										I_ZZLearnerQCTOArtisans.COLUMNNAME_ZZCompletionDate));
		configureLearnershipSelector(artisanLearnershipCol, artisanLearnershipTitleCol);

		initLearnerProgrammeTab("QCTO Artisans", I_ZZLearnerQCTOArtisans.Table_Name,
								"INNER JOIN ZZQctoLearnership q ON q.ZZQctoLearnership_ID = ZZLearnerQCTOArtisans.ZZQctoLearnership_ID",
								false,
								artisanColumns);

		initLearnerProgrammeTab("QCTO Learnerships", I_ZZLearnerQCTOLearnership.Table_Name,
								"INNER JOIN ZZQctoLearnership q ON q.ZZQctoLearnership_ID = ZZLearnerQCTOLearnership.ZZQctoLearnership_ID",
								false,
								selectableProgrammeColumns(	I_ZZLearnerQCTOLearnership.Table_Name,
															I_ZZQctoLearnership.Table_Name, I_ZZLearnerQCTOLearnership.COLUMNNAME_ZZQctoLearnership_ID,
															I_ZZQctoLearnership.COLUMNNAME_ZZQctoLearnership_ID,
															I_ZZQctoLearnership.COLUMNNAME_ZZLearnershipCode, I_ZZQctoLearnership.COLUMNNAME_ZZLearnershipTitle,
															id -> new X_ZZQctoLearnership(Env.getCtx(), id, null)));

		initLearnerProgrammeTab("QCTO Skills Programmes", I_ZZLearnerQCTOSkillsProgramme.Table_Name,
								"INNER JOIN ZZQctoSkillsProgramme q ON q.ZZQctoSkillsProgramme_ID = ZZLearnerQCTOSkillsProgramme.ZZQctoSkillsProgramme_ID",
								false,
								selectableProgrammeColumns(	I_ZZLearnerQCTOSkillsProgramme.Table_Name,
															I_ZZQctoSkillsProgramme.Table_Name,
															I_ZZLearnerQCTOSkillsProgramme.COLUMNNAME_ZZQctoSkillsProgramme_ID,
															I_ZZQctoSkillsProgramme.COLUMNNAME_ZZQctoSkillsProgramme_ID,
															I_ZZQctoSkillsProgramme.COLUMNNAME_ZZSkillsProgrammeCode,
															I_ZZQctoSkillsProgramme.COLUMNNAME_ZZSkillsProgrammeTitle,
															id -> new X_ZZQctoSkillsProgramme(Env.getCtx(), id, null)));

		initLearnerProgrammeTab("Learnerships", I_ZZLearnerLearnership.Table_Name,
								"INNER JOIN ZZLearnership p ON p.ZZLearnership_ID = ZZLearnerLearnership.ZZLearnership_ID",
								false,
								selectableProgrammeColumns(	I_ZZLearnerLearnership.Table_Name,
															I_ZZLearnership.Table_Name, I_ZZLearnerLearnership.COLUMNNAME_ZZLearnership_ID,
															I_ZZLearnership.COLUMNNAME_ZZLearnership_ID,
															I_ZZLearnership.COLUMNNAME_ZZLearnershipCode, I_ZZLearnership.COLUMNNAME_ZZLearnershipTitle,
															id -> new X_ZZLearnership(Env.getCtx(), id, null)));

		initLearnerProgrammeTab("Skills Programmes", I_ZZLearnerSkillsProgramme.Table_Name,
								"JOIN ZZSkillsProgramme p ON p.ZZSkillsProgramme_ID = ZZLearnerSkillsProgramme.ZZSkillsProgramme_ID",
								false,
								selectableProgrammeColumns(I_ZZLearnerSkillsProgramme.Table_Name,
										I_ZZSkillsProgramme.Table_Name, I_ZZLearnerSkillsProgramme.COLUMNNAME_ZZSkillsProgramme_ID,
										I_ZZSkillsProgramme.COLUMNNAME_ZZSkillsProgramme_ID,
										I_ZZSkillsProgramme.COLUMNNAME_ZZSkillsProgrammeCode, I_ZZSkillsProgramme.COLUMNNAME_ZZSkillsProgrammeTitle,
										id -> new X_ZZSkillsProgramme(Env.getCtx(), id, null)));
	}

	private List<ColumnModel> selectableProgrammeColumns(	String learnerTable, String programmeTable,
															String learnerProgrammeId, String programmeIdColumn, String codeColumn, String titleColumn,
															Function<Integer, PO> programmeLoader)
	{
		ValueAdaptColumnModel programmeCol = ValueAdaptCellModel.getValueAdaptColumnModel(Msg.getElement(Env.getCtx(), codeColumn), learnerProgrammeId,
																							CellModel.SEARCH_CELL);
		programmeCol.setTableName(learnerTable);
		ColumnModel titleCol = label(programmeTable, titleColumn);
		configureProgrammeSelector(	programmeCol, titleCol, programmeLoader, programmeIdColumn, codeColumn, titleColumn,
									programmeTable);
		String studentColumn = learnerTable	.equals(I_ZZLearnerQCTOLearnership.Table_Name)
											? I_ZZLearnerQCTOLearnership.COLUMNNAME_ZZStudentNumber
						: learnerTable	.equals(I_ZZLearnerQCTOSkillsProgramme.Table_Name)
										? I_ZZLearnerQCTOSkillsProgramme.COLUMNNAME_ZZStudentNumber
							: learnerTable	.equals(I_ZZLearnerLearnership.Table_Name)
											? I_ZZLearnerLearnership.COLUMNNAME_ZZStudentNumber
							: I_ZZLearnerSkillsProgramme.COLUMNNAME_ZZStudentNumber;
		return List.of(	programmeCol, titleCol, text(learnerTable, studentColumn),
						editableDate(learnerTable, "ZZCommencementDate"), editableDate(learnerTable, "ZZCompletionDate"));
	}

	private ColumnModel label(String tableName, String columnName)
	{
		return CellModel.getColModelForLabel(MasterUtil.getNameOfColTranslated(tableName, columnName), columnName)
						.setTableName(tableName).setReadonly(true);
	}

	private ColumnModel text(String tableName, String columnName)
	{
		return CellModel.getColModelForText(MasterUtil.getNameOfColTranslated(tableName, columnName), columnName)
						.setTableName(tableName);
	}

	private ColumnModel editableDate(String tableName, String columnName)
	{
		return DateCellModel.getDateColumnModel(MasterUtil.getNameOfColTranslated(tableName, columnName), columnName)
							.setTableName(tableName);
	}

	private void configureLearnershipSelector(ValueAdaptColumnModel learnershipCol, ColumnModel titleCol)
	{
		configureProgrammeSelector(	learnershipCol, titleCol, id -> new X_ZZQctoLearnership(Env.getCtx(), id, null),
									I_ZZQctoLearnership.COLUMNNAME_ZZQctoLearnership_ID, I_ZZQctoLearnership.COLUMNNAME_ZZLearnershipCode,
									I_ZZQctoLearnership.COLUMNNAME_ZZLearnershipTitle, I_ZZQctoLearnership.Table_Name);
	}

	private void configureProgrammeSelector(ValueAdaptColumnModel programmeCol, ColumnModel titleCol,
											Function<Integer, PO> programmeLoader, String idColumn, String codeColumn, String titleColumn,
											String programmeTable)
	{
		programmeCol.setDisplayAdaptHandle(value -> value == null ? null : ((PO) value).get_Value(codeColumn));
		programmeCol.setValueAdaptHandle(value -> value == null ? null : ((PO) value).get_ID());
		programmeCol.setValueFromDaoAdaptHandle(value -> {
			if (value == null || Integer.class.cast(value) == 0)
				return null;
			return programmeLoader.apply(Integer.class.cast(value));
		});
		programmeCol.setEventHandle((event, cellModel) -> showInfoPanel(
																		InfoPanelPara.getInstance(programmeTable, idColumn), (obj, infoPanel) -> {
																			Object[] values = (Object[]) obj;
																			PO selected = programmeLoader.apply((int) values[0]);
																			cellModel.setValue(selected);
																			cellModel.getRowModel().get(titleCol).setValue(selected.get_Value(titleColumn));
																		}));
	}

	private PO getLearnerProgramme(PO child, String learnerTable)
	{
		int programmeId = 0;
		if (I_ZZLearnerQCTOArtisans.Table_Name.equals(learnerTable))
			programmeId = child.get_ValueAsInt(I_ZZLearnerQCTOArtisans.COLUMNNAME_ZZQctoLearnership_ID);
		else if (I_ZZLearnerQCTOLearnership.Table_Name.equals(learnerTable))
			programmeId = child.get_ValueAsInt(I_ZZLearnerQCTOLearnership.COLUMNNAME_ZZQctoLearnership_ID);
		else if (I_ZZLearnerQCTOSkillsProgramme.Table_Name.equals(learnerTable))
			programmeId = child.get_ValueAsInt(I_ZZLearnerQCTOSkillsProgramme.COLUMNNAME_ZZQctoSkillsProgramme_ID);
		else if (I_ZZLearnerLearnership.Table_Name.equals(learnerTable))
			programmeId = child.get_ValueAsInt(I_ZZLearnerLearnership.COLUMNNAME_ZZLearnership_ID);
		else if (I_ZZLearnerSkillsProgramme.Table_Name.equals(learnerTable))
			programmeId = child.get_ValueAsInt(I_ZZLearnerSkillsProgramme.COLUMNNAME_ZZSkillsProgramme_ID);
		if (programmeId <= 0)
			return null;
		if (I_ZZLearnerQCTOArtisans.Table_Name.equals(learnerTable)
			|| I_ZZLearnerQCTOLearnership.Table_Name.equals(learnerTable))
			return new X_ZZQctoLearnership(Env.getCtx(), programmeId, null);
		if (I_ZZLearnerQCTOSkillsProgramme.Table_Name.equals(learnerTable))
			return new X_ZZQctoSkillsProgramme(Env.getCtx(), programmeId, null);
		if (I_ZZLearnerLearnership.Table_Name.equals(learnerTable))
			return new X_ZZLearnership(Env.getCtx(), programmeId, null);
		if (I_ZZLearnerSkillsProgramme.Table_Name.equals(learnerTable))
			return new X_ZZSkillsProgramme(Env.getCtx(), programmeId, null);
		return null;
	}

	private void initLearnerProgrammeTab(	String title, String tableName, String joinClause, boolean readOnly,
											List<ColumnModel> columns)
	{
		TableModel table = TableModel.getTableBean(TableModel.class, columns, false, tableName);
		table.setViewModel(ViewType.VIEW_GRID);
		table.setSclass("srd-learner-" + title.toLowerCase().replace(' ', '-'));
		if (!readOnly)
		{
			table.setPoSupplier(row -> createLearnerChildRecord(tableName));
			table.setCommandSetting(CommandSetting.getNonAddButton());
			table.setCreateNewRowWhenEmpty(false);
		}
		table.init();
		table.setLoadSavedDataHandle(model -> {
			if (learner == null || learner.getZZLearner_ID() <= 0)
				return;
			String where = I_ZZCompletedAssessments_v.Table_Name.equals(tableName)
																					? I_ZZCompletedAssessments_v.COLUMNNAME_ZZLearner_ID + " = ?"
																					: tableName + "." + I_ZZLearnerQCTOArtisans.COLUMNNAME_ZZLearner_ID
																						+ " = ?";
			Query query = MTable.get(Env.getCtx(), tableName).createQuery(where, null);
			if (!joinClause.isBlank())
				query.addJoinClause(joinClause);
			List<PO> rows = query.setParameters(learner.getZZLearner_ID()).list();
			if (joinClause.isBlank())
			{
				model.resetMultiPo(RowData.standardToMultiPo(rows));
			}
			else
			{
				List<List<PO>> rowData = new ArrayList<>();
				for (PO child : rows)
				{
					List<PO> row = new ArrayList<>();
					row.add(child);
					PO programme = getLearnerProgramme(child, tableName);
					if (programme != null)
						row.add(programme);
					rowData.add(row);
				}
				model.resetMultiPo(rowData);
			}
		});
		if (!readOnly)
		{
			table.setBeforeSave(event -> {
				if (event.isPOEven() && learner != null && learner.getZZLearner_ID() > 0)
				{
					event.po().set_ValueOfColumn(	I_ZZLearnerQCTOArtisans.COLUMNNAME_ZZLearner_ID,
													learner.getZZLearner_ID());
				}
				return true;
			});
		}
		NavTabPanel panel = new NavTabPanel(mainTab);
		panel.setTabTitle(title);
		panel.getCompModel().add(table);
		panel.setShowTabAddButton(true);
	}

	private PO createLearnerChildRecord(String tableName)
	{
		PO record;
		if (I_ZZLearnerQCTOArtisans.Table_Name.equals(tableName))
		{
			record = new X_ZZLearnerQCTOArtisans(Env.getCtx(), 0, null);
		}
		else if (I_ZZLearnerQCTOLearnership.Table_Name.equals(tableName))
		{
			record = new X_ZZLearnerQCTOLearnership(Env.getCtx(), 0, null);
		}
		else if (I_ZZLearnerQCTOSkillsProgramme.Table_Name.equals(tableName))
		{
			record = new X_ZZLearnerQCTOSkillsProgramme(Env.getCtx(), 0, null);
		}
		else if (I_ZZLearnerLearnership.Table_Name.equals(tableName))
		{
			record = new X_ZZLearnerLearnership(Env.getCtx(), 0, null);
		}
		else if (I_ZZLearnerSkillsProgramme.Table_Name.equals(tableName))
		{
			record = new X_ZZLearnerSkillsProgramme(Env.getCtx(), 0, null);
		}
		else
		{
			throw new AdempiereException("Unsupported learner child table: " + tableName);
		}
		if (learner != null && learner.getZZLearner_ID() > 0)
		{
			record.set_ValueOfColumn(	I_ZZLearnerQCTOArtisans.COLUMNNAME_ZZLearner_ID,
										learner.getZZLearner_ID());
		}
		return record;
	}

	ColumnModel								idNoCol;
	ListColumnModel<X_ZZ_AlternateIDType>	alternateIDTypeCol;
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
				// BindUtils.postNotifyChange(null, null, dobCell, "value");
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
				// BindUtils.postNotifyChange(null, null, genderCell, "value");
			}
		}
	}

	private void initGeneralDetail()
	{
		List<ColumnModel> cols = new ArrayList<>();

		alternateIDTypeCol = IDTypeCellModel.getIDTypeCol();
		alternateIDTypeCol.setTableName(I_ZZPerson.Table_Name);
		alternateIDTypeCol.setEventHandle((event, cellMode) -> {
			@SuppressWarnings("unchecked")
			ListCellModel<X_ZZ_AlternateIDType> alternateIDCellMode = (ListCellModel<X_ZZ_AlternateIDType>) cellMode;
			alternateIDCellMode.resetDefaultValue();
			alternateIDCellMode.getColModel().setDefaultValue(	alternateIDCellMode.getSelectedItem().getName(),
																MasterUtil.nameAlternateIdTypeCompare);
			IDCellModel idCellMode = (IDCellModel) cellMode.getRowModel().get(idNoCol);
			idCellMode.validate();
		});
		cols.add(alternateIDTypeCol);

		idNoCol = IDCellModel.getIDColumnModel().required().setTableName(I_ZZPerson.Table_Name).setReadonly(true);
		idNoCol.setEventHandle((event, cellMode) -> {
			Object idValue = cellMode.getDirtyValue();
			if (idValue != null)
			{
				cellMode.getColModel().setDefaultValue(idValue);
				String idString = idValue.toString().trim();
				if (idString.matches("\\d{13}"))
				{
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
																		MasterUtil.getNameOfColTranslated(	I_ZZPerson.Table_Name,
																											I_ZZPerson.COLUMNNAME_ZZ_LI_HomeLanguage_ID),
																		I_ZZPerson.COLUMNNAME_ZZ_LI_HomeLanguage_ID, MasterUtil.getHomeLanguage(), title -> {
																			return title.getName();
																		}, title -> {
																			return title.getZZ_LI_HomeLanguage_ID();
																		}).setzClass(X_ZZ_LI_HomeLanguage.class).setUseForID(true).required();
		cols.add(homeLanguageCol);

		ColumnModel nationalityCol = ListCellModel.getListColumnModel(
																		MasterUtil.getNameOfColTranslated(	I_ZZPerson.Table_Name,
																											I_ZZPerson.COLUMNNAME_ZZ_Nationality_ID),
																		I_ZZPerson.COLUMNNAME_ZZ_Nationality_ID, MasterUtil.getNationality(), title -> {
																			return title.getName();
																		}, title -> {
																			return title.getZZ_Nationality_ID();
																		}).setzClass(X_ZZ_Nationality.class).setUseForID(true).required();
		cols.add(nationalityCol);

		ColumnModel citizenResidentialStatusCol = ListCellModel	.getListColumnModel(
																					MasterUtil.getNameOfColTranslated(	I_ZZPerson.Table_Name,
																														I_ZZPerson.COLUMNNAME_ZZ_LI_CitizenResidentialStatus_ID),
																					I_ZZPerson.COLUMNNAME_ZZ_LI_CitizenResidentialStatus_ID, MasterUtil
																																						.getCitizenResidentialStatus(),
																					title -> {
																						return title.getName();
																					}, title -> {
																						return title.getZZ_LI_CitizenResidentialStatus_ID();
																					}).setzClass(X_ZZ_LI_CitizenResidentialStatus.class).setUseForID(true)
																.required();
		cols.add(citizenResidentialStatusCol);

		ColumnModel socioEconomicStatusCol = ListCellModel.getListColumnModel(
																				MasterUtil.getNameOfColTranslated(	I_ZZPerson.Table_Name,
																													I_ZZPerson.COLUMNNAME_ZZ_LI_SocioEconomicStatus_ID),
																				I_ZZPerson.COLUMNNAME_ZZ_LI_SocioEconomicStatus_ID, MasterUtil
																																				.getSocioEconomicStatus(),
																				title -> {
																					return title.getName();
																				}, title -> {
																					return title.getZZ_LI_SocioEconomicStatus_ID();
																				}).setzClass(X_ZZ_LI_SocioEconomicStatus.class).setUseForID(true).required();
		cols.add(socioEconomicStatusCol);

		TableModel generalDetail = TableModel.getTableBean(TableModel.class, cols, false, I_ZZPerson.Table_Name);
		generalDetail.setSclass("srd-general srd-general-learner");
		generalDetail.setDaoManage(daoManage);
		generalDetail.init();

	}

	ColumnModel firstNameCol;

	private TableModel initTbName()
	{
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

	public TableModel getTmNames()
	{
		return tmNames;
	}

	public NavTab getMainTab()
	{
		return mainTab;
	}

	public void setMainTab(NavTab mainTab)
	{
		this.mainTab = mainTab;
	}

	@Override
	public void doSave(String trxName)
	{
		if (learner == null)
		{
			learner = (X_ZZLearner) daoManage.getDaoForSave(I_ZZLearner.Table_Name);
		}
		if (person == null)
		{
			person = (X_ZZPerson) daoManage.getDaoForSave(I_ZZPerson.Table_Name);
		}

		boolean isDraft = true;
		if (learner != null)
		{
			isDraft = learner.getZZ_DocStatus() == null
						|| X_ZZLearner.ZZ_DOCSTATUS_Draft.equals(learner.getZZ_DocStatus());
		}

		if (!isDraft)
		{
			throw new AdempiereException(Msg.getMsg(Env.getCtx(), "ZZLearnerWrongStatus"));
		}

		super.doSave(trxName);

		int alternateIdTypeId = person.getZZ_AlternateIDType_ID();
		if (alternateIdTypeId > 0)
		{
			X_ZZ_AlternateIDType altType = new X_ZZ_AlternateIDType(Env.getCtx(), alternateIdTypeId, null);
			if (IDCellModel.idTypeRSA_ID.equals(altType.getName()))
			{
				String idPassportNo = person.getZZ_ID_Passport_No();
				if (idPassportNo != null && !idPassportNo.isBlank())
				{
					String sql = "SELECT COUNT(1) FROM ZZPerson WHERE ZZ_ID_Passport_No = ? AND ZZ_AlternateIDType_ID = ? AND ZZPerson_ID != ? AND IsActive='Y'";
					int count = DB.getSQLValue(trxName, sql, idPassportNo, alternateIdTypeId, person.get_ID());
					if (count > 0)
					{
						throw new AdempiereException("A person with this ID Type and ID Number already exists in the system.");
					}
				}
			}
			else
			{
				String otherIdNo = person.getZZOtherIDNo();
				if (otherIdNo != null && !otherIdNo.isBlank())
				{
					String sql = "SELECT COUNT(1) FROM ZZPerson WHERE ZZOtherIDNo = ? AND ZZ_AlternateIDType_ID = ? AND ZZPerson_ID != ? AND IsActive='Y'";
					int count = DB.getSQLValue(trxName, sql, otherIdNo, alternateIdTypeId, person.get_ID());
					if (count > 0)
					{
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
	public void doSubmit(String trxName)
	{
		super.doSubmit(trxName);
	}

	@Override
	public boolean isSupportSubmit()
	{
		return true;
	}
}
