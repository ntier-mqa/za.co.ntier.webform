package za.co.ntier.webform.sdr.component.bean;

import java.io.Serializable;

import org.zkoss.bind.BindContext;
import org.zkoss.bind.Converter;
import org.zkoss.zk.ui.Component;

public class ConvertZeroNull implements Converter<Integer, Integer, Component>, Serializable {

	private static final long serialVersionUID = -2524587022727906879L;

	/**
	 * zero mean input nothing so return null
	 */
	@Override
	public Integer coerceToUi(Integer beanProp, Component component, BindContext ctx) {
		if (beanProp != null && beanProp.equals(0))
			return null;
		
		return beanProp;
	}

	/**
	 * when input 0 it mean no input so return null 
	 */
	@Override
	public Integer coerceToBean(Integer compAttr, Component component, BindContext ctx) {
		if (compAttr != null && compAttr.equals(0))
			return null;
		
		return compAttr;
	}

}
