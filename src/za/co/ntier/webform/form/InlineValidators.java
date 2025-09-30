package za.co.ntier.webform.form;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;

import za.co.ntier.api.model.I_ZZLearningMaterial;
import za.co.ntier.webform.form.bean.component.AnnexureRow;
import za.co.ntier.webform.form.bean.component.ColumnInfo;

public final class InlineValidators {

    private InlineValidators() {}

    public static void validateContactField(
            Component ref, AnnexureRow row, ColumnInfo<?> col) {

        String dao = col.getDaoPropertyName(); // e.g. "ZZEmailWriter" / "ZZCellNoWriter"
        Object raw = row.get(col);
        String val = raw == null ? null : String.valueOf(raw).trim();

        if (I_ZZLearningMaterial.COLUMNNAME_ZZEmailWriter.equalsIgnoreCase(dao)) {
            if (val == null || !val.matches("^[^@\\s]+@[^@\\s]+\\.[A-Za-z]{2,}$")) {
                throw new WrongValueException(ref, "Please enter a valid email address");
            }
        } else if (I_ZZLearningMaterial.COLUMNNAME_ZZCellNoWriter.equalsIgnoreCase(dao)) {
            if (val == null || !val.matches("^\\s*\\d{10}\\s*$")) {
                throw new WrongValueException(ref, "Cell number must be exactly 10 digits");
            }
        }
    }
}
