package za.co.ntier.webform.sdr.component.tab.bean;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.ListModelList;

import za.co.ntier.webform.sdr.component.bean.ISaveForm;
import za.co.ntier.webform.sdr.component.bean.TableModel;

public class NavTabPanel implements ISaveForm {
	private String sclass;
	private NavTab parent;

	public boolean isDefaultTabPanel() {
		return true;
	}
	
	public String getZulPath() {
		return null;
	}
	
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
	public void syncUIToDao(String trxName) {
		
		ISaveForm.batchSyncToDao(compModel, trxName);
		
	}
	
	@Override
	public void saveAttachment(String trxName) {
		for (Object objSupportSave : compModel) {
			if (objSupportSave instanceof ISaveForm) {
				((ISaveForm)objSupportSave).saveAttachment(trxName);
			}
		}
		
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

	@Override
	public boolean validate(Boolean isSubmit) {
		return ISaveForm.validates(compModel, isSubmit);
	}

	@Override
	public void saveToDb(String trxName) {
		ISaveForm.batchSaveToDb(compModel, trxName);
		
	}

	public boolean isInputEmpty () {
		for (Object comp : getCompModel()) {
			if (comp instanceof TableModel) {
				TableModel tbModel = (TableModel)comp;
				if (!tbModel.isInputEmpty()) {
					return false;
				}
			}
		}
		
		return true;
	}

}
