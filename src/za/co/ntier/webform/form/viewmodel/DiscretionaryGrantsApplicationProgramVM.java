package za.co.ntier.webform.form.viewmodel;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.adempiere.model.GenericPO;
import org.adempiere.webui.desktop.DefaultDesktop;
import org.adempiere.webui.session.SessionManager;
import org.apache.commons.lang3.StringUtils;
import org.compiere.model.MBPartner;
import org.compiere.model.MClient;
import org.compiere.model.MMailText;
import org.compiere.model.MSysConfig;
import org.compiere.model.MTable;
import org.compiere.model.MUser;
import org.compiere.model.Query;
import org.compiere.model.X_C_BPartner;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.zkoss.bind.annotation.DependsOn;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Tabbox;

import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.form.bean.DataType;
import za.co.ntier.webform.form.bean.ProgramType;
import za.co.ntier.webform.form.bean.component.AddressInfo;
import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.Dialog;
import za.co.ntier.webform.form.bean.component.EmployerDeclarationInfo;
import za.co.ntier.webform.form.bean.component.FormInfo;
import za.co.ntier.webform.form.bean.component.OrganisationInfo;
import za.co.ntier.webform.form.bean.component.UploadDocComponent;
import za.co.ntier.webform.form.bean.program.AetProgram;
import za.co.ntier.webform.form.bean.program.ArtisanAidesProgram;
import za.co.ntier.webform.form.bean.program.ArtisanDevProgram;
import za.co.ntier.webform.form.bean.program.ArtisanRPLProgram;
import za.co.ntier.webform.form.bean.program.CandidacyProgram;
import za.co.ntier.webform.form.bean.program.CentreOfSpecialisationProgram;
import za.co.ntier.webform.form.bean.program.CetTvetProgram;
import za.co.ntier.webform.form.bean.program.InhouseTrainingProgram;
import za.co.ntier.webform.form.bean.program.InternshipProgram;
import za.co.ntier.webform.form.bean.program.MedpProgram;
import za.co.ntier.webform.form.bean.program.NcvGraduatesProgram;
import za.co.ntier.webform.form.bean.program.NonArtisanDevProgram;
import za.co.ntier.webform.form.bean.program.NonArtisanDevRPLProgram;
import za.co.ntier.webform.form.bean.program.OhasspProgram;
import za.co.ntier.webform.form.bean.program.WorkExperienceProgram;
import za.co.ntier.webform.form.viewmodel.component.ComponentVMWrapper;
import za.co.ntier.webform.model.I_ZZ_Application_Form;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class DiscretionaryGrantsApplicationProgramVM {
	public class EmailPoInfo extends GenericPO {

		private static final long serialVersionUID = -433026634223871908L;

		public EmailPoInfo() {
			super(MUser.Table_Name, Env.getCtx(), 0);
		}

		public String getAppFormDocno(){
			return applicationForm.getDocumentNo();
		}
		
		public String getAppFormSubmitedDate() {
			return applicationForm.getUpdated().toLocalDateTime().truncatedTo(ChronoUnit.SECONDS).format(dtf);
		}
		
		public String getAppFormTitle(){
			return menuContextInfo.getProgramMasterData().getTitle();
		}
		
	}
	
	public static DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

	public static final int EMPLOYER_APP_AD_FORM_ID = 1000000; // your WebForm AD_Form_ID

	public static final int FROM_EMAIL_USER_ID = MSysConfig.getIntValue("FROM_EMAIL_USER_ID",1000011);

	private static final CLogger log = CLogger.getCLogger(DiscretionaryGrantsApplicationProgramVM.class);

	public static void showAppList(){
		// 2) close the CURRENT tab first
        DefaultDesktop desktop = (DefaultDesktop) SessionManager.getAppDesktop();
        desktop.closeActiveWindow();  // <— closes the active AD Form tab

        // Open ApplicationsList.zul via WebForm context
        String ctx = ""
            + "zulPath=/za/co/ntier/webform/zul/program/ApplicationsList.zul\n"
            + "formTitle=Applications\n"
            + "ZZ_Program_Master_Data_UU=a3db65ee-97d9-429d-9734-aca9e89dd3af\n"
            + "programType=UNKNOWN\n";

       
        desktop.setPredefinedContextVariables(ctx);
        desktop.openForm(EMPLOYER_APP_AD_FORM_ID);
	}

	private AddressInfo alternateProgramContact;

	private X_ZZ_Application_Form applicationForm;

	private String documentNo;
	private EmployerDeclarationInfo employerDeclarationInfo;

	// --- Getters and Setters for Data Binding ---

	private FormInfo formInfo;

	private MenuContextInfo menuContextInfo;

	private OrganisationInfo organisationInfo;

	private IProgram program;

	private AddressInfo programContact;

	private ProgramType programType;

	private int recordId;

	private int tableId;

	private UploadDocComponent uploadDoc;

	public void deleteApp() {
		if (applicationForm != null) {
			try {
				applicationForm.setIsActive(false);
				applicationForm.saveEx(null);
				showAppList();
			}catch (Exception e) {
				Clients.showNotification(
		                "Can't Delete application form:" + e.getMessage(),
		                "error", null, "end_center", 0, true);
			}
		}else {
			showAppList();
		}
		
	}

	private ColumnInfo<?> findCol(AnnexureInfo a, String... aliases) {
	    for (ColumnInfo<?> c : a.getColumnInfos()) {
	        String t = (c.getTitle() != null ? c.getTitle() : "").trim().toLowerCase();
	        for (String alias : aliases) {
	            if (t.equals(alias.toLowerCase())) return c;
	        }
	    }
	    return null;
	}

	/**
	 * @return the alternateProgramContact
	 */
	public AddressInfo getAlternateProgramContact() {
		return alternateProgramContact;
	}

	/**
	 * @return the applicationForm
	 */
	public X_ZZ_Application_Form getApplicationForm() {
		return applicationForm;
	}


	/**
	 * @return the documentNo
	 */
	public String getDocumentNo() {
		return documentNo;
	}

	public EmployerDeclarationInfo getEmployerDeclarationInfo() {
		return employerDeclarationInfo;
	}

	/**
	 * @return the formInfo
	 */
	public FormInfo getFormInfo() {
		return formInfo;
	}

	private Integer getLearners(DataType dt, Object cell) {
	    if (cell == null) return null;
	    switch (dt) {
	        case PositiveNumber:
	            // your ZUL uses row[col].value for numbers
	            try {
	                Object v = cell.getClass().getMethod("getValue").invoke(cell);
	                if (v instanceof Number) return ((Number)v).intValue();
	            } catch (Exception ignore) {}
	            return null;
	        case Text:
	            try {
	                String s = ((String)cell).trim();
	                if (s.isEmpty()) return null;
	                return Integer.parseInt(s);
	            } catch (Exception e) { return null; }
	        default:
	            return null;
	    }
	}

	/**
	 * @return the programMasterDataID
	 */
	public MenuContextInfo getMenuContextInfo() {
		return menuContextInfo;
	}
	
	/**
	 * @return the organisationInfo
	 */
	public OrganisationInfo getOrganisationInfo() {
		return organisationInfo;
	}

	/**
	 * @return the program
	 */
	public IProgram getProgram() {
		return program;
	}


	/**
	 * @return the programContact
	 */
	public AddressInfo getProgramContact() {
		return programContact;
	}

	public ProgramType getProgramType() {
		return programType;
	}

	public int getRecordId() {
		return recordId;
	}

	public String getSubmitButtonLabel() {
		return "Submit Application";
	}

	public int getTableId() {
		return tableId;
	}

	/**
	 * @return the uploadDoc
	 */
	public UploadDocComponent getUploadDoc() {
		return uploadDoc;
	}

 	@Init
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo){

		setMenuContextInfo(menuContextInfo);
		programType = menuContextInfo.getProgramType();
		
		if (StringUtils.isNotBlank(menuContextInfo.getApplicationFormUU())) {
			applicationForm = new X_ZZ_Application_Form(Env.getCtx(), menuContextInfo.getApplicationFormUU(), null);
			if (!applicationForm.isActive()) {
				showDialog("Deleted Application Form", "This application form is deleted");
			}
			
		}
		
		employerDeclarationInfo = new EmployerDeclarationInfo();
		employerDeclarationInfo.initComponent(applicationForm);
		
		formInfo = new FormInfo(menuContextInfo);

		organisationInfo = new OrganisationInfo(menuContextInfo, this);
		organisationInfo.initComponent(applicationForm);

		if (programType.isCetTvet()) {
			setProgram(new CetTvetProgram(menuContextInfo, applicationForm));
		} else if (ProgramType.INTERNSHIP == programType) {
			setProgram(new InternshipProgram(menuContextInfo, applicationForm));
		} else if (ProgramType.CANDIDACY == programType) {
			setProgram(new CandidacyProgram(menuContextInfo, applicationForm));
		} else if (ProgramType.EXPERIENCE == programType) {
			setProgram(new WorkExperienceProgram(menuContextInfo, applicationForm));
		} else if (ProgramType.DEV_PROGRAM == programType) {
			setProgram(new MedpProgram());
		} else if (ProgramType.ARTISAN_AIDES == programType) {
			setProgram(new ArtisanAidesProgram(applicationForm));
		} else if (ProgramType.ARTISAN_DEV == programType) {
			setProgram(new ArtisanDevProgram(menuContextInfo, applicationForm));
		} else if (ProgramType.CENTRE_SPECIALISATION == programType) {
			setProgram(new CentreOfSpecialisationProgram(menuContextInfo, applicationForm));
		} else if (ProgramType.ARTISAN_RPL == programType) {
			setProgram(new ArtisanRPLProgram(applicationForm));
		} else if (ProgramType.NON_ARTISAN_DEV == programType) {
			setProgram(new NonArtisanDevProgram(menuContextInfo, applicationForm));
		} else if (ProgramType.NON_ARTISAN_DEV_RPL == programType) {
			setProgram(new NonArtisanDevRPLProgram(menuContextInfo, applicationForm));
		} else if (ProgramType.NCV_GRADUATES == programType) {
			setProgram(new NcvGraduatesProgram(menuContextInfo, applicationForm));
		} else if (ProgramType.AET == programType) {
			setProgram(new AetProgram(menuContextInfo, applicationForm));
		} else if (ProgramType.OHASSP == programType) {
			setProgram(new OhasspProgram(applicationForm));
		} else if (ProgramType.INHOUSE_TRAINING == programType) {
			setProgram(new InhouseTrainingProgram(applicationForm));
		}

		

		// main contact
		if (programType.isShowMainAddress()) {
			programContact = new AddressInfo(programType, false);
			programContact.initComponent(applicationForm);
		}
			

		// main alternate contact
		if (programType.isShowMainAddressAlter()) {
			alternateProgramContact = new AddressInfo(programType, true);
			alternateProgramContact.initComponent(applicationForm);
		}
			

		uploadDoc = new UploadDocComponent(menuContextInfo, applicationForm);
		
		org.zkoss.bind.BindUtils.postNotifyChange(null, null, this, "declarationComplete");
		org.zkoss.bind.BindUtils.postNotifyChange(null, null, this, "organisationComplete");
		org.zkoss.bind.BindUtils.postNotifyChange(null, null, this, "programComplete");
		org.zkoss.bind.BindUtils.postNotifyChange(null, null, this, "programContactComplete");

	}
	 	
	private boolean isAddressBlockComplete(AddressInfo a) {
	    boolean siteOk   = !a.showSiteName()        || notEmpty(a.getSiteName());
	    boolean lineOk   = !a.showLineAddress()     || notEmpty(a.getAddressLine());
	    boolean postOk   = !a.showPostalAddress()   ||  notEmpty(a.getAddressLine()); //notEmpty(a.getPostAddress());
	    boolean geoOk    = !a.showGeographicAddress() ||
	                       (notEmpty(a.getPostalCode())
	                        && a.getAreaSelected() != null
	                        && a.getProvinceSelected() != null);
	    boolean contactOk = !a.showContact() || (
	            notEmpty(a.getNameSiteRepresentative()) &&
	            notEmpty(a.getRepresentativeDesignation()) &&
	            isTenDigits(a.getMobileNumber()) &&
	            isTenDigits(a.getLandlineNumber()) &&
	            isEmail(a.getEmail())
	    );
	    return siteOk && lineOk && postOk && geoOk && contactOk;
	}
	
	@DependsOn("employerDeclarationInfo.acknowledged")
	public boolean isDeclarationComplete() {
	    return employerDeclarationInfo != null && Boolean.TRUE.equals(employerDeclarationInfo.getAcknowledged());
	}

	private boolean isEmail(String s)     { return s != null && s.matches("^[^@\\s]+@[^@\\s]+\\.[A-Za-z]{2,}$"); }

	private boolean isFieldOfStudyBlank(DataType dt, Object cell) {
	    if (cell == null) return true;
	    switch (dt) {
	        case Text:
	            return ((String)cell).trim().isEmpty();
	        case List:
	            // selection required; any non-null item counts as filled
	            return false;
	        case Label:
	        default:
	            // treat other types defensively
	            return (cell.toString().trim().isEmpty());
	    }
	}

	@DependsOn({
		  "programType",
		  "organisationInfo.physicalAddressInfo.areaSelected",
		  "organisationInfo.physicalAddressInfo.provinceSelected",
		  "organisationInfo.postAddressInfo.areaSelected",
		  "organisationInfo.postAddressInfo.provinceSelected",
		  "organisationInfo.orgName",
		  "organisationInfo.orgRegistrationNumber",
		  "organisationInfo.orgTaxNumber",
		  "organisationInfo.physicalAddressInfo.addressLine",
		  "organisationInfo.physicalAddressInfo.postalCode",
		  "organisationInfo.cetTvetCollegeSelected",
		  "organisationInfo.sdlNumber",
		  "organisationInfo.siteSDLNumber",
		  "organisationInfo.postAddressInfo.postalCode",
		  "organisationInfo.orgSizeInfo.numOfEmployer", 
		  // orgContact (all required)
		  "organisationInfo.orgContact.nameSiteRepresentative",
		  "organisationInfo.orgContact.representativeDesignation",
		  "organisationInfo.orgContact.mobileNumber",
		  "organisationInfo.orgContact.landlineNumber",
		  "organisationInfo.orgContact.email",

		  // alternateOrgContact (all required)
		  "organisationInfo.alternateOrgContact.nameSiteRepresentative",
		  "organisationInfo.alternateOrgContact.representativeDesignation",
		  "organisationInfo.alternateOrgContact.mobileNumber",
		  "organisationInfo.alternateOrgContact.landlineNumber",
		  "organisationInfo.alternateOrgContact.email"
		})
	
	public boolean isOrganisationComplete() {
		boolean colOk = true;
		boolean orgOk = true;
	    if (programType.isCetTvet()) {
	    	colOk = organisationInfo.getCetTvetCollegeSelected() != null;
	    } else {
	    	 orgOk = notEmpty(organisationInfo.getOrgName())  
	    			 && notEmpty(organisationInfo.getOrgTaxNumber())
	    			 && notEmpty(organisationInfo.getOrgRegistrationNumber());
	    }
	    orgOk = orgOk
	        && notEmpty(organisationInfo.getSdlNumber())
	        && notEmpty(organisationInfo.getSiteSDLNumber());	    
	    Integer n = organisationInfo.getOrgSizeInfo() != null
	            ? organisationInfo.getOrgSizeInfo().getNumOfEmployer()
	            : null;
	    orgOk = orgOk && n != null && n > 0;
	    boolean addrOk = (!organisationInfo.getPhysicalAddressInfo().showLineAddress()
	                      || notEmpty(organisationInfo.getPhysicalAddressInfo().getAddressLine()))
	        && (!organisationInfo.getPhysicalAddressInfo().showGeographicAddress()
	                      || notEmpty(organisationInfo.getPhysicalAddressInfo().getPostalCode()));
	    boolean postalOk = (!organisationInfo.getPostAddressInfo().showPostalAddress()
                || notEmpty(organisationInfo.getPostAddressInfo().getAddressLine()))
	    		&& (!organisationInfo.getPostAddressInfo().showGeographicAddress()
                || notEmpty(organisationInfo.getPostAddressInfo().getPostalCode()));
	    boolean provincesPhysicalOk = organisationInfo.getPhysicalAddressInfo().getProvinceSelected() != null  &&
	    		notEmpty(organisationInfo.getPhysicalAddressInfo().getProvinceSelected().getName());
	    boolean provincesPostalOk = (!organisationInfo.getPostAddressInfo().showPostalAddress()
	    		|| (organisationInfo.getPostAddressInfo().getProvinceSelected() != null &&
	    				notEmpty(organisationInfo.getPostAddressInfo().getProvinceSelected().getName())
	    				));
	    // orgContact required (only if contact section is shown)
	    boolean orgContactOk = organisationInfo.getOrgContact() == null 
	    		|| !organisationInfo.getOrgContact().showContact()
	        || (notEmpty(organisationInfo.getOrgContact().getNameSiteRepresentative())
	            && notEmpty(organisationInfo.getOrgContact().getRepresentativeDesignation())
	            && notEmpty(organisationInfo.getOrgContact().getMobileNumber())
	            && notEmpty(organisationInfo.getOrgContact().getLandlineNumber())
	            && notEmpty(organisationInfo.getOrgContact().getEmail()));

	    // alternateOrgContact required (only if shown)
	    boolean altContactOk = organisationInfo.getAlternateOrgContact() == null
	    		|| !organisationInfo.getAlternateOrgContact().showContact()
	        || (notEmpty(organisationInfo.getAlternateOrgContact().getNameSiteRepresentative())
	            && notEmpty(organisationInfo.getAlternateOrgContact().getRepresentativeDesignation())
	            && notEmpty(organisationInfo.getAlternateOrgContact().getMobileNumber())
	            && notEmpty(organisationInfo.getAlternateOrgContact().getLandlineNumber())
	            && notEmpty(organisationInfo.getAlternateOrgContact().getEmail()));
	    return orgOk && addrOk && colOk && postalOk
	            && provincesPhysicalOk && provincesPostalOk
	            && orgContactOk && altContactOk;
	}

	@DependsOn("program") // re-evaluate when program/annexure rows change (notified from table VM)
	public boolean isProgramComplete() {
	    if (programType == ProgramType.TVET_BURSARS) {
	        return isTvetBursarsRowsValid();
	    }
	    // keep your existing rules for other program types
	    if (programType.isDev_Program()) {
	        Integer n = ((MedpProgram) program).getNoOfLearners();
	        return n != null && n > 0;
	    }
	    if (programType == ProgramType.ARTISAN_AIDES) {
	        return isArtisanAidesValid();   
	    }
	    if (programType == ProgramType.ARTISAN_DEV) {
	        return isArtisanDevValid();       // <-- add this
	    }
	    return true;
	}
	
	@DependsOn({
		  "programType",

		  // MAIN contact
		  "programContact.siteName",
		  "programContact.addressLine",
		//  "programContact.postAddress",
		  "programContact.postalCode",
		  "programContact.areaSelected",
		  "programContact.provinceSelected",
		  "programContact.nameSiteRepresentative",
		  "programContact.representativeDesignation",
		  "programContact.mobileNumber",
		  "programContact.landlineNumber",
		  "programContact.email",

		  // ALTERNATE contact (only enforced if shown)
		  "alternateProgramContact.siteName",
		  "alternateProgramContact.addressLine",
		//  "alternateProgramContact.postAddress",
		  "alternateProgramContact.postalCode",
		  "alternateProgramContact.areaSelected",
		  "alternateProgramContact.provinceSelected",
		  "alternateProgramContact.nameSiteRepresentative",
		  "alternateProgramContact.representativeDesignation",
		  "alternateProgramContact.mobileNumber",
		  "alternateProgramContact.landlineNumber",
		  "alternateProgramContact.email"
		})
		public boolean isProgramContactComplete() {
		    if (!programType.isShowMainAddress()) return true;

		    boolean mainOk = isAddressBlockComplete(programContact);

		    boolean altOk = true;
		    if (programType.isShowMainAddressAlter()) {
		        altOk = isAddressBlockComplete(alternateProgramContact);
		    }

		    return mainOk && altOk;
		}
	
	public boolean isShowUploadDoc() {
		return uploadDoc != null && uploadDoc.getUploadDoc().getRows().size() > 0;
	}
	
	private boolean isTenDigits(String s) { return s != null && s.matches("^\\d{10}$"); }

	private boolean isTvetBursarsRowsValid() {
	    if (!(program instanceof CetTvetProgram)) return false;
	    CetTvetProgram p = (CetTvetProgram) program;

	    // try every annexure until we find the one that carries these two columns
	    for (AnnexureInfo a : p.getAnnexureInfos()) {
	        ColumnInfo<?> fosCol = findCol(a, "Field of Study", "Programme", "Program", "Qualification", "Course");
	        ColumnInfo<?> nolCol = findCol(a, "No of Learners", "No. of Learners", "Number of Learners", "Learners");
	        if (fosCol == null || nolCol == null) continue;

	        int okRows = 0;
	        for (var row : a.getRows()) {
	            Object fosCell = row.get(fosCol);
	            Object nolCell = row.get(nolCol);

	            boolean fosBlank = isFieldOfStudyBlank(fosCol.getDataType(), fosCell);
	            Integer learners = getLearners(nolCol.getDataType(), nolCell);

	            boolean learnersBlank = (learners == null);
	            boolean bothBlank = fosBlank && learnersBlank;

	            if (bothBlank) {
	                // ignore the row
	                continue;
	            }
	            // partial or invalid count → fail
	            if (fosBlank || learners == null || learners <= 0) {
	                return false;
	            }
	            // both present and learners > 0
	            okRows++;
	        }
	        // must have at least one complete row
	        return okRows >= 1;
	    }
	    // didn't find matching annexure/columns
	    return false;
	}
	
	private boolean isArtisanAidesValid() {
	    if (!(program instanceof ArtisanAidesProgram)) return false;
	    ArtisanAidesProgram p = (ArtisanAidesProgram) program;

	    AnnexureInfo qual  = p.getQualification();
	    AnnexureInfo skill = p.getSkill();

	    boolean qualOk  = qual  == null ? true : validateArtisanAnnexure_NoProgramme(qual);
	    boolean skillOk = skill == null ? true : validateArtisanAnnexure_NoProgramme(skill);

	    // At least one annexure must have a valid row
	    return (qualOk && skillOk) && (hasValidRow_NoProgramme(qual) || hasValidRow_NoProgramme(skill));
	}

	
	private boolean validateArtisanAnnexure_NoProgramme(AnnexureInfo a) {
	    if (a == null) return true;

	    ColumnInfo<?> employedCol   = findCol(a, "No. of Employed Learners", "Employed", "No Employed", "Employed Learners");
	    ColumnInfo<?> unemployedCol = findCol(a, "No. of Unemployed Learners", "Unemployed", "No Unemployed", "Unemployed Learners");
	    ColumnInfo<?> totalCol      = findCol(a, "Total No. of Learners Applied For", "Total No. of Learners", "Total Learners");
	    ColumnInfo<?> postalCol     = findCol(a, "Site Postal Code", "Postal Code", "Site Postal");
	    ColumnInfo<?> areaCol       = findCol(a, "Area");

	    // Must at least have the three counts; location is also required by your UI
	    if (employedCol == null || unemployedCol == null || totalCol == null || postalCol == null || areaCol == null) {
	        return false;
	    }

	    int validRows = 0;
	    for (var row : a.getRows()) {
	        Integer employed   = getLearners(employedCol.getDataType(),   row.get(employedCol));
	        Integer unemployed = getLearners(unemployedCol.getDataType(), row.get(unemployedCol));
	        Integer total      = getLearners(totalCol.getDataType(),      row.get(totalCol));

	        // postal stored as PostalData; area stored as AreaData (from your AnnexureInfo)
	        String postal = null;
	        Object postalCell = row.get(postalCol);
	        if (postalCell != null) {
	            try { postal = (String) postalCell.getClass().getMethod("getPostal").invoke(postalCell); } catch (Exception ignore) {}
	        }

	        Object areaCell = row.get(areaCol);
	        Object areaSelected = null;
	        if (areaCell != null) {
	            try { areaSelected = areaCell.getClass().getMethod("getSelectedArea").invoke(areaCell); } catch (Exception ignore) {}
	        }

	        boolean allBlank = (employed == null && unemployed == null && total == null && postal == null && areaSelected == null);
	        if (allBlank) {
	            // Ignore empty line
	            continue;
	        }

	        // Mandatory rules:
	        //  - counts present and consistent
	        //  - at least one count > 0
	        //  - location filled (postal + area)
	        boolean countsPresent = employed != null && unemployed != null && total != null;
	        boolean countsPositive = (employed != null && unemployed != null && total != null) &&
	                                 (employed > 0 || unemployed > 0); // (or require both > 0 if needed)
	        boolean countsConsistent = (employed != null && unemployed != null && total != null) &&
	                                   (employed + unemployed == total);

	        boolean locationOk = (postal != null && !postal.trim().isEmpty() && areaSelected != null);

	        boolean rowValid = countsPresent && countsPositive && countsConsistent && locationOk;
	        if (!rowValid) return false;  // any partially filled/invalid row fails
	        validRows++;
	    }

	    // Need at least one valid row in this annexure (qualification or skills)
	    return validRows >= 1;
	}

	private boolean hasValidRow_NoProgramme(AnnexureInfo a) {
	    if (a == null) return false;

	    ColumnInfo<?> employedCol   = findCol(a, "No. of Employed Learners", "Employed", "No Employed", "Employed Learners");
	    ColumnInfo<?> unemployedCol = findCol(a, "No. of Unemployed Learners", "Unemployed", "No Unemployed", "Unemployed Learners");
	    ColumnInfo<?> totalCol      = findCol(a, "Total No. of Learners Applied For", "Total No. of Learners", "Total Learners");
	    ColumnInfo<?> postalCol     = findCol(a, "Site Postal Code", "Postal Code", "Site Postal");
	    ColumnInfo<?> areaCol       = findCol(a, "Area");
	    if (employedCol == null || unemployedCol == null || totalCol == null || postalCol == null || areaCol == null) return false;

	    for (var row : a.getRows()) {
	        Integer employed   = getLearners(employedCol.getDataType(),   row.get(employedCol));
	        Integer unemployed = getLearners(unemployedCol.getDataType(), row.get(unemployedCol));
	        Integer total      = getLearners(totalCol.getDataType(),      row.get(totalCol));

	        String postal = null;
	        Object postalCell = row.get(postalCol);
	        if (postalCell != null) {
	            try { postal = (String) postalCell.getClass().getMethod("getPostal").invoke(postalCell); } catch (Exception ignore) {}
	        }

	        Object areaCell = row.get(areaCol);
	        Object areaSelected = null;
	        if (areaCell != null) {
	            try { areaSelected = areaCell.getClass().getMethod("getSelectedArea").invoke(areaCell); } catch (Exception ignore) {}
	        }

	        if (employed != null && unemployed != null && total != null &&
	            (employed > 0 || unemployed > 0) &&
	            employed + unemployed == total &&
	            postal != null && !postal.trim().isEmpty() &&
	            areaSelected != null) {
	            return true;
	        }
	    }
	    return false;
	}
	
	private boolean isArtisanDevValid() {
	    if (!(program instanceof ArtisanDevProgram)) return false;
	    ArtisanDevProgram p = (ArtisanDevProgram) program;

	    // main “trade” grid
	    AnnexureInfo trade = p.getTrade();
	    // the “Total No. of Learners Applied For” one-liner at the bottom
	    AnnexureInfo total = p.getTotalNumApplied();

	    boolean tradeOk = trade == null ? true : validateArtisanDevTrade(trade);
	    boolean totalOk = total == null ? true : validateArtisanDevTotal(total, trade);

	    // require at least one valid trade row and the total row coherent (if present)
	    return tradeOk && totalOk && hasValidArtisanDevRow(trade);
	}

	private boolean validateArtisanDevTrade(AnnexureInfo a) {
	    if (a == null) return true;

	    ColumnInfo<?> tradeCol   = findCol(a, "Trade"); // label column (not mandatory itself)
	    ColumnInfo<?> learnersCol= findCol(a, "No. of Learners", "No of Learners", "Learners");
	    ColumnInfo<?> postalCol  = findCol(a, "Site Postal Code", "Postal Code");
	    ColumnInfo<?> areaCol    = findCol(a, "Area");

	    // these three are required for a filled row
	    if (learnersCol == null || postalCol == null || areaCol == null) return false;

	    for (var row : a.getRows()) {
	        Integer learners = getLearners(learnersCol.getDataType(), row.get(learnersCol));

	        String postal = null;
	        Object postalCell = row.get(postalCol);
	        if (postalCell != null) {
	            try { postal = (String) postalCell.getClass().getMethod("getPostal").invoke(postalCell); } catch (Exception ignore) {}
	        }

	        Object areaCell = row.get(areaCol);
	        Object areaSelected = null;
	        if (areaCell != null) {
	            try { areaSelected = areaCell.getClass().getMethod("getSelectedArea").invoke(areaCell); } catch (Exception ignore) {}
	        }

	        boolean emptyLine = (learners == null && (postal == null || postal.isBlank()) && areaSelected == null);
	        if (emptyLine) continue; // ignore blanks

	        // row is considered filled → must be valid
	        if (learners == null || learners <= 0) return false;
	        if (postal == null || postal.isBlank()) return false;
	        if (areaSelected == null) return false;
	    }
	    return true;
	}

	private boolean hasValidArtisanDevRow(AnnexureInfo a) {
	    if (a == null) return false;
	    ColumnInfo<?> learnersCol= findCol(a, "No. of Learners", "No of Learners", "Learners");
	    ColumnInfo<?> postalCol  = findCol(a, "Site Postal Code", "Postal Code");
	    ColumnInfo<?> areaCol    = findCol(a, "Area");
	    if (learnersCol == null || postalCol == null || areaCol == null) return false;

	    for (var row : a.getRows()) {
	        Integer learners = getLearners(learnersCol.getDataType(), row.get(learnersCol));
	        String postal = null;
	        Object postalCell = row.get(postalCol);
	        if (postalCell != null) {
	            try { postal = (String) postalCell.getClass().getMethod("getPostal").invoke(postalCell); } catch (Exception ignore) {}
	        }
	        Object areaCell = row.get(areaCol);
	        Object areaSelected = null;
	        if (areaCell != null) {
	            try { areaSelected = areaCell.getClass().getMethod("getSelectedArea").invoke(areaCell); } catch (Exception ignore) {}
	        }

	        if (learners != null && learners > 0
	                && postal != null && !postal.isBlank()
	                && areaSelected != null) {
	            return true;
	        }
	    }
	    return false;
	}

	private boolean validateArtisanDevTotal(AnnexureInfo total, AnnexureInfo trade) {
	    if (total == null) return true;

	    // total grid looks like: [Total No. of Learners Applied For | Site Postal Code | Area]
	    ColumnInfo<?> totalCol  = findCol(total, "Total No. of Learners Applied For", "Total No. of Learners", "Total Learners");
	    ColumnInfo<?> postalCol = findCol(total, "Site Postal Code", "Postal Code");
	    ColumnInfo<?> areaCol   = findCol(total, "Area");
	    if (totalCol == null || postalCol == null || areaCol == null) return false;

	    if (total.getRows().isEmpty()) return false;
	    var row = total.getRows().get(0);

	    Integer totalLearners = getLearners(totalCol.getDataType(), row.get(totalCol));

	    String postal = null;
	    Object postalCell = row.get(postalCol);
	    if (postalCell != null) {
	        try { postal = (String) postalCell.getClass().getMethod("getPostal").invoke(postalCell); } catch (Exception ignore) {}
	    }
	    Object areaCell = row.get(areaCol);
	    Object areaSelected = null;
	    if (areaCell != null) {
	        try { areaSelected = areaCell.getClass().getMethod("getSelectedArea").invoke(areaCell); } catch (Exception ignore) {}
	    }

	    // if the line is entirely blank, allow it (some programs don’t require the bottom summary)
	    boolean blank = (totalLearners == null && (postal == null || postal.isBlank()) && areaSelected == null);
	    if (blank) return true;

	    // otherwise, it must be valid and (optionally) coherent with the sum of trade rows
	    if (totalLearners == null || totalLearners <= 0) return false;
	    if (postal == null || postal.isBlank()) return false;
	    if (areaSelected == null) return false;

	    // Optional coherence check: sum of trade learners equals total
	    if (trade != null) {
	        ColumnInfo<?> learnersCol = findCol(trade, "No. of Learners", "No of Learners", "Learners");
	        if (learnersCol != null) {
	            int sum = 0;
	            for (var r : trade.getRows()) {
	                Integer n = getLearners(learnersCol.getDataType(), r.get(learnersCol));
	                if (n != null) sum += n;
	            }
	            if (sum > 0 && totalLearners != sum) {
	                return false; // enforce consistency; drop this if not required
	            }
	        }
	    }
	    return true;
	}


	
	public void nextTab(Tabbox tab) {
		int currentIndex = tab.getSelectedIndex();
		tab.setSelectedIndex(currentIndex + 1);
	}
	
	private boolean notEmpty(String s){ return s != null && !s.trim().isEmpty(); }

	public void prevTab(Tabbox tab) {
		int currentIndex = tab.getSelectedIndex();
		tab.setSelectedIndex(currentIndex - 1);
	}
	
	public boolean checkExistAppForm(String registrationNumber){
		String existAppFormWhere = String.format("%s = ? AND %s = ?", I_ZZ_Application_Form.COLUMNNAME_ZZ_Application_Form_ID, I_ZZ_Application_Form.COLUMNNAME_ZZ_Org_Reg_No);
		Query existAppFormQuery = MTable.get(I_ZZ_Application_Form.Table_ID).createQuery(existAppFormWhere, null);
		List<X_ZZ_Application_Form> exitsAppForms = existAppFormQuery.setOnlyActiveRecords(true)
				.setParameters(menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID(), registrationNumber)
				.list();
		if (exitsAppForms.size() > 0) {
			showDialog("Exist Application Form", 
					String.format("An application for %s already exists. It was created by User: %s at %s", registrationNumber, 
					exitsAppForms.get(0).getUserName(), 
					exitsAppForms.get(0).getCreated().toLocalDateTime().truncatedTo(ChronoUnit.SECONDS).format(dtf)));
			return true;
		}
		return false;
	}
	
	public boolean checkExistAppForm(X_C_BPartner bparter){
		String existAppFormWhere = String.format("%s = ? AND %s = ?", I_ZZ_Application_Form.COLUMNNAME_ZZ_Program_Master_Data_ID, I_ZZ_Application_Form.COLUMNNAME_C_BPartner_ID);
		Query existAppFormQuery = MTable.get(I_ZZ_Application_Form.Table_ID).createQuery(existAppFormWhere, null);
		List<X_ZZ_Application_Form> exitsAppForms = existAppFormQuery.setOnlyActiveRecords(true).setParameters(menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID(), bparter.getC_BPartner_ID()).list();
		if (exitsAppForms.size() > 0) {
			showDialog("Exist Application Form", 
					String.format("An application for %s already exists. It was created by User: %s at %s", 
							bparter.getName(), 
							exitsAppForms.get(0).getUserName(),
							exitsAppForms.get(0).getCreated().toLocalDateTime().truncatedTo(ChronoUnit.SECONDS).format(dtf)));
			return true;
		}
		return false;
	}
	
	
	public void saveAppForm(boolean isSave, String title, String msg) throws IOException {
		String trxName = null;
		
		if (applicationForm == null) {
			applicationForm = new X_ZZ_Application_Form(Env.getCtx(), 0, trxName);
			applicationForm.setZZProgramType(programType.toString());
		}
			
		
		applicationForm.setAD_Org_ID(menuContextInfo.getProgramMasterData().getAD_Org_ID());
		applicationForm.setZZ_Program_Master_Data_ID(menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID());
		
		if (isSave) {
			applicationForm.setZZ_DocStatus(X_ZZ_Application_Form.ZZ_DOCSTATUS_Draft);
		}else {
			applicationForm.setZZ_DocStatus(X_ZZ_Application_Form.ZZ_DOCSTATUS_Submitted);
		}
		
		saveAppFormCommonPart(applicationForm);
		
		if (employerDeclarationInfo != null) {
			employerDeclarationInfo.saveForm(trxName, applicationForm);
		}

		applicationForm.saveEx(trxName);// save here to get id when save address on org info
		
		if (organisationInfo != null){
			organisationInfo.saveForm(trxName, applicationForm);
		}
		
		if (programContact != null) {
			programContact.saveForm(trxName, applicationForm);
		}

		if (alternateProgramContact != null) {
			alternateProgramContact.saveForm(trxName, applicationForm);
		}
		
		if(uploadDoc != null) {
			uploadDoc.saveForm(trxName, applicationForm);
		}

		program.saveForm(trxName, applicationForm);
		
		if(!isSave) {
			applicationForm.setDateDoc(Timestamp.valueOf(LocalDateTime.now()));
		}
		
		applicationForm.saveEx();
		
		recordId = applicationForm.getZZ_Application_Form_ID();
		tableId = applicationForm.get_Table_ID();
		setDocumentNo(applicationForm.getDocumentNo());
		
		// sent email
		showSubmitedDialog(title, msg);
		if (!isSave)
			sentEmail();
	}
	
	private void saveAppFormCommonPart(X_ZZ_Application_Form applicationForm) {
 		MUser loginUser = MUser.get(Env.getAD_User_ID(Env.getCtx()));
		applicationForm.setName(loginUser.getName() + " - " + LocalDateTime.now().format(dtf));
 	}
	public void saveClose() throws IOException {
		saveAppForm(true, "Successfully save the application form",
				"The application form has been saved.");
	}

	public void sentEmail() {
		EmailPoInfo emailPoInfo = new EmailPoInfo();
		
		MMailText submitedEmail = new MMailText(Env.getCtx(), "bb8d6f79-4bea-448d-a55e-43f52116a03c", null);
		MClient client = MClient.get(Env.getCtx());
		MUser from = MUser.get(Env.getCtx(), FROM_EMAIL_USER_ID);
		submitedEmail.setPO(emailPoInfo);
		
		int loginId = Env.getAD_User_ID(Env.getCtx());
		MUser receiver = MUser.get(loginId);
		submitedEmail.setUser(receiver);
		if (!client.sendEMail(from, receiver, submitedEmail.getMailHeader(), submitedEmail.getMailText(), null, submitedEmail.isHtml())) {
			log.fine("Problem Sending Email.  Please contact Support");
		}
		
		if(applicationForm.getC_BPartner_ID() != 0) {
			MBPartner partner = MBPartner.get(Env.getCtx(), applicationForm.getC_BPartner_ID());
			MUser[] contacts = partner.getContacts(false);
			if (contacts.length > 0 && contacts[0].getAD_User_ID() != loginId) {
				receiver = contacts[0];
				submitedEmail.setUser(receiver);
				if (!client.sendEMail(from, receiver, submitedEmail.getMailHeader(), submitedEmail.getMailText(), null, submitedEmail.isHtml())) {
					log.fine("Problem Sending Email.  Please contact Support");
				}
			}
				
		}
		

	}

	/**
	 * @param alternateProgramContact the alternateProgramContact to set
	 */
	public void setAlternateProgramContact(AddressInfo alternateProgramContact) {
		this.alternateProgramContact = alternateProgramContact;
	}
	/**
	 * @param applicationForm the applicationForm to set
	 */
	public void setApplicationForm(X_ZZ_Application_Form applicationForm) {
		this.applicationForm = applicationForm;
	}
	
	/**
	 * @param documentNo the documentNo to set
	 */
	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
	}
	

	public void setEmployerDeclarationInfo(EmployerDeclarationInfo employerDeclarationInfo) {
		this.employerDeclarationInfo = employerDeclarationInfo;
		org.zkoss.bind.BindUtils.postNotifyChange(null, null, this, "declarationComplete");
	}

	/**
	 * @param formInfo the formInfo to set
	 */
	public void setFormInfo(FormInfo formInfo) {
		this.formInfo = formInfo;
	}

	/**
	 * @param programMasterDataID the programMasterDataID to set
	 */
	public void setMenuContextInfo(MenuContextInfo menuContextInfo) {
		this.menuContextInfo = menuContextInfo;
	}


	/**
	 * @param organisationInfo the organisationInfo to set
	 */
	public void setOrganisationInfo(OrganisationInfo organisationInfo) {
		this.organisationInfo = organisationInfo;
		org.zkoss.bind.BindUtils.postNotifyChange(null, null, this, "organisationComplete");
	}

	/**
	 * @param program the program to set
	 */
	public void setProgram(IProgram program) {
		this.program = program;
		org.zkoss.bind.BindUtils.postNotifyChange(null, null, this, "programComplete");
	}

	/**
	 * @param programContact the programContact to set
	 */
	public void setProgramContact(AddressInfo programContact) {
		this.programContact = programContact;
		org.zkoss.bind.BindUtils.postNotifyChange(null, null, this, "programContactComplete");
	}

	public void setProgramType(ProgramType programType) {
		this.programType = programType;
		org.zkoss.bind.BindUtils.postNotifyChange(null, null, this, "organisationComplete");
	    org.zkoss.bind.BindUtils.postNotifyChange(null, null, this, "programComplete");
	    org.zkoss.bind.BindUtils.postNotifyChange(null, null, this, "programContactComplete");
	}


	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}
	
	public void setTableId(int tableId) {
		this.tableId = tableId;
	}

		/**
		 * @param uploadDoc the uploadDoc to set
		 */
		public void setUploadDoc(UploadDocComponent uploadDoc) {
			this.uploadDoc = uploadDoc;
		}

		protected void showDialog(String title, String msg) {
			Dialog dialog = new Dialog(title, msg);
			showDialog(dialog);
		}
		
		protected void showSubmitedDialog(String title, String msg) {
			Dialog dialog = new Dialog(title, msg, tableId, recordId, applicationForm.getDocumentNo());
			showDialog(dialog);
		}
		
		protected void showDialog(Dialog dialog) {
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			// Set the context class loader to this bundle's class loader to ensure that
			// classes provided by the bundle (e.g., za.co.ntier.webform.form.viewmodel.*)
			// used in ZUL files can be found.
			Map<String, Object> args = new HashMap<>();
			args.put(ComponentVMWrapper.ComponentKey, dialog);
			String zulPathRelative = WebForm.getBundleResourcePath("component/dialog.zul");				
			Executions.createComponents(zulPathRelative, null, args);
		}
		public void submitApplication() throws IOException {
			saveAppForm(false, "Successfully submitted the application form",
					"The application form has been submitted.");

		}


}