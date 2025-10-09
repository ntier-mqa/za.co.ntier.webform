package za.co.ntier.webform.form.bean.component;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.webui.exception.ApplicationException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.compiere.model.MCity;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.CLogger;
import org.zkoss.bind.BindUtils;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.event.UploadEvent;

import za.co.ntier.api.model.I_ZZBankingDetails;
import za.co.ntier.api.model.X_ZZBankingDetails;
import za.co.ntier.api.model.X_ZZ_Application_Form;
import za.co.ntier.webform.form.AttachmentUtil;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.Util;
import za.co.ntier.webform.form.bean.DataType;

public class AnnexureInfo implements ISaveForm{
	protected static final CLogger log = CLogger.getCLogger(AnnexureInfo.class);
	public final static String AnnexureTypeExitStrategy = "EXIT STRATEGY";
	public final static String AnnexureTypeTargetGroup = "TARGET GROUP";
	public final static String AnnexureTypeBudgetOverview = "BUDGET OVERVIEW";

	private String dataType;
	private boolean createNewRowWhenEmpty = true;

	public static <T extends AnnexureInfo> T getAnnexureInfo(Class<T> clazz, List<ColumnInfo<?>> columnInfos,
			boolean isShowTotal){

		T annexureInfo;
		try {
			annexureInfo = clazz.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			throw new ApplicationException(e.getMessage(), e);
		}
		annexureInfo.setShowTotal(isShowTotal);
		annexureInfo.setColumnInfos(columnInfos);

		List<AnnexureRow> rows = new ArrayList<>();
		annexureInfo.setRows(rows);

		if (isShowTotal) {
			Map<ColumnInfo<?>, Object> totalRow = new HashMap<>();
			for (ColumnInfo<?> columnInfo : columnInfos) {
				if (columnInfo.getDataType() == DataType.PositiveNumber) {
					totalRow.put(columnInfo, new IntData(annexureInfo, totalRow, 0));
				} else {
					totalRow.put(columnInfo, null);
				}
			}

			if (totalRow.get(columnInfos.get(0)) == null) {
				totalRow.put(columnInfos.get(0), "Total");
			}

			annexureInfo.setTotalRow(totalRow);
		}
		return annexureInfo;
	}

	public static Integer getIntegerValue(AnnexureInfo cetTvetOneLineInput, ColumnInfo<?> col) {
		return AnnexureInfo.getIntegerValue(cetTvetOneLineInput.getRows().get(0), col);
	}

	public static Integer getIntegerValue(Map<ColumnInfo<?>, Object> cetTvetMultiLineRow, ColumnInfo<?> colInfo) {

		if (colInfo != null && colInfo.getDataType() == DataType.PositiveNumber && cetTvetMultiLineRow.get(colInfo) != null) {
			IntData cellData = (IntData)cetTvetMultiLineRow.get(colInfo);
			return cellData.getValue();
		}

		return null;
	}

	public static ColumnInfo<?> lookupCol(DataType dataType, String colName, AnnexureInfo annexure){
		return lookupCol(dataType, colName, annexure.getColumnInfos());
	}

	public static ColumnInfo<?> lookupCol(DataType dataType, String colName, Collection<ColumnInfo<?>> cols){
		ColumnInfo<?> foundCol = null;

		for (ColumnInfo<?> col : cols) {
			if (dataType != null  && colName != null && dataType.equals(col.getDataType()) && colName.equals(col.getTitle())) {
				return col;
			}

			if (dataType != null && dataType.equals(col.getDataType())){
				return  col;
			}

			if (colName != null && colName.equals(col.getTitle())){
				return  col;
			}
		}

		return foundCol;
	}

	public static ColumnInfo<?> lookupColByDataType(DataType dataType, AnnexureInfo annexure){
		return lookupCol(dataType, null, annexure);
	}

	public static ColumnInfo<?> lookupColByDataType(DataType dataType, Collection<ColumnInfo<?>> cols){
		return lookupCol(dataType, null, cols);
	}

	public static ColumnInfo<?> lookupColByTitle(String colName, AnnexureInfo annexure){
		return lookupCol(null, colName, annexure);
	}
	public static ColumnInfo<?> lookupColByTitle(String colName, Collection<ColumnInfo<?>> cols){
		return lookupCol(null, colName, cols);
	}

