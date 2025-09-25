package za.co.ntier.webform.form.bean.program;
import za.co.ntier.api.model.X_ZZ_Application_Form;
import za.co.ntier.webform.form.AbstractProgram;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.Util;

public class MedpProgram extends AbstractProgram {
	private int noOfLearners;

	public MedpProgram(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm) {
		super(menuContextInfo, applicationForm);
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
		super.saveForm(applicationForm);
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
