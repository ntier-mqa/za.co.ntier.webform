package za.co.ntier.webform.form.viewmodel.component;

import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.desktop.DefaultDesktop;
import org.adempiere.webui.exception.ApplicationException;
import org.adempiere.webui.session.SessionManager;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import za.co.ntier.webform.form.bean.component.Dialog;

/**
 * If super has an init method but its ChildViewModel doesn't, you can
 * add @Init(superclass=true) on the ChildViewModel to use super's init.
 */
@Init(superclass = true)
public class DialogVMWrapper extends ComponentVMWrapper<Dialog> {
	private static final int EMPLOYER_APP_AD_FORM_ID = 1000000; // your WebForm AD_Form_ID
	private String moreInfo;
	
	@Command
	public void closeDialog() {
		getComponent().setVisible(false);
		BindUtils.postNotifyChange(this.getComponent(), "visible");
	}

	/**
	 * @return the moreInfo
	 */
	public String getMoreInfo() {
		return moreInfo;
	}


	@Command
	@NotifyChange("visible")
	public void openAppForm() {
		try {
			AEnv.zoom(getComponent().getTableId(), getComponent().getRecordId());
			closeDialog();
		}catch (ApplicationException e) {
			moreInfo = e.getMessage();
			BindUtils.postNotifyChange(this, "moreInfo");
		}	
	}

	/**
	 * @param moreInfo the moreInfo to set
	 */
	public void setMoreInfo(String moreInfo) {
		this.moreInfo = moreInfo;
	}
	
	 

	@Command("closeDialogAndOpenList")
    public void closeDialogAndOpenList() {
        // Close the modal via the bound component's 'visible' property
        if (getComponent() != null) {
            getComponent().setVisible(false);
            BindUtils.postNotifyChange(null, null, getComponent(), "visible");
        }
        
        // 2) close the CURRENT tab first
        DefaultDesktop desktop = (DefaultDesktop) SessionManager.getAppDesktop();
        desktop.closeActiveWindow();  // <— closes the active AD Form tab

        // Open ApplicationsList.zul via WebForm context
        String ctx = ""
            + "zulPath=/za/co/ntier/webform/zul/program/ApplicationsList.zul\n"
            + "formTitle=Applications\n"
            + "ZZ_Program_Master_Data_UU=a3db65ee-97d9-429d-9734-aca9e89dd3af\n"
            + "programType=UNKNOWN\n";

       
        desktop.setPredefinedContextVariables(ctx);
        desktop.openForm(EMPLOYER_APP_AD_FORM_ID);
    }
}
