package za.co.ntier.webform.form.bean.program;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.Query;

import za.co.ntier.api.model.I_ZZStandardSetting;
import za.co.ntier.api.model.X_ZZStandardSetting;
import za.co.ntier.api.model.X_ZZ_Application_Form;
import za.co.ntier.webform.form.AbstractProgram;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.ColumnInfo;

public class StandardSetting extends AbstractProgram {

	private List<StandardSettingInput> standardSettings;
	private AnnexureInfo bankInfo;
	
	public StandardSetting(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm) {
		super(menuContextInfo, applicationForm);
		setHandleButton(true);
		standardSettings = new ArrayList<>();
		String dataType = "CATEGORY A1: QDFs"; 
		String tabTitle = "QDFs";
		String sectionHeader = "CATEGORY A1: QDFs previously registered by the QCTO on their databas";
		StandardSettingInput standardSettingInput = createStandardSetting(dataType, tabTitle, sectionHeader
				, "Applicants must have been trained and previously registered as a QDF by the QCTO");
		standardSettingInput.setFiles(List.of(
				"A detailed CV"
				, "Certified ID copy"
				, "A list of Occupational qualifications developed and registered on the NQF (minimum 5 qualifications will be considered"
				, "A minimum of two (2) reference letters from SETA’s")
				);
		
		standardSettings.add(standardSettingInput);
		
		dataType = "CATEGORY A2: SMEs"; 
		tabTitle = "SMEs";
		sectionHeader = dataType;
		standardSettingInput = createStandardSetting(dataType, tabTitle, sectionHeader
				, "Applicants must have a Technical qualification in the Mining and Mineral Sector"
				, "Applicants must have participated as a CEP member on any qualification developed and/or registered by the MQA");
		standardSettingInput.setFiles(List.of(
				"A detailed CV"
				, "Certified ID copy"
				, "Certified copies of technical qualification/s at NQF Level 3 minimum or equivalent, in the Mining and Mineral sector"
				, "A list of Occupational qualification/s where you participated as a CEP member (minimum 2 learning programmes will be considered)"
				));
		
		standardSettings.add(standardSettingInput);
		
		dataType = "SMEs for Audits"; 
		tabTitle = dataType;
		sectionHeader = dataType;
		standardSettingInput = createStandardSetting(dataType, tabTitle, sectionHeader
				, "Applicants must be South African Citizen"
				, "Applicants must be registered moderators with the MQA and have a minimum of 3 years moderation and technical experience in the relevant discipline"
				, "Applicants must have a minimum NQF Level 3 technical qualification or equivalent in the relevant discipline"
				);
		standardSettingInput.setFiles(List.of(
				"Detailed CV"
				, "Certified ID copy"
				, "MQA moderation registration letter"
				, "Certified copy of technical qualifications"
				, "Detailed Record of Service OR an Employer confirmation letter detailing relevant work experience, relevant to the discipline applying for"
				));
		
		standardSettings.add(standardSettingInput);
		
		dataType = "Examiners"; 
		tabTitle = dataType;
		sectionHeader = dataType;
		standardSettingInput = createStandardSetting(dataType, tabTitle, sectionHeader
				, "Applicants must have a minimum NQF Level 3 technical qualification or equivalent in the relevant discipline"
				, "Applicants must have minimum 3 years’ experience setting the examination and/ or assessments within the sector"
				, "Applicants must have minimum 3 years’ relevant technical experience within the sector");
		standardSettingInput.setFiles(List.of(
				"Detailed CV"
				, "Certified ID copy"
				, "Certified copies of technical qualifications"
				, "Detailed record of service or an employer confirmation letter detailing minimum 3 years relevant work and examination and/ or assessment experience"
				));
		standardSettings.add(standardSettingInput);
		
		dataType = "Moderators"; 
		tabTitle = dataType;
		sectionHeader = dataType;
		standardSettingInput = createStandardSetting(dataType, tabTitle, sectionHeader
				, "Applicants must be South African Citizen"
				, "Applicants must have a minimum NQF Level 3 technical qualification or equivalent in the relevant discipline"
				, "Applicants must have a minimum 3 years’ experience as a moderator within the sector"
				, "Applicants must have minimum 3 years’ technical");
		standardSettingInput.setFiles(List.of(
				"Detailed CV"
				, "Certified ID copy"
				, "Certified copies of technical qualifications"
				, "Detailed record of service or an employer confirmation letter detailing the minimum 3 years work and moderation experience"
				, "MQA moderator registration letter (any applicants that have certificates of competency issued by MINCOSA formerly known as Chamber of Mines are exempt from this criteria)"
				));
				
		standardSettings.add(standardSettingInput);
		
		dataType = "Markers"; 
		tabTitle = dataType;
		sectionHeader = dataType;
		standardSettingInput = createStandardSetting(dataType, tabTitle, sectionHeader
				, "Applicants must be South African Citizen"
				, "Applicants must have a minimum NQF Level 3 technical qualification or equivalent in the relevant discipline"
				, "Applicants must have a minimum 3 years’ experience as a moderator within the sector"
				);
		standardSettingInput.setFiles(List.of(
				"Detailed CV"
				, "Certified ID Copy"
				, "Certified copies of technical qualifications"
				, "Detailed record of service or an employer confirmation letter detailing the minimum 3 years relevant technical work and marking or assessing experience"
				, "MQA assessor registration letter (any applicants that have certificates of competency issued by MINCOSA formerly known as Chamber of Mines are exempt from this criteria)"
				));
		
		standardSettings.add(standardSettingInput);
		
		dataType = "Chief Invigilators"; 
		tabTitle = dataType;
		sectionHeader = dataType;
		standardSettingInput = createStandardSetting(dataType, tabTitle, sectionHeader
				, "Applicants must be South African Citizen"
				, "Applicants must have a minimum NQF Level 3 technical qualification or equivalent in the relevant discipline"
				, "Applicants must have a minimum 3 years’ experience as a moderator within the sector"
				);
		standardSettingInput.setFiles(List.of(
				"Detailed CV"
				, "Certified ID copy"
				, "Certified copies of qualification"
				, "Detailed record of service or an employer confirmation letter detailing the minimum 3 years relevant work experience"
				));
		standardSettings.add(standardSettingInput);
		
		dataType = "Invigilators"; 
		tabTitle = dataType;
		sectionHeader = dataType;
		standardSettingInput = createStandardSetting(dataType, tabTitle, sectionHeader
				, "Applicants must be South African Citizen"
				, "Must have a minimum of a Grade 12 certificate or equivalent"
				);
		standardSettingInput.setFiles(List.of(
				"CV"
				, "Certified ID copy"
				, "Certified copy of qualification"
				));
		
		standardSettings.add(standardSettingInput);
		
		bankInfo = AnnexureInfo.getBankInfo(applicationForm);
		
	}

