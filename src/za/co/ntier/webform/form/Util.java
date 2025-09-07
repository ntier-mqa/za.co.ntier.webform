package za.co.ntier.webform.form;

import org.compiere.model.PO;

public class Util {
	public static void setIntNullable(PO po, Integer value, String columnName) {
		po.set_ValueOfColumn(columnName, value);
	}
}
