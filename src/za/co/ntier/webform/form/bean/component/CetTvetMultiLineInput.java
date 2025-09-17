package za.co.ntier.webform.form.bean.component;

import java.util.List;
import java.util.function.Function;

import za.co.ntier.webform.form.bean.DataType;

public class CetTvetMultiLineInput extends AnnexureInfo{
	/**
	 * for CetTvet sub, show total but not row header
	 *
	 * @param sectionHeader
	 * @param columnInfos
	 * @return
	 */
	public static <T> CetTvetMultiLineInput getCetTvetMultiLineInput(String sectionHeader, List<ColumnInfo<?>> columnInfos, Function<AnnexureInfo, AnnexureRow<?>> emptyRowSupplier) {
		
		boolean isShowTotalLine = false;
		for (ColumnInfo<?> col : columnInfos) {
			if (col.getDataType() == DataType.PositiveNumber) {
				isShowTotalLine = true;
				break;
			}
		}
		
		CetTvetMultiLineInput annexureInfo = AnnexureInfo.getAnnexureInfo(CetTvetMultiLineInput.class, columnInfos, isShowTotalLine);
		
		annexureInfo.setSupplier(emptyRowSupplier);
				
		annexureInfo.setSectionHeader(sectionHeader);
		
		annexureInfo.setShowAddButton(true);
		
		return annexureInfo;
	}

}
