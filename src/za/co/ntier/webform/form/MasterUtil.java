package za.co.ntier.webform.form;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.webui.desktop.DefaultDesktop;
import org.adempiere.webui.session.SessionManager;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.compiere.model.I_AD_Column;
import org.compiere.model.I_C_BP_Group;
import org.compiere.model.I_C_BPartner;
import org.compiere.model.I_C_Bank;
import org.compiere.model.I_C_Country;
import org.compiere.model.I_C_Region;
import org.compiere.model.MCity;
import org.compiere.model.MColumn;
import org.compiere.model.MCountry;
import org.compiere.model.MMenu;
import org.compiere.model.MRefList;
import org.compiere.model.MReference;
import org.compiere.model.MRegion;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.model.X_C_BP_Group;
import org.compiere.model.X_C_BPartner;
import org.compiere.model.X_C_Bank;
import org.compiere.util.CCache;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.ValueNamePair;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Executions;

import za.co.ntier.api.model.I_ZZAnnexure;
import za.co.ntier.api.model.I_ZZSdf;
import za.co.ntier.api.model.I_ZZSubAnnex;
import za.co.ntier.api.model.I_ZZ_Application_Form;
import za.co.ntier.api.model.I_ZZ_Nationality;
import za.co.ntier.api.model.X_ZZAnnexure;
import za.co.ntier.api.model.X_ZZSdf;
import za.co.ntier.api.model.X_ZZSubAnnex;
import za.co.ntier.api.model.X_ZZ_AlternateIDType;
import za.co.ntier.api.model.X_ZZ_Application_Form;
import za.co.ntier.api.model.X_ZZ_LI_CitizenResidentialStatus;
import za.co.ntier.api.model.X_ZZ_LI_Disability;
import za.co.ntier.api.model.X_ZZ_LI_HighestEducation;
import za.co.ntier.api.model.X_ZZ_LI_HomeLanguage;
import za.co.ntier.api.model.X_ZZ_LI_SocioEconomicStatus;
import za.co.ntier.api.model.X_ZZ_Nationality;
import za.co.ntier.api.model.X_ZZ_SETA_Master;
import za.co.ntier.webform.form.viewmodel.component.ComponentVMWrapper;
import za.co.ntier.webform.sdr.component.bean.Dialog;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.tab.bean.NavTab;

public class MasterUtil {
	public static final int limitItem = 600;
	
	public static final String SDRLinkedOrganisationsUU = "114344c0-6344-4d49-8dc9-3ba80e98e0b1";
	public static final String SDRRegistryOrgLinkUU = "1b8f1243-dbbf-458a-89f6-cd89a95a9d4c";
	public static final String SDRMaintainOrganisationUU = "d1c16a4b-9a05-41fc-a2f8-050f1f383431";
	
	
	private static CCache<String, List<X_C_BPartner>> s_CetTvetCollege = new CCache<>(
			I_C_BPartner.Table_Name + "_CetTvetCollege", 3);

	private static CCache<Integer, List<MCity>> s_Cities = new CCache<>(MCity.Table_Name + "_DisciplineHDSA", 1);
	private static CCache<Integer, List<MRegion>> s_Regions = new CCache<>(MRegion.Table_Name + "_DisciplineHDSA", 1);
	private static final List<MCity> tmpAllCity = new ArrayList<>();
	
	public static X_ZZSdf querySdf(int userId) {
		Query savedDataQuery = MTable.get(Env.getCtx(), I_ZZSdf.Table_Name)
				.createQuery(String.format("%s = ?", I_ZZSdf.COLUMNNAME_AD_User_ID), null);
		savedDataQuery.setParameters(userId);
		savedDataQuery.setOnlyActiveRecords(true);
		return savedDataQuery.firstOnly();
	}
	
	public static X_C_BP_Group getBPGroup(String bpGroupUU) {
		Query queryBpGroup = new Query(Env.getCtx(), I_C_BP_Group.Table_Name,
				String.format("%s.%s = ?", I_C_BP_Group.Table_Name, I_C_BP_Group.COLUMNNAME_C_BP_Group_UU), null);
		queryBpGroup.setParameters(bpGroupUU);//"Z-CET"
		return queryBpGroup.firstOnly();
	}
	
