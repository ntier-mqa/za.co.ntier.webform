package za.co.ntier.webform.process;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Statement;

import org.adempiere.base.annotation.Parameter;
import org.adempiere.webui.apps.ProcessModalDialog;
import org.compiere.model.MAttachment;
import org.compiere.model.MAttachmentEntry;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

@org.adempiere.base.annotation.Process(name="za.co.ntier.webform.process.RecognizeAttachmentMissingFile")
public class RecognizeAttachmentMissingFile extends SvrProcess{

	@Parameter(name = "AD_Column_ID")
	private int recognizeFlagColumnID;
	
	@Override
	protected void prepare() {
		
	}

	@Override
	protected String doIt() throws Exception {
		ProcessModalDialog modalDialog = (ProcessModalDialog)processUI;
		String tabSql = Env.getContext(getCtx(), modalDialog.getWindowNo(), modalDialog.getTabNo(), "_TabInfo_SQL");
		
		
		MTable table = new MTable(getCtx(), getTable_ID(), null);
		String tableName = table.getTableName();
		Statement stmt = DB.createStatement();
		ResultSet rs = stmt.executeQuery(tabSql);
		
		while (rs.next()) {
			BigDecimal idBd = rs.getBigDecimal(tableName + "_ID");
			PO po = table.getPO(rs, get_TrxName());
			int id = idBd.intValueExact();
			MAttachment attachment = MAttachment.get(getCtx(), getTable_ID(), id);
			if (attachment != null) {
				MAttachmentEntry[] attachEntrys = attachment.getEntries();
				for (MAttachmentEntry attachEntry : attachEntrys) {
					boolean fileExists = attachEntry.getData() != null && attachEntry.getData().length > 0;
					if (recognizeFlagColumnID > 0 && fileExists) {
						po.set_ValueOfColumn(recognizeFlagColumnID, "N");
					}else if(recognizeFlagColumnID > 0 && !fileExists){
						po.set_ValueOfColumn(recognizeFlagColumnID, "Y");
					}
					
					if (recognizeFlagColumnID > 0) {
						po.saveEx(get_TrxName());
					}
				}
			}
		}
		
		return null;
	}

}
