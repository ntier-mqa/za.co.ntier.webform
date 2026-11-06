package za.co.ntier.webform.sdr.viewmodel;

import java.text.SimpleDateFormat;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.compiere.model.MBPartner;
import org.compiere.model.I_C_Location;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MColumn;
import org.compiere.model.MLocation;
import org.compiere.model.I_C_Period;
import org.compiere.model.MPeriod;
import org.compiere.model.MRefList;
import org.compiere.model.MTable;
import org.compiere.model.MUser;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.Env;
import org.compiere.util.ValueNamePair;
import org.compiere.util.DisplayType;

import za.co.ntier.api.model.I_ZZLearnerDGProgramme;
import za.co.ntier.api.model.I_ZZLearnerLearnership;
import za.co.ntier.api.model.I_C_BPartner;
import za.co.ntier.api.model.I_C_BP_AC;
import za.co.ntier.api.model.X_C_BP_AC;
import za.co.ntier.api.model.I_ZZPerson;
import za.co.ntier.api.model.I_ZZPersonAddress;
import za.co.ntier.api.model.I_ZZQualification;
import za.co.ntier.api.model.I_ZZLearnership;
import za.co.ntier.api.model.I_ZZSkillsProgramme;
import za.co.ntier.api.model.I_ZZQctoLearnership;
import za.co.ntier.api.model.I_ZZQctoQualification;
import za.co.ntier.api.model.I_ZZQctoSkillsProgramme;
import za.co.ntier.api.model.I_ZZ_ParentDetails;
import za.co.ntier.api.model.I_ZZ_PostSchoolEducation_Details;
import za.co.ntier.api.model.X_ZZLearner_v;
import za.co.ntier.api.model.X_C_BPartner;
import za.co.ntier.api.model.X_ZZPerson;
import za.co.ntier.api.model.X_ZZ_ParentDetails;
import za.co.ntier.api.model.X_ZZ_PostSchoolEducation_Details;
import za.co.ntier.webform.sdr.component.util.BuildFormUtil;

/** Report values from learner details and the selected registration, including unsaved form values. */
public final class LearnerAgreementData
{
	private LearnerAgreementData()
	{
	}

