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
	private String learnerInputType;
	
	private int totalNoLearners = 0;
	private int totalNoEmployed = 0;
	private int totalNoUnEmployed = 0;
	
	
	private boolean showNoLearners = true;
	private boolean showNoEmployed = false;
	private boolean showNoUnEmployed = false;
	private boolean showNoTotalApply = false;
	private boolean showTotalLine = true;
	
	private String noLearnersTitle = "No. of Learners";
	private String noEmployedTitle = "No. of Employed";
	private String noUnEmployedTitle = "No. of Unemployed";
	private String noTotalApplyTitle = "Total Number of Learners Applied For";
	
	private boolean showLearnerTitle = true;
	
	public int getRightSize() {
		return 5;
	}
	
	public int getLeftSize() {
		return 4;
	}
	
	public int getNoLearnersSize() {
		if (showNoLearners) {
			return 6;//12/2
		}else {
			return 4;// 12/3
		}
	}
	
	public int getNoEmployedSize() {
		if (showNoLearners) {
			return 6;//12/2
		}else {
			return 4;// 12/3
		}
	}
	
	public int getNoUnEmployedSize() {
		if (showNoLearners) {
			return 6;//12/2
		}else {
			return 4;// 12/3
		}
	}
	
	public int getPostalCodeSize() {
		if (showNoLearners) {
			return 6;//12/2
		}else {
			return 4;// 12/3
		}
	}

	public LearnerInputTableInfo() {
		
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

	public String getLearnerTilte() {
		return "Discipline";
	}
	
	
	/**
	 * @return the programType
	 */
	public ProgramType getProgramType() {
		return programType;
	}

	/**
	 * @return the totalNoLearners
	 */
	public int getTotalLearners() {
		return totalNoLearners;
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

	public void noLearnersChange() {
		setTotalLearners(learnerInputInfos.stream().filter(t -> t.getNoLearners() != null)
				.mapToInt(LearnerInputInfo::getNoLearners).sum());

		BindUtils.postNotifyChange(this, "totalNoLearners");
	}
	
	public void noOfUnEmployedChange() {
		setTotalNoUnEmployed(learnerInputInfos.stream().filter(t -> t.getNoUnEmployed() != null)
				.mapToInt(LearnerInputInfo::getNoUnEmployed).sum());

		BindUtils.postNotifyChange(this, "totalNoUnEmployed");
	}
	
	public void noOfEmployedChange() {
		setTotalNoEmployed(learnerInputInfos.stream().filter(t -> t.getNoEmployed() != null)
				.mapToInt(LearnerInputInfo::getNoEmployed).sum());

		BindUtils.postNotifyChange(this, "totalNoEmployed");
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
	 * @param totalNoLearners the totalNoLearners to set
	 */
	public void setTotalLearners(int totalLearners) {
		this.totalNoLearners = totalLearners;
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
	 * @return the totalNoEmployed
	 */
	public int getTotalNoEmployed() {
		return totalNoEmployed;
	}

	/**
	 * @param totalNoEmployed the totalNoEmployed to set
	 */
	public void setTotalNoEmployed(int totalNoEmployed) {
		this.totalNoEmployed = totalNoEmployed;
	}

	/**
	 * @return the totalNoUnEmployed
	 */
	public int getTotalNoUnEmployed() {
		return totalNoUnEmployed;
	}

	/**
	 * @param totalNoUnEmployed the totalNoUnEmployed to set
	 */
	public void setTotalNoUnEmployed(int totalNoUnEmployed) {
		this.totalNoUnEmployed = totalNoUnEmployed;
	}

	/**
	 * @return the showNoLearners
	 */
	public boolean isShowNoLearners() {
		return showNoLearners;
	}

	/**
	 * @param showNoLearners the showNoLearners to set
	 */
	public void setShowNoLearners(boolean showNoLearners) {
		this.showNoLearners = showNoLearners;
	}

	/**
	 * @return the showNoEmployed
	 */
	public boolean isShowNoEmployed() {
		return showNoEmployed;
	}

	/**
	 * @param showNoEmployed the showNoEmployed to set
	 */
	public void setShowNoEmployed(boolean showNoEmployed) {
		this.showNoEmployed = showNoEmployed;
	}

	/**
	 * @return the showLearnerTitle
	 */
	public boolean isShowLearnerTitle() {
		return showLearnerTitle;
	}

	/**
	 * @param showLearnerTitle the showLearnerTitle to set
	 */
	public void setShowLearnerTitle(boolean showLearnerTitle) {
		this.showLearnerTitle = showLearnerTitle;
	}

	/**
	 * @return the showNoUnEmployed
	 */
	public boolean isShowNoUnEmployed() {
		return showNoUnEmployed;
	}
	
	public boolean isShowTotalLine() {
		return showTotalLine;
	}

	/**
	 * @param showNoUnEmployed the showNoUnEmployed to set
	 */
	public void setShowNoUnEmployed(boolean showNoUnEmployed) {
		this.showNoUnEmployed = showNoUnEmployed;
	}

	/**
	 * @return the showNoTotalApply
	 */
	public boolean isShowNoTotalApply() {
		return showNoTotalApply;
	}

	/**
	 * @param showNoTotalApply the showNoTotalApply to set
	 */
	public void setShowNoTotalApply(boolean showNoTotalApply) {
		this.showNoTotalApply = showNoTotalApply;
	}

	/**
	 * @return the noEmployedTitle
	 */
	public String getNoEmployedTitle() {
		return noEmployedTitle;
	}

	/**
	 * @param noEmployedTitle the noEmployedTitle to set
	 */
	public void setNoEmployedTitle(String noEmployedTitle) {
		this.noEmployedTitle = noEmployedTitle;
	}

	/**
	 * @return the noUnEmployedTitle
	 */
	public String getNoUnEmployedTitle() {
		return noUnEmployedTitle;
	}

	/**
	 * @param noUnEmployedTitle the noUnEmployedTitle to set
	 */
	public void setNoUnEmployedTitle(String noUnEmployedTitle) {
		this.noUnEmployedTitle = noUnEmployedTitle;
	}

	/**
	 * @return the noTotalApplyTitle
	 */
	public String getNoTotalApplyTitle() {
		return noTotalApplyTitle;
	}

	/**
	 * @param noTotalApplyTitle the noTotalApplyTitle to set
	 */
	public void setNoTotalApplyTitle(String noTotalApplyTitle) {
		this.noTotalApplyTitle = noTotalApplyTitle;
	}

	/**
	 * @return the noLearnersTitle
	 */
	public String getNoLearnersTitle() {
		return noLearnersTitle;
	}

	/**
	 * @param noLearnersTitle the noLearnersTitle to set
	 */
	public void setNoLearnersTitle(String noLearnersTitle) {
		this.noLearnersTitle = noLearnersTitle;
	}

	/**
	 * @param showTotalLine the showTotalLine to set
	 */
	public void setShowTotalLine(boolean showTotalLine) {
		this.showTotalLine = showTotalLine;
	}

}
