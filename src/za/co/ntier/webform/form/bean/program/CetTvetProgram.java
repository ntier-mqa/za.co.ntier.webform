package za.co.ntier.webform.form.bean.program;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.compiere.model.MTable;
import org.compiere.model.Query;
import org.compiere.util.Env;

import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.DataType;
import za.co.ntier.webform.form.bean.ProgramType;
import za.co.ntier.webform.form.bean.component.AddressInfo;
import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.AnnexureRow;
import za.co.ntier.webform.form.bean.component.CetTvetMultiLineInput;
import za.co.ntier.webform.form.bean.component.CetTvetOneLineInput;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.IntData;
import za.co.ntier.webform.form.bean.component.LearnerInputInfo;
import za.co.ntier.webform.model.I_ZZAnnexure;
import za.co.ntier.webform.model.I_ZZSubAnnex;
import za.co.ntier.webform.model.X_ZZAnnexure;
import za.co.ntier.webform.model.X_ZZSubAnnex;
import za.co.ntier.webform.model.X_ZZ_Application_Form;
import za.co.ntier.webform.model.X_ZZ_FormDiscipline;

public class CetTvetProgram implements ISaveForm, IProgram {
	private AddressInfo addressInfo;

	private List<AnnexureInfo> annexureInfos;
	private MenuContextInfo menuContextInfo;

