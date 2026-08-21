package za.co.ntier.webform.sdr.viewmodel;

import java.util.ArrayList;
import java.util.List;

import org.compiere.model.MTable;
import org.compiere.model.Query;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.NamePair;
import org.compiere.util.ValueNamePair;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.event.PagingEvent;

import za.co.ntier.api.model.X_ZZLearner_v;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;

public class DashboardLearnerVM {
	private MenuContextInfo menuContextInfo;
	private ListModelList<X_ZZLearner_v> learners = new ListModelList<X_ZZLearner_v>();
	
	public static final String learnerAssessmentFormUU = "c941f349-102f-47fe-9e2e-a7be864830b1";
	
	private String searchFirstName;
	private String searchSurname;
	private String searchIdNumber;
	
	private int activePage = 0;
	private int pageSize = 20;
	private int totalSize = 0;
	
	@Init
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo){
		this.setMenuContextInfo(menuContextInfo);
		initList();
	}
	
	@Command
	@NotifyChange({"learners", "totalSize", "activePage"})
	public void cmdRefreshList() {
		initList();
	}
	
	@Command
	@NotifyChange({"learners", "totalSize", "activePage", "searchFirstName", "searchSurname", "searchIdNumber"})
	public void cmdClearSearch() {
		searchFirstName = null;
		searchSurname = null;
		searchIdNumber = null;
		initList();
	}
	
	@Command
	@NotifyChange("learners")
	public void cmdPaging(@ContextParam(ContextType.TRIGGER_EVENT) PagingEvent event) {
		this.activePage = event.getActivePage();
		fetchPage();
	}
	
	@Command
	public void zoomLearner(@BindingParam("row") X_ZZLearner_v row) {
		NamePair contextOpenFormModel = new ValueNamePair(MenuContextInfo.OpenFormModelEdit, MenuContextInfo.OpenFormModelKey);
		NamePair contextLearnerID = new KeyNamePair(row.getZZLearner_ID(), WebForm.recordIDMenuContextKeyNonPlus);
		MasterUtil.openFormByUU(learnerAssessmentFormUU, contextLearnerID, contextOpenFormModel);
	}
	
	private void initList() {
		activePage = 0;
		Query countQuery = buildQuery();
		try {
			totalSize = countQuery.count();
		} catch (Exception e) {
			totalSize = 0;
		}
		fetchPage();
	}
	
	private void fetchPage() {
		Query learnerQuery = buildQuery();
		learnerQuery.setOrderBy("Created DESC");
		learnerQuery.setPage(pageSize, activePage);
		
		learners.clear();
		learners.addAll(learnerQuery.list());
	}
	
	private Query buildQuery() {
		StringBuilder whereClause = new StringBuilder("IsActive='Y'");
		List<Object> params = new ArrayList<>();
		
		if (searchFirstName != null && !searchFirstName.trim().isEmpty()) {
			whereClause.append(" AND UPPER(ZZFirstName) LIKE UPPER(?)");
			params.add("%" + searchFirstName.trim() + "%");
		}
		
		if (searchSurname != null && !searchSurname.trim().isEmpty()) {
			whereClause.append(" AND UPPER(Surname) LIKE UPPER(?)");
			params.add("%" + searchSurname.trim() + "%");
		}
		
		if (searchIdNumber != null && !searchIdNumber.trim().isEmpty()) {
			whereClause.append(" AND UPPER(ZZ_ID_Passport_No) LIKE UPPER(?)");
			params.add("%" + searchIdNumber.trim() + "%");
		}
		
		return MTable.get(Env.getCtx(), X_ZZLearner_v.Table_Name)
				.createQuery(whereClause.toString(), null)
				.setParameters(params);
	}

	public MenuContextInfo getMenuContextInfo() {
		return menuContextInfo;
	}

	public void setMenuContextInfo(MenuContextInfo menuContextInfo) {
		this.menuContextInfo = menuContextInfo;
	}

	public ListModelList<X_ZZLearner_v> getLearners() {
		return learners;
	}

	public void setLearners(ListModelList<X_ZZLearner_v> learners) {
		this.learners = learners;
	}

	public String getSearchFirstName() {
		return searchFirstName;
	}

	public void setSearchFirstName(String searchFirstName) {
		this.searchFirstName = searchFirstName;
	}

	public String getSearchSurname() {
		return searchSurname;
	}

	public void setSearchSurname(String searchSurname) {
		this.searchSurname = searchSurname;
	}

	public String getSearchIdNumber() {
		return searchIdNumber;
	}

	public void setSearchIdNumber(String searchIdNumber) {
		this.searchIdNumber = searchIdNumber;
	}
	
	public int getActivePage() {
		return activePage;
	}

	public void setActivePage(int activePage) {
		this.activePage = activePage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}
}
