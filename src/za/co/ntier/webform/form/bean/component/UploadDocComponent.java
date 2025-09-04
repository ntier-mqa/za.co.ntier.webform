package za.co.ntier.webform.form.bean.component;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.compiere.util.Env;

import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.DataType;
import za.co.ntier.webform.model.X_ZZDocumentUpload;
import za.co.ntier.webform.model.X_ZZDocumentUploadFile;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class UploadDocComponent implements ISaveForm {
	private UploadInput uploadDoc;

	public UploadDocComponent(MenuContextInfo menuContextInfo) throws NoSuchMethodException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		setUploadDoc(UploadInput.getUploadInput(menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID()));
	}

	/**
	 * @return the uploadDoc
	 */
	public UploadInput getUploadDoc() {
		return uploadDoc;
	}

	/**
	 * @param uploadDoc the uploadDoc to set
	 */
	public void setUploadDoc(UploadInput uploadDoc) {
		this.uploadDoc = uploadDoc;
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) throws IOException {
		for (Map<ColumnInfo<?>, Object> uploadRow : uploadDoc.getRows()) {
			ColumnInfo<?> uploadDefCol = UploadInput.lookupColByDataType(DataType.DocUploadDef, uploadDoc);
			ColumnInfo<?> uploadFileCol = UploadInput.lookupColByDataType(DataType.FileUpload, uploadDoc);
			
			X_ZZDocumentUploadFile docUploadedFile = new X_ZZDocumentUploadFile(Env.getCtx(), 0, trxName);
			docUploadedFile.setZZ_Application_Form_ID(applicationForm.getZZ_Application_Form_ID());
			
			if (uploadDefCol != null) {
				X_ZZDocumentUpload docDef = (X_ZZDocumentUpload) uploadRow.get(uploadDefCol);
				docUploadedFile.setZZDocumentUpload_ID(docDef.getZZDocumentUpload_ID());
			}
			
			UploadData uploadData = (UploadData) uploadRow.get(uploadFileCol);
			
			if (uploadData != null && StringUtils.isNoneEmpty(uploadData.getFullPath())) {
				docUploadedFile.setZZDocumentUploaded(Files.readAllBytes(Paths.get(uploadData.getFullPath())));
				docUploadedFile.setName(uploadData.getFileName());
				docUploadedFile.saveEx(trxName);
			}
			
		}
		
	}

}
