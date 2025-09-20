package za.co.ntier.webform.form.bean.program;

import java.util.Map;

import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.Util;
import za.co.ntier.webform.form.bean.AddressType;
import za.co.ntier.webform.form.bean.DataType;
import za.co.ntier.webform.form.bean.component.AddressInfo;
import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.AreaData;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.PostalData;
import za.co.ntier.webform.form.bean.component.ProgramInput;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class WorkExperienceProgram implements ISaveForm, IProgram {

	private X_ZZ_Application_Form applicationForm;
	private ProgramInput disciplines;
	private Integer noOfLearners;

	private AddressInfo vacationContact;

	public WorkExperienceProgram(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm) {
		setVacationContact(new AddressInfo(AddressType.VACATION));
		this.setDisciplines(ProgramInput.getDisciplines(
				menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID(),
				applicationForm,
				"""
										List of disciplines supported for practical training which the number of learners applying
						should be based on
										"""));
		initComponent(applicationForm);
	}

	/**
	 * @return the applicationForm
	 */
	public X_ZZ_Application_Form getApplicationForm() {
		return applicationForm;
	}

	/**
	 * @return the disciplines
	 */
	public ProgramInput getDisciplines() {
		return disciplines;
	}

	public Integer getNoOfLearners() {
		return noOfLearners;
	}
	
	/**
	 * @return the vacationContact
	 */
	public AddressInfo getVacationContact() {
		return vacationContact;
	}
	public void initComponent(X_ZZ_Application_Form applicationForm) {
		this.setApplicationForm(applicationForm);
		if (vacationContact != null) {
			vacationContact.initComponent(applicationForm);
		}
		
		if (applicationForm != null) {
			noOfLearners = Util.convert(applicationForm.getZZTotalNumberApplied());
		}
	}
	
	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm)  {
		this.setApplicationForm(applicationForm);
		vacationContact.saveForm(trxName, applicationForm);
		disciplines.save(trxName, applicationForm);
		applicationForm.setZZTotalNumberApplied(Util.convert(noOfLearners));
		applicationForm.saveEx(trxName);
	}

	/**
	 * @param applicationForm the applicationForm to set
	 */
	public void setApplicationForm(X_ZZ_Application_Form applicationForm) {
		this.applicationForm = applicationForm;
	}

	/**
	 * @param disciplines the disciplines to set
	 */
	public void setDisciplines(ProgramInput disciplines) {
		this.disciplines = disciplines;
	}

	public void setNoOfLearners(Integer noOfLearners) {
		this.noOfLearners = noOfLearners;
	}

	/**
	 * @param vacationContact the vacationContact to set
	 */
	public void setVacationContact(AddressInfo vacationContact) {
		this.vacationContact = vacationContact;
	}

	@Override
	public boolean isProgramValid() {
	    return (isVacationContactComplete(vacationContact)
	        && noOfLearners != null && noOfLearners > 0)
	        || hasAtLeastOneValidDisciplineLine(disciplines);
	}

	private boolean isVacationContactComplete(AddressInfo a) {
	    if (a == null) return false;
	    // Treat every *visible* field as required
	    boolean siteOk   = !a.showSiteName()      || notEmpty(a.getSiteName());
	    boolean addrOk   = !a.showLineAddress()   || notEmpty(a.getAddressLine());
	    boolean postalOk = !a.showGeographicAddress() || notEmpty(a.getPostalCode());
	    boolean areaOk   = !a.showGeographicAddress() || (a.getAreaSelected() != null);
	    boolean provOk   = !a.showGeographicAddress() || (a.getProvinceSelected() != null);

	    boolean contactOk = !a.showContact() || (
	            notEmpty(a.getNameSiteRepresentative()) &&
	            notEmpty(a.getRepresentativeDesignation()) &&
	            notEmpty(a.getMobileNumber()) &&
	            notEmpty(a.getLandlineNumber()) &&
	            notEmpty(a.getEmail())
	    );

	    return siteOk && addrOk && postalOk && areaOk && provOk && contactOk;
	}

	private boolean hasAtLeastOneValidDisciplineLine(ProgramInput pi) {
	    if (pi == null) return false;

	    // Resolve the three columns we care about
	    ColumnInfo<?> colNo  = AnnexureInfo.lookupColByTitle(ColumnInfo.colNoLearnersLabel, pi);
	    ColumnInfo<?> colPC  = AnnexureInfo.lookupColByTitle(ColumnInfo.colPostalCodeLabel, pi);
	    ColumnInfo<?> colArea= AnnexureInfo.lookupColByDataType(DataType.Area, pi);

	    if (colNo == null || colPC == null || colArea == null) return false;

	    for (Map<ColumnInfo<?>, Object> row : pi.getRows()) {
	        Integer n = AnnexureInfo.getIntegerValue(row, colNo);
	        PostalData pc = (PostalData) row.get(colPC);
	        AreaData area = (AreaData) row.get(colArea);

	        boolean complete = n != null && n > 0
	                && pc != null && notEmpty(pc.getPostal())
	                && area != null && area.getSelectedArea() != null;

	        if (complete) return true; // at least one valid line
	    }
	    return false;
	}

	// tiny util
	private static boolean notEmpty(String s){ return s != null && !s.trim().isEmpty(); }


}
