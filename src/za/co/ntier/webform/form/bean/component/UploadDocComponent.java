package za.co.ntier.webform.form.bean.component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.adempiere.webui.exception.ApplicationException;
import org.apache.commons.lang3.StringUtils;
import org.compiere.model.MTable;
import org.compiere.model.Query;
import org.compiere.util.Env;

import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.DataType;
import za.co.ntier.webform.model.X_ZZDocumentUpload;
import za.co.ntier.webform.model.X_ZZDocumentUploadFile;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class UploadDocComponent implements ISaveForm {
	private X_ZZ_Application_Form applicationForm;

	private UploadInput uploadDoc;

	public UploadDocComponent(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm) {
		uploadDoc = UploadInput.getUploadInput(menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID());
		this.applicationForm = applicationForm;
		initComponent(applicationForm);
	}

	/**
	 * @return the applicationForm
	 */
	public X_ZZ_Application_Form getApplicationForm() {
		return applicationForm;
	}

	/**
	 * @return the uploadDoc
	 */
	public UploadInput getUploadDoc() {
		return uploadDoc;
	}
	
	public void initComponent(X_ZZ_Application_Form applicationForm) {
		ColumnInfo<?> uploadDefCol = UploadInput.lookupColByDataType(DataType.DocUploadDef, uploadDoc);
		ColumnInfo<?> uploadFileCol = UploadInput.lookupColByDataType(DataType.FileUpload, uploadDoc);
		
		this.applicationForm = applicationForm;
		if(applicationForm != null) {
			Query uploadDocQuery = MTable.get(X_ZZDocumentUploadFile.Table_ID).createQuery(
					String.format("%s = ?", X_ZZ_Application_Form.COLUMNNAME_ZZ_Application_Form_ID)
					, null);
			
			List<X_ZZDocumentUploadFile> documentUploadFiles = uploadDocQuery.
					setOrderBy(X_ZZDocumentUploadFile.COLUMNNAME_ZZDocumentUploadFile_ID).
					setParameters(applicationForm.getZZ_Application_Form_ID()).list();
			
			for(Map<ColumnInfo<?>, Object> rowObj : uploadDoc.getRows()) {
				@SuppressWarnings("unchecked")
				AnnexureRow<X_ZZDocumentUploadFile> row = (AnnexureRow<X_ZZDocumentUploadFile>)rowObj;
				
				X_ZZDocumentUpload docDef = (X_ZZDocumentUpload)row.get(uploadDefCol);
				
				for (X_ZZDocumentUploadFile documentUploadFile : documentUploadFiles) {
					if (documentUploadFile.getZZDocumentUpload_ID() == docDef.getZZDocumentUpload_ID()) {
						UploadData uploadData = (UploadData) row.get(uploadFileCol);
						uploadData.setFileName(documentUploadFile.getName());
						row.setData(documentUploadFile);
						break;
					}
				}
			}
		}
	}
	
	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		for (Map<ColumnInfo<?>, Object> uploadRow : uploadDoc.getRows()) {
			@SuppressWarnings("unchecked")
			AnnexureRow<X_ZZDocumentUploadFile> row = (AnnexureRow<X_ZZDocumentUploadFile>)uploadRow;
			
			
			ColumnInfo<?> uploadDefCol = UploadInput.lookupColByDataType(DataType.DocUploadDef, uploadDoc);
			ColumnInfo<?> uploadFileCol = UploadInput.lookupColByDataType(DataType.FileUpload, uploadDoc);
			
			X_ZZDocumentUploadFile docUploadedFile = row.getData();
			if (docUploadedFile == null) {
				docUploadedFile = new X_ZZDocumentUploadFile(Env.getCtx(), 0, trxName);
				row.setData(docUploadedFile);
			}
			
			docUploadedFile.setZZ_Application_Form_ID(applicationForm.getZZ_Application_Form_ID());
			
			if (uploadDefCol != null) {
				X_ZZDocumentUpload docDef = (X_ZZDocumentUpload) uploadRow.get(uploadDefCol);
				docUploadedFile.setZZDocumentUpload_ID(docDef.getZZDocumentUpload_ID());
			}
			
			UploadData uploadData = (UploadData) uploadRow.get(uploadFileCol);
			
			if (uploadData != null && StringUtils.isNoneEmpty(uploadData.getFullPath())) {
				try {
					docUploadedFile.setZZDocumentUploaded(Files.readAllBytes(Paths.get(uploadData.getFullPath())));
				} catch (IOException e) {
					e.printStackTrace();
					throw new ApplicationException(e.getMessage(), e);
				}
				docUploadedFile.setName(uploadData.getFileName());
				docUploadedFile.saveEx(trxName);
			}
			
		}
		
	}

	/**
	 * @param applicationForm the applicationForm to set
	 */
	public void setApplicationForm(X_ZZ_Application_Form applicationForm) {
		this.applicationForm = applicationForm;
	}

	/**
	 * @param uploadDoc the uploadDoc to set
	 */
	public void setUploadDoc(UploadInput uploadDoc) {
		this.uploadDoc = uploadDoc;
	}

}
