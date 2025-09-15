package za.co.ntier.webform.form.viewmodel.component;

import java.io.IOException;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.DependsOn;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Tabbox;

import za.co.ntier.webform.form.bean.MainButtonComponent;
import za.co.ntier.webform.form.bean.TabType;
import za.co.ntier.webform.form.viewmodel.DiscretionaryGrantsApplicationProgramVM;

public class MainButtonComponentVMWrapper extends ComponentVMWrapper<MainButtonComponent> {
	
	  // Passed in via @init from the parent ZUL (static per panel)
    private String continueGate; // "DECLARATION", "ORG", "PROGRAM", "NONE"
    private String submitGate;   // "CONTACT", "NONE"
	@Command(value = "deleteAppForm")
	public void deleteAppForm() throws IOException {
		getComponent().getApplicationProgramVM().deleteApp();
	}
	
	
	@Init(superclass = false)
	public void init(
		    @ExecutionArgParam("applicationProgramVM") DiscretionaryGrantsApplicationProgramVM applicationProgramVM,
		    @ExecutionArgParam("tabType") TabType tabType,
		    @ExecutionArgParam("tab") Tabbox tab,
		    @ExecutionArgParam("continueGate") String continueGate,   // <-- add
		    @ExecutionArgParam("submitGate")   String submitGate      // <-- add
			) {
		setComponent(new MainButtonComponent(applicationProgramVM, tabType, tab));
		this.continueGate = (continueGate != null) ? continueGate : "NONE"; // <-- set
		this.submitGate   = (submitGate   != null) ? submitGate   : "NONE"; // <-- set
	}
	
	// === Reactive disabled flags ===
    @DependsOn({
        "component.applicationProgramVM.declarationComplete",
        "component.applicationProgramVM.organisationComplete",
        "component.applicationProgramVM.programComplete",
        "component.applicationProgramVM.programContactComplete"
    })
    public boolean isNextDisabled() {
    	int tabIndex = getComponent().getTab().getSelectedIndex();
        DiscretionaryGrantsApplicationProgramVM vm = component.getApplicationProgramVM();
        switch (continueGate) {
            case "DECLARATION": return (tabIndex == 0) ? !vm.isDeclarationComplete() : true;
            case "ORG":         return (tabIndex >= 1) ? !vm.isOrganisationComplete() : true;
            case "PROGRAM":     return (tabIndex >= 2) ? !vm.isProgramComplete() : true;
            case "PROGRAMCONTACT": return (tabIndex >= 3) ? !vm.isProgramContactComplete() : true;
            default:            return false;
        }
    }
    
	
	@DependsOn({
        "component.applicationProgramVM.programContactComplete"
    })
    public boolean isSubmitDisabled() {
        DiscretionaryGrantsApplicationProgramVM vm = component.getApplicationProgramVM();
        switch (submitGate) {
            case "CONTACT": return !vm.isProgramContactComplete();
            default:        return false;
        }
    }
	
	@Command
	public void nextTab() {
		// Only enforce this when we are on the Declaration tab (index 0).
	    // Adjust if you change tab order.
		// Martin Added 
	    if (getComponent().getTab().getSelectedIndex() == 0) {
	        if (!Boolean.TRUE.equals(getComponent().getApplicationProgramVM().getEmployerDeclarationInfo().getAcknowledged())) {
	            Clients.showNotification(
	                "Please tick the acknowledgement checkbox before continuing.",
	                "error", null, "end_center", 3000
	            );
	            return;
	        }
	    }
		int currentIndex = getComponent().getTab().getSelectedIndex();
		getComponent().getTab().setSelectedIndex(currentIndex + 1);
	}
	
	@Command
	public void prevTab() {
		getComponent().getApplicationProgramVM().prevTab(getComponent().getTab());
	}
	
	 @Command(value = "saveClose")
	public void saveClose() throws IOException {
		getComponent().getApplicationProgramVM().saveClose();
	}

    @Command(value = "submitApplication")
	public void submitApplication() throws IOException {
		getComponent().getApplicationProgramVM().submitApplication();
	}
}
