package za.co.ntier.webform.form.bean;

public enum ProgramType {
	CANDIDACY("CANDIDACY"), 
	INTERNSHIP("INTERNSHIP"), 
	EXPERIENCE("EXPERIENCE"), 
	DEVPROGRAM("DEVPROGRAM"),
	ARTISAN_AIDES("ARTISAN AIDES GRANT"),
	ARTISAN_DEV("ARTISAN DEVELOPMENT GRANT"),
	CENTRE_SPECIALISATION("CENTRE OF SPECIALISATION GRANT"),
	ARTISAN_RPL("ARTISAN RPL GRANT"),
	NON_ARTISAN_DEV("NON-ARTISAN DEVELOPMENT GRANT"),
	NON_ARTISAN_DEV_RPL("NON-ARTISAN DEVELOPMENT RPL GRANT"),
	NCV_LEVEL_4_GRADUATES("NCV LEVEL 4 GRADUATES GRANT"),
	AET("AET GRANT"),
	OCCUPATIONAL_HEALTH_AND_SAFETY_SKILLS_PROGRAMMES("OCCUPATIONAL HEALTH AND SAFETY SKILLS PROGRAMMES GRANT"),
	INHOUSE_TRAINING("IN-HOUSE TRAINING"),
	UNKNOWN("UNNOWN PROGRAM TYPE");

	private String programType;

	ProgramType(String programType) {
		this.programType = programType;
	}

	@Override
	public String toString() {
		return programType;
	}

	public boolean isShowMainAddress() {
		return this == CANDIDACY || this == INTERNSHIP || this == EXPERIENCE || this == DEVPROGRAM
				|| this == ARTISAN_AIDES || this == ARTISAN_DEV
				|| this == CENTRE_SPECIALISATION || this == ARTISAN_RPL
				|| this == NON_ARTISAN_DEV || this == NON_ARTISAN_DEV_RPL
				|| this == NCV_LEVEL_4_GRADUATES || this == AET
				|| this == OCCUPATIONAL_HEALTH_AND_SAFETY_SKILLS_PROGRAMMES
				|| this == INHOUSE_TRAINING;
	}
	
	public boolean isShowMainAddressAlter() {
		return isShowMainAddress() && this != ARTISAN_AIDES && this != ARTISAN_DEV
						&& this != CENTRE_SPECIALISATION && this != ARTISAN_RPL
						&& this != NON_ARTISAN_DEV && this != NON_ARTISAN_DEV_RPL
						&& this != NCV_LEVEL_4_GRADUATES && this != AET
						&& this != OCCUPATIONAL_HEALTH_AND_SAFETY_SKILLS_PROGRAMMES
						&& this != INHOUSE_TRAINING;
	}
	
	public boolean isShowAddressSiteField() {
		return isShowMainAddress() && this != CENTRE_SPECIALISATION;
	}
	
	public boolean isShowAddressOrgField() {
		return this == CENTRE_SPECIALISATION;
	}
}