	// Shared Java/JRXML report keys.
	public static final String	PARAMETER							= "AgreementData";
	public static final String	AGREEMENT_REFERENCE_NUMBER			= "AgreementReferenceNumber";
	public static final String	ASSESSMENT_QUALITY_PARTNER			= "AssessmentQualityPartner";
	public static final String	BIRTH_DATE							= "BirthDate";
	public static final String	CITIZEN_ANSWER						= "CitizenAnswer";
	public static final String	DISABILITY							= "Disability";
	public static final String	DISABILITY_ANSWER					= "DisabilityAnswer";
	public static final String	DISABILITY_DESCRIPTION				= "DisabilityDescription";
	public static final String	EMAIL								= "Email";
	public static final String	EMPLOYER_ADDRESS					= "EmployerAddress";
	public static final String	EMPLOYER_CELL						= "EmployerCell";
	public static final String	EMPLOYER_CONTACT					= "EmployerContact";
	public static final String	EMPLOYER_EMAIL						= "EmployerEmail";
	public static final String	EMPLOYER_FAX						= "EmployerFax";
	public static final String	EMPLOYER_LEVY						= "EmployerLevy";
	public static final String	EMPLOYER_NAME						= "EmployerName";
	public static final String	EMPLOYER_POSTAL_ADDRESS				= "EmployerPostalAddress";
	public static final String	EMPLOYER_SDL						= "EmployerSdl";
	public static final String	EMPLOYER_SIC						= "EmployerSic";
	public static final String	EMPLOYER_TELEPHONE					= "EmployerTelephone";
	public static final String	EMPLOYER_TRADING_NAME				= "EmployerTradingName";
	public static final String	EXPECTED_END_DATE					= "ExpectedEndDate";
	public static final String	AGREEMENT_START_DATE				= "AgreementStartDate";
	public static final String	FINANCIAL_YEAR						= "FinancialYear";
	public static final String	FUNDING_TYPE						= "FundingType";
	public static final String	GENDER								= "Gender";
	public static final String	GUARDIAN_EMAIL						= "GuardianEmail";
	public static final String	GUARDIAN_HOME_ADDRESS				= "GuardianHomeAddress";
	public static final String	GUARDIAN_ID							= "GuardianId";
	public static final String	GUARDIAN_NAME						= "GuardianName";
	public static final String	GUARDIAN_POSTAL_ADDRESS				= "GuardianPostalAddress";
	public static final String	GUARDIAN_SIGNATORY_NAME				= "GuardianSignatoryName";
	public static final String	GUARDIAN_TELEPHONE					= "GuardianTelephone";
	public static final String	HIGHEST_QUALIFICATION				= "HighestQualification";
	public static final String	LEARNER_ID							= "LearnerId";
	public static final String	LEARNER_NAME						= "LearnerName";
	public static final String	LEARNER_TITLE						= "LearnerTitle";
	public static final String	NON_CITIZEN_DETAILS					= "NonCitizenDetails";
	public static final String	OCCUPATION							= "Occupation";
	public static final String	PHYSICAL_ADDRESS					= "PhysicalAddress";
	public static final String	POSTAL_ADDRESS						= "PostalAddress";
	public static final String	PROGRAMME_CODE						= "ProgrammeCode";
	public static final String	PROGRAMME_TYPE						= "ProgrammeType";
	public static final String PROGRAMME_TITLE = "ProgrammeTitle";
	public static final String SECONDARY_EMPLOYER = "SecondaryEmployer";
	public static final String SECONDARY_PROVIDER = "SecondaryProvider";
	public static final String SUFFIX_LEAD = "Lead";
	public static final String SUFFIX_ACCREDITATION_NUMBER = "AccreditationNumber";
	public static final String SUFFIX_ACCREDITATION_STATUS = "AccreditationStatus";
	public static final String SUFFIX_ACCREDITATION_START_DATE = "AccreditationStartDate";
	public static final String SUFFIX_ACCREDITATION_END_DATE = "AccreditationEndDate";
	public static final String	PROVIDER_ACCREDITATION_END_DATE		= "ProviderAccreditationEndDate";
	public static final String	PROVIDER_ACCREDITATION_NUMBER		= "ProviderAccreditationNumber";
	public static final String	PROVIDER_ACCREDITATION_START_DATE	= "ProviderAccreditationStartDate";
	public static final String	PROVIDER_ACCREDITATION_STATUS		= "ProviderAccreditationStatus";
	public static final String	PROVIDER_ACCREDITING_COUNCIL		= "ProviderAccreditingCouncil";
	public static final String	PROVIDER_ADDRESS					= "ProviderAddress";
	public static final String	PROVIDER_CONTACT					= "ProviderContact";
	public static final String	PROVIDER_EMAIL						= "ProviderEmail";
	public static final String	PROVIDER_FAX						= "ProviderFax";
	public static final String	PROVIDER_LEVY						= "ProviderLevy";
	public static final String	PROVIDER_NAME						= "ProviderName";
	public static final String	PROVIDER_POSTAL_ADDRESS				= "ProviderPostalAddress";
	public static final String	PROVIDER_SDL						= "ProviderSdl";
	public static final String	PROVIDER_SIC						= "ProviderSic";
	public static final String	PROVIDER_TELEPHONE					= "ProviderTelephone";
	public static final String	PROVIDER_TRADING_NAME				= "ProviderTradingName";
	public static final String	QUALIFICATION_TITLE					= "QualificationTitle";
	public static final String	RACE								= "Race";
	public static final String	SAQA_ID								= "SaqaId";
	public static final String	SOCIO_ECONOMIC_STATUS				= "SocioEconomicStatus";
	public static final String	TELEPHONE							= "Telephone";
	public static final String	SUFFIX_ADDRESS						= "Address";
	public static final String	SUFFIX_CELL							= "Cell";
	public static final String	SUFFIX_CONTACT						= "Contact";
	public static final String	SUFFIX_EMAIL						= "Email";
	public static final String	SUFFIX_FAX							= "Fax";
	public static final String	SUFFIX_LEVY							= "Levy";
	public static final String	SUFFIX_NAME							= "Name";
	public static final String	SUFFIX_POSTAL_ADDRESS				= "PostalAddress";
	public static final String	SUFFIX_SDL							= "Sdl";
	public static final String	SUFFIX_SIC							= "Sic";
	public static final String	SUFFIX_TELEPHONE					= "Telephone";
	public static final String	SUFFIX_TRADING_NAME					= "TradingName";

