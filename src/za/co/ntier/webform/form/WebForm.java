package za.co.ntier.webform.form;

import java.util.HashMap;
import java.util.Map;

import org.adempiere.webui.panel.ADForm;
import org.compiere.util.Env;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Div;

import za.co.ntier.webform.form.bean.ProgramType;
import za.co.ntier.webform.model.I_ZZ_Program_Master_Data;
import za.co.ntier.webform.model.X_ZZ_Program_Master_Data;

@org.idempiere.ui.zk.annotation.Form(name = "za.co.ntier.webform.form.EmployerApplicationForm")
public class WebForm extends ADForm {

	private static final long serialVersionUID = -5402852171052424756L;

	public static String getBundleResourcePath(String zulPath) {
		if (!zulPath.startsWith("/")) {// relative path
			zulPath = "/za/co/ntier/webform/zul/" + zulPath;
		}
		return WebForm.class.getResource(zulPath).toString();
	}

	MenuContextInfo menuContextInfo;
	
	@Override
	protected void init(int adFormId, String name) {
		menuContextInfo = parseMenuContectInfo();
		super.init(adFormId, menuContextInfo.getFormTitle());
	}

	public static final String programMasterDataUUMenuContextKey = "+" + I_ZZ_Program_Master_Data.COLUMNNAME_ZZ_Program_Master_Data_UU;
	public static final String programTypeMenuContextKey = "+programType";
	public static final String isUploadWPAForNVCMenuContextKey = "+uploadWPAForNVC";
	public static final String menuContextInfoKey = "menuContextInfo";
	
	private MenuContextInfo parseMenuContectInfo() {
		String zulPath = Env.getContext(Env.getCtx(), m_WindowNo, "+zulPath");

		String programMasterDataUUValue = Env.getContext(Env.getCtx(), m_WindowNo, programMasterDataUUMenuContextKey);
		
		String programTypeValue = Env.getContext(Env.getCtx(), m_WindowNo, programTypeMenuContextKey);
		
		String uploadWPAForNVCValue = Env.getContext(Env.getCtx(), m_WindowNo, isUploadWPAForNVCMenuContextKey);
		
		boolean isUploadWPAForNVC = uploadWPAForNVCValue != null && "Y".equalsIgnoreCase(uploadWPAForNVCValue);
		
		I_ZZ_Program_Master_Data masterData = new X_ZZ_Program_Master_Data(Env.getCtx(), programMasterDataUUValue, null);
		
		String formTitle = Env.getContext(Env.getCtx(), m_WindowNo, "+formTitle");
		
		MenuContextInfo menuContextInfo = new MenuContextInfo(
				ProgramType.valueOf(programTypeValue),
				zulPath,
				masterData,
				isUploadWPAForNVC,
				formTitle);
		
		return menuContextInfo;
	}
	
	@Override
	protected void initForm() {
		Map<String, Object> args = new HashMap<>();
		args.put(menuContextInfoKey, menuContextInfo);
		
		String zulPathRelative = WebForm.class.getResource(menuContextInfo.getZulPath()).toString();

		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		// Set the context class loader to this bundle's class loader to ensure that
		// classes provided by the bundle (e.g., za.co.ntier.webform.form.viewmodel.*)
		// used in ZUL files can be found.
		Thread.currentThread().setContextClassLoader(WebForm.class.getClassLoader());
		Component inc = null;
		try {
			inc = Executions.createComponents(zulPathRelative, null, args);
		} finally {
			Thread.currentThread().setContextClassLoader(cl);
		}

		Div outterDiv = new Div();
		this.appendChild(outterDiv);
		outterDiv.setSclass("mqaWebForm");

		Div container = new Div();
		
		container.setSclass("container");		
		
		outterDiv.appendChild(container);

		container.appendChild(inc);
	}
}
