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
	private int totalLearners = 0;

	public DisciplineTableInfo(int programMasterDataID, boolean isTrade) {
		this.disciplines = new ArrayList<>();
		String disciplineHeader = isTrade ? "Trade" : "Discipline";
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
		}else {
			List<X_ZZ_Program_Disciplines> programDisciplines = new Query(Env.getCtx(), I_ZZ_Program_Disciplines.Table_Name,
					String.format("%s = ?", I_ZZ_Program_Master_Data.COLUMNNAME_ZZ_Program_Master_Data_ID), null)
					.setParameters(programMasterDataID).setClient_ID().setOrderBy(I_ZZ_Program_Disciplines.COLUMNNAME_Line)
					.list();

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
			colHeaders = List.of(disciplineHeader, "No. of Learners", "Site Postal Code", "Area/Suburb", "Site Province",
					"Attach WPA", "Attach Accreditation");
			colSizes = List.of(3, 1, 1, 2, 1, 2, 2);
		} else if (hasWPAReq) {
			colHeaders = List.of(disciplineHeader, "No. of Learners", "Site Postal Code", "Area/Suburb", "Site Province",
					"Attach WPA");
			colSizes = List.of(3, 1, 1, 2, 1, 4);
		} else if (hasAccreditation) {
			colHeaders = List.of(disciplineHeader, "No. of Learners", "Site Postal Code", "Area/Suburb", "Site Province",
					"Attach Accreditation");
			colSizes = List.of(3, 1, 1, 2, 1, 4);
		} else {
			colHeaders = List.of(disciplineHeader, "No. of Learners", "Site Postal Code", "Area/Suburb", "Site Province");
			colSizes = List.of(3, 2, 2, 3, 2);
		}
	}

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

	/**
	 * @return the disciplines
	 */
	public List<Discipline> getDisciplines() {
		return disciplines;
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
	 * @param totalLearners the totalLearners to set
	 */
	public void setTotalLearners(int totalLearners) {
		this.totalLearners = totalLearners;
	}

}
