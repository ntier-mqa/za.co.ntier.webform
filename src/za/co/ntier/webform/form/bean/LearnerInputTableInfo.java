package za.co.ntier.webform.form.bean;

import java.util.ArrayList;
import java.util.List;
import org.zkoss.bind.BindUtils;

import za.co.ntier.webform.form.viewmodel.master.MasterUtil;
import za.co.ntier.webform.model.X_ZZ_FormDiscipline;

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
	@SuppressWarnings("unchecked")
	public LearnerInputTableInfo(int programMasterDataID, ProgramType programType, String learnerInputType) {
		this.learnerInputInfos = new ArrayList<>();
		this.setProgramType(programType);
		this.learnerInputType = learnerInputType;
		
		List<Object> rObjs = MasterUtil.queryLearnerInputInfos(programMasterDataID, programType, learnerInputType);
		learnerInputInfos = (List<LearnerInputInfo>)rObjs.get(0);
		hasWPAReq = (boolean)rObjs.get(1);
		hasAccred = (boolean)rObjs.get(2);
		
	}
	
	

	private Integer rightColSize;

	/**
	 * @return the learnerInputInfos
	 */
	public List<LearnerInputInfo> getLearnerInputInfos() {
		return learnerInputInfos;
	}

	public String getTableTitle() {
		if (programType == ProgramType.CANDIDACY)
			return "List of disciplines supported for the HDSA Candidacy which the number of learners applying should be based on.";
		if (programType == ProgramType.INTERNSHIP && !isTrade())
			return "List of disciplines supported for Internships which the number of learners applying should be based on.";
		if (programType == ProgramType.INTERNSHIP && isTrade())
			return """
					List of disciplines supported for Artisan Internships which the number of learners applying
					should be based on. Preference will be given to the following trades that are hard to fill
					according MQA SPOI list, (Diesel Mechanic and Millwright).""";
		if (programType == ProgramType.EXPERIENCE)
			return "List of disciplines supported for practical training which the number of learners applying should be based on.";
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
	 * @return the programTypeMenuContextKey
	 */
	public ProgramType getProgramType() {
		return programType;
	}

	/**
	 * @return the totalNoLearners
	 */
	public int getTotalNoLearners() {
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
		return learnerInputType.equals(X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_Trade);
	}

	public void noLearnersChange() {
		setTotalNoLearners(learnerInputInfos.stream().filter(t -> t.getNoLearners() != null)
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
	 * @param programTypeMenuContextKey the programTypeMenuContextKey to set
	 */
	public void setProgramType(ProgramType programType) {
		this.programType = programType;
	}

	/**
	 * @param totalNoLearners the totalNoLearners to set
	 */
	public void setTotalNoLearners(int totalLearners) {
		this.totalNoLearners = totalLearners;
	}

	/**
	 * @return the rightColSize
	 */
	public int getRightColSize() {
		if (rightColSize != null)
			return rightColSize;
		
		if (hasWPAReq && hasAccred) {
			return 4; //12/3
		} else if (!hasWPAReq && !hasAccred) {
			return 12;
		} else {
			return 6; //12/2
		}
		
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
