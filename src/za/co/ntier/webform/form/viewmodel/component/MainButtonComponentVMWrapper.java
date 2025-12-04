package za.co.ntier.webform.form.viewmodel.component;

import java.io.IOException;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.DependsOn;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.GlobalCommand;
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


	@Init(superclass = true)
	public void init(
			@ExecutionArgParam("tabType") TabType tabType,
			@ExecutionArgParam("tab") Tabbox tab,
			@ExecutionArgParam("subTab") Tabbox subTab,
			@ExecutionArgParam("continueGate") String continueGate,   // <-- add
			@ExecutionArgParam("submitGate")   String submitGate      // <-- add
			) {
		setComponent(new MainButtonComponent(getApplicationProgramVM(), tabType, tab));
		getComponent().setSubTab(subTab);
		this.continueGate = (continueGate != null) ? continueGate : "NONE"; // <-- set
		this.submitGate   = (submitGate   != null) ? submitGate   : "NONE"; // <-- set
	}

	
	
	@DependsOn({
	    "component.applicationProgramVM.selectedTabIndex", 
	    "component.applicationProgramVM.declarationComplete",
	    "component.applicationProgramVM.organisationComplete",
	    "component.applicationProgramVM.programComplete",
	    "component.applicationProgramVM.programContactComplete"
	})
	public boolean isNextDisabled() {
	    int tabIndex = component.getApplicationProgramVM().getSelectedTabIndex();
	    DiscretionaryGrantsApplicationProgramVM vm = component.getApplicationProgramVM();

	    switch (continueGate) {
	    case "DECLARATION":
	        return (tabIndex == 0) ? !vm.isDeclarationComplete() : true;

	    case "ORG":
	        return (tabIndex >= 1) ? !vm.isOrganisationComplete() : true;

	    case "PROGRAM": {
	        // Special handling for Standards Setting (has subtabs)
	        if (vm.getProgram() instanceof za.co.ntier.webform.form.bean.program.StandardSetting) {
	            Tabbox sub = component.getSubTab();
	            if (sub != null && sub.getTabs() != null && !sub.getTabs().getChildren().isEmpty()) {
	                int lastIdx = sub.getTabs().getChildren().size() - 1;
	                int idx     = sub.getSelectedIndex();

	                // If we are NOT on the last sub-tab (i.e. not Invigilators),
	                // Next must always be enabled.
	                if (idx >= 0 && idx < lastIdx) {
	                    return false;   // enabled
	                }
	                // If we ARE on the last sub-tab (Invigilators),
	                // fall through to normal PROGRAM gate, which uses programComplete
	                // (StandardSetting.isProgramValid()).
	            }
	        }
	        return (tabIndex >= 2) ? !vm.isProgramComplete() : true;
	    }

	    case "PROGRAMCONTACT":
	        return (tabIndex >= 3) ? !vm.isProgramContactComplete() : true;

	    default:
	        return false;
	    }
	}



	@DependsOn({
		"component.applicationProgramVM.programContactComplete"
	})
	public boolean isSubmitDisabled() {
		DiscretionaryGrantsApplicationProgramVM vm = component.getApplicationProgramVM();
		switch (submitGate) {
		case "CONTACT": return !vm.isProgramContactComplete();
		case "DOCUPLOAD": return !vm.isMandatoryDocsUploaded();
		default:        return false;
		}
	}

	

	@Command
	public void nextTab() {
	    Tabbox subTab = getComponent().getSubTab();
	    if (subTab != null) {
	        int tabCount     = subTab.getTabs().getChildren().size();
	        int currentIndex = subTab.getSelectedIndex();
	        currentIndex++;
	        if (currentIndex < tabCount) {
	            subTab.setSelectedIndex(currentIndex);

	            // tell bindings that selection changed so nextDisabled refreshes
	            org.zkoss.bind.BindUtils.postGlobalCommand(null, null, "tabSelectionChanged", null);
	            return;
	        }
	    }

	    // (rest of your method unchanged...)
	    if (getComponent().getTab().getSelectedIndex() == 0) {
	        if (!Boolean.TRUE.equals(getComponent().getApplicationProgramVM()
	                .getEmployerDeclarationInfo().getAcknowledged())) {
	            Clients.showNotification(
	                    "Please tick the acknowledgement checkbox before continuing.",
	                    "error", null, "end_center", 3000
	            );
	            return;
	        }
	    }

	    int next = component.getApplicationProgramVM().getSelectedTabIndex() + 1;
	    getComponent().getTab().setSelectedIndex(next);
	    component.getApplicationProgramVM().setSelectedTabIndex(next);
	    org.zkoss.bind.BindUtils.postGlobalCommand(null, null, "tabSelectionChanged", null);
	}




	@GlobalCommand("tabSelectionChanged")   // name must match the one you post
	public void onTabSelectionChanged() {
		// Force re-evaluation of the getters bound in ZUL
		org.zkoss.bind.BindUtils.postNotifyChange(null, null, this, "nextDisabled");
		org.zkoss.bind.BindUtils.postNotifyChange(null, null, this, "submitDisabled");
	}

	/*
	@Command
	public void prevTab() {
		getComponent().getApplicationProgramVM().prevTab(getComponent().getTab());
	}
	*/
	
	@Command
	public void prevTab() {
		Tabbox subTab = getComponent().getSubTab();
		if (subTab != null) {
			int currentIndex = subTab.getSelectedIndex();
			if (currentIndex > 0) {
				currentIndex--;	
				subTab.setSelectedIndex(currentIndex);
				
				 // IMPORTANT: tell ZK to recompute nextDisabled / submitDisabled
	            org.zkoss.bind.BindUtils.postGlobalCommand(null, null, "tabSelectionChanged", null);
				return;
			}
		}
		
	    int prev = Math.max(0, component.getApplicationProgramVM().getSelectedTabIndex() - 1);
	    getComponent().getTab().setSelectedIndex(prev);
	    component.getApplicationProgramVM().setSelectedTabIndex(prev);
	    org.zkoss.bind.BindUtils.postGlobalCommand(null, null, "tabSelectionChanged", null);
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
