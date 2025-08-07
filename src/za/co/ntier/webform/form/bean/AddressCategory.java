package za.co.ntier.webform.form.bean;

public enum AddressCategory {
	CANDIDACY(), CANDIDACY_ALTER(), INTERNSHIP(), INTERNSHIP_ALTER(), ORG(), ORG_ALTER(), PHYSICAL(), POSTAL(),
	UNKNOWN();

	public boolean isProgramContact() {
		return this == CANDIDACY || this == INTERNSHIP || this == CANDIDACY_ALTER || this == INTERNSHIP_ALTER;
	}
}
