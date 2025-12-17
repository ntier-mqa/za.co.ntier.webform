package za.co.ntier.webform.form.viewmodel;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.adempiere.webui.desktop.DefaultDesktop;
import org.adempiere.webui.desktop.IDesktop;
import org.adempiere.webui.session.SessionManager;
import org.compiere.model.I_AD_Menu;
import org.compiere.model.MBPartner;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.model.X_AD_Menu;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Util;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

import za.co.ntier.api.model.I_ZZ_Application_Form;
import za.co.ntier.api.model.X_ZZ_Application_Form;
import za.co.ntier.api.model.X_ZZ_Program_Master_Data;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.MenuContextInfo;

public class ApplicationsListVM {

	private ListModelList<X_ZZ_Application_Form> applications;
	
	  // NEW: one open window per application (keyed by Application UU)
    private final Map<String, Integer> openAppWindows = new HashMap<>();

	//In the class:
	private final Map<Integer, Boolean> editableCache = new HashMap<>();

	
	
	@Command
	public void editSelected(@BindingParam("application") X_ZZ_Application_Form app) {
	    if (app == null) return;

	    // 1) If already open, just notify and exit
	    if (activateExistingWindow(app)) {
	        return;
	    }

	    // 2) Resolve menu as you already do
	    X_ZZ_Program_Master_Data programDefine =
	            new X_ZZ_Program_Master_Data(
	                    Env.getCtx(),
	                    app.getZZ_Program_Master_Data_ID(),
	                    null
	            );

	    Query menuQuery = MTable.get(I_AD_Menu.Table_ID).createQuery(
	            String.format(
	                    "%s = ? AND %s LIKE ? AND %s LIKE ?",
	                    I_AD_Menu.COLUMNNAME_AD_Form_ID,
	                    I_AD_Menu.COLUMNNAME_PredefinedContextVariables,
	                    I_AD_Menu.COLUMNNAME_PredefinedContextVariables
	            ),
	            null
	    );

	    menuQuery.setParameters(
	            1000000,
	            "%" + programDefine.getZZ_Program_Master_Data_UU() + "%",
	            "%" + app.getZZProgramType() + "%"
	    );

	    X_AD_Menu menu = menuQuery.first();
	    if (menu == null) {
	        Clients.showNotification(
	                "No menu configuration found for this application.",
	                Clients.NOTIFICATION_TYPE_WARNING,
	                null,
	                "top_center",
	                3000
	        );
	        return;
	    }

	    // 3) Open form once
	    DefaultDesktop desktop = (DefaultDesktop) SessionManager.getAppDesktop();
	    MasterUtil.openForm(
	            menu.getAD_Menu_UU(),
	            I_ZZ_Application_Form.COLUMNNAME_ZZ_Application_Form_UU,
	            app.getZZ_Application_Form_UU()
	    );
/*
	    // 4) Record which window number we got back
	    if (desktop != null) {
	        Component activeWin = desktop.getActiveWindow();
	        if (activeWin != null) {
	            Object winNoAttr = activeWin.getAttribute(IDesktop.WINDOWNO_ATTRIBUTE);
	            if (winNoAttr instanceof Integer) {
	                String appKey = app.getZZ_Application_Form_UU();
	                if (!Util.isEmpty(appKey, true)) {
	                    openAppWindows.put(appKey, (Integer) winNoAttr);
	                }
	            }
	        }
	    }
	    */
	 // 4) Record which window number we got back (AFTER it opens)
	    if (desktop != null) {
	        Executions.schedule(desktop.getComponent().getDesktop(), evt -> {
	            Component activeWin = desktop.getActiveWindow();
	            if (activeWin != null) {
	                Object winNoAttr = activeWin.getAttribute(IDesktop.WINDOWNO_ATTRIBUTE);
	                if (winNoAttr instanceof Integer) {
	                    String appKey = app.getZZ_Application_Form_UU();
	                    if (!Util.isEmpty(appKey, true)) {
	                        openAppWindows.put(appKey, (Integer) winNoAttr);
	                    }
	                }
	            }
	        }, new org.zkoss.zk.ui.event.Event("onAfterOpenApp"));
	    }

	}

	// IMPORTANT: return ListModel so <grid model="..."> works
	public ListModel<X_ZZ_Application_Form> getApplications() {
		return applications;
	}

	

	@Init
	public void init(@ExecutionArgParam("menuContextInfo") MenuContextInfo menuContextInfo) {
		//this.menuContextInfo = menuContextInfo;

		// default empty model (avoid NPEs in ZUL)
		this.applications = new ListModelList<>(Collections.emptyList());

		if (menuContextInfo == null || menuContextInfo.getProgramMasterData() == null) {
			return;
		}

		int userId = Env.getAD_User_ID(Env.getCtx());
		// int programMasterDataId = menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID();

		// Query all application forms created by this user for this program
		// String where = "CreatedBy=? AND ZZ_Program_Master_Data_ID=?";
		String where = "CreatedBy=? ";
		List<X_ZZ_Application_Form> loaded = new Query(
				Env.getCtx(),
				X_ZZ_Application_Form.Table_Name,
				where,
				null)
				//  .setParameters(userId, programMasterDataId)
				.setParameters(userId)
				.setClient_ID()
				.setOnlyActiveRecords(true)
				.setOrderBy("AD_Org_ID,DocumentNo")
				.list();

		this.applications = new ListModelList<>(loaded);
	}

