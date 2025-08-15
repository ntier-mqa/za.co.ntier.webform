package za.co.ntier.webform.form.bean;

import java.util.ArrayList;
import java.util.List;

public class OneLineTableInfo extends LearnerInputTableInfo {
	@Override
	public int getLeftSize() {
		if (typeNoTotalApply.equals(type)) {
			return 10;
		}
		return super.getLeftSize();
	}
	
	@Override
	public int getRightSize() {
		if (typeNoTotalApply.equals(type)) {
			return 2;
		}
		return super.getRightSize();
	}
	
	public int getNoTotalApplySize() {
		if (typeNoTotalApply.equals(type)) {
			return 10;
		}
		return -1;
	}
	
	@Override
	public int getPostalCodeSize() {
		if (typeNoTotalApply.equals(type)) {
			return 2;
		}
		
		return super.getPostalCodeSize();
	}
	
	private String type;
	private OneLineTableInfo(String type) {
		this.type = type;
	}
	private final static String typeNoTotalApply = "NoTotalApply";
	public static OneLineTableInfo createNoTotalApplyTableInfo() {
		OneLineTableInfo oneLineTableInfo = new OneLineTableInfo(typeNoTotalApply);
		oneLineTableInfo.setHasWPAReq(false);
		oneLineTableInfo.setHasAccred(false);
		
		oneLineTableInfo.setShowLearnerTitle(false);
		
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

}
