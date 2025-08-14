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
	private ArtisanAidesInfo artisanAidesInfo;
	private ArtisanDevInfo artisanDevInfo;
	private CentreOfSpecialisationInfo centreOfSpecialisationInfo;
	private ArtisanRPLInfo artisanRPLInfo;
	private NonArtisanDevInfo nonArtisanDevInfo;
	private NonArtisanDevRPLInfo nonArtisanDevRPLInfo;
	private NCVGraduatesInfo ncvGraduatesInfo;
	private AETInfo aetInfo;
	private OHASSPInfo ohasspInfo;
	private InhouseTrainingInfo inhouseTrainingInfo;

	public ProgramInfo(int programMasterDataID, ProgramType programType) {
		this.programType = programType;

		// main contact
		if (programType.isShowMainAddress())
			programContact = new AddressInfoBase(programType, false,
				MasterUtil.getRegions().get(new Random().nextInt(MasterUtil.getRegions().size())));

		// main alternate contact
		if (programType.isShowMainAddressAlter())
			alternateProgramContact = new AddressInfoBase(programType, true,
				MasterUtil.getRegions().get(new Random().nextInt(MasterUtil.getRegions().size())));

		// vacation contact
		if (this.programType == ProgramType.EXPERIENCE) {
			setVacationContact(new AddressInfoBase(AddressCategory.VACATION,
					MasterUtil.getRegions().get(new Random().nextInt(MasterUtil.getRegions().size()))));
		}
		
		// extra program info
		if (programType == ProgramType.DEV_PROGRAM) {
			devProgramInfo = new DevProgramInfo();
		} else if (programType == ProgramType.EXPERIENCE) {
			workInfo = new WorkInfo();
		}else if (programType == ProgramType.ARTISAN_AIDES) {
			this.artisanAidesInfo =  new ArtisanAidesInfo();
		}else if (programType == ProgramType.ARTISAN_DEV) {
			this.artisanDevInfo = new ArtisanDevInfo();
		}else if (programType == ProgramType.CENTRE_SPECIALISATION) {
			this.setcentreOfSpecialisationInfo(new CentreOfSpecialisationInfo());
		}else if (programType == ProgramType.ARTISAN_RPL) {
			this.artisanRPLInfo = new ArtisanRPLInfo();
		}else if (programType == ProgramType.NON_ARTISAN_DEV) {
			this.nonArtisanDevInfo = new NonArtisanDevInfo();
		}else if (programType == ProgramType.NON_ARTISAN_DEV_RPL) {
			this.nonArtisanDevRPLInfo = new NonArtisanDevRPLInfo();
		}else if (programType == ProgramType.NCV_GRADUATES) {
			this.ncvGraduatesInfo = new NCVGraduatesInfo();
		}else if (programType == ProgramType.NCV_GRADUATES) {
			this.aetInfo = new AETInfo();
		}else if (programType == ProgramType.OHASSP) {
			this.ohasspInfo = new OHASSPInfo();
		}else if (programType == ProgramType.INHOUSE_TRAINING) {
			this.inhouseTrainingInfo = new InhouseTrainingInfo();
		}
		
		if (programType.isShowTradeTable())
			tradeTableInfo = new DisciplineTableInfo(programMasterDataID, programType, X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_InternshipTrade);
		
		// discipline table info
		if (programType.isShowDisciplineTable()) {
			disciplineTableInfo = new DisciplineTableInfo(programMasterDataID, programType, X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_InternshipDiscipline);
		}
	}

	/**
	 * @return the centreOfSpecialisationInfo
	 */
	public CentreOfSpecialisationInfo getCentreOfSpecialisationInfo() {
		return centreOfSpecialisationInfo;
	}

	/**
	 * @param centreOfSpecialisationInfo the centreOfSpecialisationInfo to set
	 */
	public void setCentreOfSpecialisationInfo(CentreOfSpecialisationInfo centreOfSpecialisationInfo) {
		this.centreOfSpecialisationInfo = centreOfSpecialisationInfo;
	}

	/**
	 * @return the nonArtisanDevInfo
	 */
	public NonArtisanDevInfo getNonArtisanDevInfo() {
		return nonArtisanDevInfo;
	}

	/**
	 * @param nonArtisanDevInfo the nonArtisanDevInfo to set
	 */
	public void setNonArtisanDevInfo(NonArtisanDevInfo nonArtisanDevInfo) {
		this.nonArtisanDevInfo = nonArtisanDevInfo;
	}

	/**
	 * @return the nonArtisanDevRPLInfo
	 */
	public NonArtisanDevRPLInfo getNonArtisanDevRPLInfo() {
		return nonArtisanDevRPLInfo;
	}

	/**
	 * @param nonArtisanDevRPLInfo the nonArtisanDevRPLInfo to set
	 */
	public void setNonArtisanDevRPLInfo(NonArtisanDevRPLInfo nonArtisanDevRPLInfo) {
		this.nonArtisanDevRPLInfo = nonArtisanDevRPLInfo;
	}

	/**
	 * @return the ncvGraduatesInfo
	 */
	public NCVGraduatesInfo getNcvGraduatesInfo() {
		return ncvGraduatesInfo;
	}

	/**
	 * @param ncvGraduatesInfo the ncvGraduatesInfo to set
	 */
	public void setNcvGraduatesInfo(NCVGraduatesInfo ncvGraduatesInfo) {
		this.ncvGraduatesInfo = ncvGraduatesInfo;
	}

	/**
	 * @return the aetInfo
	 */
	public AETInfo getAetInfo() {
		return aetInfo;
	}

	/**
	 * @param aetInfo the aetInfo to set
	 */
	public void setAetInfo(AETInfo aetInfo) {
		this.aetInfo = aetInfo;
	}

	/**
	 * @return the ohasspInfo
	 */
	public OHASSPInfo getOhasspInfo() {
		return ohasspInfo;
	}

	/**
	 * @param ohasspInfo the ohasspInfo to set
	 */
	public void setOhasspInfo(OHASSPInfo ohasspInfo) {
		this.ohasspInfo = ohasspInfo;
	}

	/**
	 * @return the inhouseTrainingInfo
	 */
	public InhouseTrainingInfo getInhouseTrainingInfo() {
		return inhouseTrainingInfo;
	}

	/**
	 * @param inhouseTrainingInfo the inhouseTrainingInfo to set
	 */
	public void setInhouseTrainingInfo(InhouseTrainingInfo inhouseTrainingInfo) {
		this.inhouseTrainingInfo = inhouseTrainingInfo;
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
		return programType.getProgramTitle();
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

	/**
	 * @return the artisanAidesInfo
	 */
	public ArtisanAidesInfo getArtisanAidesInfo() {
		return artisanAidesInfo;
	}

	/**
	 * @param artisanAidesInfo the artisanAidesInfo to set
	 */
	public void setArtisanAidesInfo(ArtisanAidesInfo artisanAidesInfo) {
		this.artisanAidesInfo = artisanAidesInfo;
	}

	/**
	 * @return the artisanDevInfo
	 */
	public ArtisanDevInfo getArtisanDevInfo() {
		return artisanDevInfo;
	}

	/**
	 * @param artisanDevInfo the artisanDevInfo to set
	 */
	public void setArtisanDevInfo(ArtisanDevInfo artisanDevInfo) {
		this.artisanDevInfo = artisanDevInfo;
	}

	/**
	 * @return the centreOfSpecialisationInfo
	 */
	public CentreOfSpecialisationInfo getcentreOfSpecialisationInfo() {
		return centreOfSpecialisationInfo;
	}

	/**
	 * @param centreOfSpecialisationInfo the centreOfSpecialisationInfo to set
	 */
	public void setcentreOfSpecialisationInfo(CentreOfSpecialisationInfo centreOfSpecialisationInfo) {
		this.centreOfSpecialisationInfo = centreOfSpecialisationInfo;
	}

	/**
	 * @return the artisanRPLInfo
	 */
	public ArtisanRPLInfo getArtisanRPLInfo() {
		return artisanRPLInfo;
	}

	/**
	 * @param artisanRPLInfo the artisanRPLInfo to set
	 */
	public void setArtisanRPLInfo(ArtisanRPLInfo artisanRPLInfo) {
		this.artisanRPLInfo = artisanRPLInfo;
	}

}
