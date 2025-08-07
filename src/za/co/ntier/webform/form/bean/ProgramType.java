package za.co.ntier.webform.form.bean;

public enum ProgramType {
	CANDIDACY("CANDIDACY"), INTERNSHIP("INTERNSHIP"), UNKNOWN("UNNOWN PROGRAM TYPE");

	private String programType;

	ProgramType(String programType) {
		this.programType = programType;
	}

	@Override
	public String toString() {
		return programType;
	}

}