	static Map<String, String> build(	X_ZZLearner_v learner, Map<String, Object> registration, String programmeColumn,
										String programmeTable, String codeColumn, String programmeType, List<String> warnings)
	{
		Map<String, String> data = new LinkedHashMap<>();
		data.put(LearnerAgreementData.LEARNER_TITLE, display(learner, I_ZZPerson.COLUMNNAME_ZZLkpTitle));
		data.put(LearnerAgreementData.LEARNER_NAME, join(learner.getZZFirstName(), learner.getZZMiddleName(), learner.getSurname()));
		data.put(LearnerAgreementData.LEARNER_ID, first(learner.getZZ_ID_Passport_No(), learner.getZZOtherIDNo()));
		data.put(LearnerAgreementData.BIRTH_DATE, date(learner.getBirthday()));
		data.put(LearnerAgreementData.GENDER, display(learner, I_ZZPerson.COLUMNNAME_ZZGender));
		data.put(LearnerAgreementData.RACE, display(learner, I_ZZPerson.COLUMNNAME_ZZEquity));
		// Disability source is pending; leave report values and both checkbox options blank.
		data.put(LearnerAgreementData.DISABILITY, "");
		data.put(LearnerAgreementData.DISABILITY_ANSWER, "");
		data.put(LearnerAgreementData.DISABILITY_DESCRIPTION, "");
		PO status = linked(learner, I_ZZPerson.COLUMNNAME_ZZ_LI_CitizenResidentialStatus_ID);
		String statusName = status == null ? "" : value(status, I_C_BPartner.COLUMNNAME_Name);
		String citizenAnswer = citizenAnswer(statusName);
		data.put(LearnerAgreementData.CITIZEN_ANSWER, citizenAnswer);
		data.put(LearnerAgreementData.NON_CITIZEN_DETAILS, "No".equals(citizenAnswer) ? statusName : "");
		data.put(LearnerAgreementData.SOCIO_ECONOMIC_STATUS, display(learner, I_ZZPerson.COLUMNNAME_ZZ_LI_SocioEconomicStatus_ID));
		data.put(LearnerAgreementData.PHYSICAL_ADDRESS, address(learner.getZZPhysicalLocation_ID()));
		data.put(LearnerAgreementData.POSTAL_ADDRESS, learner.getZZPostalLocation_ID() == learner.getZZPhysicalLocation_ID()
																																? ""
																																: address(learner.getZZPostalLocation_ID()));
		data.put(LearnerAgreementData.TELEPHONE, join(learner.getPhone(), learner.getPhone2()));
		data.put(LearnerAgreementData.EMAIL, learner.getEMail());

		List<X_ZZ_ParentDetails> parents = new Query(	Env.getCtx(), I_ZZ_ParentDetails.Table_Name,
														I_ZZ_ParentDetails.COLUMNNAME_ZZLearner_ID + "=?", null)
																												.setParameters(learner.getZZLearner_ID())
																												.setOnlyActiveRecords(true).list();
		X_ZZ_ParentDetails parent = unique(	parents,
											"Multiple active parent/guardian records: guardian fields were left blank. Review the learner Parent/Guardian tab.",
											warnings);
		if (parent != null)
		{
			data.put(LearnerAgreementData.GUARDIAN_NAME, join(parent.getZZFirstName(), parent.getZZMiddleName(), parent.getSurname()));
			if (parent.getZZParentPerson_ID() > 0)
			{
				X_ZZPerson person = new X_ZZPerson(Env.getCtx(), parent.getZZParentPerson_ID(), null);
				if (data.get(LearnerAgreementData.GUARDIAN_NAME).isBlank())
					data.put(LearnerAgreementData.GUARDIAN_NAME, join(person.getZZFirstName(), person.getZZMiddleName(), person.getSurname()));
				data.put(LearnerAgreementData.GUARDIAN_ID, first(person.getZZ_ID_Passport_No(), person.getZZOtherIDNo()));
				data.put(LearnerAgreementData.GUARDIAN_HOME_ADDRESS, address(person.getZZPhysicalLocation_ID()));
				data.put(LearnerAgreementData.GUARDIAN_POSTAL_ADDRESS, person.getZZPostalLocation_ID() == person.getZZPhysicalLocation_ID()
																																			? ""
																																			: address(person.getZZPostalLocation_ID()));
				data.put(LearnerAgreementData.GUARDIAN_TELEPHONE, join(person.getPhone(), person.getPhone2()));
				data.put(LearnerAgreementData.GUARDIAN_EMAIL, person.getEMail());
			}
			if (learner.getBirthday() != null
				&& learner.getBirthday().toLocalDateTime().toLocalDate().plusYears(18).isAfter(LocalDate.now()))
				data.put(LearnerAgreementData.GUARDIAN_SIGNATORY_NAME, data.get(LearnerAgreementData.GUARDIAN_NAME));
		}
		List<X_ZZ_PostSchoolEducation_Details> education = new Query(	Env.getCtx(), I_ZZ_PostSchoolEducation_Details.Table_Name,
																		I_ZZ_PostSchoolEducation_Details.COLUMNNAME_ZZLearner_ID + "=?", null)
																																				.setParameters(learner.getZZLearner_ID())
																																				.setOnlyActiveRecords(true)
																																				.list();
		X_ZZ_PostSchoolEducation_Details highest = unique(	education,
															"Multiple post-school qualifications: highest qualification title was left blank because no authoritative ranking/selection is recorded.",
															warnings);
		if (highest != null)
			data.put(LearnerAgreementData.HIGHEST_QUALIFICATION, highest.getName());
		else if (education.isEmpty())
			data.put(LearnerAgreementData.HIGHEST_QUALIFICATION, display(learner, I_ZZPerson.COLUMNNAME_ZZ_LI_HighestEducation_ID));

		int programmeId = id(registration.get(programmeColumn));
		PO programme = programmeId > 0 && programmeTable != null
				? MTable.get(Env.getCtx(), programmeTable).getPO(programmeId, null) : null;
		registration(data, registration, programme, codeColumn, programmeType, warnings);
		return data;
	}

