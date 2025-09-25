package za.co.ntier.webform.form.bean.program;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import za.co.ntier.webform.form.AbstractProgram;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.ProgramType;
import za.co.ntier.webform.form.bean.component.AddressInfo;
import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.CetTvetMultiLineInput;
import za.co.ntier.webform.form.bean.component.CetTvetOneLineInput;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.LearnerInputInfo;
import za.co.ntier.webform.form.bean.component.ProgramInput;
import za.co.ntier.api.model.X_ZZAnnexure;
import za.co.ntier.api.model.X_ZZSubAnnex;
import za.co.ntier.api.model.X_ZZ_Application_Form;

public class CetTvetProgram extends AbstractProgram {
	private AddressInfo addressInfo;

	private List<AnnexureInfo> annexureInfos;
	
	private List<LearnerInputInfo> tradeInfo;
	
	public CetTvetProgram(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm){
		super(menuContextInfo, applicationForm);
		
		
		annexureInfos = new ArrayList<>();
		CetTvetOneLineInput annexure = null;
		CetTvetMultiLineInput subAnnexure = null;
		List<ColumnInfo<?>> cols = null;
		
		String identify = null;
		String sectionHeader = "Intervention";
		String tableTitle = null;

		ColumnInfo<?> colNoBeneficiaries = ColumnInfo.getColPositiveNumber(ColumnInfo.colNoBeneficiariesTitle, X_ZZAnnexure.COLUMNNAME_ZZBeneficiaries);
		colNoBeneficiaries.setCalTotal(true);
		ColumnInfo<?> colDiscipline = ColumnInfo.getColText(ColumnInfo.colDisciplineTitle, X_ZZAnnexure.COLUMNNAME_ZZDiscipline);
		ColumnInfo<?> colTotalNoBeneficiaries = ColumnInfo.getColPositiveNumber(ColumnInfo.colTotalNoBeneficiariesTitle, X_ZZAnnexure.COLUMNNAME_ZZTotalBeneficiaries);
		colTotalNoBeneficiaries.setCalTotal(true);
		// move from int to free text but data base not yet change because this column is not used anymore
		//ColumnInfo<?> colProgrammeApply = ColumnInfo.getColText(ColumnInfo.colProgrammeApplyTitle, X_ZZAnnexure.COLUMNNAME_ZZProgramme);
		ColumnInfo<?> colRequestedProgramme = ColumnInfo.getColText(ColumnInfo.colRequestedProgrammeTitle, X_ZZSubAnnex.COLUMNNAME_ZZRequestedProgramme);
		ColumnInfo<?> colNoManagers = ColumnInfo.getColPositiveNumber(ColumnInfo.colNoManagersTitle, X_ZZSubAnnex.COLUMNNAME_ZZManagers);
		colNoManagers.setCalTotal(true);
		ColumnInfo<?> colFieldStudy = ColumnInfo.getColText(ColumnInfo.colFieldStudyTitle, X_ZZSubAnnex.COLUMNNAME_ZZFieldStudy);
		ColumnInfo<?> colNoLearners = ColumnInfo.getColPositiveNumber(ColumnInfo.colNoLearners, X_ZZSubAnnex.COLUMNNAME_ZZLearners);
		colNoLearners.setCalTotal(true);
		
		
		
		if (menuContextInfo.getProgramType() == ProgramType.CET) {
			
			cols = List.of(colNoBeneficiaries);
			identify = "ANNEXURE A";
			tableTitle = "CET learners funded to access AET Programmes";
			annexure = CetTvetOneLineInput.getCetTvetOneLineInput(applicationForm, cols, sectionHeader, tableTitle, identify, false);
			annexureInfos.add(annexure);

			cols = List.of(colNoBeneficiaries, colDiscipline);
			identify = "ANNEXURE B";
			tableTitle = "Number of TVET Colleges and HEI graduates that entered CET Internships";
			annexure = CetTvetOneLineInput.getCetTvetOneLineInput(applicationForm, cols, sectionHeader, tableTitle, identify, true);
			annexureInfos.add(annexure);

			// cols = List.of(colNoBeneficiaries);
			identify = "ANNEXURE C";
			tableTitle = "CET Managers receiving training on curriculum related studies";
			//annexure = CetTvetOneLineInput.getCetTvetOneLineInput(applicationForm, cols, sectionHeader, tableTitle, identify);
			//annexureInfos.add(annexure);
			
			cols = List.of(colRequestedProgramme, colNoManagers);
			subAnnexure = CetTvetMultiLineInput.getCetTvetMultiLineInput(applicationForm, cols, identify, sectionHeader, tableTitle);
			annexureInfos.add(subAnnexure);
						
			//cols = List.of(colNoBeneficiaries);
			identify = "ANNEXURE D";
			tableTitle = "Number of CET Colleges lecturers awarded skills development programmes";
			//annexure = CetTvetOneLineInput.getCetTvetOneLineInput(applicationForm, cols, sectionHeader, tableTitle, identify);
			//annexureInfos.add(annexure);
			
			cols = List.of(colRequestedProgramme);
			subAnnexure = CetTvetMultiLineInput.getCetTvetMultiLineInput(applicationForm, cols, identify, sectionHeader, tableTitle);
			annexureInfos.add(subAnnexure);
			
		} else if (menuContextInfo.getProgramType() == ProgramType.TVET) {
			
			identify = "ANNEXURE E";
			String tabTile = "ANNEXURE A";
			//cols = List.of(colNoBeneficiaries, colDiscipline);
			tableTitle = "Number of TVET College graduates that entered an internship programme (the MQA prioritises engineering and related disciplines and may support other disciplines at its sole discretion)";
			//annexure = CetTvetOneLineInput.getCetTvetOneLineInput(applicationForm, cols, sectionHeader, tableTitle, identify);
			//annexureInfos.add(annexure);
			
			cols = List.of(colFieldStudy, colNoLearners);
			subAnnexure = CetTvetMultiLineInput.getCetTvetMultiLineInput(applicationForm, cols, identify, sectionHeader, tableTitle);
			subAnnexure.setTabTitle(tabTile);
			annexureInfos.add(subAnnexure);
			
			identify = "ANNEXURE F";
			tabTile = "ANNEXURE B";
			//cols = List.of(colNoBeneficiaries, colProgrammeApply);
			tableTitle = "TVET Managers receiving training on curriculum related studies";
			//annexure = CetTvetOneLineInput.getCetTvetOneLineInput(applicationForm, cols, sectionHeader, tableTitle, identify);
			//annexureInfos.add(annexure);
			
			cols = List.of(colRequestedProgramme, colNoManagers);
			subAnnexure = CetTvetMultiLineInput.getCetTvetMultiLineInput(applicationForm, cols, identify, sectionHeader, tableTitle);
			subAnnexure.setTabTitle(tabTile);
			annexureInfos.add(subAnnexure);
			
			identify = "ANNEXURE G";
			tabTile = "ANNEXURE C";
			cols = List.of(colTotalNoBeneficiaries);
			tableTitle = "Number of TVET students requiring Work Integrated Learning to complete their work integrated learning placements (WIL)";
			annexure = CetTvetOneLineInput.getCetTvetOneLineInput(applicationForm, cols, sectionHeader, tableTitle, identify, false);
			subAnnexure.setTabTitle(tabTile);
			annexureInfos.add(annexure);
			
			cols = List.of(colFieldStudy, colNoLearners);
			subAnnexure = CetTvetMultiLineInput.getCetTvetMultiLineInput(applicationForm, cols, identify, null, "Please supply the list of possible fields of study for this WIL");
			annexure.setSubAnnexure(subAnnexure);
			
			identify = "ANNEXURE H";
			tabTile = "ANNEXURE D";
			cols = List.of(colTotalNoBeneficiaries);
			tableTitle = "TVET Lecturers to be awarded bursaries to further their studies";
			annexure = CetTvetOneLineInput.getCetTvetOneLineInput(applicationForm, cols, sectionHeader, tableTitle, identify, false);
			annexure.setTabTitle(tabTile);
			annexureInfos.add(annexure);

			identify =  "ANNEXURE I";
			tabTile = "ANNEXURE E";
			tableTitle = "Lecturers exposed to industry through skills programmes";
			ColumnInfo<?> colProgrammeApply = ColumnInfo.getColLabel(ColumnInfo.colProgrammeApplyTitle);
			cols = List.of(colTotalNoBeneficiaries, colProgrammeApply);
			
			List<Map<ColumnInfo<?>, Object>> rowTitles = List.of(Map.of(colProgrammeApply, "Occupational Health and Safety"),
					Map.of(colProgrammeApply, "Other, specify"));
			annexure = CetTvetOneLineInput.getCetTvetOneLineInput(applicationForm, cols, sectionHeader, tableTitle, identify, false, rowTitles, true);
			annexure.setTabTitle(tabTile);
			annexureInfos.add(annexure);

		} else if (menuContextInfo.getProgramType() == ProgramType.TVET_BURSARS) {			
			List<LearnerInputInfo> tradeObj = ProgramInput.queryLearnerInputInfos(menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID(), true);
			tradeInfo = tradeObj;
			if (tradeInfo != null) {

				ColumnInfo<?> colRowTitle = ColumnInfo.getColList("Field of Study", tradeInfo, X_ZZSubAnnex.COLUMNNAME_ZZ_Trade_ID, "learnerInputID");
				cols = List.of(colRowTitle, colNoLearners);
				identify = "TVET UNEMPLOYED BURSARS SUPPORT FUNDING APPLICATION";
				subAnnexure = CetTvetMultiLineInput.getCetTvetMultiLineInput(applicationForm, cols, identify, null, identify);
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
	
	
	public List<LearnerInputInfo> getTradeInfo() {
		return tradeInfo;
	}

	public void initComponent(X_ZZ_Application_Form applicationForm) {
		setApplicationForm(applicationForm);
		if (addressInfo != null) {
			addressInfo.initComponent(applicationForm);
		}
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		super.saveForm(applicationForm);
		for (AnnexureInfo annexure : annexureInfos) {
			annexure.save(trxName, applicationForm);
			
			if (annexure.getSubAnnexure() != null) {
				annexure.getSubAnnexure().save(trxName, applicationForm);
			}
		}
		//applicationForm.setZZTotalNumberApplied(total);
		if (addressInfo != null)
			addressInfo.saveForm(trxName, applicationForm);
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
	

	public void setTradeInfo(List<LearnerInputInfo> tradeInfo) {
		this.tradeInfo = tradeInfo;
	}
	
	
	@Override
	public boolean isProgramValid() {
	    return true; // no partial rows found -> allow Next
	}

	


	

}
