package za.co.ntier.webform.form.bean.component;

import java.util.List;
import java.util.function.Function;

public class CetTvetOneLineInput extends AnnexureInfo{
	public static final String colDisciplineTitle = "Discipline Applying For";
	public static final String colNoBeneficiariesTitle = "Number Of Beneficiaries Applying For";
	public static final String colProgrammeApplyTitle = "Programme Applying For";
	public static final String colTotalNoBeneficiariesTitle = "Total Number of beneficiaries applying for";
	

	public static CetTvetOneLineInput getCetTvetOneLineInput(String sectionHeader,
			List<ColumnInfo<?>> columnInfos, Function<AnnexureInfo, AnnexureRow<?>> emptyRowSupplier) {
		
		CetTvetOneLineInput annexureInfo = AnnexureInfo.getAnnexureInfo(CetTvetOneLineInput.class, columnInfos, false);
		annexureInfo.setSectionHeader(sectionHeader);
		
		annexureInfo.setSupplier(emptyRowSupplier);
		
		return annexureInfo;
	}

}
