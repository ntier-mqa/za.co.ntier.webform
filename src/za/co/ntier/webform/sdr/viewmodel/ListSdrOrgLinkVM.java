package za.co.ntier.webform.sdr.viewmodel;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.ValueNamePair;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zul.ListModelList;

import za.co.ntier.api.model.I_ZZSdfOrganisation_v;
import za.co.ntier.api.model.X_ZZSdfOrganisation;
import za.co.ntier.api.model.X_ZZSdfOrganisation_v;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;

public class ListSdrOrgLinkVM {
	private MenuContextInfo menuContextInfo;
	
	private ListModelList<Map<String, Object>> linkedOrganisations = new ListModelList<>();
	
	public boolean isUnlink(Map<String, Object> row) {
		ValueNamePair zzDocStatus = (ValueNamePair)row.get(I_ZZSdfOrganisation_v.COLUMNNAME_ZZ_DocStatus);
		boolean isUnlink = X_ZZSdfOrganisation_v.ZZ_DOCSTATUS_Approved.equals(zzDocStatus.getValue());
		isUnlink = isUnlink || X_ZZSdfOrganisation_v.ZZ_DOCSTATUS_Pending.equals(zzDocStatus.getValue());
		isUnlink = isUnlink && (row.get(ListSdrOrgLinkVM.ROLE_key).equals(ListSdrOrgLinkVM.ROLE_Secondary)
				|| row.get(ListSdrOrgLinkVM.ROLE_key).equals(ListSdrOrgLinkVM.ROLE_Primary));
		
		return isUnlink;
	}
	
	public boolean isEditRequest(Map<String, Object> row) {
		ValueNamePair zzDocStatus = (ValueNamePair)row.get(I_ZZSdfOrganisation_v.COLUMNNAME_ZZ_DocStatus);
		return X_ZZSdfOrganisation_v.ZZ_DOCSTATUS_Draft.equals(zzDocStatus.getValue());
	}
	
	public boolean isOrgEditable(Map<String, Object> row) {
		return row.get(ListSdrOrgLinkVM.ROLE_key).equals(ListSdrOrgLinkVM.ROLE_Primary);
	}
	
	@Command
	public void editLinkedOrganisation(@BindingParam("row") Map<String, Object> row) {		
		MasterUtil.openForm(MasterUtil.SDRRegistryOrgLinkUU, Integer.valueOf(((BigDecimal)row.get("ZZSdfOrganisation_ID")).intValue()));
	}
	
	@Command
	public void unLinkedOrganisation(@BindingParam("row") Map<String, Object> row) {
		MasterUtil.showConfirmDialog("ZZOrganisationUnlink", t -> {
			BigDecimal sdfOrganisationID = (BigDecimal)row.get("ZZSdfOrganisation_ID");
			X_ZZSdfOrganisation sdfOrganisation = new X_ZZSdfOrganisation(Env.getCtx(), sdfOrganisationID.intValueExact(), null);
			sdfOrganisation.setZZ_DocStatus(X_ZZSdfOrganisation.ZZ_DOCSTATUS_Delinked);
			sdfOrganisation.saveEx();
			
			initList();
		});
	}
	
	
	@Command
	public void editOrganisation(@BindingParam("row") Map<String, Object> row) {		
		MasterUtil.openForm(MasterUtil.SDRMaintainOrganisationUU, Integer.valueOf(((BigDecimal)row.get("ZZSdfOrganisation_ID")).intValue()));
	}
	
	@Init
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo){
		this.setMenuContextInfo(menuContextInfo);
		initList();
	}
	
	public final static String ROLE_Primary = "Primary";
	public final static String ROLE_Secondary = "Secondary";
	public final static String ROLE_key = "role";
	
	private void initList () {
		List<List<Object>> linkedOrganisationsPo = DB.getSQLArrayObjectsEx(null, String.format("""
				SELECT %s, %s, 
					    CASE
					    WHEN ZZReplacingPrimarySDF = 'Y' THEN '%s'
					    WHEN ZZSecondarySdf = 'Y' THEN '%s'
					    ELSE '%s' END
				    AS role,
				    %s,
				    %s
			    FROM %s
			    WHERE %s = ? AND %s <> ?
				""", 
				I_ZZSdfOrganisation_v.COLUMNNAME_OrgName
				, I_ZZSdfOrganisation_v.COLUMNNAME_ZZ_SDL_No
				, ListSdrOrgLinkVM.ROLE_Primary
				, ListSdrOrgLinkVM.ROLE_Secondary
				, ListSdrOrgLinkVM.ROLE_Primary
				, I_ZZSdfOrganisation_v.COLUMNNAME_ZZ_DocStatus
				, I_ZZSdfOrganisation_v.COLUMNNAME_ZZSdfOrganisation_ID
				, I_ZZSdfOrganisation_v.Table_Name
				, I_ZZSdfOrganisation_v.COLUMNNAME_CreatedBy
				, I_ZZSdfOrganisation_v.COLUMNNAME_ZZ_DocStatus
				), Env.getAD_User_ID(Env.getCtx()), X_ZZSdfOrganisation_v.ZZ_DOCSTATUS_Delinked);
		
		if (linkedOrganisationsPo == null) {
			linkedOrganisations.clear();
		}else {
			List<Map<String, Object>> linkedOrganisationsList = new java.util.ArrayList<>();
			final List<ValueNamePair> docStatusRefs = MasterUtil.getZZDocStatus();
			
			linkedOrganisationsPo.stream().forEach(row -> {
				Map<String, Object> linkedOrganisation = new HashMap<>();
				linkedOrganisation.put(I_ZZSdfOrganisation_v.COLUMNNAME_OrgName, row.get(0));
				linkedOrganisation.put(I_ZZSdfOrganisation_v.COLUMNNAME_ZZ_SDL_No, row.get(1));
				linkedOrganisation.put(ListSdrOrgLinkVM.ROLE_key, row.get(2));
				
				String docStatus = row.get(3) == null ? X_ZZSdfOrganisation_v.ZZ_DOCSTATUS_Draft : row.get(3).toString();
				for (ValueNamePair docStatusRef : docStatusRefs) {
					String docStatusRefValue = docStatusRef.getValue();
					if (docStatusRefValue.equals(docStatus)) {
						linkedOrganisation.put(I_ZZSdfOrganisation_v.COLUMNNAME_ZZ_DocStatus, docStatusRef);
					}
				}				
				
				linkedOrganisation.put(I_ZZSdfOrganisation_v.COLUMNNAME_ZZSdfOrganisation_ID, row.get(4));
				
				linkedOrganisationsList.add(linkedOrganisation);
			});
			
			linkedOrganisations.clear();
			linkedOrganisations.addAll(linkedOrganisationsList);
		}
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
