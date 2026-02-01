package za.co.ntier.webform.sdr.component.tab.viewmodel;

import java.io.IOException;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import za.co.ntier.webform.form.viewmodel.component.ComponentVMWrapper;
import za.co.ntier.webform.sdr.component.bean.ISaveApp;
import za.co.ntier.webform.sdr.component.tab.bean.NavTab;

public class TabButtonVM extends ComponentVMWrapper<Object> {
	private ISaveApp saveApp;
	
	private NavTab navTab;

	@Init(superclass = true)
	public void init(
			@ExecutionArgParam("navTab") NavTab navTab,
			@ExecutionArgParam("saveApp") ISaveApp saveApp
			) {
		this.navTab = navTab;
		this.saveApp = saveApp;
	}

	public boolean isSupportSaveApp() {
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
	
	@Command
	@NotifyChange({"activeFirstTab", "activeEndTab", "activeMidTab"})
	public void nextTab() {
		navTab.doNextTab();
	}

	@Command
	@NotifyChange({"activeFirstTab", "activeEndTab", "activeMidTab"})
	public void prevTab() {
		navTab.doPrevTab();
	}

	/**
	 * @param navTab the navTab to set
	 */
	public void setNavTab(NavTab navTab) {
		this.navTab = navTab;
	}

	public boolean isActiveFirstTab() {
		return navTab.isActiveFirstTab();
	}

	public boolean isActiveEndTab() {
		return navTab.isActiveEndTab();
	}

	public boolean isActiveMidTab() {
		return navTab.isActiveMidTab();
	}

	public ISaveApp getSaveApp() {
		return saveApp;
	}

	public void setSaveApp(ISaveApp saveApp) {
		this.saveApp = saveApp;
	}
}
