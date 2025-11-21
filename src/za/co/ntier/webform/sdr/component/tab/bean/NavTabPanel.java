package za.co.ntier.webform.sdr.component.tab.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.zkoss.zul.ListModelList;

import za.co.ntier.api.model.X_ZZSdf;
import za.co.ntier.webform.sdr.component.bean.ISupportSave;
import za.co.ntier.webform.sdr.component.bean.TableModel;

public class NavTabPanel implements ISupportSave {
	private String sclass;
	private NavTab parent;

	public boolean isDisable() {
		return !parent.getTabPanelModel().isSelected(this);
	}

	public NavTabPanel(NavTab parent) {
		this.parent = parent;
		if (parent != null)
			parent.getTabPanelModel().add(this);
	}

	private String tabTitle;
	private ListModelList<Object> compModel = new ListModelList<Object>();

	/**
	 * @return the compModel
	 */
	public ListModelList<Object> getCompModel() {
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

	@Override
	public void save(X_ZZSdf applicationForm, String trxName) {
		List<ISupportSave> listSave = new ArrayList<>();
		for (Object objSupportSave : compModel) {
			if (objSupportSave instanceof ISupportSave) {
				listSave.add((ISupportSave)objSupportSave);
			}
		}
		saveList(listSave, applicationForm, trxName);
		
	}

	/**
	 * @return the sclass
	 */
	public String getSclass() {
		return sclass;
	}

	/**
	 * @param sclass the sclass to set
	 */
	public void setSclass(String sclass) {
		this.sclass = sclass;
	}


}
