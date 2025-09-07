package za.co.ntier.webform.form.bean.component;

import java.io.IOException;
import java.util.List;

import org.compiere.model.I_C_BPartner;
import org.compiere.model.Query;
import org.compiere.model.X_C_BPartner;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.zkoss.bind.BindUtils;
import org.zkoss.util.media.Media;

import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.AddressType;
import za.co.ntier.webform.form.bean.ProgramType;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

/**
 * base on bpartner, a Organisation is a bpartner
 */
public class OrganisationInfo implements ISaveForm {
	private AddressInfo alternateOrgContact;
	private int bPartnerId;
	private X_C_BPartner cetTvetCollegeSelected;
	private String fileNameVATCer;
	private String fullPathVATCer;

	private MenuContextInfo menuContextInfo;
	private AddressInfo orgContact;
	private String orgName;
	private String orgNameTitle = "Organisation Name";
	private String orgRegistrationNumber;
	private String orgRegistrationNumberTitle = "Registration No";
	private OrganisationSizeInfo orgSizeInfo;
	private String orgTaxNumber;
	private String orgTaxNumberTitle = "VAT No  (type Not Available if there is no vat number  and attach proof of exemption)";
	private AddressInfo physicalAddressInfo;
	private AddressInfo postAddressInfo;
	private String sdlNumber;
	private String sdlNumberTitle = "Skills Development Levy (SDL) Number (Paying or Exempted)";
	private X_ZZ_Application_Form applicationForm;
	
	private String siteSDLNumber;

	private String siteSDLNumberTitle = "Site SDL Number (if applicable)";

	public OrganisationInfo(MenuContextInfo menuContextInfo) {
		this.menuContextInfo = menuContextInfo;
		postAddressInfo = new AddressInfo(AddressType.POSTAL);

		physicalAddressInfo = new AddressInfo(AddressType.PHYSICAL);
		
		//orgSizeInfo = new OrganisationSizeInfo();
		if (!menuContextInfo.getProgramType().isCetTvet()) {
			orgSizeInfo = new OrganisationSizeInfo();  

			orgContact = new AddressInfo(AddressType.ORG);

			alternateOrgContact = new AddressInfo(AddressType.ORG_ALTER);
		}
	}
	
	/**
	 * @return the alternateOrgContact
	 */
	public AddressInfo getAlternateOrgContact() {
		return alternateOrgContact;
	}

	public int getbPartnerId() {
		return bPartnerId;
	}

	public List<X_C_BPartner> getCetTvetColleges() {
		if (ProgramType.CET.equals(menuContextInfo.getProgramType())) {
			return MasterUtil.getCetColleges();
		} else if (ProgramType.TVET.equals(menuContextInfo.getProgramType())
				|| ProgramType.TVET_BURSARS.equals(menuContextInfo.getProgramType())) {
			return MasterUtil.getTvetColleges();
		}
		return null;
	}

	public X_C_BPartner getCetTvetCollegeSelected() {
		return cetTvetCollegeSelected;
	}

	public String getCetTvetNameTitle() {
		if (ProgramType.CET.equals(menuContextInfo.getProgramType())) {
			return "Name of CET College";
		} else if (ProgramType.TVET.equals(menuContextInfo.getProgramType())
				|| ProgramType.TVET_BURSARS.equals(menuContextInfo.getProgramType()))
			return "Name of TVET College";
		else
			return "unknownCetNameTitle";
	}

	/**
	 * @return the fileNameVATCer
	 */
	public String getFileNameVATCer() {
		return fileNameVATCer;
	}

	public String getFullPathVATCer() {
		return fullPathVATCer;
	}

	public MenuContextInfo getMenuContextInfo() {
		return menuContextInfo;
	}

