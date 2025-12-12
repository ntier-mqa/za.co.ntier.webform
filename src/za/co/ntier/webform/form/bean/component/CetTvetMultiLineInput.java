package za.co.ntier.webform.form.bean.component;

import java.util.List;

import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.Env;

import za.co.ntier.api.model.I_ZZSubAnnex;
import za.co.ntier.api.model.X_ZZSubAnnex;
import za.co.ntier.api.model.X_ZZ_Application_Form;
import za.co.ntier.webform.form.bean.DataType;

public class CetTvetMultiLineInput extends AnnexureInfo{
	private String tabTitle;
	/**
	 * for CetTvet sub, show total but not row header
	 *
	 * @param sectionHeader
	 * @param columnInfos
	 * @return
	 */
	public static <T> CetTvetMultiLineInput getCetTvetMultiLineInput(X_ZZ_Application_Form appForm, List<ColumnInfo<?>> columnInfos, String dataType, String sectionHeader, String tableTitle) {
		boolean isShowTotalLine = false;
		for (ColumnInfo<?> col : columnInfos) {
			if (col.getDataType() == DataType.PositiveNumber) {
				isShowTotalLine = true;
				break;
			}
		}
		
		CetTvetMultiLineInput annexureInfo = AnnexureInfo.getAnnexureInfo(CetTvetMultiLineInput.class, columnInfos, isShowTotalLine);
		annexureInfo.setTabTitle(dataType);
		annexureInfo.setPoSupplier((annexure, app) -> {
			X_ZZSubAnnex po = new X_ZZSubAnnex(app.getCtx(), 0, app.get_TrxName());
			po.setDataType(annexure.getDataType());
			po.setZZ_Application_Form_ID(app.getZZ_Application_Form_ID());
			return po;
		});
		
		annexureInfo.setSectionHeader(sectionHeader);
		annexureInfo.setTableTitle(tableTitle);
		annexureInfo.setDataType(dataType);
		annexureInfo.setTabTitle(dataType);
		
		
		
		annexureInfo.setShowAddButton(true);
		
		List<PO> subAnnexs = null;
		if (appForm != null) {
			Query directSubAnnexQuery = MTable.get(Env.getCtx(), X_ZZSubAnnex.Table_Name)
					.createQuery(String.format("%s = ? AND %s = ?", I_ZZSubAnnex.COLUMNNAME_ZZ_Application_Form_ID, I_ZZSubAnnex.COLUMNNAME_DataType), null);
			directSubAnnexQuery.setOrderBy(I_ZZSubAnnex.COLUMNNAME_ZZSubAnnex_ID);
			directSubAnnexQuery.setParameters(appForm.getZZ_Application_Form_ID(), annexureInfo.getDataType());
			subAnnexs = directSubAnnexQuery.list();
		}
		annexureInfo.init(appForm, subAnnexs);
		
		return annexureInfo;
	}

	public static <T> CetTvetMultiLineInput getCetTvetMultiLineInput(X_ZZ_Application_Form appForm, List<ColumnInfo<?>> columnInfos, String dataType) {
		return getCetTvetMultiLineInput(appForm, columnInfos, dataType, null, null);
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
