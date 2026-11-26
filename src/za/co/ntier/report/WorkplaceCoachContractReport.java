package za.co.ntier.report;

import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.compiere.model.I_C_BPartner;
import org.compiere.model.I_C_BPartner_Location;
import org.compiere.model.MBPartner;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MLocation;
import org.compiere.model.MUser;
import org.compiere.model.MYear;
import org.compiere.model.Query;
import org.compiere.util.Util;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import za.co.ntier.api.model.I_ZZLearner_v;
import za.co.ntier.api.model.X_ZZLearnerDGProgramme;
import za.co.ntier.api.model.X_ZZLearner_v;
import za.co.ntier.api.model.X_ZZ_Program_Master_Data;

/**
 * Workplace Coaches Programme Contract report generator.
 */
public final class WorkplaceCoachContractReport
{
	private static final String REPORT_PATH = "/za/co/ntier/report/jrxmls/WorkplaceCoachContract.jrxml";
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("uuuu-MM-dd", Locale.ENGLISH);

	private WorkplaceCoachContractReport()
	{
	}

	/**
	 * Compile or retrieve the JasperReport instance from the report template.
	 */
	public static synchronized JasperReport getReport() throws JRException
	{
		try (InputStream source = WorkplaceCoachContractReport.class.getResourceAsStream(REPORT_PATH))
		{
			if (source == null)
			{
				throw new IllegalStateException("Workplace Coach Contract report template not found: " + REPORT_PATH);
			}
			return JasperCompileManager.compileReport(source);
		}
		catch (IOException ex)
		{
			throw new JRException("Could not read the Workplace Coach Contract report template.", ex);
		}
	}

	/**
	 * Sanitize an address field by converting newline-delimited lines into a clean comma-separated line.
	 */
	public static String sanitizeAddress(Object obj)
	{
		if (obj == null)
		{
			return "";
		}
		String addr = obj.toString().trim();
		if (addr.contains("\n") || addr.contains("\r"))
		{
			return Stream.of(addr.split("[\r\n]+"))
					.map(String::trim)
					.filter(s -> !s.isEmpty())
					.collect(Collectors.joining(", "));
		}
		return addr;
	}

	/**
	 * Render the Workplace Coach Contract to PDF bytes from a map of data values.
	 */
	public static byte[] render(Map<String, Object> data) throws JRException
	{
		Map<String, Object> parameters = new LinkedHashMap<>();
		if (data != null)
		{
			parameters.putAll(data);
			// Clean address fields to ensure single-line comma-separated addresses
			if (parameters.containsKey("CoachPhysicalAddress"))
			{
				parameters.put("CoachPhysicalAddress", sanitizeAddress(parameters.get("CoachPhysicalAddress")));
			}
			if (parameters.containsKey("LearnerPhysicalAddress"))
			{
				parameters.put("LearnerPhysicalAddress", sanitizeAddress(parameters.get("LearnerPhysicalAddress")));
			}
			if (parameters.containsKey("EmployerPhysicalAddress"))
			{
				parameters.put("EmployerPhysicalAddress", sanitizeAddress(parameters.get("EmployerPhysicalAddress")));
			}
			if (parameters.containsKey("CompanyPhysicalAddress"))
			{
				parameters.put("CompanyPhysicalAddress", sanitizeAddress(parameters.get("CompanyPhysicalAddress")));
			}
			parameters.put("ContractData", new LinkedHashMap<>(parameters));
		}

		ClassLoader reportClassLoader = new ClassLoader(WorkplaceCoachContractReport.class.getClassLoader())
		{
			@Override
			public InputStream getResourceAsStream(String name)
			{
				InputStream is = super.getResourceAsStream(name);
				if (is == null && name != null && name.startsWith("/"))
				{
					is = super.getResourceAsStream(name.substring(1));
				}
				return is;
			}
		};
		parameters.put(JRParameter.REPORT_CLASS_LOADER, reportClassLoader);
		if (!parameters.containsKey("HeaderImage"))
		{
			parameters.put("HeaderImage", WorkplaceCoachContractReport.class.getResource("/za/co/ntier/report/jrxmls/MQA_Address_Logo_Header.png"));
		}
		if (!parameters.containsKey("FooterImage"))
		{
			parameters.put("FooterImage", WorkplaceCoachContractReport.class.getResource("/za/co/ntier/report/jrxmls/Footer_Mining_Future_Skills.png"));
		}

		JasperPrint print = JasperFillManager.fillReport(getReport(), parameters, new JREmptyDataSource(1));
		return JasperExportManager.exportReportToPdf(print);
	}

