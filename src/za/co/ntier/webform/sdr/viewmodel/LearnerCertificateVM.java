package za.co.ntier.webform.sdr.viewmodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.ValueNamePair;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.Listitem;

import za.co.ntier.api.model.I_ZZCompletedAssessments_v;
import za.co.ntier.api.model.I_ZZLearnerLearnership;
import za.co.ntier.api.model.I_ZZLearnerLearnershipAssessments;
import za.co.ntier.api.model.I_ZZLearnerQCTOArtisans;
import za.co.ntier.api.model.I_ZZLearnerQCTOLearnership;
import za.co.ntier.api.model.I_ZZLearnerQCTOSkillsProgramme;
import za.co.ntier.api.model.I_ZZLearnerQCTOSkillsProgrammeAssessments;
import za.co.ntier.api.model.I_ZZLearnerQctoLearnershipAssessments;
import za.co.ntier.api.model.I_ZZLearnerSkillsProgramme;
import za.co.ntier.api.model.I_ZZLearnerSkillsProgrammeAssessments;
import za.co.ntier.api.model.I_ZZLearner_v;
import za.co.ntier.api.model.I_ZZLearnership;
import za.co.ntier.api.model.I_ZZLearnershipUnitStandard;
import za.co.ntier.api.model.I_ZZQctoLearnership;
import za.co.ntier.api.model.I_ZZQctoModule;
import za.co.ntier.api.model.I_ZZQctoSkillsProgramme;
import za.co.ntier.api.model.I_ZZQctoSkillsProgrammeModule;
import za.co.ntier.api.model.I_ZZSkillsProgramme;
import za.co.ntier.api.model.I_ZZSkillsProgrammeUnitStandard;
import za.co.ntier.api.model.I_ZZUnitStandard;
import za.co.ntier.api.model.X_ZZCompletedAssessments_v;
import za.co.ntier.api.model.X_ZZLearner;
import za.co.ntier.api.model.X_ZZLearnerLearnership;
import za.co.ntier.api.model.X_ZZLearnerLearnershipAssessments;
import za.co.ntier.api.model.X_ZZLearnerQCTOArtisans;
import za.co.ntier.api.model.X_ZZLearnerQCTOLearnership;
import za.co.ntier.api.model.X_ZZLearnerQCTOSkillsProgramme;
import za.co.ntier.api.model.X_ZZLearnerQCTOSkillsProgrammeAssessments;
import za.co.ntier.api.model.X_ZZLearnerQctoLearnershipAssessments;
import za.co.ntier.api.model.X_ZZLearnerSkillsProgramme;
import za.co.ntier.api.model.X_ZZLearnerSkillsProgrammeAssessments;
import za.co.ntier.api.model.X_ZZLearner_v;
import za.co.ntier.api.model.X_ZZLearnership;
import za.co.ntier.api.model.X_ZZLearnershipUnitStandard;
import za.co.ntier.api.model.X_ZZQctoLearnership;
import za.co.ntier.api.model.X_ZZQctoLearnershipModule;
import za.co.ntier.api.model.X_ZZQctoModule;
import za.co.ntier.api.model.X_ZZQctoSkillsProgramme;
import za.co.ntier.api.model.X_ZZSkillsProgramme;
import za.co.ntier.api.model.X_ZZSkillsProgrammeUnitStandard;
import za.co.ntier.api.model.X_ZZUnitStandard;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.RowModel.RowData;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.TableModel.ViewType;
import za.co.ntier.webform.sdr.component.bean.cell.CheckboxCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.ListCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.ValueAdaptCellModel;
import za.co.ntier.webform.sdr.component.bean.column.CheckboxColumnModel;
import za.co.ntier.webform.sdr.component.bean.column.ListColumnModel;
import za.co.ntier.webform.sdr.component.bean.column.ValueAdaptColumnModel;

public class LearnerCertificateVM extends StepAppVM
{
	private TableModel	tmLearnerSelection;
	private TableModel	tmLearnerSelectionInfo;
	ValueNamePair		selectedIntervention;

	private boolean		canDownload;

	public boolean isCanDownload()
	{
		return canDownload;
	}

	@Command
	public void downloadCertificate()
	{
		MasterUtil.showInfoDialog("SOR/Certificate download functionality will be implemented here.", null);
	}

	public TableModel getTmLearnerSelectionInfo()
	{
		return tmLearnerSelectionInfo;
	}

	public void setTmLearnerSelectionInfo(TableModel tmLearnerSelectionInfo)
	{
		this.tmLearnerSelectionInfo = tmLearnerSelectionInfo;
	}

	@Override
	public List<String> getSteps()
	{
		List<String> parentSteps = super.getSteps();
		if (parentSteps != null && parentSteps.contains("assessment"))
		{
			return Arrays.asList("selectAssessment", "assessment");
		}
		return parentSteps;
	}

	public TableModel getTmLearnerSelection()
	{
		return tmLearnerSelection;
	}

	public TableModel getTmInterventionSelection()
	{
		return tmInterventionSelection;
	}

	public void setTmLearnerSelection(TableModel tmLearnerSelection)
	{
		this.tmLearnerSelection = tmLearnerSelection;
	}

