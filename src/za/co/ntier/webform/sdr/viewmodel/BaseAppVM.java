package za.co.ntier.webform.sdr.viewmodel;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;

import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.api.model.I_ZZDocumentUploadFile;
import za.co.ntier.api.model.X_ZZDocumentUpload;
import za.co.ntier.api.model.X_ZZDocumentUploadFile;
import za.co.ntier.webform.dga.DgaVM;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.form.bean.component.FormInfo;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.ISaveApp;
import za.co.ntier.webform.sdr.component.bean.ISaveForm;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.TableModel.DaoManage;
import za.co.ntier.webform.sdr.component.bean.TableModel.TitleInfo;
import za.co.ntier.webform.sdr.component.bean.cell.ListCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.ListCellModel.ListColumnModelParam;
import za.co.ntier.webform.sdr.component.bean.cell.PresetTitleCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.UploadCellModel;
import za.co.ntier.webform.sdr.component.bean.column.PresetTitleColumnModel;
import za.co.ntier.webform.sdr.component.bean.column.UploadColumnModel;
import za.co.ntier.webform.sdr.component.tab.bean.NavTab;
import za.co.ntier.webform.sdr.component.tab.bean.NavTabPanel;

public abstract class BaseAppVM implements ISaveApp{
	protected CLogger log = CLogger.getCLogger (getClass());
	
	private FormInfo formInfo;
	
	private MenuContextInfo menuContextInfo;
	
	@Init
	public void initBaseApp(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo){
		this.setMenuContextInfo(menuContextInfo);
		setFormInfo(new FormInfo(menuContextInfo));
	}
	
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
			trx = Trx.get(Trx.createTrxName(this.getClass().getName()), true);
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
		
		// handle afterSave
		Deque<ISaveForm> stack = new ArrayDeque<>(saveComponents);

		while (!stack.isEmpty()) {
		    ISaveForm current = stack.pop();

		    // 1. Process the current item
		    if (current.getAfterAppSave() != null) {
		        current.getAfterAppSave().apply(current, trxName);
		    }

		    // 2. Add children to the stack to process them in the next iterations
		    List<ISaveForm> children = current.getChildren();
		    if (children != null) {
		        for (ISaveForm child : children) {
		            stack.push(child);
		        }
		    }
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
	
	protected static TableModel initUploadTab(NavTab mainTab, String tabTitle,
			List<X_ZZDocumentUpload> docUploads) {
		if (docUploads.size() == 0)
			return null;
		
		List<ColumnModel> cols = new ArrayList<>();
		
		Function<X_ZZDocumentUpload, String> convertDisplay = docUploadDef -> {
				if (docUploadDef == null)
					return null;
				return docUploadDef.getName();
			};
		
		final PresetTitleColumnModel<X_ZZDocumentUpload> documentNameCol = PresetTitleCellModel.getPresetTitleColumnModel(
				"Name"
				, convertDisplay
				);
		documentNameCol.setMatchingLoaded((rowModel, poSaveds) -> {
			X_ZZDocumentUpload docDef = (X_ZZDocumentUpload)rowModel.get(documentNameCol).getValue();
			for (PO po:poSaveds) {
				if (po != null && po instanceof X_ZZDocumentUploadFile) {
					X_ZZDocumentUploadFile poSaved = (X_ZZDocumentUploadFile)po;
					return docDef.getZZDocumentUpload_ID() == ((X_ZZDocumentUploadFile)poSaved).getZZDocumentUpload_ID();
				}
			}
			
			return false;
		});
				
		cols.add(documentNameCol);
		
		UploadColumnModel uploadCol = UploadCellModel.getUploadColumnModel("", I_ZZDocumentUploadFile.COLUMNNAME_Name, null, "doc");
		uploadCol.setRefDocUploadDefCol(documentNameCol);
		cols.add(uploadCol);
		
		NavTabPanel uploadDocInfoTab = new NavTabPanel(mainTab);
		uploadDocInfoTab.setTabTitle(tabTitle);
		
		TableModel tmUploadDocInfo = TableModel.getTableBean(TableModel.class, cols, false, I_ZZDocumentUploadFile.Table_Name);
		tmUploadDocInfo.setViewModel(TableModel.ViewType.VIEW_GRID);
		tmUploadDocInfo.setSclass("uploadDocInfo");
		tmUploadDocInfo.setPoSupplier(rowModel -> {
			X_ZZDocumentUploadFile po = new X_ZZDocumentUploadFile(Env.getCtx(), 0, null);
			X_ZZDocumentUpload docDef = (X_ZZDocumentUpload)rowModel.get(documentNameCol).getValue();
			//po.setName(docDef.getName());
			po.setZZDocumentUpload_ID(docDef.getZZDocumentUpload_ID());
			return po;
		});
		
		tmUploadDocInfo.init(null, TitleInfo.createTitleInfo(DgaVM.getTitleMap(documentNameCol, docUploads)));
		
		uploadDocInfoTab.getCompModel().add(tmUploadDocInfo);
		
		return tmUploadDocInfo;
	}

	@Override
	public MenuContextInfo getMenuContextInfo() {
		return menuContextInfo;
	}

	public void setMenuContextInfo(MenuContextInfo menuContextInfo) {
		this.menuContextInfo = menuContextInfo;
	}

	public final FormInfo getFormInfo() {
		return formInfo;
	}

	public final void setFormInfo(FormInfo formInfo) {
		this.formInfo = formInfo;
	}
	
}