	/** Build from the merged form before save; do not reload learner or child rows. */
	static Map<String, String> buildFromForm(Map<String, Object> learner, Map<String, Object> physical,
			Map<String, Object> postal, Map<String, Object> parent, Map<String, Object> education,
			Map<String, Object> registration, PO programme, String codeColumn, String programmeType,
			boolean disability, List<String> warnings)
	{
		Map<String, String> data = new LinkedHashMap<>();
		data.put(LEARNER_TITLE, text(learner.get(I_ZZPerson.COLUMNNAME_ZZLkpTitle)));
		data.put(LEARNER_NAME, join(text(learner.get(I_ZZPerson.COLUMNNAME_ZZFirstName)),
				text(learner.get(I_ZZPerson.COLUMNNAME_ZZMiddleName)), text(learner.get(I_ZZPerson.COLUMNNAME_Surname))));
		data.put(LEARNER_ID, first(text(learner.get(I_ZZPerson.COLUMNNAME_ZZ_ID_Passport_No)),
				text(learner.get(I_ZZPerson.COLUMNNAME_ZZOtherIDNo))));
		data.put(BIRTH_DATE, date(learner.get(I_ZZPerson.COLUMNNAME_Birthday)));
		data.put(GENDER, text(learner.get(I_ZZPerson.COLUMNNAME_ZZGender)));
		data.put(RACE, text(learner.get(I_ZZPerson.COLUMNNAME_ZZEquity)));
		data.put(DISABILITY, disability ? "Yes" : "No");
		data.put(DISABILITY_ANSWER, data.get(DISABILITY));
		// The merged form captures Yes/No, without a disability diagnosis.
		data.put(DISABILITY_DESCRIPTION, "");
		String status = text(learner.get(I_ZZPerson.COLUMNNAME_ZZ_LI_CitizenResidentialStatus_ID));
		String citizen = citizenAnswer(status);
		data.put(CITIZEN_ANSWER, citizen);
		data.put(NON_CITIZEN_DETAILS, "No".equals(citizen)
				? join(status, text(learner.get(I_ZZPerson.COLUMNNAME_ZZ_Nationality_ID))) : "");
		data.put(SOCIO_ECONOMIC_STATUS, text(learner.get(I_ZZPerson.COLUMNNAME_ZZ_LI_SocioEconomicStatus_ID)));
		data.put(PHYSICAL_ADDRESS, formAddress(physical));
		String postalAddress = formAddress(postal);
		data.put(POSTAL_ADDRESS, postalAddress.equals(data.get(PHYSICAL_ADDRESS)) ? "" : postalAddress);
		data.put(TELEPHONE, join(text(learner.get(I_ZZPerson.COLUMNNAME_Phone)), text(learner.get(I_ZZPerson.COLUMNNAME_Phone2))));
		data.put(EMAIL, text(learner.get(I_ZZPerson.COLUMNNAME_EMail)));
		// The Highest Education selector stores the current display name in Qualification.
		String qualification = first(text(education.get(I_ZZ_PostSchoolEducation_Details.COLUMNNAME_Qualification)),
				text(education.get(I_ZZ_PostSchoolEducation_Details.COLUMNNAME_Name)));
		data.put(HIGHEST_QUALIFICATION, first(qualification, text(learner.get(I_ZZPerson.COLUMNNAME_ZZ_LI_HighestEducation_ID))));
		data.put(GUARDIAN_NAME, join(text(parent.get(I_ZZ_ParentDetails.COLUMNNAME_ZZFirstName)),
				text(parent.get(I_ZZ_ParentDetails.COLUMNNAME_ZZMiddleName)), text(parent.get(I_ZZ_ParentDetails.COLUMNNAME_Surname))));
		Object guardian = parent.get(I_ZZ_ParentDetails.COLUMNNAME_ZZParentPerson_ID);
		if (guardian instanceof X_ZZPerson)
		{
			X_ZZPerson person = (X_ZZPerson) guardian;
			data.put(GUARDIAN_ID, first(person.getZZ_ID_Passport_No(), person.getZZOtherIDNo()));
			data.put(GUARDIAN_HOME_ADDRESS, address(person.getZZPhysicalLocation_ID()));
			data.put(GUARDIAN_POSTAL_ADDRESS, person.getZZPostalLocation_ID() == person.getZZPhysicalLocation_ID()
					? "" : address(person.getZZPostalLocation_ID()));
			data.put(GUARDIAN_TELEPHONE, join(person.getPhone(), person.getPhone2()));
			data.put(GUARDIAN_EMAIL, person.getEMail());
		}
		Object birthday = learner.get(I_ZZPerson.COLUMNNAME_Birthday);
		if (birthday instanceof Timestamp
				&& ((Timestamp) birthday).toLocalDateTime().toLocalDate().plusYears(18).isAfter(LocalDate.now()))
			data.put(GUARDIAN_SIGNATORY_NAME, data.get(GUARDIAN_NAME));
		registration(data, registration, programme, codeColumn, programmeType, warnings);
		data.put(FUNDING_TYPE, text(registration.get(I_ZZLearnerDGProgramme.COLUMNNAME_ZZFundingType)));
		return data;
	}

