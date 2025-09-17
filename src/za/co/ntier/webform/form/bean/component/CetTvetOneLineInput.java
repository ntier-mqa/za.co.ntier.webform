package za.co.ntier.webform.form.bean.component;

import java.util.List;
import java.util.function.Function;

public class CetTvetOneLineInput extends AnnexureInfo{
	public static CetTvetOneLineInput getCetTvetOneLineInput(String sectionHeader,
			List<ColumnInfo<?>> columnInfos, Function<AnnexureInfo, AnnexureRow<?>> emptyRowSupplier) {
		
		CetTvetOneLineInput annexureInfo = AnnexureInfo.getAnnexureInfo(CetTvetOneLineInput.class, columnInfos, false);
		annexureInfo.setSectionHeader(sectionHeader);
		
		annexureInfo.setSupplier(emptyRowSupplier);
		
		return annexureInfo;
	}

}
