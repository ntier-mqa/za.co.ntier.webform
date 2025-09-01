package za.co.ntier.webform.form.bean;

import java.lang.reflect.InvocationTargetException;

import za.co.ntier.webform.form.MenuContextInfo;

public class UploadDocComponent {
	private UploadInput uploadDoc;
	public UploadDocComponent(MenuContextInfo menuContextInfo) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
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
	
}
