package za.co.ntier.webform.form.bean.component;

import java.util.List;
import java.util.Map;

import org.compiere.model.MTable;
import org.compiere.model.Query;
import org.compiere.util.Env;

import za.co.ntier.api.model.X_ZZDocumentUpload;
import za.co.ntier.api.model.X_ZZDocumentUploadFile;
import za.co.ntier.api.model.X_ZZ_Application_Form;
import za.co.ntier.webform.form.AttachmentUtil;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.DataType;

public class UploadDocComponent implements ISaveForm {
	private X_ZZ_Application_Form applicationForm;

	private UploadInput uploadDoc;

	public UploadDocComponent(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm) {
		uploadDoc = UploadInput.getUploadInput(menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID());
		this.applicationForm = applicationForm;
		initComponent(applicationForm);
	}

	/**
	 * @return the sdf
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
	
	/*
	public void initComponent(X_ZZ_Application_Form sdf) {
		ColumnModel uploadDefCol = UploadInput.lookupColByDataType(DataType.DocUploadDef, uploadDoc);
		ColumnModel uploadFileCol = UploadInput.lookupColByDataType(DataType.FileUpload, uploadDoc);
		
		this.applicationForm = sdf;
		if(sdf != null) {
			Query uploadDocQuery = MTable.get(X_ZZDocumentUploadFile.Table_ID).createQuery(
					String.format("%s = ?", I_ZZ_Application_Form.COLUMNNAME_ZZ_Application_Form_ID)
					, null);
			
			List<X_ZZDocumentUploadFile> documentUploadFiles = uploadDocQuery.
					setOrderBy(I_ZZDocumentUploadFile.COLUMNNAME_ZZDocumentUploadFile_ID).
					setParameters(sdf.getZZ_Application_Form_ID()).list();
			
			for(Map<ColumnModel, Object> rowObj : uploadDoc.getRows()) {
				RowModel row = (RowModel)rowObj;
				
				X_ZZDocumentUpload docDef = (X_ZZDocumentUpload)row.get(uploadDefCol);
				
				for (X_ZZDocumentUploadFile documentUploadFile : documentUploadFiles) {
					if (documentUploadFile.getZZDocumentUpload_ID() == docDef.getZZDocumentUpload_ID()) {
						UploadCellModel uploadData = (UploadCellModel) row.get(uploadFileCol);
						uploadData.setFileName(documentUploadFile.getName());
						row.setData(documentUploadFile);
						break;
					}
				}
			}
		}
	}
	*/
	
	public void initComponent(X_ZZ_Application_Form applicationForm) {
	    ColumnInfo<?> uploadDefCol  = UploadInput.lookupColByDataType(DataType.DocUploadDef, uploadDoc);
	    ColumnInfo<?> uploadFileCol = UploadInput.lookupColByDataType(DataType.FileUpload,  uploadDoc);

	    this.applicationForm = applicationForm;
	    if (applicationForm != null) {
	        Query uploadDocQuery = MTable.get(Env.getCtx(), X_ZZDocumentUploadFile.Table_Name).createQuery(
	            String.format("%s = ?", X_ZZ_Application_Form.COLUMNNAME_ZZ_Application_Form_ID), null
	        );

	        List<X_ZZDocumentUploadFile> documentUploadFiles = uploadDocQuery
	            .setOrderBy(X_ZZDocumentUploadFile.COLUMNNAME_ZZDocumentUploadFile_ID)
	            .setParameters(applicationForm.getZZ_Application_Form_ID())
	            .list();

	        for (Map<ColumnInfo<?>, Object> rowObj : uploadDoc.getRows()) {
	            AnnexureRow row = (AnnexureRow) rowObj;
	            X_ZZDocumentUpload docDef = (X_ZZDocumentUpload) row.get(uploadDefCol);

	            for (X_ZZDocumentUploadFile documentUploadFile : documentUploadFiles) {
	                if (documentUploadFile.getZZDocumentUpload_ID() == docDef.getZZDocumentUpload_ID()) {
	                    UploadData uploadData = (UploadData) row.get(uploadFileCol);

	                    // fetch filename from the attachment entry for this record
	                    String attFileName = AttachmentUtil.getFileNameFromAttachmentEntries(
	                        documentUploadFile,
	                        uploadFileCol.getBtText(),                 // used as prefix when saving
	                        documentUploadFile.get_TrxName()
	                    );

	                    // fallback to column if no attachment/entry found
	                    uploadData.setFileName(attFileName != null && !attFileName.isEmpty()
	                        ? attFileName
	                        : documentUploadFile.getName());

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
	        AnnexureRow row = (AnnexureRow) uploadRow;

	        ColumnInfo<?> uploadDefCol  = UploadInput.lookupColByDataType(DataType.DocUploadDef, uploadDoc);
	        ColumnInfo<?> uploadFileCol = UploadInput.lookupColByDataType(DataType.FileUpload,  uploadDoc);

	        X_ZZDocumentUploadFile docUploadedFile = (X_ZZDocumentUploadFile) row.getData();
	        UploadData uploadData = (UploadData) uploadRow.get(uploadFileCol);

	        // Pull in-memory payload
	        byte[] bytes = (uploadData != null) ? uploadData.getBytes() : null;
	        String name  = (uploadData != null) ? uploadData.getFileName() : null;

	        // Only act when there is a new upload this round
	        if (bytes == null || bytes.length == 0) {
	        	if (docUploadedFile != null) {
	        		docUploadedFile.deleteEx(true, trxName);
	        		//AttachmentUtil.removeAttachmentEntry(docUploadedFile, uploadFileCol.getBtText(), trxName);
	        	}
	        }else if (bytes != null && bytes.length > 0 && org.apache.commons.lang3.StringUtils.isNotBlank(name)) {

	            // Create the row if needed
	            if (docUploadedFile == null) {
	                docUploadedFile = new X_ZZDocumentUploadFile(Env.getCtx(), 0, trxName);
	                row.setData(docUploadedFile);
	            }

	            // Set FK fields
	            docUploadedFile.setZZ_Application_Form_ID(applicationForm.getZZ_Application_Form_ID());
	            if (uploadDefCol != null) {
	                X_ZZDocumentUpload docDef = (X_ZZDocumentUpload) uploadRow.get(uploadDefCol);
	                docUploadedFile.setZZDocumentUpload_ID(docDef.getZZDocumentUpload_ID());
	            }

	            // Set display name (kept for the list/grid)
	            docUploadedFile.setName(name);

	            // Persist the record so it has an ID for AD_Attachment link
	            docUploadedFile.saveEx(trxName);

	            // === Attach the file (exactly one entry per record) ===
	            // Uses your helper that deletes any existing attachment and creates a fresh one.
	            AttachmentUtil.addOrReplaceAttachmentEntry(docUploadedFile, name, bytes, uploadFileCol.getBtText(), trxName);

	            // Free memory after persisting
	            uploadData.setBytes(null);
	        }

	        // If no new bytes: leave existing rows/attachments untouched.
	        // (If you want "clear file" to delete attachment, add a branch here to delete MAttachment.)
	    }
	}


	/**
	 * @param sdf the sdf to set
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
