package za.co.ntier.webform.form.bean.program;

import java.lang.reflect.InvocationTargetException;

import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.AddressInfo;
import za.co.ntier.webform.form.bean.AddressType;
import za.co.ntier.webform.form.bean.ProgramInput;

public class WorkExperienceProgram {

	private Integer noOfLearners;
	private AddressInfo vacationContact;
	private ProgramInput disciplines;

	public WorkExperienceProgram(MenuContextInfo menuContextInfo) throws NoSuchMethodException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		setVacationContact(new AddressInfo(AddressType.VACATION, null));
		this.setDisciplines(ProgramInput.getDisciplines(
				menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID(),

				"""
										List of disciplines supported for practical training which the number of learners applying
						should be based on
										"""));
	}

	/**
	 * @return the disciplines
	 */
	public ProgramInput getDisciplines() {
		return disciplines;
	}

	public Integer getNoOfLearners() {
		return noOfLearners;
	}

	/**
	 * @return the vacationContact
	 */
	public AddressInfo getVacationContact() {
		return vacationContact;
	}

	/**
	 * @param disciplines the disciplines to set
	 */
	public void setDisciplines(ProgramInput disciplines) {
		this.disciplines = disciplines;
	}

	public void setNoOfLearners(Integer noOfLearners) {
		this.noOfLearners = noOfLearners;
	}

	/**
	 * @param vacationContact the vacationContact to set
	 */
	public void setVacationContact(AddressInfo vacationContact) {
		this.vacationContact = vacationContact;
	}

}
