package za.co.ntier.webform.form.bean.program;

import java.util.List;
import java.util.function.Function;

import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.ProgramType;
import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.AnnexureRow;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.IntData;
import za.co.ntier.webform.form.bean.component.LabelData;
import za.co.ntier.webform.model.X_ZZSubAnnex;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class MiningCommunityUnemployedYouthProgram implements ISaveForm, IProgram{
	private X_ZZ_Application_Form applicationForm;
	private AnnexureInfo budgetOverview;
	
	private AnnexureInfo learnerApplys;
	private AnnexureInfo strategy;
	private MenuContextInfo menuContextInfo;
	
	private String programTitle;

	public MiningCommunityUnemployedYouthProgram(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm) {
		this.applicationForm = applicationForm;
		this.menuContextInfo = menuContextInfo;
		
		setProgramTitle(menuContextInfo.getProgramType() == ProgramType.MINING_COMMUNITY?
				"MINE COMMUNITY DEVELOPMENT GRANT":"UNEMPLOYED YOUTH DEVELOPMENT PROGRAMMES GRANT");
		
		// learnerApplys
		ColumnInfo<?> valueCol = ColumnInfo.getColPositiveNumber("No.");
		ColumnInfo<?> titleCol = ColumnInfo.getColLabel("Target Group (Tick)");
		List<ColumnInfo<?>> learnerApplyCols = List.of(
				valueCol,
				titleCol
				);
				
		
		Function<AnnexureInfo, AnnexureRow<?>> supplierRowAnnexure = (parent) -> new AnnexureRow<X_ZZSubAnnex>(parent);
		
		learnerApplys = AnnexureInfo.getAnnexureInfo(AnnexureInfo.class, 
				learnerApplyCols, false);
		learnerApplys.setSupplier(supplierRowAnnexure);
		learnerApplys.setTableTitle("How many learners are you applying for?");
		
		List<String> rowTitles = List.of("Ex-Mine worker",
				"People with disabilities",
				"People living in rural areas",
				"People living in urban areas");
		
		List<X_ZZSubAnnex> subAnnexs = MasterUtil.loadSubAnnex(applicationForm);
		initTable(learnerApplys, rowTitles, subAnnexs, titleCol, valueCol);
		
		// budgetOverview
		ColumnInfo<?> colDuration = ColumnInfo.getColPositiveNumber("Duration of program (Months)");
		ColumnInfo<?> colLearners = ColumnInfo.getColPositiveNumber("No. of learners");
		
		Function<AnnexureRow<?>, Integer> stipendExpression = (row) -> {
			Integer duration = ((IntData)row.get(colDuration)).getValue();
			Integer learners = ((IntData)row.get(colLearners)).getValue();
			if (duration != null && learners != null) {
				return 1700 * duration * learners;
			}else {
				return null;
			}
			
		};
		
		Function<AnnexureRow<?>, Integer> trainingFeeExpression = (row) -> {
			Integer duration = ((IntData)row.get(colDuration)).getValue();
			Integer learners = ((IntData)row.get(colLearners)).getValue();
			if (duration != null && learners != null) {
				return (15000 - (duration * 1700)) * learners;
			}else {
				return null;
			}
			
		};
		
		
		
		List<ColumnInfo<?>> budgetOverviewCols = List.of(
				ColumnInfo.getColText("Name of program"),
				ColumnInfo.getColPostal("Postal Code"),
				colDuration, colLearners,
				ColumnInfo.getColExpression("Total stipend", stipendExpression),
				ColumnInfo.getColExpression("Training Fee", trainingFeeExpression)
				);
		budgetOverview = AnnexureInfo.getAnnexureInfo(AnnexureInfo.class, 
				budgetOverviewCols, false);
		budgetOverview.setSupplier(supplierRowAnnexure);
		budgetOverview.setShowAddButton(true);
		budgetOverview.setSubSectionHeader("BUDGET OVERVIEW");
		budgetOverview.createDetailRow();
		
		//strategy
		titleCol = ColumnInfo.getColLabel("");
		valueCol = ColumnInfo.getColPositiveNumber("");
		List<ColumnInfo<?>> strategyCols = List.of(
				titleCol,
				valueCol
				);
		strategy = AnnexureInfo.getAnnexureInfo(AnnexureInfo.class, 
				strategyCols, false);
		strategy.setSubSectionHeader("EXIT STRATEGY");
		strategy.setShowColumnHeader(false);
		strategy.setSupplier(supplierRowAnnexure);
		
		rowTitles = List.of("Name of Training Intervention",
				"NQF Level and Credits",
				"Seta accredited with",
				"Provide a description of what post training opportunity/ies exist/s for the learners after completing programme (Exit strategy)");
		
		subAnnexs = MasterUtil.loadSubAnnex(applicationForm);
		initTable(strategy, rowTitles, subAnnexs, titleCol, valueCol);
	}
	
	public void initTable(AnnexureInfo annexure, List<String> rowTitles, List<X_ZZSubAnnex> subAnnexs, ColumnInfo<?> colTitle, ColumnInfo<?> colValue) {
		for (String rowTitle : rowTitles) {
			AnnexureRow<?> row = annexure.createDetailRow();
			Integer value = null;
			if (subAnnexs != null) {
				for (X_ZZSubAnnex subAnnex : subAnnexs) {
					if (rowTitle.equals(subAnnex.getZZRequestedProgramme())) {
						value = subAnnex.getZZLearners();
						break;
					}
				}
			}
			LabelData labelData = ((LabelData)row.get(colTitle));
			labelData.setValue(rowTitle);
			
			IntData valueData = ((IntData)row.get(colValue));
			valueData.setValue(value);
		}
		
	}

	/**
	 * @return the applicationForm
	 */
	public X_ZZ_Application_Form getApplicationForm() {
		return applicationForm;
	}
	public AnnexureInfo getBudgetOverview() {
		return budgetOverview;
	}
	public AnnexureInfo getLearnerApplys() {
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
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param applicationForm the applicationForm to set
	 */
	public void setApplicationForm(X_ZZ_Application_Form applicationForm) {
		this.applicationForm = applicationForm;
	}

	public void setBudgetOverview(AnnexureInfo budgetOverview) {
		this.budgetOverview = budgetOverview;
	}

	public void setLearnerApplys(AnnexureInfo learnerApplys) {
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
	public AnnexureInfo getStrategy() {
		return strategy;
	}

	/**
	 * @param strategy the strategy to set
	 */
	public void setStrategy(AnnexureInfo strategy) {
		this.strategy = strategy;
	}

}
