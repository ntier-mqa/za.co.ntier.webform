package za.co.ntier.webform.sdr.viewmodel;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.adempiere.util.ProcessUtil;
import org.compiere.model.I_AD_Process;
import org.compiere.model.MPInstance;
import org.compiere.model.MProcess;
import org.compiere.model.MRole;
import org.compiere.model.MTable;
import org.compiere.model.Query;
import org.compiere.model.X_AD_Process;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.NamePair;
import org.compiere.util.Trx;
import org.compiere.util.ValueNamePair;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Filedownload;
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

	private static final String ROLE_SDP_ADMIN_UU = "b61dca59-07ad-46d6-8cf0-183588b3b9f2";
	private static final String ROLE_AGENT_ADMIN_UU = "5ece75f4-3d6f-4670-9d8f-c2cf864f834a";
	private static final Set<String> ALLOWED_PRINT_ROLE_UUS = Set.of(ROLE_SDP_ADMIN_UU, ROLE_AGENT_ADMIN_UU);
	private static final String PRINT_LETTER_PROCESS_VALUE = "PrintLearnerRegistrationLetter";
	private static final String PRINT_LETTER_PROCESS_CLASS = "za.co.ntier.learner.process.PrintLearnerRegistrationLetter";
	private static final String PARAM_LEARNER_IDS = "ZZLearner_IDs";

	private final Set<Integer> selectedLearnerIds = new LinkedHashSet<>();
	private boolean canPrintRegistrationLetter;
	
	private String searchFirstName;
	private String searchSurname;
	private String searchIdNumber;
	
	private int activePage = 0;
	private int pageSize = 20;
	private int totalSize = 0;
	
	@Init
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo){
		this.setMenuContextInfo(menuContextInfo);
		MRole currentRole = MRole.get(Env.getCtx(), Env.getAD_Role_ID(Env.getCtx()));
		String roleUU = currentRole != null ? currentRole.getAD_Role_UU() : null;
		this.canPrintRegistrationLetter = roleUU != null && ALLOWED_PRINT_ROLE_UUS.contains(roleUU);
		initList();
	}
	
	@Command
	@NotifyChange({"learners", "totalSize", "activePage", "allPageSelected"})
	public void cmdRefreshList() {
		initList();
	}
	
	@Command
	@NotifyChange({"learners", "totalSize", "activePage", "searchFirstName", "searchSurname", "searchIdNumber", "allPageSelected"})
	public void cmdClearSearch() {
		searchFirstName = null;
		searchSurname = null;
		searchIdNumber = null;
		initList();
	}
	
	@Command
	@NotifyChange({"learners", "allPageSelected"})
	public void cmdPaging(@ContextParam(ContextType.TRIGGER_EVENT) PagingEvent event) {
		this.activePage = event.getActivePage();
		fetchPage();
	}

	@Command
	@NotifyChange({"allPageSelected", "selectedCount", "printButtonLabel"})
	public void cmdToggleSelectLearner(@BindingParam("row") X_ZZLearner_v row,
			@BindingParam("checked") boolean checked) {
		if (!canPrintRegistrationLetter || row == null || row.getZZLearner_ID() <= 0) {
			return;
		}
		if (checked) {
			selectedLearnerIds.add(row.getZZLearner_ID());
		} else {
			selectedLearnerIds.remove(row.getZZLearner_ID());
		}
	}

	@Command
	@NotifyChange({"learners", "allPageSelected", "selectedCount", "printButtonLabel"})
	public void cmdToggleSelectAllPage(@BindingParam("checked") boolean checked) {
		if (!canPrintRegistrationLetter) {
			return;
		}
		for (X_ZZLearner_v row : learners) {
			if (row != null && row.getZZLearner_ID() > 0) {
				if (checked) {
					selectedLearnerIds.add(row.getZZLearner_ID());
				} else {
					selectedLearnerIds.remove(row.getZZLearner_ID());
				}
			}
		}
	}

	@Command
	@NotifyChange({"learners", "allPageSelected", "selectedCount", "printButtonLabel"})
	public void cmdClearSelection() {
		selectedLearnerIds.clear();
	}

	@Command
	public void cmdPrintRegistrationLetter() {
		if (!canPrintRegistrationLetter) {
			return;
		}
		if (selectedLearnerIds.isEmpty()) {
			showLetterMessage("Please select at least one learner to print the Registration Letter.");
			return;
		}

		String joinedIds = selectedLearnerIds.stream()
				.map(String::valueOf)
				.collect(Collectors.joining(","));

		Trx trx = Trx.get(Trx.createTrxName("PrintLearnerRegLetter"), true);
		try {
			ProcessInfo pi = createPrintProcessInfo(joinedIds);
			boolean success = ProcessUtil.startJavaProcess(Env.getCtx(), pi, trx);
			List<File> pdfFiles = extractPdfFiles(pi);
			if (success && !pi.isError() && !pdfFiles.isEmpty()) {
				downloadPdfFiles(pdfFiles);
				return;
			}
			String summary = pi.getSummary();
			showLetterMessage(summary != null && !summary.isBlank()
					? summary
					: "Could not generate the Registration Letter.");
		} catch (Exception e) {
			showLetterMessage("Error generating the Registration Letter: " + e.getMessage());
		} finally {
			trx.close();
		}
	}

	private static ProcessInfo createPrintProcessInfo(String learnerIdsCsv) {
		int processId = DB.getSQLValue(null,
				"SELECT " + X_AD_Process.COLUMNNAME_AD_Process_ID
				+ " FROM " + X_AD_Process.Table_Name
				+ " WHERE " + X_AD_Process.COLUMNNAME_Value + " = ? AND "
				+ I_AD_Process.COLUMNNAME_IsActive + " = 'Y'",
				PRINT_LETTER_PROCESS_VALUE);

		ProcessInfo pi;
		if (processId > 0) {
			MProcess proc = new MProcess(Env.getCtx(), processId, null);
			pi = new ProcessInfo(proc.getName(), processId);
			pi.setClassName(proc.getClassname());

			MPInstance instance = new MPInstance(Env.getCtx(), processId, 0, 0, null);
			instance.saveEx();
			pi.setAD_PInstance_ID(instance.getAD_PInstance_ID());
		} else {
			pi = new ProcessInfo("Print Learner Registration Letter", 0);
			pi.setClassName(PRINT_LETTER_PROCESS_CLASS);
		}
		pi.setAD_User_ID(Env.getAD_User_ID(Env.getCtx()));
		pi.setAD_Client_ID(Env.getAD_Client_ID(Env.getCtx()));
		pi.setParameter(new ProcessInfoParameter[] {
				new ProcessInfoParameter(PARAM_LEARNER_IDS, learnerIdsCsv, null, null, null)
		});
		return pi;
	}

	private static List<File> extractPdfFiles(ProcessInfo pi) {
		List<File> files = new ArrayList<>();
		Object obj = pi.getSerializableObject();
		if (obj instanceof List<?>) {
			for (Object item : (List<?>) obj) {
				if (item instanceof File file && file.exists()) {
					files.add(file);
				}
			}
		}
		if (files.isEmpty() && pi.getExportFile() != null && pi.getExportFile().exists()) {
			files.add(pi.getExportFile());
		}
		return files;
	}

	private static void downloadPdfFiles(List<File> pdfFiles) throws Exception {
		if (pdfFiles.size() == 1) {
			File file = pdfFiles.get(0);
			byte[] pdfBytes = Files.readAllBytes(file.toPath());
			Filedownload.save(pdfBytes, "application/pdf", file.getName());
			return;
		}

		// ZK's Filedownload.save reuses a single #zk_download iframe and cancels earlier files
		// when called multiple times in one response; stagger separate hidden iframes instead.
		Desktop desktop = Executions.getCurrent().getDesktop();
		StringBuilder js = new StringBuilder();
		for (int i = 0; i < pdfFiles.size(); i++) {
			File file = pdfFiles.get(i);
			byte[] pdfBytes = Files.readAllBytes(file.toPath());
			String fileName = file.getName();
			AMedia media = new AMedia(fileName, "pdf", "application/pdf", pdfBytes);
			String downloadUri = desktop.getDownloadMediaURI(media, "/" + fileName);
			String escapedUri = downloadUri.replace("\\", "\\\\").replace("'", "\\'");
			js.append("setTimeout(function(){")
					.append("var ifr=document.createElement('iframe');")
					.append("ifr.style.display='none';")
					.append("ifr.src='").append(escapedUri).append("';")
					.append("document.body.appendChild(ifr);")
					.append("setTimeout(function(){if(ifr.parentNode){ifr.parentNode.removeChild(ifr);}},60000);")
					.append("},").append(i * 300).append(");");
		}
		Clients.evalJavaScript(js.toString());
	}

	public boolean isLearnerSelected(X_ZZLearner_v row) {
		return row != null && row.getZZLearner_ID() > 0 && selectedLearnerIds.contains(row.getZZLearner_ID());
	}

	public boolean isAllPageSelected() {
		if (learners.isEmpty()) {
			return false;
		}
		boolean hasSelectableRow = false;
		for (X_ZZLearner_v row : learners) {
			if (row != null && row.getZZLearner_ID() > 0) {
				hasSelectableRow = true;
				if (!selectedLearnerIds.contains(row.getZZLearner_ID())) {
					return false;
				}
			}
		}
		return hasSelectableRow;
	}

	public int getSelectedCount() {
		return selectedLearnerIds.size();
	}

	public String getPrintButtonLabel() {
		return selectedLearnerIds.isEmpty()
				? "Print Registration Letter"
				: "Print Registration Letter (" + selectedLearnerIds.size() + ")";
	}

	public boolean isCanPrintRegistrationLetter() {
		return canPrintRegistrationLetter;
	}

	private static void showLetterMessage(String message) {
		MasterUtil.showInfoDialog("Registration Letter", List.of(message), null);
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
