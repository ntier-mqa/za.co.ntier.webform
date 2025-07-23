package za.co.ntier.webform.form.viewmodel.master;

public enum OrganisationSize {
	SMALL("Small", "(0-49)", "small"),
	MEDIUM("Medium", "(50-149)", "medium"),
	LARGE("Large", "(<150)", "large");

	// Small
	private final String label;
	// (0-49)
	private final String size;
	// small
	private final String id;

	OrganisationSize(String label, String size, String id) {
		this.label = label;
		this.size = size;
		this.id = id;
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

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
}
