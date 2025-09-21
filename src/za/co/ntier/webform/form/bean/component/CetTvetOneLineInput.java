package za.co.ntier.webform.form.bean.component;

import java.util.List;
import java.util.Map;

import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.Query;

import za.co.ntier.webform.model.I_ZZAnnexure;
import za.co.ntier.webform.model.X_ZZAnnexure;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class CetTvetOneLineInput extends AnnexureInfo{
	public static CetTvetOneLineInput getCetTvetOneLineInput(X_ZZ_Application_Form applicationForm,
			List<ColumnInfo<?>> cols, String title, String rowTitle, ColumnInfo<?> colRowTitleNameOfIntervention) {
		
		CetTvetOneLineInput annexureInfo = AnnexureInfo.getAnnexureInfo(CetTvetOneLineInput.class, cols, false);
		annexureInfo.setSectionHeader(title);
		
		return annexureInfo;
	}

	public void initCetTvetOneLine(X_ZZ_Application_Form applicationForm, String title, String rowTitle, ColumnInfo<?> rowCol) {
		List<PO> annexureDao = null;
		if (applicationForm != null) {
			Query annexureQuery = MTable.get(X_ZZAnnexure.Table_ID).createQuery(String.format("%s = ? AND %s = ?", 
					I_ZZAnnexure.COLUMNNAME_ZZ_Application_Form_ID, I_ZZAnnexure.COLUMNNAME_Name), null);
			annexureQuery.setParameters(applicationForm.getZZ_Application_Form_ID(), title);
			annexureQuery.setOrderBy(X_ZZAnnexure.COLUMNNAME_ZZAnnexure_ID);
			annexureDao = annexureQuery.list();
		}
		
		List<Map<ColumnInfo<?>, Object>> rowTitles = null;
		if (rowTitle != null) {
			rowTitles = List.of(Map.of(rowCol, rowTitle));
		}
		
		init(applicationForm, annexureDao, rowTitles);
	}

	public static CetTvetOneLineInput getCetTvetOneLineInput(X_ZZ_Application_Form applicationForm,
			List<ColumnInfo<?>> cols, String title, Map<ColumnInfo<?>, Object> rowTitles) {
		// TODO Auto-generated method stub
		return null;
	}

}
