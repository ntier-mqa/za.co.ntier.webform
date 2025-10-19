package za.co.ntier.webform.sdr.component.bean.cell;

import java.sql.Timestamp;
import java.time.LocalDate;

import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;

public class DateData extends AbstractCellModel {
	public DateData(TableModel annexure, RowModel row) {
		super(annexure, row);
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
}