	public static class StandardSettingInput extends AnnexureInfo{
		private String header;
		public String getHeader() {
			return header;
		}

		public void setHeader(String header) {
			this.header = header;
		}

		private String tabTitle;
		private List<String> files;
		public String getTabTitle() {
			return tabTitle;
		}

		public void setTabTitle(String tabTitle) {
			this.tabTitle = tabTitle;
		}

		/**
		 * @return the files
		 */
		public List<String> getFiles() {
			return files;
		}

		/**
		 * @param files the files to set
		 */
		public void setFiles(List<String> files) {
			this.files = files;
		}
	}
	
	ColumnInfo<?> colTitle = ColumnInfo.getColLabel("", I_ZZStandardSetting.COLUMNNAME_Name);
	private List<ColumnInfo<?>> cols = List.of(colTitle
			, ColumnInfo.getColRadio("", I_ZZStandardSetting.COLUMNNAME_IsSelected).required());
	
	protected StandardSettingInput createStandardSetting(String dataType, String tabTitle, String sectionHeader, String ... rowTitles) {
		StandardSettingInput annexure = AnnexureInfo.getAnnexureInfo(StandardSettingInput.class, cols, false);
		
		annexure.setHeader(sectionHeader);
		annexure.setTabTitle(tabTitle);
		annexure.setDataType(dataType);
		annexure.setShowColumnHeader(false);
		
		annexure.setPoSupplier((anx, appForm) -> {
			X_ZZStandardSetting po = new X_ZZStandardSetting(appForm.getCtx(), 0, appForm.get_TrxName());
			po.setZZ_Application_Form_ID(appForm.getZZ_Application_Form_ID());
			po.setDataType(annexure.getDataType());
			return po;
		});
		
		List<Map<ColumnInfo<?>, Object>> rowTitleDetails = new ArrayList<>();
		Arrays.stream(rowTitles).forEach(rowTitle -> {
			rowTitleDetails.add(Map.of(colTitle, rowTitle));
		});
		
		List<PO> savedDaos = null;
		if(getApplicationForm() != null) {
			String where = String.format("%s = ? AND %s=?", 
					I_ZZStandardSetting.COLUMNNAME_ZZ_Application_Form_ID, I_ZZStandardSetting.COLUMNNAME_DataType);
			
			Query querySavedDaos = MTable.get(I_ZZStandardSetting.Table_ID).createQuery(where, null);
			savedDaos = querySavedDaos.setParameters(getApplicationForm().getZZ_Application_Form_ID(), dataType).list();
		}
		
		annexure.init(getApplicationForm(), savedDaos, rowTitleDetails);
		return annexure;
	}
	
	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		super.saveForm(applicationForm);
		
		standardSettings.forEach(standardSetting -> standardSetting.save(trxName, applicationForm));

		bankInfo.save(trxName, applicationForm);
	}

	/**
	 * @return the standardSettings
	 */
	public List<StandardSettingInput> getStandardSettings() {
		return standardSettings;
	}

	/**
	 * @param standardSettings the standardSettings to set
	 */
	public void setStandardSettings(List<StandardSettingInput> standardSettings) {
		this.standardSettings = standardSettings;
	}
	
	public AnnexureInfo getBankInfo() {
		return bankInfo;
	}

	public void setBankInfo(AnnexureInfo bankInfo) {
		this.bankInfo = bankInfo;
	}
	
	
	@Override
	public boolean isProgramValid() {
	    // bank form must be complete
	    //boolean bankOk = bankInfo != null && bankInfo.areMandatoryFieldsFilled();

	    

	    //return bankOk;
		return true;
	}


}