	@Init(superclass = true)
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey)
	MenuContextInfo menuContextInfo)
	{
		initStep("selectAssessment");
		initLearnerSelection();
		initInterventionSelection();
		initLearnerSelectionInfo();

		if (menuContextInfo != null && menuContextInfo.getRecordID() > 0)
		{
			int learnerId = menuContextInfo.getRecordID();
			learnerSelected = new X_ZZLearner_v(Env.getCtx(), learnerId, null);
			tmLearnerSelectionInfo.reset(learnerSelected);

			// Set the value in the search cell so it visually appears
			if (tmLearnerSelection != null && tmLearnerSelection.getRow() != null)
			{
				for (ColumnModel col : tmLearnerSelection.getRow().keySet())
				{
					if (col instanceof ValueAdaptColumnModel && Boolean.FALSE.equals(col.getShowTitle()))
					{
						CellModel cell = tmLearnerSelection.getRow().get(col);
						cell.setValue(learnerSelected);
					}
				}
			}
		}
	}

	X_ZZLearner_v learnerSelected;

	boolean isInterventionQCTOArtisans()
	{
		return "QCTO Artisans".equals(selectedIntervention.getValue());
	}

	boolean isInterventionQCTOLearnerships()
	{
		return "QCTO Learnerships".equals(selectedIntervention.getValue());
	}

	boolean isInterventionQCTOSkills()
	{
		return "QCTO Skills Programmes".equals(selectedIntervention.getValue());
	}

	boolean isInterventionSkillsProgrammes()
	{
		return "Skills Programmes".equals(selectedIntervention.getValue());
	}

	boolean isInterventionLearnerships()
	{
		return "Learnerships".equals(selectedIntervention.getValue());
	}

	X_ZZLearnerQCTOArtisans			learnerQCTOArtisans;
	X_ZZQctoLearnership				qctoArtisans;

	X_ZZLearnerQCTOLearnership		learnerQCTOLearnership;
	X_ZZQctoLearnership				qctoLearnership;

	X_ZZLearnerLearnership			learnerLearnership;
	X_ZZLearnership					learnership;

	X_ZZLearnerQCTOSkillsProgramme	learnerQCTOSkills;
	X_ZZQctoSkillsProgramme			qctoSkills;

	public void initLearnerSelection()
	{
		List<ColumnModel> cols = new ArrayList<>();

		ValueAdaptColumnModel chooseLearnerCol = ValueAdaptCellModel.getValueAdaptColumnModel(
																								"Select Learner",
																								null,
																								CellModel.SEARCH_CELL);

		chooseLearnerCol.setShowTitle(false);
		cols.add(chooseLearnerCol);

		chooseLearnerCol.setEventHandle((event, cellModel) -> {
			showInfoPanel(
							InfoPanelPara.getInstance(I_ZZLearner_v.Table_Name, I_ZZLearner_v.COLUMNNAME_ZZLearner_ID), (obj, infoPanel) -> {
								Object[] objs = (Object[]) obj;
								int learnerIdSelected = (int) objs[0];
								learnerSelected = new X_ZZLearner_v(Env.getCtx(), learnerIdSelected, null);

								tmLearnerSelectionInfo.reset(learnerSelected);

								selectedIntervention = null;
								if (tmInterventionSelection != null && tmInterventionSelection.getRow() != null)
								{
									List<ColumnModel> intCols = tmInterventionSelection.getColumnInfos();
									tmInterventionSelection.getRow().get(intCols.get(0)).setValue(null);
									tmInterventionSelection.getRow().get(intCols.get(1)).setVisible(false);
								}
								initStep("selectAssessment");
								BindUtils.postNotifyChange(null, null, LearnerCertificateVM.this, "steps");

								// Make intervention table visible by notifying binder
								BindUtils.postNotifyChange(null, null, LearnerCertificateVM.this, "learnerSelected");
							});
		});

		tmLearnerSelection = TableModel.getTableBean(TableModel.class, cols, false, I_ZZLearner_v.Table_Name);
		tmLearnerSelection.setViewModel(ViewType.VIEW_FORM);
		tmLearnerSelection.setSclass("srd-LearnerAssessment-selectLearner");
		tmLearnerSelection.init();
	}

	TableModel tmInterventionSelection;

	public void initInterventionSelection()
	{
		List<ColumnModel> cols = new ArrayList<>();

		List<ValueNamePair> legacyInterventions = MasterUtil.getLkpInterventionList().stream()
															.filter(vnp -> !vnp.getValue().contains("QCTO"))
															.collect(Collectors.toList());

		ListColumnModel<ValueNamePair> interventionListCol = ListCellModel.getListColumnModel(
																								Msg.getElement(Env.getCtx(), "ZZInterventionList"), null,
																								legacyInterventions, title -> {
																									return title.getName();
																								}, title -> {
																									return title.getValue();
																								});
		interventionListCol.setTableName(I_ZZLearner_v.Table_Name);

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
			actionCol.setTitle("Select " + selectedIntervention.getName());
			actionCell.setVisible(true);
		});

		actionCol.setEventHandle((event, cellModel) -> {
			if (isInterventionQCTOArtisans())
			{
				showInfoPanelForIntervention(I_ZZLearnerQCTOArtisans.Table_Name, I_ZZLearnerQCTOArtisans.COLUMNNAME_ZZLearnerQCTOArtisans_ID);
			}
			else if (isInterventionQCTOLearnerships())
			{
				showInfoPanelForIntervention(I_ZZLearnerQCTOLearnership.Table_Name, I_ZZLearnerQCTOLearnership.COLUMNNAME_ZZLearnerQCTOLearnership_ID);
			}
			else if (isInterventionQCTOSkills())
			{
				showInfoPanelForIntervention(	I_ZZLearnerQCTOSkillsProgramme.Table_Name,
												I_ZZLearnerQCTOSkillsProgramme.COLUMNNAME_ZZLearnerQCTOSkillsProgramme_ID);
			}
			else if (isInterventionSkillsProgrammes())
			{
				showInfoPanelForIntervention(I_ZZLearnerSkillsProgramme.Table_Name, I_ZZLearnerSkillsProgramme.COLUMNNAME_ZZLearnerSkillsProgramme_ID);
			}
			else if (isInterventionLearnerships())
			{
				showInfoPanelForIntervention(I_ZZLearnerLearnership.Table_Name, I_ZZLearnerLearnership.COLUMNNAME_ZZLearnerLearnership_ID);
			}
		});

		tmInterventionSelection = TableModel.getTableBean(TableModel.class, cols, false, I_ZZLearner_v.Table_Name);
		tmInterventionSelection.setViewModel(ViewType.VIEW_FORM);
		tmInterventionSelection.setSclass("srd-LearnerAssessment-selectIntervention");
		tmInterventionSelection.init();

		tmInterventionSelection.getRow().get(actionCol).setVisible(false);
	}

	public X_ZZLearner_v getLearnerSelected()
	{
		return learnerSelected;
	}

	void initLearnerSelectionInfo()
	{
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel col = CellModel.getColModelForLabel(
														MasterUtil.getNameOfColTranslated(I_ZZLearner_v.Table_Name, I_ZZLearner_v.COLUMNNAME_ZZFirstName),
														I_ZZLearner_v.COLUMNNAME_ZZFirstName);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(I_ZZLearner_v.Table_Name, I_ZZLearner_v.COLUMNNAME_Surname),
											I_ZZLearner_v.COLUMNNAME_Surname);
		cols.add(col);

		ValueAdaptColumnModel idCol = ValueAdaptCellModel.getValueAdaptColumnModel(
											MasterUtil.getNameOfColTranslated(I_ZZLearner_v.Table_Name, I_ZZLearner_v.COLUMNNAME_ZZ_ID_Passport_No),
											I_ZZLearner_v.COLUMNNAME_ZZ_ID_Passport_No,
											CellModel.LABEL_CELL);
		idCol.setValueFromDaoAdaptHandle(obj -> {
			String idPassport = (String) obj;
			if (idPassport == null || idPassport.isBlank()) {
				if (learnerSelected != null) {
					return learnerSelected.getZZOtherIDNo();
				}
			}
			return idPassport;
		});
		cols.add(idCol);

		tmLearnerSelectionInfo = TableModel.getTableBean(TableModel.class, cols, false, I_ZZLearner_v.Table_Name);
		tmLearnerSelectionInfo.setViewModel(ViewType.VIEW_GRID);
		tmLearnerSelectionInfo.setSclass("srd-LearnerAssessment-selectLearnerInfo");
		tmLearnerSelectionInfo.init();
	}

	void showInfoPanelForIntervention(String tableName, String columnId)
	{
		String whereClause = I_ZZLearnerQCTOArtisans.COLUMNNAME_ZZLearner_ID + " = " + learnerSelected.getZZLearner_ID();
		showInfoPanel(
						InfoPanelPara.getInstance(tableName, columnId).setWhereClause(whereClause), (obj, infoPanel) -> {
							Object[] objs = (Object[]) obj;
							showAssessmentSteo((int) objs[0]);

						});
	}

	void showAssessmentSteo(int id)
	{
		canDownload = false;

		if (isInterventionQCTOArtisans())
		{
			learnerQCTOArtisans = new X_ZZLearnerQCTOArtisans(Env.getCtx(), id, null);
			qctoArtisans = new X_ZZQctoLearnership(Env.getCtx(), learnerQCTOArtisans.getZZQctoLearnership_ID(), null);
			if (tmLearnerQCTOArtisans == null)
				initLearnerQCTOArtisans();

			tmLearnerQCTOArtisans.resetMultiPo(List.of(List.of(learnerQCTOArtisans, qctoArtisans)));
		}
		else if (isInterventionQCTOLearnerships())
		{
			learnerQCTOLearnership = new X_ZZLearnerQCTOLearnership(Env.getCtx(), id, null);
			qctoLearnership = new X_ZZQctoLearnership(Env.getCtx(), learnerQCTOLearnership.getZZQctoLearnership_ID(), null);
			if (tmLearnerQCTOLearnerships == null)
				initLearnerQCTOLearnership();

			tmLearnerQCTOLearnerships.resetMultiPo(List.of(List.of(learnerQCTOLearnership, qctoLearnership)));
		}
		else if (isInterventionQCTOSkills())
		{
			learnerQCTOSkills = new X_ZZLearnerQCTOSkillsProgramme(Env.getCtx(), id, null);
			qctoSkills = new X_ZZQctoSkillsProgramme(Env.getCtx(), learnerQCTOSkills.getZZQctoSkillsProgramme_ID(), null);

			if (tmLearnerQCTOSkills == null)
				initLearnerQCTOSkills();

			tmLearnerQCTOSkills.resetMultiPo(List.of(List.of(learnerQCTOSkills, qctoSkills)));

		}
		else if (isInterventionSkillsProgrammes())
		{
			learnerSkillsProgramme = new X_ZZLearnerSkillsProgramme(Env.getCtx(), id, null);
			skillsProgramme = new X_ZZSkillsProgramme(Env.getCtx(), learnerSkillsProgramme.getZZSkillsProgramme_ID(), null);

			if (tmLearnerSkillsProgrammes == null)
				initLearnerSkillsProgrammes();

			tmLearnerSkillsProgrammes.resetMultiPo(List.of(List.of(learnerSkillsProgramme, skillsProgramme)));

		}
		else if (isInterventionLearnerships())
		{
			learnerLearnership = new X_ZZLearnerLearnership(Env.getCtx(), id, null);
			learnership = new X_ZZLearnership(Env.getCtx(), learnerLearnership.getZZLearnership_ID(), null);
			if (tmLearnerLearnerships == null)
				initLearnerLearnership();

			tmLearnerLearnerships.resetMultiPo(List.of(List.of(learnerLearnership, learnership)));
		}

		initLearnerCompletions();

		X_ZZCompletedAssessments_v completedAssessment = null;

		if (isInterventionQCTOLearnerships())
		{
			completedAssessment = new Query(Env.getCtx(), X_ZZCompletedAssessments_v.Table_Name,
											I_ZZCompletedAssessments_v.COLUMNNAME_ZZLearnerQCTOLearnership_ID + "=?", null).setParameters(id).first();
		}
		else if (isInterventionQCTOSkills())
		{
			completedAssessment = new Query(Env.getCtx(), X_ZZCompletedAssessments_v.Table_Name,
											I_ZZCompletedAssessments_v.COLUMNNAME_ZZLearnerQCTOSkillsProgramme_ID + "=?", null).setParameters(id).first();
		}
		else if (isInterventionSkillsProgrammes())
		{
			completedAssessment = new Query(Env.getCtx(), X_ZZCompletedAssessments_v.Table_Name,
											I_ZZCompletedAssessments_v.COLUMNNAME_ZZLearnerSkillsProgramme_ID + "=?", null).setParameters(id).first();
		}
		else if (isInterventionLearnerships())
		{
			completedAssessment = new Query(Env.getCtx(), X_ZZCompletedAssessments_v.Table_Name, I_ZZCompletedAssessments_v.COLUMNNAME_ZZLearnerLearnership_ID
																									+ "=?", null).setParameters(id).first();
		}

		if (completedAssessment != null)
		{
			if (completedAssessment.get_Value(I_ZZCompletedAssessments_v.COLUMNNAME_ZZFirstName) == null)
			{
				completedAssessment.set_ValueNoCheck(I_ZZCompletedAssessments_v.COLUMNNAME_ZZFirstName, learnerSelected.getZZFirstName());
				completedAssessment.set_ValueNoCheck(I_ZZCompletedAssessments_v.COLUMNNAME_Surname, learnerSelected.getSurname());
				
				String idPassport = learnerSelected.getZZ_ID_Passport_No();
				if (idPassport == null || idPassport.isBlank()) {
					idPassport = learnerSelected.getZZOtherIDNo();
				}
				completedAssessment.set_ValueNoCheck(I_ZZCompletedAssessments_v.COLUMNNAME_ZZ_ID_Passport_No, idPassport);
			}

			List<PO> dummyRow = new ArrayList<>();
			dummyRow.add(completedAssessment);
			tmLearnerCompletions.resetMultiPo(List.of(dummyRow));

			String status = completedAssessment.getZZ_DocStatus();
			if (status == null || !status.equalsIgnoreCase(X_ZZLearner.ZZ_DOCSTATUS_Completed))
			{
				tmLearnerCompletions.getRow().get(downloadActionCol).setVisible(false);
			}
			else
			{
				canDownload = true;
			}
		}
		else
		{
			tmLearnerCompletions.resetMultiPo(new ArrayList<>());
			tmLearnerCompletions.getRow().get(downloadActionCol).setVisible(false);
		}

		BindUtils.postNotifyChange(null, null, this, "*");

		if (isInterventionQCTOLearnerships())
		{
			if (tmQctoLearnershipAssessments == null)
			{
				initQctoLearnershipAssessments();
			}

			int[] moduleIds = DB.getIDsEx(null, String.format(	"SELECT %s FROM %s WHERE %s = ?", X_ZZQctoModule.COLUMNNAME_ZZQctoModule_ID,
																X_ZZQctoLearnershipModule.Table_Name,
																X_ZZQctoLearnershipModule.COLUMNNAME_ZZQctoLearnership_ID), learnerQCTOLearnership
																																					.getZZQctoLearnership_ID());

			List<Object> ids = Arrays.stream(moduleIds).boxed().collect(Collectors.toList());
			String placeholders = MasterUtil.createPlaceHoldForInClause(ids);

			Query moduleAssessmentsQuery = MTable.get(Env.getCtx(), X_ZZQctoModule.Table_Name).createQuery(
																											String.format(	"%s IN (%s)",
																															X_ZZQctoModule.COLUMNNAME_ZZQctoModule_ID,
																															placeholders), null);
			moduleAssessmentsQuery.setParameters(ids);
			List<PO> modules = moduleAssessmentsQuery.list();

			Query learnerAssessmentsQuery = MTable.get(Env.getCtx(), I_ZZLearnerQctoLearnershipAssessments.Table_Name).createQuery(
																																	String.format(	"%s IN (%s) AND %s = ?",
																																					X_ZZLearnerQctoLearnershipAssessments.COLUMNNAME_ZZQctoModule_ID,
																																					placeholders,
																																					X_ZZLearnerQctoLearnershipAssessments.COLUMNNAME_ZZLearnerQCTOLearnership_ID),
																																	null);

			List<Object> learnerAssessmentsParas = new ArrayList<Object>(ids);
			learnerAssessmentsParas.add(learnerQCTOLearnership.getZZLearnerQCTOLearnership_ID());
			learnerAssessmentsQuery.setParameters(learnerAssessmentsParas);
			List<PO> learnerAssessments = learnerAssessmentsQuery.list();

			List<List<PO>> loadSavedDatas = RowData.mergedList(modules, learnerAssessments, (po1, po2) -> {
				X_ZZQctoModule module = (X_ZZQctoModule) po1;
				X_ZZLearnerQctoLearnershipAssessments learnerAssessment = (X_ZZLearnerQctoLearnershipAssessments) po2;
				return learnerAssessment.getZZQctoModule_ID() != 0 && learnerAssessment.getZZQctoModule_ID() == module.getZZQctoModule_ID();
			});

			tmQctoLearnershipAssessments.resetMultiPo(loadSavedDatas);

		}
		else if (isInterventionQCTOSkills())
		{
			if (tmQCTOSkillsAssessments == null)
			{
				initQctoSkillsAssessments();
			}

			int[] moduleIds = DB.getIDsEx(null, String.format(	"SELECT %s FROM %s WHERE %s = ?", I_ZZQctoSkillsProgrammeModule.COLUMNNAME_ZZQctoModule_ID,
																I_ZZQctoSkillsProgrammeModule.Table_Name,
																I_ZZQctoSkillsProgrammeModule.COLUMNNAME_ZZQctoSkillsProgramme_ID), learnerQCTOSkills
																																						.getZZQctoSkillsProgramme_ID());

			List<Object> ids = Arrays.stream(moduleIds).boxed().collect(Collectors.toList());
			String placeholders = MasterUtil.createPlaceHoldForInClause(ids);

			Query moduleAssessmentsQuery = MTable.get(Env.getCtx(), X_ZZQctoModule.Table_Name).createQuery(
																											String.format(	"%s IN (%s)",
																															X_ZZQctoModule.COLUMNNAME_ZZQctoModule_ID,
																															placeholders), null);
			moduleAssessmentsQuery.setParameters(ids);
			List<PO> modules = moduleAssessmentsQuery.list();

			Query learnerAssessmentsQuery = MTable.get(Env.getCtx(), I_ZZLearnerQCTOSkillsProgrammeAssessments.Table_Name).createQuery(
																																		String.format(	"%s IN (%s) AND %s = ?",
																																						I_ZZLearnerQCTOSkillsProgrammeAssessments.COLUMNNAME_ZZQctoModule_ID,
																																						placeholders,
																																						I_ZZLearnerQCTOSkillsProgrammeAssessments.COLUMNNAME_ZZLearnerQCTOSkillsProgramme_ID),
																																		null);

			List<Object> learnerAssessmentsParas = new ArrayList<Object>(ids);
			learnerAssessmentsParas.add(learnerQCTOSkills.getZZLearnerQCTOSkillsProgramme_ID());
			learnerAssessmentsQuery.setParameters(learnerAssessmentsParas);
			List<PO> learnerAssessments = learnerAssessmentsQuery.list();

			List<List<PO>> loadSavedDatas = RowData.mergedList(modules, learnerAssessments, (po1, po2) -> {
				X_ZZQctoModule module = (X_ZZQctoModule) po1;
				X_ZZLearnerQCTOSkillsProgrammeAssessments learnerAssessment = (X_ZZLearnerQCTOSkillsProgrammeAssessments) po2;
				return learnerAssessment.getZZQctoModule_ID() != 0 && learnerAssessment.getZZQctoModule_ID() == module.getZZQctoModule_ID();
			});

			tmQCTOSkillsAssessments.resetMultiPo(loadSavedDatas);

		}
		else if (isInterventionSkillsProgrammes())
		{
			if (tmSkillsProgrammeAssessments == null)
			{
				initSkillsProgrammeAssessments();
			}

			int[] moduleIds = DB.getIDsEx(null, String.format(	"SELECT %s FROM %s WHERE %s = ?", I_ZZSkillsProgrammeUnitStandard.COLUMNNAME_ZZUnitStandard_ID,
																I_ZZSkillsProgrammeUnitStandard.Table_Name,
																I_ZZSkillsProgrammeUnitStandard.COLUMNNAME_ZZSkillsProgramme_ID), learnerSkillsProgramme
																																						.getZZSkillsProgramme_ID());

			List<Object> ids = Arrays.stream(moduleIds).boxed().collect(Collectors.toList());
			String placeholders = MasterUtil.createPlaceHoldForInClause(ids);

			Query moduleAssessmentsQuery = MTable.get(Env.getCtx(), X_ZZUnitStandard.Table_Name).createQuery(
																												String.format(	"%s IN (%s)",
																																X_ZZUnitStandard.COLUMNNAME_ZZUnitStandard_ID,
																																placeholders), null);
			moduleAssessmentsQuery.setParameters(ids);
			List<PO> modules = moduleAssessmentsQuery.list();

			Query junctionQuery = MTable.get(Env.getCtx(), I_ZZSkillsProgrammeUnitStandard.Table_Name).createQuery(
																													String.format(	"%s IN (%s) AND %s = ?",
																																	I_ZZSkillsProgrammeUnitStandard.COLUMNNAME_ZZUnitStandard_ID,
																																	placeholders,
																																	I_ZZSkillsProgrammeUnitStandard.COLUMNNAME_ZZSkillsProgramme_ID),
																													null);
			List<Object> junctionParas = new ArrayList<Object>(ids);
			junctionParas.add(learnerSkillsProgramme.getZZSkillsProgramme_ID());
			junctionQuery.setParameters(junctionParas);
			List<PO> junctions = junctionQuery.list();

			Query learnerAssessmentsQuery = MTable.get(Env.getCtx(), I_ZZLearnerSkillsProgrammeAssessments.Table_Name).createQuery(
																																	String.format(	"%s IN (%s) AND %s = ?",
																																					I_ZZLearnerSkillsProgrammeAssessments.COLUMNNAME_ZZUnitStandard_ID,
																																					placeholders,
																																					I_ZZLearnerSkillsProgrammeAssessments.COLUMNNAME_ZZLearnerSkillsProgramme_ID),
																																	null);

			List<Object> learnerAssessmentsParas = new ArrayList<Object>(ids);
			learnerAssessmentsParas.add(learnerSkillsProgramme.getZZLearnerSkillsProgramme_ID());
			learnerAssessmentsQuery.setParameters(learnerAssessmentsParas);
			List<PO> learnerAssessments = learnerAssessmentsQuery.list();

			List<List<PO>> finalLoadSavedDatas = new ArrayList<>();
			for (PO modulePo : modules)
			{
				X_ZZUnitStandard module = (X_ZZUnitStandard) modulePo;

				X_ZZSkillsProgrammeUnitStandard matchingJunction = null;
				for (PO junctionPo : junctions)
				{
					X_ZZSkillsProgrammeUnitStandard junction = (X_ZZSkillsProgrammeUnitStandard) junctionPo;
					if (junction.getZZUnitStandard_ID() != 0 && junction.getZZUnitStandard_ID() == module.getZZUnitStandard_ID())
					{
						matchingJunction = junction;
						break;
					}
				}

				if (matchingJunction != null)
				{
					X_ZZLearnerSkillsProgrammeAssessments matchingAssessment = null;
					for (PO assessmentPo : learnerAssessments)
					{
						X_ZZLearnerSkillsProgrammeAssessments assessment = (X_ZZLearnerSkillsProgrammeAssessments) assessmentPo;
						if (assessment.getZZUnitStandard_ID() != 0 && assessment.getZZUnitStandard_ID() == module.getZZUnitStandard_ID())
						{
							matchingAssessment = assessment;
							break;
						}
					}

					List<PO> row = new ArrayList<>();
					row.add(module);
					row.add(matchingJunction);
					row.add(matchingAssessment != null ? matchingAssessment : new X_ZZLearnerSkillsProgrammeAssessments(Env.getCtx(), 0, null));
					finalLoadSavedDatas.add(row);
				}
			}

			tmSkillsProgrammeAssessments.resetMultiPo(finalLoadSavedDatas);
		}
		else if (isInterventionLearnerships())
		{
			if (tmLearnershipAssessments == null)
			{
				initLearnershipAssessments();
			}

			List<X_ZZLearnershipUnitStandard> mods = new Query(Env.getCtx(), I_ZZLearnershipUnitStandard.Table_Name, I_ZZLearnershipUnitStandard.COLUMNNAME_ZZLearnership_ID + "=?", null)
																																				.setParameters(learnership.getZZLearnership_ID())
																																				.list();

			List<List<PO>> loadSavedDatas = new ArrayList<>();
			for (X_ZZLearnershipUnitStandard mod : mods)
			{
				X_ZZLearnerLearnershipAssessments ass = new Query(	Env.getCtx(), I_ZZLearnerLearnershipAssessments.Table_Name,
																	I_ZZLearnerLearnershipAssessments.COLUMNNAME_ZZLearnerLearnership_ID + "=? AND " + I_ZZLearnerLearnershipAssessments.COLUMNNAME_ZZUnitStandard_ID + "=?", null)
																																.setParameters(	learnerLearnership.getZZLearnerLearnership_ID(),
																																				mod.getZZUnitStandard_ID())
																																.first();

				if (ass == null)
				{
					ass = new X_ZZLearnerLearnershipAssessments(Env.getCtx(), 0, null);
					ass.setZZLearnerLearnership_ID(learnerLearnership.getZZLearnerLearnership_ID());
					ass.setZZUnitStandard_ID(mod.getZZUnitStandard_ID());
				}

				X_ZZUnitStandard std = new X_ZZUnitStandard(Env.getCtx(), mod.getZZUnitStandard_ID(), null);

				loadSavedDatas.add(List.of(ass, std, mod));
			}

			tmLearnershipAssessments.resetMultiPo(loadSavedDatas);
		}

		setStep("assessment");
	}

	TableModel tmLearnerQCTOArtisans;

	public void initLearnerQCTOArtisans()
	{
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel col = CellModel.getColModelForLabel(
														MasterUtil.getNameOfColTranslated(	I_ZZQctoLearnership.Table_Name,
																							I_ZZQctoLearnership.COLUMNNAME_ZZLearnershipCode),
														I_ZZQctoLearnership.COLUMNNAME_ZZLearnershipCode).setTableName(I_ZZQctoLearnership.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZQctoLearnership.Table_Name,
																				I_ZZQctoLearnership.COLUMNNAME_ZZLearnershipTitle),
											I_ZZQctoLearnership.COLUMNNAME_ZZLearnershipTitle).setTableName(I_ZZQctoLearnership.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZLearnerQCTOArtisans.Table_Name,
																				I_ZZLearnerQCTOArtisans.COLUMNNAME_ZZStudentNumber),
											I_ZZLearnerQCTOArtisans.COLUMNNAME_ZZStudentNumber).setTableName(I_ZZLearnerQCTOArtisans.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZLearnerQCTOArtisans.Table_Name,
																				I_ZZLearnerQCTOArtisans.COLUMNNAME_ZZCommencementDate),
											I_ZZLearnerQCTOArtisans.COLUMNNAME_ZZStudentNumber).setTableName(I_ZZLearnerQCTOArtisans.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZLearnerQCTOArtisans.Table_Name,
																				I_ZZLearnerQCTOArtisans.COLUMNNAME_ZZCompletionDate),
											I_ZZLearnerQCTOArtisans.COLUMNNAME_ZZStudentNumber).setTableName(I_ZZLearnerQCTOArtisans.Table_Name);
		cols.add(col);

		tmLearnerQCTOArtisans = TableModel.getTableBean(TableModel.class, cols, false, I_ZZLearnerQCTOArtisans.Table_Name);
		tmLearnerQCTOArtisans.setViewModel(ViewType.VIEW_GRID);
		tmLearnerQCTOArtisans.setSclass("srd-LearnerAssessment-learnerQCTOArtisans");
	}

	public void initLearnerQCTOLearnership()
	{
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel col = CellModel.getColModelForLabel(
														MasterUtil.getNameOfColTranslated(	I_ZZQctoLearnership.Table_Name,
																							I_ZZQctoLearnership.COLUMNNAME_ZZLearnershipCode),
														I_ZZQctoLearnership.COLUMNNAME_ZZLearnershipCode).setTableName(I_ZZQctoLearnership.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZQctoLearnership.Table_Name,
																				I_ZZQctoLearnership.COLUMNNAME_ZZLearnershipTitle),
											I_ZZQctoLearnership.COLUMNNAME_ZZLearnershipTitle).setTableName(I_ZZQctoLearnership.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZLearnerQCTOLearnership.Table_Name,
																				I_ZZLearnerQCTOLearnership.COLUMNNAME_ZZStudentNumber),
											I_ZZLearnerQCTOLearnership.COLUMNNAME_ZZStudentNumber).setTableName(I_ZZLearnerQCTOLearnership.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZLearnerQCTOLearnership.Table_Name,
																				I_ZZLearnerQCTOLearnership.COLUMNNAME_ZZCommencementDate),
											I_ZZLearnerQCTOLearnership.COLUMNNAME_ZZStudentNumber).setTableName(I_ZZLearnerQCTOLearnership.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZLearnerQCTOLearnership.Table_Name,
																				I_ZZLearnerQCTOLearnership.COLUMNNAME_ZZCompletionDate),
											I_ZZLearnerQCTOLearnership.COLUMNNAME_ZZStudentNumber).setTableName(I_ZZLearnerQCTOLearnership.Table_Name);
		cols.add(col);

		tmLearnerQCTOLearnerships = TableModel.getTableBean(TableModel.class, cols, false, I_ZZLearnerQCTOLearnership.Table_Name);
		tmLearnerQCTOLearnerships.setViewModel(ViewType.VIEW_GRID);
		tmLearnerQCTOLearnerships.setSclass("srd-LearnerAssessment-learnerQCTOLearnership");
	}

	public void initLearnerQCTOSkills()
	{
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel col = CellModel.getColModelForLabel(
														MasterUtil.getNameOfColTranslated(	I_ZZQctoSkillsProgramme.Table_Name,
																							I_ZZQctoSkillsProgramme.COLUMNNAME_ZZSkillsProgrammeCode),
														I_ZZQctoSkillsProgramme.COLUMNNAME_ZZSkillsProgrammeCode).setTableName(
																																I_ZZQctoSkillsProgramme.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZQctoSkillsProgramme.Table_Name,
																				I_ZZQctoSkillsProgramme.COLUMNNAME_ZZSkillsProgrammeTitle),
											I_ZZQctoSkillsProgramme.COLUMNNAME_ZZSkillsProgrammeTitle).setTableName(I_ZZQctoSkillsProgramme.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZLearnerQCTOSkillsProgramme.Table_Name,
																				I_ZZLearnerQCTOSkillsProgramme.COLUMNNAME_ZZStudentNumber),
											I_ZZLearnerQCTOSkillsProgramme.COLUMNNAME_ZZStudentNumber).setTableName(I_ZZLearnerQCTOSkillsProgramme.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZLearnerQCTOSkillsProgramme.Table_Name,
																				I_ZZLearnerQCTOSkillsProgramme.COLUMNNAME_ZZCommencementDate),
											I_ZZLearnerQCTOSkillsProgramme.COLUMNNAME_ZZStudentNumber).setTableName(I_ZZLearnerQCTOSkillsProgramme.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZLearnerQCTOSkillsProgramme.Table_Name,
																				I_ZZLearnerQCTOSkillsProgramme.COLUMNNAME_ZZCompletionDate),
											I_ZZLearnerQCTOSkillsProgramme.COLUMNNAME_ZZStudentNumber).setTableName(I_ZZLearnerQCTOSkillsProgramme.Table_Name);
		cols.add(col);

		tmLearnerQCTOSkills = TableModel.getTableBean(TableModel.class, cols, false, I_ZZLearnerQCTOSkillsProgramme.Table_Name);
		tmLearnerQCTOSkills.setViewModel(ViewType.VIEW_GRID);
		tmLearnerQCTOSkills.setSclass("srd-LearnerAssessment-learnerQCTOSkill");

	}

	public TableModel getTmLearnerCompletions()
	{
		return tmLearnerCompletions;
	}

	TableModel	tmLearnerCompletions;

	ColumnModel	downloadActionCol;

	public void initLearnerCompletions()
	{
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel col = CellModel.getColModelForLabel(MasterUtil.getNameOfColTranslated(	I_ZZCompletedAssessments_v.Table_Name,
																							I_ZZCompletedAssessments_v.COLUMNNAME_ZZFirstName),
														I_ZZCompletedAssessments_v.COLUMNNAME_ZZFirstName).setTableName(I_ZZCompletedAssessments_v.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(MasterUtil.getNameOfColTranslated(	I_ZZCompletedAssessments_v.Table_Name,
																				I_ZZCompletedAssessments_v.COLUMNNAME_Surname),
											I_ZZCompletedAssessments_v.COLUMNNAME_Surname).setTableName(I_ZZCompletedAssessments_v.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(MasterUtil.getNameOfColTranslated(	I_ZZCompletedAssessments_v.Table_Name,
																				I_ZZCompletedAssessments_v.COLUMNNAME_ZZ_ID_Passport_No),
											I_ZZCompletedAssessments_v.COLUMNNAME_ZZ_ID_Passport_No).setTableName(I_ZZCompletedAssessments_v.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(MasterUtil.getNameOfColTranslated(	I_ZZCompletedAssessments_v.Table_Name,
																				I_ZZCompletedAssessments_v.COLUMNNAME_total_achieved_credits),
											I_ZZCompletedAssessments_v.COLUMNNAME_total_achieved_credits).setTableName(I_ZZCompletedAssessments_v.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(MasterUtil.getNameOfColTranslated(	I_ZZCompletedAssessments_v.Table_Name,
																				I_ZZCompletedAssessments_v.COLUMNNAME_minimum_required_credits),
											I_ZZCompletedAssessments_v.COLUMNNAME_minimum_required_credits).setTableName(I_ZZCompletedAssessments_v.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(MasterUtil.getNameOfColTranslated(	I_ZZCompletedAssessments_v.Table_Name,
																				I_ZZCompletedAssessments_v.COLUMNNAME_Assessment_Date),
											I_ZZCompletedAssessments_v.COLUMNNAME_Assessment_Date).setTableName(I_ZZCompletedAssessments_v.Table_Name);
		cols.add(col);

		ValueAdaptColumnModel statusCol = ValueAdaptCellModel.getValueAdaptColumnModel(	MasterUtil.getNameOfColTranslated(	I_ZZCompletedAssessments_v.Table_Name,
																															I_ZZCompletedAssessments_v.COLUMNNAME_ZZ_DocStatus),
																						I_ZZCompletedAssessments_v.COLUMNNAME_ZZ_DocStatus,
																						CellModel.LABEL_CELL);
		statusCol.setTableName(I_ZZCompletedAssessments_v.Table_Name);
		statusCol.setValueAdaptHandle(value -> {
			if (value == null)
				return "";
			for (ValueNamePair vp : MasterUtil.getZZDocStatus())
			{
				if (vp.getValue().equals(value.toString()))
					return vp.getName();
			}
			return value.toString();
		});
		cols.add(statusCol);

		downloadActionCol = CellModel.getColModelForGenericCell("Download Certificate", null, CellModel.BUTTON_CELL);
		downloadActionCol.setEventHandle((event, cellModel) -> downloadCertificate());
		cols.add(downloadActionCol);

		tmLearnerCompletions = TableModel.getTableBean(TableModel.class, cols, false, I_ZZCompletedAssessments_v.Table_Name);
		tmLearnerCompletions.setViewModel(ViewType.VIEW_GRID);
		tmLearnerCompletions.setSclass("srd-LearnerAssessment-completions");
	}

	CheckboxColumnModel qctoLearnershipAssessmentsSelectedCol;

	public void initQctoLearnershipAssessments()
	{
		List<ColumnModel> cols = new ArrayList<>();

		qctoLearnershipAssessmentsSelectedCol = CheckboxCellModel.getCheckboxColModel(
																						"", null);

		ColumnModel col = CellModel.getColModelForLabel(
														MasterUtil.getNameOfColTranslated(I_ZZQctoModule.Table_Name, I_ZZQctoModule.COLUMNNAME_ZZModuleCode),
														I_ZZQctoModule.COLUMNNAME_ZZModuleCode).setTableName(I_ZZQctoModule.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(I_ZZQctoModule.Table_Name, I_ZZQctoModule.COLUMNNAME_ZZModuleTitle),
											I_ZZQctoModule.COLUMNNAME_ZZModuleTitle).setTableName(I_ZZQctoModule.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(I_ZZQctoModule.Table_Name, I_ZZQctoModule.COLUMNNAME_ZZCredits),
											I_ZZQctoModule.COLUMNNAME_ZZCredits).setTableName(I_ZZQctoModule.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(I_ZZQctoModule.Table_Name, I_ZZQctoModule.COLUMNNAME_ZZModuleType),
											I_ZZQctoModule.COLUMNNAME_ZZModuleType).setTableName(I_ZZQctoModule.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZLearnerQctoLearnershipAssessments.Table_Name,
																				I_ZZLearnerQctoLearnershipAssessments.COLUMNNAME_ZZRPL),
											I_ZZLearnerQctoLearnershipAssessments.COLUMNNAME_ZZRPL).setTableName(
																													I_ZZLearnerQctoLearnershipAssessments.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZLearnerQctoLearnershipAssessments.Table_Name,
																				I_ZZLearnerQctoLearnershipAssessments.COLUMNNAME_ZZPreviouslyAchieved),
											I_ZZLearnerQctoLearnershipAssessments.COLUMNNAME_ZZPreviouslyAchieved).setTableName(
																																I_ZZLearnerQctoLearnershipAssessments.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZLearnerQctoLearnershipAssessments.Table_Name,
																				I_ZZLearnerQctoLearnershipAssessments.COLUMNNAME_ZZAssessorPerson_ID),
											I_ZZLearnerQctoLearnershipAssessments.COLUMNNAME_ZZAssessorPerson_ID).setTableName(
																																I_ZZLearnerQctoLearnershipAssessments.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZLearnerQctoLearnershipAssessments.Table_Name,
																				I_ZZLearnerQctoLearnershipAssessments.COLUMNNAME_ZZAssessmentDate),
											I_ZZLearnerQctoLearnershipAssessments.COLUMNNAME_ZZAssessmentDate).setTableName(
																															I_ZZLearnerQctoLearnershipAssessments.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZLearnerQctoLearnershipAssessments.Table_Name,
																				I_ZZLearnerQctoLearnershipAssessments.COLUMNNAME_ZZModerator_ID),
											I_ZZLearnerQctoLearnershipAssessments.COLUMNNAME_ZZModerator_ID).setTableName(
																															I_ZZLearnerQctoLearnershipAssessments.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZLearnerQctoLearnershipAssessments.Table_Name,
																				I_ZZLearnerQctoLearnershipAssessments.COLUMNNAME_ZZModerationDate),
											I_ZZLearnerQctoLearnershipAssessments.COLUMNNAME_ZZModerationDate).setTableName(
																															I_ZZLearnerQctoLearnershipAssessments.Table_Name);
		cols.add(col);

		tmQctoLearnershipAssessments = TableModel.getTableBean(TableModel.class, cols, false, I_ZZLearnerQctoLearnershipAssessments.Table_Name);
		tmQctoLearnershipAssessments.setViewModel(ViewType.VIEW_GRID);
		tmQctoLearnershipAssessments.setSclass("srd-LearnerAssessment-certificateAssessments");

		// read-only in certificate view

	}

	private TableModel			tmQctoLearnershipAssessments;

	TableModel					tmLearnerQCTOLearnerships;
	TableModel					tmQctoArtisansAssessments;
	TableModel					tmQCTOSkillsAssessments;
	TableModel					tmLearnerQCTOSkills;
	TableModel					tmLearnerLearnerships;
	X_ZZLearnerSkillsProgramme	learnerSkillsProgramme;
	X_ZZSkillsProgramme			skillsProgramme;
	TableModel					tmLearnerSkillsProgrammes;
	TableModel					tmSkillsProgrammeAssessments;
	CheckboxColumnModel			skillsProgrammeAssessmentsSelectedCol;
	TableModel					tmLearnershipAssessments;

	public void initQctoSkillsAssessments()
	{
		List<ColumnModel> cols = new ArrayList<>();

		qctoLearnershipAssessmentsSelectedCol = CheckboxCellModel.getCheckboxColModel("", null);
		// cols.add(qctoLearnershipAssessmentsSelectedCol);

		ColumnModel col = CellModel.getColModelForLabel(
														MasterUtil.getNameOfColTranslated(I_ZZQctoModule.Table_Name, I_ZZQctoModule.COLUMNNAME_ZZModuleCode),
														I_ZZQctoModule.COLUMNNAME_ZZModuleCode).setTableName(I_ZZQctoModule.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(I_ZZQctoModule.Table_Name, I_ZZQctoModule.COLUMNNAME_ZZModuleTitle),
											I_ZZQctoModule.COLUMNNAME_ZZModuleTitle).setTableName(I_ZZQctoModule.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(I_ZZQctoModule.Table_Name, I_ZZQctoModule.COLUMNNAME_ZZCredits),
											I_ZZQctoModule.COLUMNNAME_ZZCredits).setTableName(I_ZZQctoModule.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(I_ZZQctoModule.Table_Name, I_ZZQctoModule.COLUMNNAME_ZZModuleType),
											I_ZZQctoModule.COLUMNNAME_ZZModuleType).setTableName(I_ZZQctoModule.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZLearnerQCTOSkillsProgrammeAssessments.Table_Name,
																				I_ZZLearnerQCTOSkillsProgrammeAssessments.COLUMNNAME_ZZRPL),
											I_ZZLearnerQCTOSkillsProgrammeAssessments.COLUMNNAME_ZZRPL).setTableName(
																														I_ZZLearnerQCTOSkillsProgrammeAssessments.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZLearnerQCTOSkillsProgrammeAssessments.Table_Name,
																				I_ZZLearnerQCTOSkillsProgrammeAssessments.COLUMNNAME_Is_Previously_Achieved),
											I_ZZLearnerQCTOSkillsProgrammeAssessments.COLUMNNAME_Is_Previously_Achieved).setTableName(
																																		I_ZZLearnerQCTOSkillsProgrammeAssessments.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZLearnerQCTOSkillsProgrammeAssessments.Table_Name,
																				I_ZZLearnerQCTOSkillsProgrammeAssessments.COLUMNNAME_Assessor_ID),
											I_ZZLearnerQCTOSkillsProgrammeAssessments.COLUMNNAME_Assessor_ID).setTableName(
																															I_ZZLearnerQCTOSkillsProgrammeAssessments.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZLearnerQCTOSkillsProgrammeAssessments.Table_Name,
																				I_ZZLearnerQCTOSkillsProgrammeAssessments.COLUMNNAME_Assessment_Date),
											I_ZZLearnerQCTOSkillsProgrammeAssessments.COLUMNNAME_Assessment_Date).setTableName(
																																I_ZZLearnerQCTOSkillsProgrammeAssessments.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZLearnerQCTOSkillsProgrammeAssessments.Table_Name,
																				I_ZZLearnerQCTOSkillsProgrammeAssessments.COLUMNNAME_Moderator_ID),
											I_ZZLearnerQCTOSkillsProgrammeAssessments.COLUMNNAME_Moderator_ID).setTableName(
																															I_ZZLearnerQCTOSkillsProgrammeAssessments.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZLearnerQCTOSkillsProgrammeAssessments.Table_Name,
																				I_ZZLearnerQCTOSkillsProgrammeAssessments.COLUMNNAME_Moderation_Date),
											I_ZZLearnerQCTOSkillsProgrammeAssessments.COLUMNNAME_Moderation_Date).setTableName(
																																I_ZZLearnerQCTOSkillsProgrammeAssessments.Table_Name);
		cols.add(col);

		tmQCTOSkillsAssessments = TableModel.getTableBean(TableModel.class, cols, false, I_ZZLearnerQCTOSkillsProgrammeAssessments.Table_Name);
		tmQCTOSkillsAssessments.setViewModel(ViewType.VIEW_GRID);
		tmQCTOSkillsAssessments.setSclass("srd-LearnerAssessment-certificateAssessments");

		// read-only in certificate view
	}

	public TableModel getTmLearnerAssessments()
	{
		if (isInterventionQCTOArtisans())
			return tmQctoArtisansAssessments;
		else if (isInterventionQCTOLearnerships())
			return tmQctoLearnershipAssessments;
		else if (isInterventionQCTOSkills())
			return tmQCTOSkillsAssessments;
		else if (isInterventionSkillsProgrammes())
			return tmSkillsProgrammeAssessments;
		else if (isInterventionLearnerships())
			return tmLearnershipAssessments;
		else
			throw new AdempiereException("Not Yet suport program");
	}

	public TableModel getTmLearnerProgram()
	{
		if (isInterventionQCTOArtisans())
			return tmLearnerQCTOArtisans;
		else if (isInterventionQCTOLearnerships())
			return tmLearnerQCTOLearnerships;
		else if (isInterventionQCTOSkills())
			return tmLearnerQCTOSkills;
		else if (isInterventionSkillsProgrammes())
			return tmLearnerSkillsProgrammes;
		else if (isInterventionLearnerships())
			return tmLearnerLearnerships;
		else
			throw new AdempiereException("Not Yet suport program");
	}

	public void initLearnerSkillsProgrammes()
	{
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel col = CellModel.getColModelForLabel(
														MasterUtil.getNameOfColTranslated(	I_ZZSkillsProgramme.Table_Name,
																							I_ZZSkillsProgramme.COLUMNNAME_ZZSkillsProgrammeCode),
														I_ZZSkillsProgramme.COLUMNNAME_ZZSkillsProgrammeCode).setTableName(I_ZZSkillsProgramme.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZSkillsProgramme.Table_Name,
																				I_ZZSkillsProgramme.COLUMNNAME_ZZSkillsProgrammeTitle),
											I_ZZSkillsProgramme.COLUMNNAME_ZZSkillsProgrammeTitle).setTableName(I_ZZSkillsProgramme.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZLearnerSkillsProgramme.Table_Name,
																				I_ZZLearnerSkillsProgramme.COLUMNNAME_ZZStudentNumber),
											I_ZZLearnerSkillsProgramme.COLUMNNAME_ZZStudentNumber).setTableName(I_ZZLearnerSkillsProgramme.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZLearnerSkillsProgramme.Table_Name,
																				I_ZZLearnerSkillsProgramme.COLUMNNAME_ZZCommencementDate),
											I_ZZLearnerSkillsProgramme.COLUMNNAME_ZZCommencementDate).setTableName(I_ZZLearnerSkillsProgramme.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZLearnerSkillsProgramme.Table_Name,
																				I_ZZLearnerSkillsProgramme.COLUMNNAME_ZZCompletionDate),
											I_ZZLearnerSkillsProgramme.COLUMNNAME_ZZCompletionDate).setTableName(I_ZZLearnerSkillsProgramme.Table_Name);
		cols.add(col);

		tmLearnerSkillsProgrammes = TableModel.getTableBean(TableModel.class, cols, false, I_ZZLearnerSkillsProgramme.Table_Name);
		tmLearnerSkillsProgrammes.setViewModel(ViewType.VIEW_GRID);
		tmLearnerSkillsProgrammes.setSclass("srd-LearnerAssessment-learnerLearnership");
	}

	public void initSkillsProgrammeAssessments()
	{
		List<ColumnModel> cols = new ArrayList<>();

		skillsProgrammeAssessmentsSelectedCol = CheckboxCellModel.getCheckboxColModel("", null);
		// cols.add(skillsProgrammeAssessmentsSelectedCol);

		ColumnModel col = CellModel.getColModelForLabel(
														MasterUtil.getNameOfColTranslated(	I_ZZUnitStandard.Table_Name,
																							I_ZZUnitStandard.COLUMNNAME_ZZSaqaUnitStandardCode),
														I_ZZUnitStandard.COLUMNNAME_ZZSaqaUnitStandardCode).setTableName(I_ZZUnitStandard.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(I_ZZUnitStandard.Table_Name, I_ZZUnitStandard.COLUMNNAME_ZZSaqaUnitStandardTitle),
											I_ZZUnitStandard.COLUMNNAME_ZZSaqaUnitStandardTitle).setTableName(I_ZZUnitStandard.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(I_ZZUnitStandard.Table_Name, I_ZZUnitStandard.COLUMNNAME_ZZCredits),
											I_ZZUnitStandard.COLUMNNAME_ZZCredits).setTableName(I_ZZUnitStandard.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZSkillsProgrammeUnitStandard.Table_Name,
																				I_ZZSkillsProgrammeUnitStandard.COLUMNNAME_ZZUnitStandardType),
											I_ZZSkillsProgrammeUnitStandard.COLUMNNAME_ZZUnitStandardType).setTableName(
																														I_ZZSkillsProgrammeUnitStandard.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZLearnerSkillsProgrammeAssessments.Table_Name,
																				I_ZZLearnerSkillsProgrammeAssessments.COLUMNNAME_ZZRPL),
											I_ZZLearnerSkillsProgrammeAssessments.COLUMNNAME_ZZRPL).setTableName(
																													I_ZZLearnerSkillsProgrammeAssessments.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZLearnerSkillsProgrammeAssessments.Table_Name,
																				I_ZZLearnerSkillsProgrammeAssessments.COLUMNNAME_ZZAssessmentStatus),
											I_ZZLearnerSkillsProgrammeAssessments.COLUMNNAME_ZZAssessmentStatus).setTableName(
																																I_ZZLearnerSkillsProgrammeAssessments.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZLearnerSkillsProgrammeAssessments.Table_Name,
																				I_ZZLearnerSkillsProgrammeAssessments.COLUMNNAME_Assessor_ID),
											I_ZZLearnerSkillsProgrammeAssessments.COLUMNNAME_Assessor_ID).setTableName(
																														I_ZZLearnerSkillsProgrammeAssessments.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZLearnerSkillsProgrammeAssessments.Table_Name,
																				I_ZZLearnerSkillsProgrammeAssessments.COLUMNNAME_Assessment_Date),
											I_ZZLearnerSkillsProgrammeAssessments.COLUMNNAME_Assessment_Date).setTableName(
																															I_ZZLearnerSkillsProgrammeAssessments.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZLearnerSkillsProgrammeAssessments.Table_Name,
																				I_ZZLearnerSkillsProgrammeAssessments.COLUMNNAME_Moderator_ID),
											I_ZZLearnerSkillsProgrammeAssessments.COLUMNNAME_Moderator_ID).setTableName(
																														I_ZZLearnerSkillsProgrammeAssessments.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZLearnerSkillsProgrammeAssessments.Table_Name,
																				I_ZZLearnerSkillsProgrammeAssessments.COLUMNNAME_Moderation_Date),
											I_ZZLearnerSkillsProgrammeAssessments.COLUMNNAME_Moderation_Date).setTableName(
																															I_ZZLearnerSkillsProgrammeAssessments.Table_Name);
		cols.add(col);

		tmSkillsProgrammeAssessments = TableModel.getTableBean(TableModel.class, cols, false, I_ZZLearnerSkillsProgrammeAssessments.Table_Name);
		tmSkillsProgrammeAssessments.setViewModel(ViewType.VIEW_GRID);
		tmSkillsProgrammeAssessments.setSclass("srd-LearnerAssessment-certificateAssessments");

		// read-only in certificate view
	}

	public void initLearnerLearnership()
	{
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel col = CellModel.getColModelForLabel(
														MasterUtil.getNameOfColTranslated(	I_ZZLearnership.Table_Name,
																							I_ZZLearnership.COLUMNNAME_ZZLearnershipCode),
														I_ZZLearnership.COLUMNNAME_ZZLearnershipCode).setTableName(I_ZZLearnership.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(I_ZZLearnership.Table_Name, I_ZZLearnership.COLUMNNAME_ZZLearnershipTitle),
											I_ZZLearnership.COLUMNNAME_ZZLearnershipTitle).setTableName(I_ZZLearnership.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZLearnerLearnership.Table_Name,
																				I_ZZLearnerLearnership.COLUMNNAME_ZZStudentNumber),
											I_ZZLearnerLearnership.COLUMNNAME_ZZStudentNumber).setTableName(I_ZZLearnerLearnership.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZLearnerLearnership.Table_Name,
																				I_ZZLearnerLearnership.COLUMNNAME_ZZCommencementDate),
											I_ZZLearnerLearnership.COLUMNNAME_ZZStudentNumber).setTableName(I_ZZLearnerLearnership.Table_Name).setReadonly(
																																							true);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZLearnerLearnership.Table_Name,
																				I_ZZLearnerLearnership.COLUMNNAME_ZZEstimateCompletionDate),
											I_ZZLearnerLearnership.COLUMNNAME_ZZStudentNumber).setTableName(I_ZZLearnerLearnership.Table_Name).setReadonly(
																																							true);
		cols.add(col);

		tmLearnerLearnerships = TableModel.getTableBean(TableModel.class, cols, false, I_ZZLearnerLearnership.Table_Name);
		tmLearnerLearnerships.setViewModel(ViewType.VIEW_GRID);
		tmLearnerLearnerships.setSclass("srd-LearnerAssessment-learnerLearnership");
	}

	CheckboxColumnModel learnershipAssessmentsSelectedCol;

	public void initLearnershipAssessments()
	{
		List<ColumnModel> cols = new ArrayList<>();

		learnershipAssessmentsSelectedCol = CheckboxCellModel.getCheckboxColModel(
																					"", null);
		// cols.add(learnershipAssessmentsSelectedCol);

		ColumnModel col = CellModel.getColModelForLabel(
														MasterUtil.getNameOfColTranslated(	I_ZZUnitStandard.Table_Name,
																							I_ZZUnitStandard.COLUMNNAME_ZZSaqaUnitStandardCode),
														I_ZZUnitStandard.COLUMNNAME_ZZSaqaUnitStandardCode).setTableName(I_ZZUnitStandard.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(I_ZZUnitStandard.Table_Name, I_ZZUnitStandard.COLUMNNAME_ZZSaqaUnitStandardTitle),
											I_ZZUnitStandard.COLUMNNAME_ZZSaqaUnitStandardTitle).setTableName(I_ZZUnitStandard.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(I_ZZUnitStandard.Table_Name, I_ZZUnitStandard.COLUMNNAME_ZZCredits),
											I_ZZUnitStandard.COLUMNNAME_ZZCredits).setTableName(I_ZZUnitStandard.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZLearnershipUnitStandard.Table_Name,
																				I_ZZLearnershipUnitStandard.COLUMNNAME_ZZUnitStandardType),
											I_ZZLearnershipUnitStandard.COLUMNNAME_ZZUnitStandardType).setTableName(I_ZZLearnershipUnitStandard.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZLearnerLearnershipAssessments.Table_Name,
																				I_ZZLearnerLearnershipAssessments.COLUMNNAME_ZZRPL),
											I_ZZLearnerLearnershipAssessments.COLUMNNAME_ZZRPL).setTableName(I_ZZLearnerLearnershipAssessments.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZLearnerLearnershipAssessments.Table_Name,
																				I_ZZLearnerLearnershipAssessments.COLUMNNAME_ZZIsPreviouslyAchieved),
											I_ZZLearnerLearnershipAssessments.COLUMNNAME_ZZIsPreviouslyAchieved).setTableName(
																																I_ZZLearnerLearnershipAssessments.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZLearnerLearnershipAssessments.Table_Name,
																				I_ZZLearnerLearnershipAssessments.COLUMNNAME_ZZAssessorPerson_ID),
											I_ZZLearnerLearnershipAssessments.COLUMNNAME_ZZAssessorPerson_ID).setTableName(
																															I_ZZLearnerLearnershipAssessments.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZLearnerLearnershipAssessments.Table_Name,
																				I_ZZLearnerLearnershipAssessments.COLUMNNAME_ZZAssessmentDate),
											I_ZZLearnerLearnershipAssessments.COLUMNNAME_ZZAssessmentDate).setTableName(
																														I_ZZLearnerLearnershipAssessments.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZLearnerLearnershipAssessments.Table_Name,
																				I_ZZLearnerLearnershipAssessments.COLUMNNAME_ZZModerator_ID),
											I_ZZLearnerLearnershipAssessments.COLUMNNAME_ZZModerator_ID).setTableName(
																														I_ZZLearnerLearnershipAssessments.Table_Name);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(	I_ZZLearnerLearnershipAssessments.Table_Name,
																				I_ZZLearnerLearnershipAssessments.COLUMNNAME_ZZModerationDate),
											I_ZZLearnerLearnershipAssessments.COLUMNNAME_ZZModerationDate).setTableName(
																														I_ZZLearnerLearnershipAssessments.Table_Name);
		cols.add(col);

		tmLearnershipAssessments = TableModel.getTableBean(TableModel.class, cols, false, I_ZZLearnerLearnershipAssessments.Table_Name);
		tmLearnershipAssessments.setViewModel(ViewType.VIEW_GRID);
		tmLearnershipAssessments.setSclass("srd-LearnerAssessment-certificateAssessments");

		// read-only in certificate view
	}

}
