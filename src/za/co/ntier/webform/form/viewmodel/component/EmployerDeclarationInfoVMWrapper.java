package za.co.ntier.webform.form.viewmodel.component;

import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.validator.AbstractValidator;

import za.co.ntier.webform.form.bean.component.EmployerDeclarationInfo;

@Init(superclass = true)
public class EmployerDeclarationInfoVMWrapper extends ComponentVMWrapper<EmployerDeclarationInfo>{
	

	// Martin added to test mandotory field
	// expose the validator to ZUL
	public Validator getAckValidator() {
		return new AbstractValidator() {
			@Override
			public void validate(ValidationContext ctx) {
				Boolean v = (Boolean) ctx.getProperty().getValue();
				if (v == null || !v) {
					addInvalidMessage(ctx, "Please tick the acknowledgement checkbox before continuing.");
				}
			}
		};
	}

}
