package za.co.ntier.webform.form.bean;

public enum ProgramType {
	AET("AET"), ARTISAN_AIDES("ARTISAN AIDES"), ARTISAN_DEV("ARTISAN DEVELOPMENT"), ARTISAN_RPL("ARTISAN RPL"),
	CANDIDACY("CANDIDACY"), CENTRE_SPECIALISATION("CENTRE OF SPECIALISATION"),
	CET("CET"), DEV_PROGRAM("DEVELOPMENT PROGRAMM"),
	EXPERIENCE("EXPERIENCE"), INHOUSE_TRAINING("IN-HOUSE TRAINING"),
	INTERNSHIP("INTERNSHIP"), NCV_GRADUATES("NCV LEVEL 4 GRADUATES"), NON_ARTISAN_DEV("NON-ARTISAN DEVELOPMENT"),
	NON_ARTISAN_DEV_RPL("NON-ARTISAN DEVELOPMENT RPL"), OHASSP("OCCUPATIONAL HEALTH AND SAFETY SKILLS PROGRAMMES"), TVET("TVET"), TVET_BURSARS("TVET_BURSARS"),

	UNKNOWN("UNNOWN PROGRAM TYPE");

	private String programType;

	ProgramType(String programType) {
		this.programType = programType;
	}

	public boolean isDev_Program() {
		return ProgramType.DEV_PROGRAM.equals(this);
	}
	

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
				|| this == OHASSP || this == INHOUSE_TRAINING || this == TVET || this == CET || this == TVET_BURSARS;
	}

	public boolean isShowMainAddressAlter() {
		return isShowMainAddress() && this != ARTISAN_AIDES && this != ARTISAN_DEV && this != CENTRE_SPECIALISATION
				&& this != ARTISAN_RPL && this != NON_ARTISAN_DEV && this != NON_ARTISAN_DEV_RPL
				&& this != NCV_GRADUATES && this != AET && this != OHASSP && this != INHOUSE_TRAINING && this != TVET
				&& this != CET && this != TVET_BURSARS;
	}
	
	@Override
	public String toString() {
		return programType;
	}
}
