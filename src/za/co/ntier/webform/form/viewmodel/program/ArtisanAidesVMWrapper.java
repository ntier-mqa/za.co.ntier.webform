package za.co.ntier.webform.form.viewmodel.program;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.program.ArtisanAidesProgram;
import za.co.ntier.webform.form.viewmodel.DiscretionaryGrantsApplicationProgramVM;

/**
 * If super has an init method but its ChildViewModel doesn't, you can
 * add @Init(superclass=true) on the ChildViewModel to use super's init.
 */

public class ArtisanAidesVMWrapper extends ProgramVMWrapper<ArtisanAidesProgram> {
    private ArtisanAidesProgram program;
    private DiscretionaryGrantsApplicationProgramVM applicationProgramVM;

    @Init(superclass = true)
    public void init(
        @ExecutionArgParam("program") ArtisanAidesProgram program,
        @ExecutionArgParam("applicationProgramVM") DiscretionaryGrantsApplicationProgramVM appVM
    ) {
        this.program = program;
        this.applicationProgramVM = appVM;
    }

    public ArtisanAidesProgram getProgram() { return program; }
    public DiscretionaryGrantsApplicationProgramVM getApplicationProgramVM() { return applicationProgramVM; }
}

