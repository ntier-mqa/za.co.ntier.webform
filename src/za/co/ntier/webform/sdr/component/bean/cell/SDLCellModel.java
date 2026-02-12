package za.co.ntier.webform.sdr.component.bean.cell;

import java.time.temporal.ChronoUnit;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.compiere.model.I_C_BPartner;
import org.compiere.model.I_C_Year;
import org.compiere.model.MBPartner;
import org.compiere.model.MTable;
import org.compiere.model.MYear;
import org.compiere.model.Query;
import org.compiere.model.X_AD_User;
import org.compiere.model.X_C_BPartner;
import org.compiere.util.Env;
import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.event.InputEvent;

import za.co.ntier.api.model.I_ZZ_Application_Form;
import za.co.ntier.api.model.I_ZZ_Levy_Paying;
import za.co.ntier.api.model.I_ZZ_Program_Master_Data;
import za.co.ntier.api.model.I_ZZ_WSP_ATR_Approvals;
import za.co.ntier.api.model.MBPartner_New;
import za.co.ntier.api.model.X_ZZ_Application_Form;
import za.co.ntier.api.model.X_ZZ_Program_Master_Data;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;

public class SDLCellModel extends CellModel {
	public SDLCellModel(TableModel tableModel, RowModel rowModel, ColumnModel colModel) {
		super(tableModel, rowModel, colModel);
		setCellType(TEXT_CELL);
	}

	public static ColumnModel getSDLColumnModel(String title, String daoPropertyName) {
		if (title == null)
			title = "Skills Development Levy (SDL) Number (Paying or Exempted)";
		return CellModel.getColModelForCell(CellModelInfo.of(ColumnModel.class, SDLCellModel.class, null), CellModelParams.of(title, daoPropertyName, null));
	}
	
	@Override
	public void cmdValueChange(String value) {
		// TODO Auto-generated method stub
		super.cmdValueChange(value);
	}
	
	@Override
	protected List<String> doValidate(Object inputValue) {
		List<String> validateMsgs = super.doValidate(inputValue);
		String sdlNumber = (String)inputValue;
		
		if (StringUtils.isBlank(sdlNumber)) {
			return validateMsgs;
		}
		
		MBPartner_New bPartner = null;
		I_ZZ_Program_Master_Data programMasterData = getTableModel().getSaveApp().getMenuContextInfo().getProgramMasterData();
		String criteriaValueAll = programMasterData.getZZ_Criteria();
		if (StringUtils.isNotBlank(sdlNumber)) {
			bPartner = MBPartner_New.get(Env.getCtx(), sdlNumber);
		}
		
		int appId = 0;
		if (getTableModel().getSaveApp().getMainApp() != null) {
			appId = ((X_ZZ_Application_Form)getTableModel().getSaveApp().getMainApp()).getZZ_Application_Form_ID();
		}
		if (bPartner != null) {
			String existAppFormWhere = String.format("%s = ? AND %s = ? AND (%s <> ? OR 0 = ?)", 
					I_ZZ_Application_Form.COLUMNNAME_ZZ_Program_Master_Data_ID, 
					I_ZZ_Application_Form.COLUMNNAME_C_BPartner_ID, 
					I_ZZ_Application_Form.COLUMNNAME_ZZ_Application_Form_ID);
			
			Query existAppFormQuery = MTable.get(Env.getCtx(), I_ZZ_Application_Form.Table_Name).createQuery(existAppFormWhere, null);
			
			List<X_ZZ_Application_Form> exitsAppForms = existAppFormQuery.setOnlyActiveRecords(true)
					.setParameters(programMasterData.getZZ_Program_Master_Data_ID(), bPartner.getC_BPartner_ID(), appId, appId)
					.list();
			if (exitsAppForms.size() > 0) {
				X_AD_User createdUser = new X_AD_User(Env.getCtx(), exitsAppForms.get(0).getCreatedBy(), null);
				String phoneInfo = null;
				if (StringUtils.isNotBlank(createdUser.getPhone())) {
					phoneInfo = " - " + createdUser.getPhone();
				}else {
					phoneInfo = "";
				}
				
				validateMsgs.add(String.format("An application for %s already exists.", bPartner.getName()));
				validateMsgs.add(String.format("It was created by User: %s %s on %s", bPartner.getName(), exitsAppForms.get(0).getUserName(),
						phoneInfo,
						exitsAppForms.get(0).getCreated().toLocalDateTime().truncatedTo(ChronoUnit.SECONDS).format(MasterUtil.dtf)));
				return validateMsgs;
			}
		}
		
		if (StringUtils.isNotBlank(criteriaValueAll) && bPartner == null) {
			validateMsgs.add("Your company has not met the required criteria");
			validateMsgs.add("The Skills Development Levy (SDL) Number does not exist");
			validateMsgs.add("You can not continue with this application");
		}else if (StringUtils.isNotBlank(criteriaValueAll) && bPartner != null) {
			String[] criteriaValues = criteriaValueAll.split(",");
			int currentSize = validateMsgs.size();
			for(String criteriaValue : criteriaValues) {
				if (X_ZZ_Program_Master_Data.ZZ_CRITERIA_OrganizationInMQASector.equals(criteriaValue) && !bPartner.isZZ_Is_MQA_Sector()) {
					validateMsgs.add("Organisation in MQA Sector");
				}
				
				if (X_ZZ_Program_Master_Data.ZZ_CRITERIA_LevyPaying.equals(criteriaValue)) {
					MYear currentFinYear = new MYear(Env.getCtx(), programMasterData.getC_Year_ID(), null);
					String fiscalYearStr = currentFinYear.getFiscalYear();
					String prevFiscalYearStr = String.valueOf(Integer.valueOf(fiscalYearStr) - 1);

					Query queryLevyPaying = MTable.get(Env.getCtx(), I_ZZ_Levy_Paying.Table_Name).createQuery(
							String.format("%s = ? AND %s = ?", I_ZZ_Levy_Paying.COLUMNNAME_C_BPartner_ID, I_C_Year.COLUMNNAME_FiscalYear), null);
					queryLevyPaying.addTableDirectJoin(I_C_Year.Table_Name);
					queryLevyPaying.setParameters(bPartner.getC_BPartner_ID(), prevFiscalYearStr);

					if (queryLevyPaying.list().size() == 0)
						validateMsgs.add("Levy Paying");
				}
				
				if (X_ZZ_Program_Master_Data.ZZ_CRITERIA_WSP_ATRSubmitted.equals(criteriaValue)) {
					MYear currentFinYear = new MYear(Env.getCtx(), programMasterData.getC_Year_ID(), null);
					Query queryLevyPaying = MTable.get(Env.getCtx(), I_ZZ_WSP_ATR_Approvals.Table_Name).createQuery(
							String.format("%s = ? AND %s = ?", I_ZZ_WSP_ATR_Approvals.COLUMNNAME_C_BPartner_ID, I_ZZ_WSP_ATR_Approvals.COLUMNNAME_ZZ_Financial_Year), null);
					queryLevyPaying.setParameters(bPartner.getC_BPartner_ID(), currentFinYear.getFiscalYear());
					if(queryLevyPaying.list().size() == 0) {
						validateMsgs.add("WSP ATR Approvals");
					}
				}
			}
			
			if (currentSize < validateMsgs.size()) {
				validateMsgs.add(currentSize, "Your company has not met the required criteria");
			}
		}
		
		
		
		
		return validateMsgs;
	}
}
