package za.co.ntier.webform.sdr.component.bean;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.Converter;
import org.zkoss.zk.ui.Component;

public class ConvertBlankNull implements Converter<String, String, Component>, Serializable{

	private static final long serialVersionUID = 1679248360456777322L;

	/**
	 * input space mean nothing so return null
	 */
	@Override
	public String coerceToUi(String beanProp, Component component, BindContext ctx) {
		if (StringUtils.isBlank(beanProp))
			return null;
		return beanProp;
	}

	/**
	 * input space mean nothing so return null
	 */
	@Override
	public String coerceToBean(String compAttr, Component component, BindContext ctx) {
		if (StringUtils.isBlank(compAttr))
			return null;
		return compAttr;
	}

}
