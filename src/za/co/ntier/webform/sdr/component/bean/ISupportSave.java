package za.co.ntier.webform.sdr.component.bean;

import java.util.Collection;

import za.co.ntier.api.model.X_ZZSdf;

public interface ISupportSave {
	public void save(X_ZZSdf applicationForm, String trxName);
	
	public default <T extends ISupportSave> void saveList(Collection<T> list, X_ZZSdf applicationForm, String trxName) {
		for (ISupportSave supportSave : list) {
			supportSave.save(applicationForm, trxName);
		}
	}
}
