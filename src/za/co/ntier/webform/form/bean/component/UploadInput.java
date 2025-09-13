package za.co.ntier.webform.form.bean.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.compiere.model.MTable;
import org.compiere.util.Env;

import za.co.ntier.webform.model.I_ZZDocumentUpload;
import za.co.ntier.webform.model.X_ZZDocumentUpload;
import za.co.ntier.webform.model.X_ZZDocumentUploadFile;

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
		
		Supplier<Map<ColumnInfo<?>, Object>> supplierRowAnnexure = () -> new AnnexureRow<X_ZZDocumentUploadFile>();
		
		UploadInput uploadInput = AnnexureInfo.getAnnexureInfo(UploadInput.class, columns, false);
		uploadInput.setSupplier(supplierRowAnnexure);

		for (X_ZZDocumentUpload docUpload : docUploads) {
			Map<ColumnInfo<?>, Object> row = uploadInput.createDetailRow(columns);
			row.put(columns.get(0), docUpload);
		}
		return uploadInput;
	}
}
