package za.co.ntier.webform.sdr.component.tab.viewmodel;

import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.viewmodel.component.ComponentVMWrapper;
import za.co.ntier.webform.sdr.component.tab.bean.NavTab;

@Init(superclass = true)
public class NavTabVM extends ComponentVMWrapper<NavTab> {
	private int activeTabIndex = -1;

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


}
