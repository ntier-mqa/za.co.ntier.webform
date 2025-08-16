package za.co.ntier.webform.form.bean;

import java.util.ArrayList;
import java.util.List;

public class OneLineTableInfo extends LearnerInputTableInfo {
	@Override
	public int getLeftSize() {
		if (typeEmployed.equals(type) ||
				typeLearners.equals(type))
			return 7;
		
		return 10;
	}
	
	@Override
	public int getRightSize() {
		return 2;
	}
	
	public int getNoTotalApplySize() {
		if (typeNoTotalApply.equals(type)) {
			return 10;
		}else if (typeFullEmployed.equals(type)) {
			return 3;
		}
		return -1;
	}
	
	@Override
	public int getPostalCodeSize() {
		if (typeNoTotalApply.equals(type)
				|| typeUnEmployed.equals(type)) {
			return 2;
		}else if (typeFullEmployed.equals(type)) {
			return 3;
		}
		
		return super.getPostalCodeSize();
	}
	
	@Override
	public int getNoEmployedSize() {
		if (typeFullEmployed.equals(type)) {
			return 3;
		}
		return super.getNoEmployedSize();
	}
	
	@Override
	public int getNoLearnersSize() {
		if (typeFullEmployed.equals(type)) {
			return 3;
		}
		return super.getNoLearnersSize();
	}
	
	@Override
	public int getNoUnEmployedSize() {
		if (typeFullEmployed.equals(type)) {
			return 3;
		}else if (typeUnEmployed.equals(type)) {
			return 10;
		}
		return super.getNoUnEmployedSize();
	}
	
	@Override
	public String getLearnerTilte() {
		return "Name of Programme";
	}
	
	private String type;
	private OneLineTableInfo(String type) {
		this.type = type;
		this.setHasWPAReq(false);
		this.setHasAccred(false);
		this.setShowTotalLine(false);
		
		List<LearnerInputInfo> learnerInputInfos = new ArrayList<>();
		LearnerInputInfo learnerInputInfo = new LearnerInputInfo();
		learnerInputInfo.setUploadAccred(false);
		learnerInputInfo.setUploadWPA(false);
		
		learnerInputInfos.add(learnerInputInfo);
		this.setLearnerInputInfos(learnerInputInfos);
	}
	private final static String typeNoTotalApply = "NoTotalApply";
	private final static String typeFullEmployed = "FullEmployed";
	private final static String typeEmployed = "Employed";
	private final static String typeUnEmployed = "UnEmployed";
	private final static String typeLearners = "Learners";
	

	public static OneLineTableInfo createNoTotalApplyTableInfo() {
		OneLineTableInfo oneLineTableInfo = new OneLineTableInfo(typeNoTotalApply);
		
		oneLineTableInfo.setShowLearnerTitle(false);
		
		oneLineTableInfo.setShowNoLearners(false);
		oneLineTableInfo.setShowNoEmployed(false);
		oneLineTableInfo.setShowNoUnEmployed(false);
		oneLineTableInfo.setShowNoTotalApply(true);
		
		return oneLineTableInfo;
	}
	
	public static OneLineTableInfo createUnEmployedTableInfo() {
		OneLineTableInfo oneLineTableInfo = new OneLineTableInfo(typeUnEmployed);
		
		oneLineTableInfo.setShowLearnerTitle(false);
		
		oneLineTableInfo.setShowNoLearners(false);
		oneLineTableInfo.setShowNoEmployed(false);
		oneLineTableInfo.setShowNoUnEmployed(true);
		oneLineTableInfo.setShowNoTotalApply(false);
		
		return oneLineTableInfo;
	}
	
	public static OneLineTableInfo createEmployedTableInfo(String rowTitle) {
		OneLineTableInfo oneLineTableInfo = new OneLineTableInfo(typeEmployed);
		oneLineTableInfo.getLearnerInputInfos().get(0).setLearnerInputText(rowTitle);
		oneLineTableInfo.setShowLearnerTitle(true);
		
		oneLineTableInfo.setShowNoLearners(false);
		oneLineTableInfo.setShowNoEmployed(true);
		oneLineTableInfo.setShowNoUnEmployed(false);
		oneLineTableInfo.setShowNoTotalApply(false);
		
		return oneLineTableInfo;
	}
	
	public static OneLineTableInfo createLearnersTableInfo(String rowTitle) {
		OneLineTableInfo oneLineTableInfo = new OneLineTableInfo(typeLearners);
		oneLineTableInfo.getLearnerInputInfos().get(0).setLearnerInputText(rowTitle);
		oneLineTableInfo.setShowLearnerTitle(true);
		
		oneLineTableInfo.setShowNoLearners(true);
		oneLineTableInfo.setShowNoEmployed(false);
		oneLineTableInfo.setShowNoUnEmployed(false);
		oneLineTableInfo.setShowNoTotalApply(false);
		
		return oneLineTableInfo;
	}
	
	public static OneLineTableInfo createFullEmployedTableInfo() {
		OneLineTableInfo oneLineTableInfo = new OneLineTableInfo(typeFullEmployed);
		
		oneLineTableInfo.setShowLearnerTitle(false);
		
		oneLineTableInfo.setShowNoLearners(false);
		oneLineTableInfo.setShowNoEmployed(true);
		oneLineTableInfo.setShowNoUnEmployed(true);
		oneLineTableInfo.setShowNoTotalApply(true);
				
		return oneLineTableInfo;
	}

}
