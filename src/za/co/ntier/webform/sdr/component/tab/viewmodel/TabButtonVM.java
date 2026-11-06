package za.co.ntier.webform.sdr.component.tab.viewmodel;

import java.io.IOException;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import za.co.ntier.webform.form.viewmodel.component.ComponentVMWrapper;
import za.co.ntier.webform.sdr.component.bean.ISaveApp;
import za.co.ntier.webform.sdr.component.tab.bean.NavTab;

public class TabButtonVM extends ComponentVMWrapper<Object> {
	private ISaveApp saveApp;
	
	public String getBtSaveLabel() {
		if (saveApp != null)
			return saveApp.getBtSaveLabel();
		
		return "Save & Exit";
	}

	public String getBtNextLabel() {
		if (saveApp != null)
			return saveApp.getBtNextLabel(navTab);
		return "Next";
	}

	public String getBtNextIcon() {
		if (saveApp != null)
			return saveApp.getBtNextIcon(navTab);
		return "z-icon-arrow-right";
	}
	
	private NavTab navTab;

	@Init(superclass = true)
	public void init(
			@ExecutionArgParam("navTab") NavTab navTab,
			@ExecutionArgParam("saveApp") ISaveApp saveApp
			) {
		this.navTab = navTab;
		this.saveApp = saveApp;
	}

	public boolean isShowSubmitApp() {
		if (getSaveApp() != null && getSaveApp().isSupportSubmit()) {
			if (navTab != null) {
				return navTab.isActiveEndTab();
			}else {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isShowSaveApp() {
		if (getSaveApp() != null && !getSaveApp().isShowSaveOnFirstTab() && navTab != null && navTab.isActiveFirstTab()) {
			return false;
		}
		
		return getSaveApp() != null && getSaveApp().isSupportSave();

	}
	
	public boolean isSupportSubmitApp() {
		return getSaveApp() != null && getSaveApp().isSupportSubmit();
	}
	
	public boolean isSupportDeleteApp() {
		return getSaveApp() != null && getSaveApp().isSupportDelete();
	}
	
	@Command
	public void saveApp() {
		if (getSaveApp() != null && getSaveApp().isSupportSave())
			getSaveApp().saveApp();
	}

	@Command
	public void deleteApp() {
		if (getSaveApp() != null && getSaveApp().isSupportDelete())
			getSaveApp().deleteApp();
	}

	@Command
	public void submitApp() throws IOException {
		if (getSaveApp() != null && getSaveApp().isSupportSubmit())
			getSaveApp().submitApp();
	}
	
	public boolean isSupportNextTab() {
		return navTab == null;
	}
	
	public boolean isSupportPrevTab() {
		return navTab == null;
	}
	
	@Command
	@NotifyChange({"showDel", "showPrev", "showNext", "showSubmitApp", "showSaveApp", "btNextLabel", "btNextIcon"})
	public void nextTab() {
		if (saveApp != null && saveApp.onNextTab(navTab)) {
			return;
		}
		if (navTab != null) 
			navTab.doNextTab();
	}

	@Command
	@NotifyChange({"showDel", "showPrev", "showNext", "showSubmitApp", "showSaveApp", "btNextLabel", "btNextIcon"})
	public void prevTab() {
		if (navTab != null)
			navTab.doPrevTab();
	}

	/**
	 * @param navTab the navTab to set
	 */
	public void setNavTab(NavTab navTab) {
		this.navTab = navTab;
	}

	public boolean isShowDel() {
		return navTab != null && navTab.isActiveFirstTab();
	}
	
	public boolean isShowPrev() {
		return navTab != null && !navTab.isActiveFirstTab();
	}

	public boolean isShowNext() {
		return navTab != null && !navTab.isActiveEndTab();
	}

	public boolean isActiveMidTab() {
		return navTab != null && navTab.isActiveMidTab();
	}

	public ISaveApp getSaveApp() {
		return saveApp;
	}

	public void setSaveApp(ISaveApp saveApp) {
		this.saveApp = saveApp;
	}
	
	public String getDeleteLabel() {
		return getSaveApp() != null?getSaveApp().deleteLabel():"Default Cancel";
	}
}