	/**
	 * @return the orgContact
	 */
	public AddressInfo getOrgContact() {
		return orgContact;
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
	 * @return the orgTaxNumber
	 */
	public String getOrgTaxNumber() {
		return orgTaxNumber;
	}

	/**
	 * @return the orgTaxNumberTitle
	 */
	public String getOrgTaxNumberTitle() {
		return orgTaxNumberTitle;
	}

	/**
	 * @return the physicalAddressInfo
	 */
	public AddressInfo getPhysicalAddressInfo() {
		return physicalAddressInfo;
	}

	/**
	 * @return the postAddressInfo
	 */
	public AddressInfo getPostAddressInfo() {
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

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		applicationForm.setZZ_SDL_No(getSdlNumber());
		applicationForm.setZZ_Side_SDL_No(getSiteSDLNumber());
		applicationForm.setZZ_VAT(getOrgTaxNumber());
		applicationForm.setOrgName(getOrgName());
		applicationForm.setC_BPartner_ID(getbPartnerId());

		if (getOrgSizeInfo() != null) {
			getOrgSizeInfo().saveForm(trxName, applicationForm);
		}
		
		if (physicalAddressInfo != null) {
			physicalAddressInfo.saveForm(trxName, applicationForm);
		}
		
		if (postAddressInfo != null) {
			postAddressInfo.saveForm(trxName, applicationForm);
		}
		
		if (orgContact != null) {
			orgContact.saveForm(trxName, applicationForm);
		}
		
		if (alternateOrgContact != null) {
			alternateOrgContact.saveForm(trxName, applicationForm);
		}
	}

	public void initComponent(X_ZZ_Application_Form applicationForm) {
		this.applicationForm = applicationForm;

		if (applicationForm != null) {
			setSdlNumber(applicationForm.getZZ_SDL_No());
			setSiteSDLNumber(applicationForm.getZZ_Side_SDL_No());
			setOrgTaxNumber(applicationForm.getZZ_VAT());
			setOrgName(applicationForm.getOrgName());
			setbPartnerId(applicationForm.getC_BPartner_ID());
		}

		
		// init sub component
		if (orgSizeInfo != null) {
			orgSizeInfo.initComponent(applicationForm);
		}
		
		if (orgContact != null)
			orgContact.initComponent(applicationForm);
		
		if (alternateOrgContact != null)
			alternateOrgContact.initComponent(applicationForm);
		
		if (physicalAddressInfo != null)
			physicalAddressInfo.initComponent(applicationForm);
		
		if (postAddressInfo != null)
			postAddressInfo.initComponent(applicationForm);
		
		if (orgContact != null)
			orgContact.initComponent(applicationForm);
		
	}

	
	public void sdlNumberChange() {
		X_C_BPartner bPartner = new Query(Env.getCtx(), I_C_BPartner.Table_Name,
				String.format("%s = ?", I_C_BPartner.COLUMNNAME_Value), null).setParameters(sdlNumber).first();

		if (bPartner != null) {
			orgName = bPartner.getName();
			orgTaxNumber = bPartner.getTaxID();
			orgRegistrationNumber = bPartner.getReferenceNo();
			BindUtils.postNotifyChange(this, "orgName", "orgTaxNumber", "orgRegistrationNumber");

			if (orgSizeInfo != null) {
				orgSizeInfo.setNumOfEmployer(bPartner.get_ValueAsInt("ZZ_Number_Of_Employees"));

				int prevApprovedCount = DB.getSQLValueEx(null, String.format(
						"SELECT Count (*) FROM ZZ_WSP_ATR_Approvals WHERE C_BPartner_ID = ? AND ZZ_Grant_Status = 'A'"),
						bPartner.getC_BPartner_ID());

				orgSizeInfo.setSubmittedWSP(prevApprovedCount > 0);
				BindUtils.postNotifyChange(orgSizeInfo, "submittedWSPText", "numOfEmployer");
			}
			

			bPartnerId = bPartner.getC_BPartner_ID();
		} else {
			bPartnerId = 0;
		}
	}

	/**
	 * @param alternateOrgContact the alternateOrgContact to set
	 */
	public void setAlternateOrgContact(AddressInfo alternateOrgContact) {
		this.alternateOrgContact = alternateOrgContact;
	}

	public void setbPartnerId(int bPartnerId) {
		this.bPartnerId = bPartnerId;
	}

	public void setCetTvetCollegeSelected(X_C_BPartner cetTvetCollegeSelected) {
		this.cetTvetCollegeSelected = cetTvetCollegeSelected;
	}

	/**
	 * @param fileNameVATCer the fileNameVATCer to set
	 */
	public void setFileNameVATCer(String fileNameVATCer) {
		this.fileNameVATCer = fileNameVATCer;
		BindUtils.postNotifyChange(this, "fileNameVATCer");
	}

	public void setFullPathVATCer(String fullPathVATCer) {
		this.fullPathVATCer = fullPathVATCer;
	}

	public void setMenuContextInfo(MenuContextInfo menuContextInfo) {
		this.menuContextInfo = menuContextInfo;
	}

	/**
	 * @param orgContact the orgContact to set
	 */
	public void setOrgContact(AddressInfo orgContact) {
		this.orgContact = orgContact;
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
	 * @param orgTaxNumber the orgTaxNumber to set
	 */
	public void setOrgTaxNumber(String orgTaxNumber) {
		this.orgTaxNumber = orgTaxNumber;
	}

	/**
	 * @param orgTaxNumberTitle the orgTaxNumberTitle to set
	 */
	public void setOrgTaxNumberTitle(String orgTaxNumberTitle) {
		this.orgTaxNumberTitle = orgTaxNumberTitle;
	}

	/**
	 * @param physicalAddressInfo the physicalAddressInfo to set
	 */
	public void setPhysicalAddressInfo(AddressInfo physicalAddressInfo) {
		this.physicalAddressInfo = physicalAddressInfo;
	}

	/**
	 * @param postAddressInfo the postAddressInfo to set
	 */
	public void setPostAddressInfo(AddressInfo postAddressInfo) {
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

	public void uploadFile(Media media) throws IOException {
		setFileNameVATCer(media.getName());
		fullPathVATCer = MasterUtil.saveUploadFile(media);

	}

	/**
	 * @return the applicationForm
	 */
	public X_ZZ_Application_Form getApplicationForm() {
		return applicationForm;
	}

	/**
	 * @param applicationForm the applicationForm to set
	 */
	public void setApplicationForm(X_ZZ_Application_Form applicationForm) {
		this.applicationForm = applicationForm;
	}
}
