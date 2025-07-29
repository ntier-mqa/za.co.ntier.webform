package za.co.ntier.webform.form.viewmodel;

import java.util.ArrayList;
import java.util.List;

import org.compiere.model.Query;
import org.compiere.util.Env;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.Media;

import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.form.bean.DisciplineHDSA;
import za.co.ntier.webform.model.I_ZZ_Disciplines;
import za.co.ntier.webform.model.I_ZZ_Program_Disciplines;
import za.co.ntier.webform.model.I_ZZ_Program_Master_Data;
import za.co.ntier.webform.model.X_ZZ_Program_Disciplines;

public class DisciplineHDSAVMWrapper {
	private List<DisciplineHDSA> disciplineHDSAs;
	private boolean hasWPAReq = false;
	private boolean hasAccreditation = false;
	
	private List<String> colHeaders;
	private List<Integer> colSizes;
	
	private int totalLearners = 0;
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
        	
        	if (disciplineHDSA.isUploadWPA()) {
				hasWPAReq = true;
			}
        	
        	if (disciplineHDSA.isUploadAccreditation()) {
        		hasAccreditation = true;
        	}
        });
        
        setDisciplineHDSAs(disciplineHDSAs);
        
        if (hasWPAReq && hasAccreditation) {
        	colHeaders = List.of("Descipline", "No. of Learners", "Site Postal Code",
        			"Site Province", "Site Rural/Urban", "Attach WPA", "Attach Accreditation");
        	colSizes = List.of(3, 1, 1, 2, 1, 2, 2);
        }else if (hasWPAReq) {
			colHeaders = List.of("Descipline", "No. of Learners", "Site Postal Code",
					"Site Province", "Site Rural/Urban", "Attach WPA");
			colSizes = List.of(3, 1, 1, 2, 1, 4);
		} else if (hasAccreditation) {
			colHeaders = List.of("Descipline", "No. of Learners", "Site Postal Code",
					"Site Province", "Site Rural/Urban", "Attach Accreditation");
			colSizes = List.of(3, 1, 1, 2, 1, 4);
		} else {
			colHeaders = List.of("Descipline", "No. of Learners", "Site Postal Code",
					"Site Province", "Site Rural/Urban");
			colSizes = List.of(3, 2, 2, 3, 2);
		}
        
	}

	/**
	 * @return the hasWPAReq
	 */
	public boolean hasWPAReq() {
		return hasWPAReq;
	}

	/**
	 * @param hasWPAReq the hasWPAReq to set
	 */
	public void setHasWPAReq(boolean hasWPAReq) {
		this.hasWPAReq = hasWPAReq;
	}

	/**
	 * @return the hasAccreditation
	 */
	public boolean hasAccreditation() {
		return hasAccreditation;
	}

	/**
	 * @param hasAccreditation the hasAccreditation to set
	 */
	public void setHasAccreditation(boolean hasAccreditation) {
		this.hasAccreditation = hasAccreditation;
	}

	@Command({ "postalCodeLookup" })
	public void postalCodeLookup(@BindingParam("discipline") DisciplineHDSA discipline,
			@BindingParam("sitePostalCode") String sitePostalCode) {

	}
	
	@NotifyChange("totalLearners")
	@Command({ "noOfLearnerChange" })
	public void noOfLearnerChange(@BindingParam("discipline") DisciplineHDSA discipline) {
		
		setTotalLearners(disciplineHDSAs.stream()
				.filter(t -> t.getNoOfLearners() != null)
				.mapToInt(DisciplineHDSA::getNoOfLearners)
				.sum());
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

	/**
	 * @return the colHeaders
	 */
	public List<String> getColHeaders() {
		return colHeaders;
	}

	/**
	 * @param colHeaders the colHeaders to set
	 */
	public void setColHeaders(List<String> colHeaders) {
		this.colHeaders = colHeaders;
	}

	/**
	 * @return the colSizes
	 */
	public List<Integer> getColSizes() {
		return colSizes;
	}

	/**
	 * @param colSizes the colSizes to set
	 */
	public void setColSizes(List<Integer> colSizes) {
		this.colSizes = colSizes;
	}

	/**
	 * @return the totalLearners
	 */
	public int getTotalLearners() {
		return totalLearners;
	}

	/**
	 * @param totalLearners the totalLearners to set
	 */
	public void setTotalLearners(int totalLearners) {
		this.totalLearners = totalLearners;
	}
}
