package za.co.ntier.webform.form.viewmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.api.model.I_ZZSdfOrganisation_v;
import za.co.ntier.api.model.I_ZZ_WSP_ATR_EXTENSION;
import za.co.ntier.api.model.MUser_New;
import za.co.ntier.api.model.X_ZZ_WSP_ATR_EXTENSION;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.form.WspAtrExtensionConstants;
import za.co.ntier.webform.form.bean.component.FormInfo;
import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.ISaveForm;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.TableModel.DaoManage;
import za.co.ntier.webform.sdr.component.bean.cell.OrganisationCellModel;
import za.co.ntier.webform.sdr.component.tab.bean.NavTab;
import za.co.ntier.webform.sdr.component.tab.bean.NavTabPanel;
import za.co.ntier.webform.sdr.viewmodel.BaseAppVM;

/**
 * ViewModel for WSP-ATR Extension Request Form.
 * @author niraj
 */
public class WspAtrExtensionFormVM extends BaseAppVM
{

	private static final CLogger		log	= CLogger.getCLogger(WspAtrExtensionFormVM.class);

	private MenuContextInfo				menuContextInfo;
	private FormInfo					formInfo;
	private NavTab						mainTab;
	private TableModel					sdfDetails;
	private TableModel					organisationDetails;
	private TableModel					extensionRequest;
	private TableModel					seniorOrgRepresentative;
	private DaoManage					formManage;
	private MUser_New					loggedInUser;
	private X_ZZ_WSP_ATR_EXTENSION		extensionData;
	private List<Map<String, Object>>	linkedOrganisationsList;

