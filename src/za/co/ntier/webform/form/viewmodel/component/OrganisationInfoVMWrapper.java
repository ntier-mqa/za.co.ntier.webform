package za.co.ntier.webform.form.viewmodel.component;

import java.io.IOException;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.media.Media;

import za.co.ntier.webform.form.bean.component.OrganisationInfo;

@Init(superclass = true)
public class OrganisationInfoVMWrapper extends ComponentVMWrapper<OrganisationInfo>{
	@Command
	public void sdlNumberChange() {
		getComponent().sdlNumberChange();
	}
	@Command
 	public void uploadFile(@BindingParam("media") Media media) throws IOException {
 		getComponent().uploadFile(media);
 	}
}
