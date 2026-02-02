package za.co.ntier.webform.sdr.viewmodel;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;

import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.zkoss.bind.annotation.Command;

import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.sdr.component.bean.ISaveApp;
import za.co.ntier.webform.sdr.component.bean.ISaveForm;
import za.co.ntier.webform.sdr.component.bean.ISaveApp.ValidateException;
import za.co.ntier.webform.sdr.component.bean.TableModel.DaoManage;

public abstract class BaseAppVM implements ISaveApp{
	protected CLogger log = CLogger.getCLogger (getClass());
	
	@Command
	@Override
	public void saveApp() {
		trxWrapper(trxName -> doSave(trxName));
	}
	
	@Command
	@Override
	public void submitApp() {
		trxWrapper(trxName -> {
			doSave(trxName);
		});
	}
	
	/**
	 * override to change doc status
	 */
	public void doSubmit(String trxName) {
		// implement to change doc status
	}
	
	public void trxWrapper(Consumer<String> func) {
		Trx trx = null;
		Boolean success = null;
		Exception exc = null;
		
		try {
			trx = Trx.get(Trx.createTrxName("MaintainOrganisation"), true);
			trx.setDisplayName(getClass().getName()+"_saveClose");
			
			func.accept(trx.getTrxName());

			success = true;
		}catch (Exception e) {
			success = false;
			exc = e;
			log.log(Level.WARNING, "App error", exc);
		}finally {
			if (success != null && success && trx != null) {
				try {
					trx.commit(true);
				} catch (Exception e){
					log.log(Level.SEVERE, "Commit failed", e);
					exc = e;
				}
			}
			
			if (success != null && !success && trx != null) {
				log.log(Level.INFO, "Rollback");
				trx.rollback();
			}
			
			if (trx != null)
				trx.close();
			
			if (showResult(exc)) {
				if (exc != null) {
					showException(exc);
				}else {
					MasterUtil.showDialog("ZZSuccess", MasterUtil.fCloseActiveWindow);
				}
			}
			
		}
	}
	
	
	public void doSave(String trxName) {
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
	
	protected boolean showResult(Exception exc) {
		return true;
	}
	
	protected void showException(Exception exc) {
		if (exc != null) {
			MasterUtil.showDialog(exc.getMessage(), null);
		}
	}
	
}
