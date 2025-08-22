package za.co.ntier.webform.form.viewmodel;

import java.io.IOException;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.media.Media;

import za.co.ntier.webform.form.bean.OrganisationInfo;

public class OrganisationInfoVMWrapper {
	private OrganisationInfo organisationInfo;

	/**
	 * @return the organisationInfo
	 */
	public OrganisationInfo getOrganisationInfo() {
		return organisationInfo;
	}

	@Init
	public void init(@ExecutionArgParam("organisationInfo") OrganisationInfo organisationInfo) {
		this.organisationInfo = organisationInfo;
	}

	/**
	 * @param organisationInfo the organisationInfo to set
	 */
	public void setOrganisationInfo(OrganisationInfo organisationInfo) {
		this.organisationInfo = organisationInfo;
	}
	
	@Command
	public void uploadFile(@BindingParam("media") Media media) throws IOException {
		organisationInfo.uploadFile(media);
	}
	
	@Command
	public void sdlNumberChange() {
		organisationInfo.sdlNumberChange();
	}
}
