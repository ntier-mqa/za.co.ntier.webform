package za.co.ntier.webform.form.viewmodel.program;

import java.io.IOException;

import org.compiere.model.PO;
import org.idempiere.ui.zk.media.Medias;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zul.Filedownload;

import za.co.ntier.webform.form.bean.program.LearningMaterialsDevelopment;

@Init(superclass = true)
public class LearningMaterialsDevelopmentVMWrapper extends ProgramVMWrapper<LearningMaterialsDevelopment>{
	@Command
	public void downloadFileAnnexureB() throws IOException {
		PO programDefine = (PO)getMenuContextInfo().getProgramMasterData();
		if (programDefine != null) {
			byte[] fileData = programDefine.getPdfAttachment();
			if (fileData != null) {
				Filedownload.save(fileData, Medias.PDF_MIME_TYPE, "AnnexureB.pdf");
			}
			
		}
	}
}
