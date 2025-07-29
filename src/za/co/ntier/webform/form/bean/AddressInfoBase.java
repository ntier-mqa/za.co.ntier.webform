package za.co.ntier.webform.form.bean;

import org.compiere.util.KeyNamePair;

public class AddressInfoBase {
	private AddressCategory addressCategory;

	// Street name and number
	private String addressLine;

	private String addressLineTitle = "Street Name and Number";

	private String area;

	private String areaTitle = "Area/Suburb";

	private String city;

	private String cityTitle = "City/Town";

	private KeyNamePair districtMunicipalitySelected;

	private String email;

	private String emailTitle = "E-mail";

	private String landlineNumber;
	private String landlineNumberTitle = "Alternative Number";

	private KeyNamePair localMunicipalitySelected;

	private String mobileNumber;
	private String mobileNumberTitle = "Tel Number";

	private KeyNamePair municipalityTypeSelected;

	private String nameSiteRepresentative;

	private String nameSiteRepresentativeTitle = "Name and Surname";

	private String postAddress;

	private String postAddressTitle = "Post Address";

	private String postalCode;

	private String postalCodeTitle = "Postal Code";

	private String province;

	private Province provinceSelected;

	private String provinceTitle = "Province";

	private String representativeDesignation;

	private String representativeDesignationTitle = "Designation";

	private String siteName;

	private String siteNameTitle = "Site Name";

	public AddressInfoBase(AddressCategory addressCategory, Province selectedProvince) {
		setAddressCategory(addressCategory);
		setProvinceSelected(selectedProvince);
	}

	public AddressCategory getAddressCategory() {
		return this.addressCategory;
	}

	/**
	 * @return the addressLine
	 */
	public String getAddressLine() {
		return addressLine;
	}

