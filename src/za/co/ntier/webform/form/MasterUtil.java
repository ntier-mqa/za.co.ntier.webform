package za.co.ntier.webform.form;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.compiere.model.I_C_BP_Group;
import org.compiere.model.I_C_BPartner;
import org.compiere.model.MCity;
import org.compiere.model.MCountry;
import org.compiere.model.MRegion;
import org.compiere.model.Query;
import org.compiere.model.X_C_BPartner;
import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.zkoss.util.media.Media;

import za.co.ntier.webform.form.bean.ProgramType;
import za.co.ntier.webform.form.bean.component.LearnerInputInfo;
import za.co.ntier.webform.model.X_ZZ_Disciplines;
import za.co.ntier.webform.model.X_ZZ_FormDiscipline;
import za.co.ntier.webform.model.X_ZZ_Learnerships;
import za.co.ntier.webform.model.X_ZZ_Program_Disciplines;
import za.co.ntier.webform.model.X_ZZ_Program_Learnerships;
import za.co.ntier.webform.model.X_ZZ_Program_Trade;
import za.co.ntier.webform.model.X_ZZ_Trade;

public class MasterUtil {
	public static final int limitItem = 30;
	public static final List<KeyNamePair> districtMunicipalities;
	public static final List<KeyNamePair> localMunicipalities;
	public static final List<KeyNamePair> municipalityTypes;

	static {
		municipalityTypes = Arrays.asList(new KeyNamePair(1, "Rural"), new KeyNamePair(2, "Urban"));

		localMunicipalities = Arrays.asList(new KeyNamePair(1, "Amahlathi"), new KeyNamePair(2, "Blue Crane Route"),
				new KeyNamePair(3, "Dr Beyers Naudé"), new KeyNamePair(4, "Dihlabeng"), new KeyNamePair(5, "Kopanong"),
				new KeyNamePair(6, "Letsemeng"), new KeyNamePair(7, "Mafube"), new KeyNamePair(8, "Maluti‑a‑Phofung"),
				new KeyNamePair(9, "Emfuleni"), new KeyNamePair(10, "Midvaal"), new KeyNamePair(11, "Lesedi"),
				new KeyNamePair(12, "Mogale City"), new KeyNamePair(13, "Merafong City"),
				new KeyNamePair(14, "Rand West City"), new KeyNamePair(15, "Abaqulusi"),
				new KeyNamePair(16, "Alfred Duma"), new KeyNamePair(17, "Big 5 Hlabisa"),
				new KeyNamePair(18, "City of uMhlathuze"), new KeyNamePair(19, "Dannhauser"),
				new KeyNamePair(20, "Cape Agulhas"), new KeyNamePair(21, "Cederberg"),
				new KeyNamePair(22, "Drakenstein"), new KeyNamePair(23, "George"), new KeyNamePair(24, "Hessequa"),
				new KeyNamePair(25, "Kannaland"));

		districtMunicipalities = Arrays.asList(new KeyNamePair(1, "Alfred Nzo"), new KeyNamePair(2, "Amathole"),
				new KeyNamePair(3, "Chris Hani"), new KeyNamePair(4, "Joe Gqabi"), new KeyNamePair(5, "OR Tambo"),
				new KeyNamePair(6, "Sarah Baartman"),

				// Free State
				new KeyNamePair(7, "Fezile Dabi"), new KeyNamePair(8, "Lejweleputswa"), new KeyNamePair(9, "Motheo"),
				new KeyNamePair(10, "Thabo Mofutsanyana"), new KeyNamePair(11, "Xhariep"),

				// KwaZulu-Natal
				new KeyNamePair(12, "Amajuba"), new KeyNamePair(13, "Harry Gwala"), new KeyNamePair(14, "iLembe"),
				new KeyNamePair(15, "King Cetshwayo"), new KeyNamePair(16, "Ugu"), new KeyNamePair(17, "uMgungundlovu"),
				new KeyNamePair(18, "uMkhanyakude"), new KeyNamePair(19, "uMzinyathi"), new KeyNamePair(20, "uThukela"),
				new KeyNamePair(21, "Zululand"),

				// Limpopo
				new KeyNamePair(22, "Capricorn"), new KeyNamePair(23, "Mopani"), new KeyNamePair(24, "Sekhukhune"),
				new KeyNamePair(25, "Vhembe"), new KeyNamePair(26, "Waterberg"),

				// Mpumalanga
				new KeyNamePair(27, "Ehlanzeni"), new KeyNamePair(28, "Gert Sibande"), new KeyNamePair(29, "Nkangala"),

				// North West
				new KeyNamePair(30, "Bojanala"), new KeyNamePair(31, "Dr Kenneth Kaunda"),
				new KeyNamePair(32, "Dr Ruth Segomotsi Mompati"), new KeyNamePair(33, "Ngaka Modiri Molema"),

				// Northern Cape
				new KeyNamePair(34, "Frances Baard"), new KeyNamePair(35, "John Taolo Gaetsewe"),
				new KeyNamePair(36, "Namakwa"), new KeyNamePair(37, "Pixley ka Seme"), new KeyNamePair(38, "ZF Mgcawu"),

				// Western Cape
				new KeyNamePair(39, "Central Karoo"), new KeyNamePair(40, "Cape Winelands"),
				new KeyNamePair(41, "Eden"), new KeyNamePair(42, "Overberg"), new KeyNamePair(43, "West Coast"));
	}