	public static List<X_C_BPartner> getBpartnersByGroup(String bpGroupUU) {
		List<X_C_BPartner> bpartners = s_CetTvetCollege.get(bpGroupUU);
		if (bpartners == null) {
			Query queryCetColleges = new Query(Env.getCtx(), I_C_BPartner.Table_Name,
					String.format("%s.%s = ?", I_C_BP_Group.Table_Name, I_C_BP_Group.COLUMNNAME_C_BP_Group_UU), null);
			queryCetColleges.addTableDirectJoin(I_C_BP_Group.Table_Name);
			queryCetColleges.setParameters(bpGroupUU);//"Z-CET"
			queryCetColleges.setOrderBy(I_C_BPartner.COLUMNNAME_Name + " ASC");
			bpartners = queryCetColleges.list();
			s_CetTvetCollege.put(bpGroupUU, bpartners);
		}
		return bpartners;
	}
	
	public static List<MCity> getCities() {
		if (s_Cities.isEmpty()) {

			Query citisQuery = new Query(Env.getCtx(), MCity.Table_Name, null, null);
			citisQuery.addTableDirectJoin(MRegion.Table_Name);
			citisQuery.addJoinClause(String.format("INNER JOIN %s ON (%s.%s = %s.%s AND %s.%s = ?)",
					I_C_Country.Table_Name, MCountry.Table_Name, I_C_Country.COLUMNNAME_C_Country_ID,
					MRegion.Table_Name, I_C_Region.COLUMNNAME_C_Country_ID, I_C_Country.Table_Name,
					I_C_Country.COLUMNNAME_C_Country_ID));
			citisQuery.setParameters(305);// South Africa
			// citisQuery.setRecordstoSkip(20);

			List<MCity> cityInfos = citisQuery.list();
			s_Cities.put(Integer.MIN_VALUE, cityInfos);

			tmpAllCity.clear();
			s_Cities.get(Integer.MIN_VALUE).stream().limit(MasterUtil.limitItem).forEach(city -> {
				tmpAllCity.add(city);
			});

		}
		return s_Cities.get(Integer.MIN_VALUE);
		// return s_Cities.get(Integer.MIN_VALUE);
	}

	public static List<MCity> getCitiesByPostal(String postalCode) {
		List<MCity> areaFilters = new ArrayList<>();
		if (StringUtils.isNotEmpty(postalCode)) {
			MasterUtil.getCities().stream()
					.filter(city -> city.getPostal() != null && postalCode.equalsIgnoreCase(city.getPostal()))
					.limit(MasterUtil.limitItem).forEach(city -> {
						areaFilters.add(city);
					});
		}

		if (areaFilters.isEmpty()) {
			return getInitCities();
		} else {
			return areaFilters;
		}

	}
	
	public static List<MCity> getInitCities(){
		return new ArrayList<>();
		/*
		 * if (tmpAllCity.isEmpty()) { getCities(); } return tmpAllCity;
		 */
	}

	public static char getOffsetChar(char c, int offset) {

		return (char) (c + offset);

	}

	public static List<MRegion> getRegions() {
		if (s_Regions.isEmpty()) {
			Query regionsQuery = new Query(Env.getCtx(), I_C_Region.Table_Name,
					I_C_Region.Table_Name + "." + I_C_Country.COLUMNNAME_C_Country_ID + " = ?", null);
			regionsQuery.addTableDirectJoin(I_C_Country.Table_Name);
			regionsQuery.setParameters(305);// South Africa

			List<MRegion> regionInfos = regionsQuery.list();
			s_Regions.put(Integer.MIN_VALUE, regionInfos);
		}

		return s_Regions.get(Integer.MIN_VALUE);
	}

	public static String saveUploadFile(Media media) throws IOException {

		// System temp base folder
		String tmpDir = System.getProperty("java.io.tmpdir"); // usually /tmp

		// Create unique subfolder, e.g. myapp_20250822_123456789
		File uploadDir = Files.createTempDirectory(Paths.get(tmpDir), "MQA_").toFile();

		// Keep original filename
		String originalName = media.getName();
		File savedFile = new File(uploadDir, originalName);

		try (InputStream in = media.getStreamData(); OutputStream out = new FileOutputStream(savedFile)) {
			byte[] buffer = new byte[8192];
			int len;
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
		}

		return savedFile.getAbsolutePath();
	}

