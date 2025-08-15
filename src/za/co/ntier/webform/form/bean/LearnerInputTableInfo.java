package za.co.ntier.webform.form.bean;

import java.util.ArrayList;
import java.util.List;
import org.compiere.util.DB;
import org.zkoss.bind.BindUtils;

import za.co.ntier.webform.model.X_ZZ_Disciplines;
import za.co.ntier.webform.model.X_ZZ_FormDiscipline;
import za.co.ntier.webform.model.X_ZZ_Learnerships;
import za.co.ntier.webform.model.X_ZZ_Program_Disciplines;
import za.co.ntier.webform.model.X_ZZ_Program_Learnerships;
import za.co.ntier.webform.model.X_ZZ_Program_Trade;
import za.co.ntier.webform.model.X_ZZ_Trade;

public class LearnerInputTableInfo {
	private List<LearnerInputInfo> learnerInputInfos;
	private boolean hasAccred = false;
	private boolean hasWPAReq = false;
	private ProgramType programType;
	private int totalLearners = 0;
	private int totalEmployedLearners = 0;
	private int totalUnEmployedLearners = 0;
	private String learnerInputType;
	private boolean showLearners = true;
	private boolean showEmployedLearners = false;
	
	public int getLeftColSize() {
		if (showLearners) {
			return 6;//12/2
		}else {
			return 4;// 12/3
		}
	}

	public LearnerInputTableInfo(int programMasterDataID, ProgramType programType, String learnerInputType) {
		this.learnerInputInfos = new ArrayList<>();
		this.setProgramType(programType);
		this.learnerInputType = learnerInputType;
		
		String learnerInputProgramID;
		String learnerInputID;
		String learnerInputText = X_ZZ_Trade.COLUMNNAME_Name;
		String learnerInputProgramTable;
		String learnerInputTable;
		
		StringBuilder sql = new StringBuilder();
		if (learnerInputType == X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_InternshipTrade) {
			learnerInputProgramID = X_ZZ_Program_Trade.COLUMNNAME_ZZ_Program_Trade_ID;
			learnerInputID = X_ZZ_Program_Trade.COLUMNNAME_ZZ_Trade_ID;
			learnerInputProgramTable = X_ZZ_Program_Trade.Table_Name;
			learnerInputTable = X_ZZ_Trade.Table_Name;
		}else if (learnerInputType == X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_InternshipDiscipline) {
			learnerInputProgramID = X_ZZ_Program_Disciplines.COLUMNNAME_ZZ_Program_Disciplines_ID;
			learnerInputID = X_ZZ_Program_Disciplines.COLUMNNAME_ZZ_Disciplines_ID;
			learnerInputProgramTable = X_ZZ_Program_Disciplines.Table_Name;
			learnerInputTable = X_ZZ_Disciplines.Table_Name;
		}else if (learnerInputType == LearnershipTableInfo.learnerInputType_4IR_Learnership ||
				learnerInputType == LearnershipTableInfo.learnerInputType_General_Learnership ||
				learnerInputType == LearnershipTableInfo.learnerInputType_AET_Learnership) {
			learnerInputProgramID = X_ZZ_Program_Learnerships.COLUMNNAME_ZZ_Program_Learnerships_ID;
			learnerInputID = X_ZZ_Program_Learnerships.COLUMNNAME_ZZ_Learnerships_ID;
			learnerInputProgramTable = X_ZZ_Program_Learnerships.Table_Name;
			learnerInputTable = X_ZZ_Learnerships.Table_Name;
		}else {
			throw new IllegalArgumentException("Wrong learnerInput type");
		}
		
		sql.append(String.format("SELECT %s, %s.%s, %s, %s, %s FROM %s INNER JOIN %s ON (%s.%s = %s.%s) WHERE %s = ?", 
				learnerInputProgramID,
				learnerInputTable,
				learnerInputID,
				X_ZZ_Program_Trade.COLUMNNAME_ZZ_WPA_Req,
				X_ZZ_Program_Trade.COLUMNNAME_ZZ_Is_Accred_SLA_Req,
				learnerInputText,
				learnerInputProgramTable,
				learnerInputTable,
				learnerInputProgramTable,
				learnerInputID,
				learnerInputTable,
				learnerInputID,
				X_ZZ_Program_Trade.COLUMNNAME_ZZ_Program_Master_Data_ID));
		
		if (learnerInputType == LearnershipTableInfo.learnerInputType_4IR_Learnership) {
			sql.append(" AND ZZ_Program_Learnerships.ZZ_Learnerships_Type = '4'");
		}else if (learnerInputType == LearnershipTableInfo.learnerInputType_General_Learnership){
			sql.append(" AND ZZ_Program_Learnerships.ZZ_Learnerships_Type = 'G'");
		}else if (learnerInputType == LearnershipTableInfo.learnerInputType_AET_Learnership){
			sql.append(" AND ZZ_Program_Learnerships.ZZ_Learnerships_Type = 'A'");
		}
		
		sql.append(" ORDER BY ");
		sql.append(X_ZZ_Program_Trade.COLUMNNAME_Line);
		
		List<List<Object>> learnerInputInfoObjs = DB.getSQLArrayObjectsEx(null, sql.toString(), programMasterDataID);

		
		learnerInputInfoObjs.stream().forEach((learnerInputInfoObj) -> {
				LearnerInputInfo learnerInputInfo = new LearnerInputInfo(learnerInputInfoObj);
				learnerInputInfos.add(learnerInputInfo);

				if (learnerInputInfo.isUploadWPA()) {
					hasWPAReq = true;
				}

				if (learnerInputInfo.isUploadAccred()) {
					hasAccred = true;
				}
			});
		

		if (hasWPAReq && hasAccred) {
			rightColSize = 4; //12/3
		} else if (!hasWPAReq && !hasAccred) {
			rightColSize = 12;
		} else {
			rightColSize = 6; //12/2
		}
	}

