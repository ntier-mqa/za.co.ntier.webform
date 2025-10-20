package za.co.ntier.webform.sdr.component.tab.bean;

import org.zkoss.zul.ListModelList;

import za.co.ntier.webform.sdr.component.bean.TableModel;

public class NavTabPanel {
	private NavTab parent;

	public boolean isDisable() {
		return !parent.getTabPanelModel().isSelected(this);
	}

	public NavTabPanel(NavTab parent) {
		this.parent = parent;
		parent.getTabPanelModel().add(this);
	}

	private String tabTitle;
	private ListModelList<TableModel> compModel = new ListModelList<TableModel>();

	/**
	 * @return the compModel
	 */
	public ListModelList<TableModel> getCompModel() {
		return compModel;
	}


	/**
	 * @return the tabTitle
	 */
	public String getTabTitle() {
		return tabTitle;
	}

	/**
	 * @param tabTitle the tabTitle to set
	 */
	public void setTabTitle(String tabTitle) {
		this.tabTitle = tabTitle;
	}


	/**
	 * @return the parent
	 */
	public NavTab getParent() {
		return parent;
	}


	/**
	 * @param parent the parent to set
	 */
	public void setParent(NavTab parent) {
		this.parent = parent;
	}

}
