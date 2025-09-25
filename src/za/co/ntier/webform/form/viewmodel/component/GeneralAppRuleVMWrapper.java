package za.co.ntier.webform.form.viewmodel.component;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.compiere.model.Query;
import org.compiere.util.Env;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.api.model.I_ZZ_Program_Gen_Rules;
import za.co.ntier.api.model.X_ZZ_Program_Gen_Rules;

@Init(superclass = true)
public class GeneralAppRuleVMWrapper extends ComponentVMWrapper<Object>{

	private List<String> generalAppRules;

	/**
	 * @return the generalAppRules
	 */
	public List<String> getGeneralAppRules() {
		return generalAppRules;
	}

	public static final Pattern regIndex = Pattern.compile("^[\\d\\s\\.]+");
	public static final Pattern regSplit = Pattern.compile(System.lineSeparator() +  "[\\d\\s\\.]+");
	
	@Override
	public void afterInit() {
		super.afterInit();
		
		X_ZZ_Program_Gen_Rules programGenRule = new Query(Env.getCtx(),
			  	I_ZZ_Program_Gen_Rules.Table_Name, 
			  	String.format("%s = ?",
			  			I_ZZ_Program_Gen_Rules.COLUMNNAME_ZZ_Program_Master_Data_ID), null)
			  			.setParameters(getMenuContextInfo().getProgramMasterData().getZZ_Program_Master_Data_ID()).
			  			setClient_ID().first();
	 
	
		if (programGenRule != null) {
			String despcription = programGenRule.getName();
			
			if (StringUtils.isNotBlank(despcription)) {
				despcription = RegExUtils.removeFirst(despcription, regIndex);
				
				List<String> generalAppRules = List.of(regSplit.split(despcription));
				
				setGeneralAppRules(generalAppRules);
			}
		}
		
	}
	/**
	 * @param generalAppRules the generalAppRules to set
	 */
	public void setGeneralAppRules(List<String> generalAppRules) {
		this.generalAppRules = generalAppRules;
	}
}
