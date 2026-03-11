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

import com.google.common.base.Objects;

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
		isUnlink = isUnlink && (row.get(ListSdrOrgLinkVM.ROLE_key).equals(X_ZZSdfOrganisation_v.ZZSDFROLETYPE_Secondary)
				|| row.get(ListSdrOrgLinkVM.ROLE_key).equals(X_ZZSdfOrganisation_v.ZZSDFROLETYPE_Primary));
		
		return isUnlink;
	}
	
	public boolean isEditRequest(Map<String, Object> row) {
		ValueNamePair zzDocStatus = (ValueNamePair)row.get(I_ZZSdfOrganisation_v.COLUMNNAME_ZZ_DocStatus);
		return X_ZZSdfOrganisation_v.ZZ_DOCSTATUS_Draft.equals(zzDocStatus.getValue());
	}
	
	public String getSclassOrgMaintain(Map<String, Object> row) {
		Object maintainStatus = row.get(I_ZZSdfOrganisation_v.COLUMNNAME_ZZMaintainStatus);
		return Objects.equal(maintainStatus, X_ZZSdfOrganisation_v.ZZMAINTAINSTATUS_Yes)?"maintained":"unmaintain";
	}
	
	public boolean isOrgEditable(Map<String, Object> row) {
		ValueNamePair zzDocStatus = (ValueNamePair)row.get(I_ZZSdfOrganisation_v.COLUMNNAME_ZZ_DocStatus);
		boolean isOrgEditable = X_ZZSdfOrganisation_v.ZZ_DOCSTATUS_Approved.equals(zzDocStatus.getValue())
				|| X_ZZSdfOrganisation_v.ZZ_DOCSTATUS_Pending.equals(zzDocStatus.getValue());
		isOrgEditable = isOrgEditable && row.get(ListSdrOrgLinkVM.ROLE_key).equals(X_ZZSdfOrganisation_v.ZZSDFROLETYPE_Primary);
		return isOrgEditable;
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
	
	@Command
	public void cmdRefreshList() {
		initList();
	}
	
	@Init
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo){
		this.setMenuContextInfo(menuContextInfo);
		initList();
	}
	
	public final static String ROLE_key = "role";
	
	private void initList () {
		List<List<Object>> linkedOrganisationsPo = DB.getSQLArrayObjectsEx(null, String.format("""
				SELECT 	%s,
						%s, 
						%s, 
					   	%s AS %s,
				    	%s,
				    	%s,
				    	%s
			    FROM %s
			    WHERE %s = ? AND %s IN ('%s', '%s',  '%s')
				""", 
				I_ZZSdfOrganisation_v.COLUMNNAME_DocumentNo	
				, I_ZZSdfOrganisation_v.COLUMNNAME_OrgName
				, I_ZZSdfOrganisation_v.COLUMNNAME_ZZ_SDL_No
				, I_ZZSdfOrganisation_v.COLUMNNAME_ZZSdfRoleType
				, ROLE_key
				, I_ZZSdfOrganisation_v.COLUMNNAME_ZZ_DocStatus
				, I_ZZSdfOrganisation_v.COLUMNNAME_ZZSdfOrganisation_ID
				, I_ZZSdfOrganisation_v.COLUMNNAME_ZZMaintainStatus
				, I_ZZSdfOrganisation_v.Table_Name
				, I_ZZSdfOrganisation_v.COLUMNNAME_CreatedBy
				, I_ZZSdfOrganisation_v.COLUMNNAME_ZZ_DocStatus
				, X_ZZSdfOrganisation_v.ZZ_DOCSTATUS_Draft
				, X_ZZSdfOrganisation_v.ZZ_DOCSTATUS_Approved
				, X_ZZSdfOrganisation_v.ZZ_DOCSTATUS_Pending
				), Env.getAD_User_ID(Env.getCtx()));
		
		if (linkedOrganisationsPo == null) {
			linkedOrganisations.clear();
		}else {
			List<Map<String, Object>> linkedOrganisationsList = new java.util.ArrayList<>();
			final List<ValueNamePair> docStatusRefs = MasterUtil.getZZDocStatus();
			
			linkedOrganisationsPo.stream().forEach(row -> {
				Map<String, Object> linkedOrganisation = new HashMap<>();
				linkedOrganisation.put(I_ZZSdfOrganisation_v.COLUMNNAME_DocumentNo, row.get(0));
				linkedOrganisation.put(I_ZZSdfOrganisation_v.COLUMNNAME_OrgName, row.get(1));
				linkedOrganisation.put(I_ZZSdfOrganisation_v.COLUMNNAME_ZZ_SDL_No, row.get(2));
				linkedOrganisation.put(ListSdrOrgLinkVM.ROLE_key, row.get(3));
				
				String docStatus = row.get(3) == null ? X_ZZSdfOrganisation_v.ZZ_DOCSTATUS_Draft : row.get(4).toString();
				for (ValueNamePair docStatusRef : docStatusRefs) {
					String docStatusRefValue = docStatusRef.getValue();
					if (docStatusRefValue.equals(docStatus)) {
						linkedOrganisation.put(I_ZZSdfOrganisation_v.COLUMNNAME_ZZ_DocStatus, docStatusRef);
					}
				}				
				
				linkedOrganisation.put(I_ZZSdfOrganisation_v.COLUMNNAME_ZZSdfOrganisation_ID, row.get(5));
				linkedOrganisation.put(I_ZZSdfOrganisation_v.COLUMNNAME_ZZMaintainStatus, row.get(6));
				
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
