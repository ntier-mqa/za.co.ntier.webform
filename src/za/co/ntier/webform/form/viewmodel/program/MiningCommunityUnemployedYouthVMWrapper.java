package za.co.ntier.webform.form.viewmodel.program;

import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.ProgramType;
import za.co.ntier.webform.form.bean.program.MiningCommunityUnemployedYouthProgram;


@Init(superclass = true)
public class MiningCommunityUnemployedYouthVMWrapper extends ProgramVMWrapper<MiningCommunityUnemployedYouthProgram>{
	public boolean isShowDownload() {
		return ProgramType.MINING_COMMUNITY == getProgram().getMenuContextInfo().getProgramType() ||
				ProgramType.UNEMPLOYED_YOUTH == getProgram().getMenuContextInfo().getProgramType();
	}
    
}
