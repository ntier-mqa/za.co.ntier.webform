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
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class InhouseTrainingProgram implements ISaveForm, IProgram{
	private AnnexureInfo inhouse;

	public InhouseTrainingProgram() throws NoSuchMethodException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		this.setInhouse(CetTvetOneLineInput.getAnnexureInfoOneLine(null,
				List.of(ColumnInfo.getColLabel("Name of Programme"),
						ColumnInfo.getColPositiveNumber("No of Learners applied for"),
						ColumnInfo.getColPostal(ProgramInput.PostalCodeLabel),
						ColumnInfo.getColArea("Area",
								MasterUtil.getInitCities())),
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

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		// TODO Auto-generated method stub
		
	}
}
