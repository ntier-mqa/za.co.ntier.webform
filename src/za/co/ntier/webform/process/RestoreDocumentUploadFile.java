package za.co.ntier.webform.process;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MAttachment;
import org.compiere.model.MAttachmentEntry;
import org.compiere.model.MChangeLog;
import org.compiere.model.MClientInfo;
import org.compiere.model.MStorageProvider;
import org.compiere.model.MTable;
import org.compiere.model.Query;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Util;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import za.co.ntier.api.model.I_ZZDocumentUpload;
import za.co.ntier.api.model.I_ZZDocumentUploadFile;
import za.co.ntier.api.model.X_ZZDocumentUpload;
import za.co.ntier.api.model.X_ZZDocumentUploadFile;
import za.co.ntier.api.model.X_ZZ_Application_Form;
import za.co.ntier.webform.form.AttachmentUtil;

@org.adempiere.base.annotation.Process(name="za.co.ntier.webform.process.RestoreDocumentUploadFile")
public class RestoreDocumentUploadFile extends SvrProcess{
	
	private static CLogger	log = CLogger.getCLogger (RestoreDocumentUploadFile.class);
	
	@Override
	protected void prepare() {
		
		
	}

	MTable documentUploadFileTable = MTable.get(getCtx(), I_ZZDocumentUploadFile.Table_Name);
	MTable appFormTable = MTable.get(getCtx(), X_ZZ_Application_Form.Table_Name);
	
	List<List<Object>> queryChangeLogForDocumentUploadFile(){
		int documentUploadFileTableID = documentUploadFileTable.getAD_Table_ID();
		
		// 1. select all X_ZZDocumentUploadFile deleted
		String sqlQueryDocUploadDetails = String.format("""
				SELECT %s, %s, %s, %s
				FROM %s 
				WHERE %s = ? AND %s = ?
				ORDER BY %s 
				""",
				MChangeLog.COLUMNNAME_Record_ID, MChangeLog.COLUMNNAME_OldValue, MChangeLog.COLUMNNAME_AD_Column_ID, MChangeLog.COLUMNNAME_Updated
				, MChangeLog.Table_Name
				, MChangeLog.COLUMNNAME_AD_Table_ID, MChangeLog.COLUMNNAME_EventChangeLog
				, MChangeLog.COLUMNNAME_Record_ID);
		
		log.info("sqlQueryDocUploadDetails:");
		log.info(sqlQueryDocUploadDetails);
		log.info("documentUploadFileTableID:");
		log.info(String.valueOf(documentUploadFileTableID));
	
		return DB.getSQLArrayObjectsEx(null, sqlQueryDocUploadDetails, documentUploadFileTableID, MChangeLog.EVENTCHANGELOG_Delete);
	}
		
