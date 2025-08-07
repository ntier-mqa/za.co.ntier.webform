package za.co.ntier.webform.form.bean;

import java.util.Random;

import za.co.ntier.webform.form.viewmodel.master.MasterUtil;

public class ProgramInfo {
	private AddressInfoBase alternateProgramContact;
	private DisciplineTableInfo disciplineTableInfo;
	private AddressInfoBase programContact;
	private ProgramType programType = ProgramType.UNKNOWN;
	private DisciplineTableInfo tradeTableInfo;

	public ProgramInfo(int programMasterDataID, String sProgramType) {
		this.programType = ProgramType.valueOf(sProgramType.toUpperCase());
		AddressCategory addressCategory = this.programType == ProgramType.CANDIDACY ? AddressCategory.CANDIDACY
				: this.programType == ProgramType.INTERNSHIP ? AddressCategory.INTERNSHIP : AddressCategory.UNKNOWN;

		programContact = new AddressInfoBase(addressCategory,
				MasterUtil.getRegions().get(new Random().nextInt(MasterUtil.getRegions().size())));

		addressCategory = this.programType == ProgramType.CANDIDACY ? AddressCategory.CANDIDACY_ALTER
				: this.programType == ProgramType.INTERNSHIP ? AddressCategory.INTERNSHIP_ALTER
						: AddressCategory.UNKNOWN;

		alternateProgramContact = new AddressInfoBase(addressCategory,
				MasterUtil.getRegions().get(new Random().nextInt(MasterUtil.getRegions().size())));

		disciplineTableInfo = new DisciplineTableInfo(programMasterDataID, programType, false);

		tradeTableInfo = new DisciplineTableInfo(programMasterDataID, programType, true);

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
			return "CANDIDACY";
		if (programType == ProgramType.INTERNSHIP)
			return "INTERNSHIP";
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

}
