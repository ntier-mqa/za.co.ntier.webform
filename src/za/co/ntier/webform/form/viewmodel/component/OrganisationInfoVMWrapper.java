package za.co.ntier.webform.form.viewmodel.component;

import java.io.IOException;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.InputEvent;

import za.co.ntier.webform.form.bean.component.OrganisationInfo;

@Init(superclass = true)
public class OrganisationInfoVMWrapper extends ComponentVMWrapper<OrganisationInfo>{
	@Command
	public void sdlNumberChange(@ContextParam(ContextType.TRIGGER_EVENT) InputEvent event) {
		getComponent().sdlNumberChange(event);
	}
	@Command
 	public void uploadFile(@BindingParam("media") Media media) throws IOException {
 		getComponent().uploadFile(media);
 	}
	
	
	private boolean collegeTouched;

    
    public boolean isCollegeTouched() { return collegeTouched; }
    public za.co.ntier.webform.form.bean.component.OrganisationInfo getComponent() { return component; }

    @Command
    @NotifyChange("collegeTouched")
    public void ensureCollegeSelected() {
        // mark the field as touched so the inline label can appear
        collegeTouched = true;

        // only enforce when college is required (useBpartner == true)
      //  if (component.isUseBpartner() && component.getCetTvetCollegeSelected() == null) {
            // show an immediate toast; anchor to center or the next field (either is fine)
         //   Clients.showNotification("Please select a college", "error", null, "end_center", 2500, true);
       // }
    }

    // keep your validator for submit-time checks
    public org.zkoss.bind.Validator getCollegeRequiredValidator() {
        return new org.zkoss.bind.validator.AbstractValidator() {
            @Override public void validate(org.zkoss.bind.ValidationContext ctx) {
                if (component.isUseBpartner() && ctx.getProperty().getValue() == null) {
                    addInvalidMessage(ctx, "Please select a college");
                }
            }
        };
    }
}
