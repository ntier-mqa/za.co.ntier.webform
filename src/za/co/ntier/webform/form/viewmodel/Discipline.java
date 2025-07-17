package za.co.ntier.webform.form.viewmodel;

import java.util.ArrayList;
import java.util.List;

public class Discipline {
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
		this.provinces = new ArrayList<Province>();
		this.name = name;
	}
	
		
	public void toggleProvince(Province province, boolean checked) {
		if (checked) {
			this.provinces.add(province);
		}else {
			this.provinces.remove(province);
		}
	}
		
}

