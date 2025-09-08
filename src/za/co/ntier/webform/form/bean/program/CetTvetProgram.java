package za.co.ntier.webform.form.bean.program;

import java.lang.reflect.InvocationTargetException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;

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
import za.co.ntier.webform.model.I_ZZSubAnnex;
import za.co.ntier.webform.model.I_ZZ_Application_Form;
import za.co.ntier.webform.model.X_ZZAnnexure;
import za.co.ntier.webform.model.X_ZZSubAnnex;
import za.co.ntier.webform.model.X_ZZ_Application_Form;
import za.co.ntier.webform.model.X_ZZ_FormDiscipline;
import za.co.ntier.webform.model.X_ZZ_Trade;

public class CetTvetProgram implements ISaveForm, IProgram {
	private AddressInfo addressInfo;

	private List<AnnexureInfo> annexureInfos;
	private MenuContextInfo menuContextInfo;

	private List<LearnerInputInfo> tradeInfo;

	@SuppressWarnings("unchecked")
	public CetTvetProgram(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm){
		this.setMenuContextInfo(menuContextInfo);
		this.applicationForm = applicationForm;
		
		annexureInfos = new ArrayList<>();
		AnnexureInfo annexure = null;
		AnnexureInfo subAnnexure = null;
		if (menuContextInfo.getProgramType() == ProgramType.CET) {
			annexure = CetTvetOneLineInput.getAnnexureInfoOneLine(
					"ANNEXURE A (Applicable to CET Colleges)",
					List.of(ColumnInfo.getColLabel("Name of the Intervention"),
							ColumnInfo.getColPositiveNumber(CetTvetOneLineInput.colNoBeneficiariesTitle)),
					"CET learners funded to access AET Programmes");

			annexureInfos.add(annexure);

			annexure = CetTvetOneLineInput.getAnnexureInfoOneLine(
					"ANNEXURE B (Applicable to CET Colleges)",
					List.of(ColumnInfo.getColLabel("Name of the Intervention"),
							ColumnInfo.getColPositiveNumber(CetTvetOneLineInput.colNoBeneficiariesTitle),
							ColumnInfo.getColText(CetTvetOneLineInput.colDisciplineTitle)),
					"Number of TVET Colleges and HEI graduates that entered CET Internships");

			annexureInfos.add(annexure);

			annexure = CetTvetOneLineInput.getAnnexureInfoOneLine(
					"ANNEXURE C (Applicable to CET Colleges)",
					List.of(ColumnInfo.getColLabel("Name of the Intervention"),
							ColumnInfo.getColPositiveNumber(CetTvetOneLineInput.colNoBeneficiariesTitle)),
					"CET Managers receiving training on curriculum related studies");

			subAnnexure = CetTvetMultiLineInput.getCetTvetMultiLineInput(null,
					List.of(ColumnInfo.getColText(CetTvetMultiLineInput.colRequestedProgrammeTitle),
							ColumnInfo.getColPositiveNumber(CetTvetMultiLineInput.colNoManagersTitle))
					);
			annexure.setSubAnnexure(subAnnexure);

			annexureInfos.add(annexure);

			annexure = CetTvetOneLineInput.getAnnexureInfoOneLine(
					"ANNEXURE D (Applicable to CET Colleges)",
					List.of(ColumnInfo.getColLabel("Name of the Intervention"),
							ColumnInfo.getColPositiveNumber(CetTvetOneLineInput.colNoBeneficiariesTitle)),
					"Number of CET Colleges lecturers awarded skills development programmes");

			subAnnexure = CetTvetMultiLineInput.getCetTvetMultiLineInput(null,
					List.of(ColumnInfo.getColText(CetTvetMultiLineInput.colRequestedProgrammeTitle)));
			annexure.setSubAnnexure(subAnnexure);

			annexureInfos.add(annexure);
		} else if (menuContextInfo.getProgramType() == ProgramType.TVET) {
			annexure = CetTvetOneLineInput.getAnnexureInfoOneLine(
					"ANNEXURE E (Applicable to TVET Colleges)",
					List.of(ColumnInfo.getColLabel("Name of the Intervention"),
							ColumnInfo.getColPositiveNumber(CetTvetOneLineInput.colNoBeneficiariesTitle),
							ColumnInfo.getColText(CetTvetOneLineInput.colDisciplineTitle)),
					"Number of TVET College graduates that entered an internship programme (the MQA prioritises engineering and related disciplines and may support other disciplines at its sole discretion)");

			subAnnexure = CetTvetMultiLineInput.getCetTvetMultiLineInput(null,
					List.of(ColumnInfo.getColText(CetTvetMultiLineInput.colFieldStudyTitle),
							ColumnInfo.getColPositiveNumber(CetTvetMultiLineInput.colNoLearners)));
			annexure.setSubAnnexure(subAnnexure);

			annexureInfos.add(annexure);

			annexure = CetTvetOneLineInput.getAnnexureInfoOneLine(
					"ANNEXURE F  (Applicable to TVET Colleges)",
					List.of(ColumnInfo.getColLabel("Name of the Intervention"),
							ColumnInfo.getColPositiveNumber(CetTvetOneLineInput.colNoBeneficiariesTitle),
							ColumnInfo.getColPositiveNumber(CetTvetOneLineInput.colProgrammeApplyTitle)),
					"TVET Managers receiving training on curriculum related studies");

			subAnnexure = CetTvetMultiLineInput.getCetTvetMultiLineInput(null,
					List.of(ColumnInfo.getColText(CetTvetMultiLineInput.colRequestedProgrammeTitle),
							ColumnInfo.getColPositiveNumber(CetTvetMultiLineInput.colNoManagersTitle)));
			annexure.setSubAnnexure(subAnnexure);

			annexureInfos.add(annexure);

			annexure = CetTvetOneLineInput.getAnnexureInfoOneLine(
					"ANNEXURE G (Applicable to TVET Colleges)",
					List.of(ColumnInfo.getColLabel("Name of the Intervention"),
							ColumnInfo.getColPositiveNumber(CetTvetOneLineInput.colTotalNoBeneficiariesTitle)),
					"Number of TVET students requiring Work Integrated Learning to complete their work integrated learning placements (WIL)");

			subAnnexure = CetTvetMultiLineInput.getCetTvetMultiLineInput(
					"Please supply the list of possible fields of study for this WIL",
					List.of(ColumnInfo.getColText(CetTvetMultiLineInput.colFieldStudyTitle),
							ColumnInfo.getColPositiveNumber(CetTvetMultiLineInput.colNoLearners)));
			annexure.setSubAnnexure(subAnnexure);

			annexureInfos.add(annexure);

			annexure = CetTvetOneLineInput.getAnnexureInfoOneLine(
					"ANNEXURE H (Applicable to TVET Colleges)",
					List.of(ColumnInfo.getColLabel("Name of the Intervention"),
							ColumnInfo.getColPositiveNumber(CetTvetOneLineInput.colTotalNoBeneficiariesTitle)),
					"TVET Lecturers to be awarded bursaries to further their studies");

			annexureInfos.add(annexure);

			List<String> twoTitleFirstRow = List.of("Occupational Health and Safety", "Other, specify");
			annexure = CetTvetOneLineInput.getCetTvetOneLineInput(
					"ANNEXURE I (Applicable to TVET Colleges)",
					List.of(ColumnInfo.getColLabel("Name of the Intervention"),
							ColumnInfo.getColTwoValue("Total Number of beneficiaries applying for"),
							ColumnInfo.getColTwoTitle("Programme Applied for")),
					"Lecturers exposed to industry through skills programmes", twoTitleFirstRow);

			annexureInfos.add(annexure);

		} else if (menuContextInfo.getProgramType() == ProgramType.TVET_BURSARS) {
			List<Object> rObjs = MasterUtil.queryLearnerInputInfos(
					menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID(),
					X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_Trade);

			tradeInfo = (List<LearnerInputInfo>) rObjs.get(0);
			if (tradeInfo != null) {

				List<ColumnInfo<?>> cols = List.of(ColumnInfo.getColList("Field of Study", tradeInfo),
						ColumnInfo.getColPositiveNumber(CetTvetMultiLineInput.colNoLearners));
				
				List<AnnexureRow<X_ZZSubAnnex>> initRows = null;
				if (applicationForm != null)		
					initRows = loadInitRow(cols, true);
				
				Supplier<Map<ColumnInfo<?>, Object>> supplierRowSubAnnex = () -> new AnnexureRow<X_ZZSubAnnex>();
				
				annexure = CetTvetMultiLineInput.getCetTvetMultiLineInput("TVET UNEMPLOYED BURSARS SUPPORT FUNDING APPLICATION",cols, initRows, supplierRowSubAnnex);
				
				annexure.getTotalRow().put(annexure.getColumnInfos().get(0), "Total Number of beneficiaries applying for");

				annexureInfos.add(annexure);
			}
		}

		addressInfo = new AddressInfo(menuContextInfo.getProgramType(), false);
	}
	
