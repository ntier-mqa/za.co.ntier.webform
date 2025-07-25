package za.co.ntier.webform.form.bean;

import org.zkoss.bind.BindUtils;
import org.zkoss.util.media.Media;

public class EmployerInfo {
	private String employerName;
	private String employerNameTitle = "Employer Name";
	private String orgRegistrationNumber;
	private String orgRegistrationNumberTitle = "Organisation Registration Number (if applicable)";
	private String employerTaxNumber;
	private String employerTaxNumberTitle = "VAT Number";
	private String fileNameVATCer;
	private String sdlNumber;
	private String sdlNumberTitle = "Skills Development Levy (SDL) Number (Paying or Exempted)";
	private String siteSDLNumber;
	private String siteSDLNumberTitle = "Site SDL Number (if applicable)";

	/**
	 * @return the employerName
	 */
	public String getEmployerName() {
		return employerName;
	}

	/**
	 * @return the employerNameTitle
	 */
	public String getEmployerNameTitle() {
		return employerNameTitle;
	}

	/**
	 * @return the orgRegistrationNumber
	 */
	public String getOrgRegistrationNumber() {
		return orgRegistrationNumber;
	}

	/**
	 * @return the orgRegistrationNumberTitle
	 */
	public String getOrgRegistrationNumberTitle() {
		return orgRegistrationNumberTitle;
	}

	/**
	 * @return the sdlNumber
	 */
	public String getSdlNumber() {
		return sdlNumber;
	}

	/**
	 * @return the sdlNumberTitle
	 */
	public String getSdlNumberTitle() {
		return sdlNumberTitle;
	}

	/**
	 * @return the siteSDLNumber
	 */
	public String getSiteSDLNumber() {
		return siteSDLNumber;
	}

	/**
	 * @return the siteSDLNumberTitle
	 */
	public String getSiteSDLNumberTitle() {
		return siteSDLNumberTitle;
	}

	/**
	 * @param employerName the employerName to set
	 */
	public void setEmployerName(String employerName) {
		this.employerName = employerName;
	}

	/**
	 * @param employerNameTitle the employerNameTitle to set
	 */
	public void setEmployerNameTitle(String employerNameTitle) {
		this.employerNameTitle = employerNameTitle;
	}

	/**
	 * @param orgRegistrationNumber the orgRegistrationNumber to set
	 */
	public void setOrgRegistrationNumber(String orgRegistrationNumber) {
		this.orgRegistrationNumber = orgRegistrationNumber;
	}

	/**
	 * @param orgRegistrationNumberTitle the orgRegistrationNumberTitle to set
	 */
	public void setOrgRegistrationNumberTitle(String orgRegistrationNumberTitle) {
		this.orgRegistrationNumberTitle = orgRegistrationNumberTitle;
	}

	/**
	 * @param sdlNumber the sdlNumber to set
	 */
	public void setSdlNumber(String sdlNumber) {
		this.sdlNumber = sdlNumber;
	}

	/**
	 * @param sdlNumberTitle the sdlNumberTitle to set
	 */
	public void setSdlNumberTitle(String sdlNumberTitle) {
		this.sdlNumberTitle = sdlNumberTitle;
	}

	/**
	 * @param siteSDLNumber the siteSDLNumber to set
	 */
	public void setSiteSDLNumber(String siteSDLNumber) {
		this.siteSDLNumber = siteSDLNumber;
	}

	/**
	 * @param siteSDLNumberTitle the siteSDLNumberTitle to set
	 */
	public void setSiteSDLNumberTitle(String siteSDLNumberTitle) {
		this.siteSDLNumberTitle = siteSDLNumberTitle;
	}
	
	public void uploadFile(Media media) {
		setFileNameVATCer(media.getName());
		
	}

	/**
	 * @return the employerTaxNumber
	 */
	public String getEmployerTaxNumber() {
		return employerTaxNumber;
	}

	/**
	 * @param employerTaxNumber the employerTaxNumber to set
	 */
	public void setEmployerTaxNumber(String employerTaxNumber) {
		this.employerTaxNumber = employerTaxNumber;
	}

	/**
	 * @return the employerTaxNumberTitle
	 */
	public String getEmployerTaxNumberTitle() {
		return employerTaxNumberTitle;
	}

	/**
	 * @param employerTaxNumberTitle the employerTaxNumberTitle to set
	 */
	public void setEmployerTaxNumberTitle(String employerTaxNumberTitle) {
		this.employerTaxNumberTitle = employerTaxNumberTitle;
	}

	/**
	 * @return the fileNameVATCer
	 */
	public String getFileNameVATCer() {
		return fileNameVATCer;
	}

	/**
	 * @param fileNameVATCer the fileNameVATCer to set
	 */
	public void setFileNameVATCer(String fileNameVATCer) {
		this.fileNameVATCer = fileNameVATCer;
		BindUtils.postNotifyChange(this, "fileNameVATCer");
	}

}
