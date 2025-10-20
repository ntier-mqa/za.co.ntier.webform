package za.co.ntier.webform.sdr.component.viewmodel;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.form.bean.ProgramType;

public class BaseComponentVM<T> {
	public static final String ComponentKey = "component";
	private T component;  // Martin changed to protected
	private MenuContextInfo menuContextInfo;
	/**
	 * @return the component
	 */
	public T getComponent() {
		return component;
	}

	@Init
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo
			, @ExecutionArgParam(ComponentKey) T component) throws Exception {
		this.component = component;
		this.menuContextInfo = menuContextInfo;
		afterParentInit();
	}

	public ProgramType getProgramType() {
		return menuContextInfo.getProgramType();
	}

	public void afterParentInit() {

	}
	/**
	 * @param component the component to set
	 */
	public void setComponent(T component) {
		this.component = component;
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
