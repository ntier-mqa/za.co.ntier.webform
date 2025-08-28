package za.co.ntier.webform.form.bean;

import java.util.ArrayList;
import java.util.List;

import za.co.ntier.webform.form.MenuContextInfo;

public class OneLineTableInfo extends LearnerInputTableInfo {
	
	
	public int getNoTotalApplySize() {
		if (typeNoTotalApply.equals(type)) {
			return 10;
		}else if (typeFullEmployed.equals(type)) {
			return 3;
		}
		return -1;
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
	
	public static OneLineTableInfo createUnEmployedTableInfo(MenuContextInfo menuContextInfo) {
		OneLineTableInfo oneLineTableInfo = new OneLineTableInfo(typeUnEmployed);
		oneLineTableInfo.setHasWPAReq(menuContextInfo.getIsUploadWPAForNVC());
		oneLineTableInfo.getLearnerInputInfos().get(0).setUploadWPA(menuContextInfo.getIsUploadWPAForNVC());
		
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
