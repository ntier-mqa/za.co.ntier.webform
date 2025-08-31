package za.co.ntier.webform.form.bean;


import java.lang.reflect.InvocationTargetException;

import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.program.AETProgram;
import za.co.ntier.webform.form.bean.program.ArtisanAidesProgram;
import za.co.ntier.webform.form.bean.program.ArtisanDevProgram;
import za.co.ntier.webform.form.bean.program.ArtisanRPLProgram;
import za.co.ntier.webform.form.bean.program.CentreOfSpecialisationProgram;
import za.co.ntier.webform.form.bean.program.MedpProgram;
import za.co.ntier.webform.form.bean.program.InhouseTrainingProgram;
import za.co.ntier.webform.form.bean.program.NCVGraduatesProgram;
import za.co.ntier.webform.form.bean.program.NonArtisanDevProgram;
import za.co.ntier.webform.form.bean.program.NonArtisanDevRPLProgram;
import za.co.ntier.webform.form.bean.program.OHASSPProgram;
import za.co.ntier.webform.form.bean.program.WorkExperienceProgram;


public class ProgramInfo {
	
	private LearnerInputTableInfo disciplineTableInfo;
	
	private LearnerInputTableInfo tradeTableInfo;
	private WorkExperienceProgram workInfo;
	private MedpProgram devProgramInfo;
	private AddressInfo vacationContact;
	private ArtisanAidesProgram artisanAidesInfo;
	private ArtisanDevProgram artisanDevInfo;
	private CentreOfSpecialisationProgram centreOfSpecialisationInfo;
	private ArtisanRPLProgram artisanRPLInfo;
	private NonArtisanDevProgram nonArtisanDevInfo;
	private NonArtisanDevRPLProgram nonArtisanDevRPLInfo;
	private NCVGraduatesProgram ncvGraduatesInfo;
	private AETProgram aetInfo;
	private OHASSPProgram ohasspInfo;
	private InhouseTrainingProgram inhouseTrainingInfo;
	private MenuContextInfo menuContextInfo;
	private ProgramType programType;
	
	private AnnexureInfo tradeInfo;
	
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

	public ProgramInfo(MenuContextInfo menuContextInfo) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		this.menuContextInfo = menuContextInfo;
		programType = menuContextInfo.getProgramType();
		int programMasterDataID = menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID();

		// vacation contact
		if (programType == ProgramType.EXPERIENCE) {
			setVacationContact(new AddressInfo(AddressType.VACATION, null));
		}
		
		// extra program info
		if (programType == ProgramType.DEV_PROGRAM) {
			devProgramInfo = new MedpProgram();
		
		}else if (programType == ProgramType.ARTISAN_AIDES) {
			this.artisanAidesInfo =  new ArtisanAidesProgram();
		}else if (programType == ProgramType.ARTISAN_DEV) {
			this.artisanDevInfo = new ArtisanDevProgram(menuContextInfo);
		}else if (programType == ProgramType.CENTRE_SPECIALISATION) {
			this.setcentreOfSpecialisationInfo(new CentreOfSpecialisationProgram(menuContextInfo));
		}else if (programType == ProgramType.ARTISAN_RPL) {
			this.artisanRPLInfo = new ArtisanRPLProgram();
		}else if (programType == ProgramType.NON_ARTISAN_DEV) {
			this.nonArtisanDevInfo = new NonArtisanDevProgram(menuContextInfo);
		}else if (programType == ProgramType.NON_ARTISAN_DEV_RPL) {
			this.nonArtisanDevRPLInfo = new NonArtisanDevRPLProgram();
		}else if (programType == ProgramType.NCV_GRADUATES) {
			this.ncvGraduatesInfo = new NCVGraduatesProgram(menuContextInfo);
		}else if (programType == ProgramType.AET) {
			this.aetInfo = new AETProgram(menuContextInfo);
		}else if (programType == ProgramType.OHASSP) {
			this.ohasspInfo = new OHASSPProgram();
		}else if (programType == ProgramType.INHOUSE_TRAINING) {
			this.inhouseTrainingInfo = new InhouseTrainingProgram();
		}
		
