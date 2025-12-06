package za.co.ntier.webform.form.bean.component;

public class TextListData extends TextData {
	
	private boolean isList = false;

	@Override
	public int getLines() {
		return super.getLines();
	}

	/**
	 * @return the isList
	 */
	public boolean isList() {
		return isList;
	}

	/**
	 * @param isList the isList to set
	 */
	public void setList(boolean isList) {
		this.isList = isList;
	}
}
