package za.co.ntier.webform.form;

/**
 * Constants for WSP-ATR Extension Form Centralizes magic strings used
 * throughout the application
 * 
 * @author niraj
 */
public final class WspAtrExtensionConstants
{

	private WspAtrExtensionConstants()
	{
		throw new AssertionError("Cannot instantiate constants class");
	}

	// Tab titles
	public static final String	TAB_EXTENSION_REQUEST		= "Extension Request";

	// Section titles
	public static final String	SECTION_SDF_DETAILS			= "SDF Details";
	public static final String	SECTION_ORG_DETAILS			= "Organisation Details";
	public static final String	SECTION_EXTENSION_REQUEST	= "Extension Request";
	public static final String	SECTION_SENIOR_REP			= "Senior Organisation Representative";

	// CSS classes
	public static final String	CSS_TWO_COL					= "two-col";
	public static final String	CSS_SDF_SECTION				= "sdf-section";
	public static final String	CSS_ORG_SECTION				= "organisation-section";
	public static final String	CSS_EXTENSION_SECTION		= "extension-request-section";
	public static final String	CSS_SOR_SECTION				= "sor-section";
}
