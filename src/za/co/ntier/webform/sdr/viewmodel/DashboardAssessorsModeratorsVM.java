package za.co.ntier.webform.sdr.viewmodel;

import org.compiere.model.MTable;
import org.compiere.model.Query;
import org.compiere.util.Env;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zul.ListModelList;

import za.co.ntier.api.model.MZZAssessorPersonV;
import za.co.ntier.api.model.X_ZZAssessorPerson_v;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;

public class DashboardAssessorsModeratorsVM {
	private MenuContextInfo menuContextInfo;
	private ListModelList<MZZAssessorPersonV> assessors = new ListModelList<MZZAssessorPersonV>();
	
	@Init
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo){
		this.setMenuContextInfo(menuContextInfo);
		
		initList();
	}
	
	@Command
	public void cmdRefreshList() {
		initList();
	}
	
	public String getBtLabel(X_ZZAssessorPerson_v row) {
		boolean isApprove = X_ZZAssessorPerson_v.ZZ_DOCSTATUS_Pending.equals(row.getZZ_DocStatus()) || 
							X_ZZAssessorPerson_v.ZZ_DOCSTATUS_Approved.equals(row.getZZ_DocStatus());
		
		if (isApprove) {
			return "Scope Extension";
		}else {
			return "Edit";
		}
	}
	
	@Command
	public void editAssessor(@BindingParam("row") X_ZZAssessorPerson_v row) {
		boolean isAssessor = X_ZZAssessorPerson_v.ZZASSESSORROLE_Assessor.equals(row.getZZAssessorRole());
		boolean isDraft =  X_ZZAssessorPerson_v.ZZ_DOCSTATUS_Draft.equals(row.getZZ_DocStatus());
		boolean isScopeExtension = row.getParent_ID() > 0;
		boolean isApprove = X_ZZAssessorPerson_v.ZZ_DOCSTATUS_Pending.equals(row.getZZ_DocStatus()) || 
							X_ZZAssessorPerson_v.ZZ_DOCSTATUS_Approved.equals(row.getZZ_DocStatus());
		
		if ((isAssessor && isScopeExtension && isDraft) 
				|| (isApprove && isAssessor)){
			
			MasterUtil.openFormByUU(AssessorRegistrationVM.assessorScopeFormUU, row.getZZAssessorPerson_ID());
		}else if ((!isAssessor && isScopeExtension && isDraft) 
				|| (isApprove && !isAssessor)){
			
			MasterUtil.openFormByUU(AssessorRegistrationVM.moderatorScopeFormUU, row.getZZAssessorPerson_ID());
		}else if (isAssessor && !isScopeExtension && isDraft) {
			MasterUtil.openFormByUU(AssessorRegistrationVM.assessorFormUU, row.getZZAssessorPerson_ID());
		}else if (!isAssessor && !isScopeExtension && isDraft) {
			MasterUtil.openFormByUU(AssessorRegistrationVM.moderatorFormUU, row.getZZAssessorPerson_ID());
		}else {
			
		}
	}
	
	private void initList() {
		Query assessorPersonQuery = MTable.get(Env.getCtx(), X_ZZAssessorPerson_v.Table_Name).createQuery(null, null);
		assessors.clear();
		assessors.addAll(assessorPersonQuery.list());
		
	}

	public MenuContextInfo getMenuContextInfo() {
		return menuContextInfo;
	}

	public void setMenuContextInfo(MenuContextInfo menuContextInfo) {
		this.menuContextInfo = menuContextInfo;
	}

	public ListModelList<MZZAssessorPersonV> getAssessors() {
		return assessors;
	}

	public void setAssessors(ListModelList<MZZAssessorPersonV> assessors) {
		this.assessors = assessors;
	}
}
