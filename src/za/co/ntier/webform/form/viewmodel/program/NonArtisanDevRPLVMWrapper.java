package za.co.ntier.webform.form.viewmodel.program;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.program.NonArtisanDevRPLProgram;

public class NonArtisanDevRPLVMWrapper {
    private NonArtisanDevRPLProgram nonArtisanDevRPLInfo;

    @Init
    public void init(@ExecutionArgParam("nonArtisanDevRPLInfo") NonArtisanDevRPLProgram nonArtisanDevRPLInfo) {
        this.setNonArtisanDevRPLInfo(nonArtisanDevRPLInfo);
    }

    public NonArtisanDevRPLProgram getNonArtisanDevRPLInfo() {
        return nonArtisanDevRPLInfo;
    }

    public void setNonArtisanDevRPLInfo(NonArtisanDevRPLProgram nonArtisanDevRPLInfo) {
        this.nonArtisanDevRPLInfo = nonArtisanDevRPLInfo;
    }
}
