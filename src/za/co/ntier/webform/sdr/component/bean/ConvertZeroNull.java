package za.co.ntier.webform.sdr.component.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import org.zkoss.bind.BindContext;
import org.zkoss.bind.Converter;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Intbox;

public class ConvertZeroNull implements Converter<Object, Object, Component>, Serializable {

	private static final long serialVersionUID = -2524587022727906879L;

	/**
	 * zero mean input nothing so return null
	 */
	@Override
	public Object coerceToUi(Object beanProp, Component component, BindContext ctx) {
		if (beanProp != null) {
			if (beanProp instanceof Number && beanProp.equals(0)) {
				return null;
			}else if (beanProp instanceof BigDecimal && BigDecimal.ZERO.equals(beanProp)) {
				return null;
			}
		}

		return beanProp;
	}

	/**
	 * when input 0 it mean no input so return null
	 */
	@Override
	public Object coerceToBean(Object compAttr, Component component, BindContext ctx) {
		if (compAttr != null) {
			if (compAttr instanceof Number && compAttr.equals(0)) {
				return null;
			}else if (compAttr instanceof BigDecimal && BigDecimal.ZERO.equals(compAttr)) {
				return null;
			}
			
			final CellModel cellMode = (CellModel) ctx.getConverterArg("cellMode");
			if (component instanceof Intbox && cellMode != null) {
				
			}
		}
			

		return compAttr;
	}

}
