package za.co.ntier.webform.sdr.component.tab.viewmodel;

import java.io.IOException;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import za.co.ntier.webform.form.viewmodel.component.ComponentVMWrapper;
import za.co.ntier.webform.sdr.component.tab.bean.NavTab;

public class TabButtonVM extends ComponentVMWrapper<Object> {

	private NavTab navTab;

	@Init(superclass = true)
	public void init(
			@ExecutionArgParam("navTab") NavTab navTab
			) {
		this.navTab = navTab;
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

	

	@Command(value = "submitApplication")
	public void submitApplication() throws IOException {

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
}