	public String programTitle(X_ZZ_Application_Form app) {
		if (app == null || app.getZZ_Program_Master_Data_ID() == 0) return "";
		PO p = MTable.get(Env.getCtx(), "ZZ_Program_Master_Data")
				.getPO(app.getZZ_Program_Master_Data_ID(), null);
		if (p == null) return "";
		String title = p.get_ValueAsString("Title");
		return (title != null && !title.isEmpty()) ? title : "";
	}    


	/**
	 * Organisation column:
	 * 1) Take SDL from the application (ZZ_SDL_No)
	 * 2) Find C_BPartner where Value = SDL (client-scoped)
	 * 3) Use that BP's AD_Org_ID to fetch AD_Org.Name
	 */
	public String getOrgName(X_ZZ_Application_Form app) {
		if (app == null) return "";
		String name = "";
		if (app.getC_BPartner_ID() > 0)  {
			MBPartner bp = (MBPartner) app.getC_BPartner();
			name = (bp != null && bp.getName() != null) ? bp.getName() : "";
		} else {
			name = app.getOrgName();
		}
		return name;
	}

	// in ApplicationsListVM

	// constant for your list
	private static final int DOCSTATUS_REF_ID = 1000006;

	// cache: code -> name (e.g., "DR" -> "Draft")
	private final Map<String, String> docStatusNameCache = new HashMap<>();

	public String getStatusName(X_ZZ_Application_Form app) {
		if (app == null) return "";
		String code = app.getZZ_DocStatus();
		if (code == null || code.isEmpty()) return "";

		String cached = docStatusNameCache.get(code);
		if (cached != null) return cached;

		PO refList = new Query(Env.getCtx(), "AD_Ref_List",
				"AD_Reference_ID=? AND Value=? AND AD_Client_ID=?", null)
				.setParameters(DOCSTATUS_REF_ID, code,0)
				.first();

		String name = (refList != null) ? refList.get_ValueAsString("Name") : "";
		if (name == null) name = "";

		docStatusNameCache.put(code, name);
		return name;
	}
	/**
	 * Only editable when:
	 *  - app.isActive() == true
	 *  - ZZ_DocStatus == 'AP'
	 *  - app.DateDoc between StartDate and EndDate of some active ZZ_Open_Application
	 *  - app.ZZ_Program_Master_Data_ID is in that ZZ_Open_Application.ZZ_Programs CSV
	 */
	public boolean canEdit(X_ZZ_Application_Form app) {
		if (app == null) return false;

		final int appId = app.getZZ_Application_Form_ID();
//editableCache.put(appId, true);
		Boolean cached = editableCache.get(appId);
		if (cached != null) return cached;

		// Basic guards
		if (!app.isActive()) { editableCache.put(appId, false); return false; }

		Timestamp dateDoc = app.getDateDoc();
		if (dateDoc == null) { editableCache.put(appId, false); return false; }

		int programId = app.getZZ_Program_Master_Data_ID();
		if (programId <= 0) { editableCache.put(appId, false); return false; }

		// Check ZZ_Open_Application: active window covering dateDoc AND containing programId in CSV
		// Using POSITION with comma guards to match whole IDs safely
		String sql =
				"SELECT 1 " +
						"FROM zz_open_application oa " +
						"WHERE oa.isactive='Y' " +
						"  AND oa.zz_programs IS NOT NULL " +
						"  AND now() BETWEEN oa.startdate AND oa.enddate " +
						"  AND POSITION(',' || ? || ',' IN ',' || oa.zz_programs || ',') > 0 " +
						"  AND oa.zz_docstatus = 'AP'" +
						"LIMIT 1";

		int found = DB.getSQLValueEx(null, sql,  String.valueOf(programId));
		boolean ok = (found > 0); // 1 if exists; -1/0 means no match
		editableCache.put(appId, ok);
		return ok;
	}
	
	private boolean activateExistingWindow(X_ZZ_Application_Form app) {
	    DefaultDesktop desktop = (DefaultDesktop) SessionManager.getAppDesktop();
	    if (desktop == null || app == null) return false;

	    String appKey = app.getZZ_Application_Form_UU();
	    if (Util.isEmpty(appKey, true)) return false;

	    Integer winNo = openAppWindows.get(appKey);
	    if (winNo == null) return false;

	    Object winObj = desktop.findWindow(winNo);
	    if (winObj instanceof Component) {
	        Component c = (Component) winObj;

	        // IMPORTANT: only "open" if still attached
	        boolean alive = c.getDesktop() != null && c.getPage() != null;
	        if (alive) {
	            Clients.showNotification("This application is already open in another tab.",
	                    Clients.NOTIFICATION_TYPE_INFO, null, "top_center", 3000);
	            return true;
	        }
	    }

	    // stale
	    openAppWindows.remove(appKey);
	    return false;
	}
}
