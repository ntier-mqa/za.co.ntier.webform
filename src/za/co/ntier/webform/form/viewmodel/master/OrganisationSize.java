package za.co.ntier.webform.form.viewmodel.master;

@Deprecated
public enum OrganisationSize {
	LARGE("Large", "(<150)", "large"), MEDIUM("Medium", "(50-149)", "medium"), SMALL("Small", "(0-49)", "small");

	// small
	private final String id;
	// Small
	private final String label;
	// (0-49)
	private final String size;

	OrganisationSize(String label, String size, String id) {
		this.label = label;
		this.size = size;
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	/**
	 * @return the size
	 */
	public String getSize() {
		return size;
	}
}