	/**
	 * Build report data from an intervention registration record.
	 */
	public static Map<String, Object> buildData(X_ZZLearnerDGProgramme registration, String trxName)
	{
		Map<String, Object> data = new LinkedHashMap<>();
		if (registration == null)
		{
			return data;
		}

		Properties ctx = registration.getCtx();

		// Coach / Learner details
		X_ZZLearner_v learner = new Query(ctx, I_ZZLearner_v.Table_Name,
				I_ZZLearner_v.COLUMNNAME_ZZLearner_ID + " = ?", trxName)
				.setParameters(registration.getZZLearner_ID())
				.setClient_ID()
				.setOnlyActiveRecords(true)
				.first();

		if (learner != null)
		{
			String fullName = String.join(" ",
					safeText(learner.getZZFirstName()),
					safeText(learner.getZZMiddleName()),
					safeText(learner.getSurname())).replaceAll(" +", " ").trim();

			data.put("CoachName", fullName);
			data.put("EmployeeName", fullName);
			data.put("LearnerName", fullName);
			data.put("CoachAttention", fullName);
			data.put("LearnerAttention", fullName);

			String idNumber = firstNonEmpty(learner.getZZ_ID_Passport_No(), learner.getZZOtherIDNo());
			data.put("IDNumber", idNumber);
			data.put("LearnerId", idNumber);

			String coachAddress = formatAddress(ctx, learner.getZZPhysicalLocation_ID(), trxName);
			data.put("CoachPhysicalAddress", coachAddress);
			data.put("LearnerPhysicalAddress", coachAddress);
			data.put("CoachEmail", safeText(learner.getEMail()));
			data.put("LearnerEmail", safeText(learner.getEMail()));
			data.put("CoachContact", firstNonEmpty(learner.getPhone(), learner.getPhone2()));
			data.put("LearnerContact", firstNonEmpty(learner.getPhone(), learner.getPhone2()));
		}

		// Employer details
		int employerId = registration.getZZ_Employer_ID();
		if (employerId > 0)
		{
			MBPartner employer = new Query(ctx, I_C_BPartner.Table_Name,
					I_C_BPartner.COLUMNNAME_C_BPartner_ID + " = ?", trxName)
					.setParameters(employerId)
					.setClient_ID()
					.first();

			if (employer != null)
			{
				data.put("CompanyName", safeText(employer.getName()));
				data.put("EmployerName", safeText(employer.getName()));

				String sdl = firstNonEmpty(employer.getTaxID(), employer.getValue());
				data.put("SDLNumber", sdl);
				data.put("EmployerSdl", sdl);

				String employerAddress = formatPartnerAddress(employer, trxName);
				data.put("EmployerPhysicalAddress", employerAddress);

				String contactPerson = firstNonEmpty(registration.getZZ_EmployerContactPerson(), employer.getName());
				String contactEmail = registration.getZZ_EmployerContactEmail();
				String contactPhone = registration.getZZ_EmployerContactNo();

				if (Util.isEmpty(contactEmail) || Util.isEmpty(contactPhone))
				{
					MUser[] contacts = employer.getContacts(false);
					if (contacts != null && contacts.length > 0)
					{
						MUser bestContact = contacts[0];
						for (MUser u : contacts)
						{
							if (u.isActive() && !Util.isEmpty(u.getEMail()))
							{
								bestContact = u;
								break;
							}
						}
						if (Util.isEmpty(contactPerson))
						{
							contactPerson = bestContact.getName();
						}
						if (Util.isEmpty(contactEmail))
						{
							contactEmail = bestContact.getEMail();
						}
						if (Util.isEmpty(contactPhone))
						{
							contactPhone = firstNonEmpty(bestContact.getPhone(), bestContact.getPhone2());
						}
					}
				}

				data.put("EmployerAttention", safeText(contactPerson));
				data.put("EmployerEmail", safeText(contactEmail));
				data.put("EmployerContact", safeText(contactPhone));
			}
		}

		// Programme Dates
		if (registration.getZZCommencementDate() != null)
		{
			String startDate = registration.getZZCommencementDate().toLocalDateTime().toLocalDate().format(DATE_FORMAT);
			data.put("CommencementDate", startDate);
			data.put("AgreementStartDate", startDate);
		}

		if (registration.getZZCompletionDate() != null)
		{
			String endDate = registration.getZZCompletionDate().toLocalDateTime().toLocalDate().format(DATE_FORMAT);
			data.put("CompletionDate", endDate);
			data.put("ExpectedEndDate", endDate);
		}

		// Financial year (from registration Financial Year, or program master data)
		String finYear = "";
		if (registration.getZZFinancialYear_ID() > 0)
		{
			MYear year = new MYear(ctx, registration.getZZFinancialYear_ID(), trxName);
			finYear = firstNonEmpty(year.getDescription(), year.getFiscalYear());
		}
		if (Util.isEmpty(finYear) && registration.getZZ_Program_Master_Data_ID() > 0)
		{
			X_ZZ_Program_Master_Data programMasterData = new X_ZZ_Program_Master_Data(
					ctx, registration.getZZ_Program_Master_Data_ID(), trxName);
			if (programMasterData.getC_Year_ID() > 0)
			{
				MYear year = new MYear(ctx, programMasterData.getC_Year_ID(), trxName);
				finYear = firstNonEmpty(year.getDescription(), year.getFiscalYear());
			}
		}
		if (!Util.isEmpty(finYear))
		{
			data.put("FinancialYear", finYear);
		}

		return data;
	}

