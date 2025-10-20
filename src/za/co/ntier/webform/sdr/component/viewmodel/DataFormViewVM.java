package za.co.ntier.webform.sdr.component.viewmodel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.SelectEvent;

import za.co.ntier.api.model.I_ZZ_FormContact;
import za.co.ntier.api.model.X_ZZ_Application_Form;
import za.co.ntier.api.model.X_ZZ_FormContact;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.bean.AddressType;
import za.co.ntier.webform.form.viewmodel.component.ComponentVMWrapper;
import za.co.ntier.webform.sdr.component.bean.BaseCellModel;
import za.co.ntier.webform.sdr.component.bean.BaseColumnModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.cell.AreaCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.PostalCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.ProvinceCellModel;

@Init(superclass = true)
public class DataFormViewVM extends BaseComponentVM<TableModel>{

	private X_ZZ_Application_Form applicationForm;
	
	@Command
	public void cmdValueChanged(
			@BindingParam("annexure") TableModel annexure
			, @BindingParam("row") Map<BaseColumnModel, Object> row
			, @BindingParam("col") BaseColumnModel col
			, @ContextParam(ContextType.TRIGGER_EVENT) InputEvent event) throws IOException {
		annexure.cmdValueChanged(row, col, event);
		//notifyProgramComplete(); 
	}
	
	@Command
	public void cmdSelected(
			@BindingParam("annexure") TableModel annexure
			, @BindingParam("row") Map<BaseColumnModel, Object> row
			, @BindingParam("col") BaseColumnModel col
			, @ContextParam(ContextType.TRIGGER_EVENT) SelectEvent<?, ?> event) throws IOException {
		annexure.cmdSelected(row, col, event);
	}
	
}
