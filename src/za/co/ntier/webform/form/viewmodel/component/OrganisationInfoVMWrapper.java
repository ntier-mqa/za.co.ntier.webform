package za.co.ntier.webform.form.viewmodel.component;

import java.io.IOException;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.media.Media;

import za.co.ntier.webform.form.bean.component.OrganisationInfo;
import za.co.ntier.webform.form.viewmodel.DiscretionaryGrantsApplicationProgramVM;

public class OrganisationInfoVMWrapper {
	private DiscretionaryGrantsApplicationProgramVM applicationProgramVM;
	private OrganisationInfo organisationInfo;
	



	

	public DiscretionaryGrantsApplicationProgramVM getApplicationProgramVM(){ return applicationProgramVM; }

	/**
	 * @return the organisationInfo
	 */
	public OrganisationInfo getOrganisationInfo() {
		return organisationInfo;
	}
	
	@Init
	public void init(@ExecutionArgParam("organisationInfo") OrganisationInfo orgInfo,
	        @ExecutionArgParam("applicationProgramVM") DiscretionaryGrantsApplicationProgramVM appVM) {
		 this.organisationInfo = orgInfo;
	     this.applicationProgramVM = appVM;
	}

	@Command
	public void sdlNumberChange() {
		organisationInfo.sdlNumberChange();
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
}
