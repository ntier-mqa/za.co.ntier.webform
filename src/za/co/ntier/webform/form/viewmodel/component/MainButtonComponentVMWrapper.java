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

	// === Reactive disabled flags ===
	@DependsOn({
		"component.applicationProgramVM.selectedTabIndex", 
		"component.applicationProgramVM.declarationComplete",
		"component.applicationProgramVM.organisationComplete",
		"component.applicationProgramVM.programComplete",
		"component.applicationProgramVM.programContactComplete"
	})
	public boolean isNextDisabled() {
		//int tabIndex = getComponent().getTab().getSelectedIndex();
		int tabIndex = component.getApplicationProgramVM().getSelectedTabIndex();
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
		Tabbox subTab = getComponent().getSubTab();
		if (subTab != null) {
			int tabCount = subTab.getTabs().getChildren().size();
			int currentIndex = subTab.getSelectedIndex();
			currentIndex++;
			if (currentIndex < tabCount) {
				
				subTab.setSelectedIndex(currentIndex);
				return;
			}
		}
		
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

		int next = component.getApplicationProgramVM().getSelectedTabIndex() + 1;

		// 1) move the UI
		getComponent().getTab().setSelectedIndex(next);

		// 2) update the VM (this is what @DependsOn listens to)
		component.getApplicationProgramVM().setSelectedTabIndex(next);

		// 3) (optional) if you still want to poke other binders
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
