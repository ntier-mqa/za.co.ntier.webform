package za.co.ntier.webform.form.bean;

import java.util.Random;

import za.co.ntier.webform.form.viewmodel.master.MasterUtil;

public class ProgramInfo {
	public static final String PROGRAM_TYPE_CANDIDACY = "CANDIDACY";
	public static final String PROGRAM_TYPE_INTERNSHIP = "INTERNSHIP";

	private AddressInfoBase alternateProgramContact;
	private DisciplineTableInfo disciplineTableInfo;
	private boolean isCandidacy = true;
	private boolean isInternShip = false;
	private AddressInfoBase programContact;
	private DisciplineTableInfo tradeTableInfo;

	public ProgramInfo(int programMasterDataID, String programType) {
		programContact = new AddressInfoBase(AddressCategory.PROGRAM_CONTACT,
				MasterUtil.getRegions().get(new Random().nextInt(MasterUtil.getRegions().size())));
		alternateProgramContact = new AddressInfoBase(AddressCategory.PROGRAM_CONTACT,
				MasterUtil.getRegions().get(new Random().nextInt(MasterUtil.getRegions().size())));
		
		disciplineTableInfo = new DisciplineTableInfo(programMasterDataID, false);

		tradeTableInfo = new DisciplineTableInfo(programMasterDataID, true);

		isCandidacy = PROGRAM_TYPE_CANDIDACY.equalsIgnoreCase(programType);
		isInternShip = PROGRAM_TYPE_INTERNSHIP.equalsIgnoreCase(programType);
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
	 * @return the tradeTableInfo
	 */
	public DisciplineTableInfo getTradeTableInfo() {
		return tradeTableInfo;
	}

	/**
	 * @return the isCandidacy
	 */
	public boolean isCandidacy() {
		return isCandidacy;
	}

	/**
	 * @return the isInternShip
	 */
	public boolean isInternShip() {
		return isInternShip;
	}

	/**
	 * @param alternateProgramContact the alternateProgramContact to set
	 */
	public void setAlternateProgramContact(AddressInfoBase alternateProgramContact) {
		this.alternateProgramContact = alternateProgramContact;
	}

	/**
	 * @param isCandidacy the isCandidacy to set
	 */
	public void setCandidacy(boolean isCandidacy) {
		this.isCandidacy = isCandidacy;
	}

	/**
	 * @param disciplineTableInfo the disciplineTableInfo to set
	 */
	public void setDisciplineTableInfo(DisciplineTableInfo disciplineTableInfo) {
		this.disciplineTableInfo = disciplineTableInfo;
	}

	/**
	 * @param isInternShip the isInternShip to set
	 */
	public void setInternShip(boolean isInternShip) {
		this.isInternShip = isInternShip;
	}

	/**
	 * @param programContact the programContact to set
	 */
	public void setProgramContact(AddressInfoBase programContact) {
		this.programContact = programContact;
	}

	/**
	 * @param tradeTableInfo the tradeTableInfo to set
	 */
	public void setTradeTableInfo(DisciplineTableInfo tradeTableInfo) {
		this.tradeTableInfo = tradeTableInfo;
	}
}
