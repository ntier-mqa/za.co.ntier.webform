package za.co.ntier.webform.form.bean.program;

import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.AddressType;
import za.co.ntier.webform.form.bean.component.AddressInfo;
import za.co.ntier.webform.form.bean.component.ProgramInput;
import za.co.ntier.webform.model.I_ZZ_Application_Form;
import za.co.ntier.webform.model.X_ZZ_Application_Form;
import za.co.ntier.webform.model.X_ZZ_FormDiscipline;

public class WorkExperienceProgram implements ISaveForm, IProgram {

	private X_ZZ_Application_Form applicationForm;
	private ProgramInput disciplines;
	private Integer noOfLearners;

	private AddressInfo vacationContact;

	public WorkExperienceProgram(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm) {
		setVacationContact(new AddressInfo(AddressType.VACATION));
		this.setDisciplines(ProgramInput.getDisciplines(
				menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID(),
				applicationForm,
				"""
										List of disciplines supported for practical training which the number of learners applying
						should be based on
										"""));
		initComponent(applicationForm);
	}

	/**
	 * @return the applicationForm
	 */
	public X_ZZ_Application_Form getApplicationForm() {
		return applicationForm;
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
	public void initComponent(X_ZZ_Application_Form applicationForm) {
		this.setApplicationForm(applicationForm);
		if (vacationContact != null) {
			vacationContact.initComponent(applicationForm);
		}
		
		if (applicationForm != null && applicationForm.getZZTotalNumberApplied() > 0) {
			noOfLearners = applicationForm.getZZTotalNumberApplied();
		}
	}
	
	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm)  {
		this.setApplicationForm(applicationForm);
		vacationContact.saveForm(trxName, applicationForm);
		ProgramInput.saveFormDisciplines(trxName, applicationForm, disciplines, X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_Discipline);
		if (noOfLearners == null)
			applicationForm.set_ValueOfColumn(I_ZZ_Application_Form.COLUMNNAME_ZZTotalNumberApplied, null);
		else
			applicationForm.setZZTotalNumberApplied(noOfLearners);
		applicationForm.saveEx(trxName);
	}

	/**
	 * @param applicationForm the applicationForm to set
	 */
	public void setApplicationForm(X_ZZ_Application_Form applicationForm) {
		this.applicationForm = applicationForm;
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

	@Override
	public boolean isProgramValid() {
		// TODO Auto-generated method stub
		return true;
	}

}