	private List<LearnerInputInfo> tradeInfo;

	
	public CetTvetProgram(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm){
		this.setMenuContextInfo(menuContextInfo);
		this.applicationForm = applicationForm;
		
		annexureInfos = new ArrayList<>();
		AnnexureInfo annexure = null;
		AnnexureInfo subAnnexure = null;
		List<ColumnInfo<?>> cols = null;
		Supplier<Map<ColumnInfo<?>, Object>> supplierRowSubAnnex = () -> new AnnexureRow<X_ZZSubAnnex>();
		Supplier<Map<ColumnInfo<?>, Object>> supplierRowAnnexure = () -> new AnnexureRow<X_ZZAnnexure>();
		String title = null;
		String rowTitle = null;
		X_ZZAnnexure annexureDap = null;
		
		if (menuContextInfo.getProgramType() == ProgramType.CET) {
			cols = List.of(ColumnInfo.getColLabel("Name of the Intervention"),
					ColumnInfo.getColPositiveNumber(CetTvetOneLineInput.colNoBeneficiariesTitle));
			title = "ANNEXURE A (Applicable to CET Colleges)";
			rowTitle = "CET learners funded to access AET Programmes";
			annexure = CetTvetOneLineInput.getCetTvetOneLineInput(
					title, cols, supplierRowAnnexure);
			
			annexureDap = loadInitRowAnnexure(annexure, cols, title, rowTitle);
			annexureInfos.add(annexure);

			cols = List.of(ColumnInfo.getColLabel("Name of the Intervention"),
					ColumnInfo.getColPositiveNumber(CetTvetOneLineInput.colNoBeneficiariesTitle),
					ColumnInfo.getColText(CetTvetOneLineInput.colDisciplineTitle));
			title = "ANNEXURE B (Applicable to CET Colleges)";
			rowTitle = "Number of TVET Colleges and HEI graduates that entered CET Internships";
			annexure = CetTvetOneLineInput.getCetTvetOneLineInput(
					title, cols, supplierRowAnnexure);

			annexureDap = loadInitRowAnnexure(annexure, cols, title, rowTitle);
			annexureInfos.add(annexure);

			cols = List.of(ColumnInfo.getColLabel("Name of the Intervention"),
					ColumnInfo.getColPositiveNumber(CetTvetOneLineInput.colNoBeneficiariesTitle));
			title = "ANNEXURE C (Applicable to CET Colleges)";
			rowTitle = "CET Managers receiving training on curriculum related studies";
			annexure = CetTvetOneLineInput.getCetTvetOneLineInput(
					title, cols, supplierRowAnnexure);

			annexureDap = loadInitRowAnnexure(annexure, cols, title, rowTitle);
			annexureInfos.add(annexure);
			
			cols = List.of(ColumnInfo.getColText(CetTvetMultiLineInput.colRequestedProgrammeTitle),
					ColumnInfo.getColPositiveNumber(CetTvetMultiLineInput.colNoManagersTitle));
			subAnnexure = CetTvetMultiLineInput.getCetTvetMultiLineInput(null,
					cols,
					supplierRowSubAnnex);
			
			loadInitRowSubAnnex(annexureDap, subAnnexure, cols);
			annexure.setSubAnnexure(subAnnexure);
			

			cols = List.of(ColumnInfo.getColLabel("Name of the Intervention"),
					ColumnInfo.getColPositiveNumber(CetTvetOneLineInput.colNoBeneficiariesTitle));
			title = "ANNEXURE D (Applicable to CET Colleges)";
			rowTitle = "Number of CET Colleges lecturers awarded skills development programmes";
			annexure = CetTvetOneLineInput.getCetTvetOneLineInput(
					title, cols, supplierRowAnnexure);

			annexureDap = loadInitRowAnnexure(annexure, cols, title, rowTitle);
			annexureInfos.add(annexure);
			
			cols = List.of(ColumnInfo.getColText(CetTvetMultiLineInput.colRequestedProgrammeTitle));
			subAnnexure = CetTvetMultiLineInput.getCetTvetMultiLineInput(null,
					cols,
					supplierRowSubAnnex);
			
			loadInitRowSubAnnex(annexureDap, subAnnexure, cols);
			annexure.setSubAnnexure(subAnnexure);
			
		} else if (menuContextInfo.getProgramType() == ProgramType.TVET) {
			
			cols = List.of(ColumnInfo.getColLabel("Name of the Intervention"),
					ColumnInfo.getColPositiveNumber(CetTvetOneLineInput.colNoBeneficiariesTitle),
					ColumnInfo.getColText(CetTvetOneLineInput.colDisciplineTitle));
			title = "ANNEXURE E (Applicable to TVET Colleges)";
			rowTitle = "Number of TVET College graduates that entered an internship programme (the MQA prioritises engineering and related disciplines and may support other disciplines at its sole discretion)";
			annexure = CetTvetOneLineInput.getCetTvetOneLineInput(
					title, cols, supplierRowAnnexure);

			annexureDap = loadInitRowAnnexure(annexure, cols, title, rowTitle);
			annexureInfos.add(annexure);
			
			cols = List.of(ColumnInfo.getColText(CetTvetMultiLineInput.colFieldStudyTitle),
					ColumnInfo.getColPositiveNumber(CetTvetMultiLineInput.colNoLearners));
			subAnnexure = CetTvetMultiLineInput.getCetTvetMultiLineInput(null,
					cols,
					supplierRowSubAnnex);
			
			loadInitRowSubAnnex(annexureDap, subAnnexure, cols);
			annexure.setSubAnnexure(subAnnexure);
			

			cols = List.of(ColumnInfo.getColLabel("Name of the Intervention"),
					ColumnInfo.getColPositiveNumber(CetTvetOneLineInput.colNoBeneficiariesTitle),
					ColumnInfo.getColPositiveNumber(CetTvetOneLineInput.colProgrammeApplyTitle));
			title = "ANNEXURE F  (Applicable to TVET Colleges)";
			rowTitle = "TVET Managers receiving training on curriculum related studies";
			annexure = CetTvetOneLineInput.getCetTvetOneLineInput(
					title, cols, supplierRowAnnexure);
			
			annexureDap = loadInitRowAnnexure(annexure, cols, title, rowTitle);
			annexureInfos.add(annexure);
			
			cols = List.of(ColumnInfo.getColText(CetTvetMultiLineInput.colRequestedProgrammeTitle),
					ColumnInfo.getColPositiveNumber(CetTvetMultiLineInput.colNoManagersTitle));
			subAnnexure = CetTvetMultiLineInput.getCetTvetMultiLineInput(null,
					cols,
					supplierRowSubAnnex);
			
			loadInitRowSubAnnex(annexureDap, subAnnexure, cols);
			annexure.setSubAnnexure(subAnnexure);
			

			cols = List.of(ColumnInfo.getColLabel("Name of the Intervention"),
					ColumnInfo.getColPositiveNumber(CetTvetOneLineInput.colTotalNoBeneficiariesTitle));
			title = "ANNEXURE G (Applicable to TVET Colleges)";
			rowTitle = "Number of TVET students requiring Work Integrated Learning to complete their work integrated learning placements (WIL)";
			annexure = CetTvetOneLineInput.getCetTvetOneLineInput(
					title, cols, supplierRowAnnexure);

			annexureDap = loadInitRowAnnexure(annexure, cols, title, rowTitle);
			annexureInfos.add(annexure);
			
			cols = List.of(ColumnInfo.getColText(CetTvetMultiLineInput.colFieldStudyTitle),
					ColumnInfo.getColPositiveNumber(CetTvetMultiLineInput.colNoLearners));
			subAnnexure = CetTvetMultiLineInput.getCetTvetMultiLineInput(
					"Please supply the list of possible fields of study for this WIL",
					cols,
					supplierRowSubAnnex);
			
			loadInitRowSubAnnex(annexureDap, subAnnexure, cols);
			annexure.setSubAnnexure(subAnnexure);

			cols = List.of(ColumnInfo.getColLabel("Name of the Intervention"),
					ColumnInfo.getColPositiveNumber(CetTvetOneLineInput.colTotalNoBeneficiariesTitle));
			title = "ANNEXURE H (Applicable to TVET Colleges)";
			rowTitle = "TVET Lecturers to be awarded bursaries to further their studies";
			annexure = CetTvetOneLineInput.getCetTvetOneLineInput(
					title, cols, supplierRowAnnexure);

			annexureDap = loadInitRowAnnexure(annexure, cols, title, rowTitle);
			annexureInfos.add(annexure);

			cols = List.of(ColumnInfo.getColLabel("Name of the Intervention"),
					ColumnInfo.getColTwoValue("Total Number of beneficiaries applying for"),
					ColumnInfo.getColTwoTitle("Programme Applied for"));
			List<String> twoTitleFirstRow = List.of("Occupational Health and Safety", "Other, specify");
			title =  "ANNEXURE I (Applicable to TVET Colleges)";
			rowTitle = "Lecturers exposed to industry through skills programmes";
			annexure = CetTvetOneLineInput.getCetTvetOneLineInput(
					title, cols, supplierRowAnnexure);
			
			annexureDap = loadInitRowAnnexure(annexure, cols, title, rowTitle, twoTitleFirstRow);
			annexureInfos.add(annexure);

		} else if (menuContextInfo.getProgramType() == ProgramType.TVET_BURSARS) {
			List<Object> rObjs = MasterUtil.queryLearnerInputInfos(
					menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID(),
					X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_Trade);
			
			@SuppressWarnings("unchecked")
			List<LearnerInputInfo> tradeObj = (List<LearnerInputInfo>) rObjs.get(0);
			tradeInfo = tradeObj;
			if (tradeInfo != null) {

				cols = List.of(ColumnInfo.getColList("Field of Study", tradeInfo),
						ColumnInfo.getColPositiveNumber(CetTvetMultiLineInput.colNoLearners));
				
				annexure = CetTvetMultiLineInput.getCetTvetMultiLineInput("TVET UNEMPLOYED BURSARS SUPPORT FUNDING APPLICATION",cols, supplierRowSubAnnex);
				
				loadInitRowSubAnnex(applicationForm, annexure, cols);
				
				annexure.getTotalRow().put(annexure.getColumnInfos().get(0), "Total Number of beneficiaries applying for");

				annexureInfos.add(annexure);
			}
		}

		addressInfo = new AddressInfo(menuContextInfo.getProgramType(), false);
	}
	
