package za.co.ntier.webform.form.bean.program;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import za.co.ntier.api.model.I_ZZLearnersApplied;
import za.co.ntier.api.model.X_ZZ_Application_Form;
import za.co.ntier.webform.form.AbstractProgram;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.ProgramType;
import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.AnnexureRow;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.IntData;
import za.co.ntier.webform.form.bean.component.LabelData;
import za.co.ntier.webform.form.bean.component.ProjectInput;
import za.co.ntier.webform.form.bean.component.TextData;

public class MiningCommunityUnemployedYouthProgram extends AbstractProgram{

	private ProjectInput budgetOverview;
	
	private ProjectInput learnerApplys;
	private ProjectInput strategy;
	
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
	
	Function<AnnexureRow, Integer> stipendExpression = (row) -> {
		Integer duration = ((IntData)row.get(budgetColDuration)).getValue();
		Integer learners = ((IntData)row.get(budgetColLearners)).getValue();
		if (duration != null && learners != null) {
			int totalMonthlyStipend = monthlyStipend * duration;
			return (totalMonthlyStipend > maxMonthlyAllocation ? maxMonthlyAllocation : totalMonthlyStipend) * learners;
		}else {
			return null;
		}
		
	};
	
	Function<AnnexureRow, Integer> trainingFeeExpression = (row) -> {
		Integer duration = ((IntData)row.get(budgetColDuration)).getValue();
		Integer learners = ((IntData)row.get(budgetColLearners)).getValue();
		if (duration != null && learners != null) {
			int totalAllocation = (maxMonthlyAllocation - (monthlyStipend * duration)) * learners;
			return totalAllocation < 0 ? 0 : totalAllocation;
		}else {
			return null;
		}
		
	};
	
	
	public MiningCommunityUnemployedYouthProgram(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm) {
		super(menuContextInfo, applicationForm);
		
		budgetColLearners.setCalTotal(true);
		
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
		String specialRowTitle = "Provide a description of what post training opportunity/ies exist/s for the learners after completing programme (Exit strategy)";
		strategy = ProjectInput.getProject(strategyCols, null, false);
		strategy.setDecoratorCell((row) -> {
			LabelData titleData = (LabelData)row.get(titleColExitStrategy);
			if (specialRowTitle.equals(titleData.getValue())) {
				TextData textData = (TextData)row.get(valueColExitStrategy);
				textData.setLines(3);
			}
		});
		strategy.setSubSectionHeader("EXIT STRATEGY");
		strategy.setShowColumnHeader(false);
		strategy.setDataType(AnnexureInfo.AnnexureTypeExitStrategy);
		
		titleRows = List.of(Map.of(titleColExitStrategy, "Name of Training Intervention")
						, Map.of(titleColExitStrategy, "NQF Level and Credits")
						, Map.of(titleColExitStrategy, "Seta accredited with")
						, Map.of(titleColExitStrategy, specialRowTitle));
		strategy.initProject(applicationForm, titleRows);				
	}
	
	
	public ProjectInput getBudgetOverview() {
		return budgetOverview;
	}
	public ProjectInput getLearnerApplys() {
		return learnerApplys;
	}
	

	/**
	 * @return the programTitle
	 */
	public String getProgramTitle() {
		return programTitle;
	}

	

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		super.saveForm(applicationForm);
		
		learnerApplys.save(trxName, applicationForm);
		strategy.save(trxName, applicationForm);
		budgetOverview.save(trxName, applicationForm);
	}

	public void setBudgetOverview(ProjectInput budgetOverview) {
		this.budgetOverview = budgetOverview;
	}

	public void setLearnerApplys(ProjectInput learnerApplys) {
		this.learnerApplys = learnerApplys;
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
	
	@Override
	public boolean isProgramValid() {
	    // --- 1) Target Group: need at least one positive number ---
	    boolean hasTargetRow = false;
	    if (learnerApplys != null && learnerApplys.getRows() != null) {
	        for (Map<ColumnInfo<?>, Object> row : learnerApplys.getRows()) {
	            Integer n = AnnexureInfo.getIntegerValue(row, valueColLearnerApplys);
	            if (n != null && n > 0) { hasTargetRow = true; break; }
	        }
	    }

	    // --- 2) Budget Overview: at least one COMPLETE row if it’s been touched ---
	    boolean hasCompleteBudgetRow = false;
	    boolean hasTouchedBudgetRow  = false;

	    if (budgetOverview != null && budgetOverview.getRows() != null) {
	        for (Map<ColumnInfo<?>, Object> row : budgetOverview.getRows()) {

	            Integer dur      = AnnexureInfo.getIntegerValue(row, budgetColDuration);
	            Integer learners = AnnexureInfo.getIntegerValue(row, budgetColLearners);
	            String  name     = ((TextData) row.get(budgetColNameProgram)).getValue();

	            String postal = null;
	            Object pObj = row.get(budgetColPostalCode);
	            if (pObj instanceof za.co.ntier.webform.form.bean.component.PostalData) {
	                postal = ((za.co.ntier.webform.form.bean.component.PostalData) pObj).getPostal();
	            }

	            boolean touched =
	                (dur != null) ||
	                (learners != null) ||
	                (name != null && !name.trim().isEmpty()) ||
	                (postal != null && !postal.trim().isEmpty());

	            if (touched) {
	                hasTouchedBudgetRow = true;
	                boolean complete =
	                    (dur != null && dur > 0) &&
	                    (learners != null && learners > 0) &&
	                    (name != null && !name.trim().isEmpty()) &&
	                    (postal != null && !postal.trim().isEmpty());

	                if (complete) {
	                    hasCompleteBudgetRow = true;
	                } else {
	                    // a touched but incomplete row makes the section invalid
	                    return false;
	                }
	            }
	        }
	    }

	    // If nothing is entered at all in Budget Overview, it's invalid (must have >=1 row)
	    // Otherwise require at least one complete row.
	    boolean budgetOk = hasTouchedBudgetRow && hasCompleteBudgetRow;

	    return hasTargetRow && budgetOk;
	}



}