	/**
	 * @return the addressLineTitle
	 */
	public String getAddressLineTitle() {
		return addressLineTitle;
	}

	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
	}

	/**
	 * @return the areaTitle
	 */
	public String getAreaTitle() {
		return areaTitle;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @return the cityTitle
	 */
	public String getCityTitle() {
		return cityTitle;
	}

	/**
	 * @return the districtMunicipalitySelected
	 */
	public KeyNamePair getDistrictMunicipalitySelected() {
		return districtMunicipalitySelected;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return the emailTitle
	 */
	public String getEmailTitle() {
		return emailTitle;
	}

	/**
	 * @return the landlineNumber
	 */
	public String getLandlineNumber() {
		return landlineNumber;
	}

	/**
	 * @return the landlineNumberTitle
	 */
	public String getLandlineNumberTitle() {
		return landlineNumberTitle;
	}

	/**
	 * @return the localMunicipalitySelected
	 */
	public KeyNamePair getLocalMunicipalitySelected() {
		return localMunicipalitySelected;
	}

	/**
	 * @return the mobileNumber
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * @return the mobileNumberTitle
	 */
	public String getMobileNumberTitle() {
		return mobileNumberTitle;
	}

	/**
	 * @return the municipalityTypeSelected
	 */
	public KeyNamePair getMunicipalityTypeSelected() {
		return municipalityTypeSelected;
	}

	/**
	 * @return the nameSiteRepresentative
	 */
	public String getNameSiteRepresentative() {
		return nameSiteRepresentative;
	}

	/**
	 * @return the nameSiteRepresentativeTitle
	 */
	public String getNameSiteRepresentativeTitle() {
		return nameSiteRepresentativeTitle;
	}

	/**
	 * @return the postAddress
	 */
	public String getPostAddress() {
		return postAddress;
	}

	/**
	 * @return the postAddressTitle
	 */
	public String getPostAddressTitle() {
		return postAddressTitle;
	}

	/**
	 * @return the postalCode
	 */
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * @return the postalCodeTitle
	 */
	public String getPostalCodeTitle() {
		return postalCodeTitle;
	}

	/**
	 * @return the province
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * @return the provinceSelected
	 */
	public Province getProvinceSelected() {
		return provinceSelected;
	}

	/**
	 * @return the provinceTitle
	 */
	public String getProvinceTitle() {
		return provinceTitle;
	}

	/**
	 * @return the representativeDesignation
	 */
	public String getRepresentativeDesignation() {
		return representativeDesignation;
	}

	/**
	 * @return the representativeDesignationTitle
	 */
	public String getRepresentativeDesignationTitle() {
		return representativeDesignationTitle;
	}

	/**
	 * @return the siteName
	 */
	public String getSiteName() {
		return siteName;
	}

	/**
	 * @return the siteNameTitle
	 */
	public String getSiteNameTitle() {
		return siteNameTitle;
	}

	public boolean showSiteName() {
		return addressCategory == AddressCategory.CANDIDACY_CONTACT;
	}
	
	public boolean showContact() {
		return addressCategory == AddressCategory.CANDIDACY_CONTACT ||
				addressCategory == AddressCategory.ORG_CONTACT;
	}

	public boolean showLineAddress() {
		return addressCategory == AddressCategory.PHYSICAL || addressCategory == AddressCategory.CANDIDACY_CONTACT;
	}
	
	public boolean showGeographicAddress() {
		return addressCategory == AddressCategory.PHYSICAL || addressCategory == AddressCategory.CANDIDACY_CONTACT
				|| addressCategory == AddressCategory.POSTAL;
	}
	

	public boolean showPostalAddress() {
		return addressCategory == AddressCategory.POSTAL;
	}

	public boolean showMunicipalities() {
		return false;
	}

	public void setAddressCategory(AddressCategory addressCategory) {
		this.addressCategory = addressCategory;
	}

	/**
	 * @param addressLine the addressLine to set
	 */
	public void setAddressLine(String addressLine) {
		this.addressLine = addressLine;
	}

	/**
	 * @param addressLineTitle the addressLineTitle to set
	 */
	public void setAddressLineTitle(String addressLineTitle) {
		this.addressLineTitle = addressLineTitle;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * @param areaTitle the areaTitle to set
	 */
	public void setAreaTitle(String areaTitle) {
		this.areaTitle = areaTitle;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @param cityTitle the cityTitle to set
	 */
	public void setCityTitle(String cityTitle) {
		this.cityTitle = cityTitle;
	}

	/**
	 * @param districtMunicipalitySelected the districtMunicipalitySelected to set
	 */
	public void setDistrictMunicipalitySelected(KeyNamePair districtMunicipalitySelected) {
		this.districtMunicipalitySelected = districtMunicipalitySelected;

	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @param emailTitle the emailTitle to set
	 */
	public void setEmailTitle(String emailTitle) {
		this.emailTitle = emailTitle;
	}

	/**
	 * @param landlineNumber the landlineNumber to set
	 */
	public void setLandlineNumber(String landlineNumber) {
		this.landlineNumber = landlineNumber;
	}

	/**
	 * @param landlineNumberTitle the landlineNumberTitle to set
	 */
	public void setLandlineNumberTitle(String landlineNumberTitle) {
		this.landlineNumberTitle = landlineNumberTitle;
	}

	/**
	 * @param localMunicipalitySelected the localMunicipalitySelected to set
	 */
	public void setLocalMunicipalitySelected(KeyNamePair localMunicipalitySelected) {
		this.localMunicipalitySelected = localMunicipalitySelected;
	}

	/**
	 * @param mobileNumber the mobileNumber to set
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	/**
	 * @param mobileNumberTitle the mobileNumberTitle to set
	 */
	public void setMobileNumberTitle(String mobileNumberTitle) {
		this.mobileNumberTitle = mobileNumberTitle;
	}

	/**
	 * @param municipalityTypeSelected the municipalityTypeSelected to set
	 */
	public void setMunicipalityTypeSelected(KeyNamePair municipalityTypeSelected) {
		this.municipalityTypeSelected = municipalityTypeSelected;
	}

	/**
	 * @param nameSiteRepresentative the nameSiteRepresentative to set
	 */
	public void setNameSiteRepresentative(String nameSiteRepresentative) {
		this.nameSiteRepresentative = nameSiteRepresentative;
	}

	/**
	 * @param nameSiteRepresentativeTitle the nameSiteRepresentativeTitle to set
	 */
	public void setNameSiteRepresentativeTitle(String nameSiteRepresentativeTitle) {
		this.nameSiteRepresentativeTitle = nameSiteRepresentativeTitle;
	}

	/**
	 * @param postAddress the postAddress to set
	 */
	public void setPostAddress(String postAddress) {
		this.postAddress = postAddress;
	}

	/**
	 * @param postAddressTitle the postAddressTitle to set
	 */
	public void setPostAddressTitle(String postAddressTitle) {
		this.postAddressTitle = postAddressTitle;
	}

	/**
	 * @param postalCode the postalCode to set
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	/**
	 * @param postalCodeTitle the postalCodeTitle to set
	 */
	public void setPostalCodeTitle(String postalCodeTitle) {
		this.postalCodeTitle = postalCodeTitle;
	}

	/**
	 * @param province the province to set
	 */
	public void setProvince(String province) {
		this.province = province;
	}

	/**
	 * @param provinceSelected the provinceSelected to set
	 */
	public void setProvinceSelected(Province provinceSelected) {
		this.provinceSelected = provinceSelected;
	}

	/**
	 * @param provinceTitle the provinceTitle to set
	 */
	public void setProvinceTitle(String provinceTitle) {
		this.provinceTitle = provinceTitle;
	}

	/**
	 * @param representativeDesignation the representativeDesignation to set
	 */
	public void setRepresentativeDesignation(String representativeDesignation) {
		this.representativeDesignation = representativeDesignation;
	}

	/**
	 * @param representativeDesignationTitle the representativeDesignationTitle to
	 *                                       set
	 */
	public void setRepresentativeDesignationTitle(String representativeDesignationTitle) {
		this.representativeDesignationTitle = representativeDesignationTitle;
	}

	/**
	 * @param siteName the siteName to set
	 */
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	/**
	 * @param siteNameTitle the siteNameTitle to set
	 */
	public void setSiteNameTitle(String siteNameTitle) {
		this.siteNameTitle = siteNameTitle;
	}

}
