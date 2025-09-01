package za.co.ntier.webform.form.bean.program;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.AnnexureInfo;
import za.co.ntier.webform.form.bean.ColumnInfo;
import za.co.ntier.webform.form.bean.ProjectInput;

public class NonArtisanDevRPLProgram {
	private AnnexureInfo totalNumApplied;
	
	public NonArtisanDevRPLProgram(MenuContextInfo menuContextInfo) throws NoSuchMethodException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		setTotalNumApplied(ProjectInput.getProject(
				List.of(ColumnInfo.getColPositiveNumber("Total Number of Learners Applied For"))));

}

	/**
	 * @return the totalNumApplied
	 */
	public AnnexureInfo getTotalNumApplied() {
		return totalNumApplied;
	}

	/**
	 * @param totalNumApplied the totalNumApplied to set
	 */
	public void setTotalNumApplied(AnnexureInfo totalNumApplied) {
		this.totalNumApplied = totalNumApplied;
	}

}
