package za.co.ntier.webform.sdr.component.viewmodel;

import java.io.IOException;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.event.UploadEvent;

import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;

@Init(superclass = true)
public class DataFormViewVM extends BaseComponentVM<TableModel>{

	@Command
	public void cmdValueChanged(
			@BindingParam("tableModel") TableModel tableModel
			, @BindingParam("row") RowModel rowModel
			, @BindingParam("col") ColumnModel colModel
			, @ContextParam(ContextType.TRIGGER_EVENT) InputEvent event) throws IOException {
		tableModel.cmdValueChanged(rowModel, colModel, event);
		//notifyProgramComplete();
	}

	@Command
	public void cmdSelected(
			@BindingParam("tableModel") TableModel tableModel
			, @BindingParam("row") RowModel rowModel
			, @BindingParam("col") ColumnModel colModel
			, @ContextParam(ContextType.TRIGGER_EVENT) SelectEvent<?, ?> event) throws IOException {
		tableModel.cmdSelected(rowModel, colModel, event);
	}
	
	@Command
	public void cmdCheckeck(
			@BindingParam("tableModel") TableModel tableModel
			, @BindingParam("row") RowModel rowModel
			, @BindingParam("col") ColumnModel colModel
			, @ContextParam(ContextType.TRIGGER_EVENT) CheckEvent event) throws IOException {
		tableModel.cmdCheckeck(rowModel, colModel, event);
	}
	
	@Command
	public void cmdUploadFile(
			@BindingParam("tableModel") TableModel tableModel
			, @BindingParam("row") RowModel rowModel
			, @BindingParam("col") ColumnModel colModel
			, @ContextParam(ContextType.TRIGGER_EVENT) UploadEvent event) throws IOException {
		tableModel.cmdUploadFile(rowModel, colModel, event);
	}

}
