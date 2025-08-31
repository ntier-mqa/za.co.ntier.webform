package za.co.ntier.webform.form.bean.program;

import java.lang.reflect.InvocationTargetException;

import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.AddressInfo;
import za.co.ntier.webform.form.bean.AddressType;
import za.co.ntier.webform.form.bean.LearnerInput;

public class WorkExperienceProgram {
	
	private Integer noOfLearners;
	private AddressInfo vacationContact;
	private LearnerInput disciplines;
	
	
	public WorkExperienceProgram(MenuContextInfo menuContextInfo) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		setVacationContact(new AddressInfo(AddressType.VACATION, null));
		this.setDisciplines(LearnerInput.getDisciplines(menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID(), 
				
				"""
				List of disciplines supported for practical training which the number of learners applying
should be based on
				"""));
	}
	
	public Integer getNoOfLearners() {
		return noOfLearners;
	}

	public void setNoOfLearners(Integer noOfLearners) {
		this.noOfLearners = noOfLearners;
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
	 * @return the disciplines
	 */
	public LearnerInput getDisciplines() {
		return disciplines;
	}

	/**
	 * @param disciplines the disciplines to set
	 */
	public void setDisciplines(LearnerInput disciplines) {
		this.disciplines = disciplines;
	}

	

}
