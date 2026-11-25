package za.co.ntier.webform.sdr.viewmodel;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.compiere.model.I_C_Location;
import org.compiere.model.I_C_Period;
import org.compiere.model.MBPartner;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MColumn;
import org.compiere.model.MLocation;
import org.compiere.model.MOrg;
import org.compiere.model.MPeriod;
import org.compiere.model.MRefList;
import org.compiere.model.MTable;
import org.compiere.model.MUser;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.ValueNamePair;

import za.co.ntier.api.model.I_C_BP_AC;
import za.co.ntier.api.model.I_C_BPartner;
import za.co.ntier.api.model.I_ZZLearnerDGProgramme;
import za.co.ntier.api.model.I_ZZLearnerLearnership;
import za.co.ntier.api.model.I_ZZLearnership;
import za.co.ntier.api.model.I_ZZPerson;
import za.co.ntier.api.model.I_ZZPersonAddress;
import za.co.ntier.api.model.I_ZZQctoLearnership;
import za.co.ntier.api.model.I_ZZQctoQualification;
import za.co.ntier.api.model.I_ZZQctoSkillsProgramme;
import za.co.ntier.api.model.I_ZZQualification;
import za.co.ntier.api.model.I_ZZSkillsProgramme;
import za.co.ntier.api.model.I_ZZ_Institution_Qualifications;
import za.co.ntier.api.model.X_ZZ_Institution_Qualifications;
import za.co.ntier.api.model.I_ZZ_ParentDetails;
import za.co.ntier.api.model.I_ZZ_PostSchoolEducation_Details;
import za.co.ntier.api.model.I_ZZ_Program_Master_Data;
import za.co.ntier.api.model.X_ZZ_Program_Master_Data;
import za.co.ntier.api.model.X_C_BP_AC;
import za.co.ntier.api.model.X_C_BPartner;
import za.co.ntier.api.model.X_ZZLearner_v;
import za.co.ntier.api.model.X_ZZPerson;
import za.co.ntier.api.model.X_ZZ_Institution;
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
	public static final String	EMPLOYED_BEFORE_AGREEMENT			= "EmployedBeforeAgreement";
	public static final String	PAST_WORKPLACE_AGREEMENT			= "PastWorkplaceAgreement";
	public static final String	CONTRACT_SPECIFIC_ANSWER			= "ContractSpecificAnswer";
	public static final String	CONTRACT_COPY_ANSWER				= "ContractCopyAnswer";
	public static final String	HAS_CONTRACT_SPECIFIC				= "HasContractSpecific";
	public static final String	HAS_CONTRACT_COPY					= "HasContractCopy";
	public static final String	HAS_CONTRACT_SECTION				= "HasContractSection";
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
	/** Employer business-partner Value, used as the SDL value on the Candidacy Annexure summary. */
	public static final String	EMPLOYER_VALUE						= "EmployerValue";
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
	public static final String	LEARNER_FIRST_NAME					= "LearnerFirstName";
	public static final String	LEARNER_SURNAME						= "LearnerSurname";
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
	public static final String	IS_EMPLOYER_BASED					= "IsEmployerBased";
	public static final String	SECTION_CONTRACT_OF_EMPLOYMENT		= "SectionContractOfEmployment";
	public static final String	POINT_CONTRACT_SPECIFIC				= "PointContractSpecific";
	public static final String	POINT_CONTRACT_COPY					= "PointContractCopy";
	public static final String	SECTION_PROGRAMME_SELECTION			= "SectionProgrammeSelection";
	public static final String	SECTION_SIGNATORIES					= "SectionSignatories";
	public static final String	EMPLOYER_FINANCIAL_OBLIGATION		= "EmployerFinancialObligation";
	public static final String	IS_LP_STRATEGIC						= "IsLPStrategic";
	public static final String	LP_PROJECTS_ORG_UU					= "ca4dbcb0-53d1-4afb-80e5-dc773251568c";
	/** Shows the Annexure A summary only for the requested Candidacy programme definition. */
	public static final String	CANDIDACY_ANNEXURE_SUMMARY			= "CandidacyAnnexureSummary";
	private static final String	CANDIDACY_PROGRAM_MASTER_DATA_UU	= "ca8b4d40-76c3-4d21-b507-5d1724e9cc83";

	static Map<String, String> build(	X_ZZLearner_v learner, Map<String, Object> registration, String programmeColumn,
										String programmeTable, String codeColumn, String programmeType, List<String> warnings)
	{
		Map<String, String> data = new LinkedHashMap<>();
		data.put(LearnerAgreementData.LEARNER_TITLE, display(learner, I_ZZPerson.COLUMNNAME_ZZLkpTitle));
		data.put(LearnerAgreementData.LEARNER_NAME, join(learner.getZZFirstName(), learner.getZZMiddleName(), learner.getSurname()));
		data.put(LearnerAgreementData.LEARNER_FIRST_NAME, text(learner.getZZFirstName()));
		data.put(LearnerAgreementData.LEARNER_SURNAME, text(learner.getSurname()));
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

		// Registration map is a case-insensitive TreeMap, which rejects null keys (no config for DG-only programmes).
		int programmeId = programmeColumn == null ? 0 : id(registration.get(programmeColumn));
		PO programme = programmeId > 0 && programmeTable != null
				? MTable.get(Env.getCtx(), programmeTable).getPO(programmeId, null) : null;
		registration(data, registration, programme, codeColumn, programmeType, warnings);
		int progMasterId = id(registration.get(I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_Program_Master_Data_ID));
		if (progMasterId > 0 && !data.containsKey(HAS_CONTRACT_SPECIFIC) && !data.containsKey(HAS_CONTRACT_COPY))
		{
			X_ZZ_Program_Master_Data pmd = new X_ZZ_Program_Master_Data(Env.getCtx(), progMasterId, null);
			putEmployerFinancialObligation(data, pmd);
		}
		checkEmployerBasedAqp(data);
		putDynamicSectionSequences(data);
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
		data.put(LEARNER_FIRST_NAME, text(learner.get(I_ZZPerson.COLUMNNAME_ZZFirstName)));
		data.put(LEARNER_SURNAME, text(learner.get(I_ZZPerson.COLUMNNAME_Surname)));
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
		checkEmployerBasedAqp(data);
		putDynamicSectionSequences(data);
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

	static String toYesNo(Object value)
	{
		if (value == null)
			return "";
		if (value instanceof Boolean)
			return ((Boolean) value).booleanValue() ? "Yes" : "No";
		String s = text(value).trim();
		if ("Y".equalsIgnoreCase(s) || "Yes".equalsIgnoreCase(s) || "true".equalsIgnoreCase(s))
			return "Yes";
		if ("N".equalsIgnoreCase(s) || "No".equalsIgnoreCase(s) || "false".equalsIgnoreCase(s))
			return "No";
		return "";
	}

	private static void putAgreementAnswers(Map<String, String> data, Map<String, Object> registration)
	{
		if (registration == null)
			return;
		String empBefore = toYesNo(registration.get(I_ZZLearnerDGProgramme.COLUMNNAME_ZZEmployedBeforeAgreement));
		if (empBefore.isBlank())
			empBefore = toYesNo(registration.get("ZZLearnerEmployed"));
		if (!empBefore.isBlank())
			data.put(EMPLOYED_BEFORE_AGREEMENT, empBefore);

		String pastWp = toYesNo(registration.get(I_ZZLearnerDGProgramme.COLUMNNAME_ZZPastWorkplaceAgreement));
		if (pastWp.isBlank())
			pastWp = toYesNo(registration.get("ZZPreviousEmployed"));
		if (!pastWp.isBlank())
			data.put(PAST_WORKPLACE_AGREEMENT, pastWp);

		String contractSpecific = toYesNo(registration.get(I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_IsContractSpecificToPeriod));
		if (!contractSpecific.isBlank())
		{
			data.put(CONTRACT_SPECIFIC_ANSWER, contractSpecific);
			if (!data.containsKey(HAS_CONTRACT_SPECIFIC))
			{
				data.put(HAS_CONTRACT_SPECIFIC, "Y");
			}
		}

		String contractCopy = toYesNo(registration.get(I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_HasContractCopy));
		if (!contractCopy.isBlank())
		{
			data.put(CONTRACT_COPY_ANSWER, contractCopy);
			if (!data.containsKey(HAS_CONTRACT_COPY))
			{
				data.put(HAS_CONTRACT_COPY, "Y");
			}
		}
	}

	private static void registration(Map<String, String> data, Map<String, Object> registration,
			PO programme, String codeColumn, String programmeType, List<String> warnings)
	{
		if (programme == null)
		{
			Object qualObj = registration.get(I_ZZ_Institution_Qualifications.COLUMNNAME_ZZ_Institution_Qualifications_ID);
			if (qualObj instanceof PO)
			{
				programme = (PO) qualObj;
				if (codeColumn == null || codeColumn.isBlank())
					codeColumn = I_ZZ_Institution_Qualifications.COLUMNNAME_Saqa_Code;
			}
			else
			{
				int qualId = id(qualObj);
				if (qualId > 0)
				{
					programme = new X_ZZ_Institution_Qualifications(Env.getCtx(), qualId, null);
					if (codeColumn == null || codeColumn.isBlank())
						codeColumn = I_ZZ_Institution_Qualifications.COLUMNNAME_Saqa_Code;
				}
			}
		}
		int instId = id(registration.get(I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_Institution_ID));
		if (instId > 0) {
			X_ZZ_Institution inst = new X_ZZ_Institution(Env.getCtx(), instId, null);
			data.put("Employer" + SUFFIX_LEAD, "Yes");
			data.put("Employer" + LearnerAgreementData.SUFFIX_NAME, inst.getName());
			String contactPerson = first(text(registration.get(I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_EmployerContactPerson)),
					text(registration.get(I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_AltContactPerson)));
			if (!contactPerson.isBlank()) {
				data.put("Employer" + LearnerAgreementData.SUFFIX_CONTACT, contactPerson);
			}
			String contactEmail = text(registration.get(I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_EmployerContactEmail));
			if (!contactEmail.isBlank()) {
				data.put("Employer" + LearnerAgreementData.SUFFIX_EMAIL, contactEmail);
			}
			String contactPhone = text(registration.get(I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_EmployerContactNo));
			String altPhone = text(registration.get(I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_AltContactNumber));
			putContactNumbers(data, "Employer", contactPhone, altPhone, "", "");
		} else {
			party(data, "Employer", id(registration.get(I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_Employer_ID)));
			partyDetails(	data, registration, "Employer", I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_Employer_ID,
							I_ZZLearnerDGProgramme.COLUMNNAME_ZZWAContact_ID, I_ZZLearnerDGProgramme.COLUMNNAME_ZZWALevy);
		}
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
		String fundingYear = text(registration.get(I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_Funding_Year));
		if (!fundingYear.isBlank()) {
			data.put("FundingYear", fundingYear);
		}
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
		putAgreementAnswers(data, registration);
	}

	static void putProgrammeDefinition(Map<String, String> data, boolean enterQualification,
			String selectedProgrammeType, String dgProgrammeTitle)
	{
		data.put(PROGRAMME_TITLE, enterQualification ? first(selectedProgrammeType, dgProgrammeTitle) : dgProgrammeTitle);
		data.put(QUALIFICATION_TITLE, first(data.get(QUALIFICATION_TITLE), dgProgrammeTitle));
		data.put(SAQA_ID, first(data.get(SAQA_ID), first(data.get(PROGRAMME_CODE), "")));
		checkEmployerBasedAqp(data);
	}

	/** Clause 2.4.8: employer financial obligation, printed only for employer-based programmes. */
	static void putEmployerFinancialObligation(Map<String, String> data, I_ZZ_Program_Master_Data programMasterData)
	{
		boolean isEmployerBased = programMasterData != null && programMasterData.isZZ_Is_Employer_Based();
		data.put(IS_EMPLOYER_BASED, isEmployerBased ? "Y" : "N");
		data.put(EMPLOYER_FINANCIAL_OBLIGATION, isEmployerBased ? plainTextToHtml(programMasterData.getZZ_Comp_Fin_Oblig()) : "");
		boolean isCandidacy = programMasterData != null
				&& CANDIDACY_PROGRAM_MASTER_DATA_UU.equalsIgnoreCase(programMasterData.getZZ_Program_Master_Data_UU());
		data.put(CANDIDACY_ANNEXURE_SUMMARY, isCandidacy ? "Y" : "N");
		data.put(IS_LP_STRATEGIC, isLPStrategicProgram(programMasterData) ? "Y" : "N");
		if (programMasterData != null)
		{
			try
			{
				data.put(HAS_CONTRACT_SPECIFIC, programMasterData.isZZ_IsContractSpecificToPeriod() ? "Y" : "N");
			}
			catch (Exception ignored)
			{
			}
			try
			{
				data.put(HAS_CONTRACT_COPY, programMasterData.isZZ_HasContractCopy() ? "Y" : "N");
			}
			catch (Exception ignored)
			{
			}
		}
		checkEmployerBasedAqp(data);
		putDynamicSectionSequences(data);
	}

	public static boolean isLPStrategicProgram(I_ZZ_Program_Master_Data prog)
	{
		if (prog == null)
			return false;
		int orgId = prog.getAD_Org_ID();
		if (orgId <= 0)
			return false;
		try
		{
			MOrg org = MOrg.get(Env.getCtx(), orgId);
			if (org != null)
				return LP_PROJECTS_ORG_UU.equalsIgnoreCase(org.getAD_Org_UU());
		}
		catch (Exception ignored)
		{
		}
		return false;
	}

	static boolean isCandidacy(Map<String, String> data)
	{
		if ("Y".equals(data.get(CANDIDACY_ANNEXURE_SUMMARY)))
			return true;
		String progType = data.get(PROGRAMME_TYPE);
		if (progType != null && progType.trim().equalsIgnoreCase("Candidacy"))
			return true;
		String progTitle = data.get(PROGRAMME_TITLE);
		if (progTitle != null && progTitle.trim().equalsIgnoreCase("Candidacy"))
			return true;
		String qualTitle = data.get(QUALIFICATION_TITLE);
		if (qualTitle != null && qualTitle.trim().equalsIgnoreCase("Candidacy"))
			return true;
		return false;
	}

	static void checkEmployerBasedAqp(Map<String, String> data)
	{
		boolean isEmployerBased = "Y".equals(data.get(IS_EMPLOYER_BASED)) || isCandidacy(data);
		if (isEmployerBased)
		{
			String aqp = data.get(ASSESSMENT_QUALITY_PARTNER);
			if (aqp == null || aqp.isBlank())
				data.put(ASSESSMENT_QUALITY_PARTNER, "MQA");
		}
	}

	/** Formats download file name: Learner_Agreement_Name_Surname.pdf */
	public static String getAgreementFileName(Map<String, String> data)
	{
		String firstName = data != null ? data.get(LEARNER_FIRST_NAME) : null;
		String surname = data != null ? data.get(LEARNER_SURNAME) : null;
		if ((firstName == null || firstName.isBlank()) && (surname == null || surname.isBlank()))
		{
			String fullName = data != null ? data.get(LEARNER_NAME) : null;
			if (fullName != null && !fullName.isBlank())
			{
				String sanitized = fullName.trim().replaceAll("\\s+", "_").replaceAll("[^a-zA-Z0-9_]+", "");
				if (!sanitized.isBlank())
					return "Learner_Agreement_" + sanitized + ".pdf";
			}
			return "Learner_Agreement.pdf";
		}
		StringBuilder namePart = new StringBuilder();
		if (firstName != null && !firstName.isBlank())
			namePart.append(firstName.trim().replaceAll("\\s+", "_").replaceAll("[^a-zA-Z0-9_]+", ""));
		if (surname != null && !surname.isBlank())
		{
			if (namePart.length() > 0)
				namePart.append("_");
			namePart.append(surname.trim().replaceAll("\\s+", "_").replaceAll("[^a-zA-Z0-9_]+", ""));
		}
		return namePart.length() > 0 ? "Learner_Agreement_" + namePart + ".pdf" : "Learner_Agreement.pdf";
	}

	static void putDynamicSectionSequences(Map<String, String> data)
	{
		boolean isEmployerBased = "Y".equals(data.get(IS_EMPLOYER_BASED));
		boolean hasSpecific = "Y".equals(data.get(HAS_CONTRACT_SPECIFIC));
		boolean hasCopy = "Y".equals(data.get(HAS_CONTRACT_COPY));
		boolean hasSection = hasSpecific || hasCopy;
		data.put(HAS_CONTRACT_SECTION, hasSection ? "Y" : "N");

		int sec = isEmployerBased ? 4 : 7;
		if (hasSection)
		{
			data.put(SECTION_CONTRACT_OF_EMPLOYMENT, sec + ". Contract of Employment");
			int pt = 1;
			if (hasSpecific)
			{
				data.put(POINT_CONTRACT_SPECIFIC, sec + "." + pt + " Is the learner's contract of employment specific to the period of the agreement?");
				pt++;
			}
			else
			{
				data.put(POINT_CONTRACT_SPECIFIC, "");
			}

			if (hasCopy)
			{
				data.put(POINT_CONTRACT_COPY, sec + "." + pt + " Does the learner have a copy of the contract of employment?");
				pt++;
			}
			else
			{
				data.put(POINT_CONTRACT_COPY, "");
			}
			sec++;
		}
		else
		{
			data.put(SECTION_CONTRACT_OF_EMPLOYMENT, "");
			data.put(POINT_CONTRACT_SPECIFIC, "");
			data.put(POINT_CONTRACT_COPY, "");
		}

		data.put(SECTION_PROGRAMME_SELECTION, sec + ". Workplace Based Learning Programme Selection:");
		sec++;
		data.put(SECTION_SIGNATORIES, sec + ". Signatories");
	}

	/**
	 * The report field uses HTML markup; the column holds plain text. Escape it, keep line breaks
	 * (including blank lines) and keep leading spaces/tabs so users can indent sub-items.
	 */
	static String plainTextToHtml(String value)
	{
		if (value == null || value.isBlank())
			return "";
		String[] lines = value.strip().replace("\r\n", "\n").replace('\r', '\n').split("\n", -1);
		StringBuilder html = new StringBuilder();
		for (int i = 0; i < lines.length; i++)
		{
			if (i > 0)
				html.append("<br/>");
			String line = lines[i].stripTrailing();
			int indent = 0;
			while (indent < line.length() && (line.charAt(indent) == ' ' || line.charAt(indent) == '\t'))
				html.append(line.charAt(indent++) == '\t' ? "&nbsp;&nbsp;&nbsp;&nbsp;" : "&nbsp;");
			html.append(line.substring(indent).replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;"));
		}
		return html.toString();
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
		else if (programme instanceof I_ZZQualification || programme instanceof I_ZZQctoQualification
				|| programme instanceof I_ZZ_Institution_Qualifications)
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
		else if (qualification instanceof I_ZZ_Institution_Qualifications)
		{
			I_ZZ_Institution_Qualifications selected = (I_ZZ_Institution_Qualifications) qualification;
			title = selected.getName();
			saqaId = selected.getSaqa_Code();
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
		MBPartner partner = partnerId > 0 ? new MBPartner(Env.getCtx(), partnerId, null) : null;
		if (partner != null)
		{
			if ("Employer".equals(prefix))
				data.put(EMPLOYER_VALUE, partner.getValue());
			putPartyAddresses(data, prefix, partner,
					organisationAddress(partnerId, "Physical"), organisationAddress(partnerId, "Postal"));
		}
		String regContactPerson = "Employer".equals(prefix)
				? first(text(registration.get(I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_EmployerContactPerson)),
						text(registration.get(I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_AltContactPerson)))
				: "";
		String regContactNo = "Employer".equals(prefix)
				? text(registration.get(I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_EmployerContactNo)) : "";
		String regAltContactNo = "Employer".equals(prefix)
				? text(registration.get(I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_AltContactNumber)) : "";
		String regContactEmail = "Employer".equals(prefix)
				? text(registration.get(I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_EmployerContactEmail)) : "";

		MUser contact = null;
		int contactId = id(registration.get(contactColumn));
		if (contactId > 0)
		{
			MUser selected = new MUser(Env.getCtx(), contactId, null);
			if (selected.isActive() && selected.getC_BPartner_ID() == partnerId)
				contact = selected;
		}
		MUser[] contacts = partner != null ? partner.getContacts(false) : null;
		if (contact == null && !regContactPerson.isBlank() && contacts != null)
		{
			for (MUser u : contacts)
			{
				if (u.isActive() && regContactPerson.equalsIgnoreCase(u.getName()))
				{
					contact = u;
					break;
				}
			}
		}
		if (contact == null && contacts != null)
		{
			if (contacts.length == 1 && contacts[0].isActive())
				contact = contacts[0];
			else if (contacts.length > 1)
			{
				for (MUser u : contacts)
				{
					if (u.isActive())
					{
						contact = u;
						break;
					}
				}
			}
		}

		String contactName = first(regContactPerson, contact != null ? contact.getName() : "");
		if (!contactName.isBlank())
			data.put(prefix + LearnerAgreementData.SUFFIX_CONTACT, contactName);

		String contactEmail = first(regContactEmail, contact != null ? contact.getEMail() : "");
		if (!contactEmail.isBlank())
			data.put(prefix + LearnerAgreementData.SUFFIX_EMAIL, contactEmail);

		if (contact != null && contact.getFax() != null && !contact.getFax().isBlank())
			data.put(prefix + LearnerAgreementData.SUFFIX_FAX, contact.getFax());

		String cPhone = contact != null && contact.getPhone() != null ? contact.getPhone() : "";
		String cPhone2 = contact != null && contact.getPhone2() != null ? contact.getPhone2() : "";
		putContactNumbers(data, prefix, regContactNo, regAltContactNo, cPhone, cPhone2);
		Object levy = registration.get(levyColumn);
		if (levy instanceof ValueNamePair)
			levy = ((ValueNamePair) levy).getValue();
		if (Boolean.TRUE.equals(levy) || "Y".equals(levy))
			data.put(prefix + LearnerAgreementData.SUFFIX_LEVY, "Yes");
		else if (Boolean.FALSE.equals(levy) || "N".equals(levy))
			data.put(prefix + LearnerAgreementData.SUFFIX_LEVY, "No");
		else if (partner != null && X_C_BPartner.ZZLEVYNUMBERTYPE_LNumber.equals(value(partner, I_C_BPartner.COLUMNNAME_ZZLevyNumberType)))
			data.put(prefix + LearnerAgreementData.SUFFIX_LEVY, "Yes");
		data.put(prefix + LearnerAgreementData.SUFFIX_SDL,
				"Yes".equals(data.get(prefix + LearnerAgreementData.SUFFIX_LEVY)) && partner != null ? partner.getValue() : "");
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
		return record == null || column == null || record.get_ColumnIndex(column) < 0 ? "" : record.get_ValueAsString(column);
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

	static void putContactNumbers(Map<String, String> data, String prefix,
			String regNo, String regAltNo, String contactPhone, String contactPhone2)
	{
		regNo = regNo == null ? "" : regNo.strip();
		regAltNo = regAltNo == null ? "" : regAltNo.strip();
		contactPhone = contactPhone == null ? "" : contactPhone.strip();
		contactPhone2 = contactPhone2 == null ? "" : contactPhone2.strip();

		String tel = "";
		String cell = "";
		if (!regNo.isBlank())
		{
			if (!regAltNo.isBlank())
			{
				if (isMobile(regNo) && !isMobile(regAltNo))
				{
					cell = regNo;
					tel = regAltNo;
				}
				else if (!isMobile(regNo) && isMobile(regAltNo))
				{
					tel = regNo;
					cell = regAltNo;
				}
				else
				{
					cell = regNo;
					tel = regAltNo;
				}
			}
			else
			{
				if (isMobile(regNo))
				{
					cell = regNo;
					tel = first(contactPhone2, regNo);
				}
				else
				{
					tel = regNo;
					cell = first(contactPhone, regNo);
				}
			}
		}
		else
		{
			tel = prefix != null && prefix.endsWith("Provider") ? join(contactPhone2, contactPhone) : contactPhone2;
			cell = contactPhone;
			if (tel.isBlank() && !cell.isBlank())
				tel = cell;
			else if (cell.isBlank() && !tel.isBlank())
				cell = tel;
		}

		if (!tel.isBlank())
			data.put(prefix + LearnerAgreementData.SUFFIX_TELEPHONE, tel);
		if (!cell.isBlank())
			data.put(prefix + LearnerAgreementData.SUFFIX_CELL, cell);
	}

	static boolean isMobile(String phone)
	{
		if (phone == null || phone.isBlank())
			return false;
		String digits = phone.replaceAll("[^0-9]", "");
		if (digits.startsWith("27"))
			digits = "0" + digits.substring(2);
		return digits.startsWith("06") || digits.startsWith("07") || digits.startsWith("08");
	}

	private static String first(String... values)
	{
		for (String value : values)
		{
			if (value != null && !value.isBlank())
				return value;
		}
		return "";
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
		String status = statusName.trim();
		// value renamed from "South Africa" to "South African"; accept both
		return "South African".equalsIgnoreCase(status) || "South Africa".equalsIgnoreCase(status) ? "Yes" : "No";
	}
}
