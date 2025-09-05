package za.co.ntier.webform.form.bean.program;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.CetTvetOneLineInput;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.ProgramInput;
import za.co.ntier.webform.form.bean.component.ProjectInput;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class OhasspProgram implements ISaveForm, IProgram {
	private AnnexureInfo healthSafetySkills;

	public OhasspProgram() throws NoSuchMethodException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		setHealthSafetySkills(CetTvetOneLineInput.getAnnexureInfoOneLine(null,
				List.of(ColumnInfo.getColLabel(ProjectInput.colNameProgrammeLabel),
						ColumnInfo.getColPositiveNumber(ProjectInput.colNoEmployedLabel),
						ColumnInfo.getColPostal(ProgramInput.colPostalCodeLabel),
						ColumnInfo.getColArea(ProgramInput.colAreaLabel,
								MasterUtil.getInitCities())),
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