	@Init
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey)
	MenuContextInfo menuContextInfo)
	{
		this.menuContextInfo = menuContextInfo;
		setFormInfo(new FormInfo(menuContextInfo));

		int loginUserId = Env.getAD_User_ID(Env.getCtx());
		loggedInUser = new MUser_New(Env.getCtx(), loginUserId, null);

		loadLinkedOrganisations();
		initExtensionData();

		formManage = new DaoManage();
		formManage.setDao(extensionData);

		sdfDetails = getSdfDetailsComp(formManage);
		sdfDetails.setSectionHeader(WspAtrExtensionConstants.SECTION_SDF_DETAILS);
		
		organisationDetails = getOrganisationDetailsComp(formManage);
		organisationDetails.setSectionHeader(WspAtrExtensionConstants.SECTION_ORG_DETAILS);
		
		extensionRequest = getExtensionRequestComp(formManage);
		extensionRequest.setSectionHeader(WspAtrExtensionConstants.SECTION_EXTENSION_REQUEST);
		
		seniorOrgRepresentative = getSeniorOrgRepresentativeComp(formManage);
		seniorOrgRepresentative.setSectionHeader(WspAtrExtensionConstants.SECTION_SENIOR_REP);

		mainTab = new NavTab();
		NavTabPanel extensionTab = new NavTabPanel(mainTab);
		extensionTab.setTabTitle(WspAtrExtensionConstants.TAB_EXTENSION_REQUEST);
		extensionTab.getCompModel().add(sdfDetails);
		extensionTab.getCompModel().add(organisationDetails);
		extensionTab.getCompModel().add(extensionRequest);
		extensionTab.getCompModel().add(seniorOrgRepresentative);
	}

	private void initExtensionData()
	{
		extensionData = new X_ZZ_WSP_ATR_EXTENSION(Env.getCtx(), 0, null);
		extensionData.setZZ_SDF_FirstName(loggedInUser.getFirstName());
		extensionData.setZZ_SDF_Surname(loggedInUser.getLastName());
		extensionData.setAD_Org_ID(menuContextInfo.getProgramMasterData().getAD_Org_ID());
	}

	private void loadLinkedOrganisations()
	{
		try
		{
			List<List<Object>> linkedOrganisationsPo = DB.getSQLArrayObjectsEx(null, buildOrganisationQuery(),
					Env.getAD_User_ID(Env.getCtx()));

			if (linkedOrganisationsPo == null)
			{
				linkedOrganisationsList = new ArrayList<>();
				return;
			}

			linkedOrganisationsList = new ArrayList<>();
			linkedOrganisationsPo.forEach(row -> {
				linkedOrganisationsList.add(mapOrganisationRow(row));
			});

		}
		catch (Exception e)
		{
			log.severe("Error loading linked organisations: " + e.getMessage());
			linkedOrganisationsList = new ArrayList<>();
		}
	}

	private String buildOrganisationQuery()
	{
		return String.format("""
				SELECT
				    sdo.%s,
				    sdo.%s,
				    CASE
				        WHEN sdo.ZZReplacingPrimarySDF = 'Y' THEN '%s'
				        WHEN sdo.ZZSecondarySdf = 'Y' THEN '%s'
				        ELSE '%s'
				    END AS %s,
				    sdo.%s,
				    sdo.%s,
				    sdo.%s,
				    bp.Value AS %s,
				    bp.ZZ_Number_Of_Employees AS %s
				FROM %s sdo
				LEFT JOIN C_BPartner bp ON sdo.C_BPartner_ID = bp.C_BPartner_ID
				WHERE sdo.CreatedBy = ?
				""", I_ZZSdfOrganisation_v.COLUMNNAME_OrgName, I_ZZSdfOrganisation_v.COLUMNNAME_ZZ_SDL_No,
				WspAtrExtensionConstants.SDF_ROLE_PRIMARY, WspAtrExtensionConstants.SDF_ROLE_SECONDARY,
				WspAtrExtensionConstants.SDF_ROLE_PRIMARY, WspAtrExtensionConstants.SDF_ROLE,
				I_ZZSdfOrganisation_v.COLUMNNAME_ZZ_DocStatus, I_ZZSdfOrganisation_v.COLUMNNAME_ZZSdfOrganisation_ID,
				WspAtrExtensionConstants.BP_ID, WspAtrExtensionConstants.BP_SEARCH_KEY,
				WspAtrExtensionConstants.BP_NUMBER_OF_EMPLOYEES, I_ZZSdfOrganisation_v.Table_Name);
	}

	private Map<String, Object> mapOrganisationRow(List<Object> row)
	{
		Map<String, Object> linkedOrganisation = new HashMap<>();
		linkedOrganisation.put(I_ZZSdfOrganisation_v.COLUMNNAME_OrgName, row.get(0));
		linkedOrganisation.put(I_ZZSdfOrganisation_v.COLUMNNAME_ZZ_SDL_No, row.get(1));
		linkedOrganisation.put(WspAtrExtensionConstants.SDF_ROLE, row.get(2));
		linkedOrganisation.put(I_ZZSdfOrganisation_v.COLUMNNAME_ZZ_DocStatus,
				row.get(3) != null ? row.get(3) : WspAtrExtensionConstants.DOC_STATUS_DRAFTED);
		linkedOrganisation.put(I_ZZSdfOrganisation_v.COLUMNNAME_ZZSdfOrganisation_ID, row.get(4));
		linkedOrganisation.put(WspAtrExtensionConstants.BP_ID, row.get(5));
		linkedOrganisation.put(WspAtrExtensionConstants.BP_SEARCH_KEY, row.get(6));
		linkedOrganisation.put(WspAtrExtensionConstants.BP_NUMBER_OF_EMPLOYEES, row.get(7));
		return linkedOrganisation;
	}

	private List<Map<String, Object>> getApprovedPrimarySDFOrganisations()
	{
		List<Map<String, Object>> approvedOrgs = new ArrayList<>();
		for (Map<String, Object> org : linkedOrganisationsList)
		{
			String docStatus = (String) org.get(I_ZZSdfOrganisation_v.COLUMNNAME_ZZ_DocStatus);
			String role = (String) org.get(WspAtrExtensionConstants.SDF_ROLE);

			if (WspAtrExtensionConstants.DOC_STATUS_APPROVED.equalsIgnoreCase(docStatus)
					&& WspAtrExtensionConstants.SDF_ROLE_PRIMARY.equalsIgnoreCase(role))
			{
				approvedOrgs.add(org);
			}
		}
		return approvedOrgs;
	}

	private TableModel getSdfDetailsComp(DaoManage formManage)
	{
		List<ColumnModel> cols = new ArrayList<>();

		cols.add(createReadOnlyColumn(I_ZZ_WSP_ATR_EXTENSION.COLUMNNAME_ZZ_SDF_FirstName));
		cols.add(createReadOnlyColumn(I_ZZ_WSP_ATR_EXTENSION.COLUMNNAME_ZZ_SDF_Surname));

		return createTableModel(cols, formManage,
				WspAtrExtensionConstants.CSS_TWO_COL + " " + WspAtrExtensionConstants.CSS_SDF_SECTION);
	}

	private TableModel getOrganisationDetailsComp(DaoManage formManage)
	{
		List<ColumnModel> cols = new ArrayList<>();
		List<Map<String, Object>> approvedOrganisations = getApprovedPrimarySDFOrganisations();

		ColumnModel organisationCol = OrganisationCellModel.getOrganisationColumnModel(
				getTranslatedColumnName(I_ZZ_WSP_ATR_EXTENSION.COLUMNNAME_ZZ_Organisation_Name),
				I_ZZ_WSP_ATR_EXTENSION.COLUMNNAME_ZZ_Organisation_Name, approvedOrganisations);
		organisationCol.required().setTableName(I_ZZ_WSP_ATR_EXTENSION.Table_Name);
		cols.add(organisationCol);

		cols.add(createReadOnlyColumn(I_ZZ_WSP_ATR_EXTENSION.COLUMNNAME_ZZ_SDL_No));
		cols.add(createReadOnlyNumberColumn(I_ZZ_WSP_ATR_EXTENSION.COLUMNNAME_ZZ_Number_Of_Employees));

		return createTableModel(cols, formManage,
				WspAtrExtensionConstants.CSS_TWO_COL + " " + WspAtrExtensionConstants.CSS_ORG_SECTION);
	}

	private TableModel getExtensionRequestComp(DaoManage formManage)
	{
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel reasonCol = CellModel
				.getColModelForText(getTranslatedColumnName(I_ZZ_WSP_ATR_EXTENSION.COLUMNNAME_ZZ_Reason_For_Extension),
						I_ZZ_WSP_ATR_EXTENSION.COLUMNNAME_ZZ_Reason_For_Extension)
				.required().setTableName(I_ZZ_WSP_ATR_EXTENSION.Table_Name);

		cols.add(reasonCol);

		return createTableModel(cols, formManage, WspAtrExtensionConstants.CSS_EXTENSION_SECTION);
	}

	private TableModel getSeniorOrgRepresentativeComp(DaoManage formManage)
	{
		List<ColumnModel> cols = new ArrayList<>();

		cols.add(createRequiredColumn(I_ZZ_WSP_ATR_EXTENSION.COLUMNNAME_ZZ_SOR_FirstName));
		cols.add(createRequiredColumn(I_ZZ_WSP_ATR_EXTENSION.COLUMNNAME_ZZ_SOR_Surname));
		cols.add(createRequiredColumn(I_ZZ_WSP_ATR_EXTENSION.COLUMNNAME_ZZ_Designation));
		cols.add(createRequiredPhoneColumn(I_ZZ_WSP_ATR_EXTENSION.COLUMNNAME_ZZ_SOR_Phone));
		cols.add(createRequiredEmailColumn(I_ZZ_WSP_ATR_EXTENSION.COLUMNNAME_ZZ_SOR_EMAIL));

		return createTableModel(cols, formManage,
				WspAtrExtensionConstants.CSS_TWO_COL + " " + WspAtrExtensionConstants.CSS_SOR_SECTION);
	}

	// Helper methods
	private ColumnModel createReadOnlyColumn(String columnName)
	{
		return CellModel.getColModelForText(getTranslatedColumnName(columnName), columnName).setReadonly(true)
				.setTableName(I_ZZ_WSP_ATR_EXTENSION.Table_Name);
	}

	private ColumnModel createReadOnlyNumberColumn(String columnName)
	{
		return CellModel.getColModelForPositiveNumber(getTranslatedColumnName(columnName), columnName).setReadonly(true)
				.setTableName(I_ZZ_WSP_ATR_EXTENSION.Table_Name);
	}

	private ColumnModel createRequiredColumn(String columnName)
	{
		return CellModel.getColModelForText(getTranslatedColumnName(columnName), columnName).required()
				.setTableName(I_ZZ_WSP_ATR_EXTENSION.Table_Name);
	}

	private ColumnModel createRequiredPhoneColumn(String columnName)
	{
		return CellModel.getColModelForPhone(getTranslatedColumnName(columnName), columnName).required()
				.setTableName(I_ZZ_WSP_ATR_EXTENSION.Table_Name);
	}

	private ColumnModel createRequiredEmailColumn(String columnName)
	{
		return CellModel.getColModelForEmail(getTranslatedColumnName(columnName), columnName).required()
				.setTableName(I_ZZ_WSP_ATR_EXTENSION.Table_Name);
	}

	private String getTranslatedColumnName(String columnName)
	{
		return MasterUtil.getNameOfColTranslated(I_ZZ_WSP_ATR_EXTENSION.Table_Name, columnName);
	}

	private TableModel createTableModel(List<ColumnModel> cols, DaoManage formManage, String cssClass)
	{
		TableModel tableModel = TableModel.getTableBean(TableModel.class, cols, false);
		tableModel.setSclass(cssClass);
		tableModel.setDaoManage(formManage);
		tableModel.init(null, null);
		return tableModel;
	}

	/**
	 * Returns save components (NavTab). NavTab automatically syncs UI values to
	 * extensionData.
	 */
	@Override
	public List<ISaveForm> getSaveComponents()
	{
		return List.of(mainTab);
	}

	/**
	 * Returns DAO managers. formManage will save extensionData to database.
	 */
	@Override
	public List<DaoManage> getDaoManages()
	{
		return List.of(formManage);
	}

	/**
	 * Returns additional PO objects to save. Empty because formManage handles
	 * extensionData.
	 */
	@Override
	public List<PO> getDaos()
	{
		return List.of();
	}

	@Override
	public void doSubmit(String trxName)
	{
	}

	@Override
	protected void showResult(boolean isSubmit)
	{
		String title = isSubmit ? "Extension Request Submitted" : "Extension Request Saved";

		List<String> msgs = new ArrayList<>();

		if (isSubmit)
		{
			msgs.add("Your extension request has been submitted successfully.");
		}
		else
		{
			msgs.add("Your extension request has been saved.");
		}

		msgs.add("Extension ID: " + extensionData.get_ID());

		MasterUtil.showInfoDialog(title, msgs, t -> {
			MasterUtil.closeActiveWindow();
		});
	}

	// Getters
	public MenuContextInfo getMenuContextInfo()
	{
		return menuContextInfo;
	}

	public void setMenuContextInfo(MenuContextInfo menuContextInfo)
	{
		this.menuContextInfo = menuContextInfo;
	}

	public FormInfo getFormInfo()
	{
		return formInfo;
	}

	public void setFormInfo(FormInfo formInfo)
	{
		this.formInfo = formInfo;
	}

	public TableModel getSdfDetails()
	{
		return sdfDetails;
	}

	public TableModel getOrganisationDetails()
	{
		return organisationDetails;
	}

	public TableModel getExtensionRequest()
	{
		return extensionRequest;
	}

	public TableModel getSeniorOrgRepresentative()
	{
		return seniorOrgRepresentative;
	}

	public NavTab getMainTab()
	{
		return mainTab;
	}

	@Override
	public Object getMainApp()
	{
		return extensionData;
	}
}
