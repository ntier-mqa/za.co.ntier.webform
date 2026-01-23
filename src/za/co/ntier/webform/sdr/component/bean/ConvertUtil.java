package za.co.ntier.webform.sdr.component.bean;

import java.io.Serializable;

import org.zkoss.bind.BindContext;
import org.zkoss.bind.Converter;
import org.zkoss.zk.ui.Component;

public class ConvertUtil {
	private static ConvertBlankNull convertBlankNull = new ConvertBlankNull();
	private static ConvertZeroNull convertZeroNull = new ConvertZeroNull();
	private static ConvertLongText convertLongText = new ConvertLongText();
	
	public static ConvertLongText getConvertLongText() {
		return convertLongText;
	}
	
	public static ConvertBlankNull getConvertBlankNull() {
		return convertBlankNull;
	}

	public static ConvertZeroNull getConvertZeroNull() {
		return convertZeroNull;
	}

	public static class ConvertLongText implements Converter<Long, String, Component>, Serializable{

		private static final long serialVersionUID = -8503090974382971867L;

		@Override
		public Long coerceToUi(String beanProp, Component component, BindContext ctx) {
			if (beanProp == null)
				return null;
			
			return Long.valueOf(beanProp);
		}

		@Override
		public String coerceToBean(Long compAttr, Component component, BindContext ctx) {
			if (compAttr == null)
				return null;
			return compAttr.toString().trim();
		}

	}
}
