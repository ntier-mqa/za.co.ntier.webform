package za.co.ntier.webform.form.bean.program;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.compiere.util.Env;

import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.Util;
import za.co.ntier.webform.form.bean.ProgramType;
import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.AnnexureRow;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.IntData;
import za.co.ntier.webform.form.bean.component.LabelData;
import za.co.ntier.webform.form.bean.component.PostalData;
import za.co.ntier.webform.model.X_ZZAnnexure;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class MiningCommunityUnemployedYouthProgram implements ISaveForm, IProgram{
	private X_ZZ_Application_Form applicationForm;
	private AnnexureInfo budgetOverview;
	
	private AnnexureInfo learnerApplys;
	private AnnexureInfo strategy;
	private MenuContextInfo menuContextInfo;
	
	private String programTitle;
	
	private BiConsumer<AnnexureRow<?>, X_ZZAnnexure> learnerSaveFunc;
	private BiConsumer<AnnexureRow<?>, X_ZZAnnexure> strategySaveFunc;
	private BiConsumer<AnnexureRow<?>, X_ZZAnnexure> budgetSaveFunc;
		
	private ColumnInfo<?> budgetColDuration = ColumnInfo.getColPositiveNumber("Duration of program (Months)");
	private ColumnInfo<?> budgetColLearners = ColumnInfo.getColPositiveNumber(ColumnInfo.colNoLearnersLabel);
	private ColumnInfo<?> budgetColNameProgram = ColumnInfo.getColText(ColumnInfo.colNameProgrammeLabel);
	private ColumnInfo<?> budgetColPostalCode = ColumnInfo.getColPostal(ColumnInfo.colPostalCodeLabel);
	public MiningCommunityUnemployedYouthProgram(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm) {
		this.applicationForm = applicationForm;
		this.menuContextInfo = menuContextInfo;
		
		setProgramTitle(menuContextInfo.getProgramType() == ProgramType.MINING_COMMUNITY?
				"MINE COMMUNITY DEVELOPMENT GRANT":"UNEMPLOYED YOUTH DEVELOPMENT PROGRAMMES GRANT");
		
		// learnerApplys
		ColumnInfo<?> valueColLearnerApplys = ColumnInfo.getColPositiveNumber("No.");
		ColumnInfo<?> titleColLearnerApplys = ColumnInfo.getColLabel("Target Group (Tick)");
		
		List<ColumnInfo<?>> learnerApplyCols = List.of(
				valueColLearnerApplys,
				titleColLearnerApplys
				);
				
		
		Function<AnnexureInfo, AnnexureRow<?>> supplierRowAnnexure = (parent) -> new AnnexureRow<X_ZZAnnexure>(parent);
		
		learnerApplys = AnnexureInfo.getAnnexureInfo(AnnexureInfo.class, 
				learnerApplyCols, false);
		learnerApplys.setSupplier(supplierRowAnnexure);
		learnerApplys.setTableTitle("How many learners are you applying for?");
		
		List<String> rowTitles = List.of("Ex-Mine worker",
				"People with disabilities",
				"People living in rural areas",
				"People living in urban areas");
		
		List<X_ZZAnnexure> subAnnexs = MasterUtil.loadAnnexure(applicationForm, AnnexureInfo.AnnexureTypeTargetGroup);
		initTable(learnerApplys, rowTitles, subAnnexs, titleColLearnerApplys, valueColLearnerApplys);
		
		learnerSaveFunc = (row, dao) -> {
			LabelData labelData = (LabelData)row.get(titleColLearnerApplys);
			dao.setName(labelData.getValue());
			
			IntData valueData = ((IntData)row.get(valueColLearnerApplys));
			dao.setZZNoLearners(Util.convert(valueData.getValue()));
		};
		
		// budgetOverview
		
		Function<AnnexureRow<?>, Integer> stipendExpression = (row) -> {
			Integer duration = ((IntData)row.get(budgetColDuration)).getValue();
			Integer learners = ((IntData)row.get(budgetColLearners)).getValue();
			if (duration != null && learners != null) {
				return 1700 * duration * learners;
			}else {
				return null;
			}
			
		};
		
		Function<AnnexureRow<?>, Integer> trainingFeeExpression = (row) -> {
			Integer duration = ((IntData)row.get(budgetColDuration)).getValue();
			Integer learners = ((IntData)row.get(budgetColLearners)).getValue();
			if (duration != null && learners != null) {
				return (15000 - (duration * 1700)) * learners;
			}else {
				return null;
			}
			
		};
		
		budgetSaveFunc = (row, dao) -> {
			String textData = (String)row.get(budgetColNameProgram);
			dao.setName(textData);
			
			PostalData postalData = (PostalData)row.get(budgetColPostalCode);
			dao.setPostal(Util.convertStr(postalData.getPostal()));
			
			IntData intData = ((IntData)row.get(budgetColLearners));
			dao.setZZNoLearners(Util.convert(intData.getValue()));
			
			intData = ((IntData)row.get(budgetColDuration));
			dao.setNoMonths(Util.convert(intData.getValue()));
		};
		
		List<ColumnInfo<?>> budgetOverviewCols = List.of(
				budgetColNameProgram,
				budgetColPostalCode,
				budgetColDuration, budgetColLearners,
				ColumnInfo.getColExpression("Total stipend", stipendExpression),
				ColumnInfo.getColExpression("Training Fee", trainingFeeExpression)
				);
		budgetOverview = AnnexureInfo.getAnnexureInfo(AnnexureInfo.class, 
				budgetOverviewCols, false);
		budgetOverview.setSupplier(supplierRowAnnexure);
		budgetOverview.setShowAddButton(true);
		budgetOverview.setSubSectionHeader("BUDGET OVERVIEW");
		
		subAnnexs = MasterUtil.loadAnnexure(applicationForm, AnnexureInfo.AnnexureTypeBudgetOverview);
		initBudgetTable(budgetOverview, subAnnexs);
		
		//strategy
		ColumnInfo<?> titleColExitStrategy = ColumnInfo.getColLabel("");
		ColumnInfo<?> valueColExitStrategy = ColumnInfo.getColPositiveNumber("");
		
		List<ColumnInfo<?>> strategyCols = List.of(
				titleColExitStrategy,
				valueColExitStrategy
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
		
		subAnnexs = MasterUtil.loadAnnexure(applicationForm, AnnexureInfo.AnnexureTypeExitStrategy);
		
		strategySaveFunc = (row, dao) -> {
			LabelData labelData = (LabelData)row.get(titleColExitStrategy);
			dao.setName(labelData.getValue());
			
			IntData valueData = ((IntData)row.get(valueColExitStrategy));
			dao.setZZNoLearners(Util.convert(valueData.getValue()));
		};
		
		initTable(strategy, rowTitles, subAnnexs, titleColExitStrategy, valueColExitStrategy);
	}
	
	public void initBudgetTable(AnnexureInfo annexure, List<X_ZZAnnexure> subAnnexs) {
		if (subAnnexs != null && subAnnexs.size() > 0) {
			for (X_ZZAnnexure dao : subAnnexs) {
				@SuppressWarnings("unchecked")
				AnnexureRow<X_ZZAnnexure> row = (AnnexureRow<X_ZZAnnexure>)annexure.createDetailRow();
				row.setData(dao);
				
				((IntData)row.get(budgetColDuration)).setValue(Util.convert(dao.getNoMonths()));
				((IntData)row.get(budgetColLearners)).setValue(Util.convert(dao.getZZNoLearners()));
				((PostalData)row.get(budgetColPostalCode)).setPostal(Util.convertStr(dao.getPostal()));
				row.put(budgetColNameProgram, Util.convertStr(dao.getName()));
				
			}
		}else {
			annexure.createDetailRow();
		}
		
		annexure.updateExpressionCol();
	}
	
	public void initTable(AnnexureInfo annexure, List<String> rowTitles, List<X_ZZAnnexure> subAnnexs, ColumnInfo<?> colTitle, ColumnInfo<?> colValue) {
		for (String rowTitle : rowTitles) {
			@SuppressWarnings("unchecked")
			AnnexureRow<X_ZZAnnexure> row = (AnnexureRow<X_ZZAnnexure>)annexure.createDetailRow();
			Integer value = null;
			if (subAnnexs != null) {
				for (X_ZZAnnexure subAnnex : subAnnexs) {
					if (rowTitle.equals(subAnnex.getName())) {
						value = Util.convert(subAnnex.getZZNoLearners());
						row.setData(subAnnex);
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

	public void saveTable(AnnexureInfo annexure, String annexureType, String trxName, BiConsumer<AnnexureRow<?>, X_ZZAnnexure> setter) {
		for (AnnexureRow<?> row : annexure.getRows()) {
			X_ZZAnnexure annexureDao = (X_ZZAnnexure)row.getData();
			if (annexureDao == null) {
				annexureDao = new X_ZZAnnexure(Env.getCtx(), 0, null);
				annexureDao.setDataType(annexureType);
				annexureDao.setZZ_Application_Form_ID(applicationForm.getZZ_Application_Form_ID());
			}
			
			setter.accept(row, annexureDao);

			annexureDao.saveEx(trxName);
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
		this.applicationForm = applicationForm;
		
		saveTable(learnerApplys, AnnexureInfo.AnnexureTypeTargetGroup, trxName, learnerSaveFunc);
		
		saveTable(strategy, AnnexureInfo.AnnexureTypeExitStrategy, trxName, strategySaveFunc);
		
		saveTable(budgetOverview, AnnexureInfo.AnnexureTypeBudgetOverview, trxName, budgetSaveFunc);
		
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
