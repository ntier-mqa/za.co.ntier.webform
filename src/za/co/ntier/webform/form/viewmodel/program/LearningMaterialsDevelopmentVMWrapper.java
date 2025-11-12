package za.co.ntier.webform.form.viewmodel.program;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.compiere.model.MAttachment;
import org.compiere.model.PO;
import org.compiere.util.Env;
import org.idempiere.ui.zk.media.Medias;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.media.AMedia;
import org.zkoss.zul.Filedownload;

import za.co.ntier.api.model.X_ZZ_Program_Master_Data;
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
