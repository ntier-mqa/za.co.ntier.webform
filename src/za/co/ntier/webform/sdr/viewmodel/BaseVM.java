package za.co.ntier.webform.sdr.viewmodel;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.logging.Level;

import org.compiere.util.CLogger;
import org.compiere.util.Trx;
import org.zkoss.bind.annotation.Command;

import za.co.ntier.webform.form.MasterUtil;

public abstract class BaseVM {
	protected CLogger log = CLogger.getCLogger (getClass());
	
	@Command(value = "saveClose")
	public void saveClose() throws IOException {
		Trx trx = null;
		Boolean success = null;
		Exception exc = null;
		
		try {
			trx = Trx.get(Trx.createTrxName("MaintainOrganisation"), true);
			trx.setDisplayName(getClass().getName()+"_saveClose");
			
			doSave(trx.getTrxName());

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
	
	
	protected boolean showResult(Exception exc) {
		return true;
	}
	
	protected void showException(Exception exc) {
		if (exc != null) {
			MasterUtil.showDialog(exc.getMessage(), null);
		}
	}
	
	protected abstract void doSave(String trxName);
	
}
