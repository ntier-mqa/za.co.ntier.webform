package za.co.ntier.webform.sdr.viewmodel;

import org.compiere.model.MTable;
import org.compiere.model.MUser;
import org.compiere.model.Query;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.NamePair;
import org.compiere.util.ValueNamePair;
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
	private int loginId;
	
	@Init
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo){
		this.setMenuContextInfo(menuContextInfo);
		loginId = Env.getAD_User_ID(Env.getCtx());
		initList();
	}
	
	@Command
	public void cmdRefreshList() {
		initList();
	}
	
	public boolean showExtensionBt(X_ZZAssessorPerson_v row) {
		return X_ZZAssessorPerson_v.ZZ_DOCSTATUS_Approved.equals(row.getZZ_DocStatus());
		
	}
	
	public boolean showEditBt(X_ZZAssessorPerson_v row) {
		return X_ZZAssessorPerson_v.ZZ_DOCSTATUS_Draft.equals(row.getZZ_DocStatus());
	}
	
	
	@Command
	public void extensionAssessor(@BindingParam("row") X_ZZAssessorPerson_v row) {
		boolean isAssessor = X_ZZAssessorPerson_v.ZZASSESSORROLE_Assessor.equals(row.getZZAssessorRole());
		
		NamePair contextOpenFormModel = new ValueNamePair(MenuContextInfo.OpenFormModelNew, MenuContextInfo.OpenFormModelKey);
		NamePair contectAssessorPersonID = new KeyNamePair(row.getZZAssessorPerson_ID(), WebForm.recordIDMenuContextKeyNonPlus);
		
		if (isAssessor)
			MasterUtil.openFormByUU(AssessorRegistrationVM.assessorScopeFormUU
				, contextOpenFormModel
				, contectAssessorPersonID);
		else {
			MasterUtil.openFormByUU(AssessorRegistrationVM.moderatorScopeFormUU
					, contextOpenFormModel
					, contectAssessorPersonID);
		}
	}
	
	@Command
	public void editAssessor(@BindingParam("row") X_ZZAssessorPerson_v row) {
		boolean isAssessor = X_ZZAssessorPerson_v.ZZASSESSORROLE_Assessor.equals(row.getZZAssessorRole());
		boolean isScopeExtension = row.getParent_ID() > 0;

		NamePair contextOpenFormModel = new ValueNamePair(MenuContextInfo.OpenFormModelEdit, MenuContextInfo.OpenFormModelKey);
		NamePair contectAssessorPersonID = new KeyNamePair(row.getZZAssessorPerson_ID(), WebForm.recordIDMenuContextKeyNonPlus);
		
		if (isAssessor && isScopeExtension) {
			MasterUtil.openFormByUU(AssessorRegistrationVM.assessorScopeFormUU
					, contectAssessorPersonID
					, contextOpenFormModel);
		}else if (isAssessor) {
			MasterUtil.openFormByUU(AssessorRegistrationVM.assessorFormUU
					, contectAssessorPersonID
					, contextOpenFormModel);
		}else if (!isAssessor && isScopeExtension) {
			MasterUtil.openFormByUU(AssessorRegistrationVM.moderatorScopeFormUU
					, contectAssessorPersonID
					, contextOpenFormModel);
		}else {
			MasterUtil.openFormByUU(AssessorRegistrationVM.moderatorFormUU
					, contectAssessorPersonID
					, contextOpenFormModel);
		}
	}
	
	private void initList() {
		Query assessorPersonQuery = MTable.get(Env.getCtx(), X_ZZAssessorPerson_v.Table_Name).createQuery("", null);
		//assessorPersonQuery.setParameters(loginId);
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
