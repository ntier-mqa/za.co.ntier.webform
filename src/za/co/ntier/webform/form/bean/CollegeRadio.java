package za.co.ntier.webform.form.bean;

public class CollegeRadio {
	private String label;
	private Boolean value;
	
	public CollegeRadio(String label, Boolean value) {
		this.label = label;
		this.value = value;
	}
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * @return the value
	 */
	public Boolean getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(Boolean value) {
		this.value = value;
	}
	
}
