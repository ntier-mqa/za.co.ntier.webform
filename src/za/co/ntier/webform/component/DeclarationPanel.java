package za.co.ntier.webform.component;

import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.sdr.component.tab.bean.NavTab;
import za.co.ntier.webform.sdr.component.tab.bean.NavTabPanel;

public class DeclarationPanel extends NavTabPanel {
	
	public DeclarationPanel(NavTab parent) {
		super(parent);
		
	}
		
	@Override
	public boolean isDefaultTabPanel() {
		return false;
	}
	
	@Override
	public String getZulPath() {
		return WebForm.getBundleResourcePath("/za/co/ntier/webform/component/declaration.zul");
	}

}
