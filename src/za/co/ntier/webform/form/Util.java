package za.co.ntier.webform.form;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.compiere.model.PO;

import za.co.ntier.webform.form.bean.component.AnnexureRow;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.IntData;

public class Util {
	public static Integer sumCol(AnnexureRow row, List<ColumnInfo<?>> calCols) {
		Integer total = Integer.valueOf(0);
		for(ColumnInfo<?> calCol : calCols) {
			total += Util.convert(((IntData)row.get(calCol)).getValue());
		}
		return total;
	}
	
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
	
	public static String convertStr(String value) {
		if (StringUtils.isBlank(value)) {
			return null;
		}
		
		return value;
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
