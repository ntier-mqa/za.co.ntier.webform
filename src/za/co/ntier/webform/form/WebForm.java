package za.co.ntier.webform.form;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.webui.panel.ADForm;
import org.apache.commons.lang3.StringUtils;
import org.compiere.model.MTable;
import org.compiere.util.Env;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Div;

import za.co.ntier.api.model.I_ZZ_Application_Form;
import za.co.ntier.api.model.I_ZZ_Program_Master_Data;
import za.co.ntier.webform.form.bean.ProgramType;

@org.idempiere.ui.zk.annotation.Form(name = "za.co.ntier.webform.form.EmployerApplicationForm")
public class WebForm extends ADForm {

	public static final String applicationFormUUKey="+" + I_ZZ_Application_Form.COLUMNNAME_ZZ_Application_Form_UU;
	public static final String isUploadWPAForNVCMenuContextKey = "+uploadWPAForNVC";
	public static final String menuContextInfoKey = "menuContextInfo";
	
	public static final String recordUUMenuContextKey = "+recordUU";
	public static final String recordUUMenuContextKeyNonPlus = "recordUU";
	public static final String recordIDMenuContextKey = "+recordID";
	public static final String recordIDMenuContextKeyNonPlus = "recordID";
	
	
	
	public static final String programMasterDataUUMenuContextKey = "+"
			+ I_ZZ_Program_Master_Data.COLUMNNAME_ZZ_Program_Master_Data_UU;
	public static final String programTypeMenuContextKey = "+programType";

	private static final long serialVersionUID = -5402852171052424756L;
	
	public static final String zulPathRool = "/za/co/ntier/webform/zul/";
	
	public static final String zulSdrPathRool = "/za/co/ntier/webform/";
	
	public static String getBundleResourcePath(String zulPath) {
		if (zulPath.startsWith("sdr/")) {
			zulPath = zulSdrPathRool + zulPath;
		}else if (!zulPath.startsWith("/")) {// relative path
			zulPath = zulPathRool + zulPath;
		}
		
		URL url = WebForm.class.getResource(zulPath);
		return url.toString();
	}
	MenuContextInfo menuContextInfo;
	@Override
	protected void init(int adFormId, String name) {
		menuContextInfo = parseMenuContectInfo();
		super.init(adFormId, menuContextInfo.getFormTitle());
	}

	@Override
	protected void initForm() {
		Map<String, Object> args = new HashMap<>();
		args.put(menuContextInfoKey, menuContextInfo);
			
		String zulPathRelative = WebForm.class.getResource(menuContextInfo.getZulPath()).toString();
		Component inc = Executions.createComponents(zulPathRelative, null, args);

		Div outterDiv = new Div();
		this.appendChild(outterDiv);
		outterDiv.setSclass("mqaWebForm");

		Div container = new Div();

		container.setSclass("container");

		outterDiv.appendChild(container);

		container.appendChild(inc);
	}

	private MenuContextInfo parseMenuContectInfo() {
		String zulPath = Env.getContext(Env.getCtx(), m_WindowNo, "+zulPath");
		if (StringUtils.isBlank(zulPath)) {
			zulPath = "/za/co/ntier/webform/zul/discretionaryGrantsApplicationProgram.zul";
		}

		String programMasterDataUUValue = Env.getContext(Env.getCtx(), m_WindowNo, programMasterDataUUMenuContextKey);

		String programTypeValue = Env.getContext(Env.getCtx(), m_WindowNo, programTypeMenuContextKey);

		String uploadWPAForNVCValue = Env.getContext(Env.getCtx(), m_WindowNo, isUploadWPAForNVCMenuContextKey);

		boolean isUploadWPAForNVC = uploadWPAForNVCValue != null && ("Y".equalsIgnoreCase(uploadWPAForNVCValue) ||
				"true".equalsIgnoreCase(uploadWPAForNVCValue));

		I_ZZ_Program_Master_Data masterData = MTable.get(I_ZZ_Program_Master_Data.Table_ID)
					.createQuery(String.format("%s = ?", I_ZZ_Program_Master_Data.COLUMNNAME_ZZ_Program_Master_Data_UU), null)
					.setParameters(programMasterDataUUValue).firstOnly();
		if (masterData == null) {
			throw new AdempiereException("Menu use a Wrong program defination");
		}
		String formTitle = Env.getContext(Env.getCtx(), m_WindowNo, "+formTitle");  // My Applications does not have program master data
		if (formTitle == null || formTitle.equals("")) {
			formTitle = masterData.getTitle();
		}

		String applicationFormUU = Env.getContext(Env.getCtx(), m_WindowNo, applicationFormUUKey);
		
		MenuContextInfo menuContextInfo = new MenuContextInfo(ProgramType.valueOf(programTypeValue), zulPath,
				masterData, isUploadWPAForNVC, formTitle, applicationFormUU);
		
		String recordUUValue = Env.getContext(Env.getCtx(), m_WindowNo, recordUUMenuContextKey);
		menuContextInfo.setRecordUU(recordUUValue);
		
		int recordIDValue = Env.getContextAsInt(Env.getCtx(), m_WindowNo, recordIDMenuContextKey);
		menuContextInfo.setRecordID(recordIDValue);
		
		return menuContextInfo;
	}
}
