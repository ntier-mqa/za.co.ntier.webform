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
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.compiere.model.I_AD_Column;
import org.compiere.model.I_C_BP_Group;
import org.compiere.model.I_C_BPartner;
import org.compiere.model.I_C_Country;
import org.compiere.model.I_C_Region;
import org.compiere.model.MCity;
import org.compiere.model.MColumn;
import org.compiere.model.MCountry;
import org.compiere.model.MRefList;
import org.compiere.model.MReference;
import org.compiere.model.MRegion;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.model.X_C_BPartner;
import org.compiere.util.CCache;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.ValueNamePair;
import org.idempiere.cache.ImmutablePOCache;
import org.zkoss.util.media.Media;

import com.google.common.cache.Cache;

import za.co.ntier.api.model.I_ZZAnnexure;
import za.co.ntier.api.model.I_ZZSubAnnex;
import za.co.ntier.api.model.I_ZZ_LI_HighestEducation;
import za.co.ntier.api.model.X_ZZAnnexure;
import za.co.ntier.api.model.X_ZZSubAnnex;
import za.co.ntier.api.model.X_ZZ_Application_Form;
import za.co.ntier.api.model.X_ZZ_LI_CitizenResidentialStatus;
import za.co.ntier.api.model.X_ZZ_LI_Disability;
import za.co.ntier.api.model.X_ZZ_LI_HighestEducation;
import za.co.ntier.api.model.X_ZZ_LI_HomeLanguage;
import za.co.ntier.api.model.X_ZZ_LI_SocioEconomicStatus;
import za.co.ntier.webform.form.bean.ProgramType;

public class MasterUtil {
	public static final int limitItem = 600;
	
	private static CCache<ProgramType, List<X_C_BPartner>> s_CetTvetCollege = new CCache<>(
			I_C_BPartner.Table_Name + "_CetTvetCollege", 3);

	private static CCache<Integer, List<MCity>> s_Cities = new CCache<>(MCity.Table_Name + "_DisciplineHDSA", 1);
	private static CCache<Integer, List<MRegion>> s_Regions = new CCache<>(MRegion.Table_Name + "_DisciplineHDSA", 1);
	private static final List<MCity> tmpAllCity = new ArrayList<>();

	public static List<X_C_BPartner> getCetColleges() {
		List<X_C_BPartner> cetColleges = s_CetTvetCollege.get(ProgramType.CET);
		if (cetColleges == null) {
			cetColleges = getBpartnerByGroup("Z-CET");
			s_CetTvetCollege.put(ProgramType.CET, cetColleges);
		}
		return cetColleges;
	}

	public static List<X_C_BPartner> getBpartnerByGroup(String groupValue) {
		Query queryCetColleges = new Query(Env.getCtx(), I_C_BPartner.Table_Name,
				String.format("%s.%s = ?", I_C_BP_Group.Table_Name, I_C_BP_Group.COLUMNNAME_Value), null);
		queryCetColleges.addTableDirectJoin(I_C_BP_Group.Table_Name);
		queryCetColleges.setParameters(groupValue);// "Z-CET"
		queryCetColleges.setOrderBy(I_C_BPartner.COLUMNNAME_Name + " ASC");
		return queryCetColleges.list();
	}

