package za.co.ntier.webform.form.viewmodel.component;

import java.io.IOException;

import org.compiere.model.PO;
import org.idempiere.ui.zk.media.Medias;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zul.Filedownload;


public class DownloadButtonVMWrapper extends ComponentVMWrapper<Object>{
	private String label; 
	private String fileName;
	
	@Init(superclass = true)
	public void init(@ExecutionArgParam("label") String label, @ExecutionArgParam("fileName") String fileName) throws Exception {
		this.label = label;
		this.fileName = fileName;
	}
	
	@Command
	public void downloadFile() throws IOException {
		PO programDefine = (PO)getMenuContextInfo().getProgramMasterData();
		if (programDefine != null) {
			byte[] fileData = programDefine.getPdfAttachment();
			if (fileData != null) {
				Filedownload.save(fileData, Medias.PDF_MIME_TYPE, fileName);
			}
			
		}
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
