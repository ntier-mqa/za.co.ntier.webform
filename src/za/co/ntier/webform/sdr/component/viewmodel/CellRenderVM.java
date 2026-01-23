package za.co.ntier.webform.sdr.component.viewmodel;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
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

import za.co.ntier.api.model.MUser_New;
import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;

@Init(superclass = true)
public class CellRenderVM extends BaseComponentVM<RowModel>{
	
	public static class PhoneValidator extends AbstractValidator{

		@Override
		public void validate(ValidationContext ctx) {
			String phoneInput = (String)ctx.getProperty().getValue();
			CellModel cellModel = (CellModel)ctx.getValidatorArg("cellModel");
			CellRenderVM vm = (CellRenderVM)ctx.getBindContext().getValidatorArg("vm");
			if(StringUtils.isNoneBlank(phoneInput) && !MUser_New.isValidPhoneNumber(phoneInput)) {
				addInvalidMessage(ctx, vm.getRandom(), "Phone number must be in format +27999999999");
			}
		}
		
	}
	
	private Validator phoneValidator = new PhoneValidator();
	
	String random = UUID.randomUUID().toString();
	
	public String getRandom() {
		return random;
	}
	
	public Validator getPhoneValidator() {
		return phoneValidator;
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
