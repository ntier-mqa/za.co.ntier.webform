package za.co.ntier.webform.sdr.component.tab.bean;

import java.util.List;
import java.util.stream.Collectors;

import org.compiere.util.CLogger;
import org.zkoss.zul.ListModelList;

import za.co.ntier.webform.sdr.component.bean.CellModel.InputCheckResult;
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
	protected static final CLogger log = CLogger.getCLogger(NavTabPanel.class);
	public InputCheckResult parseInputState() {
		InputCheckResult rowInputCheckResult = new InputCheckResult();
		rowInputCheckResult.setEmpty(true).setFillMandatory(true).setNotChange(true);
		
		for (Object comp : getCompModel()) {
			TableModel tbModel = null;
			if (comp instanceof TableModel)
				tbModel = (TableModel)comp;
			
			if (tbModel != null && tbModel.isUsed()) {
				InputCheckResult cellInputCheckResult = tbModel.parseInputState();
				if (!cellInputCheckResult.getEmpty()) {// has at least once field have value
					rowInputCheckResult.setEmpty(false);
				}
				
				if (cellInputCheckResult.getHasMandatory())
					rowInputCheckResult.setHasMandatory(true);
				
				if (!cellInputCheckResult.getFillMandatory()) {
					rowInputCheckResult.setFillMandatory(false);// has at least once field have value
					log.warning("not input for mandatory field on table:" + tbModel.getTableTitle() + " sclass:" + tbModel.getSclass());
				}
				
				if (!cellInputCheckResult.getNotChange()) {
					rowInputCheckResult.setNotChange(false);// has at least once field has change when compare to default
				}
			}
			
		}
		
		return rowInputCheckResult;
	}

	@Override
	public List<ISaveForm> getChildren() {
		return compModel.stream()
	            .map(panel -> (ISaveForm) panel)
	            .collect(Collectors.toList());
	}

}
