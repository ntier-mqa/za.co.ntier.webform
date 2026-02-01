package za.co.ntier.webform.sdr.component.bean;

import java.util.List;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.sdr.component.bean.TableModel.DaoManage;

public interface ISaveApp {
	public static class ValidateException extends AdempiereException{

		private static final long serialVersionUID = -3877257428244735438L;
		
		public ValidateException(String msg) {
			super(msg);
		}
	}
	
	public default List<ISaveForm> getSaveComponents(){
		return List.of();
	}
	public default List<DaoManage> getDaoManages(){
		return List.of();
	}
	
	public default boolean isSupportSave() {
		return true;
	}
	
	public void saveApp();
	
	public default void doSave(String trxName) {
		List<ISaveForm> saveComponents = getSaveComponents();
		List<DaoManage> daoManages = getDaoManages();
		for (DaoManage daoManage : daoManages) {
			daoManage.setTrxName(trxName);
		}
		
		boolean isValidate = ISaveForm.validates(saveComponents);
		
		if (!isValidate)
			throw new ValidateException(Msg.getMsg(Env.getCtx(), "ZZValidateFormFail"));
					
		for (ISaveForm saveComponent : saveComponents) {
			saveComponent.save(trxName);
		}
		
		for (DaoManage daoManage : daoManages) {
			daoManage.saveDao(trxName);
		}
		
		for (ISaveForm saveComponent : saveComponents) {
			saveComponent.saveAttachment(trxName);
		}
	}
	
	public default boolean isSupportSubmit() {
		return false;
	}
	
	public default void submitApp() {
		// do nothing
	}
	
	public default boolean isSupportDelete() {
		return true;
	}
	
	public default void deleteApp() {
		MasterUtil.closeActiveWindow();
	}
	
}
