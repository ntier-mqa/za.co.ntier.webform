package za.co.ntier.webform.sdr.component.bean;

import java.util.Collection;

public interface ISaveForm {
	public boolean validate();
	
	public void syncUIToDao(String trxName);
	
	public void saveToDb(String trxName);
	
	public static void batchSaveToDb(Collection<?> list, String trxName) {
		for (Object saveForm : list) {
			if (saveForm instanceof ISaveForm) {
				((ISaveForm)saveForm).saveToDb(trxName);
			}
			
		}
	}
	
	public void saveAttachment(String trxName);
	
	public static void batchSyncToDao(Collection<?> list, String trxName) {
		for (Object saveForm : list) {
			if (saveForm instanceof ISaveForm) {
				((ISaveForm)saveForm).syncUIToDao(trxName);
			}
			
		}
	}

	public static boolean validates(Collection<?> list) {
		boolean isValid = true;
		for (Object saveForm : list) {
			if (saveForm instanceof ISaveForm && !((ISaveForm)saveForm).validate()) {
				isValid = false;
			}
		}
		
		return isValid;
	}

}
