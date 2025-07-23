package za.co.ntier.webform.form.viewmodel;

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
		showMunicipalities = addressCategory == AddressCategory.PHYSICAL;
	}
	
	private boolean showMunicipalities;
	
	public boolean isShowMunicipalities() {
		return showMunicipalities;
	}
	
	public boolean isPostalAddress() {
		return addressCategory == AddressCategory.POSTAL;
	}
	
	public boolean isNormalAddress() {
		return addressCategory == AddressCategory.PHYSICAL;
	}
	
	public void setShowMunicipalities(boolean showMunicipalities) {
		this.showMunicipalities = showMunicipalities;
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
}
