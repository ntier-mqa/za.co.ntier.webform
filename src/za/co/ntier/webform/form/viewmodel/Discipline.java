package za.co.ntier.webform.form.viewmodel;

import java.util.ArrayList;
import java.util.List;

public class Discipline {
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
	}
	
	private List<Province> provinces;
	private String name;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the provinces
	 */
	public List<Province> getProvinces() {
		return provinces;
	}
	/**
	 * @param provinces the provinces to set
	 */
	public void setProvinces(List<Province> provinces) {
		this.provinces = provinces;
	}
	
	public Discipline(String name) {
		this.provinces = new ArrayList<Discipline.Province>();
		this.name = name;
	}
	
	public static Province[] getAllProvinces() {
        return Province.values();
    }
	
	public void toggleProvince(Province province, boolean checked) {
		if (checked) {
			this.provinces.add(province);
		}else {
			this.provinces.remove(province);
		}
	}
		
}

