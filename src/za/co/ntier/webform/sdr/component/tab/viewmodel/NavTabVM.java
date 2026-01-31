package za.co.ntier.webform.sdr.component.tab.viewmodel;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.form.viewmodel.component.ComponentVMWrapper;
import za.co.ntier.webform.sdr.component.bean.ISupportSaveApp;
import za.co.ntier.webform.sdr.component.tab.bean.NavTab;
import za.co.ntier.webform.sdr.component.tab.bean.NavTabPanel;
import za.co.ntier.webform.sdr.component.tab.bean.OrglinkTabPanel;


public class NavTabVM extends ComponentVMWrapper<NavTab> {
	private int activeTabIndex = -1;

	@Init(superclass = true)
	public void init(@ExecutionArgParam("supportSaveApp") ISupportSaveApp supportSaveApp) {
		getComponent().setSupportSaveApp(supportSaveApp);
	}
			
	/**
	 * @return the activeTabIndex
	 */
	public int getActiveTabIndex() {
		return activeTabIndex;
	}

	/**
	 * @param activeTabIndex the activeTabIndex to set
	 */
	public void setActiveTabIndex(int activeTabIndex) {
		this.activeTabIndex = activeTabIndex;
	}

	public boolean isOrglinkTabPanel(NavTabPanel tabModel) {
		return tabModel instanceof OrglinkTabPanel;
	}
}
