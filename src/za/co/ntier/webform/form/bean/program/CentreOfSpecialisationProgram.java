package za.co.ntier.webform.form.bean.program;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.CollegeRadio;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class CentreOfSpecialisationProgram extends ArtisanDevProgram implements ISaveForm, IProgram{
	private Boolean isCollegeRegistered = true;
	private Boolean isCollegeRecognised= true;
	private Boolean isCollegeSla = true;
	
	private List<CollegeRadio> collegeValues = List.of(new CollegeRadio("YES", Boolean.TRUE),
			new CollegeRadio("NO", Boolean.FALSE));
	
	public CentreOfSpecialisationProgram(MenuContextInfo menuContextInfo) throws NoSuchMethodException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		super(menuContextInfo);
	}
	
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) throws IOException{
		super.saveForm(trxName, applicationForm);
		//save radio
	}

	/**
	 * @return the isCollegeRegistered
	 */
	public Boolean getCollegeRegistered() {
		return isCollegeRegistered;
	}

	/**
	 * @param isCollegeRegistered the isCollegeRegistered to set
	 */
	public void setCollegeRegistered(Boolean isCollegeRegistered) {
		this.isCollegeRegistered = isCollegeRegistered;
	}

	/**
	 * @return the isCollegeRecognised
	 */
	public Boolean getCollegeRecognised() {
		return isCollegeRecognised;
	}

	/**
	 * @param isCollegeRecognised the isCollegeRecognised to set
	 */
	public void setCollegeRecognised(Boolean isCollegeRecognised) {
		this.isCollegeRecognised = isCollegeRecognised;
	}

	/**
	 * @return the isCollegeSla
	 */
	public Boolean getCollegeSla() {
		return isCollegeSla;
	}

	/**
	 * @param isCollegeSla the isCollegeSla to set
	 */
	public void setCollegeSla(Boolean isCollegeSla) {
		this.isCollegeSla = isCollegeSla;
	}

	/**
	 * @return the collegeValues
	 */
	public List<CollegeRadio> getCollegeValues() {
		return collegeValues;
	}

	/**
	 * @param collegeValues the collegeValues to set
	 */
	public void setCollegeValues(List<CollegeRadio> collegeValues) {
		this.collegeValues = collegeValues;
	}

}
