package za.co.ntier.webform.form.bean;

import za.co.ntier.webform.model.X_ZZ_FormContact;

public enum AddressCategory {
	MAIN(X_ZZ_FormContact.ZZ_CONTACTTYPE_PrimaryOrganisationContact),
	MAIN_ALTER(X_ZZ_FormContact.ZZ_CONTACTTYPE_PrimaryOrganisationContact),
	ORG(X_ZZ_FormContact.ZZ_CONTACTTYPE_PrimaryOrganisationContact), 
	ORG_ALTER(X_ZZ_FormContact.ZZ_CONTACTTYPE_AlternateOrganisationContact), 
	PHYSICAL(X_ZZ_FormContact.ZZ_CONTACTTYPE_PhysicalAddress), 
	POSTAL(X_ZZ_FormContact.ZZ_CONTACTTYPE_PotalAddress),
	VACATION(X_ZZ_FormContact.ZZ_CONTACTTYPE_PotalAddress),
	UNKNOWN("UNKNOWN CONTACT TYPE");

	String contactType;
	AddressCategory(String	 contactType) {
		this.contactType = contactType;
	}
	
	@Override
	public String toString() {
		return contactType;
	}
	
	public String getAddressTitle(ProgramType programType, boolean isAlternate) {
		if (this == AddressCategory.PHYSICAL)
			return "PHYSICAL ADDRESS";
		else if (this == AddressCategory.POSTAL)
			return "POSTAL ADDRESS";
		else if (this == AddressCategory.ORG)
			return "Primary Organisation Contact:";
		else if (this == AddressCategory.ORG_ALTER)
			return "Alternate Organisation Contact:";
		else if (this == AddressCategory.VACATION)
			return "Contact details of person responsible for VACATION WORK:";
		else if (programType == null)
			return UNKNOWN.toString();
		else if (programType == ProgramType.CANDIDACY && !isAlternate)
			return "Contact details of person responsible for CANDIDACY:";
		else if (programType == ProgramType.CANDIDACY && isAlternate)
			return "Alternate contact details of the person responsible for CANDIDACY:";
		else if (programType == ProgramType.INTERNSHIP  && !isAlternate)
			return "Contact details of person responsible for INTERNSHIP:";
		else if (programType == ProgramType.INTERNSHIP  && isAlternate)
			return "Alternate contact details of the person responsible for INTERNSHIP:";
		else if (programType == ProgramType.EXPERIENCE && !isAlternate)
			return "Contact details of person responsible for PRACTICAL TRAINING:";
		else if (programType == ProgramType.EXPERIENCE && isAlternate)
			return "Alternate contact details of the person responsible for PRACTICAL TRAINING:";
		else if (programType == ProgramType.DEV_PROGRAM && !isAlternate)
			return "Contact details of person responsible for MEDP";
		else if (programType == ProgramType.DEV_PROGRAM && isAlternate)
			return "Alternative contact details of person responsible for MEDP";
		else if (programType == ProgramType.ARTISAN_AIDES)
			return "Contact details of person responsible for Artisan Aides:";
		else if (programType == ProgramType.CENTRE_SPECIALISATION)
			return "Contact details of person responsible for Centre of Specialisation";
		else if (programType == ProgramType.ARTISAN_RPL)
			return "Contact details of person responsible for Artisan RPL:";
		else if (programType == ProgramType.NON_ARTISAN_DEV)
			return "Contact details of person responsible for Non-Artisan Development:";
		else if (programType == ProgramType.NON_ARTISAN_DEV_RPL)
			return "Contact details of person responsible for Non-Artisan Development RPL:";
		else if (programType == ProgramType.NCV_LEVEL_4_GRADUATES)
			return "Contact details of person responsible for NCV Graduates:";
		else if (programType == ProgramType.AET)
			return "Contact details of person responsible for AET:";
		else if (programType == ProgramType.OCCUPATIONAL_HEALTH_AND_SAFETY_SKILLS_PROGRAMMES)
			return "Contact details of person responsible for Occupational Health and Safety Skills Programmes:";
		else if (programType == ProgramType.INHOUSE_TRAINING)
			return "Contact details of person responsible for IHT:";
		else
			return UNKNOWN.toString();
	}
}
