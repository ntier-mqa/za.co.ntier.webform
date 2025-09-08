package za.co.ntier.webform.form.bean.component;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import za.co.ntier.webform.form.bean.DataType;

public class CetTvetMultiLineInput extends AnnexureInfo{
	public static final String colFieldStudyTitle = "Field of Study";
	public static final String colNoLearners = "Number of learners";
	public static final String colNoManagersTitle = "Number of managers";
	public static final String colRequestedProgrammeTitle = "Requested Programme";
	
	
	public static <T> CetTvetMultiLineInput getCetTvetMultiLineInput(String sectionHeader,
			List<ColumnInfo<?>> columnInfos) {
		return getCetTvetMultiLineInput(sectionHeader, columnInfos, null, null);
	}
	/**
	 * for CetTvet sub, show total but not row header
	 *
	 * @param sectionHeader
	 * @param columnInfos
	 * @return
	 */
	public static <T> CetTvetMultiLineInput getCetTvetMultiLineInput(String sectionHeader,
			List<ColumnInfo<?>> columnInfos, List<AnnexureRow<T>>  initRows, Supplier<Map<ColumnInfo<?>, Object>> emptyRowSupplier) {
		
		boolean isShowTotalLine = false;
		for (ColumnInfo<?> col : columnInfos) {
			if (col.getDataType() == DataType.PositiveNumber) {
				isShowTotalLine = true;
				break;
			}
		}
		
		CetTvetMultiLineInput annexureInfo = AnnexureInfo.getAnnexureInfo(CetTvetMultiLineInput.class, columnInfos, isShowTotalLine);
		annexureInfo.setSupplier(emptyRowSupplier);
		
		if (initRows == null || initRows.size() == 0) {
			Map<ColumnInfo<?>, Object> row = annexureInfo.createDetailRow(columnInfos);
			annexureInfo.getRows().add(row);
		}else {
			for (Map<ColumnInfo<?>, Object> initRow : initRows) {
				Map<ColumnInfo<?>, Object> row = annexureInfo.createDetailRow(columnInfos, initRow);
				annexureInfo.getRows().add(row);
			}
		}
				
		annexureInfo.setSectionHeader(sectionHeader);
		
		annexureInfo.setShowAddButton(true);
		
		
		return annexureInfo;
	}

}