	public static List<X_ZZSubAnnex> loadSubAnnex(X_ZZ_Application_Form parent) {
		List<X_ZZSubAnnex> subAnnexs = null;
		if (parent != null) {
			Query directSubAnnexQuery = MTable.get(Env.getCtx(), X_ZZSubAnnex.Table_Name)
					.createQuery(String.format("%s = ?", I_ZZSubAnnex.COLUMNNAME_ZZ_Application_Form_ID), null);
			directSubAnnexQuery.setParameters(parent.getZZ_Application_Form_ID());
			subAnnexs = directSubAnnexQuery.list();
		}

		return subAnnexs;
	}

	public static List<X_ZZAnnexure> loadAnnexure(X_ZZ_Application_Form applicationForm, String annexureType) {
		if (applicationForm == null)
			return null;

		Query annexureQuery = MTable.get(Env.getCtx(), X_ZZAnnexure.Table_Name).createQuery(String.format("%s = ? AND %s = ?",
				I_ZZAnnexure.COLUMNNAME_ZZ_Application_Form_ID, I_ZZAnnexure.COLUMNNAME_DataType), null);
		annexureQuery.setParameters(applicationForm.getZZ_Application_Form_ID(), annexureType);
		annexureQuery.setOrderBy(I_ZZAnnexure.COLUMNNAME_ZZAnnexure_ID);
		return annexureQuery.list();
	}

	public static <P extends PO> List<P> loadSaved(X_ZZSdf applicationForm, int tableID) {
		if (applicationForm == null)
			return null;

		Query savedDataQuery = MTable.get(tableID).createQuery(String.format("%s = ?",
				I_ZZSdf.COLUMNNAME_ZZSdf_ID), null);
		savedDataQuery.setParameters(applicationForm.getZZSdf_ID());
		savedDataQuery.setOrderBy(I_ZZ_Application_Form.COLUMNNAME_Created);
		return savedDataQuery.list();
	}
	
	public static String getNameOfColTranslated(String table, String colName) {
		return MColumn.get(Env.getCtx(), table, colName).get_Translation(I_AD_Column.COLUMNNAME_Name);
	}
	
	public static String getDescOfColTranslated(String table, String colName) {
		return MColumn.get(Env.getCtx(), table, colName).get_Translation(I_AD_Column.COLUMNNAME_Description);
	}
	
	public static final Entry<String, Integer> LkpGenderListIdentify = new AbstractMap.SimpleEntry<>("d28327d7-01ca-485a-89a2-baa868c8f446", null);
	public static final Entry<String, Integer> LkpFunctionListIdentify = new AbstractMap.SimpleEntry<>("e4b70818-faaa-4af1-ae55-81563efc4633", null);
	public static final Entry<String, Integer> LkpTitleListIdentify = new AbstractMap.SimpleEntry<>("1870ee55-4f9a-430e-ad6a-b5dc587216c7", null);
	public static final Entry<String, Integer> LkpAppointmentListIdentify = new AbstractMap.SimpleEntry<>("6e1d8f96-0c9f-4ed5-940a-cb73aecb5d46", null);
	public static final Entry<String, Integer> LkpEquityListIdentify = new AbstractMap.SimpleEntry<>("a2a8d729-1acb-40d7-bf8d-116e8ac4e7b4", null);
	public static final Entry<String, Integer> LkpAlternateIDIdentify = new AbstractMap.SimpleEntry<>("04b31964-4f89-4d78-bc3c-64333fc14de3", null);
	public static final Entry<String, Integer> LkpAccountTypeIdentify = new AbstractMap.SimpleEntry<>("d2808f16-73a8-4ed5-b5e8-27efd62186a3", null);
	
	public static final Entry<String, Integer> LkpOrganisationRegistrationNumberType = new AbstractMap.SimpleEntry<>("d406001b-56dd-4ce9-a92b-1f70bb2a7cf3", null);
	public static final Entry<String, Integer> LkpOrganisationType = new AbstractMap.SimpleEntry<>("2d43c7e3-b8ac-4269-8501-5585db71d754", null);
	public static final Entry<String, Integer> LkpLevyNumberType = new AbstractMap.SimpleEntry<>("778e320f-85ec-4577-9c60-c7db46a9e3d7", null);
	public static final Entry<String, Integer> LkpSicCode = new AbstractMap.SimpleEntry<>("cddc9d77-2c4f-4650-a8a5-58c87f72dbb4", null);
	public static final Entry<String, Integer> LkpSubSector = new AbstractMap.SimpleEntry<>("bc9bef41-360e-4fbc-b4f1-93ec2892cef9", null);
	public static final Entry<String, Integer> LkpChamberCode = new AbstractMap.SimpleEntry<>("15a4a826-3bcb-402b-9ff4-602beeec8c3f", null);
	
