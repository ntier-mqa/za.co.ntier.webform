package za.co.ntier.webform.form.bean.component;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class CetTvetMultiLineInput extends AnnexureInfo{
	public static final String colRequestedProgramme = "Requested Programme";
	public static final String colManagers = "Number of managers";
	public static final String colFieldStudy = "Field of Study";
	public static final String colLearners = "Number of learners";
	
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
		CetTvetMultiLineInput cetTvetMultiLineInput = AnnexureInfo.getAnnexureInfoOneLine(CetTvetMultiLineInput.class, sectionHeader, columnInfos, null, true, null);
		cetTvetMultiLineInput.setShowAddButton(true);
		return cetTvetMultiLineInput;
	}

}
