package za.co.ntier.webform.sdr.component.bean.cell;

import java.sql.Timestamp;
import java.time.LocalDate;

import za.co.ntier.webform.sdr.component.bean.BaseCellModel;
import za.co.ntier.webform.sdr.component.bean.BaseColumnModel;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;

public class DateCellModel extends BaseCellModel {
	public DateCellModel(TableModel annexure, RowModel row, BaseColumnModel colModel) {
		super(annexure, row, colModel, DATE_CELL);
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


	public static BaseColumnModel getDateColumnModel(String title, String daoPropertyName) {
		return BaseCellModel.getColModel(DateCellModel.class, title, daoPropertyName);
	}
}
