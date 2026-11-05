package za.co.ntier.report;

import java.io.InputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.compiere.model.I_C_BPartner;
import org.compiere.model.MBPartner;
import org.compiere.model.Query;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;
import za.co.ntier.api.model.I_ZZLearner_v;
import za.co.ntier.api.model.X_ZZLearnerDGProgramme;
import za.co.ntier.api.model.X_ZZLearnerTermination;
import za.co.ntier.api.model.X_ZZLearner_v;
import za.co.ntier.api.model.X_ZZ_Program_Master_Data;
import za.co.ntier.webform.form.AttachmentUtil;

/** The electronic application attached when a termination request is submitted. */
public final class LearnerTerminationReport
{
	private static final String ATTACHMENT_PREFIX = "Termination Application";
	private static JasperReport report;

	private LearnerTerminationReport() {}

	public static void attach(X_ZZLearnerTermination request, X_ZZLearnerDGProgramme registration,
			X_ZZ_Program_Master_Data programme, String trxName) throws JRException
	{
		X_ZZLearner_v learner = new Query(request.getCtx(), I_ZZLearner_v.Table_Name,
				I_ZZLearner_v.COLUMNNAME_ZZLearner_ID + " = ?", trxName)
				.setParameters(request.getZZLearner_ID()).setClient_ID().setOnlyActiveRecords(true).first();
		if (learner == null)
			throw new IllegalStateException("The learner is no longer available. Select the learner again.");
		MBPartner employer = new Query(request.getCtx(), I_C_BPartner.Table_Name,
				I_C_BPartner.COLUMNNAME_C_BPartner_ID + " = ?", trxName)
				.setParameters(registration.getZZ_Employer_ID()).setClient_ID().first();
		Map<String, Object> data = build(request, registration, programme, learner, employer == null ? "" : employer.getName());
		String fileName = "Termination_Application_" + request.get_ID() + ".pdf";
		AttachmentUtil.addOrReplaceAttachmentEntry(request, fileName, render(data), ATTACHMENT_PREFIX, trxName);
	}

	static Map<String, Object> build(X_ZZLearnerTermination request, X_ZZLearnerDGProgramme registration,
			X_ZZ_Program_Master_Data programme, X_ZZLearner_v learner, String employerName)
	{
		Map<String, Object> data = new LinkedHashMap<>();
		data.put("Reference", text(request.getDocumentNo()));
		data.put("EmployerName", text(employerName));
		data.put("LearnerName", String.join(" ", text(learner.getZZFirstName()), text(learner.getZZMiddleName()),
				text(learner.getSurname())).replaceAll(" +", " ").trim());
		data.put("LearnerIdentity", first(learner.getZZ_ID_Passport_No(), learner.getZZOtherIDNo()));
		data.put("ProgrammeName", first(programme.getTitle(), programme.getName()));
		String type = first(registration.getZZProgramType(), programme.getTitle());
		String category = programmeCategory(type);
		data.put("ProgrammeType", text(type));
		data.put("ProgrammeCategory", category);
		data.put("ProgrammeCode", "Skills".equals(category) ? text(registration.getZZProgramCode()) : "");
		data.put("EffectiveDate", request.getZZTerminationDate() == null ? ""
				: request.getZZTerminationDate().toLocalDateTime().toLocalDate().format(DateTimeFormatter.ofPattern("dd MMMM uuuu", Locale.ENGLISH)));
		data.put("Reason", text(request.getZZ_Reason()));
		data.put("PermanentEmployment", request.isZZIsPermanentEmployment() ? "X" : "");
		return data;
	}

	private static String programmeCategory(String type)
	{
		String value = text(type).toLowerCase(Locale.ROOT);
		if (value.contains("skills programme")) return "Skills";
		if (value.contains("non-artisan") || value.contains("non artisan")) return "Learnership";
		if (value.contains("artisan")) return "Artisan";
		if (value.contains("learnership") || value.equals("rpl")) return "Learnership";
		if (value.contains("work experience")) return "WorkExperience";
		if (value.contains("vacation")) return "Vacation";
		if (value.contains("internship")) return "Internship";
		if (value.contains("management") && value.contains("development")) return "Management";
		if (value.contains("workplace coach")) return "Coaches";
		if (value.contains("candidacy")) return "Candidacy";
		return "";
	}

	public static byte[] render(Map<String, Object> data) throws JRException
	{
		Map<String, Object> parameters = new LinkedHashMap<>(data);
		parameters.put(JRParameter.REPORT_CLASS_LOADER, LearnerTerminationReport.class.getClassLoader());
		parameters.put("Header", LearnerTerminationReport.class.getResource("/za/co/ntier/report/jrxmls/MQA_Address_Logo_Header.png"));
		return JasperExportManager.exportReportToPdf(JasperFillManager.fillReport(getReport(), parameters, new JREmptyDataSource(1)));
	}

	private static synchronized JasperReport getReport() throws JRException
	{
		if (report == null)
		{
			try (InputStream source = LearnerTerminationReport.class.getResourceAsStream("/za/co/ntier/report/jrxmls/LearnerTermination.jrxml"))
			{
				if (source == null) throw new IllegalStateException("Termination application template is missing.");
				report = JasperCompileManager.compileReport(source);
			}
			catch (IOException ex)
			{
				throw new JRException("Could not read the termination application template.", ex);
			}
		}
		return report;
	}

	private static String first(String preferred, String fallback)
	{
		return preferred == null || preferred.isBlank() ? text(fallback) : preferred;
	}

	private static String text(String value)
	{
		return value == null ? "" : value;
	}
}
