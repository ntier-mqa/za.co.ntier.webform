package za.co.ntier.webform.form.viewmodel;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.adempiere.webui.desktop.DefaultDesktop;
import org.adempiere.webui.session.SessionManager;
import org.compiere.model.MBPartner;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.model.I_ZZ_Application_Form;
import za.co.ntier.webform.model.I_ZZ_Program_Master_Data;
import za.co.ntier.webform.model.X_ZZ_Application_Form;
import za.co.ntier.webform.model.X_ZZ_Program_Master_Data;

public class ApplicationsListVM {

	private ListModelList<X_ZZ_Application_Form> applications;
	private MenuContextInfo menuContextInfo;
	// Simple cache: SDL -> Org Name (avoids repeated queries while the list is shown)
	private final Map<String, String> orgNameBySdlCache = new HashMap<>();

	//In the class:
	private final Map<Integer, Boolean> editableCache = new HashMap<>();

	@Command
	public void editSelected(@BindingParam("application") X_ZZ_Application_Form app) {
		if (app == null) return;

		X_ZZ_Program_Master_Data programDefine = new X_ZZ_Program_Master_Data(Env.getCtx(), app.getZZ_Program_Master_Data_ID(), null);

		Map<String,Object> args = new HashMap<>();
		args.put("applicationId", app.getZZ_Application_Form_ID());
		args.put("menuContextInfo", menuContextInfo);

		String menuContext = "zulPath=/za/co/ntier/webform/zul/discretionaryGrantsApplicationProgram.zul\n"
				+ "formTitle=" + programDefine.getTitle() + "\n"
				+ I_ZZ_Program_Master_Data.COLUMNNAME_ZZ_Program_Master_Data_UU + "=" + programDefine.getZZ_Program_Master_Data_UU() + "\n"
				+ "programType=" + app.getZZProgramType() + "\n"
				+ I_ZZ_Application_Form.COLUMNNAME_ZZ_Application_Form_UU + "=" + app.getZZ_Application_Form_UU();

		DefaultDesktop desktop = (DefaultDesktop) SessionManager.getAppDesktop();
		desktop.setPredefinedContextVariables(menuContext);
		desktop.openForm(1000000);
	}

	// IMPORTANT: return ListModel so <grid model="..."> works
	public ListModel<X_ZZ_Application_Form> getApplications() {
		return applications;
	}


	@Init
	public void init(@ExecutionArgParam("menuContextInfo") MenuContextInfo menuContextInfo) {
		this.menuContextInfo = menuContextInfo;

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

		String sdl = app.getZZ_SDL_No();
		if (sdl == null || sdl.trim().isEmpty()) return "";

		// cache
		String cached = orgNameBySdlCache.get(sdl);
		if (cached != null) return cached;

		// Find BP by Value (SDL)
		MBPartner bp = new Query(Env.getCtx(), MBPartner.Table_Name, "Value=?", null)
				.setParameters(sdl.trim())
				.setClient_ID()          // respect AD_Client_ID
				.firstOnly();

		if (bp == null) {
			orgNameBySdlCache.put(sdl, "");
			return "";
		}




		String name = (bp != null && bp.getName() != null) ? bp.getName() : "";

		orgNameBySdlCache.put(sdl, name);
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
}
