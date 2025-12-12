package za.co.ntier.webform.form.bean.component;

import java.sql.Timestamp;
import java.time.LocalDate;

public class DateData implements ICellData{
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

	@Override
	public void clearData() {
		localDate = null;
		
	}
}
