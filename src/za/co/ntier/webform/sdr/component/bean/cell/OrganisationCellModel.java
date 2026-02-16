package za.co.ntier.webform.sdr.component.bean.cell;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.event.Event;

import za.co.ntier.api.model.I_ZZ_WSP_ATR_EXTENSION;
import za.co.ntier.api.model.I_ZZSdfOrganisation_v;
import za.co.ntier.webform.form.WspAtrExtensionConstants;
import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.column.ListColumnModel;


/**
 * @author niraj
 */
public class OrganisationCellModel extends ListCellModel<Map<String, Object>>
{

	public OrganisationCellModel(TableModel tableModel, RowModel rowModel,
			ListColumnModel<Map<String, Object>> colModel)
	{
		super(tableModel, rowModel, colModel);
	}

	@Override
	public void cmdSelectedHandle(Event selectedEvent)
	{
		Map<String, Object> selectedOrg = getSelectedObj(selectedEvent);

		if (selectedOrg == null)
		{
			clearSDLAndEmployees();
			return;
		}

		String sdlNumber = (String) selectedOrg.get(WspAtrExtensionConstants.BP_SEARCH_KEY);
		Object employees = selectedOrg.get(WspAtrExtensionConstants.BP_NUMBER_OF_EMPLOYEES);

		updateSDLField(sdlNumber);
		updateEmployeesField(employees);
	}

	@Override
	public Object getValue()
	{
		Object value = super.getValue();

		if (value instanceof Map)
		{
			@SuppressWarnings("unchecked")
			Map<String, Object> orgMap = (Map<String, Object>) value;
			return orgMap.get(I_ZZSdfOrganisation_v.COLUMNNAME_OrgName);
		}

		return value;
	}

	private void updateSDLField(String sdlNumber)
	{
		CellModel sdlCell = findCellByColumnName(I_ZZ_WSP_ATR_EXTENSION.COLUMNNAME_ZZ_SDL_No);
		if (sdlCell != null)
		{
			sdlCell.setValue(sdlNumber);
			BindUtils.postNotifyChange(sdlCell, "value");
		}
	}

	private void updateEmployeesField(Object employees)
	{
		CellModel empCell = findCellByColumnName(I_ZZ_WSP_ATR_EXTENSION.COLUMNNAME_ZZ_Number_Of_Employees);
		if (empCell != null && employees != null)
		{
			BigDecimal empCount = convertToEmployeeCount(employees);
			if (empCount != null)
			{
				empCell.setValue(empCount);
				BindUtils.postNotifyChange(empCell, "value");
			}
		}
	}

	private CellModel findCellByColumnName(String columnName)
	{
		if (getTableModel() == null || getRowModel() == null)
		{
			return null;
		}

		for (CellModel cell : getRowModel().values())
		{
			if (cell.getColModel() != null && columnName.equals(cell.getColModel().getDaoPropertyName()))
			{
				return cell;
			}
		}

		return null;
	}

	private BigDecimal convertToEmployeeCount(Object employees)
	{
		if (employees instanceof Integer)
		{
			return BigDecimal.valueOf((Integer) employees);
		}
		else if (employees instanceof BigDecimal)
		{
			return (BigDecimal) employees;
		}
		else
		{
			try
			{
				return new BigDecimal(employees.toString());
			}
			catch (Exception e)
			{
				return null;
			}
		}
	}

	private void clearSDLAndEmployees()
	{
		CellModel sdlCell = findCellByColumnName(I_ZZ_WSP_ATR_EXTENSION.COLUMNNAME_ZZ_SDL_No);
		if (sdlCell != null)
		{
			sdlCell.setValue(null);
			BindUtils.postNotifyChange(sdlCell, "value");
		}

		CellModel empCell = findCellByColumnName(I_ZZ_WSP_ATR_EXTENSION.COLUMNNAME_ZZ_Number_Of_Employees);
		if (empCell != null)
		{
			empCell.setValue(null);
			BindUtils.postNotifyChange(empCell, "value");
		}
	}

	@Override
	public String getDisplayText(Map<String, Object> item)
	{
		if (item == null)
		{
			return "";
		}
		return (String) item.get(I_ZZSdfOrganisation_v.COLUMNNAME_OrgName);
	}

	public static ListColumnModel<Map<String, Object>> getOrganisationColumnModel(String title, String daoPropertyName,
			List<Map<String, Object>> organisations)
	{

		if (title == null)
		{
			title = "Organisation Name";
		}

		@SuppressWarnings("unchecked")
		ListColumnModel<Map<String, Object>> listColumnModel = ListCellModel.getListColumnModel(ListColumnModel.class,
				OrganisationCellModel.class, title, daoPropertyName, organisations, null, null, CellModel.LIST_CELL);

		return listColumnModel;
	}
}
