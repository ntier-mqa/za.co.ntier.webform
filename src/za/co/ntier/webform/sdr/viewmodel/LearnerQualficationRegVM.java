package za.co.ntier.webform.sdr.viewmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.X_C_BPartner;
import org.compiere.model.MBPartner;
import org.compiere.model.MTable;
import org.compiere.model.MUser;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.ValueNamePair;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.Listitem;

import za.co.ntier.api.model.X_ZZLearner;
import za.co.ntier.api.model.X_ZZLearnerLearnership;
import za.co.ntier.api.model.X_ZZLearnerQCTOArtisans;
import za.co.ntier.api.model.X_ZZLearnerQCTOLearnership;
import za.co.ntier.api.model.X_ZZLearnerQCTOSkillsProgramme;
import za.co.ntier.api.model.X_ZZLearnerSkillsProgramme;
import za.co.ntier.api.model.X_ZZLearner_v;
import za.co.ntier.api.model.X_ZZLearnership;
import za.co.ntier.api.model.X_ZZPerson;
import za.co.ntier.api.model.X_ZZQctoLearnership;
import za.co.ntier.api.model.X_ZZQctoSkillsProgramme;
import za.co.ntier.api.model.X_ZZSkillsProgramme;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.form.bean.component.FormInfo;
import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.ISaveForm;
import za.co.ntier.webform.sdr.component.bean.RowModel.RowData;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.TableModel.CommandSetting;
import za.co.ntier.webform.sdr.component.bean.TableModel.DaoManage;
import za.co.ntier.webform.sdr.component.bean.TableModel.ViewType;
import za.co.ntier.webform.sdr.component.bean.cell.DateCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.ListCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.ValueAdaptCellModel;
import za.co.ntier.webform.sdr.component.bean.column.ListColumnModel;
import za.co.ntier.webform.sdr.component.bean.column.ValueAdaptColumnModel;

/**
 * ViewModel for Learner Qualification Registration.
 * Follows the step-based flow:
 * 1. Select the Learner and Intervention
 * 2. Fill in details for the selected Intervention and Save
 */
public class LearnerQualficationRegVM extends StepAppVM
{
	private TableModel		tmLearnerSelection;
	private TableModel		tmLearnerSelectionInfo;
	private TableModel		tmRegistration;

	private ValueNamePair	selectedIntervention;
	private X_ZZLearner_v	learnerSelected;
	private X_ZZLearner		learner;
	private X_ZZPerson		personSelected;

	private int				adminBpId;
	private int				loginId;

	private DaoManage		daoManage	= new DaoManage();

	public TableModel getTmLearnerSelection()
	{
		return tmLearnerSelection;
	}

	public void setTmLearnerSelection(TableModel tmLearnerSelection)
	{
		this.tmLearnerSelection = tmLearnerSelection;
	}

	public TableModel getTmLearnerSelectionInfo()
	{
		return tmLearnerSelectionInfo;
	}

	public void setTmLearnerSelectionInfo(TableModel tmLearnerSelectionInfo)
	{
		this.tmLearnerSelectionInfo = tmLearnerSelectionInfo;
	}

	public TableModel getTmRegistration()
	{
		return tmRegistration;
	}

	public void setTmRegistration(TableModel tmRegistration)
	{
		this.tmRegistration = tmRegistration;
	}

	public ValueNamePair getSelectedIntervention()
	{
		return selectedIntervention;
	}

	public X_ZZLearner_v getLearnerSelected()
	{
		return learnerSelected;
	}

