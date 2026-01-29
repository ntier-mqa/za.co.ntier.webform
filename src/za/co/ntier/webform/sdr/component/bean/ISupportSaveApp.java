package za.co.ntier.webform.sdr.component.bean;

import java.util.List;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import za.co.ntier.webform.sdr.component.bean.TableModel.DaoManage;

public interface ISupportSaveApp {
	public static class ValidateException extends AdempiereException{

		private static final long serialVersionUID = -3877257428244735438L;
		
		public ValidateException(String msg) {
			super(msg);
		}
	}
	
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
		
		boolean isValidate = ISupportSave.validates(saveComponents);
		
		if (!isValidate)
			throw new ValidateException(Msg.getMsg(Env.getCtx(), "ZZValidateFormFail"));
					
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
