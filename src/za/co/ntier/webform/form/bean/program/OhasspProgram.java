package za.co.ntier.webform.form.bean.program;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.ProgramInput;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class OhasspProgram implements ISaveForm, IProgram {
	private AnnexureInfo healthSafetySkills;

	public OhasspProgram() throws NoSuchMethodException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		setHealthSafetySkills(AnnexureInfo.getAnnexureInfoOneLine(AnnexureInfo.class, null,
				List.of(ColumnInfo.getColLabel("Name of Programme"),
						ColumnInfo.getColPositiveNumber("No of 18.1 Employed Learners"),
						ColumnInfo.getColPostal(ProgramInput.PostalCodeLabel),
						ColumnInfo.getColArea(ProgramInput.AreaTitle,
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

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		// TODO Auto-generated method stub
		
	}

}