	List<X_ZZDocumentUploadFile> recreateDeletedDocumentUploadFile(List<List<Object>> docUploadDetails){
		int currentRecordId = 0;
		
		List<X_ZZDocumentUploadFile> docUploadFiles = new ArrayList<X_ZZDocumentUploadFile>();
		
		X_ZZDocumentUploadFile docUploadFile = null;
		boolean isIgnoreCurrentId = false;
		for (List<Object> docUploadDetail : docUploadDetails) {
			int recordId = ((BigDecimal)docUploadDetail.get(0)).intValueExact();
			
			if (recordId == currentRecordId && isIgnoreCurrentId) {
				continue;
			}else {
				isIgnoreCurrentId = false;
			}
			
			if (currentRecordId != 0 && recordId != currentRecordId) {// move to next record_id so enough info to build attachment path
				StringBuilder path = new StringBuilder();
				path.append(docUploadFile.getAD_Client_ID()).append(File.separator)
						.append(docUploadFile.getAD_Org_ID()).append(File.separator)
						.append(documentUploadFileTable.getAD_Table_ID()).append(File.separator)
						.append(currentRecordId);
				String fileName = AttachmentUtil.normalizedEntryNames("doc", docUploadFile.getName()).newEntryName();
				path.append(File.separator).append(fileName);
				
				docUploadFile.setZZOldAttachmentPath(path.toString());
				log.info("path:" + docUploadFile.getZZOldAttachmentPath());
			}
			
			if (currentRecordId == 0 || recordId != currentRecordId) {
				docUploadFile = new X_ZZDocumentUploadFile(getCtx(), 0, null);
				docUploadFiles.add(docUploadFile);
				
				Timestamp updated = (Timestamp)docUploadDetail.get(3);
				docUploadFile.setT_DateTime(updated);
				
				docUploadFile.setOldValue(Integer.toString(recordId));
				currentRecordId = recordId;
			}
			
			
			String value = (String)docUploadDetail.get(1);
			int columnId = ((BigDecimal)docUploadDetail.get(2)).intValueExact();
			
			if (columnId == documentUploadFileTable.getColumn(I_ZZDocumentUploadFile.COLUMNNAME_ZZ_Application_Form_ID).getAD_Column_ID()) {
				docUploadFile.setZZ_Application_Form_ID(Integer.valueOf(value));
			}else if (columnId == documentUploadFileTable.getColumn(I_ZZDocumentUploadFile.COLUMNNAME_AD_Org_ID).getAD_Column_ID()) {
				docUploadFile.setAD_Org_ID(Integer.valueOf(value));
			}else if (columnId == documentUploadFileTable.getColumn(I_ZZDocumentUploadFile.COLUMNNAME_Name).getAD_Column_ID()) {
				docUploadFile.setName(value);
			}else if (columnId == documentUploadFileTable.getColumn(I_ZZDocumentUploadFile.COLUMNNAME_ZZDocumentUpload_ID).getAD_Column_ID()) {
				X_ZZDocumentUpload docUploadDef = (X_ZZDocumentUpload)MTable.get(getCtx(), I_ZZDocumentUpload.Table_Name).getPO(Integer.valueOf(value), null);
				if (docUploadDef == null) {
					log.warning(I_ZZDocumentUpload.Table_Name + " deleted for id:" + value + " so don't re-create for:" + docUploadFile.getZZOldAttachmentPath());
					addLog(I_ZZDocumentUpload.Table_Name + " deleted for id:" + value + " so don't re-create for:" + docUploadFile.getZZOldAttachmentPath());
					isIgnoreCurrentId = true;
					log.warning("");
					docUploadFiles.remove(docUploadFile);
				}else {
					docUploadFile.setZZDocumentUpload_ID(Integer.valueOf(value));
				}
			}else {
				//addLog("column id:" + columnId);
			}
		}
		
		return docUploadFiles;
	}
	@Override
	protected String doIt() throws Exception {
		List<List<Object>> docUploadDetails = queryChangeLogForDocumentUploadFile();
			
		List<X_ZZDocumentUploadFile> docUploadFiles = recreateDeletedDocumentUploadFile(docUploadDetails);
		
		// sort to older delete is before newer 
		docUploadFiles.sort((o1, o2) -> {
			if (o1.getZZ_Application_Form_ID() != o2.getZZ_Application_Form_ID()) {// order by app id first
				return o1.getZZ_Application_Form_ID() - o2.getZZ_Application_Form_ID();
			}
			
			if (o1.getZZDocumentUpload_ID() != o2.getZZDocumentUpload_ID()) {// order by DocumentUpload 
				return o1.getZZDocumentUpload_ID() - o2.getZZDocumentUpload_ID();
			}
			
			long compare = (o1.getT_DateTime().getTime() - o2.getT_DateTime().getTime());// order by time deleted
			
			int compareTime = 0;
			
			if (compare > 0)
				compareTime = 1;
			
			if (compare < 0)
				compareTime = -1;
			
			return compareTime;
		});
		
		int count = 0;
		for (int index = 0; index < docUploadFiles.size(); index++) {
			X_ZZDocumentUploadFile docUploadFileTest = docUploadFiles.get(index);
			
			X_ZZ_Application_Form appForm = (X_ZZ_Application_Form)appFormTable.getPO(docUploadFileTest.getZZ_Application_Form_ID(), null);
			if (appForm == null) {// its appform is deleted also so don't need to recover
				
				continue;
			}
			
			Query queryCurrentAttach = MTable.get(getCtx(), X_ZZDocumentUploadFile.Table_Name).createQuery(String.format("""
					%s = ? AND %s = ?
					""", X_ZZDocumentUploadFile.COLUMNNAME_ZZ_Application_Form_ID
					, X_ZZDocumentUploadFile.COLUMNNAME_ZZDocumentUpload_ID), null);
			
			queryCurrentAttach.setParameters(docUploadFileTest.getZZ_Application_Form_ID(), docUploadFileTest.getZZDocumentUpload_ID());
			X_ZZDocumentUploadFile currentAttach = queryCurrentAttach.firstOnly();
			if (currentAttach != null) {// app have new attach for ZZDocumentUpload_ID so don't need recover ZZDocumentUploadFile for same ZZDocumentUpload_ID
				log.warning("exists new attachment so don't need to restore for:" + docUploadFileTest.getZZOldAttachmentPath());
				addBufferLog(0, null, null, "don't need to re-create for:" + docUploadFileTest.getZZOldAttachmentPath(), currentAttach.get_Table_ID(), currentAttach.getZZDocumentUploadFile_ID());					
				continue;
			}
			
			if(index < docUploadFiles.size() - 1) {
				X_ZZDocumentUploadFile nextDocUploadFiles = docUploadFiles.get(index + 1);
				if (docUploadFileTest.getZZ_Application_Form_ID() == nextDocUploadFiles.getZZ_Application_Form_ID() 
						&&  docUploadFileTest.getZZDocumentUpload_ID() == nextDocUploadFiles.getZZDocumentUpload_ID()) {
					if (nextDocUploadFiles.getT_DateTime().before(docUploadFileTest.getT_DateTime())) {
						throw new AdempiereException("wrong order");
					}
					log.warning("exists deleted newer so don't need to re-create for:" + docUploadFileTest.getZZOldAttachmentPath());
					continue;
				}
			}
			
			docUploadFileTest.saveEx(get_TrxName());
			
			AttachmentUtil.addOrReplaceAttachmentEntry(docUploadFileTest, docUploadFileTest.getName(), new byte[] {0}, "doc", get_TrxName());
			
			StringBuilder path = new StringBuilder();
			path.append(docUploadFileTest.getAD_Client_ID()).append(File.separator)
					.append(docUploadFileTest.getAD_Org_ID()).append(File.separator)
					.append(documentUploadFileTable.getAD_Table_ID()).append(File.separator)
					.append(docUploadFileTest.getZZDocumentUploadFile_ID());
			String fileName = AttachmentUtil.normalizedEntryNames("doc", docUploadFileTest.getName()).newEntryName();
			path.append(File.separator).append(fileName);
			
			String newBuildPath = path.toString();
			
			/*
			 * MAttachment att = MAttachment.get(Env.getCtx(),
			 * docUploadFileTest.get_Table_ID(), docUploadFileTest.get_ID(), get_TrxName());
			 * if (att != null) { String newPath = getFilePath(att); if
			 * (!newBuildPath.equals(newPath)){ throw new
			 * AdempiereException("build new path wrong"); } }
			 */
			
			docUploadFileTest.setZZAttachmentPath(newBuildPath);
			docUploadFileTest.saveEx(get_TrxName());
			count++;
			
			
			
		}
		
		addLog("total restore:" + count);
		//MChangeLog deleted = MChangeLog.
		return null;
	}
		
