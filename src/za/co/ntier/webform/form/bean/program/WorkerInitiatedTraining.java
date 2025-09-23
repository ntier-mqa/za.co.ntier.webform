package za.co.ntier.webform.form.bean.program;

import java.util.List;
import java.util.Map;

import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.CetTvetMultiLineInput;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.ProjectInput;
import za.co.ntier.webform.model.I_ZZLearnersApplied;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class WorkerInitiatedTraining implements ISaveForm, IProgram{
	private ProjectInput beneficiarie;
	
	public WorkerInitiatedTraining(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm) {
		ColumnInfo<?> colBeneficiarieTitle = ColumnInfo.getColLabel("Types of Training intervention(s) identified by the union as relevant and required programmes identified in the training gap analysis"
							, I_ZZLearnersApplied.COLUMNNAME_Name);
		ColumnInfo<?> colBeneficiarie = ColumnInfo.getColPositiveNumber("Number of people applying for per intervention* (Insert the number of beneficiaries applying for per training intervention)"
							, I_ZZLearnersApplied.COLUMNNAME_ZZBeneficiaries);
		colBeneficiarie.setCalTotal(true);
		List<ColumnInfo<?>> beneficiarieCols = List.of(colBeneficiarieTitle, colBeneficiarie);
		
		beneficiarie = ProjectInput.getProject(beneficiarieCols, null, false);
		
		List<Map<ColumnInfo<?>, Object>> titleRows = List.of(Map.of(colBeneficiarieTitle, "Short Programme")
				, Map.of(colBeneficiarieTitle, "Skills programme")
				, Map.of(colBeneficiarieTitle, "Learnerships")
				);
		beneficiarie.initProject(applicationForm, titleRows);	
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		beneficiarie.save(trxName, applicationForm);
		
	}

	/**
	 * @return the beneficiarie
	 */
	public ProjectInput getBeneficiarie() {
		return beneficiarie;
	}

	/**
	 * @param beneficiarie the beneficiarie to set
	 */
	public void setBeneficiarie(ProjectInput beneficiarie) {
		this.beneficiarie = beneficiarie;
	}

}
