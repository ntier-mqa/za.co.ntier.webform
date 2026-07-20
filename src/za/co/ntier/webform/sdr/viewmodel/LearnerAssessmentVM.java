package za.co.ntier.webform.sdr.viewmodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.ValueNamePair;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.Listitem;

import za.co.ntier.api.model.I_AD_User;
import za.co.ntier.api.model.I_ZZAssessorPerson;
import za.co.ntier.api.model.I_ZZAssessorPerson_v;
import za.co.ntier.api.model.I_ZZLearner;
import za.co.ntier.api.model.I_ZZLearnerQCTOArtisans;
import za.co.ntier.api.model.I_ZZLearnerQCTOLearnership;
import za.co.ntier.api.model.I_ZZLearnerQCTOSkillsProgramme;
import za.co.ntier.api.model.I_ZZLearnerQctoLearnershipAssessments;
import za.co.ntier.api.model.I_ZZLearner_v;
import za.co.ntier.api.model.I_ZZLinkAssessorQualification;
import za.co.ntier.api.model.I_ZZQctoLearnership;
import za.co.ntier.api.model.I_ZZQctoLearnershipModule;
import za.co.ntier.api.model.I_ZZQctoModule;
import za.co.ntier.api.model.I_ZZQctoSkillsProgramme;
import za.co.ntier.api.model.I_ZZQualification;
import za.co.ntier.api.model.I_ZZSkillsProgramme;
import za.co.ntier.api.model.X_ZZAssessorPerson_v;
import za.co.ntier.api.model.X_ZZLearner;
import za.co.ntier.api.model.X_ZZLearnerQCTOArtisans;
import za.co.ntier.api.model.X_ZZLearnerQCTOLearnership;
import za.co.ntier.api.model.X_ZZLearnerQCTOSkillsProgramme;
import za.co.ntier.api.model.X_ZZLearnerQctoLearnershipAssessments;
import za.co.ntier.api.model.X_ZZLearner_v;
import za.co.ntier.api.model.X_ZZLkpSchoolEmis;
import za.co.ntier.api.model.X_ZZQctoLearnership;
import za.co.ntier.api.model.X_ZZQctoLearnershipModule;
import za.co.ntier.api.model.X_ZZQctoModule;
import za.co.ntier.api.model.X_ZZQctoSkillsProgramme;
import za.co.ntier.api.model.X_ZZQualification;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.ISaveForm;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.RowModel.RowData;
import za.co.ntier.webform.sdr.component.bean.TableModel.CommandSetting;
import za.co.ntier.webform.sdr.component.bean.TableModel.ViewType;
import za.co.ntier.webform.sdr.component.bean.cell.CheckboxCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.DateCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.ListCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.ValueAdaptCellModel;
import za.co.ntier.webform.sdr.component.bean.column.CheckboxColumnModel;
import za.co.ntier.webform.sdr.component.bean.column.ListColumnModel;
import za.co.ntier.webform.sdr.component.bean.column.ValueAdaptColumnModel;
import za.co.ntier.webform.sdr.viewmodel.BaseAppVM.InfoPanelPara;

public class LearnerAssessmentVM extends StepAppVM{
	private TableModel tmLearnerSelection;
	private TableModel tmLearnerSelectionInfo;
	ValueNamePair selectedIntervention;
	
	public TableModel getTmLearnerSelectionInfo() {
		return tmLearnerSelectionInfo;
	}

	public void setTmLearnerSelectionInfo(TableModel tmLearnerSelectionInfo) {
		this.tmLearnerSelectionInfo = tmLearnerSelectionInfo;
	}

	public TableModel getTmLearnerSelection() {
		return tmLearnerSelection;
	}

	public void setTmLearnerSelection(TableModel tmLearnerSelection) {
		this.tmLearnerSelection = tmLearnerSelection;
	}

