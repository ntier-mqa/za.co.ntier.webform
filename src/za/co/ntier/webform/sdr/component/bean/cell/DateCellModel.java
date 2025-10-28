package za.co.ntier.webform.sdr.component.bean.cell;

import java.sql.Timestamp;
import java.time.LocalDate;

import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;

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
		super.setValue(getTimestamp());
	}

	private Timestamp getTimestamp() {
		if(localDate == null) {
			return null;
		}else {
			return Timestamp.valueOf(localDate.atStartOfDay());
		}

	}
	@Override
	public void setValue(Object timestampObj) {
		
		if(timestampObj == null) {
			this.localDate = null;
		}else {
			Timestamp timeValue = Timestamp.class.cast(timestampObj);
			this.localDate = timeValue.toLocalDateTime().toLocalDate();
		}
		
		super.setValue(timestampObj);
	}


	public static ColumnModel getDateColumnModel(String title, String daoPropertyName) {
		return CellModel.getColModelForCell(CellModelInfo.of(ColumnModel.class, DateCellModel.class, null), 
				CellModelParams.of(title, daoPropertyName, null)
				);
	}
}
