package za.co.ntier.webform.form.bean.program;
import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class CentreOfSpecialisationProgram extends ArtisanDevProgram implements ISaveForm, IProgram{
	public static String convert(Boolean value) {
		if (value == null) {
			return null;
		}
		
		return value?"Y":"N";
	}
	public static Boolean convert(String value) {
		Boolean converted = null;
		if (value == null)
			converted = null;
		else if ("Y".equals(value))
			converted = Boolean.TRUE;
		else if ("N".equals(value))
			converted = Boolean.FALSE;
		else
			throw new IllegalArgumentException("need Y/N string");
		
		return converted;
	}
	private Boolean isCollegeRecognised= Boolean.FALSE;
	
	private Boolean isCollegeRegistered = Boolean.TRUE;
	
	private Boolean isCollegeSla = null;

	public CentreOfSpecialisationProgram(MenuContextInfo menuContextInfo)  {
		super(menuContextInfo);
	}

	/**
	 * @return the isCollegeRecognised
	 */
	public String getCollegeRecognised() {
		return convert(this.isCollegeRecognised);
	}

	/**
	 * @return the isCollegeRegistered
	 */
	public String getCollegeRegistered() {
		return convert(this.isCollegeRegistered);
	}

	/**
	 * @return the isCollegeSla
	 */
	public String getCollegeSla() {
		return convert(isCollegeSla);
	}

	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		super.saveForm(trxName, applicationForm);
		applicationForm.setZZCollegeRecognised(convert(isCollegeRecognised));
		applicationForm.setZZCollegeRegistered(convert(isCollegeRegistered));
		applicationForm.setZZCollegeSla(convert(isCollegeSla));
		applicationForm.saveEx(trxName);
	}

	/**
	 * @param isCollegeRecognised the isCollegeRecognised to set
	 */
	public void setCollegeRecognised(String isCollegeRecognised) {
		this.isCollegeRecognised = convert(isCollegeRecognised);
	}

	/**
	 * @param isCollegeRegistered the isCollegeRegistered to set
	 */
	public void setCollegeRegistered(String isCollegeRegistered) {
		this.isCollegeRegistered = convert(isCollegeRegistered);
	}
	
	/**
	 * @param isCollegeSla the isCollegeSla to set
	 */
	public void setCollegeSla(String isCollegeSla) {
		this.isCollegeSla = convert(isCollegeSla);
	}
}
