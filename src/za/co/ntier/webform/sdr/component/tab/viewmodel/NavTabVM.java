package za.co.ntier.webform.sdr.component.tab.viewmodel;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.viewmodel.component.ComponentVMWrapper;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.tab.bean.NavTab;
import za.co.ntier.webform.sdr.component.tab.bean.NavTabPanel;
import za.co.ntier.webform.sdr.component.tab.bean.OrglinkTabPanel;

@Init(superclass = true)
public class NavTabVM extends ComponentVMWrapper<NavTab> {
	private int activeTabIndex = -1;

	@Command
	public void addTableRow(@BindingParam("tableModel") TableModel tableModel) {
		if (tableModel != null) {
			tableModel.addNewRow(null);
		}
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
