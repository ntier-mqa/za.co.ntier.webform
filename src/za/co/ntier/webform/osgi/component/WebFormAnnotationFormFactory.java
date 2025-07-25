package za.co.ntier.webform.osgi.component;

import org.adempiere.webui.factory.AnnotationBasedFormFactory;
import org.adempiere.webui.factory.IFormFactory;
import org.osgi.service.component.annotations.Component;

@Component(immediate = true, service = IFormFactory.class, property = { "service.ranking:Integer=1" })
public class WebFormAnnotationFormFactory extends AnnotationBasedFormFactory {
	@Override
	protected String[] getPackages() {
		return new String[] { "za.co.ntier.webform.form" };
	}
}
