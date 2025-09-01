package za.co.ntier.webform.form.bean.program;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.ColumnInfo;
import za.co.ntier.webform.form.bean.ProjectInput;

public class NcvGraduatesProgram {
	private ProjectInput unemployed;
	public NcvGraduatesProgram(MenuContextInfo menuContextInfo) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		setUnemployed(ProjectInput.getProject(
				List.of(
						ColumnInfo.getColPositiveNumber("No. of Unemployed Learners")
						)));

	}
	/**
	 * @return the unemployed
	 */
	public ProjectInput getUnemployed() {
		return unemployed;
	}
	/**
	 * @param unemployed the unemployed to set
	 */
	public void setUnemployed(ProjectInput unemployed) {
		this.unemployed = unemployed;
	}
}
