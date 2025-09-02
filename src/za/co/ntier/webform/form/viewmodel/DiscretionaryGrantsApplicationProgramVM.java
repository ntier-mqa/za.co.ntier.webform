package za.co.ntier.webform.form.viewmodel;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.compiere.model.MUser;
import org.compiere.util.Env;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Tabbox;

import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.form.bean.AddressInfo;
import za.co.ntier.webform.form.bean.Dialog;
import za.co.ntier.webform.form.bean.EmployerDeclarationInfo;
import za.co.ntier.webform.form.bean.FormInfo;
import za.co.ntier.webform.form.bean.OrganisationInfo;
import za.co.ntier.webform.form.bean.ProgramType;
import za.co.ntier.webform.form.bean.UploadDocComponent;
import za.co.ntier.webform.form.bean.program.AetProgram;
import za.co.ntier.webform.form.bean.program.ArtisanAidesProgram;
import za.co.ntier.webform.form.bean.program.ArtisanDevProgram;
import za.co.ntier.webform.form.bean.program.ArtisanRPLProgram;
import za.co.ntier.webform.form.bean.program.CandidacyProgram;
import za.co.ntier.webform.form.bean.program.CentreOfSpecialisationProgram;
import za.co.ntier.webform.form.bean.program.InhouseTrainingProgram;
import za.co.ntier.webform.form.bean.program.InternshipProgram;
import za.co.ntier.webform.form.bean.program.MedpProgram;
import za.co.ntier.webform.form.bean.program.NcvGraduatesProgram;
import za.co.ntier.webform.form.bean.program.NonArtisanDevProgram;
import za.co.ntier.webform.form.bean.program.NonArtisanDevRPLProgram;
import za.co.ntier.webform.form.bean.program.OhasspProgram;
import za.co.ntier.webform.form.bean.program.ProgramCetTvetInfo;
import za.co.ntier.webform.form.bean.program.WorkExperienceProgram;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class DiscretionaryGrantsApplicationProgramVM {
	private InternshipProgram internship;
	private CandidacyProgram candidacy;
	private WorkExperienceProgram workExperience;
	private MedpProgram medp;

	private ArtisanAidesProgram artisanAides;
	private ArtisanDevProgram artisanDev;
	private CentreOfSpecialisationProgram centreOfSpecialisation;
	private ArtisanRPLProgram artisanRPL;
	private NonArtisanDevProgram nonArtisanDev;
	private NonArtisanDevRPLProgram nonArtisanDevRPL;
	private NcvGraduatesProgram ncvGraduates;
	private AetProgram aet;
	private OhasspProgram ohassp;
	private InhouseTrainingProgram inhouseTraining;

	private ProgramCetTvetInfo programCetTvetInfo;

	private OrganisationInfo organisationInfo;

	private UploadDocComponent uploadDoc;

	private FormInfo formInfo;

	private MenuContextInfo menuContextInfo;

	private int recordId;

	private int tableId;

	private EmployerDeclarationInfo employerDeclarationInfo;

	private AddressInfo alternateProgramContact;
	private AddressInfo programContact;

	// --- Getters and Setters for Data Binding ---

	private ProgramType programType;

	@Command
	public void cancelApp() {

	}

	/*
	 * public List<X_ZZ_FormDiscipline> createDiscipline(LearnerInputTableInfo
	 * disciplineTableInfo, int applicationFormID) throws IOException { for
	 * (LearnerInputInfo discipline : disciplineTableInfo.getLearnerInputInfos()) {
	 * if (discipline.getNoLearners() == null) continue;
	 * 
	 * X_ZZ_FormDiscipline formDisciplines = new X_ZZ_FormDiscipline(Env.getCtx(),
	 * 0, null); formDisciplines.setZZ_Application_Form_ID(applicationFormID);
	 * formDisciplines.setZZ_LearnersNo(discipline.getNoLearners());
	 * 
	 * if (discipline.getAreaSelected() != null)
	 * formDisciplines.setC_City_ID(discipline.getAreaSelected().getC_City_ID());
	 * 
	 * if (discipline.getProvince() != null)
	 * formDisciplines.setC_Region_ID(discipline.getProvince().getC_Region_ID());
	 * 
	 * formDisciplines.setPostal(discipline.getPostalCode());
	 * formDisciplines.setZZ_DisciplineType(disciplineTableInfo.getLearnerInputType(
	 * )); if (disciplineTableInfo.isTrade()) {
	 * formDisciplines.setZZ_Trade_ID(discipline.getLearnerInputID()); }else {
	 * formDisciplines.setZZ_Disciplines_ID(discipline.getLearnerInputID()); }
	 * 
	 * if (StringUtils.isNoneEmpty(discipline.getFullPathWPA())) {
	 * formDisciplines.setZZ_WPAFile(Files.readAllBytes(Paths.get(discipline.
	 * getFullPathWPA()))); }
	 * 
	 * if (StringUtils.isNoneEmpty(discipline.getFullPathAccred())) {
	 * formDisciplines.setZZ_AccredFile((Files.readAllBytes(Paths.get(discipline.
	 * getFullPathAccred())))); }
	 * 
	 * formDisciplines.saveEx(); }
	 * 
	 * return null; }
	 */

	/**
	 * @return the aet
	 */
	public AetProgram getAet() {
		return aet;
	}

	/**
	 * @return the alternateProgramContact
	 */
	public AddressInfo getAlternateProgramContact() {
		return alternateProgramContact;
	}

	/**
	 * @return the artisanAides
	 */
	public ArtisanAidesProgram getArtisanAides() {
		return artisanAides;
	}

	/**
	 * @return the artisanDev
	 */
	public ArtisanDevProgram getArtisanDev() {
		return artisanDev;
	}

	/**
	 * @return the artisanRPL
	 */
	public ArtisanRPLProgram getArtisanRPL() {
		return artisanRPL;
	}

	/**
	 * @return the candidacy
	 */
	public CandidacyProgram getCandidacy() {
		return candidacy;
	}

	/**
	 * @return the centreOfSpecialisation
	 */
	public CentreOfSpecialisationProgram getCentreOfSpecialisation() {
		return centreOfSpecialisation;
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

	/**
	 * @return the inhouseTraining
	 */
	public InhouseTrainingProgram getInhouseTraining() {
		return inhouseTraining;
	}

	/**
	 * @return the internship
	 */
	public InternshipProgram getInternship() {
		return internship;
	}

	/**
	 * @return the medp
	 */
	public MedpProgram getMedp() {
		return medp;
	}

	/**
	 * @return the programMasterDataID
	 */
	public MenuContextInfo getMenuContextInfo() {
		return menuContextInfo;
	}

	/**
	 * @return the ncvGraduates
	 */
	public NcvGraduatesProgram getNcvGraduates() {
		return ncvGraduates;
	}

	/**
	 * @return the nonArtisanDev
	 */
	public NonArtisanDevProgram getNonArtisanDev() {
		return nonArtisanDev;
	}

	/**
	 * @return the nonArtisanDevRPL
	 */
	public NonArtisanDevRPLProgram getNonArtisanDevRPL() {
		return nonArtisanDevRPL;
	}

	/**
	 * @return the ohassp
	 */
	public OhasspProgram getOhassp() {
		return ohassp;
	}

	/**
	 * @return the organisationInfo
	 */
	public OrganisationInfo getOrganisationInfo() {
		return organisationInfo;
	}

	public ProgramCetTvetInfo getProgramCetTvetInfo() {
		return programCetTvetInfo;
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

	/**
	 * @return the workExperience
	 */
	public WorkExperienceProgram getWorkExperience() {
		return workExperience;
	}

	@Init
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo)
			throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {

		setMenuContextInfo(menuContextInfo);
		programType = menuContextInfo.getProgramType();
		setFormInfo(new FormInfo(menuContextInfo));

		organisationInfo = new OrganisationInfo(menuContextInfo);

		if (programType.isCetTvet()) {
			setProgramCetTvetInfo(new ProgramCetTvetInfo(menuContextInfo));
		} else if (ProgramType.INTERNSHIP == programType) {
			internship = new InternshipProgram(menuContextInfo);
		} else if (ProgramType.CANDIDACY == programType) {
			setCandidacy(new CandidacyProgram(menuContextInfo));
		} else if (ProgramType.EXPERIENCE == programType) {
			setWorkExperience(new WorkExperienceProgram(menuContextInfo));
		} else if (ProgramType.DEV_PROGRAM == programType) {
			setMedp(new MedpProgram());
		} else if (ProgramType.ARTISAN_AIDES == programType) {
			setArtisanAides(new ArtisanAidesProgram());
		} else if (ProgramType.ARTISAN_DEV == programType) {
			setArtisanDev(new ArtisanDevProgram(menuContextInfo));
		} else if (ProgramType.CENTRE_SPECIALISATION == programType) {
			setCentreOfSpecialisation(new CentreOfSpecialisationProgram(menuContextInfo));
		} else if (ProgramType.ARTISAN_RPL == programType) {
			setArtisanRPL(new ArtisanRPLProgram());
		} else if (ProgramType.NON_ARTISAN_DEV == programType) {
			setNonArtisanDev(new NonArtisanDevProgram(menuContextInfo));
		} else if (ProgramType.NON_ARTISAN_DEV_RPL == programType) {
			setNonArtisanDevRPL(new NonArtisanDevRPLProgram(menuContextInfo));
		} else if (ProgramType.NCV_GRADUATES == programType) {
			setNcvGraduates(new NcvGraduatesProgram(menuContextInfo));
		} else if (ProgramType.AET == programType) {
			setAet(new AetProgram(menuContextInfo));
		} else if (ProgramType.OHASSP == programType) {
			setOhassp(new OhasspProgram());
		} else if (ProgramType.INHOUSE_TRAINING == programType) {
			setInhouseTraining(new InhouseTrainingProgram());
		}

		employerDeclarationInfo = new EmployerDeclarationInfo();

		// main contact
		if (programType.isShowMainAddress())
			programContact = new AddressInfo(programType, false, null);

		// main alternate contact
		if (programType.isShowMainAddressAlter())
			alternateProgramContact = new AddressInfo(programType, true, null);

		uploadDoc = new UploadDocComponent(menuContextInfo);

	}

	public boolean isShowUploadDoc() {
		return uploadDoc != null && uploadDoc.getUploadDoc().getRows().size() > 0;
	}

	@Command
	public void nextTab(@BindingParam("tab") Tabbox tab) {
		int currentIndex = tab.getSelectedIndex();
		tab.setSelectedIndex(currentIndex + 1);
	}

	@Command
	public void prevTab(@BindingParam("tab") Tabbox tab) {
		int currentIndex = tab.getSelectedIndex();
		tab.setSelectedIndex(currentIndex - 1);
	}

	/**
	 * @param aet the aet to set
	 */
	public void setAet(AetProgram aet) {
		this.aet = aet;
	}

	/**
	 * @param alternateProgramContact the alternateProgramContact to set
	 */
	public void setAlternateProgramContact(AddressInfo alternateProgramContact) {
		this.alternateProgramContact = alternateProgramContact;
	}

	/**
	 * @param artisanAides the artisanAides to set
	 */
	public void setArtisanAides(ArtisanAidesProgram artisanAidesProgram) {
		this.artisanAides = artisanAidesProgram;
	}

	/**
	 * @param artisanDev the artisanDev to set
	 */
	public void setArtisanDev(ArtisanDevProgram artisanDev) {
		this.artisanDev = artisanDev;
	}

	/**
	 * @param artisanRPL the artisanRPL to set
	 */
	public void setArtisanRPL(ArtisanRPLProgram artisanRPL) {
		this.artisanRPL = artisanRPL;
	}

	/**
	 * @param candidacy the candidacy to set
	 */
	public void setCandidacy(CandidacyProgram candidacy) {
		this.candidacy = candidacy;
	}

	/**
	 * @param centreOfSpecialisation the centreOfSpecialisation to set
	 */
	public void setCentreOfSpecialisation(CentreOfSpecialisationProgram centreOfSpecialisation) {
		this.centreOfSpecialisation = centreOfSpecialisation;
	}

	public void setEmployerDeclarationInfo(EmployerDeclarationInfo employerDeclarationInfo) {
		this.employerDeclarationInfo = employerDeclarationInfo;
	}

	/**
	 * @param formInfo the formInfo to set
	 */
	public void setFormInfo(FormInfo formInfo) {
		this.formInfo = formInfo;
	}

	/**
	 * @param inhouseTraining the inhouseTraining to set
	 */
	public void setInhouseTraining(InhouseTrainingProgram inhouseTraining) {
		this.inhouseTraining = inhouseTraining;
	}

	/**
	 * @param internship the internship to set
	 */
	public void setInternship(InternshipProgram internship) {
		this.internship = internship;
	}

	/**
	 * @param medp the medp to set
	 */
	public void setMedp(MedpProgram medp) {
		this.medp = medp;
	}

	/**
	 * @param programMasterDataID the programMasterDataID to set
	 */
	public void setMenuContextInfo(MenuContextInfo menuContextInfo) {
		this.menuContextInfo = menuContextInfo;
	}

	/**
	 * @param ncvGraduates the ncvGraduates to set
	 */
	public void setNcvGraduates(NcvGraduatesProgram ncvGraduates) {
		this.ncvGraduates = ncvGraduates;
	}

	/**
	 * @param nonArtisanDev the nonArtisanDev to set
	 */
	public void setNonArtisanDev(NonArtisanDevProgram nonArtisanDev) {
		this.nonArtisanDev = nonArtisanDev;
	}

	/**
	 * @param nonArtisanDevRPL the nonArtisanDevRPL to set
	 */
	public void setNonArtisanDevRPL(NonArtisanDevRPLProgram nonArtisanDevRPL) {
		this.nonArtisanDevRPL = nonArtisanDevRPL;
	}

	/**
	 * @param ohassp the ohassp to set
	 */
	public void setOhassp(OhasspProgram ohassp) {
		this.ohassp = ohassp;
	}

	/**
	 * @param organisationInfo the organisationInfo to set
	 */
	public void setOrganisationInfo(OrganisationInfo organisationInfo) {
		this.organisationInfo = organisationInfo;
	}

	public void setProgramCetTvetInfo(ProgramCetTvetInfo programCetTvetInfo) {
		this.programCetTvetInfo = programCetTvetInfo;
	}

	/**
	 * @param programContact the programContact to set
	 */
	public void setProgramContact(AddressInfo programContact) {
		this.programContact = programContact;
	}

	public void setProgramType(ProgramType programType) {
		this.programType = programType;
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

	/**
	 * @param workExperience the workExperience to set
	 */
	public void setWorkExperience(WorkExperienceProgram workExperience) {
		this.workExperience = workExperience;
	}

 	private void saveAppFormCommonPart(X_ZZ_Application_Form applicationForm) {
 		MUser loginUser = MUser.get(Env.getAD_User_ID(Env.getCtx()));
		DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
		applicationForm.setName(loginUser.getName() + " - " + LocalDateTime.now().format(dtf));
 	}
	 	
	@Command(value = "submitApplication")
	public void submitApplication() throws IOException {
		String trxName = null;
		X_ZZ_Application_Form applicationForm = new X_ZZ_Application_Form(Env.getCtx(), 0, trxName);
		
		saveAppFormCommonPart(applicationForm);
		
		applicationForm.saveEx();
		
		if (employerDeclarationInfo != null) {
			employerDeclarationInfo.saveForm(trxName, applicationForm);
		}

		if (organisationInfo != null){
			organisationInfo.saveForm(trxName, applicationForm);
		}
		
		applicationForm.saveEx();
		
		if (programContact != null) {
			programContact.saveForm(trxName, applicationForm);
		}

		if (alternateProgramContact != null) {
			alternateProgramContact.saveForm(trxName, applicationForm);
		}

		recordId = applicationForm.getZZ_Application_Form_ID();
		tableId = applicationForm.get_Table_ID();
		
		
		
		showDialog();

	}
	
	protected void showDialog() {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		// Set the context class loader to this bundle's class loader to ensure that
		// classes provided by the bundle (e.g., za.co.ntier.webform.form.viewmodel.*)
		// used in ZUL files can be found.
		String zulPathRelative = WebForm.getBundleResourcePath("component/dialog.zul");
		Map<String, Object> args = new HashMap<>();
		args.put(ComponentVMWrapper.ComponentKey, new Dialog("Successfully submitted the application form", tableId, recordId, true));
		
		Thread.currentThread().setContextClassLoader(WebForm.class.getClassLoader());
		try {
			
			Executions.createComponents(zulPathRelative, null, args);
		} finally {
			Thread.currentThread().setContextClassLoader(cl);
		}
	}
}