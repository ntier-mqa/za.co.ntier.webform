package za.co.ntier.webform.form.bean.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.compiere.model.MTable;
import org.compiere.util.Env;

import za.co.ntier.webform.model.I_ZZDocumentUpload;
import za.co.ntier.webform.model.X_ZZDocumentUpload;

public class UploadInput extends AnnexureInfo {
	public static final String DocUploadTitle = "Document Name";
	public static UploadInput getUploadInput(int programMasterDataID){
		// query upload info
		List<X_ZZDocumentUpload> docUploads = MTable.get(Env.getCtx(), I_ZZDocumentUpload.Table_ID)
				.createQuery(I_ZZDocumentUpload.COLUMNNAME_ZZ_Program_Master_Data_ID + " = ?", null)
				.setParameters(programMasterDataID).list();

		List<ColumnInfo<?>> columns = new ArrayList<>();
		columns.add(ColumnInfo.getColDocUpload(DocUploadTitle));
		columns.add(ColumnInfo.getColFileUpload("", "doc"));
		
		UploadInput uploadInput = AnnexureInfo.getAnnexureInfo(UploadInput.class, columns, false);

		Map<ColumnInfo<?>, Object> rowDataInits = null;

		for (X_ZZDocumentUpload docUpload : docUploads) {
			rowDataInits = new HashMap<>();
			rowDataInits.put(columns.get(0), docUpload);
			Map<ColumnInfo<?>, Object> newRow = uploadInput.createDetailRow(columns, rowDataInits);
			uploadInput.getRows().add(newRow);
		}
		return uploadInput;
	}
}
