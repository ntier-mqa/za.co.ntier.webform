package za.co.ntier.webform.form.viewmodel.component;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.form.viewmodel.DiscretionaryGrantsApplicationProgramVM;

public class ComponentVMWrapper<T> {
	public static final String ComponentKey = "component";
	protected T component;  // Martin changed to protected
	private DiscretionaryGrantsApplicationProgramVM applicationProgramVM;
	private String notifyTarget;
	private MenuContextInfo menuContextInfo;
	/**
	 * @return the component
	 */
	public T getComponent() {
		return component;
	}

	@Init
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo,
			@ExecutionArgParam(ComponentKey) T component,
			@ExecutionArgParam("applicationProgramVM") DiscretionaryGrantsApplicationProgramVM applicationProgramVM,
			@ExecutionArgParam("notifyTarget") String notifyTarget) throws Exception {
		this.component = component;
		this.menuContextInfo = menuContextInfo;
		this.applicationProgramVM = applicationProgramVM;
		this.notifyTarget = notifyTarget;
		afterInit();
	}

	public void afterInit() {
		
	}
	
	/**
	 * @param component the component to set
	 */
	public void setComponent(T component) {
		this.component = component;
	}

	/**
	 * @return the applicationProgramVM
	 */
	public DiscretionaryGrantsApplicationProgramVM getApplicationProgramVM() {
		return applicationProgramVM;
	}

	/**
	 * @param applicationProgramVM the applicationProgramVM to set
	 */
	public void setApplicationProgramVM(DiscretionaryGrantsApplicationProgramVM applicationProgramVM) {
		this.applicationProgramVM = applicationProgramVM;
	}

	/**
	 * @return the notifyTarget
	 */
	public String getNotifyTarget() {
		return notifyTarget;
	}

	/**
	 * @param notifyTarget the notifyTarget to set
	 */
	public void setNotifyTarget(String notifyTarget) {
		this.notifyTarget = notifyTarget;
	}

	/**
	 * @return the menuContextInfo
	 */
	public MenuContextInfo getMenuContextInfo() {
		return menuContextInfo;
	}

	/**
	 * @param menuContextInfo the menuContextInfo to set
	 */
	public void setMenuContextInfo(MenuContextInfo menuContextInfo) {
		this.menuContextInfo = menuContextInfo;
	}

}
