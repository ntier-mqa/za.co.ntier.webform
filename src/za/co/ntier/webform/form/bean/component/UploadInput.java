package za.co.ntier.webform.form.bean.component;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.compiere.model.MTable;
import org.compiere.util.Env;

import za.co.ntier.webform.model.I_ZZDocumentUpload;
import za.co.ntier.webform.model.X_ZZDocumentUpload;

public class UploadInput extends AnnexureInfo {

	public static UploadInput getUploadInput(int programMasterDataID) throws NoSuchMethodException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// query upload info
		List<X_ZZDocumentUpload> docUploads = MTable.get(Env.getCtx(), I_ZZDocumentUpload.Table_ID)
				.createQuery(I_ZZDocumentUpload.COLUMNNAME_ZZ_Program_Master_Data_ID + " = ?", null)
				.setParameters(programMasterDataID).list();

		List<ColumnInfo<?>> columns = new ArrayList<>();
		columns.add(ColumnInfo.getColLabel("Document Name"));
		columns.add(ColumnInfo.getColFileUpload("", "doc"));
		
		UploadInput uploadInput = AnnexureInfo.getAnnexureInfo(UploadInput.class, columns, false);

		Map<ColumnInfo<?>, Object> rowDataInits = new HashMap<>();

		for (X_ZZDocumentUpload docUpload : docUploads) {
			rowDataInits.put(columns.get(0), docUpload.getName());
			Map<ColumnInfo<?>, Object> newRow = uploadInput.createDetailRow(columns, rowDataInits);
			uploadInput.getRows().add(newRow);
		}
		return uploadInput;
	}
}
