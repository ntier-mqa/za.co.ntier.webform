package za.co.ntier.webform.sdr.component.bean;

import java.util.Collection;

import za.co.ntier.api.model.X_ZZSdf;

public interface ISupportSave {
	public void save(X_ZZSdf applicationForm, String trxName);
	
	public static <T extends ISupportSave> void saveList(Collection<T> list, X_ZZSdf applicationForm, String trxName) {
		for (ISupportSave supportSave : list) {
			supportSave.save(applicationForm, trxName);
		}
	}
	
	public void saveAttachment(X_ZZSdf applicationForm, String trxName);

	public static <T extends ISupportSave> boolean validates(Collection<T> list, X_ZZSdf applicationForm, String trxName) {
		boolean isValid = true;
		for (ISupportSave supportSave : list) {
			if (!supportSave.validate()) {
				isValid = false;
			}
		}
		
		return isValid;
	}
	
	public default boolean validate() {
		return true;
	}
}
