package za.co.ntier.webform.form.viewmodel.master;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.compiere.model.MPeriod;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

public class MasterUtil {
	public static final List<KeyNamePair> districtMunicipalities;
	public static final List<KeyNamePair> localMunicipalities;
	public static final List<KeyNamePair> municipalityTypes;
	
	public static LocalDate getcurrrentPeriodYear() {
		MPeriod period = MPeriod.get(Env.getCtx(), Env.getContextAsDate(Env.getCtx(), "#Date"), 0, null);
		return period.getStartDate().toLocalDateTime().toLocalDate();
	}

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
}
