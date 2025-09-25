package za.co.ntier.webform.form.bean.program;
import java.util.Map;

import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.Util;
import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.AreaData;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.IntData;
import za.co.ntier.webform.form.bean.component.PostalData;
import za.co.ntier.webform.form.bean.component.ProjectInput;
import za.co.ntier.api.model.X_ZZ_Application_Form;

public class CentreOfSpecialisationProgram extends ArtisanDevProgram {
	private Boolean isCollegeRecognised= null;

	private Boolean isCollegeRegistered = null;

	private Boolean isCollegeSla = null;

	public CentreOfSpecialisationProgram(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm)  {
		super(menuContextInfo, applicationForm);
		initComponent(applicationForm);
	}

	/**
	 * @return the isCollegeRecognised
	 */
	public String getCollegeRecognised() {
		return Util.convert(this.isCollegeRecognised);
	}

	/**
	 * @return the isCollegeRegistered
	 */
	public String getCollegeRegistered() {
		return Util.convert(this.isCollegeRegistered);
	}

	/**
	 * @return the isCollegeSla
	 */
	public String getCollegeSla() {
		return Util.convert(isCollegeSla);
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		super.saveForm(trxName, applicationForm);
		applicationForm.setZZCollegeRecognised(Util.convert(isCollegeRecognised));
		applicationForm.setZZCollegeRegistered(Util.convert(isCollegeRegistered));
		applicationForm.setZZCollegeSla(Util.convert(isCollegeSla));
	}

	public void initComponent(X_ZZ_Application_Form applicationForm) {
		if (applicationForm != null) {
			isCollegeRecognised = Util.convert(applicationForm.getZZCollegeRecognised());
			isCollegeRegistered = Util.convert(applicationForm.getZZCollegeRegistered());
			isCollegeSla = Util.convert(applicationForm.getZZCollegeSla());
		}
	}
	/**
	 * @param isCollegeRecognised the isCollegeRecognised to set
	 */
	public void setCollegeRecognised(String isCollegeRecognised) {
		this.isCollegeRecognised = Util.convert(isCollegeRecognised);
	}

	/**
	 * @param isCollegeRegistered the isCollegeRegistered to set
	 */
	public void setCollegeRegistered(String isCollegeRegistered) {
		this.isCollegeRegistered = Util.convert(isCollegeRegistered);
	}

	/**
	 * @param isCollegeSla the isCollegeSla to set
	 */
	public void setCollegeSla(String isCollegeSla) {
		this.isCollegeSla = Util.convert(isCollegeSla);
	}

	@Override
    public boolean isProgramValid() {
		 // 1) all 3 YES/NO questions must be answered
		boolean radiosOk = isCollegeRecognised != null
		                        && isCollegeRegistered != null
		                        && isCollegeSla != null;
		
		        // 2) at least one valid line in either trade or total table
		        boolean tablesOk = hasAtLeastOneValidProgramRow(getTrade());
		
		        return radiosOk && tablesOk;
    }

    // ---- helpers (same pattern used in your other programs) ----
    private static boolean hasAtLeastOneValidProgramRow(AnnexureInfo table) {
        if (table == null || table.getRows() == null) return false;

        ColumnInfo<?> cNoLearners   = AnnexureInfo.lookupColByTitle(ColumnInfo.colNoLearnersLabel,   table);
        ColumnInfo<?> cNoEmployed   = AnnexureInfo.lookupColByTitle(ColumnInfo.colNoEmployedLabel,   table);
        ColumnInfo<?> cNoUnemployed = AnnexureInfo.lookupColByTitle(ColumnInfo.colNoUnEmployedLabel, table);
        ColumnInfo<?> cPostal       = AnnexureInfo.lookupColByTitle(ColumnInfo.colPostalCodeLabel,   table);
        ColumnInfo<?> cArea         = AnnexureInfo.lookupColByTitle(ColumnInfo.colAreaLabel,         table);

        for (Map<ColumnInfo<?>, Object> row : table.getRows()) {
            int n = 0;
            if (cNoLearners   != null && row.get(cNoLearners)   instanceof IntData) n += val((IntData) row.get(cNoLearners));
            if (cNoEmployed   != null && row.get(cNoEmployed)   instanceof IntData) n += val((IntData) row.get(cNoEmployed));
            if (cNoUnemployed != null && row.get(cNoUnemployed) instanceof IntData) n += val((IntData) row.get(cNoUnemployed));

            boolean postalOk = (cPostal != null && row.get(cPostal) instanceof PostalData)
                    && notEmpty(((PostalData) row.get(cPostal)).getPostal());
            boolean areaOk   = (cArea   != null && row.get(cArea)   instanceof AreaData)
                    && ((AreaData) row.get(cArea)).getSelectedArea() != null;

            if (n > 0 && postalOk && areaOk) return true;
        }
        return false;
    }

    private static boolean isProjectTableValid(ProjectInput table) {
        if (table == null || table.getRows() == null || table.getRows().isEmpty()) return false;

        Map<ColumnInfo<?>, Object> row = table.getRows().get(0);
        ColumnInfo<?> cTotal  = AnnexureInfo.lookupColByTitle(ColumnInfo.colTotalLearnersLabel, table);
        ColumnInfo<?> cPostal = AnnexureInfo.lookupColByTitle(ColumnInfo.colPostalCodeLabel,    table);
        ColumnInfo<?> cArea   = AnnexureInfo.lookupColByTitle(ColumnInfo.colAreaLabel,          table);

        int n = (cTotal != null && row.get(cTotal) instanceof IntData) ? val((IntData) row.get(cTotal)) : 0;
        boolean postalOk = (cPostal != null && row.get(cPostal) instanceof PostalData)
                && notEmpty(((PostalData) row.get(cPostal)).getPostal());
        boolean areaOk   = (cArea   != null && row.get(cArea)   instanceof AreaData)
                && ((AreaData) row.get(cArea)).getSelectedArea() != null;

        return n > 0 && postalOk && areaOk;
    }

    private static int  val(IntData d){ return d.getValue()==null ? 0 : d.getValue(); }
    private static boolean notEmpty(String s){ return s!=null && !s.trim().isEmpty(); }

}
