package za.co.ntier.webform.form.bean.program;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.bean.AnnexureInfo;
import za.co.ntier.webform.form.bean.ColumnInfo;

public class OhasspProgram {
	private AnnexureInfo healthSafetySkills;
	
	public OhasspProgram() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		setHealthSafetySkills(AnnexureInfo.getAnnexureInfoOneLine(AnnexureInfo.class, null, 
				List.of(ColumnInfo.getColLabel("Name of Programme"),
						ColumnInfo.getColPositiveNumber("No of 18.1 Employed Learners"),
						ColumnInfo.getColPositiveNumber("Site Postal Code"),
						ColumnInfo.getColArea("Area", 
								MasterUtil.getCities().stream().limit(MasterUtil.limitItem).toList())),
				"Occupational Health and Safety Skills Programmes"));
	}

	/**
	 * @return the healthSafetySkills
	 */
	public AnnexureInfo getHealthSafetySkills() {
		return healthSafetySkills;
	}

	/**
	 * @param healthSafetySkills the healthSafetySkills to set
	 */
	public void setHealthSafetySkills(AnnexureInfo healthSafetySkills) {
		this.healthSafetySkills = healthSafetySkills;
	}

}
