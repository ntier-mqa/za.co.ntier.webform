package za.co.ntier.webform.form.bean.program;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import za.co.ntier.api.model.I_ZZAnnexure;
import za.co.ntier.api.model.I_ZZSubAnnex;
import za.co.ntier.api.model.X_ZZ_Application_Form;
import za.co.ntier.webform.form.AbstractProgram;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.ProgramType;
import za.co.ntier.webform.form.bean.component.AddressInfo;
import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.AnnexureRow;
import za.co.ntier.webform.form.bean.component.CetTvetMultiLineInput;
import za.co.ntier.webform.form.bean.component.CetTvetOneLineInput;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.IntData;
import za.co.ntier.webform.form.bean.component.LearnerInputInfo;
import za.co.ntier.webform.form.bean.component.ProgramInput;

public class CetTvetProgram extends AbstractProgram {
	private AddressInfo addressInfo;
	
	private List<AnnexureInfo> annexureInfos;
	
	private List<LearnerInputInfo> tradeInfo;
	
	private CetTvetMultiLineInput tradeAnnexure;       // bursars/university table
	private ColumnInfo<?> colFieldOfStudyCol;          // "Field of Study" column
	private ColumnInfo<?> colNoLearnersCol;            // "Number of learners" column
    private final ProgramType programType;
	
