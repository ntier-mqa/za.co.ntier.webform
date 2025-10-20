package za.co.ntier.webform.sdr.component.viewmodel;

import java.util.Map;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.impl.InputElement;

import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.form.bean.ProgramType;
import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.AnnexureRow;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.viewmodel.DiscretionaryGrantsApplicationProgramVM;

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
