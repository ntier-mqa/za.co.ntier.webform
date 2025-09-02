package za.co.ntier.webform.form.viewmodel;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;

public class ComponentVMWrapper<T> {
	public static final String ComponentKey = "component";
	private T component;

	/**
	 * @return the component
	 */
	public T getComponent() {
		return component;
	}

	@Init
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo,
			@ExecutionArgParam(ComponentKey) T component) throws Exception {
		this.component = component;
	}

	/**
	 * @param component the component to set
	 */
	public void setComponent(T component) {
		this.component = component;
	}

}