	private static String formAddress(Map<String, Object> values)
	{
		return Stream.of(I_C_Location.COLUMNNAME_Address1, I_C_Location.COLUMNNAME_Address2,
				I_C_Location.COLUMNNAME_C_City_ID, I_C_Location.COLUMNNAME_Postal, I_C_Location.COLUMNNAME_C_Region_ID)
				.map(column -> text(values.get(column))).filter(value -> !value.isBlank()).collect(Collectors.joining(", "));
	}

	private static String text(Object value)
	{
		if (value instanceof ValueNamePair)
			return ((ValueNamePair) value).getName();
		if (value instanceof PO)
			return value((PO) value, I_C_BPartner.COLUMNNAME_Name);
		return value == null ? "" : value.toString();
	}

	private static void registration(Map<String, String> data, Map<String, Object> registration,
			PO programme, String codeColumn, String programmeType, List<String> warnings)
	{
		party(data, "Employer", id(registration.get(I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_Employer_ID)));
		partyDetails(	data, registration, "Employer", I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_Employer_ID,
						I_ZZLearnerDGProgramme.COLUMNNAME_ZZWAContact_ID, I_ZZLearnerDGProgramme.COLUMNNAME_ZZWALevy);
		party(data, "Provider", id(registration.get(I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_SDP_ID)));
		partyDetails(	data, registration, "Provider", I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_SDP_ID,
						I_ZZLearnerDGProgramme.COLUMNNAME_ZZLeadSDProviderContact_ID, I_ZZLearnerDGProgramme.COLUMNNAME_ZZLeadSDProviderLevy);

		party(data, SECONDARY_EMPLOYER, id(registration.get(I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondaryWA_ID)));
		partyDetails(data, registration, SECONDARY_EMPLOYER, I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondaryWA_ID,
				I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondaryWAContact_ID, I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondaryWALevy);
		party(data, SECONDARY_PROVIDER, id(registration.get(I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondarySDProvider_ID)));
		partyDetails(data, registration, SECONDARY_PROVIDER, I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondarySDProvider_ID,
				I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondarySDProviderContact_ID, I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondarySDProviderLevy);
		providerAccreditation(data, SECONDARY_PROVIDER,
				id(registration.get(I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondarySDProvider_ID)), warnings);

		data.put(LearnerAgreementData.PROGRAMME_TYPE, programmeType);
		data.put(PROGRAMME_TITLE, programmeType);
		int financialYearId = id(registration.get(I_ZZLearnerDGProgramme.COLUMNNAME_ZZFinancialYear_ID));
		if (financialYearId <= 0)
			financialYearId = id(registration.get(I_ZZLearnerLearnership.COLUMNNAME_ZZ_FinYear_ID));
		data.put(LearnerAgreementData.FINANCIAL_YEAR, financialYear(financialYearId));
		Object agreementReference = registration.get(I_ZZLearnerDGProgramme.COLUMNNAME_ZZAgreementReferenceNumber);
		data.put(LearnerAgreementData.AGREEMENT_REFERENCE_NUMBER, agreementReference == null ? "" : agreementReference.toString());
		data.put(LearnerAgreementData.EXPECTED_END_DATE, date(registration.get(I_ZZLearnerDGProgramme.COLUMNNAME_ZZCompletionDate)));
		data.put(LearnerAgreementData.AGREEMENT_START_DATE, date(registration.get(I_ZZLearnerDGProgramme.COLUMNNAME_ZZCommencementDate)));
		if (programme != null)
		{
			data.put(LearnerAgreementData.PROGRAMME_CODE, value(programme, codeColumn));
			data.put(LearnerAgreementData.PROVIDER_ACCREDITING_COUNCIL, display(programme, I_ZZQualification.COLUMNNAME_ZZQualityAssuranceBody));
			data.put(LearnerAgreementData.OCCUPATION, display(programme, I_ZZQualification.COLUMNNAME_ZZLkpOfoOccupationTree_ID));
			PO qualification = putQualificationDetails(data, programme);
			PO aqpQualification = linked(programme, I_ZZQualification.COLUMNNAME_ZZQualification_ID);
			if (aqpQualification == null && qualification != null)
				aqpQualification = linked(qualification, I_ZZQualification.COLUMNNAME_ZZQualification_ID);
			if (aqpQualification != null)
				data.put(LearnerAgreementData.ASSESSMENT_QUALITY_PARTNER, display(aqpQualification, I_ZZQualification.COLUMNNAME_ZZQualityAssuranceBody));
			if (qualification != null)
			{
				if (data.get(LearnerAgreementData.PROVIDER_ACCREDITING_COUNCIL) == null || data	.get(LearnerAgreementData.PROVIDER_ACCREDITING_COUNCIL)
																								.isBlank())
					data.put(LearnerAgreementData.PROVIDER_ACCREDITING_COUNCIL, display(qualification,
																						I_ZZQualification.COLUMNNAME_ZZQualityAssuranceBody));
			}
		}
		providerAccreditation(data, "Provider", id(registration.get(I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_SDP_ID)), warnings);
	}

