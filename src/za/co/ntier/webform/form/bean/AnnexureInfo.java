package za.co.ntier.webform.form.bean;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.UploadEvent;

import za.co.ntier.webform.form.MasterUtil;

public class AnnexureInfo {
	public static Map<ColumnInfo<?>, Object> createDetailRow(List<ColumnInfo<?>> columnInfos) {
		return AnnexureInfo.createDetailRow(columnInfos, null);
	}

	public static Map<ColumnInfo<?>, Object> createDetailRow(List<ColumnInfo<?>> columnInfos,
			Map<ColumnInfo<?>, Object> rowDataInits) {
		Map<ColumnInfo<?>, Object> newRow = new HashMap<>();

		for (ColumnInfo<?> columnInfo : columnInfos) {
			Object cellData = null;
			if (rowDataInits != null) {
				cellData = rowDataInits.get(columnInfo);
			}
			if (cellData == null) {

				if (columnInfo.getDataType() == DataType.TwoTitles) {
					cellData = Arrays.asList(null, null);

				} else if (columnInfo.getDataType() == DataType.TwoValues) {
					cellData = Arrays.asList(null, null);

				} else if (columnInfo.getDataType() == DataType.FileUpload) {
					cellData = new UploadInfo();

				}
			}
			newRow.put(columnInfo, cellData);

		}

		return newRow;
	}

	public static <T extends AnnexureInfo> T getAnnexureInfo(Class<T> clazz, List<ColumnInfo<?>> columnInfos,
			boolean isShowTotal) throws NoSuchMethodException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {

		T annexureInfo = clazz.getDeclaredConstructor().newInstance();
		annexureInfo.setShowTotal(isShowTotal);
		annexureInfo.setColumnInfos(columnInfos);

		List<Map<ColumnInfo<?>, Object>> rows = new ArrayList<>();
		annexureInfo.setRows(rows);

		if (isShowTotal) {
			Map<ColumnInfo<?>, Object> totalRow = new HashMap<>();
			for (ColumnInfo<?> columnInfo : columnInfos) {
				if (columnInfo.getDataType() == DataType.Label) {
					if (columnInfos.indexOf(columnInfo) == 0) {
						totalRow.put(columnInfos.get(0), "Total");
					}
				} else if (columnInfo.getDataType() == DataType.PositiveNumber) {
					totalRow.put(columnInfo, 0);
				} else {
					totalRow.put(columnInfo, null);
				}
			}

			annexureInfo.setTotalRow(totalRow);
		}
		return annexureInfo;
	}

	/**
	 * for CetTvet sub, show total but not row header
	 *
	 * @param sectionHeader
	 * @param columnInfos
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws NoSuchMethodException
	 */
	public static AnnexureInfo getAnnexureInfoOneLine(String sectionHeader, List<ColumnInfo<?>> columnInfos)
			throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		return AnnexureInfo.getAnnexureInfoOneLine(sectionHeader, columnInfos, null, true, null);
	}

	/**
	 * for CetTvet master, show row title but not total
	 *
	 * @param sectionHeader
	 * @param columnInfos
	 * @param rowTitle
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws NoSuchMethodException
	 */
	public static AnnexureInfo getAnnexureInfoOneLine(String sectionHeader, List<ColumnInfo<?>> columnInfos,
			String rowTitle) throws NoSuchMethodException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		return AnnexureInfo.getAnnexureInfoOneLine(sectionHeader, columnInfos, rowTitle, false, null);
	}

	public static AnnexureInfo getAnnexureInfoOneLine(String sectionHeader, List<ColumnInfo<?>> columnInfos,
			String rowTitle, boolean isShowTotal, List<String> twoTitleValue) throws NoSuchMethodException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		AnnexureInfo annexureInfo = AnnexureInfo.getAnnexureInfo(AnnexureInfo.class, columnInfos, isShowTotal);

		Map<ColumnInfo<?>, Object> rowDataInits = new HashMap<>();
		for (ColumnInfo<?> colInfo : columnInfos) {
			if (colInfo.getDataType() == DataType.Label && rowTitle != null) {
				rowDataInits.put(colInfo, rowTitle);
			} else if (colInfo.getDataType() == DataType.TwoTitles && twoTitleValue != null) {
				rowDataInits.put(colInfo, twoTitleValue);
			}
		}

		Map<ColumnInfo<?>, Object> fistRow = AnnexureInfo.createDetailRow(columnInfos, rowDataInits);
		annexureInfo.getRows().add(fistRow);
		annexureInfo.setSectionHeader(sectionHeader);
		return annexureInfo;
	}

	public static AnnexureInfo getAnnexureInfoOneLine(String sectionHeader, List<ColumnInfo<?>> columnInfos,
			String rowTitle, List<String> twoTitleValue) throws NoSuchMethodException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return getAnnexureInfoOneLine(sectionHeader, columnInfos, rowTitle, false, twoTitleValue);
	}

	private List<ColumnInfo<?>> columnInfos;
	private List<Map<ColumnInfo<?>, Object>> rows;

	private String sectionHeader;
	private boolean showTotal = false;

	private AnnexureInfo subAnnexure;

	private Map<ColumnInfo<?>, Object> totalRow;

	public void addRow() {
		Map<ColumnInfo<?>, Object> row = createDetailRow(getColumnInfos());
		getRows().add(row);
		BindUtils.postNotifyChange(this, "rows");
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
	public List<Map<ColumnInfo<?>, Object>> getRows() {
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
	 * @return the totalRow
	 */
	public Map<ColumnInfo<?>, Object> getTotalRow() {
		return totalRow;
	}

	/**
	 * @return the showTotal
	 */
	public boolean isShowTotal() {
		return showTotal;
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
	public void setRows(List<Map<ColumnInfo<?>, Object>> rows) {
		this.rows = rows;
	}

	/**
	 * @param sectionHeader the sectionHeader to set
	 */
	public void setSectionHeader(String sectionHeader) {
		this.sectionHeader = sectionHeader;
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
	 * @param totalRow the totalRow to set
	 */
	public void setTotalRow(Map<ColumnInfo<?>, Object> totalRow) {
		this.totalRow = totalRow;
	}

	public void uploadFile(Map<ColumnInfo<?>, Object> row, ColumnInfo<?> col, UploadEvent event) throws IOException {
		UploadInfo uploadInfoObj = (UploadInfo) row.get(col);
		uploadInfoObj.setFileName(event.getMedia().getName());
		uploadInfoObj.setFullPath(MasterUtil.saveUploadFile(event.getMedia()));
		
		BindUtils.postNotifyChange(row.get(col), "fileName");
	}

	public void valueNumChange(ColumnInfo<?> detailCol) {
		int total = 0;
		for (Map<ColumnInfo<?>, Object> row : rows) {
			if (row.get(detailCol) != null) {
				total += (int) row.get(detailCol);
			}
		}
		totalRow.put(detailCol, total);
		BindUtils.postNotifyChange(this, "*");
	}

	public void numChange(Map<ColumnInfo<?>, Object> row, ColumnInfo<?> col, InputEvent event) {
		if (col.getDataType() == DataType.PositiveNumber) {
			Integer total = 0;
			for (Map<ColumnInfo<?>, Object> r : getRows()) {
				if (r.get(col) != null) {
					total += (int) r.get(col);
				}
			}
			
			totalRow.put(col, total);
			BindUtils.postNotifyChange(this, "totalRow");
		}
		
	}

}
