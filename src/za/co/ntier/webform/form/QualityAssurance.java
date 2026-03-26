package za.co.ntier.webform.form;

import java.util.HashMap;
import java.util.Map;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.webui.panel.ADForm;
import org.compiere.model.MForm;
import org.compiere.model.MTable;
import org.compiere.model.Query;
import org.compiere.util.Env;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Div;

import za.co.ntier.api.model.I_ZZ_Program_Master_Data;
import za.co.ntier.api.model.X_ZZ_Program_Master_Data;

@org.idempiere.ui.zk.annotation.Form(name = "za.co.ntier.webform.form.QualityAssurance")
public class QualityAssurance extends ADForm{

	private static final long serialVersionUID = 3000374226779334282L;
	MenuContextInfo menuContextInfo;
	
	@Override
	protected void init(int adFormId, String name) {
		menuContextInfo = new MenuContextInfo(null, null, null, false, null, null);
		
		MForm adForm = MForm.get(adFormId);
		
		String zulPathRelative = null;
		if ("Assessor Registration".equalsIgnoreCase(adForm.getDescription())) {
			zulPathRelative = WebForm.class.getResource("/za/co/ntier/webform/sdr/zul/assessorRegistration.zul").toString();
			
		}else {
			throw new AdempiereException("Not Support Form");
		}
		
		menuContextInfo.setZulPath(zulPathRelative);
		
		Query query = MTable.get(Env.getCtx(), I_ZZ_Program_Master_Data.Table_Name).createQuery("AD_Form_ID = ?", null);
		query.setClient_ID().setParameters(adFormId);
		
		X_ZZ_Program_Master_Data programMaster = null;//query.firstOnly();
		menuContextInfo.setProgramMasterData(programMaster);
		
		String title = null;
		if (programMaster != null) {
			title = programMaster.getTitle();
		}else {
			title = adForm.getName();
		}
		menuContextInfo.setFormTitle(title);
		super.init(adFormId, menuContextInfo.getFormTitle());
	}
	
	@Override
	protected void initForm() {
		Map<String, Object> args = new HashMap<>();
		args.put(WebForm.menuContextInfoKey, menuContextInfo);
		
		Component inc = Executions.createComponents(menuContextInfo.getZulPath(), null, args);

		Div outterDiv = new Div();
		this.appendChild(outterDiv);
		outterDiv.setSclass("mqaWebForm");

		Div container = new Div();

		container.setSclass("container");

		outterDiv.appendChild(container);

		container.appendChild(inc);
		
	}

}
