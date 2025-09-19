package za.co.ntier.webform.form.bean.program;
import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.Util;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class CentreOfSpecialisationProgram extends ArtisanDevProgram implements ISaveForm, IProgram{
	private Boolean isCollegeRecognised= null;

	private Boolean isCollegeRegistered = null;

	private Boolean isCollegeSla = null;

	public CentreOfSpecialisationProgram(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm)  {
		super(menuContextInfo, applicationForm);
		initComponent(applicationForm);
	}

	/**
	 * @return the isCollegeRecognised
	 */
	public String getCollegeRecognised() {
		return Util.convert(this.isCollegeRecognised);
	}

	/**
	 * @return the isCollegeRegistered
	 */
	public String getCollegeRegistered() {
		return Util.convert(this.isCollegeRegistered);
	}

	/**
	 * @return the isCollegeSla
	 */
	public String getCollegeSla() {
		return Util.convert(isCollegeSla);
	}

	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		super.saveForm(trxName, applicationForm);
		applicationForm.setZZCollegeRecognised(Util.convert(isCollegeRecognised));
		applicationForm.setZZCollegeRegistered(Util.convert(isCollegeRegistered));
		applicationForm.setZZCollegeSla(Util.convert(isCollegeSla));
		applicationForm.saveEx(trxName);
	}

	public void initComponent(X_ZZ_Application_Form applicationForm) {
		if (applicationForm != null) {
			isCollegeRecognised = Util.convert(applicationForm.getZZCollegeRecognised());
			isCollegeRegistered = Util.convert(applicationForm.getZZCollegeRegistered());
			isCollegeSla = Util.convert(applicationForm.getZZCollegeSla());
		}
	}
	/**
	 * @param isCollegeRecognised the isCollegeRecognised to set
	 */
	public void setCollegeRecognised(String isCollegeRecognised) {
		this.isCollegeRecognised = Util.convert(isCollegeRecognised);
	}

	/**
	 * @param isCollegeRegistered the isCollegeRegistered to set
	 */
	public void setCollegeRegistered(String isCollegeRegistered) {
		this.isCollegeRegistered = Util.convert(isCollegeRegistered);
	}

	/**
	 * @param isCollegeSla the isCollegeSla to set
	 */
	public void setCollegeSla(String isCollegeSla) {
		this.isCollegeSla = Util.convert(isCollegeSla);
	}

	@Override
	public boolean isProgramValid() {
		boolean radiosDone = isCollegeRegistered != null
				&& isCollegeRecognised != null
				&& isCollegeSla != null;
		return radiosDone;
	}

}
