package za.co.ntier.webform.sdr.component.bean.cell;

import java.sql.Timestamp;
import java.time.LocalDate;

import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.CellModel.CellModelInfo;
import za.co.ntier.webform.sdr.component.bean.CellModel.CellModelParams;

public class DateCellModel extends CellModel {
	public DateCellModel(TableModel annexure, RowModel row, ColumnModel colModel) {
		super(annexure, row, colModel);
		setCellType(DATE_CELL);
	}

	private LocalDate localDate;

	/**
	 * @return the localDate
	 */
	public LocalDate getLocalDate() {
		return localDate;
	}

	/**
	 * @param localDate the localDate to set
	 */
	public void setLocalDate(LocalDate localDate) {
		this.localDate = localDate;
	}

	public Timestamp getTimestamp() {
		if(localDate == null) {
			return null;
		}else {
			return Timestamp.valueOf(localDate.atStartOfDay());
		}

	}

	public void setLocalDate(Timestamp timestamp) {
		if(timestamp == null) {
			this.localDate = null;
		}else {
			this.localDate = timestamp.toLocalDateTime().toLocalDate();
		}
	}


	public static ColumnModel getDateColumnModel(String title, String daoPropertyName) {
		return CellModel.getColModelForCell(CellModelInfo.of(ColumnModel.class, DateCellModel.class, null), 
				CellModelParams.of(title, daoPropertyName, null)
				);
	}
}