	static void putProgrammeDefinition(Map<String, String> data, boolean enterQualification,
			String selectedProgrammeType, String dgProgrammeTitle)
	{
		data.put(PROGRAMME_TITLE, enterQualification ? first(selectedProgrammeType, dgProgrammeTitle) : dgProgrammeTitle);
		if (!enterQualification)
		{
			data.put(QUALIFICATION_TITLE, dgProgrammeTitle);
			data.put(SAQA_ID, "");
		}
		else if (selectedProgrammeType == null || selectedProgrammeType.isBlank())
			data.put(QUALIFICATION_TITLE, first(data.get(QUALIFICATION_TITLE), dgProgrammeTitle));
	}

	/** Use the programme's actual qualification relationship, independent of lookup/view metadata. */
	static PO putQualificationDetails(Map<String, String> data, PO programme)
	{
		PO qualification = null;
		String programmeTitle = "";
		if (programme instanceof I_ZZQctoLearnership)
		{
			I_ZZQctoLearnership selected = (I_ZZQctoLearnership) programme;
			programmeTitle = selected.getZZLearnershipTitle();
			if (selected.getZZQctoQualification_ID() > 0)
				qualification = (PO) selected.getZZQctoQualification();
			if (qualification == null && selected.getZZQualification_ID() > 0)
				qualification = (PO) selected.getZZQualification();
		}
		else if (programme instanceof I_ZZLearnership)
		{
			I_ZZLearnership selected = (I_ZZLearnership) programme;
			programmeTitle = selected.getZZLearnershipTitle();
			if (selected.getZZQualification_ID() > 0)
				qualification = (PO) selected.getZZQualification();
		}
		else if (programme instanceof I_ZZSkillsProgramme)
		{
			I_ZZSkillsProgramme selected = (I_ZZSkillsProgramme) programme;
			programmeTitle = selected.getZZSkillsProgrammeTitle();
			if (selected.getZZQualification_ID() > 0)
				qualification = (PO) selected.getZZQualification();
		}
		else if (programme instanceof I_ZZQctoSkillsProgramme)
		{
			I_ZZQctoSkillsProgramme selected = (I_ZZQctoSkillsProgramme) programme;
			programmeTitle = selected.getZZSkillsProgrammeTitle();
			if (selected.getZZQualification_ID() > 0)
				qualification = (PO) selected.getZZQualification();
		}
		else if (programme instanceof I_ZZQualification || programme instanceof I_ZZQctoQualification)
			qualification = programme;

		String title = "";
		String saqaId = "";
		if (qualification instanceof I_ZZQualification)
		{
			I_ZZQualification selected = (I_ZZQualification) qualification;
			title = selected.getZZSaqaQualificationTitle();
			saqaId = selected.getZZSaqaQualificationCode();
		}
		else if (qualification instanceof I_ZZQctoQualification)
		{
			I_ZZQctoQualification selected = (I_ZZQctoQualification) qualification;
			title = selected.getZZSaqaQualificationTitle();
			saqaId = selected.getZZSaqaQualificationCode();
		}
		data.put(QUALIFICATION_TITLE, first(title, programmeTitle));
		data.put(SAQA_ID, first(saqaId, ""));
		return qualification;
	}

	private static void party(Map<String, String> data, String prefix, int partnerId)
	{
		if (partnerId <= 0)
			return;
		MBPartner partner = new MBPartner(Env.getCtx(), partnerId, null);
		data.put(prefix + SUFFIX_LEAD, prefix.startsWith("Secondary") ? "No" : "Yes");
		data.put(prefix + LearnerAgreementData.SUFFIX_NAME, partner.getName());
		String tradingName = partner.getName2();
		data.put(prefix + LearnerAgreementData.SUFFIX_TRADING_NAME, tradingName != null && partner.getName() != null
																	&& partner.getName().trim().equalsIgnoreCase(tradingName.trim()) ? "" : tradingName);
		// The agreement asks for the stored SIC reference value (code), not its display name.
		data.put(prefix + LearnerAgreementData.SUFFIX_SIC, value(partner, I_C_BPartner.COLUMNNAME_ZZSicCode));
	}

