package za.co.ntier.webform.form.viewmodel.component;

import java.io.IOException;
import java.util.Map;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.event.UploadEvent;

import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.ColumnInfo;

public class AnnexureTableVMWrapper {
	private AnnexureInfo annexureInfo;

	private boolean isSubAnnexure = false;

	@Command
	public void addDetailLine(@BindingParam("annexure") AnnexureInfo annexure) {
		annexure.addRow();
	}
	
	@Command
	public void areaSelect(@BindingParam("annexure") AnnexureInfo annexure,
			@BindingParam("row") Map<ColumnInfo<?>, Object> row, @BindingParam("col") ColumnInfo<?> col,
			@ContextParam(ContextType.TRIGGER_EVENT) SelectEvent<?, ?> event) throws IOException {
		annexure.areaSelect(row, col, event);
	}
	
	/**
	 * @return the annexureInfo
	 */
	public AnnexureInfo getAnnexureInfo() {
		return annexureInfo;
	}
	
	@Init
	public void init(@ExecutionArgParam("annexureInfo") AnnexureInfo annexureInfo,
			@ExecutionArgParam("isSubAnnexure") Boolean isSubAnnexure
			) {
		this.setAnnexureInfo(annexureInfo);
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

	@Command
	public void numChange(@BindingParam("annexure") AnnexureInfo annexure,
			@BindingParam("row") Map<ColumnInfo<?>, Object> row, @BindingParam("col") ColumnInfo<?> col,
			@ContextParam(ContextType.TRIGGER_EVENT) InputEvent event) throws IOException {
		if (annexure.isShowTotal()) {
			annexure.numChange(row, col, event);
		}

	}

	@Command
	public void postalChange(@BindingParam("annexure") AnnexureInfo annexure,
			@BindingParam("row") Map<ColumnInfo<?>, Object> row, @BindingParam("col") ColumnInfo<?> col,
			@ContextParam(ContextType.TRIGGER_EVENT) InputEvent event) throws IOException {
		annexure.postalChange(row, col, event);
	}
	
	/**
	 * @param annexureInfo the annexureInfo to set
	 */
	public void setAnnexureInfo(AnnexureInfo annexureInfo) {
		this.annexureInfo = annexureInfo;
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
}
