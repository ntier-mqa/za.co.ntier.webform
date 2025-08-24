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
				ProgramType.TVET.equals(this);
	}
	
	public boolean isShowMainAddress() {
		return this == CANDIDACY || this == INTERNSHIP || this == EXPERIENCE || this == DEV_PROGRAM
				|| this == ARTISAN_AIDES || this == ARTISAN_DEV
				|| this == CENTRE_SPECIALISATION || this == ARTISAN_RPL
				|| this == NON_ARTISAN_DEV || this == NON_ARTISAN_DEV_RPL
				|| this == NCV_GRADUATES || this == AET
				|| this == OHASSP
				|| this == INHOUSE_TRAINING;
	}
	
	public boolean isShowMainAddressAlter() {
		return isShowMainAddress() && this != ARTISAN_AIDES && this != ARTISAN_DEV
						&& this != CENTRE_SPECIALISATION && this != ARTISAN_RPL
						&& this != NON_ARTISAN_DEV && this != NON_ARTISAN_DEV_RPL
						&& this != NCV_GRADUATES && this != AET
						&& this != OHASSP
						&& this != INHOUSE_TRAINING;
	}
	
	public boolean isShowAddressSiteField() {
		return isShowMainAddress() && this != CENTRE_SPECIALISATION;
	}
	
	public boolean isShowAddressOrgField() {
		return this == CENTRE_SPECIALISATION;
	}
	
	public String getProgramTitle() {
		if (this == ProgramType.CANDIDACY)
			return "D. CANDIDACY";
		if (this == ProgramType.INTERNSHIP)
			return "D. INTERNSHIP";
		if (this == ProgramType.EXPERIENCE)
			return "D. WORK EXPERIENCE AND VACATION WORK";
		if (this == ProgramType.DEV_PROGRAM)
			return "D. MANAGEMENT AND EXECUTIVE DEVELOPMENT PROGRAMME (MEDP) SPECIFIC GUIDELINES";
		if (this == ProgramType.ARTISAN_AIDES)
			return "D. ARTISAN AIDES GRANT";
		if (this == ProgramType.ARTISAN_DEV)
			return "D. ARTISAN DEVELOPMENT GRANT";
		if (this == ProgramType.CENTRE_SPECIALISATION)
			return "D. CENTRE OF SPECIALISATION GRANT";
		if (this == ProgramType.ARTISAN_RPL)
			return "D. ARTISAN RPL GRANT";
		if (this == ProgramType.NON_ARTISAN_DEV)
			return "D. NON-ARTISAN DEVELOPMENT GRANT";
		if (this == ProgramType.NON_ARTISAN_DEV_RPL)
			return "D. NON-ARTISAN DEVELOPMENT (RPL) GRANT";
		if (this == ProgramType.NCV_GRADUATES)
			return "D. NCV (LEVEL 4 GRADUATES) GRANT";
		if (this == ProgramType.AET)
			return "D. AET GRANT";
		if (this == ProgramType.OHASSP)
			return "D. OCCUPATIONAL HEALTH AND SAFETY SKILLS PROGRAMMES GRANT";
		if (this == ProgramType.INHOUSE_TRAINING)
			return "D. IN-HOUSE TRAINING";
		else
			return programType.toString();
	}
	
	public boolean isShowDisciplineTable() {
		return this != ProgramType.DEV_PROGRAM && this != ProgramType.ARTISAN_AIDES && 
				this != ProgramType.ARTISAN_DEV && this != ProgramType.CENTRE_SPECIALISATION && 
				this != ProgramType.AET && this != ProgramType.ARTISAN_RPL &&
				this != ProgramType.NON_ARTISAN_DEV && this != ProgramType.NON_ARTISAN_DEV_RPL &&
				this != ProgramType.NCV_GRADUATES && this != ProgramType.INHOUSE_TRAINING &&
				this != ProgramType.OHASSP;
	}
	
	public boolean isShowTradeTable() {
		return this == ProgramType.INTERNSHIP || this == ProgramType.ARTISAN_DEV || 
				this == ProgramType.CENTRE_SPECIALISATION;
		
	}
	
}