	private static void partyDetails(	Map<String, String> data, Map<String, Object> registration, String prefix,
										String partnerColumn, String contactColumn, String levyColumn)
	{
		int partnerId = id(registration.get(partnerColumn));
		if (partnerId <= 0)
			return;
		MBPartner partner = new MBPartner(Env.getCtx(), partnerId, null);
		putPartyAddresses(data, prefix, partner,
				organisationAddress(partnerId, "Physical"), organisationAddress(partnerId, "Postal"));
		MUser contact = null;
		int contactId = id(registration.get(contactColumn));
		if (contactId > 0)
		{
			MUser selected = new MUser(Env.getCtx(), contactId, null);
			if (selected.isActive() && selected.getC_BPartner_ID() == partnerId)
				contact = selected;
		}
		if (contact == null)
		{
			MUser[] contacts = partner.getContacts(false);
			if (contacts.length == 1)
				contact = contacts[0];
		}
		if (contact != null)
		{
			data.put(prefix + LearnerAgreementData.SUFFIX_CONTACT, contact.getName());
			// Organisation Contact Details labels Phone as Cell and Phone2 as Telephone.
			data.put(prefix + LearnerAgreementData.SUFFIX_TELEPHONE, prefix.endsWith("Provider")
																								? join(contact.getPhone2(), contact.getPhone())
																								: contact.getPhone2());
			data.put(prefix + LearnerAgreementData.SUFFIX_CELL, contact.getPhone());
			data.put(prefix + LearnerAgreementData.SUFFIX_FAX, contact.getFax());
			data.put(prefix + LearnerAgreementData.SUFFIX_EMAIL, contact.getEMail());
		}
		Object levy = registration.get(levyColumn);
		if (levy instanceof ValueNamePair)
			levy = ((ValueNamePair) levy).getValue();
		if (Boolean.TRUE.equals(levy) || "Y".equals(levy))
			data.put(prefix + LearnerAgreementData.SUFFIX_LEVY, "Yes");
		else if (Boolean.FALSE.equals(levy) || "N".equals(levy))
			data.put(prefix + LearnerAgreementData.SUFFIX_LEVY, "No");
		else if (X_C_BPartner.ZZLEVYNUMBERTYPE_LNumber.equals(value(partner, I_C_BPartner.COLUMNNAME_ZZLevyNumberType)))
			data.put(prefix + LearnerAgreementData.SUFFIX_LEVY, "Yes");
		data.put(prefix + LearnerAgreementData.SUFFIX_SDL, "Yes".equals(data.get(prefix + LearnerAgreementData.SUFFIX_LEVY)) ? partner.getValue() : "");
	}

	static void putPartyAddresses(Map<String, String> data, String prefix, MBPartner partner,
			String physical, String postal)
	{
		MBPartnerLocation[] locations = partner.getLocations(false);
		boolean leadEmployer = "Employer".equals(prefix);
		if (leadEmployer)
		{
			String business = namedPartnerAddress(locations, "Business Address");
			if (business.isBlank())
				business = partnerAddress(partner.getLocation(0));
			physical = first(business, physical);
			postal = first(namedPartnerAddress(locations, "Postal Address"), postal);
		}
		else if (physical.isBlank() && locations.length == 1)
			physical = address(locations[0].getC_Location_ID());

		if (!physical.isBlank())
			data.put(prefix + SUFFIX_ADDRESS, physical);
		boolean sameAddress = leadEmployer ? postal.equalsIgnoreCase(physical) : postal.equals(physical);
		if (!postal.isBlank() && !sameAddress)
			data.put(prefix + SUFFIX_POSTAL_ADDRESS, postal);
	}

	private static String namedPartnerAddress(MBPartnerLocation[] locations, String name)
	{
		return Arrays.stream(locations).filter(location -> name.equalsIgnoreCase(location.getName()))
				.map(LearnerAgreementData::partnerAddress)
				.filter(address -> !address.isBlank()).findFirst().orElse("");
	}

	private static String partnerAddress(MBPartnerLocation partnerLocation)
	{
		MLocation location = partnerLocation == null ? null : partnerLocation.getLocation(false);
		if (location == null)
			return "";
		return Stream.of(location.getAddress1(), location.getAddress2(), location.getAddress3(),
				location.getAddress4(), location.getCity(), location.getPostal(), location.getRegionName(true))
				.filter(line -> line != null && !line.isBlank()).map(String::strip).collect(Collectors.joining(", "));
	}

	private static String organisationAddress(int partnerId, String type)
	{
		// Same address relationship and latest-record selection as MaintainOrganisationVM.
		PO record = BuildFormUtil.getSavedAddress(partnerId, type, false);
		if (record == null || !record.isActive())
			return "";
		return Stream	.of(value(record, I_ZZPersonAddress.COLUMNNAME_ZZComplexSectionFarm),
							value(record, I_ZZPersonAddress.COLUMNNAME_Address1), value(record, I_ZZPersonAddress.COLUMNNAME_Address2),
							value(record, I_ZZPersonAddress.COLUMNNAME_Address3), display(record, I_ZZPersonAddress.COLUMNNAME_C_City_ID),
							value(record, I_ZZPersonAddress.COLUMNNAME_Postal), display(record, I_ZZPersonAddress.COLUMNNAME_C_Region_ID),
							value(record, I_ZZPersonAddress.COLUMNNAME_ZZProvince))
						.filter(s -> s != null && !s.isBlank()).distinct().collect(Collectors.joining(", "));
	}

