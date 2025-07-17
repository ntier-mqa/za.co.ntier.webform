package za.co.ntier.webform.form.viewmodel;

import java.io.IOException;

import org.compiere.model.MClientInfo;
import org.compiere.model.MImage;
import org.compiere.util.Env;
import org.zkoss.image.AImage;

public class CompanyInfo {
	private String companyName;
	private String companyAddress;
	private String companyTel;
	private String companyEmail;
	private String companyWebsite;
	
	private String companyNameTitle;
	private String companyAddressTitle;
	private String companyTelTitle;
	private String companyEmailTitle;
	private String companyWebsiteTitle;
	
	private AImage companyLogo;

	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @return the companyAddress
	 */
	public String getCompanyAddress() {
		return companyAddress;
	}

	/**
	 * @param companyAddress the companyAddress to set
	 */
	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	/**
	 * @return the companyTel
	 */
	public String getCompanyTel() {
		return companyTel;
	}

	/**
	 * @param companyTel the companyTel to set
	 */
	public void setCompanyTel(String companyTel) {
		this.companyTel = companyTel;
	}

	/**
	 * @return the companyEmail
	 */
	public String getCompanyEmail() {
		return companyEmail;
	}

	/**
	 * @param companyEmail the companyEmail to set
	 */
	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}

	/**
	 * @return the companyWebsite
	 */
	public String getCompanyWebsite() {
		return companyWebsite;
	}

	/**
	 * @param companyWebsite the companyWebsite to set
	 */
	public void setCompanyWebsite(String companyWebsite) {
		this.companyWebsite = companyWebsite;
	}

	/**
	 * @return the companyNameTitle
	 */
	public String getCompanyNameTitle() {
		return companyNameTitle;
	}

	/**
	 * @param companyNameTitle the companyNameTitle to set
	 */
	public void setCompanyNameTitle(String companyNameTitle) {
		this.companyNameTitle = companyNameTitle;
	}

	/**
	 * @return the companyAddressTitle
	 */
	public String getCompanyAddressTitle() {
		return companyAddressTitle;
	}

	/**
	 * @param companyAddressTitle the companyAddressTitle to set
	 */
	public void setCompanyAddressTitle(String companyAddressTitle) {
		this.companyAddressTitle = companyAddressTitle;
	}

	/**
	 * @return the companyTelTitle
	 */
	public String getCompanyTelTitle() {
		return companyTelTitle;
	}

	/**
	 * @param companyTelTitle the companyTelTitle to set
	 */
	public void setCompanyTelTitle(String companyTelTitle) {
		this.companyTelTitle = companyTelTitle;
	}

	/**
	 * @return the companyEmailTitle
	 */
	public String getCompanyEmailTitle() {
		return companyEmailTitle;
	}

	/**
	 * @param companyEmailTitle the companyEmailTitle to set
	 */
	public void setCompanyEmailTitle(String companyEmailTitle) {
		this.companyEmailTitle = companyEmailTitle;
	}

	/**
	 * @return the companyWebsiteTitle
	 */
	public String getCompanyWebsiteTitle() {
		return companyWebsiteTitle;
	}

	/**
	 * @param companyWebsiteTitle the companyWebsiteTitle to set
	 */
	public void setCompanyWebsiteTitle(String companyWebsiteTitle) {
		this.companyWebsiteTitle = companyWebsiteTitle;
	}

	/**
	 * @return the companyLogo
	 */
	public AImage getCompanyLogo() {
		return companyLogo;
	}

	/**
	 * @param companyLogo the companyLogo to set
	 */
	public void setCompanyLogo(AImage companyLogo) {
		this.companyLogo = companyLogo;
	}
	
	public CompanyInfo() throws IOException {
		setCompanyName("Mining Qualifications Authority");
		setCompanyAddress("7 Anerley Rd, Parktown, Johannesburg, 2193");
		setCompanyTel("011 547 2600");
		setCompanyWebsite("www.mqa.org.za");
		setCompanyEmail("info@mqa.org.za");
		
		setCompanyTelTitle("Tel");
		setCompanyEmailTitle("Email");
		
		MClientInfo clientInfo = MClientInfo.get(Env.getAD_Client_ID(Env.getCtx()));
		MImage logo = MImage.get(Env.getCtx(), clientInfo.getLogo_ID());
		
		AImage logoMedia = new AImage("company logo", logo.getData());
		setCompanyLogo(logoMedia);
	}
	
	public static CompanyInfo getDefaultCompanyInfo() throws IOException {
		return new CompanyInfo();
	}
}
