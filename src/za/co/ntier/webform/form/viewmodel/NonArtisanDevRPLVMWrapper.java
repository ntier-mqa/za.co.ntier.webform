package za.co.ntier.webform.form.viewmodel;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import za.co.ntier.webform.form.bean.NonArtisanDevRPLInfo;

public class NonArtisanDevRPLVMWrapper {
    private NonArtisanDevRPLInfo nonArtisanDevRPLInfo;

    @Init
    public void init(@ExecutionArgParam("nonArtisanDevRPLInfo") NonArtisanDevRPLInfo nonArtisanDevRPLInfo) {
        this.setNonArtisanDevRPLInfo(nonArtisanDevRPLInfo);
    }

    public NonArtisanDevRPLInfo getNonArtisanDevRPLInfo() {
        return nonArtisanDevRPLInfo;
    }

    public void setNonArtisanDevRPLInfo(NonArtisanDevRPLInfo nonArtisanDevRPLInfo) {
        this.nonArtisanDevRPLInfo = nonArtisanDevRPLInfo;
    }
}
