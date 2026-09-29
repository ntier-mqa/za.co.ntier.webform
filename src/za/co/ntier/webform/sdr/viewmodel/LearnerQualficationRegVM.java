package za.co.ntier.webform.sdr.viewmodel;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.webui.panel.RegistrationWindow;
import org.compiere.model.I_C_BPartner;
import org.compiere.model.MBPartner;
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
import za.co.ntier.api.model.X_ZZQctoQualification;
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
		initLearnerProgrammeTab("QCTO Artisans", I_ZZLearnerQCTOArtisans.Table_Name,
								"LEFT JOIN ZZQctoLearnership q ON q.ZZQctoLearnership_ID = ZZLearnerQCTOArtisans.ZZQctoLearnership_ID",
								false,
								selectableProgrammeColumns(	I_ZZLearnerQCTOArtisans.Table_Name,
															I_ZZQctoLearnership.Table_Name, I_ZZLearnerQCTOArtisans.COLUMNNAME_ZZQctoLearnership_ID,
															I_ZZQctoLearnership.COLUMNNAME_ZZQctoLearnership_ID,
															I_ZZQctoLearnership.COLUMNNAME_ZZLearnershipCode, I_ZZQctoLearnership.COLUMNNAME_ZZLearnershipTitle,
															id -> new X_ZZQctoLearnership(Env.getCtx(), id, null)));

		initLearnerProgrammeTab("QCTO Learnerships", I_ZZLearnerQCTOLearnership.Table_Name,
								"LEFT JOIN ZZQctoLearnership q ON q.ZZQctoLearnership_ID = ZZLearnerQCTOLearnership.ZZQctoLearnership_ID",
								false,
								selectableProgrammeColumns(	I_ZZLearnerQCTOLearnership.Table_Name,
															I_ZZQctoLearnership.Table_Name, I_ZZLearnerQCTOLearnership.COLUMNNAME_ZZQctoLearnership_ID,
															I_ZZQctoLearnership.COLUMNNAME_ZZQctoLearnership_ID,
															I_ZZQctoLearnership.COLUMNNAME_ZZLearnershipCode, I_ZZQctoLearnership.COLUMNNAME_ZZLearnershipTitle,
															id -> new X_ZZQctoLearnership(Env.getCtx(), id, null)));

		initLearnerProgrammeTab("QCTO Skills Programmes", I_ZZLearnerQCTOSkillsProgramme.Table_Name,
								"LEFT JOIN ZZQctoSkillsProgramme q ON q.ZZQctoSkillsProgramme_ID = ZZLearnerQCTOSkillsProgramme.ZZQctoSkillsProgramme_ID",
								false,
								selectableProgrammeColumns(	I_ZZLearnerQCTOSkillsProgramme.Table_Name,
															I_ZZQctoSkillsProgramme.Table_Name,
															I_ZZLearnerQCTOSkillsProgramme.COLUMNNAME_ZZQctoSkillsProgramme_ID,
															I_ZZQctoSkillsProgramme.COLUMNNAME_ZZQctoSkillsProgramme_ID,
															I_ZZQctoSkillsProgramme.COLUMNNAME_ZZSkillsProgrammeCode,
															I_ZZQctoSkillsProgramme.COLUMNNAME_ZZSkillsProgrammeTitle,
															id -> new X_ZZQctoSkillsProgramme(Env.getCtx(), id, null)));

		initLearnerProgrammeTab("Learnerships", I_ZZLearnerLearnership.Table_Name,
								"LEFT JOIN ZZLearnership p ON p.ZZLearnership_ID = ZZLearnerLearnership.ZZLearnership_ID",
								false,
								selectableProgrammeColumns(	I_ZZLearnerLearnership.Table_Name,
															I_ZZLearnership.Table_Name, I_ZZLearnerLearnership.COLUMNNAME_ZZLearnership_ID,
															I_ZZLearnership.COLUMNNAME_ZZLearnership_ID,
															I_ZZLearnership.COLUMNNAME_ZZLearnershipCode, I_ZZLearnership.COLUMNNAME_ZZLearnershipTitle,
															id -> new X_ZZLearnership(Env.getCtx(), id, null)));

		initLearnerProgrammeTab("Skills Programmes", I_ZZLearnerSkillsProgramme.Table_Name,
								"LEFT JOIN ZZSkillsProgramme p ON p.ZZSkillsProgramme_ID = ZZLearnerSkillsProgramme.ZZSkillsProgramme_ID",
								false,
								selectableProgrammeColumns(	I_ZZLearnerSkillsProgramme.Table_Name,
															I_ZZSkillsProgramme.Table_Name, I_ZZLearnerSkillsProgramme.COLUMNNAME_ZZSkillsProgramme_ID,
															I_ZZSkillsProgramme.COLUMNNAME_ZZSkillsProgramme_ID,
															I_ZZSkillsProgramme.COLUMNNAME_ZZSkillsProgrammeCode,
															I_ZZSkillsProgramme.COLUMNNAME_ZZSkillsProgrammeTitle,
															id -> new X_ZZSkillsProgramme(Env.getCtx(), id, null)));
	}

	private List<ColumnModel> selectableProgrammeColumns(	String learnerTable, String programmeTable,
															String learnerProgrammeId, String programmeIdColumn, String codeColumn, String titleColumn,
															Function<Integer, PO> programmeLoader)
	{
		List<ColumnModel> cols = new ArrayList<>();

		boolean isQctoArtisans = I_ZZLearnerQCTOArtisans.Table_Name.equals(learnerTable);
		String rawHeaderLabel = isQctoArtisans ? "Artisan Code" : Msg.getElement(Env.getCtx(), codeColumn);
		String codeHeaderLabel = rawHeaderLabel != null ? rawHeaderLabel.replaceAll("SkillsProgramme", "Skills Programme") : rawHeaderLabel;

		ValueAdaptColumnModel programmeCol = ValueAdaptCellModel.getValueAdaptColumnModel(	codeHeaderLabel, learnerProgrammeId,
																							CellModel.SEARCH_CELL);
		programmeCol.setTableName(learnerTable);
		programmeCol.required();

		String sdpColName = I_ZZLearnerQCTOLearnership.COLUMNNAME_ZZ_SDP_ID;
		String employerColName = I_ZZLearnerQCTOLearnership.COLUMNNAME_ZZ_Employer_ID;

		ValueAdaptColumnModel sdpCol = ValueAdaptCellModel.getValueAdaptColumnModel(Msg.getElement(Env.getCtx(), sdpColName),
																					sdpColName, CellModel.SEARCH_CELL);
		sdpCol.setTableName(learnerTable).required();

		ValueAdaptColumnModel employerCol = ValueAdaptCellModel.getValueAdaptColumnModel(	Msg.getElement(Env.getCtx(), employerColName),
																							employerColName, CellModel.SEARCH_CELL);
		employerCol.setTableName(learnerTable).required();

		// Add SDP column first, then Programme Code (switched column order), then Employer
		cols.add(sdpCol);
		cols.add(programmeCol);
		cols.add(employerCol);

		ColumnModel titleCol = label(programmeTable, titleColumn);
		cols.add(titleCol);

		String programmeWhereClause = buildProgrammeBaseWhereClause(learnerTable, programmeTable);

		configureProgrammeSelector(	sdpCol, null,
									id -> new MBPartner(Env.getCtx(), id, null), I_C_BPartner.COLUMNNAME_C_BPartner_ID,
									I_C_BPartner.COLUMNNAME_Name, I_C_BPartner.COLUMNNAME_Name, I_C_BPartner.Table_Name,
									" C_BPartner.IsActive ='Y' AND C_BPartner.ZZ_Is_SDP='Y'",
									programmeCol, learnerTable);
		configureProgrammeSelector(	programmeCol, titleCol, programmeLoader, programmeIdColumn, codeColumn, titleColumn,
									programmeTable, programmeWhereClause, sdpCol, learnerTable);
		configureProgrammeSelector(	employerCol, null,
									id -> new MBPartner(Env.getCtx(), id, null), I_C_BPartner.COLUMNNAME_C_BPartner_ID,
									I_C_BPartner.COLUMNNAME_Name, I_C_BPartner.COLUMNNAME_Name, I_C_BPartner.Table_Name,
									"C_BPartner.IsActive ='Y' AND C_BPartner.C_BPartner_ID IN  (SELECT AD_User.C_BPartner_ID FROM AD_User WHERE AD_User.IsActive ='Y')",
									null, learnerTable);

		String studentColumn = learnerTable.equals(I_ZZLearnerQCTOLearnership.Table_Name) ? I_ZZLearnerQCTOLearnership.COLUMNNAME_ZZStudentNumber
						: learnerTable.equals(I_ZZLearnerQCTOSkillsProgramme.Table_Name) ? I_ZZLearnerQCTOSkillsProgramme.COLUMNNAME_ZZStudentNumber
							: learnerTable.equals(I_ZZLearnerLearnership.Table_Name) ? I_ZZLearnerLearnership.COLUMNNAME_ZZStudentNumber
							: learnerTable.equals(I_ZZLearnerQCTOArtisans.Table_Name) ? I_ZZLearnerQCTOArtisans.COLUMNNAME_ZZStudentNumber
							: I_ZZLearnerSkillsProgramme.COLUMNNAME_ZZStudentNumber;

		cols.add(text(learnerTable, studentColumn));
		ColumnModel startDateCol = editableDate(learnerTable, "ZZCommencementDate").required();
		startDateCol.setTitle("Start Date");
		cols.add(startDateCol);
		cols.add(editableDate(learnerTable, "ZZCompletionDate"));

		return cols;
	}

	private String buildProgrammeBaseWhereClause(String learnerTable, String programmeTable)
	{
		boolean isQctoArtisans = I_ZZLearnerQCTOArtisans.Table_Name.equals(learnerTable);
		String programmeWhereClause = programmeTable + ".IsActive = 'Y' AND (" + programmeTable + ".ZZLastEnrolmentDate IS NULL OR " + programmeTable + ".ZZLastEnrolmentDate >= CURRENT_DATE)";

		if (isQctoArtisans)
		{
			programmeWhereClause += " AND (ZZQctoLearnership.ZZArtisanLearnership = 'Y' OR ZZQctoLearnership.ZZQctoQualification_ID IN (SELECT q.ZZQctoQualification_ID FROM ZZQctoQualification q WHERE q.ZZArtisanQualification = 'Y'))";
		}
		else if (I_ZZLearnerQCTOLearnership.Table_Name.equals(learnerTable))
		{
			programmeWhereClause += """
							 AND ZZQctoLearnership.ZZQualification_ID IN (    select zzlinkassessorqualification.ZZQualification_ID    from zzlinkassessorqualification
														inner join zzassessorperson on zzassessorperson.zzassessorperson_id = zzlinkassessorqualification.ZZAssessorPerson_ID
														where zzassessorperson.ZZ_DocStatus = 'AP'       and zzlinkassessorqualification.ZZQualification_ID IS NOT NULL) """;
		}
		else if (I_ZZLearnerQCTOSkillsProgramme.Table_Name.equals(learnerTable))
		{
			programmeWhereClause += """
							 AND ZZQctoSkillsProgramme.ZZQctoSkillsProgramme_ID IN (select ZZQctoSkillsProgramme_ID from zzlinkassessorskillsprogramme
																where zzassessorperson_id in (	select zzassessorperson_id from zzassessorperson
																								where ZZ_DocStatus='AP') AND ZZQctoSkillsProgramme_ID IS NOT NULL ) """;
		}
		else if (I_ZZLearnerLearnership.Table_Name.equals(learnerTable))
		{
			programmeWhereClause += """
							 AND ZZLearnership.ZZQualification_ID IN (
								select zzlinkassessorqualification.ZZQualification_ID    from zzlinkassessorqualification
								inner join zzassessorperson        on zzassessorperson.zzassessorperson_id = zzlinkassessorqualification.ZZAssessorPerson_ID
								where zzassessorperson.ZZ_DocStatus = 'AP'       and zzlinkassessorqualification.ZZQualification_ID IS NOT NULL)""";
		}
		else if (I_ZZLearnerSkillsProgramme.Table_Name.equals(learnerTable))
		{
			programmeWhereClause += """
							 AND ZZSkillsProgramme.zzskillsprogramme_ID IN (select zzskillsprogramme_id from zzlinkassessorskillsprogramme
																		where zzassessorperson_id in (	select zzassessorperson_id from zzassessorperson
																										where ZZ_DocStatus='AP') AND zzskillsprogramme_id IS NOT NULL ) """;
		}
		return programmeWhereClause;
	}

	private ColumnModel label(String tableName, String columnName)
	{
		return CellModel.getColModelForLabel(MasterUtil.getNameOfColTranslated(tableName, columnName), columnName)
						.setTableName(tableName).setReadonly(true);
	}

	private ColumnModel text(String tableName, String columnName)
	{
		return CellModel.getColModelForText(MasterUtil.getNameOfColTranslated(tableName, columnName), columnName).setTableName(tableName);
	}

	private ColumnModel editableDate(String tableName, String columnName)
	{
		return DateCellModel.getDateColumnModel(MasterUtil.getNameOfColTranslated(tableName, columnName), columnName).setTableName(tableName);
	}

	private void configureProgrammeSelector(ValueAdaptColumnModel programmeCol, ColumnModel titleCol, Function<Integer, PO> programmeLoader,
											String idColumn, String codeColumn, String titleColumn,
											String programmeTable, String whereClause, ColumnModel progColRef, String learnerTable)
	{
		programmeCol.setDisplayAdaptHandle(value -> {
			if (value == null)
				return null;
			PO po = (PO) value;
			Object val = po.get_Value(codeColumn);
			if (val != null && !val.toString().isBlank())
				return val;
			Object name = po.get_Value("Name");
			if (name != null && !name.toString().isBlank())
				return name;
			Object code = po.get_Value("ZZProviderCode");
			if (code != null && !code.toString().isBlank())
				return code;
			return po.toString();
		});
		programmeCol.setValueAdaptHandle(value -> value == null ? null : ((PO) value).get_ID());
		programmeCol.setValueFromDaoAdaptHandle(value -> {
			if (value == null || Integer.class.cast(value) == 0)
				return null;
			return programmeLoader.apply(Integer.class.cast(value));
		});
		programmeCol.setEventHandle((event, cellModel) -> {
			InfoPanelPara para = InfoPanelPara.getInstance(programmeTable, idColumn);
			String finalWhereClause = whereClause;

			if (progColRef != null && learnerTable != null && cellModel.getRowModel() != null)
			{
				CellModel progCell = cellModel.getRowModel().get(progColRef);
				if (progCell != null && progCell.getValue() != null)
				{
					int selectedRefId = 0;
					Object val = progCell.getValue();
					if (val instanceof PO)
					{
						selectedRefId = ((PO) val).get_ID();
					}
					else if (val instanceof Number)
					{
						selectedRefId = ((Number) val).intValue();
					}

					if (selectedRefId > 0)
					{
						if (I_C_BPartner.Table_Name.equals(programmeTable))
						{
							finalWhereClause = buildSdpLookupWhereClauseForSelectedProgramme(learnerTable, selectedRefId, whereClause);
						}
						else
						{
							finalWhereClause = buildProgrammeLookupWhereClauseForSelectedSdp(learnerTable, programmeTable, selectedRefId, whereClause);
						}
					}
				}
			}

			if (finalWhereClause != null && !finalWhereClause.isBlank())
			{
				para.setWhereClause(finalWhereClause);
			}
			showInfoPanel(para, (obj, infoPanel) -> {
				Object[] values = (Object[]) obj;
				PO selected = programmeLoader.apply((int) values[0]);
				cellModel.setValue(selected);
				cellModel.resetValidate();
				if (titleCol != null)
				{
					cellModel.getRowModel().get(titleCol).setValue(selected.get_Value(titleColumn));
				}
			});
		});
	}

	/**
	 * Builds the SQL WHERE clause for filtering Skills Development Providers (SDP C_BPartner) based on the selected programme in the row.
	 * Checks accreditation status (ZZ_Status = 'AC'), active record state, and unexpired EndDate across BP sub-tables (C_BP_Trades, C_BP_OC, C_BP_SkillsProgramme).
	 * 
	 * @param learnerTableName Name of the learner child table (e.g. ZZLearnerSkillsProgramme)
	 * @param selectedProgrammeId ID of the programme selected in the row
	 * @param defaultWhereClause Default base WHERE clause
	 * @return SQL WHERE clause for SDP lookup
	 */
	private String buildSdpLookupWhereClauseForSelectedProgramme(String learnerTableName, int selectedProgrammeId, String defaultWhereClause)
	{
		StringBuilder sqlWhere = new StringBuilder(defaultWhereClause != null && !defaultWhereClause.isBlank() ? defaultWhereClause
																												: "C_BPartner.IsActive ='Y' AND C_BPartner.ZZ_Is_SDP='Y'");
		if (I_ZZLearnerQCTOArtisans.Table_Name.equals(learnerTableName))
		{
			sqlWhere.append(" AND C_BPartner.C_BPartner_ID IN (SELECT C_BP_Trades.C_BPartner_ID FROM C_BP_Trades WHERE C_BP_Trades.ZZQctoQualification_ID IN (SELECT q.ZZQctoQualification_ID FROM ZZQctoLearnership q WHERE q.ZZQctoLearnership_ID = ")
					.append(selectedProgrammeId)
					.append(") AND C_BP_Trades.IsActive = 'Y' AND C_BP_Trades.ZZ_Status = 'AC' AND (C_BP_Trades.EndDate IS NULL OR C_BP_Trades.EndDate >= CURRENT_DATE))");
		}
		else if (I_ZZLearnerQCTOLearnership.Table_Name.equals(learnerTableName))
		{
			sqlWhere.append(" AND C_BPartner.C_BPartner_ID IN (SELECT bp.C_BPartner_ID FROM C_BP_OC bp WHERE bp.ZZQctoQualification_ID IN (SELECT q.ZZQctoQualification_ID FROM ZZQctoLearnership q WHERE q.ZZQctoLearnership_ID = ")
					.append(selectedProgrammeId)
					.append(") AND bp.IsActive = 'Y' AND bp.ZZ_Status = 'AC' AND (bp.EndDate IS NULL OR bp.EndDate >= CURRENT_DATE))");
		}
		else if (I_ZZLearnerLearnership.Table_Name.equals(learnerTableName))
		{
			sqlWhere.append(" AND C_BPartner.C_BPartner_ID IN (SELECT bp.C_BPartner_ID FROM C_BP_OC bp WHERE bp.ZZQualification_ID IN (SELECT q.ZZQualification_ID FROM ZZLearnership q WHERE q.ZZLearnership_ID = ")
					.append(selectedProgrammeId)
					.append(") AND bp.IsActive = 'Y' AND bp.ZZ_Status = 'AC' AND (bp.EndDate IS NULL OR bp.EndDate >= CURRENT_DATE))");
		}
		else if (I_ZZLearnerQCTOSkillsProgramme.Table_Name.equals(learnerTableName))
		{
			sqlWhere.append(" AND C_BPartner.C_BPartner_ID IN (SELECT C_BP_SkillsProgramme.C_BPartner_ID FROM C_BP_SkillsProgramme WHERE C_BP_SkillsProgramme.ZZQctoSkillsProgramme_ID = ")
					.append(selectedProgrammeId)
					.append(" AND C_BP_SkillsProgramme.IsActive = 'Y' AND C_BP_SkillsProgramme.ZZ_Status = 'AC' AND (C_BP_SkillsProgramme.EndDate IS NULL OR C_BP_SkillsProgramme.EndDate >= CURRENT_DATE))");
		}
		else if (I_ZZLearnerSkillsProgramme.Table_Name.equals(learnerTableName))
		{
			sqlWhere.append(" AND C_BPartner.C_BPartner_ID IN (SELECT C_BP_SkillsProgramme.C_BPartner_ID FROM C_BP_SkillsProgramme WHERE C_BP_SkillsProgramme.ZZSkillsProgramme_ID = ")
					.append(selectedProgrammeId)
					.append(" AND C_BP_SkillsProgramme.IsActive = 'Y' AND C_BP_SkillsProgramme.ZZ_Status = 'AC' AND (C_BP_SkillsProgramme.EndDate IS NULL OR C_BP_SkillsProgramme.EndDate >= CURRENT_DATE))");
		}
		return sqlWhere.toString();
	}

	/**
	 * Builds the SQL WHERE clause for filtering Programme options based on the selected SDP (C_BPartner_ID).
	 * Ensures the selected SDP is certified for the programme (status = 'AC', active, unexpired EndDate).
	 * 
	 * @param learnerTableName Name of the learner child table (e.g. ZZLearnerSkillsProgramme)
	 * @param programmeTableName Name of the target programme table (e.g. ZZSkillsProgramme)
	 * @param selectedSdpBPartnerId BPartner ID of the selected SDP
	 * @param defaultWhereClause Default base WHERE clause
	 * @return SQL WHERE clause for Programme lookup
	 */
	private String buildProgrammeLookupWhereClauseForSelectedSdp(String learnerTableName, String programmeTableName, int selectedSdpBPartnerId, String defaultWhereClause)
	{
		StringBuilder sqlWhere = new StringBuilder(defaultWhereClause != null && !defaultWhereClause.isBlank() ? defaultWhereClause : programmeTableName + ".IsActive = 'Y'");
		if (I_ZZLearnerQCTOArtisans.Table_Name.equals(learnerTableName))
		{
			sqlWhere.append(" AND ")
					.append(programmeTableName)
					.append(".ZZQctoQualification_ID IN (SELECT t.ZZQctoQualification_ID FROM C_BP_Trades t WHERE t.C_BPartner_ID = ")
					.append(selectedSdpBPartnerId)
					.append(" AND t.IsActive = 'Y' AND t.ZZ_Status = 'AC' AND (t.EndDate IS NULL OR t.EndDate >= CURRENT_DATE))");
		}
		else if (I_ZZLearnerQCTOLearnership.Table_Name.equals(learnerTableName))
		{
			sqlWhere.append(" AND ")
					.append(programmeTableName)
					.append(".ZZQctoQualification_ID IN (SELECT bp.ZZQctoQualification_ID FROM C_BP_OC bp WHERE bp.C_BPartner_ID = ")
					.append(selectedSdpBPartnerId)
					.append(" AND bp.IsActive = 'Y' AND bp.ZZ_Status = 'AC' AND (bp.EndDate IS NULL OR bp.EndDate >= CURRENT_DATE))");
		}
		else if (I_ZZLearnerLearnership.Table_Name.equals(learnerTableName))
		{
			sqlWhere.append(" AND ")
					.append(programmeTableName)
					.append(".ZZQualification_ID IN (SELECT bp.ZZQualification_ID FROM C_BP_OC bp WHERE bp.C_BPartner_ID = ")
					.append(selectedSdpBPartnerId)
					.append(" AND bp.IsActive = 'Y' AND bp.ZZ_Status = 'AC' AND (bp.EndDate IS NULL OR bp.EndDate >= CURRENT_DATE))");
		}
		else if (I_ZZLearnerQCTOSkillsProgramme.Table_Name.equals(learnerTableName))
		{
			sqlWhere.append(" AND ")
					.append(programmeTableName)
					.append(".ZZQctoSkillsProgramme_ID IN (SELECT sp.ZZQctoSkillsProgramme_ID FROM C_BP_SkillsProgramme sp WHERE sp.C_BPartner_ID = ")
					.append(selectedSdpBPartnerId)
					.append(" AND sp.IsActive = 'Y' AND sp.ZZ_Status = 'AC' AND (sp.EndDate IS NULL OR sp.EndDate >= CURRENT_DATE))");
		}
		else if (I_ZZLearnerSkillsProgramme.Table_Name.equals(learnerTableName))
		{
			sqlWhere.append(" AND ")
					.append(programmeTableName)
					.append(".ZZSkillsProgramme_ID IN (SELECT sp.ZZSkillsProgramme_ID FROM C_BP_SkillsProgramme sp WHERE sp.C_BPartner_ID = ")
					.append(selectedSdpBPartnerId)
					.append(" AND sp.IsActive = 'Y' AND sp.ZZ_Status = 'AC' AND (sp.EndDate IS NULL OR sp.EndDate >= CURRENT_DATE))");
		}
		return sqlWhere.toString();
	}

	private PO getLearnerProgramme(PO child, String learnerTable)
	{
		int programmeId = 0;
		if (Objects.isNull(child))
			return null;
		if (I_ZZLearnerQCTOArtisans.Table_Name.equals(learnerTable))
		{
			programmeId = child.get_ValueAsInt(I_ZZLearnerQCTOArtisans.COLUMNNAME_ZZQualification_ID);
			if (programmeId <= 0)
				programmeId = child.get_ValueAsInt(I_ZZLearnerQCTOArtisans.COLUMNNAME_ZZQctoLearnership_ID);
		}
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
		if (I_ZZLearnerQCTOArtisans.Table_Name.equals(learnerTable))
			return new X_ZZQctoQualification(Env.getCtx(), programmeId, null);
		if (I_ZZLearnerQCTOLearnership.Table_Name.equals(learnerTable))
			return new X_ZZQctoLearnership(Env.getCtx(), programmeId, null);
		if (I_ZZLearnerQCTOSkillsProgramme.Table_Name.equals(learnerTable))
			return new X_ZZQctoSkillsProgramme(Env.getCtx(), programmeId, null);
		if (I_ZZLearnerLearnership.Table_Name.equals(learnerTable))
			return new X_ZZLearnership(Env.getCtx(), programmeId, null);
		if (I_ZZLearnerSkillsProgramme.Table_Name.equals(learnerTable))
			return new X_ZZSkillsProgramme(Env.getCtx(), programmeId, null);
		return null;
	}

	private List<PO> getChildRelatedRecords(PO child, String learnerTable)
	{
		List<PO> relatedRecords = new ArrayList<>();
		PO programme = getLearnerProgramme(child, learnerTable);
		if (programme != null)
			relatedRecords.add(programme);

		return relatedRecords;
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
			String where = I_ZZCompletedAssessments_v.Table_Name.equals(tableName)	? I_ZZCompletedAssessments_v.COLUMNNAME_ZZLearner_ID + " = ?"
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
					row.addAll(getChildRelatedRecords(child, tableName));
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
					if (event.po().get_ID() <= 0 || event.po().get_ValueAsInt(I_ZZLearnerQCTOArtisans.COLUMNNAME_ZZLearner_ID) <= 0)
						event.po().set_ValueOfColumn(I_ZZLearnerQCTOArtisans.COLUMNNAME_ZZLearner_ID, learner.getZZLearner_ID());
				}
				PO programme = getLearnerProgramme(event.po(), tableName);
				if (programme != null)
				{
					int credits = programme.get_ValueAsInt("ZZCredits");
					if (credits > 0)
						event.po().set_ValueOfColumn("ZZCredits", credits);
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
						throw new AdempiereException("A person with this ID Type and ID Number already exists in the system.");
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
