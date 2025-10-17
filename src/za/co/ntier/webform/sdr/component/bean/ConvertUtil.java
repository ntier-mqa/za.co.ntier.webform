package za.co.ntier.webform.sdr.component.bean;

public class ConvertUtil {
	private static ConvertBlankNull convertBlankNull = new ConvertBlankNull();
	private static ConvertZeroNull convertZeroNull = new ConvertZeroNull();
	public static ConvertBlankNull getConvertBlankNull() {
		return convertBlankNull;
	}
	
	public static ConvertZeroNull getConvertZeroNull() {
		return convertZeroNull;
	}
	
}
