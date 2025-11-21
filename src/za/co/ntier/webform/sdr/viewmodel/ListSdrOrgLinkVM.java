package za.co.ntier.webform.sdr.viewmodel;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.compiere.util.DB;
import org.compiere.util.Env;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zul.ListModelList;

import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;

public class ListSdrOrgLinkVM {
	private MenuContextInfo menuContextInfo;
	
	private ListModelList<Map<String, Object>> linkedOrganisations;
	
	public boolean isEditable(Map<String, Object> row) {
		return true;
	}
	
	@Command
	public void editLinkedOrganisation(@BindingParam("row") Map<String, Object> row) {		
		MasterUtil.openForm(MasterUtil.SDRRegistryOrgLinkUU, Integer.valueOf(((BigDecimal)row.get("ZZSdfOrganisation_ID")).intValue()));
	}
	
	@Init
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo){
		this.setMenuContextInfo(menuContextInfo);

		List<List<Object>> linkedOrganisationsPo = DB.getSQLArrayObjectsEx(null, """
				SELECT orgName, zz_SdlNumber, 
					    CASE
					    WHEN ZZReplacingPrimarySDF = 'Y' THEN 'Primary'
					    WHEN ZZSecondarySdf = 'Y' THEN 'Secondary'
					    ELSE 'Primary' END
				    AS role,
				    ZZ_Status,
				    ZZSdfOrganisation_ID
			    FROM ZZSdfOrganisation_V
			    WHERE CreatedBy = ?
				""", Env.getAD_User_ID(Env.getCtx()));
		
		if (linkedOrganisationsPo == null) {
			setLinkedOrganisations(new ListModelList<>());
		}else {
			List<Map<String, Object>> linkedOrganisationsList = new java.util.ArrayList<>();
			linkedOrganisationsPo.stream().forEach(row -> {
				Map<String, Object> linkedOrganisation = new HashMap<>();
				linkedOrganisation.put("orgName", row.get(0));
				linkedOrganisation.put("sdlNumber", row.get(1));
				linkedOrganisation.put("role", row.get(2));
				linkedOrganisation.put("ZZ_Status", row.get(3));
				linkedOrganisation.put("ZZSdfOrganisation_ID", row.get(4));
				
				linkedOrganisationsList.add(linkedOrganisation);
			});
			
			setLinkedOrganisations(new ListModelList<>(linkedOrganisationsList));
		}
		
		//Organisation, SDL Number, Role, Status, Action.
	}

	/**
	 * @return the menuContextInfo
	 */
	public MenuContextInfo getMenuContextInfo() {
		return menuContextInfo;
	}

	/**
	 * @param menuContextInfo the menuContextInfo to set
	 */
	public void setMenuContextInfo(MenuContextInfo menuContextInfo) {
		this.menuContextInfo = menuContextInfo;
	}

	/**
	 * @return the linkedOrganisations
	 */
	public ListModelList<Map<String, Object>> getLinkedOrganisations() {
		return linkedOrganisations;
	}

	/**
	 * @param linkedOrganisations the linkedOrganisations to set
	 */
	public void setLinkedOrganisations(ListModelList<Map<String, Object>> linkedOrganisations) {
		this.linkedOrganisations = linkedOrganisations;
	}

}
