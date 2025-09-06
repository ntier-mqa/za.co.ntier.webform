package za.co.ntier.webform.form.bean;

import org.zkoss.zul.Tabbox;

import za.co.ntier.webform.form.viewmodel.DiscretionaryGrantsApplicationProgramVM;

public class MainButtonComponent {
	private	DiscretionaryGrantsApplicationProgramVM applicationProgramVM;
	private	TabType tabType;
	private Tabbox tab;
	public MainButtonComponent(DiscretionaryGrantsApplicationProgramVM applicationProgramVM, TabType tabType, Tabbox tab) {
		this.applicationProgramVM = applicationProgramVM;
		this.tabType = tabType;
		this.setTab(tab);
	}
	
	/**
	 * @return the applicationProgramVM
	 */
	public DiscretionaryGrantsApplicationProgramVM getApplicationProgramVM() {
		return applicationProgramVM;
	}
	/**
	 * @param applicationProgramVM the applicationProgramVM to set
	 */
	public void setApplicationProgramVM(DiscretionaryGrantsApplicationProgramVM applicationProgramVM) {
		this.applicationProgramVM = applicationProgramVM;
	}
	/**
	 * @return the tabType
	 */
	public TabType getTabType() {
		return tabType;
	}
	/**
	 * @param tabType the tabType to set
	 */
	public void setTabType(TabType tabType) {
		this.tabType = tabType;
	}

	/**
	 * @return the tab
	 */
	public Tabbox getTab() {
		return tab;
	}

	/**
	 * @param tab the tab to set
	 */
	public void setTab(Tabbox tab) {
		this.tab = tab;
	}
}