	public static final Entry<String, Integer> YesNoIdentify = new AbstractMap.SimpleEntry<>("de0c3f82-e8fa-4118-939a-9876ec70f1a8", null);
	
	
	private static CCache<Entry<String, Integer>, List<ValueNamePair>> lkpCache = new CCache<>("lkpCache", 10);	
	
	public static List<ValueNamePair> getChamberCode () {
		return getRefList(LkpChamberCode);
	}
	
	public static List<ValueNamePair> getSubSector () {
		return getRefList(LkpSubSector);
	}
	
	public static List<ValueNamePair> getSicCode () {
		return getRefList(LkpSicCode);
	}
	
	public static List<ValueNamePair> getLevyNumberType () {
		return getRefList(LkpLevyNumberType);
	}
	
	public static List<ValueNamePair> getOrganisationType () {
		return getRefList(LkpOrganisationType);
	}
	
	public static List<ValueNamePair> getOrganisationRegistrationNumberType () {
		return getRefList(LkpOrganisationRegistrationNumberType);
	}
	
	public static List<ValueNamePair> getYesNoList () {
		return getRefList(YesNoIdentify);
	}
	
	public static List<ValueNamePair> getLkpAccountType () {
		return getRefList(LkpAccountTypeIdentify);
	}
	
	public static List<ValueNamePair> getLkpAppointment () {
		return getRefList(LkpAppointmentListIdentify);
	}
	
	public static List<ValueNamePair> getLkpGenders () {
		return getRefList(LkpGenderListIdentify);
	}
	
	public static List<ValueNamePair> getLkpFunctionLists () {
		return getRefList(LkpFunctionListIdentify);
	}
	
	public static List<ValueNamePair> getLkpTitleLists () {
		return getRefList(LkpTitleListIdentify);
	}
	
	public static List<ValueNamePair> getLkpEquity() {
		return getRefList(LkpEquityListIdentify);
	}
	
	public static List<ValueNamePair> getLkpAltID() {
		return getRefList(LkpAlternateIDIdentify);
	}
	private static int getIdFromUU(Entry<String, Integer> identify) {
		if (identify.getValue() == null) {
			MReference ref = MReference.get(Env.getCtx(), identify.getKey());
			identify.setValue(ref.getAD_Reference_ID());
		}
		
		return identify.getValue();
	}
	
	
	public static List<ValueNamePair> getRefList (Entry<String, Integer> identify) {
		List<ValueNamePair> lkpList = lkpCache.get(identify);
		if (lkpList == null) {
			int id = MasterUtil.getIdFromUU(identify);
			lkpList = List.of(MRefList.getList(Env.getCtx(), id, false));
			lkpCache.put(identify, lkpList);
		}
		
		return lkpList;
	}
	
	private static CCache<Integer, List<X_C_Bank>> bankCache = new CCache<>("master-X_C_Bank", 1);
	
	public static List<X_C_Bank> getBanks(){
		if (bankCache.isEmpty()) {
			List<X_C_Bank> list = getMasterList(I_C_Bank.Table_Name);
			bankCache.put(Integer.MAX_VALUE, list);
			return list;
		}
		return bankCache.get(Integer.MAX_VALUE);
	}
	
	private static CCache<Integer, List<X_ZZ_LI_HighestEducation>> highestEducationCache = new CCache<>("master-X_ZZ_LI_HighestEducation", 1);
	
	public static List<X_ZZ_LI_HighestEducation> getHighestEducations(){
		if (highestEducationCache.isEmpty()) {
			List<X_ZZ_LI_HighestEducation> list = getMasterList(X_ZZ_LI_HighestEducation.Table_Name);
			highestEducationCache.put(Integer.MAX_VALUE, list);
			return list;
		}
		return highestEducationCache.get(Integer.MAX_VALUE);
	}
	