	String attachmentPathRoot = null;
	private String getAttachmentPathRoot(MStorageProvider prov) {
		if (attachmentPathRoot != null)
			return attachmentPathRoot;
		
		attachmentPathRoot = prov.getFolder();
		if (attachmentPathRoot == null)
			attachmentPathRoot = "";
		if (Util.isEmpty(attachmentPathRoot)) {
			log.severe("no attachmentPath defined");
		} else if (!attachmentPathRoot.endsWith(File.separator)){
			attachmentPathRoot = attachmentPathRoot + File.separator;
			log.fine(attachmentPathRoot);
		}
		
		return attachmentPathRoot;
	}
	
	MStorageProvider storageProvider;
	private MStorageProvider getStorageProvider(Properties ctx)
	{
		if (storageProvider == null) {
			MClientInfo clientInfo = MClientInfo.get(ctx, getAD_Client_ID());
			storageProvider = MStorageProvider.get(ctx, clientInfo.getAD_StorageProvider_ID());
		}
		
		return storageProvider;
			
	}
	
	public String getFilePath(MAttachment attach) {
		MStorageProvider prov = getStorageProvider(getCtx());
		//String attachmentPathRoot = getAttachmentPathRoot(prov);
		attachmentPathRoot = "";
		// Reset
		attach.m_items = new ArrayList<MAttachmentEntry>();
		//
		byte[] data = attach.getBinaryData();
		if (data == null)
			return null;
		if (log.isLoggable(Level.FINE)) log.fine("TextFileSize=" + data.length);
		if (data.length == 0)
			return null;

		NodeList entries = getEntriesFromXML(data);
		for (int i = 0; i < entries.getLength(); i++) {
			final Node entryNode = entries.item(i);
			final NamedNodeMap attributes = entryNode.getAttributes();
			final Node fileNode = attributes.getNamedItem("file");
			final Node nameNode = attributes.getNamedItem("name");
			if(fileNode==null || nameNode==null){
				log.severe("no filename for entry " + i);
				attach.m_items = null;
				return null;
			}
			if (log.isLoggable(Level.FINE)) log.fine("name: " + nameNode.getNodeValue());
			String filePath = fileNode.getNodeValue();
			if (log.isLoggable(Level.FINE)) log.fine("filePath: " + filePath);
			if(filePath!=null){
				filePath = filePath.replaceFirst(attach.ATTACHMENT_FOLDER_PLACEHOLDER, attachmentPathRoot.replaceAll("\\\\","\\\\\\\\"));
				//just to be sure...
				String replaceSeparator = File.separator;
				if(!replaceSeparator.equals("/")){
					replaceSeparator = "\\\\";
				}
				filePath = filePath.replaceAll("/", replaceSeparator);
				filePath = filePath.replaceAll("\\\\", replaceSeparator);
			}
			if (log.isLoggable(Level.FINE)) log.fine("filePath: " + filePath);
			
			return filePath;
		}

		return null;
	}
	
	private NodeList getEntriesFromXML(byte[] data) {
		NodeList entries = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
			final DocumentBuilder builder = factory.newDocumentBuilder();
			final Document document = builder.parse(new ByteArrayInputStream(data));
			entries = document.getElementsByTagName("entry");
		} catch (SAXException sxe) {
			// Error generated during parsing)
			Exception x = sxe;
			if (sxe.getException() != null)
				x = sxe.getException();
			x.printStackTrace();
			log.severe(x.getMessage());

		} catch (ParserConfigurationException pce) {
			// Parser with specified options can't be built
			pce.printStackTrace();
			log.severe(pce.getMessage());

		} catch (IOException ioe) {
			// I/O error
			ioe.printStackTrace();
			log.severe(ioe.getMessage());
		}
		return entries;
	}

	
}
