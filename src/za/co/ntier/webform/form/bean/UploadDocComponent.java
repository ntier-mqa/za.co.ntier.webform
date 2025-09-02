package za.co.ntier.webform.form.bean;

import java.lang.reflect.InvocationTargetException;

import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class UploadDocComponent implements ISaveForm {
	private UploadInput uploadDoc;

	public UploadDocComponent(MenuContextInfo menuContextInfo) throws NoSuchMethodException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		setUploadDoc(UploadInput.getUploadInput(menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID()));
	}

	/**
	 * @return the uploadDoc
	 */
	public UploadInput getUploadDoc() {
		return uploadDoc;
	}

	/**
	 * @param uploadDoc the uploadDoc to set
	 */
	public void setUploadDoc(UploadInput uploadDoc) {
		this.uploadDoc = uploadDoc;
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		// TODO Auto-generated method stub
		
	}

}