		// discipline table info
		if (programType.isShowDisciplineTable()) {
			disciplineTableInfo = TradeDisciplineInputTableInfo.getDiscipline(programMasterDataID, programType);
		}
	}

	/**
	 * @return the centreOfSpecialisationInfo
	 */
	public CentreOfSpecialisationProgram getCentreOfSpecialisationInfo() {
		return centreOfSpecialisationInfo;
	}

	/**
	 * @param centreOfSpecialisationInfo the centreOfSpecialisationInfo to set
	 */
	public void setCentreOfSpecialisationInfo(CentreOfSpecialisationProgram centreOfSpecialisationInfo) {
		this.centreOfSpecialisationInfo = centreOfSpecialisationInfo;
	}

	/**
	 * @return the nonArtisanDevInfo
	 */
	public NonArtisanDevProgram getNonArtisanDevInfo() {
		return nonArtisanDevInfo;
	}

	/**
	 * @param nonArtisanDevInfo the nonArtisanDevInfo to set
	 */
	public void setNonArtisanDevInfo(NonArtisanDevProgram nonArtisanDevInfo) {
		this.nonArtisanDevInfo = nonArtisanDevInfo;
	}

	/**
	 * @return the nonArtisanDevRPLInfo
	 */
	public NonArtisanDevRPLProgram getNonArtisanDevRPLInfo() {
		return nonArtisanDevRPLInfo;
	}

	/**
	 * @param nonArtisanDevRPLInfo the nonArtisanDevRPLInfo to set
	 */
	public void setNonArtisanDevRPLInfo(NonArtisanDevRPLProgram nonArtisanDevRPLInfo) {
		this.nonArtisanDevRPLInfo = nonArtisanDevRPLInfo;
	}

	/**
	 * @return the ncvGraduatesInfo
	 */
	public NCVGraduatesProgram getNcvGraduatesInfo() {
		return ncvGraduatesInfo;
	}

	/**
	 * @param ncvGraduatesInfo the ncvGraduatesInfo to set
	 */
	public void setNcvGraduatesInfo(NCVGraduatesProgram ncvGraduatesInfo) {
		this.ncvGraduatesInfo = ncvGraduatesInfo;
	}

	/**
	 * @return the aetInfo
	 */
	public AETProgram getAetInfo() {
		return aetInfo;
	}

	/**
	 * @param aetInfo the aetInfo to set
	 */
	public void setAetInfo(AETProgram aetInfo) {
		this.aetInfo = aetInfo;
	}

	/**
	 * @return the ohasspInfo
	 */
	public OHASSPProgram getOhasspInfo() {
		return ohasspInfo;
	}

	/**
	 * @param ohasspInfo the ohasspInfo to set
	 */
	public void setOhasspInfo(OHASSPProgram ohasspInfo) {
		this.ohasspInfo = ohasspInfo;
	}

	/**
	 * @return the inhouseTrainingInfo
	 */
	public InhouseTrainingProgram getInhouseTrainingInfo() {
		return inhouseTrainingInfo;
	}

	/**
	 * @param inhouseTrainingInfo the inhouseTrainingInfo to set
	 */
	public void setInhouseTrainingInfo(InhouseTrainingProgram inhouseTrainingInfo) {
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

	public WorkExperienceProgram getWorkInfo() {
		return workInfo;
	}

	public void setWorkInfo(WorkExperienceProgram workInfo) {
		this.workInfo = workInfo;
	}

	/**
	 * @return the vacationContact
	 */
	public AddressInfo getVacationContact() {
		return vacationContact;
	}

	/**
	 * @param vacationContact the vacationContact to set
	 */
	public void setVacationContact(AddressInfo vacationContact) {
		this.vacationContact = vacationContact;
	}

	/**
	 * @return the devProgramInfo
	 */
	public MedpProgram getDevProgramInfo() {
		return devProgramInfo;
	}

	/**
	 * @param devProgramInfo the devProgramInfo to set
	 */
	public void setDevProgramInfo(MedpProgram devProgramInfo) {
		this.devProgramInfo = devProgramInfo;
	}

	/**
	 * @return the artisanAidesInfo
	 */
	public ArtisanAidesProgram getArtisanAidesInfo() {
		return artisanAidesInfo;
	}

	/**
	 * @param artisanAidesInfo the artisanAidesInfo to set
	 */
	public void setArtisanAidesInfo(ArtisanAidesProgram artisanAidesInfo) {
		this.artisanAidesInfo = artisanAidesInfo;
	}

	/**
	 * @return the artisanDevInfo
	 */
	public ArtisanDevProgram getArtisanDevInfo() {
		return artisanDevInfo;
	}

	/**
	 * @param artisanDevInfo the artisanDevInfo to set
	 */
	public void setArtisanDevInfo(ArtisanDevProgram artisanDevInfo) {
		this.artisanDevInfo = artisanDevInfo;
	}

	/**
	 * @return the centreOfSpecialisationInfo
	 */
	public CentreOfSpecialisationProgram getcentreOfSpecialisationInfo() {
		return centreOfSpecialisationInfo;
	}

	/**
	 * @param centreOfSpecialisationInfo the centreOfSpecialisationInfo to set
	 */
	public void setcentreOfSpecialisationInfo(CentreOfSpecialisationProgram centreOfSpecialisationInfo) {
		this.centreOfSpecialisationInfo = centreOfSpecialisationInfo;
	}

	/**
	 * @return the artisanRPLInfo
	 */
	public ArtisanRPLProgram getArtisanRPLInfo() {
		return artisanRPLInfo;
	}

	/**
	 * @param artisanRPLInfo the artisanRPLInfo to set
	 */
	public void setArtisanRPLInfo(ArtisanRPLProgram artisanRPLInfo) {
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

	/**
	 * @return the tradeInfo
	 */
	public AnnexureInfo getTradeInfo() {
		return tradeInfo;
	}

	/**
	 * @param tradeInfo the tradeInfo to set
	 */
	public void setTradeInfo(AnnexureInfo tradeInfo) {
		this.tradeInfo = tradeInfo;
	}


}
