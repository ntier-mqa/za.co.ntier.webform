package za.co.ntier.webform.form.bean.program;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import za.co.ntier.webform.form.bean.AnnexureInfo;
import za.co.ntier.webform.form.bean.ColumnInfo;
import za.co.ntier.webform.form.bean.ProjectInput;

public class ArtisanRPLProgram {
	private AnnexureInfo allLearners;

	public ArtisanRPLProgram() throws NoSuchMethodException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		allLearners = ProjectInput.getProject(List.of(ColumnInfo.getColPositiveNumber("No. of Employed Learners"),
				ColumnInfo.getColPositiveNumber("No. of Unemployed Learners"),
				ColumnInfo.getColPositiveNumber("Total No. of Learners Applied For")));
	}

	/**
	 * @return the allLearners
	 */
	public AnnexureInfo getAllLearners() {
		return allLearners;
	}

	/**
	 * @param allLearners the allLearners to set
	 */
	public void setAllLearners(AnnexureInfo allLearners) {
		this.allLearners = allLearners;
	}

}
