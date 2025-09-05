package za.co.ntier.webform.form.bean.program;
import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class MedpProgram implements ISaveForm, IProgram {
	private int noOfLearners;

	/**
	 * @return the noOfLearners
	 */
	public int getNoOfLearners() {
		return noOfLearners;
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		applicationForm.setZZTotalNumberApplied(noOfLearners);
		applicationForm.saveEx(trxName);
		
	}

	/**
	 * @param noOfLearners the noOfLearners to set
	 */
	public void setNoOfLearners(int noOfLearners) {
		this.noOfLearners = noOfLearners;
	}
}
