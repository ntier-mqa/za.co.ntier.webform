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
import org.zkoss.zk.ui.event.UploadEvent;

import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.ColumnInfo;

public class AnnexureTableVMWrapper {
	private AnnexureInfo annexureInfo;

	/**
	 * @return the annexureInfo
	 */
	public AnnexureInfo getAnnexureInfo() {
		return annexureInfo;
	}

	@Init
	public void init(@ExecutionArgParam("annexureInfo") AnnexureInfo annexureInfo) {
		this.setAnnexureInfo(annexureInfo);
	}

	@Command
	public void numChange(@BindingParam("annexure") AnnexureInfo annexure,
			@BindingParam("row") Map<ColumnInfo<?>, Object> row, @BindingParam("col") ColumnInfo<?> col,
			@ContextParam(ContextType.TRIGGER_EVENT) InputEvent event) throws IOException {
		if (annexure.isShowTotal()) {
			annexure.numChange(row, col, event);
		}

	}

	/**
	 * @param annexureInfo the annexureInfo to set
	 */
	public void setAnnexureInfo(AnnexureInfo annexureInfo) {
		this.annexureInfo = annexureInfo;
	}

	@Command
	public void uploadFile(@BindingParam("annexure") AnnexureInfo annexure,
			@BindingParam("row") Map<ColumnInfo<?>, Object> row, @BindingParam("col") ColumnInfo<?> col,
			@ContextParam(ContextType.TRIGGER_EVENT) UploadEvent event) throws IOException {
		annexure.uploadFile(row, col, event);
	}
	
	@Command
	public void addDetailLine(@BindingParam("annexure") AnnexureInfo annexure) {
		annexure.addRow();
	}
}
