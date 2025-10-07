package za.co.ntier.webform.form.bean;

import org.zkoss.zul.Tabbox;

import za.co.ntier.webform.form.viewmodel.DiscretionaryGrantsApplicationProgramVM;

public class MainButtonComponent {
	private	DiscretionaryGrantsApplicationProgramVM applicationProgramVM;
	private boolean continueDisabled; // defaults to false
	private boolean submitDisabled;   // defaults to false


	private Tabbox tab;
	private Tabbox subTab;
	private	TabType tabType;
	
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
	 * @return the tab
	 */
	public Tabbox getTab() {
		return tab;
	}

	/**
	 * @return the tabType
	 */
	public TabType getTabType() {
		return tabType;
	}
	public boolean isContinueDisabled() { return continueDisabled; }
	public boolean isSubmitDisabled() { return submitDisabled; }
	/**
	 * @param applicationProgramVM the applicationProgramVM to set
	 */
	public void setApplicationProgramVM(DiscretionaryGrantsApplicationProgramVM applicationProgramVM) {
		this.applicationProgramVM = applicationProgramVM;
	}
	public void setContinueDisabled(boolean continueDisabled) { this.continueDisabled = continueDisabled; }
	public void setSubmitDisabled(boolean submitDisabled) { this.submitDisabled = submitDisabled; }

	/**
	 * @param tab the tab to set
	 */
	public void setTab(Tabbox tab) {
		this.tab = tab;
	}

	/**
	 * @param tabType the tabType to set
	 */
	public void setTabType(TabType tabType) {
		this.tabType = tabType;
	}
	/**
	 * @return the subTab
	 */
	public Tabbox getSubTab() {
		return subTab;
	}
	/**
	 * @param subTab the subTab to set
	 */
	public void setSubTab(Tabbox subTab) {
		this.subTab = subTab;
	}
}
