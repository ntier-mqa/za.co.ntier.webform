package za.co.ntier.webform.form.bean;

import za.co.ntier.webform.model.X_ZZ_FormContact;

public enum AddressType {
	MAIN(X_ZZ_FormContact.ZZ_CONTACTTYPE_Main), MAIN_ALTER(X_ZZ_FormContact.ZZ_CONTACTTYPE_MainAlter),
	ORG(X_ZZ_FormContact.ZZ_CONTACTTYPE_Org), ORG_ALTER(X_ZZ_FormContact.ZZ_CONTACTTYPE_OrgAlter),
	PHYSICAL(X_ZZ_FormContact.ZZ_CONTACTTYPE_Physical), POSTAL(X_ZZ_FormContact.ZZ_CONTACTTYPE_Postal),
	VACATION(X_ZZ_FormContact.ZZ_CONTACTTYPE_Vacation), UNKNOWN("UNKNOWN CONTACT TYPE");

	String contactType;

	AddressType(String contactType) {
		this.contactType = contactType;
	}

	public String getAddressTitle(ProgramType programType, boolean isAlternate) {
		if (this == AddressType.PHYSICAL)
			return "PHYSICAL ADDRESS";
		else if (this == AddressType.POSTAL)
			return "POSTAL ADDRESS";
		else if (this == AddressType.ORG)
			return "Primary Organisation Contact:";
		else if (this == AddressType.ORG_ALTER)
			return "Alternate Organisation Contact:";
		else if (this == AddressType.VACATION)
			return "Contact details of person responsible for VACATION WORK:";
		else if (programType == null)
			return UNKNOWN.toString();
		else if (programType == ProgramType.CANDIDACY && !isAlternate)
			return "Contact details of person responsible for CANDIDACY:";
		else if (programType == ProgramType.CANDIDACY && isAlternate)
			return "Alternate contact details of the person responsible for CANDIDACY:";
		else if (programType == ProgramType.INTERNSHIP && !isAlternate)
			return "Contact details of person responsible for INTERNSHIP:";
		else if (programType == ProgramType.INTERNSHIP && isAlternate)
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
		else if (programType == ProgramType.ARTISAN_DEV)
			return "Contact details of person responsible for Artisan Development:";
		else if (programType == ProgramType.CENTRE_SPECIALISATION)
			return "Contact details of person responsible for Centre of Specialisation";
		else if (programType == ProgramType.ARTISAN_RPL)
			return "Contact details of person responsible for Artisan RPL:";
		else if (programType == ProgramType.NON_ARTISAN_DEV)
			return "Contact details of person responsible for Non-Artisan Development:";
		else if (programType == ProgramType.NON_ARTISAN_DEV_RPL)
			return "Contact details of person responsible for Non-Artisan Development RPL:";
		else if (programType == ProgramType.NCV_GRADUATES)
			return "Contact details of person responsible for NCV Graduates:";
		else if (programType == ProgramType.AET)
			return "Contact details of person responsible for AET:";
		else if (programType == ProgramType.OHASSP)
			return "Contact details of person responsible for Occupational Health and Safety Skills Programmes:";
		else if (programType == ProgramType.INHOUSE_TRAINING)
			return "Contact details of person responsible for IHT:";
		else if (programType == ProgramType.TVET || programType == ProgramType.CET
				|| programType == ProgramType.TVET_BURSARS)
			return "Contact details of the responsible person:";
		else
			return UNKNOWN.toString();
	}

	@Override
	public String toString() {
		return contactType;
	}
}
