package za.co.ntier.webform.form.bean;

import java.util.ArrayList;
import java.util.List;

import org.compiere.util.Env;

import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.model.MAnnexure;
import za.co.ntier.webform.model.X_ZZAnnexure;

public class ProgramCetTvetInfo {
	private MenuContextInfo menuContextInfo;
	private List<MAnnexure> annexures;
	private AddressInfoBase addressInfo;
	
	public static final String COL_NAME_titleHeaderText = "titleHeaderText";
	public static final String COL_NAME_customizeDetaileTemplate = "customizeDetaileTemplate";

	public ProgramCetTvetInfo(MenuContextInfo menuContextInfo) {
		this.setMenuContextInfo(menuContextInfo);
		
		setAnnexures(new ArrayList<>());
		MAnnexure annexure = null;
		if (menuContextInfo.getProgramType() == ProgramType.CET) {
			annexure = new MAnnexure(Env.getCtx(), 0, null);
			annexure.setZZHeader("ANNEXURE A (Applicable to CET Colleges)");
			annexure.setTitle("Name of the Intervention:");
			annexure.set_Attribute(COL_NAME_titleHeaderText, "CET learners funded to access AET Programmes");
			annexure.setZZFirst(X_ZZAnnexure.ZZFIRST_NumberOfBeneficiariesApplyingFor);
			
			annexures.add(annexure);
			
			annexure = new MAnnexure(Env.getCtx(), 0, null);
			annexure.setZZHeader("ANNEXURE B (Applicable to CET Colleges)");
			annexure.setTitle("Name of the Intervention:");
			annexure.set_Attribute(COL_NAME_titleHeaderText, "Number of TVET Colleges and HEI graduates that entered CET Internships");
			annexure.setZZFirst(X_ZZAnnexure.ZZFIRST_NumberOfBeneficiariesApplyingFor);
			annexure.setZZSecond(X_ZZAnnexure.ZZSECOND_DisciplineApplyingFor);
			
			annexures.add(annexure);
			
			annexure = new MAnnexure(Env.getCtx(), 0, null);
			annexure.setZZHeader("ANNEXURE C (Applicable to CET Colleges)");
			annexure.setTitle("Name of the Intervention:");
			annexure.set_Attribute(COL_NAME_titleHeaderText, "CET Managers receiving training on curriculum related studies");
			annexure.setZZFirst(X_ZZAnnexure.ZZFIRST_NumberOfBeneficiariesApplyingFor);
			
			annexure.setZZFirstSubcolumn(X_ZZAnnexure.ZZFIRSTSUBCOLUMN_RequestedProgramme);
			annexure.setZZSecondSubcolumn(X_ZZAnnexure.ZZFOURTHSUBCOLUMN_NumberOfManagers);
			
			annexures.add(annexure);
			
			annexure = new MAnnexure(Env.getCtx(), 0, null);
			annexure.setZZHeader("ANNEXURE D (Applicable to CET Colleges)");
			annexure.setTitle("Name of the Intervention:");
			annexure.set_Attribute(COL_NAME_titleHeaderText, "Number of CET Colleges lecturers awarded skills development programmes");
			annexure.setZZFirst(X_ZZAnnexure.ZZFIRST_NumberOfBeneficiariesApplyingFor);
			
			annexure.setZZFirstSubcolumn(X_ZZAnnexure.ZZFIRSTSUBCOLUMN_RequestedProgramme);
			
			annexures.add(annexure);
			
		} else if (menuContextInfo.getProgramType() == ProgramType.TVET) {
			annexure = new MAnnexure(Env.getCtx(), 0, null);
			annexure.setZZHeader("ANNEXURE E (Applicable to TVET Colleges)");
			annexure.setTitle("Name of the Intervention:");
			annexure.set_Attribute(COL_NAME_titleHeaderText, "Number of TVET College graduates that entered an internship programme (the MQA prioritises engineering and related disciplines and may support other disciplines at its sole discretion)");
			annexure.setZZFirst(X_ZZAnnexure.ZZFIRST_NumberOfBeneficiariesApplyingFor);
			annexure.setZZSecond(X_ZZAnnexure.ZZSECOND_DisciplineApplyingFor);
			
			annexure.setZZFirstSubcolumn(X_ZZAnnexure.ZZFIRSTSUBCOLUMN_FieldOfStudy);
			annexure.setZZSecondSubcolumn(X_ZZAnnexure.ZZSECONDSUBCOLUMN_NumberOfLearners);
			
			annexures.add(annexure);
			
			annexure = new MAnnexure(Env.getCtx(), 0, null);
			annexure.setZZHeader("ANNEXURE F  (Applicable to TVET Colleges)");
			annexure.setTitle("Name of the Intervention:");
			annexure.set_Attribute(COL_NAME_titleHeaderText, "TVET Managers receiving training on curriculum related studies");
			annexure.setZZFirst(X_ZZAnnexure.ZZFIRST_NumberOfBeneficiariesApplyingFor);
			annexure.setZZSecond(X_ZZAnnexure.ZZSECOND_ProgrammeApplyingFor);
			
			annexure.setZZFirstSubcolumn(X_ZZAnnexure.ZZFIRSTSUBCOLUMN_RequestedProgramme);
			annexure.setZZSecondSubcolumn(X_ZZAnnexure.ZZSECONDSUBCOLUMN_NumberOfManagers);
			
			annexures.add(annexure);
			
			annexure = new MAnnexure(Env.getCtx(), 0, null);
			annexure.setZZHeader("ANNEXURE G (Applicable to TVET Colleges)");
			annexure.setTitle("Name of the Intervention:");
			annexure.set_Attribute(COL_NAME_titleHeaderText, "Number of TVET students requiring Work Integrated Learning to complete their work integrated learning placements (WIL)");
			annexure.setZZFirst(X_ZZAnnexure.ZZFIRST_NumberOfBeneficiariesApplyingFor);
			
			annexure.setZZFirstSubcolumn(X_ZZAnnexure.ZZFIRSTSUBCOLUMN_FieldOfStudy);
			annexure.setZZSecondSubcolumn(X_ZZAnnexure.ZZSECONDSUBCOLUMN_NumberOfLearnersAppliedFor);
			
			annexures.add(annexure);
			
			annexure = new MAnnexure(Env.getCtx(), 0, null);
			annexure.setZZHeader("ANNEXURE H (Applicable to TVET Colleges)");
			annexure.setTitle("Name of the Intervention:");
			annexure.set_Attribute(COL_NAME_titleHeaderText, "TVET Lecturers to be awarded bursaries to further their studies");
			annexure.setZZFirst(X_ZZAnnexure.ZZFIRST_NumberOfBeneficiariesApplyingFor);

			annexures.add(annexure);
			
			
			annexure = new MAnnexure(Env.getCtx(), 0, null);
			annexure.setZZHeader("ANNEXURE I (Applicable to TVET Colleges)");
			annexure.setTitle("Name of the Intervention:");
			annexure.set_Attribute(COL_NAME_titleHeaderText, "Lecturers exposed to industry through skills programmes");
			annexure.setZZFirst(X_ZZAnnexure.ZZFIRST_NumberOfBeneficiariesApplyingFor);
			annexure.setZZSecond(X_ZZAnnexure.ZZSECOND_ProgrammeApplyingFor);
			annexure.set_Attribute(COL_NAME_customizeDetaileTemplate, true);
			annexures.add(annexure);
		}
		
		addressInfo = new AddressInfoBase(menuContextInfo.getProgramType(), false, null);
	}

	public MenuContextInfo getMenuContextInfo() {
		return menuContextInfo;
	}

	public void setMenuContextInfo(MenuContextInfo menuContextInfo) {
		this.menuContextInfo = menuContextInfo;
	}

	public List<MAnnexure> getAnnexures() {
		return annexures;
	}

	public void setAnnexures(List<MAnnexure> annexures) {
		this.annexures = annexures;
	}

	public AddressInfoBase getAddressInfo() {
		return addressInfo;
	}

	public void setAddressInfo(AddressInfoBase addressInfoBase) {
		this.addressInfo = addressInfoBase;
	}
}