	public X_ZZAnnexure loadInitRowAnnexure(AnnexureInfo annexure, List<ColumnInfo<?>> cols, String title, String rowTitle) {
		return loadInitRowAnnexure(annexure, cols, title, rowTitle, null);
	}
	
	public X_ZZAnnexure loadInitRowAnnexure(AnnexureInfo annexure, List<ColumnInfo<?>> cols, String title, String rowTitle, List<String> twoTitleValue) {
		X_ZZAnnexure annexureDao = null;
		
		if (applicationForm != null) {
			Query annexureQuery = MTable.get(X_ZZAnnexure.Table_ID).createQuery(String.format("%s = ? AND %s = ?", 
					I_ZZAnnexure.COLUMNNAME_ZZ_Application_Form_ID, I_ZZAnnexure.COLUMNNAME_Name), null);
			annexureQuery.setParameters(applicationForm.getZZ_Application_Form_ID(), title);
			annexureQuery.setOrderBy(X_ZZAnnexure.COLUMNNAME_ZZAnnexure_ID);
			annexureDao = annexureQuery.first();
		}
		
		@SuppressWarnings("unchecked")
		AnnexureRow<X_ZZAnnexure> newRow = (AnnexureRow<X_ZZAnnexure>)annexure.createDetailRow(cols);
		
		if (title != null && cols.get(0).getDataType() == DataType.Label) {
			newRow.put(cols.get(0), rowTitle);
		}
		
		ColumnInfo<?> colTwoValue = AnnexureInfo.lookupColByDataType(DataType.TwoTitles, cols);
		if (twoTitleValue != null && colTwoValue != null) { 
			newRow.put(colTwoValue, twoTitleValue);
		}
		
		if (annexureDao == null) {
			return null;
		}
		
		ColumnInfo<?> colBeneficiaries = AnnexureInfo.lookupColByTitle(CetTvetOneLineInput.colNoBeneficiariesTitle, cols);
		ColumnInfo<?> colTotalBeneficiaries = AnnexureInfo.lookupColByTitle(CetTvetOneLineInput.colTotalNoBeneficiariesTitle, cols);
		ColumnInfo<?> colDiscipline = AnnexureInfo.lookupColByTitle(CetTvetOneLineInput.colDisciplineTitle, cols);
		ColumnInfo<?> colProgramme = AnnexureInfo.lookupColByTitle(CetTvetOneLineInput.colProgrammeApplyTitle, cols);
		
		newRow.setData(annexureDao);
		
		if (colBeneficiaries != null && annexureDao.getZZBeneficiaries() > 0) {
			((IntData)newRow.get(colBeneficiaries)).setValue(annexureDao.getZZBeneficiaries());
		}
		
		if (colTotalBeneficiaries != null && annexureDao.getZZTotalBeneficiaries() > 0) {
			((IntData)newRow.get(colTotalBeneficiaries)).setValue(annexureDao.getZZTotalBeneficiaries());
		}
		
		if (colDiscipline != null) {
			newRow.put(colDiscipline, annexureDao.getZZDiscipline());
		}
		
		if (colProgramme != null && annexureDao.getZZProgramme() > 0) {
			((IntData)newRow.get(colProgramme)).setValue(annexureDao.getZZProgramme());
		}
		
		return annexureDao;
	}
	
