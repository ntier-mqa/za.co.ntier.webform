package za.co.ntier.webform.sdr.component.bean;

import java.util.List;

import za.co.ntier.webform.sdr.component.bean.TableModel.DaoManage;

public interface ISupportSaveApp {
	public default List<ISupportSave> getSaveComponents(){
		return List.of();
	}
	public default List<DaoManage> getDaoManages(){
		return List.of();
	}
	
	public void saveClose();
	
	public default void doSave(String trxName) {
		List<ISupportSave> saveComponents = getSaveComponents();
		List<DaoManage> daoManages = getDaoManages();
		for (DaoManage daoManage : daoManages) {
			daoManage.setTrxName(trxName);
		}
		
		boolean isValidate = true;
		for (ISupportSave saveComponent : saveComponents) {
			 if (!saveComponent.validate()) {
				 isValidate = false;
			 }
		}
		
		if (!isValidate)
			return;
					
		for (ISupportSave saveComponent : saveComponents) {
			saveComponent.save(null, trxName);
		}
		
		for (DaoManage daoManage : daoManages) {
			daoManage.saveDao(trxName);
		}
		
		for (ISupportSave saveComponent : saveComponents) {
			saveComponent.saveAttachment(null, trxName);
		}
	}
}
