package za.co.ntier.webform.form.bean.program;

public class WorkProgram {
	private Integer noOfLearners;
	
	private String vacationWorkLabel = "• VACATION WORK";
	
	private String numOfEmployerLabel = "Number of learners applying for Vacation Work?";
	
	public WorkProgram() {
		
	}
	
	public Integer getNoOfLearners() {
		return noOfLearners;
	}

	public void setNoOfLearners(Integer noOfLearners) {
		this.noOfLearners = noOfLearners;
	}

	public String getVacationWorkLabel() {
		return vacationWorkLabel;
	}

	public void setVacationWorkLabel(String vacationWorkLabel) {
		this.vacationWorkLabel = vacationWorkLabel;
	}

	public String getNumOfEmployerLabel() {
		return numOfEmployerLabel;
	}

	public void setNumOfEmployerLabel(String numOfEmployerLabel) {
		this.numOfEmployerLabel = numOfEmployerLabel;
	}

	

}