	private List<ColumnInfo<?>> columnInfos;
	private BiFunction<AnnexureInfo, X_ZZ_Application_Form, PO> poSupplier;
	private List<AnnexureRow> rows;
	private String sectionHeader;

	private boolean showAddButton = false;

	private boolean showTotal = false;

	private boolean showColumnHeader = true;

	private AnnexureInfo subAnnexure;

	private String subSectionHeader;

	private String tableTitle;

	private Map<ColumnInfo<?>, Object> totalRow;

	public void addRow() {
		createDetailRow();
		BindUtils.postNotifyChange(this, "rows");
	}

	public void areaSelect (Map<ColumnInfo<?>, Object> row, 
			ColumnInfo<?> col,
			SelectEvent<?, ?> event){

	}

	public AnnexureRow createDetailRow() {
		return createDetailRow(null);
	}
	@SuppressWarnings("unchecked")
	public AnnexureRow createDetailRow(Map<ColumnInfo<?>, Object> rowTitle) {

		AnnexureRow row = new AnnexureRow(this);

		for (ColumnInfo<?> columnInfo : columnInfos) {
			Object cellData = null;

			cellData = row.get(columnInfo);

			if (cellData == null) {

				if (columnInfo.getDataType() == DataType.TwoTitles) {
					cellData = Arrays.asList(null, null);

				} else if (columnInfo.getDataType() == DataType.TwoValues) {
					cellData = Arrays.asList(null, null);

				} else if (columnInfo.getDataType() == DataType.FileUpload) {
					cellData = new UploadData();

				} else if (columnInfo.getDataType() == DataType.Area) {
					AreaData areaData = new AreaData(this, row);
					areaData.setDataProvider((List<MCity>)columnInfo.getDataProvider());
					cellData = areaData;
				} else if (columnInfo.getDataType() == DataType.Postal) {
					PostalData textData = new PostalData(this, row, null);
					cellData = textData;
				}else if (columnInfo.getDataType() == DataType.PositiveNumber) {
					cellData = new IntData(this, row, null);
				}else if (columnInfo.getDataType() == DataType.Label) {
					cellData = new LabelData();
				}else if (columnInfo.getDataType() == DataType.Date) {
					cellData = new DateData();
				}else if (columnInfo.getDataType() == DataType.Text) {
					cellData = new TextData();
				}

			}
			
			row.put(columnInfo, cellData);
		}
		
		if (rowTitle != null) {
			for (Entry<ColumnInfo<?>, Object> colTile : rowTitle.entrySet()) {
				AnnexureInfo.setCellValue(row, colTile.getKey(), colTile.getValue());
			}
		}
		
		if (decoratorCell != null) {
			decoratorCell.accept(row);
		}

		getRows().add(row);
		return row;
	}

	private Consumer<AnnexureRow> decoratorCell;
	
	public Consumer<AnnexureRow> getDecoratorCell() {
		return decoratorCell;
	}

	public void setDecoratorCell(Consumer<AnnexureRow> decoratorCell) {
		this.decoratorCell = decoratorCell;
	}

	/**
	 * update all cell on total row, use on init data
	 */
	public void updateTotalRow() {
		if (!showTotal)
			return;

		for (ColumnInfo<?> col:getColumnInfos()) {
			if (col.getDataType() == DataType.PositiveNumber) {
				updateTotalRow(col, false);
			}
		}
	}

	/**
	 * update total cell of column has data change
	 * @param col
	 * @param needNotify
	 */
	public void updateTotalRow(ColumnInfo<?> col, boolean needNotify) {
		Integer total = 0;
		for (Map<ColumnInfo<?>, Object> r : getRows()) {
			IntData intData = (IntData)r.get(col);
			if (intData.getValue() != null) {
				total += intData.getValue();
			}
		}

		IntData totalValue = (IntData)totalRow.get(col);
		totalValue.setValue(total);

		if(needNotify) {
			BindUtils.postNotifyChange(totalValue, "value");
		}
	}

	/**
	 * @return the columnInfos
	 */
	public List<ColumnInfo<?>> getColumnInfos() {
		return columnInfos;
	}

	/**
	 * @return the rows
	 */
	public List<AnnexureRow> getRows() {
		return rows;
	}