	public void loadInitRowSubAnnex(X_ZZAnnexure parent, AnnexureInfo subAnnex, List<ColumnInfo<?>> cols) {
		List<X_ZZSubAnnex> subAnnexs = null;
		if (parent != null) {
			Query subAnnexQuery = MTable.get(X_ZZSubAnnex.Table_ID).createQuery(String.format("%s = ?", I_ZZSubAnnex.COLUMNNAME_ZZAnnexure_ID), null);
			subAnnexQuery.setParameters(parent.getZZAnnexure_ID());
			subAnnexs = subAnnexQuery.list();
		}
		
		loadInitRowSubAnnex(subAnnexs, subAnnex, cols);
	}
	
	public void loadInitRowSubAnnex(X_ZZ_Application_Form parent, AnnexureInfo subAnnex, List<ColumnInfo<?>> cols) {
		List<X_ZZSubAnnex> subAnnexs = null;
		if (parent != null) {
			Query directSubAnnexQuery = MTable.get(X_ZZSubAnnex.Table_ID).createQuery(String.format("%s = ?", I_ZZSubAnnex.COLUMNNAME_ZZ_Application_Form_ID), null);
			directSubAnnexQuery.setParameters(applicationForm.getZZ_Application_Form_ID());
			subAnnexs = directSubAnnexQuery.list();
		}
		
		loadInitRowSubAnnex(subAnnexs, subAnnex, cols);
	}
	
