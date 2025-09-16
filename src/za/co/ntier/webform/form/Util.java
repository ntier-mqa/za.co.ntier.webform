package za.co.ntier.webform.form;

import org.compiere.model.PO;

public class Util {
	public static void setIntNullable(PO po, Integer value, String columnName) {
		po.set_ValueOfColumn(columnName, value);
	}

	public static int convert(Integer value) {
		if (value == null) {
			return 0;
		}
		
		return value.intValue();
	}
	
	public static Integer convert(int value) {
		if (value == 0) {
			return null;
		}
		
		return Integer.valueOf(value);
	}
	
	public static String convert(Boolean value) {
		if (value == null) {
			return null;
		}
		
		return value?"Y":"N";
	}

	public static Boolean convert(String value) {
		Boolean converted = null;
		if (value == null)
			converted = null;
		else if ("Y".equals(value))
			converted = Boolean.TRUE;
		else if ("N".equals(value))
			converted = Boolean.FALSE;
		else
			throw new IllegalArgumentException("need Y/N string");
		
		return converted;
	}
}
