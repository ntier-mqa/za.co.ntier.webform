package za.co.ntier.webform.sdr.component.viewmodel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.adempiere.webui.panel.RegistrationWindow;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.sys.BinderCtrl;
import org.zkoss.bind.sys.ValidationMessages;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.event.UploadEvent;

import za.co.ntier.api.model.MUser_New;
import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;

@Init(superclass = true)
public class CellRenderVM extends BaseComponentVM<RowModel>{
	
	public static class BaseValidator extends AbstractValidator{
		CellModel cellModel;
		Object value;
		CellRenderVM vm;
		List<String> msgs = new ArrayList<>();
		
		void doValidate(ValidationContext ctx) {
			
		}
		
		@Override
		public void validate(ValidationContext ctx) {
			msgs.clear();
			cellModel = (CellModel)ctx.getValidatorArg("cellModel");
			value = ctx.getProperty().getValue();
			vm = (CellRenderVM)ctx.getBindContext().getValidatorArg("vm");
			
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
		return getComponent().getAnnexure().getColumnInfos();

	}
	
	public RowModel getRow() {
		return getComponent();
	}
	
	public TableModel getTableModel() {
		return getComponent().getAnnexure();
	}

	
	@Command
	public void cmdValueChanged(
			@BindingParam("annexure") TableModel annexure
			, @BindingParam("row") Map<ColumnModel, Object> row
			, @BindingParam("col") ColumnModel col
			, @ContextParam(ContextType.TRIGGER_EVENT) InputEvent event) throws IOException {
		annexure.cmdValueChanged(row, col, event);
		//notifyProgramComplete();
	}

	@Command
	public void cmdSelected(
			@BindingParam("annexure") TableModel annexure
			, @BindingParam("row") Map<ColumnModel, Object> row
			, @BindingParam("col") ColumnModel col
			, @ContextParam(ContextType.TRIGGER_EVENT) SelectEvent<?, ?> event) throws IOException {
		annexure.cmdSelected(row, col, event);
	}
	
	@Command
	public void cmdCheckeck(
			@BindingParam("annexure") TableModel annexure
			, @BindingParam("row") Map<ColumnModel, Object> row
			, @BindingParam("col") ColumnModel col
			, @ContextParam(ContextType.TRIGGER_EVENT) CheckEvent event) throws IOException {
		annexure.cmdCheckeck(row, col, event);
	}
	
	@Command
	public void cmdBtClick(
			@BindingParam("annexure") TableModel annexure
			, @BindingParam("row") RowModel row
			, @BindingParam("col") ColumnModel col
			, @ContextParam(ContextType.TRIGGER_EVENT) Event event) throws IOException {
		annexure.cmdCellAction(row, col, event);
	}
	
	@Command
	public void cmdUploadFile(
			@BindingParam("annexure") TableModel annexure
			, @BindingParam("row") Map<ColumnModel, Object> row
			, @BindingParam("col") ColumnModel col
			, @ContextParam(ContextType.TRIGGER_EVENT) UploadEvent event) throws IOException {
		annexure.cmdUploadFile(row, col, event);
	}

	/**
	 * @return the formView
	 */
	public boolean isFormView() {
		return getComponent().getAnnexure().isFormView();
	}
	
	public boolean showTitle(CellModel cell) {
		if (cell.getColModel().getShowTitle() == null) {
			if (isFormView() && cell.getCellType() == CellModel.CHECK_CELL)
				return false;
			else{
				return true;
			}
		}else {
			return cell.getColModel().getShowTitle().booleanValue();
		}
	}
}
