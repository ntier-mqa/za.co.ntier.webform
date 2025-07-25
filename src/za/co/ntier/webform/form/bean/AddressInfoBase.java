package za.co.ntier.webform.form.bean;

import org.compiere.util.KeyNamePair;

public class AddressInfoBase {
	/**
	 * @return the addressLine
	 */
	public String getAddressLine() {
		return addressLine;
	}

	private String addressLineTitle = "Street Name and Number";
	
	/**
	 * @param addressLine the addressLine to set
	 */
	public void setAddressLine(String addressLine) {
		this.addressLine = addressLine;
	}

	private String postAddressTitle = "Post Address";
	
	private String postAddress;
	
	/**
	 * @return the postAddressTitle
	 */
	public String getPostAddressTitle() {
		return postAddressTitle;
	}

	/**
	 * @param postAddressTitle the postAddressTitle to set
	 */
	public void setPostAddressTitle(String postAddressTitle) {
		this.postAddressTitle = postAddressTitle;
	}

	/**
	 * @return the postAddress
	 */
	public String getPostAddress() {
		return postAddress;
	}

	/**
	 * @param postAddress the postAddress to set
	 */
	public void setPostAddress(String postAddress) {
		this.postAddress = postAddress;
	}

	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
	}

	private String areaTitle = "Area/Suburb";
	/**
	 * @param area the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	
	private String cityTitle = "City/Town";
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the postalCode
	 */
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * @param postalCode the postalCode to set
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	
	private String postalCodeTitle = "Postal Code";

	/**
	 * @return the province
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * @param province the province to set
	 */
	public void setProvince(String province) {
		this.province = province;
	}
	
	private String provinceTitle = "Province";

	// Street name and number
	private String addressLine;
	
	/**
	 * @return the addressLineTitle
	 */
	public String getAddressLineTitle() {
		return addressLineTitle;
	}

	/**
	 * @param addressLineTitle the addressLineTitle to set
	 */
	public void setAddressLineTitle(String addressLineTitle) {
		this.addressLineTitle = addressLineTitle;
	}

	/**
	 * @return the areaTitle
	 */
	public String getAreaTitle() {
		return areaTitle;
	}

	/**
	 * @param areaTitle the areaTitle to set
	 */
	public void setAreaTitle(String areaTitle) {
		this.areaTitle = areaTitle;
	}

	/**
	 * @return the cityTitle
	 */
	public String getCityTitle() {
		return cityTitle;
	}

	/**
	 * @param cityTitle the cityTitle to set
	 */
	public void setCityTitle(String cityTitle) {
		this.cityTitle = cityTitle;
	}

	/**
	 * @return the postalCodeTitle
	 */
	public String getPostalCodeTitle() {
		return postalCodeTitle;
	}

	/**
	 * @param postalCodeTitle the postalCodeTitle to set
	 */
	public void setPostalCodeTitle(String postalCodeTitle) {
		this.postalCodeTitle = postalCodeTitle;
	}

	/**
	 * @return the provinceTitle
	 */
	public String getProvinceTitle() {
		return provinceTitle;
	}

	/**
	 * @param provinceTitle the provinceTitle to set
	 */
	public void setProvinceTitle(String provinceTitle) {
		this.provinceTitle = provinceTitle;
	}

	private String area;
	
	private String city;
	
	private String postalCode;
	
	private String province;
	
	private Province provinceSelected;
	
	private KeyNamePair districtMunicipalitySelected;
	
	/**
	 * @return the districtMunicipalitySelected
	 */
	public KeyNamePair getDistrictMunicipalitySelected() {
		return districtMunicipalitySelected;
	}

	/**
	 * @param districtMunicipalitySelected the districtMunicipalitySelected to set
	 */
	public void setDistrictMunicipalitySelected(KeyNamePair districtMunicipalitySelected) {
		this.districtMunicipalitySelected = districtMunicipalitySelected;

	}

	/**
	 * @return the provinceSelected
	 */
	public Province getProvinceSelected() {
		return provinceSelected;
	}
	/**
	 * @param provinceSelected the provinceSelected to set
	 */
	public void setProvinceSelected(Province provinceSelected) {
		this.provinceSelected = provinceSelected;
	}
	
	private AddressCategory addressCategory;
	
	public AddressCategory getAddressCategory() {
		return this.addressCategory;
	}
	
	public void setAddressCategory(AddressCategory addressCategory) {
		this.addressCategory = addressCategory;
	}
	
	public boolean isShowMunicipalities() {
		return addressCategory == AddressCategory.PHYSICAL;
	}
	
	public boolean isPostalAddress() {
		return addressCategory == AddressCategory.POSTAL;
	}
	
	public boolean isNormalAddress() {
		return addressCategory == AddressCategory.PHYSICAL || addressCategory == AddressCategory.CANDIDACY_CONTACT;
	}
	
	public boolean isContact() {
		return addressCategory == AddressCategory.CANDIDACY_CONTACT;
	}
	
	public AddressInfoBase(AddressCategory addressCategory, Province selectedProvince) {
		setAddressCategory(addressCategory);
		setProvinceSelected(selectedProvince);
	}
	
	private KeyNamePair localMunicipalitySelected;

	/**
	 * @return the localMunicipalitySelected
	 */
	public KeyNamePair getLocalMunicipalitySelected() {
		return localMunicipalitySelected;
	}

	/**
	 * @param localMunicipalitySelected the localMunicipalitySelected to set
	 */
	public void setLocalMunicipalitySelected(KeyNamePair localMunicipalitySelected) {
		this.localMunicipalitySelected = localMunicipalitySelected;
	}
	
	private KeyNamePair municipalityTypeSelected;

	/**
	 * @return the municipalityTypeSelected
	 */
	public KeyNamePair getMunicipalityTypeSelected() {
		return municipalityTypeSelected;
	}

	/**
	 * @param municipalityTypeSelected the municipalityTypeSelected to set
	 */
	public void setMunicipalityTypeSelected(KeyNamePair municipalityTypeSelected) {
		this.municipalityTypeSelected = municipalityTypeSelected;
	}
	

	/**
	 * @return the siteName
	 */
	public String getSiteName() {
		return siteName;
	}

	/**
	 * @param siteName the siteName to set
	 */
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	/**
	 * @return the siteNameTitle
	 */
	public String getSiteNameTitle() {
		return siteNameTitle;
	}

	/**
	 * @param siteNameTitle the siteNameTitle to set
	 */
	public void setSiteNameTitle(String siteNameTitle) {
		this.siteNameTitle = siteNameTitle;
	}

	/**
	 * @return the nameSiteRepresentative
	 */
	public String getNameSiteRepresentative() {
		return nameSiteRepresentative;
	}

	/**
	 * @param nameSiteRepresentative the nameSiteRepresentative to set
	 */
	public void setNameSiteRepresentative(String nameSiteRepresentative) {
		this.nameSiteRepresentative = nameSiteRepresentative;
	}

	/**
	 * @return the nameSiteRepresentativeTitle
	 */
	public String getNameSiteRepresentativeTitle() {
		return nameSiteRepresentativeTitle;
	}

	/**
	 * @param nameSiteRepresentativeTitle the nameSiteRepresentativeTitle to set
	 */
	public void setNameSiteRepresentativeTitle(String nameSiteRepresentativeTitle) {
		this.nameSiteRepresentativeTitle = nameSiteRepresentativeTitle;
	}

	/**
	 * @return the representativeDesignation
	 */
	public String getRepresentativeDesignation() {
		return representativeDesignation;
	}

	/**
	 * @param representativeDesignation the representativeDesignation to set
	 */
	public void setRepresentativeDesignation(String representativeDesignation) {
		this.representativeDesignation = representativeDesignation;
	}

	/**
	 * @return the representativeDesignationTitle
	 */
	public String getRepresentativeDesignationTitle() {
		return representativeDesignationTitle;
	}

	/**
	 * @param representativeDesignationTitle the representativeDesignationTitle to set
	 */
	public void setRepresentativeDesignationTitle(String representativeDesignationTitle) {
		this.representativeDesignationTitle = representativeDesignationTitle;
	}

	/**
	 * @return the mobileNumber
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * @param mobileNumber the mobileNumber to set
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	/**
	 * @return the mobileNumberTitle
	 */
	public String getMobileNumberTitle() {
		return mobileNumberTitle;
	}

	/**
	 * @param mobileNumberTitle the mobileNumberTitle to set
	 */
	public void setMobileNumberTitle(String mobileNumberTitle) {
		this.mobileNumberTitle = mobileNumberTitle;
	}

	/**
	 * @return the landlineNumber
	 */
	public String getLandlineNumber() {
		return landlineNumber;
	}

	/**
	 * @param landlineNumber the landlineNumber to set
	 */
	public void setLandlineNumber(String landlineNumber) {
		this.landlineNumber = landlineNumber;
	}

	/**
	 * @return the landlineNumberTitle
	 */
	public String getLandlineNumberTitle() {
		return landlineNumberTitle;
	}

	/**
	 * @param landlineNumberTitle the landlineNumberTitle to set
	 */
	public void setLandlineNumberTitle(String landlineNumberTitle) {
		this.landlineNumberTitle = landlineNumberTitle;
	}
	
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the emailTitle
	 */
	public String getEmailTitle() {
		return emailTitle;
	}

	/**
	 * @param emailTitle the emailTitle to set
	 */
	public void setEmailTitle(String emailTitle) {
		this.emailTitle = emailTitle;
	}

	private String siteName;
	private String siteNameTitle = "Site Name";
	private String nameSiteRepresentative;
	private String nameSiteRepresentativeTitle = "Name and Surname of Site Representative";
	private String representativeDesignation;
	private String representativeDesignationTitle = "Representative Designation";
	private String mobileNumber;
	private String mobileNumberTitle = "Tel Numbe";
	private String landlineNumber;
	private String landlineNumberTitle = "Alternative Number";;
	private String email;
	private String emailTitle = "E-mail";
	
}
