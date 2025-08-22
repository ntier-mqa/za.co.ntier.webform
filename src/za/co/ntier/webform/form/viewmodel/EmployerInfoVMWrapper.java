package za.co.ntier.webform.form.viewmodel;

import java.io.IOException;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.media.Media;

import za.co.ntier.webform.form.bean.EmployerInfo;

public class EmployerInfoVMWrapper {
	private EmployerInfo employerInfo;

	/**
	 * @return the employerInfo
	 */
	public EmployerInfo getEmployerInfo() {
		return employerInfo;
	}

	@Init
	public void init(@ExecutionArgParam("employerInfo") EmployerInfo employerInfo) {
		this.employerInfo = employerInfo;
	}

	/**
	 * @param employerInfo the employerInfo to set
	 */
	public void setEmployerInfo(EmployerInfo employerInfo) {
		this.employerInfo = employerInfo;
	}
	
	@Command
	public void uploadFile(@BindingParam("media") Media media) throws IOException {
		employerInfo.uploadFile(media);
	}
	
	@Command
	public void sdlNumberChange() {
		employerInfo.sdlNumberChange();
	}
}
