package za.co.ntier.webform.form.bean;

import java.io.IOException;

import org.compiere.model.MClientInfo;
import org.compiere.model.MImage;
import org.compiere.util.Env;
import org.zkoss.image.AImage;

public class CompanyInfo {
	private static final CompanyInfo companyInfo;
	static {
		try {
			companyInfo = new CompanyInfo();
		} catch (IOException e) {
			throw new RuntimeException("Failed to initialize default company info", e);
		}
	}

	public static CompanyInfo getDefaultCompanyInfo() {
		return companyInfo;
	}

	private String companyAddress;
	private String companyAddressTitle;
	private String companyEmail;
	private String companyEmailTitle;

	private AImage companyLogo;
	private String companyName;
	private String companyNameTitle;
	private String companyTel;
	private String companyTelTitle;

	private String companyWebsite;

	private String companyWebsiteTitle;

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

		AImage logoMedia = null;
		byte[] data = logo.getData();
		if (data != null) {
			logoMedia = new AImage("company logo", data);
			setCompanyLogo(logoMedia);
		}
	}

	/**
	 * @return the companyAddress
	 */
	public String getCompanyAddress() {
		return companyAddress;
	}

	/**
	 * @return the companyAddressTitle
	 */
	public String getCompanyAddressTitle() {
		return companyAddressTitle;
	}

	/**
	 * @return the companyEmail
	 */
	public String getCompanyEmail() {
		return companyEmail;
	}

	/**
	 * @return the companyEmailTitle
	 */
	public String getCompanyEmailTitle() {
		return companyEmailTitle;
	}

	/**
	 * @return the companyLogo
	 */
	public AImage getCompanyLogo() {
		return companyLogo;
	}

	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @return the companyNameTitle
	 */
	public String getCompanyNameTitle() {
		return companyNameTitle;
	}

	/**
	 * @return the companyTel
	 */
	public String getCompanyTel() {
		return companyTel;
	}

	/**
	 * @return the companyTelTitle
	 */
	public String getCompanyTelTitle() {
		return companyTelTitle;
	}

	/**
	 * @return the companyWebsite
	 */
	public String getCompanyWebsite() {
		return companyWebsite;
	}

	/**
	 * @return the companyWebsiteTitle
	 */
	public String getCompanyWebsiteTitle() {
		return companyWebsiteTitle;
	}

	/**
	 * @param companyAddress the companyAddress to set
	 */
	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	/**
	 * @param companyAddressTitle the companyAddressTitle to set
	 */
	public void setCompanyAddressTitle(String companyAddressTitle) {
		this.companyAddressTitle = companyAddressTitle;
	}

	/**
	 * @param companyEmail the companyEmail to set
	 */
	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}

	/**
	 * @param companyEmailTitle the companyEmailTitle to set
	 */
	public void setCompanyEmailTitle(String companyEmailTitle) {
		this.companyEmailTitle = companyEmailTitle;
	}

	/**
	 * @param companyLogo the companyLogo to set
	 */
	public void setCompanyLogo(AImage companyLogo) {
		this.companyLogo = companyLogo;
	}

	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @param companyNameTitle the companyNameTitle to set
	 */
	public void setCompanyNameTitle(String companyNameTitle) {
		this.companyNameTitle = companyNameTitle;
	}

	/**
	 * @param companyTel the companyTel to set
	 */
	public void setCompanyTel(String companyTel) {
		this.companyTel = companyTel;
	}

	/**
	 * @param companyTelTitle the companyTelTitle to set
	 */
	public void setCompanyTelTitle(String companyTelTitle) {
		this.companyTelTitle = companyTelTitle;
	}

	/**
	 * @param companyWebsite the companyWebsite to set
	 */
	public void setCompanyWebsite(String companyWebsite) {
		this.companyWebsite = companyWebsite;
	}

	/**
	 * @param companyWebsiteTitle the companyWebsiteTitle to set
	 */
	public void setCompanyWebsiteTitle(String companyWebsiteTitle) {
		this.companyWebsiteTitle = companyWebsiteTitle;
	}
}
