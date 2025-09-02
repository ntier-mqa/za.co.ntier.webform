package za.co.ntier.webform.form.viewmodel.component;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

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
}
