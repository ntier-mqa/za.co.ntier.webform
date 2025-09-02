package za.co.ntier.webform.form;

import org.zkoss.xel.VariableResolver;
import org.zkoss.xel.XelException;

import za.co.ntier.webform.form.viewmodel.component.AnnexureTableVMWrapper;

public class ViewModelResolver implements VariableResolver{
	
	@Override
	public Object resolveVariable(String name) throws XelException {
		if("vmAnnexureTableVMWrapper".equals(name)) {
			return new AnnexureTableVMWrapper();
		}
		return null;
		
	}
	

}
