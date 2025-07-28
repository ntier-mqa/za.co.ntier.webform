package za.co.ntier.webform.form.viewmodel;

import java.util.ArrayList;
import java.util.List;

import org.compiere.model.Query;
import org.compiere.util.Env;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.media.Media;

import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.form.bean.DisciplineHDSA;
import za.co.ntier.webform.model.I_ZZ_Disciplines;
import za.co.ntier.webform.model.I_ZZ_Program_Disciplines;
import za.co.ntier.webform.model.I_ZZ_Program_Master_Data;
import za.co.ntier.webform.model.X_ZZ_Program_Disciplines;

public class DisciplineHDSAVMWrapper {
	private List<DisciplineHDSA> disciplineHDSAs;

	/**
	 * @return the disciplineHDSAs
	 */
	public List<DisciplineHDSA> getDisciplineHDSAs() {
		return disciplineHDSAs;
	}

	@Init
	public void init(@ExecutionArgParam(WebForm.programMasterDataIDKey) int programMasterDataID) {
		List<X_ZZ_Program_Disciplines> programDisciplines = new Query(Env.getCtx(), I_ZZ_Program_Disciplines.Table_Name, 
        		String.format("%s = ?", I_ZZ_Program_Master_Data.COLUMNNAME_ZZ_Program_Master_Data_ID), null)
			.setParameters(programMasterDataID)
			.setClient_ID()
			.list();
        
		List<DisciplineHDSA> disciplineHDSAs = new ArrayList<>();
        
        programDisciplines.stream().forEach((programDiscipline) -> {
        	I_ZZ_Disciplines discipline = programDiscipline.getZZ_Disciplines();
        	DisciplineHDSA disciplineHDSA = new DisciplineHDSA(discipline.getName(), programDiscipline.isZZ_WPA_Req(), programDiscipline.isZZ_Is_Accred_SLA_Req());
        	disciplineHDSAs.add(disciplineHDSA);
        });
        
        setDisciplineHDSAs(disciplineHDSAs);
	}

	@Command({ "postalCodeLookup" })
	public void postalCodeLookup(@BindingParam("discipline") DisciplineHDSA discipline,
			@BindingParam("sitePostalCode") String sitePostalCode) {

	}

	/**
	 * @param disciplineHDSAs the disciplineHDSAs to set
	 */
	public void setDisciplineHDSAs(List<DisciplineHDSA> disciplineHDSAs) {
		this.disciplineHDSAs = disciplineHDSAs;
	}

	@Command
	public void uploadFile(@BindingParam("media") Media media, @BindingParam("isDSA") boolean isDSA,
			@BindingParam("discipline") DisciplineHDSA discipline, @BindingParam("index") int index) {
		discipline.uploadFile(media, isDSA);
		
	}
}
