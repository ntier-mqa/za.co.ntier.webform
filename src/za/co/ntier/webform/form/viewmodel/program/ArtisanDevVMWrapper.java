package za.co.ntier.webform.form.viewmodel.program;

import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.program.ArtisanDevProgram;
import za.co.ntier.webform.form.viewmodel.DiscretionaryGrantsApplicationProgramVM;

/** Use super's @Init and also capture args used by the ZUL includes */

@Init(superclass = true)
public class ArtisanDevVMWrapper extends ProgramVMWrapper<ArtisanDevProgram> {

    private ArtisanDevProgram program;
    private DiscretionaryGrantsApplicationProgramVM applicationProgramVM;

    // getters used by ZUL
    public ArtisanDevProgram getProgram() { return program; }
    public DiscretionaryGrantsApplicationProgramVM getApplicationProgramVM() { return applicationProgramVM; }
}