	private int rightColSize;

	/**
	 * @return the learnerInputInfos
	 */
	public List<LearnerInputInfo> getLearnerInputInfos() {
		return learnerInputInfos;
	}

	public String getTableTitle() {
		if (programType == ProgramType.CANDIDACY)
			return "List of learnerInputInfos supported for the HDSA Candidacy which the number of learners applying should be based on.";
		if (programType == ProgramType.INTERNSHIP && !isTrade())
			return "List of learnerInputInfos supported for Internships which the number of learners applying should be based on.";
		if (programType == ProgramType.INTERNSHIP && isTrade())
			return """
					List of disciplines supported for Artisan Internships which the number of learners applying
					should be based on. Preference will be given to the following trades that are hard to fill
					according MQA SPOI list, (Diesel Mechanic and Millwright).""";
		if (programType == ProgramType.EXPERIENCE)
			return "List of learnerInputInfos supported for practical training which the number of learners applying should be based on.";
		else
			return null;

	}
	
	public String getOverallTitle() {
		if (programType == ProgramType.EXPERIENCE) {
			return "• WORK EXPERIENCE (PRACTICAL TRAINING)";
		} 
		
		return null;
	}

	public String getLearnerInputColTilte() {
		return "Discipline";
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
	 * @return the hasAccred
	 */
	public boolean isHasAccred() {
		return hasAccred;
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
		return learnerInputType.equals(X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_InternshipTrade);
	}

	public void noOfLearnerChange() {
		setTotalLearners(learnerInputInfos.stream().filter(t -> t.getNoOfLearners() != null)
				.mapToInt(LearnerInputInfo::getNoOfLearners).sum());

		BindUtils.postNotifyChange(this, "totalLearners");
	}
	
	public void noOfUnEmployedLearnersChange() {
		setTotalUnEmployedLearners(learnerInputInfos.stream().filter(t -> t.getNoOfUnEmployedLearners() != null)
				.mapToInt(LearnerInputInfo::getNoOfUnEmployedLearners).sum());

		BindUtils.postNotifyChange(this, "totalUnEmployedLearners");
	}
	
	public void noOfEmployedLearnersChange() {
		setTotalEmployedLearners(learnerInputInfos.stream().filter(t -> t.getNoOfEmployedLearners() != null)
				.mapToInt(LearnerInputInfo::getNoOfEmployedLearners).sum());

		BindUtils.postNotifyChange(this, "totalEmployedLearners");
	}


	/**
	 * @param learnerInputInfos the learnerInputInfos to set
	 */
	public void setLearnerInputInfos(List<LearnerInputInfo> learnerInputInfos) {
		this.learnerInputInfos = learnerInputInfos;
	}

	/**
	 * @param hasAccred the hasAccred to set
	 */
	public void setHasAccred(boolean hasAccred) {
		this.hasAccred = hasAccred;
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

	/**
	 * @return the learnerInputType
	 */
	public String getLearnerInputType() {
		return learnerInputType;
	}

	/**
	 * @param learnerInputType the learnerInputType to set
	 */
	public void setLearnerInputType(String learnerInputType) {
		this.learnerInputType = learnerInputType;
	}

	/**
	 * @return the totalEmployedLearners
	 */
	public int getTotalEmployedLearners() {
		return totalEmployedLearners;
	}

	/**
	 * @param totalEmployedLearners the totalEmployedLearners to set
	 */
	public void setTotalEmployedLearners(int totalEmployedLearners) {
		this.totalEmployedLearners = totalEmployedLearners;
	}

	/**
	 * @return the totalUnEmployedLearners
	 */
	public int getTotalUnEmployedLearners() {
		return totalUnEmployedLearners;
	}

	/**
	 * @param totalUnEmployedLearners the totalUnEmployedLearners to set
	 */
	public void setTotalUnEmployedLearners(int totalUnEmployedLearners) {
		this.totalUnEmployedLearners = totalUnEmployedLearners;
	}

	/**
	 * @return the showLearners
	 */
	public boolean isShowLearners() {
		return showLearners;
	}

	/**
	 * @param showLearners the showLearners to set
	 */
	public void setShowLearners(boolean showLearners) {
		this.showLearners = showLearners;
	}

	/**
	 * @return the showEmployedLearners
	 */
	public boolean isShowEmployedLearners() {
		return showEmployedLearners;
	}

	/**
	 * @param showEmployedLearners the showEmployedLearners to set
	 */
	public void setShowEmployedLearners(boolean showEmployedLearners) {
		this.showEmployedLearners = showEmployedLearners;
	}

}
