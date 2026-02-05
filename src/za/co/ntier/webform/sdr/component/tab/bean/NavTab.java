package za.co.ntier.webform.sdr.component.tab.bean;

import org.zkoss.bind.BindUtils;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.event.ListDataEvent;
import org.zkoss.zul.event.ListDataListener;

import za.co.ntier.webform.sdr.component.bean.ISaveForm;

public class NavTab implements ListDataListener, ISaveForm{
	private ListModelList<NavTabPanel> tabPanelModel;
	
	public NavTab() {
		tabPanelModel = new ListModelList<NavTabPanel>();
		tabPanelModel.addListDataListener(this);
	}
	/**
	 * @return the tabPanelModel
	 */
	public ListModelList<NavTabPanel> getTabPanelModel() {
		return tabPanelModel;
	}

	@Override
	public void onChange(ListDataEvent event) {
		if (event.getType() == ListDataEvent.INTERVAL_ADDED) {
			if (tabPanelModel.isSelectionEmpty()) {
				tabPanelModel.addToSelection(tabPanelModel.get(0));
				activeTabIndex = 0;
			}
		}
	}


	private int activeTabIndex;

	protected boolean validateActiveTab() {
		NavTabPanel activeTabPanel = getTabPanelModel().get(activeTabIndex);
		return activeTabPanel.validate();
	}
	
	public void doNextTab() {
		if (activeTabIndex == getTabPanelModel().size() - 1
				|| getTabPanelModel().size() == 0) {
			// end tab do nothing
		}else {
			if (validateActiveTab()) {
				setActiveTab(activeTabIndex + 1, activeTabIndex);
			}else {
				// do nothing
			}
		}
	}

	private void setActiveTab(int newActiveIndex, int oldActiveIndex) {
		getTabPanelModel().clearSelection();
		NavTabPanel oldActiveTabPanel = getTabPanelModel().get(oldActiveIndex);
		NavTabPanel nextActiveTabPanel = getTabPanelModel().get(newActiveIndex);
		// disable also de-select current selected tab
		BindUtils.postNotifyChange(oldActiveTabPanel, "disable");
		// track selected tab
		getTabPanelModel().addToSelection(nextActiveTabPanel);
		// enable also update selected to new tab
		BindUtils.postNotifyChange(nextActiveTabPanel, "disable");
		activeTabIndex = newActiveIndex;
	}

	public void doPrevTab() {
		if (activeTabIndex == 0
				|| getTabPanelModel().size() == 0) {
			// begin tab do nothing
		}else {
			if (validateActiveTab()) {
				setActiveTab(activeTabIndex - 1, activeTabIndex);
			}else {
				// do nothing
			}
			
		}

	}

	public boolean isActiveFirstTab() {
		if (getTabPanelModel().size() == 0)
			return false;

		return activeTabIndex == 0;
	}

	public boolean isActiveEndTab() {
		if (getTabPanelModel().size() == 0)
			return false;

		return activeTabIndex == getTabPanelModel().size() - 1;
	}

	public boolean isActiveMidTab() {
		if (getTabPanelModel().size() == 1)
			return true;

		if (getTabPanelModel().size() == 0)
			return false;

		return 0 < activeTabIndex && activeTabIndex < getTabPanelModel().size() - 1;
	}
	@Override
	public void syncUIToDao(String trxName) {
		ISaveForm.batchSyncToDao(tabPanelModel, trxName);
	}
	@Override
	public void saveAttachment(String trxName) {
		tabPanelModel.forEach(t -> t.saveAttachment(trxName));
		
	}
	@Override
	public boolean validate() {
		return ISaveForm.validates(tabPanelModel);
	}
	@Override
	public void saveToDb(String trxName) {
		ISaveForm.batchSaveToDb(tabPanelModel, trxName);
		
	}
	
}