	private static CCache<Integer, List<X_ZZ_AlternateIDType>> alternateIDTypeCache = new CCache<>("master-X_ZZ_AlternateIDType", 1);
	
	public static List<X_ZZ_AlternateIDType> getAlternateIDType() {
		if (alternateIDTypeCache.isEmpty()) {
			List<X_ZZ_AlternateIDType> list = getMasterList(X_ZZ_AlternateIDType.Table_Name);
			alternateIDTypeCache.put(Integer.MAX_VALUE, list);
			return list;
		}
		return alternateIDTypeCache.get(Integer.MAX_VALUE);
	}
	
	private static CCache<Integer, List<X_ZZ_SETA_Master>> setaMasterCache = new CCache<>("master-X_ZZ_SETA_Master", 1);
	public static List<X_ZZ_SETA_Master> getSetaMaster() {
		if (setaMasterCache.isEmpty()) {
			List<X_ZZ_SETA_Master> list = getMasterList(X_ZZ_SETA_Master.Table_Name);
			setaMasterCache.put(Integer.MAX_VALUE, list);
			return list;
		}
		return setaMasterCache.get(Integer.MAX_VALUE);
	}
	
	private static CCache<Integer, List<X_ZZ_LI_HomeLanguage>> homeLanguageCache = new CCache<>("master-X_ZZ_LI_HomeLanguage", 1);
	
	public static List<X_ZZ_LI_HomeLanguage> getHomeLanguage(){
		if (homeLanguageCache.isEmpty()) {
			List<X_ZZ_LI_HomeLanguage> list = getMasterList(X_ZZ_LI_HomeLanguage.Table_Name);
			homeLanguageCache.put(Integer.MAX_VALUE, list);
			return list;
		}
		return homeLanguageCache.get(Integer.MAX_VALUE);
	}
	
	private static CCache<Integer, List<X_ZZ_LI_Disability>> disabilityCache = new CCache<>("master-X_ZZ_LI_Disability", 1);
	
	public static List<X_ZZ_LI_Disability> getDisability(){
		if (disabilityCache.isEmpty()) {
			List<X_ZZ_LI_Disability> list = getMasterList(X_ZZ_LI_Disability.Table_Name);
			disabilityCache.put(Integer.MAX_VALUE, list);
			return list;
		}
		return disabilityCache.get(Integer.MAX_VALUE);
	}
	
	private static CCache<Integer, List<X_ZZ_LI_CitizenResidentialStatus>> citizenResidentialStatusCache = new CCache<>("master-X_ZZ_LI_CitizenResidentialStatus", 1);
	
	public static List<X_ZZ_LI_CitizenResidentialStatus> getCitizenResidentialStatus(){
		if (citizenResidentialStatusCache.isEmpty()) {
			List<X_ZZ_LI_CitizenResidentialStatus> list = getMasterList(X_ZZ_LI_CitizenResidentialStatus.Table_Name);
			citizenResidentialStatusCache.put(Integer.MAX_VALUE, list);
			return list;
		}
		return citizenResidentialStatusCache.get(Integer.MAX_VALUE);
	}
	
	private static CCache<Integer, List<X_ZZ_LI_SocioEconomicStatus>> socioEconomicStatusCache = new CCache<>("master-X_ZZ_LI_SocioEconomicStatus", 1);
	
	public static List<X_ZZ_LI_SocioEconomicStatus> getSocioEconomicStatus(){
		if (socioEconomicStatusCache.isEmpty()) {
			List<X_ZZ_LI_SocioEconomicStatus> list = getMasterList(X_ZZ_LI_SocioEconomicStatus.Table_Name);
			socioEconomicStatusCache.put(Integer.MAX_VALUE, list);
			return list;
		}
		return socioEconomicStatusCache.get(Integer.MAX_VALUE);
	}
	
	private static CCache<Integer, List<X_ZZ_Nationality>> nationalityCache = new CCache<>("master-ZZ_Nationality", 1);
	