	@Init(superclass = true)
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo){
		initStep("selectAssessment");
		initLearnerSelection();
		initLearnerSelectionInfo();
	}
	X_ZZLearner_v learnerSelected;
	
	boolean isInterventionQCTOArtisans() {
		return "QCTO Artisans".equals(selectedIntervention.getValue());
	}
	
	boolean isInterventionQCTOLearnerships() {
		return "QCTO Learnerships".equals(selectedIntervention.getValue());
	}
	
	boolean isInterventionQCTOSkills() {
		return "QCTO Skills Programmes".equals(selectedIntervention.getValue());
	}
	
	X_ZZLearnerQCTOArtisans learnerQCTOArtisans;
	X_ZZQctoLearnership qctoArtisans;
	
	X_ZZLearnerQCTOLearnership learnerQCTOLearnership;
	X_ZZQctoLearnership qctoLearnership;
	
	X_ZZLearnerQCTOSkillsProgramme learnerQCTOSkills;
	X_ZZQctoSkillsProgramme qctoSkills;
	
	public void initLearnerSelection(){
		List<ColumnModel> cols = new ArrayList<>();
		
		ValueAdaptColumnModel chooseLearnerCol = ValueAdaptCellModel.getValueAdaptColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZLearner.Table_Name, I_ZZLearner.COLUMNNAME_ZZLearner_ID), 
				null, 
				CellModel.SEARCH_CELL);
		
		chooseLearnerCol.setShowTitle(false);
		cols.add(chooseLearnerCol);
		
		chooseLearnerCol.setEventHandle((event, cellModel) -> {
			showInfoPanel(
			InfoPanelPara.getInstance(I_ZZLearner_v.Table_Name
					, I_ZZLearner_v.COLUMNNAME_ZZLearner_ID)
			, obj -> {
				Object [] objs = (Object [])obj;
				int learnerIdSelected = (int)objs[0];
				learnerSelected = new X_ZZLearner_v(Env.getCtx(), learnerIdSelected, null);
				
				tmLearnerSelectionInfo.reset(learnerSelected);
			});
		});
		
		
		ListColumnModel<ValueNamePair> interventionListCol = ListCellModel.getListColumnModel(
				Msg.getElement(Env.getCtx(), "ZZInterventionList")
				, null
				, MasterUtil.getLkpInterventionList()
				, title -> {return title.getName();}
				, title -> {return title.getValue();}
			);
		interventionListCol.setzClass(ValueNamePair.class).required();
		cols.add(interventionListCol);
		
		ColumnModel actionCol = CellModel.getColModelForGenericCell("", null, CellModel.BUTTON_CELL);
		actionCol.setShowTitle(false);
		cols.add(actionCol);
		
		interventionListCol.setEventHandle((event, cellModel) -> {
			@SuppressWarnings("unchecked")
			SelectEvent<Listitem, ValueNamePair> selectEvent = (SelectEvent<Listitem, ValueNamePair>)event;
			selectedIntervention = selectEvent.getSelectedObjects().iterator().next();
			
			CellModel actionCell = cellModel.getRowModel().get(actionCol);
			actionCol.setTitle(selectedIntervention.getName());
			actionCell.setVisible(true);
			
		});
		
		actionCol.setEventHandle((event, cellMoqctoArtisansdel) -> {
			if (isInterventionQCTOArtisans()) {
				showInfoPanelForIntervention(I_ZZLearnerQCTOArtisans.Table_Name, I_ZZLearnerQCTOArtisans.COLUMNNAME_ZZLearnerQCTOArtisans_ID);
				
			}else if (isInterventionQCTOLearnerships()) {
				showInfoPanelForIntervention(I_ZZLearnerQCTOLearnership.Table_Name, I_ZZLearnerQCTOLearnership.COLUMNNAME_ZZLearnerQCTOLearnership_ID);
				
			}else if (isInterventionQCTOSkills()) {
				showInfoPanelForIntervention(I_ZZLearnerQCTOSkillsProgramme.Table_Name, I_ZZLearnerQCTOSkillsProgramme.COLUMNNAME_ZZLearnerQCTOSkillsProgramme_ID);
				
			}
		});
		
		tmLearnerSelection = TableModel.getTableBean(TableModel.class, cols, false, I_ZZLearner_v.Table_Name);
		tmLearnerSelection.setViewModel(ViewType.VIEW_FORM);
		tmLearnerSelection.setSclass("srd-LearnerAssessment-selectLearner");
		tmLearnerSelection.init();
		
		tmLearnerSelection.getRow().get(actionCol).setVisible(false);
	}
	
	
	void initLearnerSelectionInfo() {
		List<ColumnModel> cols = new ArrayList<>();
		
		ColumnModel col = CellModel.getColModelForLabel(
				MasterUtil.getNameOfColTranslated(I_ZZLearner_v.Table_Name, I_ZZLearner_v.COLUMNNAME_ZZFirstName)
				, I_ZZLearner_v.COLUMNNAME_ZZFirstName
				);
		cols.add(col);
		
		col = CellModel.getColModelForLabel(
				MasterUtil.getNameOfColTranslated(I_ZZLearner_v.Table_Name, I_ZZLearner_v.COLUMNNAME_Surname)
				, I_ZZLearner_v.COLUMNNAME_Surname
				);
		cols.add(col);
		
		col = CellModel.getColModelForLabel(
				MasterUtil.getNameOfColTranslated(I_ZZLearner_v.Table_Name, I_ZZLearner_v.COLUMNNAME_ZZ_ID_Passport_No)
				, I_ZZLearner_v.COLUMNNAME_ZZ_ID_Passport_No
				);
		cols.add(col);
		
		tmLearnerSelectionInfo = TableModel.getTableBean(TableModel.class, cols, false, I_ZZLearner_v.Table_Name);
		tmLearnerSelectionInfo.setViewModel(ViewType.VIEW_GRID);
		tmLearnerSelectionInfo.setSclass("srd-LearnerAssessment-selectLearnerInfo");
		tmLearnerSelectionInfo.init();
	}
	void showInfoPanelForIntervention(String tableName, String columnId) {
		String whereClause = I_ZZLearnerQCTOArtisans.COLUMNNAME_ZZLearner_ID + " = " + learnerSelected.getZZLearner_ID();
		showInfoPanel(
				InfoPanelPara.getInstance(tableName, columnId).setWhereClause(whereClause)
				, obj -> {
					Object [] objs = (Object [])obj;
					showAssessmentSteo((int)objs[0]);
					
				});
	}
	
	void showAssessmentSteo(int id) {
		if (isInterventionQCTOArtisans()) {
			learnerQCTOArtisans = new X_ZZLearnerQCTOArtisans(Env.getCtx(), id, null);
			qctoArtisans = new X_ZZQctoLearnership(Env.getCtx(), learnerQCTOArtisans.getZZQctoLearnership_ID(), null);
			if (tmLearnerQCTOArtisans == null)
				initLearnerQCTOArtisans();
			
			tmLearnerQCTOArtisans.resetMultiPo(List.of(List.of(learnerQCTOArtisans, qctoArtisans)));
		}else if (isInterventionQCTOLearnerships()) {
			learnerQCTOLearnership = new X_ZZLearnerQCTOLearnership(Env.getCtx(), id, null);
			qctoLearnership = new X_ZZQctoLearnership(Env.getCtx(), learnerQCTOLearnership.getZZQctoLearnership_ID(), null);
			if (tmLearnerQCTOLearnerships == null)
				initLearnerQCTOLearnership();
			
			tmLearnerQCTOLearnerships.resetMultiPo(List.of(List.of(learnerQCTOLearnership, qctoLearnership)));
		}else if (isInterventionQCTOSkills()) {
			learnerQCTOSkills = new X_ZZLearnerQCTOSkillsProgramme(Env.getCtx(), id, null);
			qctoSkills = new X_ZZQctoSkillsProgramme(Env.getCtx(), learnerQCTOSkills.getZZQctoSkillsProgramme_ID(), null);
			
			if (tmLearnerQCTOSkills == null)
				initLearnerQCTOSkills();
			
			tmLearnerQCTOSkills.resetMultiPo(List.of(List.of(learnerQCTOSkills, qctoSkills)));
			
		}
		
		initAssessmentParam();
		
		if (isInterventionQCTOLearnerships()) {
			if (tmQctoLearnershipAssessments == null) {
				initQctoLearnershipAssessments();
			}
			
			int [] moduleIds = DB.getIDsEx(null
					, String.format("SELECT %s FROM %s WHERE %s = ?"
						, X_ZZQctoModule.COLUMNNAME_ZZQctoModule_ID
						, X_ZZQctoLearnershipModule.Table_Name
						, X_ZZQctoLearnershipModule.COLUMNNAME_ZZQctoLearnership_ID
						)
					,learnerQCTOLearnership.getZZQctoLearnership_ID());
			
			List<Object> ids = Arrays.stream(moduleIds).boxed().collect(Collectors.toList());
			String placeholders = MasterUtil.createPlaceHoldForInClause(ids);
			
			Query moduleAssessmentsQuery = MTable.get(Env.getCtx(), X_ZZQctoModule.Table_Name).createQuery(
					String.format("%s IN (%s)"
							, X_ZZQctoModule.COLUMNNAME_ZZQctoModule_ID
							, placeholders)
					, null);
			moduleAssessmentsQuery.setParameters(ids);
			List<PO> modules = moduleAssessmentsQuery.list();
			
			Query learnerAssessmentsQuery = MTable.get(Env.getCtx(), I_ZZLearnerQctoLearnershipAssessments.Table_Name).createQuery(
					String.format("%s IN (%s) AND %s = ?"
							, X_ZZLearnerQctoLearnershipAssessments.COLUMNNAME_ZZQctoModule_ID
							, placeholders
							, X_ZZLearnerQctoLearnershipAssessments.COLUMNNAME_ZZLearnerQCTOLearnership_ID
							), null);
			
			List<Object> learnerAssessmentsParas = new ArrayList<Object>(ids);
			learnerAssessmentsParas.add(learnerQCTOLearnership.getZZLearnerQCTOLearnership_ID());
			learnerAssessmentsQuery.setParameters(learnerAssessmentsParas);
			List<PO> learnerAssessments = learnerAssessmentsQuery.list();
			
			List<List<PO>> loadSavedDatas = RowData.mergedList(modules, learnerAssessments, (po1, po2) -> {
				X_ZZQctoModule module = (X_ZZQctoModule)po1;
				X_ZZLearnerQctoLearnershipAssessments learnerAssessment = (X_ZZLearnerQctoLearnershipAssessments)po2;
				return learnerAssessment.getZZQctoModule_ID() != 0 && learnerAssessment.getZZQctoModule_ID() == module.getZZQctoModule_ID();
			});
			
			tmQctoLearnershipAssessments.resetMultiPo(loadSavedDatas);
		}
		
		
		
		setStep("assessment");
	}
	
	TableModel tmLearnerQCTOArtisans;
	public void initLearnerQCTOArtisans() {
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel col = CellModel.getColModelForLabel(
				MasterUtil.getNameOfColTranslated(I_ZZQctoLearnership.Table_Name, I_ZZQctoLearnership.COLUMNNAME_ZZLearnershipCode)
				, I_ZZQctoLearnership.COLUMNNAME_ZZLearnershipCode
				).setTableName(I_ZZQctoLearnership.Table_Name);
		cols.add(col);
		
		col = CellModel.getColModelForLabel(
				MasterUtil.getNameOfColTranslated(I_ZZQctoLearnership.Table_Name, I_ZZQctoLearnership.COLUMNNAME_ZZLearnershipTitle)
				, I_ZZQctoLearnership.COLUMNNAME_ZZLearnershipTitle
				).setTableName(I_ZZQctoLearnership.Table_Name);
		cols.add(col);
		
		col = CellModel.getColModelForLabel(
				MasterUtil.getNameOfColTranslated(I_ZZLearnerQCTOArtisans.Table_Name, I_ZZLearnerQCTOArtisans.COLUMNNAME_ZZStudentNumber)
				, I_ZZLearnerQCTOArtisans.COLUMNNAME_ZZStudentNumber
				).setTableName(I_ZZLearnerQCTOArtisans.Table_Name);
		cols.add(col);
		
		col = DateCellModel.getDateColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZLearnerQCTOArtisans.Table_Name, I_ZZLearnerQCTOArtisans.COLUMNNAME_ZZCommencementDate)
				, I_ZZLearnerQCTOArtisans.COLUMNNAME_ZZStudentNumber
				).setTableName(I_ZZLearnerQCTOArtisans.Table_Name).setReadonly(true);
		cols.add(col);
		
		col = DateCellModel.getDateColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZLearnerQCTOArtisans.Table_Name, I_ZZLearnerQCTOArtisans.COLUMNNAME_ZZCompletionDate)
				, I_ZZLearnerQCTOArtisans.COLUMNNAME_ZZStudentNumber
				).setTableName(I_ZZLearnerQCTOArtisans.Table_Name).setReadonly(true);
		cols.add(col);
		
		tmLearnerQCTOArtisans = TableModel.getTableBean(TableModel.class, cols, false, I_ZZLearnerQCTOArtisans.Table_Name);
		tmLearnerQCTOArtisans.setViewModel(ViewType.VIEW_GRID);
		tmLearnerQCTOArtisans.setSclass("srd-LearnerAssessment-learnerQCTOArtisans");
	}
	
	public void initLearnerQCTOLearnership() {
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel col = CellModel.getColModelForLabel(
				MasterUtil.getNameOfColTranslated(I_ZZQctoLearnership.Table_Name, I_ZZQctoLearnership.COLUMNNAME_ZZLearnershipCode)
				, I_ZZQctoLearnership.COLUMNNAME_ZZLearnershipCode
				).setTableName(I_ZZQctoLearnership.Table_Name);
		cols.add(col);
		
		col = CellModel.getColModelForLabel(
				MasterUtil.getNameOfColTranslated(I_ZZQctoLearnership.Table_Name, I_ZZQctoLearnership.COLUMNNAME_ZZLearnershipTitle)
				, I_ZZQctoLearnership.COLUMNNAME_ZZLearnershipTitle
				).setTableName(I_ZZQctoLearnership.Table_Name);
		cols.add(col);
		
		col = CellModel.getColModelForLabel(
				MasterUtil.getNameOfColTranslated(I_ZZLearnerQCTOLearnership.Table_Name, I_ZZLearnerQCTOLearnership.COLUMNNAME_ZZStudentNumber)
				, I_ZZLearnerQCTOLearnership.COLUMNNAME_ZZStudentNumber
				).setTableName(I_ZZLearnerQCTOLearnership.Table_Name);
		cols.add(col);
		
		col = DateCellModel.getDateColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZLearnerQCTOLearnership.Table_Name, I_ZZLearnerQCTOLearnership.COLUMNNAME_ZZCommencementDate)
				, I_ZZLearnerQCTOLearnership.COLUMNNAME_ZZStudentNumber
				).setTableName(I_ZZLearnerQCTOLearnership.Table_Name).setReadonly(true);
		cols.add(col);
		
		col = DateCellModel.getDateColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZLearnerQCTOLearnership.Table_Name, I_ZZLearnerQCTOLearnership.COLUMNNAME_ZZCompletionDate)
				, I_ZZLearnerQCTOLearnership.COLUMNNAME_ZZStudentNumber
				).setTableName(I_ZZLearnerQCTOLearnership.Table_Name).setReadonly(true);
		cols.add(col);
		
		tmLearnerQCTOLearnerships = TableModel.getTableBean(TableModel.class, cols, false, I_ZZLearnerQCTOLearnership.Table_Name);
		tmLearnerQCTOLearnerships.setViewModel(ViewType.VIEW_GRID);
		tmLearnerQCTOLearnerships.setSclass("srd-LearnerAssessment-learnerQCTOLearnership");
	}
	
	public void initLearnerQCTOSkills() {
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel col = CellModel.getColModelForLabel(
				MasterUtil.getNameOfColTranslated(I_ZZQctoSkillsProgramme.Table_Name, I_ZZQctoSkillsProgramme.COLUMNNAME_ZZSkillsProgrammeCode)
				, I_ZZQctoSkillsProgramme.COLUMNNAME_ZZSkillsProgrammeCode
				).setTableName(I_ZZQctoSkillsProgramme.Table_Name);
		cols.add(col);
		
		col = CellModel.getColModelForLabel(
				MasterUtil.getNameOfColTranslated(I_ZZQctoSkillsProgramme.Table_Name, I_ZZQctoSkillsProgramme.COLUMNNAME_ZZSkillsProgrammeTitle)
				, I_ZZQctoSkillsProgramme.COLUMNNAME_ZZSkillsProgrammeTitle
				).setTableName(I_ZZQctoSkillsProgramme.Table_Name);
		cols.add(col);
		
		col = CellModel.getColModelForLabel(
				MasterUtil.getNameOfColTranslated(I_ZZLearnerQCTOSkillsProgramme.Table_Name, I_ZZLearnerQCTOSkillsProgramme.COLUMNNAME_ZZStudentNumber)
				, I_ZZLearnerQCTOSkillsProgramme.COLUMNNAME_ZZStudentNumber
				).setTableName(I_ZZLearnerQCTOSkillsProgramme.Table_Name);
		cols.add(col);
		
		col = DateCellModel.getDateColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZLearnerQCTOSkillsProgramme.Table_Name, I_ZZLearnerQCTOSkillsProgramme.COLUMNNAME_ZZCommencementDate)
				, I_ZZLearnerQCTOSkillsProgramme.COLUMNNAME_ZZStudentNumber
				).setTableName(I_ZZLearnerQCTOSkillsProgramme.Table_Name).setReadonly(true);
		cols.add(col);
		
		col = DateCellModel.getDateColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZLearnerQCTOSkillsProgramme.Table_Name, I_ZZLearnerQCTOSkillsProgramme.COLUMNNAME_ZZCompletionDate)
				, I_ZZLearnerQCTOSkillsProgramme.COLUMNNAME_ZZStudentNumber
				).setTableName(I_ZZLearnerQCTOSkillsProgramme.Table_Name).setReadonly(true);
		cols.add(col);
		
		tmLearnerQCTOSkills = TableModel.getTableBean(TableModel.class, cols, false, I_ZZLearnerQCTOSkillsProgramme.Table_Name);
		tmLearnerQCTOSkills.setViewModel(ViewType.VIEW_GRID);
		tmLearnerQCTOSkills.setSclass("srd-LearnerAssessment-learnerQCTOSkill");
		
	}
	
	public static void setupAssessorSearchCol(ValueAdaptColumnModel chooseAssessorCol, String where) {
		chooseAssessorCol.setEventHandle((event, cellModel) -> {
			showInfoPanel(
			InfoPanelPara.getInstance(I_ZZAssessorPerson_v.Table_Name
					, I_ZZAssessorPerson_v.COLUMNNAME_ZZAssessorPerson_v_ID).setWhereClause(where)
			, obj -> {
				Object [] objs = (Object [])obj;
				int assessorID = (int)objs[0];
				
				X_ZZAssessorPerson_v assessor = new X_ZZAssessorPerson_v(Env.getCtx(), assessorID, null); 
				cellModel.setValue(assessor);
			});
		});
		
		chooseAssessorCol.setDisplayAdaptHandle(value -> {
			if (value == null)
				return null;

			X_ZZAssessorPerson_v Assessor = (X_ZZAssessorPerson_v) value;
			return Assessor.getZZFirstName() + " " + Assessor.getZZSurname();
		});

		chooseAssessorCol.setValueAdaptHandle(value -> {
			if (value == null)
				return null;

			X_ZZAssessorPerson_v schoolEmis = (X_ZZAssessorPerson_v) value;
			return schoolEmis.getZZAssessorPerson_ID();
		});

		chooseAssessorCol.setValueFromDaoAdaptHandle(obj -> {
			if (obj == null)
				return null;

			Integer id = Integer.class.cast(obj);
			if (id == 0)
				return null;

			return new X_ZZAssessorPerson_v(Env.getCtx(), id, null);
		});
	}
	
	ValueAdaptColumnModel chooseAssessorCol;
	ColumnModel assessmentDate;
	ValueAdaptColumnModel chooseModeratorCol;
	ColumnModel moderationDatecol;
	CheckboxColumnModel competentCol;
	ColumnModel rplcol;
	public void initAssessmentParam() {
		List<ColumnModel> cols = new ArrayList<>();

		chooseAssessorCol = ValueAdaptCellModel.getValueAdaptColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZLearnerQctoLearnershipAssessments.Table_Name, I_ZZLearnerQctoLearnershipAssessments.COLUMNNAME_ZZAssessorPerson_ID), 
				I_ZZLearnerQctoLearnershipAssessments.COLUMNNAME_ZZAssessorPerson_ID, 
				CellModel.SEARCH_CELL);
		
		cols.add(chooseAssessorCol);
		
		assessmentDate = DateCellModel.getDateColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZLearnerQctoLearnershipAssessments.Table_Name, I_ZZLearnerQctoLearnershipAssessments.COLUMNNAME_ZZAssessmentDate)
				, I_ZZLearnerQctoLearnershipAssessments.COLUMNNAME_ZZAssessmentDate
				).setTableName(I_ZZLearnerQctoLearnershipAssessments.Table_Name);
		cols.add(assessmentDate);
		
		chooseModeratorCol = ValueAdaptCellModel.getValueAdaptColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZLearnerQctoLearnershipAssessments.Table_Name, I_ZZLearnerQctoLearnershipAssessments.COLUMNNAME_ZZModerator_ID), 
				I_ZZLearnerQctoLearnershipAssessments.COLUMNNAME_ZZModerator_ID, 
				CellModel.SEARCH_CELL);
		
		cols.add(chooseModeratorCol);
		
		moderationDatecol = DateCellModel.getDateColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZLearnerQctoLearnershipAssessments.Table_Name, I_ZZLearnerQctoLearnershipAssessments.COLUMNNAME_ZZModerationDate)
				, I_ZZLearnerQctoLearnershipAssessments.COLUMNNAME_ZZModerationDate
				).setTableName(I_ZZLearnerQctoLearnershipAssessments.Table_Name);
		cols.add(moderationDatecol);
		
		competentCol = CheckboxCellModel.getCheckboxColModel(
				"Competent"
				, null
				);
		cols.add(competentCol);
		
		rplcol = CheckboxCellModel.getCheckboxColModel(
				MasterUtil.getNameOfColTranslated(I_ZZLearnerQctoLearnershipAssessments.Table_Name, I_ZZLearnerQctoLearnershipAssessments.COLUMNNAME_ZZRPL)
				, I_ZZLearnerQctoLearnershipAssessments.COLUMNNAME_ZZRPL
				).setTableName(I_ZZLearnerQctoLearnershipAssessments.Table_Name);
		cols.add(rplcol);
		
		ColumnModel colAssessmentBt = CellModel.getColModelForGenericCell("Assessment", null, CellModel.BUTTON_CELL);
		cols.add(colAssessmentBt);
		
		colAssessmentBt.setEventHandle((event, cellModel) -> {
			try {
				TableModel currentLearnerAssessments = getTmLearnerAssessments();
				
				ISaveForm.batchManualSaveToDb(List.of(currentLearnerAssessments));
				
				currentLearnerAssessments.syncDaoToUI();
				
				MasterUtil.showInfoDialog("ZZLearnerAssessmentsSuccess", null);
				
			} catch (Exception e) {
				log.log(Level.WARNING, "ZZLearnerAssessmentsError", e);
				MasterUtil.showInfoDialog("ZZLearnerAssessmentsError", null);
			}
		});
		
		setupAssessorSearchCol(chooseAssessorCol, I_ZZAssessorPerson_v.COLUMNNAME_ZZAssessorRole + "='" + X_ZZAssessorPerson_v.ZZASSESSORROLE_Assessor + "'");
		setupAssessorSearchCol(chooseModeratorCol, I_ZZAssessorPerson_v.COLUMNNAME_ZZAssessorRole + "='" + X_ZZAssessorPerson_v.ZZASSESSORROLE_Moderator + "'");
		
		tmAssessmentParam = TableModel.getTableBean(TableModel.class, cols, false, I_ZZLearnerQctoLearnershipAssessments.Table_Name);
		tmAssessmentParam.setViewModel(ViewType.VIEW_GRID);
		tmAssessmentParam.setSclass("srd-LearnerAssessment-assessmentParam");
		tmAssessmentParam.init();
		
	}
	
	CheckboxColumnModel qctoLearnershipAssessmentsSelectedCol;
	public void initQctoLearnershipAssessments() {
		List<ColumnModel> cols = new ArrayList<>();

		qctoLearnershipAssessmentsSelectedCol = CheckboxCellModel.getCheckboxColModel(
				""
				, null
				);
		cols.add(qctoLearnershipAssessmentsSelectedCol);
		
		ColumnModel col = CellModel.getColModelForLabel(
				MasterUtil.getNameOfColTranslated(I_ZZQctoModule.Table_Name, I_ZZQctoModule.COLUMNNAME_ZZModuleCode)
				, I_ZZQctoModule.COLUMNNAME_ZZModuleCode
				).setTableName(I_ZZQctoModule.Table_Name);
		cols.add(col);
		
		col = CellModel.getColModelForLabel(
				MasterUtil.getNameOfColTranslated(I_ZZQctoModule.Table_Name, I_ZZQctoModule.COLUMNNAME_ZZModuleTitle)
				, I_ZZQctoModule.COLUMNNAME_ZZModuleTitle
				).setTableName(I_ZZQctoModule.Table_Name);
		cols.add(col);
		
		//TODO not sure load form I_ZZLearnerQCTOLearnership.COLUMNNAME_ZZCredits or I_ZZQctoModule.COLUMNNAME_ZZCredits
		col = CellModel.getColModelForLabel(
				MasterUtil.getNameOfColTranslated(I_ZZQctoModule.Table_Name, I_ZZQctoModule.COLUMNNAME_ZZCredits)
				, I_ZZQctoModule.COLUMNNAME_ZZModuleTitle
				).setTableName(I_ZZQctoModule.Table_Name);
		cols.add(col);
		
		col = CellModel.getColModelForLabel(
				MasterUtil.getNameOfColTranslated(I_ZZQctoModule.Table_Name, I_ZZQctoModule.COLUMNNAME_ZZModuleType)
				, I_ZZQctoModule.COLUMNNAME_ZZModuleType
				).setTableName(I_ZZQctoModule.Table_Name);
		cols.add(col);
		
		col = CellModel.getColModelForLabel(
				MasterUtil.getNameOfColTranslated(I_ZZLearnerQctoLearnershipAssessments.Table_Name, I_ZZLearnerQctoLearnershipAssessments.COLUMNNAME_ZZRPL)
				, I_ZZLearnerQctoLearnershipAssessments.COLUMNNAME_ZZRPL
				).setTableName(I_ZZLearnerQctoLearnershipAssessments.Table_Name);
		cols.add(col);
		
		col = CellModel.getColModelForLabel(
				MasterUtil.getNameOfColTranslated(I_ZZLearnerQctoLearnershipAssessments.Table_Name, I_ZZLearnerQctoLearnershipAssessments.COLUMNNAME_ZZPreviouslyAchieved)
				, I_ZZLearnerQctoLearnershipAssessments.COLUMNNAME_ZZPreviouslyAchieved
				).setTableName(I_ZZLearnerQctoLearnershipAssessments.Table_Name);
		cols.add(col);
		
		col = CellModel.getColModelForLabel(
				MasterUtil.getNameOfColTranslated(I_ZZLearnerQctoLearnershipAssessments.Table_Name, I_ZZLearnerQctoLearnershipAssessments.COLUMNNAME_ZZAssessorPerson_ID)
				, I_ZZLearnerQctoLearnershipAssessments.COLUMNNAME_ZZAssessorPerson_ID
				).setTableName(I_ZZLearnerQctoLearnershipAssessments.Table_Name);
		cols.add(col);
		
		col = DateCellModel.getDateColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZLearnerQctoLearnershipAssessments.Table_Name, I_ZZLearnerQctoLearnershipAssessments.COLUMNNAME_ZZAssessmentDate)
				, I_ZZLearnerQctoLearnershipAssessments.COLUMNNAME_ZZAssessmentDate
				).setTableName(I_ZZLearnerQctoLearnershipAssessments.Table_Name).setReadonly(true);
		cols.add(col);
		
		col = CellModel.getColModelForLabel(
				MasterUtil.getNameOfColTranslated(I_ZZLearnerQctoLearnershipAssessments.Table_Name, I_ZZLearnerQctoLearnershipAssessments.COLUMNNAME_ZZModerator_ID)
				, I_ZZLearnerQctoLearnershipAssessments.COLUMNNAME_ZZModerator_ID
				).setTableName(I_ZZLearnerQctoLearnershipAssessments.Table_Name);
		cols.add(col);
		
		col = DateCellModel.getDateColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZLearnerQctoLearnershipAssessments.Table_Name, I_ZZLearnerQctoLearnershipAssessments.COLUMNNAME_ZZModerationDate)
				, I_ZZLearnerQctoLearnershipAssessments.COLUMNNAME_ZZModerationDate
				).setTableName(I_ZZLearnerQctoLearnershipAssessments.Table_Name).setReadonly(true);
		cols.add(col);
		
		tmQctoLearnershipAssessments = TableModel.getTableBean(TableModel.class, cols, false, I_ZZLearnerQctoLearnershipAssessments.Table_Name);
		tmQctoLearnershipAssessments.setViewModel(ViewType.VIEW_GRID);
		tmQctoLearnershipAssessments.setSclass("srd-LearnerAssessment-qctoLearnershipAssessments");
		
		tmQctoLearnershipAssessments.setRowSaveFilter(rowMode -> {
			CheckboxCellModel selectionCell = (CheckboxCellModel)rowMode.get(qctoLearnershipAssessmentsSelectedCol);
			return selectionCell.isChecked();
		});
		
		tmQctoLearnershipAssessments.setBeforeSave((rowDbEventArgs) -> {
			if (!rowDbEventArgs.isRowEven())
				return true;
			
			X_ZZLearnerQctoLearnershipAssessments assessment = (X_ZZLearnerQctoLearnershipAssessments)rowDbEventArgs.row().getRowData().getDataNewWhenNull(I_ZZLearnerQctoLearnershipAssessments.Table_Name);
			
			DateCellModel assessmentDateCell = (DateCellModel)tmAssessmentParam.getRow().get(assessmentDate);
			assessment.setZZAssessmentDate(assessmentDateCell.getTimestamp());
			
			ValueAdaptCellModel assessorSelected = (ValueAdaptCellModel)tmAssessmentParam.getRow().get(chooseAssessorCol);
			if (assessorSelected.getValue() == null) {
				assessment.setZZAssessorPerson_ID(0);
			}else {
				assessment.setZZAssessorPerson_ID((int)assessorSelected.getValue());
			}
			
			//Query checkModerationQuery = MTable.get(Env.getCtx(), I_ZZAssessorPerson.Table_Name).createQuery("", null); 
			
			ValueAdaptCellModel moderationSelected = (ValueAdaptCellModel)tmAssessmentParam.getRow().get(chooseModeratorCol);
			if (moderationSelected.getValue() == null) {
				assessment.setZZModerator_ID(0);
			}else {
				assessment.setZZModerator_ID((int)moderationSelected.getValue());
			}
			
			DateCellModel moderatorDateCell = (DateCellModel)tmAssessmentParam.getRow().get(moderationDatecol);
			assessment.setZZModerationDate(moderatorDateCell.getTimestamp());
			
			CheckboxCellModel competentCell =  (CheckboxCellModel)tmAssessmentParam.getRow().get(competentCol);
			//
			
			CheckboxCellModel rplCell =  (CheckboxCellModel)tmAssessmentParam.getRow().get(rplcol);
			assessment.setZZRPL(rplCell.isChecked()?X_ZZLearnerQctoLearnershipAssessments.ZZRPL_Yes:X_ZZLearnerQctoLearnershipAssessments.ZZRPL_No);
			
			X_ZZQctoModule qctoModule = (X_ZZQctoModule)rowDbEventArgs.row().getRowData().getDataNullable(I_ZZQctoModule.Table_Name);
			assessment.setZZQctoModule_ID(qctoModule.getZZQctoModule_ID());
			assessment.setZZLearnerQCTOLearnership_ID(learnerQCTOLearnership.getZZLearnerQCTOLearnership_ID());
			assessment.saveEx(rowDbEventArgs.trxName());
			// assessment.setZZAssessmentStatus(null)
			//assessment.setZZAssessmentStatus()
			//assessment.setZZPreviouslyAchieved(null)
			return true;
		});
		
		
	}
	
	private TableModel tmQctoLearnershipAssessments;
	private TableModel tmAssessmentParam;
	
	public TableModel getTmAssessmentParam() {
		return tmAssessmentParam;
	}
	
	TableModel tmLearnerQCTOLearnerships;
	TableModel tmQctoArtisansAssessments;
	TableModel tmQCTOSkillsAssessments;
	TableModel tmLearnerQCTOSkills;
	
	public TableModel getTmLearnerAssessments() {
		if (isInterventionQCTOArtisans())
			return tmQctoArtisansAssessments;
		else if (isInterventionQCTOLearnerships())
			return tmQctoLearnershipAssessments;
		else if (isInterventionQCTOSkills())
			return tmQCTOSkillsAssessments;
		else 
			throw new AdempiereException("Not Yet suport program");
	}
	
	public TableModel getTmLearnerProgram() {
		if (isInterventionQCTOArtisans())
			return tmLearnerQCTOArtisans;
		else if (isInterventionQCTOLearnerships())
			return tmLearnerQCTOLearnerships;
		else if (isInterventionQCTOSkills())
			return tmLearnerQCTOSkills;
		else 
			throw new AdempiereException("Not Yet suport program");
	}
}