	public CetTvetProgram(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm){
		super(menuContextInfo, applicationForm);
		this.programType = menuContextInfo.getProgramType();
		
		setHandleButton(menuContextInfo.getProgramType() != ProgramType.TVET_BURSARS && menuContextInfo.getProgramType() != ProgramType.UNIVERSITY);
		annexureInfos = new ArrayList<>();
		CetTvetOneLineInput annexure = null;
		CetTvetMultiLineInput subAnnexure = null;
		List<ColumnInfo<?>> cols = null;
		
		String identify = null;
		String sectionHeader = "Intervention";
		String tableTitle = null;

		ColumnInfo<?> colNoBeneficiaries = ColumnInfo.getColPositiveNumber(ColumnInfo.colNoBeneficiariesTitle, I_ZZAnnexure.COLUMNNAME_ZZBeneficiaries);
		colNoBeneficiaries.setCalTotal(true);
		ColumnInfo<?> colDiscipline = ColumnInfo.getColText(ColumnInfo.colDisciplineTitle, I_ZZAnnexure.COLUMNNAME_ZZDiscipline);
		ColumnInfo<?> colTotalNoBeneficiaries = ColumnInfo.getColPositiveNumber(ColumnInfo.colTotalNoBeneficiariesTitle, I_ZZAnnexure.COLUMNNAME_ZZTotalBeneficiaries);
		colTotalNoBeneficiaries.setCalTotal(true);
		// move from int to free text but data base not yet change because this column is not used anymore
		//ColumnModel colProgrammeApply = ColumnModel.getColText(ColumnModel.colProgrammeApplyTitle, X_ZZAnnexure.COLUMNNAME_ZZProgramme);
		ColumnInfo<?> colRequestedProgramme = ColumnInfo.getColText(ColumnInfo.colRequestedProgrammeTitle, I_ZZSubAnnex.COLUMNNAME_ZZRequestedProgramme);
		ColumnInfo<?> colNoManagers = ColumnInfo.getColPositiveNumber(ColumnInfo.colNoManagersTitle, I_ZZSubAnnex.COLUMNNAME_ZZManagers);
		colNoManagers.setCalTotal(true);
		ColumnInfo<?> colFieldStudy = ColumnInfo.getColText(ColumnInfo.colFieldStudyTitle, I_ZZSubAnnex.COLUMNNAME_ZZFieldStudy);
		ColumnInfo<?> colNoLearners = ColumnInfo.getColPositiveNumber(ColumnInfo.colNoLearners, I_ZZSubAnnex.COLUMNNAME_ZZLearners);
		colNoLearners.setCalTotal(true);
		
		if (menuContextInfo.getProgramType() == ProgramType.CET) {
			
			cols = List.of(colNoBeneficiaries);
			identify = "ANNEXURE A";
			tableTitle = "CET learners funded to access AET Programmes";
			annexure = CetTvetOneLineInput.getCetTvetOneLineInput(applicationForm, cols, sectionHeader, tableTitle, identify, false);
			annexure.setTabTitle("AET");
			annexureInfos.add(annexure);

			cols = List.of(colNoBeneficiaries, colDiscipline);
			identify = "ANNEXURE B";
			tableTitle = "Number of TVET Colleges and HEI graduates that entered CET Internships";
			annexure = CetTvetOneLineInput.getCetTvetOneLineInput(applicationForm, cols, sectionHeader, tableTitle, identify, true);
			annexure.setTabTitle("Internships");
			annexureInfos.add(annexure);

			identify = "ANNEXURE C";
			tableTitle = "CET Managers receiving training on curriculum related studies";
			cols = List.of(colRequestedProgramme, colNoManagers);
			subAnnexure = CetTvetMultiLineInput.getCetTvetMultiLineInput(applicationForm, cols, identify, sectionHeader, tableTitle);
			subAnnexure.setTabTitle("Mgrs Curriculum Training");
			annexureInfos.add(subAnnexure);
						
			identify = "ANNEXURE D";
			tableTitle = "Number of CET Colleges lecturers awarded skills development programmes";
			ColumnInfo<?> colNumber = ColumnInfo.getColPositiveNumber("Number", I_ZZSubAnnex.COLUMNNAME_ZZManagers);
			colNumber.setCalTotal(true);
			cols = List.of(colRequestedProgramme, colNumber);
			subAnnexure = CetTvetMultiLineInput.getCetTvetMultiLineInput(applicationForm, cols, identify, sectionHeader, tableTitle);
			subAnnexure.setTabTitle("Lecturers Skills Programs");
			annexureInfos.add(subAnnexure);
			
		} else if (menuContextInfo.getProgramType() == ProgramType.TVET) {
			
			identify = "ANNEXURE E";
			tableTitle = "Number of TVET College graduates that entered an internship programme (the MQA prioritises engineering and related disciplines and may support other disciplines at its sole discretion)";
			
			cols = List.of(colFieldStudy, colNoLearners);
			subAnnexure = CetTvetMultiLineInput.getCetTvetMultiLineInput(applicationForm, cols, identify, sectionHeader, tableTitle);
			subAnnexure.setTabTitle("Internships");
			annexureInfos.add(subAnnexure);
			
			identify = "ANNEXURE F";
			tableTitle = "TVET Managers receiving training on curriculum related studies";
			
			cols = List.of(colRequestedProgramme, colNoManagers);
			subAnnexure = CetTvetMultiLineInput.getCetTvetMultiLineInput(applicationForm, cols, identify, sectionHeader, tableTitle);
			subAnnexure.setTabTitle("Mgrs Curriculum Training");
			annexureInfos.add(subAnnexure);
			
			identify = "ANNEXURE G";
			cols = List.of(colTotalNoBeneficiaries);
			tableTitle = "Number of TVET students requiring Work Integrated Learning to complete their work integrated learning placements (WIL)";
			annexure = CetTvetOneLineInput.getCetTvetOneLineInput(applicationForm, cols, sectionHeader, tableTitle, identify, false);
			annexure.setTabTitle("Lecturers - WIL");
			annexureInfos.add(annexure);
			
			cols = List.of(colFieldStudy, colNoLearners);
			subAnnexure = CetTvetMultiLineInput.getCetTvetMultiLineInput(applicationForm, cols, identify, null, "Please supply the list of possible fields of study for this WIL");
			annexure.setSubAnnexure(subAnnexure);
			
			identify = "ANNEXURE H";
			cols = List.of(colTotalNoBeneficiaries);
			tableTitle = "TVET Lecturers to be awarded bursaries to further their studies";
			annexure = CetTvetOneLineInput.getCetTvetOneLineInput(applicationForm, cols, sectionHeader, tableTitle, identify, false);
			annexure.setTabTitle("Lecturers - Bursaries");
			annexureInfos.add(annexure);

			identify =  "ANNEXURE I";
			tableTitle = "Lecturers exposed to industry through skills programmes";
			ColumnInfo<?> colProgrammeApply = ColumnInfo.getColLabel(ColumnInfo.colProgrammeApplyTitle);
			cols = List.of(colTotalNoBeneficiaries, colProgrammeApply);
			
			List<Map<ColumnInfo<?>, Object>> rowTitles = List.of(Map.of(colProgrammeApply, "Occupational Health and Safety"),
					Map.of(colProgrammeApply, "Other, specify"));
			annexure = CetTvetOneLineInput.getCetTvetOneLineInput(applicationForm, cols, sectionHeader, tableTitle, identify, false, rowTitles, true);
			annexure.setTabTitle("Lecturers - Programs Exposure");
			annexureInfos.add(annexure);

		} else if (menuContextInfo.getProgramType() == ProgramType.TVET_BURSARS || menuContextInfo.getProgramType() == ProgramType.UNIVERSITY) {			
			List<LearnerInputInfo> tradeObj = null;
			if(menuContextInfo.getProgramType() == ProgramType.UNIVERSITY) {
				tradeObj = ProgramInput.queryLearnerInputInfos(menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID(), false);
				identify = "University Graduates Placement";
			}else {
				tradeObj = ProgramInput.queryLearnerInputInfos(menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID(), true);
				identify = "TVET UNEMPLOYED BURSARS SUPPORT FUNDING APPLICATION";
			}
			
			tradeInfo = tradeObj;
			if (tradeInfo != null) {

				ColumnInfo<?> colRowTitle = ColumnInfo.getColList("Field of Study", tradeInfo, I_ZZSubAnnex.COLUMNNAME_ZZ_Trade_ID, "learnerInputID");
				// keep a reference for validation
				this.colFieldOfStudyCol = colRowTitle;
				this.colNoLearnersCol = colNoLearners; // defined earlier in the constructor
				cols = List.of(colRowTitle, colNoLearners);
				
				subAnnexure = CetTvetMultiLineInput.getCetTvetMultiLineInput(applicationForm, cols, identify, null, identify);
				// keep a reference for validation
				this.tradeAnnexure = subAnnexure;

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
		//sdf.setZZTotalNumberApplied(total);
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
	    // Only enforce for these program types
	    if (programType != ProgramType.TVET_BURSARS
	            && programType != ProgramType.UNIVERSITY) {
	        return true;
	    }

	    if (tradeAnnexure == null || colFieldOfStudyCol == null || colNoLearnersCol == null) {
	        return false; // misconfigured
	    }

	    java.util.List<AnnexureRow> rows = tradeAnnexure.getRows();
	    if (rows == null || rows.isEmpty()) {
	        return false;
	    }

	    boolean hasCompleteRow = false;

	    for (AnnexureRow row : rows) {
	        if (row == null) {
	            continue;
	        }

	        if (isTradeRowBlank(row)) {
	            // completely empty row (e.g. freshly added but untouched) – allowed
	            continue;
	        }

	        if (isTradeRowComplete(row)) {
	            hasCompleteRow = true;
	            continue;
	        }

	        // If we get here, user started the line but did not finish it → invalid
	        return false;
	    }

	    // Must have at least one full row overall
	    return hasCompleteRow;
	}

	
	private boolean isFieldOfStudyFilled(AnnexureRow row) {
	    Object fieldVal = row.get(colFieldOfStudyCol);
	    if (fieldVal == null) {
	        return false;
	    }

	    // What you saw in the debugger: LearnerInputInfo@...
	    if (fieldVal instanceof LearnerInputInfo) {
	        String text = ((LearnerInputInfo) fieldVal).getLearnerInputText();
	        return text != null && !text.trim().isEmpty();
	    }

	    // Fallback – non-null means “chosen”
	    return true;
	}

	private Integer extractLearners(AnnexureRow row) {
	    Object learnersVal = row.get(colNoLearnersCol);
	    if (learnersVal == null) {
	        return null;
	    }

	    // <<< IMPORTANT: IntData wrapper >>>
	    if (learnersVal instanceof IntData) {
	        return ((IntData) learnersVal).getValue();
	    }

	    if (learnersVal instanceof Number) {
	        return ((Number) learnersVal).intValue();
	    }

	    try {
	        return Integer.valueOf(learnersVal.toString().trim());
	    } catch (NumberFormatException e) {
	        return null;
	    }
	}

	private boolean isTradeRowBlank(AnnexureRow row) {
	    boolean fieldFilled = isFieldOfStudyFilled(row);
	    Integer nLearners = extractLearners(row);

	    boolean learnersBlank = (nLearners == null || nLearners == 0);

	    // Blank means: nothing selected + no learners entered
	    return !fieldFilled && learnersBlank;
	}

	private boolean isTradeRowComplete(AnnexureRow row) {
	    boolean fieldFilled = isFieldOfStudyFilled(row);
	    Integer nLearners = extractLearners(row);

	    return fieldFilled && nLearners != null && nLearners > 0;
	}

	
	
	
}
