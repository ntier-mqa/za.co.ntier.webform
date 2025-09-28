package za.co.ntier.webform.form;


import org.compiere.model.MAttachment;
import org.compiere.model.PO;
import org.compiere.util.Env;

public final class AttachmentUtil {
    private AttachmentUtil() {}

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

        // sanitize btText: trim and remove '/'
        final String rawPrefix = btText == null ? "" : btText.trim().replace("/", "");
        final String normalizedPrefix = rawPrefix.isEmpty() ? "" : (rawPrefix + ":");
        final String newEntryName = normalizedPrefix.isEmpty() ? fileName : (rawPrefix + ": " + fileName);

        // get or create container
        MAttachment att = MAttachment.get(Env.getCtx(), po.get_Table_ID(), po.get_ID(), trxName);
        if (att == null) {
            att = new MAttachment(Env.getCtx(), po.get_Table_ID(), po.get_ID(), /*Record_UU*/ null, trxName);
        }

        // find existing entry by sanitized "<btText>:" prefix (with optional space in stored name)
        int idx = findEntryIndexByPrefix(att, normalizedPrefix);

        if (idx >= 0) {
            String currentName = safeName(att.getEntryName(idx));
            // if the visible name is the same, just update bytes; else rename via delete+add
            if (currentName.equals(newEntryName)) {
                att.updateEntry(idx, bytes);
            } else {
                att.deleteEntry(idx);
                att.addEntry(newEntryName, bytes);
            }
        } else {
            // not present -> append
            att.addEntry(newEntryName, bytes);
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
}