	private static CCache<Integer, List<MCity>> s_Cities = new CCache<>(MCity.Table_Name + "_DisciplineHDSA", 1);
	private static CCache<Integer, List<MRegion>> s_Regions = new CCache<>(MRegion.Table_Name + "_DisciplineHDSA", 1);
	private static CCache<ProgramType, List<X_C_BPartner>> s_CetTvetCollege = new CCache<>(
			I_C_BPartner.Table_Name + "_CetTvetCollege", 2);

	public static List<X_C_BPartner> getCetColleges() {
		List<X_C_BPartner> cetColleges = s_CetTvetCollege.get(ProgramType.CET);
		if (cetColleges == null) {
			Query queryCetColleges = new Query(Env.getCtx(), I_C_BPartner.Table_Name,
					String.format("%s.%s = ?", I_C_BP_Group.Table_Name, I_C_BP_Group.COLUMNNAME_Value), null);
			queryCetColleges.addTableDirectJoin(I_C_BP_Group.Table_Name);
			queryCetColleges.setParameters("Z-CET");
			cetColleges = queryCetColleges.list();

			s_CetTvetCollege.put(ProgramType.CET, cetColleges);
		}
		return cetColleges;
	}
	private static final List<MCity> tmpAllCity = new ArrayList<>();
	public static List<MCity> getCities() {
		if (s_Cities.isEmpty()) {

			Query citisQuery = new Query(Env.getCtx(), MCity.Table_Name, null, null);
			citisQuery.addTableDirectJoin(MRegion.Table_Name);
			citisQuery.addJoinClause(String.format("INNER JOIN %s ON (%s.%s = %s.%s AND %s.%s = ?)",
					MCountry.Table_Name, MCountry.Table_Name, MCountry.COLUMNNAME_C_Country_ID, MRegion.Table_Name,
					MRegion.COLUMNNAME_C_Country_ID, MCountry.Table_Name, MCountry.COLUMNNAME_C_Country_ID));
			citisQuery.setParameters(305);// South Africa
			citisQuery.setRecordstoSkip(20);

			List<MCity> cityInfos = citisQuery.list();
			s_Cities.put(Integer.MIN_VALUE, cityInfos);
			
			tmpAllCity.clear();
			s_Cities.get(Integer.MIN_VALUE).stream().limit(MasterUtil.limitItem).forEach(city -> {
				tmpAllCity.add(city);
			}); 
			
		}
		return tmpAllCity;
		//return s_Cities.get(Integer.MIN_VALUE);
	}

	public static List<MCity> getCitiesByPostal(String postalCode) {
		
		if (StringUtils.isNotEmpty(postalCode)) {
			List<MCity> areaFilters = new ArrayList<>();
			MasterUtil.getCities().stream()
					.filter(city -> city.getPostal() != null && postalCode.equalsIgnoreCase(city.getPostal()))
					.limit(MasterUtil.limitItem).forEach(city -> {
						areaFilters.add(city);
					});
			return areaFilters;
		}else {
			return getCities();
		}
		
		
	}
	
	public static char getOffsetChar(char c, int offset) {

		return (char) (c + offset);

	}

	public static List<MRegion> getRegions() {
		if (s_Regions.isEmpty()) {
			Query regionsQuery = new Query(Env.getCtx(), MRegion.Table_Name,
					MRegion.Table_Name + "." + MCountry.COLUMNNAME_C_Country_ID + " = ?", null);
			regionsQuery.addTableDirectJoin(MCountry.Table_Name);
			regionsQuery.setParameters(305);// South Africa

			List<MRegion> regionInfos = regionsQuery.list();
			s_Regions.put(Integer.MIN_VALUE, regionInfos);
		}

		return s_Regions.get(Integer.MIN_VALUE);
	}