	public static List<X_C_BPartner> getUniversity() {
		List<X_C_BPartner> universitys = s_CetTvetCollege.get(ProgramType.HET_LECTURE_SUPPORT);
		if (universitys == null) {
			universitys = getBpartnerByGroup("Z-UNIVERSITY");
			s_CetTvetCollege.put(ProgramType.HET_LECTURE_SUPPORT, universitys);
		}
		return universitys;
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

	public static List<MCity> getInitCities() {
		if (tmpAllCity.isEmpty()) {
			getCities();
		}
		return tmpAllCity;
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

	public static List<X_C_BPartner> getTvetColleges() {
		List<X_C_BPartner> tvetColleges = s_CetTvetCollege.get(ProgramType.TVET);
		if (tvetColleges == null) {
			tvetColleges = getBpartnerByGroup("Z-TVET");
			s_CetTvetCollege.put(ProgramType.TVET, tvetColleges);
		}
		return tvetColleges;
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
			Query directSubAnnexQuery = MTable.get(X_ZZSubAnnex.Table_ID)
					.createQuery(String.format("%s = ?", I_ZZSubAnnex.COLUMNNAME_ZZ_Application_Form_ID), null);
			directSubAnnexQuery.setParameters(parent.getZZ_Application_Form_ID());
			subAnnexs = directSubAnnexQuery.list();
		}

		return subAnnexs;
	}

	public static List<X_ZZAnnexure> loadAnnexure(X_ZZ_Application_Form applicationForm, String annexureType) {
		if (applicationForm == null)
			return null;

		Query annexureQuery = MTable.get(X_ZZAnnexure.Table_ID).createQuery(String.format("%s = ? AND %s = ?",
				I_ZZAnnexure.COLUMNNAME_ZZ_Application_Form_ID, I_ZZAnnexure.COLUMNNAME_DataType), null);
		annexureQuery.setParameters(applicationForm.getZZ_Application_Form_ID(), annexureType);
		annexureQuery.setOrderBy(I_ZZAnnexure.COLUMNNAME_ZZAnnexure_ID);
		return annexureQuery.list();
	}

	public static String getNameOfColTranslated(String table, String colName) {
		return MColumn.get(Env.getCtx(), table, colName).get_Translation(I_AD_Column.COLUMNNAME_Name);
	}
	
	public static final Entry<String, Integer> LkpGenderListIdentify = new AbstractMap.SimpleEntry<>("d28327d7-01ca-485a-89a2-baa868c8f446", null);
	public static final Entry<String, Integer> LkpFunctionListIdentify = new AbstractMap.SimpleEntry<>("e4b70818-faaa-4af1-ae55-81563efc4633", null);
	public static final Entry<String, Integer> LkpTitleListIdentify = new AbstractMap.SimpleEntry<>("1870ee55-4f9a-430e-ad6a-b5dc587216c7", null);
	public static final Entry<String, Integer> LkpAppointmentListIdentify = new AbstractMap.SimpleEntry<>("6e1d8f96-0c9f-4ed5-940a-cb73aecb5d46", null);
	
	private static CCache<Entry<String, Integer>, List<ValueNamePair>> lkpCache = new CCache<>("lkpCache", 5);	
	
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
	
	public static List<ValueNamePair> getLkpEquity() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private static CCache<Integer, List<X_ZZ_LI_HighestEducation>> highestEducationCache = new CCache<>("master-X_ZZ_LI_HighestEducation", 1);
	
	public static List<X_ZZ_LI_HighestEducation> getHighestEducations(){
		if (highestEducationCache.isEmpty()) {
			List<X_ZZ_LI_HighestEducation> list = getMasterList(X_ZZ_LI_HighestEducation.Table_ID);
			highestEducationCache.put(Integer.MAX_VALUE, list);
			return list;
		}
		return highestEducationCache.get(Integer.MAX_VALUE);
	}
	
	private static CCache<Integer, List<X_ZZ_LI_HomeLanguage>> homeLanguageCache = new CCache<>("master-X_ZZ_LI_HomeLanguage", 1);
	
	public static List<X_ZZ_LI_HomeLanguage> getHomeLanguage(){
		if (homeLanguageCache.isEmpty()) {
			List<X_ZZ_LI_HomeLanguage> list = getMasterList(X_ZZ_LI_HomeLanguage.Table_ID);
			homeLanguageCache.put(Integer.MAX_VALUE, list);
			return list;
		}
		return homeLanguageCache.get(Integer.MAX_VALUE);
	}
	
	private static CCache<Integer, List<X_ZZ_LI_Disability>> disabilityCache = new CCache<>("master-X_ZZ_LI_Disability", 1);
	
	public static List<X_ZZ_LI_Disability> getDisability(){
		if (disabilityCache.isEmpty()) {
			List<X_ZZ_LI_Disability> list = getMasterList(X_ZZ_LI_Disability.Table_ID);
			disabilityCache.put(Integer.MAX_VALUE, list);
			return list;
		}
		return disabilityCache.get(Integer.MAX_VALUE);
	}
	
	private static CCache<Integer, List<X_ZZ_LI_CitizenResidentialStatus>> citizenResidentialStatusCache = new CCache<>("master-X_ZZ_LI_CitizenResidentialStatus", 1);
	
	public static List<X_ZZ_LI_CitizenResidentialStatus> getCitizenResidentialStatus(){
		if (citizenResidentialStatusCache.isEmpty()) {
			List<X_ZZ_LI_CitizenResidentialStatus> list = getMasterList(X_ZZ_LI_CitizenResidentialStatus.Table_ID);
			citizenResidentialStatusCache.put(Integer.MAX_VALUE, list);
			return list;
		}
		return citizenResidentialStatusCache.get(Integer.MAX_VALUE);
	}
	
	private static CCache<Integer, List<X_ZZ_LI_SocioEconomicStatus>> socioEconomicStatusCache = new CCache<>("master-X_ZZ_LI_SocioEconomicStatus", 1);
	
	public static List<X_ZZ_LI_SocioEconomicStatus> getSocioEconomicStatus(){
		if (socioEconomicStatusCache.isEmpty()) {
			List<X_ZZ_LI_SocioEconomicStatus> list = getMasterList(X_ZZ_LI_SocioEconomicStatus.Table_ID);
			socioEconomicStatusCache.put(Integer.MAX_VALUE, list);
			return list;
		}
		return socioEconomicStatusCache.get(Integer.MAX_VALUE);
	}
	
	
	private static <T extends PO> List<T> getMasterList(int tableID){
		Query annexureQuery = MTable.get(tableID).createQuery(null, null)
				.setOnlyActiveRecords(true)
				.setClient_ID();
		return annexureQuery.list();		
	}

}