	public static List<X_ZZ_Nationality> getNationality(){
		if (nationalityCache.isEmpty()) {
			List<X_ZZ_Nationality> list = getMasterList(I_ZZ_Nationality.Table_Name);
			nationalityCache.put(Integer.MAX_VALUE, list);
			return list;
		}
		return nationalityCache.get(Integer.MAX_VALUE);
	}
	/*
	 * private static CCache<Integer, List<X_bank>> nationalityCache = new
	 * CCache<>("master-ZZ_Nationality", 1);
	 * 
	 * public static List<X_ZZ_Nationality> getNationality(){ if
	 * (nationalityCache.isEmpty()) { List<X_ZZ_Nationality> list =
	 * getMasterList(I_ZZ_Nationality.Table_ID);
	 * nationalityCache.put(Integer.MAX_VALUE, list); return list; } return
	 * nationalityCache.get(Integer.MAX_VALUE); }
	 */
	
	private static <T extends PO> List<T> getMasterList(String tableName){
		Query annexureQuery = MTable.get(Env.getCtx(), tableName).createQuery(null, null)
				.setOnlyActiveRecords(true)
				.setClient_ID();
		return annexureQuery.list();		
	}

	public static void setObjectProperty(Object obj, String propertyName, Object value) {
		try {
			PropertyUtils.setSimpleProperty(obj, propertyName, value);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AdempiereException(e.getMessage(), e);
		}
	}
	
	public static Object getObjectPropertyValue(Object dao, String propertyName) {
		try {
			return PropertyUtils.getSimpleProperty(dao, propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AdempiereException(e.getMessage(), e);
		}
	}
	
	public static boolean isNavTab(Object obj) {
		return obj instanceof NavTab;
	}
	
	public static boolean isFormViewTableModel(Object obj) {
		if (obj instanceof TableModel) {
			TableModel tableModel = (TableModel)obj;
			return tableModel.isFormView();
		}
		
		return false;
	}
	
	public static boolean isListViewTableModel(Object obj) {
		if (obj instanceof TableModel) {
			TableModel tableModel = (TableModel)obj;
			return !tableModel.isFormView();
		}
		
		return false;
	}
	
	public static boolean isListOrgLink(Object obj) {
		return obj instanceof List<?>;

	}

	public static void openForm(String menuUU, String recordUU) {
		openForm(menuUU, WebForm.recordUUMenuContextKeyNonPlus, recordUU);
	}
	
	public static void openForm(String menuUU, Integer recordID) {
		if (recordID != null)
			openForm(menuUU, WebForm.recordIDMenuContextKeyNonPlus, recordID.toString());
	}
	
	public static void openForm(String menuUU) {
		openForm(menuUU, null, null);
	}
	
	public static void openForm(String menuUU, String key, String value) {
		MMenu menu = new MMenu(Env.getCtx(), menuUU, null);
		DefaultDesktop desktop = (DefaultDesktop) SessionManager.getAppDesktop();
		
		String contextVariables = menu.getPredefinedContextVariables();
		if (contextVariables == null && (key != null && value != null)) {
			contextVariables = "";
		}
		
		if (key != null && value != null) {
			contextVariables += String.format("\n%s=%s", key, value);
		}
		
		desktop.setPredefinedContextVariables(contextVariables);
		desktop.setMenuIsSOTrx(menu.isSOTrx());
        desktop.openForm(menu.getAD_Form_ID());  
		
	}

	public static void showDialog(String msgKey, Consumer<Object> onCloseDialog) {
		Dialog dialog = new Dialog(Msg.getMsg(Env.getCtx(), msgKey, false), 
				new StringBuilder(Msg.getMsg(Env.getCtx(), msgKey, true)).toString());
		dialog.setSclass(msgKey);
		dialog.setOnCloseDialog(onCloseDialog);
		
		Map<String, Object> args = new HashMap<>();
		args.put(ComponentVMWrapper.ComponentKey, dialog);
		String zulPathRelative = WebForm.getBundleResourcePath("sdr/component/zul/dialog.zul");	
		Executions.createComponents(zulPathRelative, null, args);
	}
	
	public static Consumer<Object> fCloseActiveWindow =  t -> {
		MasterUtil.closeActiveWindow();
	};
	
	public static void closeActiveWindow() {
		DefaultDesktop desktop = (DefaultDesktop) SessionManager.getAppDesktop();
		desktop.closeActiveWindow();
	}
	
	public static int getTableId(String tableName) {
		return MTable.get(Env.getCtx(), tableName).get_Table_ID();
		
	}
}
