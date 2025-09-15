package za.co.ntier.webform.form.viewmodel.program;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.program.ArtisanRPLProgram;
import za.co.ntier.webform.form.viewmodel.DiscretionaryGrantsApplicationProgramVM;

public class ArtisanRPLVMWrapper extends ProgramVMWrapper<ArtisanRPLProgram> {

    private ArtisanRPLProgram program;
    private DiscretionaryGrantsApplicationProgramVM applicationProgramVM;

    @Init(superclass = true) // run ProgramVMWrapper's init first
    public void init(
        @ExecutionArgParam("program") ArtisanRPLProgram program,
        @ExecutionArgParam("applicationProgramVM") DiscretionaryGrantsApplicationProgramVM appVM
    ) {
        this.program = program;
        this.applicationProgramVM = appVM;
    }

    public ArtisanRPLProgram getProgram() { return program; }
    public DiscretionaryGrantsApplicationProgramVM getApplicationProgramVM() { return applicationProgramVM; }
}
