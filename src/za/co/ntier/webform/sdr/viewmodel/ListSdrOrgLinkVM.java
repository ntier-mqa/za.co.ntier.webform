package za.co.ntier.webform.sdr.viewmodel;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.List;
import java.util.Map;

import org.adempiere.util.ProcessUtil;
import org.compiere.model.I_C_BPartner;
import org.compiere.model.MPInstance;
import org.compiere.model.MProcess;
import org.compiere.model.X_AD_Process;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.DB;
import org.compiere.util.Trx;
import org.compiere.util.Env;
import org.compiere.util.ValueNamePair;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.Filedownload;
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

	/** AD_Process search key of za.co.ntier.wsp_atr.process.GenerateMandatoryGrantStatement (mqaplugin). */
	public static final String GRANT_STATEMENT_PROCESS = "MandatoryGrantStatementWeb";

	private boolean statementVisible;
	private Map<String, Object> statementRow;
	private Date statementDateFrom;
	private Date statementDateTo;

	public boolean isStatementAvailable(Map<String, Object> row) {
		ValueNamePair zzDocStatus = (ValueNamePair)row.get(I_ZZSdfOrganisation_v.COLUMNNAME_ZZ_DocStatus);
		return zzDocStatus != null && X_ZZSdfOrganisation_v.ZZ_DOCSTATUS_Approved.equals(zzDocStatus.getValue())
				&& row.get(I_ZZSdfOrganisation_v.COLUMNNAME_C_BPartner_ID) != null;
	}

	@Command
	@NotifyChange({"statementVisible", "statementOrgName", "statementDateFrom", "statementDateTo"})
	public void openStatement(@BindingParam("row") Map<String, Object> row) {
		statementRow = row;
		LocalDate today = LocalDate.now();
		// Default to the start of the current financial year (1 April).
		int fyStartYear = today.getMonthValue() >= 4 ? today.getYear() : today.getYear() - 1;
		statementDateFrom = toDate(LocalDate.of(fyStartYear, 4, 1));
		statementDateTo = toDate(today);
		statementVisible = true;
	}

	@Command
	@NotifyChange("statementVisible")
	public void cancelStatement() {
		statementVisible = false;
		statementRow = null;
	}

	@Command
	@NotifyChange("statementVisible")
	public void generateStatement() {
		if (statementRow == null)
			return;
		if (statementDateFrom == null || statementDateTo == null) {
			showStatementMessage("Please enter a Start Date and an End Date.");
			return;
		}
		if (statementDateFrom.after(statementDateTo)) {
			showStatementMessage("Start Date must be on or before End Date.");
			return;
		}

		int processId = DB.getSQLValue(null, "SELECT " + X_AD_Process.COLUMNNAME_AD_Process_ID + " FROM " + X_AD_Process.Table_Name
				+ " WHERE " + X_AD_Process.COLUMNNAME_Value + " = ? AND IsActive = 'Y'", GRANT_STATEMENT_PROCESS);
		if (processId <= 0) {
			showStatementMessage("Process not found! Please log in as System Administrator and create a Report & Process record with Search Key: "
					+ GRANT_STATEMENT_PROCESS + " and Classname: za.co.ntier.wsp_atr.process.GenerateMandatoryGrantStatement");
			return;
		}

		int bpartnerId = ((Number)statementRow.get(I_ZZSdfOrganisation_v.COLUMNNAME_C_BPartner_ID)).intValue();
		int bpartnerTableId = org.compiere.model.MTable.getTable_ID(I_C_BPartner.Table_Name);
		Timestamp from = new Timestamp(statementDateFrom.getTime());
		Timestamp to = new Timestamp(statementDateTo.getTime());

		Trx trx = null;
		try {
			MProcess proc = new MProcess(Env.getCtx(), processId, null);
			ProcessInfo pi = new ProcessInfo(proc.getName(), processId, bpartnerTableId, bpartnerId);
			pi.setClassName(proc.getClassname());
			pi.setAD_User_ID(Env.getAD_User_ID(Env.getCtx()));
			pi.setAD_Client_ID(Env.getAD_Client_ID(Env.getCtx()));
			pi.setParameter(new ProcessInfoParameter[] {
					new ProcessInfoParameter("DateFrom", from, null, null, null),
					new ProcessInfoParameter("DateTo", to, null, null, null) });

			MPInstance instance = new MPInstance(Env.getCtx(), processId, bpartnerTableId, bpartnerId, null);
			instance.saveEx();
			pi.setAD_PInstance_ID(instance.getAD_PInstance_ID());

			trx = Trx.get(Trx.createTrxName("GrantStmt"), true);
			boolean success = ProcessUtil.startJavaProcess(Env.getCtx(), pi, trx);
			if (success && !pi.isError() && pi.getExportFile() != null) {
				SimpleDateFormat fmt = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
				String fileName = String.format("%s - %s - Grant Statement - %s - %s.pdf",
						statementRow.get(I_ZZSdfOrganisation_v.COLUMNNAME_OrgName),
						statementRow.get(I_ZZSdfOrganisation_v.COLUMNNAME_ZZ_SDL_No),
						fmt.format(statementDateFrom), fmt.format(statementDateTo))
						.replaceAll("[\\\\/:*?\"<>|]", "");
				byte[] pdfBytes = Files.readAllBytes(pi.getExportFile().toPath());
				Filedownload.save(pdfBytes, "application/pdf", fileName);
				statementVisible = false;
				statementRow = null;
			} else {
				showStatementMessage("Could not generate the statement: " + pi.getSummary());
			}
		} catch (Exception e) {
			showStatementMessage("Error generating the statement: " + e.getMessage());
		} finally {
			if (trx != null)
				trx.close();
		}
	}

	private static void showStatementMessage(String message) {
		MasterUtil.showInfoDialog("Grant Statement", List.of(message), null);
	}

	private static Date toDate(LocalDate date) {
		return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	public boolean isStatementVisible() {
		return statementVisible;
	}

	public String getStatementOrgName() {
		return statementRow == null ? "" : String.valueOf(statementRow.get(I_ZZSdfOrganisation_v.COLUMNNAME_OrgName));
	}

	public Date getStatementDateFrom() {
		return statementDateFrom;
	}

	public void setStatementDateFrom(Date statementDateFrom) {
		this.statementDateFrom = statementDateFrom;
	}

	public Date getStatementDateTo() {
		return statementDateTo;
	}

	public void setStatementDateTo(Date statementDateTo) {
		this.statementDateTo = statementDateTo;
	}
	
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
		return true;
	}
	
	@Command
	public void editLinkedOrganisation(@BindingParam("row") Map<String, Object> row) {		
		MasterUtil.openFormByMenu(MasterUtil.SDRRegistryOrgLinkUU, Integer.valueOf(((BigDecimal)row.get("ZZSdfOrganisation_ID")).intValue()));
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
		MasterUtil.openFormByMenu(MasterUtil.SDRMaintainOrganisationUU, Integer.valueOf(((BigDecimal)row.get("ZZSdfOrganisation_ID")).intValue()));
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
				    	%s,
				    	%s
			    FROM %s
			    WHERE %s = ? AND %s IN ('%s', '%s',  '%s')
			    ORDER BY %s
				""",
				I_ZZSdfOrganisation_v.COLUMNNAME_DocumentNo
				, I_ZZSdfOrganisation_v.COLUMNNAME_OrgName
				, I_ZZSdfOrganisation_v.COLUMNNAME_ZZ_SDL_No
				, I_ZZSdfOrganisation_v.COLUMNNAME_ZZSdfRoleType
				, ROLE_key
				, I_ZZSdfOrganisation_v.COLUMNNAME_ZZ_DocStatus
				, I_ZZSdfOrganisation_v.COLUMNNAME_ZZSdfOrganisation_ID
				, I_ZZSdfOrganisation_v.COLUMNNAME_ZZMaintainStatus
				, I_ZZSdfOrganisation_v.COLUMNNAME_C_BPartner_ID
				, I_ZZSdfOrganisation_v.Table_Name
				, I_ZZSdfOrganisation_v.COLUMNNAME_CreatedBy
				, I_ZZSdfOrganisation_v.COLUMNNAME_ZZ_DocStatus
				, X_ZZSdfOrganisation_v.ZZ_DOCSTATUS_Draft
				, X_ZZSdfOrganisation_v.ZZ_DOCSTATUS_Approved
				, X_ZZSdfOrganisation_v.ZZ_DOCSTATUS_Pending
				, I_ZZSdfOrganisation_v.COLUMNNAME_OrgName
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
				linkedOrganisation.put(I_ZZSdfOrganisation_v.COLUMNNAME_C_BPartner_ID, row.get(7));
				
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
