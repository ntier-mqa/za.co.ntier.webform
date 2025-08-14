package za.co.ntier.webform.form.viewmodel;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import za.co.ntier.webform.form.bean.AETInfo;

public class AETVMWrapper {
    private AETInfo aetInfo;

    @Init
    public void init(@ExecutionArgParam("aetInfo") AETInfo aetInfo) {
        this.setAETInfo(aetInfo);
    }

    public AETInfo getAETInfo() {
        return aetInfo;
    }

    public void setAETInfo(AETInfo aetInfo) {
        this.aetInfo = aetInfo;
    }
}
