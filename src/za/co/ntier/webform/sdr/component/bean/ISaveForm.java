package za.co.ntier.webform.sdr.component.bean;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.compiere.model.PO;

public interface ISaveForm extends IInputState{
	
	
	public boolean validate(Boolean isSubmit);
	
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

	public static boolean validates(Collection<?> list, Boolean isSubmit) {
		boolean isValid = true;
		for (Object saveForm : list) {
			if (saveForm instanceof ISaveForm && !((ISaveForm)saveForm).validate(isSubmit)) {
				isValid = false;
			}
		}
		
		return isValid;
	}
	
	public default BiFunction<ISaveForm, String, Boolean> getAfterAppSave (){
		return null;
	}
	
	public List<ISaveForm> getChildren();

}
