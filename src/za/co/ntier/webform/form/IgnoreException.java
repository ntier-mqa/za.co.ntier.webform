package za.co.ntier.webform.form;

import org.adempiere.webui.exception.ApplicationException;

public class IgnoreException extends ApplicationException{

	private static final long serialVersionUID = -6256410075426409574L;
	
	public IgnoreException(String msg) {
		super(msg);
		
	}

	public IgnoreException(String msg, Exception ex) {
		super(msg, ex);
		
	}


}
