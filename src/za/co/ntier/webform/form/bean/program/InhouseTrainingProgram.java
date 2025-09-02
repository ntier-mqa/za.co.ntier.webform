package za.co.ntier.webform.form.bean.program;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.bean.AnnexureInfo;
import za.co.ntier.webform.form.bean.ColumnInfo;

public class InhouseTrainingProgram {
	private AnnexureInfo inhouse;

	public InhouseTrainingProgram() throws NoSuchMethodException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		this.setInhouse(AnnexureInfo.getAnnexureInfoOneLine(AnnexureInfo.class, null,
				List.of(ColumnInfo.getColLabel("Name of Programme"),
						ColumnInfo.getColPositiveNumber("No of Learners applied for"),
						ColumnInfo.getColPositiveNumber("Site Postal Code"),
						ColumnInfo.getColArea("Area",
								MasterUtil.getCities().stream().limit(MasterUtil.limitItem).toList())),
				"Inhouse /Industry/Company based short courses"));

	}

	/**
	 * @return the inhouse
	 */
	public AnnexureInfo getInhouse() {
		return inhouse;
	}

	/**
	 * @param inhouse the inhouse to set
	 */
	public void setInhouse(AnnexureInfo inhouse) {
		this.inhouse = inhouse;
	}
}
