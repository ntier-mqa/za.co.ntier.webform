package za.co.ntier.webform.form.bean.component;

import java.util.List;

import za.co.ntier.webform.form.bean.DataType;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class CetTvetMultiLineInput extends AnnexureInfo{
	/**
	 * for CetTvet sub, show total but not row header
	 *
	 * @param sectionHeader
	 * @param columnInfos
	 * @return
	 */
	public static <T> CetTvetMultiLineInput getCetTvetMultiLineInput(String sectionHeader, List<ColumnInfo<?>> columnInfos) {
		boolean isShowTotalLine = false;
		for (ColumnInfo<?> col : columnInfos) {
			if (col.getDataType() == DataType.PositiveNumber) {
				isShowTotalLine = true;
				break;
			}
		}
		
		CetTvetMultiLineInput annexureInfo = AnnexureInfo.getAnnexureInfo(CetTvetMultiLineInput.class, columnInfos, isShowTotalLine);
				
		annexureInfo.setSectionHeader(sectionHeader);
		
		annexureInfo.setShowAddButton(true);
		
		return annexureInfo;
	}
	
	public void initCetTvetMultiLine() {
		init(null, null, null);
	}

	public void initCetTvetMultiLine(X_ZZ_Application_Form applicationForm) {
		// TODO Auto-generated method stub
		
	}

	public static CetTvetMultiLineInput getCetTvetMultiLineInput(String title, List<ColumnInfo<?>> cols,
			String string) {
		// TODO Auto-generated method stub
		return null;
	}

}
