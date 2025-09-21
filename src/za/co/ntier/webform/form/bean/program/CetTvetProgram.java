package za.co.ntier.webform.form.bean.program;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

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
import za.co.ntier.webform.form.bean.component.LabelData;
import za.co.ntier.webform.form.bean.component.LearnerInputInfo;
import za.co.ntier.webform.form.bean.component.ProgramInput;
import za.co.ntier.webform.model.I_ZZAnnexure;
import za.co.ntier.webform.model.I_ZZSubAnnex;
import za.co.ntier.webform.model.X_ZZAnnexure;
import za.co.ntier.webform.model.X_ZZSubAnnex;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class CetTvetProgram implements ISaveForm, IProgram {
	private AddressInfo addressInfo;

	private List<AnnexureInfo> annexureInfos;
	private X_ZZ_Application_Form applicationForm;

	private MenuContextInfo menuContextInfo;

	
	
	private List<LearnerInputInfo> tradeInfo;
	
	public CetTvetProgram(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm){
		this.setMenuContextInfo(menuContextInfo);
		this.applicationForm = applicationForm;
		
		annexureInfos = new ArrayList<>();
		CetTvetOneLineInput annexure = null;
		CetTvetMultiLineInput subAnnexure = null;
		List<ColumnInfo<?>> cols = null;
		
		String title = null;
		String rowTitle = null;
		
		ColumnInfo<?> colRowTitleNameOfIntervention = ColumnInfo.getColLabel("Name of the Intervention");
		ColumnInfo<?> colNoBeneficiaries = ColumnInfo.getColPositiveNumber(ColumnInfo.colNoBeneficiariesTitle);
		ColumnInfo<?> colDiscipline = ColumnInfo.getColText(ColumnInfo.colDisciplineTitle);
		ColumnInfo<?> colRequestedProgramme = ColumnInfo.getColText(ColumnInfo.colRequestedProgrammeTitle);
		ColumnInfo<?> colNoManagers = ColumnInfo.getColPositiveNumber(ColumnInfo.colNoManagersTitle);
		ColumnInfo<?> colFieldStudy = ColumnInfo.getColText(ColumnInfo.colFieldStudyTitle);
		ColumnInfo<?> colNoLearners = ColumnInfo.getColPositiveNumber(ColumnInfo.colNoLearners);
		ColumnInfo<?> colProgrammeApply = ColumnInfo.getColPositiveNumber(ColumnInfo.colProgrammeApplyTitle);
		ColumnInfo<?> colTotalNoBeneficiaries = ColumnInfo.getColPositiveNumber(ColumnInfo.colTotalNoBeneficiariesTitle);
		
		if (menuContextInfo.getProgramType() == ProgramType.CET) {
			
			cols = List.of(colRowTitleNameOfIntervention, colNoBeneficiaries);
			title = "ANNEXURE A";
			rowTitle = "CET learners funded to access AET Programmes";
			annexure = CetTvetOneLineInput.getCetTvetOneLineInput(applicationForm, cols, title, rowTitle, colRowTitleNameOfIntervention);
			annexureInfos.add(annexure);

			cols = List.of(colRowTitleNameOfIntervention, colNoBeneficiaries, colDiscipline);
			title = "ANNEXURE B";
			rowTitle = "Number of TVET Colleges and HEI graduates that entered CET Internships";
			annexure = CetTvetOneLineInput.getCetTvetOneLineInput(applicationForm, cols, title, rowTitle, colRowTitleNameOfIntervention);
			annexureInfos.add(annexure);

			cols = List.of(colRowTitleNameOfIntervention, colNoBeneficiaries);
			title = "ANNEXURE C";
			rowTitle = "CET Managers receiving training on curriculum related studies";
			annexure = CetTvetOneLineInput.getCetTvetOneLineInput(applicationForm, cols, title, rowTitle, colRowTitleNameOfIntervention);
			annexureInfos.add(annexure);
			
			cols = List.of(colRequestedProgramme, colNoManagers);
			subAnnexure = CetTvetMultiLineInput.getCetTvetMultiLineInput(title, cols);
			subAnnexure.initCetTvetMultiLine();
			annexure.setSubAnnexure(subAnnexure);
			
			
			cols = List.of(colRowTitleNameOfIntervention, colNoBeneficiaries);
			title = "ANNEXURE D";
			rowTitle = "Number of CET Colleges lecturers awarded skills development programmes";
			annexure = CetTvetOneLineInput.getCetTvetOneLineInput(applicationForm, cols, title, rowTitle, colRowTitleNameOfIntervention);
			annexureInfos.add(annexure);
			
			cols = List.of(colRequestedProgramme);
			subAnnexure = CetTvetMultiLineInput.getCetTvetMultiLineInput(title, cols);
			subAnnexure.initCetTvetMultiLine();
			annexure.setSubAnnexure(subAnnexure);
			
		} else if (menuContextInfo.getProgramType() == ProgramType.TVET) {
			
			cols = List.of(colRowTitleNameOfIntervention, colNoBeneficiaries, colDiscipline);
			title = "ANNEXURE E (Applicable to TVET Colleges)";
			rowTitle = "Number of TVET College graduates that entered an internship programme (the MQA prioritises engineering and related disciplines and may support other disciplines at its sole discretion)";
			annexure = CetTvetOneLineInput.getCetTvetOneLineInput(applicationForm, cols, title, rowTitle, colRowTitleNameOfIntervention);
			annexureInfos.add(annexure);
			
			cols = List.of(colFieldStudy, colNoLearners);
			subAnnexure = CetTvetMultiLineInput.getCetTvetMultiLineInput(title, cols);
			annexure.setSubAnnexure(subAnnexure);
			
			cols = List.of(colRowTitleNameOfIntervention, colNoBeneficiaries, colProgrammeApply);
			title = "ANNEXURE F  (Applicable to TVET Colleges)";
			rowTitle = "TVET Managers receiving training on curriculum related studies";
			annexure = CetTvetOneLineInput.getCetTvetOneLineInput(applicationForm, cols, title, rowTitle, colRowTitleNameOfIntervention);
			annexureInfos.add(annexure);
			
			cols = List.of(colRequestedProgramme, colNoManagers);
			subAnnexure = CetTvetMultiLineInput.getCetTvetMultiLineInput(title, cols);
			annexure.setSubAnnexure(subAnnexure);
			
			cols = List.of(colRowTitleNameOfIntervention, colTotalNoBeneficiaries);
			title = "ANNEXURE G (Applicable to TVET Colleges)";
			rowTitle = "Number of TVET students requiring Work Integrated Learning to complete their work integrated learning placements (WIL)";
			annexure = CetTvetOneLineInput.getCetTvetOneLineInput(applicationForm, cols, title, rowTitle, colRowTitleNameOfIntervention);
			annexureInfos.add(annexure);
			
			cols = List.of(colFieldStudy, colNoLearners);
			subAnnexure = CetTvetMultiLineInput.getCetTvetMultiLineInput(title, cols, "Please supply the list of possible fields of study for this WIL");
			annexure.setSubAnnexure(subAnnexure);

			cols = List.of(colRowTitleNameOfIntervention, colTotalNoBeneficiaries);
			title = "ANNEXURE H (Applicable to TVET Colleges)";
			rowTitle = "TVET Lecturers to be awarded bursaries to further their studies";
			annexure = CetTvetOneLineInput.getCetTvetOneLineInput(applicationForm, cols, title, rowTitle, colRowTitleNameOfIntervention);
			annexure.initCetTvetOneLine(applicationForm, title, rowTitle, colRowTitleNameOfIntervention);
			annexureInfos.add(annexure);

			ColumnInfo<?> colTwoValue = ColumnInfo.getColTwoValue("Total Number of beneficiaries applying for");
			ColumnInfo<?> colTwoTitle = ColumnInfo.getColTwoTitle("Programme Applied for");
			cols = List.of(colRowTitleNameOfIntervention, colTwoValue, colTwoTitle);
			title =  "ANNEXURE I (Applicable to TVET Colleges)";
			List<String> twoTitleFirstRow = List.of("Occupational Health and Safety", "Other, specify");
			rowTitle = "Lecturers exposed to industry through skills programmes";
			Map<ColumnInfo<?>, Object> rowTitles = Map.of(colRowTitleNameOfIntervention, rowTitle, colTwoTitle, twoTitleFirstRow);
			annexure = CetTvetOneLineInput.getCetTvetOneLineInput(applicationForm, cols, title, rowTitles);

			annexureInfos.add(annexure);

		} else if (menuContextInfo.getProgramType() == ProgramType.TVET_BURSARS) {			
			List<LearnerInputInfo> tradeObj = ProgramInput.queryLearnerInputInfos(menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID(), true);
			tradeInfo = tradeObj;
			if (tradeInfo != null) {

				ColumnInfo<?> colRowTitle = ColumnInfo.getColList("Field of Study", tradeInfo);
				cols = List.of(colRowTitle, colNoLearners);
				subAnnexure = CetTvetMultiLineInput.getCetTvetMultiLineInput("TVET UNEMPLOYED BURSARS SUPPORT FUNDING APPLICATION", cols);
				subAnnexure.getTotalRow().put(colRowTitle, "Total Number of beneficiaries applying for");
				annexureInfos.add(subAnnexure);
			}
		}

		addressInfo = new AddressInfo(menuContextInfo.getProgramType(), false);
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
	
	/**
	 * @return the applicationForm
	 */
	public X_ZZ_Application_Form getApplicationForm() {
		return applicationForm;
	}
	
	public MenuContextInfo getMenuContextInfo() {
		return menuContextInfo;
	}

	public List<LearnerInputInfo> getTradeInfo() {
		return tradeInfo;
	}

	public void initComponent(X_ZZ_Application_Form applicationForm) {
		this.applicationForm = applicationForm;
		if (addressInfo != null) {
			addressInfo.initComponent(applicationForm);
		}
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
	
	
	
	public int saveMultilineInput(String trxName, X_ZZ_Application_Form applicationForm, X_ZZAnnexure zzAnnexure, CetTvetMultiLineInput cetTvetOneLineInput) {
		ColumnInfo<?> colTrade = AnnexureInfo.lookupColByDataType(DataType.List, cetTvetOneLineInput);
		ColumnInfo<?> colRequestedProgramme = AnnexureInfo.lookupColByTitle(ColumnInfo.colRequestedProgrammeTitle, cetTvetOneLineInput);
		ColumnInfo<?> colFieldStudy = AnnexureInfo.lookupColByTitle(ColumnInfo.colFieldStudyTitle, cetTvetOneLineInput);
		ColumnInfo<?> colNoLearners = AnnexureInfo.lookupColByTitle(ColumnInfo.colNoLearners, cetTvetOneLineInput);
		ColumnInfo<?> colNoManager = AnnexureInfo.lookupColByTitle(ColumnInfo.colNoManagersTitle, cetTvetOneLineInput);
		ColumnInfo<?> colNoBeneficiaries = AnnexureInfo.lookupColByTitle(ColumnInfo.colNoBeneficiariesTitle, cetTvetOneLineInput);
		ColumnInfo<?> colProgrammeApply = AnnexureInfo.lookupColByTitle(ColumnInfo.colProgrammeApplyTitle, cetTvetOneLineInput);
		ColumnInfo<?> colTotalNoBeneficiaries = AnnexureInfo.lookupColByTitle(ColumnInfo.colTotalNoBeneficiariesTitle, cetTvetOneLineInput);
		
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
		ColumnInfo<?> colBeneficiaries = AnnexureInfo.lookupColByTitle(ColumnInfo.colNoBeneficiariesTitle, cetTvetOneLineInput);
		ColumnInfo<?> colTotalBeneficiaries = AnnexureInfo.lookupColByTitle(ColumnInfo.colTotalNoBeneficiariesTitle, cetTvetOneLineInput);
		ColumnInfo<?> colDiscipline = AnnexureInfo.lookupColByTitle(ColumnInfo.colDisciplineTitle, cetTvetOneLineInput);
		ColumnInfo<?> colProgramme = AnnexureInfo.lookupColByTitle(ColumnInfo.colProgrammeApplyTitle, cetTvetOneLineInput);
		
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
	
	/**
	 * @param applicationForm the applicationForm to set
	 */
	public void setApplicationForm(X_ZZ_Application_Form applicationForm) {
		this.applicationForm = applicationForm;
	}

	public void setMenuContextInfo(MenuContextInfo menuContextInfo) {
		this.menuContextInfo = menuContextInfo;
	}

	public void setTradeInfo(List<LearnerInputInfo> tradeInfo) {
		this.tradeInfo = tradeInfo;
	}
	
	
	@Override
	public boolean isProgramValid() {
	    return true; // no partial rows found -> allow Next
	}

	


	

}
