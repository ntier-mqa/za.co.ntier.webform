package za.co.ntier.webform.form.bean.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.compiere.model.MCity;
import org.compiere.model.MRegion;
import org.compiere.model.MTable;
import org.compiere.model.Query;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.zkoss.bind.annotation.NotifyChange;

import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.bean.AddressType;
import za.co.ntier.webform.form.bean.ProgramType;
import za.co.ntier.webform.model.X_ZZ_Application_Form;
import za.co.ntier.webform.model.X_ZZ_FormContact;

public class AddressInfo implements ISaveForm {
	private AddressType addressCategory;

	// Street name and number
	private String addressLine;

	private String addressLineTitle = "Street Name and Number";

	private X_ZZ_Application_Form applicationForm;

	private Collection<MCity> areas;

	private MCity areaSelected;

	private String areaTitle = "Area/Suburb";

	private String email;

	private String emailTitle = "E-mail";
	private X_ZZ_FormContact formContact;

	private boolean isAlternate;

	private String landlineNumber;
	private String landlineNumberTitle = "Alternative Number";

	private KeyNamePair localMunicipalitySelected;

	private String mobileNumber;

	private String mobileNumberTitle = "Tel Number";

	private String nameSiteRepresentative;

	private String nameSiteRepresentativeTitle = "Name and Surname";

	private String orgName;

	private String orgNameTitle = "Organisation Name";
	
	private String postAddress;

	private String postAddressTitle = "Post Address";

	private String postalCode;

	private String postalCodeTitle = "Postal Code";

	private ProgramType programType;

	private Collection<MRegion> provinces;

	private MRegion provinceSelected;

	private String provinceTitle = "Province";

	private String representativeDesignation;

	private String representativeDesignationTitle = "Designation";

	private String siteName;
	
	private String siteNameTitle = "Site Name";

	public AddressInfo(AddressType addressCategory) {
		this.addressCategory = addressCategory;
		init();
	}
	
