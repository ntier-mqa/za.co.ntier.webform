package za.co.ntier.webform.component;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.compiere.model.Query;
import org.compiere.util.Env;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.api.model.I_ZZ_Program_Gen_Rules;
import za.co.ntier.api.model.X_ZZ_Program_Gen_Rules;
import za.co.ntier.webform.sdr.component.viewmodel.BaseComponentVM;


public class DeclarationVM extends BaseComponentVM<DeclarationPanel>{

	private List<Entry<String, List<String>>> generalAppRules;

	/**
	 * @return the generalAppRules
	 */
	public List<Entry<String, List<String>>> getGeneralAppRules() {
		return generalAppRules;
	}

	public static final Pattern regIndex = Pattern.compile("^[\\d\\s\\.]+");
	public static final Pattern regSubIndex = Pattern.compile("^[•\\s\\.]+");
	public static final Pattern regSplit = Pattern.compile(System.lineSeparator());
	
	@Init(superclass = true)
	public void initDeclarationVM() {
		initRule();
	}
	
	private void initRule() {
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
				List<Entry<String, List<String>>> removeIndexAppRules = new ArrayList<>();
					
				List<String> sub = null;
				for (String appRule :  generalAppRules) {
					if(appRule.startsWith("• ")) {
						appRule = RegExUtils.removeFirst(appRule, regSubIndex);
						sub.add(appRule);
					}else {
						sub = new ArrayList<>();
						appRule = RegExUtils.removeFirst(appRule, regIndex);
						removeIndexAppRules.add(new AbstractMap.SimpleEntry<>(appRule, sub));
					}
				}
				setGeneralAppRules(removeIndexAppRules);
			}
		}
	}
	
	/**
	 * @param generalAppRules the generalAppRules to set
	 */
	public void setGeneralAppRules(List<Entry<String, List<String>>> generalAppRules) {
		this.generalAppRules = generalAppRules;
	}
}
