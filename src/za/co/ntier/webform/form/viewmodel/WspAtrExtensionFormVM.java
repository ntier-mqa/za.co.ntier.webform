package za.co.ntier.webform.form.viewmodel;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.api.model.I_ZZSdf;
import za.co.ntier.api.model.I_ZZSdfOrganisation_v;
import za.co.ntier.api.model.I_ZZ_WSP_ATR_EXTENSION;
import za.co.ntier.api.model.I_ZZ_WSP_ATR_EXTENSION_BATCH;
import za.co.ntier.api.model.MUser_New;
import za.co.ntier.api.model.X_ZZ_WSP_ATR_EXTENSION;
import za.co.ntier.api.model.X_ZZ_WSP_ATR_EXTENSION_BATCH;
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
 * 
 * @author niraj
 */
public class WspAtrExtensionFormVM extends BaseAppVM
{

	private static final CLogger		log	= CLogger.getCLogger(WspAtrExtensionFormVM.class);
	private final Properties			ctx	= Env.getCtx();

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

		int loginUserId = Env.getAD_User_ID(ctx);
		loggedInUser = new MUser_New(ctx, loginUserId, null);

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
		extensionData = new X_ZZ_WSP_ATR_EXTENSION(ctx, 0, null);
		extensionData.setAD_Org_ID(menuContextInfo.getProgramMasterData().getAD_Org_ID());
		extensionData.setZZ_SDF_Phone(loggedInUser.getPhone());
		extensionData.setZZ_SDF_EMAIL(loggedInUser.getEMail());
		extensionData.setZZ_Submission_Date(new Timestamp(System.currentTimeMillis()));
	}

	private void loadLinkedOrganisations()
	{
		try
		{
			List<List<Object>> linkedOrganisationsPo = DB.getSQLArrayObjectsEx(null, buildOrganisationQuery(),
					Env.getAD_User_ID(ctx));

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
				    %s AS %s,
				    sdo.%s,
				    sdo.%s,
				    sdo.%s,
				    bp.Value AS %s,
				    bp.ZZ_Number_Of_Employees AS %s
				FROM %s sdo
				LEFT JOIN C_BPartner bp ON sdo.C_BPartner_ID = bp.C_BPartner_ID AND bp.IsActive='Y'
				WHERE sdo.CreatedBy = ?
				""", I_ZZSdfOrganisation_v.COLUMNNAME_OrgName, I_ZZSdfOrganisation_v.COLUMNNAME_ZZ_SDL_No,
				I_ZZSdfOrganisation_v.COLUMNNAME_ZZSdfRoleType, WspAtrExtensionConstants.SDF_ROLE,
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
//			String docStatus = (String) org.get(I_ZZSdfOrganisation_v.COLUMNNAME_ZZ_DocStatus);
			String role = (String) org.get(WspAtrExtensionConstants.SDF_ROLE);

			if (
//					WspAtrExtensionConstants.DOC_STATUS_APPROVED.equalsIgnoreCase(docStatus) && 
			WspAtrExtensionConstants.SDF_ROLE_PRIMARY.equalsIgnoreCase(role))
			{
				approvedOrgs.add(org);
			}
		}
		return approvedOrgs;
	}

	private TableModel getSdfDetailsComp(DaoManage formManage)
	{
		List<Object> userInfos = DB.getSQLValueObjectsEx(null, """
				SELECT
					COALESCE(z.zzfirstname, au.name),  z.zzsurname
				FROM 
					ad_user au LEFT JOIN zzsdf z ON (au.ad_user_id = z.ad_user_id)
				WHERE
					au.ad_user_id = ?
				""", Env.getAD_User_ID(Env.getCtx()));
		
		List<ColumnModel> cols = new ArrayList<>();
		
		ColumnModel col = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZSdf.Table_Name, I_ZZSdf.COLUMNNAME_ZZFirstName),
				null).setReadonly(true).setDefaultValue(userInfos.get(0));
		cols.add(col);
		
		col = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZSdf.Table_Name, I_ZZSdf.COLUMNNAME_ZZSurname),
				null).setReadonly(true).setDefaultValue(userInfos.get(1));
		cols.add(col);

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

		return createTableModel(cols, formManage, "srd-person-detail");
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
		String title = Msg.getMsg(ctx, "ZZExtRequestSubmitSuccess", false);

		List<String> msgs = new ArrayList<>();

		if (isSubmit)
		{
			msgs.add(Msg.getMsg(ctx, "ZZExtRequestSubmitSuccess", true));
		}

		msgs.add("Request ID: " + extensionData.getDocumentNo());

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

	@Override
	public void doSave(String trxName)
	{

		X_ZZ_WSP_ATR_EXTENSION_BATCH batch = new Query(ctx, I_ZZ_WSP_ATR_EXTENSION_BATCH.Table_Name,
				I_ZZ_WSP_ATR_EXTENSION_BATCH.COLUMNNAME_ZZ_DocStatus + "=?" + " AND "
						+ I_ZZ_WSP_ATR_EXTENSION_BATCH.COLUMNNAME_IsActive + "='Y'" + " AND "
						+ I_ZZ_WSP_ATR_EXTENSION_BATCH.COLUMNNAME_ZZ_WSP_ATR_Ext_Start_Date + " > TRUNC(SYSDATE)",
				trxName).setParameters(X_ZZ_WSP_ATR_EXTENSION_BATCH.ZZ_DOCSTATUS_Draft)
						.setOrderBy(I_ZZ_WSP_ATR_EXTENSION_BATCH.COLUMNNAME_ZZ_WSP_ATR_Ext_Start_Date + " ASC").first();

		if (batch == null)
		{
			throw new AdempiereException(Msg.getMsg(ctx, "ZZExtReqNotAllowed", false));
		}

		Timestamp startTs = batch.getZZ_WSP_ATR_Ext_Start_Date();
		if (startTs == null)
		{
			throw new AdempiereException(Msg.getMsg(ctx, "ZZExtReqNotAllowed", false));
		}

		LocalDate today = LocalDate.now(ZoneId.systemDefault());
		LocalDate startDate = startTs.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		if (!today.isBefore(startDate))
		{
			throw new AdempiereException(Msg.getMsg(ctx, "ZZExtReqNotAllowed", false));
		}

		extensionData.setZZ_WSP_ATR_EXTENSION_BATCH_ID(batch.getZZ_WSP_ATR_EXTENSION_BATCH_ID());

		super.doSave(trxName);

	}
	
	@Override
	public boolean isSupportSubmit() {
		return true;
	}
	
	@Override
	public boolean isSupportSave() {
		return false;
	}

}