	public void loadInitRowSubAnnex(List<X_ZZSubAnnex> subAnnexs, AnnexureInfo subAnnexure, List<ColumnInfo<?>> cols) {
		if (subAnnexs == null || subAnnexs.size() == 0) {
			subAnnexure.createDetailRow(cols);
			return;
		}

		ColumnInfo<?> colTrade = AnnexureInfo.lookupColByDataType(DataType.List, cols);
		ColumnInfo<?> colRequestedProgramme = AnnexureInfo.lookupColByTitle(CetTvetMultiLineInput.colRequestedProgrammeTitle, cols);
		ColumnInfo<?> colFieldStudy = AnnexureInfo.lookupColByTitle(CetTvetMultiLineInput.colFieldStudyTitle, cols);
		ColumnInfo<?> colNoLearners = AnnexureInfo.lookupColByTitle(CetTvetMultiLineInput.colNoLearners, cols);
		ColumnInfo<?> colNoManager = AnnexureInfo.lookupColByTitle(CetTvetMultiLineInput.colNoManagersTitle, cols);
		ColumnInfo<?> colNoBeneficiaries = AnnexureInfo.lookupColByTitle(CetTvetOneLineInput.colNoBeneficiariesTitle, cols);
		ColumnInfo<?> colProgrammeApply = AnnexureInfo.lookupColByTitle(CetTvetOneLineInput.colProgrammeApplyTitle, cols);
		ColumnInfo<?> colTotalNoBeneficiaries = AnnexureInfo.lookupColByTitle(CetTvetOneLineInput.colTotalNoBeneficiariesTitle, cols);
		
		for (X_ZZSubAnnex subAnnex : subAnnexs) {
			@SuppressWarnings("unchecked")
			AnnexureRow<X_ZZSubAnnex> newRow = (AnnexureRow<X_ZZSubAnnex>)subAnnexure.createDetailRow(cols);
			newRow.setData(subAnnex);
			if(colTrade != null) {
				if(subAnnex.getZZ_Trade_ID() != 0) {
					for (Object tradeObj : colTrade.getDataProvider()) {
						LearnerInputInfo trade = (LearnerInputInfo)tradeObj;
						if (trade.getLearnerInputID() == subAnnex.getZZ_Trade_ID()){
							newRow.put(colTrade, colNoLearners);
							break;
						}
						
					}
				}
			}
			
			if (colRequestedProgramme != null) {
				newRow.put(colRequestedProgramme, subAnnex.getZZRequestedProgramme());
			}
			
			if (colFieldStudy != null && colFieldStudy.getDataType() != DataType.List) {
				newRow.put(colFieldStudy, subAnnex.getZZFieldStudy());
			}
			
			if (colNoLearners != null && subAnnex.getZZLearners() > 0) {
				((IntData)newRow.get(colNoLearners)).setValue(subAnnex.getZZLearners());

			}
			
			if (colNoManager != null && subAnnex.getZZManagers() > 0) {
				((IntData)newRow.get(colNoManager)).setValue(subAnnex.getZZManagers());

			}
			
			if (colNoBeneficiaries != null && subAnnex.getZZBeneficiaries() > 0) {
				((IntData)newRow.get(colNoBeneficiaries)).setValue(subAnnex.getZZBeneficiaries());

			}
			
			if (colTotalNoBeneficiaries != null && subAnnex.getZZTotalBeneficiaries() > 0) {
				((IntData)newRow.get(colTotalNoBeneficiaries)).setValue(subAnnex.getZZTotalBeneficiaries());

			}
			
			if (colProgrammeApply != null && subAnnex.getZZProgramme() > 0) {
				((IntData)newRow.get(colProgrammeApply)).setValue(subAnnex.getZZProgramme());

			}
			
			
		}
	}

	public AddressInfo getAddressInfo() {
		return addressInfo;
	}

	/**
	 * @return the annexureInfos
	 */
	public List<AnnexureInfo> getAnnexureInfos() {
		return annexureInfos;
	}

	public MenuContextInfo getMenuContextInfo() {
		return menuContextInfo;
	}

	public List<LearnerInputInfo> getTradeInfo() {
		return tradeInfo;
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		this.applicationForm = applicationForm;
		int total = 0;
		for (AnnexureInfo annexure : annexureInfos) {
			int areaTotal = 0;
			if (annexure instanceof CetTvetOneLineInput) {
				Entry<X_ZZAnnexure, Integer> result = saveOnelineInput(trxName, applicationForm, (CetTvetOneLineInput)annexure);
				areaTotal = result.getValue();
				if (annexure.getSubAnnexure() != null) {
					saveMultilineInput(trxName, applicationForm, result.getKey(), (CetTvetMultiLineInput)annexure.getSubAnnexure());
				}
			}else if (annexure instanceof CetTvetMultiLineInput) {
				areaTotal = saveMultilineInput(trxName, applicationForm, null, (CetTvetMultiLineInput)annexure);
			}
			
			total += areaTotal;
		}
		applicationForm.setZZTotalNumberApplied(total);
		if (addressInfo != null)
			addressInfo.saveForm(trxName, applicationForm);
	}

	private X_ZZ_Application_Form applicationForm;
	
