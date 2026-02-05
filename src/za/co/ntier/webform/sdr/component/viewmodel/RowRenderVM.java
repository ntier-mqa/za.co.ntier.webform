package za.co.ntier.webform.sdr.component.viewmodel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.event.UploadEvent;

import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;

@Init(superclass = true)
public class RowRenderVM extends BaseComponentVM<RowModel>{
	
	public static class BaseValidator extends AbstractValidator{
		CellModel cellModel;
		Object value;
		RowRenderVM vm;
		List<String> msgs = new ArrayList<>();
		
		void doValidate(ValidationContext ctx) {
			
		}
		
		@Override
		public void validate(ValidationContext ctx) {
			msgs.clear();
			cellModel = (CellModel)ctx.getValidatorArg("cellModel");
			value = ctx.getProperty().getValue();
			vm = (RowRenderVM)ctx.getBindContext().getValidatorArg("vm");
			
			doValidate(ctx);
			if (msgs.size() > 0)
				addInvalidMessages(ctx, cellModel.getValidateMsgKey(), msgs.toArray(new String[0]));
		}
		
	}
	
	
	public static class CellValidator extends BaseValidator{
		void doValidate(ValidationContext ctx) {
			msgs.addAll(cellModel.validate(value));
		}
	}
	
	private CellValidator cellValidator;
	public Validator getCellValidator() {
		if (cellValidator == null) {
			cellValidator = new CellValidator();
		}
		return cellValidator;
	}
	
	public List<ColumnModel> getCols() {
		return getComponent().getTableModel().getColumnInfos();

	}
	
	public RowModel getRow() {
		return getComponent();
	}
	
	public TableModel getTableModel() {
		return getComponent().getTableModel();
	}

	
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
		rowModel.get(colModel).resetValidate();// clear form validate for listbox
		tableModel.cmdSelected(rowModel, colModel, event);
	}
	
	@Command
	public void cmdCheckeck(
			@BindingParam("tableModel") TableModel tableModel
			, @BindingParam("row") RowModel rowModel
			, @BindingParam("col") ColumnModel colModel
			, @ContextParam(ContextType.TRIGGER_EVENT) CheckEvent event) throws IOException {
		rowModel.get(colModel).resetValidate();
		tableModel.cmdCheckeck(rowModel, colModel, event);
	}
	
	@Command
	public void cmdBtClick(
			@BindingParam("tableModel") TableModel tableModel
			, @BindingParam("row") RowModel rowModel
			, @BindingParam("col") ColumnModel colModel
			, @ContextParam(ContextType.TRIGGER_EVENT) Event event) throws IOException {
		tableModel.cmdCellAction(rowModel, colModel, event);
	}
	
	@Command
	public void cmdUploadFile(
			@BindingParam("tableModel") TableModel tableModel
			, @BindingParam("row") RowModel rowModel
			, @BindingParam("col") ColumnModel colModel
			, @ContextParam(ContextType.TRIGGER_EVENT) UploadEvent event) throws IOException {
		rowModel.get(colModel).resetValidate();
		tableModel.cmdUploadFile(rowModel, colModel, event);
	}

	@Command
	public void cmdRemoveAttachment(@BindingParam("tableModel") TableModel tableModel
			, @BindingParam("row") RowModel rowModel
			, @BindingParam("col") ColumnModel colModel
			, @ContextParam(ContextType.TRIGGER_EVENT) Event event) throws IOException {
		tableModel.cmdRemoveAttachment(rowModel, colModel, event);
		
	}
	
	/**
	 * @return the formView
	 */
	public boolean showFieldTitle(CellModel cell) {
		return (cell.getColModel().getShowTitle() == null || cell.getColModel().getShowTitle()) &&
		(getComponent().getTableModel().isFormView() || getComponent().getTableModel().isCardView()) &&
		cell.getCellType() != CellModel.CHECK_CELL;
		
	}
}
