package za.co.ntier.webform.form.bean;

import java.sql.Timestamp;
import java.time.LocalDate;

public class EmployerDeclarationInfo {
	private String userName;
	private LocalDate localDate;

	public Timestamp getDate() {
		return Timestamp.valueOf(localDate.atStartOfDay());
	}

	public LocalDate getLocalDate() {
		return localDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setLocalDate(LocalDate localDate) {
		this.localDate = localDate;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
