package za.co.ntier.webform.form.viewmodel.component;

import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.validator.AbstractValidator;

import za.co.ntier.webform.form.bean.component.EmployerDeclarationInfo;

public class EmployerDeclarationInfoVMWrapper {
	private EmployerDeclarationInfo employerDeclarationInfo;

	public EmployerDeclarationInfo getEmployerDeclarationInfo() {
		return employerDeclarationInfo;
	}

	@Init
	public void init(@ExecutionArgParam("employerDeclarationInfo") EmployerDeclarationInfo employerDeclarationInfo) {
		this.employerDeclarationInfo = employerDeclarationInfo;
	}

	public void setEmployerDeclarationInfo(EmployerDeclarationInfo employerDeclarationInfo) {
		this.employerDeclarationInfo = employerDeclarationInfo;
	}
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