	public void initComponent(X_ZZ_Application_Form applicationForm) {
		this.applicationForm = applicationForm;
		if (addressInfo != null) {
			addressInfo.initComponent(applicationForm);
		}
	}
	
	public int saveMultilineInput(String trxName, X_ZZ_Application_Form applicationForm, X_ZZAnnexure zzAnnexure, CetTvetMultiLineInput cetTvetOneLineInput) {
		ColumnInfo<?> colTrade = AnnexureInfo.lookupColByDataType(DataType.List, cetTvetOneLineInput);
		ColumnInfo<?> colRequestedProgramme = AnnexureInfo.lookupColByTitle(CetTvetMultiLineInput.colRequestedProgrammeTitle, cetTvetOneLineInput);
		ColumnInfo<?> colFieldStudy = AnnexureInfo.lookupColByTitle(CetTvetMultiLineInput.colFieldStudyTitle, cetTvetOneLineInput);
		ColumnInfo<?> colNoLearners = AnnexureInfo.lookupColByTitle(CetTvetMultiLineInput.colNoLearners, cetTvetOneLineInput);
		ColumnInfo<?> colNoManager = AnnexureInfo.lookupColByTitle(CetTvetMultiLineInput.colNoManagersTitle, cetTvetOneLineInput);
		ColumnInfo<?> colNoBeneficiaries = AnnexureInfo.lookupColByTitle(CetTvetOneLineInput.colNoBeneficiariesTitle, cetTvetOneLineInput);
		ColumnInfo<?> colProgrammeApply = AnnexureInfo.lookupColByTitle(CetTvetOneLineInput.colProgrammeApplyTitle, cetTvetOneLineInput);
		ColumnInfo<?> colTotalNoBeneficiaries = AnnexureInfo.lookupColByTitle(CetTvetOneLineInput.colTotalNoBeneficiariesTitle, cetTvetOneLineInput);
		
		int total = 0;
		Integer cellData = null;
		
		
		for (Map<ColumnInfo<?>, Object> rowObj : cetTvetOneLineInput.getRows()) {
			@SuppressWarnings("unchecked")
			AnnexureRow<X_ZZSubAnnex> row = (AnnexureRow<X_ZZSubAnnex>)rowObj;
			
			X_ZZSubAnnex subAnnex = null;
			if (row.getData() == null) {
				subAnnex = new X_ZZSubAnnex(Env.getCtx(), 0, trxName);
			}else
				subAnnex = row.getData();
			
			boolean hasData = false;
			
			String cellDataStr = (String)row.get(colRequestedProgramme);
			if (cellDataStr != null) {
				subAnnex.setZZRequestedProgramme(cellDataStr);
				hasData = true;
			}
			
			cellData = AnnexureInfo.getIntegerValue(row, colProgrammeApply);
			if (cellData != null && cellData != 0) {
				subAnnex.setZZProgramme(cellData);
				total += cellData;
				hasData = true;
			}
			
			cellData = AnnexureInfo.getIntegerValue(row, colNoManager);
			if (cellData != null && cellData != 0) {
				subAnnex.setZZManagers(cellData);
				total += cellData;
				hasData = true;
			}
			
			cellData = AnnexureInfo.getIntegerValue(row, colNoBeneficiaries);
			if (cellData != null && cellData != 0) {
				subAnnex.setZZBeneficiaries(cellData);
				total += cellData;
				hasData = true;
			}
			
			cellData = AnnexureInfo.getIntegerValue(row, colTotalNoBeneficiaries);
			if (cellData != null && cellData != 0) {
				subAnnex.setZZTotalBeneficiaries(cellData);
				total += cellData;
				hasData = true;
			}
			
			if (colFieldStudy != null && colFieldStudy.getDataType() != DataType.List) {
				cellDataStr = (String)row.get(colFieldStudy);
				if (cellDataStr != null) {
					subAnnex.setZZFieldStudy(cellDataStr);
					hasData = true;
				}
			}
			
			cellData = AnnexureInfo.getIntegerValue(row, colNoLearners);
			if (cellData != null && cellData != 0) {
				subAnnex.setZZLearners(cellData);
				total += cellData;
				hasData = true;
			}
			
			Object cellTrade = row.get(colTrade);
			if (cellData != null && cellTrade instanceof LearnerInputInfo) {
				LearnerInputInfo trade = (LearnerInputInfo)cellTrade;
				subAnnex.setZZ_Trade_ID(trade.getLearnerInputID());
				hasData = true;
			}
			
			if (hasData) {
				if(zzAnnexure != null)
					subAnnex.setZZAnnexure_ID(zzAnnexure.getZZAnnexure_ID());
				else if (applicationForm != null)
					subAnnex.setZZ_Application_Form_ID(applicationForm.getZZ_Application_Form_ID());
				
				subAnnex.saveEx(trxName);
				
				if (row.getData() == null)
					row.setData(subAnnex);
			}
		}
		
		return total;
	}
	
	
	
