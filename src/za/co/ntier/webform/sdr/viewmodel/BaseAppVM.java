package za.co.ntier.webform.sdr.viewmodel;

import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;

import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.zkoss.bind.annotation.Command;

import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.sdr.component.bean.ISaveApp;
import za.co.ntier.webform.sdr.component.bean.ISaveForm;
import za.co.ntier.webform.sdr.component.bean.TableModel.DaoManage;

public abstract class BaseAppVM implements ISaveApp{
	protected CLogger log = CLogger.getCLogger (getClass());
	
	@Command
	@Override
	public void saveApp() {
		isSubmit = false;
		trxWrapper(trxName -> doSave(trxName));
	}
	
	@Command
	@Override
	public void submitApp() {
		isSubmit = true;
		trxWrapper(trxName -> {
			doSave(trxName);
			doSubmit(trxName);
		});
	}
	
	private boolean isSubmit = false;
	
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
			
			
			if (exc != null) {
				showException(exc);
			}else {
				showResult(isSubmit);	
			}
			
		}
	}
	
	
	public void doSave(String trxName) {
		List<ISaveForm> saveComponents = getSaveComponents();
		List<DaoManage> daoManages = getDaoManages();
		List<PO> daos = getDaos();
		
		for (DaoManage daoManage : daoManages) {
			daoManage.setTrxName(trxName);
		}
		
		boolean isValidate = ISaveForm.validates(saveComponents, isSubmit);
		
		if (!isValidate)
			throw new ValidateException(Msg.getMsg(Env.getCtx(), "ZZValidateFormFail"));
					
		for (ISaveForm saveComponent : saveComponents) {
			saveComponent.syncUIToDao(trxName);
		}
		
		for (DaoManage daoManage : daoManages) {
			daoManage.saveDao(trxName);
		}
		
		for (PO dao : daos) {
			dao.saveEx(trxName);
		}
		
		for (ISaveForm saveComponent : saveComponents) {
			saveComponent.saveToDb(trxName);
		}
		
		for (ISaveForm saveComponent : saveComponents) {
			saveComponent.saveAttachment(trxName);
		}
	}
	
	protected void showResult(boolean isSubmit) {
		MasterUtil.showInfoDialog("ZZSuccess", MasterUtil.fCloseActiveWindow);
	}
	
	protected void showException(Exception exc) {
		if (exc != null) {
			MasterUtil.showInfoDialog(exc.getMessage(), null);
		}
	}
	
}
