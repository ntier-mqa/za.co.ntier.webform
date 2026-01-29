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

	public static boolean validates(Collection<?> list) {
		boolean isValid = true;
		for (Object supportSave : list) {
			if (supportSave instanceof ISupportSave && !((ISupportSave)supportSave).validate()) {
				isValid = false;
			}
		}
		
		return isValid;
	}
	
	public boolean validate();
}
