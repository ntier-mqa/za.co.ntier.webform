package za.co.ntier.webform.form.bean.program;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.ProjectInput;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class NcvGraduatesProgram implements ISaveForm, IProgram {
	private ProjectInput unemployed;

	public NcvGraduatesProgram(MenuContextInfo menuContextInfo) throws NoSuchMethodException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		setUnemployed(ProjectInput.getProject(List.of(ColumnInfo.getColPositiveNumber(ProjectInput.colNoUnEmployedLabel))));

	}

	/**
	 * @return the unemployed
	 */
	public ProjectInput getUnemployed() {
		return unemployed;
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param unemployed the unemployed to set
	 */
	public void setUnemployed(ProjectInput unemployed) {
		this.unemployed = unemployed;
	}
}
