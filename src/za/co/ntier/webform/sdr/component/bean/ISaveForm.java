package za.co.ntier.webform.sdr.component.bean;

import java.util.Collection;

public interface ISaveForm {
	public boolean validate();
	
	public void save(String trxName);
	
	public void saveAttachment(String trxName);
	
	public static void saveList(Collection<?> list, String trxName) {
		for (Object saveForm : list) {
			if (saveForm instanceof ISaveForm) {
				((ISaveForm)saveForm).save(trxName);
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
