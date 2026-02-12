package za.co.ntier.webform.sdr.component.bean.cell;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.adempiere.exceptions.AdempiereException;
import org.apache.commons.lang3.StringUtils;
import org.compiere.model.PO;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.zkoss.bind.BindUtils;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.UploadEvent;

import za.co.ntier.api.model.I_ZZDocumentUpload;
import za.co.ntier.webform.form.AttachmentUtil;
import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.column.UploadColumnModel;

public class UploadCellModel extends CellModel {
	public UploadCellModel(TableModel tableModel, RowModel rowModel, ColumnModel colModel) {
		super(tableModel, rowModel, colModel);
		setCellType(BTUPLOAD_CELL);
	}

	private String btText;
	public String getBtText() {
		return btText;
	}
	public void setBtText(String btText) {
		this.btText = btText;
	}

	private String fileName;
	private byte[] bytes;     // <- in-memory payload (no disk)

	public String getFileName() { return fileName; }
	public void setFileName(String fileName) { 
		this.fileName = fileName; 
		// refresh the filename label
		BindUtils.postNotifyChange(this, "fileName");
	}

	public byte[] getBytes() { return bytes; }
	public void setBytes(byte[] bytes) { this.bytes = bytes; }

	// (optional) convenience
	public boolean hasBytes() { return bytes != null && bytes.length > 0; }


	public static UploadColumnModel getUploadColumnModel(String title, String daoPropertyName, String daoPropertyFileName, String btText) {
		
		UploadColumnModel uploadColumnModel = CellModel.getColModelForCell(
				CellModelInfo.of(UploadColumnModel.class, UploadCellModel.class, null), CellModelParams.of(title, daoPropertyName, null));
		
		uploadColumnModel.setDaoPropertyFileName(daoPropertyFileName);
		uploadColumnModel.setBtText(btText);
		return uploadColumnModel;
	}
	
	@Override
	public List<String> doValidate(Object inputValue) {
		if (isMandatory() && StringUtils.isBlank(fileName) && bytes == null)
			return List.of(Msg.getMsg(Env.getCtx(), "ZZValidateNotNull"));
		
		return List.of();
	}
	
	public void cmdUploadFile(UploadEvent event) {
		Media m = event.getMedia();

		setFileName(m.getName());

		try {
			byte[] data;
			if (m.isBinary() && m.getByteData() != null) {
				data = m.getByteData();  // fast path
			} else {
				// robust stream fallback (also works for large uploads)
				try (InputStream in = m.getStreamData();
						ByteArrayOutputStream out = new ByteArrayOutputStream(32 * 1024)) {
					byte[] buf = new byte[32 * 1024];
					int n;
					while ((n = in.read(buf)) > 0) out.write(buf, 0, n);
					data = out.toByteArray();
				}
				if (data == null && !m.isBinary() && m.getStringData() != null) {
					data = m.getStringData().getBytes(StandardCharsets.UTF_8);
				}
			}
			setBytes(data);
		} catch (Exception e) {
			throw new AdempiereException("Unable to read uploaded file", e);
		}
		
	}
	
	private boolean removed = false;
	
	public void cmdRemoveAttachment() {
		bytes = null;
		fileName = null;
		removed = true;
		BindUtils.postNotifyChange(this, "fileName");
	}
	
	public void attachFile(PO data, String trxName) {
		byte[] bytes = getBytes(); // <-- in-memory only
		String fileName = getFileName();

		if (removed) {
			AttachmentUtil.removeAttachmentEntry(data, getBtText(), trxName);
		}else if (bytes != null && bytes.length > 0 && org.apache.commons.lang3.StringUtils.isNotBlank(fileName)) {
			// one-entry semantics: delete-and-recreate
			AttachmentUtil.addOrReplaceAttachmentEntry(data, fileName, bytes, getBtText() ,trxName);

			// free memory for this row after persisting
			setBytes(null);
		}
		
	}
	
	@Override
	public void setValueFromDao(PO daoPerCol) {
		String fileName = AttachmentUtil.getFileNameFromAttachmentEntries(
				daoPerCol,
				getBtText(),
				null
				);
		
		setFileName(fileName);
		
	}

	@Override
	public void copyTo(CellModel cellModel) {
		UploadCellModel upCellModelTo = (UploadCellModel)cellModel;
		upCellModelTo.setValue(getValue());
		upCellModelTo.setBytes(getBytes());
		upCellModelTo.setFileName(getFileName());
		upCellModelTo.removed = removed;
	}
	
	@Override
	public void reset(boolean perRow) {
		removed = false;
		bytes = null;
		setFileName(null);
		super.reset(perRow);

	}
	
	@Override
	public boolean isMandatory() {
		CellModel refDocUploadDefCell = null;
		if (getColModel().getRefDocUploadDefCol() != null) {
			refDocUploadDefCell = getRowModel().get(getColModel().getRefDocUploadDefCol());
		}
		
		Object refDocUploadDefObj = null;
		if (refDocUploadDefCell != null) {
			refDocUploadDefObj = refDocUploadDefCell.getValue();
		}
		
		if (refDocUploadDefObj !=null && refDocUploadDefObj instanceof I_ZZDocumentUpload) {
			I_ZZDocumentUpload refDocUploadDefPo = (I_ZZDocumentUpload)refDocUploadDefObj;
			return refDocUploadDefPo.isMandatory();
		}else if (refDocUploadDefObj !=null && !(refDocUploadDefObj instanceof I_ZZDocumentUpload)) {
			throw new AdempiereException(Msg.getMsg(Env.getCtx(), "ZZWrongValueForDocUploadDef"));
		}else {
			return super.isMandatory();
		}
	}
	
	@Override
	public boolean notInputed() {
		return StringUtils.isEmpty(fileName);
	}
}