	public Entry<X_ZZAnnexure, Integer> saveOnelineInput(String trxName, X_ZZ_Application_Form applicationForm, CetTvetOneLineInput cetTvetOneLineInput) {
		ColumnInfo<?> colBeneficiaries = AnnexureInfo.lookupColByTitle(CetTvetOneLineInput.colNoBeneficiariesTitle, cetTvetOneLineInput);
		ColumnInfo<?> colTotalBeneficiaries = AnnexureInfo.lookupColByTitle(CetTvetOneLineInput.colTotalNoBeneficiariesTitle, cetTvetOneLineInput);
		ColumnInfo<?> colDiscipline = AnnexureInfo.lookupColByTitle(CetTvetOneLineInput.colDisciplineTitle, cetTvetOneLineInput);
		ColumnInfo<?> colProgramme = AnnexureInfo.lookupColByTitle(CetTvetOneLineInput.colProgrammeApplyTitle, cetTvetOneLineInput);
		
		@SuppressWarnings("unchecked")	
		AnnexureRow<X_ZZAnnexure> row = (AnnexureRow<X_ZZAnnexure>)cetTvetOneLineInput.getRows().get(0);
		
		X_ZZAnnexure annexure = row.getData();
		if (annexure == null)
			annexure= new X_ZZAnnexure(Env.getCtx(), 0, trxName);
		
		int total = 0;
		boolean hasData = false;
		
		Integer cellData = AnnexureInfo.getIntegerValue(cetTvetOneLineInput, colBeneficiaries);
		if (cellData != null && cellData != 0) {
			annexure.setZZBeneficiaries(cellData);
			total += cellData;
			hasData = true;
		}
		
		cellData = AnnexureInfo.getIntegerValue(cetTvetOneLineInput, colTotalBeneficiaries);
		if (cellData != null && cellData != 0) {
			annexure.setZZTotalBeneficiaries(cellData);
			total += cellData;
			hasData = true;
		}
		
		String cellDataStr = (String)cetTvetOneLineInput.getRows().get(0).get(colDiscipline);
		if (StringUtils.isNoneBlank(cellDataStr)) {
			annexure.setZZDiscipline(cellDataStr);
			hasData = true;
		}
		
		cellData = AnnexureInfo.getIntegerValue(cetTvetOneLineInput, colProgramme);
		if (cellData != null && cellData != 0) {
			annexure.setZZProgramme(cellData);
			total += cellData;
			hasData = true;
		}
		
		if (hasData) {
			annexure.setName(cetTvetOneLineInput.getSectionHeader());
			annexure.setZZ_Application_Form_ID(applicationForm.getZZ_Application_Form_ID());
			annexure.saveEx(trxName);
			row.setData(annexure);
		}
		
		return new AbstractMap.SimpleEntry<>(annexure, total);
	}
	
	public void setAddressInfo(AddressInfo addressInfo) {
		this.addressInfo = addressInfo;
	}
	
	/**
	 * @param annexureInfos the annexureInfos to set
	 */
	public void setAnnexureInfos(List<AnnexureInfo> annexureInfos) {
		this.annexureInfos = annexureInfos;
	}

	public void setMenuContextInfo(MenuContextInfo menuContextInfo) {
		this.menuContextInfo = menuContextInfo;
	}
	
	public void setTradeInfo(List<LearnerInputInfo> tradeInfo) {
		this.tradeInfo = tradeInfo;
	}

	/**
	 * @return the applicationForm
	 */
	public X_ZZ_Application_Form getApplicationForm() {
		return applicationForm;
	}

	/**
	 * @param applicationForm the applicationForm to set
	 */
	public void setApplicationForm(X_ZZ_Application_Form applicationForm) {
		this.applicationForm = applicationForm;
	}
}
