package za.co.ntier.webform.form.bean.program;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.ProgramType;
import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.AnnexureRow;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.IntData;
import za.co.ntier.webform.form.bean.component.ProjectInput;
import za.co.ntier.webform.model.I_ZZLearnersApplied;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class MiningCommunityUnemployedYouthProgram implements ISaveForm, IProgram{
	private X_ZZ_Application_Form applicationForm;
	private ProjectInput budgetOverview;
	
	private ProjectInput learnerApplys;
	private ProjectInput strategy;
	private MenuContextInfo menuContextInfo;
	
	private String programTitle;
	private Integer monthlyStipend = 1700;
	private Integer maxMonthlyAllocation = 15000;
	
	private ColumnInfo<?> budgetColDuration = ColumnInfo.getColPositiveNumber("Duration of program (Months)" 
			, I_ZZLearnersApplied.COLUMNNAME_NoMonths);
	private ColumnInfo<?> budgetColLearners = ColumnInfo.getColPositiveNumber(ColumnInfo.colNoLearnersLabel
			, I_ZZLearnersApplied.COLUMNNAME_ZZNoLearners);
	private ColumnInfo<?> budgetColNameProgram = ColumnInfo.getColText(ColumnInfo.colNameProgrammeLabel
			, I_ZZLearnersApplied.COLUMNNAME_Name);
	private ColumnInfo<?> budgetColPostalCode = ColumnInfo.getColPostal(ColumnInfo.colPostalCodeLabel
			, I_ZZLearnersApplied.COLUMNNAME_Postal);
	private ColumnInfo<?> valueColLearnerApplys = ColumnInfo.getColPositiveNumber(ColumnInfo.colNoLearners
			, I_ZZLearnersApplied.COLUMNNAME_ZZNoLearners);
	private ColumnInfo<?> titleColLearnerApplys = ColumnInfo.getColLabel("Target Group"
			, I_ZZLearnersApplied.COLUMNNAME_Name);
	private ColumnInfo<?> titleColExitStrategy = ColumnInfo.getColLabel(""
			, I_ZZLearnersApplied.COLUMNNAME_Name);
	private ColumnInfo<?> valueColExitStrategy = ColumnInfo.getColText(""
			, I_ZZLearnersApplied.COLUMNNAME_Name2);
	
	Function<AnnexureRow<?>, Integer> stipendExpression = (row) -> {
		Integer duration = ((IntData)row.get(budgetColDuration)).getValue();
		Integer learners = ((IntData)row.get(budgetColLearners)).getValue();
		if (duration != null && learners != null) {
			int totalMonthlyStipend = monthlyStipend * duration;
			return (totalMonthlyStipend > maxMonthlyAllocation ? maxMonthlyAllocation : totalMonthlyStipend) * learners;
		}else {
			return null;
		}
		
	};
	
	Function<AnnexureRow<?>, Integer> trainingFeeExpression = (row) -> {
		Integer duration = ((IntData)row.get(budgetColDuration)).getValue();
		Integer learners = ((IntData)row.get(budgetColLearners)).getValue();
		if (duration != null && learners != null) {
			int totalAllocation = (maxMonthlyAllocation - (monthlyStipend * duration)) * learners;
			return (totalAllocation < 0 ? 0 : totalAllocation) * learners;
		}else {
			return null;
		}
		
	};
	
	
	public MiningCommunityUnemployedYouthProgram(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm) {
		this.applicationForm = applicationForm;
		this.menuContextInfo = menuContextInfo;
		
		setProgramTitle(menuContextInfo.getProgramType() == ProgramType.MINING_COMMUNITY?
				"MINE COMMUNITY DEVELOPMENT GRANT":"UNEMPLOYED YOUTH DEVELOPMENT PROGRAMMES GRANT");
		
		// learnerApplys
		List<ColumnInfo<?>> learnerApplyCols = List.of(
				valueColLearnerApplys,
				titleColLearnerApplys
				);
		
		learnerApplys = ProjectInput.getProject(learnerApplyCols, null, false);
		learnerApplys.setTableTitle("How many learners are you applying for?");
		learnerApplys.setDataType(AnnexureInfo.AnnexureTypeTargetGroup);
		
		List<Map<ColumnInfo<?>, Object>> titleRows = List.of(Map.of(titleColLearnerApplys, "Ex-Mine worker")
				, Map.of(titleColLearnerApplys, "People with disabilities")
				, Map.of(titleColLearnerApplys, "People living in rural areas")
				, Map.of(titleColLearnerApplys, "People living in urban areas"));
		learnerApplys.initProject(applicationForm, titleRows);
		
		// budgetOverview
		List<ColumnInfo<?>> budgetOverviewCols = List.of(
				budgetColNameProgram,
				budgetColPostalCode,
				budgetColDuration, budgetColLearners,
				ColumnInfo.getColExpression("Total stipend", stipendExpression),
				ColumnInfo.getColExpression("Training Fee", trainingFeeExpression)
				);
		budgetOverview = ProjectInput.getProject(budgetOverviewCols, null, false);
		budgetOverview.setShowAddButton(true);
		budgetOverview.setSubSectionHeader("BUDGET OVERVIEW");
		budgetOverview.setDataType(AnnexureInfo.AnnexureTypeBudgetOverview);
		
		budgetOverview.initProject(applicationForm);
		
		//strategy
		List<ColumnInfo<?>> strategyCols = List.of(
				titleColExitStrategy,
				valueColExitStrategy
				);
		strategy = ProjectInput.getProject(strategyCols, null, false);
		strategy.setSubSectionHeader("EXIT STRATEGY");
		strategy.setShowColumnHeader(false);
		strategy.setDataType(AnnexureInfo.AnnexureTypeExitStrategy);
		
		titleRows = List.of(Map.of(titleColExitStrategy, "Name of Training Intervention")
						, Map.of(titleColExitStrategy, "NQF Level and Credits")
						, Map.of(titleColExitStrategy, "Seta accredited with")
						, Map.of(titleColExitStrategy, "Provide a description of what post training opportunity/ies exist/s for the learners after completing programme (Exit strategy)"));
		strategy.initProject(applicationForm, titleRows);				
	}
	
	/**
	 * @return the applicationForm
	 */
	public X_ZZ_Application_Form getApplicationForm() {
		return applicationForm;
	}
	public ProjectInput getBudgetOverview() {
		return budgetOverview;
	}
	public ProjectInput getLearnerApplys() {
		return learnerApplys;
	}
	
	/**
	 * @return the menuContextInfo
	 */
	public MenuContextInfo getMenuContextInfo() {
		return menuContextInfo;
	}

	/**
	 * @return the programTitle
	 */
	public String getProgramTitle() {
		return programTitle;
	}

	@Override
	public boolean isProgramValid() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		this.applicationForm = applicationForm;
		
		learnerApplys.save(trxName, applicationForm);
		strategy.save(trxName, applicationForm);
		budgetOverview.save(trxName, applicationForm);
		// update total
		applicationForm.saveEx(trxName);
	}

	/**
	 * @param applicationForm the applicationForm to set
	 */
	public void setApplicationForm(X_ZZ_Application_Form applicationForm) {
		this.applicationForm = applicationForm;
	}

	public void setBudgetOverview(ProjectInput budgetOverview) {
		this.budgetOverview = budgetOverview;
	}

	public void setLearnerApplys(ProjectInput learnerApplys) {
		this.learnerApplys = learnerApplys;
	}

	/**
	 * @param menuContextInfo the menuContextInfo to set
	 */
	public void setMenuContextInfo(MenuContextInfo menuContextInfo) {
		this.menuContextInfo = menuContextInfo;
	}

	/**
	 * @param programTitle the programTitle to set
	 */
	public void setProgramTitle(String programTitle) {
		this.programTitle = programTitle;
	}

	/**
	 * @return the strategy
	 */
	public ProjectInput getStrategy() {
		return strategy;
	}

	/**
	 * @param strategy the strategy to set
	 */
	public void setStrategy(ProjectInput strategy) {
		this.strategy = strategy;
	}

}
