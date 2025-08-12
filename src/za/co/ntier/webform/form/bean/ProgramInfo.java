package za.co.ntier.webform.form.bean;

import java.util.Random;

import za.co.ntier.webform.form.viewmodel.master.MasterUtil;
import za.co.ntier.webform.model.X_ZZ_FormDiscipline;

public class ProgramInfo {
	private AddressInfoBase alternateProgramContact;
	private DisciplineTableInfo disciplineTableInfo;
	private AddressInfoBase programContact;
	private ProgramType programType = ProgramType.UNKNOWN;
	private DisciplineTableInfo tradeTableInfo;
	private WorkInfo workInfo;
	private DevProgramInfo devProgramInfo;
	private AddressInfoBase vacationContact;

	public ProgramInfo(int programMasterDataID, ProgramType programType) {
		this.programType = programType;
		AddressCategory addressCategory = this.programType == ProgramType.CANDIDACY ? AddressCategory.CANDIDACY
				: this.programType == ProgramType.INTERNSHIP ? AddressCategory.INTERNSHIP
				: this.programType == ProgramType.DEVPROGRAM ? AddressCategory.DEVPROGRAM
				: this.programType == ProgramType.EXPERIENCE ? AddressCategory.EXPERIENCE: AddressCategory.UNKNOWN;

		programContact = new AddressInfoBase(addressCategory,
				MasterUtil.getRegions().get(new Random().nextInt(MasterUtil.getRegions().size())));

		addressCategory = this.programType == ProgramType.CANDIDACY ? AddressCategory.CANDIDACY_ALTER
				: this.programType == ProgramType.INTERNSHIP ? AddressCategory.INTERNSHIP_ALTER
				: this.programType == ProgramType.DEVPROGRAM? AddressCategory.DEVPROGRAM_ALTER
				: this.programType == ProgramType.EXPERIENCE ? AddressCategory.EXPERIENCE_ALTER
						: AddressCategory.UNKNOWN;

		alternateProgramContact = new AddressInfoBase(addressCategory,
				MasterUtil.getRegions().get(new Random().nextInt(MasterUtil.getRegions().size())));

		if (this.programType == ProgramType.EXPERIENCE) {
			setVacationContact(new AddressInfoBase(AddressCategory.VACATION,
					MasterUtil.getRegions().get(new Random().nextInt(MasterUtil.getRegions().size()))));
		}
		
		String disciplineType = programType == ProgramType.INTERNSHIP ? X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_InternshipDiscipline :
				 	programType == ProgramType.CANDIDACY ? X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_CandidacyDiscipline : 
					programType == ProgramType.EXPERIENCE? X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_ExperienceDiscipline :
					X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_UnknowDiscipline	;
	
		if (programType == ProgramType.DEVPROGRAM) {
			devProgramInfo = new DevProgramInfo();
		} else if (programType == ProgramType.EXPERIENCE) {
			workInfo = new WorkInfo();
		}else if (programType == ProgramType.INTERNSHIP) {
			tradeTableInfo = new DisciplineTableInfo(programMasterDataID, programType, X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_InternshipTrade);
		} 
		
		if (programType != ProgramType.DEVPROGRAM) {
			disciplineTableInfo = new DisciplineTableInfo(programMasterDataID, programType, disciplineType);
		}
	}

	/**
	 * @return the alternateProgramContact
	 */
	public AddressInfoBase getAlternateProgramContact() {
		return alternateProgramContact;
	}

	/**
	 * @return the disciplineTableInfo
	 */
	public DisciplineTableInfo getDisciplineTableInfo() {
		return disciplineTableInfo;
	}

	/**
	 * @return the programContact
	 */
	public AddressInfoBase getProgramContact() {
		return programContact;
	}

	/**
	 * @return the programTitle
	 */
	public String getProgramTitle() {
		if (programType == ProgramType.CANDIDACY)
			return "D. CANDIDACY";
		if (programType == ProgramType.INTERNSHIP)
			return "D. INTERNSHIP";
		if (programType == ProgramType.EXPERIENCE)
			return "D. WORK EXPERIENCE AND VACATION WORK";
		if (programType == ProgramType.DEVPROGRAM)
			return "D. MANAGEMENT AND EXECUTIVE DEVELOPMENT PROGRAMME (MEDP) SPECIFIC GUIDELINES";
		else
			return programType.toString();
	}

	/**
	 * @return the programType
	 */
	public ProgramType getProgramType() {
		return programType;
	}

	/**
	 * @return the tradeTableInfo
	 */
	public DisciplineTableInfo getTradeTableInfo() {
		return tradeTableInfo;
	}

	/**
	 * @param alternateProgramContact the alternateProgramContact to set
	 */
	public void setAlternateProgramContact(AddressInfoBase alternateProgramContact) {
		this.alternateProgramContact = alternateProgramContact;
	}

	/**
	 * @param disciplineTableInfo the disciplineTableInfo to set
	 */
	public void setDisciplineTableInfo(DisciplineTableInfo disciplineTableInfo) {
		this.disciplineTableInfo = disciplineTableInfo;
	}

	/**
	 * @param programContact the programContact to set
	 */
	public void setProgramContact(AddressInfoBase programContact) {
		this.programContact = programContact;
	}

	/**
	 * @param programType the programType to set
	 */
	public void setProgramType(ProgramType programType) {
		this.programType = programType;
	}

	/**
	 * @param tradeTableInfo the tradeTableInfo to set
	 */
	public void setTradeTableInfo(DisciplineTableInfo tradeTableInfo) {
		this.tradeTableInfo = tradeTableInfo;
	}

	public WorkInfo getWorkInfo() {
		return workInfo;
	}

	public void setWorkInfo(WorkInfo workInfo) {
		this.workInfo = workInfo;
	}

	/**
	 * @return the vacationContact
	 */
	public AddressInfoBase getVacationContact() {
		return vacationContact;
	}

	/**
	 * @param vacationContact the vacationContact to set
	 */
	public void setVacationContact(AddressInfoBase vacationContact) {
		this.vacationContact = vacationContact;
	}

	/**
	 * @return the devProgramInfo
	 */
	public DevProgramInfo getDevProgramInfo() {
		return devProgramInfo;
	}

	/**
	 * @param devProgramInfo the devProgramInfo to set
	 */
	public void setDevProgramInfo(DevProgramInfo devProgramInfo) {
		this.devProgramInfo = devProgramInfo;
	}

}
