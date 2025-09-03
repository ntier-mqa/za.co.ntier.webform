package za.co.ntier.webform.form.viewmodel.component;

import java.util.ArrayList;
import java.util.List;

import org.compiere.model.Query;
import org.compiere.util.Env;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.model.I_ZZ_Program_Gen_Rules;
import za.co.ntier.webform.model.I_ZZ_Program_Master_Data;
import za.co.ntier.webform.model.X_ZZ_Program_Gen_Rules;

public class GeneralAppRuleVMWrapper {

	private List<String> generalAppRules;

	/**
	 * @return the generalAppRules
	 */
	public List<String> getGeneralAppRules() {
		return generalAppRules;
	}

	@Init
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo) {
		List<X_ZZ_Program_Gen_Rules> programGenRules = new Query(Env.getCtx(), I_ZZ_Program_Gen_Rules.Table_Name,
				String.format("%s = ?", I_ZZ_Program_Master_Data.COLUMNNAME_ZZ_Program_Master_Data_ID), null)
				.setParameters(menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID()).setClient_ID()
				.setOrderBy(I_ZZ_Program_Gen_Rules.COLUMNNAME_Line).list();

		List<String> generalAppRules = new ArrayList<>();

		programGenRules.stream().forEach((programGenRule) -> {

			generalAppRules.add(programGenRule.getName());
		});

		setGeneralAppRules(generalAppRules);
	}

	/**
	 * @param generalAppRules the generalAppRules to set
	 */
	public void setGeneralAppRules(List<String> generalAppRules) {
		this.generalAppRules = generalAppRules;
	}
}
