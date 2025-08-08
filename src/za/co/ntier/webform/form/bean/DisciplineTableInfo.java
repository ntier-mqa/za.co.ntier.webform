package za.co.ntier.webform.form.bean;

import java.util.ArrayList;
import java.util.List;

import org.compiere.model.Query;
import org.compiere.util.Env;
import org.zkoss.bind.BindUtils;

import za.co.ntier.webform.model.I_ZZ_Disciplines;
import za.co.ntier.webform.model.I_ZZ_Program_Disciplines;
import za.co.ntier.webform.model.I_ZZ_Program_Master_Data;
import za.co.ntier.webform.model.I_ZZ_Program_Trade;
import za.co.ntier.webform.model.I_ZZ_Trade;
import za.co.ntier.webform.model.X_ZZ_Program_Disciplines;
import za.co.ntier.webform.model.X_ZZ_Program_Trade;
import za.co.ntier.webform.model.X_ZZ_Trade;

public class DisciplineTableInfo {
	private List<String> colHeaders;
	private List<Integer> colSizes;
	private List<Discipline> disciplines;
	private boolean hasAccreditation = false;
	private boolean hasWPAReq = false;
	private boolean isTrade = false;
	private ProgramType programType;
	private int totalLearners = 0;

	public DisciplineTableInfo(int programMasterDataID, ProgramType programType, boolean isTrade) {
		this.disciplines = new ArrayList<>();
		this.setProgramType(programType);
		this.setTrade(isTrade);

		if (isTrade) {
			List<X_ZZ_Program_Trade> programTrades = new Query(Env.getCtx(), I_ZZ_Program_Trade.Table_Name,
					String.format("%s = ?", I_ZZ_Program_Master_Data.COLUMNNAME_ZZ_Program_Master_Data_ID), null)
					.setParameters(programMasterDataID).setClient_ID().setOrderBy(I_ZZ_Program_Trade.COLUMNNAME_Line)
					.list();

			programTrades.stream().forEach((programTrade) -> {
				I_ZZ_Trade trade = new X_ZZ_Trade(Env.getCtx(), programTrade.getZZ_Trade_ID(), null);
				Discipline disciplineTrade = new Discipline(programTrade, trade);
				disciplines.add(disciplineTrade);

				if (disciplineTrade.isUploadWPA()) {
					hasWPAReq = true;
				}

				if (disciplineTrade.isUploadAccreditation()) {
					hasAccreditation = true;
				}
			});
		} else {
			List<X_ZZ_Program_Disciplines> programDisciplines = new Query(Env.getCtx(),
					I_ZZ_Program_Disciplines.Table_Name,
					String.format("%s = ?", I_ZZ_Program_Master_Data.COLUMNNAME_ZZ_Program_Master_Data_ID), null)
					.setParameters(programMasterDataID).setClient_ID()
					.setOrderBy(I_ZZ_Program_Disciplines.COLUMNNAME_Line).list();

			programDisciplines.stream().forEach((programDiscipline) -> {
				I_ZZ_Disciplines discipline = programDiscipline.getZZ_Disciplines();
				Discipline disciplineHDSA = new Discipline(programDiscipline, discipline);
				disciplines.add(disciplineHDSA);

				if (disciplineHDSA.isUploadWPA()) {
					hasWPAReq = true;
				}

				if (disciplineHDSA.isUploadAccreditation()) {
					hasAccreditation = true;
				}
			});
		}

		if (hasWPAReq && hasAccreditation) {
			rightColSize = 12/4;
		} else if (!hasWPAReq && !hasAccreditation) {
			rightColSize = 12/2;
		} else {
			rightColSize = 12/3;
		}
	}

	private int rightColSize;
	
	/**
	 * @return the colHeaders
	 */
	public List<String> getColHeaders() {
		return colHeaders;
	}

	/**
	 * @return the colSizes
	 */
	public List<Integer> getColSizes() {
		return colSizes;
	}

	public String getDisciplineNote() {
		return """
				All Employers/Organisations with multiple sites using one levy number must submit one
				consolidated application for all sites.
				""";
	}

	/**
	 * @return the disciplines
	 */
	public List<Discipline> getDisciplines() {
		return disciplines;
	}

	public String getDisciplineTitle() {
		if (programType == ProgramType.CANDIDACY)
			return "List of disciplines supported for the HDSA Candidacy which the number of learners applying should be based on.";
		if (programType == ProgramType.INTERNSHIP && !isTrade)
			return "List of disciplines supported for Internships which the number of learners applying should be based on.";
		if (programType == ProgramType.INTERNSHIP && isTrade)
			return """
					List of disciplines supported for Artisan Internships which the number of learners applying
					should be based on. Preference will be given to the following trades that are hard to fill
					according MQA SPOI list, (Diesel Mechanic and Millwright).""";
		if (programType == ProgramType.EXPERIENCE)
			return "List of disciplines supported for practical training which the number of learners applying should be based on.";
		else
			return programType.toString();

	}
	
	public String getOverallTitle() {
		if (programType == ProgramType.EXPERIENCE) {
			return "• WORK EXPERIENCE (PRACTICAL TRAINING)";
		} 
		
		return null;
	}

	/**
	 * @return the programType
	 */
	public ProgramType getProgramType() {
		return programType;
	}

	/**
	 * @return the totalLearners
	 */
	public int getTotalLearners() {
		return totalLearners;
	}

	/**
	 * @return the hasAccreditation
	 */
	public boolean isHasAccreditation() {
		return hasAccreditation;
	}

	/**
	 * @return the hasWPAReq
	 */
	public boolean isHasWPAReq() {
		return hasWPAReq;
	}

	/**
	 * @return the isTrade
	 */
	public boolean isTrade() {
		return isTrade;
	}

	public void noOfLearnerChange() {
		setTotalLearners(disciplines.stream().filter(t -> t.getNoOfLearners() != null)
				.mapToInt(Discipline::getNoOfLearners).sum());

		BindUtils.postNotifyChange(this, "totalLearners");
	}

	/**
	 * @param colHeaders the colHeaders to set
	 */
	public void setColHeaders(List<String> colHeaders) {
		this.colHeaders = colHeaders;
	}

	/**
	 * @param colSizes the colSizes to set
	 */
	public void setColSizes(List<Integer> colSizes) {
		this.colSizes = colSizes;
	}

	/**
	 * @param disciplines the disciplines to set
	 */
	public void setDisciplines(List<Discipline> disciplines) {
		this.disciplines = disciplines;
	}

	/**
	 * @param hasAccreditation the hasAccreditation to set
	 */
	public void setHasAccreditation(boolean hasAccreditation) {
		this.hasAccreditation = hasAccreditation;
	}

	/**
	 * @param hasWPAReq the hasWPAReq to set
	 */
	public void setHasWPAReq(boolean hasWPAReq) {
		this.hasWPAReq = hasWPAReq;
	}

	/**
	 * @param programType the programType to set
	 */
	public void setProgramType(ProgramType programType) {
		this.programType = programType;
	}

	/**
	 * @param totalLearners the totalLearners to set
	 */
	public void setTotalLearners(int totalLearners) {
		this.totalLearners = totalLearners;
	}

	/**
	 * @param isTrade the isTrade to set
	 */
	public void setTrade(boolean isTrade) {
		this.isTrade = isTrade;
	}

	/**
	 * @return the rightColSize
	 */
	public int getRightColSize() {
		return rightColSize;
	}

	/**
	 * @param rightColSize the rightColSize to set
	 */
	public void setRightColSize(int rightColSize) {
		this.rightColSize = rightColSize;
	}

}
