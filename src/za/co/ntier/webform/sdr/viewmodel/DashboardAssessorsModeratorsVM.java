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
	
	@Command
	public void editAssessor(@BindingParam("row") X_ZZAssessorPerson_v row) {
		if (X_ZZAssessorPerson_v.ZZASSESSORROLE_Assessor.equals(row.getZZAssessorRole())) {
			MasterUtil.openFormByUU("53779b2b-eb44-4f13-8c03-a8407c3fceae", row.getZZAssessorPerson_ID());
		}else if (X_ZZAssessorPerson_v.ZZASSESSORROLE_Moderator.equals(row.getZZAssessorRole())) {
			MasterUtil.openFormByUU(AssessorRegistrationVM.moderatorFormUU, row.getZZAssessorPerson_ID());
		}else {
			MasterUtil.openFormByUU(AssessorRegistrationVM.assessorFormUU, row.getZZAssessorPerson_ID());
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
