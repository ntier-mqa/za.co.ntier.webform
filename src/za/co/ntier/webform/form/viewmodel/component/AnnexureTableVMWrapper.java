package za.co.ntier.webform.form.viewmodel.component;

import java.io.IOException;
import java.util.Map;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.impl.InputElement;

import za.co.ntier.webform.form.InlineValidators;
import za.co.ntier.webform.form.bean.DataType;
import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.AnnexureRow;
import za.co.ntier.webform.form.bean.component.ColumnInfo;

public class AnnexureTableVMWrapper extends ComponentVMWrapper<AnnexureInfo>{
	private boolean isSubAnnexure = false;

	@Command
	public void addDetailLine(@BindingParam("annexure") AnnexureInfo annexure) {
		annexure.addRow();
		notifyProgramComplete();
	}

	@Command
	public void areaSelect(@BindingParam("annexure") AnnexureInfo annexure,
			@BindingParam("row") Map<ColumnInfo<?>, Object> row, @BindingParam("col") ColumnInfo<?> col,
			@ContextParam(ContextType.TRIGGER_EVENT) SelectEvent<?, ?> event) throws IOException {
		annexure.areaSelect(row, col, event);
		notifyProgramComplete(); 
	}

	@Init(superclass = true)
	public void init(
			@ExecutionArgParam("isSubAnnexure") Boolean isSubAnnexure
			) {
		if (isSubAnnexure == null)
			this.isSubAnnexure = false;
		else
			this.isSubAnnexure = isSubAnnexure;
	}

	/**
	 * @return the isSubAnnexure
	 */
	public boolean isSubAnnexure() {
		return isSubAnnexure;
	}
	/** Notify parent VM that program completeness may have changed */
	@Command
	public void notifyProgramComplete() { 
		if (getApplicationProgramVM() != null && getNotifyTarget() != null) {
			org.zkoss.bind.BindUtils.postNotifyChange(null, null, getApplicationProgramVM(), getNotifyTarget());
		}
	}

	@Command
	public void numChange(@BindingParam("annexure") AnnexureInfo annexure,
			@BindingParam("row") AnnexureRow row, @BindingParam("col") ColumnInfo<?> col,
			@ContextParam(ContextType.TRIGGER_EVENT) InputEvent event) throws IOException {

		annexure.numChange(row, col, event);

		notifyProgramComplete();

	}

	@Command
	public void postalChange(@BindingParam("annexure") AnnexureInfo annexure,
			@BindingParam("row") Map<ColumnInfo<?>, Object> row, @BindingParam("col") ColumnInfo<?> col,
			@ContextParam(ContextType.TRIGGER_EVENT) InputEvent event) throws IOException {
		annexure.postalChange(row, col, event);
		notifyProgramComplete(); 
	}

	/**
	 * @param isSubAnnexure the isSubAnnexure to set
	 */
	public void setSubAnnexure(boolean isSubAnnexure) {
		this.isSubAnnexure = isSubAnnexure;
	}

	@Command
	public void uploadFile(@BindingParam("annexure") AnnexureInfo annexure,
			@BindingParam("row") Map<ColumnInfo<?>, Object> row, @BindingParam("col") ColumnInfo<?> col,
			@ContextParam(ContextType.TRIGGER_EVENT) UploadEvent event) throws IOException {
		annexure.uploadFile(row, col, event);
	}

	@Command
	public void validateContact(@BindingParam("row") AnnexureRow row,
			@BindingParam("col") ColumnInfo<?> col,
			@BindingParam("ref") Component ref) {
		InlineValidators.validateContactField(ref, row, col);
	}
	
	@Command
	public void instantEdit(@BindingParam("row") AnnexureRow row,
	                        @BindingParam("col") ColumnInfo<?> col,
	                        @BindingParam("ref") Component ref,
	                        @BindingParam("newVal") String newVal) {

	    // 1) Commit latest keystroke so completeness check sees up-to-date value
	    if (col.getDataType() == DataType.Text) {
	        AnnexureInfo.setCellValue(row, col, newVal);
	    }

	    // 2) Clear any previous inline error on the field
	    Clients.clearWrongValue(ref);
	    if (ref instanceof InputElement ie) ie.setErrorMessage(null);

	    // 3) Validate email/phone; ALWAYS notify completeness, even if invalid
	    String dao = col.getDaoPropertyName();
	    String v = newVal == null ? null : newVal.trim();
	    boolean invalid = false;
	    String err = null;

	    if (dao != null) {
	        String key = dao.toLowerCase();
	        if (key.endsWith("emailwriter")) {
	            invalid = (v == null || !v.matches("^[^@\\s]+@[^@\\s]+\\.[A-Za-z]{2,}$"));
	            if (invalid) err = "Please enter a valid email address";
	        } else if (key.endsWith("cellnowriter")) {
	            invalid = (v == null || !v.matches("^\\s*\\d{10}\\s*$"));
	            if (invalid) err = "Cell number must be exactly 10 digits";
	        }
	    }

	    // 4) Tell the application VM to re-evaluate program completeness
	    //    (this is your existing hook that triggers isProgramComplete()/isNextDisabled())
	    notifyProgramComplete();

	    // 5) If invalid, now throw to paint the field red and block commit
	    if (invalid) {
	        throw new WrongValueException(ref, err);
	    }
	}
}