	/**
	 * Format physical location address lines into a comma-delimited string.
	 */
	private static String formatAddress(Properties ctx, int locationId, String trxName)
	{
		if (locationId <= 0)
		{
			return "";
		}
		MLocation loc = MLocation.getCopy(ctx, locationId, trxName);
		if (loc == null)
		{
			return "";
		}
		return Stream.of(loc.getAddress1(), loc.getAddress2(), loc.getAddress3(), loc.getAddress4(),
				loc.getCity(), loc.getPostal(), loc.getRegionName(true))
				.filter(s -> s != null && !s.isBlank())
				.collect(Collectors.joining(", "));
	}

	/**
	 * Retrieve and format the primary physical address of a business partner.
	 */
	private static String formatPartnerAddress(MBPartner partner, String trxName)
	{
		MBPartnerLocation bpl = new Query(partner.getCtx(), I_C_BPartner_Location.Table_Name,
				I_C_BPartner_Location.COLUMNNAME_C_BPartner_ID + " = ?", trxName)
				.setParameters(partner.get_ID())
				.setClient_ID()
				.setOnlyActiveRecords(true)
				.first();

		if (bpl != null && bpl.getC_Location_ID() > 0)
		{
			return formatAddress(partner.getCtx(), bpl.getC_Location_ID(), trxName);
		}
		return "";
	}

	/**
	 * Helper to format default file name for the generated contract PDF.
	 */
	public static String getContractFileName(Map<String, Object> data)
	{
		String coachName = data != null ? safeText(firstNonEmpty(
				safeText(data.get("CoachName")),
				safeText(data.get("EmployeeName")),
				safeText(data.get("LearnerName")))) : "";
		if (!coachName.isBlank())
		{
			String sanitized = coachName.trim().replaceAll("\\s+", "_").replaceAll("[^a-zA-Z0-9_]+", "");
			if (!sanitized.isBlank())
			{
				return "Workplace_Coach_Contract_" + sanitized + ".pdf";
			}
		}
		return "Workplace_Coach_Contract.pdf";
	}

	private static String safeText(Object obj)
	{
		return obj == null ? "" : obj.toString().trim();
	}

	private static String firstNonEmpty(String... values)
	{
		for (String v : values)
		{
			if (!Util.isEmpty(v))
			{
				return v.trim();
			}
		}
		return "";
	}
}
