package za.co.ntier.webform.form.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.compiere.model.MCity;
import org.compiere.model.MRegion;
import org.compiere.util.KeyNamePair;
import org.zkoss.bind.annotation.NotifyChange;

import za.co.ntier.webform.form.viewmodel.master.MasterUtil;

public class AddressInfoBase {
	private AddressType addressCategory;

	// Street name and number
	private String addressLine;

	private String addressLineTitle = "Street Name and Number";

	private Collection<MCity> areas;

	private MCity areaSelected;

	private String areaTitle = "Area/Suburb";

	private String email;

	private String emailTitle = "E-mail";

	private String landlineNumber;
	private String landlineNumberTitle = "Alternative Number";

	private KeyNamePair localMunicipalitySelected;

	private String mobileNumber;
	private String mobileNumberTitle = "Tel Number";

	private String nameSiteRepresentative;

	private String nameSiteRepresentativeTitle = "Name and Surname";

	private String postAddress;

	private String postAddressTitle = "Post Address";

	private String postalCode;

	private String postalCodeTitle = "Postal Code";

	private MRegion provinceSelected;

	private String provinceTitle = "Province";

	private String representativeDesignation;

	private String representativeDesignationTitle = "Designation";

	private String siteName;

	private String siteNameTitle = "Site Name";
	
	private String orgName;

	private String orgNameTitle = "Organisation Name";

	private ProgramType programType;

	private boolean isAlternate;

	public AddressInfoBase(ProgramType programType, boolean isAlternate, MRegion provinceSelected) {
		this(isAlternate?AddressType.MAIN_ALTER:AddressType.MAIN, provinceSelected);
		this.isAlternate = isAlternate;
		this.programType = programType;
	}

	public AddressInfoBase(AddressType addressCategory, MRegion provinceSelected) {
		this.addressCategory = addressCategory;
		this.provinceSelected = provinceSelected;
	}
	
	public AddressType getAddressCategory() {
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

	public String getAddressTitle() {
		return addressCategory.getAddressTitle(programType, isAlternate);
	}

	/**
	 * @return the areas
	 */
	public Collection<MCity> getAreas() {
		return areas;
	}

	/**
	 * @return the areaSelected
	 */
	public MCity getAreaSelected() {
		return areaSelected;
	}

	/**
	 * @return the areaTitle
	 */
	public String getAreaTitle() {
		return areaTitle;
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
	 * @return the provinceSelected
	 */
	public MRegion getProvinceSelected() {
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

	public void setAddressCategory(AddressType addressCategory) {
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
	 * @param areas the areas to set
	 */
	public void setAreas(Collection<MCity> areas) {
		this.areas = areas;
	}

	/**
	 * @param areaSelected the areaSelected to set
	 */
	@NotifyChange({ "postalCode", "provinceSelected" })
	public void setAreaSelected(MCity areaSelected) {
		this.areaSelected = areaSelected;
		if (areaSelected != null) {
			postalCode = areaSelected.getPostal();
			provinceSelected = MRegion.get(areaSelected.getC_Region_ID());
		} else {
			postalCode = null;
			provinceSelected = null;
		}
	}

	/**
	 * @param areaTitle the areaTitle to set
	 */
	public void setAreaTitle(String areaTitle) {
		this.areaTitle = areaTitle;
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
	@NotifyChange({ "provinceSelected", "areas" })
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;

		Collection<MCity> areaFilters = new ArrayList<>();

		if (StringUtils.isNotEmpty(postalCode)) {
			MasterUtil.getCities().stream()
					.filter(city -> city.getPostal() != null && postalCode.equalsIgnoreCase(city.getPostal()))
					.limit(MasterUtil.limitItem).forEach(city -> {
						areaFilters.add(city);
					});
		}

		areaSelected = null;

		if (!areaFilters.isEmpty()) {
			provinceSelected = MRegion.get(areaFilters.iterator().next().getC_Region_ID());
			if (areaFilters.size() == 1) {
				areaSelected = areaFilters.iterator().next();
			}
		} else {
			MasterUtil.getCities().stream().limit(MasterUtil.limitItem).forEach(city -> {
				areaFilters.add(city);
			});

			provinceSelected = null;
		}

		areas = areaFilters;

	}

	/**
	 * @param postalCodeTitle the postalCodeTitle to set
	 */
	public void setPostalCodeTitle(String postalCodeTitle) {
		this.postalCodeTitle = postalCodeTitle;
	}

	/**
	 * @param provinceSelected the provinceSelected to set
	 */
	@NotifyChange({ "areas", "areaSelected", "postalCode" })
	public void setProvinceSelected(MRegion provinceSelected) {
		this.provinceSelected = provinceSelected;

		areaSelected = null;

		if (provinceSelected == null) {
			areas = null;
			postalCode = null;
		} else {
			List<MCity> areaFilters = new ArrayList<>();

			MasterUtil.getCities().stream().filter(city -> city.getC_Region_ID() == provinceSelected.getC_Region_ID())
					.limit(MasterUtil.limitItem).forEach(city -> {
						areaFilters.add(city);
					});

			areas = areaFilters;
			if (!areas.isEmpty()) {
				postalCode = areaFilters.iterator().next().getPostal();
				if (areaFilters.size() == 1) {
					areaSelected = areaFilters.iterator().next();
				}
			}
		}

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

	public boolean showContact() {
		return programType != null || addressCategory == AddressType.ORG
				|| addressCategory == AddressType.ORG_ALTER;
	}

	public boolean showGeographicAddress() {
		return programType != null || addressCategory == AddressType.PHYSICAL
				|| addressCategory == AddressType.POSTAL;
	}

	public boolean showLineAddress() {
		return programType != null || addressCategory == AddressType.PHYSICAL;
	}

	public boolean showMunicipalities() {
		return false;
	}

	public boolean showPostalAddress() {
		return addressCategory == AddressType.POSTAL;
	}

	public boolean showSiteName() {
		return programType != null && programType.isShowAddressSiteField();
	}
	
	public boolean showOrgName() {
		return programType != null && programType.isShowAddressOrgField();
	}

	/**
	 * @return the orgName
	 */
	public String getOrgName() {
		return orgName;
	}

	/**
	 * @param orgName the orgName to set
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	/**
	 * @return the orgNameTitle
	 */
	public String getOrgNameTitle() {
		return orgNameTitle;
	}

	/**
	 * @param orgNameTitle the orgNameTitle to set
	 */
	public void setOrgNameTitle(String orgNameTitle) {
		this.orgNameTitle = orgNameTitle;
	}

	/**
	 * @return the programType
	 */
	public ProgramType getProgramType() {
		return programType;
	}

	/**
	 * @param programType the programType to set
	 */
	public void setProgramType(ProgramType programType) {
		this.programType = programType;
	}

}
