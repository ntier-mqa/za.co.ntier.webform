package za.co.ntier.webform.form.bean;

import java.util.ArrayList;
import java.util.List;

public class OneLineTableInfo extends LearnerInputTableInfo {
	@Override
	public int getLeftSize() {
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
		if (typeNoTotalApply.equals(type)) {
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
		}
		return super.getNoUnEmployedSize();
	}
	
	private String type;
	private OneLineTableInfo(String type) {
		this.type = type;
	}
	private final static String typeNoTotalApply = "NoTotalApply";
	private final static String typeFullEmployed = "FullEmployed";
	public static OneLineTableInfo createNoTotalApplyTableInfo() {
		OneLineTableInfo oneLineTableInfo = new OneLineTableInfo(typeNoTotalApply);
		oneLineTableInfo.setHasWPAReq(false);
		oneLineTableInfo.setHasAccred(false);
		
		oneLineTableInfo.setShowLearnerTitle(false);
		oneLineTableInfo.setShowTotalLine(false);
		
		oneLineTableInfo.setShowNoLearners(false);
		oneLineTableInfo.setShowNoEmployed(false);
		oneLineTableInfo.setShowNoUnEmployed(false);
		
		oneLineTableInfo.setShowNoTotalApply(true);
		
		List<LearnerInputInfo> learnerInputInfos = new ArrayList<>();
		LearnerInputInfo learnerInputInfo = new LearnerInputInfo();
		learnerInputInfo.setUploadAccred(false);
		learnerInputInfo.setUploadWPA(false);
		
		learnerInputInfos.add(learnerInputInfo);
		oneLineTableInfo.setLearnerInputInfos(learnerInputInfos);
		
		return oneLineTableInfo;
	}
	
	public static OneLineTableInfo createFullEmployedTableInfo() {
		OneLineTableInfo oneLineTableInfo = new OneLineTableInfo(typeFullEmployed);
		oneLineTableInfo.setHasWPAReq(false);
		oneLineTableInfo.setHasAccred(false);
		
		oneLineTableInfo.setShowLearnerTitle(false);
		oneLineTableInfo.setShowTotalLine(false);
		
		oneLineTableInfo.setShowNoLearners(false);
		oneLineTableInfo.setShowNoEmployed(true);
		oneLineTableInfo.setShowNoUnEmployed(true);
		
		oneLineTableInfo.setShowNoTotalApply(true);
		
		List<LearnerInputInfo> learnerInputInfos = new ArrayList<>();
		LearnerInputInfo learnerInputInfo = new LearnerInputInfo();
		learnerInputInfo.setUploadAccred(false);
		learnerInputInfo.setUploadWPA(false);
		
		learnerInputInfos.add(learnerInputInfo);
		oneLineTableInfo.setLearnerInputInfos(learnerInputInfos);
		
		return oneLineTableInfo;
	}

}
