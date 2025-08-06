package za.co.ntier.webform.form.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.compiere.model.Query;
import org.compiere.util.Env;
import org.zkoss.bind.BindUtils;

import za.co.ntier.webform.form.viewmodel.master.MasterUtil;
import za.co.ntier.webform.model.I_ZZ_Disciplines;
import za.co.ntier.webform.model.I_ZZ_Program_Disciplines;
import za.co.ntier.webform.model.I_ZZ_Program_Master_Data;
import za.co.ntier.webform.model.X_ZZ_Program_Disciplines;

public class CandidacyInfo {
	private AddressInfoBase candidacyContact;
	private AddressInfoBase alternateCandidacyContact;
	private List<DisciplineHDSA> disciplineHDSAs;
	private boolean hasWPAReq = false;
	private boolean hasAccreditation = false;
	private int totalLearners = 0;
	private List<String> colHeaders;
	private List<Integer> colSizes;
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
	 * @return the candidacyContact
	 */
	public AddressInfoBase getCandidacyContact() {
		return candidacyContact;
	}
	/**
	 * @param candidacyContact the candidacyContact to set
	 */
	public void setCandidacyContact(AddressInfoBase candidacyContact) {
		this.candidacyContact = candidacyContact;
	}
	/**
	 * @return the alternateCandidacyContact
	 */
	public AddressInfoBase getAlternateCandidacyContact() {
		return alternateCandidacyContact;
	}
	/**
	 * @param alternateCandidacyContact the alternateCandidacyContact to set
	 */
	public void setAlternateCandidacyContact(AddressInfoBase alternateCandidacyContact) {
		this.alternateCandidacyContact = alternateCandidacyContact;
	}
	/**
	 * @return the disciplineHDSAs
	 */
	public List<DisciplineHDSA> getDisciplineHDSAs() {
		return disciplineHDSAs;
	}
	/**
	 * @param disciplineHDSAs the disciplineHDSAs to set
	 */
	public void setDisciplineHDSAs(List<DisciplineHDSA> disciplineHDSAs) {
		this.disciplineHDSAs = disciplineHDSAs;
	}
	/**
	 * @return the hasWPAReq
	 */
	public boolean isHasWPAReq() {
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
	public boolean isHasAccreditation() {
		return hasAccreditation;
	}
	/**
	 * @param hasAccreditation the hasAccreditation to set
	 */
	public void setHasAccreditation(boolean hasAccreditation) {
		this.hasAccreditation = hasAccreditation;
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
	
	public CandidacyInfo(int programMasterDataID) {
		candidacyContact = new AddressInfoBase(AddressCategory.CANDIDACY_CONTACT, MasterUtil.getRegions().get(new Random().nextInt(MasterUtil.getRegions().size())));
		alternateCandidacyContact = new AddressInfoBase(AddressCategory.CANDIDACY_CONTACT, MasterUtil.getRegions().get(new Random().nextInt(MasterUtil.getRegions().size())));
		
		List<X_ZZ_Program_Disciplines> programDisciplines = new Query(Env.getCtx(), I_ZZ_Program_Disciplines.Table_Name, 
        		String.format("%s = ?", I_ZZ_Program_Master_Data.COLUMNNAME_ZZ_Program_Master_Data_ID), null)
			.setParameters(programMasterDataID)
			.setClient_ID()
			.setOrderBy(I_ZZ_Program_Disciplines.COLUMNNAME_Line)
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
        			"Area/Suburb", "Site Province", "Attach WPA", "Attach Accreditation");
        	colSizes = List.of(3, 1, 1, 2, 1, 2, 2);
        }else if (hasWPAReq) {
			colHeaders = List.of("Descipline", "No. of Learners", "Site Postal Code",
					"Area/Suburb", "Site Province", "Attach WPA");
			colSizes = List.of(3, 1, 1, 2, 1, 4);
		} else if (hasAccreditation) {
			colHeaders = List.of("Descipline", "No. of Learners", "Site Postal Code",
					"Area/Suburb", "Site Province", "Attach Accreditation");
			colSizes = List.of(3, 1, 1, 2, 1, 4);
		} else {
			colHeaders = List.of("Descipline", "No. of Learners", "Site Postal Code",
					"Area/Suburb", "Site Province");
			colSizes = List.of(3, 2, 2, 3, 2);
		}
	}
	
	public void noOfLearnerChange() {
		setTotalLearners(disciplineHDSAs.stream()
				.filter(t -> t.getNoOfLearners() != null)
				.mapToInt(DisciplineHDSA::getNoOfLearners)
				.sum());
		
		BindUtils.postNotifyChange(this, "totalLearners");
	}
}
