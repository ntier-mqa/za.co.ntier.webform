package za.co.ntier.webform.form.viewmodel;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Province {
    EC("EC", "E. Cape", "Eastern Cape"),
    LIM("LIM", "Limpopo", "Limpopo"),
    MPH("MPH", "Mpuma.", "Mpumalanga"),
    GT("GT", "Gauteng", "Gauteng"),
    WS("WS", "W. Cape", "Western Cape"),
    NC("NC", "N. Cape", "Northern Cape"),
    NW("NW", "N. West", "North West"),
    KZN("KZN", "KZN", "KwaZulu-Natal"),
    FS("FS", "Free St.", "Free State"),
    TOTAL("Total", "Total", "Total");

    private final String code;
    private final String shortName;
    private final String fullName;

    Province(String code, String shortName, String fullName) {
        this.code = code;
        this.shortName = shortName;
        this.fullName = fullName;
    }

    public String getCode() {
        return code;
    }

    public String getShortName() {
        return shortName;
    }

    public String getFullName() {
        return fullName;
    }
    
    public boolean isTotal() {
        return this == TOTAL;
    }
    
    private static final List<Province> withoutTotal;

    static {
    	withoutTotal = Arrays.stream(values())
            .filter(p -> p != Province.TOTAL)
            .collect(Collectors.toUnmodifiableList()); // Java 10+
    	
    	withTotal = values();
    }
    
    public static List<Province> getProvincesWithoutTotal() {
        return withoutTotal;
    }
    
    private static final Province [] withTotal;
    public static Province [] getProvincesWithTotal() {
        return withTotal;
    }
}