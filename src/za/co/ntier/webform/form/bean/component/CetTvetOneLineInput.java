package za.co.ntier.webform.form.bean.component;

import java.util.List;
import java.util.Map;

import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.Env;

import za.co.ntier.api.model.I_ZZAnnexure;
import za.co.ntier.api.model.X_ZZAnnexure;
import za.co.ntier.api.model.X_ZZ_Application_Form;

public class CetTvetOneLineInput extends AnnexureInfo{
	private String tabTitle;
	
	public static CetTvetOneLineInput getCetTvetOneLineInput(X_ZZ_Application_Form applicationForm,
			List<ColumnInfo<?>> cols, String sectionHeader, String tableTitle, String identify, boolean showColumnHeader) {

		return CetTvetOneLineInput.getCetTvetOneLineInput(applicationForm, cols, sectionHeader, tableTitle, identify, showColumnHeader, null, false);
	}

	public static CetTvetOneLineInput getCetTvetOneLineInput(X_ZZ_Application_Form applicationForm,
			List<ColumnInfo<?>> cols, String sectionHeader, String tableTitle, String identify, boolean showColumnHeader, List<Map<ColumnInfo<?>, Object>> rowTitles, boolean showTotalRow) {
		
		CetTvetOneLineInput annexureInfo = AnnexureInfo.getAnnexureInfo(CetTvetOneLineInput.class, cols, showTotalRow);
		annexureInfo.setShowColumnHeader(showColumnHeader);
		annexureInfo.setPoSupplier((annexure, app) -> {
			X_ZZAnnexure po = new X_ZZAnnexure(app.getCtx(), 0, app.get_TrxName());
			po.setDataType(annexure.getDataType());
			po.setName(annexure.getDataType());
			po.setZZ_Application_Form_ID(app.getZZ_Application_Form_ID());
			return po;
		});
		
		annexureInfo.setDataType(identify);
		annexureInfo.setSectionHeader(sectionHeader);
		annexureInfo.setTabTitle(identify);
		annexureInfo.setTableTitle(tableTitle);
		
		List<PO> daos = null;
		if (applicationForm != null) {
			Query annexureQuery = MTable.get(Env.getCtx(), X_ZZAnnexure.Table_Name).createQuery(String.format("%s = ? AND %s = ?", 
			I_ZZAnnexure.COLUMNNAME_ZZ_Application_Form_ID, I_ZZAnnexure.COLUMNNAME_DataType), null);
			annexureQuery.setParameters(applicationForm.getZZ_Application_Form_ID(), annexureInfo.getDataType());
			annexureQuery.setOrderBy(I_ZZAnnexure.COLUMNNAME_ZZAnnexure_ID);
			daos = annexureQuery.list();
		}
		
		annexureInfo.init(applicationForm, daos, rowTitles);
		return annexureInfo;
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

}
