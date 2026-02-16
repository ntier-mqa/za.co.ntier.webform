package za.co.ntier.webform.sdr.component.bean;

import java.util.List;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.PO;

import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.sdr.component.bean.TableModel.DaoManage;

public interface ISaveApp {
	
	abstract public Object getMainApp();
	abstract public MenuContextInfo getMenuContextInfo();
	public default boolean isShowSaveOnFirstTab() {
		return true;
	}
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
	public default List<PO> getDaos(){
		return List.of();
	}
	public default boolean isSupportSave() {
		return true;
	}
	
	public void saveApp();
	
	public void submitApp() ;
	
	public default boolean isSupportSubmit() {
		return false;
	}
	
	
	
	public default boolean isSupportDelete() {
		return true;
	}
	
	public default void deleteApp() {
		MasterUtil.closeActiveWindow();
	}
	
}
