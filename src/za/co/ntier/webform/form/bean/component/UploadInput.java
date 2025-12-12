package za.co.ntier.webform.form.bean.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.compiere.model.MTable;
import org.compiere.util.Env;

import za.co.ntier.api.model.I_ZZDocumentUpload;
import za.co.ntier.api.model.X_ZZDocumentUpload;

public class UploadInput extends AnnexureInfo {
	public static UploadInput getUploadInput(int programMasterDataID){
		// query upload info
		List<X_ZZDocumentUpload> docUploads = MTable.get(Env.getCtx(), I_ZZDocumentUpload.Table_Name)
				.createQuery(I_ZZDocumentUpload.COLUMNNAME_ZZ_Program_Master_Data_ID + " = ?", null)
				.setParameters(programMasterDataID).list();

		List<ColumnInfo<?>> columns = new ArrayList<>();
		columns.add(ColumnInfo.getColDocUpload(ColumnInfo.DocUploadTitle));
		columns.add(ColumnInfo.getColFileUpload("", "doc"));
		
		UploadInput uploadInput = AnnexureInfo.getAnnexureInfo(UploadInput.class, columns, false);

		for (X_ZZDocumentUpload docUpload : docUploads) {
			Map<ColumnInfo<?>, Object> row = uploadInput.createDetailRow();
			row.put(columns.get(0), docUpload);
		}
		return uploadInput;
	}
}
