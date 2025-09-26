package za.co.ntier.webform.form;

import org.compiere.model.MAttachment;
import org.compiere.model.PO;
import org.compiere.util.Env;

public final class AttachmentUtil {
    private AttachmentUtil() {}

    /**
     * Add or replace an attachment entry (match by file name) for the given PO.
     * Uses the non-deprecated constructor and index-based update APIs.
     */
    public static void addOrReplaceAttachmentEntry(PO po, String fileName, byte[] bytes, String trxName) {
    	if (po == null || po.get_ID() <= 0)
            throw new IllegalArgumentException("Persist PO before attaching");

        // If an attachment exists, remove it (provider cleanup is handled in postDelete)
        MAttachment existing = MAttachment.get(Env.getCtx(), po.get_Table_ID(), po.get_ID(), trxName);
        if (existing != null) {
            existing.delete(true); // calls beforeDelete/postDelete, removes provider blobs too
        }

        // Create a fresh attachment with a single entry
        MAttachment att = new MAttachment(Env.getCtx(), po.get_Table_ID(), po.get_ID(), /*Record_UU*/ null, trxName);
        att.addEntry(fileName, bytes);
        att.saveEx(trxName);
    }
}
