package za.co.ntier.webform.form.bean;

import za.co.ntier.webform.model.X_ZZ_FormContact;

public enum AddressCategory {
	CANDIDACY(X_ZZ_FormContact.ZZ_CONTACTTYPE_ResponsibleCandidacy), 
	CANDIDACY_ALTER(X_ZZ_FormContact.ZZ_CONTACTTYPE_AlternateResponsibleCandidacy), 
	INTERNSHIP(X_ZZ_FormContact.ZZ_CONTACTTYPE_ResponsibleInternship), 
	INTERNSHIP_ALTER(X_ZZ_FormContact.ZZ_CONTACTTYPE_AlternateResponsibleInternship),
	EXPERIENCE(X_ZZ_FormContact.ZZ_CONTACTTYPE_Experience),
	EXPERIENCE_ALTER(X_ZZ_FormContact.ZZ_CONTACTTYPE_AlternateExperience),
	VACATION(X_ZZ_FormContact.ZZ_CONTACTTYPE_Vacation),
	ORG(X_ZZ_FormContact.ZZ_CONTACTTYPE_PrimaryOrganisationContact), 
	ORG_ALTER(X_ZZ_FormContact.ZZ_CONTACTTYPE_AlternateOrganisationContact), 
	PHYSICAL(X_ZZ_FormContact.ZZ_CONTACTTYPE_PhysicalAddress), 
	POSTAL(X_ZZ_FormContact.ZZ_CONTACTTYPE_PotalAddress),
	UNKNOWN("UNKNOWN CONTACT TYPE");

	String contactType;
	AddressCategory(String contactType) {
		this.contactType = contactType;
	}
	
	public boolean isProgramContact() {
		return this == CANDIDACY || this == INTERNSHIP || this == CANDIDACY_ALTER || this == INTERNSHIP_ALTER || this == EXPERIENCE|| this == EXPERIENCE_ALTER || this == VACATION;
	}
	
	@Override
	public String toString() {
		return contactType;
	}
}
