package za.co.ntier.webform.form.bean.program;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.AddressType;
import za.co.ntier.webform.form.bean.component.AddressInfo;
import za.co.ntier.webform.form.bean.component.ProgramInput;
import za.co.ntier.webform.form.bean.component.ProjectInput;
import za.co.ntier.webform.model.X_ZZ_Application_Form;
import za.co.ntier.webform.model.X_ZZ_FormDiscipline;

public class WorkExperienceProgram implements ISaveForm, IProgram {

	private ProgramInput disciplines;
	private Integer noOfLearners;
	private AddressInfo vacationContact;

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

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) throws IOException {
		vacationContact.saveForm(trxName, applicationForm);
		ProgramInput.saveFormDisciplines(trxName, applicationForm, disciplines, X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_Discipline);
		
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
