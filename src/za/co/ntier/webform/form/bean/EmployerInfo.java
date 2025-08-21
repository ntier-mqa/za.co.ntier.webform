package za.co.ntier.webform.form.bean;

import java.util.List;
import java.util.Random;

import org.compiere.model.I_C_BPartner;
import org.compiere.model.Query;
import org.compiere.model.X_C_BPartner;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.zkoss.bind.BindUtils;
import org.zkoss.util.media.Media;

import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.viewmodel.master.MasterUtil;

public class EmployerInfo {
	private AddressInfoBase alternateOrgContact;
	private String employerName;
	private String employerNameTitle = "Organisation Name";
	private String employerTaxNumber;
	private String employerTaxNumberTitle = "Tax ID";

	private String fileNameVATCer;
	private AddressInfoBase orgContact;
	private String orgRegistrationNumber;
	private String orgRegistrationNumberTitle = "Reference No";
	private OrganisationSizeInfo orgSizeInfo;
	private AddressInfoBase physicalAddressInfo;
	private AddressInfoBase postAddressInfo;
	private String sdlNumber;
	private String sdlNumberTitle = "Skills Development Levy (SDL) Number (Paying or Exempted)";
	private String siteSDLNumber;
	private String siteSDLNumberTitle = "Site SDL Number (if applicable)";
	
	private MenuContextInfo menuContextInfo;
	
	
	public MenuContextInfo getMenuContextInfo() {
		return menuContextInfo;
	}

	public void setMenuContextInfo(MenuContextInfo menuContextInfo) {
		this.menuContextInfo = menuContextInfo;
	}

	public String getCetTvetNameTitle() {
		if (ProgramType.CET.equals(menuContextInfo.getProgramType())) {
			return "Name of CET College";
		}else if(ProgramType.TVET.equals(menuContextInfo.getProgramType()))
			return "Name of TVET College";
		else
			return "unknownCetNameTitle";
	}
	
	private X_C_BPartner cetTvetCollegeSelected;
	
	public List<X_C_BPartner> getCetTvetColleges() {
		if (ProgramType.CET.equals(menuContextInfo.getProgramType())) {
			return MasterUtil.getCetColleges();
		} else if (ProgramType.TVET.equals(menuContextInfo.getProgramType())) {
			return MasterUtil.getTvetColleges();
		}
		return null;
	}

	public EmployerInfo(MenuContextInfo menuContextInfo) {
		this.menuContextInfo = menuContextInfo;
		postAddressInfo = new AddressInfoBase(AddressType.POSTAL,
				MasterUtil.getRegions().get(new Random().nextInt(MasterUtil.getRegions().size())));

		physicalAddressInfo = new AddressInfoBase(AddressType.PHYSICAL,
				MasterUtil.getRegions().get(new Random().nextInt(MasterUtil.getRegions().size())));

		
		if (!menuContextInfo.getProgramType().isCetTvet()) {
			orgSizeInfo = new OrganisationSizeInfo();

			orgContact = new AddressInfoBase(AddressType.ORG,
					MasterUtil.getRegions().get(new Random().nextInt(MasterUtil.getRegions().size())));

			alternateOrgContact = new AddressInfoBase(AddressType.ORG_ALTER,
					MasterUtil.getRegions().get(new Random().nextInt(MasterUtil.getRegions().size())));
		}
	}

	/**
	 * @return the alternateOrgContact
	 */
	public AddressInfoBase getAlternateOrgContact() {
		return alternateOrgContact;
	}

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
	 * @return the employerTaxNumber
	 */
	public String getEmployerTaxNumber() {
		return employerTaxNumber;
	}

	/**
	 * @return the employerTaxNumberTitle
	 */
	public String getEmployerTaxNumberTitle() {
		return employerTaxNumberTitle;
	}

	/**
	 * @return the fileNameVATCer
	 */
	public String getFileNameVATCer() {
		return fileNameVATCer;
	}

