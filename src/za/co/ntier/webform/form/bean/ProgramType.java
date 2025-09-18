package za.co.ntier.webform.form.bean;

public enum ProgramType {
	AET, ARTISAN_AIDES, ARTISAN_DEV, ARTISAN_RPL,
	CANDIDACY, CENTRE_SPECIALISATION,
	CET, DEV_PROGRAM,
	EXPERIENCE, INHOUSE_TRAINING,
	INTERNSHIP, NCV_GRADUATES, NON_ARTISAN_DEV,
	NON_ARTISAN_DEV_RPL, OHASSP, TVET, TVET_BURSARS,
	WORKPLACE_COACHES,
	MINING_COMMUNITY,
	UNEMPLOYED_YOUTH,
	SMALL_BUSINESS,
	WORKER_INITIATED_TRAINING,
	LEARNING_MATERIALS_DEVELOPMENT,
	MULTIYEAR_PARTNERSHIP_INTERNSHIP,
	MULTIYEAR_PARTNERSHIP_WORK_EXPERIENCE,
	STANDARD_SETTING,
	UNKNOWN;
	
	
	public String getZulPath() {
		switch (this) {
			case CANDIDACY:
				return "program/candidacy.zul";
			case INTERNSHIP:
				return "program/internship.zul";
			case EXPERIENCE:
				return "program/workExperience.zul";
			case DEV_PROGRAM:
				return "program/medp.zul";
			case ARTISAN_AIDES:
				return "program/artisanAides.zul";
			case ARTISAN_DEV:
				return "program/artisanDev.zul";
			case CENTRE_SPECIALISATION:
				return "program/centreOfSpecialisation.zul";
			case ARTISAN_RPL:
				return "program/artisanRPL.zul";
			case NON_ARTISAN_DEV:
				return "program/nonArtisanDev.zul";
			case NON_ARTISAN_DEV_RPL:
				return "program/nonArtisanDevRPL.zul";
			case NCV_GRADUATES:
				return "program/ncvGraduates.zul";
			case AET:
				return "program/aet.zul";
			case OHASSP:
				return "program/ohassp.zul";
			case INHOUSE_TRAINING:
				return "program/inhouseTraining.zul";
			case WORKPLACE_COACHES:
				return "program/workplaceCoaches.zul";
			case MINING_COMMUNITY:
				return "program/miningCommunityUnemployedYouth.zul";
			case UNEMPLOYED_YOUTH:
				return "program/miningCommunityUnemployedYouth.zul";
			case SMALL_BUSINESS:
				return "program/smallBusiness.zul";
			case WORKER_INITIATED_TRAINING:
				return "program/workerInitiatedTraining.zul";
			case LEARNING_MATERIALS_DEVELOPMENT:
				return "program/learningMaterialsDevelopment.zul";
			case MULTIYEAR_PARTNERSHIP_INTERNSHIP:
				return "program/multiyearPartnershipInternship.zul";
			case MULTIYEAR_PARTNERSHIP_WORK_EXPERIENCE:
				return "program/multiyearPartnershipWorkExperience.zul";
			case STANDARD_SETTING:
				return "program/standardSetting.zul";
			case CET:
			case TVET:
			case TVET_BURSARS:
				return "program/cetTvet.zul";
			default:
				return null;
		}
	}
	

	public boolean isCetTvet() {
		return ProgramType.CET.equals(this) || ProgramType.TVET.equals(this) || ProgramType.TVET_BURSARS.equals(this);
	}

	public boolean isDev_Program() {
		return ProgramType.DEV_PROGRAM.equals(this);
	}

	public boolean isShowAddressOrgField() {
		return this == CENTRE_SPECIALISATION;
	}

	public boolean isShowAddressSiteField() {
		return isShowMainAddress() && this != CENTRE_SPECIALISATION;
	}

	public boolean isShowMainAddress() {
		return this == CANDIDACY || this == INTERNSHIP || this == EXPERIENCE || this == DEV_PROGRAM
				|| this == ARTISAN_AIDES || this == ARTISAN_DEV || this == CENTRE_SPECIALISATION || this == ARTISAN_RPL
				|| this == NON_ARTISAN_DEV || this == NON_ARTISAN_DEV_RPL || this == NCV_GRADUATES || this == AET
				|| this == OHASSP || this == INHOUSE_TRAINING || this == TVET || this == CET || this == TVET_BURSARS 
				|| this == WORKPLACE_COACHES || this == MINING_COMMUNITY || this == UNEMPLOYED_YOUTH
				|| this == SMALL_BUSINESS || this == WORKER_INITIATED_TRAINING || this == LEARNING_MATERIALS_DEVELOPMENT 
				|| this == MULTIYEAR_PARTNERSHIP_INTERNSHIP || this == MULTIYEAR_PARTNERSHIP_WORK_EXPERIENCE || this == STANDARD_SETTING  
				;

	}

	public boolean isShowMainAddressAlter() {
		return isShowMainAddress() && this != ARTISAN_AIDES && this != ARTISAN_DEV && this != CENTRE_SPECIALISATION
				&& this != ARTISAN_RPL && this != NON_ARTISAN_DEV && this != NON_ARTISAN_DEV_RPL
				&& this != NCV_GRADUATES && this != AET && this != OHASSP && this != INHOUSE_TRAINING && this != TVET
				&& this != CET && this != MINING_COMMUNITY && this != UNEMPLOYED_YOUTH && this != SMALL_BUSINESS
				&& this != WORKER_INITIATED_TRAINING && this != LEARNING_MATERIALS_DEVELOPMENT
				&& this != STANDARD_SETTING;
				
	}

}
