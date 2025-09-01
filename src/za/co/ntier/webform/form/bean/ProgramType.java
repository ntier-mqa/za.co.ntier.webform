package za.co.ntier.webform.form.bean;

public enum ProgramType {
	CANDIDACY("CANDIDACY"), 
	INTERNSHIP("INTERNSHIP"), 
	EXPERIENCE("EXPERIENCE"), 
	DEV_PROGRAM("DEVELOPMENT PROGRAMM"),
	ARTISAN_AIDES("ARTISAN AIDES"),
	ARTISAN_DEV("ARTISAN DEVELOPMENT"),
	CENTRE_SPECIALISATION("CENTRE OF SPECIALISATION"),
	ARTISAN_RPL("ARTISAN RPL"),
	NON_ARTISAN_DEV("NON-ARTISAN DEVELOPMENT"),
	NON_ARTISAN_DEV_RPL("NON-ARTISAN DEVELOPMENT RPL"),
	NCV_GRADUATES("NCV LEVEL 4 GRADUATES"),
	AET("AET"),
	OHASSP("OCCUPATIONAL HEALTH AND SAFETY SKILLS PROGRAMMES"),
	INHOUSE_TRAINING("IN-HOUSE TRAINING"),
	TVET_BURSARS("TVET_BURSARS"),
	CET("CET"),
	TVET("TVET"),
	
	UNKNOWN("UNNOWN PROGRAM TYPE");

	private String programType;

	ProgramType(String programType) {
		this.programType = programType;
	}

	@Override
	public String toString() {
		return programType;
	}
	
	public boolean isCetTvet(){
		return ProgramType.CET.equals(this) ||
				ProgramType.TVET.equals(this) ||
				ProgramType.TVET_BURSARS.equals(this);
	}
	
	public boolean isShowMainAddress() {
		return this == CANDIDACY || this == INTERNSHIP || this == EXPERIENCE || this == DEV_PROGRAM
				|| this == ARTISAN_AIDES || this == ARTISAN_DEV
				|| this == CENTRE_SPECIALISATION || this == ARTISAN_RPL
				|| this == NON_ARTISAN_DEV || this == NON_ARTISAN_DEV_RPL
				|| this == NCV_GRADUATES || this == AET
				|| this == OHASSP
				|| this == INHOUSE_TRAINING
				|| this == TVET
				|| this == CET
				|| this == TVET_BURSARS;
	}
	
	public boolean isShowMainAddressAlter() {
		return isShowMainAddress() && this != ARTISAN_AIDES && this != ARTISAN_DEV
						&& this != CENTRE_SPECIALISATION && this != ARTISAN_RPL
						&& this != NON_ARTISAN_DEV && this != NON_ARTISAN_DEV_RPL
						&& this != NCV_GRADUATES && this != AET
						&& this != OHASSP
						&& this != INHOUSE_TRAINING
						&& this != TVET
						&& this != CET
						&& this != TVET_BURSARS;
	}
	
	public boolean isShowAddressSiteField() {
		return isShowMainAddress() && this != CENTRE_SPECIALISATION;
	}
	
	public boolean isShowAddressOrgField() {
		return this == CENTRE_SPECIALISATION;
	}
	
}