	public X_ZZPerson getPersonSelected()
	{
		return personSelected;
	}

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
		List<ISaveForm> saves = new ArrayList<>();
		if (tmRegistration != null)
		{
			saves.add(tmRegistration);
		}
		return saves;
	}

	@Override
	protected void showResult(boolean isSubmit)
	{
		String msg = Msg.getMsg(Env.getCtx(), "ZZLearnerRegisterSuccess", true);
		if (msg == null || msg.isBlank() || "ZZLearnerRegisterSuccess".equals(msg))
		{
			msg = "Registered successfully";
		}
		MasterUtil.showInfoDialog("Success", List.of(msg), MasterUtil.fCloseActiveWindow);
	}

	@Init(superclass = true)
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey)
	MenuContextInfo menuContextInfo)
	{
		loginId = Env.getAD_User_ID(Env.getCtx());
		if (loginId > 0)
		{
			MUser sdpAdmin = new MUser(Env.getCtx(), loginId, null);
			adminBpId = sdpAdmin.getC_BPartner_ID();
		}

		setFormInfo(new FormInfo(menuContextInfo));
		initStep("selectRegistration");
		initLearnerSelection();
		initLearnerSelectionInfo();

		if (menuContextInfo != null && menuContextInfo.getRecordID() > 0)
		{
			int learnerId = menuContextInfo.getRecordID();
			loadLearner(learnerId);

			if (tmLearnerSelection != null && tmLearnerSelection.getRow() != null)
			{
				for (ColumnModel col : tmLearnerSelection.getRow().keySet())
				{
					if (col instanceof ValueAdaptColumnModel && Boolean.FALSE.equals(col.getShowTitle()))
					{
						CellModel cell = tmLearnerSelection.getRow().get(col);
						cell.setValue(learnerSelected != null ? learnerSelected : learner);
					}
				}
			}
		}
	}

	public int getSelectedLearnerId()
	{
		if (learnerSelected != null && learnerSelected.getZZLearner_ID() > 0)
			return learnerSelected.getZZLearner_ID();
		if (learner != null && learner.getZZLearner_ID() > 0)
			return learner.getZZLearner_ID();
		return 0;
	}

	private void loadLearner(int learnerId)
	{
		learner = new X_ZZLearner(Env.getCtx(), learnerId, null);
		if (learner.getZZPerson_ID() > 0)
		{
			personSelected = new X_ZZPerson(Env.getCtx(), learner.getZZPerson_ID(), null);
		}
		try
		{
			learnerSelected = new X_ZZLearner_v(Env.getCtx(), learnerId, null);
		}
		catch (Exception e)
		{
			log.warning("Could not load ZZLearner_v view (" + e.getMessage() + ")");
		}
		if (personSelected != null)
		{
			tmLearnerSelectionInfo.reset(personSelected);
		}
		else if (learnerSelected != null)
		{
			tmLearnerSelectionInfo.reset(learnerSelected);
		}
		BindUtils.postNotifyChange(null, null, tmLearnerSelectionInfo, "rows");
		BindUtils.postNotifyChange(null, null, this, "*");
	}

	public List<ValueNamePair> getFilteredInterventionList()
	{
		List<String> allowedOrder = List.of("QCTO Artisans", "QCTO Learnerships", "QCTO Skills Programmes", "Learnerships", "Skills Programmes");

		List<ValueNamePair> allInterventions = MasterUtil.getLkpInterventionList();
		List<ValueNamePair> filtered = new ArrayList<>();

		for (String target : allowedOrder)
		{
			ValueNamePair found = null;
			if (allInterventions != null)
			{
				for (ValueNamePair vnp : allInterventions)
				{
					if (target.equalsIgnoreCase(vnp.getValue()) || target.equalsIgnoreCase(vnp.getName()))
					{
						found = vnp;
						break;
					}
					if (target.startsWith("Skills Programme") && (vnp.getValue().startsWith("Skills Programme") || vnp.getName().startsWith(
																																			"Skills Programme")))
					{
						found = vnp;
						break;
					}
				}
			}
			if (found != null && !filtered.contains(found))
			{
				filtered.add(found);
			}
			else if (found == null)
			{
				filtered.add(new ValueNamePair(target, target));
			}
		}

		return filtered;
	}

	private String buildSdpLearnerFilter()
	{
		if (adminBpId <= 0 && loginId <= 0)
		{
			return "1=0";
		}

		StringBuilder sb = new StringBuilder();
		sb.append(X_ZZLearner_v.Table_Name).append(".").append(X_ZZLearner_v.COLUMNNAME_IsActive).append(" = 'Y'");
		sb.append(" AND ").append(X_ZZLearner_v.Table_Name).append(".").append(X_ZZLearner_v.COLUMNNAME_ZZLearner_ID).append(" IN (");

		List<String> subQueries = new ArrayList<>();
		if (loginId > 0)
		{
			subQueries.add(String.format("SELECT l.%s FROM %s l WHERE l.%s = %d OR l.CreatedBy = %d",
				X_ZZLearner.COLUMNNAME_ZZLearner_ID, X_ZZLearner.Table_Name,
				X_ZZLearner.COLUMNNAME_ZZ_SDPAdmin_ID, loginId,
				loginId));
		}
		if (adminBpId > 0)
		{
			subQueries.add(String.format("SELECT l.%s FROM %s l WHERE l.%s IN (SELECT AD_User_ID FROM AD_User WHERE C_BPartner_ID = %d) OR l.CreatedBy IN (SELECT AD_User_ID FROM AD_User WHERE C_BPartner_ID = %d)",
				X_ZZLearner.COLUMNNAME_ZZLearner_ID, X_ZZLearner.Table_Name,
				X_ZZLearner.COLUMNNAME_ZZ_SDPAdmin_ID, adminBpId,
				adminBpId));
			subQueries.add(String.format("SELECT %s FROM %s WHERE %s = %d",
				X_ZZLearnerLearnership.COLUMNNAME_ZZLearner_ID, X_ZZLearnerLearnership.Table_Name,
				X_ZZLearnerLearnership.COLUMNNAME_ZZ_SDP_ID, adminBpId));
			subQueries.add(String.format("SELECT %s FROM %s WHERE %s = %d",
				X_ZZLearnerSkillsProgramme.COLUMNNAME_ZZLearner_ID, X_ZZLearnerSkillsProgramme.Table_Name,
				X_ZZLearnerSkillsProgramme.COLUMNNAME_ZZ_SDP_ID, adminBpId));
			subQueries.add(String.format("SELECT %s FROM %s WHERE %s = %d",
				X_ZZLearnerQCTOLearnership.COLUMNNAME_ZZLearner_ID, X_ZZLearnerQCTOLearnership.Table_Name,
				X_ZZLearnerQCTOLearnership.COLUMNNAME_ZZ_SDP_ID, adminBpId));
			subQueries.add(String.format("SELECT %s FROM %s WHERE %s = %d",
				X_ZZLearnerQCTOSkillsProgramme.COLUMNNAME_ZZLearner_ID, X_ZZLearnerQCTOSkillsProgramme.Table_Name,
				X_ZZLearnerQCTOSkillsProgramme.COLUMNNAME_ZZ_SDP_ID, adminBpId));
			subQueries.add(String.format("SELECT %s FROM %s WHERE %s = %d",
				X_ZZLearnerQCTOArtisans.COLUMNNAME_ZZLearner_ID, X_ZZLearnerQCTOArtisans.Table_Name,
				X_ZZLearnerQCTOArtisans.COLUMNNAME_ZZ_SDP_ID, adminBpId));
		}

		sb.append(String.join(" UNION ", subQueries));
		sb.append(")");

		return sb.toString();
	}

	private void initLearnerSelection()
	{
		List<ColumnModel> cols = new ArrayList<>();

		ValueAdaptColumnModel chooseLearnerCol = ValueAdaptCellModel.getValueAdaptColumnModel(
																								MasterUtil.getNameOfColTranslated(	X_ZZLearner.Table_Name,
																																	X_ZZLearner.COLUMNNAME_ZZLearner_ID),
																								null,
																								CellModel.SEARCH_CELL);
		chooseLearnerCol.setShowTitle(false);
		cols.add(chooseLearnerCol);

		chooseLearnerCol.setEventHandle((event, cellModel) -> {
			InfoPanelPara infoPara = InfoPanelPara.getInstance(X_ZZLearner_v.Table_Name, X_ZZLearner_v.COLUMNNAME_ZZLearner_ID);
			String sdpFilter = buildSdpLearnerFilter();
			if (sdpFilter != null && !sdpFilter.isBlank())
			{
				infoPara.setWhereClause(sdpFilter);
			}

			showInfoPanel(infoPara, (obj, infoPanel) -> {
				Object[] objs = (Object[]) obj;
				if (objs == null || objs.length == 0 || objs[0] == null)
					return;
				int learnerIdSelected = objs[0] instanceof Number ? ((Number) objs[0]).intValue() : Integer.parseInt(objs[0].toString());
				loadLearner(learnerIdSelected);
				if (selectedIntervention != null)
				{
					openProgramInfoPanelOrLoad();
				}
			});
		});

		ListColumnModel<ValueNamePair> interventionListCol = ListCellModel.getListColumnModel(
																								Msg.getElement(Env.getCtx(), "ZZInterventionList"),
																								null,
																								getFilteredInterventionList(),
																								title -> title.getName(),
																								title -> title.getValue());
		interventionListCol.setzClass(ValueNamePair.class).required();
		cols.add(interventionListCol);

		ColumnModel actionCol = CellModel.getColModelForGenericCell("", null, CellModel.BUTTON_CELL);
		actionCol.setShowTitle(false);
		cols.add(actionCol);

		interventionListCol.setEventHandle((event, cellModel) -> {
			@SuppressWarnings("unchecked")
			SelectEvent<Listitem, ValueNamePair> selectEvent = (SelectEvent<Listitem, ValueNamePair>) event;
			selectedIntervention = selectEvent.getSelectedObjects().iterator().next();

			CellModel actionCell = cellModel.getRowModel().get(actionCol);
			actionCol.setTitle(selectedIntervention.getName());
			actionCell.setBtText(selectedIntervention.getName());
			actionCell.setVisible(true);
			BindUtils.postNotifyChange(null, null, actionCell, "btText");
			BindUtils.postNotifyChange(null, null, actionCell, "visible");
			BindUtils.postNotifyChange(null, null, tmLearnerSelection, "row");

			if (getSelectedLearnerId() > 0)
			{
				openProgramInfoPanelOrLoad();
			}
		});

		actionCol.setEventHandle((event, cellModel) -> {
			if (getSelectedLearnerId() <= 0)
			{
				MasterUtil.showInfoDialog("Please select a learner first.", null);
				return;
			}
			if (selectedIntervention == null)
			{
				MasterUtil.showInfoDialog("Please select an intervention type.", null);
				return;
			}
			openProgramInfoPanelOrLoad();
		});

		tmLearnerSelection = TableModel.getTableBean(TableModel.class, cols, false, X_ZZLearner_v.Table_Name);
		tmLearnerSelection.setViewModel(ViewType.VIEW_FORM);
		tmLearnerSelection.setSclass("srd-LearnerRegistration-selectLearner");
		tmLearnerSelection.init();

		tmLearnerSelection.getRow().get(actionCol).setVisible(false);
	}

	private void initLearnerSelectionInfo()
	{
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel col = CellModel.getColModelForLabel(
														MasterUtil.getNameOfColTranslated(X_ZZPerson.Table_Name, X_ZZPerson.COLUMNNAME_ZZFirstName),
														X_ZZPerson.COLUMNNAME_ZZFirstName);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(X_ZZPerson.Table_Name, X_ZZPerson.COLUMNNAME_Surname),
											X_ZZPerson.COLUMNNAME_Surname);
		cols.add(col);

		ValueAdaptColumnModel idCol = ValueAdaptCellModel.getValueAdaptColumnModel(
																					MasterUtil.getNameOfColTranslated(	X_ZZPerson.Table_Name,
																														X_ZZPerson.COLUMNNAME_ZZ_ID_Passport_No),
																					X_ZZPerson.COLUMNNAME_ZZ_ID_Passport_No,
																					CellModel.LABEL_CELL);
		idCol.setValueFromDaoAdaptHandle(obj -> {
			String idPassport = (String) obj;
			if (idPassport == null || idPassport.isBlank())
			{
				if (personSelected != null)
				{
					return personSelected.getZZOtherIDNo();
				}
				if (learnerSelected != null)
				{
					return learnerSelected.getZZOtherIDNo();
				}
			}
			return idPassport;
		});
		cols.add(idCol);

		tmLearnerSelectionInfo = TableModel.getTableBean(TableModel.class, cols, false, X_ZZPerson.Table_Name);
		tmLearnerSelectionInfo.setViewModel(ViewType.VIEW_GRID);
		tmLearnerSelectionInfo.setSclass("srd-LearnerRegistration-selectLearnerInfo");
		tmLearnerSelectionInfo.init();
	}

	private String getLearnerTableIdColumn(String interventionValue)
	{
		if ("QCTO Artisans".equals(interventionValue) || X_ZZLearnerQCTOArtisans.Table_Name.equals(interventionValue))
			return X_ZZLearnerQCTOArtisans.COLUMNNAME_ZZLearnerQCTOArtisans_ID;
		if ("QCTO Learnerships".equals(interventionValue) || X_ZZLearnerQCTOLearnership.Table_Name.equals(interventionValue))
			return X_ZZLearnerQCTOLearnership.COLUMNNAME_ZZLearnerQCTOLearnership_ID;
		if ("QCTO Skills Programmes".equals(interventionValue) || "QCTO Skills Programme".equals(interventionValue)
				|| X_ZZLearnerQCTOSkillsProgramme.Table_Name.equals(interventionValue))
			return X_ZZLearnerQCTOSkillsProgramme.COLUMNNAME_ZZLearnerQCTOSkillsProgramme_ID;
		if ("Learnerships".equals(interventionValue) || "Learnership".equals(interventionValue)
				|| X_ZZLearnerLearnership.Table_Name.equals(interventionValue))
			return X_ZZLearnerLearnership.COLUMNNAME_ZZLearnerLearnership_ID;
		if ("Skills Programmes".equals(interventionValue) || "Skills Programme".equals(interventionValue)
				|| X_ZZLearnerSkillsProgramme.Table_Name.equals(interventionValue))
			return X_ZZLearnerSkillsProgramme.COLUMNNAME_ZZSkillsProgramme_ID;
		return null;
	}

	private void openProgramInfoPanelOrLoad()
	{
		showRegistrationStep(0);
	}

	private String getTableNameForIntervention(String interventionValue)
	{
		if ("QCTO Artisans".equals(interventionValue))
			return X_ZZLearnerQCTOArtisans.Table_Name;
		if ("QCTO Learnerships".equals(interventionValue))
			return X_ZZLearnerQCTOLearnership.Table_Name;
		if ("QCTO Skills Programmes".equals(interventionValue) || "QCTO Skills Programme".equals(interventionValue))
			return X_ZZLearnerQCTOSkillsProgramme.Table_Name;
		if ("Learnerships".equals(interventionValue) || "Learnership".equals(interventionValue))
			return X_ZZLearnerLearnership.Table_Name;
		if ("Skills Programmes".equals(interventionValue) || "Skills Programme".equals(interventionValue))
			return X_ZZLearnerSkillsProgramme.Table_Name;
		return null;
	}

	private String getJoinClauseForIntervention(String interventionValue)
	{
		if ("QCTO Artisans".equals(interventionValue))
			return "LEFT JOIN ZZQctoLearnership q ON q.ZZQctoLearnership_ID = ZZLearnerQCTOArtisans.ZZQctoLearnership_ID";
		if ("QCTO Learnerships".equals(interventionValue))
			return "LEFT JOIN ZZQctoLearnership q ON q.ZZQctoLearnership_ID = ZZLearnerQCTOLearnership.ZZQctoLearnership_ID";
		if ("QCTO Skills Programmes".equals(interventionValue) || "QCTO Skills Programme".equals(interventionValue))
			return "LEFT JOIN ZZQctoSkillsProgramme q ON q.ZZQctoSkillsProgramme_ID = ZZLearnerQCTOSkillsProgramme.ZZQctoSkillsProgramme_ID";
		if ("Learnerships".equals(interventionValue) || "Learnership".equals(interventionValue))
			return "LEFT JOIN ZZLearnership p ON p.ZZLearnership_ID = ZZLearnerLearnership.ZZLearnership_ID";
		if ("Skills Programmes".equals(interventionValue) || "Skills Programme".equals(interventionValue))
			return "LEFT JOIN ZZSkillsProgramme p ON p.ZZSkillsProgramme_ID = ZZLearnerSkillsProgramme.ZZSkillsProgramme_ID";
		return "";
	}

	private Function<Integer, PO> getProgrammeLoaderForIntervention(String interventionValue)
	{
		if ("QCTO Artisans".equals(interventionValue) || "QCTO Learnerships".equals(interventionValue))
			return id -> new X_ZZQctoLearnership(Env.getCtx(), id, null);
		if ("QCTO Skills Programmes".equals(interventionValue) || "QCTO Skills Programme".equals(interventionValue))
			return id -> new X_ZZQctoSkillsProgramme(Env.getCtx(), id, null);
		if ("Learnerships".equals(interventionValue) || "Learnership".equals(interventionValue))
			return id -> new X_ZZLearnership(Env.getCtx(), id, null);
		if ("Skills Programmes".equals(interventionValue) || "Skills Programme".equals(interventionValue))
			return id -> new X_ZZSkillsProgramme(Env.getCtx(), id, null);
		return null;
	}

	private String getProgrammeTableForIntervention(String interventionValue)
	{
		if ("QCTO Artisans".equals(interventionValue) || "QCTO Learnerships".equals(interventionValue))
			return X_ZZQctoLearnership.Table_Name;
		if ("QCTO Skills Programmes".equals(interventionValue) || "QCTO Skills Programme".equals(interventionValue))
			return X_ZZQctoSkillsProgramme.Table_Name;
		if ("Learnerships".equals(interventionValue))
			return X_ZZLearnership.Table_Name;
		if ("Skills Programmes".equals(interventionValue) || "Skills Programme".equals(interventionValue))
			return X_ZZSkillsProgramme.Table_Name;
		return null;
	}

	private String getLearnerProgrammeIdColumnForIntervention(String interventionValue)
	{
		if ("QCTO Artisans".equals(interventionValue))
			return X_ZZLearnerQCTOArtisans.COLUMNNAME_ZZQctoLearnership_ID;
		if ("QCTO Learnerships".equals(interventionValue))
			return X_ZZLearnerQCTOLearnership.COLUMNNAME_ZZQctoLearnership_ID;
		if ("QCTO Skills Programmes".equals(interventionValue) || "QCTO Skills Programme".equals(interventionValue))
			return X_ZZLearnerQCTOSkillsProgramme.COLUMNNAME_ZZQctoSkillsProgramme_ID;
		if ("Learnerships".equals(interventionValue))
			return X_ZZLearnerLearnership.COLUMNNAME_ZZLearnership_ID;
		if ("Skills Programmes".equals(interventionValue) || "Skills Programme".equals(interventionValue))
			return X_ZZLearnerSkillsProgramme.COLUMNNAME_ZZSkillsProgramme_ID;
		return null;
	}

	private String getProgrammeIdColumnForIntervention(String interventionValue)
	{
		if ("QCTO Artisans".equals(interventionValue) || "QCTO Learnerships".equals(interventionValue))
			return X_ZZQctoLearnership.COLUMNNAME_ZZQctoLearnership_ID;
		if ("QCTO Skills Programmes".equals(interventionValue) || "QCTO Skills Programme".equals(interventionValue))
			return X_ZZQctoSkillsProgramme.COLUMNNAME_ZZQctoSkillsProgramme_ID;
		if ("Learnerships".equals(interventionValue))
			return X_ZZLearnership.COLUMNNAME_ZZLearnership_ID;
		if ("Skills Programmes".equals(interventionValue) || "Skills Programme".equals(interventionValue))
			return X_ZZSkillsProgramme.COLUMNNAME_ZZSkillsProgramme_ID;
		return null;
	}

	private String getProgrammeCodeColumnForIntervention(String interventionValue)
	{
		if ("QCTO Artisans".equals(interventionValue) || "QCTO Learnerships".equals(interventionValue))
			return X_ZZQctoLearnership.COLUMNNAME_ZZLearnershipCode;
		if ("QCTO Skills Programmes".equals(interventionValue) || "QCTO Skills Programme".equals(interventionValue))
			return X_ZZQctoSkillsProgramme.COLUMNNAME_ZZSkillsProgrammeCode;
		if ("Learnerships".equals(interventionValue))
			return X_ZZLearnership.COLUMNNAME_ZZLearnershipCode;
		if ("Skills Programmes".equals(interventionValue) || "Skills Programme".equals(interventionValue))
			return X_ZZSkillsProgramme.COLUMNNAME_ZZSkillsProgrammeCode;
		return null;
	}

	private String getProgrammeTitleColumnForIntervention(String interventionValue)
	{
		if ("QCTO Artisans".equals(interventionValue) || "QCTO Learnerships".equals(interventionValue))
			return X_ZZQctoLearnership.COLUMNNAME_ZZLearnershipTitle;
		if ("QCTO Skills Programmes".equals(interventionValue) || "QCTO Skills Programme".equals(interventionValue))
			return X_ZZQctoSkillsProgramme.COLUMNNAME_ZZSkillsProgrammeTitle;
		if ("Learnerships".equals(interventionValue))
			return X_ZZLearnership.COLUMNNAME_ZZLearnershipTitle;
		if ("Skills Programmes".equals(interventionValue) || "Skills Programme".equals(interventionValue))
			return X_ZZSkillsProgramme.COLUMNNAME_ZZSkillsProgrammeTitle;
		return null;
	}

	private void showRegistrationStep(int selectedRecordId)
	{
		if (selectedIntervention == null)
			return;

		String val = selectedIntervention.getValue();
		String tableName = getTableNameForIntervention(val);
		String programmeTable = getProgrammeTableForIntervention(val);
		String learnerProgrammeId = getLearnerProgrammeIdColumnForIntervention(val);
		String programmeIdColumn = getProgrammeIdColumnForIntervention(val);
		String codeColumn = getProgrammeCodeColumnForIntervention(val);
		String titleColumn = getProgrammeTitleColumnForIntervention(val);
		Function<Integer, PO> programmeLoader = getProgrammeLoaderForIntervention(val);
		String joinClause = getJoinClauseForIntervention(val);
		String primaryKeyColumn = getLearnerTableIdColumn(val);

		List<ColumnModel> cols = selectableProgrammeColumns(tableName, programmeTable, learnerProgrammeId,
															programmeIdColumn, codeColumn, titleColumn, programmeLoader);

		tmRegistration = TableModel.getTableBean(TableModel.class, cols, false, tableName);
		tmRegistration.setViewModel(ViewType.VIEW_GRID);
		tmRegistration.setSclass("srd-learner-" + selectedIntervention.getName().toLowerCase().replace(' ', '-'));
		tmRegistration.setPoSupplier(row -> createLearnerChildRecord(tableName));
		tmRegistration.setCommandSetting(CommandSetting.getOnlyRemoveButton());
		tmRegistration.setCreateNewRowWhenEmpty(false);
		tmRegistration.init();

		tmRegistration.setRowReadonlyLogic(rowModel -> {
			if (rowModel == null || rowModel.getRowData() == null)
				return false;
			PO child = rowModel.getRowData().getDataNullable(tableName);
			return child != null && child.get_ID() > 0;
		});

		tmRegistration.setRowStyleLogic(rowModel -> {
			if (rowModel != null && !rowModel.isEditable())
			{
				return "pointer-events: none; opacity: 0.85; background-color: #f5f5f7;";
			}
			return "";
		});

		tmRegistration.setRowSaveFilter(rowModel -> rowModel != null && rowModel.isEditable());

		tmRegistration.setLoadSavedDataHandle(model -> {
			if (getSelectedLearnerId() <= 0)
				return;
			String where = tableName + "." + X_ZZLearnerQCTOArtisans.COLUMNNAME_ZZLearner_ID + " = ?";
			Query query;
			if (selectedRecordId > 0 && primaryKeyColumn != null)
			{
				where += " AND " + tableName + "." + primaryKeyColumn + " = ?";
				query = MTable.get(Env.getCtx(), tableName).createQuery(where, null);
				if (!joinClause.isBlank())
					query.addJoinClause(joinClause);
				query.setParameters(getSelectedLearnerId(), selectedRecordId);
			}
			else
			{
				query = MTable.get(Env.getCtx(), tableName).createQuery(where, null);
				if (!joinClause.isBlank())
					query.addJoinClause(joinClause);
				query.setParameters(getSelectedLearnerId());
			}

			List<PO> rows = query.list();
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

			// Always display a new line for the user to fill
			model.addNewRow(null);
		});

		tmRegistration.setBeforeSave(event -> {
			if (event.row() != null && !event.row().isEditable())
			{
				return true;
			}
			if (event.isPOEven() && event.po() != null && tableName.equals(event.po().get_TableName()) && getSelectedLearnerId() > 0)
			{
				if (event.po().get_ID() <= 0 || event.po().get_ValueAsInt(X_ZZLearnerQCTOArtisans.COLUMNNAME_ZZLearner_ID) <= 0)
					event.po().set_ValueOfColumn(X_ZZLearnerQCTOArtisans.COLUMNNAME_ZZLearner_ID, getSelectedLearnerId());
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

		tmRegistration.reloadDao();
		tmRegistration.loadSavedData();

		setStep("registration");
		BindUtils.postNotifyChange(null, null, this, "tmRegistration");
	}

	@Command
	@NotifyChange("tmRegistration")
	public void addRegistrationRow()
	{
		if (tmRegistration != null)
		{
			tmRegistration.addNewRow(null);
			BindUtils.postNotifyChange(null, null, tmRegistration, "rows");
		}
	}

	@Command
	public void backToSelection()
	{
		setStep("selectRegistration");
	}

	private List<ColumnModel> selectableProgrammeColumns(	String learnerTable, String programmeTable,
															String learnerProgrammeId, String programmeIdColumn, String codeColumn, String titleColumn,
															Function<Integer, PO> programmeLoader)
	{
		List<ColumnModel> cols = new ArrayList<>();

		boolean isQctoArtisans = X_ZZLearnerQCTOArtisans.Table_Name.equals(learnerTable);
		String rawHeaderLabel = isQctoArtisans ? "Artisan Code" : Msg.getElement(Env.getCtx(), codeColumn);
		String codeHeaderLabel = rawHeaderLabel != null ? rawHeaderLabel.replaceAll("SkillsProgramme", "Skills Programme") : rawHeaderLabel;

		ValueAdaptColumnModel programmeCol = ValueAdaptCellModel.getValueAdaptColumnModel(	codeHeaderLabel, learnerProgrammeId,
																							CellModel.SEARCH_CELL);
		programmeCol.setTableName(learnerTable);
		programmeCol.required();

		String sdpColName = X_ZZLearnerQCTOLearnership.COLUMNNAME_ZZ_SDP_ID;
		String employerColName = X_ZZLearnerQCTOLearnership.COLUMNNAME_ZZ_Employer_ID;

		ValueAdaptColumnModel sdpCol = ValueAdaptCellModel.getValueAdaptColumnModel(Msg.getElement(Env.getCtx(), sdpColName),
																					sdpColName, CellModel.SEARCH_CELL);
		sdpCol.setTableName(learnerTable).required();

		ValueAdaptColumnModel employerCol = ValueAdaptCellModel.getValueAdaptColumnModel(	Msg.getElement(Env.getCtx(), employerColName),
																							employerColName, CellModel.SEARCH_CELL);
		employerCol.setTableName(learnerTable).required();

		cols.add(sdpCol);
		cols.add(programmeCol);
		cols.add(employerCol);

		ColumnModel titleCol = label(programmeTable, titleColumn);
		cols.add(titleCol);

		String programmeWhereClause = buildProgrammeBaseWhereClause(learnerTable, programmeTable);

		configureProgrammeSelector(	sdpCol, null,
									id -> new MBPartner(Env.getCtx(), id, null), X_C_BPartner.COLUMNNAME_C_BPartner_ID,
									X_C_BPartner.COLUMNNAME_Name, X_C_BPartner.COLUMNNAME_Name, X_C_BPartner.Table_Name,
									" C_BPartner.IsActive ='Y' AND C_BPartner.ZZ_Is_SDP='Y'",
									programmeCol, learnerTable);
		configureProgrammeSelector(	programmeCol, titleCol, programmeLoader, programmeIdColumn, codeColumn, titleColumn,
									programmeTable, programmeWhereClause, sdpCol, learnerTable);
		configureProgrammeSelector(	employerCol, null,
									id -> new MBPartner(Env.getCtx(), id, null), X_C_BPartner.COLUMNNAME_C_BPartner_ID,
									X_C_BPartner.COLUMNNAME_Name, X_C_BPartner.COLUMNNAME_Name, X_C_BPartner.Table_Name,
									"C_BPartner.IsActive ='Y' AND C_BPartner.C_BPartner_ID IN  (SELECT AD_User.C_BPartner_ID FROM AD_User WHERE AD_User.IsActive ='Y')",
									null, learnerTable);

		String studentColumn = learnerTable.equals(X_ZZLearnerQCTOLearnership.Table_Name) ? X_ZZLearnerQCTOLearnership.COLUMNNAME_ZZStudentNumber
						: learnerTable.equals(X_ZZLearnerQCTOSkillsProgramme.Table_Name) ? X_ZZLearnerQCTOSkillsProgramme.COLUMNNAME_ZZStudentNumber
							: learnerTable.equals(X_ZZLearnerLearnership.Table_Name) ? X_ZZLearnerLearnership.COLUMNNAME_ZZStudentNumber
							: learnerTable.equals(X_ZZLearnerQCTOArtisans.Table_Name) ? X_ZZLearnerQCTOArtisans.COLUMNNAME_ZZStudentNumber
							: X_ZZLearnerSkillsProgramme.COLUMNNAME_ZZStudentNumber;

		cols.add(text(learnerTable, studentColumn));
		ColumnModel startDateCol = editableDate(learnerTable, "ZZCommencementDate").required();
		startDateCol.setTitle("Start Date");
		cols.add(startDateCol);
		cols.add(editableDate(learnerTable, "ZZCompletionDate"));

		return cols;
	}

	private String buildProgrammeBaseWhereClause(String learnerTable, String programmeTable)
	{
		boolean isQctoArtisans = X_ZZLearnerQCTOArtisans.Table_Name.equals(learnerTable);
		String programmeWhereClause = programmeTable	+ ".IsActive = 'Y' AND (" + programmeTable + ".ZZLastEnrolmentDate IS NULL OR " + programmeTable
										+ ".ZZLastEnrolmentDate >= CURRENT_DATE)";

		if (isQctoArtisans)
		{
			programmeWhereClause += " AND (ZZQctoLearnership.ZZArtisanLearnership = 'Y' OR ZZQctoLearnership.ZZQctoQualification_ID IN (SELECT q.ZZQctoQualification_ID FROM ZZQctoQualification q WHERE q.ZZArtisanQualification = 'Y'))";
		}
		else if (X_ZZLearnerQCTOLearnership.Table_Name.equals(learnerTable))
		{
			programmeWhereClause += """
							AND ZZQctoLearnership.ZZQualification_ID IN (    select zzlinkassessorqualification.ZZQualification_ID    from zzlinkassessorqualification
													inner join zzassessorperson on zzassessorperson.zzassessorperson_id = zzlinkassessorqualification.ZZAssessorPerson_ID
													where zzassessorperson.ZZ_DocStatus = 'AP'       and zzlinkassessorqualification.ZZQualification_ID IS NOT NULL) """;
		}
		else if (X_ZZLearnerQCTOSkillsProgramme.Table_Name.equals(learnerTable))
		{
			programmeWhereClause += """
							AND ZZQctoSkillsProgramme.ZZQctoSkillsProgramme_ID IN (select ZZQctoSkillsProgramme_ID from zzlinkassessorskillsprogramme
															where zzassessorperson_id in (	select zzassessorperson_id from zzassessorperson
																							where ZZ_DocStatus='AP') AND ZZQctoSkillsProgramme_ID IS NOT NULL ) """;
		}
		else if (X_ZZLearnerLearnership.Table_Name.equals(learnerTable))
		{
			programmeWhereClause += """
							AND ZZLearnership.ZZQualification_ID IN (
							select zzlinkassessorqualification.ZZQualification_ID    from zzlinkassessorqualification
							inner join zzassessorperson        on zzassessorperson.zzassessorperson_id = zzlinkassessorqualification.ZZAssessorPerson_ID
							where zzassessorperson.ZZ_DocStatus = 'AP'       and zzlinkassessorqualification.ZZQualification_ID IS NOT NULL)""";
		}
		else if (X_ZZLearnerSkillsProgramme.Table_Name.equals(learnerTable))
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
			if (value == null)
				return null;
			int id = value instanceof Number ? ((Number) value).intValue() : Integer.parseInt(value.toString());
			if (id <= 0)
				return null;
			return programmeLoader.apply(id);
		});
		programmeCol.setEventHandle((event, cellModel) -> {
			if (cellModel.getRowModel() != null && !cellModel.getRowModel().isEditable())
			{
				return;
			}
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
						if (X_C_BPartner.Table_Name.equals(programmeTable))
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
				if (values == null || values.length == 0 || values[0] == null)
					return;
				int selectedId = values[0] instanceof Number ? ((Number) values[0]).intValue() : Integer.parseInt(values[0].toString());
				PO selected = programmeLoader.apply(selectedId);
				cellModel.setValue(selected);
				cellModel.resetValidate();
				if (titleCol != null)
				{
					cellModel.getRowModel().get(titleCol).setValue(selected.get_Value(titleColumn));
				}
			});
		});
	}

	private String buildSdpLookupWhereClauseForSelectedProgramme(String learnerTableName, int selectedProgrammeId, String defaultWhereClause)
	{
		StringBuilder sqlWhere = new StringBuilder(defaultWhereClause != null && !defaultWhereClause.isBlank()	? defaultWhereClause
																												: "C_BPartner.IsActive ='Y' AND C_BPartner.ZZ_Is_SDP='Y'");
		if (X_ZZLearnerQCTOArtisans.Table_Name.equals(learnerTableName))
		{
			sqlWhere.append(" AND C_BPartner.C_BPartner_ID IN (SELECT C_BP_Trades.C_BPartner_ID FROM C_BP_Trades WHERE C_BP_Trades.ZZQctoQualification_ID IN (SELECT q.ZZQctoQualification_ID FROM ZZQctoLearnership q WHERE q.ZZQctoLearnership_ID = ")
					.append(selectedProgrammeId)
					.append(") AND C_BP_Trades.IsActive = 'Y' AND C_BP_Trades.ZZ_Status = 'AC' AND (C_BP_Trades.EndDate IS NULL OR C_BP_Trades.EndDate >= CURRENT_DATE))");
		}
		else if (X_ZZLearnerQCTOLearnership.Table_Name.equals(learnerTableName))
		{
			sqlWhere.append(" AND C_BPartner.C_BPartner_ID IN (SELECT bp.C_BPartner_ID FROM C_BP_OC bp WHERE bp.ZZQctoQualification_ID IN (SELECT q.ZZQctoQualification_ID FROM ZZQctoLearnership q WHERE q.ZZQctoLearnership_ID = ")
					.append(selectedProgrammeId)
					.append(") AND bp.IsActive = 'Y' AND bp.ZZ_Status = 'AC' AND (bp.EndDate IS NULL OR bp.EndDate >= CURRENT_DATE))");
		}
		else if (X_ZZLearnerLearnership.Table_Name.equals(learnerTableName))
		{
			sqlWhere.append(" AND C_BPartner.C_BPartner_ID IN (SELECT bp.C_BPartner_ID FROM C_BP_OC bp WHERE bp.ZZQualification_ID IN (SELECT q.ZZQualification_ID FROM ZZLearnership q WHERE q.ZZLearnership_ID = ")
					.append(selectedProgrammeId)
					.append(") AND bp.IsActive = 'Y' AND bp.ZZ_Status = 'AC' AND (bp.EndDate IS NULL OR bp.EndDate >= CURRENT_DATE))");
		}
		else if (X_ZZLearnerQCTOSkillsProgramme.Table_Name.equals(learnerTableName))
		{
			sqlWhere.append(" AND C_BPartner.C_BPartner_ID IN (SELECT C_BP_SkillsProgramme.C_BPartner_ID FROM C_BP_SkillsProgramme WHERE C_BP_SkillsProgramme.ZZQctoSkillsProgramme_ID = ")
					.append(selectedProgrammeId)
					.append(" AND C_BP_SkillsProgramme.IsActive = 'Y' AND C_BP_SkillsProgramme.ZZ_Status = 'AC' AND (C_BP_SkillsProgramme.EndDate IS NULL OR C_BP_SkillsProgramme.EndDate >= CURRENT_DATE))");
		}
		else if (X_ZZLearnerSkillsProgramme.Table_Name.equals(learnerTableName))
		{
			sqlWhere.append(" AND C_BPartner.C_BPartner_ID IN (SELECT C_BP_SkillsProgramme.C_BPartner_ID FROM C_BP_SkillsProgramme WHERE C_BP_SkillsProgramme.ZZSkillsProgramme_ID = ")
					.append(selectedProgrammeId)
					.append(" AND C_BP_SkillsProgramme.IsActive = 'Y' AND C_BP_SkillsProgramme.ZZ_Status = 'AC' AND (C_BP_SkillsProgramme.EndDate IS NULL OR C_BP_SkillsProgramme.EndDate >= CURRENT_DATE))");
		}
		return sqlWhere.toString();
	}

	private String buildProgrammeLookupWhereClauseForSelectedSdp(	String learnerTableName, String programmeTableName, int selectedSdpBPartnerId,
																	String defaultWhereClause)
	{
		StringBuilder sqlWhere = new StringBuilder(defaultWhereClause != null && !defaultWhereClause.isBlank()	? defaultWhereClause
																												: programmeTableName + ".IsActive = 'Y'");
		if (X_ZZLearnerQCTOArtisans.Table_Name.equals(learnerTableName))
		{
			sqlWhere.append(" AND ")
					.append(programmeTableName)
					.append(".ZZQctoQualification_ID IN (SELECT t.ZZQctoQualification_ID FROM C_BP_Trades t WHERE t.C_BPartner_ID = ")
					.append(selectedSdpBPartnerId)
					.append(" AND t.IsActive = 'Y' AND t.ZZ_Status = 'AC' AND (t.EndDate IS NULL OR t.EndDate >= CURRENT_DATE))");
		}
		else if (X_ZZLearnerQCTOLearnership.Table_Name.equals(learnerTableName))
		{
			sqlWhere.append(" AND ")
					.append(programmeTableName)
					.append(".ZZQctoQualification_ID IN (SELECT bp.ZZQctoQualification_ID FROM C_BP_OC bp WHERE bp.C_BPartner_ID = ")
					.append(selectedSdpBPartnerId)
					.append(" AND bp.IsActive = 'Y' AND bp.ZZ_Status = 'AC' AND (bp.EndDate IS NULL OR bp.EndDate >= CURRENT_DATE))");
		}
		else if (X_ZZLearnerLearnership.Table_Name.equals(learnerTableName))
		{
			sqlWhere.append(" AND ")
					.append(programmeTableName)
					.append(".ZZQualification_ID IN (SELECT bp.ZZQualification_ID FROM C_BP_OC bp WHERE bp.C_BPartner_ID = ")
					.append(selectedSdpBPartnerId)
					.append(" AND bp.IsActive = 'Y' AND bp.ZZ_Status = 'AC' AND (bp.EndDate IS NULL OR bp.EndDate >= CURRENT_DATE))");
		}
		else if (X_ZZLearnerQCTOSkillsProgramme.Table_Name.equals(learnerTableName))
		{
			sqlWhere.append(" AND ")
					.append(programmeTableName)
					.append(".ZZQctoSkillsProgramme_ID IN (SELECT sp.ZZQctoSkillsProgramme_ID FROM C_BP_SkillsProgramme sp WHERE sp.C_BPartner_ID = ")
					.append(selectedSdpBPartnerId)
					.append(" AND sp.IsActive = 'Y' AND sp.ZZ_Status = 'AC' AND (sp.EndDate IS NULL OR sp.EndDate >= CURRENT_DATE))");
		}
		else if (X_ZZLearnerSkillsProgramme.Table_Name.equals(learnerTableName))
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
		if (X_ZZLearnerQCTOArtisans.Table_Name.equals(learnerTable))
		{
			programmeId = child.get_ValueAsInt(X_ZZLearnerQCTOArtisans.COLUMNNAME_ZZQctoLearnership_ID);
			if (programmeId <= 0)
				programmeId = child.get_ValueAsInt(X_ZZLearnerQCTOArtisans.COLUMNNAME_ZZQualification_ID);
		}
		else if (X_ZZLearnerQCTOLearnership.Table_Name.equals(learnerTable))
			programmeId = child.get_ValueAsInt(X_ZZLearnerQCTOLearnership.COLUMNNAME_ZZQctoLearnership_ID);
		else if (X_ZZLearnerQCTOSkillsProgramme.Table_Name.equals(learnerTable))
			programmeId = child.get_ValueAsInt(X_ZZLearnerQCTOSkillsProgramme.COLUMNNAME_ZZQctoSkillsProgramme_ID);
		else if (X_ZZLearnerLearnership.Table_Name.equals(learnerTable))
			programmeId = child.get_ValueAsInt(X_ZZLearnerLearnership.COLUMNNAME_ZZLearnership_ID);
		else if (X_ZZLearnerSkillsProgramme.Table_Name.equals(learnerTable))
			programmeId = child.get_ValueAsInt(X_ZZLearnerSkillsProgramme.COLUMNNAME_ZZSkillsProgramme_ID);
		if (programmeId <= 0)
			return null;
		if (X_ZZLearnerQCTOArtisans.Table_Name.equals(learnerTable))
			return new X_ZZQctoLearnership(Env.getCtx(), programmeId, null);
		if (X_ZZLearnerQCTOLearnership.Table_Name.equals(learnerTable))
			return new X_ZZQctoLearnership(Env.getCtx(), programmeId, null);
		if (X_ZZLearnerQCTOSkillsProgramme.Table_Name.equals(learnerTable))
			return new X_ZZQctoSkillsProgramme(Env.getCtx(), programmeId, null);
		if (X_ZZLearnerLearnership.Table_Name.equals(learnerTable))
			return new X_ZZLearnership(Env.getCtx(), programmeId, null);
		if (X_ZZLearnerSkillsProgramme.Table_Name.equals(learnerTable))
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

	private PO createLearnerChildRecord(String tableName)
	{
		PO record;
		if (X_ZZLearnerQCTOArtisans.Table_Name.equals(tableName))
		{
			record = new X_ZZLearnerQCTOArtisans(Env.getCtx(), 0, null);
		}
		else if (X_ZZLearnerQCTOLearnership.Table_Name.equals(tableName))
		{
			record = new X_ZZLearnerQCTOLearnership(Env.getCtx(), 0, null);
		}
		else if (X_ZZLearnerQCTOSkillsProgramme.Table_Name.equals(tableName))
		{
			record = new X_ZZLearnerQCTOSkillsProgramme(Env.getCtx(), 0, null);
		}
		else if (X_ZZLearnerLearnership.Table_Name.equals(tableName))
		{
			record = new X_ZZLearnerLearnership(Env.getCtx(), 0, null);
		}
		else if (X_ZZLearnerSkillsProgramme.Table_Name.equals(tableName))
		{
			record = new X_ZZLearnerSkillsProgramme(Env.getCtx(), 0, null);
		}
		else
		{
			throw new AdempiereException("Unsupported learner child table: " + tableName);
		}
		if (getSelectedLearnerId() > 0)
		{
			record.set_ValueOfColumn(X_ZZLearnerQCTOArtisans.COLUMNNAME_ZZLearner_ID, getSelectedLearnerId());
		}
		return record;
	}

	@Override
	public void doSave(String trxName)
	{
		if (getSelectedLearnerId() <= 0)
		{
			throw new AdempiereException("Please select a learner before saving.");
		}
		int currentUserId = Env.getAD_User_ID(Env.getCtx());
		if (learner != null && currentUserId > 0)
		{
			learner.setZZ_SDPAdmin_ID(currentUserId);
			learner.saveEx(trxName);
		}
		super.doSave(trxName);
	}

	@Override
	public void doSubmit(String trxName)
	{
		int currentUserId = Env.getAD_User_ID(Env.getCtx());
		if (learner != null && currentUserId > 0)
		{
			learner.setZZ_SDPAdmin_ID(currentUserId);
			learner.saveEx(trxName);
		}
		super.doSubmit(trxName);
	}

	@Override
	public boolean isSupportSubmit()
	{
		return false;
	}

	@Override
	public boolean isSupportSave()
	{
		return true;
	}

	@Command
	public void save()
	{
		saveApp();
	}

	@Command
	public void cancelApp()
	{
		MasterUtil.closeActiveWindow();
	}

	@Command
	public void cancel()
	{
		cancelApp();
	}
}