	public List<AnnexureRow<X_ZZSubAnnex>> loadInitRow(List<ColumnInfo<?>> cols, boolean isDirectSubAnnex) {
		Query directSubAnnexQuery = MTable.get(X_ZZSubAnnex.Table_ID).createQuery(String.format("%s = ?", I_ZZ_Application_Form.COLUMNNAME_ZZ_Application_Form_ID), null);
		directSubAnnexQuery.setParameters(applicationForm.getZZ_Application_Form_ID());
		List<X_ZZSubAnnex> subAnnexs = directSubAnnexQuery.list();
		
		ColumnInfo<?> colTrade = AnnexureInfo.lookupColByDataType(DataType.List, cols);
		ColumnInfo<?> colRequestedProgramme = AnnexureInfo.lookupColByTitle(CetTvetMultiLineInput.colRequestedProgrammeTitle, cols);
		ColumnInfo<?> colFieldStudy = AnnexureInfo.lookupColByTitle(CetTvetMultiLineInput.colFieldStudyTitle, cols);
		ColumnInfo<?> colNoLearners = AnnexureInfo.lookupColByTitle(CetTvetMultiLineInput.colNoLearners, cols);
		
		List<AnnexureRow<X_ZZSubAnnex>> rowDataInits = new ArrayList<>();
		
		for (X_ZZSubAnnex subAnnex : subAnnexs) {
			AnnexureRow<X_ZZSubAnnex> rowInit = new AnnexureRow<>();
			rowInit.setData(subAnnex);
			if(colTrade != null) {
				if(subAnnex.getZZ_Trade_ID() == 0)
					rowInit.put(colTrade, null);
				else {
					for (Object tradeObj : colTrade.getDataProvider()) {
						LearnerInputInfo trade = (LearnerInputInfo)tradeObj;
						if (trade.getLearnerInputID() == subAnnex.getZZ_Trade_ID()){
							rowInit.put(colTrade, trade);
							break;
						}
						
					}
					
				}
			}
			
			if (colFieldStudy != null && colFieldStudy.getDataType() != DataType.List) {
				rowInit.put(colFieldStudy, subAnnex.getZZFieldStudy());
			}
			
			if (colRequestedProgramme != null) {
				rowInit.put(colFieldStudy, subAnnex.getZZRequestedProgramme());
			}
			
			if (colNoLearners != null) {
				if (subAnnex.get_Value(I_ZZSubAnnex.COLUMNNAME_ZZLearners) == null)
					rowInit.put(colNoLearners, new IntData(rowInit, null));
				else
					rowInit.put(colNoLearners, new IntData(rowInit, subAnnex.getZZLearners()));

			}
			
			rowDataInits.add(rowInit);
		}
		
		return rowDataInits;
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
			
			if (colFieldStudy.getDataType() != DataType.List) {
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
		
		X_ZZAnnexure annexure = new X_ZZAnnexure(Env.getCtx(), 0, trxName);
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
		if (cellData != null && cellData != 0) {
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