	public AddressInfo(ProgramType programType, boolean isAlternate) {
		this(isAlternate ? AddressType.MAIN_ALTER : AddressType.MAIN);
		this.isAlternate = isAlternate;
		this.programType = programType;
		init();
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
	 * @return the applicationForm
	 */
	public X_ZZ_Application_Form getApplicationForm() {
		return applicationForm;
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
	 * @return the orgName
	 */
	public String getOrgName() {
		return orgName;
	}

	/**
	 * @return the orgNameTitle
	 */
	public String getOrgNameTitle() {
		return orgNameTitle;
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
	 * @return the programTypeMenuContextKey
	 */
	public ProgramType getProgramType() {
		return programType;
	}

	/**
	 * @return the provinces
	 */
	public Collection<MRegion> getProvinces() {
		return provinces;
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
	
	protected void init() {
		areas = MasterUtil.getInitCities();
		provinces = MasterUtil.getRegions();
		
	} 

	public void initComponent(X_ZZ_Application_Form applicationForm) {
		this.applicationForm = applicationForm;
		if (applicationForm != null) {
			Query formContactQuery = MTable.get(X_ZZ_FormContact.Table_ID).createQuery(
					String.format("%s = ? AND %s = ?", X_ZZ_Application_Form.COLUMNNAME_ZZ_Application_Form_ID, X_ZZ_FormContact.COLUMNNAME_ZZ_ContactType)
					, null);
			
			formContact = formContactQuery.setOrderBy(X_ZZ_FormContact.COLUMNNAME_ZZ_FormContact_ID).setParameters(applicationForm.getZZ_Application_Form_ID(), getAddressCategory().toString()).first();
		}
		
		if (formContact != null) {
			if (formContact.getC_Region_ID() != 0) {
				for (MRegion province : provinces) {
					if(province.getC_Region_ID() == formContact.getC_Region_ID()) {
						provinceSelected = province;
						break;
					}
				}
				
			}
				
			
			if (formContact.getC_City_ID() != 0) {
				for (MCity area: MasterUtil.getCities()) {
					if(area.getC_City_ID() == formContact.getC_City_ID()) {
						areaSelected = area;
						if (!areas.contains(area)) {
							areas.add(area);
						}
						break;
					}
				}
				
			}
			
			this.postalCode = formContact.getPostal();
			
			setSiteName(formContact.getZZ_SideName());
			
			setAddressLine(formContact.getAddress());

			setNameSiteRepresentative(formContact.getContactName());
			setRepresentativeDesignation(formContact.getZZ_Designation());
			setMobileNumber(formContact.getPhone());
			setLandlineNumber(formContact.getPhone2());
			setEmail(formContact.getEMail());
		}
		
		 
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		int applicationFormID = applicationForm.getZZ_Application_Form_ID();
		
		if (formContact == null) {
			formContact = new X_ZZ_FormContact(Env.getCtx(), 0, null);
			formContact.setZZ_Application_Form_ID(applicationFormID);
		}
			
		
		if (getProvinceSelected() != null)
			formContact.setC_Region_ID(getProvinceSelected().getC_Region_ID());

		if (getAreaSelected() != null)
			formContact.setC_City_ID(getAreaSelected().getC_City_ID());

		formContact.setPostal(getPostalCode());
		
//		formContact.setPostal(getPostalCode());

		formContact.setZZ_SideName(getSiteName());
		formContact.setAddress(getAddressLine());

		formContact.setContactName(getNameSiteRepresentative());
		formContact.setZZ_Designation(getRepresentativeDesignation());
		formContact.setPhone(getMobileNumber());
		formContact.setPhone2(getLandlineNumber());
		formContact.setEMail(getEmail());
		formContact.setZZ_ContactType(getAddressCategory().toString());
		formContact.saveEx(trxName);
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
	 * @param applicationForm the applicationForm to set
	 */
	public void setApplicationForm(X_ZZ_Application_Form applicationForm) {
		this.applicationForm = applicationForm;
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
			provinces = new ArrayList<>();
			provinces.add(provinceSelected);
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
	 * @param orgName the orgName to set
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	/**
	 * @param orgNameTitle the orgNameTitle to set
	 */
	public void setOrgNameTitle(String orgNameTitle) {
		this.orgNameTitle = orgNameTitle;
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
	@NotifyChange({ "provinceSelected", "areas", "provinces"})
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;

		Collection<MCity> areaFilters = new ArrayList<>();
		Collection<MRegion> provinceFilters = new ArrayList<>();

		if (StringUtils.isNotEmpty(postalCode)) {
			MasterUtil.getCities().stream()
					.filter(city -> city.getPostal() != null && postalCode.equalsIgnoreCase(city.getPostal()))
					.limit(MasterUtil.limitItem).forEach(city -> {
						areaFilters.add(city);
						MRegion linkRegion = MRegion.get(city.getC_Region_ID());
						if (!provinceFilters.contains(linkRegion))
							provinceFilters.add(linkRegion);
					});
		}

		areaSelected = null;
		provinceSelected = null;
		if (!areaFilters.isEmpty()) {
			if (areaFilters.size() == 1) {
				areaSelected = areaFilters.iterator().next();
			}
			
			if (provinceFilters.size() == 1) {
				provinceSelected = provinceFilters.iterator().next();
			}
			
			areas = areaFilters;
			provinces = provinceFilters;
		} else {
			areas = MasterUtil.getInitCities();

			provinces = MasterUtil.getRegions();
		}
	}

	/**
	 * @param postalCodeTitle the postalCodeTitle to set
	 */
	public void setPostalCodeTitle(String postalCodeTitle) {
		this.postalCodeTitle = postalCodeTitle;
	}

	/**
	 * @param programTypeMenuContextKey the programTypeMenuContextKey to set
	 */
	public void setProgramType(ProgramType programType) {
		this.programType = programType;
	}

	/**
	 * @param provinces the provinces to set
	 */
	public void setProvinces(Collection<MRegion> provinces) {
		this.provinces = provinces;
	}

	/**
	 * @param provinceSelected the provinceSelected to set
	 */
	@NotifyChange({ "areas", "areaSelected", "postalCode" })
	public void setProvinceSelected(MRegion provinceSelected) {
		this.provinceSelected = provinceSelected;

		areaSelected = null;
		postalCode = null;
		
		if (provinceSelected == null) {
			areas = MasterUtil.getInitCities();
			provinces = MasterUtil.getRegions();
			postalCode = null;
		} else {
			List<MCity> areaFilters = new ArrayList<>();

			MasterUtil.getCities().stream().filter(city -> city.getC_Region_ID() == provinceSelected.getC_Region_ID())
					.limit(MasterUtil.limitItem).forEach(city -> {
						areaFilters.add(city);
					});

			areas = areaFilters;
			if (!areas.isEmpty()) {
				if (areaFilters.size() == 1) {
					areaSelected = areaFilters.iterator().next();
					postalCode = areaSelected.getPostal();
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
		return addressCategory == AddressType.MAIN || addressCategory == AddressType.MAIN_ALTER
				|| addressCategory == AddressType.ORG || addressCategory == AddressType.ORG_ALTER
				|| addressCategory == AddressType.VACATION;
	}

	public boolean showGeographicAddress() {
		return addressCategory == AddressType.MAIN || addressCategory == AddressType.MAIN_ALTER
				|| addressCategory == AddressType.PHYSICAL || addressCategory == AddressType.POSTAL
				|| addressCategory == AddressType.VACATION;
	}

	public boolean showLineAddress() {
		return addressCategory == AddressType.MAIN || addressCategory == AddressType.MAIN_ALTER
				|| addressCategory == AddressType.PHYSICAL || addressCategory == AddressType.VACATION;
	}

	public boolean showMunicipalities() {
		return false;
	}

	public boolean showOrgName() {
		return programType != null && programType.isShowAddressOrgField();
	}

	public boolean showPostalAddress() {
		return addressCategory == AddressType.POSTAL;
	}

	public boolean showSiteName() {
		return (programType != null && programType.isShowAddressSiteField()) || addressCategory == AddressType.VACATION;
	}
}
