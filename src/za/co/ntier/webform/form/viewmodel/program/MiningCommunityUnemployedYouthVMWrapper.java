package za.co.ntier.webform.form.viewmodel.program;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.program.MiningCommunityUnemployedYouthProgram;
import za.co.ntier.webform.form.viewmodel.DiscretionaryGrantsApplicationProgramVM;



public class MiningCommunityUnemployedYouthVMWrapper extends ProgramVMWrapper<MiningCommunityUnemployedYouthProgram>{
	
    private MiningCommunityUnemployedYouthProgram program;
    private DiscretionaryGrantsApplicationProgramVM applicationProgramVM;

    @Init(superclass = true)
    public void init(
        @ExecutionArgParam("program") MiningCommunityUnemployedYouthProgram program,
        @ExecutionArgParam("applicationProgramVM") DiscretionaryGrantsApplicationProgramVM appVM
    ) {
        this.program = program;
        this.applicationProgramVM = appVM;
    }

    public MiningCommunityUnemployedYouthProgram getProgram() { return program; }
    public DiscretionaryGrantsApplicationProgramVM getApplicationProgramVM() { return applicationProgramVM; }

}
