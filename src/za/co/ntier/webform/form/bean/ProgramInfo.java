package za.co.ntier.webform.form.bean;


import za.co.ntier.webform.form.MenuContextInfo;


public class ProgramInfo {
	
	private LearnerInputTableInfo disciplineTableInfo;
	
	private LearnerInputTableInfo tradeTableInfo;
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
	private MenuContextInfo menuContextInfo;
	private ProgramType programType;
	
	/**
	 * @return the programType
	 */
	public ProgramType getProgramType() {
		return programType;
	}

	/**
	 * @param programType the programType to set
	 */
	public void setProgramType(ProgramType programType) {
		this.programType = programType;
	}

	public ProgramInfo(MenuContextInfo menuContextInfo) {
		this.menuContextInfo = menuContextInfo;
		programType = menuContextInfo.getProgramType();
		int programMasterDataID = menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID();

		// vacation contact
		if (programType == ProgramType.EXPERIENCE) {
			setVacationContact(new AddressInfoBase(AddressType.VACATION, null));
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
			this.nonArtisanDevInfo = new NonArtisanDevInfo(menuContextInfo);
		}else if (programType == ProgramType.NON_ARTISAN_DEV_RPL) {
			this.nonArtisanDevRPLInfo = new NonArtisanDevRPLInfo();
		}else if (programType == ProgramType.NCV_GRADUATES) {
			this.ncvGraduatesInfo = new NCVGraduatesInfo(menuContextInfo);
		}else if (programType == ProgramType.AET) {
			this.aetInfo = new AETInfo(menuContextInfo);
		}else if (programType == ProgramType.OHASSP) {
			this.ohasspInfo = new OHASSPInfo();
		}else if (programType == ProgramType.INHOUSE_TRAINING) {
			this.inhouseTrainingInfo = new InhouseTrainingInfo();
		}
		
		if (programType.isShowTradeTable())
			tradeTableInfo = TradeDisciplineInputTableInfo.getTrade (programMasterDataID, programType);
		
		// discipline table info
		if (programType.isShowDisciplineTable()) {
			disciplineTableInfo = TradeDisciplineInputTableInfo.getDiscipline(programMasterDataID, programType);
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
	 * @return the disciplineTableInfo
	 */
	public LearnerInputTableInfo getDisciplineTableInfo() {
		return disciplineTableInfo;
	}


	/**
	 * @return the programTitle
	 */
	public String getProgramTitle() {
		return menuContextInfo.getProgramType().getProgramTitle();
	}

	/**
	 * @return the tradeTableInfo
	 */
	public LearnerInputTableInfo getTradeTableInfo() {
		return tradeTableInfo;
	}


	/**
	 * @param disciplineTableInfo the disciplineTableInfo to set
	 */
	public void setDisciplineTableInfo(LearnerInputTableInfo disciplineTableInfo) {
		this.disciplineTableInfo = disciplineTableInfo;
	}

	/**
	 * @param tradeTableInfo the tradeTableInfo to set
	 */
	public void setTradeTableInfo(LearnerInputTableInfo tradeTableInfo) {
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

	/**
	 * @return the menuContextInfo
	 */
	public MenuContextInfo getMenuContextInfo() {
		return menuContextInfo;
	}

	/**
	 * @param menuContextInfo the menuContextInfo to set
	 */
	public void setMenuContextInfo(MenuContextInfo menuContextInfo) {
		this.menuContextInfo = menuContextInfo;
	}

}