	public static List<X_C_BPartner> getTvetColleges() {
		List<X_C_BPartner> tvetColleges = s_CetTvetCollege.get(ProgramType.TVET);
		if (tvetColleges == null) {
			Query queryCetColleges = new Query(Env.getCtx(), I_C_BPartner.Table_Name,
					String.format("%s.%s = ?", I_C_BP_Group.Table_Name, I_C_BP_Group.COLUMNNAME_Value), null);
			queryCetColleges.setParameters("Z-TVET");
			queryCetColleges.addTableDirectJoin(I_C_BP_Group.Table_Name);
			tvetColleges = queryCetColleges.list();

			s_CetTvetCollege.put(ProgramType.TVET, tvetColleges);
		}
		return tvetColleges;
	}

	public static List<Object> queryLearnerInputInfos(int programMasterDataID, String learnerInputType) {

		String learnerInputProgramID;
		String learnerInputID;
		String learnerInputText = X_ZZ_Trade.COLUMNNAME_Name;
		String learnerInputProgramTable;
		String learnerInputTable;

		StringBuilder sql = new StringBuilder();
		if (learnerInputType == X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_Trade) {
			learnerInputProgramID = X_ZZ_Program_Trade.COLUMNNAME_ZZ_Program_Trade_ID;
			learnerInputID = X_ZZ_Program_Trade.COLUMNNAME_ZZ_Trade_ID;
			learnerInputProgramTable = X_ZZ_Program_Trade.Table_Name;
			learnerInputTable = X_ZZ_Trade.Table_Name;
		} else if (learnerInputType == X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_Discipline) {
			learnerInputProgramID = X_ZZ_Program_Disciplines.COLUMNNAME_ZZ_Program_Disciplines_ID;
			learnerInputID = X_ZZ_Program_Disciplines.COLUMNNAME_ZZ_Disciplines_ID;
			learnerInputProgramTable = X_ZZ_Program_Disciplines.Table_Name;
			learnerInputTable = X_ZZ_Disciplines.Table_Name;
		} else if (learnerInputType == X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_4IRLearnership
				|| learnerInputType == X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_GeneralLearnership
				|| learnerInputType == X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_AETLearnership) {
			learnerInputProgramID = X_ZZ_Program_Learnerships.COLUMNNAME_ZZ_Program_Learnerships_ID;
			learnerInputID = X_ZZ_Program_Learnerships.COLUMNNAME_ZZ_Learnerships_ID;
			learnerInputProgramTable = X_ZZ_Program_Learnerships.Table_Name;
			learnerInputTable = X_ZZ_Learnerships.Table_Name;
		} else {
			throw new IllegalArgumentException("Wrong learnerInput type");
		}

		sql.append(String.format("SELECT %s, %s.%s, %s, %s, %s FROM %s INNER JOIN %s ON (%s.%s = %s.%s) WHERE %s = ?",
				learnerInputProgramID, learnerInputTable, learnerInputID, X_ZZ_Program_Trade.COLUMNNAME_ZZ_WPA_Req,
				X_ZZ_Program_Trade.COLUMNNAME_ZZ_Is_Accred_SLA_Req, learnerInputText, learnerInputProgramTable,
				learnerInputTable, learnerInputProgramTable, learnerInputID, learnerInputTable, learnerInputID,
				X_ZZ_Program_Trade.COLUMNNAME_ZZ_Program_Master_Data_ID));

		if (learnerInputType == X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_4IRLearnership) {
			sql.append(" AND ZZ_Program_Learnerships.ZZ_Learnerships_Type = '4'");
		} else if (learnerInputType == X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_GeneralLearnership) {
			sql.append(" AND ZZ_Program_Learnerships.ZZ_Learnerships_Type = 'G'");
		} else if (learnerInputType == X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_AETLearnership) {
			sql.append(" AND ZZ_Program_Learnerships.ZZ_Learnerships_Type = 'A'");
		}

		sql.append(" ORDER BY ");
		sql.append(X_ZZ_Program_Trade.COLUMNNAME_Line);

		List<List<Object>> learnerInputInfoObjs = DB.getSQLArrayObjectsEx(null, sql.toString(), programMasterDataID);

		List<LearnerInputInfo> learnerInputInfos = new ArrayList<>();

		boolean hasWPAReq = false;
		boolean hasAccred = false;
		if (learnerInputInfoObjs == null)
			learnerInputInfoObjs = new ArrayList<>();

		for (List<Object> learnerInputInfoObj : learnerInputInfoObjs) {
			LearnerInputInfo learnerInputInfo = new LearnerInputInfo(learnerInputInfoObj);
			learnerInputInfos.add(learnerInputInfo);

			if (learnerInputInfo.isUploadWPA()) {
				hasWPAReq = true;
			}

			if (learnerInputInfo.isUploadAccred()) {
				hasAccred = true;
			}
		}

		return List.of(learnerInputInfos, hasWPAReq, hasAccred);
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
}