	private static void providerAccreditation(Map<String, String> data, String prefix, int partnerId, List<String> warnings)
	{
		if (partnerId <= 0)
			return;
		List<X_C_BP_AC> records = new Query(Env.getCtx(), I_C_BP_AC.Table_Name,
											I_C_BP_AC.COLUMNNAME_C_BPartner_ID + "=?", null)
																							.setParameters(partnerId).setOnlyActiveRecords(true).list();
		X_C_BP_AC accreditation = unique(	records,
											"Multiple active accreditation records for the selected SDP: accreditation fields were left blank. Please identify the applicable record.",
											warnings);
		if (accreditation != null)
			putAccreditation(data, prefix, accreditation);
	}

	private static void putAccreditation(Map<String, String> data, String prefix, X_C_BP_AC accreditation)
	{
		data.put(prefix + SUFFIX_ACCREDITATION_NUMBER, accreditation.getZZ_AccreditationNumber());
		data.put(prefix + SUFFIX_ACCREDITATION_STATUS, display(accreditation, I_C_BP_AC.COLUMNNAME_ZZ_Status));
		data.put(prefix + SUFFIX_ACCREDITATION_START_DATE, date(accreditation.getStartDate()));
		data.put(prefix + SUFFIX_ACCREDITATION_END_DATE, date(accreditation.getEndDate()));
	}

	private static String financialYear(int yearId)
	{
		if (yearId <= 0)
			return "";
		List<MPeriod> periods = new Query(	Env.getCtx(), I_C_Period.Table_Name,
											I_C_Period.COLUMNNAME_C_Year_ID + "=? AND " + I_C_Period.COLUMNNAME_PeriodType + "=?", null)
																																		.setParameters(	yearId,
																																						MPeriod.PERIODTYPE_StandardCalendarPeriod)
																																		.setOnlyActiveRecords(true)
																																		.list();
		Date start = null;
		Date end = null;
		for (MPeriod period : periods)
		{
			if (period.getStartDate() != null && (start == null || period.getStartDate().before(start)))
				start = period.getStartDate();
			if (period.getEndDate() != null && (end == null || period.getEndDate().after(end)))
				end = period.getEndDate();
		}
		if (start == null || end == null)
			return "";
		SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
		return format.format(start) + " - " + format.format(end);
	}

	private static String address(int locationId)
	{
		if (locationId <= 0)
			return "";
		// getRegionName(true) updates RegionName internally; never call it on the immutable cache
		// entry.
		MLocation location = MLocation.getCopy(Env.getCtx(), locationId, null);
		if (location == null)
			return "";
		return Stream	.of(location.getAddress1(), location.getAddress2(), location.getCity(),
							location.getPostal(), location.getRegionName(true)).filter(s -> s != null && !s.isBlank())
						.collect(Collectors.joining(", "));
	}

	private static PO linked(PO record, String column)
	{
		if (record.get_ColumnIndex(column) < 0 || record.get_ValueAsInt(column) <= 0)
			return null;
		MColumn metadata = MTable.get(Env.getCtx(), record.get_TableName()).getColumn(column);
		String table = metadata == null ? null : metadata.getReferenceTableName();
		return table == null ? null : MTable.get(Env.getCtx(), table).getPO(record.get_ValueAsInt(column), null);
	}

	private static String display(PO record, String column)
	{
		String raw = value(record, column);
		if (raw.isBlank())
			return "";
		MColumn metadata = MTable.get(Env.getCtx(), record.get_TableName()).getColumn(column);
		if (metadata == null)
			return "";
		if (DisplayType.isList(metadata.getAD_Reference_ID()))
			return MRefList.getListName(Env.getCtx(), metadata.getAD_Reference_Value_ID(), raw);
		if (record.get_Value(column) instanceof Number)
		{
			PO reference = linked(record, column);
			return reference == null ? "" : value(reference, I_C_BPartner.COLUMNNAME_Name);
		}
		return raw;
	}

	private static String value(PO record, String column)
	{
		return column == null || record.get_ColumnIndex(column) < 0 ? "" : record.get_ValueAsString(column);
	}

	private static int id(Object value)
	{
		if (value instanceof PO)
			return ((PO) value).get_ID();
		if (value instanceof Number)
			return ((Number) value).intValue();
		try
		{
			return value == null ? 0 : Integer.parseInt(value.toString());
		}
		catch (NumberFormatException e)
		{
			return 0;
		}
	}

	private static String date(Object value)
	{
		if (value instanceof Date)
			return new SimpleDateFormat("yyyy/MM/dd").format((Date) value);
		return value instanceof LocalDate ? value.toString().replace('-', '/') : "";
	}

	private static String first(String primary, String fallback)
	{
		return primary == null || primary.isBlank() ? fallback : primary;
	}

	private static String join(String... values)
	{
		return Stream.of(values).filter(s -> s != null && !s.isBlank()).collect(Collectors.joining(" "));
	}

	static <T> T unique(List<T> records, String warning, List<String> warnings)
	{
		if (records.size() == 1)
			return records.get(0);
		if (records.size() > 1)
			warnings.add(warning);
		return null;
	}

	static String citizenAnswer(String statusName)
	{
		if (statusName == null || statusName.isBlank())
			return "";
		return "South Africa".equalsIgnoreCase(statusName.trim()) ? "Yes" : "No";
	}
}