	/**
	 * @return the orgContact
	 */
	public AddressInfoBase getOrgContact() {
		return orgContact;
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
	 * @return the orgSizeInfo
	 */
	public OrganisationSizeInfo getOrgSizeInfo() {
		return orgSizeInfo;
	}

	/**
	 * @return the physicalAddressInfo
	 */
	public AddressInfoBase getPhysicalAddressInfo() {
		return physicalAddressInfo;
	}

	/**
	 * @return the postAddressInfo
	 */
	public AddressInfoBase getPostAddressInfo() {
		return postAddressInfo;
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
	 * @param alternateOrgContact the alternateOrgContact to set
	 */
	public void setAlternateOrgContact(AddressInfoBase alternateOrgContact) {
		this.alternateOrgContact = alternateOrgContact;
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
	 * @param employerTaxNumber the employerTaxNumber to set
	 */
	public void setEmployerTaxNumber(String employerTaxNumber) {
		this.employerTaxNumber = employerTaxNumber;
	}

	/**
	 * @param employerTaxNumberTitle the employerTaxNumberTitle to set
	 */
	public void setEmployerTaxNumberTitle(String employerTaxNumberTitle) {
		this.employerTaxNumberTitle = employerTaxNumberTitle;
	}

	/**
	 * @param fileNameVATCer the fileNameVATCer to set
	 */
	public void setFileNameVATCer(String fileNameVATCer) {
		this.fileNameVATCer = fileNameVATCer;
		BindUtils.postNotifyChange(this, "fileNameVATCer");
	}

	/**
	 * @param orgContact the orgContact to set
	 */
	public void setOrgContact(AddressInfoBase orgContact) {
		this.orgContact = orgContact;
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
	 * @param orgSizeInfo the orgSizeInfo to set
	 */
	public void setOrgSizeInfo(OrganisationSizeInfo orgSizeInfo) {
		this.orgSizeInfo = orgSizeInfo;
	}

	/**
	 * @param physicalAddressInfo the physicalAddressInfo to set
	 */
	public void setPhysicalAddressInfo(AddressInfoBase physicalAddressInfo) {
		this.physicalAddressInfo = physicalAddressInfo;
	}

	/**
	 * @param postAddressInfo the postAddressInfo to set
	 */
	public void setPostAddressInfo(AddressInfoBase postAddressInfo) {
		this.postAddressInfo = postAddressInfo;
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
	
	public void sdlNumberChange() {
		X_C_BPartner bPartner = new Query(Env.getCtx(), I_C_BPartner.Table_Name, 
				String.format("%s = ?", I_C_BPartner.COLUMNNAME_Value), null)
				.setParameters(sdlNumber)
				.first();
		
		if (bPartner != null) {
			employerName = bPartner.getName();
			employerTaxNumber = bPartner.getTaxID();
			orgRegistrationNumber = bPartner.getReferenceNo();
			BindUtils.postNotifyChange(this, "employerName", "employerTaxNumber", "orgRegistrationNumber");
			
			orgSizeInfo.setNumOfEmployer(bPartner.get_ValueAsInt("ZZ_Number_Of_Employees"));
			
			int prevApprovedCount = DB.getSQLValueEx(null, 
					String.format("SELECT Count (*) FROM ZZ_WSP_ATR_Approvals WHERE C_BPartner_ID = ? AND ZZ_Grant_Status = 'A'"), 
					bPartner.getC_BPartner_ID());
			
			
			orgSizeInfo.setSubmittedWSP(prevApprovedCount > 0);
			BindUtils.postNotifyChange(orgSizeInfo, "submittedWSPText", "numOfEmployer");
		}
	}

	public X_C_BPartner getCetTvetCollegeSelected() {
		return cetTvetCollegeSelected;
	}

	public void setCetTvetCollegeSelected(X_C_BPartner cetTvetCollegeSelected) {
		this.cetTvetCollegeSelected = cetTvetCollegeSelected;
	}
}
