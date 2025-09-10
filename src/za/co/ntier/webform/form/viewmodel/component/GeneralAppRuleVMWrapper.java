package za.co.ntier.webform.form.viewmodel.component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.RegExUtils;
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

	public static final Pattern regIndex = Pattern.compile("^[\\d\\s\\.]+");
	public static final Pattern regSplit = Pattern.compile(System.lineSeparator() +  "[\\d\\s\\.]+");
	
	@Init
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo) {
		
		  X_ZZ_Program_Gen_Rules programGenRule = new Query(Env.getCtx(),
				  	I_ZZ_Program_Gen_Rules.Table_Name, 
				  	String.format("%s = ?",
				  			I_ZZ_Program_Gen_Rules.COLUMNNAME_ZZ_Program_Master_Data_ID), null)
				  			.setParameters(menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID()).
				  			setClient_ID().first();
		 
		
		String despcription = programGenRule.getName();
		
		if (despcription != null) {
			despcription = RegExUtils.removeFirst(despcription, regIndex);
			
			List<String> generalAppRules = List.of(regSplit.split(despcription));
			
			setGeneralAppRules(generalAppRules);
		}
				
	}

	/**
	 * @param generalAppRules the generalAppRules to set
	 */
	public void setGeneralAppRules(List<String> generalAppRules) {
		this.generalAppRules = generalAppRules;
	}
}
