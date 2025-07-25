package za.co.ntier.webform.form.bean;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Province {
	EC("EC", "E. Cape", "Eastern Cape"), FS("FS", "Free St.", "Free State"), GT("GT", "Gauteng", "Gauteng"),
	KZN("KZN", "KZN", "KwaZulu-Natal"), LIM("LIM", "Limpopo", "Limpopo"), MPH("MPH", "Mpuma.", "Mpumalanga"),
	NC("NC", "N. Cape", "Northern Cape"), NW("NW", "N. West", "North West"), TOTAL("Total", "Total", "Total"),
	WS("WS", "W. Cape", "Western Cape");

	private static final List<Province> withoutTotal;
	private static final Province[] withTotal;
	static {
		withoutTotal = Arrays.stream(values()).filter(p -> p != Province.TOTAL)
				.collect(Collectors.toUnmodifiableList()); // Java 10+

		withTotal = values();
	}

	public static List<Province> getProvincesWithoutTotal() {
		return withoutTotal;
	}

	public static Province[] getProvincesWithTotal() {
		return withTotal;
	}

	private final String code;

	private final String fullName;

	private final String shortName;

	Province(String code, String shortName, String fullName) {
		this.code = code;
		this.shortName = shortName;
		this.fullName = fullName;
	}

	public String getCode() {
		return code;
	}

	public String getFullName() {
		return fullName;
	}

	public String getShortName() {
		return shortName;
	}

	public boolean isTotal() {
		return this == TOTAL;
	}
}