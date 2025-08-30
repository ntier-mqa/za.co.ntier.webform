package za.co.ntier.webform.form.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.adempiere.exceptions.AdempiereException;
import org.zkoss.bind.BindUtils;

public class AnnexureInfo {
	private String header;
	private List<ColumnInfo<?>> columnInfos;
	private List<Map<ColumnInfo<?>, Object>> rows;
	private boolean showTotal = false;
	private Map<ColumnInfo<?>, Object> totalRow;
	
	/**
	 * @return the columnInfos
	 */
	public List<ColumnInfo<?>> getColumnInfos() {
		return columnInfos;
	}
	/**
	 * @param columnInfos the columnInfos to set
	 */
	public void setColumnInfos(List<ColumnInfo<?>> columnInfos) {
		this.columnInfos = columnInfos;
	}
	/**
	 * @return the rows
	 */
	public List<Map<ColumnInfo<?>, Object>> getRows() {
		return rows;
	}
	/**
	 * @param rows the rows to set
	 */
	public void setRows(List<Map<ColumnInfo<?>, Object>> rows) {
		this.rows = rows;
	}
	

	/**
	 * @return the subAnnexure
	 */
	public AnnexureInfo getSubAnnexure() {
		return subAnnexure;
	}
	/**
	 * @param subAnnexure the subAnnexure to set
	 */
	public void setSubAnnexure(AnnexureInfo subAnnexure) {
		this.subAnnexure = subAnnexure;
	}


	/**
	 * @return the header
	 */
	public String getHeader() {
		return header;
	}
	/**
	 * @param header the header to set
	 */
	public void setHeader(String header) {
		this.header = header;
	}


	private AnnexureInfo subAnnexure;
	
	/**
	 * for CetTvet sub, show total but not row header
	 * @param header
	 * @param columnInfos
	 * @return
	 */
	public static AnnexureInfo getAnnexureInfoOneLine(String header, List<ColumnInfo<?>> columnInfos) {
		return AnnexureInfo.getAnnexureInfoOneLine(header, columnInfos, null, true, null);
	}
	
	/**
	 * for CetTvet master, show row title but not total
	 * @param header
	 * @param columnInfos
	 * @param rowTitle
	 * @return
	 */
	public static AnnexureInfo getAnnexureInfoOneLine(String header, List<ColumnInfo<?>> columnInfos, String rowTitle) {
		return AnnexureInfo.getAnnexureInfoOneLine(header, columnInfos, rowTitle, false, null);
	}
	
	public static Map<ColumnInfo<?>, Object> createDetailRow(List<ColumnInfo<?>> columnInfos) {
		return AnnexureInfo.createDetailRow(columnInfos, null, null);
	}
	
	public static Map<ColumnInfo<?>, Object> createDetailRow(List<ColumnInfo<?>> columnInfos, String rowTitle, List<String> twoTitleValue) {
		Map<ColumnInfo<?>, Object> newRow = new HashMap<>();
		
		for (ColumnInfo<?> columnInfo : columnInfos) {
			if (columnInfo.getDataType() == DataType.Label && rowTitle != null) {
				newRow.put(columnInfo, rowTitle);
			}else if (columnInfo.getDataType() == DataType.TwoTitles) {
				if (twoTitleValue == null) {
					twoTitleValue = Arrays.asList(null, null);
				}
				newRow.put(columnInfo, twoTitleValue);
			}else if (columnInfo.getDataType() == DataType.TwoValues) {
				List<Integer> values = Arrays.asList(null, null);
				newRow.put(columnInfo, values);
			}else {
				newRow.put(columnInfo, null);
			}
		}
		
		return newRow;
	}
	
	public static AnnexureInfo getAnnexureInfoOneLine(String header, List<ColumnInfo<?>> columnInfos, String rowTitle, 
			List<String> twoTitleValue) {
		return getAnnexureInfoOneLine(header, columnInfos, rowTitle, false, twoTitleValue);
	}
	
	public static AnnexureInfo getAnnexureInfoOneLine(String header, List<ColumnInfo<?>> columnInfos, String rowTitle, boolean isShowTotal, List<String> twoTitleValue) {
		AnnexureInfo annexureInfo = new AnnexureInfo();
		annexureInfo.setShowTotal(isShowTotal);
		annexureInfo.setHeader(header);
		annexureInfo.setColumnInfos(columnInfos);
		
		List<Map<ColumnInfo<?>, Object>> rows = new ArrayList<>();
		annexureInfo.setRows(rows);
		
		Map<ColumnInfo<?>, Object> fistRow = AnnexureInfo.createDetailRow(columnInfos, rowTitle, twoTitleValue);
		rows.add(fistRow);
		
		if (isShowTotal) {
			Map<ColumnInfo<?>, Object> totalRow = new HashMap<>();
			for (ColumnInfo<?> columnInfo : columnInfos) {
				if (columnInfo.getDataType() == DataType.Label) {
					if (columnInfos.indexOf(columnInfo) != 0) {
						throw new AdempiereException("Moment row title need to ad first column");
					}
					totalRow.put(columnInfos.get(0), "Total");
				}else if (columnInfo.getDataType() == DataType.PositiveNumber) {
					totalRow.put(columnInfo, 0);
				}else {
					totalRow.put(columnInfo, null);
				}
			}
			
			annexureInfo.setTotalRow(totalRow);
		}
		
		
		
		
		return annexureInfo;
	}
	/**
	 * @return the showTotal
	 */
	public boolean isShowTotal() {
		return showTotal;
	}
	/**
	 * @param showTotal the showTotal to set
	 */
	public void setShowTotal(boolean showTotal) {
		this.showTotal = showTotal;
	}
	/**
	 * @return the totalRow
	 */
	public Map<ColumnInfo<?>, Object> getTotalRow() {
		return totalRow;
	}
	/**
	 * @param totalRow the totalRow to set
	 */
	public void setTotalRow(Map<ColumnInfo<?>, Object> totalRow) {
		this.totalRow = totalRow;
	}
	
	public void valueNumChange(ColumnInfo<?> detailCol) {
		int total = 0;
		for (Map<ColumnInfo<?>, Object> row : rows) {
			if (row.get(detailCol) != null)
				total += (int)row.get(detailCol);
		}
		totalRow.put(detailCol, total);
		BindUtils.postNotifyChange(this, "totalRow");
	}
	
	public void addRow(){
		createDetailRow(getColumnInfos());
		BindUtils.postNotifyChange(this, "rows");
	}
}
