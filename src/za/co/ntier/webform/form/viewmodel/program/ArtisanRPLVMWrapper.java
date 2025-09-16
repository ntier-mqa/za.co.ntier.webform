package za.co.ntier.webform.form.viewmodel.program;

import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.program.ArtisanRPLProgram;
import za.co.ntier.webform.form.viewmodel.DiscretionaryGrantsApplicationProgramVM;

@Init(superclass = true)
public class ArtisanRPLVMWrapper extends ProgramVMWrapper<ArtisanRPLProgram> {

    private ArtisanRPLProgram program;
    private DiscretionaryGrantsApplicationProgramVM applicationProgramVM;

    public ArtisanRPLProgram getProgram() { return program; }
    public DiscretionaryGrantsApplicationProgramVM getApplicationProgramVM() { return applicationProgramVM; }
}
