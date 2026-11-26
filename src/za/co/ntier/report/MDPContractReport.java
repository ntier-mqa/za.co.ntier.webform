package za.co.ntier.report;

import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.SimpleJasperReportsContext;
import net.sf.jasperreports.engine.fonts.FontFamily;
import net.sf.jasperreports.engine.fonts.SimpleFontFamily;
import za.co.ntier.api.model.I_ZZLearner_v;
import za.co.ntier.api.model.X_ZZLearnerDGProgramme;
import za.co.ntier.api.model.X_ZZLearner_v;
import za.co.ntier.api.model.X_ZZ_Program_Master_Data;

/**
 * Management Development Programme (MDP) Contract report generator.
 */
public final class MDPContractReport
{
	private static final String REPORT_PATH = "/za/co/ntier/report/jrxmls/MDPContract.jrxml";
	private static final String FONT_DIR = "za/co/ntier/report/fonts/";
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd MMMM uuuu", Locale.ENGLISH);
	private static JasperReport cachedReport;
	private static JasperReportsContext reportContext;

	private MDPContractReport()
	{
	}

	private static synchronized JasperReportsContext getReportContext()
	{
		if (reportContext == null)
		{
			SimpleJasperReportsContext ctx = new SimpleJasperReportsContext(DefaultJasperReportsContext.getInstance());
			List<FontFamily> families = new ArrayList<>();
			List<FontFamily> parentFamilies = DefaultJasperReportsContext.getInstance().getExtensions(FontFamily.class);
			if (parentFamilies != null)
			{
				families.addAll(parentFamilies);
			}
			// Map Arial to bundled metric-compatible Liberation Sans so PDF export works cleanly on Linux without MS Core Fonts.
			SimpleFontFamily arialFamily = new SimpleFontFamily(ctx);
			arialFamily.setName("Arial");
			arialFamily.setNormal(FONT_DIR + "LiberationSans-Regular.ttf");
			arialFamily.setBold(FONT_DIR + "LiberationSans-Bold.ttf");
			arialFamily.setItalic(FONT_DIR + "LiberationSans-Italic.ttf");
			arialFamily.setBoldItalic(FONT_DIR + "LiberationSans-BoldItalic.ttf");
			arialFamily.setPdfEncoding("Identity-H");
			arialFamily.setPdfEmbedded(Boolean.TRUE);
			families.add(arialFamily);
			ctx.setExtensions(FontFamily.class, families);
			reportContext = ctx;
		}
		return reportContext;
	}

	/**
	 * Compile or retrieve the cached JasperReport instance.
	 */
	public static synchronized JasperReport getReport() throws JRException
	{
		if (cachedReport == null)
		{
			try (InputStream source = MDPContractReport.class.getResourceAsStream(REPORT_PATH))
			{
				if (source == null)
				{
					throw new IllegalStateException("MDP Contract report template not found: " + REPORT_PATH);
				}
				cachedReport = JasperCompileManager.getInstance(getReportContext()).compile(source);
			}
			catch (IOException ex)
			{
				throw new JRException("Could not read the MDP Contract report template.", ex);
			}
		}
		return cachedReport;
	}

	/**
	 * Render the MDP Contract to PDF bytes from a map of data values.
	 */
	public static byte[] render(Map<String, Object> data) throws JRException
	{
		Map<String, Object> parameters = new LinkedHashMap<>();
		if (data != null)
		{
			parameters.putAll(data);
			parameters.put("ContractData", data);
		}

		ClassLoader reportClassLoader = new ClassLoader(MDPContractReport.class.getClassLoader())
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
			parameters.put("HeaderImage", MDPContractReport.class.getResource("/za/co/ntier/report/jrxmls/MQA_Address_Logo_Header.png"));
		}
		if (!parameters.containsKey("FooterImage"))
		{
			parameters.put("FooterImage", MDPContractReport.class.getResource("/za/co/ntier/report/jrxmls/Footer_Mining_Future_Skills.png"));
		}

		JasperReportsContext jrContext = getReportContext();
		ClassLoader prevCl = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(reportClassLoader);
		try
		{
			JasperPrint print = JasperFillManager.getInstance(jrContext).fill(getReport(), parameters, new JREmptyDataSource(1));
			return JasperExportManager.getInstance(jrContext).exportToPdf(print);
		}
		finally
		{
			Thread.currentThread().setContextClassLoader(prevCl);
		}
	}

	/**
	 * Build report data from an MDP learner registration record.
	 */
	public static Map<String, Object> buildData(X_ZZLearnerDGProgramme registration, String trxName)
	{
		Map<String, Object> data = new LinkedHashMap<>();
		if (registration == null)
		{
			return data;
		}

		Properties ctx = registration.getCtx();

		// Learner details
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

			data.put("EmployeeName", fullName);
			data.put("LearnerName", fullName);
			data.put("LearnerAttention", fullName);

			String idNumber = firstNonEmpty(learner.getZZ_ID_Passport_No(), learner.getZZOtherIDNo());
			data.put("IDNumber", idNumber);
			data.put("LearnerId", idNumber);

			String learnerAddress = formatAddress(ctx, learner.getZZPhysicalLocation_ID(), trxName);
			data.put("LearnerPhysicalAddress", learnerAddress);
			data.put("LearnerEmail", safeText(learner.getEMail()));
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

		// Financial year from program master data, with fallback
		String finYear = "";
		if (registration.getZZ_Program_Master_Data_ID() > 0)
		{
			X_ZZ_Program_Master_Data programMasterData = new X_ZZ_Program_Master_Data(
					ctx, registration.getZZ_Program_Master_Data_ID(), trxName);
			if (programMasterData.getC_Year_ID() > 0)
			{
				MYear year = new MYear(ctx, programMasterData.getC_Year_ID(), trxName);
				finYear = firstNonEmpty(year.getDescription(), year.getFiscalYear());
			}
		}
		if (Util.isEmpty(finYear))
		{
			finYear = "2026/2027";
		}
		data.put("FinancialYear", finYear);

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
		String employeeName = data != null ? safeText(data.get("EmployeeName")) : "";
		if (!employeeName.isBlank())
		{
			String sanitized = employeeName.trim().replaceAll("\\s+", "_").replaceAll("[^a-zA-Z0-9_]+", "");
			if (!sanitized.isBlank())
			{
				return "MDP_Contract_" + sanitized + ".pdf";
			}
		}
		return "MDP_Contract.pdf";
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