	/**
	 * @return the sectionHeader
	 */
	public String getSectionHeader() {
		return sectionHeader;
	}

	/**
	 * @return the subAnnexure
	 */
	public AnnexureInfo getSubAnnexure() {
		return subAnnexure;
	}

	/**
	 * @return the subSectionHeader
	 */
	public String getSubSectionHeader() {
		return subSectionHeader;
	}

	/**
	 * @return the tableTitle
	 */
	public String getTableTitle() {
		return tableTitle;
	}

	/**
	 * @return the totalRow
	 */
	public Map<ColumnInfo<?>, Object> getTotalRow() {
		return totalRow;
	}

	/**
	 * @return the showAddButton
	 */
	public boolean isShowAddButton() {
		return showAddButton;
	}

	/**
	 * @return the showTotal
	 */
	public boolean isShowTotal() {
		return showTotal;
	}

	public void numChange(AnnexureRow row, ColumnInfo<?> col, InputEvent event) {
		// update total row
		if (col.getDataType() == DataType.PositiveNumber && showTotal) {
			updateTotalRow(col, true);
		}

		if (col.getDataType() == DataType.PositiveNumber) {
			updateExpressionCol (row, true);
		}

	}

	public void updateExpressionCol () {
		boolean hasExpressionCol = false;
		for (ColumnInfo<?> colTotal : getColumnInfos()) {
			if (colTotal.getExpression() != null) {
				hasExpressionCol = true;
			}
		}

		if(hasExpressionCol) {
			for(AnnexureRow row:getRows()) {
				updateExpressionCol(row, false);
			}
		}
	}
	/**
	 * update cell all Expression Col on current row
	 * @param row
	 */
	public void updateExpressionCol (AnnexureRow row, boolean needNotify) {
		// update expression column
		for (ColumnInfo<?> colTotal : getColumnInfos()) {
			if (colTotal.getExpression() != null) {
				Integer value = colTotal.getExpression().apply(row);
				LabelData expressionLable = (LabelData) row.get(colTotal);
				if (value == null) {
					expressionLable.setValue(null);
				}else {
					expressionLable.setValue(String.valueOf(value));
				}
				if(needNotify) {
					BindUtils.postNotifyChange(expressionLable, "value");
				}

			}
		}
	}

	public void postalChange (Map<ColumnInfo<?>, Object> row, 
			ColumnInfo<?> col,
			InputEvent event){

	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		// TODO Auto-generated method stub

	}

	/**
	 * @param columnInfos the columnInfos to set
	 */
	public void setColumnInfos(List<ColumnInfo<?>> columnInfos) {
		this.columnInfos = columnInfos;
	}

	/**
	 * @param rows the rows to set
	 */
	public void setRows(List<AnnexureRow> rows) {
		this.rows = rows;
	}

	/**
	 * @param sectionHeader the sectionHeader to set
	 */
	public void setSectionHeader(String sectionHeader) {
		this.sectionHeader = sectionHeader;
	}

	/**
	 * @param showAddButton the showAddButton to set
	 */
	public void setShowAddButton(boolean showAddButton) {
		this.showAddButton = showAddButton;
	}

	/**
	 * @param showTotal the showTotal to set
	 */
	public void setShowTotal(boolean showTotal) {
		this.showTotal = showTotal;
	}

	/**
	 * @param subAnnexure the subAnnexure to set
	 */
	public void setSubAnnexure(AnnexureInfo subAnnexure) {
		this.subAnnexure = subAnnexure;
	}

	/**
	 * @param subSectionHeader the subSectionHeader to set
	 */
	public void setSubSectionHeader(String subSectionHeader) {
		this.subSectionHeader = subSectionHeader;
	}

	/**
	 * @param tableTitle the tableTitle to set
	 */
	public void setTableTitle(String tableTitle) {
		this.tableTitle = tableTitle;
	}

	/**
	 * @param totalRow the totalRow to set
	 */
	public void setTotalRow(Map<ColumnInfo<?>, Object> totalRow) {
		this.totalRow = totalRow;
	}	


	public void uploadFile(Map<ColumnInfo<?>, Object> row, ColumnInfo<?> col, UploadEvent event) {
		UploadData ud = (UploadData) row.get(col);
		Media m = event.getMedia();

		ud.setFileName(m.getName());

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
			ud.setBytes(data);
		} catch (Exception e) {
			throw new AdempiereException("Unable to read uploaded file", e);
		}

