package za.co.ntier.webform.form.viewmodel;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.adempiere.webui.desktop.DefaultDesktop;
import org.adempiere.webui.session.SessionManager;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.Query;
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
    
    public String getOrgName(X_ZZ_Application_Form app) {
        if (app == null || app.getAD_Org_ID() <= 0) return "";
        PO p = MTable.get(Env.getCtx(), "AD_Org")
                     .getPO(app.getAD_Org_ID(), null);
        if (p == null) return "";
        String name = p.get_ValueAsString("Name");
        return (name != null && !name.isEmpty()) ? name : "";
    }

    
	
}
