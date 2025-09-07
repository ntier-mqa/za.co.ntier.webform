package za.co.ntier.webform.form.viewmodel.program;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.program.CetTvetProgram;
import za.co.ntier.webform.form.viewmodel.DiscretionaryGrantsApplicationProgramVM;

/**
 * If super has an init method but its ChildViewModel doesn't, you can
 * add @Init(superclass=true) on the ChildViewModel to use super's init.
 */

public class CetTvetVMWrapper extends ProgramVMWrapper<CetTvetProgram>{
	

    private CetTvetProgram program;
    private DiscretionaryGrantsApplicationProgramVM applicationProgramVM;

    @Init
    public void init(
        @ExecutionArgParam("program") CetTvetProgram program,
        @ExecutionArgParam("applicationProgramVM") DiscretionaryGrantsApplicationProgramVM appVM
    ){
        this.program = program;
        this.applicationProgramVM = appVM;
    }

    public CetTvetProgram getProgram() { return program; }
    public DiscretionaryGrantsApplicationProgramVM getApplicationProgramVM() { return applicationProgramVM; }
}