		// refresh the filename label
		BindUtils.postNotifyChange(ud, "fileName");
	}

	/**
	 * @return the showColumnHeader
	 */
	public boolean isShowColumnHeader() {
		return showColumnHeader;
	}

	/**
	 * @param showColumnHeader the showColumnHeader to set
	 */
	public void setShowColumnHeader(boolean showColumnHeader) {
		this.showColumnHeader = showColumnHeader;
	}

	/**
	 * @return the dataType
	 */
	public String getDataType() {
		return dataType;
	}

	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Entry<Integer, Boolean> fillDaoData (Collection<ColumnInfo<?>> cols, AnnexureRow row, Object dao){
		Integer total = Integer.valueOf(0);
		boolean isEmptyData = true;
		boolean isFullData = true;
		boolean hasDaoPropertyName = false;
		for (ColumnInfo<?> col:cols) {
			boolean ignoreSetDao = false;
			if (StringUtils.isNotBlank(col.getDaoPropertyName())){
				hasDaoPropertyName = true;
				Boolean hasCellData = Boolean.FALSE;
				Object cellValueObj = null;
				if (col.getDataType() == DataType.PositiveNumber) {
					Entry<Integer, Boolean>  entryIntObj = getCellValue(row, col);
					if (col.isCalTotal())
						total += entryIntObj.getKey();
					cellValueObj = entryIntObj.getKey();
					hasCellData = entryIntObj.getValue();
				}else if (col.getDataType() == DataType.Text) {
					Entry<Integer, Boolean>  entryTextObj = getCellValue(row, col);
					cellValueObj = entryTextObj.getKey();
					hasCellData = entryTextObj.getValue();
				}else if (col.getDataType() == DataType.LearnerInfo) {
					LearnerInputInfo learnerInfo = (LearnerInputInfo)row.get(col);
					int learnerInputID = 0;// int of PO don't accept null
					if (learnerInfo != null) {
						learnerInputID = learnerInfo.getLearnerInputID();
						hasCellData = Boolean.TRUE;
					}
					cellValueObj = learnerInputID;
				}else if (col.getDataType() == DataType.Date) {
					DateData dateData = (DateData)row.get(col);
					cellValueObj = dateData.getTimestamp();
					if (cellValueObj != null) {
						hasCellData = true;
					}
				}else if (col.getDataType() == DataType.FileUpload) {
					// Martin changed to save to attachments instead to a binary file
					UploadData uploadData = (UploadData) row.get(col);
					if (uploadData != null && uploadData.getBytes() != null && uploadData.getBytes().length > 0) {
						hasCellData = Boolean.TRUE;
					}
					ignoreSetDao = true; // never set DAO properties for files anymore
				}else if (col.getDataType() == DataType.List) {
					Object selectedObj = row.get(col);
					if (StringUtils.isNotBlank(col.getBeanPropertyName())) {
						if (selectedObj != null) {
							cellValueObj = AnnexureInfo.getDaoValue(selectedObj, col.getBeanPropertyName());
							hasCellData = Boolean.TRUE;
						}else {
							cellValueObj = null;
						}
					}else {
						cellValueObj = selectedObj;
						if (cellValueObj != null)
							hasCellData = Boolean.TRUE;
					}
				}else {
					Entry<Object, Boolean> celDataEntryObj = getCellValue(row, col);
					cellValueObj = celDataEntryObj.getKey();
					hasCellData = celDataEntryObj.getValue();
				}

				if (hasCellData) {
					isEmptyData = false;
				}else {
					isFullData = false;
				}

				if (!ignoreSetDao) {
					if (cellValueObj == null) {
						try {
							PropertyDescriptor pd = new PropertyDescriptor(col.getDaoPropertyName(), dao.getClass());
							Class<?> propertyType = pd.getPropertyType();
							if(propertyType.getTypeName().equals("int")) {
								cellValueObj = 0;
							}
						} catch (IntrospectionException e) {
							e.printStackTrace();
							throw new AdempiereException(e.getMessage(), e);
						}
					}

					setDaoValue(dao, col.getDaoPropertyName(), cellValueObj);
				}
			}
		}
		
		// null mean don't need to save po (no properties for save or empty input data)
		// false mean input some item
		// true mean input full
		Boolean rowStatus = null;
		if (!hasDaoPropertyName) {
			rowStatus = null;
		}else if (isEmptyData) {
			rowStatus = null;
		}else if (!isFullData) {
			rowStatus = false;
		}else {
			rowStatus = true;
		}
		return new AbstractMap.SimpleEntry<>(total, rowStatus);
	}


	public void save(String trxName, X_ZZ_Application_Form applicationForm) {

		int total = 0;

		for (AnnexureRow row : getRows()) {
			PO po = (PO) row.getData();
			if (po == null) {
				applicationForm.set_TrxName(trxName); // important
				po = poSupplier.apply(this, applicationForm);
			}

			Entry<Integer, Boolean> result = fillDaoData(getColumnInfos(), row, po);

			if (result.getValue() == null) {
				// delete row record if user cleared all input for this row
				po.delete(true);
			}else if (result.getValue()) {
				po.saveEx(trxName);
				total += result.getKey();

				// --- Save FileUpload columns as ATTACHMENTS (memory only) ---
				for (ColumnInfo<?> col : getColumnInfos()) {
					if (col.getDataType() != DataType.FileUpload) continue;

					UploadData upload = (UploadData) row.get(col);
					if (upload == null) continue;

					byte[] bytes = upload.getBytes(); // <-- in-memory only
					String fileName = upload.getFileName();

					if (bytes != null && bytes.length > 0 && org.apache.commons.lang3.StringUtils.isNotBlank(fileName)) {
						// one-entry semantics: delete-and-recreate
						AttachmentUtil.addOrReplaceAttachmentEntry(po, fileName, bytes, col.getBtText() ,trxName);

						// free memory for this row after persisting
						upload.setBytes(null);
					}

					// If you want to support "delete attachment when user clears the file":
					// else if ((bytes == null || bytes.length == 0)) {
					//     MAttachment att = MAttachment.get(Env.getCtx(), po.get_Table_ID(), po.get_ID(), trxName);
					//     if (att != null) att.delete(true);
					// }
				}
			} else {
				log.warning("row isn't input full data so don't save to dao also delete dao saved");
				po.delete(true);
			}
		}

		applicationForm.setZZTotalNumberApplied(total + applicationForm.getZZTotalNumberApplied());
	}


	public static void setCellValue(AnnexureRow row, ColumnInfo<?> col, Object value) {
		log.info(String.format("Set cell value: row=%s, col=%s, col datatype=%s, value=%s", row, col.getTitle(), col.getDataType(), value));
		Object valueObj = row.get(col);
		if (col.getDataType() == DataType.Area) {
			AreaData areaData = (AreaData)valueObj;
			if (value == null || (int)value == 0) {
				areaData.setSelectedAreaInternal(null);
			}else {
				//for(MCity area : areaData.getDataProvider()) {
				for(MCity area : MasterUtil.getCities()) {// search on all cities, not only in data provider
					if (area.getC_City_ID() == (int)value) {
						@SuppressWarnings("unchecked")
						List<MCity> dataProvider = (List<MCity>)col.getDataProvider();
						dataProvider.add(area);
						areaData.setSelectedAreaInternal(area);
					}
				}
			}
		}else if (col.getDataType() == DataType.PositiveNumber) {
			IntData intData = (IntData)valueObj;
			intData.setValue(Util.convert((int)value));
		}else if (col.getDataType() == DataType.Text) {
			TextData textData = (TextData)valueObj;
			textData.setValue(Util.convertStr((String)value));
		}else if (col.getDataType() == DataType.Postal) {
			PostalData postalData = (PostalData)valueObj;
			postalData.setPostalInternal(Util.convertStr((String)value));
		}else if (col.getDataType() == DataType.Label) {
			LabelData valueData = (LabelData)valueObj;
			valueData.setValue(Util.convertStr((String)value));
		}else if (col.getDataType() == DataType.FileUpload) {
			UploadData valueData = (UploadData)valueObj;
			valueData.setFileName(Util.convertStr((String)value));
		}else if (col.getDataType() == DataType.LearnerInfo && valueObj != null && !(value instanceof LearnerInputInfo)) {
			// don't need, already set when init row tile
		}else if (col.getDataType() == DataType.List && StringUtils.isNotBlank(col.getBeanPropertyName())) {
			if (value == null || (int)value == 0) {
				row.put(col, null);
			}else {
				for(Object selectedObj : col.getDataProvider()) {
					Object obj = AnnexureInfo.getDaoValue(selectedObj, col.getBeanPropertyName());
					if (com.google.common.base.Objects.equal(value, obj)) {
						row.put(col, selectedObj);
						break;
					}
				}
			}
		}else if (col.getDataType() == DataType.Date) {
			DateData valueData = (DateData)valueObj;
			valueData.setLocalDate((Timestamp)value);
		}else {
			row.put(col, value);
		}
	}

	@SuppressWarnings("unchecked")
	/**
	 * return entry with value already convert (example 0 for non input int), value is true in case input non-null
	 */
	public static <T> Entry<T, Boolean> getCellValue(AnnexureRow row, ColumnInfo<?> col) {
		Object valueObj = row.get(col);

		if (col.getDataType() == DataType.Area) {
			AreaData areaData = (AreaData)valueObj;
			MCity area = areaData.getSelectedArea();
			if (area == null)
				return new AbstractMap.SimpleEntry<>((T)Integer.valueOf(0), Boolean.FALSE);
			else
				return new AbstractMap.SimpleEntry<>((T)Integer.valueOf(area.getC_City_ID()), Boolean.TRUE);

		}else if (col.getDataType() == DataType.PositiveNumber) {
			IntData intData = (IntData)valueObj;
			if (intData.getValue() == null) {
				return new AbstractMap.SimpleEntry<>((T)Integer.valueOf(0), Boolean.FALSE);
			}else {
				return new AbstractMap.SimpleEntry<>((T)intData.getValue(), Boolean.TRUE);
			}

		}else if (col.getDataType() == DataType.Text) {
			TextData textData = (TextData)valueObj;
			if (StringUtils.isBlank(textData.getValue())) {
				return new AbstractMap.SimpleEntry<>(null, Boolean.FALSE);
			}else {
				return new AbstractMap.SimpleEntry<>((T)Util.convertStr(textData.getValue()), Boolean.TRUE);
			}

		}else if (col.getDataType() == DataType.Postal) {
			PostalData postalData = (PostalData)valueObj;
			T value = (T)postalData.getPostal();
			return new AbstractMap.SimpleEntry<>(value, value == null?Boolean.FALSE:Boolean.TRUE);
		}else if (col.getDataType() == DataType.Label) {
			LabelData valueData = (LabelData)valueObj;
			T value = (T)valueData.getValue();
			return new AbstractMap.SimpleEntry<>(value, value == null?Boolean.FALSE:Boolean.TRUE);
		}else if (col.getDataType() == DataType.FileUpload) {
			UploadData valueData = (UploadData)valueObj;
			T value = (T)valueData.getFileName();
			return new AbstractMap.SimpleEntry<>(value, value == null?Boolean.FALSE:Boolean.TRUE);
		}else if (col.getDataType() == DataType.LearnerInfo) {
			LearnerInputInfo valueData = (LearnerInputInfo)valueObj;
			Integer value = Integer.valueOf(0);
			if(valueData != null) {
				value = Integer.valueOf(valueData.getLearnerInputID());
			}
			return new AbstractMap.SimpleEntry<>((T)value, value.equals(0) ? Boolean.FALSE:Boolean.TRUE);
		}	
		return new AbstractMap.SimpleEntry<>((T)valueObj, valueObj == null?Boolean.FALSE:Boolean.TRUE);
	}

	public void setDaoValue(Object dao, String propertyName, Object value) {
		try {
			PropertyUtils.setSimpleProperty(dao, propertyName, value);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AdempiereException(e.getMessage(), e);
		}
	}

	public static Object getDaoValue(Object dao, String propertyName) {
		try {
			return PropertyUtils.getSimpleProperty(dao, propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AdempiereException(e.getMessage(), e);
		}

	}

	/**
	 * 
	 * @param row
	 * @param dao
	 * @param keyColumns
	 * @return
	 */
	public boolean isMatchingRow(AnnexureRow row, Object dao, Collection<ColumnInfo<?>> keyColumns) {
		boolean isMatching = true;
		for (ColumnInfo<?> keyColumn : keyColumns) {
			if (StringUtils.isNotBlank(keyColumn.getDaoPropertyName())) {
				Entry<Object, Boolean> result = getCellValue(row, keyColumn);
				Object daoValue = getDaoValue(dao, keyColumn.getDaoPropertyName());
				isMatching = Objects.equals(result.getKey(), daoValue);
				if (!isMatching)
					break;
			}else {
				log.warning(String.format("DaoPropertyName of Key column is blank: %s", keyColumn.getTitle()));
			}
		}
		return isMatching;
	}

	/**
	 * check each column to get matching property of bean, query data of bean and set to row 
	 * @param cols
	 * @param row
	 * @param dao
	 */
	public void fillRowDataFromDao(List<ColumnInfo<?>> cols, AnnexureRow row, Object dao) {
		for (ColumnInfo<?> col : cols) {
			if (StringUtils.isNotBlank(col.getDaoPropertyName())){
				Object daoValue = null;
				if (col.getDataType() == DataType.FileUpload && StringUtils.isNotBlank(col.getDaoPropertyFileName())) {
					String attFileName = null;
					if (dao instanceof PO) {
						daoValue = AttachmentUtil.getFileNameFromAttachmentEntries(
								(PO) dao,
								col.getBtText(),
								((PO) dao).get_TrxName()
								);
					}
					//daoValue = getDaoValue(dao, col.getDaoPropertyFileName());
				}else if (col.getDataType() != DataType.FileUpload  && StringUtils.isNotBlank(col.getDaoPropertyName())) {
					daoValue = getDaoValue(dao, col.getDaoPropertyName());
				}

				AnnexureInfo.setCellValue(row, col, daoValue);
			}
		}
	}

	public void init(X_ZZ_Application_Form applicationForm) {
		init(applicationForm, null);
	}

	public void init(X_ZZ_Application_Form applicationForm, List<PO> savedDatas) {
		init(applicationForm, savedDatas, null);
	}

	public void init(X_ZZ_Application_Form applicationForm, List<PO> savedDatas, List<Map<ColumnInfo<?>, Object>> rowTitles) {
		if(rowTitles == null || rowTitles.size() == 0)
			init(applicationForm, savedDatas, null, null);
		else
			init(applicationForm, savedDatas, rowTitles, rowTitles.get(0).keySet());
	}

	public void init(X_ZZ_Application_Form applicationForm, 
			List<PO> savedDatas, List<Map<ColumnInfo<?>, Object>> rowTitles, Collection<ColumnInfo<?>> keyColumns) {
		// init rows with rowTitles
		if (rowTitles != null)
			for (Map<ColumnInfo<?>, Object> rowTitle : rowTitles) {
				AnnexureRow row = (AnnexureRow)createDetailRow(rowTitle);
			}

		// init rows with saved data
		if (savedDatas != null && savedDatas.size() > 0) {
			List<AnnexureRow> matchedRows = new ArrayList<>();

			boolean learnerInfoKey = false;
			if (keyColumns != null && keyColumns.size() > 0) {
				if (lookupColByDataType(DataType.LearnerInfo, keyColumns) != null) {
					learnerInfoKey = true;
				}
			}

			for (PO dao : savedDatas) {
				boolean isMatching = false;
				if (keyColumns != null && keyColumns.size() > 0) {
					for (AnnexureRow row : getRows()) {
						if (matchedRows.contains(row))
							continue;

						isMatching = isMatchingRow(row, dao, keyColumns);

						if (isMatching) {
							matchedRows.add(row);
							row.setData(dao);
							fillRowDataFromDao(getColumnInfos(), row, dao);
							break;
						}
					}
				}

				if (!isMatching && learnerInfoKey) {
					// moment don't handle this case, in case what to handle it need to change createDetailRow to create LearnerInputInfo for LearnerInfo column
				}else if(!isMatching) {
					AnnexureRow row = createDetailRow();
					row.setData(dao);
					fillRowDataFromDao(getColumnInfos(), row, dao);
				}
			}

		}

		if (getRows().size() == 0 && createNewRowWhenEmpty) {
			createDetailRow();
		}

		updateExpressionCol();
		updateTotalRow();
	}

	/**
	 * @return the poSupplier
	 */
	public BiFunction<AnnexureInfo, X_ZZ_Application_Form, PO> getPoSupplier() {
		return poSupplier;
	}

	/**
	 * @param poSupplier the poSupplier to set
	 */
	public void setPoSupplier(BiFunction<AnnexureInfo, X_ZZ_Application_Form, PO> poSupplier) {
		this.poSupplier = poSupplier;
	}

	/**
	 * @return the createNewRowWhenEmpty
	 */
	public boolean isCreateNewRowWhenEmpty() {
		return createNewRowWhenEmpty;
	}

	/**
	 * @param createNewRowWhenEmpty the createNewRowWhenEmpty to set
	 */
	public void setCreateNewRowWhenEmpty(boolean createNewRowWhenEmpty) {
		this.createNewRowWhenEmpty = createNewRowWhenEmpty;
	}


	public static AnnexureInfo getBankInfo(X_ZZ_Application_Form applicationForm) {
		/* List<ColumnInfo<?>> cols = new ArrayList<ColumnInfo<?>>();
		cols.add(ColumnInfo.getColText("Name of Bank", I_ZZBankingDetails.COLUMNNAME_BankName));
		cols.add(ColumnInfo.getColText("Branch Name", I_ZZBankingDetails.COLUMNNAME_ZZ_Branch_Name));
		cols.add(ColumnInfo.getColText("Branch Codes", I_ZZBankingDetails.COLUMNNAME_ZZ_Branch_Number));
		cols.add(ColumnInfo.getColText("Account Number", I_ZZBankingDetails.COLUMNNAME_AccountNo));
		*/
		
		
	    List<ColumnInfo<?>> cols = new ArrayList<>();
	    ColumnInfo<?> cBank   = ColumnInfo.getColText("Name of Bank",  I_ZZBankingDetails.COLUMNNAME_BankName).required();
	    ColumnInfo<?> cBranch = ColumnInfo.getColText("Branch Name",   I_ZZBankingDetails.COLUMNNAME_ZZ_Branch_Name).required();
	    ColumnInfo<?> cCode   = ColumnInfo.getColText("Branch Codes",  I_ZZBankingDetails.COLUMNNAME_ZZ_Branch_Number).required();
	    ColumnInfo<?> cAcct   = ColumnInfo.getColText("Account Number",I_ZZBankingDetails.COLUMNNAME_AccountNo).required();

	    cols.add(cBank); cols.add(cBranch); cols.add(cCode); cols.add(cAcct);

		AnnexureInfo bankInfo = AnnexureInfo.getAnnexureInfo(AnnexureInfo.class, cols, false);
		bankInfo.setSubSectionHeader("TRAINING PROVIDER BANKING DETAILS");
		bankInfo.setPoSupplier((ann, appForm) -> {
			X_ZZBankingDetails po = new X_ZZBankingDetails(appForm.getCtx(), 0, null);
			po.setZZ_Application_Form_ID(appForm.getZZ_Application_Form_ID());
			return po;
		});


		List<PO> savedDaos = null;
		if(applicationForm != null) {
			String where = String.format("%s = ?", 
					I_ZZBankingDetails.COLUMNNAME_ZZ_Application_Form_ID);

			Query querySavedDaos = MTable.get(I_ZZBankingDetails.Table_ID).createQuery(where, null);
			savedDaos = querySavedDaos.setParameters(applicationForm.getZZ_Application_Form_ID()).list();
		}

		bankInfo.init(applicationForm, savedDaos);

		return bankInfo;
	}
	
	public boolean areMandatoryFieldsFilled() {
	    if (rows == null || rows.isEmpty()) return false; // bank form needs one row
	    for (AnnexureRow r : rows) {
	        for (ColumnInfo<?> col : columnInfos) {
	            if (!col.isMandatory()) continue;
	            var present = AnnexureInfo.getCellValue(r, col).getValue();
	            if (!Boolean.TRUE.equals(present)) return false;
	            //Object v = r.get(col);
	          //  if (v == null) return false;
	          //  if (v instanceof CharSequence && ((CharSequence) v).toString().trim().isEmpty()) return false;
	        }
	    }
	    return true;
	}


}
