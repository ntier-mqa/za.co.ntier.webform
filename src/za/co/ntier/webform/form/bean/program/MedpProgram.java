package za.co.ntier.webform.form.bean.program;
import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.Util;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class MedpProgram implements ISaveForm, IProgram {
	private int noOfLearners;

	public MedpProgram(X_ZZ_Application_Form applicationForm) {
		initComponent(applicationForm);
	}

	/**
	 * @return the noOfLearners
	 */
	public int getNoOfLearners() {
		return noOfLearners;
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		applicationForm.setZZTotalNumberApplied(noOfLearners);
		applicationForm.setZZNoLearners(noOfLearners);
		
	}
	
	public void initComponent(X_ZZ_Application_Form applicationForm) {
		if (applicationForm != null) {
			noOfLearners = Util.convert(applicationForm.getZZNoLearners());
		}
	}
	
	/**
	 * @param noOfLearners the noOfLearners to set
	 */
	public void setNoOfLearners(int noOfLearners) {
		this.noOfLearners = noOfLearners;
	}

	@Override
	public boolean isProgramValid() {
		Integer n = ((MedpProgram) this).getNoOfLearners();
        return n != null && n > 0;
	}
}
