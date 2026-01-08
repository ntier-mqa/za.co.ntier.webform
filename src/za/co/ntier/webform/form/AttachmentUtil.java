package za.co.ntier.webform.form;


import org.apache.commons.lang3.StringUtils;
import org.compiere.model.MAttachment;
import org.compiere.model.PO;
import org.compiere.util.Env;

public final class AttachmentUtil {
    private AttachmentUtil() {}

    private record EntryNames(String normalizedPrefix, String newEntryName) {}
    
    private static EntryNames normalizedEntryNames (String btText, String fileName) {
    	// sanitize btText: trim and remove '/'
    	String rawPrefix = btText == null ? "" : btText.trim().replace("/", "");
        String normalizedPrefix = rawPrefix.isEmpty() ? "" : (rawPrefix + ":");
        String newEntryName = normalizedPrefix.isEmpty() ? fileName : (rawPrefix + ": " + fileName);
        return new EntryNames(normalizedPrefix, newEntryName);
    }
    
    public static void removeAttachmentEntry(
            PO po, String btText, String trxName) {
    	
    	EntryNames entryNames = AttachmentUtil.normalizedEntryNames (btText, null);

        // get or create container
        MAttachment att = MAttachment.get(Env.getCtx(), po.get_Table_ID(), po.get_ID(), trxName);
        if (att != null) {
        	// find existing entry by sanitized "<btText>:" prefix (with optional space in stored name)
            int idx = findEntryIndexByPrefix(att, entryNames.normalizedPrefix);
            att.deleteEntry(idx);
            att.saveEx(trxName);
        }        
    }
    /**
     * Add or replace exactly one attachment entry identified by name prefix "<btText>:".
     * If found, updates its bytes; if the displayed file name changed, it deletes+readds
     * the entry with the new name. If not found, it appends a new entry.
     *
     * NOTE: Any '/' characters in btText are removed before use.
     */
    public static void addOrReplaceAttachmentEntry(
            PO po, String fileName, byte[] bytes, String btText, String trxName) {

        if (po == null || po.get_ID() <= 0)
            throw new IllegalArgumentException("Persist PO before attaching");
        if (bytes == null || bytes.length == 0)
            throw new IllegalArgumentException("Empty attachment data");

        EntryNames entryNames = AttachmentUtil.normalizedEntryNames (btText, fileName);

        // get or create container
        MAttachment att = MAttachment.get(Env.getCtx(), po.get_Table_ID(), po.get_ID(), trxName);
        if (att == null) {
            att = new MAttachment(Env.getCtx(), po.get_Table_ID(), po.get_ID(), /*Record_UU*/ null, trxName);
        }

        // find existing entry by sanitized "<btText>:" prefix (with optional space in stored name)
        int idx = findEntryIndexByPrefix(att, entryNames.normalizedPrefix);

        if (idx >= 0) {
            String currentName = safeName(att.getEntryName(idx));
            // if the visible name is the same, just update bytes; else rename via delete+add
            if (currentName.equals(entryNames.newEntryName)) {
                att.updateEntry(idx, bytes);
            } else {
                att.deleteEntry(idx);
                att.addEntry(entryNames.newEntryName, bytes);
            }
        } else {
            // not present -> append
            att.addEntry(entryNames.newEntryName, bytes);
        }

        att.saveEx(trxName);
    }

    private static int findEntryIndexByPrefix(MAttachment att, String prefix) {
        if (att == null) return -1;
        String want = prefix == null ? "" : prefix.trim();
        if (!want.isEmpty() && !want.endsWith(":")) want = want + ":";

        for (int i = 0, n = att.getEntryCount(); i < n; i++) {
            String name = safeName(att.getEntryName(i));
            // normalize optional space after colon so "btText:filename" and "btText: filename" both match
            String normalized = name.replaceFirst("^([^:]+):\\s*", "$1:");
            // also remove any '/' from the stored name's prefix so legacy names match sanitized btText
            normalized = normalized.replace("/", "");
            if (normalized.startsWith(want)) {
                return i;
            }
        }
        return -1;
    }

    private static String safeName(String s) {
        return s == null ? "" : s;
    }
    
    public static String getFileNameFromAttachmentEntries(PO po, String btText, String trxName) {
        if (po == null || po.get_ID() <= 0) return null;

        MAttachment att = MAttachment.get(Env.getCtx(), po.get_Table_ID(), po.get_ID(), trxName);
        if (att == null || att.getEntryCount() == 0) return null;

        // sanitize btText: trim and remove all '/'
        final String rawPrefix = btText == null ? "" : btText.trim().replace("/", "");
        final String want = rawPrefix.isEmpty() ? "" : (rawPrefix + ":");

        int idx = findEntryIndexByPrefix(att, want);
        if (idx < 0 && StringUtils.isNotBlank(btText)) {
        	return null;
        }else if (idx < 0) {
        	idx = 0; // fallback to first entry
        }

        String entryName = safe(att.getEntryName(idx));
        // strip "<btText>:" (with optional space) to return the pure file name
        String base = entryName.replaceFirst("^([^:]+):\\s*", "");
        return StringUtils.defaultIfBlank(base, entryName);
    }

   

    private static String safe(String s) { return s == null ? "" : s; }

}
