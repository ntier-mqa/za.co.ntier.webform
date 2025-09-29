package za.co.ntier.webform.form.viewmodel.component;

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
import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.AnnexureRow;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
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
	
	 /** Toggle buttons on any change */
    @Command
    public void notifyProgramComplete() {
        if (getApplicationProgramVM() != null && getNotifyTarget() != null) {
            org.zkoss.bind.BindUtils.postNotifyChange(null, null, getApplicationProgramVM(), getNotifyTarget());
        }
    }

    /** Commit value while typing so completeness logic sees it */
    @Command
    public void instantEdit(@BindingParam("row") Map<ColumnInfo<?>, Object> row,
                            @BindingParam("col") ColumnInfo<?> col,
                            @BindingParam("ref") Component ref,
                            @BindingParam("newVal") String newVal) {

        // Commit to model
        if (row instanceof AnnexureRow) {
            AnnexureInfo.setCellValue((AnnexureRow) row, col, newVal);
        } else {
            row.put(col, newVal);
        }

        // Clear any previous error bubble while typing
        Clients.clearWrongValue(ref);
        if (ref instanceof InputElement ie) ie.setErrorMessage(null);

        // Re-evaluate buttons
        notifyProgramComplete();
    }

    /** Show the red error bubble when leaving the field */
    @Command
    public void blurValidate(@BindingParam("row") Map<ColumnInfo<?>, Object> row,
                             @BindingParam("col") ColumnInfo<?> col,
                             @BindingParam("ref") Component ref,
                             @BindingParam("newVal") String newVal) {

        String v = newVal == null ? null : newVal.trim();

        if (Boolean.TRUE.equals(col.isMandatory())) {
            if (v == null || v.isEmpty()) {
                throw new WrongValueException(ref,
                        (col.getTitle() != null ? col.getTitle() : "This field") + " is required");
            }
        }

        // If valid, ensure any previous error is cleared
        Clients.clearWrongValue(ref);
        if (ref instanceof InputElement ie) ie.setErrorMessage(null);

        // Optionally re-check completeness on blur too
        notifyProgramComplete();
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
