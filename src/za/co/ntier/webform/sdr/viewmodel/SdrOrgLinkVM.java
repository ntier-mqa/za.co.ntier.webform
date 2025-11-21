package za.co.ntier.webform.sdr.viewmodel;

import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.api.model.X_ZZSdf;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.form.bean.component.FormInfo;
import za.co.ntier.webform.sdr.component.tab.bean.OrglinkTabPanel;

public class SdrOrgLinkVM {
	private MenuContextInfo menuContextInfo;
	private FormInfo formInfo;
	private OrglinkTabPanel orgLinkTab;
	
	@Init
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo){
		this.setMenuContextInfo(menuContextInfo);
		setFormInfo(new FormInfo(menuContextInfo));
		X_ZZSdf sdf = MasterUtil.querySdf(Env.getAD_User_ID(Env.getCtx()));
		if (sdf != null)
			orgLinkTab= new OrglinkTabPanel(null, sdf, menuContextInfo);
		else
			MasterUtil.showDialog(
						Msg.getMsg(Env.getCtx(), "ZZOrgLinksSDFNotFound", false), 
						new StringBuilder(Msg.getMsg(Env.getCtx(), "ZZOrgLinksSDFNotFound", true))
					);
	}

	@Command
	public void saveClose() {
		orgLinkTab.save(null, null);
		MasterUtil.showDialog(
						Msg.getMsg(Env.getCtx(), "ZZOrgLinksSaveSuccess", false), 
						new StringBuilder(Msg.getMsg(Env.getCtx(), "ZZOrgLinksSaveSuccess", true))
					);
	}
	
	@Command
	public void submit() {
		orgLinkTab.submit(null, null);
		MasterUtil.showDialog(
				Msg.getMsg(Env.getCtx(), "ZZOrgLinksSubmitSuccess", false), 
				new StringBuilder(Msg.getMsg(Env.getCtx(), "ZZOrgLinksSubmitSuccess", true))
			);
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
	 * @return the formInfo
	 */
	public FormInfo getFormInfo() {
		return formInfo;
	}

	/**
	 * @param formInfo the formInfo to set
	 */
	public void setFormInfo(FormInfo formInfo) {
		this.formInfo = formInfo;
	}

	/**
	 * @return the orgLinkTab
	 */
	public OrglinkTabPanel getOrgLinkTab() {
		return orgLinkTab;
	}

	/**
	 * @param orgLinkTab the orgLinkTab to set
	 */
	public void setOrgLinkTab(OrglinkTabPanel orgLinkTab) {
		this.orgLinkTab = orgLinkTab;
	}
}
