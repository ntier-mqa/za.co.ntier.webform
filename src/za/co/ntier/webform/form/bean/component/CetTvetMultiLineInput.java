package za.co.ntier.webform.form.bean.component;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import za.co.ntier.webform.form.bean.DataType;

public class CetTvetMultiLineInput extends AnnexureInfo{
	public static final String colRequestedProgrammeTitle = "Requested Programme";
	public static final String colNoManagersTitle = "Number of managers";
	public static final String colFieldStudyTitle = "Field of Study";
	public static final String colNoLearners = "Number of learners";
	
	/**
	 * for CetTvet sub, show total but not row header
	 *
	 * @param sectionHeader
	 * @param columnInfos
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws NoSuchMethodException
	 */
	public static CetTvetMultiLineInput getCetTvetMultiLineInput(String sectionHeader,
			List<ColumnInfo<?>> columnInfos) throws NoSuchMethodException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		boolean isShowTotalLine = false;
		for (ColumnInfo<?> col : columnInfos) {
			if (col.getDataType() == DataType.PositiveNumber) {
				isShowTotalLine = true;
				break;
			}
		}
		
		CetTvetMultiLineInput cetTvetMultiLineInput = AnnexureInfo.getAnnexureInfoOneLine(CetTvetMultiLineInput.class, sectionHeader, columnInfos, null, isShowTotalLine, null);
		
		cetTvetMultiLineInput.setShowAddButton(true);
		
		
		return cetTvetMultiLineInput;
	}

}
