package za.co.ntier.webform.form.bean.program;

import java.util.Map;

import za.co.ntier.api.model.X_ZZ_Application_Form;
import za.co.ntier.api.model.X_ZZ_FormDiscipline;
import za.co.ntier.webform.form.AbstractProgram;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.AreaData;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.IntData;
import za.co.ntier.webform.form.bean.component.PostalData;
import za.co.ntier.webform.form.bean.component.ProgramInput;

public class AetProgram extends AbstractProgram {
	private ProgramInput aetLearnership;

	public AetProgram(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm) {
		super(menuContextInfo, applicationForm);
		aetLearnership = ProgramInput.getLearnership(X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_AETLearnership,
				applicationForm,
				menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID()
				);
		
	}

	/**
	 * @return the aetLearnership
	 */
	public ProgramInput getAetLearnership() {
		return aetLearnership;
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm)  {
		super.saveForm(applicationForm);
		aetLearnership.save(trxName, applicationForm);
		
	}

	/**
	 * @param aetLearnership the aetLearnership to set
	 */
	public void setAetLearnership(ProgramInput aetLearnership) {
		this.aetLearnership = aetLearnership;
	}

	@Override
    public boolean isProgramValid() {
        return hasAtLeastOneValidAetRow(aetLearnership);
    }

    private static boolean hasAtLeastOneValidAetRow(ProgramInput table) {
        if (table == null || table.getRows() == null) return false;

        ColumnInfo<?> cEmp    = AnnexureInfo.lookupColByTitle(ColumnInfo.colNoEmployedLabel,    table);
        ColumnInfo<?> cUnemp  = AnnexureInfo.lookupColByTitle(ColumnInfo.colNoUnEmployedLabel,  table);
        ColumnInfo<?> cPostal = AnnexureInfo.lookupColByTitle(ColumnInfo.colPostalCodeLabel,    table);
        ColumnInfo<?> cArea   = AnnexureInfo.lookupColByTitle(ColumnInfo.colAreaLabel,          table);

        for (Map<ColumnInfo<?>, Object> row : table.getRows()) {
            int emp   = (cEmp   != null && row.get(cEmp)   instanceof IntData)
                        ? intVal((IntData) row.get(cEmp))   : 0;
            int unemp = (cUnemp != null && row.get(cUnemp) instanceof IntData)
                        ? intVal((IntData) row.get(cUnemp)) : 0;

            boolean postalOk = (cPostal != null && row.get(cPostal) instanceof PostalData)
                    && notEmpty(((PostalData) row.get(cPostal)).getPostal());
            boolean areaOk   = (cArea   != null && row.get(cArea)   instanceof AreaData)
                    && ((AreaData) row.get(cArea)).getSelectedArea() != null;

            if ((emp + unemp) > 0 && postalOk && areaOk) {
                return true;
            }
        }
        return false;
    }

    private static int  intVal(IntData d){ return d.getValue()==null ? 0 : d.getValue(); }
    private static boolean notEmpty(String s){ return s!=null && !s.trim().isEmpty(); }

}
